package com.visva.android.hangman.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.Utils;

public class MainMenuScreen extends Activity implements GlobalDef {
	/** Called when the activity is first created. */
	private static final String TAG = Utils.PREFIX_TAG + MainMenuScreen.class.getSimpleName();
	private Button btn_onePlayer;
	private Button btn_twoPlayer;
	private Button btn_options;
	private Button btn_moreGames;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		// init control
		initControl();
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		btn_onePlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentPlaySetting = new Intent(MainMenuScreen.this, PlayerSettingsScreen.class);
				startActivity(intentPlaySetting);
				finish();
			}
		});
		btn_twoPlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentPlaySetting = new Intent(MainMenuScreen.this, TwoPlayerSettingScreen.class);
				startActivity(intentPlaySetting);
				finish();
			}
		});
		btn_options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentOption = new Intent(MainMenuScreen.this, OptionsScreen.class);
				startActivity(intentOption);
				finish();
			}
		});
		btn_moreGames.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Utils.getUrlMoreGame()));
				} catch (ActivityNotFoundException activityNotFoundException1) {
					Log.e(TAG, "Market Intent not found");
				}
			}
		});
	}

	public void initControl() {
		btn_onePlayer = (Button) findViewById(R.id.btn_one_player);
		btn_twoPlayer = (Button) findViewById(R.id.btn_two_players);
		btn_options = (Button) findViewById(R.id.btn_options);
		btn_moreGames = (Button) findViewById(R.id.btn_more_games);
	}

	@Override
	public void onBackPressed() {
		android.os.Process.killProcess(android.os.Process.myPid());
		setResult(RESULT_OK);
		finish();
		return;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Log.e(TAG, "Accept result code");
			finish();
		}
	}

}