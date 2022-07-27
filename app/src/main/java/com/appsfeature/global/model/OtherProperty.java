package com.appsfeature.global.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OtherProperty {

    @Expose
    @SerializedName(value="isPlayerStyleMinimal")
    private boolean isPlayerStyleMinimal;

    @Expose
    @SerializedName(value="is_browser_chrome")
    private boolean isBrowserChrome = false;

    public boolean isPlayerStyleMinimal() {
        return isPlayerStyleMinimal;
    }

    public void setPlayerStyleMinimal(boolean playerStyleMinimal) {
        isPlayerStyleMinimal = playerStyleMinimal;
    }

    public boolean isBrowserChrome() {
        return isBrowserChrome;
    }

    public void setBrowserChrome(boolean browserChrome) {
        isBrowserChrome = browserChrome;
    }
}
