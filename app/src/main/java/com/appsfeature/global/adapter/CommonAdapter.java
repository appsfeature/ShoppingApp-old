package com.appsfeature.global.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.model.CommonModel;
import com.helper.callback.Response;

import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Response.OnClickListener<CommonModel> clickListener;
    private final List<CommonModel> mList;

    public CommonAdapter(List<CommonModel> mList, Response.OnClickListener<CommonModel> clickListener) {
        this.mList = mList;
        this.clickListener = clickListener;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_item_selector, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {
        ViewHolder myViewHolder = (ViewHolder) holder;
        myViewHolder.tvName.setText(mList.get(i).getTitle());

//        if(mList.get(i).getProfilePicture() != null) {
//            Picasso.get().load(mList.get(i).getProfilePicture())
//                    .placeholder(R.drawable.ic_user_profile)
//                    .error(R.drawable.ic_user_profile)
//                    .into(myViewHolder.ivPic);
//        } else {
//            myViewHolder.ivPic.setImageResource(R.drawable.ic_user_profile);
//        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvName;

        private ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClicked(v, mList.get(getAbsoluteAdapterPosition()));
        }
    }

}
