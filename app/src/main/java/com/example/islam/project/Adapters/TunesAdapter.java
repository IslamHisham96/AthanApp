package com.example.islam.project.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.islam.project.Constants;
import com.example.islam.project.MyApplication;
import com.example.islam.project.R;

public class TunesAdapter extends RecyclerView.Adapter<TunesAdapter.MyViewHolder> {
    private int[] tunes;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public MyViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TunesAdapter() {
        tunes = new int[Constants.tune.length + 1];
        for(int i=0;i<tunes.length-1;i++)
            tunes[i] = Constants.tune[i];
        Log.d(Constants.TAG, tunes.length+""+Constants.tune.length);
        tunes[tunes.length - 1] = Constants.hijriAdj;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TunesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View customView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tune_item, parent, false);
        //
        MyViewHolder vh = new MyViewHolder(customView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        View customView = holder.mView;
        Button decBtn = customView.findViewById(R.id.decBtn);
        Button incBtn = customView.findViewById(R.id.incBtn);
        TextView prayerTxt = customView.findViewById(R.id.prayerTxt);
        final TextView tuneTxt = customView.findViewById(R.id.tuneTxt);
        final int pos = position;
        tuneTxt.setText(tunes[position]+"");
        if(position<tunes.length - 1)
            prayerTxt.setText(MyApplication.getAppContext().getResources().getStringArray(R.array.prayers)[position] +"");
        else prayerTxt.setText(R.string.hijri_adj);
        incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = tunes[pos];
                cur = Math.min(cur+1, Constants.MAX_TUNE);
                tunes[pos] = cur;
                tuneTxt.setText(cur+"");
            }
        });
        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = tunes[pos];
                cur = Math.max(cur-1, Constants.MIN_TUNE);
                tunes[pos] = cur;
                tuneTxt.setText(cur+"");
            }
        });

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tunes.length;
    }

    public int[] getTunes(){
        int[] retTunes = new int[Constants.PRAYERS_COUNT];
        for(int i=0;i<retTunes.length;i++)
            retTunes[i] = tunes[i];
        return retTunes;
    }
    public int getHijriAdj(){
        return tunes[tunes.length-1];
    }
}
