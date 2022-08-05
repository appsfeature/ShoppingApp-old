package com.appsfeature.global.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class ColorModel implements Serializable {

    private String title;

    private List<ProductDetail> list;

    public ColorModel() {
    }

    public ColorModel(String title, List<ProductDetail> list) {
        this.title = title;
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        if(list != null && list.size() > 0){
            for (ProductDetail item : list){
                if(!TextUtils.isEmpty(item.getImages())){
                    return item.getImages();
                }
            }
        }
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProductDetail> getList() {
        return list;
    }

    public void setList(List<ProductDetail> list) {
        this.list = list;
    }
}
