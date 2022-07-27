package com.appsfeature.global.model;

import com.dynamic.model.DMCategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryModel extends DMCategory<ContentModel> implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("table_name")
    @Expose
    public String tableName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
