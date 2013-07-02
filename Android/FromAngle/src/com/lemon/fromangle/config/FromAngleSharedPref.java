package com.lemon.fromangle.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class which saves setting values
 * 
 * @author Lemon
 */
public class FromAngleSharedPref {


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
	public static final String VALIDATION_OK = "VALIDATION_OK";
	public static final String VALIDATION_CLOSE = "VALIDATION_CLOSE";
	public static final String RUN_FROM_ACTIVITY = "RUN_FROM_ACTIVITY";
	public static final String FIRST_TIME_SETTING = "FIRST_TIME_SETTING";
	public static final String KEY_STOP_ALARM = "KEY_STOP_ALARM";
	public static final String KEY_START_SERVICE = "KEY_START_SERVICE";
	public static final String KEY_RUN_ON_BACK_GROUND = "KEY_RUN_ON_BACK_GROUND";
	public static final String KEY_EXIST_BY_TOP_SCREEN = "KEY_EXIST_BY_TOP_SCREEN";
	public static final String KEY_CHECK_OPEN_DIALOG_REMINDER = "KEY_CHECK_OPEN_DIALOG_REMINDER";
	public static final String KEY_CHECK_VIBRATE = "KEY_CHECK_VIBRATE";
	public static final String KEY_CHECK_RINGTONE = "KEY_CHECK_RINGTONE";
	public static final String KEY_APP_STATUS = "KEY_APP_STATUS";
	public static final String KEY_RUN_ALARM = "KEY_RUN_ALARM";
	public static final String KEY_MODE_SERVICE_DESTROYED = "KEY_MODE_SERVICE_DESTROYED";

	public static final String KEY_SAVE_INPUT_MESSAGE = "SAVE_INPUT_MESSAGE";
	public static final String[] KEY_MESSGE1 = { "OWN_NAME_01", "EMAIL_01",
			"PHONE_01", "MESSAGE_01" };
	public static final String[] KEY_MESSGE2 = { "OWN_NAME_02", "EMAIL_02",
			"PHONE_02", "MESSAGE_02" };
	public static final String[] KEY_MESSGE3 = { "OWN_NAME_03", "EMAIL_03",
			"PHONE_03", "MESSAGE_03" };
	// ================================================================

	private Context context;

	public FromAngleSharedPref(Context context) {
		this.context = context;
	}

	// ======================== UTILITY FUNCTIONS ========================

	public void setCheckVibrate(boolean b) {
		putBooleanValue(KEY_CHECK_VIBRATE, b);
	}

	public boolean getCheckVibrate() {
		return getBooleanValue(KEY_CHECK_VIBRATE);
	}

	public void setVibrateMode(boolean value) {

		putBooleanValue(PREF_SETTING_VIBRATE_MODE, value);
	}

	public boolean getVibrateMode() {
		return getBooleanValue(PREF_SETTING_VIBRATE_MODE);
	}

	public void setKeyRunAlarm(boolean b) {
		putBooleanValue(KEY_RUN_ALARM, b);
	}

	public boolean getKeyRunAlarm() {
		return getBooleanValue(KEY_RUN_ALARM);
	}

	public void setOpenDialogReminder(boolean value) {
		putBooleanValue(KEY_CHECK_OPEN_DIALOG_REMINDER, value);
	}

	public boolean getOpenDialogReminder() {
		return getBooleanValue(KEY_CHECK_OPEN_DIALOG_REMINDER);
	}

	public void setRingTuneFile(String fileName) {

		putStringValue(PREF_SETTING_RING_TUNE_URL, fileName);
	}

	public String getRingTuneFile() {
		return getStringValue(PREF_SETTING_RING_TUNE_URL);
	}

	public void putAppStatus(String status) {
		putStringValue(KEY_APP_STATUS, status);
	}

