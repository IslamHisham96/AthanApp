package com.example.islam.project.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.example.islam.project.Constants;
import com.example.islam.project.MyApplication;
import com.example.islam.project.RequestQueueSingleton;

public class EntryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueueSingleton.getInstance(getApplicationContext());

        if(MyApplication.getFirst()){
            Intent intent = new Intent(this,MainActivity.class);
            Log.d(Constants.TAG,"hello1");
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, PrayerTimesActicity.class);
            Log.d(Constants.TAG,"hello2");
            startActivity(intent);
            finish();
        }

    }



}
