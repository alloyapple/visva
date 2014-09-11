/**
 * CameraUtilities
 * 
 * Author: Dang Dinh Quan - SetaCinq Vietnam
 * Created: 03/02/2012
 * Description: Camera Utilities implemented some useful functions to control camera include flash light (if available). Easy to use
 * 
 * */

package com.fgsecure.ujoolt.app.camera.other;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class CameraUtilities {
	/**
	 * Private Static Objects Declaration
	 * */
	private static boolean _cameraConnected = false;
	private static Camera _camera = null;;

	/**
	 * Connect the current application to Camera Services.
	 * */
	public static void connectCamera() {
		if (CameraUtilities._cameraConnected) return;

		CameraUtilities._camera = Camera.open();
		CameraUtilities._cameraConnected = true;
	}

	/**
	 * Disconnect the current application to Camera Services, it's help another applications are able to use Camera.
	 * */
	public static void disconnectCamera() {
		if (!CameraUtilities._cameraConnected) return;

		if (CameraUtilities._camera == null) return;

		CameraUtilities._camera.release();
		CameraUtilities._cameraConnected = false;
	}

	/**
	 * Turning on Flash Light with mode. If the application not yet connect to Camera, automatically call
	 * connectCamera() flashMode: Parameters.FLASH_MODE_AUTO | Parameters.FLASH_MODE_OFF | Parameters.FLASH_MODE_ON |
	 * Parameters.FLASH_MODE_RED_EYE | Parameters.FLASH_MODE_TORCH
	 * */
	public static void setFlashLightMode(String flashMode) {
		// Detect the status of application connection to camera. If not yet connected to Camera, automatically connect.
		if (!CameraUtilities._cameraConnected || CameraUtilities._camera == null) CameraUtilities.connectCamera();

		Parameters __cameraParams = CameraUtilities._camera.getParameters();
		__cameraParams.setFlashMode(flashMode);
		CameraUtilities._camera.setParameters(__cameraParams);
	}

	/**
	 * Turning on Flash Light with TORCH mode.
	 * */
	public static void turnOnFlashLight() {
		CameraUtilities.setFlashLightMode(Parameters.FLASH_MODE_TORCH);
	}

	/**
	 * Turning off Flash Light.
	 * */
	public static void turnOffFlashLight() {
		CameraUtilities.setFlashLightMode(Parameters.FLASH_MODE_OFF);
	}

	/**
	 * Detect Flash Light hardware available on the current device.
	 * */
	public static boolean isFlashLightAvailable(Activity context) {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

	public static boolean isFlashLightAvailable(Context context) {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

	/**
	 * Detect Camera hardware available on the current device.
	 * */
	public static boolean isCameraAvailable(Activity context) {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}	
}
