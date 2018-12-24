package com.example.islam.project.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.islam.project.Constants;
import com.example.islam.project.MyApplication;
import com.example.islam.project.R;

import java.util.Calendar;


public class MainActivity extends MyActivity {

    private CoordinatorLayout mainCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentFrame = R.id.mainCoordinatorLayout;
        mainCoordinatorLayout = findViewById(R.id.mainCoordinatorLayout);
        mBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constants.ACTION_CALL_SUCCESS)){
                    observer.saveParams();
                    MyApplication.setFirst(false);
                    Intent i = new Intent(getApplicationContext(), PrayerTimesActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(intent.getAction().equals(Constants.ACTION_CALL_FAILED)){
                    serviceFailed();
                    finish();
                }
            }
        };
        initializeBroadcastReceiver();
        Log.d(Constants.TAG,"here");
        goToLocationSettingFrament();
    }
    @Override
    public void showLocationSettings() {
        Snackbar snackbar = Snackbar
                .make(mainCoordinatorLayout, R.string.location_error,

                        Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.enable, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = sbView

                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }










    @Override
    public void CalcMethodSet(int method) {
        super.CalcMethodSet(method);
        yearSet(Calendar.getInstance().get(Calendar.YEAR));
        finishSettings();
    }
    @Override
    public void LocationSet(Location location) {
        super.LocationSet(location);
        goToCalcMethodFragment();
    }


}
