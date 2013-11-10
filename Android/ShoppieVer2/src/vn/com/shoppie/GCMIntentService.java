package vn.com.shoppie;

import vn.com.shoppie.activity.ActivityNotification;
import vn.com.shoppie.activity.LoginActivity;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.database.smng.GcmNotifyMng;
import vn.com.shoppie.database.sobject.GcmNotify;
import vn.com.shoppie.util.CommonUtilities;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(GlobalValue.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		CommonUtilities.displayMessage(context,
				"Your device registred with GCM");
		LoginActivity.regId = registrationId;
		// ServerUtilities.register(context, MainActivity.name,
		// MainActivity.email, registrationId);
		// Toast.makeText(this, "services: "+registrationId,
		// Toast.LENGTH_SHORT).show();
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_unregistered));
		// ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString(GlobalValue.EXTRA_MESSAGE);
		// Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		String type = intent.getExtras().getString(GlobalValue.EXTRA_TYPE);
		String pieQty = intent.getExtras().getString(GlobalValue.EXTRA_PIE_QTY);
		sendBroastCast(context, message, type, pieQty);
		// CommonUtilities.displayMessage(context, intent);
		// notifies user
		generateNotification(context, message, type, pieQty);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		CommonUtilities.displayMessage(context, message);
		// notifies user
		generateNotification(context, "delete message" + "", "deleted", total
				+ "");
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message,
			String type, String pieQty) {
		Log.e("message", "message "+message);
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, ActivityLogo.class);
		notificationIntent.putExtra(ActivityNotification.FLAG_NOTIFI, type);
		notificationIntent.putExtra(ActivityNotification.FLAG_NOTIFI_DATA,
				pieQty);
		// set intent so it does not start a new activity
		notificationIntent
				.setFlags(/* Intent.FLAG_ACTIVITY_CLEAR_TOP | */Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		notification.sound = Uri.parse("android.resource://vn.com.shoppie" /*
																			 * +
																			 * context
																			 * .
																			 * getPackageName
																			 * (
																			 * )
																			 */
				+ "/" + R.raw.ting_ting);

		// Vibrate if vibrate is enabled
		notification.defaults = Notification.DEFAULT_LIGHTS
				| Notification.DEFAULT_VIBRATE;
		int notiId = 0;
		if (type.equals(GlobalValue.EXTRA_TYPE_CHECKIN))
			notiId = GlobalValue.NOTIFICATION_ID_CHECKIN;
		if (type.equals(GlobalValue.EXTRA_TYPE_REGISTER))
			notiId = GlobalValue.NOTIFICATION_ID_REGISTER;
		if (type.equals(GlobalValue.EXTRA_TYPE_PURCHASE))
			notiId = GlobalValue.NOTIFICATION_ID_PURCHARSE;
		notificationManager.notify(notiId, notification);

		try {
			GcmNotifyMng gcmNotifyMng = new GcmNotifyMng(context);
			GcmNotify notify = new GcmNotify(-1,
					new String[GcmNotify.NUM_FIELDS]);
			notify.message = message;
			notify.type = type;
			notify.pieQty = pieQty + "";
			notify.time = System.currentTimeMillis() + "";
			gcmNotifyMng.insertNewTo(notify.getValues());
		} catch (SQLException e) {
			Log.e("SQL exception", "can not insert New gcm notify to db");
		}
	}

	public static void sendBroastCast(Context context, String message,
			String type, String pieQty) {
		Intent intent = new Intent(GlobalValue.DISPLAY_MESSAGE_ACTION);
		// intent.setAction(GlobalValue.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(GlobalValue.EXTRA_MESSAGE, message);
		intent.putExtra(GlobalValue.EXTRA_TYPE, type);
		intent.putExtra(GlobalValue.EXTRA_PIE_QTY, pieQty);
		context.sendBroadcast(intent);
	}
}
