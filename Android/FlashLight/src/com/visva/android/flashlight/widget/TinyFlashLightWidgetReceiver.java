package com.visva.android.flashlight.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TinyFlashLightWidgetReceiver extends BroadcastReceiver {
	private static boolean _lightOn = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("intent.getACtion", ""+intent.getAction());
//		RemoteViews __rViews = new RemoteViews(context.getPackageName(), R.layout.tinyflashlightwidget);
//		
//		if(TinyFlashLightWidgetReceiver._lightOn) {
//			__rViews.setImageViewResource(R.id.btnWidgetLight, R.drawable.widget_flashlight_on);
//		}
//		else {
//			__rViews.setImageViewResource(R.id.btnWidgetLight, R.drawable.widget_flashlight_off);
//		}
//		
//		AppWidgetManager __widgetManger = AppWidgetManager.getInstance(context);
//		__widgetManger.updateAppWidget(new ComponentName(context, TinyFlashLightWidgetProvider.class), __rViews);
//		
//		if(TinyFlashLightWidgetReceiver._lightOn) {
//			CameraUtilities.turnOffFlashLight();
//			TinyFlashLightWidgetReceiver._lightOn = false;
//		}
//		else {
//			CameraUtilities.turnOnFlashLight();
//			TinyFlashLightWidgetReceiver._lightOn = true;
//		}
	}
}