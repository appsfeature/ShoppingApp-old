package com.appsfeature.global.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helper.model.BaseCategoryProperty;
import com.helper.util.GsonParser;

import java.io.Serializable;

public class ExtraProperty extends BaseCategoryProperty implements Cloneable, Serializable {

    private String description;
    private String videoId;
    private int videoTime = 0;
    private int videoDuration = 0;
    private int isRead = 0;
    private int isFav = 0;
    private String jsonData;
    private String channelId;
    private boolean isPlayerStyleMinimal;

    private CommonModel commonModel;

    public CommonModel getCommonModel() {
        return commonModel;
    }

    public void setCommonModel(CommonModel commonModel) {
        this.commonModel = commonModel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(int videoTime) {
        this.videoTime = videoTime;
    }

    public int getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(int videoDuration) {
        this.videoDuration = videoDuration;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String toJson() {
        return GsonParser.getGson().toJson(this, ExtraProperty.class);
    }

    public boolean isPlayerStyleMinimal() {
        return isPlayerStyleMinimal;
    }

    public void setPlayerStyleMinimal(boolean playerStyleMinimal) {
        isPlayerStyleMinimal = playerStyleMinimal;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ExtraProperty getClone() {
        try {
            return (ExtraProperty) clone();
        } catch (CloneNotSupportedException e) {
            return new ExtraProperty();
        }
    }
}
