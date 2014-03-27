/**
 * HardwareUtilities
 * 
 * Author: Dang Dinh Quan - SetaCinq Vietnam
 * Created: 03/02/2012
 * Description: Hardware Utilities implemented some useful functions to control hardware like: vibrate, light sensors
 * 
 * */

package com.visva.android.flashlight.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

public class HardwareUtilities {

	/**
	 * Vibrate device with duration
	 * */
	public static void vibrate(Activity context, long duration) {
		Vibrator __vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		__vibrator.vibrate(duration);
	}

	public static void vibrate(Context context, long duration) {
		Vibrator __vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		__vibrator.vibrate(duration);
	}

	/**
	 * Vibrate device with duration pattern and repeat
	 * */
	public static void vibrate(Activity context, long[] pattern, int repeat) {
		Vibrator __vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		__vibrator.vibrate(pattern, repeat);
	}
}
