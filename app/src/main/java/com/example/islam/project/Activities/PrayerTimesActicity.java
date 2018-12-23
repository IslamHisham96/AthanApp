package com.example.islam.project.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.islam.project.AzkarParam;
import com.example.islam.project.Constants;
import com.example.islam.project.Fragments.AzkarFragment;
import com.example.islam.project.Fragments.PrayerTimesFragment;
import com.example.islam.project.Fragments.TunesFragment;
import com.example.islam.project.R;

public class PrayerTimesActicity extends MyActivity {
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout prayersCoordinatorLayout;
    private PrayerTimesFragment prayerTimesFragment;
    private TunesFragment tunesFragment;
    private AzkarFragment azkarFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constants.ACTION_CALL_SUCCESS)){
                    observer.saveParams();
                    goToPrayerFragment();
                }
                else if(intent.getAction().equals(Constants.ACTION_CALL_FAILED)){
                    serviceFailed();
                    goToPrayerFragment();
                }
            }
        };
        initializeBroadcastReceiver();

        setContentView(R.layout.activity_prayer_times);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        prayerTimesFragment = new PrayerTimesFragment();
        tunesFragment = new TunesFragment();
        azkarFragment = new AzkarFragment();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        fragmentFrame = R.id.content_frame;
        prayersCoordinatorLayout = findViewById(R.id.prayersCoordinatorLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        switch (id){
                            case R.id.nav_calcmethod:
                                goToCalcMethodFragment();
                                break;
                            case R.id.nav_location:
                                goToLocationSettingFrament();
                                break;
                            case R.id.nav_tunes:
                                goToTuneFragment();
                                break;
                            case R.id.nav_supplication:
                                goToAfterPrayerAzkarFragment();
                                break;
                        }
                        return true;
                    }
                });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, prayerTimesFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLocationSettings() {
        Snackbar snackbar = Snackbar
                .make(prayersCoordinatorLayout, R.string.location_error,Snackbar.LENGTH_INDEFINITE)
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
    public void goToPrayerFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, prayerTimesFragment)
                .commit();
    }

    @Override
    public void goToTuneFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, tunesFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToCalcMethodFragment() {
        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
            .replace(fragmentFrame, calcMethodFragment)
            .addToBackStack(null)
            .commit();
    }

    @Override
    public void goToAfterPrayerAzkarFragment() {
        azkarFragment.setTitle(getString(R.string.after_prayer_azkar));
        azkarFragment.setAzkarParam(new AzkarParam(R.array.arabic, R.array.azkar_english, R.array.pronunciation, R.array.azkar_reference));
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, azkarFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void LocationSet(Location location) {
        super.LocationSet(location);
        finishSettings();
    }

    @Override
    public void CalcMethodSet(int method) {
        super.CalcMethodSet(method);
        finishSettings();
    }

    @Override
    public void yearSet(int year) {
        super.yearSet(year);
        finishSettings();
    }
}
