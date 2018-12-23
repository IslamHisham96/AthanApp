package com.example.islam.project.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.islam.project.Constants;
import com.example.islam.project.Activities.MyActivity;
import com.example.islam.project.MyApplication;
import com.example.islam.project.R;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class LoadingFragment extends Fragment implements LocationListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public static int PERMISSIONS_REQUEST_TAG = 88;
    public static String[] PERMISSIONS = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};

    // TODO: Rename and change types of parameters
    private String loadingMessage = "";
    private ProgressBar loadingProgressBar;
    private TextView loadingTextView;
    private OnFragmentInteractionListener mListener;
    private LocationManager locationManager;
    private MyActivity activity;
    private boolean enable_pos = false;
    private boolean mode_gps = true;

    public LoadingFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_loading, container, false);
        loadingTextView = v.findViewById(R.id.loadingTextView);
        loadingTextView.setText(loadingMessage);
        loadingProgressBar = v.findViewById(R.id.loadingProgressBar);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (mode_gps) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                setLoadingMessage(getResources().getString(R.string.enable_location));
                activity.showLocationSettings();
            } else
                checkPermissions();
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof MyActivity) {
            activity = (MyActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MyActivity");
        }

    }

    public void loadingComplete() {
        loadingProgressBar.setVisibility(View.INVISIBLE);
        loadingTextView.setText(R.string.location_got);

    }

    public void setLoadingMessage(String loadingMessage) {
        //Log.d(Constants.TAG,loadingMessage);
        this.loadingMessage = loadingMessage;
        if (loadingTextView != null)
            loadingTextView.setText(loadingMessage);
        if(loadingProgressBar != null)
            loadingProgressBar.setVisibility(View.VISIBLE);

    }

    public void setMode(boolean mode) {
        this.mode_gps = mode;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (locationManager != null)
            locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Constants.TAG, "location dude out");
        if (enable_pos) {
            Log.d(Constants.TAG, "location dude");
            loadingComplete();
            mListener.LocationSet(location);
            enable_pos = false;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private boolean checkPermissions(String[] permissionsId) {
        setLoadingMessage(getResources().getString(R.string.waiting_permissions));
        boolean permissions = true;
        for (final String permission : permissionsId) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissions = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        permission)) {

                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.permissions_title)
                            .setMessage(R.string.location_permissions_message)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    ActivityCompat.requestPermissions(getActivity(),
                                            new String[]{permission},
                                            PERMISSIONS_REQUEST_TAG);
                                }
                            })
                            .create()
                            .show();
                } else {
                    // probably first time, request permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{permission},
                            PERMISSIONS_REQUEST_TAG);
                }
            }
        }
        Log.d(Constants.TAG, permissions + " done");
        return permissions;
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (mode_gps && provider.equals(LocationManager.GPS_PROVIDER)) {
            checkPermissions();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void checkPermissions() {
        if (checkPermissions(PERMISSIONS)) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Log.d(Constants.TAG, "after checkpermissions");
            setLoadingMessage(getResources().getString(R.string.waiting_location));
            Object[] params = getLastKnownLocation();
            Location location = (Location)params[0];
            String bestProvider = (String)params[1];
            enable_pos = true;
            if(location != null)
                onLocationChanged(location);
            Log.d(Constants.TAG, "waiting and enable_pos");
            locationManager.requestLocationUpdates(bestProvider, Constants.MIN_TIME_BW_UPDATES,

                    Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        }
    }

    private Object[] getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        String bestProvider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        for (String provider : providers) {

            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: l
                bestLocation = l;
                bestProvider = provider;
            }
        }
        return new Object[] {bestLocation, bestProvider};
    }
}
