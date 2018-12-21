package com.example.islam.project.Observers;

public interface PrayerElapsedSubject {
    void setObserver(PrayerElapsedObserver observer);
    void notifyWithUpdate(int id, Object update);
}
