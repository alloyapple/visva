package com.lemon.fromangle;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;

public class SplashActivity extends LemonBaseActivity {

	private static int TIME_SHOW_SPLASH = 3000;
	private boolean isTouch = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_splash);

		GlobalValue.prefs = new FromAngleSharedPref(self);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isTouch) {
					gotoActivity(self, TopScreenActivity.class);
					finish();
				}
			}
		}, TIME_SHOW_SPLASH);
	}

	public void start(View v) {
		isTouch = true;
		gotoActivity(self, TopScreenActivity.class);
		finish();
	}
}
