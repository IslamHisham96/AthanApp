package com.example.islam.project;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.islam.project.Activities.PrayerTimesActicity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.islam.project.Constants.TAG;
import static com.example.islam.project.Constants.TIMINGS;


public class AthanCallIntentService extends IntentService {

    public AthanCallIntentService() {
        super("AthanCallIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (bundle == null) return;
            String athanCall = bundle.getString(Constants.ATHAN_CALL);

            JsonObjectRequest request = new JsonObjectRequest(athanCall, null,

                    new Response.Listener<JSONObject>() {
                        @Override

                        public void onResponse(JSONObject result) {
                            MyApplication.deleteAllRecords();
                            Log.i(TAG, "onResponse: Result= " + result.toString());
                            //TODO cake
                            parseAthanResults(result);
                            startIntent();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: Error= " + error);
                            Log.e(TAG, "onErrorResponse: Error= " + error.getMessage());
                        }
                    });
            RequestQueueSingleton.getInstance(null).addToRequestQueue(request);
        }
        catch (Exception ex){
            Log.e(Constants.TAG,"Error in service");
        }
    }

    public void startIntent(){
        Intent i = new Intent(getApplicationContext(), PrayerTimesActicity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public void parseAthanResults(JSONObject result){
        try{
            if(result.getString("status").equalsIgnoreCase("OK")){
                Log.d(TAG, "parsing..");
                JSONObject data = result.getJSONObject("data");
                int year = Integer.parseInt(data.getJSONArray("1").getJSONObject(0).getJSONObject("date").getJSONObject("gregorian").getString("year"));
                String [] names = MyApplication.getAppContext().getResources().getStringArray(R.array.prayers);
                for(int i = 1;i <= 12;i++){
                    JSONArray month = data.getJSONArray(i+"");
                    int daysinMonth = DateUtils.getDaysinMonth(i, year);
                    for(int j=0;j<daysinMonth;j++){
                        JSONObject object = month.getJSONObject(j);
                        String date = object.getJSONObject("date").getJSONObject("hijri").getString("date");
                        JSONObject timings = object.getJSONObject("timings");
                        for(int k=0;k<names.length;k++){
                            String time = timings.getString(TIMINGS[k]);
                            time = time.substring(0,time.indexOf('(')-1);
                            MyApplication.addRecord(new DBElement(names[k],date,time));
                        }
                    }
                }

                Log.d(TAG, "parsing done");
            }
        }
        catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseAthanResult: Error=" + e.getMessage());
        }
    }


}
