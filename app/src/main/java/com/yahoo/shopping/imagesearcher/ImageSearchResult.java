package com.yahoo.shopping.imagesearcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesyan on 8/4/15.
 */
public class ImageSearchResult {
    private int count;
    private String moreResultUrl;
    private List<Image> images = new ArrayList<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMoreResultUrl() {
        return moreResultUrl;
    }

    public void setMoreResultUrl(String moreResultUrl) {
        this.moreResultUrl = moreResultUrl;
    }

    public List<Image> getImages() {
        return images;
    }

    public void addImage(Image image) {
        this.images.add(image);
    }
}
