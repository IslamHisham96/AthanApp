package com.example.islam.project.Observers;

public interface ParamsSubject{
    void setObserver(ParamsObserver observer);
    void notifyWithUpdate(int id, Object update);
}
