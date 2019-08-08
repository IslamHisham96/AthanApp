package com.example.islam.project;

import android.util.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Time {

    public int seconds;
    public int minutes;
    public int hours;

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        //Log.d(Constants.TAG, "Time now: "+hours+":"+minutes+":"+seconds);
    }
    public Time(Time t){
        hours = t.hours;
        minutes = t.minutes;
        seconds = t.seconds;
    }
    public Time(String time){
        String[] splitTime = time.split(":");
        hours = Integer.parseInt(splitTime[0]);
        minutes = Integer.parseInt(splitTime[1]);
        if(splitTime.length>2)
            seconds = Integer.parseInt(splitTime[2]);
    }
    public Time(long milliseconds){
        seconds = (int) (milliseconds / 1000) % 60 ;
        minutes = (int) ((milliseconds / (1000*60)) % 60);
        hours   = (int) ((milliseconds / (1000*60*60)) % 24);
    }

    public void increment(){
        seconds+=1;
        if(seconds>=60) {
            minutes++;
            if (minutes >= 60) {
                minutes = 0;
                hours++;
                if (hours >= 24)
                    hours = 0;
            }
        }
    }


    public static Time difference(Time stop, Time start)
    {
        Time diff = new Time(0, 0, 0);
        Time tstart = new Time(start);
        Time tstop = new Time(stop);


        if(tstop.seconds > tstart.seconds){
            --tstart.minutes;
            tstart.seconds += 60;
        }

        diff.seconds = tstart.seconds - tstop.seconds;
        if(tstop.minutes > tstart.minutes){
            --tstart.hours;
            tstart.minutes += 60;
        }

        if(tstop.hours > tstart.hours) tstart.hours+= 24;


        diff.minutes = tstart.minutes - tstop.minutes;
        diff.hours = tstart.hours - tstop.hours;
        /*Log.d(Constants.TAG,start.toString());
        Log.d(Constants.TAG,stop.toString());
        Log.d(Constants.TAG,diff.toString());*/

        //Log.d(Constants.TAG,diff.toString());
        return diff;
    }
    public static long differenceInMillis(Time start, Time stop){
        Time diff = difference(start, stop);
        return diff.toMillis();
    }
    public long toMillis(){
        long ret = 0;
        ret+=this.seconds*1000;
        ret+=this.minutes*60*1000;
        ret+=this.hours*60*60*1000;
        return ret;
    }

    public long toCalendarMillis(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        if(hours==0 && minutes==0)
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTimeInMillis();
    }
    public static Time getTimeNow(){
        Calendar ca = Calendar.getInstance();
        return new Time(ca.get(Calendar.HOUR_OF_DAY),ca.get(Calendar.MINUTE),ca.get(Calendar.SECOND));
    }

    public static Object[] timeToNextPrayer(final List<DBElement> prayersFromDB){
        Time now = Time.getTimeNow();
        now.increment();

        Log.d(Constants.TAG,"viewholder");
        /*
        if(PrayerTimesFragment.debug) {
            Log.d(Constants.TAG,"viewholder debug");
            now.hours = 14;
            now.minutes = 35;
            now.seconds = 55;
        }
        else{
            now.hours = 18;
            now.minutes = 25;
            now.seconds = 0;
        }*/
        long min=Long.MAX_VALUE;
        boolean endOfDay = false;
        int nextPrayerIndex = -1;
        for(int position = 0; position < prayersFromDB.size() - 1; position++){
            Time prayer = new Time(prayersFromDB.get(position).getTime());
            long diff = Time.differenceInMillis(now, prayer);
            if(diff < min){
                min = diff;
                nextPrayerIndex = position;
            }
            if(position == prayersFromDB.size()-2 && nextPrayerIndex==0 && now.hours >= prayer.hours) {
                endOfDay = true;
                Log.d(Constants.TAG, "end of day");
                nextPrayerIndex = position+1;
            }
        }
        String nextPrayer = (endOfDay)?"00:00":prayersFromDB.get(nextPrayerIndex).getTime();
        Log.d(Constants.TAG,"next prayer: "+nextPrayer);
        return new Object[]{nextPrayerIndex, Time.difference(Time.getTimeNow(), new Time(nextPrayer))};
    }

    public static Object[] timeToNextPrayerNew(final List<DBElement> prayersFromDB){
        Time now = Time.getTimeNow();
        now.increment();

        Log.d(Constants.TAG,"viewholder");
        /*
        if(PrayerTimesFragment.debug) {
            Log.d(Constants.TAG,"viewholder debug");
            now.hours = 14;
            now.minutes = 35;
            now.seconds = 55;
        }
        else{
            now.hours = 18;
            now.minutes = 25;
            now.seconds = 0;
        }*/
        long min=Long.MAX_VALUE;
        boolean endOfDay = false;
        int nextPrayerIndex = -1;
        for(int position = 0; position < prayersFromDB.size() - 1; position++){
            Time prayer = new Time(prayersFromDB.get(position).getTime());
            long diff = Time.differenceInMillis(now, prayer);
            if(diff < min){
                min = diff;
                nextPrayerIndex = position;
            }
            if(position == prayersFromDB.size()-2 && nextPrayerIndex==0 && now.hours >= prayer.hours) {
                endOfDay = true;
                Log.d(Constants.TAG, "end of day");
                nextPrayerIndex = position+1;
            }
        }
        String nextPrayer = (endOfDay)?"00:00":prayersFromDB.get(nextPrayerIndex).getTime();
        Log.d(Constants.TAG,"next prayer: "+nextPrayer);
        return new Object[]{nextPrayerIndex, new Time(nextPrayer)};
    }

    @Override
    public String toString(){
        return hours+":"+minutes+":"+seconds;
    }
}