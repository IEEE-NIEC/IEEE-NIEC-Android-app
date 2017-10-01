package com.sahni.rahul.ieee_niec.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sahni on 30-Sep-17.
 */

public class Feed implements Parcelable{

    private String mFeedImageUrl;

    public Feed() {
    }

    protected Feed(Parcel in) {
        mFeedImageUrl = in.readString();
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
        parcel.writeString(mFeedImageUrl);
    }


    public String getmFeedImageUrl() {
        return mFeedImageUrl;
    }

    public void setmFeedImageUrl(String mFeedImageUrl) {
        this.mFeedImageUrl = mFeedImageUrl;
    }
}
