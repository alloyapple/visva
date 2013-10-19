package com.visva.android.ailatrieuphu_visva.highscore;

import android.content.Context;
import android.content.SharedPreferences;

public class ALTPPreferences {
	private static final String SHOPIE_PREFERENCES = "SHOPIE_PREFERENCES";
	public static final String[] KEY_NAME = { "NAME1", "NAME2", "NAME3",
			"NAME4", "NAME5", "NAME6", "NAME7", "NAME8", "NAME9", "NAME10" };
	public static final String[] KEY_SCORE = { "SCORE1", "SCORE2", "SCORE3",
			"SCORE4", "SCORE5", "SCORE6", "SCORE7", "SCORE8", "SCORE9",
			"SCORE10" };
	// ================================================================

	private Context context;

	public ALTPPreferences(Context context) {
		this.context = context;
	}

	public void setNames(String[] name) {
		putStringValue("NAME1", name[0]);
		putStringValue("NAME2", name[1]);
		putStringValue("NAME3", name[2]);
		putStringValue("NAME4", name[3]);
		putStringValue("NAME5", name[4]);
		putStringValue("NAME6", name[5]);
		putStringValue("NAME7", name[6]);
		putStringValue("NAME8", name[7]);
		putStringValue("NAME9", name[8]);
		putStringValue("NAME10", name[9]);

	}

	public String[] getNames() {
		String result[] = new String[10];
		result[0] = getStringValue("NAME1", "");
		result[1] = getStringValue("NAME2", "");
		result[2] = getStringValue("NAME3", "");
		result[3] = getStringValue("NAME4", "");
		result[4] = getStringValue("NAME5", "");
		result[5] = getStringValue("NAME6", "");
		result[6] = getStringValue("NAME7", "");
		result[7] = getStringValue("NAME8", "");
		result[8] = getStringValue("NAME9", "");
		result[9] = getStringValue("NAME10", "");
		return result;
	}

	public void setScores(int score[]) {
		putIntValue("SCORE1", score[0]);
		putIntValue("SCORE2", score[1]);
		putIntValue("SCORE3", score[2]);
		putIntValue("SCORE4", score[3]);
		putIntValue("SCORE5", score[4]);
		putIntValue("SCORE6", score[5]);
		putIntValue("SCORE7", score[6]);
		putIntValue("SCORE8", score[7]);
		putIntValue("SCORE9", score[8]);
		putIntValue("SCORE10", score[9]);
	}

	public int[] getScores() {
		int result[] = new int[10];
		result[0] = getIntValue("SCORE1");
		result[1] = getIntValue("SCORE2");
		result[2] = getIntValue("SCORE3");
		result[3] = getIntValue("SCORE4");
		result[4] = getIntValue("SCORE5");
		result[5] = getIntValue("SCORE6");
		result[6] = getIntValue("SCORE7");
		result[7] = getIntValue("SCORE8");
		result[8] = getIntValue("SCORE9");
		result[9] = getIntValue("SCORE10");
		return result;
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	/**
	 * Save an float to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putFloatValue(String key, float f) {
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				SHOPIE_PREFERENCES, 0);
		return pref.getFloat(key, 0.0f);
	}
}
