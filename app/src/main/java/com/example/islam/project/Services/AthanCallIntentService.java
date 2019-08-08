package com.example.islam.project.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.islam.project.Constants;
import com.example.islam.project.DBElement;
import com.example.islam.project.DateUtils;
import com.example.islam.project.MyApplication;
import com.example.islam.project.R;
import com.example.islam.project.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

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
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(athanCall, null, future, future);

                   /* new Response.Listener<JSONObject>() {
                        @Override

                        public void onResponse(JSONObject result) {
                            MyApplication.deleteAllRecords();
                            Log.i(TAG, "onResponse: Result= " + result.toString());
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
                    });*/
            Log.d(Constants.TAG, "adding to queue");
            RequestQueueSingleton.getInstance(null).addToRequestQueue(request);
            Log.d(Constants.TAG, "request added");
            JSONObject result = future.get(Constants.TIMEOUT, TimeUnit.SECONDS);
            Log.d(Constants.TAG, "date get");
            MyApplication.deleteAllRecords();
            parseAthanResults(result);
            serviceSuccess();
        }
        catch (Exception ex){
            Log.e(Constants.TAG,"Error in service");
            ex.printStackTrace();
            serviceFailed();
        }
    }

    public void serviceSuccess(){
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        localBroadcastManager.sendBroadcast(new Intent(
                Constants.ACTION_CALL_SUCCESS));
    }
    public void serviceFailed(){
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        localBroadcastManager.sendBroadcast(new Intent(
                Constants.ACTION_CALL_FAILED));
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
                    int daysInMonth = DateUtils.getDaysInMonth(i, year);
                    for(int j=0;j<daysInMonth;j++){
                        JSONObject object = month.getJSONObject(j);
                        String date = object.getJSONObject("date").getJSONObject("hijri").getString("date");
                        JSONObject timings = object.getJSONObject("timings");
                        for(int k=0;k<names.length;k++){
                            String time = timings.getString(TIMINGS[k]);
                            time = time.substring(0,time.indexOf('(')-1);
                            MyApplication.addRecord(new DBElement(names[k],time,date));
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
