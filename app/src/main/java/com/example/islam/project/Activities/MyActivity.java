package com.example.islam.project.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.islam.project.Constants;
import com.example.islam.project.Fragments.CalcMethodFragment;
import com.example.islam.project.Fragments.LoadingFragment;
import com.example.islam.project.Fragments.OnFragmentInteractionListener;
import com.example.islam.project.R;

public abstract class MyActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    protected LoadingFragment loadingFragment;
    protected CalcMethodFragment calcMethodFragment;
    protected int fragmentFrame;
    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.ACTION_CLOSE)){
                finish();
            }
        }
    };


    public abstract void showLocationSettings();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.ACTION_CLOSE);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), R.string.permissions_not_granted, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        loadingFragment.checkPermissions();
    }

    @Override
    public void goToLoadingFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, loadingFragment)
                .commit();
    }

    @Override
    public void goToCalcMethodFragment() {
        //Log.d("MyTag","at3asha");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, calcMethodFragment)
                .commit();
    }


}
