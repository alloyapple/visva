package vn.zgome.game.streetknight.android;

import vn.zgome.game.streetknight.core.ISmsEvent;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * (C) 2013 ZeroTeam
 * 
 * @author QuanLT
 */

public class SmsCreate {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private ProgressDialog mProgressDialog;
	private String mSmsNumber = "";

	private Context mContext;
	private ISmsEvent mSmsEvent;
	private BroadcastReceiver mReceiver;
	private PendingIntent mIntent;

	// ===========================================================
	// Constructor
	// ===========================================================
	public SmsCreate(final Context context, final ISmsEvent smsEvent) {
		mContext = context;
		mSmsEvent = smsEvent;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setSmsEvent(final ISmsEvent smsEvent) {
		mSmsEvent = smsEvent;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void sendSMS(final String strNumber, final String strMsg) {
		mSmsNumber = strNumber;
		initProgressDialog();
		mProgressDialog.show();
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(mSmsNumber, null, strMsg,
				mIntent, null);
	}

	public void prepareSms(final boolean isPlaying) {
		final String sendSms = "SMS_SENT";
		final Intent intent = new Intent(sendSms);

		if (mIntent != null) {
			mIntent.cancel();
			mIntent = null;
		}

		mIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

		if (mReceiver != null) {
			release();
		}

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(mContext, "SMS sent", Toast.LENGTH_SHORT)
							.show();
					releaseProgressDialog();
					if (mSmsEvent != null) {
						if (!isPlaying) {
							mSmsEvent.onSendSMSSuccess();
						} else {
							mSmsEvent.onSmsAutoSuccess();
						}
					}
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(mContext, "Generic failure",
							Toast.LENGTH_SHORT).show();
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(mContext, "No service", Toast.LENGTH_SHORT)
							.show();
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(mContext, "Null PDU", Toast.LENGTH_SHORT)
							.show();
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(mContext, "Radio off", Toast.LENGTH_SHORT)
							.show();
				default:
					releaseProgressDialog();
					if (mSmsEvent != null) {
						if (!isPlaying) {
							mSmsEvent.onSendSMSFailure();
						} else {
							mSmsEvent.onSmsAutoError();
						}
					}
				}
			}
		};

		mContext.registerReceiver(mReceiver, new IntentFilter(sendSms));
	}

	protected void initProgressDialog() {
		releaseProgressDialog();
		mProgressDialog = new ProgressDialog(this.mContext);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle(null);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setMessage("Vui lòng đợi ...");
		mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				return (keyCode == 84) && (event.getRepeatCount() == 0);
			}
		});
	}

	public void releaseProgressDialog() {
		if (mProgressDialog != null) {
			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	private void release() {
		if (mReceiver != null) {
			mContext.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
