package com.yahoo.shopping.imagesearcher;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesyan on 8/4/15.
 */
public class ImageSearchAyncTask extends AsyncTask<Void, Void, ImageSearchResult> {
    OnSearchFinished mObserver;

    public ImageSearchAyncTask(OnSearchFinished observer) {
        this.mObserver = observer;
    }

    private String fetchContent() throws IOException {
        URL url = null;
        StringBuffer jsonString = new StringBuffer();

        url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%22android%22&rsz=8");
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

    private ImageSearchResult convertToImageModelList(String jsonString) throws JSONException {
        List<Image> list = new ArrayList<>();

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

        ImageSearchResult searchResult = new ImageSearchResult();

        JSONObject cursor = data.getJSONObject("cursor");
        searchResult.setCount(cursor.getInt("estimatedResultCount"));
        searchResult.setMoreResultUrl(cursor.getString("moreResultsUrl"));

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

            searchResult.addImage(image);
        }

        return searchResult;
    }


    @Override
    protected ImageSearchResult doInBackground(Void... params) {
        try {
            String jsonString = fetchContent();
            ImageSearchResult imageSearchResult = convertToImageModelList(jsonString);

            return imageSearchResult;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(ImageSearchResult searchResult) {
        mObserver.onSearchFinished(searchResult);
    }
}
