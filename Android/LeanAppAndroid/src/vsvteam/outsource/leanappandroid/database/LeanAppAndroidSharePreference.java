package vsvteam.outsource.leanappandroid.database;

import vsvteam.outsource.leanappandroid.define.GlobalValue;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Lean app android share preference class which saves setting values
 * 
 * @author Lemon
 */
public class LeanAppAndroidSharePreference {

	public static final String APPLICATION_INSTALL_FIRST_TIME = "APPLICATION_INSTALL_FIRST_TIME";

	// ================================================================

	private static LeanAppAndroidSharePreference instance;

	private Context context;

	private LeanAppAndroidSharePreference() {
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @return
	 */
	public static LeanAppAndroidSharePreference getInstance(Context context) {
		if (instance == null) {
			instance = new LeanAppAndroidSharePreference();
			instance.context = context;
		}
		return instance;
	}

	// =========================Setting Preferences =====================

	public static final String CURRENT_PROJECT_NAME_ACTIVE = "CURRENT_PROJECT_ACTIVE";
	public static final String CURRENT_PROJECT_ID_ACTIVE = "CURRENT_PROJECT_ID_ACTIVE";
	public static final String CURRENT_PROJECT_CREATE_OR_SELECT_EXIST = "CURRENT_PROJECT_CHOICE_OR_SELECT_EXIST";

	/**
	 * // ======================== CORE FUNCTIONS ========================
	 * 
	 */

	/**
	 * set vs get current project name active
	 */
	public void setProjectNameActive(String key, String projectName) {
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.LEANAPP_SHARE_PREFERENCE,
				0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, projectName);
		editor.commit();
	}

	public String getProjectNameActive(String key) {
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.LEANAPP_SHARE_PREFERENCE,
				0);
		return pref.getString(key, "");
	}

	public String getProjectNameActive() {
		String projectName;
		projectName = getProjectNameActive(CURRENT_PROJECT_NAME_ACTIVE);
		return projectName;
	}

	public void setProjectNameActive(String projectName) {
		setProjectNameActive(CURRENT_PROJECT_NAME_ACTIVE, projectName);
	}

	/**
	 * set vs get project id active
	 */
	public void setProjectIdActive(String key, int id) {
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.LEANAPP_SHARE_PREFERENCE,
				0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, id);
		editor.commit();
	}

	public int getProjectIdActive(String key) {
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.LEANAPP_SHARE_PREFERENCE,
				0);
		return pref.getInt(key, -1);
	}

	public int getProjectIdActive() {
		int idActive;
		idActive = getProjectIdActive(CURRENT_PROJECT_ID_ACTIVE);
		return idActive;
	}

	public void setProjectIdActive(int id) {
		setProjectIdActive(CURRENT_PROJECT_ID_ACTIVE, id);
	}

	/**
	 * set vs get project is created or seleted exist
	 * 
	 * is created :true is selected exist : false
	 */
	public void setProjectCreatedOrSelectedExist(String key, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.LEANAPP_SHARE_PREFERENCE,
				0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public boolean isProjectCreatedOrSelectedExist(String key) {
		SharedPreferences pref = context.getSharedPreferences(GlobalValue.LEANAPP_SHARE_PREFERENCE,
				0);
		return pref.getBoolean(key, true);
	}

	public void setProjectCreatedOrSelectedExist(boolean b) {
		setProjectCreatedOrSelectedExist(CURRENT_PROJECT_CREATE_OR_SELECT_EXIST, b);
	}

	public boolean isProjectCreatedOrSelectedExist() {
		boolean b;
		b = isProjectCreatedOrSelectedExist(CURRENT_PROJECT_CREATE_OR_SELECT_EXIST);
		return b;
	}
}
