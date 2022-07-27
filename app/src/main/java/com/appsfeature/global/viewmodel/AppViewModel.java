package com.appsfeature.global.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.appsfeature.global.listeners.AppCallback;
import com.appsfeature.global.model.CommonModel;
import com.appsfeature.global.model.ExtraProperty;
import com.appsfeature.global.model.PresenterModel;
import com.appsfeature.global.network.NetworkManager;
import com.appsfeature.global.util.AppConstant;
import com.appsfeature.global.util.SupportUtil;
import com.helper.callback.Response;

import java.util.List;

public class AppViewModel extends ViewModel {
    private AppCallback.View viewCallback;
    private Context context;
    private NetworkManager networkHandler;
    private ExtraProperty extraProperty;

    public ExtraProperty getExtraProperty() {
        return extraProperty;
    }

    public void initialize(Activity activity, Intent intent) {
        this.context = activity;
        this.viewCallback = (AppCallback.View) activity;
        networkHandler = new NetworkManager(context);
//        databaseHandler = DatabaseHandler(context);
        setArguments(intent);
    }

    public void initialize(Fragment fragment, Bundle bundle) {
        this.context = fragment.getActivity();
        this.viewCallback = (AppCallback.View) fragment;
        networkHandler = new NetworkManager(context);
//        databaseHandler = DatabaseHandler(context);
        setArguments(bundle);
    }

    private void setArguments(Intent intent) {
        if (intent.getSerializableExtra(AppConstant.CATEGORY_PROPERTY) instanceof ExtraProperty) {
            extraProperty = (ExtraProperty) intent.getSerializableExtra(AppConstant.CATEGORY_PROPERTY);
        } else {
            SupportUtil.showToast(context, AppConstant.INVALID_PROPERTY);
        }
    }

    private void setArguments(Bundle bundle) {
        if (bundle.getSerializable(AppConstant.CATEGORY_PROPERTY) instanceof ExtraProperty) {
            extraProperty = (ExtraProperty) bundle.getSerializable(AppConstant.CATEGORY_PROPERTY);
        } else {
            SupportUtil.showToast(context, AppConstant.INVALID_PROPERTY);
        }
    }

    public void getCommonData(String userId) {
        viewCallback.onStartProgressBar();
        networkHandler.getCommonData(userId, new Response.Callback<List<CommonModel>>() {
            @Override
            public void onSuccess(List<CommonModel> response) {
                viewCallback.onStopProgressBar();
                viewCallback.onUpdateUI(new PresenterModel().setCommonList(response));
            }

            @Override
            public void onFailure(Exception e) {
                viewCallback.onStopProgressBar();
                viewCallback.onErrorOccurred(e);
            }
        });
    }


}
