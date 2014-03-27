package com.visva.android.flashlight.utilities;

import android.app.Activity;
import android.content.Intent;

import com.visva.android.flashlight.activities.AppsActivity;
import com.visva.android.flashlight.activities.ColorLightActivity;
import com.visva.android.flashlight.activities.LEDLightActivity;
import com.visva.android.flashlight.activities.LightBuldActivity;
import com.visva.android.flashlight.activities.MorseCodeActivity;
import com.visva.android.flashlight.activities.PoliceLightActivity;
import com.visva.android.flashlight.activities.ScreenLightActivity;
import com.visva.android.flashlight.activities.SettingActivity;
import com.visva.android.flashlight.activities.StrobeLightActivity;
import com.visva.android.flashlight.activities.WarningLightActivity;
import com.visva.android.flashlight.common.Key;

public class SwitchUtilities implements Key {

	public static void switchTo(Activity activity, int screenSelected) {

		Intent __intent = new Intent();
		switch (screenSelected) {
		case COLOR_LIGHT:
			__intent.setClass(activity, ColorLightActivity.class);
			break;
		case LED_LIGHT:
			__intent.setClass(activity, LEDLightActivity.class);
			break;
		case LIGHT_BULD:
			__intent.setClass(activity, LightBuldActivity.class);
			break;
		case MORSE_CODE:
			__intent.setClass(activity, MorseCodeActivity.class);
			break;
		case POLICE_LIGHT:
			__intent.setClass(activity, PoliceLightActivity.class);
			break;
		case SCREEN_LIGHT:
			__intent.setClass(activity, ScreenLightActivity.class);
			break;
		case SETTING:
			__intent.setClass(activity, SettingActivity.class);
			break;
		case STROBE_LIGHT:
			__intent.setClass(activity, StrobeLightActivity.class);
			break;
		case WARNING_LIGHT:
			__intent.setClass(activity, WarningLightActivity.class);
			break;
		case APPS:
			__intent.setClass(activity, AppsActivity.class);
			break;
		}

		activity.startActivity(__intent);
		activity.finish();
	}
}
