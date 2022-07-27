package com.appsfeature.global.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.appsfeature.global.R;
import com.appsfeature.global.model.PresenterModel;
import com.appsfeature.global.util.SupportUtil;
import com.appsfeature.global.util.WebViewHelper;
import com.helper.util.BaseConstants;


public class HtmlViewerActivity extends BaseActivity {

    private View llNoData;
    private WebView webView;
    private WebViewHelper webViewHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_viewer);

        onInitializeUI();
        setUpToolBar(appPresenter.getExtraProperty().getTitle());
        loadData();
    }

    private void loadData() {
        onStartProgressBar();
        String textColor = SupportUtil.getColorValue(this, R.color.themeColorBlackWhite);
        String bgColor = SupportUtil.getColorValue(this, R.color.themeWindowBackground);

        if(!TextUtils.isEmpty(appPresenter.getExtraProperty().getDescription())) {
            webViewHelper.setDataWebView(appPresenter.getExtraProperty().getDescription(), textColor, bgColor);
        }else {
            onErrorOccurred(new Exception(BaseConstants.NO_DATA));
        }
    }

    public void onInitializeUI() {
        llNoData = findViewById(R.id.ll_no_data);
        webView = findViewById(R.id.wv_desc);
        webViewHelper = new WebViewHelper(webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onStopProgressBar();
            }
        });
    }

    @Override
    public void onUpdateUI(PresenterModel response) {

    }

    @Override
    public void onErrorOccurred(Exception e) {
        SupportUtil.showToast(this, e.getMessage());
        SupportUtil.showNoData(llNoData, View.VISIBLE);
        webView.setVisibility(View.GONE);
    }

    @Override
    public void onStartProgressBar() {
        SupportUtil.showNoDataProgress(llNoData);
    }

    @Override
    public void onStopProgressBar() {
        SupportUtil.showNoData(llNoData, View.GONE);
    }
}
