package com.yahoo.shopping.imagesearcher;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnSearchFinished {

    private ImageSearchResultAdapter mSearchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchResultAdapter = new ImageSearchResultAdapter(this, 0, new ArrayList<Image>());

        StaggeredGridView gridView = (StaggeredGridView) findViewById(R.id.activity_main_grid_view);
        gridView.setAdapter(mSearchResultAdapter);

        new ImageSearchAyncTask(this).execute();
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
                Log.i("xxxxxxx", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("xxxxxxx", newText);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fm = getSupportFragmentManager();
            SearchConfigDIalog searchConfigDIalog = SearchConfigDIalog.newInstance("Some Title");
            searchConfigDIalog.show(fm, "fragment_edit_name");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchFinished(ImageSearchResult searchResult) {
        mSearchResultAdapter.clear();
        mSearchResultAdapter.addAll(searchResult.getImages());
        mSearchResultAdapter.notifyDataSetChanged();
    }
}
