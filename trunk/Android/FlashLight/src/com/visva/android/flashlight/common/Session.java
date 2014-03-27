package com.visva.android.flashlight.common;

public class Session {
	private static boolean _vibrateOnActivateLED = false;
	private static float _highestScreenBrightness = 1.0f;
	private static float _lowestScreenBrightness = 0.3f;
	private static int _warningLightDelay = 500;
	
	public static boolean isVibrateOnActivateLED() {
		return Session._vibrateOnActivateLED;
	}
	
	public static void setVibrateOnActiveLED(boolean vibrate) {
		Session._vibrateOnActivateLED = vibrate;
	}
	
	public static float getHighestScreenBrightness(){
		return Session._highestScreenBrightness;
	}
	
	public static void setHighestScreenBrightness(float brightnessValue){
		Session._highestScreenBrightness = brightnessValue;
	}
	
	public static float getLowestScreenBrightness(){
		return Session._lowestScreenBrightness;
	}
	
	public static void setLowScreenBrightness(float brightnessValue){
		Session._lowestScreenBrightness = brightnessValue;
	}

	public static int getWarningLightDelay(){
		return Session._warningLightDelay;
	}
	
	public static void setWarningLightDelay(int delay){
		Session._warningLightDelay = delay;
	}
}
