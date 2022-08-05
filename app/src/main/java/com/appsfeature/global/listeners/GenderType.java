package com.appsfeature.global.listeners;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({GenderType.TYPE_GIRL, GenderType.TYPE_BOY})
@Retention(RetentionPolicy.SOURCE)
public @interface GenderType {
    int TYPE_GIRL = 1;
    int TYPE_BOY = 2;
}
