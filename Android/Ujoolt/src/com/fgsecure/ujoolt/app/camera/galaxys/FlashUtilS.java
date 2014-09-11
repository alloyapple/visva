package com.fgsecure.ujoolt.app.camera.galaxys;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

public final class FlashUtilS {
	private static String TAG = "CameraUtil";

	public static boolean isFlashSupport(Context mContext) {

		return mContext.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH);
	}

	public static boolean setFlashlight(Camera mCamera, boolean isOn) {
		Log.e(TAG, "Start change Flash to : " + isOn);

		if (mCamera == null) {
			Log.e(TAG, "Camera null");
			return false;
		}

		Camera.Parameters params = mCamera.getParameters();
		String value;
		if (isOn) // we are being ask to turn it on
		{
			value = Camera.Parameters.FLASH_MODE_TORCH;

			if (android.os.Build.MANUFACTURER.toLowerCase().contains("sam")) {
				Log.e(TAG, "Turn on flash for Samsung device");
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
			}

		} else // we are being asked to turn it off
		{
			value = Camera.Parameters.FLASH_MODE_OFF;
		}

		try {
			params.setFlashMode(value);
			mCamera.setParameters(params);

			String nowMode = mCamera.getParameters().getFlashMode();

			if (isOn && nowMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
				return true;
			}
			if (!isOn && nowMode.equals(Camera.Parameters.FLASH_MODE_AUTO)) {
				return true;
			} 
			return false;
		} catch (Exception ex) {
			Log.e("CameraUtil",

			" Error setting flash mode to: " + value + " " + ex.toString());
			return false;
		}
	}
}