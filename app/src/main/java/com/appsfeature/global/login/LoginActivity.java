package com.appsfeature.global.login;


import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.appsfeature.global.R;


public class LoginActivity extends AppCompatActivity {

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFragment();
    }

    private void initFragment() {
        Runnable runnable = new Runnable() {
            public void run() {
                Fragment fragment = LoginFragment.newInstance();
//                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content, fragment);
                transaction.commitAllowingStateLoss();
            }
        };
        new Handler().post(runnable);
    }
}
