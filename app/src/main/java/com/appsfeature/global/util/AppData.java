package com.appsfeature.global.util;

import com.appsfeature.global.AppValues;
import com.appsfeature.global.R;
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.listeners.LoginType;
import com.appsfeature.global.network.ApiEndPoint;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

import java.util.ArrayList;
import java.util.List;

public class AppData {

    public static String TOOLS_REGISTER_URL = "https://www.bizwiz.co.in/register";
    public static String ASSOCIATE_REGISTER_URL = "https://www.bizwiz.co.in/agent/register";
    public static String USER_FORGOT_PASSWORD_URL = "https://www.bizwiz.co.in/forgotpassword";
    public static String COURSE_REGISTER_URL = "";

    public static String COURSE_FORGET_PASSWORD = "https://www.bizwiz.co.in/resetcoursepassword";
    public static String TOOLS_FORGET_PASSWORD = "https://www.bizwiz.co.in/forgotpassword";

    public static String[] loginTitle = {"Course Login", "Tools Login", "Associate Login"};
    public static String[] signUpTitle = {"Course Registration", "Tools Registration", "Associate Registration"};
    public static String[] signUpUrl = {COURSE_REGISTER_URL, TOOLS_REGISTER_URL, ASSOCIATE_REGISTER_URL};
    public static String[] loginHosts = {ApiEndPoint.COURSE_LOGIN, ApiEndPoint.TOOLS_LOGIN, ApiEndPoint.ASSOCIATE_LOGIN};
    public static String[] forgetPasswordUrls = {ApiEndPoint.COURSE_FORGET_PASSWORD, ApiEndPoint.TOOLS_FORGET_PASSWORD, ApiEndPoint.ASSOCIATE_FORGET_PASSWORD};


    public static int[] ids = new int[]{1, 2, 3};
    public static int[] itemTypes = new int[]{LoginType.TYPE_COURSE, LoginType.TYPE_TOOLS, LoginType.TYPE_ASSOCIATE};
    public static int[] images = new int[]{
            R.drawable.ic_login_course, R.drawable.ic_login_tools, R.drawable.ic_login_assosiate
    };
    public static String[] title = new String[]{
            "Course for Excel, MS Word, Power Point",
            "Automation tools, Billing & Accounting Software",
            "Business Partner Login to manage Billing, Accounting, Office task & Staff"
    };
    public static String[] buttonLabel = new String[]{
            "Login for Course",
            "Login for Tools",
            "Login for Associates"
    };

    private static DMContent getLoginList(int pos, boolean isLoginComplete) {
        DMContent item;
        item = new DMContent();
        item.setId(ids[pos]);
        item.setItemType(itemTypes[pos]);
        item.setTitle(title[pos]);
        item.setDescription(isLoginComplete ? "Click Here to start session" : buttonLabel[pos]);
        item.setVisibility(images[pos]);
        return item;
    }
}
