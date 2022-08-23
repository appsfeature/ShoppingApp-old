package com.appsfeature.global.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appsfeature.global.adapter.app.ProductAdapter;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.ExtraProperty;
import com.appsfeature.global.network.AppDataManager;
import com.appsfeature.global.util.ClassUtil;
import com.dynamic.R;
import com.dynamic.fragment.base.DMBaseGenericFragment;
import com.dynamic.listeners.DynamicCallback;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;
import com.helper.widget.RecyclerViewCardMarginGrid;

import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends DMBaseGenericFragment<ExtraProperty> {
    private View layoutNoData;
    private ProductAdapter adapter;
    private final List<ContentModel> mList = new ArrayList<>();
    private Activity activity;
    private RecyclerView rvList;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dm_fragment_dynamic, container, false);
        activity = getActivity();
        initView(view);
        loadData();
        return view;
    }

    private void loadData() {
        getDataFromServer();
    }


    private void initView(View view) {
        layoutNoData = view.findViewById(R.id.ll_no_data);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        rvList = view.findViewById(R.id.recycler_view);
        rvList.addItemDecoration(new RecyclerViewCardMarginGrid(2, 16, true));
        rvList.setLayoutManager(new GridLayoutManager(activity, 2));
        adapter = new ProductAdapter(activity, mList, new Response.OnClickListener<ContentModel>() {
            @Override
            public void onItemClicked(View view, ContentModel item) {
                openItemOnClicked(view, item);
            }
        });
        rvList.setAdapter(adapter);

        if(swipeRefresh != null) {
            showProgress(false);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });
        }
    }

    private void openItemOnClicked(View view, ContentModel item) {
        ClassUtil.openActivityProductView(activity, property, item);
    }

    private void getDataFromServer() {
        AppDataManager.get(activity).getAppProductBySubCategory(property.getParentId(), property.getCatId(), new DynamicCallback.Listener<List<ContentModel>>() {
            @Override
            public void onSuccess(List<ContentModel> response) {
                showProgress(false);
                loadList(response);
            }

            @Override
            public void onValidate(List<ContentModel> list, Response.Status<List<ContentModel>> callback) {
                DynamicCallback.Listener.super.onValidate(list, callback);
            }

            @Override
            public void onFailure(Exception e) {
                showProgress(false);
                if(mList.size() == 0) {
                    BaseUtil.showNoData(layoutNoData, View.VISIBLE);
                }
            }

            @Override
            public void onRequestCompleted() {
                showProgress(false);
            }
        });
    }

    private void loadList(List<ContentModel> list) {
        rvList.setVisibility(View.VISIBLE);
        BaseUtil.showNoData(layoutNoData, View.GONE);
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        if (list == null || list.size() <= 0) {
            BaseUtil.showNoData(layoutNoData, View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    protected void showProgress(boolean isShow) {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(isShow);
        }
    }
}
