package com.appsfeature.global.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.HomeChildAdapter;
import com.appsfeature.global.adapter.app.NotificationAdapter;
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.NotificationItem;
import com.appsfeature.global.onesignal.NotificationCacheManager;
import com.appsfeature.global.util.AppCartMaintainer;
import com.appsfeature.global.util.SupportUtil;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.listeners.DMContentType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.BaseUtil;
import com.helper.widget.ItemDecorationCardMargin;

import java.util.ArrayList;
import java.util.List;


public class CartActivity extends AppCompatActivity {

    private View llNoData;
    private HomeChildAdapter adapter;
    private final List<ContentModel> mList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setUpToolBar("My Cart");

        onInitializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        AppCartMaintainer.getCartList(this, new Response.Status<List<ContentModel>>() {
            @Override
            public void onSuccess(List<ContentModel> response) {
                onUpdateUI(response);
            }
        });
    }


    public void onInitializeUI() {
        llNoData = findViewById(R.id.ll_no_data);
        RecyclerView rvList = findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new HomeChildAdapter(this, CategoryType.TYPE_CART_VIEW, new CategoryModel(), mList, new DynamicCallback.OnClickListener<CategoryModel, ContentModel>() {
            @Override
            public void onItemClicked(View v, CategoryModel category, ContentModel item) {

            }
        });
        rvList.setAdapter(adapter);
    }

    public void onUpdateUI(List<ContentModel> response) {
        SupportUtil.showNoData(llNoData, View.GONE);
        llNoData.setVisibility(View.GONE);
        mList.clear();
        if (response!= null && response.size() > 0) {
            mList.addAll(response);
        } else {
            SupportUtil.showNoData(llNoData, View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }


    protected void setUpToolBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (!TextUtils.isEmpty(title)) {
                actionBar.setTitle(title);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home ){
            onBackPressed();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}