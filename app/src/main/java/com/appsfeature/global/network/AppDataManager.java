package com.appsfeature.global.network;

import android.content.Context;

import com.appsfeature.global.model.CategoryModel;
import com.dynamic.listeners.DynamicCallback;
import com.helper.callback.Response;

import java.util.List;

public class AppDataManager {
    private static AppDataManager instance;

    private final NetworkManager networkManager;

    private AppDataManager(Context context) {
        networkManager = NetworkManager.getInstance(context);
    }

    public static AppDataManager get(Context context) {
        if(instance == null) instance = new AppDataManager(context);
        return instance;
    }

    public void getAppDataUser(int catId, int seasonId, DynamicCallback.Listener<List<CategoryModel>> callback) {
        networkManager.getAppDataUser(catId, seasonId, new DynamicCallback.Listener<List<CategoryModel>>() {
            @Override
            public void onSuccess(List<CategoryModel> response) {
                callback.onValidate(arraySortContent(response), new Response.Status<List<CategoryModel>>() {
                    @Override
                    public void onSuccess(List<CategoryModel> response) {
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }

    private List<CategoryModel> arraySortContent(List<CategoryModel> response) {
        return response;
    }
}
