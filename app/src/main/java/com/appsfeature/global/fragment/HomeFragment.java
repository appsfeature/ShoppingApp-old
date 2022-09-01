package com.appsfeature.global.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.HomeAdapter;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.network.AppDataManager;
import com.appsfeature.global.util.AppPreference;
import com.appsfeature.global.util.ClassUtil;
import com.dynamic.fragment.base.DMBaseGenericFragment;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.util.DMProperty;
import com.dynamic.util.DMUtility;
import com.helper.callback.Response;
import com.helper.util.BaseUtil;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends DMBaseGenericFragment<DMProperty> {
    private View layoutNoData;
    private HomeAdapter adapter;
    private final List<CategoryModel> mList = new ArrayList<>();
    private Activity activity;
    private RecyclerView rvList;
    private SwipeRefreshLayout swipeRefresh;

    public static HomeFragment getInstance(int catId) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(DMUtility.getPropertyBundle(catId));
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        initView(view);
        loadData(property.getCatId(), AppPreference.getSeason());
        return view;
    }

    public void loadData(int catId, int seasonId) {
        getDataFromServer(catId, seasonId);
    }


    private void initView(View view) {
        layoutNoData = view.findViewById(R.id.ll_no_data);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        rvList = view.findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        adapter = new HomeAdapter(activity, mList, new DynamicCallback.OnClickListener<CategoryModel, ContentModel>() {
            @Override
            public void onItemClicked(View v, CategoryModel category, ContentModel item) {
                openItemOnClicked(v, category, item);
            }
        });
        rvList.setAdapter(adapter);

        if(swipeRefresh != null) {
            showProgress(false);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData(AppPreference.getGender(), AppPreference.getSeason());
                }
            });
        }
    }

    private void openItemOnClicked(View view, CategoryModel category, ContentModel item) {
        ClassUtil.onItemClicked(activity, property, category, item);
    }

    private void getDataFromServer(int genderId, int seasonId) {
        AppDataManager.get(activity).getAppDataUser(genderId, seasonId, new DynamicCallback.Listener<List<CategoryModel>>() {
            @Override
            public void onSuccess(List<CategoryModel> response) {
                showProgress(false);
                loadList(response);
            }

            @Override
            public void onValidate(List<CategoryModel> list, Response.Status<List<CategoryModel>> callback) {
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

    private void loadList(List<CategoryModel> list) {
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
