package com.visva.android.hangman.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.GamePreferences;

public class OptionsScreen extends Activity implements GlobalDef {
	/** Called when the activity is first created. */
	private ImageButton btn_back;
	private TextView img_soundOn;
	private TextView img_soundOff;
	private TextView img_gallownsShow;
	private TextView img_gallownsHide;
	private TextView img_sound;
	private TextView img_gallows;
	private int soundEnabled = ON;
	private int gallownsShow = SHOW;
	private Typeface mFont;
	private int mFontDefaultColor;
	private int mFontPressedColor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_screen);
		initControl();
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		loadPref();
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent _mainMenuIntent = new Intent(OptionsScreen.this, MainMenuScreen.class);
				startActivity(_mainMenuIntent);
				finish();
				finish();
			}
		});
		img_soundOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_soundOn.setTextColor(mFontPressedColor);
				img_soundOff.setTextColor(mFontDefaultColor);
				GamePreferences.setIntVal(OptionsScreen.this, SOUND_ON, ON);
			}
		});
		img_soundOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				img_soundOn.setTextColor(mFontDefaultColor);
				img_soundOff.setTextColor(mFontPressedColor);
				GamePreferences.setIntVal(OptionsScreen.this, SOUND_ON, OFF);
			}
		});
		img_gallownsShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_gallownsShow.setTextColor(mFontPressedColor);
				img_gallownsHide.setTextColor(mFontDefaultColor);
				GamePreferences.setIntVal(OptionsScreen.this, GALL_OWNS, SHOW);
			}
		});
		img_gallownsHide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				img_gallownsShow.setTextColor(mFontDefaultColor);
				img_gallownsHide.setTextColor(mFontPressedColor);
				GamePreferences.setIntVal(OptionsScreen.this, GALL_OWNS, HIDE);
			}
		});
	}

	/*
	 * init controls
	 */
	public void initControl() {
		mFont = Typeface.createFromAsset(getAssets(), "fonts/SHOWG.TTF");
		mFontDefaultColor = getResources().getColor(R.color.font_default_color);
		mFontPressedColor = getResources().getColor(R.color.font_pressed_color2);
		btn_back = (ImageButton) findViewById(R.id.back_options);
		img_soundOn = (TextView) findViewById(R.id.sound_on);
		img_soundOn.setTypeface(mFont);
		img_soundOn.setTextColor(mFontDefaultColor);
		img_soundOff = (TextView) findViewById(R.id.sound_off);
		img_soundOff.setTypeface(mFont);
		img_soundOff.setTextColor(mFontDefaultColor);
		img_gallownsShow = (TextView) findViewById(R.id.show_gallows);
		img_gallownsShow.setTypeface(mFont);
		img_gallownsShow.setTextColor(mFontDefaultColor);
		img_gallownsHide = (TextView) findViewById(R.id.hide_gallows);
		img_gallownsHide.setTypeface(mFont);
		img_gallownsHide.setTextColor(mFontDefaultColor);
		img_sound = ( TextView)findViewById(R.id.lbl_sound);
		img_sound.setTypeface(mFont);
		img_sound.setTextColor(mFontDefaultColor);
		img_gallows = (TextView)findViewById(R.id.lbl_gallows);
		img_gallows.setTypeface(mFont);
		img_gallows.setTextColor(mFontDefaultColor);
	}

	/*
	 * Load pref
	 */
	public void loadPref() {
		soundEnabled = GamePreferences.getIntVal(this, SOUND_ON, ON);
		gallownsShow = GamePreferences.getIntVal(this, GALL_OWNS, SHOW);
		if (soundEnabled == ON) {
			img_soundOn.setTextColor(mFontPressedColor);
		} else {
			img_soundOff.setTextColor(mFontDefaultColor);
		}
		if (gallownsShow == SHOW) {
			img_gallownsShow.setTextColor(mFontPressedColor);
		} else {
			img_gallownsHide.setTextColor(mFontDefaultColor);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		Intent _mainMenuIntent = new Intent(OptionsScreen.this, MainMenuScreen.class);
		startActivity(_mainMenuIntent);
		finish();
		super.onBackPressed();
	}

}