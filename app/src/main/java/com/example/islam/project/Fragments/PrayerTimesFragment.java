package com.example.islam.project.Fragments;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
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
import com.example.islam.project.MyAdapter;
import com.example.islam.project.MyApplication;
import com.example.islam.project.Observers.PrayerElapsedObserver;
import com.example.islam.project.Observers.PrayerElapsedSubject;
import com.example.islam.project.OnFragmentInteractionListener;
import com.example.islam.project.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;



public class PrayerTimesFragment extends Fragment implements PrayerElapsedObserver {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView dateTxt;
    private List<String[]> mDataset;
    private PrayerElapsedSubject subject;

    private OnFragmentInteractionListener mListener;

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
        Log.d("MyTag","her now");
        View v = inflater.inflate(R.layout.fragment_prayer_times, container, false);
        mRecyclerView = v.findViewById(R.id.prayersRecyclerView);
        dateTxt = v.findViewById(R.id.dateTxt);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), ((LinearLayoutManager)mLayoutManager).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        int start = (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1) * Constants.TIMINGS.length; //TODO cake
        mDataset = MyApplication.displayRecordSetFromTo(start, start + Constants.TIMINGS.length);
        dateTxt.setText(DateUtils.formatHijriDate(mDataset.get(0)[2]));
        mDataset.add(null);
        Log.d("MyTag", Arrays.toString(mDataset.get(0)));
        Log.d("MyTag", mDataset.size()+"");
        mAdapter = new MyAdapter(mDataset, this);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("MyTag","you are a rockstar");
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
    public void update() {

    }
}
