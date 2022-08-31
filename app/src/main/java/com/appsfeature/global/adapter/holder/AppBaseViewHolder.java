package com.appsfeature.global.adapter.holder;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.squareup.picasso.Picasso;

public class AppBaseViewHolder extends RecyclerView.ViewHolder {

    public AppBaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected String getImageUrlFromJson(String imageUrl, String appImage) {
        if (!TextUtils.isEmpty(appImage)) {
            String mImage = appImage;
            if(appImage.startsWith("[")) {
                String[] images = GsonParser.fromJson(appImage, new TypeToken<String[]>() {
                });
                if(images != null && images.length > 0) {
                    mImage =  images[0];
                }
            }

            if(BaseUtil.isValidUrl(mImage)) {
                return mImage;
            }else {
                return imageUrl + appImage;
            }
        }
        return appImage;
    }

    private Picasso picasso;

    public Picasso getPicasso() {
        if (picasso == null) picasso = Picasso.get();
        return picasso;
    }

}
