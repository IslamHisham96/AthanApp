package com.example.islam.project.Observers;

public interface PrayerElapsedSubject {
    void setObserver(PrayerElapsedObserver observer);
    void notifyWithUpdate(boolean endOfDay);
}
