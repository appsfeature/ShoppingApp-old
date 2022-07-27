package com.appsfeature.global.activity;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.CommonAdapter;
import com.appsfeature.global.model.CommonModel;
import com.appsfeature.global.model.PresenterModel;
import com.appsfeature.global.util.ClassUtil;
import com.appsfeature.global.util.SupportUtil;
import com.helper.callback.Response;

import java.util.ArrayList;
import java.util.List;


public class CommonListActivity extends BaseActivity {

    private View llNoData;
    private CommonAdapter adapter;
    private final List<CommonModel> mList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        onInitializeUI();
        setUpToolBar("All Doctors");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getExtraProperty() != null) {
            String userId = "";
            appPresenter.getCommonData(userId);
        }
    }

    public void onInitializeUI() {
        llNoData = findViewById(R.id.ll_no_data);
        RecyclerView rvList = findViewById(R.id.recycler_view);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CommonAdapter(mList, new Response.OnClickListener<CommonModel>() {
            @Override
            public void onItemClicked(View view, CommonModel item) {
//                ClassUtil.openActivity(CommonListActivity.this, item);
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public void onUpdateUI(PresenterModel response) {
        SupportUtil.showNoData(llNoData, View.GONE);
        llNoData.setVisibility(View.GONE);
        mList.clear();
        if (response.getCommonList() != null && response.getCommonList().size() > 0) {
            mList.addAll(response.getCommonList());
        } else {
            SupportUtil.showNoData(llNoData, View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorOccurred(Exception e) {
        SupportUtil.showToast(this, e.getMessage());
        SupportUtil.showNoData(llNoData, View.VISIBLE);
    }

    @Override
    public void onStartProgressBar() {
        SupportUtil.showNoDataProgress(llNoData);
    }

    @Override
    public void onStopProgressBar() {

    }
}
