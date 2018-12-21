package com.example.islam.project.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.islam.project.Fragments.PrayerTimesFragment;
import com.example.islam.project.R;

public class PrayerTimesActicity extends MyActivity {
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout prayersCoordinatorLayout;
    private PrayerTimesFragment prayerTimesFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_times);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        prayerTimesFragment = new PrayerTimesFragment();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        fragmentFrame = R.id.content_frame;
        prayersCoordinatorLayout = findViewById(R.id.prayersCoordinatorLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch (id){
                            case R.id.nav_calcmethod:
                                break;
                            case R.id.nav_location:
                                break;
                            case R.id.nav_tunes:
                                break;
                        }
                        return true;
                    }
                });
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame, prayerTimesFragment).commit();
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
                .make(prayersCoordinatorLayout, R.string.location_error,

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

    }

    @Override
    public void LocationSet(Location location) {

    }
}
