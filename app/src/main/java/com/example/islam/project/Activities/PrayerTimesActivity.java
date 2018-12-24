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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.islam.project.AzkarParam;
import com.example.islam.project.Constants;
import com.example.islam.project.Fragments.AddFragmentHandler;
import com.example.islam.project.Fragments.AzkarFragment;
import com.example.islam.project.Fragments.BackButtonSupportFragment;
import com.example.islam.project.Fragments.MyFragment;
import com.example.islam.project.Fragments.PrayerTimesFragment;
import com.example.islam.project.Fragments.TunesFragment;
import com.example.islam.project.R;

public class PrayerTimesActivity extends MyActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private CoordinatorLayout prayersCoordinatorLayout;
    private PrayerTimesFragment prayerTimesFragment;
    private TunesFragment tunesFragment;
    private AzkarFragment azkarFragment;

    private FragmentManager fragmentManager;
    private AddFragmentHandler fragmentHandler;
    private final View.OnClickListener navigationBackPressListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            fragmentManager.popBackStack();
        }
    };

    private final FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            onBackStackChangedEvent();
        }
    };

    private void onBackStackChangedEvent() {
        syncDrawerToggleState();
    }
    private void syncDrawerToggleState() {
        if (drawerToggle == null) {
            return;
        }
        if (fragmentManager.getBackStackEntryCount() > 1) {
            drawerToggle.setDrawerIndicatorEnabled(false);
            drawerToggle.setToolbarNavigationClickListener(navigationBackPressListener); //pop backstack
        } else {
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.setToolbarNavigationClickListener(drawerToggle.getToolbarNavigationClickListener()); //open nav menu drawer
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_times);
        mBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constants.ACTION_CALL_SUCCESS)){
                    observer.saveParams();
                    goToPrayerFragment();
                }
                else if(intent.getAction().equals(Constants.ACTION_CALL_FAILED)){
                    Log.d(Constants.TAG, "service failed");
                    serviceFailed();
                    finish();
                }
            }
        };
        initializeBroadcastReceiver();

        toolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setupDrawerAndToggle();
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        fragmentFrame = R.id.content_frame;
        fragmentManager = getSupportFragmentManager();
        fragmentHandler = new AddFragmentHandler(fragmentManager, fragmentFrame);
        fragmentManager.addOnBackStackChangedListener(backStackListener);

        prayerTimesFragment = new PrayerTimesFragment();
        tunesFragment = new TunesFragment();
        azkarFragment = new AzkarFragment();


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
        fragmentHandler.add(prayerTimesFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                syncDrawerToggleState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                syncDrawerToggleState();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
    @Override
    protected void onDestroy() {
        fragmentManager.removeOnBackStackChangedListener(backStackListener);
        fragmentManager = null;
        super.onDestroy();
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
    public void goToLoadingFragment() {
        loadingFragment.setLoadingMessage(getString(R.string.please_wait));
        loadingFragment.setMode(false);
        fragmentHandler.add(loadingFragment);
    }
    @Override
    public void goToLocationSettingFrament() {
        loadingFragment.setMode(true);
        fragmentHandler.add(loadingFragment);
    }

    @Override
    public void goToPrayerFragment() {
        try {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(prayerTimesFragment).commit();
            prayerTimesFragment = new PrayerTimesFragment();
            fragmentHandler.add(prayerTimesFragment);
        } catch (Exception ex){
            Log.e(Constants.TAG, ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void goToTuneFragment() {
        fragmentHandler.add(tunesFragment);
    }

    @Override
    public void goToCalcMethodFragment() {
        fragmentHandler.add(calcMethodFragment);
    }

    @Override
    public void goToAfterPrayerAzkarFragment() {
        azkarFragment.setTitle(getString(R.string.after_prayer_azkar));
        azkarFragment.setAzkarParam(new AzkarParam(R.array.arabic, R.array.azkar_english, R.array.pronunciation, R.array.azkar_reference));
        fragmentHandler.add(azkarFragment);
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

    @Override
    public void onBackPressed() {
        if (sendBackPressToDrawer()) {
            return;
        }

        if (sendBackPressToFragmentOnTop()) {
            return;
        }

        super.onBackPressed();

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            return;
        }
        finish();
    }

    private boolean sendBackPressToDrawer() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private boolean sendBackPressToFragmentOnTop() {
        MyFragment fragmentOnTop = fragmentHandler.getCurrentFragment();
        if (fragmentOnTop == null) {
            return false;
        }
        return fragmentOnTop.onBackPressed();
    }

    private void setupDrawerAndToggle() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setDrawerIndicatorEnabled(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
}
