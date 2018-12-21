package com.example.islam.project;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.islam.project.Constants.MY_PREFS_NAME;
import static com.example.islam.project.Constants.UPDATE_CALC_METHOD;
import static com.example.islam.project.Constants.UPDATE_HIJRI_ADJ;
import static com.example.islam.project.Constants.UPDATE_LOCATION;
import static com.example.islam.project.Constants.UPDATE_TUNES;
import static com.example.islam.project.Constants.UPDATE_YEAR;
import static com.example.islam.project.Constants.calcMethod;
import static com.example.islam.project.Constants.hijriAdj;
import static com.example.islam.project.Constants.latitude;
import static com.example.islam.project.Constants.location;
import static com.example.islam.project.Constants.longitude;
import static com.example.islam.project.Constants.tune;
import static com.example.islam.project.Constants.tuneString;
import static com.example.islam.project.Constants.year;

public class MyApplication extends Application {
    private static Context context;
    public static DBAdapter myDb;
    public static SharedPreferences sharedPreferences;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        readParams();
        openDB();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        closeDB();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB(){
        myDb.close();
    }
    public static void addRecord(DBElement dbElement){
        myDb.insertRow(dbElement);
    }
    public static void deleteAllRecords(){
        myDb.deleteAll();
    }
    public static List<String[]> displayRecordSet() {
        Cursor cursor = myDb.getAllRows();
        // populate the message from the cursor
        ArrayList<String[]> myDataset = new ArrayList<>();
        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String event = cursor.getString(DBAdapter.COL_EVENT);
                //String date = cursor.getString(DBAdapter.COL_DATE);
                String time = cursor.getString(DBAdapter.COL_TIME);
                myDataset.add(new String[]{event,time});
                // Create arraylist(s)? and use it(them) in the list view
            } while(cursor.moveToNext());
        }
        cursor.close();
        return myDataset;
    }
    public static List<String[]> displayRecordSetFromTo(int from, int to) {
        Cursor cursor = myDb.getAllRows();
        Log.d(Constants.TAG,myDb.getCount()+"");
        // populate the message from the cursor
        ArrayList<String[]> myDataset = new ArrayList<>();
        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            int id = 0;
            do {
                // Process the data:
                if(id>=from && id<to) {
                    String event = cursor.getString(DBAdapter.COL_EVENT);
                    String date = cursor.getString(DBAdapter.COL_DATE);
                    String time = cursor.getString(DBAdapter.COL_TIME);
                    myDataset.add(new String[]{event, time, date});
                }
                id++;
                // Create arraylist(s)? and use it(them) in the list view
            } while(cursor.moveToNext());
        }
        cursor.close();
        return myDataset;
    }

    public static void readParams(){
        calcMethod = sharedPreferences.getInt(UPDATE_CALC_METHOD, calcMethod);
        hijriAdj = sharedPreferences.getInt(UPDATE_HIJRI_ADJ, hijriAdj);
        location = sharedPreferences.getString(UPDATE_LOCATION, location);
        String [] temp = location.split(",");
        latitude = Double.parseDouble(temp[0]);
        longitude = Double.parseDouble(temp[1]);
        year = sharedPreferences.getInt(UPDATE_YEAR, year);
        tuneString = sharedPreferences.getString(UPDATE_TUNES,tuneString);
        tune = AthanCallParams.setTuneFromString(tuneString);
    }
    public static void saveParams(AthanCallParams params){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(UPDATE_CALC_METHOD,calcMethod = params.getCalc_method());
        editor.putInt(UPDATE_HIJRI_ADJ,hijriAdj = params.getHijri_adj());
        editor.putString(UPDATE_LOCATION,params.getLocation());
        latitude = params.getLatitude();
        longitude = params.getLongitude();
        editor.putInt(UPDATE_YEAR,params.getYear());
        tuneString = params.getTuneString();
        editor.putString(UPDATE_TUNES,tuneString);
        tune = params.getTune();
        editor.apply();
        Toast.makeText(getAppContext(),R.string.saved,Toast.LENGTH_SHORT).show();
    }

    public static boolean getFirst(){
        return sharedPreferences.getBoolean(Constants.FIRST_TIME, true);
    }
    public static void setFirst(boolean first){
        sharedPreferences.edit().putBoolean(Constants.FIRST_TIME, first).apply();
    }
}
