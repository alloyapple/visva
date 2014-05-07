package vsvteam.outsource.android.soundeffect.receiver;

import vsvteam.outsource.android.soundeffect.util.SoundEffectSharePreference;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeadsetStateReceiver extends BroadcastReceiver {

	private NotificationManager mNotificationManager;
	private SoundEffectSharePreference soundEffectSharePreference;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean isConnected = intent.getIntExtra("state", 0) == 1; // Can also
																	// be 2 if
																	// headset
																	// is
																	// attached
																	// w/o mic.

		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		soundEffectSharePreference = SoundEffectSharePreference.getInstance(context);
		soundEffectSharePreference.putHeadSetConnectionValue(isConnected);

		if (isConnected) {
			Log.e(this.getClass().toString(), "Intent Headset_plug connected");
		} else {
			Log.e(this.getClass().toString(), "Intent Headset_plug disconnected");
			mNotificationManager.cancelAll();
		}
		Log.e("get head set conection",
				"get head set connection " + soundEffectSharePreference.getHeadSetConnectionValue());
	}

}
