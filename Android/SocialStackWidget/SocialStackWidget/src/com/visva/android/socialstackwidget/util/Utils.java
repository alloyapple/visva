package com.visva.android.socialstackwidget.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.visva.android.socialstackwidget.constant.GlobalContstant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertToDateTime(String strDate) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        long createdTime = 0;
        Date date = null;
        try {
            date = formatDate.parse(strDate);
            createdTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return GlobalContstant.SUB_STRING_CODE;
        }
        long currentTime = System.currentTimeMillis();
        String returnDate = "";
        String createdDateStr = convertTime(strDate);
        long deltaTime = (currentTime - createdTime) / 1000;
        if (deltaTime < 60)
            returnDate = deltaTime + " seconds ago";
        else if (deltaTime >= 60 && deltaTime < 3600)
        {
            int deltaMinute = (int) deltaTime / 60;
            returnDate = deltaMinute + " minutes ago ";
        } else if (deltaTime >= 3600 && deltaTime < (3600 * 24)) {
            int deltaHour = (int) deltaTime / 3600;
            returnDate = deltaHour + " hours ago";
        } else if (deltaTime >= (3600 * 24) && deltaTime < (3 * 3600 * 24))
        {
            int deltaDays = (int) deltaTime / (3600 * 24);
            returnDate = deltaDays + " days ago";
        } else {
            returnDate = createdDateStr;
        }

        return returnDate;
    }
    
    
    @SuppressLint("SimpleDateFormat")
    public static String convertToDateTimeTwitter(String strDate) {
        SimpleDateFormat formatDate = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzzz yyyy");
        long createdTime = 0;
        Date date = null;
        try {
            date = formatDate.parse(strDate);
            createdTime = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long currentTime = System.currentTimeMillis();
        String returnDate = "";
        String createdDateStr = convertTimeTwitter(strDate);
        long deltaTime =  (currentTime - createdTime) / 1000;
        if (deltaTime < 60)
            returnDate = deltaTime + " seconds ago";
        else if (deltaTime >= 60 && deltaTime < 3600)
        {
            int deltaMinute =(int) deltaTime / 60;
            returnDate = deltaMinute + " minutes ago ";
        } else if (deltaTime >= 3600 && deltaTime < (3600 * 24)) {
            int deltaHour = (int)deltaTime / 3600;
            returnDate = deltaHour + " hours ago";
        } else if (deltaTime >= (3600 * 24) && deltaTime < (3 * 3600 * 24))
        {
            int deltaDays =(int) deltaTime / (3600 * 24);
            returnDate = deltaDays + " days ago";
        } else {
            returnDate = createdDateStr;
        }

        return returnDate;
    }

    @SuppressLint("SimpleDateFormat")
    private static String convertTime(String strDate) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        String returnDate = "";
        try {
            Date date = formater.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            returnDate += calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnDate;
    }
    
    @SuppressLint("SimpleDateFormat")
    private static String convertTimeTwitter(String strDate) {
        SimpleDateFormat formater = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzzz yyyy");
        String returnDate = "";
        try {
            Date date = formater.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            returnDate += calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static long convertTimeToLong(String dateStr) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        long returnDate = 0;
        try {
            Date date = formater.parse(dateStr);
            returnDate = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnDate;
    }
    
    
    @SuppressLint("SimpleDateFormat")
    public static long convertTWTimeToLong(String dateStr){
        SimpleDateFormat formater = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzzz yyyy");
        long returnDate = 0;
        try {
            Date date = formater.parse(dateStr);
            returnDate = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnDate;
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null) {
            return false;
        }
        if (!i.isConnected()) {
            return false;
        }
        if (!i.isAvailable()) {
            return false;
        }
        return true;
    }
    
    public static void LogExt(String tag, String sb){
        if (sb.length() > 4000) {
            Log.d(tag, "sb.length = " + sb.length());
            int chunkCount = sb.length() / 4000;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= sb.length()) {
                    Log.d(tag, "chunk " + i + " of " + chunkCount + ":" + sb.substring(4000 * i));
                } else {
                    Log.d(tag, "chunk " + i + " of " + chunkCount + ":" + sb.substring(4000 * i, max));
                }
            }
        }
    }
}