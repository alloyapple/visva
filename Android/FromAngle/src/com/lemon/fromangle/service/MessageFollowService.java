package com.lemon.fromangle.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MessageFollowService extends Service {
	private MediaPlayer mediaPlayer;

	@Override
	public void onCreate() {

		// TODO Auto-generated method stub

		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG)
				.show();
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

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, MessageFollowService.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		// After after 30 seconds
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000 * 5, pi);
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, MessageFollowService.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	@Override
	public void onStart(Intent intent, int startId) {

		// TODO Auto-generated method stub

		super.onStart(intent, startId);

		Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {

		// TODO Auto-generated method stub

		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG)
				.show();

		return super.onUnbind(intent);

	}

}
