package com.appsfeature.global;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.appsfeature.global.login.LoginListener;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.onesignal.NotificationWillShowInForegroundHandler;
import com.appsfeature.global.onesignal.NotificationReceivedCallback;
import com.appsfeature.global.onesignal.OneSignalOpenHandler;
import com.appsfeature.global.util.AppConstant;
import com.appsfeature.global.util.ClassUtil;
import com.appsfeature.global.util.SupportUtil;
import com.browser.BrowserSdk;
import com.dynamic.DynamicModule;
import com.dynamic.listeners.ApiHost;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMProperty;
import com.formbuilder.FormBuilder;
import com.formbuilder.model.FormLocationProperties;
import com.helper.application.BaseApplication;
import com.helper.callback.ActivityLifecycleListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;

public class AppApplication extends BaseApplication {

    private static AppApplication instance;
    private LoginListener mLoginListener;

    public static AppApplication getInstance() {
        return instance;
    }
    public ViewModelProvider.Factory viewModelFactory = new ViewModelProvider.AndroidViewModelFactory(this);

    public ViewModelProvider.Factory getViewModelFactory() {
        return viewModelFactory;
    }

    @Override
    public boolean isDebugMode() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("OneSignalExample", "AppApplication.onCreate");
        instance = this;
        BrowserSdk.getInstance()
                .setEnableInternetErrorViewOnly(getInstance(), true)
                .setDebugMode(isDebugMode());
//        getLoginSDK();
        if(AppValues.isEnableNotification) {
            initOneSignal();
        }

        DynamicModule.getInstance()
                .setDebugMode(isDebugMode())
                .setImageResize(512, 512)
                .addBaseUrlHost(getInstance(), ApiHost.HOST_MAIN, AppConstant.HOST_URL)
                .addListClickListener(hashCode(), new DynamicCallback.OnDynamicListListener() {
                    @Override
                    public void onItemClicked(Activity activity, View view, DMProperty parent, DMContent item) {
                        ClassUtil.onItemClicked(activity, parent, new CategoryModel(), item);
                    }
                })
                .init(getInstance());

        FormBuilder.getInstance()
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setEnableJsonEncode(true)
                .setLocationProperty(FormLocationProperties.Builder()
                        .setEnableSearchBar(true)
                        .setEnableAddressLine1(true)
                        .setEnableAddressLine2(true)
                        .setEnableCityDetails(true)
                        .setHintAddressLine1("Enter Detail")
                        .setApiKey(getApiKey()))
                .setDebugModeEnabled(isDebugMode());

        addActivityLifecycleListener(hashCode(), new ActivityLifecycleListener() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                mCurrentActivity = activity;
            }
        });
    }


    private Activity mCurrentActivity;

    @Nullable
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }


    private void initOneSignal() {
        if(isDebugMode()) {
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        }

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        String apiKey = getOneSignalApiKey();
        if(TextUtils.isEmpty(apiKey)){
            apiKey = getString(R.string.onesignal_app_id);
        }
        Log.i("OneSignalExample", "initOneSignal() : " + apiKey);
        OneSignal.setAppId(apiKey);

        OneSignal.setNotificationOpenedHandler(new OneSignalOpenHandler(this));
        OneSignal.setNotificationWillShowInForegroundHandler(new NotificationWillShowInForegroundHandler());
    }

    public String getOneSignalApiKey() {
        return SupportUtil.getManifestMetaData(this, "com.appsfeature.onesignal");
    }

    public String getApiKey() {
        return SupportUtil.getManifestMetaData(this, "com.appsfeature.apikey");
    }

    private final ArrayList<NotificationReceivedCallback> onNotificationReceivedArrayList = new ArrayList<>();

    public void registerForNotificationCallBack(NotificationReceivedCallback onNotificationReceived){
        onNotificationReceivedArrayList.add(onNotificationReceived);
    }

    public void notifyNotificationCallBack(){
        if ( onNotificationReceivedArrayList.size() > 0 ){
            for ( NotificationReceivedCallback notificationReceived : onNotificationReceivedArrayList ){
                if ( notificationReceived != null ){
                    notificationReceived.updateMenuItems();
                }
            }
        }
    }

    public void clearNotificationCallBack(){
        onNotificationReceivedArrayList.clear();
    }

    public void updateLoginListener() {
        if (mLoginListener != null) {
            mLoginListener.onLoginComplete();
            mLoginListener = null;
        }
    }

    public void setLoginListener(LoginListener mLoginListener) {
        this.mLoginListener = mLoginListener;
    }
}
