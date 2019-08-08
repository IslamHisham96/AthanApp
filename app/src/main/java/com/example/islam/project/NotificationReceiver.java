package com.example.islam.project;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.islam.project.Activities.PrayerTimesActivity;
import com.example.islam.project.Services.PrayerTimeElapsedService;

public class NotificationReceiver extends BroadcastReceiver {
    public static String CHANNEL_ID = "com.example.islam.project.channel_id";
    private static int notificationId = 66;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("receiver", "received");
        Bundle extras = intent.getExtras();
        if(extras == null) return;
        String textTitle = extras.getString(Constants.NOTIFICATION_TITLE);
        String textContent = extras.getString(Constants.NOTIFICATION_CONTENT);
        boolean with_sound = extras.getBoolean(Constants.NOTIFICATION_WITH_SOUND);
        if(textTitle == null || textContent == null) return;

        Intent notificationIntent = new Intent(context, PrayerTimesActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_calcmethod)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, 0))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(Notification.VISIBILITY_PUBLIC);

        if(with_sound) mBuilder
                .setSound(Uri.parse("android.resource://"
                + context.getPackageName() + "/raw/athan"));

        Notification notif = mBuilder.build();

        NotificationManagerCompat.from(context)
                .notify(notificationId, notif);

        if(context instanceof PrayerTimeElapsedService){
            Log.d("Service", "true");
            ((PrayerTimeElapsedService)context).startTimer();
        }
    }
}
