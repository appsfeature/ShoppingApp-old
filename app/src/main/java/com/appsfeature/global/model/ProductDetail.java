package com.appsfeature.global.model;

import java.io.Serializable;

public class ProductDetail implements Serializable {
    public int price;
    public String images;
    public String video;
    public String color;
    public String size;

    public String getColor() {
        return color;
    }

    public ProductDetail setColor(String color) {
        this.color = color;
        return this;
    }

    public String getSize() {
        return size;
    }

    public ProductDetail setSize(String size) {
        this.size = size;
        return this;
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
}
