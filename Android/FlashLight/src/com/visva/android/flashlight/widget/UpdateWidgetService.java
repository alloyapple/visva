package com.visva.android.flashlight.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.utilities.PreferenceUtilities;
import com.visva.android.flashlightmaster.R;

public class UpdateWidgetService extends Service implements Key {

	private int[] saveWidgetIds = null;
	private PreferenceUtilities _preferenceUtilities;

	public boolean checkUpdate(int widgetId) {
		boolean result = true;
		if (saveWidgetIds != null) {
			for (int i = 0; i < saveWidgetIds.length; i++) {
				if (widgetId == saveWidgetIds[i]) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		_preferenceUtilities = new PreferenceUtilities(getBaseContext());
		// _preferenceUtilities.setCheckTurnLed(true);

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		saveWidgetIds = _preferenceUtilities.getWidgetIds();

		for (int widgetId : allWidgetIds) {
			if (!checkUpdate(widgetId)) {
				continue;
			}
			RemoteViews remoteViews = new RemoteViews(getBaseContext().getPackageName(), R.layout.tinyflashlightwidget);

			switch (_preferenceUtilities.getWidgetItem()) {
			case WIDGET_1:
				remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_off);
				break;
			case WIDGET_2:
				remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_purple_off);
				break;
			case WIDGET_3:
				remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_bulb_green_off);
				break;
			case WIDGET_4:
				remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_off);
				break;
			case WIDGET_5:
				remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_purple_off);
				break;
			case WIDGET_6:
				remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_led_green_off);
				break;
			case WIDGET_8:
				remoteViews.setImageViewResource(R.id.ivWidgetLight, R.drawable.widget_flashlight_off);
				break;
			default:
				break;
			}
			_preferenceUtilities.addWidgetKeys(_preferenceUtilities.getWidgetItem());

			// Register an onClickListener
			Intent clickIntent;
			PendingIntent pendingIntent;

			clickIntent = new Intent(getBaseContext(), WidgetBroadcast.class);
			clickIntent.setAction("RunWidgetBroadcast");
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

			pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			remoteViews.setOnClickPendingIntent(R.id.ivWidgetLight, pendingIntent);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		_preferenceUtilities.setWidgetIds(allWidgetIds);
		stopSelf();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
