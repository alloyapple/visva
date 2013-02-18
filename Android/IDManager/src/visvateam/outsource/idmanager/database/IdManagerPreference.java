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
	public static final String MASTER_PASSWORD ="MASTER_PASSWORD"; 

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
	/* set vs get master password  */
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
}
