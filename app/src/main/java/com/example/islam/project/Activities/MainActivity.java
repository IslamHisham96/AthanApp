package com.example.islam.project.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.islam.project.Constants;
import com.example.islam.project.Fragments.CalcMethodFragment;
import com.example.islam.project.Fragments.LoadingFragment;
import com.example.islam.project.MyActivity;
import com.example.islam.project.Observers.ParamsObserver;
import com.example.islam.project.Observers.ParamsSubject;
import com.example.islam.project.OnFragmentInteractionListener;
import com.example.islam.project.R;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener, ParamsSubject, MyActivity {

    private CoordinatorLayout mainCoordinatorLayout;
    private LoadingFragment loadingFragment;
    private CalcMethodFragment calcMethodFragment;
    private ParamsObserver observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainCoordinatorLayout = findViewById(R.id.mainCoordinatorLayout);
        loadingFragment = new LoadingFragment();
        calcMethodFragment = new CalcMethodFragment();
        setObserver(new ParamsObserver(this));
        Log.d(Constants.TAG,"here");
            goToLoadingFragment();

            /*else{
                if (checkPermissions(PERMISSIONS)) {

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
                    locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(), true), Constants.MIN_TIME_BW_UPDATES,

                            Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, loadingFragment);
                }
            }*/

    }

    public void changeSettingsLocation() {

    }

    public void changeSettingsCalcMethod() {

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
                .replace(R.id.mainCoordinatorLayout, loadingFragment)
                .commit();
    }

    @Override
    public void goToCalcMethodFragment() {
        Log.d("MyTag","howhowhow");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(R.id.mainCoordinatorLayout, calcMethodFragment)
                .commit();
    }

    @Override
    public void CalcMethodSet(int index) {
        notifyWithUpdate(Constants.CALC_METHOD_OBSERVED, index);
        finishSettings();
    }
    @Override
    public void LocationSet(Location location) {
        Log.d("MyTag","at3'ada");
        notifyWithUpdate(Constants.LOCATION_OBSERVED, location.getLatitude()+","+location.getLongitude());
        Log.d("MyTag","after");
        //if(firstTime)
            goToCalcMethodFragment();
    }
    public void finishSettings(){
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
