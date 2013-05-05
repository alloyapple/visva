package visvateam.outsource.idmanager.idletime;

import visvateam.outsource.idmanager.activities.securityservice.SecurityMasterPasswordActivity;
import visvateam.outsource.idmanager.database.IdManagerPreference;
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
		if (mPref.getSecurityMode() == 0) {
			waiter = new Waiter(Long.MAX_VALUE, this);
		} else {
			waiter = new Waiter(mPref.getSecurityMode() * 60 * 1000, this);
		} // 15 mins
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
