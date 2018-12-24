package com.example.islam.project;

import android.content.SharedPreferences;

public class Constants {
    public static int calcMethod = 1;
    public static String location = "0.0,0.0";
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    public static int year = 2018;
    public static int hijriAdj = 0;
    public static int[] tune = new int[]{0,0,0,0,0,0};
    public static String tuneString = "0,0,0,0,0,0";
    public static final int CALC_METHOD_OBSERVED = 0;
    public static final int LOCATION_OBSERVED = 1;
    public static final int YEAR_OBSERVED = 2;
    public static final int HIJRI_ADJ_OBSERVED = 3;
    public static final int TUNES_OBSERVED = 4;
    public static final int PRAYERS_COUNT = 6;
    public static final int MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    public static final int MIN_TIME_BW_UPDATES =  0;
    public static final int MIN_TUNE = -10;
    public static final int MAX_TUNE = 10;
    public static final double KAABA_LATITUDE = 21.42250833;
    public static final double KAABA_LONGITUDE = 39.82616111;
    public static final String[] TIMINGS = new String[]{"Fajr","Sunrise","Dhuhr","Asr","Maghrib","Isha"};

    public static final String TAG = "MyTag";
    public static final String ATHAN_CALL = "athan_call";
    public static final String ACTION_CALL_SUCCESS = "com.example.islam.project.close_action";
    public static final String ACTION_CALL_FAILED = "com.example.islam.project.failed_action";
    public static final int TIMEOUT = 20;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String FIRST_TIME = "firstTime";
    public static final String UPDATE_CALC_METHOD = "updateCalcMethod";
    public static final String UPDATE_LAT = "updateLatitude";
    public static final String UPDATE_YEAR = "updateYear";
    public static final String UPDATE_LOCATION = "updateLocation";
    public static final String UPDATE_LONG = "updateLongitude";
    public static final String UPDATE_HIJRI_ADJ = "updateHijriAdj";
    public static final String UPDATE_TUNES = "updateTunes";
}
