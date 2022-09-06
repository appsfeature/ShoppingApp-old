package com.appsfeature.global.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.listeners.GenderType;
import com.appsfeature.global.listeners.SeasonType;
import com.helper.util.BasePrefUtil;

public class AppPreference extends BasePrefUtil {

    private static final String TAG = "LoginPrefUtil";
    private static final String IMAGE_URL = "image_url";
    private static final String FILTER_GENDER = "filter_gender";
    private static final String FILTER_SEASON = "filter_season";
    private static final String PROFILE = "profile";
    private static final String COUNTRY = "country";
    public static final String ATTRIBUTES = "Attributes";


    @GenderType
    public static int getGender() {
        return getInt(AppApplication.getInstance(), FILTER_GENDER, GenderType.TYPE_GIRL);
    }

    public static void setGender(@GenderType int value) {
        setInt(AppApplication.getInstance(), FILTER_GENDER, value);
    }

    @SeasonType
    public static int getSeason() {
        return getInt(AppApplication.getInstance(), FILTER_SEASON, SeasonType.TYPE_WINTER);
    }

    public static void setSeason(@SeasonType int value) {
        setInt(AppApplication.getInstance(), FILTER_SEASON, value);
    }


    public static boolean isLoginCompleted() {
        return !TextUtils.isEmpty(getProfile());
    }

    public static String getImageUrl() {
        return getString(AppApplication.getInstance(), IMAGE_URL);
    }

    public static void setImageUrl(String value) {
        if(!TextUtils.isEmpty(value)) {
            setString(AppApplication.getInstance(), IMAGE_URL, value);
        }
    }

    public static String getProfile() {
        return getString(AppApplication.getInstance(), PROFILE);
    }

    public static void setProfile(String value) {
        if(!TextUtils.isEmpty(value)) {
            setString(AppApplication.getInstance(), PROFILE, value);
        }
    }

    public static String getCountry() {
        return getString(AppApplication.getInstance(), COUNTRY);
    }

    public static void setCountry(String value) {
        if(!TextUtils.isEmpty(value)) {
            setString(AppApplication.getInstance(), COUNTRY, value);
        }
    }
}