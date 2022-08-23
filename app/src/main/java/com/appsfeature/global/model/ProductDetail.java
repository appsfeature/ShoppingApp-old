package com.appsfeature.global.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductDetail implements Cloneable,  Serializable {
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("size")
    @Expose
    private int size;

    private boolean isChecked;

    public String getColor() {
        return color;
    }

    public ProductDetail setColor(String color) {
        this.color = color;
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getImages() {
        return images;
    }

    public ProductDetail setImages(String images) {
        this.images = images;
        return this;
    }

    public String getVideo() {
        return video;
    }

    public ProductDetail setVideo(String video) {
        this.video = video;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public ProductDetail setPrice(int price) {
        this.price = price;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductDetail setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ProductDetail getClone() {
        try {
            return (ProductDetail) clone();
        } catch (CloneNotSupportedException e) {
            return new ProductDetail();
        }
    }
}
