package com.example.islam.project.Fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.islam.project.Constants;
import com.example.islam.project.R;
import static com.example.islam.project.MathUtils.*;


public class QiblaFragment extends MyFragment implements SensorEventListener {

    private OnFragmentInteractionListener mListener;
    private SensorManager sensorManager;
    private Sensor magSensor;
    private Sensor accSensor;
    private ImageView compassImageView;
    private TextView angleTxt;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currentAzimuth = 0f;
    private final float alpha = 0.97f;
    public QiblaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qibla, container, false);
        compassImageView = v.findViewById(R.id.compassImageView);
        angleTxt = v.findViewById(R.id.angleTxt);
        TextView qiblaTxt = v.findViewById(R.id.qiblaTxt);
        qiblaTxt.setText(getString(R.string.qibla_direction, (int)qiblaDir()));
        initSensors();
        return v;
    }
    public static double qiblaDir() {
        double degrees = atan2Deg(sinDeg(Constants.KAABA_LONGITUDE - Constants.longitude),
                cosDeg(Constants.latitude) * tanDeg(Constants.KAABA_LATITUDE)
                        - sinDeg(Constants.latitude) * cosDeg(Constants.KAABA_LONGITUDE - Constants.longitude));
        return degrees >= 0 ? degrees : degrees + 360;
    }
    public void initSensors(){
        try {
            sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
            magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if (magSensor == null) {
                Toast.makeText(getContext(), R.string.sensor_not_found, Toast.LENGTH_SHORT).show();
            }
            accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accSensor == null) {
                Toast.makeText(getContext(), R.string.sensor_not_found, Toast.LENGTH_SHORT).show();
            }
        } catch(NullPointerException ex){
            Toast.makeText(getContext(), R.string.sensor_not_found, Toast.LENGTH_SHORT).show();
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected int getTitleID() {
        return R.string.qibla_title;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                mGravity[0] = alpha*mGravity[0] + (1-alpha)*event.values[0];
                mGravity[1] = alpha*mGravity[1] + (1-alpha)*event.values[1];
                mGravity[2] = alpha*mGravity[2] + (1-alpha)*event.values[2];
            }
            if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mGeomagnetic[0] = alpha*mGeomagnetic[0] + (1-alpha)*event.values[0];
                mGeomagnetic[1] = alpha*mGeomagnetic[1] + (1-alpha)*event.values[1];
                mGeomagnetic[2] = alpha*mGeomagnetic[2] + (1-alpha)*event.values[2];
            }

            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
            if(success){
                float[] orientation = new float[3];
                SensorManager.getOrientation(R,orientation);
                azimuth = (float)Math.toDegrees(orientation[0]);
                azimuth = (azimuth+360)%360;

                Animation animation = new RotateAnimation(-currentAzimuth, -azimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                currentAzimuth = azimuth;
                animation.setDuration(500);
                animation.setRepeatCount(0);
                animation.setFillAfter(true);
                compassImageView.startAnimation(animation);
                angleTxt.setText(formatDegree(currentAzimuth));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
