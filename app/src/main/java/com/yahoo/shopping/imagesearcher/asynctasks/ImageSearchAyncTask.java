package com.yahoo.shopping.imagesearcher.asynctasks;

import android.os.AsyncTask;

import com.yahoo.shopping.imagesearcher.interfaces.OnSearchFinished;
import com.yahoo.shopping.imagesearcher.models.Image;
import com.yahoo.shopping.imagesearcher.models.SearchResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jamesyan on 8/4/15.
 */
public class ImageSearchAyncTask extends AsyncTask<Void, Void, SearchResult> {
    private OnSearchFinished mObserver;
    private String mUrl;

    public ImageSearchAyncTask(String url, OnSearchFinished observer) {
        this.mObserver = observer;
        this.mUrl = url;
    }

    private String fetchContent() throws IOException {
        URL url = null;
        StringBuilder jsonString = new StringBuilder();

        url = new URL(mUrl);
        URLConnection urlConnection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        String line;
        do {
            line = reader.readLine();

            if (line != null) {
                jsonString.append(line);
            }
        } while (line != null);

        return jsonString.toString();
    }

    private SearchResult convertToImageModelList(String jsonString) throws JSONException {
        JSONObject root = new JSONObject(jsonString);
        if (root.isNull("responseData")) {
            // handle null case
            return null;
        }

        // handle cursor data
        JSONObject data = root.getJSONObject("responseData");
        if (data.isNull("cursor")) {
            return null;
        }

        SearchResult searchResult = new SearchResult();

        JSONObject cursor = data.getJSONObject("cursor");
        searchResult.setCount(cursor.getInt("estimatedResultCount"));
        searchResult.setMoreResultUrl(cursor.getString("moreResultsUrl"));
        searchResult.setCurrentPageIndex(cursor.getInt("currentPageIndex"));

        // handle list data
        if (data.isNull("results")) {
            return null;
        }
        JSONArray result = data.getJSONArray("results");
        for (int i = 0; i < result.length(); i++) {
            JSONObject imageResult = result.getJSONObject(i);

            Image image = new Image();
            image.setTitle(imageResult.getString("title"));
            image.setUrl(imageResult.getString("url"));
            image.setContent(imageResult.getString("content"));
            image.setWidth(imageResult.getInt("width"));
            image.setHeight(imageResult.getInt("height"));

            searchResult.addImage(image);
        }

        return searchResult;
    }


    @Override
    protected SearchResult doInBackground(Void... params) {
        try {
            String jsonString = fetchContent();

            return convertToImageModelList(jsonString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(SearchResult searchResult) {
        mObserver.onSearchFinished(searchResult);
    }
}
