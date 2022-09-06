package com.appsfeature.global.model;

import java.io.Serializable;
import java.util.List;

public class CartModel implements Serializable {

    private float price;
    private float discount;
    private float delivery;
    private float total;
    private List<ContentModel> products;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getDelivery() {
        return delivery;
    }

    public void setDelivery(float delivery) {
        this.delivery = delivery;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<ContentModel> getProducts() {
        return products;
    }

    public void setProducts(List<ContentModel> products) {
        this.products = products;
    }
}
