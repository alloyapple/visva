package vsvteam.outsource.leanappandroid.activity.home;


import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.R.anim;
import vsvteam.outsource.leanappandroid.R.id;
import vsvteam.outsource.leanappandroid.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashScreen extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 5000;
	public static boolean isStart;
	public String status;
	public static final int GPS = 0;
	public boolean isTurnOnGPS;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		setContentView(R.layout.page_splash_screen);

		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					LinearLayout mlayout = (LinearLayout) findViewById(R.id.splash);
					Animation slide = AnimationUtils.loadAnimation(SplashScreen.this,
							R.anim.splash_anim);
					mlayout.startAnimation(slide);
					while (_active && (waited < _splashTime)) {
						if (waited == 0) {

						}
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {

					finish();
					startActivity(new Intent(SplashScreen.this.getBaseContext(),
							HomeActivity.class));

					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
					interrupt();
				}
			}
		};
		splashThread.start();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
	// _active = false;
	// }
	// return true;
	// }

}