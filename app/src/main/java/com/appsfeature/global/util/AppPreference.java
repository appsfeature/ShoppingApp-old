package com.appsfeature.global.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.appsfeature.global.AppApplication;

public class AppPreference {

    private static final String TAG = "LoginPrefUtil";
    private static final String SESSION_LOGIN_URL = "session_login_url";
    private static final String IS_USER_LOGGED_IN_STATUS = "is_user_logged_in_status";
    private static final String SESSION_LOGIN_USERNAME = "session_login_username";
    private static final String SESSION_LOGIN_PASSWORD = "session_login_password";
    private static final String IMAGE_URL = "image_url";




    public static String getString(String key) {
        if (getDefaultSharedPref() != null) {
            return decrypt(getDefaultSharedPref().getString(encrypt(key), ""));
        } else {
            return decrypt("");
        }
    }

    public static int getInt(String key, int defaultValue) {
        if (getDefaultSharedPref() != null) {
            return getDefaultSharedPref().getInt(encrypt(key), defaultValue);
        } else {
            return (0);
        }
    }

    public static int getIntDef(String key, int def) {
        return getDefaultSharedPref().getInt(encrypt(key), def);
    }

    public static float getFloat(String key) {
        return getDefaultSharedPref().getFloat(encrypt(key), 0);

    }

    public static long getLong(String key) {
        return getDefaultSharedPref().getLong(encrypt(key), 0);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        if (getDefaultSharedPref() != null) {
            return getDefaultSharedPref().getBoolean(key, defaultValue);
        } else {
            return false;
        }
    }


    private static String getEmptyData(String data) {
        return TextUtils.isEmpty(data) ? "" : data;
    }

    public static void setString(String key, String values) {
        if (getDefaultSharedPref() != null && !TextUtils.isEmpty(key)) {
            final SharedPreferences.Editor editor = getDefaultSharedPref().edit();
            if (editor != null) {
                editor.putString(encrypt(key), encrypt(values));
                editor.apply();
            }
        }
    }

    /**
     * Set int value for key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void setInt(String key, int value) {
        if (getDefaultSharedPref() != null && !TextUtils.isEmpty(key)) {
            final SharedPreferences.Editor editor = getDefaultSharedPref().edit();
            if (editor != null) {
                editor.putInt(encrypt(key), value);
                editor.apply();
            }
        }
    }

    /**
     * Set float value for a key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void setFloat(String key, float value) {
        final SharedPreferences.Editor editor = getDefaultSharedPref().edit();
        editor.putFloat(encrypt(key), value);
        editor.apply();
    }

    /**
     * Set long value for key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void setLong(String key, long value) {
        final SharedPreferences.Editor editor = getDefaultSharedPref().edit();
        editor.putLong(encrypt(key), value);
        editor.apply();
    }

    /**
     * Set boolean value for key.
     *
     * @param key   The string resource Id of the key
     * @param value The value to set for the key
     */
    public static void setBoolean(String key, boolean value) {
        if (getDefaultSharedPref() != null && !TextUtils.isEmpty(key)) {
            final SharedPreferences.Editor editor = getDefaultSharedPref().edit();
            if (editor != null) {
                editor.putBoolean(encrypt(key), value);
                editor.apply();
            }
        }
    }

    /**
     * Clear all preferences.
     */
    public static void clearPreferences() {
//        final SharedPreferences.Editor editor = getDefaultSharedPref().edit();
//        editor.clear();
//        editor.apply();
        setSessionLoginUrl("");
        setUserLoggedIn(AppConstant.NOT_LOGIN);
    }

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences getDefaultSharedPref() {
        if (sharedPreferences == null) {
            Context context = AppApplication.getInstance();
            sharedPreferences = context.getSharedPreferences(context.getPackageName() + "main", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }


    private static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return input;
//        if (SupportUtil.isEmptyOrNull( input )) {
//            return input ;
//        } else {
//            return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
//        }
    }

    private static String decrypt(String input) {
        return input;
//        if (SupportUtil.isEmptyOrNull( input )) {
//            return input ;
//        } else {
//            return new String(Base64.decode(input, Base64.DEFAULT));
//        }
    }

    public static String getSessionLoginUrl() {
        return getString(SESSION_LOGIN_URL);
    }

    public static void setSessionLoginUrl(String sessionLoginUrl) {
        setString(SESSION_LOGIN_URL, sessionLoginUrl);
    }

    public static String getUsername(int loginType) {
        return getString(SESSION_LOGIN_USERNAME + loginType);
    }

    public static void setUsername(int loginType, String username) {
        setString(SESSION_LOGIN_USERNAME + loginType, username);
    }

    public static String getPassword(int loginType) {
        return getString(SESSION_LOGIN_PASSWORD + loginType);
    }

    public static void setPassword(int loginType, String password) {
        setString(SESSION_LOGIN_PASSWORD + loginType, password);
    }

    public static boolean isLoginCompleted() {
        return !TextUtils.isEmpty(getSessionLoginUrl());
    }

    public static int isUserLoggedIn() {
        return getInt(IS_USER_LOGGED_IN_STATUS, AppConstant.NOT_LOGIN);
    }

    public static void setUserLoggedIn(int isUserLoggedIn) {
        setInt(IS_USER_LOGGED_IN_STATUS, isUserLoggedIn);
    }

    public static String getImageUrl() {
        return getString(IMAGE_URL);
    }

    public static void setImageUrl(String value) {
        setString(IMAGE_URL, value);
    }
}