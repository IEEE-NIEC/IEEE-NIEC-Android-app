package com.sahni.rahul.ieee_niec.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sahni on 27-Aug-17.
 */

public class ContentUtils {

    public static String EVENTS = "Events";
    public static String ACHIEVEMENTS = "Achievements";
    public static String PROJECTS = "Projects";
    public static String IEEE_RESOURCES = "IEEE Resources";
    public static String ABOUT_IEEE = "About IEEE";
    public static String INFO_KEY = "info_key";
    public static String SHARED_PREF = "ieee_niec";

    public static String USER_ID = "user_id";

    public static String ACTION_SIGN = "action_sign";
    public static String SIGNED_OUT = "sign_out";
    public static String SIGNED_IN = "sign_in";

    public static String USER_KEY = "user_key";

    public static String FEED_KEY = "feed_key";

    public static final String IMAGE_URL_KEY = "image_url_key";

    public static final int SHOW_INTEREST = 1;
    public static final int EDIT_INTEREST = 2;

    public static final String STATE_RESOLVING_ERROR = "resolving_error";

    public static final String TRANSITION_NAME = "transition_name";


    public static ArrayList<String> getInterestArrayList(String interest){
        ArrayList<String> interestArrayList = new ArrayList<>();
        if(interest != null){
            String interestArray[] = interest.split(";");
            Collections.addAll(interestArrayList, interestArray);
            interestArrayList.remove(0);
        }

        return interestArrayList;
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int)px;
    }

}
