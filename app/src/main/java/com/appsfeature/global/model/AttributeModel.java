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
}
