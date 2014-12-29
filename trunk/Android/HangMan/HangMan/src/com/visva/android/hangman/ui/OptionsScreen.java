package com.visva.android.hangman.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.GamePreferences;

public class OptionsScreen extends Activity implements GlobalDef {
	/** Called when the activity is first created. */
	private ImageButton btn_back;
	private TextView img_sound;
	private TextView img_share;
	private Button btn_share;
	private int soundEnabled = ON;
	private Typeface mFont;
	private int mFontDefaultColor;
	private int mFontPressedColor;
	private ToggleButton mBtnSoundMode;

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
				Intent _mainMenuIntent = new Intent(OptionsScreen.this, MainMenuScreen.class);
				startActivity(_mainMenuIntent);
				finish();
				finish();
			}
		});
		mBtnSoundMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					GamePreferences.setIntVal(OptionsScreen.this, SOUND_ON, ON);
				else
					GamePreferences.setIntVal(OptionsScreen.this, SOUND_ON, OFF);
			}
		});
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(OptionsScreen.this, "Shared", Toast.LENGTH_SHORT).show();
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
		btn_share = (Button) findViewById(R.id.btn_share_via);
		img_share = (TextView) findViewById(R.id.lbl_share_via);
		img_share.setTypeface(mFont);
		img_share.setTextColor(mFontDefaultColor);
		img_sound = (TextView) findViewById(R.id.lbl_sound);
		img_sound.setTypeface(mFont);
		img_sound.setTextColor(mFontDefaultColor);
		mBtnSoundMode = (ToggleButton) findViewById(R.id.toggle_btn_sound_mode);
	}

	/*
	 * Load pref
	 */
	public void loadPref() {
		soundEnabled = GamePreferences.getIntVal(this, SOUND_ON, ON);
		if (soundEnabled == ON)
			mBtnSoundMode.setChecked(true);
		else
			mBtnSoundMode.setChecked(false);
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