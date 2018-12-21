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
import com.example.islam.project.MyActivity;
import com.example.islam.project.OnFragmentInteractionListener;
import com.example.islam.project.R;

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

    public LoadingFragment() {
    }
    // TODO: Rename and change types and number of parameters
    public static LoadingFragment newInstance(String param1, String param2) {
        LoadingFragment fragment = new LoadingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loadingMessage = getArguments().getString(ARG_PARAM1);
        }
        Log.d("MyTag","oncreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(Constants.TAG,"hey man");
        View v = inflater.inflate(R.layout.fragment_loading, container, false);
        loadingTextView = v.findViewById(R.id.loadingTextView);
        loadingTextView.setText(loadingMessage);
        loadingProgressBar = v.findViewById(R.id.loadingProgressBar);
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
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setLoadingMessage(getResources().getString(R.string.waiting_location));
            activity.showLocationSettings();
        }
        else
            checkPermissions();
    }
    public void loadingComplete(){
        loadingProgressBar.setVisibility(View.INVISIBLE);
        loadingTextView.setText(R.string.location_got);

    }
    public void setLoadingMessage(String loadingMessage){
        //Log.d(Constants.TAG,loadingMessage);
        this.loadingMessage = loadingMessage;
        if(loadingTextView!=null)
            loadingTextView.setText(loadingMessage);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(locationManager != null)
            locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (enable_pos) {
            Log.d("MyTag","location dude");
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
        Log.d("MyTag",permissions+" done");
        return permissions;
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            checkPermissions();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void checkPermissions(){
        if (checkPermissions(PERMISSIONS)) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                    Constants.MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            enable_pos = true;
        }
    }
}
