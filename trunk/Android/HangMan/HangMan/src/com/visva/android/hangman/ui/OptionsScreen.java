package com.visva.android.hangman.ui;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.visva.android.hangman.R;
import com.visva.android.hangman.definition.GlobalDef;
import com.visva.android.hangman.ultis.GamePreferences;

public class OptionsScreen extends Activity implements GlobalDef, ConnectionCallbacks, OnConnectionFailedListener {
	/** Called when the activity is first created. */
	private ImageButton btn_back;
	private TextView img_sound;
	private TextView img_share;
	private int soundEnabled = ON;
	private Typeface mFont;
	private int mFontDefaultColor;
	private ToggleButton mBtnSoundMode;

	private UiLifecycleHelper uiHelper;

	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;
	private static final int REQ_SHARE_GG = 1;

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	/*
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();

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
		uiHelper.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
			}
		});
		if (requestCode == RC_SIGN_IN) {
			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	public void onClickShareFacebook(View v) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_icon);
		List<Bitmap> images = new ArrayList<Bitmap>();
		images.add(bitmap);
		OpenGraphObject video = OpenGraphObject.Factory.createForPost("games.other");
		video.setTitle(getString(R.string.app_name));
		video.setDescription(getString(R.string.share_sns_introduce_project));

		OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
		action.setProperty("game", video);
		action.setType("games.saves");

		FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(this, action, "game").setImageAttachmentsForAction(images, true).build();
		uiHelper.trackPendingDialogCall(shareDialog.present());

	}

	public void onClickShareGooglePlus(View v) {
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
		} else if (mGoogleApiClient.isConnecting()) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_icon);
			Uri selectedImage = getImageUri(this, bitmap);
			ContentResolver cr = this.getContentResolver();
			String mime = cr.getType(selectedImage);

			PlusShare.Builder share = new PlusShare.Builder(this);
			share.setText(getString(R.string.share_sns_introduce_project));
			share.addStream(selectedImage);
			share.setType(mime);
			startActivityForResult(share.getIntent(), REQ_SHARE_GG);
		} else {
			mGoogleApiClient.connect();
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (!mIntentInProgress && connectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				startIntentSenderForResult(connectionResult.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				// mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		Uri selectedImage = getImageUri(OptionsScreen.this, bitmap);
		ContentResolver cr = this.getContentResolver();
		String mime = cr.getType(selectedImage);

		PlusShare.Builder share = new PlusShare.Builder(this);
		share.setText(getString(R.string.can_you_guess_this_word));
		share.addStream(selectedImage);
		share.setType(mime);
		startActivityForResult(share.getIntent(), REQ_SHARE_GG);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onDestroy() {
		// finish();
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
}