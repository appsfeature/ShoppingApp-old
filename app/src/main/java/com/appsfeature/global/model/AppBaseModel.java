package com.appsfeature.global.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppBaseModel implements Serializable {

    @SerializedName("allcategory")
    @Expose
    private List<CategoryModel> list;

    @SerializedName("product_list")
    @Expose
    private List<ContentModel> productList;

    @SerializedName("image_slider")
    @Expose
    private List<ContentModel> sliderList;

    @SerializedName("product_view")
    @Expose
    private ContentModel productView;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("total_rows")
    @Expose
    private int totalRows;

    @SerializedName("total_page")
    @Expose
    private int totalPage;

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

    public List<ContentModel> getProductList() {
        return productList;
    }

    public void setProductList(List<ContentModel> productList) {
        this.productList = productList;
    }

    public ContentModel getProductView() {
        return productView;
    }

    public void setProductView(ContentModel productView) {
        this.productView = productView;
    }

    public List<ContentModel> getSliderList() {
        return sliderList;
    }

    public void setSliderList(List<ContentModel> sliderList) {
        this.sliderList = sliderList;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
