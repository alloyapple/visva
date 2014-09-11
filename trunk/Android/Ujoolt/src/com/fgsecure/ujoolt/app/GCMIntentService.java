/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fgsecure.ujoolt.app;

import static com.fgsecure.ujoolt.app.CommonUtilities.SENDER_ID;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import com.fgsecure.ujoolt.app.info.UjooltSharedPreferences;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.google.android.gcm.GCMBaseIntentService;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	// wakelock
	private static final String WAKELOCK_KEY = "GCM_LIB";
	private static PowerManager.WakeLock sWakeLock;

	// Java lock used to synchronize access to sWakelock
	private static final Object LOCK = GCMBaseIntentService.class;
	private UjooltSharedPreferences ujooltSharedPreferences;

	public GCMIntentService() {
		super(SENDER_ID);
		ujooltSharedPreferences = new UjooltSharedPreferences(this);
	}

	private void popupNotification(Context context, String message) {
		Intent intentPopup = new Intent(context, MessageBox.class);
		intentPopup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentPopup.putExtra("message", message);
		context.startActivity(intentPopup);
	}

	/*
	 * Check app is running (in background)
	 */
	private static boolean isApplicationSentToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	@Override
	protected void onError(Context arg0, String arg1) {
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		Log.d(TAG, "RECIEVED A MESSAGE");
		// Get the data from intent and send to notificaion bar
		// generateNotification(arg0, arg1.getStringExtra("message"));
		// if(u)

		Log.e(TAG, "reg: Vua nhan nofify");

		if (ujooltSharedPreferences.getPushStatus()) {
			handleMessage(arg0, arg1);
		}
	}

	public static String idPostJolt = "";

	public void handleMessage(Context context, Intent intent) {
		String message = intent.getExtras().getString("message");
		String latNotify = intent.getExtras().getString("jolt_lat");
		String longNotify = intent.getExtras().getString("jolt_long");
		String joltId = intent.getExtras().getString("jolt_id");

		Log.e("Received notification message", "Post regiter : " + message);
		Log.e("Received notification message", "Post regiter : " + intent.getExtras().toString());
		Log.e("message", "Post regiter: Lat : " + latNotify + " Long : " + longNotify + " JoltId :"
				+ joltId);

		if (isApplicationSentToBackground(context) && !joltId.equalsIgnoreCase(idPostJolt)) {

			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(1000);

			createNotification(context, message);
			popupNotification(context, message);
		}
	}

	private void createNotification(Context context, String message) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(R.drawable.icon, "" + message,
				System.currentTimeMillis());

		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(context, MainScreenActivity.class);
		intent.putExtra("registration_id", message);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, "Ujoolt", "" + message, pendingIntent);
		notificationManager.notify(0, notification);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		Log.e(TAG, "reg id: " + arg1);

		if (ujooltSharedPreferences.getRegistrationId().equalsIgnoreCase("")) {
			if (arg1.equalsIgnoreCase("") && (arg1 != null)) {
				CommonUtilities.REGISTRATION_ID = arg1;
			}
		} else {
			CommonUtilities.REGISTRATION_ID = ujooltSharedPreferences.getRegistrationId();
		}

		if (!MainScreenActivity.isSendRegistrationId && !CommonUtilities.isRegistrationNull()) {

			Log.d(TAG, "reg id: send ok!");

			MainScreenActivity mainScreenActivity = (MainScreenActivity) arg0;
			mainScreenActivity.postRegistrationId();
			MainScreenActivity.isSendRegistrationId = true;
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// TODO Auto-generated method stub
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Called from the broadcast receiver.
	 * <p>
	 * Will process the received intent, call handleMessage(), registered(),
	 * etc. in background threads, with a wake lock, while keeping the service
	 * alive.
	 */
	static void runIntentInService(Context context, Intent intent, String className) {
		synchronized (LOCK) {
			if (sWakeLock == null) {
				// This is called from BroadcastReceiver, there is no init.
				PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				sWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_KEY);
			}
		}

		Log.v(TAG, "Acquiring wakelock");
		sWakeLock.acquire();
		intent.setClassName(context, className);
		context.startService(intent);
	}
}
