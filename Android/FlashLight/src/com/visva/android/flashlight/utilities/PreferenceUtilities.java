package com.visva.android.flashlight.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.visva.android.flashlight.common.Key;

public class PreferenceUtilities implements Key {

	private SharedPreferences _preferences = null;
	private Activity _activity;

	public PreferenceUtilities(Activity activity) {
		this._activity = activity;
		this._preferences = PreferenceManager.getDefaultSharedPreferences(activity);
	}

	public PreferenceUtilities(Context context) {
		this._preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setScreenSelected(int selected) {
		_preferences.edit().putInt(SCREEN_SELECTED, selected).commit();
	}

	public int getScreenSelected() {
		return _preferences.getInt(SCREEN_SELECTED, SCREEN_LIGHT);
	}

	public void setFirstRunLightBuld(boolean isFirst) {
		_preferences.edit().putBoolean(FIRST_RUN_LIGHT_BULD, isFirst).commit();
	}

	public boolean isFirstRunLightBuld() {
		return _preferences.getBoolean(FIRST_RUN_LIGHT_BULD, false);
	}

	public void setFirstRunColorLight(boolean isFirst) {
		_preferences.edit().putBoolean(FIRST_RUN_COLOR_LIGHT, isFirst).commit();
	}

	public boolean isFirstRunColorLight() {
		return _preferences.getBoolean(FIRST_RUN_COLOR_LIGHT, false);
	}

	public void setPlaySound(boolean isPlaySound) {
		_preferences.edit().putBoolean(PLAY_SOUND, isPlaySound).commit();
	}

	public boolean isPlaySound() {
		return _preferences.getBoolean(PLAY_SOUND, false);
	}

	public void setDefaultLightSource(int defaultLightSource) {
		_preferences.edit().putInt(DEFAULT_LIGHT_SOURCE, defaultLightSource).commit();
	}

	public int getDefaultLightSource() {
		if (CameraUtilities.isFlashLightAvailable(_activity) || LEDUtilities.isSupported()) {
			return _preferences.getInt(DEFAULT_LIGHT_SOURCE, LED_LIGHT);
		} else {
			return _preferences.getInt(DEFAULT_LIGHT_SOURCE, SCREEN_LIGHT);
		}
	}

	public void setVisibleLightSources(String visibleLightSources) {
		_preferences.edit().putString(VISIBLE_LIGHT_SOURCE, visibleLightSources).commit();
	}

	public String getVisibleLightSources() {
		String def = LED_LIGHT + "," + SCREEN_LIGHT + "," + MORSE_CODE + "," + STROBE_LIGHT + "," + WARNING_LIGHT + ","
				+ LIGHT_BULD + "," + COLOR_LIGHT + "," + POLICE_LIGHT + "," + SETTING + "," + APPS;
		String def2 = SCREEN_LIGHT + "," + WARNING_LIGHT + "," + LIGHT_BULD + "," + COLOR_LIGHT + "," + POLICE_LIGHT
				+ "," + SETTING + "," + APPS;
		if (CameraUtilities.isFlashLightAvailable(_activity) || LEDUtilities.isSupported()) {
			return _preferences.getString(VISIBLE_LIGHT_SOURCE, def);
		} else {
			return _preferences.getString(VISIBLE_LIGHT_SOURCE, def2);
		}
	}

	public void setAskOnQuit(boolean askOnQuit) {
		_preferences.edit().putBoolean(ASK_ON_QUIT, askOnQuit).commit();
	}

	public boolean isAskonQuit() {
		return _preferences.getBoolean(ASK_ON_QUIT, false);
	}

	public void setLedDefaultState(boolean defaultState) {
		_preferences.edit().putBoolean(LED_DEFAULT_STATE, defaultState).commit();
	}

	public boolean isLedDefaultState() {
		return _preferences.getBoolean(LED_DEFAULT_STATE, false);
	}

	public void setLedSharkOnLockScreen(boolean sharkOnLockScreen) {
		_preferences.edit().putBoolean(LED_SHARK_ON_LOCK_SCREEN, sharkOnLockScreen).commit();
	}

	public boolean isLedSharkOnLockScreen() {
		return _preferences.getBoolean(LED_SHARK_ON_LOCK_SCREEN, false);
	}

	public void setLedShakeLightTimeOut(int positionLightTimeOut) {
		_preferences.edit().putInt(LED_SHAKE_LIGHT_TIME_OUT, positionLightTimeOut).commit();
	}

	public int getLedShakeLightTimeOut() {
		return _preferences.getInt(LED_SHAKE_LIGHT_TIME_OUT, 0);
	}

	public void setLedShowNotification(boolean showNotification) {
		_preferences.edit().putBoolean(LED_SHOW_NOTIFICATION, showNotification).commit();
	}

	public boolean isLedShowNotification() {
		return _preferences.getBoolean(LED_SHOW_NOTIFICATION, false);
	}

	public void setLedVibration(boolean ledVibration) {
		_preferences.edit().putBoolean(LED_VIBRATION, ledVibration).commit();
	}

	public boolean isLedVibration() {
		return _preferences.getBoolean(LED_VIBRATION, false);
	}

	public void setLedShakeSensitivity(int sensitivity) {
		if (sensitivity == 0) {
			sensitivity = 1;
		}
		_preferences.edit().putInt(LED_SHAKE_SENSITIVITY, sensitivity).commit();
	}

	public int getLedShakeSensitivity() {
		return _preferences.getInt(LED_SHAKE_SENSITIVITY, 5);
	}

	public void setScreenLightDefaultState(boolean defaultState) {
		_preferences.edit().putBoolean(SCREEN_LIGHT_DEFAULT_STATE, defaultState).commit();
	}

	public boolean isScreenLightDefaultState() {
		return _preferences.getBoolean(SCREEN_LIGHT_DEFAULT_STATE, false);
	}

	public void setScreenLightVibration(boolean vibration) {
		_preferences.edit().putBoolean(SCREEN_LIGHT_VIBRATION, vibration).commit();
	}

	public boolean isScreenLightVibration() {
		return _preferences.getBoolean(SCREEN_LIGHT_VIBRATION, false);
	}

	public void setLightBuldColor(int color) {
		_preferences.edit().putInt(LIGHT_BULD_COLOR_SELECTED, color).commit();
	}

	public int getLightBuldColor() {
		return _preferences.getInt(LIGHT_BULD_COLOR_SELECTED, ColorUtilities.color[0]);
	}

	public void setColorLightColor(int color) {
		_preferences.edit().putInt(COLOR_LIGHT_COLOR_SELECTED, color).commit();
	}

	public int getColorLightColor() {
		return _preferences.getInt(COLOR_LIGHT_COLOR_SELECTED, ColorUtilities.color[0]);
	}

	public void setTextMorseCode(String __text) {
		_preferences.edit().putString(TEXT_MORSE_CODE, __text).commit();
	}

	public String getTextMorseCode() {
		return _preferences.getString(TEXT_MORSE_CODE, "SOS");
	}

	public void setLedLightTurnOn(boolean isTurnOn) {
		_preferences.edit().putBoolean(LED_LIGHT_TURN_ON, isTurnOn).commit();
	}

	public boolean isLedLightTurnOn() {
		return _preferences.getBoolean(LED_LIGHT_TURN_ON, false);
	}

	public void setTextMorseCodeSpeed(int speed) {
		_preferences.edit().putInt(TEXT_MORSE_CODE_SPEED, speed).commit();
	}

	public int getTextMorseCodeSpeed() {
		return _preferences.getInt(TEXT_MORSE_CODE_SPEED, 50);
	}

	public void setStrobeLightOnSpeed(int speed) {
		_preferences.edit().putInt(STROBE_LIGHT_ON_SPEED, speed).commit();
	}

	public int getStrobeLightOnSpeed() {
		return _preferences.getInt(STROBE_LIGHT_ON_SPEED, 0);
	}

	public void setStrobeLightOffSpeed(int speed) {
		_preferences.edit().putInt(STROBE_LIGHT_OFF_SPEED, speed).commit();
	}

	public int getStrobeLightOffSpeed() {
		return _preferences.getInt(STROBE_LIGHT_OFF_SPEED, 0);
	}

	public void setShowColorLightLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_COLOR_LIGHT_LABEL, isShow).commit();
	}

	public boolean isShowColorLightLabel() {
		return _preferences.getBoolean(SHOW_COLOR_LIGHT_LABEL, false);
	}

	public void setShowLedLightLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_LED_LIGHT_LABEL, isShow).commit();
	}

	public boolean isShowLedLightLabel() {
		return _preferences.getBoolean(SHOW_LED_LIGHT_LABEL, false);
	}

	public void setShowMorseLightLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_MORSE_LIGHT_LABEL, isShow).commit();
	}

	public boolean isShowMorseLightLabel() {
		return _preferences.getBoolean(SHOW_MORSE_LIGHT_LABEL, false);
	}

	public void setShowStrobeLightLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_STROBE_LIGHT_LABEL, isShow).commit();
	}

	public boolean isShowStrobeLightLabel() {
		return _preferences.getBoolean(SHOW_STROBE_LIGHT_LABEL, false);
	}

	public void setShowWarningLightLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_WARNING_LIGHT_LABEL, isShow).commit();
	}

	public boolean isShowWarningLightLabel() {
		return _preferences.getBoolean(SHOW_WARNING_LIGHT_LABEL, false);
	}

	public void setShowLightBuldLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_LIGHT_BULD_LABEL, isShow).commit();
	}

	public boolean isShowLightBuldLabel() {
		return _preferences.getBoolean(SHOW_LIGHT_BULD_LABEL, false);
	}

	public void setShowPoliceLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_POLICE_LIGHT_LABEL, isShow).commit();
	}

	public boolean isShowPoliceLightLabel() {
		return _preferences.getBoolean(SHOW_POLICE_LIGHT_LABEL, false);
	}

	public void setShowAppsLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_APPS_LIGHT_LABEL, isShow).commit();
	}

	public boolean isShowAppsLabel() {
		return _preferences.getBoolean(SHOW_APPS_LIGHT_LABEL, false);
	}

	public void setShowScreenLightLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_SCREEN_LIGHT_LABEL, isShow).commit();
	}

	public boolean isShowScreenLightLabel() {
		return _preferences.getBoolean(SHOW_SCREEN_LIGHT_LABEL, false);
	}

	public void setShowSettingLabel(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_SETTING_LABEL, isShow).commit();
	}

	public boolean isShowSettingLabel() {
		return _preferences.getBoolean(SHOW_SETTING_LABEL, false);
	}

	public void setShowDefaultLightSource(boolean isShow) {
		_preferences.edit().putBoolean(SHOW_DEFAULT_LIGHT_SOURCE, isShow).commit();
	}

	public boolean isShowDefaultLightSource() {
		return _preferences.getBoolean(SHOW_DEFAULT_LIGHT_SOURCE, false);
	}

	public void setWidgetItem(int selected) {
		_preferences.edit().putInt(WIDGET_ITEM, selected).commit();
	}

	public int getWidgetItem() {
		return _preferences.getInt(WIDGET_ITEM, WIDGET_NONE);
	}

	public void setWidgetIds(int[] widgetIds) {
		String strWidgetIds = "";
		if (widgetIds != null) {
			int length = widgetIds.length;
			for (int i = 0; i < length; i++) {
				if (i < length - 1) {
					strWidgetIds += widgetIds[i] + ",";
				} else {
					strWidgetIds += widgetIds[i];
				}
			}
		}
		Log.d("SET SAVE_WIDGET", "xxx = " + strWidgetIds);
		_preferences.edit().putString(WIDGET_IDS, strWidgetIds).commit();
	}

	public int[] getWidgetIds() {
		int[] widgetIds = null;
		String strWidgetIds = _preferences.getString(WIDGET_IDS, "");
		Log.d("GET SAVE WIDGET", "xxx = " + strWidgetIds);
		if (!strWidgetIds.equals("")) {
			String[] arrStrWidgetIds = strWidgetIds.split("[,]");
			widgetIds = new int[arrStrWidgetIds.length];
			for (int i = 0; i < arrStrWidgetIds.length; i++) {
				try {
					widgetIds[i] = Integer.parseInt(arrStrWidgetIds[i]);
				} catch (Exception e) {
				}
			}
			return widgetIds;
		}
		return null;
	}

	public void addWidgetKeys(int widgetKey) {
		String strWidgetKeys = _preferences.getString(WIDGET_KEYS, "");
		strWidgetKeys += widgetKey + ",";
		Log.d("ADD", "xxx = " + strWidgetKeys);
		_preferences.edit().putString(WIDGET_KEYS, strWidgetKeys).commit();
	}

	public void setWidgetKeys(int[] widgetKeys) {
		String strWidgetKeys = "";
		if (widgetKeys != null) {
			int length = widgetKeys.length;
			for (int i = 0; i < length; i++) {
				if (i < length - 1) {
					strWidgetKeys += widgetKeys[i] + ",";
				} else {
					strWidgetKeys += widgetKeys[i];
				}
			}
		}
		Log.d("SET SAVE_WIDGET", "xxx = " + strWidgetKeys);
		_preferences.edit().putString(WIDGET_KEYS, strWidgetKeys).commit();
	}

	public int[] getWidgetKeys() {
		int[] widgetKeys = null;
		String strWidgetKeys = _preferences.getString(WIDGET_KEYS, "");
		Log.d("GET SAVE WIDGET", "xxx = " + strWidgetKeys);
		if (!strWidgetKeys.equals("")) {
			String[] arrStrWidgetIds = strWidgetKeys.split("[,]");
			widgetKeys = new int[arrStrWidgetIds.length];
			for (int i = 0; i < arrStrWidgetIds.length; i++) {
				try {
					widgetKeys[i] = Integer.parseInt(arrStrWidgetIds[i]);
				} catch (Exception e) {
				}
			}
			return widgetKeys;
		}
		return null;
	}

	public void setCheckTurnLed(boolean isCheck) {
		_preferences.edit().putBoolean(TURN_LED, isCheck).commit();
	}

	public boolean isCheckTurnLed() {
		return _preferences.getBoolean(TURN_LED, false);
	}

	public void setBrightnessMode(int brightness) {
		_preferences.edit().putInt(BRIGHTNESS_MODE, brightness).commit();
	}

	public int getBrightnessMode() {
		return _preferences.getInt(BRIGHTNESS_MODE, 0);
	}

	public void setBrightnessModeLed(int brightness) {
		_preferences.edit().putInt(BRIGHTNESS_MODE_LED, brightness).commit();
	}

	public int getBrightnessModeLed() {
		return _preferences.getInt(BRIGHTNESS_MODE_LED, 0);
	}

	public void setMorseCodeRepeat(boolean repeat) {
		_preferences.edit().putBoolean(MORSE_CODE_REPEAT, repeat).commit();
	}

	public boolean isMorseCodeRepeat() {
		return _preferences.getBoolean(MORSE_CODE_REPEAT, false);
	}

	public void setScreenLightTurnOn(boolean turnOn) {
		_preferences.edit().putBoolean(SCREEN_LIGHT_TURN_ON, turnOn).commit();
	}

	public boolean isScreenLightTurnOn() {
		return _preferences.getBoolean(SCREEN_LIGHT_TURN_ON, false);
	}

	public void setFirstStrobeLightWarning(boolean show) {
		_preferences.edit().putBoolean(FIRST_SHOW_STROBE_LIGHT_WARNING, show).commit();
	}

	public boolean isFirstStrobeLightWarning() {
		return _preferences.getBoolean(FIRST_SHOW_STROBE_LIGHT_WARNING, false);
	}
}
