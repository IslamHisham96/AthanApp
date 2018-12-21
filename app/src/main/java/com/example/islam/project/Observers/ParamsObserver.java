package com.example.islam.project.Observers;

import android.util.Log;

import com.example.islam.project.AthanCall;
import com.example.islam.project.Constants;

import static com.example.islam.project.Constants.*;


public class ParamsObserver {
    private ParamsSubject subject;
    private AthanCall.AthanCallBuilder callBuilder;
    public ParamsObserver(ParamsSubject subject){
        this.subject = subject;
        this.callBuilder = new AthanCall.AthanCallBuilder();
    }
    public void update(int id, Object update){
        try {
            switch (id) {
                case CALC_METHOD_OBSERVED:
                    callBuilder.setCalcMethod((Integer)update);
                    break;
                case LOCATION_OBSERVED:
                    String[] location = ((String)update).split(",");
                    callBuilder.setLocation(Double.parseDouble(location[0]),Double.parseDouble(location[1]));
                    break;
                case YEAR_OBSERVED:
                    callBuilder.setYear((Integer)update);
                    break;
                case HIJRI_ADJ_OBSERVED:
                    callBuilder.setHijriAdj((Integer)update);
                    break;
                case TUNES_OBSERVED:
                    String[] tuneParams = ((String)update).split(",");
                    callBuilder.TunePrayer(Integer.parseInt(tuneParams[0]),Integer.parseInt(tuneParams[1]));
            }
        }
        catch(Exception ex){
            Log.d(Constants.TAG, ex.getMessage());
        }
    }
    public void updateMultiple(int[] ids, String[] updates){
        if(ids.length == updates.length) {
            for (int i = 0; i < ids.length; i++) {
                update(ids[i],updates[i]);
            }
        }
    }
    public void sendRequest(){
        callBuilder.build();
    }
}