	public String getAppStatus() {
		return getStringValue(KEY_APP_STATUS);
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

	public void setValidationMode(int mode) {
		putIntValue(VALIDATION_OK, mode);
	}

	public int getValidationMode() {
		return getIntValue(VALIDATION_OK);
	}

	public void setRunFromActivity(boolean b) {
		putBooleanValue(RUN_FROM_ACTIVITY, b);
	}

	public boolean getRunFromActivity() {
		return getBooleanValue(RUN_FROM_ACTIVITY);
	}

	public void setFirstTimeSetting(boolean b) {
		putBooleanValue(FIRST_TIME_SETTING, b);
	}

	public boolean getFirstTimeSetting() {
		return getBooleanValue(FIRST_TIME_SETTING);
	}

	public void setStopAlarm(boolean b) {
		putBooleanValue(KEY_STOP_ALARM, b);
	}

	public boolean getStopAlarm() {
		return getBooleanValue(KEY_STOP_ALARM);
	}

	public void setStartService(boolean b) {
		putBooleanValue(KEY_START_SERVICE, b);
	}

	public boolean getStartService() {
		return getBooleanValue(KEY_START_SERVICE);
	}

	public void setRunOnBackGround(boolean b) {
		putBooleanValue(KEY_RUN_ON_BACK_GROUND, b);
	}

	public boolean getRunOnBackGround() {
		return getBooleanValue(KEY_RUN_ON_BACK_GROUND);
	}

	public void setExistByTopScreen(boolean b) {
		putBooleanValue(KEY_EXIST_BY_TOP_SCREEN, b);
	}

	public boolean getExistByTopScreen() {
		return getBooleanValue(KEY_EXIST_BY_TOP_SCREEN);
	}

	public void setMessageSettingTab1(String ownname, String email,
			String phone, String message) {
		putStringValue(KEY_MESSGE1[0], ownname);
		putStringValue(KEY_MESSGE1[1], email);
		putStringValue(KEY_MESSGE1[2], phone);
		putStringValue(KEY_MESSGE1[3], message);
	}

	public void setMessageSettingTab2(String ownname, String email,
			String phone, String message) {
		putStringValue(KEY_MESSGE2[0], ownname);
		putStringValue(KEY_MESSGE2[1], email);
		putStringValue(KEY_MESSGE2[2], phone);
		putStringValue(KEY_MESSGE2[3], message);
	}

	public void setMessageSettingTab3(String ownname, String email,
			String phone, String message) {
		putStringValue(KEY_MESSGE3[0], ownname);
		putStringValue(KEY_MESSGE3[1], email);
		putStringValue(KEY_MESSGE3[2], phone);
		putStringValue(KEY_MESSGE3[3], message);
	}

	public String[] getMessageSettingTab1() {
		String result[] = new String[4];
		result[0] = getStringValue(KEY_MESSGE1[0], "");
		result[1] = getStringValue(KEY_MESSGE1[1], "");
		result[2] = getStringValue(KEY_MESSGE1[2], "");
		result[3] = getStringValue(KEY_MESSGE1[3], "");
		return result;
	}

	public String[] getMessageSettingTab2() {
		String result[] = new String[4];
		result[0] = getStringValue(KEY_MESSGE2[0], "");
		result[1] = getStringValue(KEY_MESSGE2[1], "");
		result[2] = getStringValue(KEY_MESSGE2[2], "");
		result[3] = getStringValue(KEY_MESSGE2[3], "");
		return result;
	}

	public String[] getMessageSettingTab3() {
		String result[] = new String[4];
		result[0] = getStringValue(KEY_MESSGE3[0], "");
		result[1] = getStringValue(KEY_MESSGE3[1], "");
		result[2] = getStringValue(KEY_MESSGE3[2], "");
		result[3] = getStringValue(KEY_MESSGE3[3], "");
		return result;
	}

	public void saveInputMessage(boolean b) {
		putBooleanValue(KEY_SAVE_INPUT_MESSAGE, b);
	}

	public boolean getSaveInputMassage() {
		return getBooleanValue(KEY_SAVE_INPUT_MESSAGE);
	}

	public void putModeDestroyedService(int mode) {
		putIntValue(KEY_MODE_SERVICE_DESTROYED, mode);
	}

	public int getModeDestroyedService() {
		return getIntValue(KEY_MODE_SERVICE_DESTROYED);
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
