package com.appsfeature.global.adapter.app;


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
import com.appsfeature.global.model.NotificationItem;
import com.helper.callback.Response;
import com.helper.model.common.BaseTimeViewHolder;
import com.helper.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Response.OnListClickListener<NotificationItem> clickListener;
    private final List<NotificationItem> mList;

    public NotificationAdapter(List<NotificationItem> mList, Response.OnListClickListener<NotificationItem> clickListener) {
        this.mList = mList;
        this.clickListener = clickListener;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_item_notification, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {
        ViewHolder myViewHolder = (ViewHolder) holder;
        myViewHolder.setData(mList.get(i));
    }

    private class ViewHolder extends BaseTimeViewHolder implements View.OnClickListener {

        private final TextView tvTitle, tvBody, tvDate;
        private final View cardView;
        private final ImageView ivIcon;

        private ViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view);
            ivIcon = v.findViewById(R.id.iv_icon);
            tvTitle = v.findViewById(R.id.tv_title);
            tvBody = v.findViewById(R.id.tv_body);
            tvDate = v.findViewById(R.id.tv_date);
            (v.findViewById(R.id.iv_delete)).setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.iv_delete){
                clickListener.onDeleteClicked(v, getAbsoluteAdapterPosition(), mList.get(getAbsoluteAdapterPosition()));
            }else {
                clickListener.onItemClicked(v, mList.get(getAbsoluteAdapterPosition()));
            }
        }

        public void setData(NotificationItem item) {
            tvTitle.setText(item.getTitle());
            tvBody.setText(item.getBody());
            if (!TextUtils.isEmpty(item.getUpdatedAt())) {
                tvDate.setText(getTimeInDaysAgoFormat(item.getUpdatedAt()));
            }
            if(item.isRead()){
                cardView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.themeDividerColor));
            }else {
                cardView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.themeBackgroundCardColor));
            }
            if(!TextUtils.isEmpty(item.getImageUrl()) && BaseUtil.isValidUrl(item.getImageUrl())) {
                Picasso.get().load(item.getImageUrl())
                        .placeholder(R.drawable.ic_dm_placeholder_slider)
                        .error(R.drawable.ic_dm_placeholder_slider)
                        .into(ivIcon);
                ivIcon.setVisibility(View.VISIBLE);
            } else {
                ivIcon.setVisibility(View.GONE);
            }
        }
    }

}
