package com.appsfeature.global.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.HomeChildAdapter;
import com.appsfeature.global.adapter.app.ColorAdapter;
import com.appsfeature.global.adapter.app.SizeAdapter;
import com.appsfeature.global.adapter.holder.ProductViewHolder;
import com.appsfeature.global.dialog.AppDialog;
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.PresenterModel;
import com.appsfeature.global.model.ProductDetail;
import com.appsfeature.global.model.SizeModel;
import com.appsfeature.global.network.AppDataManager;
import com.appsfeature.global.util.AppCartMaintainer;
import com.appsfeature.global.util.ClassUtil;
import com.appsfeature.global.util.SupportUtil;
import com.dynamic.DynamicModule;
import com.dynamic.adapter.holder.DMAutoSliderViewHolder;
import com.dynamic.listeners.DynamicCallback;
import com.helper.callback.Response;
import com.helper.util.BaseAnimationUtil;
import com.helper.util.BaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener{

    private View llNoData;
    private View viewMain;
    private RecyclerView rvSize, rvColor;

    private final ArrayList<SizeModel> sizesList = new ArrayList<>();
    private final ArrayList<ProductDetail> colorsList = new ArrayList<>();
    private ContentModel mContentDetail;
    private TextView tvProductCode;

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
        rvSize = findViewById(R.id.rv_size);
        rvColor = findViewById(R.id.rv_color);
        tvProductCode = findViewById(R.id.tv_product_code);
        (findViewById(R.id.btn_add_to_cart)).setOnClickListener(this);
        (findViewById(R.id.btn_buy_now)).setOnClickListener(this);
        (findViewById(R.id.ll_size_chart)).setOnClickListener(this);
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

    private void loadUi(ContentModel response) {
        this. mContentDetail = response;
        if (!TextUtils.isEmpty(mContentDetail.getProductCode())) {
            tvProductCode.setText("Product Code : " + mContentDetail.getProductCode());
            tvProductCode.setVisibility(View.VISIBLE);
        }else {
            tvProductCode.setVisibility(View.GONE);
        }
        ProductViewHolder viewHolder = new ProductViewHolder(getWindow().getDecorView());
        String imageUrl = DynamicModule.getInstance().getImageBaseUrl(this);
        viewHolder.setData(response, imageUrl);
        updateSliderImages(response);
        BaseAnimationUtil.alphaAnimation(viewMain, View.VISIBLE);
        AppDataManager.get(this).processAdditionalAttributes(sizesList, response.getVariants(), new Response.Status<HashMap<Integer, List<ProductDetail>>>() {
            @Override
            public void onSuccess(HashMap<Integer, List<ProductDetail>> response) {
                updateSizeAdapter();
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
            mCategory.setItemType(CategoryType.TYPE_PRODUCT_DETAIL);
            mCategory.setChildList(imagesList);
            slider.setData(mCategory, 0);
        }
    }

    private void updateSizeAdapter() {
        if(sizesList.size() > 0) {
            rvSize.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            if(rvSize.getOnFlingListener() == null) {
                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(rvSize);
            }
            SizeAdapter adapter = new SizeAdapter(this, sizesList, new Response.OnClickListener<SizeModel>() {
                @Override
                public void onItemClicked(View view, SizeModel item) {
                    updateColorAdapter(item.isChecked(), item.getList());
                }
            });
            rvSize.setAdapter(adapter);
            rvSize.setVisibility(View.VISIBLE);
        }else {
            rvSize.setVisibility(View.GONE);
        }
    }

    private void updateColorAdapter(boolean checked, List<ProductDetail> list) {
        colorsList.clear();
        if(checked && list != null && list.size() > 0){
            colorsList.addAll(list);
        }
        updateColorAdapter();
    }
    private void updateColorAdapter() {
        if(colorsList.size() > 0) {
            rvColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
            if(rvColor.getOnFlingListener() == null) {
                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(rvColor);
            }
            ColorAdapter adapter = new ColorAdapter(this, colorsList, new Response.OnClickListener<ProductDetail>() {
                @Override
                public void onItemClicked(View view, ProductDetail item) {

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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_add_to_cart){
            addProductToCart(false);
        }else if(view.getId() == R.id.btn_buy_now){
            addProductToCart(true);
        }else if(view.getId() == R.id.ll_size_chart){
            AppDialog.openSizeChart(this, mContentDetail.categoryId);
        }
    }

    private void addProductToCart(boolean isOpenCart) {
        int selectedSize = 0;
        ProductDetail productDetail = null;
        for (SizeModel item : sizesList){
            if(item.isChecked()){
                selectedSize = item.getSize();
            }
        }
        for (ProductDetail item : colorsList){
            if(item.isChecked()){
                productDetail = item.getClone();
                productDetail.setSize(selectedSize);
            }
        }
        if(sizesList.size() > 0) {
            if (selectedSize <= 0) {
                BaseUtil.showToast(this, "Please Select Size.");
                return;
            }
            if (productDetail == null) {
                BaseUtil.showToast(this, "Please Select Color.");
                return;
            }
        }
        mContentDetail.setProductDetail(productDetail);
        AppCartMaintainer.addOnCart(this, mContentDetail);
        BaseUtil.showToast(this, "Product added on Cart.");
        if(isOpenCart){
            ClassUtil.openActivityCart(this);
        }
    }
}
