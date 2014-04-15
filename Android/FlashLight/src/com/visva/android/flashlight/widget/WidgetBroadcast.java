package com.visva.android.flashlight.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.utilities.CameraUtilities;
import com.visva.android.flashlight.utilities.LEDUtilities;
import com.visva.android.flashlight.utilities.PreferenceUtilities;
import com.visva.android.flashlightmaster.R;

public class WidgetBroadcast extends BroadcastReceiver implements Key {
	public static boolean _ledActivated;
	private static boolean _screenActivated = false;
	private Intent _intent;
	public static RemoteViews _remoteViews;
	public static AppWidgetManager _appWidgetManager;
	private PreferenceUtilities _pref;
	public static int[] _appWidgetIds;
	public static int[] _widgetKeys;
	private boolean _flash = false;
	public static NotificationManager mNotificationManager;
	public static int SIMPLE_NOTFICATION_ID = 1;

	@Override
	public void onReceive(Context context, Intent intent) {
		this._intent = intent;
		_appWidgetManager = AppWidgetManager.getInstance(context);
		this._pref = new PreferenceUtilities(context);
		_appWidgetIds = _intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		this._flash = CameraUtilities.isFlashLightAvailable(context);
		_remoteViews = new RemoteViews(context.getPackageName(), R.layout.tinyflashlightwidget);

		_widgetKeys = _pref.getWidgetKeys();

		if (_flash) {
			if (!_ledActivated) {
				_pref.setCheckTurnLed(true);
				// show up notification
				if (_pref.isLedShowNotification()) {
					mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notifyDetails = new Notification(R.drawable.notification, context.getResources()
							.getString(R.string.notifier_status_bar), System.currentTimeMillis());

					Intent clickIntent = new Intent(context, TurnOffWidgetBroadcast.class);
					clickIntent.setAction("TurnOffWidgetBroadcast");
					PendingIntent myIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					notifyDetails.setLatestEventInfo(context,
							context.getResources().getString(R.string.notifier_title), context.getResources()
									.getString(R.string.notifier_content), myIntent);
					notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
					mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails);
				}
				// turn on flash
				// CameraUtilities.turnOnFlashLight();
				turnOn();
				updateOnAll(_appWidgetIds, _widgetKeys);
				_ledActivated = true;

			} else {
				// _pref.setCheckTurnLed(false);
				// remove notification
				if (_pref.isLedShowNotification()) {
					mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					mNotificationManager.cancel(SIMPLE_NOTFICATION_ID);
				}
				// turn off flash
				// CameraUtilities.turnOffFlashLight();
				//turnOff();
				updateOffAll(_appWidgetIds, _widgetKeys);
				_ledActivated = false;

			}

			_pref.setLedLightTurnOn(_ledActivated);
		} else {
			if (!_screenActivated) {
				updateOnAll(_appWidgetIds, _widgetKeys);
				_screenActivated = true;
			} else {
				updateOffAll(_appWidgetIds, _widgetKeys);
				_screenActivated = false;
			}
//			Intent intent2 = new Intent(context, MainActivity.class);
//			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(intent2);
		}
	}

	private void updateOnAll(int[] widgetId, int[] widgetKey) {
		for (int i = 0; i < widgetId.length; i++) {
			updateOn(widgetId[i], widgetKey[i]);
		}
	}

	private void updateOn(int widgetId, int widgetKey) {
		switch (widgetKey) {
		case WIDGET_1:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_on);
			break;
		case WIDGET_2:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_purple_on);
			break;
		case WIDGET_3:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_green_on);
			break;
		case WIDGET_4:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_on);
			break;
		case WIDGET_5:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_purple_on);
			break;
		case WIDGET_6:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_green_on);
			break;
		case WIDGET_8:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_flashlight_on);
			break;
		default:
			break;
		}
		_appWidgetManager.updateAppWidget(widgetId, _remoteViews);
	}

	private void updateOffAll(int[] widgetId, int[] widgetKey) {
		for (int i = 0; i < widgetId.length; i++) {
			updateOff(widgetId[i], widgetKey[i]);
		}
	}

	private void updateOff(int widgetId, int widgetKey) {
		switch (widgetKey) {
		case WIDGET_1:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_off);
			break;
		case WIDGET_2:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_purple_off);
			break;
		case WIDGET_3:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_green_off);
			break;
		case WIDGET_4:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_off);
			break;
		case WIDGET_5:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_purple_off);
			break;
		case WIDGET_6:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_green_off);
			break;
		case WIDGET_8:
			_remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_flashlight_off);
			break;
		default:
			break;
		}
		_appWidgetManager.updateAppWidget(widgetId, _remoteViews);
	}

	private static void turnOn() {
		if (LEDUtilities.isSupported()) {
			LEDUtilities.turnOn();
		} else {
			CameraUtilities.turnOnFlashLight();
		}
	}

	private static void turnOff() {
		if (LEDUtilities.isSupported()) {
			LEDUtilities.turnOff();
		} else {
			CameraUtilities.turnOffFlashLight();
		}
	}

}
