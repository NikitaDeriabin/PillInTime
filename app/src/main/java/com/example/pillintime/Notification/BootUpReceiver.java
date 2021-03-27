package com.example.pillintime.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pillintime.AddReminderActivity;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, AddReminderActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
