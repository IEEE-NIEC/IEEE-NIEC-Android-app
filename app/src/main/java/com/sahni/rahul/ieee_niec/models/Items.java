package com.sahni.rahul.ieee_niec.models;

/**
 * Created by sahni on 27-Aug-17.
 */

public class Items {

    private String title;
    private int drawableId;
    private int bgDrawableId;

    public Items(String title, int drawableId, int bgDrawableId) {
        this.title = title;
        this.drawableId = drawableId;
        this.bgDrawableId = bgDrawableId;
    }

    public String getTitle() {
        return title;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getBgDrawableId() {
        return bgDrawableId;
    }
}
