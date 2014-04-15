package com.visva.android.flashlightmaster;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.visva.android.flashlight.activities.AppsActivity;
import com.visva.android.flashlight.activities.BaseActivity;
import com.visva.android.flashlight.activities.ColorLightActivity;
import com.visva.android.flashlight.activities.LEDLightActivity;
import com.visva.android.flashlight.activities.LightBuldActivity;
import com.visva.android.flashlight.activities.MorseCodeActivity;
import com.visva.android.flashlight.activities.PoliceLightActivity;
import com.visva.android.flashlight.activities.ScreenLightActivity;
import com.visva.android.flashlight.activities.StrobeLightActivity;
import com.visva.android.flashlight.activities.WarningLightActivity;
import com.visva.android.flashlight.common.Key;

public class MainActivity extends BaseActivity implements Key {

	private ActivityManager _managerTask;

	private List<ActivityManager.RunningTaskInfo> _listTask;

	private RunningTaskInfo _infoTask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		_preferenceUtilities.setStrobeLightOnSpeed(100);
		_preferenceUtilities.setStrobeLightOffSpeed(100);
		_managerTask = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		if (!isTaskRoot()) {
			final Intent intent = getIntent();
			final String intentAction = intent.getAction();
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null
					&& intentAction.equals(Intent.ACTION_MAIN)) {
				finish();
				return;
			}
		}

		_preferenceUtilities.setShowDefaultLightSource(true);
		if (_preferenceUtilities.isCheckTurnLed()) {
			_preferenceUtilities.setCheckTurnLed(false);
			// AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
			// ComponentName thisWidget = new ComponentName(getBaseContext(), TinyFlashLightWidgetProvider.class);
			// int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
			// Intent intent = new Intent(getBaseContext(), WidgetBroadcast.class);
			// intent.setAction("RunWidgetBroadcast");
			// intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
			// PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent,
			// PendingIntent.FLAG_UPDATE_CURRENT);
			// try {
			// pendingIntent.send();
			// } catch (CanceledException e) {
			// }
		}

		switch (_preferenceUtilities.getDefaultLightSource()) {
		case LED_LIGHT:
			// if (CameraUtilities.isFlashLightAvailable(this)) {
			// Session.setVibrateOnActiveLED(true);
			switchToActivity(LED_LIGHT, LEDLightActivity.class);
			// }
			break;
		case SCREEN_LIGHT:
			switchToActivity(SCREEN_LIGHT, ScreenLightActivity.class);
			break;
		case MORSE_CODE:
			switchToActivity(MORSE_CODE, MorseCodeActivity.class);
			break;
		case STROBE_LIGHT:
			switchToActivity(STROBE_LIGHT, StrobeLightActivity.class);
			break;
		case WARNING_LIGHT:
			switchToActivity(WARNING_LIGHT, WarningLightActivity.class);
			break;
		case LIGHT_BULD:
			switchToActivity(LIGHT_BULD, LightBuldActivity.class);
			break;
		case COLOR_LIGHT:
			switchToActivity(COLOR_LIGHT, ColorLightActivity.class);
			break;
		case POLICE_LIGHT:
			switchToActivity(POLICE_LIGHT, PoliceLightActivity.class);
			break;
		case APPS:
			switchToActivity(APPS, AppsActivity.class);
			break;
		default:
			switchToActivity(SCREEN_LIGHT, ScreenLightActivity.class);
			break;
		}
		finish();

		new Thread() {
			public void run() {
				while (true) {
					_listTask = _managerTask.getRunningTasks(1);
					_infoTask = (RunningTaskInfo) _listTask.get(0);

					String __className = _infoTask.topActivity.getClassName();
					if (__className.length() < 29) {
						return;
					}
					__className = __className.substring(0, 29);

					if (__className.compareToIgnoreCase("com.visva.android.flashlight") != 0) {
						if (!_preferenceUtilities.isCheckTurnLed()) {
//							Log.d("THREAD", "xxx");
							//StrobeLightActivity.cancelFlash();
							//
							//MorseCodeActivity.cancelMorseCode();
							//LEDLightActivity.cancelLed();
							//CameraUtilities.turnOffFlashLight();
						}
					}
				}
			}
		}.start();
	}
}