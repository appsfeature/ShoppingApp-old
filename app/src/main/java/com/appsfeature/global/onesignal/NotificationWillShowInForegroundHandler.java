package com.appsfeature.global.onesignal;

import android.app.AlertDialog;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.R;
import com.appsfeature.global.util.SupportUtil;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;


public class NotificationWillShowInForegroundHandler implements OneSignal.OSNotificationWillShowInForegroundHandler{

    @Override
    public void notificationWillShowInForeground(OSNotificationReceivedEvent osNotificationReceivedEvent) {
        if (AppApplication.getInstance() != null ){
            handleForegroundNotificationUI(osNotificationReceivedEvent.getNotification());
            osNotificationReceivedEvent.complete(null);
        }else {
            osNotificationReceivedEvent.complete(osNotificationReceivedEvent.getNotification());
        }
    }

    private void handleForegroundNotificationUI(final OSNotification notification) {
        if(AppApplication.getInstance() == null || AppApplication.getInstance().getCurrentActivity() == null){
            return;
        }
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AppApplication.getInstance().getCurrentActivity(), R.style.DialogTheme);
                    builder.setMessage(notification.getBody())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> {
                                dialog.dismiss();
                                dialog.cancel();
                                NotificationCacheManager.updateReadStatus(AppApplication.getInstance(), notification.getNotificationId());
                                SupportUtil.onNotificationOpened(AppApplication.getInstance(), notification);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                dialog.dismiss();
                                dialog.cancel();
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle(notification.getTitle());
                    alert.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            AppApplication.getInstance().getCurrentActivity().runOnUiThread(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new Handler().post(runnable);

    }
}
