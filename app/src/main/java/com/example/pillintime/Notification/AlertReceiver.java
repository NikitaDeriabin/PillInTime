package com.example.pillintime.Notification;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pillintime.Models.Reminder;
import com.example.pillintime.Notification.NotificationHelper;

import java.util.Calendar;


public class AlertReceiver extends BroadcastReceiver {
    public static Reminder reminder;
    private final int SUMMARY_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        int randId = (int) ((Calendar.getInstance().getTimeInMillis() / 1000L) % Integer.MAX_VALUE);

        NotificationHelper mNotificationHelper = new NotificationHelper(context);

        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(reminder.getName(),
                reminder.getAlarmTimeList().get(0).getAlarmTimeStr() + " It's time to take medicine!");
        mNotificationHelper.getManager().notify(randId, nb.build());

//        NotificationCompat.Builder nbSummary = mNotificationHelper.getSummaryNotification();
//        mNotificationHelper.getManager().notify(SUMMARY_ID, nbSummary.build());


    }
}
