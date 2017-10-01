package com.sahni.rahul.ieee_niec.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sahni on 27-Aug-17.
 */

public class Information implements Parcelable{

    private int id;
    private String title;
    private String description;

//    @SerializedName("url")
    private ArrayList<String> imageUrlArrayList;

    public Information() {
    }

    protected Information(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        imageUrlArrayList = in.createStringArrayList();
    }

    public static final Creator<Information> CREATOR = new Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel in) {
            return new Information(in);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getImageUrlArrayList() {
        return imageUrlArrayList;
    }

    public void setImageUrlArrayList(ArrayList<String> imageUrlArrayList) {
        this.imageUrlArrayList = imageUrlArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeStringList(imageUrlArrayList);
    }
}
