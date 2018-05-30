package com.example.karlo.sstconference.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.database.LocalDatabase;
import com.example.karlo.sstconference.database.topic.LocalTopicDataSource;
import com.example.karlo.sstconference.database.topic.TopicDataSource;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.modules.login.LoginActivity;
import com.example.karlo.sstconference.servertasks.RetrofitUtil;
import com.example.karlo.sstconference.servertasks.interfaces.ProgramApi;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class SendReminderService extends IntentService {

    private final String TAG = "SendReminderService";

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private TopicDataSource mDataSource;

    public SendReminderService() {
        super("SendReminderService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDataSource = new LocalTopicDataSource(
                LocalDatabase.getDatabase(this)
                        .topicModel(),
                RetrofitUtil.getRetrofit(Constants.FIREBASE_BASE_URL)
                        .create(ProgramApi.class));
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            String eventId = intent.getExtras().getString(Constants.ID);
            mCompositeDisposable.add(mDataSource.getTopics()
                    .flatMap(Observable::fromIterable)
                    .filter(topic -> topic.getId() == Integer.valueOf(eventId))
                    .firstElement()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::postNotification));
        }
    }

    private void postNotification(Topic topic) {
        //if (isAppInForeground()) {
        //    android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
        //            .setTitle(topic.getTitle())
        //            .setMessage(getString(R.string.topic_starting_in_fifteen_minutes))
        //            .setPositiveButton(R.string.ok, null)
        //            .create();
        //    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        //    dialog.show();
        //
        //} else {
        //      postNotification(topic);
        //}
        Intent notificationIntent = new Intent(this, LoginActivity.class);
        //notificationIntent.setAction(IntentConstants.INTENT_ACTION_REMINDER);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-01";
        String channelName = "Channel Name";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentIntent(contentIntent)
                .setContentTitle(topic.getTitle())
                .setContentText(getString(R.string.topic_starting_in_fifteen_minutes))
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(0, notification);
    }

    private boolean isAppInForeground() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE)
        {
            return true;
        }

        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        // App is foreground, but screen is locked, so show notification
        return km.inKeyguardRestrictedInputMode();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mCompositeDisposable.clear();
    }
}
