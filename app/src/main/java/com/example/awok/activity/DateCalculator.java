package com.example.awok.activity;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCalculator {

    public long getDateDifference(long date){
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDateInMills = new Date(System.currentTimeMillis());
        Date serverTime;
        serverTime = new Date(date);
        String temp =  formatDate.format(currentDateInMills) +" "+ formatTime.format(serverTime);

        Date synchDate = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            synchDate = sdf.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return synchDate.getTime()- currentDateInMills.getTime();
    }
    public String getFormattedTime(long time){
        String result = "";
        long seconds = time / 1000 % 60;
        long minutes = time / (60 * 1000) % 60;
        long hours = time / (60 * 60 * 1000) % 24;

        if(hours<10)
            result = "0"+hours;
        else
            result = String.valueOf(hours);

        result += ":";

        if(minutes<10)
            result += "0"+minutes;
        else
            result += String.valueOf(minutes);

        result += ":";

        if(seconds<10)
            result += "0"+seconds;
        else
            result += String.valueOf(seconds);

        return result;

    }
}
