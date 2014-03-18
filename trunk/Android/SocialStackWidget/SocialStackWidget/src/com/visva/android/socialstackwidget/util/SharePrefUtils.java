package com.visva.android.socialstackwidget.util;

import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefUtils {
    private static final String SOCIAL_STACKS_PREF = "SOCIAL_STACKS_PREF";

    public static void putLongValue(Context context, String key, long n) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, n);
        editor.commit();
    }

    public static long getLongValue(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        return pref.getLong(key, -1);
    }

    public static void putIntValue(Context context, String key, int n) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n);
        editor.commit();
    }

    public static int getIntValue(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        return pref.getInt(key, -1);
    }

    public static void putStringValue(Context context, String key, String s) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.commit();
    }

    public static String getStringValue(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        return pref.getString(key, "");
    }

    public static String getStringValue(Context context, String key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        return pref.getString(key, defaultValue);
    }

    public static void putBooleanValue(Context context, String key, Boolean b) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    public static boolean getBooleanValue(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        return pref.getBoolean(key, false);
    }

    public static void putFloatValue(Context context, String key, float f) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, f);
        editor.commit();
    }

    public static float getFloatValue(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        return pref.getFloat(key, 0.0f);
    }

    public static void putStringSet(Context context, String key, Set<String> s) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, s);
        editor.commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        return pref.getStringSet(key, null);
    }

    public static void remove(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SOCIAL_STACKS_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }
}
