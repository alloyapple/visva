package com.visva.voicerecorder.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * always share preference to save base information of my recorder data
 * 
 * @author kieu.thang
 */
public class MyCallRecorderSharePrefs {
    // ================================================================
    private static final String MY_RECORDER_CALL_PREF = "my_recorder_call_pref";
    private Context               mContext;
    
    /* singleton class */
    private static MyCallRecorderSharePrefs mInstance;

    /**
     * BravoSharePrefs constructor
     * 
     * @param context
     */
    public MyCallRecorderSharePrefs(Context context) {
        super();
        mContext = context;

    }

    /**
     * get instance of BravoSharePrefs singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized MyCallRecorderSharePrefs getInstance(Context context) {
        if (mInstance == null)
            mInstance = new MyCallRecorderSharePrefs(context);
        return mInstance;
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
        return pref.getBoolean(key, true);
    }

    /**
     * Save an float to SharedPreferences
     * 
     * @param key
     * @param s
     */
    public void putFloatValue(String key, float f) {
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
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
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
        return pref.getFloat(key, 0.0f);
    }

    public void remove(String key) {
        // SmartLog.log(TAG, "Set boolean value");
        SharedPreferences pref = mContext.getSharedPreferences(MY_RECORDER_CALL_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

}
