package com.visva.android.flashlight.activities;

import java.util.ArrayList;

import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.R;
import com.visva.android.flashlight.adapter.CustomAdapter;
import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.common.SaveVisibleLightSource;
import com.visva.android.flashlight.common.ScreenLight;
import com.visva.android.flashlight.common.ScreenLightKey;
import com.visva.android.flashlight.common.Session;
import com.visva.android.flashlight.utilities.ScreenUtilities;
import com.visva.android.flashlight.utilities.SwitchUtilities;

public class FeatureActivity extends BaseActivity implements Key {

	private LinearLayout _lnFeature;
	private CustomAdapter _customAdapter;
	private String visibleLightSource = "";
	private ArrayList<Integer> _lstVisibleLightSourcesId = new ArrayList<Integer>();
	private ArrayList<ScreenLight> _lstVisibleLightSources = new ArrayList<ScreenLight>();
	private Button _btnFeatures;
	private AdView layoutAds;

	@SuppressWarnings("deprecation")
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feature);
		mapUIElement();
		
		refreshAdsMob();
        layoutAds = (AdView) this.findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest();
        adRequest.setTesting(true);
        layoutAds.loadAd(adRequest);
        layoutAds.bringToFront();
	}
	private void refreshAdsMob() {
        new CountDownTimer(12000, 20000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                AdRequest adRequest = new AdRequest();
                adRequest.setTesting(true);
                layoutAds.refreshDrawableState();
                layoutAds.loadAd(adRequest);
                layoutAds.invalidate();
                layoutAds.bringToFront();
                refreshAdsMob();
            }
        }.start();
    }
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SwitchUtilities.switchTo(FeatureActivity.this, _preferenceUtilities.getScreenSelected());
			StrobeLightActivity.cancelFlash();
			finish();
		}
		return false;
	}

	private void mapUIElement() {
		Point point = ScreenUtilities.getScreenSize(FeatureActivity.this);
		boolean isSmallScreen = false;
		if (point.x <= 240 && point.y <= 320) {
			isSmallScreen = true;
		}

		this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
		this._btnFeatures.setSelected(true);
		this._btnFeatures.setOnClickListener(_btnFeatures_Clicks);
		this._lnFeature = (LinearLayout) findViewById(R.id.lnFeature);

		visibleLightSource = _preferenceUtilities.getVisibleLightSources();
		Log.d("KieuThang", "visibleLightSource "+visibleLightSource.length());
		_lstVisibleLightSourcesId = SaveVisibleLightSource.getListVisibleSources(visibleLightSource);
		for (int i = 0; i < _lstVisibleLightSourcesId.size(); i++) {
			for (int j = 0; j < ScreenLightKey.lstScreenLight.size(); j++) {
				if (_lstVisibleLightSourcesId.get(i) == ScreenLightKey.lstScreenLight.get(j).getId()) {
					_lstVisibleLightSources.add(ScreenLightKey.lstScreenLight.get(j));
				}
			}
		}

		_customAdapter = new CustomAdapter(this, isSmallScreen, _lstVisibleLightSources);
		_lnFeature.addView(_customAdapter.getTableLayout());

	}

	private OnClickListener _btnFeatures_Clicks = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SwitchUtilities.switchTo(FeatureActivity.this, _preferenceUtilities.getScreenSelected());
		}
	};

	public void switchToScreen(int position) {
		StrobeLightActivity.cancelFlash();
		MorseCodeActivity.cancelMorseCode();
		LEDLightActivity.cancelLed();
		// CameraUtilities.turnOffFlashLight();
		switch (position) {
		case LED_LIGHT:
			_preferenceUtilities.setScreenLightTurnOn(false);
			// if (CameraUtilities.isFlashLightAvailable(FeatureActivity.this) || LEDUtilities.isSupported()) {
			Session.setVibrateOnActiveLED(true);
			_preferenceUtilities.setShowLedLightLabel(true);
			switchToActivity(LED_LIGHT, LEDLightActivity.class);
			finish();
			// }
			break;
		case SCREEN_LIGHT:
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setShowScreenLightLabel(true);
			switchToActivity(SCREEN_LIGHT, ScreenLightActivity.class);
			finish();
			break;
		case MORSE_CODE:
			_preferenceUtilities.setScreenLightTurnOn(false);
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setShowMorseLightLabel(true);
			switchToActivity(MORSE_CODE, MorseCodeActivity.class);
			finish();
			break;
		case STROBE_LIGHT:
			_preferenceUtilities.setScreenLightTurnOn(false);
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setShowStrobeLightLabel(true);
			switchToActivity(STROBE_LIGHT, StrobeLightActivity.class);
			finish();
			break;
		case WARNING_LIGHT:
			_preferenceUtilities.setScreenLightTurnOn(false);
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setShowWarningLightLabel(true);
			switchToActivity(WARNING_LIGHT, WarningLightActivity.class);
			finish();
			break;
		case LIGHT_BULD:
			_preferenceUtilities.setScreenLightTurnOn(false);
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setShowLightBuldLabel(true);
			switchToActivity(LIGHT_BULD, LightBuldActivity.class);
			finish();
			break;
		case COLOR_LIGHT:
			_preferenceUtilities.setScreenLightTurnOn(false);
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setShowColorLightLabel(true);
			switchToActivity(COLOR_LIGHT, ColorLightActivity.class);
			finish();
			break;
		case POLICE_LIGHT:
			_preferenceUtilities.setScreenLightTurnOn(false);
			_preferenceUtilities.setShowPoliceLabel(true);
			switchToActivity(POLICE_LIGHT, PoliceLightActivity.class);
			finish();
			break;
		case APPS:
			_preferenceUtilities.setScreenLightTurnOn(false);
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setShowAppsLabel(true);
			switchToActivity(APPS, AppsActivity.class);
			finish();
			break;
		case SETTING:
			_preferenceUtilities.setScreenLightTurnOn(false);
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setShowSettingLabel(true);
			switchToActivity(SETTING, SettingActivity.class);
			finish();
			break;
		default:
			break;
		}
	}
}
