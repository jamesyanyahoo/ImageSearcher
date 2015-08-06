package com.yahoo.shopping.imagesearcher.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import com.etsy.android.grid.StaggeredGridView;
import com.yahoo.shopping.imagesearcher.R;
import com.yahoo.shopping.imagesearcher.adapters.SearchResultAdapter;
import com.yahoo.shopping.imagesearcher.asynctasks.ImageSearchAyncTask;
import com.yahoo.shopping.imagesearcher.fragments.SearchConfigDialogFragment;
import com.yahoo.shopping.imagesearcher.interfaces.OnApplySettings;
import com.yahoo.shopping.imagesearcher.interfaces.OnSearchFinished;
import com.yahoo.shopping.imagesearcher.models.FilterConfig;
import com.yahoo.shopping.imagesearcher.models.Image;
import com.yahoo.shopping.imagesearcher.models.SearchResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class SearchListActivity extends AppCompatActivity
        implements OnSearchFinished, OnApplySettings, AbsListView.OnScrollListener {

    private FilterConfig mConfig = new FilterConfig();
    private SearchResult mSearchResult;
    private SearchResultAdapter mSearchResultAdapter;
    private String mKeyword = "";

    private boolean shouldLoadMore = false;

    private String buildSearchUrl(String keyword, int pageIndex) {
        String sizeParam = "";
        switch (mConfig.getSizeFilter()) {
            case NotSpecified:
                break;
            case Small:
                sizeParam = "imgsz=small";
                break;
            case Medium:
                sizeParam = "imgsz=medium";
                break;
            case Large:
                sizeParam = "imgsz=large";
                break;
            case ExtraLarge:
                sizeParam = "imgsz=xlarge";
                break;
        }

        String colorParam = "";
        switch (mConfig.getColorFilter()) {
            case NotSpecified:
                break;
            case Black:
                colorParam = "imgcolor=black";
                break;
            case Blue:
                colorParam = "imgcolor=blue";
                break;
            case Brown:
                colorParam = "imgcolor=brown";
                break;
            case Gray:
                colorParam = "imgcolor=gray";
                break;
            case Green:
                colorParam = "imgcolor=green";
                break;
        }

        String typeParam = "";
        switch(mConfig.getTypeFilter()) {
            case NotSpecified:
                break;
            case Face:
                typeParam = "imgtype=face";
                break;
            case Photo:
                typeParam = "imgtype=photo";
                break;
            case ClipArt:
                typeParam = "imgtype=clipart";
                break;
            case LineArt:
                typeParam = "imgtype=lineart";
                break;
        }

        StringBuffer url = new StringBuffer("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8");
        try {
            url.append(String.format("&q=%s", URLEncoder.encode(keyword, "utf-8")));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        if (!sizeParam.isEmpty()) {
            url.append(String.format("&%s", sizeParam));
        }
        if (!colorParam.isEmpty()) {
            url.append(String.format("&%s", colorParam));
        }
        if (!typeParam.isEmpty()) {
            url.append(String.format("&%s", typeParam));
        }

        url.append(String.format("&start=%d", pageIndex * 8));

        return url.toString();
    }

    private void refreshSearch() {
        if (!mKeyword.isEmpty()) {
            findViewById(R.id.activity_main_no_result).setVisibility(View.INVISIBLE);

            String searchUrl = buildSearchUrl(mKeyword, 0);
            if (searchUrl != null) {
                new ImageSearchAyncTask(searchUrl, this).execute();
            }
        }
    }

    private void loadMoreData() {
        if (mSearchResult != null) {
            String searchUrl = buildSearchUrl(mKeyword, mSearchResult.getCurrentPageIndex() + 1);
            if (searchUrl != null) {
                new ImageSearchAyncTask(searchUrl, this).execute(); // should test whether the load more is empty
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchResultAdapter = new SearchResultAdapter(this, 0, new ArrayList<Image>());

        StaggeredGridView gridView = (StaggeredGridView) findViewById(R.id.activity_main_grid_view);
        gridView.setAdapter(mSearchResultAdapter);
        gridView.setOnScrollListener(this);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.ic_launcher);
        menu.setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchListActivity.this.mKeyword = query;
                refreshSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SearchConfigDialogFragment dialog = SearchConfigDialogFragment.newInstance("Settings");
            dialog.show(fragmentManager, "fragment_dialog_config");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchFinished(SearchResult searchResult) {
        mSearchResult = searchResult;

        if (mSearchResult != null) {
            if (mSearchResult.getCurrentPageIndex() == 0) {
                mSearchResultAdapter.clear();
            }
            mSearchResultAdapter.addAll(mSearchResult.getImages());
            mSearchResultAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onApplySettings(FilterConfig config) {
        mConfig = config;

        refreshSearch();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (shouldLoadMore) {
                loadMoreData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            shouldLoadMore = true;
        }
    }
}
