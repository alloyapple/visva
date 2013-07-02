package com.lemon.fromangle.service;

import java.io.IOException;
import java.util.List;
import com.lemon.fromangle.ValidateScreenActivity;
import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("Wakelock")
public class MessageFollowService extends Service {
	private FromAngleSharedPref mPref;
	private Vibrator v;
	private MediaPlayer mMediaPlayer;
	private int countTimer = 0;

	@Override
	public void onCreate() {

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// @Override
	// public void onTaskRemoved(Intent rootIntent) {
	// // TODO Auto-generated method stub
	// Log.e("onTaskRemoved", "onTaskRemoved");
	// super.onTaskRemoved(rootIntent);
	// }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mPref = new FromAngleSharedPref(this);
		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE); 
        KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
		mMediaPlayer = new MediaPlayer();
		mPref.setFirstTimeSetting(false);
		mPref.setStopAlarm(false);
		mPref.putModeDestroyedService(GlobalValue.KEY_DESTROYED_SERVICE_BY_FORCE_CLOSE);
		if (isApplicationSentToBackground(this))
			mPref.setRunOnBackGround(true);
		else
			mPref.setRunOnBackGround(false);

		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// if (!StringUtility.isEmpty(mPref.getUserId())) {
		Log.e("rin " + mPref.getVibrateMode(),
				"run here " + mPref.getStopAlarm());
		
		if (!mPref.getStartService()) {
			Intent intentValidation = new Intent(MessageFollowService.this,
					ValidateScreenActivity.class);
			intentValidation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentValidation.putExtra(GlobalValue.IS_RUN_FROM_ACTIVITY, false);
			startActivity(intentValidation);
		}
		
		playRingTone(mPref.getRingTuneFile());
		new CountDownTimer(30000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				Log.e("onFinish serrverce",
						"on finish service1 " + mPref.getStopAlarm());
				countTimer++;
				if (mPref.getVibrateMode() && !mPref.getStopAlarm()) {
					// Vibrate for 500 milliseconds
					mPref.setRunFromActivity(false);
					if (v != null && countTimer % 3 == 0)
						v.vibrate(1500);
				}
				if (mPref.getStopAlarm()) {
					if (v != null)
						v.cancel();
					v = null;
					if (mMediaPlayer.isLooping() && mMediaPlayer.isPlaying())
						mMediaPlayer.stop();
					onFinish();
					// MessageFollowService.this.onDestroy();
				}
			}

			@Override
			public void onFinish() {
				Log.e("onFinish serrverce", "on finish service");
				countTimer = 0;
				if (mMediaPlayer.isLooping() && mMediaPlayer.isPlaying())
					mMediaPlayer.stop();
				mPref.setStartService(false);
				mPref.setStopAlarm(false);
				mPref.setRunFromActivity(true);
				mPref.putModeDestroyedService(GlobalValue.KEY_DESTROYED_SERVICE_BY_ON_FINISH);
				MessageFollowService.this.onDestroy();
			};
		}.start();

		wakeLock.release();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {

		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);

	}

	public void playRingTone(String uriRingtune) {
		Log.e("uriRingtone", "uriRingtone " + uriRingtune);
		Uri uri = Uri.parse(uriRingtune);
		if (!mPref.getStartService()) {
			// ringtone = RingtoneManager.getRingtone(MessageFollowService.this,
			// uri);
			// if (ringtone != null)
			// ringtone.play();
			mMediaPlayer = new MediaPlayer();
			try {
				mMediaPlayer.setDataSource(this, uri);
				final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

				if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
					switch (audioManager.getRingerMode()) {
					case AudioManager.RINGER_MODE_SILENT:
						Log.i("silent", "ok");
						mMediaPlayer.setVolume(0, 0);
						break;
					case AudioManager.RINGER_MODE_VIBRATE:
						Log.i("vibrate", "ok");
						mMediaPlayer.setVolume(0, 0);
						break;
					case AudioManager.RINGER_MODE_NORMAL:
						Log.i("normal", "ok");
						mMediaPlayer.setVolume(100, 100);
						break;
					}
					mMediaPlayer.setLooping(true);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
					mPref.setStartService(true);
					mPref.setRunFromActivity(false);
					Log.i("play", "ok");
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			mPref.setStartService(false);
		}
	}

	/*
	 * Check app is running (in background)
	 */
	private boolean isApplicationSentToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}
