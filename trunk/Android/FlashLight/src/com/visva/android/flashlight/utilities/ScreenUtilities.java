/**
 * ScreenUtilities
 * 
 * Author: Dang Dinh Quan - SetaCinq Vietnam
 * Created: 03/02/2012
 * Description: Screen Utilities implemented some useful functions to control device's Screen like brightness
 * 
 * */

package com.visva.android.flashlight.utilities;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class ScreenUtilities {
	
	/**
	 * Get the current Brightness value.
	 * */
	public static float getCurrentBrightness(Activity context){
		Window __window = context.getWindow();
		WindowManager.LayoutParams __layoutParams = __window.getAttributes();
		return __layoutParams.screenBrightness;
	}
	
	/**
	 * Set Brightness to value.
	 * */
	public static void setBrightness(Activity context, float brightness) {
		Window __window = context.getWindow();
		WindowManager.LayoutParams __layoutParams = __window.getAttributes();
		__layoutParams.screenBrightness = brightness;
		__window.setAttributes(__layoutParams);
	}

	/**
	 * Get the current Screen size.
	 * */
	public static Point getScreenSize(Activity context) {
		Display __displayScreen = context.getWindowManager().getDefaultDisplay();
		Point __screenPoint = new Point();
		__screenPoint.x = __displayScreen.getWidth();
		__screenPoint.y = __displayScreen.getHeight();				
		return __screenPoint;
	}
}
