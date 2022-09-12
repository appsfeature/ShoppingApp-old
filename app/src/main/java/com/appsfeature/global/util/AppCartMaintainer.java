package com.appsfeature.global.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;

import com.appsfeature.global.model.CartModel;
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

    public static void getCartList(Context context, Response.Status<CartModel> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<CartModel>() {
            @Override
            public CartModel call() throws Exception {
                List<ContentModel> mProducts = getData(context, CART_DATA, new TypeToken<List<ContentModel>>() {
                });
                return getCalculatedCartList(mProducts);
            }
        }, new TaskRunner.Callback<CartModel>() {
            @Override
            public void onComplete(CartModel result) {
                callback.onSuccess(result);
            }
        });
    }

    public static void getCartList(List<ContentModel> mProducts, Response.Status<CartModel> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<CartModel>() {
            @Override
            public CartModel call() throws Exception {
                return getCalculatedCartList(mProducts);
            }
        }, new TaskRunner.Callback<CartModel>() {
            @Override
            public void onComplete(CartModel result) {
                callback.onSuccess(result);
            }
        });
    }

    private static CartModel getCalculatedCartList(List<ContentModel> mProducts) {
        float price = 0, discount = 0, delivery = 0, total = 0;
        CartModel cartModel = null;
        if(mProducts != null && mProducts.size() > 0) {
            cartModel = new CartModel();
            cartModel.setProducts(mProducts);
            for (ContentModel item : mProducts){
                price += item.getPrice();
                if (item.getDiscountPrice() < item.getPrice()) {
                    discount += (item.getPrice() - item.getDiscountPrice());
                }
            }
            total = (price + delivery) - discount;
            cartModel.setPrice(price);
            cartModel.setDiscount(discount);
            cartModel.setDelivery(delivery);
            cartModel.setTotal(total);
        }
        return cartModel;
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
