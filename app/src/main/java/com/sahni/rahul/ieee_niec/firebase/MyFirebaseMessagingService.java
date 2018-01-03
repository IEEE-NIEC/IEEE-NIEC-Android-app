package com.sahni.rahul.ieee_niec.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sahni.rahul.ieee_niec.R;
import com.sahni.rahul.ieee_niec.activity.MainActivity;
import com.sahni.rahul.ieee_niec.helpers.ContentUtils;
import com.sahni.rahul.ieee_niec.helpers.NotificationHelper;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    private boolean isDataAvailable = false;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getData());
        }
    }


    private void showNotification(String messageBody, Map<String, String> dataPayload) {

        String title = "";

        Intent intent = new Intent(this, MainActivity.class);
        if (dataPayload != null) {
            String dataKey = dataPayload.get(ContentUtils.NOTIFICATION_DATA_PAYLOAD_KEY);
            intent.putExtra(ContentUtils.NOTIFICATION_DATA_PAYLOAD_KEY, dataKey);
            title = dataPayload.get(ContentUtils.NOTIFICATION_TITLE_KEY);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createNotificationChannel(this, getString(R.string.default_notification_channel_id));
        }

//        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentText(messageBody)
                .setColor(getResources().getColor(R.color.notification_color))
                .setContentTitle(""+title)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
