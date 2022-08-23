package com.appsfeature.global.util;

import android.content.Context;
import android.text.TextUtils;

import com.appsfeature.global.model.ContentModel;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.BasePrefUtil;
import com.helper.util.GsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AppCartMaintainer extends ListMaintainer {

    private static final String CART_DATA = "cart_data";

    public static void addOnCart(Context context, ContentModel mProduct) {
        saveData(context, CART_DATA, mProduct, mProduct.getTitle());
    }

    public static void getCartList(Context context, Response.Status<List<ContentModel>> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<List<ContentModel>>() {
            @Override
            public List<ContentModel> call() throws Exception {
                return getData(context, CART_DATA, new TypeToken<List<ContentModel>>() {
                });
            }
        }, new TaskRunner.Callback<List<ContentModel>>() {
            @Override
            public void onComplete(List<ContentModel> result) {
                callback.onSuccess(result);
            }
        });
    }

    public static void removeCartItem(Context context, String matchText, Response.Status<Boolean> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return removeData(context, matchText);
            }
        }, new TaskRunner.Callback<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                callback.onSuccess(result);
            }
        });
    }

    private static boolean removeData(Context context, String matchText) {
        String key = CART_DATA;
        List<ContentModel> value = GsonParser.fromJson((BasePrefUtil.getRecentFeatureData(context, key)), new TypeToken<List<ContentModel>>(){});
        if (value == null) {
            value = new ArrayList<>();
        }
        int prevSize = value.size();
        for (ContentModel item : value) {
            if(item.getTitle().equalsIgnoreCase(matchText)){
                value.remove(item);
                break;
            }
        }
        if (value.size() <= 0) {
            BasePrefUtil.setRecentFeatureData(context, key, "");
        } else {
            String newJson = GsonParser.toJson(value, new TypeToken<List<ContentModel>>() {
            });
            if (!TextUtils.isEmpty(newJson)) {
                BasePrefUtil.setRecentFeatureData(context, key, newJson);
            }
        }
        return prevSize != value.size();
    }
}
