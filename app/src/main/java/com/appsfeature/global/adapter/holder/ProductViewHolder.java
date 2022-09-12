package com.appsfeature.global.adapter.holder;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
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
import com.helper.util.StyleUtil;
import com.squareup.picasso.Picasso;

public class ProductViewHolder extends AppBaseViewHolder{

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
        tvTitle.setText(BaseUtil.fromHtml(item.getTitle()));
        tvPrice.setText(getPrice(item.getPrice(), item.getDiscountPrice()));

        if (tvShortDesc != null && !TextUtils.isEmpty(item.getShortDescription())) {
            tvShortDesc.setText(BaseUtil.fromHtml(item.getShortDescription()));
            tvShortDesc.setVisibility(View.VISIBLE);
        }
        if (tvLongDesc != null && !TextUtils.isEmpty(item.getLongDescription())) {
            tvLongDesc.setText(BaseUtil.fromHtml(item.getLongDescription()));
            tvLongDesc.setVisibility(View.VISIBLE);
        }

        if (ivIcon != null) {
            String imagePath = getImageUrlFromJson(imageUrl, item.getImage());
            int placeHolder = R.drawable.ic_placeholder_icon;
            if (BaseUtil.isValidUrl(imagePath)) {
                getPicasso().load(imagePath)
                        .resize(512,512)
                        .placeholder(placeHolder)
                        .into(ivIcon);
            } else {
                ivIcon.setImageResource(placeHolder);
            }
        }
    }

    public SpannableString getPrice(int price, int discountPrice) {
        SpannableString spannable;
        if(discountPrice >= price){
            spannable = new SpannableString("MRP : Rs." + price);
        }else {
            spannable = new SpannableString("MRP : Rs." + price + " Rs." + discountPrice);
            spannable.setSpan(new ForegroundColorSpan(Color.GRAY), 6, ((String.valueOf(price).length()) + 9), 0);
            spannable.setSpan(new StrikethroughSpan(), 6, ((String.valueOf(price).length()) + 9), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }
}
