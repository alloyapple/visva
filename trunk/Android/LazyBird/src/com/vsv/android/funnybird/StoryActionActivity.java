package com.vsv.android.funnybird;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.mystictreegames.pagecurl.PageCurlView;

public class StoryActionActivity extends Activity {
	public static final int INFO_ACTIVITY = 0;
	private static final String TAG = "VSV";

	public static final String EXTRA_MODE = "StoryActionActivity.mMode";
	public static final String EXTRA_START_PAGE = "StoryActionActivity.startPage";

	private static Typeface mPapyrus;
	private static Typeface mMyriadPro;
	private static Typeface mTimes;

	public static final int MODE_READ_TO_ME_ORIGINAL = 0;
	public static final int MODE_READ_TO_ME_MYOWN = 1;
	public static final int MODE_READ_MYSELF = 2;
	public static final int MODE_RECORD = 3;

	private enum RecordControlsState {
		STATE_TO_RECORD, STATE_RECORD_FINISHED, STATE_RECORD_SAVED
	}

	public static final String SHARED_PREFS = "LazyBird.pref";
	public static final String PREF_LAST_READ_PAGE = "StoryActionActivity.mLastReadpage";

	private PageCurlView mPageCurl;
	private PagesManager mPagesManager;
	private int mMode;
	private AudioPlayer.OnTimestampListener mTimestampListener;
	public static final int MODE_INFO = 1;
	public static final int MODE_END = 2;
	public static final int MODE_MENU = 3;

	private AdView layoutAds1;
	private AdView layoutAds2;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getWindow().addFlags(WindowManager.LayoutParams.PREVENT_POWER_KEY);
		super.onCreate(savedInstanceState);
		Log.i(TAG, "true");
		// ExceptionHandler.register(getApplicationContext(),
		// "http://crashes.vv-master.com/android-remote-stacktrace/server.php",
		// "crashes", "!androidteam!");
		int startPage;
		if (savedInstanceState != null) {
			mMode = savedInstanceState.getInt(EXTRA_MODE, MODE_READ_MYSELF);
			startPage = savedInstanceState.getInt(EXTRA_START_PAGE, 0);
		} else {
			Intent intent = getIntent();
			mMode = intent.getIntExtra(EXTRA_MODE, MODE_READ_MYSELF);
			startPage = intent.getIntExtra(EXTRA_START_PAGE, 0);
		}

		// Run as full-screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// getWindow().addFlags(WindowManager.LayoutParams.PREVENT_POWER_KEY);

		setContentView(R.layout.story_action);

		mTimestampListener = new AudioPlayer.OnTimestampListener() {
			@Override
			public void onEvent(int time, int index) {
				// Log.d(TAG, "onEvent for timestamp: time - " + time +
				// " index: " + index);
				if (mPageCurl != null)
					mPageCurl.setHighlightedWordsCount(index);
			}
		};

		mPagesManager = new PagesManager(this);

		final AudioPlayer player = AudioPlayer.getInstance(this);

