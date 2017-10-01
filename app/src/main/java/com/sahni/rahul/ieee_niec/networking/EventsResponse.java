package com.sahni.rahul.ieee_niec.networking;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sahni.rahul.ieee_niec.models.Information;

import java.util.ArrayList;

/**
 * Created by sahni on 27-Aug-17.
 */

public class EventsResponse implements Parcelable{

    @SerializedName("events")
    private ArrayList<Information> infoArrayList;


    public ArrayList<Information> getInfoArrayList() {
        return infoArrayList;
    }

    public void setInfoArrayList(ArrayList<Information> infoArrayList) {
        this.infoArrayList = infoArrayList;
    }

    public static final Creator<EventsResponse> CREATOR = new Creator<EventsResponse>() {
        @Override
        public EventsResponse createFromParcel(Parcel in) {
            return new EventsResponse(in);
        }

        @Override
        public EventsResponse[] newArray(int size) {
            return new EventsResponse[size];
        }
    };

    protected EventsResponse(Parcel in) {
        infoArrayList = in.createTypedArrayList(Information.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(infoArrayList);
    }
}
