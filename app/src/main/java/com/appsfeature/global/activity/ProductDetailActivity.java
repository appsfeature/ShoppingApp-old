package com.appsfeature.global.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.appsfeature.global.R;
import com.appsfeature.global.adapter.holder.ProductViewHolder;
import com.appsfeature.global.model.PresenterModel;
import com.dynamic.DynamicModule;

public class ProductDetailActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        onUpdateUI(new PresenterModel());
    }

    @Override
    public void onUpdateUI(PresenterModel response) {
        ProductViewHolder viewHolder = new ProductViewHolder(getWindow().getDecorView());
        String imageUrl = DynamicModule.getInstance().getImageBaseUrl(this);
        viewHolder.setData(property.getContentModel(), imageUrl);
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
