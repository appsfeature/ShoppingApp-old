package com.appsfeature.global.model;

import com.dynamic.model.DMContent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContentModel extends DMContent implements Serializable {

    @SerializedName("category_id")
    @Expose
    public int categoryId;
    @SerializedName("season_id")
    @Expose
    public String seasonId;
    @SerializedName(value="short_description")
    @Expose
    public String shortDescription;
    @SerializedName("long_description")
    @Expose
    public String longDescription;
    @SerializedName(value="video_url")
    @Expose
    public String videoUrl;
    @SerializedName(value="is_active")
    @Expose
    public int visibility;
    @SerializedName("price")
    @Expose
    public int price;
    @SerializedName("quantity")
    @Expose
    public int quantity;
    @SerializedName("feature")
    @Expose
    public String feature;
    @SerializedName("brand")
    @Expose
    public String brand;
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("view")
    @Expose
    public String productView;
    @SerializedName(value="published_date", alternate={"upload_time"})
    @Expose
    public String publishedDate;
    @SerializedName("publication_status")
    @Expose
    public String publicationStatus;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int getVisibility() {
        return visibility;
    }

    @Override
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProductView() {
        return productView;
    }

    public void setProductView(String productView) {
        this.productView = productView;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublicationStatus() {
        return publicationStatus;
    }

    public void setPublicationStatus(String publicationStatus) {
        this.publicationStatus = publicationStatus;
    }
}
