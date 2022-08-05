package com.appsfeature.global.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.model.ContentModel;
import com.google.gson.reflect.TypeToken;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.squareup.picasso.Picasso;

public class ProductViewHolder extends RecyclerView.ViewHolder{

    public final TextView tvTitle, tvPrice;
    public final ImageView ivIcon;
    @Nullable
    private final TextView tvShortDesc, tvLongDesc;

    public ProductViewHolder(@NonNull View view) {
        super(view);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvTitle = view.findViewById(R.id.tv_title);
        tvPrice = view.findViewById(R.id.tv_price);
        tvShortDesc = view.findViewById(R.id.tv_short_desc);
        tvLongDesc = view.findViewById(R.id.tv_long_desc);
    }

    public void setData(ContentModel item, String imageUrl) {
        tvTitle.setText(item.getTitle());
        tvPrice.setText("Rs." + item.getPrice());

        if (tvShortDesc != null && !TextUtils.isEmpty(item.getShortDescription())) {
            tvShortDesc.setText(item.getShortDescription());
            tvShortDesc.setVisibility(View.VISIBLE);
        }
        if (tvLongDesc != null && !TextUtils.isEmpty(item.getLongDescription())) {
            tvLongDesc.setText(item.getLongDescription());
            tvLongDesc.setVisibility(View.VISIBLE);
        }

        if (ivIcon != null) {
            String imagePath = getImageUrlFromJson(imageUrl, item.getImage());
            int placeHolder = R.drawable.ic_placeholder_icon;
            if (BaseUtil.isValidUrl(imagePath)) {
                Picasso.get().load(imagePath)
                        .placeholder(placeHolder)
                        .into(ivIcon);
            } else {
                ivIcon.setImageResource(placeHolder);
            }
        }
    }

    private String getImageUrlFromJson(String imageUrl, String appImage) {
        if (!TextUtils.isEmpty(appImage)) {
            if(BaseUtil.isValidUrl(appImage)) {
                return imageUrl + appImage;
            }else if(appImage.startsWith("[")) {
                String[] images = GsonParser.fromJson(appImage, new TypeToken<String[]>() {
                });
                if(images != null && images.length > 0) {
                    return imageUrl + images[0];
                }else {
                    return null;
                }
            }
        }
        return appImage;
    }
}
