package com.sahni.rahul.ieee_niec.helpers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.sahni.rahul.ieee_niec.R;

/**
 * Created by sahni on 28-Dec-17.
 */

public class NotificationHelper {

    private static NotificationManager notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean checkNotificationChannelExist(Context context, String channelId){
        return getNotificationManager(context).getNotificationChannel(channelId) != null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context, String channelId){
        if(!checkNotificationChannelExist(context, channelId)){
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                   context.getString(R.string.default_notification_channel_name) , NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getNotificationManager(context).createNotificationChannel(notificationChannel);
        }
    }

    private static NotificationManager getNotificationManager(Context context){
        if(notificationManager == null){
            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
}
