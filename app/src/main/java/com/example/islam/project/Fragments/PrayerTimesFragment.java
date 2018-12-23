package com.example.islam.project.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.islam.project.Constants;
import com.example.islam.project.DateUtils;
import com.example.islam.project.Adapters.PrayerAdapter;
import com.example.islam.project.MyApplication;
import com.example.islam.project.Observers.PrayerElapsedObserver;
import com.example.islam.project.Observers.PrayerElapsedSubject;
import com.example.islam.project.R;
import com.example.islam.project.Time;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;



public class PrayerTimesFragment extends Fragment implements PrayerElapsedObserver {
    private RecyclerView mRecyclerView;
    private PrayerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView dateTxt;
    private List<String[]> mDataset;
    private PrayerElapsedSubject subject;
    private OnFragmentInteractionListener mListener;
    public static boolean debug = true;
    public int nextPrayerIndex;
    public PrayerTimesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_prayer_times, container, false);
        mRecyclerView = v.findViewById(R.id.prayersRecyclerView);
        dateTxt = v.findViewById(R.id.dateTxt);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), ((LinearLayoutManager)mLayoutManager).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        update(true);
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void setSubject(PrayerElapsedSubject subject) {
        this.subject = subject;
    }

    @Override
    public void update(boolean endOfDay) {
        //debug = false;
        if(endOfDay)
            loadDayData();
        mAdapter = new PrayerAdapter(mDataset, this, timeToNextPrayer(), nextPrayerIndex);
        if(mRecyclerView!=null)
            mRecyclerView.setAdapter(mAdapter);
    }

    private void loadDayData(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        if(year != Constants.year){
            Log.d(Constants.TAG, "year not equal");
            mListener.yearSet(year);
            return;
        }
        int start = (calendar.get(Calendar.DAY_OF_YEAR) - 1  /*+ (debug?0:1) */) * Constants.PRAYERS_COUNT;
        mDataset = MyApplication.displayRecordSetFromTo(start, start + Constants.PRAYERS_COUNT);
        dateTxt.setText(DateUtils.formatHijriDate(mDataset.get(0)[2]));
        mDataset.add(null);
    }
    private Time timeToNextPrayer(){
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
        for(int position = 0; position < mDataset.size() - 1; position++){
            Time prayer = new Time(mDataset.get(position)[1]);
            long diff = Time.differenceInMillis(now, prayer);
            if(diff < min){
                min = diff;
                nextPrayerIndex = position;
            }
            if(position == mDataset.size()-2 && nextPrayerIndex==0 && now.hours >= prayer.hours) {
                endOfDay = true;
                Log.d(Constants.TAG, "end of day");
                nextPrayerIndex = position+1;
            }
        }
        String nextPrayer = (endOfDay)?"00:00":mDataset.get(nextPrayerIndex)[1];
        Log.d(Constants.TAG,"next prayer: "+nextPrayer);
        return Time.difference(Time.getTimeNow(), new Time(nextPrayer));
    }
}
