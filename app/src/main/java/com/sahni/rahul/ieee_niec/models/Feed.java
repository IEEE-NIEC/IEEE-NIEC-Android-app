package com.sahni.rahul.ieee_niec.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sahni on 30-Sep-17.
 */

public class Feed implements Parcelable{

    private String feedImageUrl;

    public Feed() {
    }

    protected Feed(Parcel in) {
        feedImageUrl = in.readString();
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(feedImageUrl);
    }


    public String getFeedImageUrl() {
        return feedImageUrl;
    }

    public void setFeedImageUrl(String feedImageUrl) {
        this.feedImageUrl = feedImageUrl;
    }
}
