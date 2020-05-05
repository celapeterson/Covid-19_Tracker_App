package com.example.covid_19symptomtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class StartingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
    }

    public void startTrackerOnClick(View view) {
        scheduleNotification(getNotification());

        Intent intent = new Intent(this, SymptomTrackerActivity.class);
        startActivity(intent);
    }

    private void scheduleNotification(Notification notification) {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra(AppNotification.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmIntent != null && alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
        if (alarmManager != null)
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()
                    + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    private Notification getNotification() {
        Intent activityIntent = new Intent(this, StartingScreenActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0 ,
                activityIntent, 0);

        return new NotificationCompat.Builder(this, AppNotification.NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_local_hospital_black_24dp)
                .setContentTitle("Symptoms")
                .setContentText("Check in and track your symptoms.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
    }

    public void resultsButtonOnClick(View view) {
        Intent intent = new Intent(this, PastResults.class);
        startActivity(intent);

    }

    public void mapButtonOnClick(View view) {
        Intent intent = new Intent(this, HeatMapActivity.class);
        startActivity(intent);
    }
}
