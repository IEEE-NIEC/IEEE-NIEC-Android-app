package com.sahni.rahul.ieee_niec.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.sahni.rahul.ieee_niec.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by sahni on 27-Aug-17.
 */

public class ContentUtils {

    public static final String EVENTS = "Events";
    public static final String ACHIEVEMENTS = "Achievements";
    public static final String PROJECTS = "Projects";
    public static final String IEEE_RESOURCES = "IEEE Resources";
    public static final String ABOUT_IEEE = "About IEEE";
    public static final String INFO_KEY = "info_key";
    public static final String SHARED_PREF = "ieee_niec";

    public static final String ACTION_SIGN = "action_sign";
    public static final String SIGNED_OUT = "sign_out";
    public static final String DELETE_ACCOUNT = "delete_account";

    public static final String USER_KEY = "user_key";

    public static final String FEED_KEY = "feed_key";

    public static final String IMAGE_URL_KEY = "image_url_key";

    public static final int SHOW_INTEREST = 1;
    public static final int EDIT_INTEREST = 2;

    public static final String STATE_RESOLVING_ERROR = "resolving_error";

    public static final String TRANSITION_NAME = "transition_name";

    public static final String SEARCH_BY = "search_by";
    public static final String SEARCH_BY_NAME = "search_by_name";
    public static final String SEARCH_BY_INTEREST = "search_by_interest";
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String OTHER_SEX = "Other";



    public static ArrayList<String> getInterestArrayList(String interest) {
        ArrayList<String> interestArrayList = new ArrayList<>();
        if (interest != null) {
            String interestArray[] = interest.split(";");
            Collections.addAll(interestArrayList, interestArray);
            interestArrayList.remove(0);
        }

        return interestArrayList;
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public static HashMap<String, Boolean> getMapFromArrayList(ArrayList<String> interestArrayList) {
        HashMap<String, Boolean> interestMap = new HashMap<>();
        for (String interest : interestArrayList) {
            interestMap.put(interest, true);
        }
        return interestMap;
    }

    public static ArrayList<String> getArrayListFromMap(HashMap<String, Boolean> interestMap) {
        ArrayList<String> interestArrayList = new ArrayList<>();
        interestArrayList.addAll(interestMap.keySet());
        return interestArrayList;
    }

    public static void saveUserDataInSharedPref(User user, Context context) {
        Gson gson = new Gson();
        String userString = gson.toJson(user);
        SharedPreferences.Editor editor = context.getSharedPreferences(ContentUtils.SHARED_PREF,
                Context.MODE_PRIVATE).edit();
        editor.putString(ContentUtils.USER_KEY, userString);
        editor.apply();
    }

    /**
     * @param context
     * @return {{@link User}} if available else returns null
     */
    public static User getUserDataFromSharedPref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ContentUtils.SHARED_PREF, Context.MODE_PRIVATE);
        String userData = sharedPreferences.getString(ContentUtils.USER_KEY, null);
        return userData == null ? null : new Gson().fromJson(userData, User.class);
    }

    public static void deleteUserDataFromSharedPref(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(ContentUtils.SHARED_PREF,
                Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}