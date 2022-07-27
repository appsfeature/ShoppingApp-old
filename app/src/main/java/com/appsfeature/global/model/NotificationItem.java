package com.appsfeature.global.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationItem {

    @Expose
    @SerializedName(value="id")
    private int id;
    @Expose
    @SerializedName(value="androidNotificationId")
    private int androidNotificationId;
    @Expose
    @SerializedName(value="itemId")
    private int itemId;
    @Expose
    @SerializedName(value="uuid")
    private String uuid;
    @Expose
    @SerializedName(value="notificationType")
    private String notificationType;
    @Expose
    @SerializedName(value="title")
    private String title;
    @Expose
    @SerializedName(value="body")
    private String body;
    @Expose
    @SerializedName(value="imageUrl")
    private String imageUrl;
    @Expose
    @SerializedName(value="launchURL")
    private String launchURL;
    @Expose
    @SerializedName(value="jsonData")
    private String jsonData;
    @Expose
    @SerializedName(value="isRead")
    private boolean isRead;
    @Expose
    @SerializedName(value="updatedAt")
    private String updatedAt;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAndroidNotificationId() {
        return androidNotificationId;
    }

    public void setAndroidNotificationId(int androidNotificationId) {
        this.androidNotificationId = androidNotificationId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private JSONObject jsonObject;

    public JSONObject getJsonDataObject() {
        try {
            if (!TextUtils.isEmpty(jsonData)) {
                jsonObject = new JSONObject(jsonData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLaunchURL() {
        return launchURL;
    }

    public void setLaunchURL(String launchURL) {
        this.launchURL = launchURL;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
