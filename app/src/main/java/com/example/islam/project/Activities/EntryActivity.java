package com.example.islam.project.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.example.islam.project.AthanParams;
import com.example.islam.project.MyApplication;
import com.example.islam.project.R;
import com.example.islam.project.RequestQueueSingleton;

import static com.example.islam.project.Constants.*;
import static com.example.islam.project.MyApplication.sharedPreferences;
public class EntryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueueSingleton.getInstance(getApplicationContext());

        readParams();
        /*if(sharedPreferences.getBoolean(FIRST_TIME,true)){
            Intent intent = new Intent(this,MainActivity.class);
            Log.d(TAG,"hello1");
            startActivity(intent);
            finish();
        }
        else*/{
            Intent intent = new Intent(this, MainActivity.class);
            Log.d(TAG,"hello2");
            startActivity(intent);
            finish();
        }

    }


    public void readParams(){
        calcMethod = sharedPreferences.getInt(UPDATE_CALC_METHOD, calcMethod);
        hijriAdj = sharedPreferences.getInt(UPDATE_HIJRI_ADJ, hijriAdj);
        location = sharedPreferences.getString(UPDATE_LOCATION, location);
        String [] temp = location.split(",");
        latitude = Double.parseDouble(temp[0]);
        longitude = Double.parseDouble(temp[1]);
        year = sharedPreferences.getInt(UPDATE_YEAR, year);
        tuneString = sharedPreferences.getString(UPDATE_TUNES,tuneString);
        tune = AthanParams.setTuneFromString(tuneString);
    }
    public static void saveParams(AthanParams params){
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
        Toast.makeText(MyApplication.getAppContext(),R.string.saved,Toast.LENGTH_SHORT).show();
    }
}
