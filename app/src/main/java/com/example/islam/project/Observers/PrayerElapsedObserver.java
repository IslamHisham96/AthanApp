package com.example.islam.project.Observers;



public interface PrayerElapsedObserver {
    void setSubject(PrayerElapsedSubject subject);
    void update(boolean endOfDay);
}
