package vsvteam.outsource.android.soundeffect.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Share preference class which saves setting values
 * 
 * @author vsvteam
 */
public class SoundEffectSharePreference {
	private static SoundEffectSharePreference instance;

	private Context context;

	private SoundEffectSharePreference() {
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @return
	 */
	public static SoundEffectSharePreference getInstance(Context context) {
		if (instance == null) {
			instance = new SoundEffectSharePreference();
			instance.context = context;
		}
		return instance;
	}

	// =========================Setting Preferences =====================
	public static final String SOUND_EFFECT_PREFERENCES = "SOUND_EFFECT_PREFERENCES";
	public static final String LIVE_CORRECTION_PREFERENCNE = "LIVE_CORRECTION_PREFERENCNE";
	public static final String SAMPLE_RATE_PREFERENCE = "SAMPLE_RATE_PREFERENCE";
	public static final String BUFFER_SIZE_PREFERENCE = "BUFFER_SIZE_PREFERENCE";
	public static final String HEAD_SET_CONECTION_PREFERENCE = "HEAD_SET_CONECTION_PREFERENCE";

	// ==================PREF methods for get link in splash======
	/**
	 * set and get live - correction value
	 * 
	 * @param b
	 */
	public void putLiveCorrectionValue(boolean b) {
		setLiveCorrectionValue(LIVE_CORRECTION_PREFERENCNE, b);
	}

	public void setLiveCorrectionValue(String key, boolean s) {
		// SmartLog.log(TAG, "Set string value");
		SharedPreferences pref = context.getSharedPreferences(SOUND_EFFECT_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, s);
		editor.commit();
	}

	public boolean getLiveCorrectionValue() {
		boolean b;
		b = getLiveCorrectionValue(LIVE_CORRECTION_PREFERENCNE);
		return b;
	}

	private boolean getLiveCorrectionValue(String key) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(SOUND_EFFECT_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	/**
	 * set and get head_set_connection value
	 * 
	 * @param b
	 */
	public void putHeadSetConnectionValue(boolean b) {
		setLiveCorrectionValue(HEAD_SET_CONECTION_PREFERENCE, b);
	}

	public void setHeadSetConnectionValue(String key, boolean s) {
		// SmartLog.log(TAG, "Set string value");
		SharedPreferences pref = context.getSharedPreferences(SOUND_EFFECT_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, s);
		editor.commit();
	}

	public boolean getHeadSetConnectionValue() {
		boolean b;
		b = getHeadSetConnectionValue(HEAD_SET_CONECTION_PREFERENCE);
		return b;
	}

	private boolean getHeadSetConnectionValue(String key) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(SOUND_EFFECT_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	/**
	 * set and get buffer size value
	 * 
	 * @param b
	 */
	public void putBufferSizeValue(int b) {
		setBufferSizeValue(BUFFER_SIZE_PREFERENCE, b);
	}

	public void setBufferSizeValue(String key, int s) {
		// SmartLog.log(TAG, "Set string value");
		SharedPreferences pref = context.getSharedPreferences(SOUND_EFFECT_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, s);
		editor.commit();
	}

	public int getBufferSizeValue() {
		int b;
		b = getBufferSizeValue(BUFFER_SIZE_PREFERENCE);
		return b;
	}

	private int getBufferSizeValue(String key) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(SOUND_EFFECT_PREFERENCES, 0);
		return pref.getInt(key, 1);
	}

	/**
	 * set and get sample rate value
	 * 
	 * @param b
	 */
	public void putSampleRateValue(int b) {
		setBufferSizeValue(SAMPLE_RATE_PREFERENCE, b);
	}

	public void setSampleRateValue(String key, int s) {
		// SmartLog.log(TAG, "Set string value");
		SharedPreferences pref = context.getSharedPreferences(SOUND_EFFECT_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, s);
		editor.commit();
	}

	public int getSampleRateValue() {
		int b;
		b = getSampleRateValue(SAMPLE_RATE_PREFERENCE);
		return b;
	}

	private int getSampleRateValue(String key) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(SOUND_EFFECT_PREFERENCES, 0);
		return pref.getInt(key, 11025);
	}
}
