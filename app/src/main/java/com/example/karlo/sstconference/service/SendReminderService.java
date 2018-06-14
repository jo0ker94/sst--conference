package com.example.karlo.sstconference.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.karlo.sstconference.App;
import com.example.karlo.sstconference.R;
import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.database.topic.TopicDataSource;
import com.example.karlo.sstconference.mock.MockUtility;
import com.example.karlo.sstconference.models.program.Topic;
import com.example.karlo.sstconference.modules.program.ProgramActivity;
import com.example.karlo.sstconference.utility.MockObject;

import net.globulus.easyprefs.EasyPrefs;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SendReminderService extends IntentService {

    private final String TAG = "SendReminderService";

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Inject
    TopicDataSource mDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) getApplication()).getComponent().inject(this);
    }

    public SendReminderService() {
        super("SendReminderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (MockUtility.isRunningTest()) {
            handleTopic(new Topic(0, 0, "Title", null, 0));
            return;
        }
        if (intent != null && intent.getExtras() != null) {
            String eventId = intent.getExtras().getString(Constants.ID);
            mCompositeDisposable.add(mDataSource.getTopics()
                    .flatMap(Observable::fromIterable)
                    .filter(topic -> topic.getId() == Integer.valueOf(eventId))
                    .firstElement()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleTopic));
        }
    }

    private void handleTopic(Topic topic) {
        if (EasyPrefs.getShowNotifications(this)) {
            postNotification(topic);
        }
    }

    private void postNotification(Topic topic) {
        Intent notificationIntent = new Intent(SendReminderService.this, ProgramActivity.class);
        notificationIntent.putExtra(Constants.INTENT_TOPIC_DETAILS, true);
        notificationIntent.putExtra(Constants.DATA, topic);
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
                .setContentTitle(topic.getTitle())
                .setContentText(getString(R.string.topic_starting_in_fifteen_minutes))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.sst_icon_2017)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.sst_icon_2017))
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(0, notification);
    }
}
