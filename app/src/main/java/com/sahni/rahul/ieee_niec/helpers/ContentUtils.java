package com.sahni.rahul.ieee_niec.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;

import com.google.gson.Gson;
import com.sahni.rahul.ieee_niec.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;

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

    public static final String FIRESTORE_EVENTS = "events";
    public static final String FIRESTORE_ACHIEVEMENTS = "achievements";
    public static final String FIRESTORE_PROJECTS = "projects";
    public static final String FIRESTORE_USERS = "users";
    public static final String FIRESTORE_FEEDS = "feeds";
    public static final String FIRESTORE_EXECOMM = "execomm";
    public static final String FIREBASE_STORAGE_PROFILE_IMAGE = "profile_image";
    public static final  String FIRESTORE_PAST_EXECOMM = "pastExecomm";
    public static final  String FIRESTORE_PAST_EXECOMM_SESSION = "session";
    public static final  String FIRESTORE_PAST_EXECOMM_SESSION_YEAR = "year";


    public static final String NOTIFICATION_DATA_PAYLOAD_KEY = "data_key";
    public static final String NOTIFICATION_TITLE_KEY = "title";
    public static final String FEEDS_DATA_PAYLOAD = "feed";
    public static final String EVENTS_DATA_PAYLOAD = "event";
    public static final String ACHIEVEMENT_DATA_PAYLOAD = "achievement";
    public static final String PROJECT_DATA_PAYLOAD = "project";
    public static final String EXECOMM_DATA_PAYLOAD = "execomm";

    public static final String WEBSITE_URL = "http://www.ieeeniec.com";
    public static final String FACEBOOK_URL = "https://www.facebook.com/ieeeniec";
    public static final String INSTAGRAM_URL = "https://www.instagram.com/ieeeniec";
    public static final String TWITTER_URL = "https://www.twitter.com/ieeeniec";
    public static final String GOOGLE_PLUS_URL = "https://plus.google.com/102106854753581893748";
    public static final String JOIN_IEEE_URL = "https://goo.gl/forms/pGTEhdkBEc5ZiTgR2";
    public static final String PRIVACY_POLICY_URL = "https://sites.google.com/view/ieeeniec-app-privacy/home";



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

    public static String formatString(String description){
        description = description.replace("\\n", "\n").replace("\\r", "\r");
        return description;

    }

    public static void syncIndicatorWithViewPager(ViewPager viewPager, CircleIndicator circleIndicator){
        if(viewPager == null || circleIndicator == null) {
            return;
        }
        if (viewPager.getAdapter().getCount() > 1) {
            circleIndicator.setVisibility(View.VISIBLE);
        } else {
            circleIndicator.setVisibility(View.INVISIBLE);
        }
    }
}