package com.appsfeature.global.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.dynamic.adapter.BaseDynamicAdapter;
import com.dynamic.adapter.holder.base.BaseCommonHolder;
import com.dynamic.listeners.DMFlingType;
import com.dynamic.listeners.DynamicCallback;

import java.util.List;

public class HomeAdapter extends BaseDynamicAdapter<CategoryModel, ContentModel> {

    private final Activity activity;

    public HomeAdapter(Activity context, List<CategoryModel> mList, DynamicCallback.OnClickListener<CategoryModel, ContentModel> listener) {
        super(context, mList, DMFlingType.None, listener);
        this.activity = context;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderDynamic(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CategoryType.TYPE_VIDEO_PRODUCT:
                return new GridViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_parent_slot_list, parent, false), 2);
            case CategoryType.TYPE_SUB_CATEGORY:
            case CategoryType.TYPE_PRODUCT:
                return new HorizontalCardScrollHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_parent_slot_list, parent, false));
            default:
                return new CommonHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.dm_parent_slot_list, parent, false));
        }
    }

    @Override
    protected void onBindViewHolderDynamic(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CategoryModel item = mList.get(position);
        if (viewHolder instanceof GridViewHolder) {
            GridViewHolder holder = (GridViewHolder) viewHolder;
            holder.setData(item, position);
        } else if (viewHolder instanceof CommonHolder) {// this viewHolder is always on the bottom
            CommonHolder<CategoryModel, ContentModel> holder = (CommonHolder) viewHolder;
            holder.setData(item, position);
        }
    }

    @Override
    protected <T1, T2> RecyclerView.Adapter<RecyclerView.ViewHolder> getDynamicChildAdapter(int itemType, T1 category, List<T2> childList) {
        return new HomeChildAdapter(activity, itemType, (CategoryModel) category, (List<ContentModel>) childList, listener);
    }


    public class GridViewHolder extends BaseCommonHolder<CategoryModel> {
        private final ImageView ivImage;
        private final int gridSize;

        GridViewHolder(View view, int gridSize) {
            super(view);
            this.gridSize = gridSize;
            ivImage = view.findViewById(R.id.iv_image);
        }

        public void setData(CategoryModel item, int position) {
            if (recyclerView != null) {
                if (item.getChildList() != null && item.getChildList().size() > 0) {
                    adapter = getDynamicChildAdapter(item.getItemType(), item, item.getChildList());
                    recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), gridSize));
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