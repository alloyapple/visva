package com.japanappstudio.IDxPassword.idletime;

import com.japanappstudio.IDxPassword.activities.securityservice.SecurityMasterPasswordActivity;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class ControlApplication extends Application {
	private static final String TAG = ControlApplication.class.getName();
	private Waiter waiter; // Thread which controls idle time
	private IdManagerPreference mPref;
	private Activity activity;

	// private boolean isTimerDown;

	// only lazy initializations here!
	@Override
	public void onCreate() {
		super.onCreate();
		// setTimerDown(true);
		mPref = IdManagerPreference.getInstance(this);
		Log.d(TAG, "Starting application" + this.toString());
		waiter = new Waiter(Long.MAX_VALUE, this);
		int sercurity_mode=mPref.getSecurityMode();
		if (sercurity_mode == 0) {
			waiter.setPeriod(Long.MAX_VALUE);
		} else if (sercurity_mode == 1) {
			waiter.setPeriod(Long.MAX_VALUE);
		} else if (sercurity_mode == 2) {
			waiter.setPeriod(1 * 5 * 1000);
		} else if (sercurity_mode == 3) {
			waiter.setPeriod(3 * 60 * 1000);
		} else if (sercurity_mode == 4) {
			waiter.setPeriod(5 * 60 * 1000);
		} else if (sercurity_mode == 5) {
			waiter.setPeriod(10 * 60 * 1000);
		}
//		if (mPref.getSecurityMode() == 0) {
//			waiter = new Waiter(Long.MAX_VALUE, this);
//		} else {
//			waiter = new Waiter(mPref.getSecurityMode() * 60 * 1000, this);
//		} // 15 mins
		waiter.start();
	}

	public void touch() {
		waiter.touch();
	}

	public void setPeriod(long period) {
		waiter.setPeriod(period);
	}

	public void setIdle(long idle) {
		waiter.setIdle(idle);
		waiter.setCheck(true);
		waiter.setLastUsed();
	}

	public void stop() {
		waiter.stop(true);
	}

	public void startMasterPass() {
		Intent i = new Intent(activity, SecurityMasterPasswordActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);

	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	// public void setTimerDown(boolean isTimerDown) {
	// this.isTimerDown = isTimerDown;
	// }
	//
	// public boolean isTimerDown() {
	// return isTimerDown;
	// }
}
