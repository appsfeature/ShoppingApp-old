package com.appsfeature.global.listeners;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({SeasonType.TYPE_WINTER, SeasonType.TYPE_SUMMER})
@Retention(RetentionPolicy.SOURCE)
public @interface SeasonType {
    int TYPE_WINTER = 1;
    int TYPE_SUMMER = 2;
}
