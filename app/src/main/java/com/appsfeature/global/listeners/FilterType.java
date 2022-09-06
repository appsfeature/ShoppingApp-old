package com.appsfeature.global.listeners;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FilterType.TYPE_COLOR, FilterType.TYPE_SIZE})
@Retention(RetentionPolicy.SOURCE)
public @interface FilterType {
    int TYPE_COLOR = 1;
    int TYPE_SIZE = 2;
}
