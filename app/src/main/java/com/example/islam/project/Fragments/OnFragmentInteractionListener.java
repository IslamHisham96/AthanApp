package com.example.islam.project.Fragments;

import android.location.Location;

public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void goToLoadingFragment();

    void goToCalcMethodFragment();

    void CalcMethodSet(int method);

    void LocationSet(Location location);
}