package com.example.karlo.sstconference.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.karlo.sstconference.commons.Constants;
import com.example.karlo.sstconference.service.SendReminderService;

public class EventAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context.getApplicationContext(), SendReminderService.class);
        serviceIntent.putExtra(Constants.ID, intent.getAction());
        context.getApplicationContext().startService(serviceIntent);
    }
}
