package vsvteam.outsource.leanappandroid.database;

import vsvteam.outsource.leanappandroid.define.GlobalValue;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Lean app android share preference class which saves setting values
 * 
 * @author visva team
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
	public static final String CURRENT_PROCESS_NAME_ACTIVE = "CURRENT_PROCESS_NAME_ACTIVE";
	public static final String CURRENT_PROCESS_ID_ACTIVE = "CURRENT_PROCESS_ID_ACTIVE";
	public static final String CURRENT_STEP_ID_ACTIVE = "CURRENT_STEP_ID_ACTIVE";
	public static final String CURRENT_STEP_NAME_ACTIVE = "CURRENT_STEP_NAME_ACTIVE";
	public static final String CURRENT_TAKT_TIME_DONE_OR_CANCEL = "CURRENT_TAKT_TIME_DONE_OR_CANCEL";
	public static final String TYPE_EXPORT = "TYPE_EXPORT";
	public static final String FILE_NAME_TEMP="FILE_NAME_TEMP";

	/**
	 * // ======================== CORE FUNCTIONS ========================
	 * 
	 */

	/**
	 * set vs get current project name active
	 */
	public void setProjectNameActive(String key, String projectName) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, projectName);
		editor.commit();
	}

	public String getProjectNameActive(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, id);
		editor.commit();
	}

	public int getProjectIdActive(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	public boolean isProjectCreatedOrSelectedExist(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		return pref.getBoolean(key, true);
	}

	public void setProjectCreatedOrSelectedExist(boolean b) {
		setProjectCreatedOrSelectedExist(
				CURRENT_PROJECT_CREATE_OR_SELECT_EXIST, b);
	}

	public boolean isProjectCreatedOrSelectedExist() {
		boolean b;
		b = isProjectCreatedOrSelectedExist(CURRENT_PROJECT_CREATE_OR_SELECT_EXIST);
		return b;
	}

	/**
	 * set vs get process is actived
	 */
	public void setProcessIdActive(String key, int id) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, id);
		editor.commit();
	}

	public int getProcessIdActive(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		return pref.getInt(key, -1);
	}

	public int getProcessIdActive() {
		int idActive;
		idActive = getProjectIdActive(CURRENT_PROCESS_ID_ACTIVE);
		return idActive;
	}

	public void setProcessIdActive(int id) {
		setProjectIdActive(CURRENT_PROCESS_ID_ACTIVE, id);
	}

	/**
	 * set vs get process name is actived
	 */
	public void setProcessNameActive(String key, String projectName) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, projectName);
		editor.commit();
	}

	public String getProcessNameActive(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		return pref.getString(key, "");
	}

	public String getProcessNameActive() {
		String projectName;
		projectName = getProjectNameActive(CURRENT_PROCESS_NAME_ACTIVE);
		return projectName;
	}

	public void setProcessNameActive(String projectName) {
		setProjectNameActive(CURRENT_PROCESS_NAME_ACTIVE, projectName);
	}

	/**
	 * set vs get step name is actived
	 */
	public void setStepNameActive(String key, String projectName) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, projectName);
		editor.commit();
	}

	public String getStepNameActive(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		return pref.getString(key, "");
	}

	public String getStepNameActive() {
		String projectName;
		projectName = getProjectNameActive(CURRENT_STEP_NAME_ACTIVE);
		return projectName;
	}

	public void setStepNameActive(String projectName) {
		setProjectNameActive(CURRENT_STEP_NAME_ACTIVE, projectName);
	}

	/**
	 * set vs get step id is actived
	 */
	public void setStepIdActive(String key, int id) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, id);
		editor.commit();
	}

	public int getStepIdActive(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		return pref.getInt(key, -1);
	}

	public int getStepIdActive() {
		int idActive;
		idActive = getProjectIdActive(CURRENT_STEP_ID_ACTIVE);
		return idActive;
	}

	public void setStepIdActive(int id) {
		setProjectIdActive(CURRENT_STEP_ID_ACTIVE, id);
	}

	/**
	 * set vs get mode for takt time control ; MODE 1 : cancel takt time; MODE 2
	 * :done takt time ok and go to stream map; MODE 3 : done takt time but no
	 * process or project created.
	 * 
	 */
	public void setModeTaktTime(String key, int id) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, id);
		editor.commit();
	}

	public void setModeExport(String key, int typeExport) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, typeExport);
		editor.commit();
	}

	public int getModeExport(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		return pref.getInt(key, -1);
	}

	public int getModeTaktTime(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		return pref.getInt(key, 1);
	}

	public int getModeTaktTimee() {
		int idActive;
		idActive = getProjectIdActive(CURRENT_TAKT_TIME_DONE_OR_CANCEL);
		return idActive;
	}

	public void setModeTaktTime(int id) {
		setProjectIdActive(CURRENT_TAKT_TIME_DONE_OR_CANCEL, id);
	}
	
	/**
	 * set vs get current file name temp
	 */
	public void setFileName(String key, String projectName) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, projectName);
		editor.commit();
	}

	public String getFileName(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.LEANAPP_SHARE_PREFERENCE, 0);
		return pref.getString(key, "");
	}

	public String getFileName() {
		String projectName;
		projectName = getFileName(FILE_NAME_TEMP);
		return projectName;
	}

	public void setFileName(String projectName) {
		setFileName(FILE_NAME_TEMP, projectName);
	}
}
