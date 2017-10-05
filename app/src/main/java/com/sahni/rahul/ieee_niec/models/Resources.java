package com.sahni.rahul.ieee_niec.models;

/**
 * Created by sahni on 01-Oct-17.
 */

public class Resources {

    private String mTitle;

    private int mImageResId;

    private String mUrl;

    public Resources(String mTitle, int mImageResId, String mUrl) {
        this.mTitle = mTitle;
        this.mImageResId = mImageResId;
        this.mUrl = mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmImageResId() {
        return mImageResId;
    }

    public void setmImageResId(int mImageResId) {
        this.mImageResId = mImageResId;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
