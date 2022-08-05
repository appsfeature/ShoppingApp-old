package com.appsfeature.global.listeners;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({AttributeType.None, AttributeType.Color, AttributeType.Size})
@Retention(RetentionPolicy.SOURCE)
public @interface AttributeType {
    String None = "None";
    String Color = "Color";
    String Size = "Size";
}
