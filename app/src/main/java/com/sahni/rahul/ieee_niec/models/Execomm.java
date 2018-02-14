package com.sahni.rahul.ieee_niec.models;

/**
 * Created by sahni on 03-Jan-18.
 */

public class Execomm {
    private String name;
    private String designation;
    private String phoneNo;
    private String emailId;
    private String photoUrl;

    public Execomm() {
    }

    public Execomm(String name, String designation, String phoneNo, String emailId, String photoUrl) {
        this.name = name;
        this.designation = designation;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public String toString() {
        return "name ="+name+"\npost= "+designation+"\nphone ="+phoneNo+
                "\nemail ="+emailId;
    }
}
