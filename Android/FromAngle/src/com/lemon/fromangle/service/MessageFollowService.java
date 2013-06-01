package com.lemon.fromangle.service;

import java.io.IOException;
import java.util.List;

import com.lemon.fromangle.ValidateScreenActivity;
import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;
import com.lemon.fromangle.utility.StringUtility;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("Wakelock")
public class MessageFollowService extends Service {
	private FromAngleSharedPref mPref;
	private Ringtone ringtone;
	private Vibrator v;
	private MediaPlayer mMediaPlayer;

	@Override
	public void onCreate() {

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void SetAlarm(Context context, long startTime, long delayTime) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		// After after 30 seconds
		am.setRepeating(AlarmManager.RTC_WAKEUP, startTime, delayTime, pi);
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mPref = new FromAngleSharedPref(this);
		PowerManager pm = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();
		mMediaPlayer = new MediaPlayer();
		mPref.setFirstTimeSetting(false);
		if (isApplicationSentToBackground(this))
			mPref.setRunOnBackGround(true);
		else
			mPref.setRunOnBackGround(false);

		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Log.e("rin2 " + mPref.getUserId(),
				"run here2" + mPref.getValidationMode());
		if (!StringUtility.isEmpty(mPref.getUserId())) {
			Log.e("rin " + mPref.getVibrateMode(),
					"run here " + mPref.getStopAlarm());
			Intent intentValidation = new Intent(this,
					ValidateScreenActivity.class);
			intentValidation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intentValidation.putExtra(GlobalValue.IS_RUN_FROM_ACTIVITY, false);
			startActivity(intentValidation);
			playRingTone(mPref.getRingTuneFile());

			new CountDownTimer(30000, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
					if (mPref.getVibrateMode() && !mPref.getStopAlarm()) {
						// Vibrate for 500 milliseconds
						mPref.setRunFromActivity(false);
						v.vibrate(1500);
					}
					if (mPref.getStopAlarm()) {
						v.cancel();
						if (mMediaPlayer.isLooping() && mMediaPlayer.isPlaying())
							mMediaPlayer.stop();
						MessageFollowService.this.onDestroy();
					}
				}

				@Override
				public void onFinish() {

					if (mMediaPlayer.isLooping() && mMediaPlayer.isPlaying())
						mMediaPlayer.stop();
					v.cancel();
					mPref.setStartService(false);
					mPref.setStopAlarm(false);
					mPref.setRunFromActivity(true);
					MessageFollowService.this.onDestroy();
				};
			}.start();
		}
		wl.release();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {

		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();
		return super.onUnbind(intent);

	}

	public void playRingTone(String uriRingtune) {
		Log.e("uriRingtone", "uriRingtone "+uriRingtune);
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
					mMediaPlayer.setLooping(true);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
					mPref.setStartService(true);
					mPref.setRunFromActivity(false);
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
