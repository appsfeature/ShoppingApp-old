package com.appsfeature.global.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VariantsModel implements Serializable {

    @SerializedName("price")
    @Expose
    public int price;
    @SerializedName("quantity")
    @Expose
    public int quantity;
    @SerializedName("images")
    @Expose
    public String images;
    @SerializedName("options")
    @Expose
    public List<AttributeModel> options;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<AttributeModel> getOptions() {
        return options;
    }

    public void setOptions(List<AttributeModel> options) {
        this.options = options;
    }
}
