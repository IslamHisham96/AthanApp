package com.example.islam.project.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.islam.project.Adapters.AzkarAdapter;
import com.example.islam.project.AzkarParam;
import com.example.islam.project.R;


public class AzkarFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private String title;
    private RecyclerView mRecyclerView;
    private AzkarAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AzkarParam param;
    public AzkarFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_azkar, container, false);
        TextView titleTxt = v.findViewById(R.id.azkarTitle);
        titleTxt.setText(title);

        mRecyclerView = v.findViewById(R.id.azkarRecyclerView);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), ((LinearLayoutManager)mLayoutManager).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new AzkarAdapter(param);
        mRecyclerView.setAdapter(mAdapter);
        
        return v;
    }
    
    public void setTitle(String title){
        this.title = title;
    }

    public void setAzkarParam(AzkarParam param){
        this.param = param;
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
}
