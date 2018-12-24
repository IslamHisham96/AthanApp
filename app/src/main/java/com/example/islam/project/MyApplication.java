package com.example.islam.project;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.islam.project.Activities.PrayerTimesActivity;
import com.example.islam.project.Adapters.DBAdapter;
import com.example.islam.project.Services.PrayerTimeElapsedService;

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
    private static DBAdapter myDb;
    private static SharedPreferences sharedPreferences;
    public static String CHANNEL_ID = "com.example.islam.project.channel_id";
    private static int notificationId = 66;


    Intent mServiceIntent;
    private PrayerTimeElapsedService pService;
    //public static int audioID;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        /*
        Log.d(Constants.TAG,"audio"+audioID);
        audioID = getResources().getIdentifier("athan","raw",getPackageName());
        Log.d(Constants.TAG,"audio"+audioID);
        */
        createNotificationChannel();
        //playNotification("hey","man");
        sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        readParams();
        openDB();
        pService = new PrayerTimeElapsedService();
        mServiceIntent = new Intent(this, pService.getClass());
        if (!isMyServiceRunning(pService.getClass())) {
            startService(mServiceIntent);
        }
    }
    //TODO change if possible
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i (Constants.TAG, "isMyServiceRunning? "+true);
                return true;
            }
        }
        Log.i (Constants.TAG, "isMyServiceRunning? "+true);
        return false;
    }
    @Override
    public void onTerminate() {
        closeDB();
        stopService(mServiceIntent);
        super.onTerminate();
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
        editor.putInt(UPDATE_YEAR,year = params.getYear());
        editor.putString(UPDATE_LOCATION,location = params.getLocation());
        latitude = params.getLatitude();
        longitude = params.getLongitude();
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

    public static int playNotification(String textTitle, String textContent, Context ctx){
        Intent intent = new Intent(ctx, PrayerTimesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_calcmethod)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(ctx, 0, intent, 0))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSound(Uri.parse("android.resource://"
                        + context.getPackageName() + "/raw/athan"));

        Notification notif = mBuilder.build();

        NotificationManagerCompat.from(ctx)
                .notify(notificationId, notif);

        return notificationId;
    }
    public static int playNotification(String textTitle, String textContent){
        return playNotification(textTitle,  textContent, getAppContext());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
