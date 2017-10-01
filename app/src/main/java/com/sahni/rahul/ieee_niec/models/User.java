package com.sahni.rahul.ieee_niec.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sahni on 01-Sep-17.
 */

public class User implements Parcelable{

    private String name;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("email")
    private String emailId;

    @SerializedName("mobile")
    private String mobileNumber;

    @SerializedName("interest")
    private ArrayList<String> interestArrayList;

    @SerializedName("id")
    private String userId;

    private String idToken;

    public User(){

    }

    public User(String name, String imageUrl, String emailId, String mobileNumber, ArrayList<String> interestArrayList, String userId, String idToken) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.interestArrayList = interestArrayList;
        this.userId = userId;
        this.idToken = idToken;
    }

    public User(String name, String imageUrl, String emailId, String mobileNumber, ArrayList<String> interestArrayList, String userId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.emailId = emailId;
        this.mobileNumber = mobileNumber;
        this.interestArrayList = interestArrayList;
        this.userId = userId;
    }

    public User(String name, String imageUrl, String emailId, String userId, String idToken) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.emailId = emailId;
        this.userId = userId;
        this.idToken = idToken;
    }

    protected User(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        emailId = in.readString();
        mobileNumber = in.readString();
        interestArrayList = in.createStringArrayList();
        userId = in.readString();
        idToken = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(imageUrl);
        parcel.writeString(emailId);
        parcel.writeString(mobileNumber);
        parcel.writeStringList(interestArrayList);
        parcel.writeString(userId);
        parcel.writeString(idToken);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public ArrayList<String> getInterestArrayList() {
        return interestArrayList;
    }

    public void setInterestArrayList(ArrayList<String> interestArrayList) {
        this.interestArrayList = interestArrayList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
