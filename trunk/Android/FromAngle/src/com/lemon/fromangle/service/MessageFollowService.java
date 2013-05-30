package com.lemon.fromangle.service;

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
			playRingTone(mPref.getRingTuneFile());
			new CountDownTimer(30000, 2000) {
				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
					if (mPref.getVibrateMode() && !mPref.getStopAlarm()) {
						// Vibrate for 500 milliseconds
						mPref.setRunFromActivity(false);
						v.vibrate(1000);
					}
					if (mPref.getStopAlarm()) {
						if (ringtone != null)
							ringtone.stop();
						v.cancel();
						mPref.setStartService(false);
						mPref.setStopAlarm(false);
						mPref.setRunFromActivity(true);
					}
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					if (ringtone != null)
						ringtone.stop();
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
		Uri uri = Uri.parse(uriRingtune);
		if (!mPref.getStartService()) {
			Log.e("run service", "run servuce "+uriRingtune);
			ringtone = RingtoneManager.getRingtone(MessageFollowService.this,
					uri);
			if (ringtone != null)
				ringtone.play();
			Log.e("rin tom", "runsdoijff " + ringtone);
			mPref.setStartService(true);

			mPref.setRunFromActivity(false);
			Intent intentValidation = new Intent(this,
					ValidateScreenActivity.class);
			intentValidation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentValidation);
		} else {
			mPref.setStartService(false);
		}
	}

	/*
	 * Check app is running (in background)
	 */
	private static boolean isApplicationSentToBackground(final Context context) {
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
