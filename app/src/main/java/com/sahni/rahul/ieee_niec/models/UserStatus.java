package com.sahni.rahul.ieee_niec.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sahni on 09-Sep-17.
 */

public class UserStatus {

    @SerializedName("user")
    private TempUser tempUser;

    private int code;

    public TempUser getUser() {
        return tempUser;
    }

    public void setUser(TempUser tempUser) {
        this.tempUser = tempUser;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    public static class TempUser{

        private String name;

        @SerializedName("image_url")
        private String imageUrl;

        @SerializedName("email")
        private String emailId;

        @SerializedName("mobile")
        private String mobileNumber;

        private String interest;

        @SerializedName("id")
        private String userId;


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

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
