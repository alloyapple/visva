package com.visva.voicerecorder.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 
 * @author KieuThang
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtility {

    public static final int HOUR_IN_MILIS   = 60 * 60 * 1000;
    public static final int MINUTE_IN_MILIS = 60 * 1000;

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

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress - 
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static String pad2(int c) {
        if (c == 12)
            return String.valueOf(c);
        if (c == 00)
            return String.valueOf(c + 12);
        if (c > 12)
            return String.valueOf(c - 12);
        else
            return String.valueOf(c);
    }

    public static String pad3(int c) {
        if (c == 12)
            return " PM";
        if (c == 00)
            return " AM";
        if (c > 12)
            return " PM";
        else
            return " AM";
    }

    public static long getDateInMilisFromTime(long checkedTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(checkedTime);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(year, month, day, 0, 0, 0);
        return calendar2.getTimeInMillis();

    }
}
