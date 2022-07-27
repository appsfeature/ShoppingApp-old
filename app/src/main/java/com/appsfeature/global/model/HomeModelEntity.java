package com.appsfeature.global.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeModelEntity implements Serializable {

    @SerializedName("allcategory")
    @Expose
    private List<CategoryModel> list;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    public List<CategoryModel> getList() {
        return list;
    }

    public void setList(List<CategoryModel> list) {
        this.list = list;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
