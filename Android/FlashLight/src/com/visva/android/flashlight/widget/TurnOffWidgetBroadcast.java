package com.visva.android.flashlight.widget;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.visva.android.flashlight.R;
import com.visva.android.flashlight.activities.BaseActivity;
import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.utilities.CameraUtilities;
import com.visva.android.flashlight.utilities.LEDUtilities;

public class TurnOffWidgetBroadcast extends BroadcastReceiver implements Key {

	@Override
	public void onReceive(Context context, Intent intent) {
		// remove notification
		WidgetBroadcast.mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		WidgetBroadcast.mNotificationManager.cancel(WidgetBroadcast.SIMPLE_NOTFICATION_ID);
		// turn off flash
		// CameraUtilities.turnOffFlashLight();
		//turnOff();
		updateOffAll(WidgetBroadcast._appWidgetIds, WidgetBroadcast._widgetKeys);
		BaseActivity._ledActivated = false;
	}

	private void updateOffAll(int[] widgetId, int[] widgetKey) {
		for (int i = 0; i < widgetId.length; i++) {
			updateOff(widgetId[i], widgetKey[i]);
		}
	}

	private void updateOff(int widgetId, int widgetKey) {
		switch (widgetKey) {
		case WIDGET_1:
			WidgetBroadcast._remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_off);
			break;
		case WIDGET_2:
			WidgetBroadcast._remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_purple_off);
			break;
		case WIDGET_3:
			WidgetBroadcast._remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_green_off);
			break;
		case WIDGET_4:
			WidgetBroadcast._remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_off);
			break;
		case WIDGET_5:
			WidgetBroadcast._remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_purple_off);
			break;
		case WIDGET_6:
			WidgetBroadcast._remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_green_off);
			break;
		case WIDGET_8:
			WidgetBroadcast._remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_flashlight_off);
			break;
		default:
			break;
		}
		WidgetBroadcast._appWidgetManager.updateAppWidget(widgetId, WidgetBroadcast._remoteViews);
	}

	private static void turnOff() {
		if (LEDUtilities.isSupported()) {
			LEDUtilities.turnOff();
		} else {
			CameraUtilities.turnOffFlashLight();
		}
	}
}
