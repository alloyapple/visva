package com.visva.android.flashlight.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;

public class LEDUtilities {
	public static int LOW_BRIGHTNESS = 125;// 25
	public static int MEDIUM_BRIGHTNESS = 50;// 75
	public static int HIGH_BRIGHTNESS = 128;// 125
	private static final String PATH_HTC = "/sys/devices/platform/flashlight.0/leds/flashlight/brightness";
	private static final String PATH_SONY_ERICSSON_X10 = "/sys/class/leds/lv5219lg:fled/brightness";
	private static String PATH = "";

	public static synchronized void turnOn() {
		setBrightness(HIGH_BRIGHTNESS);
	}

	public static synchronized void turnOff() {
		setBrightness(0);
	}

	public static boolean isSupported() {
		boolean ret = false;
		PATH = PATH_HTC;
		File brightness = new File(PATH);
		ret = brightness.exists() && brightness.canWrite() && !android.os.Build.DEVICE.equals("passion");
		LOW_BRIGHTNESS = 125;// 25
		MEDIUM_BRIGHTNESS = 50;// 75
		HIGH_BRIGHTNESS = 128;// 125
		if (!ret) {
			LOW_BRIGHTNESS = 7;
			MEDIUM_BRIGHTNESS = 14;
			HIGH_BRIGHTNESS = 20;
			PATH = PATH_SONY_ERICSSON_X10;
			brightness = new File(PATH);
			ret = brightness.exists() && brightness.canWrite() && !android.os.Build.DEVICE.equals("passion");
		}

		return ret;
	}

	public static synchronized void setBrightness(int brightness) {
		if (!isSupported()) {
			return;
		}

		write(PATH, brightness);
	}

	private static boolean write(String filepath, int brightness) {

		if (brightness < 0 || brightness > 128) return false;

		String content = Integer.toString(brightness) + "\n";
		File f = new File(filepath);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(content);
			out.close();
		} catch (IOException ex) {
			Log.w("Flash light", "write ", ex);
			return false;
		}
		return true;
	}
}
