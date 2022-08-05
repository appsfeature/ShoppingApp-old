package com.appsfeature.global.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.AppDesign;
import com.appsfeature.global.R;
import com.appsfeature.global.dialog.AppDialog;
import com.appsfeature.global.listeners.GenderType;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.onesignal.NotificationReceivedCallback;
import com.appsfeature.global.onesignal.NotificationCacheManager;
import com.appsfeature.global.util.AppPreference;
import com.appsfeature.global.util.ClassUtil;
import com.appsfeature.global.util.DynamicUrlCreator;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMProperty;
import com.helper.callback.Response;
import com.helper.util.BaseDynamicUrlCreator;

import java.util.Locale;

public class MainActivity extends BaseInAppUpdateImmediateActivity implements DynamicCallback.OnDynamicListListener, NotificationReceivedCallback {

    private AppDesign appDesign;
    private TextView tvCartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDesign = AppDesign.get(this, false);
        setupToolbar();
        initView();

        onNewIntent(getIntent());
        appDesign.attachHomeFragment();
        registerNotificationReceiveHandler();
    }

    private void registerNotificationReceiveHandler() {
        AppApplication.getInstance().registerForNotificationCallBack(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_nav_drawer);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.app_name));
            }
        }
    }

    private void initView() {
    }

    @Override
    public void onItemClicked(Activity activity, View view, DMProperty parent, DMContent item) {
        ClassUtil.onItemClicked(this, parent, new CategoryModel(), item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (appDesign != null) {
            appDesign.onStart();
        }
    }

    private MenuItem menuItemFilter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menuItemFilter = menu.findItem(R.id.menu_item_filter);
        final MenuItem menuItemCart = menu.findItem(R.id.menu_item_cart);
        View view = menuItemCart.getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItemCart);
            }
        });
        tvCartCount = view.findViewById(R.id.tv_menu_cart);
        updateMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_filter) {
            AppDialog.openFilterHome(this, new Response.Status<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    appDesign.reloadHomeData();
                    updateMenuItems();
                }
            });
        } else if (item.getItemId() == R.id.menu_item_cart) {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        } else if (item.getItemId() == android.R.id.home) {
            if (appDesign != null && appDesign.drawerLayout != null) {
                appDesign.drawerLayout.openDrawer(GravityCompat.END);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            setIntent(intent);
            if (DynamicUrlCreator.isValidIntent(this)) {
                registerDynamicLinks();
            } else {
                if (getIntent() != null && getIntent().getData() != null) {
                    DynamicUrlCreator.openActivity(this, getIntent().getData(), null);
                }
            }
        }
    }

    private void registerDynamicLinks() {
        new DynamicUrlCreator(this)
                .register(new BaseDynamicUrlCreator.DynamicUrlResult() {
                    @Override
                    public void onDynamicUrlResult(Uri uri, String extraData) {
                        DynamicUrlCreator.openActivity(MainActivity.this, uri, extraData);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("PDFDynamicShare", "onError" + e.toString());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMenuItems();
    }

    @Override
    public void updateMenuItems() {
        if (tvCartCount != null) {
            NotificationCacheManager.getNotificationUnReadCount(this, new Response.Status<Integer>() {
                @Override
                public void onSuccess(Integer count) {
                    if (count > 0) {
                        tvCartCount.setText(String.format(Locale.ENGLISH, "%d", count));
                        tvCartCount.setVisibility(View.VISIBLE);
                    } else {
                        tvCartCount.setVisibility(View.GONE);
                    }
                }
            });
        }
        if(menuItemFilter != null){
            if(AppPreference.getGender() == GenderType.TYPE_GIRL){
                menuItemFilter.setIcon(R.drawable.ic_menu_female);
            }else {
                menuItemFilter.setIcon(R.drawable.ic_menu_male);
            }
        }
    }
}
