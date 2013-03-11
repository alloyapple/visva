package visvateam.outsource.idmanager.activities.securityservice;

import visvateam.outsource.idmanager.activities.MasterPasswordActivity;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SecurityService extends Service {

	private Binder myBinder = new LocalService();
	private CountDownTimer myTimer;
	private IdManagerPreference mIdManagerPreference;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinder;
	}

	public class LocalService extends Binder {
		public SecurityService getService() {
			return SecurityService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	private void showToast(String string) {
		Toast.makeText(this, string, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * create count down timer after 10s panda snooze or cry
	 * 
	 * @return
	 */
	public CountDownTimer createMyTimer(long time) {
		return new CountDownTimer(time, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				mIdManagerPreference = IdManagerPreference.getInstance(SecurityService.this);
				if (!mIdManagerPreference.isSecurityLoop()){
					onFinish();
				}
			}

			@Override
			public void onFinish() {
				if (mIdManagerPreference.isSecurityLoop()) {
					showToast("Security mode is actived");
					Intent intentSecure = new Intent(SecurityService.this,
							SecurityMasterPasswordActivity.class);
					intentSecure.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intentSecure);
					stopSelf();
				}
			}
		};
	}

	/**
	 * set count down timer for set the animation
	 */
	public void startCountDownTimer(long time) {
		if (myTimer == null) {
			myTimer = createMyTimer(time);
		}
		myTimer.start();
	}

	/**
	 * stop my count down timer
	 */
	private void finishCountDownTimer() {

	}
}
