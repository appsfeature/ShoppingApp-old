package com.appsfeature.global.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.listeners.AppCallback;
import com.appsfeature.global.model.ExtraProperty;
import com.appsfeature.global.viewmodel.AppViewModel;
import com.dynamic.activity.DMBaseActivity;
import com.dynamic.activity.DMBaseGenericActivity;
import com.dynamic.fragment.base.DMBaseGenericFragment;


public abstract class BaseActivity extends DMBaseGenericActivity<ExtraProperty> implements AppCallback.View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(property !=null && !TextUtils.isEmpty(property.getTitle())){
            setUpToolBar(property.getTitle());
        }
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
