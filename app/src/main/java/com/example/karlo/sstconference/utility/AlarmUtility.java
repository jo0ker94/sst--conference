package com.example.karlo.sstconference.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

public class AlarmUtility {

    /**
     * Schedules an exact, non-repeating alarm at a specific time.
     * @param context
     * @param calendar
     * 	Defines the time at which the alarm should go off.
     * @param guid
     * 	Guid for this alarm, set as its action. Its content is not strictly defined.
     * @param receiverClass
     * 	BroadcastReceiver subclass that will receive the alarm.
     */
    public static void scheduleAlarm(Context context, Calendar calendar, String guid,
                                     Class<? extends BroadcastReceiver> receiverClass) {
        Intent intent = new Intent(context, receiverClass);
        intent.setAction(guid);
        PendingIntent operation = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), operation);
    }

    public static void cancelAlarm(Context context, String guid,
                                   Class<? extends BroadcastReceiver> receiverClass, Bundle extras) {
        Intent intent = new Intent(context, receiverClass);
        intent.setAction(guid);
        if (extras != null) {
            intent.putExtras(extras);
        }
        PendingIntent operation = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(operation);
    }

    //public static void cancelAllReminderAlarms(Context context) throws SQLException {
    //        Intent broadcast = new Intent(IntentConstants.INTENT_ACTION_CANCEL_ALL_REMINDERS);
    //        broadcast.putStringArrayListExtra(IntentConstants.BUNDLE_REMINDER, guids);
    //        context.sendBroadcast(broadcast);
    //}
}
