package com.visva.android.flashlight.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.common.Session;
import com.visva.android.flashlight.utilities.ScreenUtilities;
import com.visva.android.flashlightmaster.R;

public class WarningLightActivity extends BaseActivity {
	private ImageView imgUpLight;
	private ImageView imgDownLight;
	private boolean _upLightActivated = true;
	private Button _btnFeatures;
	private TextView _tvLabel;
	private AdView layoutAds;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warninglight);

		ScreenUtilities.setBrightness(this, Session.getHighestScreenBrightness());
		this.mapUIElement();
		this.detectResizeImageByScreen();
		this.initializeTimer();

		if (_preferenceUtilities.isShowDefaultLightSource() || _preferenceUtilities.isShowWarningLightLabel()) {
			flip(_tvLabel);
			_preferenceUtilities.setShowWarningLightLabel(false);
			_preferenceUtilities.setShowDefaultLightSource(false);
		} else {
			_tvLabel.setVisibility(View.GONE);
		}
		layoutAds = (AdView) this.findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest();
        adRequest.setTesting(true);
        layoutAds.loadAd(adRequest);
        layoutAds.bringToFront();
	}

	private void detectResizeImageByScreen() {
		Point __screenSize = ScreenUtilities.getScreenSize(this);
		LayoutParams __imageLayoutParams = this.imgUpLight.getLayoutParams();
		// The image size is depend on the screen width and height
		if (__screenSize.x <= 240) {
			__imageLayoutParams.width = 140;
			__imageLayoutParams.height = 140;
		} else if (__screenSize.x > 240 && __screenSize.x <= 320) {
			__imageLayoutParams.width = 220;
			__imageLayoutParams.height = 220;
		} else {
			__imageLayoutParams.width = 390;
			__imageLayoutParams.height = 390;
		}

		this.imgUpLight.setLayoutParams(__imageLayoutParams);
		this.imgDownLight.setLayoutParams(__imageLayoutParams);
	}

	private void mapUIElement() {
		this.imgUpLight = (ImageView) this.findViewById(R.id.imgUpLight);
		this.imgDownLight = (ImageView) this.findViewById(R.id.imgDownLight);
		this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
		this._btnFeatures.setOnClickListener(_btnFeature_Click);
		this._tvLabel = (TextView) this.findViewById(R.id.tvLabel);
	}

	private void initializeTimer() {
		Timer __changeLightTimer = new Timer();
		__changeLightTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				executeTimerTick();

			}
		}, 100, 800);
	}

	private void executeTimerTick() {
		// timer_Tick.run();
		this.runOnUiThread(this.timer_Tick);
	}

	private Runnable timer_Tick = new Runnable() {

		@Override
		public void run() {
			if (_upLightActivated) {
				imgUpLight.setImageResource(R.drawable.w_warning_light_off);
				imgDownLight.setImageResource(R.drawable.w_warning_light_on);
				_upLightActivated = false;
			} else {
				imgUpLight.setImageResource(R.drawable.w_warning_light_on);
				imgDownLight.setImageResource(R.drawable.w_warning_light_off);
				_upLightActivated = true;
			}

		}
	};
}
