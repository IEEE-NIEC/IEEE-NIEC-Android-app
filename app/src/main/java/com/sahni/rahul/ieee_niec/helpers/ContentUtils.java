package com.sahni.rahul.ieee_niec.helpers;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sahni on 27-Aug-17.
 */

public class ContentUtils {

    public static String EVENTS = "Events";
    public static String ACHIEVEMENTS = "Achievements";
    public static String PROJECTS = "Projects";
    public static String IEEE = "IEEE";
    public static String INFO_KEY = "info_key";
    public static String SHARED_PREF = "ieee_niec";
    public static String USER_ID = "user_id";
    public static String SIGNED_OUT = "sign_out";
    public static String USER_KEY = "user_key";

    public static ArrayList<String> getInterestArrayList(String interest){
        ArrayList<String> interestArrayList = new ArrayList<>();
        if(interest != null){
            String interestArray[] = interest.split(";");
            Collections.addAll(interestArrayList, interestArray);
            interestArrayList.remove(0);
        }

        return interestArrayList;
    }
}
