package com.visva.android.hangman.ultis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.EditText;

/**
 * StringUtility class
 * 
 * @author kieu.thang
 * 
 */
@SuppressLint("SimpleDateFormat")
public final class StringUtility {
    /**
     * Check Edit Text input string
     * 
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        if (editText == null || editText.getEditableText() == null || editText.getEditableText().toString().trim().equalsIgnoreCase("")) {
            return true;
        }
        return false;
    }

    public static boolean checkEmail2(String email) {

        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    /**
     * Check input string
     * 
     * @param editText
     * @return
     */
    public static boolean isEmpty(String editText) {
        if (editText == null || editText.trim().equalsIgnoreCase("")) {
            return true;
        }
        return false;
    }

    public static boolean isTheSame(Context context, EditText editText,
            int stringId) {
        if (editText == null || editText.getText().toString().trim().equalsIgnoreCase("")) {
            return false;

        }
        if (editText.getText().toString().trim().equalsIgnoreCase(context.getString(stringId)))
            return true;
        return false;
    }

    public static boolean isTheSame(EditText editText, EditText editText2) {
        if (editText == null || !editText.getText().toString().trim().equalsIgnoreCase(editText2.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    public static String getSubString(String input, int maxLength) {
        String temp = input;
        if (input.length() < maxLength)
            return temp;
        else
            return input.substring(0, maxLength - 1) + "...";
    }

    /**
     * Merge all elements of a string array into a string
     * 
     * @param strings
     * @param separator
     * @return
     */
    public static String join(String[] strings, String separator) {
        StringBuffer sb = new StringBuffer();
        int max = strings.length;
        for (int i = 0; i < max; i++) {
            if (i != 0)
                sb.append(separator);
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    /**
     * Convert current date time to string
     * 
     * @param updateTime
     * @return
     */
    public static String convertNowToFullDateString() {
        SimpleDateFormat dateformat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        // dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Calendar calendar =
        // Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        // return dateformat.format(calendar.getTime());
        return dateformat.format(new Date());
    }

    public static String convertNowToDateString(String format) {
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        // dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Calendar calendar =
        // Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        // return dateformat.format(calendar.getTime());
        return dateformat.format(new Date());
    }

    /**
     * Initial sync date string
     * 
     * @return
     */
    public static String initDateString() {
        return "1900-01-01 09:00:00";
    }

    /**
     * Convert a string divided by ";" to multiple xmpp users
     * 
     * @param userString
     * @return
     */
    public static String[] convertStringToXmppUsers(String userString) {
        return userString.split(";");
    }

    /**
     * get Unique Random String
     * 
     * @return
     */

    public static String getUniqueRandomString() {

        // return String.valueOf(System.currentTimeMillis());
        UUID uuid = UUID.randomUUID();
        return uuid.toString();

    }
}
