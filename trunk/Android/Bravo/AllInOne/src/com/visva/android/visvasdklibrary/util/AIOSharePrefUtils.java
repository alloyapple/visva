package com.visva.android.visvasdklibrary.util;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class AIOSharePrefUtils {
    private static final String AIO_SHARE_PREFS = "aio_common_share_prefs";

    public static void putLongValue(Context mContext, String key, long n) {
        SharedPreferences pref = mContext.getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, n);
        editor.commit();
    }

    public static long getLongValue(Context mContext, String key) {
        SharedPreferences pref = mContext.getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getLong(key, -1);
    }

    public static long getLongValue(Context mContext, String key, long mDeafult) {
        SharedPreferences pref = mContext.getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getLong(key, mDeafult);
    }

    public static void putIntValue(Context mContext, String key, int n) {
        SharedPreferences pref = mContext.getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n);
        editor.commit();
    }

    public static int getIntValue(Context mContext, String key) {
        SharedPreferences pref = mContext.getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getInt(key, -1);
    }

    public static int getIntValue(Context mContext, String key, int mDeafult) {
        SharedPreferences pref = mContext.getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getInt(key, mDeafult);
    }

    public static void putStringValue(Context mContext, String key, String s) {
        SharedPreferences pref = mContext.getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.commit();
    }

    public static String getStringValue(Context mContext, String key) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getString(key, "");
    }

    public static String getStringValue(Context mContext, String key, String defaultValue) {
        SharedPreferences pref = mContext. getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getString(key, defaultValue);
    }

    public static void putBooleanValue(Context mContext, String key, Boolean b) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    public static boolean getBooleanValue(Context mContext, String key) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getBoolean(key, false);
    }

    public static boolean getBooleanValue(Context mContext, String key, boolean mDefault) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getBoolean(key, mDefault);
    }

    public static void putFloatValue(Context mContext, String key, float f) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, f);
        editor.commit();
    }

    public static float getFloatValue(Context mContext, String key) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getFloat(key, 0.0f);
    }

    public static float getFloatValue(Context mContext, String key, float mDefault) {
        SharedPreferences pref = mContext.getSharedPreferences(AIO_SHARE_PREFS,
                Context.MODE_MULTI_PROCESS);
        return pref.getFloat(key, mDefault);
    }

    public static void putStringSet(Context mContext, String key, Set<String> s) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, s);
        editor.commit();
    }

    public static Set<String> getStringSet(Context mContext, String key) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        return pref.getStringSet(key, null);
    }

    public static void remove(Context mContext, String key) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    public static boolean isKeyExist(Context mContext, String key) {
        SharedPreferences pref = mContext.
                getSharedPreferences(AIO_SHARE_PREFS, Context.MODE_MULTI_PROCESS);
        if (pref.contains(key)) {
            return true;
        }
        return false;
    }
}
