package com.appsfeature.global.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class SizeModel implements Serializable {

    private int size;

    private boolean isChecked;

    private List<ProductDetail> list;

    public SizeModel() {
    }

    public SizeModel(int size, boolean isChecked, List<ProductDetail> list) {
        this.size = size;
        this.isChecked = isChecked;
        this.list = list;
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


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ProductDetail> getList() {
        return list;
    }

    public void setList(List<ProductDetail> list) {
        this.list = list;
    }
}
