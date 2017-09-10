package com.sahni.rahul.ieee_niec.networking;

import com.google.gson.annotations.SerializedName;
import com.sahni.rahul.ieee_niec.models.UserStatus;

import java.util.ArrayList;

/**
 * Created by sahni on 09-Sep-17.
 */

public class SearchResponse {

    @SerializedName("users")
    private ArrayList<UserStatus.TempUser> tempUserArrayList;

    private int code;

    public ArrayList<UserStatus.TempUser> getTempUserArrayList() {
        return tempUserArrayList;
    }

    public void setTempUserArrayList(ArrayList<UserStatus.TempUser> tempUserArrayList) {
        this.tempUserArrayList = tempUserArrayList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
