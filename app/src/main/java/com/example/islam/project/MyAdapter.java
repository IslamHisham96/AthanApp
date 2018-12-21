package com.example.islam.project;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.islam.project.Observers.PrayerElapsedObserver;
import com.example.islam.project.Observers.PrayerElapsedSubject;

import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PrayerElapsedSubject {
    public static int nextPrayerIndex = 3;
    private PrayerElapsedObserver observer;
    private List<String[]> mDataset;
    private Time left;

    @Override
    public void setObserver(PrayerElapsedObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyWithUpdate(int id, Object update) {
        observer.update();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PrayerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public PrayerViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }
    }

    public static class TimerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        CountDownTimer timer;
        private PrayerElapsedObserver observer;
        public TimerViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }

/*        public void updateTimer(){
            left.seconds--;
            if(left.seconds<0){
                left.seconds = 59;
                left.minutes--;
                if(left.minutes<0) {
                    left.minutes = 59;
                    left.hours--;
                }
            }
            hoursTxt.setText(String.format(Locale.US,"%02d",left.hours));
            minutesTxt.setText(String.format(Locale.US,"%02d",left.minutes));
            secondsTxt.setText(String.format(Locale.US,"%02d",left.seconds));
        }*/

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String[]> myDataset, PrayerElapsedObserver observer) {
        mDataset = myDataset;
        this.observer = observer;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        View customView;
        switch (viewType) {
            case 0:
                customView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.prayer_item, parent, false);
                vh = new PrayerViewHolder(customView);
                break;
            case 1:
                customView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.timer_item, parent,false);
                vh = new TimerViewHolder(customView);
                break;
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        View customView;
        String[] data = mDataset.get(position>nextPrayerIndex?position-1:position);
        switch (holder.getItemViewType()) {
            case 0:
                PrayerViewHolder pHolder = (PrayerViewHolder) holder;
                customView = pHolder.mView;
                if(nextPrayerIndex<Constants.PRAYERS_COUNT && nextPrayerIndex == position-1)
                    customView.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(),R.color.colorPrimary));
                TextView nameTxt = customView.findViewById(R.id.nameTxt);
                TextView timeTxt = customView.findViewById(R.id.timeTxt);
                nameTxt.setText(data[0]);
                timeTxt.setText(data[1]);
                break;

            case 1:
                TimerViewHolder tHolder = (TimerViewHolder) holder;
                customView = tHolder.mView;
                final TextView hoursTxt = customView.findViewById(R.id.hoursTxt);
                final TextView minutesTxt = customView.findViewById(R.id.minutesTxt);
                final TextView secondsTxt = customView.findViewById(R.id.secondsTxt);
                String nextPrayer = (position==mDataset.size()-1)?"00:00":mDataset.get(position)[1];
                Log.d(Constants.TAG,"next prayer: "+nextPrayer);
                left = Time.difference(new Time("17:00"),new Time(nextPrayer));
                hoursTxt.setText(String.format(Locale.US,"%02d",left.hours));
                minutesTxt.setText(String.format(Locale.US,"%02d",left.minutes));
                secondsTxt.setText(String.format(Locale.US,"%02d",left.seconds));
                tHolder.timer = new CountDownTimer(left.toMillis(), 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        left.seconds--;
                        if(left.seconds<0){
                            left.seconds = 59;
                            left.minutes--;
                            if(left.minutes<0) {
                                left.minutes = 59;
                                left.hours--;
                            }
                        }
                        hoursTxt.setText(String.format(Locale.US,"%02d",left.hours));
                        minutesTxt.setText(String.format(Locale.US,"%02d",left.minutes));
                        secondsTxt.setText(String.format(Locale.US,"%02d",left.seconds));
                    }

                    @Override
                    public void onFinish() {
                        nextPrayerIndex++;
                        if(nextPrayerIndex==Constants.PRAYERS_COUNT+1){
                            nextPrayerIndex=0;
                        }
                    }
                }.start();
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return (position==nextPrayerIndex)?1:0;
    }

    public void remove(int i) {
        mDataset.remove(i);
        notifyItemRemoved(i);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
