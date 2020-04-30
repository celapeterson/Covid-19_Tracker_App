package com.example.covid_19symptomtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        Log.i("1", "Schedule");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(AppNotification.NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);

    }
}
