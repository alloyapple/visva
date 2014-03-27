package com.visva.android.flashlight.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.visva.android.flashlight.activities.TinyFlashLightWidgetActivity;
import com.visva.android.flashlight.utilities.PreferenceUtilities;

public class TinyFlashLightWidgetProvider extends AppWidgetProvider {

	private PreferenceUtilities _preferenceUtilities;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		_preferenceUtilities = new PreferenceUtilities(context);
		_preferenceUtilities.setCheckTurnLed(false);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
//		Toast.makeText(context, "aaa", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIDs) {

		// Get all ids
		ComponentName thisWidget = new ComponentName(context, TinyFlashLightWidgetProvider.class);

		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		Intent intentWidgetActivity = new Intent(context, TinyFlashLightWidgetActivity.class);
		intentWidgetActivity.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		intentWidgetActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intentWidgetActivity);

	}

}
