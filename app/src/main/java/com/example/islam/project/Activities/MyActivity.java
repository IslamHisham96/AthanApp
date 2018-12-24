package com.example.islam.project.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.islam.project.Constants;
import com.example.islam.project.Fragments.CalcMethodFragment;
import com.example.islam.project.Fragments.LoadingFragment;
import com.example.islam.project.Fragments.OnFragmentInteractionListener;
import com.example.islam.project.Observers.ParamsObserver;
import com.example.islam.project.Observers.ParamsSubject;
import com.example.islam.project.R;

public abstract class MyActivity extends AppCompatActivity implements OnFragmentInteractionListener, ParamsSubject {
    protected LoadingFragment loadingFragment;
    protected CalcMethodFragment calcMethodFragment;
    protected int fragmentFrame;
    protected ParamsObserver observer;
    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mBroadcastReceiver;


    public abstract void showLocationSettings();

    public void initializeBroadcastReceiver(){
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.ACTION_CALL_SUCCESS);
        mIntentFilter.addAction(Constants.ACTION_CALL_FAILED);
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
    }
    public void serviceFailed(){
        Toast.makeText(this, R.string.service_failed, Toast.LENGTH_LONG).show();
    }
    @Override
    public void finishSettings(){
        goToLoadingFragment();
        observer.sendRequest();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingFragment = new LoadingFragment();
        calcMethodFragment = new CalcMethodFragment();
        setObserver(new ParamsObserver(this));
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
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
            return;
        }
        loadingFragment.checkPermissions();
    }



    @Override
    public void goToCalcMethodFragment() {
        //Log.d("MyTag","at3asha");
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, calcMethodFragment)
                .commit();
    }

    @Override
    public void goToLoadingFragment() {
        loadingFragment.setLoadingMessage(getString(R.string.please_wait));
        loadingFragment.setMode(false);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, loadingFragment)
                .commit();
    }
    @Override
    public void goToLocationSettingFrament() {
        loadingFragment.setMode(true);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, loadingFragment)
                .commit();
    }

    @Override
    public void goToPrayerFragment() {}

    @Override
    public void goToTuneFragment() {}

    @Override
    public void goToQiblaFragment() {}

    @Override
    public void goToAfterPrayerAzkarFragment() {}

    @Override
    public void CalcMethodSet(int method) {
        notifyWithUpdate(Constants.CALC_METHOD_OBSERVED, method);
    }

    @Override
    public void LocationSet(Location location) {
        //Log.d("MyTag","at3'ada");
        notifyWithUpdate(Constants.LOCATION_OBSERVED, location.getLatitude()+","+location.getLongitude());
        //Log.d("MyTag","after");
    }

    @Override
    public void TunesSet(int[] tunes) {
        notifyWithUpdate(Constants.TUNES_OBSERVED, tunes);
    }

    @Override
    public void hijriAdjSet(int hijriAdj) {
        notifyWithUpdate(Constants.HIJRI_ADJ_OBSERVED, hijriAdj);
    }

    @Override
    public void yearSet(int year) {
        notifyWithUpdate(Constants.YEAR_OBSERVED, year);
    }

    @Override
    public void setObserver(ParamsObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyWithUpdate(int id, Object update) {
        observer.update(id, update);
    }

    public int getFragmentFrame(){
        return fragmentFrame;
    }
}
