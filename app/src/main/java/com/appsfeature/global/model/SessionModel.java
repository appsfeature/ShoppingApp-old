package com.appsfeature.global.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionModel {

    @SerializedName("userLoginUrl")
    @Expose
    private String userLoginUrl;

    public String getUserLoginUrl() {
        return userLoginUrl;
    }

    public void setUserLoginUrl(String userLoginUrl) {
        this.userLoginUrl = userLoginUrl;
    }
}
