package com.sharebravo.bravo.utils;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * always share preference to save base information of always data
 * 
 * @author kieu.thang
 */
public class BravoSharePrefs {
    // ================================================================
    private Context context;

    public BravoSharePrefs(Context context) {
        this.context = context;
    }

    // ======================== CORE FUNCTIONS ========================

    /**
     * Save a long integer to SharedPreferences
     * 
     * @param key
     * @param n
     */
    public void putLongValue(String key, long n) {
        // SmartLog.log(TAG, "Set long integer value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, n);
        editor.commit();
    }

    /**
     * Read a long integer to SharedPreferences
     * 
     * @param key
     * @return
     */
    public long getLongValue(String key) {
        // SmartLog.log(TAG, "Get long integer value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        return pref.getLong(key, 0);
    }

    /**
     * Save an integer to SharedPreferences
     * 
     * @param key
     * @param n
     */
    public void putIntValue(String key, int n) {
        // SmartLog.log(TAG, "Set integer value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, n);
        editor.commit();
    }

    /**
     * Read an integer to SharedPreferences
     * 
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        // SmartLog.log(TAG, "Get integer value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        return pref.getInt(key, 0);
    }

    /**
     * Save an string to SharedPreferences
     * 
     * @param key
     * @param s
     */
    public void putStringValue(String key, String s) {
        // SmartLog.log(TAG, "Set string value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.commit();
    }

    /**
     * Read an string to SharedPreferences
     * 
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        // SmartLog.log(TAG, "Get string value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        return pref.getString(key, "");
    }

    /**
     * Read an string to SharedPreferences
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        // SmartLog.log(TAG, "Get string value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        return pref.getString(key, defaultValue);
    }

    /**
     * Save an boolean to SharedPreferences
     * 
     * @param key
     * @param s
     */
    public void putBooleanValue(String key, Boolean b) {
        // SmartLog.log(TAG, "Set boolean value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    /**
     * Read an boolean to SharedPreferences
     * 
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        // SmartLog.log(TAG, "Get boolean value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        return pref.getBoolean(key, false);
    }

    /**
     * Save an float to SharedPreferences
     * 
     * @param key
     * @param s
     */
    public void putFloatValue(String key, float f) {
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, f);
        editor.commit();
    }

    /**
     * Read an float to SharedPreferences
     * 
     * @param key
     * @return
     */
    public float getFloatValue(String key) {
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        return pref.getFloat(key, 0.0f);
    }

    public void putStringSet(String key, Set<String> s) {
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(key, s);
        editor.commit();
    }

    public Set<String> getStringSet(String key) {
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        return pref.getStringSet(key, null);
    }

    public void remove(String key) {
        // SmartLog.log(TAG, "Set boolean value");
        SharedPreferences pref = context.getSharedPreferences(BravoConstant.BRAVO_PREFERENCE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

}
