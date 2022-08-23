package com.appsfeature.global.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.appsfeature.global.AppValues;
import com.appsfeature.global.activity.CartActivity;
import com.appsfeature.global.activity.HtmlViewerActivity;
import com.appsfeature.global.activity.MainActivity;
import com.appsfeature.global.activity.ProductDetailActivity;
import com.appsfeature.global.activity.ProductListActivity;
import com.appsfeature.global.listeners.CategoryType;
import com.appsfeature.global.listeners.ContentType;
import com.appsfeature.global.listeners.LoginType;
import com.appsfeature.global.login.LoginActivity;
import com.appsfeature.global.model.CategoryModel;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.ExtraProperty;
import com.appsfeature.global.model.OtherProperty;
import com.appsfeature.global.video.util.YTUtility;
import com.browser.BrowserSdk;
import com.dynamic.listeners.DMContentType;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMProperty;
import com.formbuilder.FormBuilder;
import com.formbuilder.interfaces.FormResponse;
import com.formbuilder.model.FormBuilderModel;
import com.formbuilder.util.FBPreferences;
import com.google.gson.JsonSyntaxException;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.helper.util.Logger;
import com.helper.util.SocialUtil;

import java.util.TreeMap;

public class ClassUtil {

    public static void onItemClicked(Activity activity, DMProperty parent, CategoryModel category, DMContent mItem) {
        onItemClicked(activity, parent, category, SupportUtil.getContentModel(mItem));
    }

