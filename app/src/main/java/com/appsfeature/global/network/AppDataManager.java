package com.appsfeature.global.network;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.appsfeature.global.listeners.AttributeType;
import com.appsfeature.global.listeners.FilterType;
import com.appsfeature.global.model.AppBaseModel;
import com.appsfeature.global.model.AttributeModel;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.FilterModel;
import com.appsfeature.global.model.SizeModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.ProductDetail;
import com.appsfeature.global.model.VariantsModel;
import com.appsfeature.global.util.AppPreference;
import com.appsfeature.global.util.ListMaintainer;
import com.appsfeature.global.util.SupportUtil;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.util.DMBaseSorting;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

public class AppDataManager extends DMBaseSorting {
    private static AppDataManager instance;

    private final NetworkManager networkManager;
    private final Context context;

    private AppDataManager(Context context) {
        this.context = context;
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

    public void getAppProductBySubCategory(int catId, int seasonId, int pageId, Map<Integer, String> filterMap, DynamicCallback.Listener<AppBaseModel> callback) {
        networkManager.getAppProductBySubCategory(catId, seasonId, pageId, filterMap, new DynamicCallback.Listener<AppBaseModel>() {
            @Override
            public void onSuccess(AppBaseModel response) {
                arraySortAppContent(response.getProductList());
                callback.onSuccess(response);
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

    public void getAppProductDetails(int productId, DynamicCallback.Listener<ContentModel> callback) {
        networkManager.getAppProductDetails(productId, new DynamicCallback.Listener<ContentModel>() {
            @Override
            public void onSuccess(ContentModel response) {
                callback.onSuccess(response);
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

    public void processAdditionalAttributes(ArrayList<SizeModel> sizeList, List<VariantsModel> variants, Response.Status<HashMap<Integer, List<ProductDetail>>> callback) {
        if(variants != null && variants.size() > 0){
            HashMap<Integer, List<ProductDetail>> productDetailHashMap = new HashMap<>();
            ArrayList<ProductDetail> variantsList = new ArrayList<>();
            sizeList.clear();
            callback.onProgressUpdate(true);
            TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    addingAttributesList();
                    mappingAttributesDetails();
                    boolean isFirstSelected = true;
                    for (Map.Entry<Integer, List<ProductDetail>> entry : productDetailHashMap.entrySet()) {
                        sizeList.add(new SizeModel(entry.getKey(), isFirstSelected, entry.getValue()));
                        isFirstSelected = false;
                    }
                    return true;
                }

                private void mappingAttributesDetails() {
                    for (ProductDetail item : variantsList) {
                        if (productDetailHashMap.get(item.getSize()) == null) {
                            productDetailHashMap.put(item.getSize(), new ArrayList<>());
                        }
                        List<ProductDetail> detailList = productDetailHashMap.get(item.getSize());
                        if (detailList != null) detailList.add(item);
                    }
                }

                private void addingAttributesList() {
                    for (VariantsModel item : variants) {
                        ProductDetail detail = new ProductDetail()
                                .setPrice(item.price)
                                .setQuantity(item.quantity)
                                .setImages(item.images);
                        if (item.getOptions() != null) {
                            for (AttributeModel option : item.getOptions()) {
                                if (option.getAttributeName().equalsIgnoreCase(AttributeType.Color)) {
                                    detail.setColor(option.getAttributesValue());
                                } else if (option.getAttributeName().equalsIgnoreCase(AttributeType.Size)) {
                                    detail.setSize(SupportUtil.parseInt(option.getAttributesValue()));
                                }
                                detail.setSkuCode(option.getSkuCode());
                            }
                        }
                        variantsList.add(detail);
                    }
                }
            }, new TaskRunner.Callback<Boolean>() {
                @Override
                public void onComplete(Boolean result) {
                    callback.onProgressUpdate(false);
                    callback.onSuccess(productDetailHashMap);
                }
            });
        }
    }

    public void processSliderList(List<ContentModel> sliderList, Response.Status<List<ContentModel>> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<List<ContentModel>>() {
            @Override
            public List<ContentModel> call() throws Exception {
                for (ContentModel item : sliderList) {
                    String image = SupportUtil.getImageUrlFromJson(AppPreference.getImageUrl(), item.getImage());
                    item.setImage(image);
                }
                return sliderList;
            }
        }, new TaskRunner.Callback<List<ContentModel>>() {
            @Override
            public void onComplete(List<ContentModel> result) {
                callback.onSuccess(result);
            }
        });
    }

    public void getAttributeData(DynamicCallback.Listener<List<FilterModel>> callback) {
        List<FilterModel> savedList = ListMaintainer.getList(context, AppPreference.ATTRIBUTES, new TypeToken<List<FilterModel>>() {
        });
        if(savedList != null && savedList.size() > 0){
            callback.onSuccess(savedList);
        }
        networkManager.getAttributeData(new DynamicCallback.Listener<List<FilterModel>>() {
            @Override
            public void onSuccess(List<FilterModel> response) {
                callback.onSuccess(response);
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


    public static void getFilterData(List<FilterModel> response, Response.Status<Map<Integer, String>> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<Map<Integer, String>>() {
            @Override
            public Map<Integer, String> call() throws Exception {
                Map<Integer, String> filterMap = new TreeMap<>();
                if (response != null && response.size() > 0) {
                    List<Integer> colors = new ArrayList<>();
                    List<Integer> sizes = new ArrayList<>();
                    for (FilterModel item : response) {
                        if (item.getId() == 1) {
                            for (FilterModel child : item.getAttributesIds()) {
                                if (child.isChecked()) {
                                    colors.add(child.getId());
                                }
                            }
                        } else if (item.getId() == 2) {
                            for (FilterModel child : item.getAttributesIds()) {
                                if (child.isChecked()) {
                                    sizes.add(child.getId());
                                }
                            }
                        }
                    }
                    if (colors.size() > 0 || sizes.size() > 0) {
                        if (colors.size() > 0) {
                            filterMap.put(FilterType.TYPE_COLOR, GsonParser.toJson(colors, new TypeToken<List<Integer>>() {
                            }));
                        }
                        if (sizes.size() > 0) {
                            filterMap.put(FilterType.TYPE_SIZE, GsonParser.toJson(sizes, new TypeToken<List<Integer>>() {
                            }));
                        }
                    }
                }
                return filterMap;
            }
        }, new TaskRunner.Callback<Map<Integer, String>>() {
            @Override
            public void onComplete(Map<Integer, String> result) {
                callback.onSuccess(result);
            }
        });
    }

}
