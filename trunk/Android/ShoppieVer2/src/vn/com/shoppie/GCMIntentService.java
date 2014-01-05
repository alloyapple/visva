package vn.com.shoppie;

import java.util.List;

import vn.com.shoppie.activity.ActivityNotification;
import vn.com.shoppie.activity.HomeActivity;
import vn.com.shoppie.activity.LoginActivity;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.smng.GcmNotifyMng;
import vn.com.shoppie.database.sobject.GcmNotify;
import vn.com.shoppie.util.CommonUtilities;
import vn.com.shoppie.util.log;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	private static ShoppieSharePref mShoppieSharePref;

	public GCMIntentService() {
		super(GlobalValue.SENDER_ID);
		mShoppieSharePref = new ShoppieSharePref(this);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		log.i(TAG, "Device registered: regId = " + registrationId);
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
		log.i(TAG, "Device unregistered");
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_unregistered));
		// ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		log.i(TAG, "Received service message");
		String message = intent.getExtras().getString(GlobalValue.EXTRA_MESSAGE);
		// Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		String type = intent.getExtras().getString(GlobalValue.EXTRA_TYPE);
		String pieQty = intent.getExtras().getString(GlobalValue.EXTRA_PIE_QTY);
		log.d("Pie", ">>>>>>>>>>>>>>>>> pie " + pieQty);
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
		log.i(TAG, "Received deleted messages notification");
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
		log.i(TAG, "Received error: " + errorId);
		Toast.makeText(this, "Received error: ", Toast.LENGTH_SHORT).show();
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		log.i(TAG, "Received recoverable error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("deprecation")
	private void generateNotification(Context context, String message,
			String type, String pieQty) {
		log.e(TAG, "GMCmessage "+message);
		String _message = message.toLowerCase();
		if(_message.contains(context.getString(R.string.message_contain_checkin1))||_message.contains(context.getString(R.string.message_contain_checkin2))||_message.contains(context.getString(R.string.message_contain_checkin3))){
			mShoppieSharePref.setCheckinStatus(1);
			openPieAnimate(Integer.parseInt(pieQty));
		}else
			mShoppieSharePref.setCheckinStatus(0);
		int icon = R.drawable.icon_launcher;
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
			log.e("SQL exception", "can not insert New gcm notify to db");
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
	
	public void openPieAnimate(int pie) {
	   MyApplication myApp = (MyApplication) getApplication();
	   if(myApp._homeActivity != null) {
		   Message msg = new Message();
		   msg.what = 0;
		   msg.arg1 = pie;
		   myApp._homeActivity.mHandler.sendMessage(msg);
	   }
	}
}
