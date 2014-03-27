package com.visva.android.flashlight.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.visva.android.flashlight.R;
import com.visva.android.flashlight.adapter.WidgetAdapter;
import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.common.ScreenLight;
import com.visva.android.flashlight.common.WidgetItemKey;
import com.visva.android.flashlight.utilities.PreferenceUtilities;
import com.visva.android.flashlight.utilities.ScreenUtilities;
import com.visva.android.flashlight.widget.TinyFlashLightWidgetProvider;
import com.visva.android.flashlight.widget.UpdateWidgetService;

public class TinyFlashLightWidgetActivity extends Activity implements Key {
	private GridView _gvFeature;
	private WidgetAdapter _featureAdapter;
	private ArrayList<ScreenLight> lstVisibleLightSources = new ArrayList<ScreenLight>();
	private int[] allWidgetIds = null;
	private Bundle _bundle;
	private PreferenceUtilities _preferenceUtilities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tinyflashlightwidget_test);
		mapUIElement();

		_bundle = getIntent().getExtras();
		if (_bundle != null) {
			allWidgetIds = _bundle.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (allWidgetIds != null) {
				Intent deleteWidget = new Intent(getBaseContext(), TinyFlashLightWidgetProvider.class);
				deleteWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_DELETED);
				deleteWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
				PendingIntent pendingDeleteWidget = PendingIntent.getBroadcast(getBaseContext(), 0, deleteWidget,
						PendingIntent.FLAG_UPDATE_CURRENT);
				try {
					pendingDeleteWidget.send();
				} catch (CanceledException e) {
				}
			}
			finish();
		}
		return false;
	}

	private void mapUIElement() {
		_preferenceUtilities = new PreferenceUtilities(this);

		for (int j = 0; j < WidgetItemKey.lstWidgetItem.size(); j++) {
			lstVisibleLightSources.add(WidgetItemKey.lstWidgetItem.get(j));
		}
		_gvFeature = (GridView) findViewById(R.id.gvFeature2);
		Point point = ScreenUtilities.getScreenSize(TinyFlashLightWidgetActivity.this);
		boolean isSmallScreen = false;
		if (point.x <= 240 && point.y <= 320) {
			isSmallScreen = true;
		}
		_featureAdapter = new WidgetAdapter(TinyFlashLightWidgetActivity.this, lstVisibleLightSources, isSmallScreen);
		_gvFeature.setAdapter(_featureAdapter);
		_gvFeature.setOnItemClickListener(_gvFeature_Click);

	}

	private OnItemClickListener _gvFeature_Click = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			chooseWidgetItem(lstVisibleLightSources.get(position).getId());
		}
	};

	private void chooseWidgetItem(int position) {
		switch (position) {
		case WIDGET_1:
			_preferenceUtilities.setWidgetItem(WIDGET_1);
			Log.e("TAG", "WIDGET_1");
			break;
		case WIDGET_2:
			_preferenceUtilities.setWidgetItem(WIDGET_2);
			Log.e("TAG", "WIDGET_2");
			break;
		case WIDGET_3:
			_preferenceUtilities.setWidgetItem(WIDGET_3);
			Log.e("TAG", "WIDGET_3");
			break;
		case WIDGET_4:
			_preferenceUtilities.setWidgetItem(WIDGET_4);
			Log.e("TAG", "WIDGET_4");
			break;
		case WIDGET_5:
			_preferenceUtilities.setWidgetItem(WIDGET_5);
			Log.e("TAG", "WIDGET_5");
			break;
		case WIDGET_6:
			_preferenceUtilities.setWidgetItem(WIDGET_6);
			Log.e("TAG", "WIDGET_6");
			break;
		case WIDGET_TRANSPARENT:
			_preferenceUtilities.setWidgetItem(WIDGET_TRANSPARENT);
			Log.e("TAG", "WIDGET_7");
			break;
		case WIDGET_8:
			_preferenceUtilities.setWidgetItem(WIDGET_8);
			Log.e("TAG", "WIDGET_8");
			break;
		default:
			break;
		}

		// Build the intent to call the service
		if (allWidgetIds != null) {
			Intent intent = new Intent(getBaseContext(), UpdateWidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
			// Update the widgets via the service
			startService(intent);
		}
		finish();
	}

}
