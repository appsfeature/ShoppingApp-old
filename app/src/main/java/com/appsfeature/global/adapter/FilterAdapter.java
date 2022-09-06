package com.appsfeature.global.adapter;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.model.FilterModel;

import java.util.List;


public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<FilterModel> mList;

    public FilterAdapter(List<FilterModel> mList) {
        this.mList = mList;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_filter_list, parent, false));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder myViewHolder, int position) {
        FilterModel item = mList.get(position);
        ViewHolder holder = (ViewHolder) myViewHolder;
        holder.tvTitle.setText(item.getTitle());
        holder.setData(item, position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvTitle;
        private final RecyclerView recyclerView;

        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_title);
            recyclerView = view.findViewById(R.id.recycler_view);
        }

        public void setData(FilterModel item, int position) {
            if (recyclerView != null) {
                if (item.getAttributesIds() != null && item.getAttributesIds().size() > 0) {
                    FilterChildAdapter adapter = new FilterChildAdapter(FilterChildAdapter.ItemType.ITEM_TYPE_LIST, item.getAttributesIds());
                    recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }
    }

}
