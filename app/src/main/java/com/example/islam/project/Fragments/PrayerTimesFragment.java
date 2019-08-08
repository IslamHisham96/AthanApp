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
import com.example.islam.project.DBElement;
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



public class PrayerTimesFragment extends MyFragment implements PrayerElapsedObserver {
    private RecyclerView mRecyclerView;
    private PrayerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView dateTxt;
    private List<DBElement> mDataset;
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
    protected int getTitleID() {
        return R.string.prayers_title;
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
        if(endOfDay) {
            if(!loadDayData())
                return;
        }
        Object[] nextPrayerParams = Time.timeToNextPrayer(mDataset);
        nextPrayerIndex = (int)nextPrayerParams[0];
        mAdapter = new PrayerAdapter(mDataset, this, (Time)nextPrayerParams[1], nextPrayerIndex);
        if(mRecyclerView!=null)
            mRecyclerView.setAdapter(mAdapter);
    }

    private boolean loadDayData(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        if(year != Constants.year){
            Log.d(Constants.TAG, "year not equal");
            mListener.yearSet(year);
            return false;
        }
        int start = (calendar.get(Calendar.DAY_OF_YEAR) - 1  /*+ (debug?0:1) */) * Constants.PRAYERS_COUNT;
        mDataset = MyApplication.displayRecordSetFromTo(start, start + Constants.PRAYERS_COUNT);
        dateTxt.setText(DateUtils.formatHijriDate(mDataset.get(0).getDate()));
        mDataset.add(null);
        return true;
    }

}
