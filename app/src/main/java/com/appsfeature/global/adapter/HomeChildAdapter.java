package com.appsfeature.global.adapter;


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
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.dynamic.adapter.BaseDynamicChildAdapter;
import com.dynamic.adapter.holder.base.BaseCommonHolder;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeChildAdapter extends BaseDynamicChildAdapter<CategoryModel, ContentModel> {


    public HomeChildAdapter(Context context, int itemType, CategoryModel category, List<ContentModel> mList, DynamicCallback.OnClickListener<CategoryModel, ContentModel> clickListener) {
        super(context, itemType, category, mList, clickListener);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        if(viewType == CategoryType.TYPE_VIDEO){
            return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_video_play_list, parent, false));
        }else if(viewType == CategoryType.TYPE_SUB_CATEGORY){
            return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_sub_category, parent, false));
        }else {
            return new CommonChildHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_slot_list_card_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolderDynamic(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VideoViewHolder) {
            VideoViewHolder holder = (VideoViewHolder) viewHolder;
            holder.setData(mList.get(position), position);
        } else if (viewHolder instanceof CommonChildHolder) {// this viewHolder is always on the bottom
            CommonChildHolder<ContentModel> holder = (CommonChildHolder) viewHolder;
            holder.setData(mList.get(position), position);
        }
    }

    public class AppViewHolder extends BaseCommonHolder<CategoryModel> implements View.OnClickListener{
        protected final ImageView ivImage;
        protected final TextView tvTitle;
        protected final Button btnLogin;

        AppViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.iv_image);
            tvTitle = view.findViewById(R.id.tv_title);
            btnLogin = view.findViewById(R.id.btn_login);
            btnLogin.setOnClickListener(this);
        }
        public void setData(DMContent item, int position) {
            ivImage.setImageResource(item.getVisibility());
            tvTitle.setText(item.getTitle());
            btnLogin.setText(item.getDescription());
        }

        @Override
        public void onClick(View view) {
            if (getAbsoluteAdapterPosition() >= 0 && getAbsoluteAdapterPosition() < mList.size()) {
                clickListener.onItemClicked(view, category, mList.get(getAbsoluteAdapterPosition()));
            }
        }
    }

    public class VideoViewHolder extends CommonChildHolder<ContentModel> implements View.OnClickListener {
        public final TextView tvWatchTime;
        public final ProgressBar progressBar;

        public VideoViewHolder(View v) {
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
                        .placeholder(R.drawable.ic_yt_placeholder)
                        .error(R.drawable.ic_yt_placeholder)
                        .into(ivIcon);
                ivIcon.setVisibility(View.VISIBLE);
            } else {
                if (TextUtils.isEmpty(item.getImage())) {
                    ivIcon.setImageResource(R.drawable.ic_yt_placeholder);
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

    private boolean isMediumVideoPlaceholderQuality = false;

    public String getYoutubePlaceholderImage(String videoId) {
        String quality = isMediumVideoPlaceholderQuality ? "mqdefault.jpg" : "maxresdefault.jpg";
        return "https://i.ytimg.com/vi/" + videoId + "/" + quality;
    }
}