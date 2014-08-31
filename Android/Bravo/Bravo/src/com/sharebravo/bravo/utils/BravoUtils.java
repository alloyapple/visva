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
            } catch (Exception e) {
                if (e != null)
                    e.printStackTrace();
            }
        return monthName;
    }
}
