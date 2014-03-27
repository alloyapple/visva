package com.visva.android.flashlight.common;

import java.util.ArrayList;

import com.visva.android.flashlight.R;

public class ScreenLightKey implements Key {
	public static ArrayList<ScreenLight> lstScreenLight = new ArrayList<ScreenLight>();

	static {
		lstScreenLight.add(new ScreenLight("LED Light", LED_LIGHT, R.drawable.w_led_icon));
		lstScreenLight.add(new ScreenLight("Screen Light", SCREEN_LIGHT, R.drawable.w_screen_icon));
		lstScreenLight.add(new ScreenLight("Morse Code", MORSE_CODE, R.drawable.w_morse_icon));
		lstScreenLight.add(new ScreenLight("Strobe Light", STROBE_LIGHT, R.drawable.w_strobe_icon));
		lstScreenLight.add(new ScreenLight("Warning Lights", WARNING_LIGHT, R.drawable.w_warning_icon));
		lstScreenLight.add(new ScreenLight("Light Bulb", LIGHT_BULD, R.drawable.w_bulb_icon));
		lstScreenLight.add(new ScreenLight("Color Light", COLOR_LIGHT, R.drawable.w_color_icon));
		lstScreenLight.add(new ScreenLight("Police Lights", POLICE_LIGHT, R.drawable.w_police_icon));
		lstScreenLight.add(new ScreenLight("Settings", SETTING, R.drawable.w_settings_icon));

	}

}
