package vn.com.shoppie.activity;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.AdapterWelcomeImage;
import vn.com.shoppie.adapter.AdapterWelcomeImage.OnWelcomeRegisterListener;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.util.SUtil;
import vn.com.shoppie.util.SUtilText;
import vn.com.shoppie.util.SUtilXml;
import vn.com.shoppie.view.pageindicator.CirclePageIndicator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.model.LatLng;

public class ActivityWelcome extends Activity implements
		OnWelcomeRegisterListener, LocationListener {
	public static final String ERR_BLUETOOTH_NULL = "-2";
	public static final String ERR_UNKNOWN = "-1";
	public static final String ERR_SERVER = "-3";
	public static final String ERR_GCM = "-4";
	public static final String ERR_STILL_REGISTER = "-5";
	public static final String ERR_DATABASE_ERR = "-6";

	// FaceBook
	UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			Log.e("Session change", session.isOpened() + "-" + state.toString());
			onSessionStateChange(session, state, exception);
		}
	};

	ViewPager mPager;
	//CirclePageIndicator mIndicator;

	AdapterWelcomeImage mAdapter;
	ArrayList<Integer> mData = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		// Facebook
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(arg0);

		setContentView(R.layout.activity_welcome);

		findViewById();

		addDataSample();

		mAdapter.setOnRegisterListener(this);

		// request Location
		mLocaMng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		try {
			mLocaMng.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
					0, 0, this);
			mLocaMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					this);
			mLocaMng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					0, 0, this);
		} catch (IllegalArgumentException e) {

		}

		Location tmpLoc = mLocaMng
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if (tmpLoc == null) {
			tmpLoc = mLocaMng
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (tmpLoc == null) {
				tmpLoc = mLocaMng
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}
		if (tmpLoc != null) {
			ActivityShoppie.myLocation = new LatLng(tmpLoc.getLatitude(),
					tmpLoc.getLongitude());
		}

		// check network
		checkNetwork();

		retreviveGcm();

		blueMac = SUtil.getInstance().getBluetoothAddress(this, false);
		emeil = SUtil.getInstance().getDeviceId(getApplicationContext());
	}

	public static String regId;
	String blueMac;
	String emeil;
	String name;
	String name64;
	String email;
	static boolean registering = false;

	public void findViewById() {
		mPager = (ViewPager) findViewById(R.id.pager);
	//	mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

		mAdapter = new AdapterWelcomeImage(this, mData);
		mPager.setAdapter(mAdapter);
		//mIndicator.setViewPager(mPager);

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// Log.e("page change", "" + arg0);
			//	mIndicator.setCurrentItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	public static int PAGE_REGISTER = 999999;

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();

		final Session session = Session.getActiveSession();
		if (session == null || session.isClosed() || !session.isOpened()) {
			uiHelper = new UiLifecycleHelper(this, callback);
		} else {
			startActivity(new Intent(this, HomeActivity.class));
			this.finish();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	public void retreviveGcm() {
		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(this);
		// writeRegId(regId, name, email);
		// Log.e("regId", regId);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, GlobalValue.SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				// Toast.makeText(getApplicationContext(),
				// "Already registered with GCM", Toast.LENGTH_LONG).show();
			} else {
			}
		}
	}

	public void addDataSample() {
		mData.clear();
		mData.add(R.drawable.slide1);
		mData.add(R.drawable.slide2);
		mData.add(R.drawable.slide3);
		mData.add(R.drawable.slide4);
		mData.add(R.drawable.slide5);
		mData.add(R.drawable.slide6);
		mData.add(PAGE_REGISTER);
		// mAdapter.notifyDataSetChanged();

		mAdapter = new AdapterWelcomeImage(this, mData);
		mPager.setAdapter(mAdapter);
		//mIndicator.setViewPager(mPager);
	}

	LocationManager mLocaMng;

	@Override
	public void btnRegisterClick(final View btnRegister, final EditText edtName) {
		checkNetwork();
		// getInfo
		name = edtName.getText().toString();
		if (name.equals("")) {
			edtName.post(new Runnable() {

				@Override
				public void run() {
					showToast(getResources()
							.getString(R.string.name_note_empty));
				}
			});

			return;
		}
		new AsyncTask<String, Void, Boolean>() {
			protected void onPreExecute() {
				edtName.setEnabled(false);
				btnRegister.setEnabled(false);
			};
			@Override
			protected Boolean doInBackground(String... params) {
				register(name);
				return false;
			}
			protected void onPostExecute(Boolean result) {
				edtName.setEnabled(true);
				btnRegister.setEnabled(true);
			};
		}.execute();

	}

	public void register(final String name) {
		ActivityShoppie.myUser.custName = name;
		name64 = new String(Base64.encode(name.getBytes(), Base64.DEFAULT));
		SettingPreference.setUserName(ActivityWelcome.this, name);
		// Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
		// Log.e("user name", name);
		new AsyncTask<String, String, String>() {
			boolean isOn = false;

			protected void onPreExecute() {
				// show progressbar
				// showToast("Ä�ang Ä‘Äƒng kÃ½...");
			}

			@Override
			protected String doInBackground(String... params) {
				if (registering) {
					return ERR_STILL_REGISTER;
				}
				registering = true;
				if (regId.equals("")) {
					retreviveGcm();
				}
				if (regId.equals("")) {
					return ERR_GCM;
				}
				String lat = ActivityShoppie.myLocation.latitude + "";
				String lng = ActivityShoppie.myLocation.longitude + "";
				BluetoothAdapter mAdapter = BluetoothAdapter
						.getDefaultAdapter();
				if (mAdapter != null && mAdapter.isEnabled()) {
					isOn = true;
				}
				if (mAdapter != null) {
					blueMac = mAdapter.getAddress();
					while (!mAdapter.isEnabled() || blueMac == null
							|| blueMac.equals("")) {

						mAdapter.enable();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
						blueMac = SUtil.getInstance().getBluetoothAddress(
								ActivityWelcome.this, false);
					}
				}
				if (blueMac == null || blueMac.equals(""))
					return ERR_BLUETOOTH_NULL;
				return SUtilXml.getInstance().registerAccount(
						ActivityWelcome.this, regId, blueMac, emeil, lat, lng,
						SUtilText.removeAccent(name));
			}

			@SuppressWarnings("static-access")
			protected void onPostExecute(String result) {
				registering = false;
				boolean startHome = true;
				if (result.equals(ERR_UNKNOWN)) {
					// showToast("Register failed: code: " + result);
					showToast("CÃ³ lá»—i xáº£y ra chÆ°a rÃµ nguyÃªn nhÃ¢n!");
					// tvLog.setText("i don't known this ERROR!");
				} else if (result.equals(ERR_BLUETOOTH_NULL)) {
					showToast("ChÃºng tÃ´i chÆ°a thá»ƒ Ä‘Äƒng kÃ½ Ä‘Æ°á»£c mÃ¡y báº¡n vá»›i há»‡ thá»‘ng!");
					// tvLog.setText("i can not get your bluetooth!");
				} else if (result.equals(ERR_STILL_REGISTER)) {
					registering = true;
					showToast("Váº«n Ä‘ang Ä‘Äƒng kÃ½. Báº¡n Ä‘á»£i trong giÃ¢y lÃ¡t...");
					// tvLog.setText("You're registering. Don't try more!");
				} else if (result.equals(ERR_SERVER)) {
					showToast("ChÆ°a káº¿t ná»‘i Ä‘Æ°á»£c vá»›i há»‡ thá»‘ng Shoppie. Báº¡n hÃ£y thá»­ láº¡i vÃ o lÃºc khÃ¡c!");
					// tvLog.setText("Our server having some trouble. Try later!");
				} else if (result.equals(ERR_DATABASE_ERR)) {
					showToast("CÃ³ váº¥n Ä‘á»� vá»� lÆ°u thÃ´ng tin cá»§a báº¡n.");
					// tvLog.setText("What did you to my DATABASE???");
				} else {
					SettingPreference.setFirstUse(getApplicationContext(),
							false);
					SettingPreference
							.setUserName(getApplicationContext(), name);
					// save data to sdcard
					SUtil.getInstance().writeRegId(getApplicationContext(),
							regId, name, blueMac);
					// showToast("Register success: userId: " + result +
					// " gcm: " + regId + " blue: " + blueMac + " emeil: " +
					// emeil);
					showToast("ChÃ o má»«ng báº¡n sá»­ dá»¥ng há»‡ thá»‘ng Shoppie!");
					// tvLog.setText("Register success: \nuserId: " + result +
					// " \ngcm: " + regId + " \nblue: " + blueMac + " \nemeil: "
					// + emeil + " name: " + name);
					startHome = false;
					startActivity(new Intent(ActivityWelcome.this,
							HomeActivity.class));
					finish();
					try {
						int userId = Integer.valueOf(result);
						SettingPreference.setUserID(getApplicationContext(),
								userId);
					} catch (NumberFormatException e) {
						onPostExecute(ERR_SERVER);
						return;
					}

					ActivityShoppie.myUser.custId = result;
					ActivityShoppie.myUser.custName = name;

				}
				BluetoothAdapter mAdapter = BluetoothAdapter
						.getDefaultAdapter();
				if (!isOn) {
					if (mAdapter != null) {
						mAdapter.disable();
					}
				}
				//
				if (startHome) {
					startActivity(new Intent(ActivityWelcome.this,
							HomeActivity.class));
					finish();
				}

			};
		}.execute("");
	}

	final int REQ_NETWORK = 1;

	public void checkNetwork() {
		if (!SUtil.getInstance().isNetworkConnected(this)) {
			showToast("Báº¡n hÃ£y báº­t máº¡ng Ä‘á»ƒ sá»­ dá»¥ng Shoppie!");
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.setClassName("com.android.phone",
			// "com.android.phone.NetworkSetting");
			// startActivityForResult(intent, REQ_NETWORK);
			return;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_NETWORK) {
			// checkNetwork();
		}
	}

	Toast mToast;

	public void showToast(String text) {
		if (mToast == null)
			mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		mToast.cancel();
		mToast.setText(text);
		mToast.show();
	}

	@Override
	public void onLocationChanged(Location location) {
		ActivityShoppie.myLocation = new LatLng(location.getLatitude(),
				location.getLongitude());
		// mLyTop.setText(location.getLatitude() + "-" +
		// location.getLongitude());
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

	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
			if (state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				Session.getActiveSession();
			} else {
				Request request = Request.newMeRequest(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								if (session == Session.getActiveSession()) {
									if (user != null) {
										String name = user.getUsername();
										register(name);
									}
								}
							}
						});
				request.executeAsync();
			}
		} else {
		}
	}
}
