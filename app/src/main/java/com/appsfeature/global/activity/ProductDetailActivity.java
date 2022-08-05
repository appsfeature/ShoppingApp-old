package com.appsfeature.global.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.HomeChildAdapter;
import com.appsfeature.global.adapter.app.ColorAdapter;
import com.appsfeature.global.adapter.app.ProductAdapter;
import com.appsfeature.global.adapter.holder.ProductViewHolder;
import com.appsfeature.global.listeners.AttributeType;
import com.appsfeature.global.model.AttributeModel;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ColorModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.PresenterModel;
import com.appsfeature.global.model.ProductDetail;
import com.appsfeature.global.model.VariantsModel;
import com.appsfeature.global.network.AppDataManager;
import com.appsfeature.global.util.SupportUtil;
import com.dynamic.DynamicModule;
import com.dynamic.adapter.holder.DMAutoSliderViewHolder;
import com.dynamic.listeners.DMCategoryType;
import com.dynamic.listeners.DynamicCallback;
import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.BaseAnimationUtil;
import com.helper.util.BaseUtil;
import com.helper.widget.RecyclerViewCardMarginGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ProductDetailActivity extends BaseActivity{

    private View llNoData;
    private View viewMain;
    private RecyclerView rvColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initUi();
        getDataFromServer(property.getCatId());
    }

    private void initUi() {
        llNoData = findViewById(R.id.ll_no_data);
        viewMain = findViewById(R.id.view_main);
        rvColor = findViewById(R.id.rv_color);
    }

    private void getDataFromServer(int productId) {
        AppDataManager.get(this).getAppProductDetails(productId, new DynamicCallback.Listener<ContentModel>() {
            @Override
            public void onSuccess(ContentModel response) {
                BaseUtil.showNoData(llNoData, View.GONE);
                loadUi(response);
            }

            @Override
            public void onValidate(ContentModel list, Response.Status<ContentModel> callback) {
                DynamicCallback.Listener.super.onValidate(list, callback);
            }

            @Override
            public void onFailure(Exception e) {
//                if(mList.size() == 0) {
                    BaseUtil.showNoData(llNoData, View.VISIBLE);
//                }
            }

            @Override
            public void onRequestCompleted() {
            }
        });
    }

    private final ArrayList<ColorModel> colorsList = new ArrayList<>();

    private void loadUi(ContentModel response) {
        ProductViewHolder viewHolder = new ProductViewHolder(getWindow().getDecorView());
        String imageUrl = DynamicModule.getInstance().getImageBaseUrl(this);
        viewHolder.setData(response, imageUrl);
        updateSliderImages(response);
        BaseAnimationUtil.alphaAnimation(viewMain, View.VISIBLE);
        AppDataManager.get(this).processAdditionalAttributes(colorsList, response.getVariants(), new Response.Status<HashMap<String, List<ProductDetail>>>() {
            @Override
            public void onSuccess(HashMap<String, List<ProductDetail>> response) {
                Log.d("@Tester", "size: "+response.size());
                updateColorAdapter();
            }

            @Override
            public void onProgressUpdate(Boolean isShow) {

            }
        });
    }

    private void updateSliderImages(ContentModel response) {
        ArrayList<ContentModel> imagesList = new ArrayList<>();
        String[] images = SupportUtil.getImageUrlFromJson(response.getImage());
        if(images != null && images.length > 0) {
            for (String img : images) {
                ContentModel mItem = new ContentModel();
                mItem.setImage(img);
                imagesList.add(mItem);
            }
            DMAutoSliderViewHolder<CategoryModel, ContentModel> slider = new DMAutoSliderViewHolder(getWindow().getDecorView()) {
                @Override
                protected RecyclerView.Adapter<RecyclerView.ViewHolder> getChildAdapter(int itemType, Object category, List childList) {
                    return new HomeChildAdapter(ProductDetailActivity.this, itemType, (CategoryModel) category, (List<ContentModel>) childList, new DynamicCallback.OnClickListener<CategoryModel, ContentModel>() {
                        @Override
                        public void onItemClicked(View v, CategoryModel category, ContentModel item) {

                        }
                    });
                }
            };
            CategoryModel mCategory = new CategoryModel();
            mCategory.setItemType(DMCategoryType.TYPE_VIEWPAGER_AUTO_SLIDER_NO_TITLE);
            mCategory.setChildList(imagesList);
            slider.setData(mCategory, 0);
        }
    }

    private void updateColorAdapter() {
        if(colorsList.size() > 0) {
            rvColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            if(rvColor.getOnFlingListener() == null) {
                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(rvColor);
            }
            ColorAdapter adapter = new ColorAdapter(this, colorsList, new Response.OnClickListener<ColorModel>() {
                @Override
                public void onItemClicked(View view, ColorModel item) {

                }
            });
            rvColor.setAdapter(adapter);
            rvColor.setVisibility(View.VISIBLE);
        }else {
            rvColor.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUpdateUI(PresenterModel response) {

    }

    @Override
    public void onErrorOccurred(Exception e) {

    }

    @Override
    public void onStartProgressBar() {

    }

    @Override
    public void onStopProgressBar() {

    }
}
