package com.example.islam.project.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.islam.project.Constants;

public class ServiceRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(Constants.TAG, "Service restarting");
        context.startService(new Intent(context, PrayerTimeElapsedService.class));;
    }
}
