package com.visva.android.flashlight.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.visva.android.flashlight.adapter.ShareAdapter;
import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.common.OnShakeListener;
import com.visva.android.flashlight.common.ShakedListener;
import com.visva.android.flashlight.utilities.CameraUtilities;
import com.visva.android.flashlight.utilities.HardwareUtilities;
import com.visva.android.flashlight.utilities.LEDUtilities;
import com.visva.android.flashlight.utilities.PreferenceUtilities;
import com.visva.android.flashlight.utilities.ShareUtilities;
import com.visva.android.flashlight.utilities.SoundUtilities;
import com.visva.android.flashlightmaster.R;

public class BaseActivity extends Activity implements Key {

	public static PreferenceUtilities _preferenceUtilities;
	public static SoundUtilities _soundUtilities;
	public static ShareUtilities _shareUtilities;

	public AlertDialog _dlgQuit;   
	public static LayoutInflater _inflater;
	public static ListView _lvShare;
	public static ShareAdapter _shareAdapter;
	public AlertDialog _dlgShare;

	public static ShakedListener _shakedListener = null;
	public static KeyguardManager _keyguardManager = null;

	public static PowerManager _powerManager = null;
	public static PowerManager.WakeLock _wakeLock = null;

	public static Timer _timer;
	public static MyTimerTask _timerTask;

	public BroadcastReceiver _screenReceiver = null;
	public IntentFilter _filter = null;;

	public static Context _context;
	public static Handler _handler;

	public static boolean _screenOn = false;
	public static boolean _runCountDown = false;
	public static boolean _ledActivated = false;

	public static long _elapsed = 0;
	public static long _timeOut = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (this.getClass() != LEDLightActivity.class && this.getClass() != MorseCodeActivity.class
				&& this.getClass() != StrobeLightActivity.class && this.getClass() != FeatureActivity.class) {

			CameraUtilities.turnOffFlashLight();
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		create();
		createQuitDialog();
		createShareDialog();
	}

	@Override
	protected void onResume() {
		super.onResume();
		_wakeLock.acquire();
		_shakedListener.resume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		_shakedListener.pause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		_wakeLock.release();
	}

	@Override
	protected void onDestroy() {
		if (_screenReceiver != null) {
			unregisterReceiver(_screenReceiver);
			_screenReceiver = null;
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnuSettings:
			StrobeLightActivity.cancelFlash();
			MorseCodeActivity.cancelMorseCode();
			LEDLightActivity.cancelLed();
			switchToActivity(SETTING, SettingActivity.class);
			finish();
			break;
		case R.id.mnuShare:
			_dlgShare.show();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// get key ask to quit
			if (_preferenceUtilities.isAskonQuit())
				_dlgQuit.show();
			else {
				_preferenceUtilities.setLedLightTurnOn(false);
				_preferenceUtilities.setScreenLightTurnOn(false);
				_ledActivated = false;
				StrobeLightActivity.cancelFlash();
				LEDLightActivity.cancelLed();
				MorseCodeActivity.cancelMorseCode();
				finish();
			}
		}

		return false;
	}

	public void switchToActivity(Class<?> activityClass) {
		Intent __intent = new Intent(this, activityClass);
		this.startActivity(__intent);
	}

	public void switchToActivity(int selected, Class<?> activityClass) {
		_preferenceUtilities.setScreenSelected(selected);
		Intent __intent = new Intent(this, activityClass);
		this.startActivity(__intent);
	}

	public void flipOut(TextView tv) {
		AlphaAnimation __flipOut = new AlphaAnimation(1.0f, 0.0f);
		__flipOut.setDuration(1000);
		tv.startAnimation(__flipOut);
		tv.setVisibility(View.INVISIBLE);
	}

	public void flipIn(TextView tv) {
		AlphaAnimation __flipIn = new AlphaAnimation(0.90f, 1.0f);
		__flipIn.setDuration(1000);
		tv.startAnimation(__flipIn);
	}