    public static void onItemClicked(Activity activity, DMProperty parent, CategoryModel category, ContentModel mItem) {
        OtherProperty otherProperty = null;
//        if(!mItem.isContent()){
//            DMClassUtil.openDynamicListActivity(activity, DMUtility.getProperty(parent, mItem));
//        }else {
            switch (category.getItemType()){
                case LoginType.TYPE_COURSE:
                case LoginType.TYPE_TOOLS:
                case LoginType.TYPE_ASSOCIATE:
                    ClassUtil.openLoginActivity(activity, mItem.getItemType());
                    break;
                case DMContentType.TYPE_LINK:
                    try {
                        otherProperty = GsonParser.getGson().fromJson(getOtherProperty(parent, mItem), OtherProperty.class);
                    } catch (JsonSyntaxException e) {
                        Logger.e(e.getMessage());
                    }
                    boolean isBrowserChrome = false;
                    if(otherProperty != null) isBrowserChrome = otherProperty.isBrowserChrome();
                    ClassUtil.openLink(activity, mItem.getTitle(), mItem.getLink(), false, isBrowserChrome);
                    break;
                case DMContentType.TYPE_PDF:
                    ClassUtil.openPdf(activity, mItem.getId(), mItem.getTitle(), mItem.getLink());
                    break;
                case DMContentType.TYPE_HTML_VIEW:
                    ClassUtil.openActivityHtmlViewer(activity, mItem.getId(), mItem.getTitle(), mItem.getDescription());
                    break;
                case DMContentType.TYPE_VIDEOS:
                case CategoryType.TYPE_VIDEO_PRODUCT:
                    try {
                        otherProperty = GsonParser.getGson().fromJson(parent.getOtherProperty(), OtherProperty.class);
                    } catch (JsonSyntaxException e) {
                        Logger.e(e.getMessage());
                    }
                    boolean isPlayerStyleMinimal = false;
                    if(otherProperty != null) isPlayerStyleMinimal = otherProperty.isPlayerStyleMinimal();
                    ClassUtil.openVideo(activity, mItem.getSubCatId(), mItem.getTitle(), mItem.getDescription(), mItem.getVideoUrl(), mItem.getVideoTime(), mItem.getVideoDuration(), isPlayerStyleMinimal);
                    break;
                case ContentType.TYPE_FORM:
                    try {
                        FormBuilderModel formBuilder = null;
                        try {
                            formBuilder = GsonParser.getGson().fromJson(mItem.getOtherProperty(), FormBuilderModel.class);
                        } catch (JsonSyntaxException e) {
                            Logger.e(e.getMessage());
                        }
                        if(formBuilder != null){
                            formBuilder.setExtraParams(new TreeMap<>());
                            formBuilder.getExtraParams().put("pkg_name", activity.getPackageName());
                            formBuilder.getExtraParams().put("cat_id", formBuilder.getFormId() + "");
                            FBPreferences.setFormSubmitted(activity, formBuilder.getFormId(), false);
                            FormBuilder.getInstance().openDynamicFormActivity(activity, formBuilder.getFormId(), formBuilder, new FormResponse.FormSubmitListener() {
                                @Override
                                public void onFormSubmitted(String data) {

                                }
                            });
                        }else {
                            BaseUtil.showToast(activity, "Invalid form format. Please try after sometime.");
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        BaseUtil.showToast(activity, "Invalid form format. Please try after sometime.");
                    }
                    break;
                default:
                    if(category.getItemType() == CategoryType.TYPE_SUB_CATEGORY
                            || category.getItemType() == CategoryType.TYPE_PRODUCT){
                        openActivityProductList(activity, mItem.getId(), mItem.getCategoryId(), mItem.getTitle());
                    }else {
                        BaseUtil.showToast(activity, "No option available for take action.");
                    }
                    break;
            }
//        }
    }

    private static String getOtherProperty(DMProperty parent, DMContent mItem) {
        if(mItem != null && !TextUtils.isEmpty(mItem.getOtherProperty())){
            return mItem.getOtherProperty();
        }
        if(parent != null && !TextUtils.isEmpty(parent.getOtherProperty())){
            return parent.getOtherProperty();
        }
        return null;
    }

    public static void openLink(Context context, String title, String webUrl) {
        openLink(context, title, webUrl, false);
    }


    public static void openPdf(Activity context, int id, String title, String pdfNameOrUrl) {
        if(TextUtils.isEmpty(pdfNameOrUrl)){
            BaseUtil.showToast(context, "Invalid Url.");
            return;
        }
        String urlPrefix = UriBuilder.getUrlPrefix(pdfNameOrUrl);
        String fileName = UriBuilder.getFileName(pdfNameOrUrl);
        if(BaseUtil.isValidUrl(pdfNameOrUrl)){
            openPdf(context, id, title, fileName, urlPrefix, pdfNameOrUrl, null);
        }else {
            openPdf(context, id, title, pdfNameOrUrl, null);
        }
    }

    public static void openPdf(Activity activity, int id, String pdfTitle, String pdfFileName, String subTitle) {
        String pdfFileUrl = AppValues.BASE_URL_PDF_DOWNLOAD + pdfFileName;
        openPdf(activity, id, pdfTitle, pdfFileName, pdfFileUrl, subTitle);
    }

    public static void openPdf(Activity activity, int id, String pdfTitle, String pdfFileName, String pdfFileUrl, String subTitle) {
        openPdf(activity, id, pdfTitle, pdfFileName, AppValues.BASE_URL_PDF_DOWNLOAD, pdfFileUrl, subTitle);
    }

    public static void openPdf(Activity activity, int id, String pdfTitle, String pdfFileName, String pdfBaseUrlPrefix, String pdfFileUrl, String subTitle) {
//        PDFViewer.openPdfDownloadActivity(activity, id, pdfTitle, pdfFileName, pdfBaseUrlPrefix, pdfFileUrl, subTitle);
        BaseUtil.showToast(activity, "Pdf viewer not available");
    }

    public static void openVideo(Activity activity, int catId, String title, String description, String videoIdOrUrl, int videoTime, int videoDuration, boolean isPlayerStyleMinimal) {
        if(TextUtils.isEmpty(videoIdOrUrl)){
            BaseUtil.showToast(activity, "Invalid Video Id or Url.");
            return;
        }
        YTUtility.playVideo(activity, catId, title, description, videoIdOrUrl, videoTime, videoDuration, isPlayerStyleMinimal);
    }

    public static void openLink(Context context, String title, String webUrl, boolean isRemoveHeaderFooter) {
        openLink(context, title, webUrl,isRemoveHeaderFooter, false);
    }
    public static void openLink(Context context, String title, String webUrl, boolean isRemoveHeaderFooter, boolean isBrowserChrome) {
        if (BaseUtil.isValidUrl(webUrl)) {
            if(isBrowserChrome){
                SocialUtil.openLinkInBrowserChrome(context, webUrl);
            }else {
                BrowserSdk.open(context, title, webUrl, false, isRemoveHeaderFooter);
            }
        } else {
            BaseUtil.showToast(context, "Invalid Link.");
        }
    }

    public static void openLoginActivity(Activity activity, int loginType) {
        if(!AppPreference.isLoginCompleted()){
            try {
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.putExtra(AppConstant.EXTRA_PROPERTY, loginType);
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                BaseUtil.showToast(activity, "No option available for take action.");
            }
        }else {
            ClassUtil.openLink(activity, "", AppPreference.getSessionLoginUrl(), false);
        }
    }
    public static void openHomeActivity(Activity activity) {
        Intent intent = activity.getIntent();
        if(activity.getIntent() == null){
            intent = new Intent();
        }
        intent.setClass(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static void openActivityHtmlViewer(Activity activity, int id, String title, String description) {
        ExtraProperty extraProperty = new ExtraProperty();
        extraProperty.setCatId(id);
        extraProperty.setTitle(title);
        extraProperty.setDescription(description);
        activity.startActivity(new Intent(activity, HtmlViewerActivity.class)
                .putExtra(AppConstant.CATEGORY_PROPERTY, extraProperty));
    }

    public static void openActivityProductList(Activity activity, int catId, int subCatId, String title) {
        ExtraProperty extraProperty = new ExtraProperty();
        extraProperty.setCatId(catId);
        extraProperty.setParentId(subCatId);
        extraProperty.setTitle(title);
        activity.startActivity(new Intent(activity, ProductListActivity.class)
                .putExtra(AppConstant.CATEGORY_PROPERTY, extraProperty));
    }


    public static void openActivityProductView(Activity activity, ExtraProperty property, ContentModel item) {
        ExtraProperty extraProperty = property.getClone();
        extraProperty.setCatId(item.getId());
        extraProperty.setContentModel(item);
        activity.startActivity(new Intent(activity, ProductDetailActivity.class)
                .putExtra(AppConstant.CATEGORY_PROPERTY, extraProperty));
    }


    public static void openActivityCart(Activity activity) {
        activity.startActivity(new Intent(activity, CartActivity.class));
    }
}
