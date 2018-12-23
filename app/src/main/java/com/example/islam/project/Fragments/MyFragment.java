package com.example.islam.project.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.islam.project.Activities.MyActivity;
import com.example.islam.project.MyApplication;

public abstract class MyFragment extends Fragment implements BackButtonSupportFragment {
    private AddFragmentHandler fragmentHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentHandler = new AddFragmentHandler(getActivity().getSupportFragmentManager(),((MyActivity)getActivity()).getFragmentFrame());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTitle());
    }

    protected abstract int getTitleID();
    public String getTitle(){
        return MyApplication.getAppContext().getString(getTitleID());
    }
    protected void add(MyFragment fragment) {
        fragmentHandler.add(fragment);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