		mPageCurl = (PageCurlView) findViewById(R.id.dcgpagecurlPageCurlView1);
		createWakeLock();
		mPageCurl.setPagesManager(mPagesManager, startPage);
		mPageCurl.setOnPageChangedListener(new PageCurlView.OnPageChangedListener() {
			@Override
			public void onChange(int foreground, int background) {
				Log.d(TAG, "page onChange: foreground: " + foreground + " background: " + background);
				if (foreground == 4 ||foreground == 8 || foreground >= 26) {
					layoutAds1.setVisibility(View.VISIBLE);
					layoutAds2.setVisibility(View.GONE);
				} else if(foreground == 12 || foreground == 16 || foreground == 20 ){
					layoutAds1.setVisibility(View.GONE);
					layoutAds2.setVisibility(View.GONE);
				}else {
					layoutAds1.setVisibility(View.GONE);
					layoutAds2.setVisibility(View.VISIBLE);
				}
				final PageData pageData = mPagesManager.getPage(foreground);
				if (!pageData.mIsZoom)
					player.playPageFlip();
				switch (mMode) {
				case MODE_READ_TO_ME_ORIGINAL:
				case MODE_READ_TO_ME_MYOWN: {
					startPageReading(foreground);
					break;
				}
				case MODE_RECORD: {
					stopPlayingInRecordMode();
					saveRecording();
					resetRecording();
				}
				}
			}

			@Override
			public void onEnd() {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(StoryActionActivity.this,
				// InfoPageActivity.class);
				// intent.putExtra("modeIntent", MODE_END);
				// startActivityForResult(intent,
				// StoryActionActivity.INFO_ACTIVITY);
			}

			@Override
			public void onTab(int idSound) {
				// TODO Auto-generated method stub
				player.playSoundTap(idSound);
			}

			@Override
			public void onNextZoom() {
				// TODO Auto-generated method stub
				AudioPlayer.getInstance(StoryActionActivity.this).stop();
				player.playPageFlip();

			}
		});

		LinearLayout recordDim = (LinearLayout) findViewById(R.id.recordDim);
		if (mMode == MODE_RECORD) {
			recordDim.setVisibility(View.VISIBLE);
			final CheckBox btn_record = (CheckBox) findViewById(R.id.btn_record);
			final CheckBox btn_play = (CheckBox) findViewById(R.id.btn_play);
			final Button btn_saveRecord = (Button) findViewById(R.id.btn_saveRecord);
			final Button btn_deleteRecord = (Button) findViewById(R.id.btn_deleteRecord);
			btn_record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
					if (b) {
						stopPlayingInRecordMode();
						btn_play.setEnabled(false);
						player.startRecording(mPagesManager.getPage(mPageCurl.getCurrentPageIndex()).mPageId);
					} else {
						player.stopRecording();
						setRecordControls(RecordControlsState.STATE_RECORD_FINISHED);
					}
				}
			});
			btn_play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
					if (b) {
						player.playPage(mPagesManager.getPage(mPageCurl.getCurrentPageIndex()).mPageId, true);
					} else {
						player.stop();
					}
				}
			});
			btn_saveRecord.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					saveRecording();
					setRecordControls(RecordControlsState.STATE_RECORD_SAVED);
				}
			});
			btn_deleteRecord.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					resetRecording();
				}
			});
			setRecordControls(RecordControlsState.STATE_TO_RECORD);
		} else {
			recordDim.setVisibility(View.INVISIBLE);
		}

		layoutAds1 = (AdView) this.findViewById(R.id.first_adView);
		AdRequest adRequest = new AdRequest();
		adRequest.setTesting(true);
		layoutAds1.loadAd(adRequest);
		layoutAds1.bringToFront();

		layoutAds2 = (AdView) this.findViewById(R.id.second_adView);
		AdRequest adRequest2 = new AdRequest();
		adRequest2.setTesting(true);
		layoutAds2.loadAd(adRequest);
		layoutAds2.bringToFront();
	}

	protected WakeLock wakeLock = null;

	protected void createWakeLock() {
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "lazy wakelock");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt(EXTRA_MODE, mMode);
		savedInstanceState.putInt(EXTRA_START_PAGE, mPageCurl.getCurrentPageIndex());
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, PagesManager.FLURRY_KEY);
	}

	@Override
	public void onResume() {
		if (wakeLock != null)
			wakeLock.acquire();
		super.onResume();
		switch (mMode) {
		case MODE_READ_TO_ME_ORIGINAL:
		case MODE_READ_TO_ME_MYOWN: {
			startPageReading(mPageCurl.getCurrentPageIndex());
			break;
		}
		case MODE_RECORD: {
			resetRecording();
			break;
		}
		}
	}

	@Override
	public void onPause() {
		if (wakeLock != null)
			wakeLock.release();
		super.onPause();
		AudioPlayer.getInstance(this).stop();

		if (mMode != MODE_RECORD) {
			saveLastReadPage();
		} else {
			saveRecording();
			resetRecording();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	public void onDestroy() {
		System.gc();
		mPageCurl.recycle();
		super.onDestroy();
		mPageCurl.setOnPageChangedListener(null);
		AudioPlayer.getInstance(this).setTimestampListener(null, null);
		mPageCurl = null;

		mPagesManager = null;
		mTimestampListener = null;
	}

	public static Typeface getTypeface(Context context, String typeface) {
		if (mPapyrus == null)
			mPapyrus = Typeface.createFromAsset(context.getAssets(), "fonts/Papyrus.ttf");
		if (mMyriadPro == null)
			mMyriadPro = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Regular.ttf");
		if (mTimes == null)
			mTimes = Typeface.createFromAsset(context.getAssets(), "fonts/times.ttf");
		if (typeface.equals("times"))
			return mTimes;
		else
			return typeface.equals("MyriadPro-Regular") ? mMyriadPro : mPapyrus;
	}

	public static void startIt(Activity caller, int mode, int startPage) {
		Intent intent = new Intent(caller, StoryActionActivity.class);
		intent.putExtra(EXTRA_MODE, mode);
		intent.putExtra(EXTRA_START_PAGE, startPage);
		caller.startActivity(intent);
	}

	private void startPageReading(int id) {
		final AudioPlayer player = AudioPlayer.getInstance(this);

		final PageData pageData = mPagesManager.getPage(id);
		player.setTimestampListener(pageData.mTimeStamps, mTimestampListener);
		player.playPage(pageData.mPageId, mMode == MODE_READ_TO_ME_MYOWN);
	}

	// private void playPage(int idSound) {
	// final AudioPlayer player = AudioPlayer.getInstance(this);
	// player.playSoundTap(idSound);
	// }

	private void setRecordControls(RecordControlsState state) {
		final CheckBox btn_record = (CheckBox) findViewById(R.id.btn_record);
		final CheckBox btn_play = (CheckBox) findViewById(R.id.btn_play);
		final Button btn_saveRecord = (Button) findViewById(R.id.btn_saveRecord);
		final Button btn_deleteRecord = (Button) findViewById(R.id.btn_deleteRecord);
		switch (state) {
		case STATE_RECORD_FINISHED: {
			btn_record.setVisibility(View.GONE);
			btn_play.setChecked(false);
			btn_play.setEnabled(true);
			btn_play.setVisibility(View.VISIBLE);
			btn_saveRecord.setVisibility(View.VISIBLE);
			btn_deleteRecord.setVisibility(View.VISIBLE);
			break;
		}
		case STATE_RECORD_SAVED: {
			btn_record.setChecked(false);
			btn_record.setVisibility(View.VISIBLE);
			btn_play.setChecked(false);
			btn_play.setEnabled(true);
			btn_play.setVisibility(View.VISIBLE);
			btn_saveRecord.setVisibility(View.GONE);
			btn_deleteRecord.setVisibility(View.GONE);
			break;
		}
		case STATE_TO_RECORD: {
			btn_record.setChecked(false);
			btn_record.setVisibility(View.VISIBLE);
			btn_play.setVisibility(View.GONE);
			btn_saveRecord.setVisibility(View.GONE);
			btn_deleteRecord.setVisibility(View.GONE);
			break;
		}
		}

	}

	private void stopPlayingInRecordMode() {
		final AudioPlayer player = AudioPlayer.getInstance(this);
		player.stop();
		final CheckBox btn_play = (CheckBox) findViewById(R.id.btn_play);
		btn_play.setChecked(false);
	}

	private void saveRecording() {
		final AudioPlayer player = AudioPlayer.getInstance(this);
		player.saveRecording();
	}

	private void resetRecording() {
		final AudioPlayer player = AudioPlayer.getInstance(this);

		player.deleteRecording();
		final PageData pageData = mPagesManager.getPage(mPageCurl.getCurrentPageIndex());
		player.setTimestampListener(pageData.mTimeStamps, mTimestampListener);
		if (player.hasOwnRecording(pageData.mPageId)) {
			setRecordControls(RecordControlsState.STATE_RECORD_SAVED);
		} else {
			setRecordControls(RecordControlsState.STATE_TO_RECORD);
		}
	}

	private void saveLastReadPage() {
		SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, 0);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(PREF_LAST_READ_PAGE, mPageCurl.getCurrentPageIndex());
		editor.commit();
	}

	public static int getLastReadPage(Context ctx) {
		SharedPreferences preferences = ctx.getSharedPreferences(SHARED_PREFS, 0);
		return preferences.getInt(PREF_LAST_READ_PAGE, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		// if (requestCode == INFO_ACTIVITY) {
		// if (resultCode == InfoPageActivity.FINISH) {
		// finish();
		// }
		// }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(StoryActionActivity.this, MenuActivity.class);
			startActivity(i);
			finish();
			return true;

		}
		if (keyCode == KeyEvent.KEYCODE_POWER) {
			event.startTracking(); 
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_CALL) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_POWER) {
			// Do something here...
			return true;
		}
		return super.onKeyLongPress(keyCode, event);
	}

}

// class ReceiverScreen extends BroadcastReceiver {
//
// public static boolean wasScreenOn = true;
//
// @Override
// public void onReceive(Context context, Intent intent) {
// if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
// // do whatever you need to do here
// wasScreenOn = false;
// } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
// // and do whatever you need to do here
// wasScreenOn = true;
// }
// }
// }
