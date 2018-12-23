package com.example.islam.project;

import android.util.Log;

import java.util.Arrays;

public class AthanCallParams {
    private int year, calc_method, hijri_adj;
    private double longitude, latitude;
    private int[] tune;

    public AthanCallParams(int year, int calc_method, int hijri_adj, double longitude, double latitude, int[] tune) {
        this.year = year;
        this.calc_method = calc_method;
        this.hijri_adj = hijri_adj;
        this.longitude = longitude;
        this.latitude = latitude;
        this.tune = tune;
    }

    public int getYear() {
        return year;
    }

    public int getCalc_method() {
        return calc_method;
    }

    public int getHijri_adj() {
        return hijri_adj;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int[] getTune() {
        return tune;
    }
    public String getLocation(){
        return latitude+","+longitude;
    }
    public static int[] setTuneFromString(String tuneString){
        String[] splitTunes = tuneString.replaceAll(" ","").split(",");
        int [] tunes = new int[splitTunes.length];
        for(int i=0;i<tunes.length;i++)
            tunes[i] = Integer.parseInt(splitTunes[i]);
        Log.d(Constants.TAG, tuneString + " tunes string " + splitTunes.length);
        return tunes;
    }
    public String getTuneString(){
        String tuneString = Arrays.toString(tune).replaceAll(" ", "");
        return tuneString.substring(1, tuneString.length()-1);
    }
}
