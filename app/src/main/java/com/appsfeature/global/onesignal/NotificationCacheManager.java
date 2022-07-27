package com.appsfeature.global.onesignal;

import android.content.Context;

import com.appsfeature.global.activity.NotificationActivity;
import com.appsfeature.global.model.NotificationItem;
import com.google.gson.reflect.TypeToken;
import com.helper.callback.Response;
import com.helper.task.TaskRunner;
import com.helper.util.ListMaintainer;
import com.onesignal.OSNotification;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class NotificationCacheManager extends ListMaintainer {

    private static final String KEY_NOTIFICATION = "notification";
    private static final int listSize = 15;

    public static <T> void saveNotification(Context context, T item) {
        saveData(context, KEY_NOTIFICATION, listSize, item, null);
    }

    public static List<NotificationItem> getNotificationList(Context context) {
        return getData(context, KEY_NOTIFICATION, new TypeToken<List<NotificationItem>>() {
        });
    }

    public static void getNotificationList(Context context, Response.Status<List<NotificationItem>> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<List<NotificationItem>>() {
            @Override
            public List<NotificationItem> call() throws Exception {
                return getNotificationList(context);
            }
        }, new TaskRunner.Callback<List<NotificationItem>>() {
            @Override
            public void onComplete(List<NotificationItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    public static void getNotificationUnReadCount(Context context, Response.Status<Integer> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int count = 0;
                List<NotificationItem> mList = getNotificationList(context);
                if(mList != null && mList.size() > 0){
                    for (NotificationItem item : mList){
                        if(!item.isRead()){
                            count+=1;
                        }
                    }
                }
                return count;
            }
        }, new TaskRunner.Callback<Integer>() {
            @Override
            public void onComplete(Integer result) {
                callback.onSuccess(result);
            }
        });
    }

    public static boolean isNotificationUnique(Context context, String notificationId) {
        return !isNotificationExist(context, notificationId);
    }

    public static boolean isNotificationExist(Context context, String notificationId) {
        if(context == null) return false;
        List<NotificationItem> mList = getNotificationList(context);
        if(mList != null && mList.size() > 0){
            for (NotificationItem item : mList){
                if(item.getUuid().equals(notificationId)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void updateReadStatus(Context context, String notificationIdOrUUId) {
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<NotificationItem> mList = getNotificationList(context);
                if(mList != null && mList.size() > 0){
                    for (NotificationItem item : mList){
                        if(item.getUuid().equals(notificationIdOrUUId)){
                            item.setRead(true);
                        }
                    }
                    saveList(context, KEY_NOTIFICATION, mList);
                }
                return true;
            }
        });
    }

    public static void removeItem(Context context, String uuid, TaskRunner.Callback<Boolean> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<NotificationItem> mList = getNotificationList(context);
                if(mList != null && mList.size() > 0){
                    for(Iterator<NotificationItem> i = mList.iterator(); i.hasNext();) {
                        if (i.next().getUuid().equals(uuid)) {
                            i.remove();
                        }
                    }
                    if(mList.size() > 0) {
                        saveList(context, KEY_NOTIFICATION, mList);
                    }else {
                        clear(context, KEY_NOTIFICATION);
                    }
                }
                return true;
            }
        }, callback);
    }
}
