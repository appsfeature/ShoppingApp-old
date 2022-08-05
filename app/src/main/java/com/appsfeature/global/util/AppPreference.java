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
    private static final String SESSION_LOGIN_URL = "session_login_url";
    private static final String IS_USER_LOGGED_IN_STATUS = "is_user_logged_in_status";
    private static final String SESSION_LOGIN_USERNAME = "session_login_username";
    private static final String SESSION_LOGIN_PASSWORD = "session_login_password";
    private static final String IMAGE_URL = "image_url";
    private static final String FILTER_GENDER = "filter_gender";
    private static final String FILTER_SEASON = "filter_season";


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

    public static String getSessionLoginUrl() {
        return getString(AppApplication.getInstance(), SESSION_LOGIN_URL);
    }

    public static void setSessionLoginUrl(String sessionLoginUrl) {
        setString(AppApplication.getInstance(), SESSION_LOGIN_URL, sessionLoginUrl);
    }

    public static String getUsername(int loginType) {
        return getString(AppApplication.getInstance(), SESSION_LOGIN_USERNAME + loginType);
    }

    public static void setUsername(int loginType, String username) {
        setString(AppApplication.getInstance(), SESSION_LOGIN_USERNAME + loginType, username);
    }

    public static String getPassword(int loginType) {
        return getString(AppApplication.getInstance(), SESSION_LOGIN_PASSWORD + loginType);
    }

    public static void setPassword(int loginType, String password) {
        setString(AppApplication.getInstance(), SESSION_LOGIN_PASSWORD + loginType, password);
    }

    public static boolean isLoginCompleted() {
        return !TextUtils.isEmpty(getSessionLoginUrl());
    }

    public static int isUserLoggedIn() {
        return getInt(AppApplication.getInstance(), IS_USER_LOGGED_IN_STATUS, AppConstant.NOT_LOGIN);
    }

    public static void setUserLoggedIn(int isUserLoggedIn) {
        setInt(AppApplication.getInstance(), IS_USER_LOGGED_IN_STATUS, isUserLoggedIn);
    }

    public static String getImageUrl() {
        return getString(AppApplication.getInstance(), IMAGE_URL);
    }

    public static void setImageUrl(String value) {
        setString(AppApplication.getInstance(), IMAGE_URL, value);
    }
}