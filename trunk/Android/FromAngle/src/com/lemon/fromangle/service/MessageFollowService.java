package com.lemon.fromangle.service;

import com.lemon.fromangle.ValidateScreenActivity;
import com.lemon.fromangle.config.FromAngleSharedPref;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.widget.Toast;

public class MessageFollowService extends Service {
	private FromAngleSharedPref mPref;
	public Ringtone ringtone;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate() {

		// TODO Auto-generated method stub

		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
				.show();
		mPref = new FromAngleSharedPref(this);
	}

	@Override
	public IBinder onBind(Intent intent) {

		// TODO Auto-generated method stub

		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG)
				.show();

		return null;

	}

	@Override
	public void onDestroy() {

		// TODO Auto-generated method stub

		super.onDestroy();

		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG)
				.show();

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

		// TODO Auto-generated method stub

		super.onStart(intent, startId);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		PowerManager pm = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();
		wl.release();
		playRingTone(mPref.getRingTuneFile());

		new CountDownTimer(30000, 4000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if (mPref.getVibrateMode()) {
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					// Vibrate for 500 milliseconds
					v.vibrate(1000);
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				ringtone.stop();
				MessageFollowService.this.onDestroy();
			};
		}.start();

		mPref.setRunFromActivity(false);
		Intent intentValidation = new Intent(this, ValidateScreenActivity.class);
		intentValidation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intentValidation);
		
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
		ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
		ringtone.play();
	}
}
