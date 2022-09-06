package com.appsfeature.global.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FilterModel implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("attributes_id")
    @Expose
    private int attributesId;
    @SerializedName("attributes_ids")
    @Expose
    private List<FilterModel> attributesIds;

    private boolean isChecked = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getAttributesId() {
        return attributesId;
    }

    public void setAttributesId(int attributesId) {
        this.attributesId = attributesId;
    }

    public List<FilterModel> getAttributesIds() {
        return attributesIds;
    }

    public void setAttributesIds(List<FilterModel> attributesIds) {
        this.attributesIds = attributesIds;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
