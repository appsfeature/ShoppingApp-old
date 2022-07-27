package com.appsfeature.global.onesignal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.model.NotificationItem;
import com.appsfeature.global.util.DynamicUrlCreator;
import com.appsfeature.global.util.SupportUtil;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class OneSignalOpenHandler implements OneSignal.OSNotificationOpenedHandler {

//    private final Context context;

    public OneSignalOpenHandler(Context context) {
//        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        String launchUrl = result.getNotification().getLaunchURL();
        Log.i("OneSignalExample", "launchUrl set with value: " + (launchUrl != null ? launchUrl : "null"));
        NotificationCacheManager.updateReadStatus(AppApplication.getInstance().getApplicationContext(), result.getNotification().getNotificationId());
        SupportUtil.onNotificationOpened(AppApplication.getInstance().getApplicationContext(), result.getNotification());
    }

    public static NotificationItem parseNotificationData(OSNotification notification) {
        NotificationItem item = new NotificationItem();
        item.setAndroidNotificationId(notification.getAndroidNotificationId());
        item.setUuid(notification.getNotificationId());
        item.setImageUrl(notification.getBigPicture());
        item.setBody(notification.getBody());
        item.setUpdatedAt(SupportUtil.getDatabaseDateTime());

        if (notification.getLaunchURL() != null) {
            Log.d("@TestNot", "notification.getLaunchURL() : " + notification.getLaunchURL());
            item.setTitle(notification.getTitle());
            item.setLaunchURL(notification.getLaunchURL());
            item.setNotificationType(DynamicUrlCreator.TYPE_NOTIFICATION);
        }
        if(notification.getAdditionalData() != null) {
            Log.d("@TestNot", "notification.getAdditionalData() : " + notification.getAdditionalData().toString());
            try {
                JSONObject data = notification.getAdditionalData();
                if (data != null && !TextUtils.isEmpty(data.optString(DynamicUrlCreator.KEY_TITLE))) {
                    item.setTitle(data.optString(DynamicUrlCreator.KEY_TITLE));
                    item.setLaunchURL(data.optString(DynamicUrlCreator.KEY_URL));
                    item.setNotificationType(data.optString(DynamicUrlCreator.ACTION_TYPE));
                    item.setJsonData(notification.getAdditionalData().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return item;
    }
}
