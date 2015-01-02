package com.visva.android.hangman.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.FacebookUtil;
import com.visva.android.hangman.ultis.GamePreferences;
import com.visva.android.hangman.ultis.IRequestListener;

public class OptionsScreen extends Activity implements GlobalDef {
	/** Called when the activity is first created. */
	private ImageButton btn_back;
	private TextView img_sound;
	private TextView img_share;
	private int soundEnabled = ON;
	private Typeface mFont;
	private int mFontDefaultColor;
	private ToggleButton mBtnSoundMode;
	private SimpleFacebook mSimpleFacebook;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSimpleFacebook = SimpleFacebook.getInstance(this);

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
	}

	/*
	 * init controls
	 */
	public void initControl() {
		mFont = Typeface.createFromAsset(getAssets(), "fonts/SHOWG.TTF");
		mFontDefaultColor = getResources().getColor(R.color.font_default_color);
		getResources().getColor(R.color.font_pressed_color2);
		btn_back = (ImageButton) findViewById(R.id.back_options);
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

	@Override
	protected void onResume() {
		super.onResume();
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
	}

	public void onClickShareFacebook(View v) {
		if (mSimpleFacebook == null || !mSimpleFacebook.isLogin()) {
			mSimpleFacebook.login(onLoginListener);
			return;
		} else {
			requestUserFacebookInfo();
		}
	}

	/**
	 * Login example.
	 */
	// Login listener
	final OnLoginListener onLoginListener = new OnLoginListener() {

		@Override
		public void onFail(String reason) {
			Log.d("KieuThang", "Failed to login");
		}

		@Override
		public void onException(Throwable throwable) {
			Log.d("KieuThang", "Bad thing happened", throwable);
		}

		@Override
		public void onThinking() {
			// show progress bar or something to the user while login is
			// happening
		}

		@Override
		public void onLogin() {
			// change the state of the button or do whatever you want
			Log.d("KieuThang", "onLogin");
			requestUserFacebookInfo();
		}

		@Override
		public void onNotAcceptingPermissions(Permission.Type type) {
			Toast.makeText(OptionsScreen.this, String.format("You didn't accept %s permissions", type.name()), Toast.LENGTH_SHORT).show();
		}
	};

	private void requestUserFacebookInfo() {
		if (mSimpleFacebook == null) {
			mSimpleFacebook = SimpleFacebook.getInstance(this);
			return;
		}
		SimpleFacebook.getInstance().getProfile(new OnProfileListener() {

			@Override
			public void onThinking() {
			}

			@Override
			public void onException(Throwable throwable) {
			}

			@Override
			public void onFail(String reason) {
			}

			@Override
			public void onComplete(Profile profile) {
				onFacebookUserConnected(profile);
			}
		});

	}

	private void onFacebookUserConnected(Profile profile) {
		FacebookUtil.getInstance(this).publishShareInBackground(new IRequestListener() {

			@Override
			public void onResponse(String response) {
				String shareDone = getString(R.string.shared_facebook);
				Toast.makeText(OptionsScreen.this, shareDone, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onErrorResponse(String errorMessage) {

			}
		});
	}

	public void onClickShareGooglePlus(View v) {

	}
}