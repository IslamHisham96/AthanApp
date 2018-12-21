package com.example.islam.project.Activities;

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
import com.example.islam.project.Fragments.CalcMethodFragment;
import com.example.islam.project.Fragments.LoadingFragment;
import com.example.islam.project.MyApplication;
import com.example.islam.project.Observers.ParamsObserver;
import com.example.islam.project.Observers.ParamsSubject;
import com.example.islam.project.R;


public class MainActivity extends MyActivity implements ParamsSubject {

    private CoordinatorLayout mainCoordinatorLayout;
    private ParamsObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentFrame = R.id.mainCoordinatorLayout;
        mainCoordinatorLayout = findViewById(R.id.mainCoordinatorLayout);
        loadingFragment = new LoadingFragment();
        calcMethodFragment = new CalcMethodFragment();
        setObserver(new ParamsObserver(this));
        Log.d(Constants.TAG,"here");
        loadingFragment.setMode(true);
        goToLoadingFragment();
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
    public void CalcMethodSet(int index) {
        notifyWithUpdate(Constants.CALC_METHOD_OBSERVED, index);
        finishSettings();
    }
    @Override
    public void LocationSet(Location location) {
        //Log.d("MyTag","at3'ada");
        notifyWithUpdate(Constants.LOCATION_OBSERVED, location.getLatitude()+","+location.getLongitude());
        //Log.d("MyTag","after");
        //if(firstTime)
        goToCalcMethodFragment();
    }
    public void finishSettings(){
        loadingFragment.setLoadingMessage(getString(R.string.please_wait));
        loadingFragment.setMode(false);
        goToLoadingFragment();
        MyApplication.setFirst(false);
        observer.sendRequest();
    }

    @Override
    public void setObserver(ParamsObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyWithUpdate(int id, Object update) {
        observer.update(id, update);
    }
}
