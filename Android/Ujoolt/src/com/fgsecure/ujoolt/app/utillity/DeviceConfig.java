package com.fgsecure.ujoolt.app.utillity;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceConfig {
	public static String device_id;
	public static String device_name;

	public static final String HTC = "htc";
	public static final String SAMSUNG = "sam";
	public static final String LG = "lg ";
	public static final String SONY = "son";
	public static final String MOTOROLA = "mot";

	public static void getDeviceInfo(Activity activity) {
		if (device_id == null) {
			TelephonyManager tManager = (TelephonyManager) activity
					.getSystemService(Context.TELEPHONY_SERVICE);
			device_id = tManager.getDeviceId();
		}
		device_name = android.os.Build.MANUFACTURER;
		device_name = device_name.substring(0, 3);
		device_name = device_name.toLowerCase();
	}
	public static String getDeviceId(Context context){
		if (device_id == null) {
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			device_id = tManager.getDeviceId();
		}
		return device_id;
	}
}
