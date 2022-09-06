package com.appsfeature.global.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.model.FilterModel;

import java.util.List;

public class FilterChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int mItemType;
    private final List<FilterModel> mList;

    public FilterChildAdapter(int mItemType, List<FilterModel> mList) {
        this.mItemType = mItemType;
        this.mList = mList;
    }

    public interface ItemType {
        int ITEM_TYPE_GRID = 1;
        int ITEM_TYPE_LIST = 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mItemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == ItemType.ITEM_TYPE_GRID) {
            return new GridViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slot_item_filter, viewGroup, false));
        } else {
            return new ListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slot_item_filter, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        FilterModel item = mList.get(position);
        if (viewHolder instanceof ListViewHolder) {
            ListViewHolder holder = (ListViewHolder) viewHolder;
            holder.tvName.setText(item.getTitle());
            holder.cbCheckBox.setChecked(item.isChecked());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvName;
        private final CheckBox cbCheckBox;

        private ListViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_title);
            cbCheckBox = v.findViewById(R.id.cb_check_box);
            itemView.setOnClickListener(this);
            cbCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    mList.get(getAbsoluteAdapterPosition()).setChecked(isChecked);
                }
            });
        }

        @Override
        public void onClick(View v) {
            updateCheckedStatus(getAbsoluteAdapterPosition());
        }
    }

    private void updateCheckedStatus(int position) {
        mList.get(position).setChecked(!mList.get(position).isChecked());
        notifyItemChanged(position);
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;

        GridViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_title);
        }
    }
}