package com.example.islam.project;


public class DateUtils {
    private static int[] days = {0,31,28,31,30,31,30,31,31,30,31,30,31};
    public static boolean isLeapYear(int year){
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
    public static int getDaysInMonth(int month, int year){
        return (month==2 && isLeapYear(year))? 29 : days[month];
    }
    public static String formatHijriDate(String date){
        String[] splitDate = date.split("-");
        int month = Integer.parseInt(splitDate[1]) - 1;
        return splitDate[0] + " / " + MyApplication.getAppContext().getResources().getStringArray(R.array.hijri_months)[month] + " / " + splitDate[2];
    }

}
