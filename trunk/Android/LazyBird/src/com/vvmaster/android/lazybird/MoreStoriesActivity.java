package com.vvmaster.android.lazybird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MoreStoriesActivity extends Activity {
	public static final int MODE_MENU = 1;
	public static final int MODE_INFO = 2;
	private int mMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mMode = savedInstanceState.getInt("modeIntent", MODE_MENU);
		} else {
			Intent intent = getIntent();
			mMode = intent.getIntExtra("modeIntent", MODE_MENU);
		}
		// Run as full-screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.more_stories);
		ImageButton btn_house = (ImageButton) findViewById(R.id.btn_house);
		btn_house.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mMode == MODE_INFO){
					Intent intent = new Intent(MoreStoriesActivity.this,
							MenuActivity.class);
					startActivity(intent);
					finish();
				}
				else if (mMode == MODE_MENU) {
					Intent intent = new Intent(MoreStoriesActivity.this,
							MenuActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
		final ImageButton btn_doubting_dasha = (ImageButton) findViewById(R.id.btn_doubting_dasha);
		btn_doubting_dasha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		final ImageButton btn_mommy_love_you = (ImageButton) findViewById(R.id.btn_mommy_love_you);
		btn_mommy_love_you.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		final ImageButton btn_booger_town = (ImageButton) findViewById(R.id.btn_booger_town);
		btn_booger_town.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void startIt(Activity caller, int mMode) {
		Intent intent = new Intent(caller, MoreStoriesActivity.class);
		intent.putExtra("modeIntent", mMode);
		caller.startActivity(intent);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(MoreStoriesActivity.this, MenuActivity.class);
			startActivity(i);
			finish();
			return true;

		}
		if (keyCode == KeyEvent.KEYCODE_CALL) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
