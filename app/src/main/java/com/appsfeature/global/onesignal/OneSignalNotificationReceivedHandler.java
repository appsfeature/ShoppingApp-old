package com.appsfeature.global.onesignal;


import android.content.Context;

import com.appsfeature.global.AppApplication;
import com.appsfeature.global.model.NotificationItem;
import com.helper.task.TaskRunner;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

/**
 * Created by amit on 16/7/17.
 */

public class OneSignalNotificationReceivedHandler implements OneSignal.OSRemoteNotificationReceivedHandler {

    private boolean isNotificationUnique = true ;

    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent osNotificationReceivedEvent) {
        TaskRunner.getInstance().executeAsync(() -> isNotificationUnique(osNotificationReceivedEvent.getNotification()), result -> {
            if ( isNotificationUnique ){
                handleNotificationData(osNotificationReceivedEvent , osNotificationReceivedEvent.getNotification());
                insertNotification(osNotificationReceivedEvent.getNotification());
            }else {
                displayNotification(osNotificationReceivedEvent, osNotificationReceivedEvent.getNotification());
            }
        });
    }

    private boolean isNotificationUnique(OSNotification notification) {
        isNotificationUnique = NotificationCacheManager.isNotificationUnique(AppApplication.getInstance(), notification.getNotificationId());
        return true;
    }

    private void handleNotificationData(OSNotificationReceivedEvent osNotificationReceivedEvent, OSNotification notification){
        displayNotification(osNotificationReceivedEvent, notification);
    }

    private void insertNotification(OSNotification notification){
        NotificationItem mItem = OneSignalOpenHandler.parseNotificationData(notification);
        insertNotificationDB(mItem);
    }

    private void displayNotification(OSNotificationReceivedEvent osNotificationReceivedEvent, OSNotification notification) {
        osNotificationReceivedEvent.complete(notification);
    }

    private void ignoreNotification(OSNotificationReceivedEvent notification){
        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()
        notification.complete(null);
    }

    private void insertNotificationDB(NotificationItem item){
        try {
            TaskRunner.getInstance().executeAsync(() -> {
                NotificationCacheManager.saveNotification(AppApplication.getInstance(), item);
                return null;
            }, (TaskRunner.Callback<Void>) result -> AppApplication.getInstance().notifyNotificationCallBack());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}