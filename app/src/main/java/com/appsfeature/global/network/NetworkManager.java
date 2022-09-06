package com.appsfeature.global.network;

import android.content.Context;
import android.text.TextUtils;

import com.appsfeature.global.model.AppBaseModel;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.CommonModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.FilterModel;
import com.appsfeature.global.model.UserModel;
import com.appsfeature.global.util.AppPreference;
import com.appsfeature.global.util.ListMaintainer;
import com.dynamic.DynamicModule;
import com.dynamic.listeners.ApiHost;
import com.dynamic.listeners.ApiRequestType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.network.DMNetworkManager;
import com.dynamic.network.NetworkCallback;
import com.dynamic.network.NetworkModel;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.util.BaseConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class NetworkManager extends DMNetworkManager {
    private static NetworkManager instance;

    public NetworkManager(Context context) {
        super(context);
    }

    public static NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public void getCommonData(String userId, final Response.Callback<List<CommonModel>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        configManager.getData(ApiRequestType.GET, ApiEndPoint.DEFAULT, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if(status && !TextUtils.isEmpty(data.getData())) {
                        List<CommonModel> list = data.getData(new TypeToken<List<CommonModel>>() {
                        });
                        if (list != null && list.size() > 0) {
                            callback.onSuccess(list);
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    }else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void login(String phone, final Response.Callback<NetworkModel> callback) {
        callback.onProgressUpdate(true);
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        configManager.getData(ApiRequestType.POST_FORM, ApiHost.HOST_MAIN, ApiEndPoint.GET_APP_USER_SIGNUP, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if(status) {
                        callback.onSuccess(data);
                    }else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void verifyOtp(String phone, String otp, final Response.Callback<UserModel> callback) {
        callback.onProgressUpdate(true);
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("otp", otp);
        configManager.getData(ApiRequestType.POST_FORM, ApiHost.HOST_MAIN, ApiEndPoint.USER_MATCH_OTP, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if (status && !TextUtils.isEmpty(data.getData())) {
                        AppBaseModel entity = data.getData(new TypeToken<AppBaseModel>() {
                        });
                        if (entity != null && entity.getUserData() != null && entity.getUserData().size() > 0) {
                            AppPreference.setProfile(entity.getUserData().get(0).toJson());
                            callback.onSuccess(entity.getUserData().get(0));
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    } else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public void getAppDataUser(int genderId, int seasonId, DynamicCallback.Listener<List<CategoryModel>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("category_id", genderId + "");
//        params.put("season_id", seasonId + "");
        configManager.getData(ApiRequestType.POST_FORM, ApiHost.HOST_MAIN, ApiEndPoint.GET_APP_DATA_USER, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if (status && !TextUtils.isEmpty(data.getData())) {
                        AppBaseModel entity = data.getData(new TypeToken<AppBaseModel>() {
                        });
                        if (entity != null && entity.getList() != null && entity.getList().size() > 0) {
                            AppPreference.setImageUrl(entity.getImageUrl());
                            DynamicModule.getInstance().setImageBaseUrl(context, ApiHost.HOST_DEFAULT, entity.getImageUrl());
                            callback.onSuccess(entity.getList());
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    } else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }

    public void getAppProductBySubCategory(int catId, int subCatId, int pageId, DynamicCallback.Listener<AppBaseModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("category_id", catId + "");
        params.put("subcategory_id", subCatId + "");
        params.put("page_id", pageId + "");
        params.put("size_id", "");
        params.put("color_id", "");
        configManager.getData(ApiRequestType.POST_FORM, ApiHost.HOST_MAIN, ApiEndPoint.GET_APP_PRODUCT_BY_SUBCATEGORY, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if (status && !TextUtils.isEmpty(data.getData())) {
                        AppBaseModel entity = data.getData(new TypeToken<AppBaseModel>() {
                        });
                        if (entity != null && entity.getProductList() != null && entity.getProductList().size() > 0) {
                            AppPreference.setImageUrl(entity.getImageUrl());
                            DynamicModule.getInstance().setImageBaseUrl(context, ApiHost.HOST_DEFAULT, entity.getImageUrl());
                            callback.onSuccess(entity);
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    } else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }

    public void getAppProductDetails(int productId, DynamicCallback.Listener<ContentModel> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("product_id", productId + "");
        configManager.getData(ApiRequestType.POST_FORM, ApiHost.HOST_MAIN, ApiEndPoint.GET_APP_PRODUCT_DETAILS, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if (status && !TextUtils.isEmpty(data.getData())) {
                        AppBaseModel entity = data.getData(new TypeToken<AppBaseModel>() {
                        });
                        if (entity != null && entity.getProductView() != null) {
                            AppPreference.setImageUrl(entity.getImageUrl());
                            DynamicModule.getInstance().setImageBaseUrl(context, ApiHost.HOST_DEFAULT, entity.getImageUrl());
                            callback.onSuccess(entity.getProductView());
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    } else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }

    public void getCountryCodes(DynamicCallback.Listener<List<CommonModel>> callback) {
        Map<String, String> params = new HashMap<>();
        configManager.getData(ApiRequestType.GET, ApiHost.HOST_MAIN, ApiEndPoint.GET_APP_COUNTRY_VIEW, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if (status && !TextUtils.isEmpty(data.getData())) {
                        AppBaseModel entity = data.getData(new TypeToken<AppBaseModel>() {
                        });
                        if (entity != null && entity.getCountry() != null && entity.getCountry().size() > 0) {
                            callback.onSuccess(entity.getCountry());
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    } else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }

    public void getAttributeData(DynamicCallback.Listener<List<FilterModel>> callback) {
        Map<String, String> params = new HashMap<>();
        configManager.getData(ApiRequestType.GET, ApiHost.HOST_MAIN, ApiEndPoint.GET_APP_ATTRIBUTES, params, new NetworkCallback.Response<NetworkModel>() {
            @Override
            public void onComplete(boolean status, NetworkModel data) {
                try {
                    if (status && !TextUtils.isEmpty(data.getData())) {
                        AppBaseModel entity = data.getData(new TypeToken<AppBaseModel>() {
                        });
                        if (entity != null && entity.getAttributes() != null && entity.getAttributes().size() > 0) {
                            ListMaintainer.saveList(context, AppPreference.ATTRIBUTES, entity.getAttributes());
                            callback.onSuccess(entity.getAttributes());
                        } else {
                            callback.onFailure(new Exception(BaseConstants.NO_DATA));
                        }
                    } else {
                        callback.onFailure(new Exception(data != null ? data.getMessage() : BaseConstants.NO_DATA));
                    }
                } catch (JsonSyntaxException e) {
                    callback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<NetworkModel> call, Exception e) {
                callback.onFailure(e);
            }

            @Override
            public void onRequestCompleted() {
                callback.onRequestCompleted();
            }
        });
    }
}
