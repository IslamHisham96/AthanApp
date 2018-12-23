package com.example.islam.project.Fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.islam.project.Adapters.PrayerAdapter;
import com.example.islam.project.Adapters.TunesAdapter;
import com.example.islam.project.Constants;
import com.example.islam.project.R;

import java.lang.reflect.Field;


public class TunesFragment extends MyFragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private TunesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public TunesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tunes, container, false);
        mRecyclerView = v.findViewById(R.id.tunesRecyclerView);
        Button setBtn = v.findViewById(R.id.setBtn);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), ((LinearLayoutManager)mLayoutManager).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new TunesAdapter();
        mRecyclerView.setAdapter(mAdapter);
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean changed = false;
                int[] tunes = mAdapter.getTunes();
                for(int i=0;i<tunes.length;i++){
                    if(tunes[i] != Constants.tune[i]){
                        mListener.TunesSet(tunes);
                        changed = true;
                        break;
                    }
                }
                int hijriAdj = mAdapter.getHijriAdj();
                if(hijriAdj != Constants.hijriAdj) {
                    mListener.hijriAdjSet(hijriAdj);
                    changed = true;
                }

                if(changed)
                    mListener.finishSettings();
                else
                    mListener.goToPrayerFragment();
            }
        });
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
    protected int getTitleID() {
        return R.string.tunes_title;
    }
}
