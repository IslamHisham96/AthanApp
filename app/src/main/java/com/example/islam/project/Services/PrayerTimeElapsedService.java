package com.example.islam.project.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.example.islam.project.Activities.PrayerTimesActivity;
import com.example.islam.project.Adapters.DBAdapter;
import com.example.islam.project.Constants;
import com.example.islam.project.DateUtils;
import com.example.islam.project.MyApplication;
import com.example.islam.project.Observers.ParamsObserver;
import com.example.islam.project.Observers.ParamsSubject;
import com.example.islam.project.R;
import com.example.islam.project.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




public class PrayerTimeElapsedService extends Service implements ParamsSubject {
    private Timer timer;
    private TimerTask timerTask;
    private List<String[]> todaysPrayersFromDB;
    private int nextPrayerIndex;
    private long timeLeft;
    private DBAdapter myDb;
    private ParamsObserver observer;

    private final BroadcastReceiver timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                timeChanged();
            }
            else if(intent.getAction().equals(Constants.ACTION_CALL_SUCCESS)){
                observer.saveParams();
                startTimer();
            }
            //else if(intent.getAction().equals(Constants.ACTION_CALL_FAILED)){}
        }
    };

    private static IntentFilter timeChangedIntentFilter;

    static {
        timeChangedIntentFilter = new IntentFilter();
        timeChangedIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        timeChangedIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        timeChangedIntentFilter.addAction(Constants.ACTION_CALL_SUCCESS);
        timeChangedIntentFilter.addAction(Constants.ACTION_CALL_FAILED);
    }

    public PrayerTimeElapsedService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(loadDayData())
            startTimer();
        return START_STICKY;
    }

    private void openDB(){
        myDb = new DBAdapter(getApplicationContext());
        myDb.open();
    }

    private void closeDB(){
        myDb.close();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        openDB();
        setObserver(new ParamsObserver(this));
        registerReceiver(timeChangedReceiver, timeChangedIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(Constants.TAG, "service ondestroy!");
        closeDB();
        Intent broadcastIntent = new Intent(this, ServiceRestarterBroadcastReceiver.class);

        unregisterReceiver(timeChangedReceiver);
        sendBroadcast(broadcastIntent);
        stopTimer();
    }



    public void startTimer() {
        timer = new Timer();
        if(prayerElapsed()) {
            initializeTimerTask();

            timer.schedule(timerTask, timeLeft);
        }
    }
    public void stopTimer() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                if(nextPrayerIndex != Constants.PRAYERS_COUNT) {
                    MyApplication.playNotification(getString(R.string.prayer_time_now),
                            getResources().getStringArray(R.array.prayer_time_now_array)[nextPrayerIndex],
                            getApplicationContext());
                }
                startTimer();
            }
        };
    }

    public boolean prayerElapsed(){
        if(nextPrayerIndex==Constants.PRAYERS_COUNT){ //end of day
            if(!loadDayData())
                return false;
        }
        Object[] nextPrayerParams = Time.timeToNextPrayer(todaysPrayersFromDB);
        nextPrayerIndex = (int)nextPrayerParams[0];
        timeLeft = ((Time)nextPrayerParams[1]).toMillis();
        return true;
    }
    public void timeChanged(){
        stopTimer();
        if(loadDayData())
            startTimer();
    }

    private boolean loadDayData(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Log.i(Constants.TAG,year+":"+Constants.year);
        if(year != Constants.year){
            Log.i(Constants.TAG, "year not equal");
            //yearSet(year);
            return false;
        }
        int start = (calendar.get(Calendar.DAY_OF_YEAR) - 1  /*+ (debug?0:1) */) * Constants.PRAYERS_COUNT;
        todaysPrayersFromDB = displayRecordSetFromTo(start, start + Constants.PRAYERS_COUNT);
        return true;
    }
    private void yearSet(int year){
        observer.update(Constants.YEAR_OBSERVED, year);
        observer.sendRequest();
    }
    private List<String[]> displayRecordSetFromTo(int from, int to) {
        Cursor cursor = myDb.getAllRows();
        Log.d(Constants.TAG,myDb.getCount()+"");
        ArrayList<String[]> myDataset = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int id = 0;
            do {
                if(id>=from && id<to) {
                    String event = cursor.getString(DBAdapter.COL_EVENT);
                    String date = cursor.getString(DBAdapter.COL_DATE);
                    String time = cursor.getString(DBAdapter.COL_TIME);
                    myDataset.add(new String[]{event, time, date});
                }
                id++;
            } while(cursor.moveToNext());
        }
        cursor.close();
        myDataset.add(null);
        return myDataset;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void setObserver(ParamsObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyWithUpdate(int id, Object update) {

    }
}
