package com.appsfeature.global.adapter;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.holder.CartViewHolder;
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.util.AppCartMaintainer;
import com.appsfeature.global.util.CircleTransform;
import com.dynamic.adapter.BaseDynamicChildAdapter;
import com.dynamic.adapter.holder.base.BaseCommonHolder;
import com.dynamic.listeners.DMContentType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeChildAdapter extends BaseDynamicChildAdapter<CategoryModel, ContentModel> implements CartViewHolder.RemoveListener {

    private final Activity activity;

    public HomeChildAdapter(Activity context, int itemType, CategoryModel category, List<ContentModel> mList, DynamicCallback.OnClickListener<CategoryModel, ContentModel> clickListener) {
        super(context, itemType, category, mList, clickListener);
        this.activity = context;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        if(viewType == CategoryType.TYPE_VIDEO_PRODUCT){
            return new VideoProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_video_play_list, parent, false));
        }else if(viewType == CategoryType.TYPE_SUB_CATEGORY){
            return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_sub_category, parent, false));
        }else if(viewType == CategoryType.TYPE_PRODUCT){
            return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_home_product, parent, false));
        } else if(viewType == CategoryType.TYPE_CART_VIEW){
            return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_cart_view, parent, false), this);
        } else if(viewType == CategoryType.TYPE_PRODUCT_DETAIL){
            return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_slider_product, parent, false));
        } else {
            return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_card_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolderDynamic(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VideoProductViewHolder) {
            VideoProductViewHolder holder = (VideoProductViewHolder) viewHolder;
            holder.setData(mList.get(position), position);
        } else if (viewHolder instanceof ProductHolder) {
            ProductHolder holder = (ProductHolder) viewHolder;
            holder.setData(mList.get(position), position);
        } else if (viewHolder instanceof CartViewHolder) {
            CartViewHolder holder = (CartViewHolder) viewHolder;
            holder.setData(activity, mList.get(position), imageUrl);
        } else if (viewHolder instanceof CommonChildHolder) {// this viewHolder is always on the bottom
            CommonChildHolder<ContentModel> holder = (CommonChildHolder) viewHolder;
            holder.setData(mList.get(position), position);
        }
    }

    @Override
    public void onItemRemove(int position) {
        AppCartMaintainer.removeCartItem(context, mList.get(position).getTitle(), new Response.Status<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                mList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, mList.size());
            }
        });
    }

    @Override
    public void onItemSaveForLater(int position) {

    }

    public class ProductHolder extends BaseCommonHolder<CategoryModel> implements View.OnClickListener{
        protected final ImageView ivIcon;

        ProductHolder(View view) {
            super(view);
            ivIcon = view.findViewById(R.id.iv_icon);
            itemView.setOnClickListener(this);
        }
        public void setData(DMContent item, int position) {
            tvTitle.setText(item.getTitle());

            if (ivIcon != null) {
                String imagePath = getImageUrlFromJson(imageUrl, item.getImage());
                int placeHolder = getPlaceHolder();
                if (BaseUtil.isValidUrl(imagePath)) {
                    Picasso.get().load(imagePath)
                            .transform(new CircleTransform())
                            .placeholder(placeHolder)
                            .into(ivIcon);
                } else {
                    ivIcon.setImageResource(placeHolder);
                }
            }
        }

        private String getImageUrlFromJson(String imageUrl, String appImage) {
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

        @Override
        public void onClick(View view) {
            if (getAbsoluteAdapterPosition() >= 0 && getAbsoluteAdapterPosition() < mList.size()) {
                clickListener.onItemClicked(view, category, mList.get(getAbsoluteAdapterPosition()));
            }
        }
    }

    public class VideoProductViewHolder extends CommonChildHolder<ContentModel> implements View.OnClickListener {
        public final TextView tvWatchTime;
        public final ProgressBar progressBar;

        public VideoProductViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view);
            ivIcon = v.findViewById(R.id.iv_icon);
            tvWatchTime = v.findViewById(R.id.watch_time);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }

        public void setData(ContentModel item, int pos) {
            super.setData(item, pos);
            if (!TextUtils.isEmpty(item.getVideoUrl())) {
                String videoPreviewUrl = getYoutubePlaceholderImage(getVideoIdFromUrl(item.getVideoUrl()));
                Picasso.get().load(videoPreviewUrl)
                        .placeholder(R.drawable.play_logo_screenshot)
                        .error(R.drawable.play_logo_screenshot)
                        .into(ivIcon);
                ivIcon.setVisibility(View.VISIBLE);
            } else {
                if (TextUtils.isEmpty(item.getImage())) {
                    ivIcon.setImageResource(R.drawable.play_logo_screenshot);
                }
            }
            if (cardView != null && cardView instanceof CardView) {
                ((CardView) cardView).setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), item.getVideoDuration() > 0 ? R.color.yt_color_video_watched : R.color.themeBackgroundCardColor));
            }
            if (item.getVideoDuration() > 0) {
                progressBar.setMax(item.getVideoDuration());
                progressBar.setProgress(item.getVideoTime());
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
//            if (item.getVideoTime() > 0 && !TextUtils.isEmpty(item.getVideoTimeFormatted())) {
//                viewHolder.tvWatchTime.setText("Watched: " + mList.get(i).getVideoTimeFormatted());
//                viewHolder.tvWatchTime.setVisibility(View.VISIBLE);
//            }else {
//                viewHolder.tvWatchTime.setVisibility(View.GONE);
//            }
        }
    }
}