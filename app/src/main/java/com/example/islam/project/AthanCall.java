package com.example.islam.project;

import android.content.Intent;
import android.util.Log;

import static com.example.islam.project.Constants.ATHAN_CALL;
import static com.example.islam.project.Constants.TAG;

import java.util.Arrays;

public class AthanCall {

    public AthanCall(final AthanCallBuilder builder){
        String tuneString = Arrays.toString(builder.tune).replaceAll(" ", "");
        StringBuilder athanCall =

                new StringBuilder("https://api.aladhan.com/v1/calendar?");
        athanCall.append("latitude=").append(builder.latitude).append("&longitude=").append(builder.longitude);
        athanCall.append("&method=").append(builder.calcMethod);
        athanCall.append("&year=").append(builder.year);
        athanCall.append("&annual=true");
        athanCall.append("&tune=").append(tuneString.substring(1, tuneString.length() - 2));
        athanCall.append("&adjustment=").append(builder.hijriAdj);
        Log.d(TAG,athanCall.toString());

        MyApplication.saveParams(builder.athanParam);

        Intent serviceCall = new Intent(MyApplication.getAppContext(),AthanCallIntentService.class);
        serviceCall.putExtra(ATHAN_CALL, athanCall.toString());
        MyApplication.getAppContext().startService(serviceCall);
    }



    public static class AthanCallBuilder {
        private int calcMethod;
        private double latitude;
        private double longitude;
        private int year;
        private int hijriAdj;
        private int[] tune;
        private AthanCallParams athanParam;
        public AthanCallBuilder() {
            calcMethod = Constants.calcMethod;
            latitude = Constants.latitude;
            longitude = Constants.longitude;
            year = Constants.year;
            tune = Constants.tune;
            hijriAdj = Constants.hijriAdj;
        }

        public AthanCallBuilder setCalcMethod(int id) {
            this.calcMethod = id;
            return this;
        }

        public AthanCallBuilder setLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            return this;
        }

        public AthanCallBuilder setYear(int year) {
            this.year = year;
            return this;
        }

        public AthanCallBuilder setHijriAdj(int hijriAdj) {
            this.hijriAdj = hijriAdj;
            return this;
        }

        public AthanCallBuilder TunePrayer(int id, int tuning) {
            this.tune[id] = tuning;
            return this;
        }

        public AthanCall build() {
            athanParam = new AthanCallParams(year,calcMethod,hijriAdj,longitude,latitude,tune);
            return new AthanCall(this);
        }
    }
}
