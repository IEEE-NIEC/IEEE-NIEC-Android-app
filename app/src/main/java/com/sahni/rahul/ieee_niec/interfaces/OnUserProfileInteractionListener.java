package com.sahni.rahul.ieee_niec.interfaces;

import com.sahni.rahul.ieee_niec.models.User;

/**
 * Created by sahni on 24-Sep-17.
 */

public interface OnUserProfileInteractionListener {

    void editProfile(User user);

    void signOut();

    void deleteAccount();
}
