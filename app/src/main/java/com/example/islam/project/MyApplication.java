package com.example.islam.project;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import static com.example.islam.project.Constants.MY_PREFS_NAME;

public class MyApplication extends Application {
    private static Context context;
    public static DBAdapter myDb;
    public static SharedPreferences sharedPreferences;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
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
}
