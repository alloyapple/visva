package com.japanappstudio.IDxPassword.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * id manager android share preference class which saves setting values
 * 
 * @author visva team
 */
public class IdManagerPreference {

	public static final String IDMANAGER_SHARE_PREFERENCE = "IDxPASSWORD_SHARE_PREFERENCE";

	// ================================================================

	private static IdManagerPreference instance;

	private Context context;

	private IdManagerPreference() {
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @return
	 */
	public static IdManagerPreference getInstance(Context context) {
		if (instance == null) {
			instance = new IdManagerPreference();
			instance.context = context;
		}
		return instance;
	}

	// =========================Setting Preferences =====================

	public static final String APPLICATION_INSTALL_FIRST_TIME = "APPLICATION_INSTALL_FIRST_TIME";
	public static final String MASTER_PASSWORD = "MASTER_PASSWORD";
	public static final String REMOVE_DATA = "REMOVE_DATA";
	public static final String WEIGHT_SLIDE_ITEM = "WEIGHT_SLIDE_ITEM";
	public static final String SECURITY_MODE = "SECURITY_MODE";
	public static final String NUMBER_ITEMS = "NUMBER_ITEMS";
	public static final String IS_PAYMENT_UNLIMIT = "IS_PAYMENT_UNLIMIT";
	public static final String IS_PAYMENT_NO_AD = "IS_PAYMENT_NO_AD";
	public static final String IS_PAYMENT_EXPORT = "IS_PAYMENT_EXPORT";
	public static final String IS_SECURITY_LOOP = "IS_SECURITY_LOOP";
	public static final String LAST_TIME_SYNC_CLOUD = "LAST_TIME_SYNC_CLOUD";
	public static final String GOOGLE_ACCOUNT_NAME_SESSION = "GOOGLE_ACCOUNT_NAME_SESSION";
	public static final String IS_EDIT_MODE = "IS_EDIT_MODE";
	public static final String CURRENT_FOLDER_ID = "CURRENT_FOLDER_ID";
	public static final String DROPBOX_CHECK_LOGIN = "DROPBOX_CHECK_LOGIN";
	public static final String KEY_AUTHEN_GG_DRIVE = "KEY_AUTHEN_GG_DRIVE";
	public static final String KEY_PAUSE_APP = "PAUSE_APP";
	public static final String KEY_CLOUD_SETTING_SUCCESS = "KEY_CLOUD_SETTING_SUCCESS";
	public static final String KEY_IS_EDIT_ON_HOME = "KEY_IS_EDIT_ON_HOME";

	/**
	 * // ======================== CORE FUNCTIONS ========================
	 * 
	 */

	/* set vs get project is first time installed */
	public void setApplicationFirstTimeInstalled(String key, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public boolean isApplicationFirstTimeInstalled(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, true);
	}

	public void setApplicationFirstTimeInstalled(boolean b) {
		setApplicationFirstTimeInstalled(APPLICATION_INSTALL_FIRST_TIME, b);
	}

	public boolean isApplicationFirstTimeInstalled() {
		boolean b;
		b = isApplicationFirstTimeInstalled(APPLICATION_INSTALL_FIRST_TIME);
		return b;
	}

	/* set vs get master password */
	public void setMasterPW(String key, String pw) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, pw);
		editor.commit();
	}

	public String getMasterPW(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getString(key, "");
	}

	public void setMasterPW(String b) {
		setMasterPW(MASTER_PASSWORD, b);
	}

	public String getMasterPW() {
		String b;
		b = getMasterPW(MASTER_PASSWORD);
		return b;
	}

	/* set vs get values remove data */
	public void setValuesremoveData(String key, int dataValues) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, dataValues);
		editor.commit();
	}

	public int getValuesRemoveData(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getInt(key, 0);
	}

	public void setValuesremoveData(int b) {
		setValuesremoveData(REMOVE_DATA, b);
	}

	public int getValuesRemoveData() {
		int b;
		b = getValuesRemoveData(REMOVE_DATA);
		return b;
	}

	/* set vs get values security mode */
	public void setSecurityMode(String key, int securityMode) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, securityMode);
		editor.commit();
	}

	public int getSecurityMode(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getInt(key, 0);
	}

	public void setSecurityMode(int b) {
		setSecurityMode(SECURITY_MODE, b);
	}

	public int getSecurityMode() {
		int b;
		b = getSecurityMode(SECURITY_MODE);
		return b;
	}

	/* set vs get is security loop */
	public void setSecurityLoop(String key, boolean securityMode) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, securityMode);
		editor.commit();
	}

	public boolean isSecurityLoop(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, true);
	}

	public void setSecurityLoop(boolean b) {
		setSecurityLoop(IS_SECURITY_LOOP, b);
	}

	public boolean isSecurityLoop() {
		boolean b;
		b = isSecurityLoop(IS_SECURITY_LOOP);
		return b;
	}

	/* set vs get last time sync cloud */
	public void setLastTimeSyncCloud(String key, long lastTimeSync) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, lastTimeSync);
		editor.commit();
	}

	public long getLastTimeSyncCloud(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getLong(key, 0);
	}

	public void setNumberItem(String key, int number) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, number);
		editor.commit();
	}

	public int getNumberItems(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getInt(key, 0);
	}

	public int getNumberItems() {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getInt(NUMBER_ITEMS, 0);
	}

	public void setIsPaymentUnlimit(String key, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public void setIsPaymentUnlimit(boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(IS_PAYMENT_UNLIMIT, b);
		editor.commit();
	}

	public void setIsPaymentNoAd(String key, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public void setIsPaymentNoAd(boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(IS_PAYMENT_NO_AD, b);
		editor.commit();
	}

	public void setIsPaymentExport(String key, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public void setIsPaymentExport(boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(IS_PAYMENT_EXPORT, b);
		editor.commit();
	}

	public boolean getIsPaymentUnlimit(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, false);
	}

	public boolean getIsPaymentUnlimit() {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(IS_PAYMENT_UNLIMIT, false);
	}

	public boolean getIsPaymentNoAd(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, false);
	}

	public boolean getIsPaymentNoAd() {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(IS_PAYMENT_NO_AD, false);
	}

	public boolean getIsPaymentExport(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, false);
	}

	public boolean getIsPaymentExport() {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(IS_PAYMENT_EXPORT, false);
	}

	public void setLastTimeSyncCloud(long b) {
		setLastTimeSyncCloud(LAST_TIME_SYNC_CLOUD, b);
	}

	public long getLastTimeSyncCloud() {
		long b;
		b = getLastTimeSyncCloud(LAST_TIME_SYNC_CLOUD);
		return b;
	}

	/* set vs get google acc name */
	public void setGoogleAccNameSession(String key, String acc) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, acc);
		editor.commit();
	}

	public String getGoogleAccNameSession(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getString(key, "");
	}

	public void setGoogleAccNameSession(String b) {
		setGoogleAccNameSession(GOOGLE_ACCOUNT_NAME_SESSION, b);
	}

	public String getGoogleAccNameSession() {
		String b;
		b = getGoogleAccNameSession(GOOGLE_ACCOUNT_NAME_SESSION);
		return b;
	}

	/* set vs get edit mode */
	public void setEditMode(String key, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public boolean isEditMode(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, false);
	}

	public void setEditMode(boolean b) {
		setEditMode(IS_EDIT_MODE, b);
	}

	public boolean isEditMode() {
		boolean b;
		b = isEditMode(IS_EDIT_MODE);
		return b;
	}

	public void setDropboxLogin(boolean b) {
		setDropboxLogin(DROPBOX_CHECK_LOGIN, b);
	}

	private void setDropboxLogin(String key, boolean b) {
		// TODO Auto-generated method stub
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public boolean isDropboxLogin() {
		boolean b;
		b = isDropboxLogin(DROPBOX_CHECK_LOGIN);
		return b;
	}

	private boolean isDropboxLogin(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, false);
	}

	/* set vs get current folder id */
	public void setCurrentFolderId(String key, int acc) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, acc);
		editor.commit();
	}

	public int getCurrentFolderId(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getInt(key, 0);
	}

	public void setCurrentFolderId(int b) {
		setCurrentFolderId(CURRENT_FOLDER_ID, b);
	}

	public int getCurrentFolderId() {
		int b;
		b = getCurrentFolderId(CURRENT_FOLDER_ID);
		return b;
	}

	/* set vs get key authen gg */
	public void setKeyAuthenGGDrive(String key, String pw) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, pw);
		editor.commit();
	}

	public String getKeyAuthenGGDrive(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getString(key, "");
	}

	public float getWeightSlideItem() {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getFloat(WEIGHT_SLIDE_ITEM, -1);
	}

	public void setWeightSlideItem(float w) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putFloat(WEIGHT_SLIDE_ITEM, w);
		editor.commit();
	}

	public void setKeyAuthenGGDrive(String b) {
		setKeyAuthenGGDrive(KEY_AUTHEN_GG_DRIVE, b);
	}

	public String getKeyAuthenGGDrive() {
		String b;
		b = getMasterPW(KEY_AUTHEN_GG_DRIVE);
		return b;
	}

	public boolean getIsPauseApp() {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(KEY_PAUSE_APP, false);
	}

	public void setPauseApp(boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_PAUSE_APP, b);
		editor.commit();
	}

	/* set vs get project is first time installed */
	public void setCloudSettingSuccess(String key, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public boolean isCloudSettingSuccess(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, false);
	}

	public void setCloudSettingSuccess(boolean b) {
		setCloudSettingSuccess(KEY_CLOUD_SETTING_SUCCESS, b);
	}

	public boolean isCloudSettingSuccess() {
		boolean b;
		b = isCloudSettingSuccess(KEY_CLOUD_SETTING_SUCCESS);
		return b;
	}

	public void setEditIDxPassHome(boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(KEY_IS_EDIT_ON_HOME, b);
		editor.commit();
	}
	public boolean isEditIDxPassHome() {
		SharedPreferences pref = context.getSharedPreferences(
				IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getBoolean(KEY_IS_EDIT_ON_HOME, false);
	}
}
