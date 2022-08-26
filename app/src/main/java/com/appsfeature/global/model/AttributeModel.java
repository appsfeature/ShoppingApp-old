package com.appsfeature.global.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttributeModel implements Serializable {
    @SerializedName("attribute_name")
    @Expose
    public String attributeName;
    @SerializedName("attributes_value")
    @Expose
    public String attributesValue;
    @SerializedName("sku_code")
    @Expose
    public String skuCode;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributesValue() {
        return attributesValue;
    }

    public void setAttributesValue(String attributesValue) {
        this.attributesValue = attributesValue;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
}
