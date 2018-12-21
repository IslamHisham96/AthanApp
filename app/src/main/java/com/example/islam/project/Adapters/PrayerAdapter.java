package com.example.islam.project.Adapters;

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

import com.example.islam.project.Constants;
import com.example.islam.project.Fragments.PrayerTimesFragment;
import com.example.islam.project.MyApplication;
import com.example.islam.project.Observers.PrayerElapsedObserver;
import com.example.islam.project.Observers.PrayerElapsedSubject;
import com.example.islam.project.R;
import com.example.islam.project.Time;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PrayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PrayerElapsedSubject {
    private PrayerElapsedObserver observer;
    private List<String[]> mDataset;
    private Time left;
    private int nextPrayerIndex;
    @Override
    public void setObserver(PrayerElapsedObserver observer) {
        this.observer = observer;
    }

    @Override
    public void notifyWithUpdate(boolean endOfDay) {
        observer.update(endOfDay);
    }

    public static class PrayerViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private PrayerViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }
    }

    public static class TimerViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        CountDownTimer timer;
        private TimerViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }
    }

    public PrayerAdapter(List<String[]> myDataset, PrayerElapsedObserver observer, Time timeLeft, int nextPrayerIndex) {
        mDataset = myDataset;
        this.observer = observer;
        left = timeLeft;
        this.nextPrayerIndex = nextPrayerIndex;
    }

    @Override
    public @NonNull RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
                        notifyWithUpdate(nextPrayerIndex==Constants.PRAYERS_COUNT); //end of day
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
