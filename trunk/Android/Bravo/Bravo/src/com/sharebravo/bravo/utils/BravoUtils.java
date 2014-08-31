package com.sharebravo.bravo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.model.response.ObPostUserSuccess;
import com.sharebravo.bravo.sdk.log.AIOLog;

@SuppressLint("SimpleDateFormat")
public class BravoUtils {

    public static String getUserIdFromUserBravoInfo(Context context) {
        String userBravoInfo = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_BRAVO);
        Gson gson = new GsonBuilder().serializeNulls().create();
        ObPostUserSuccess obPostUserSuccess = gson.fromJson(userBravoInfo.toString(), ObPostUserSuccess.class);
        if (obPostUserSuccess == null)
            return "";
        return obPostUserSuccess.data.User_ID;
    }
    
    public static String getAccessTokenFromUserBravoInfo(Context context) {
        String userBravoInfo = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_BRAVO);
        Gson gson = new GsonBuilder().serializeNulls().create();
        ObPostUserSuccess obPostUserSuccess = gson.fromJson(userBravoInfo.toString(), ObPostUserSuccess.class);
        if (obPostUserSuccess == null)
            return "";
        return obPostUserSuccess.data.Access_Token;
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertToDateTime(long createdTime) {
        String returnDate;
        String createdDateStr = convertTime(createdTime);
        long deltaTime = (System.currentTimeMillis() - createdTime) / 1000;
        if (deltaTime < 60)
            returnDate = deltaTime + " seconds ago";
        else if (deltaTime >= 60 && deltaTime < 3600)
        {
            int deltaMinute = (int) deltaTime / 60;
            returnDate = deltaMinute + " minutes ago ";
        } else if (deltaTime >= 3600 && deltaTime < (3600 * 24)) {
            int deltaHour = (int) deltaTime / 3600;
            returnDate = deltaHour + " hours ago";
        }
        // else if (deltaTime >= (3600 * 24) && deltaTime < (3 * 3600 * 24)) {
        // int deltaDays = (int) deltaTime / (3600 * 24);
        // returnDate = deltaDays + " days ago";
        // }
        else {
            returnDate = createdDateStr;
        }

        return returnDate;
    }

    @SuppressLint("SimpleDateFormat")
    private static String convertTime(long createdTime) {
        String returnDate = "";
        Date date = new Date(createdTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        calendar.setTimeInMillis(createdTime);
        AIOLog.d("adklfjd " + calendar.get(Calendar.DAY_OF_MONTH)+", adfdf:"+createdTime) ;
        returnDate += calendar.get(Calendar.DAY_OF_MONTH) + " " + getMonthShortName(calendar.get(Calendar.MONTH) + 1);
        return returnDate;
    }

    /**
     * @param monthNumber
     *            Month Number starts with 0. For <b>January</b> it is <b>0</b> and for <b>December</b> it is <b>11</b>.
     * @return
     */
    public static String getMonthShortName(int monthNumber) {
        String monthName = "";

        if (monthNumber >= 0 && monthNumber < 12)
            try
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            } catch (Exception e)
            {
                if (e != null)
                    e.printStackTrace();
            }
        return monthName;
    }
}
