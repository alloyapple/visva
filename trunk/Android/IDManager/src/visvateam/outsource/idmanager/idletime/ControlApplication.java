package visvateam.outsource.idmanager.idletime;

import visvateam.outsource.idmanager.database.IdManagerPreference;
import android.app.Application;
import android.util.Log;

public class ControlApplication extends Application {
	private static final String TAG = ControlApplication.class.getName();
	private Waiter waiter; // Thread which controls idle time
	private IdManagerPreference mPref;

	// only lazy initializations here!
	@Override
	public void onCreate() {
		super.onCreate();
		mPref = IdManagerPreference.getInstance(this);
		Log.d(TAG, "Starting application" + this.toString());
		if (mPref.getSecurityMode() == 0) {
			waiter = new Waiter(Integer.MAX_VALUE * 60 * 1000);
		} else {
			waiter = new Waiter(mPref.getSecurityMode() * 60 * 1000);
		} // 15 mins
		waiter.start();
	}

	public void touch() {
		waiter.touch();
	}

	public void setPeriod(long period) {
		waiter.setPeriod(period);
	}
}
