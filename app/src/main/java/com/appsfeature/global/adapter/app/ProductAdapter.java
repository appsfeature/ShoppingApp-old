package com.appsfeature.global.adapter.app;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.holder.ProductViewHolder;
import com.appsfeature.global.model.ContentModel;
import com.dynamic.DynamicModule;
import com.helper.callback.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
 

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private final List<ContentModel> mList;
    private final Response.OnClickListener<ContentModel> mCallback;
    public final ArrayList<ContentModel> tempList;
    private final String imageUrl;
    private String searchText;

    public ProductAdapter(Context context, List<ContentModel> mList, Response.OnClickListener<ContentModel> mCallback) {
        this.mList = mList;
        this.mCallback = mCallback;
        this.tempList = new ArrayList<>(mList);
        this.imageUrl = DynamicModule.getInstance().getImageBaseUrl(context);
    }

    /**
     * @param list : update this list when new items added in main list.
     */
    public void updateFilterList(List<ContentModel> list) {
        tempList.clear();
        tempList.addAll(list);
    }
    /**
     * mRefreshListener : used for update no data TextView
     */
    private ListRefreshListener mRefreshListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slot_product, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ContentModel item = mList.get(position);
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.setData(item, imageUrl);
            highlightText(holder, item.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setSearchQuery(String searchText) {
        this.searchText = searchText.toLowerCase(Locale.getDefault());
    }

    public class ViewHolder extends ProductViewHolder implements View.OnClickListener {

        ViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(getAbsoluteAdapterPosition() >= 0 && getAbsoluteAdapterPosition() < mList.size()) {
                mCallback.onItemClicked(v, mList.get(getAbsoluteAdapterPosition()));
            }
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ContentModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tempList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ContentModel item : tempList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                mList.clear();
                notifyDataSetChanged();
                if (results.values != null) {
                    mList.addAll((List<ContentModel>) results.values);
                    notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(mRefreshListener != null){
                mRefreshListener.onListRefreshed(getItemCount());
            }
        }
    };

    /**
     * @param mRefreshListener : used for update no data TextView
     */
    public ProductAdapter addListRefreshListener(ListRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
        return this;
    }

    public interface ListRefreshListener{
        void onListRefreshed(int itemCount);
    }

    private void highlightText(ViewHolder holder, String title) {
        try {
            String mTitle = title.toLowerCase(Locale.getDefault());
            Spannable spanText = Spannable.Factory.getInstance()
                    .newSpannable(holder.tvTitle.getText()); // <- EDITED: Use the original string, as `country` has been converted to lowercase.
            if(searchText == null){
                return;
            }
            String[] searchWords = searchText.split(" ");
            for (String item : searchWords){
                if (mTitle.contains(item)) {
                    int startPos = mTitle.indexOf(item);
                    int endPos = startPos + item.length();

                    spanText.setSpan(
                            new ForegroundColorSpan(Color.parseColor("#E8970F")),
                            startPos,
                            endPos,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                    holder.tvTitle.setText(spanText, TextView.BufferType.SPANNABLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
