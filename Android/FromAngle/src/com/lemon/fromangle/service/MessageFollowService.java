package com.lemon.fromangle.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MessageFollowService extends Service {
	private MediaPlayer mediaPlayer;

	@Override
	public void onCreate() {

		// TODO Auto-generated method stub

		Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {

		// TODO Auto-generated method stub

		Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();

		return null;

	}

	@Override
	public void onDestroy() {

		// TODO Auto-generated method stub

		super.onDestroy();

		Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onStart(Intent intent, int startId) {

		// TODO Auto-generated method stub

		super.onStart(intent, startId);

		Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onUnbind(Intent intent) {

		// TODO Auto-generated method stub

		Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();

		return super.onUnbind(intent);

	}

}
