package com.appsfeature.global.network;

import com.appsfeature.global.util.AppData;

public interface ApiEndPoint {
    String DEFAULT = "default";
    String INDEX  = "index";
    String COURSE_LOGIN = "courseLogin";//POST - email,password
    String TOOLS_LOGIN = "userLogin";//POST - email,password
    String ASSOCIATE_LOGIN = "associateLogin";// (POST - email,password)

    String COURSE_FORGET_PASSWORD = AppData.COURSE_FORGET_PASSWORD;
    String TOOLS_FORGET_PASSWORD = AppData.TOOLS_FORGET_PASSWORD;
    String ASSOCIATE_FORGET_PASSWORD = "";
    String GET_APP_DATA_USER = "getAppDataUser";
    String GET_APP_PRODUCT_BY_SUBCATEGORY = "getAppproduct_By_subcategory";
}
