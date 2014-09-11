package com.fgsecure.ujoolt.app.screen;

import static com.fgsecure.ujoolt.app.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.fgsecure.ujoolt.app.CommonUtilities;
import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.camera.other.VideoRecodingActivity;
import com.fgsecure.ujoolt.app.info.UjooltSharedPreferences;
import com.fgsecure.ujoolt.app.utillity.DeviceConfig;
import com.fgsecure.ujoolt.app.utillity.Language;
import com.fgsecure.ujoolt.app.utillity.NetworkUtility;
import com.google.android.gcm.GCMRegistrar;

public class SplashScreen extends Activity implements LocationListener {
	protected int splashTime = 2000;
	public static boolean isStart;
	public String status;
	public static final int GPS = 0;
	private LocationManager locationManager;
	public boolean isTurnOnGPS;
	private final String TAG = getClass().getSimpleName();

	@Override
	protected void onPostResume() {
		super.onPostResume();
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
		Intent intent = new Intent(this, SplashScreen.class);
		startActivity(intent);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_splash);

		UjooltSharedPreferences ujooltSharedPreferences = new UjooltSharedPreferences(this);
		Language.setLanguage(ujooltSharedPreferences.getLanguage());

		NetworkUtility.getInstance(getBaseContext()).turnNetWorkLocationOn(this);

		if (NetworkUtility.getInstance(getBaseContext()).isNetworkAvailable()) {

			// Set up location manager for determining present location of phone
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			// Check GPS
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1, this);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 1.0f, this);
			isTurnOnGPS = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isTurnOnGPS) {
				showDialogRequestGPS();
				registerGCM();
				DeviceConfig.getDeviceInfo(this);
				isStart = true;
			} else {
				splashTread.start();
			}

		} else {
			showDialogRequestNetwork();
		}

	}

	public static boolean isNetworkAvailable(Context context) {
		boolean outcome = false;

		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

			for (NetworkInfo tempNetworkInfo : networkInfos) {

				/**
				 * Can also check if the user is in roaming
				 */
				if (tempNetworkInfo.isConnected()) {
					outcome = true;
					break;
				}
			}
		}

		return outcome;
	}

	private Thread splashTread = new Thread() {
		@Override
		public void run() {
			try {
				int waited = 0;
				while (waited < splashTime) {
					sleep(100);
					waited += 100;
				}
			} catch (InterruptedException e) {
			} finally {

				finish();
				startActivity(new Intent(SplashScreen.this.getBaseContext(),
						MainScreenActivity.class));

				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
				interrupt();
			}
		}
	};

	public void registerGCM() {
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("") || regId == null) {
			// replace this with the project ID
			GCMRegistrar.register(SplashScreen.this, SENDER_ID);
			Log.e(TAG, "new regId: " + regId);

		} else {
			CommonUtilities.REGISTRATION_ID = regId;
			Log.d(TAG, "already registered as id:" + regId);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerGCM();
	}

	private void showDialogRequestGPS() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Language.confirmTurnOnGPS).setCancelable(false).setTitle("Alert")
				.setPositiveButton(Language.cancel, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						finish();
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						startActivity(intent);
					}
				}).setNegativeButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showDialogRequestNetwork() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Language.confirmTurnOnNetwork).setCancelable(false).setTitle("Alert")
				.setPositiveButton(Language.cancel, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						finish();
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						startActivity(intent);
					}
				}).setNegativeButton("OK", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						Intent settings = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
						settings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(settings);
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == GPS) {
				finish();
				startActivity(new Intent(SplashScreen.this.getBaseContext(),
						MainScreenActivity.class));
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}