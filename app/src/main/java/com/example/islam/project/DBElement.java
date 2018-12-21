package com.example.islam.project;

public class DBElement {
    private String prayer;
    private  String date;
    private String time;
    public DBElement(String prayer, String date, String time){
        this.prayer = prayer;
        this.date = date;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getPrayer() {
        return prayer;
    }
}
