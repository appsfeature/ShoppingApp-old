package com.appsfeature.global;

import android.app.Activity;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.appsfeature.global.fragment.HomeFragment;
import com.appsfeature.global.listeners.GenderType;
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
    private final boolean isAddBottomBar;
    private BottomNavigationView mBottomNavigationView;
    public DrawerLayout drawerLayout;
    private MenuItem navLogout;
    private Toolbar toolbar;
    private MenuItem menuProfile;

    public static AppDesign get(AppCompatActivity activity) {
        return AppDesign.get(activity, true);
    }
    public static AppDesign get(AppCompatActivity activity, boolean isAddBottomBar) {
        return new AppDesign(activity, isAddBottomBar);
    }

    private AppDesign(AppCompatActivity activity, boolean isAddBottomBar) {
        this.activity = activity;
        this.isAddBottomBar = isAddBottomBar;
        this.initUi(activity);
    }

    private void initUi(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        toolbar = rootView.findViewById(R.id.toolbar);
        setupSideBarNavigation(rootView);
        setupBottomNavigation(rootView);
    }

    private void setupSideBarNavigation(View rootView) {
        NavigationView navigationView = rootView.findViewById(R.id.nav_view);
        drawerLayout = rootView.findViewById(R.id.drawer_layout);

        menuProfile = navigationView.getMenu().findItem(R.id.nav_login);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if (drawerLayout != null) {
                    drawerLayout.closeDrawers();
                }
//              String s = SharedPrefUtil.getString(AppConstant.SharedPref.PLAYER_ID);
                int itemId = item.getItemId();
                if (itemId == R.id.nav_login) {
                    ClassUtil.openLoginActivity(activity);
                } else if (itemId == R.id.nav_women) {
                    AppPreference.setGender(GenderType.TYPE_GIRL);
                    reloadHomeData();
                } else if (itemId == R.id.nav_mens) {
                    AppPreference.setGender(GenderType.TYPE_BOY);
                    reloadHomeData();
                } else if (itemId == R.id.nav_share) {
                    SupportUtil.share(activity, "");
                } else if (itemId == R.id.nav_rate_us) {
                    SocialUtil.rateUs(activity);
                } else if (itemId == R.id.nav_more_apps) {
                    SocialUtil.moreApps(activity, AppConstant.DEVELOPER_NAME);
                } else if (itemId == R.id.nav_logout) {
                    AppPreference.clearPreferences(activity);
                    activity.finish();
                    ClassUtil.openHomeActivity(activity);
                }
                return true;
            }
        });
        navLogout = navigationView.getMenu().findItem(R.id.nav_logout);
    }

    private void setupBottomNavigation(View rootView) {
        mBottomNavigationView = rootView.findViewById(R.id.navigation);
        if (mBottomNavigationView != null) {
            if(isAddBottomBar) {
//            mBottomNavigationView.setItemIconTintList(null);
//            mBottomNavigationView.setItemTextColor(SupportUtil.getNavColor(this));
                mBottomNavigationView.setVisibility(View.VISIBLE);
                mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
                        updateTitle(item.getTitle().toString());
                        if (itemId == R.id.menu_1) {
                            fragmentMapping(getDynamicFragment(AppValues.DASHBOARD_ID));
//                        fragmentMapping(getDynamicFragment(AppValues.DASHBOARD_DEMO1));
                            return true;
                        } else if (itemId == R.id.menu_2) {
                            fragmentMapping(getDynamicFragment(AppValues.DASHBOARD_DEMO1));
                            return true;
                        } else if (itemId == R.id.menu_3) {
                            fragmentMapping(getDynamicFragment(AppValues.DASHBOARD_BLOGGER));
                            return true;
                        } else if (itemId == R.id.menu_4) {
                            fragmentMapping(getDynamicFragment(AppValues.DASHBOARD_DEMO2));
                            return true;
                        }
                        return false;
                    }
                });
            }else {
                mBottomNavigationView.setVisibility(View.GONE);
            }
        }
    }

    private void updateTitle(String study) {
        if (toolbar != null) {
            toolbar.setTitle(study);
        }
    }

    public void attachHomeFragment() {
        if(isAddBottomBar){
            setBottomNavigationViewToHome();
        }else {
            fragmentMapping(getDynamicFragment(AppPreference.getGender()));
        }
    }

    private HomeFragment homeFragment;

    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    private HomeFragment getDynamicFragment(int catId) {
        if(homeFragment == null) homeFragment = HomeFragment.getInstance(catId);
        return homeFragment;
    }

    private void fragmentMapping(Fragment fragment) {
        if (activity != null) {
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();
        }
    }

    private void setBottomNavigationViewToHome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBottomNavigationView != null) {
                    mBottomNavigationView.setSelectedItemId(R.id.menu_1);
                }
            }
        }, 100);
    }

    public void onStart() {
        handleLoginData();
    }

    private void handleLoginData() {
        if(AppPreference.isLoginCompleted()){
            if (navLogout != null) {
                navLogout.setVisible(true);
            }
            if (menuProfile != null) {
                menuProfile.setTitle("Profile");
            }
        }else {
            if (navLogout != null) {
                navLogout.setVisible(false);
            }
            if (menuProfile != null) {
                menuProfile.setTitle("Login");
            }
        }
    }

    public void reloadHomeData() {
        if(getHomeFragment() != null){
            getHomeFragment().loadData(AppPreference.getGender(), AppPreference.getSeason());
        }
    }
}
