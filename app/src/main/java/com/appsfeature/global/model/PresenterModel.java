package com.appsfeature.global.model;

import java.util.List;

public class PresenterModel {

    private List<CommonModel> commonList = null;

    public List<CommonModel> getCommonList() {
        return commonList;
    }

    public PresenterModel setCommonList(List<CommonModel> commonList) {
        this.commonList = commonList;
        return this;
    }
}
