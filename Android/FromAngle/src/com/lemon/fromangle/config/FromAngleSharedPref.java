package com.lemon.fromangle.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class which saves setting values
 * 
 * @author Lemon
 */
public class FromAngleSharedPref {

	private String TAG = getClass().getSimpleName();

	public static final String APPLICATION_INSTALL_FIRST_TIME = "APPLICATION_INSTALL_FIRST_TIME";

	public static final String PREF_SETTING_USER_ID = "PREF_SETTING_USER_ID";

	public static final String PREF_SETTING_USER_NAME = "PREF_SETTING_USER_NAME";

	public static final String PREF_SETTING_VIBRATE_MODE = "PREF_SETTING_VIBRATE_MODE";

	public static final String PREF_SETTING_RING_TUNE_URL = "PREF_SETTING_RING_TUNE_URL";

	public static final String FINISH_SETTTING_MESSAGE = "FINISH_SETTTING_MESSAGE";
	public static final String FINISH_SETTTING = "FINISH_SETTTING";

	public static final String FINISH_VALIDATE = "FINISH_VALIDATE";
	public static final String LAST_VALIDATE_DATE = "LAST_VALIDATE_DATE";
	public static final String NEXT_VALIDATE_DATE = "NEXT_VALIDATE_DATE";
	public static final String SETTING_USER_NAME = "SETTING_USER_NAME";
	public static final String SETTING_EMAIL = "SETTING_EMAIL";
	public static final String SETTING_TELEPHONE = "SETTING_TELEPHONE";
	public static final String SETTING_USER_ID = "SETTING_USER_ID";
	public static final String SETTING_VALIDATE_DATE = "SETTING_VALIDATE_DATE";
	public static final String SETTING_VALIDATE_TIME = "SETTING_VALIDATE_TIME";
	public static final String SETTING_VALIDATE_DAYS_AFTER = "SETTING_VALIDATE_DAYS_AFTER";
	public static final String TOP_SCREEN_FINAL_VALIDATION = "TOP_SCREEN_FINAL_VALIDATION";
	public static final String TOP_SCREEN_NEXT_VALIDATION = "TOP_SCREEN_NEXT_VALIDATION";
	public static final String MESSAGE_SETTING_STATUS = "MESSAGE_SETTING_STATUS";

	// ================================================================

	private Context context;

	public FromAngleSharedPref(Context context) {
		this.context = context;
	}

	// ======================== UTILITY FUNCTIONS ========================

	public void setFirstIntall() {

		putBooleanValue(APPLICATION_INSTALL_FIRST_TIME, true);
	}

	public boolean getFirstIntall() {
		return getBooleanValue(APPLICATION_INSTALL_FIRST_TIME);
	}

	public void setFinishValidate() {

		putBooleanValue(FINISH_VALIDATE, true);
	}

	public boolean getFinishValidate() {
		return getBooleanValue(FINISH_VALIDATE);
	}

	public void setFinishSettingMessage() {

		putBooleanValue(FINISH_SETTTING_MESSAGE, true);
	}

	public boolean getFinishSettingMessage() {
		return getBooleanValue(FINISH_SETTTING_MESSAGE);
	}

	public void setFinishSetting() {
		putBooleanValue(FINISH_SETTTING, true);
	}

	public boolean getFinishSetting() {
		return getBooleanValue(FINISH_SETTTING);
	}

	public void setVibrateMode(boolean value) {

		putBooleanValue(PREF_SETTING_VIBRATE_MODE, value);
	}

	public boolean getVibrateMode() {
		return getBooleanValue(PREF_SETTING_VIBRATE_MODE);
	}

	public void setRingTuneFile(String fileName) {

		putStringValue(PREF_SETTING_RING_TUNE_URL, fileName);
	}

	public String getRingTuneFile() {
		return getStringValue(PREF_SETTING_RING_TUNE_URL);
	}

	public void setUserName(String fileName) {

		putStringValue(SETTING_USER_NAME, fileName);
	}

	public String getUserName() {
		return getStringValue(SETTING_USER_NAME);
	}

	public void setUserId(String userId) {
		putStringValue(SETTING_USER_ID, userId);
	}

	public String getUserId() {
		return getStringValue(SETTING_USER_ID);
	}

	public void setEmail(String fileName) {

		putStringValue(SETTING_EMAIL, fileName);
	}

	public String getEmail() {
		return getStringValue(SETTING_EMAIL);
	}

	public void setPhone(String phone) {

		putStringValue(SETTING_TELEPHONE, phone);
	}

	public String getPhone() {
		return getStringValue(SETTING_TELEPHONE);
	}

	public void setValidationDate(String date) {
		putStringValue(SETTING_VALIDATE_DATE, date);
	}

	public String getValidationDate() {
		return getStringValue(SETTING_VALIDATE_DATE);
	}

	public void setValidationTime(String time) {
		putStringValue(SETTING_VALIDATE_TIME, time);
	}

	public String getValidationTime() {
		return getStringValue(SETTING_VALIDATE_TIME);
	}

	public void setValidationDaysAfter(String daysAfter) {
		putStringValue(SETTING_VALIDATE_DAYS_AFTER, daysAfter);
	}

	public String getValidationDaysAfter() {
		return getStringValue(SETTING_VALIDATE_DAYS_AFTER);
	}

	public void setTopScreenFinalValidation(String finalValidation) {
		putStringValue(TOP_SCREEN_FINAL_VALIDATION, finalValidation);
	}

	public String getTopScreenFinalValidation() {
		return getStringValue(TOP_SCREEN_FINAL_VALIDATION);
	}

	public void setTopScreenNextValidation(String nextValidation) {
		putStringValue(TOP_SCREEN_NEXT_VALIDATION, nextValidation);
	}

	public String getTopScreenNextValidation() {
		return getStringValue(TOP_SCREEN_NEXT_VALIDATION);
	}

	public void setMessageSettingStatus(String status) {
		putStringValue(MESSAGE_SETTING_STATUS, status);
	}

	public String getMessageSettingStatus() {
		return getStringValue(MESSAGE_SETTING_STATUS);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
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
				GlobalValue.FROM_ANGLE_PREFERENCES, 0);
		return pref.getFloat(key, 0.0f);
	}
}
