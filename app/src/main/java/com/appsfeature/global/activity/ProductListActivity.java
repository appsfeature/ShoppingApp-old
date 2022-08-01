package com.appsfeature.global.activity;


import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsfeature.global.R;
import com.appsfeature.global.fragment.ProductListFragment;
import com.appsfeature.global.model.PresenterModel;


public class ProductListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_activity_frag_holder);
        initFragment();
    }

    private void initFragment() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Runnable runnable = new Runnable() {
                public void run() {
                    Fragment fragment = new ProductListFragment();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.content, fragment);
                    transaction.commitAllowingStateLoss();
                }
            };
            new Handler().post(runnable);
        } else {
            Toast.makeText(this, "Invalid bundle", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onUpdateUI(PresenterModel response) {

    }

    @Override
    public void onErrorOccurred(Exception e) {

    }

    @Override
    public void onStartProgressBar() {

    }

    @Override
    public void onStopProgressBar() {

    }
}
