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
import com.appsfeature.global.adapter.NotificationAdapter;
import com.appsfeature.global.model.NotificationItem;
import com.appsfeature.global.onesignal.NotificationCacheManager;
import com.appsfeature.global.util.SupportUtil;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.BaseUtil;
import com.helper.widget.ItemDecorationCardMargin;

import java.util.ArrayList;
import java.util.List;


public class NotificationActivity extends AppCompatActivity {

    private View llNoData;
    private NotificationAdapter adapter;
    private final List<NotificationItem> mList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setUpToolBar("Notification");

        onInitializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        NotificationCacheManager.getNotificationList(this, new Response.Status<List<NotificationItem>>() {
            @Override
            public void onSuccess(List<NotificationItem> response) {
                onUpdateUI(response);
            }
        });
    }


    public void onInitializeUI() {
        llNoData = findViewById(R.id.ll_no_data);
        RecyclerView rvList = findViewById(R.id.recycler_view);
        rvList.addItemDecoration(new ItemDecorationCardMargin(this));
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new NotificationAdapter(mList, new Response.OnListClickListener<NotificationItem>() {
            @Override
            public void onItemClicked(View view, NotificationItem item) {
                NotificationCacheManager.updateReadStatus(NotificationActivity.this, item.getUuid());
                SupportUtil.onNotificationClicked(NotificationActivity.this, item);
            }

            @Override
            public void onDeleteClicked(View view, int position, NotificationItem item) {
                NotificationCacheManager.removeItem(NotificationActivity.this, item.getUuid(), new TaskRunner.Callback<Boolean>() {
                    @Override
                    public void onComplete(Boolean result) {
                        if (position >= 0 && position < mList.size()) {
                            mList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, mList.size());
                            if(mList.size() == 0){
                                BaseUtil.showNoData(llNoData, View.VISIBLE);
                            }
                        }
                    }
                });
            }
        });
        rvList.setAdapter(adapter);
    }

    public void onUpdateUI(List<NotificationItem> response) {
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
