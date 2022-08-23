package com.appsfeature.global.adapter.app;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.model.SizeModel;
import com.helper.callback.Response;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Response.OnClickListener<SizeModel> clickListener;
    private final ArrayList<SizeModel> mList;

    public SizeAdapter(Context context, ArrayList<SizeModel> mList, Response.OnClickListener<SizeModel> clickListener) {
        this.mList = mList;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_size, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.tvTitle.setText(mList.get(i).getSize()+"");

        if(mList.get(i).isChecked()){
            holder.tvTitle.setBackgroundResource(R.drawable.bg_shape_size_selected);
            holder.tvTitle.setTextColor(Color.WHITE);
        }else {
            holder.tvTitle.setBackgroundResource(R.drawable.bg_shape_size);
            holder.tvTitle.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.themeTextColor));
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;

        private ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAbsoluteAdapterPosition() >= 0 && getAbsoluteAdapterPosition() < mList.size()) {
                updateCheckedStatus(getAbsoluteAdapterPosition());
                clickListener.onItemClicked(v, mList.get(getAbsoluteAdapterPosition()));
            }
        }

        private void updateCheckedStatus(int position) {
            resetList();
            mList.get(position).setChecked(true);
            notifyItemChanged(position);
        }

        private void resetList() {
            for (SizeModel item : mList){
                item.setChecked(false);
            }
            notifyItemRangeChanged(0, mList.size());
        }
    }
}