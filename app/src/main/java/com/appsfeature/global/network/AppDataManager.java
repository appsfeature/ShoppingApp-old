package com.appsfeature.global.network;

import android.content.Context;
import android.text.TextUtils;

import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMBaseSorting;
import com.helper.callback.Response;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppDataManager extends DMBaseSorting {
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
                callback.onValidate(arraySortAppCategory(response), new Response.Status<List<CategoryModel>>() {
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

    public void getAppProductBySubCategory(int catId, int seasonId, DynamicCallback.Listener<List<ContentModel>> callback) {
        networkManager.getAppProductBySubCategory(catId, seasonId, new DynamicCallback.Listener<List<ContentModel>>() {
            @Override
            public void onSuccess(List<ContentModel> response) {
                callback.onValidate(arraySortAppContent(response), new Response.Status<List<ContentModel>>() {
                    @Override
                    public void onSuccess(List<ContentModel> response) {
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

    private List<CategoryModel> arraySortAppCategory(List<CategoryModel> list) {
        return arraySortAppCategory(list, true);
    }
    private List<CategoryModel> arraySortAppCategory(List<CategoryModel> list, boolean isOrderByAsc) {
        Collections.sort(list, new Comparator<CategoryModel>() {
            @Override
            public int compare(CategoryModel item, CategoryModel item2) {
                Date value = getDate(item.getCreatedAt());
                Date value2 = getDate(item2.getCreatedAt());
                return isOrderByAsc ? value.compareTo(value2) : value2.compareTo(value);
            }
        });
        Collections.sort(list, new Comparator<DMCategory>() {
            @Override
            public int compare(DMCategory item, DMCategory item2) {
                Integer value = item.getRanking();
                Integer value2 = item2.getRanking();
                return value.compareTo(value2);
            }
        });
        return list;
    }

    public List<ContentModel> arraySortAppContent(List<ContentModel> list) {
        Collections.sort(list, new Comparator<ContentModel>() {
            @Override
            public int compare(ContentModel item, ContentModel item2) {
                Integer value = item.getRanking();
                Integer value2 = item2.getRanking();
                return value.compareTo(value2);
            }
        });
        return list;
    }
}
