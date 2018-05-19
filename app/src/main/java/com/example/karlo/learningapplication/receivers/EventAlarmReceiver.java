package com.example.karlo.learningapplication.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.karlo.learningapplication.R;
import com.example.karlo.learningapplication.modules.login.LoginActivity;

public class EventAlarmReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        //String guid = intent.getAction();
        //Topic topic = new Topic();
        Log.e("tagic", intent.getAction());
        postNotification();


        //if (AppState.sharedInstance().isAppInForeground()) {
        //    new android.support.v7.app.AlertDialog.Builder(mContext)
        //            .setMessage("Topic starts in 10 minutes!")
        //            .setPositiveButton(R.string.ok, null)
        //            .show();
//
        //} else {
            postNotification();
        //}
    }

    private void postNotification() {
        Intent notificationIntent = new Intent(mContext, LoginActivity.class);
        //notificationIntent.setAction(IntentConstants.INTENT_ACTION_REMINDER);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-01";
        String channelName = "Channel Name";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = new NotificationCompat.Builder(mContext, channelId)
                .setContentIntent(contentIntent)
                .setContentTitle("Topic is going to start soon!")
                .setContentText("This topic is starting in 10 minutes!")
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(0, notification);
    }

}
