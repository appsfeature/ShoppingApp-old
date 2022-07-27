package com.appsfeature.global.util;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appsfeature.global.R;
import com.appsfeature.global.activity.MainActivity;
import com.appsfeature.global.activity.SplashScreen;
import com.appsfeature.global.model.ContentModel;
import com.appsfeature.global.model.NotificationItem;
import com.appsfeature.global.onesignal.OneSignalOpenHandler;
import com.dynamic.model.DMContent;
import com.dynamic.util.DMConstants;
import com.dynamic.util.DMUtility;
import com.helper.util.BaseUtil;
import com.helper.util.GsonParser;
import com.onesignal.OSNotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SupportUtil extends BaseUtil {



    public static Bundle getPropertyBundle(int catId, boolean isDisableCaching) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DMConstants.CATEGORY_PROPERTY, DMUtility.getProperty(catId).setDisableCaching(isDisableCaching));
        return bundle;
    }

    public static void hideKeybord(Activity activity) {
        if(activity!=null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View f = activity.getCurrentFocus();
            if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
                imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
            else
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public static SpannableString generateBoldTitle(String title, String boldText) {
        SpannableString s = new SpannableString(title + " " + boldText);
        s.setSpan(new StyleSpan(Typeface.BOLD), title.length(), title.length() + boldText.length() + 1  , 0);
        return s;
    }


    public static void showNoDataProgress(View view) {
        if (view != null) {
            view.setVisibility(VISIBLE);
            if (view.findViewById(R.id.player_progressbar) != null) {
                view.findViewById(R.id.player_progressbar).setVisibility(VISIBLE);
            }
            TextView tvNoData = view.findViewById(R.id.tv_no_data);
            if (tvNoData != null) {
                tvNoData.setVisibility(GONE);
            }
        }
    }

    public static void share(Context context, String message) {
        String appLink = message + "Download " + context.getString(R.string.app_name) + " app. \nLink : " + AppConstant.DYNAMIC_SHARE_URL;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, appLink);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void shareUrl(Context context, String title, String url) {
        new DynamicUrlCreator(context).shareUrl(title, url);
    }

    public static void openUrl(Activity activity, String title, String link) {
        ClassUtil.openLink(activity, title, link);
    }

    public static String getManifestMetaData(Context context, String key) {
        try {
            ApplicationInfo ai= context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if(value != null){
                return value.toString();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void onNotificationOpened(Context context, OSNotification notification) {
        Log.i("OneSignalExample", "onNotificationOpened");
        if(context == null) return;
        try {
            NotificationItem mItem = OneSignalOpenHandler.parseNotificationData(notification);
            Uri uri = DynamicUrlCreator.getWebsiteUri(mItem.getNotificationType(), mItem.getTitle(), mItem.getLaunchURL());

            Intent intent = new Intent(context, SplashScreen.class);
            intent.setAction(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(uri);
            context.startActivity(intent);
            Log.i("OneSignalExample", "context.startActivity(intent)");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("OneSignalExample", "Exception : " + e.toString());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void onNotificationClicked(Activity activity, NotificationItem mItem) {
        if(activity == null) return;
        try {
            Uri uri = DynamicUrlCreator.getWebsiteUri(mItem.getNotificationType(), mItem.getTitle(), mItem.getLaunchURL());
            DynamicUrlCreator.openActivity(activity, uri, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDatabaseDateTime() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date());
    }


    public static DMContent getContentModel(ContentModel mItem) {
        mItem.setLink(mItem.getVideoUrl());
        return mItem;
    }

}
