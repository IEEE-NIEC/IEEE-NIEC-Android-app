package com.sahni.rahul.ieee_niec.networking;

import com.google.gson.annotations.SerializedName;
import com.sahni.rahul.ieee_niec.models.Information;

import java.util.ArrayList;

/**
 * Created by sahni on 27-Aug-17.
 */

public class EventsResponse {

    @SerializedName("events")
    private ArrayList<Information> infoArrayList;

    public ArrayList<Information> getInfoArrayList() {
        return infoArrayList;
    }

    public void setInfoArrayList(ArrayList<Information> infoArrayList) {
        this.infoArrayList = infoArrayList;
    }
}
