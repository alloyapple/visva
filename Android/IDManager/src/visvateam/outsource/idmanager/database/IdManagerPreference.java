package visvateam.outsource.idmanager.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * id manager android share preference class which saves setting values
 * 
 * @author visva team
 */
public class IdManagerPreference {

	public static final String IDMANAGER_SHARE_PREFERENCE = "LEANAPP_SHARE_PREFERENCE";

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
	public static final String SECURITY_MODE = "SECURITY_MODE";
	public static final String IS_SECURITY_LOOP = "IS_SECURITY_LOOP";
	public static final String LAST_TIME_SYNC_CLOUD = "LAST_TIME_SYNC_CLOUD";

	/**
	 * // ======================== CORE FUNCTIONS ========================
	 * 
	 */

	/* set vs get project is first time installed */
	public void setApplicationFirstTimeInstalled(String key, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public boolean isApplicationFirstTimeInstalled(String key) {
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
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
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, pw);
		editor.commit();
	}

	public String getMasterPW(String key) {
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
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
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, dataValues);
		editor.commit();
	}

	public int getValuesRemoveData(String key) {
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
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
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, securityMode);
		editor.commit();
	}

	public int getSecurityMode(String key) {
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
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
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, securityMode);
		editor.commit();
	}

	public boolean isSecurityLoop(String key) {
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
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
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, lastTimeSync);
		editor.commit();
	}

	public long getLastTimeSyncCloud(String key) {
		SharedPreferences pref = context.getSharedPreferences(IDMANAGER_SHARE_PREFERENCE, 0);
		return pref.getLong(key, 0);
	}

	public void setLastTimeSyncCloud(long b) {
		setLastTimeSyncCloud(LAST_TIME_SYNC_CLOUD, b);
	}

	public long getLastTimeSyncCloud() {
		long b;
		b = getLastTimeSyncCloud(LAST_TIME_SYNC_CLOUD);
		return b;
	}
}
