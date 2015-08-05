package com.yahoo.shopping.imagesearcher.models;

/**
 * Created by jamesyan on 8/5/15.
 */
public class FilterConfig {
    public enum ImageSizeFilter {NotSpecified, Small, Medium, Large, ExtraLarge}
    public enum ImageColorFilter {NotSpecified, Black, Blue, Brown, Gray, Green}
    public enum ImageTypeFiler {NotSpecified, Face, Photo, ClipArt, LineArt}

    private ImageSizeFilter mSizeFilter = ImageSizeFilter.NotSpecified;
    private ImageColorFilter mColorFilter = ImageColorFilter.NotSpecified;
    private ImageTypeFiler mTypeFilter = ImageTypeFiler.NotSpecified;
    private String mSiteFilter = "";

    public ImageSizeFilter getSizeFilter() {
        return mSizeFilter;
    }

    public void setSizeFilter(ImageSizeFilter sizeFilter) {
        this.mSizeFilter = sizeFilter;
    }

    public ImageColorFilter getColorFilter() {
        return mColorFilter;
    }

    public void setColorFilter(ImageColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
    }

    public ImageTypeFiler getTypeFilter() {
        return mTypeFilter;
    }

    public void setTypeFilter(ImageTypeFiler typeFilter) {
        this.mTypeFilter = typeFilter;
    }

    public String getSiteFilter() {
        return mSiteFilter;
    }

    public void setSiteFilter(String siteFilter) {
        this.mSiteFilter = siteFilter;
    }
}
