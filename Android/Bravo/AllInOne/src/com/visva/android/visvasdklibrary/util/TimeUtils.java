package com.visva.android.visvasdklibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 
 * @author kieu.thang
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

    public static String getCurTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
        return String.valueOf(currentTimestamp);
    }

    public static String formatDateStr(String format, Date input) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(format);
        return inputFormat.format(input);
    }

    public static Date getDate(String format, String input) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(format);
        try {
            return inputFormat.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String formatTimeStr(int hour, int minute) {
        return (hour < 10 ? hour + "0" : hour + "") + ":" + (minute < 10 ? minute + "0" : minute + "");
    }

    public static int compareDate(String dateFormat, String dateA, String dateB) {
        int result = 0;
        String aDateFormat = dateFormat.equalsIgnoreCase("") ? "yyyy-MM-dd HH:mm:ss" : dateFormat;

        SimpleDateFormat inputFormat = new SimpleDateFormat(aDateFormat);

        Date startDate;
        Date endDate;
        try {
            startDate = inputFormat.parse(dateA);
            endDate = inputFormat.parse(dateB);
            result = (startDate.getTime() - endDate.getTime()) > 0 ? -1 : 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.YEAR, 0); // set hour to midnight
        cal.set(Calendar.MINUTE, 0); // set minute in hour
        cal.set(Calendar.SECOND, 0); // set second in minute
        cal.set(Calendar.MILLISECOND, 0); // set millisecond in second

        return cal; // return the date part
    }

    public static String convertDateToString(Date date) {
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(date);
        String dateStr = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DATE);
        return dateStr;
    }

    /**
     * This method also assumes endDate >= startDate
     **/
    public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public final static long MILLISECS_PER_DAY = 24 * 60 * 60 * 1000;

    public static boolean isDateAfter(String inputDate) {

        int year, month, day;
        boolean result;
        String[] newStr = inputDate.split("-");
        year = Integer.parseInt(newStr[0]);
        month = Integer.parseInt(newStr[1]);
        day = Integer.parseInt(newStr[2]);
        Calendar cal = Calendar.getInstance();
        Calendar currentcal = Calendar.getInstance();
        cal.set(year, month, day);
        currentcal.set(currentcal.get(Calendar.YEAR),
                currentcal.get(Calendar.MONTH),
                currentcal.get(Calendar.DAY_OF_MONTH));
        if (cal.before(currentcal))
            result = true;
        else if (cal.after(currentcal))
            result = false;
        else
            result = true;
        return result;
    }
}
