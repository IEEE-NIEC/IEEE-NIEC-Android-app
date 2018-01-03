package com.sahni.rahul.ieee_niec.models;

/**
 * Created by sahni on 28-Dec-17.
 */

public class Social {

    private String title;
    private int imageId;
    private String url;

    public Social(String title, int imageId, String url) {
        this.title = title;
        this.imageId = imageId;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public int getImageId() {
        return imageId;
    }

    public String getUrl() {
        return url;
    }
}
