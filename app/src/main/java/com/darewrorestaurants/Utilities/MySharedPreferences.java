package com.darewrorestaurants.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class MySharedPreferences {

    private String MY_PREFS_NAME = "com.darewrorestaurants";

    private String IS_FIRST = "firstrun";
    private String IS_LOGIN = "login";

    public SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
    }

    public boolean isFirstRun(Context context) {
        return getPrefs(context).getBoolean(IS_FIRST, false);
    }
    public void setFirstRun(Context context,boolean firstRun) {
        getPrefs(context).edit().putBoolean(IS_FIRST, firstRun).commit();
    }

    public boolean isLogin(Context context) {
        return getPrefs(context).getBoolean(IS_LOGIN, false);
    }
    public void setLogin(Context context,boolean register) {
        getPrefs(context).edit().putBoolean(IS_LOGIN, register).commit();
    }

    public int getUserID(Context context) {
        return getPrefs(context).getInt(Constants.USER_ID, 0);
    }
    public void setUserID(Context context,int userID) {
        getPrefs(context).edit().putInt(Constants.USER_ID, userID).commit();
    }

    public String getUserTitle(Context context) {
        return getPrefs(context).getString(Constants.USER_TITLE, "");
    }
    public void setUserTitle(Context context,String userTitle) {
        getPrefs(context).edit().putString(Constants.USER_TITLE, userTitle).commit();
    }

    public String getUserEmail(Context context) {
        return getPrefs(context).getString(Constants.USER_EMAIL, "");
    }
    public void setUserEmail(Context context,String userEmail) {
        getPrefs(context).edit().putString(Constants.USER_EMAIL, userEmail).commit();
    }

    public String getUserImage(Context context) {
        return getPrefs(context).getString(Constants.USER_IMAGE ,"");
    }
    public void setUserImage(Context context,String userImage) {
        getPrefs(context).edit().putString(Constants.USER_IMAGE, userImage).commit();
    }
    public String getUserMobileNumber(Context context) {
        return getPrefs(context).getString(Constants.USER_MOBILE_NUMBER, "");
    }
    public void setUserMobileNumber(Context context,String userMobNo) {
        getPrefs(context).edit().putString(Constants.USER_MOBILE_NUMBER, userMobNo).commit();
    }

    public int getRestaurantID(Context context) {
        return getPrefs(context).getInt(Constants.RESTAURANT_ID, 0);
    }
    public void setRestaurantID(Context context,int restroID) {
        getPrefs(context).edit().putInt(Constants.RESTAURANT_ID, restroID).commit();
    }

    public String getRestaurantName(Context context) {
        return getPrefs(context).getString(Constants.RESTAURANT_NAME, "");
    }
    public void setRestaurantName(Context context,String restroName) {
        getPrefs(context).edit().putString(Constants.RESTAURANT_NAME, restroName).commit();
    }

    public String getRestaurantLocation(Context context) {
        return getPrefs(context).getString(Constants.RESTAURANT_LOCATION, "");
    }
    public void setRestaurantLocation(Context context,String restroLoc) {
        getPrefs(context).edit().putString(Constants.RESTAURANT_LOCATION, restroLoc).commit();
    }

    public String getRestaurantDetails(Context context) {
        return getPrefs(context).getString(Constants.RESTAURANT_DETAIL, "");
    }
    public void setRestaurantDetails(Context context,String restroDetail) {
        getPrefs(context).edit().putString(Constants.RESTAURANT_DETAIL, restroDetail).commit();
    }

    public String getRestaurantContactNumber(Context context) {
        return getPrefs(context).getString(Constants.RESTAURANT_CONTACT_NO, "");
    }
    public void setRestaurantContactNumber(Context context,String contactNum) {
        getPrefs(context).edit().putString(Constants.RESTAURANT_CONTACT_NO, contactNum).commit();
    }

    public String getRestaurantStartTime(Context context) {
        return getPrefs(context).getString(Constants.RESTAURANT_START_TIME, "");
    }
    public void setRestaurantStartTime(Context context,String startTime) {
        getPrefs(context).edit().putString(Constants.RESTAURANT_START_TIME, startTime).commit();
    }

    public String getRestaurantEndTime(Context context) {
        return getPrefs(context).getString(Constants.RESTAURANT_CLOSE_TIME, "");
    }
    public void setRestaurantEndTime(Context context,String entTime) {
        getPrefs(context).edit().putString(Constants.RESTAURANT_CLOSE_TIME, entTime).commit();
    }

    public String getRestaurantLogo(Context context) {
        return getPrefs(context).getString(Constants.RESTAURANT_LOGO, "");
    }
    public void setRestaurantLogo(Context context,String logo) {
        getPrefs(context).edit().putString(Constants.RESTAURANT_LOGO, logo).commit();
    }
}
