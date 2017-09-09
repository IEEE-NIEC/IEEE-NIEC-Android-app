package com.sahni.rahul.ieee_niec.models;

/**
 * Created by sahni on 02-Sep-17.
 */

public class UserSingInStatus {

    private boolean status;
    private User user;

    public UserSingInStatus(boolean status, User user) {
        this.status = status;
        this.user = user;
    }

    public boolean getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}
