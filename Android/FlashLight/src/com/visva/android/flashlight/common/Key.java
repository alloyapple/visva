package com.visva.android.flashlight.common;

import com.visva.android.flashlightmaster.R;

public interface Key {

	public static final int LED_LIGHT = 0;
	public static final int SCREEN_LIGHT = 1;
	public static final int MORSE_CODE = 2;
	public static final int STROBE_LIGHT = 3;
	public static final int WARNING_LIGHT = 4;
	public static final int LIGHT_BULD = 5;
	public static final int COLOR_LIGHT = 6;
	public static final int POLICE_LIGHT = 7;
	public static final int SETTING = 8;
	public static final int APPS = 9;
	public static final int FEATURES = 10;

	public static final String SHOW_LED_LIGHT_LABEL = "show_led_light_label";
	public static final String SHOW_SCREEN_LIGHT_LABEL = "show_screen_light_label";
	public static final String SHOW_MORSE_LIGHT_LABEL = "show_morse_light_label";
	public static final String SHOW_STROBE_LIGHT_LABEL = "show_strobe_light_label";
	public static final String SHOW_WARNING_LIGHT_LABEL = "show_warning_light_label";
	public static final String SHOW_LIGHT_BULD_LABEL = "show_light_buld_label";
	public static final String SHOW_COLOR_LIGHT_LABEL = "show_color_light_label";
	public static final String SHOW_POLICE_LIGHT_LABEL = "show_police_light_label";
	public static final String SHOW_APPS_LIGHT_LABEL = "show_apps_light_label";
	public static final String SHOW_SETTING_LABEL = "show_setting_label";

	public static final String FIRST_RUN_LIGHT_BULD = "first_run_light_buld";
	public static final String FIRST_RUN_COLOR_LIGHT = "first_run_color_light";
	public static final String LIGHT_BULD_COLOR_SELECTED = "light_buld_color_selected";
	public static final String COLOR_LIGHT_COLOR_SELECTED = "color_light_color_selected";
	public static final String LED_LIGHT_TURN_ON = "led_light_turn_on";
	public static final String SCREEN_LIGHT_TURN_ON = "screen_light_turn_on";
	public static final String SHOW_DEFAULT_LIGHT_SOURCE = "show_default_light_source";

	public static final String[] SHARES = new String[] { "Facebook", "Twitter", "Google+", "E-Mail" };
	public static final String[] LIGHT_SOURCE = new String[] { "LED Light", "Screen Light", "Morse Code",
			"Strobe Light", "Warning Lights", "Light Buld", "Color Light", "Police Lights", "Apps" };

	public static final int[] IMAGE_IDS = new int[] { R.drawable.w_led_icon, R.drawable.w_screen_icon,
			R.drawable.w_morse_icon, R.drawable.w_strobe_icon, R.drawable.w_warning_icon, R.drawable.w_bulb_icon,
			R.drawable.w_color_icon, R.drawable.w_police_icon, R.drawable.w_apps_rate, R.drawable.w_settings_icon };

	public static final String[] SHAKE_LIGHT_TIMEOUT = new String[] { "20 seconds", "1 minute", "5 minutes",
			"10 minutes", "30 minutes", "No limit" };
	public static final int[] LIGHT_TIMEOUT = new int[] { 20000, 60000, 300000, 600000, 1800000, -1 };

	public static final String SCREEN_SELECTED = "screen_selected";
	public static final String PLAY_SOUND = "play_sound";
	public static final String DEFAULT_LIGHT_SOURCE = "default_light_source";
	public static final String VISIBLE_LIGHT_SOURCE = "visible_light_source";
	public static final String ASK_ON_QUIT = "ask_on_quit";
	public static final String LED_DEFAULT_STATE = "led_default_state";
	public static final String LED_SHARK_ON_LOCK_SCREEN = "led_shark_on_lock_screen";
	public static final String LED_SHAKE_SENSITIVITY = "led_shake_sensitivity";
	public static final String LED_SHAKE_LIGHT_TIME_OUT = "led_shark_light_time_out";
	public static final String LED_SHOW_NOTIFICATION = "led_notification";
	public static final String LED_VIBRATION = "led_vibration";
	public static final String SCREEN_LIGHT_DEFAULT_STATE = "screen_light_default_state";
	public static final String SCREEN_LIGHT_VIBRATION = "screen_light_vibration";
	public static final String TEXT_MORSE_CODE = "text_morse_code";
	public static final String TEXT_MORSE_CODE_SPEED = "text_morse_code_speed";
	public static final String STROBE_LIGHT_ON_SPEED = "strobe_light_on_speed";
	public static final String STROBE_LIGHT_OFF_SPEED = "strobe_light_off_speed";
	public static final String WIDGET_ITEM = "widget_item";
	public static final String MORSE_CODE_REPEAT = "morse_code_repeat";
	public static final String FIRST_SHOW_STROBE_LIGHT_WARNING = "first_show_strobe_light_warning";

	// Widget items
	public static final int WIDGET_NONE = 0;
	public static final int WIDGET_1 = 1;
	public static final int WIDGET_2 = 2;
	public static final int WIDGET_3 = 3;
	public static final int WIDGET_4 = 4;
	public static final int WIDGET_5 = 5;
	public static final int WIDGET_6 = 6;
	public static final int WIDGET_TRANSPARENT = 7;
	public static final int WIDGET_8 = 8;
	public static final String WIDGET_IDS = "widget_ids";
	public static final String WIDGET_KEYS = "widget_keys";
	public static final String LED = "led";
	public static final String TURN_LED = "turn_led";
	public static final int POSITION_LIGHT_TIMER_UNCHECK = 5;
	public static final long INTERVAL = 1000;
	public static final String BRIGHTNESS_MODE = "brightness_mode";
	public static final String BRIGHTNESS_MODE_LED = "brightness_mode_led";
}
