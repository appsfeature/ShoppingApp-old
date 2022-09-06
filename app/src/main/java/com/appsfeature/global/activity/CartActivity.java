package com.appsfeature.global.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.HomeChildAdapter;
import com.appsfeature.global.adapter.app.NotificationAdapter;
import com.appsfeature.global.adapter.holder.CartViewHolder;
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.model.CartModel;
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
    private View llPriceDetail;
    private TextView tvPrice, tvDiscount, tvDelivery, tvTotal;

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
        AppCartMaintainer.getCartList(this, new Response.Status<CartModel>() {
            @Override
            public void onSuccess(CartModel response) {
                if(response != null) {
                    onUpdateUI(response.getProducts());
                    updatePriceDetails(response);
                }else {
                    showNoDataView();
                }
            }
        });
    }


    public void onInitializeUI() {
        llNoData = findViewById(R.id.ll_no_data);
        tvPrice = findViewById(R.id.tv_price);
        tvDiscount = findViewById(R.id.tv_discount);
        tvDelivery = findViewById(R.id.tv_delivery);
        tvTotal = findViewById(R.id.tv_total);
        llPriceDetail = findViewById(R.id.ll_price_detail);
        RecyclerView rvList = findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new HomeChildAdapter(this, CategoryType.TYPE_CART_VIEW, new CategoryModel(), mList, new DynamicCallback.OnClickListener<CategoryModel, ContentModel>() {
            @Override
            public void onItemClicked(View v, CategoryModel category, ContentModel item) {

            }
        });
        adapter.setRemoveListener(new CartViewHolder.RemoveListener(){

            @Override
            public void onItemRemove(int position) {
                showNoDataView();
            }

            @Override
            public void onItemSaveForLater(int position) {

            }
        });
        rvList.setAdapter(adapter);
    }

    private void showNoDataView() {
        if(mList.size() <= 0){
            BaseUtil.showNoData(llNoData, "Cart is Empty!", View.VISIBLE);
            llPriceDetail.setVisibility(View.GONE);
        }
    }

    public void onUpdateUI(List<ContentModel> response) {
        SupportUtil.showNoData(llNoData, View.GONE);
        llNoData.setVisibility(View.GONE);
        mList.clear();
        if (response!= null && response.size() > 0) {
            mList.addAll(response);
            llPriceDetail.setVisibility(View.VISIBLE);
        } else {
            showNoDataView();
        }
        adapter.notifyDataSetChanged();
    }

    private void updatePriceDetails(CartModel response) {
        tvPrice.setText(getString(R.string.price_rs, response.getPrice()));
        tvDiscount.setText(getString(R.string.price_rs, response.getDiscount()));
        tvDelivery.setText(getString(R.string.price_rs, response.getDelivery()));
        tvTotal.setText(getString(R.string.price_rs, response.getTotal()));
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
