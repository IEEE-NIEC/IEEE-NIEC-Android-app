package com.sahni.rahul.ieee_niec.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by sahni on 05-Oct-17.
 */

public class FirestoreUser implements Parcelable {

    private String uId;

    private String name;

    private String emailId;

    private String imageUrl;

    private String mobileNo;

    private HashMap<String, Boolean> interestMap;

    private String about;

    public FirestoreUser() {
    }

    public FirestoreUser(String uId, String name, String emailId, String imageUrl) {
        this.uId = uId;
        this.name = name;
        this.emailId = emailId;
        this.imageUrl = imageUrl;
    }

    protected FirestoreUser(Parcel in) {
        uId = in.readString();
        name = in.readString();
        emailId = in.readString();
        imageUrl = in.readString();
        mobileNo = in.readString();
        interestMap = new HashMap<>();
        in.readMap(interestMap, Boolean.class.getClassLoader());
        about = in.readString();
    }

    public static final Creator<FirestoreUser> CREATOR = new Creator<FirestoreUser>() {
        @Override
        public FirestoreUser createFromParcel(Parcel in) {
            return new FirestoreUser(in);
        }

        @Override
        public FirestoreUser[] newArray(int size) {
            return new FirestoreUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uId);
        parcel.writeString(name);
        parcel.writeString(emailId);
        parcel.writeString(imageUrl);
        parcel.writeString(mobileNo);
        parcel.writeMap(interestMap);
        parcel.writeString(about);
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public HashMap<String, Boolean> getInterestMap() {
        return interestMap;
    }

    public void setInterestMap(HashMap<String, Boolean> interestMap) {
        this.interestMap = interestMap;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
