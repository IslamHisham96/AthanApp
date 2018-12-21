package com.example.islam.project;

import android.util.Log;

import java.io.Console;
import java.util.Arrays;

public class Time {

    public int seconds;
    public int minutes;
    public int hours;

    public Time(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }
    public Time(Time t){
        hours = t.hours;
        minutes = t.minutes;
        seconds = t.seconds;
    }
    public Time(String time){
        String[] splitTime = time.split(":");
        Log.d(Constants.TAG,Arrays.toString(splitTime));
        hours = Integer.parseInt(splitTime[0]);
        minutes = Integer.parseInt(splitTime[1]);
        if(splitTime.length>2)
            seconds = Integer.parseInt(splitTime[2]);
        Log.d(Constants.TAG,this.toString());
    }
    public Time(long milliseconds){
        seconds = (int) (milliseconds / 1000) % 60 ;
        minutes = (int) ((milliseconds / (1000*60)) % 60);
        hours   = (int) ((milliseconds / (1000*60*60)) % 24);
    }


    public static Time difference(Time stop, Time start)
    {
        Time diff = new Time(0, 0, 0);
        Time tstart = new Time(start);
        Time tstop = new Time(stop);

        if(tstop.hours > tstart.hours) tstart.hours+= 24;

        if(tstop.seconds > tstart.seconds){
            --tstart.minutes;
            tstart.seconds += 60;
        }

        diff.seconds = tstart.seconds - tstop.seconds;
        if(tstop.minutes > tstart.minutes){
            --tstart.hours;
            tstart.minutes += 60;
        }


        diff.minutes = tstart.minutes - tstop.minutes;
        diff.hours = tstart.hours - tstop.hours;
        Log.d(Constants.TAG,start.toString());
        Log.d(Constants.TAG,stop.toString());
        Log.d(Constants.TAG,diff.toString());
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
    public String toString(){
        return hours+":"+minutes+":"+seconds;
    }
}