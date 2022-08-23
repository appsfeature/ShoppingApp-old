package com.appsfeature.global.adapter.app;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.model.ProductDetail;
import com.appsfeature.global.model.SizeModel;
import com.dynamic.DynamicModule;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Response.OnClickListener<ProductDetail> clickListener;
    private final ArrayList<ProductDetail> mList;
    private final String imageUrl;

    public ColorAdapter(Context context, ArrayList<ProductDetail> mList, Response.OnClickListener<ProductDetail> clickListener) {
        this.mList = mList;
        this.clickListener = clickListener;
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_color, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.tvTitle.setText(mList.get(i).getColor());
        String imagePath = getUrl(imageUrl, mList.get(i).getImages());
        if (BaseUtil.isValidUrl(imagePath)) {
            Picasso.get().load(imagePath)
                    .into(holder.ivIcon);
            holder.ivIcon.setVisibility(View.VISIBLE);
            holder.tvTitle.setVisibility(View.GONE);
        } else {
            holder.ivIcon.setVisibility(View.INVISIBLE);
//            if(mList.get(i).getColor() != null) {
//                String color = colorPlatte.get(mList.get(i).getColor().toLowerCase());
//                if (!TextUtils.isEmpty(color)) {
//                    holder.tvTitle.setTextColor(Color.parseColor(color));
////                    holder.tvTitle.setVisibility(View.GONE);
//                }
//            }
        }
        if(mList.size() == 1){
            mList.get(i).setChecked(true);
        }

        if(mList.get(i).isChecked()){
            holder.cardView.setBackgroundResource(R.drawable.bg_shape_color_selected);
        }else {
            holder.cardView.setBackgroundResource(R.drawable.bg_shape_color);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View cardView;
        private final TextView tvTitle;
        private final ImageView ivIcon;

        private ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view);
            tvTitle = v.findViewById(R.id.tv_title);
            ivIcon = v.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAbsoluteAdapterPosition() >= 0 && getAbsoluteAdapterPosition() < mList.size()) {
                updateCheckedStatus(getAbsoluteAdapterPosition());
                clickListener.onItemClicked(v, mList.get(getAbsoluteAdapterPosition()));
            }
        }
    }

    private void updateCheckedStatus(int position) {
        resetList();
        mList.get(position).setChecked(true);
        notifyItemChanged(position);
    }

    private void resetList() {
        for (ProductDetail item : mList){
            item.setChecked(false);
        }
        notifyItemRangeChanged(0, mList.size());
    }

    protected String getUrl(String imageUrl, String appImage) {
        if (TextUtils.isEmpty(appImage) || BaseUtil.isValidUrl(appImage)) {
            return appImage;
        }
        return imageUrl + appImage;
    }



    protected Map<String, String> colorPlatte  = new HashMap<String, String>() {{
        put("red", "#F70000");
        put("yellow", "#F7B31C");
        put("green", "#008140");
        put("blue", "#0057D9");
        put("orange", "#F76300");
        put("pink", "#F766AE");
        put("black", "#000000");
        put("gray", "#7C7C7C");
    }};
}