package com.example.islam.project.Fragments;

import android.location.Location;

public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void goToLoadingFragment();

    void goToLocationSettingFrament();

    void goToCalcMethodFragment();

    void goToPrayerFragment();

    void goToTuneFragment();

    void CalcMethodSet(int method);

    void LocationSet(Location location);

    void TunesSet(int[] tunes);

    void hijriAdjSet(int hijriAdj);

    void yearSet(int year);

    void finishSettings();
}