	public void flip(final TextView tv) {
		_handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				flipIn(tv);
				flipOut(tv);
			}
		}, 2000);

	}

	public OnClickListener _btnFeature_Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switchToActivity(FeatureActivity.class);
			finish();
		}
	};

	public DialogInterface.OnClickListener positiveButton_Click = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			_preferenceUtilities.setScreenSelected(SCREEN_LIGHT);
			_preferenceUtilities.setLedLightTurnOn(false);
			_preferenceUtilities.setScreenLightTurnOn(false);
			_ledActivated = false;
			StrobeLightActivity.cancelFlash();
			LEDLightActivity.cancelLed();
			MorseCodeActivity.cancelMorseCode();
			_dlgQuit.dismiss();
			finish();
		}
	};

	public DialogInterface.OnClickListener negetiveButton_Click = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			_dlgQuit.dismiss();
		}
	};

	public void create() {

		if (_filter == null || _screenReceiver == null) {
			_screenReceiver = new ScreenReceiver();
			_filter = new IntentFilter();
			_filter.addAction(Intent.ACTION_SCREEN_OFF);
			_filter.addAction(Intent.ACTION_SCREEN_ON);

			registerReceiver(_screenReceiver, _filter);
		}

		if (_preferenceUtilities == null) {
			_preferenceUtilities = new PreferenceUtilities(this);
		}

		if (_inflater == null) {
			_inflater = LayoutInflater.from(this);
		}

		_soundUtilities = new SoundUtilities(this, _preferenceUtilities);

		if (_shareUtilities == null) {
			_shareUtilities = new ShareUtilities(this);
		}

		if (_shakedListener == null) {
			_shakedListener = new ShakedListener(this);
			_shakedListener.setPreferenceUtilities(_preferenceUtilities);
			_shakedListener.setCheckShakeCount(Math.abs(_preferenceUtilities.getLedShakeSensitivity() - 9));
			_shakedListener.setOnShakeListener(_onShakeListener);
		}

		if (_keyguardManager == null) {
			_keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		}

		if (_powerManager == null) {
			_powerManager = (PowerManager) getSystemService(POWER_SERVICE);
		}

		if (_wakeLock == null) {
			_wakeLock = _powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyTag");
		}

		if (_context == null) {
			_context = this;
		}

		if (_handler == null) {
			_handler = new Handler();
		}
	}

	public void createShareDialog() {
		View __viewShare = _inflater.inflate(R.layout.list_view_share, null);
		_lvShare = (ListView) __viewShare.findViewById(R.id.lvShare);
		_shareAdapter = new ShareAdapter(this);
		_lvShare.setAdapter(_shareAdapter);
		_lvShare.setOnItemClickListener(_itemShare_Click);
		_dlgShare = new AlertDialog.Builder(this).setTitle("Share").setIcon(R.drawable.share).setView(__viewShare)
				.create();
	}

	public void createQuitDialog() {
		_dlgQuit = new AlertDialog.Builder(this).setTitle("Are you sure?")
				.setMessage("Are you sure you want to quit Tiny Flashlight?")
				.setPositiveButton("Yes", positiveButton_Click).setNegativeButton("No", negetiveButton_Click).create();
	}

	public OnItemClickListener _itemShare_Click = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			switch (position) {
			case 0:
				// facebook
				_shareUtilities.shareFacebook();
				break;
			case 1:
				// twiter
				_shareUtilities.shareTwitter();
				break;
			case 2:
				// google+
				_shareUtilities.shareGooglePlus();
				break;
			case 3:
				// email
				_shareUtilities.sendMail(new String[] { "" }, new String[] { "" }, ShareUtilities.SUBJECT,
						ShareUtilities.BODY);
				break;
			}
			_dlgShare.dismiss();
		}
	};

	public static OnShakeListener _onShakeListener = new OnShakeListener() {

		@Override
		public void onShake() {
			if (_preferenceUtilities.isLedSharkOnLockScreen()) {
				if (_screenOn && _keyguardManager.inKeyguardRestrictedInputMode()) {

					turnOnOffFlashLight();

					if (_context != null) {
						HardwareUtilities.vibrate(_context, 300);
					}

					if (_ledActivated) {
						int p = _preferenceUtilities.getLedShakeLightTimeOut();
						if (p == POSITION_LIGHT_TIMER_UNCHECK) return;

						// start timer
						_timeOut = LIGHT_TIMEOUT[p];
						_timer = new Timer();
						_timerTask = new MyTimerTask();
						_timer.schedule(_timerTask, INTERVAL, INTERVAL);
						_runCountDown = true;
					} else {
						// stop timer
						if (_runCountDown) {
							_timerTask.cancel();
							_timer.cancel();
							_runCountDown = false;
							_elapsed = 0;
							_timer = null;
							_timerTask = null;

						}
					}// end if _ledActivated
				}
			}
		}
	};

	/**
	 * Method use to turn on flash light in origin thread
	 */
	public static void turnOnFlashLightBackground() {
		// runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// if (!_ledActivated) return;
		// turnFlashLight();
		// }
		// });

		_handler.post(new Runnable() {

			@Override
			public void run() {
				if (!_ledActivated) return;
				turnOnOffFlashLight();
			}
		});
	}

	/**
	 * Method used to control flash light
	 */
	public static void turnOnOffFlashLight() {
		if (!_ledActivated) {
			turnOnFlashLight();
			_ledActivated = true;
		} else {
			turnOffFlashLight();
			_ledActivated = false;
		}

		if (_context != null) {
			if (_preferenceUtilities.isLedVibration()) {
				HardwareUtilities.vibrate(_context, 50);

			}
		}
		_preferenceUtilities.setLedLightTurnOn(_ledActivated);
	}

	/**
	 * Method use to turn on flash light
	 */
	private static void turnOnFlashLight() {
		if (LEDUtilities.isSupported()) {
			LEDUtilities.turnOn();
		} else {
			CameraUtilities.turnOnFlashLight();
		}
	}

	/**
	 * Method use to turn off flash light
	 */
	private static void turnOffFlashLight() {
		if (LEDUtilities.isSupported()) {
			LEDUtilities.turnOff();
		} else {
			CameraUtilities.turnOffFlashLight();
		}
	}

	/**
	 * Broadcast to check screen on or screen off
	 */
	class ScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				_screenOn = false;
			} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
				_screenOn = true;
			}

		}
	}

	/**
	 * Timer-task for timer run
	 */
	static class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			_elapsed += INTERVAL;
			if (_elapsed >= _timeOut || !_keyguardManager.inKeyguardRestrictedInputMode()) {
				turnOnFlashLightBackground();
				_timerTask.cancel();
				_timer.cancel();
				_timer = null;
				_timerTask = null;
				_elapsed = 0;
				_runCountDown = false;
				return;
			}
		}

	}
}
