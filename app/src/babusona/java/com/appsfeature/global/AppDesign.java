package com.appsfeature.global;

import android.app.Activity;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.appsfeature.global.fragment.HomeDynamicFragment;
import com.appsfeature.global.util.AppConstant;
import com.appsfeature.global.util.AppPreference;
import com.appsfeature.global.util.ClassUtil;
import com.appsfeature.global.util.SupportUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.helper.util.SocialUtil;

public class AppDesign {
    private final AppCompatActivity activity;
    public DrawerLayout drawerLayout;
    private MenuItem navLogout;

    public static AppDesign get(AppCompatActivity activity) {
        return new AppDesign(activity);
    }

    private AppDesign(AppCompatActivity activity) {
        this.activity = activity;
        this.initUi(activity);
    }

    private void initUi(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        setupSideBarNavigation(rootView);
    }

    public void onStart() {
        handleLoginData();
    }

    private void setupSideBarNavigation(View rootView) {
        NavigationView navigationView = rootView.findViewById(R.id.nav_view);
        drawerLayout = rootView.findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if (drawerLayout != null) {
                    drawerLayout.closeDrawers();
                }
//              String s = SharedPrefUtil.getString(AppConstant.SharedPref.PLAYER_ID);
                int itemId = item.getItemId();
                if (itemId == R.id.nav_share) {
                    SupportUtil.share(activity, "");
                } else if (itemId == R.id.nav_rate_us) {
                    SocialUtil.rateUs(activity);
                } else if (itemId == R.id.nav_more_apps) {
                    SocialUtil.moreApps(activity, AppConstant.DEVELOPER_NAME);
                } else if (itemId == R.id.nav_logout) {
                    AppPreference.clearPreferences();
                    ClassUtil.openHomeActivity(activity);
                    activity.finish();
                }
                return true;
            }
        });
        navLogout = navigationView.getMenu().findItem(R.id.nav_logout);
    }

    public void attachHomeFragment() {
        fragmentMapping(HomeDynamicFragment.getInstance(AppValues.DASHBOARD_ID));
    }

    private void fragmentMapping(Fragment fragment) {
        if (activity != null) {
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();
        }
    }

    private void handleLoginData() {
        if (navLogout != null) {
            navLogout.setVisible(AppPreference.isLoginCompleted());
        }
    }
}
