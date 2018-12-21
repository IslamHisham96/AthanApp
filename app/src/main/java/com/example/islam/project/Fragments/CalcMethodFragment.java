package com.example.islam.project.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.islam.project.R;


public class CalcMethodFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public CalcMethodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calc_method, container, false);
        final Spinner spinner = v.findViewById(R.id.spinner);
        v.findViewById(R.id.calcMethodSetBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.CalcMethodSet(spinner.getSelectedItemPosition() + 1);
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


}
