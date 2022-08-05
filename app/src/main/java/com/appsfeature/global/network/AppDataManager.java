package com.appsfeature.global.network;

import android.content.Context;

import com.appsfeature.global.listeners.AttributeType;
import com.appsfeature.global.model.AttributeModel;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ColorModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.ProductDetail;
import com.appsfeature.global.model.VariantsModel;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.util.DMBaseSorting;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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

    public void processAdditionalAttributes(ArrayList<ColorModel> colorsList, List<VariantsModel> variants, Response.Status<HashMap<String, List<ProductDetail>>> callback) {
        if(variants != null && variants.size() > 0){
            HashMap<String, List<ProductDetail>> productDetailHashMap = new HashMap<>();
            ArrayList<ProductDetail> variantsList = new ArrayList<>();
            colorsList.clear();
            callback.onProgressUpdate(true);
            TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    addingAttributesList();
                    mappingAttributesDetails();
                    for (Map.Entry<String, List<ProductDetail>> entry : productDetailHashMap.entrySet()) {
                      colorsList.add(new ColorModel(entry.getKey(), entry.getValue()));
                    }
                    return true;
                }

                private void mappingAttributesDetails() {
                    for (ProductDetail item : variantsList) {
                        if (productDetailHashMap.get(item.getColor()) == null) {
                            productDetailHashMap.put(item.getColor(), new ArrayList<>());
                        }
                        List<ProductDetail> detailList = productDetailHashMap.get(item.color);
                        if (detailList != null) detailList.add(item);
                    }
                }

                private void addingAttributesList() {
                    for (VariantsModel item : variants) {
                        ProductDetail detail = new ProductDetail()
                                .setPrice(item.price)
                                .setImages(item.images);
                        if (item.getOptions() != null) {
                            for (AttributeModel option : item.getOptions()) {
                                if (option.getAttributeName().equalsIgnoreCase(AttributeType.Color)) {
                                    detail.setColor(option.getAttributesValue());
                                } else if (option.getAttributeName().equalsIgnoreCase(AttributeType.Size)) {
                                    detail.setSize(option.getAttributesValue());
                                }
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
}
