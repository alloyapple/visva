package com.visva.android.flashlight.common;

import java.util.ArrayList;

import com.visva.android.flashlight.R;

public class WidgetItemKey implements Key{
	public static ArrayList<ScreenLight> lstWidgetItem = new ArrayList<ScreenLight>();
	
	static{
		lstWidgetItem.add(new ScreenLight("Widget 1", WIDGET_1, R.drawable.widget_bulb_off));
		lstWidgetItem.add(new ScreenLight("Widget 2", WIDGET_2, R.drawable.widget_bulb_purple_off));
		lstWidgetItem.add(new ScreenLight("Widget 3", WIDGET_3, R.drawable.widget_bulb_green_off));
		lstWidgetItem.add(new ScreenLight("Widget 4", WIDGET_4, R.drawable.widget_led_off));
		lstWidgetItem.add(new ScreenLight("Widget 5", WIDGET_5, R.drawable.widget_led_purple_off));
		lstWidgetItem.add(new ScreenLight("Widget 6", WIDGET_6, R.drawable.widget_led_green_off));
		lstWidgetItem.add(new ScreenLight("Widget 7", WIDGET_TRANSPARENT, R.drawable.widget_transparent));
		lstWidgetItem.add(new ScreenLight("Widget 8", WIDGET_8, R.drawable.widget_flashlight_off));
	}
}
