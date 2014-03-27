package com.visva.android.flashlight.common;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.visva.android.flashlight.utilities.PreferenceUtilities;

public class ShakedListener implements SensorEventListener {
	private static final int FORCE_THRESHOLD = 300; // 350
	private static final int TIME_THRESHOLD = 100;
	// each shake in 500ms
	private static final int SHAKE_TIMEOUT = 500;
	private static final int SHAKE_DURATION = 1000;
	private static final int SHAKE_COUNT = 3;

	private OnShakeListener onShakeListener;
	private SensorManager sensorManager;
	private Sensor sensor;
	private Context context;
	private boolean supportSensor = false;
	private boolean registerSensor = false;

	// x, y, z last shake
	private float xLast, yLast, zLast;
	// last time force shake
	private long lastForce = 0;
	// count number shake between two force
	private int shakeCount = 0;
	// last time
	private long lastTime;
	// last shake
	private long lastShake;

	private int checkShakeCount = SHAKE_COUNT;

	private int checkTimeThreshold = TIME_THRESHOLD;

	private int checkForceThrehold = FORCE_THRESHOLD;

	private PreferenceUtilities preferenceUtilities;

	public PreferenceUtilities getPreferenceUtilities() {
		return preferenceUtilities;
	}

	public void setPreferenceUtilities(PreferenceUtilities preferenceUtilities) {
		this.preferenceUtilities = preferenceUtilities;
	}

	public int getCheckShakeCount() {
		return checkShakeCount;
	}

	public void setCheckShakeCount(int checkShakeCount) {
		this.checkShakeCount = checkShakeCount;
	}

	public int getCheckTimeThreshold() {
		return checkTimeThreshold;
	}

	public void setCheckTimeThreshold(int checkTimeThreshold) {
		this.checkTimeThreshold = checkTimeThreshold;
	}

	public int getCheckForceThrehold() {
		return checkForceThrehold;
	}

	public void setCheckForceThrehold(int checkForceThrehold) {
		this.checkForceThrehold = checkForceThrehold;
	}

	public void setOnShakeListener(OnShakeListener onShakeListener) {
		this.onShakeListener = onShakeListener;
	}

	public ShakedListener(Context context) {
		this.context = context;
		resume();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (!preferenceUtilities.isLedSharkOnLockScreen()) return;

		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

		// get time now
		long now = System.currentTimeMillis();

		// check shake has time out, if true reset shakeCount
		if ((now - lastForce) > SHAKE_TIMEOUT) {
			shakeCount = 0;
		}

		// get time between two shake
		long diff = now - lastTime;

		// if this time larger than time threshold, begin calculator distance between 2 point A(now) & B(last) and speed
		if (diff > checkTimeThreshold) {

			// calculator distance
			float distance = Math.abs(event.values[0] + event.values[1] + event.values[2] - xLast - yLast - zLast);
			// calculator speed
			float speed = distance / diff * 10000;

			// if speed larger than time force threshold
			if (speed > checkForceThrehold) {

				if ((++shakeCount >= checkShakeCount) && (now - lastShake > SHAKE_DURATION)) {
					lastShake = now;
					shakeCount = 0;

					if (onShakeListener != null) {
						onShakeListener.onShake();
					}
				}

				// save time last force check to implement
				lastForce = now;
			}

			// save time last time check between 2 point
			lastTime = now;

			xLast = event.values[0];
			yLast = event.values[1];
			zLast = event.values[2];
		}

	}

	public void resume() {
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager == null) {
			supportSensor = false;
			return;
		}
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		registerSensor = sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

		if (!registerSensor) {
			sensorManager.unregisterListener(this);
		}
	}

	public void pause() {
		if (supportSensor) {
			sensorManager.unregisterListener(this);
			sensorManager = null;
		}
	}
}