package vn.com.shoppie.activity;

import java.util.List;

import org.apache.http.NameValuePair;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.NetworkUtility;
import vn.com.shoppie.util.SUtil;
import vn.com.shoppie.util.WakeLocker;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.model.LatLng;

public class LoginActivity extends ActivityShoppie {

	private final String PENDING_ACTION_BUNDLE_KEY = "com.visva.android.shoppieandroid:PendingAction";
	private static final int REQ_NETWORK = 1;
	public static final String ERR_BLUETOOTH_NULL = "-2";
	public static final String ERR_UNKNOWN = "-1";
	public static final String ERR_SERVER = "-3";
	public static final String ERR_GCM = "-4";
	public static final String ERR_STILL_REGISTER = "-5";
	public static final String ERR_DATABASE_ERR = "-6";
	// private Button postStatusUpdateButton;
	// private Button postPhotoButton;
	// private Button pickFriendsButton;
	// private Button pickPlaceButton;
	private LoginButton loginFBButton;
	private Button loginSPButton;
	private PendingAction pendingAction = PendingAction.NONE;
	private GraphUser user;

	LocationManager mLocaMng;
	private EditText mEditTextName;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private UiLifecycleHelper uiHelper;

	public static String regId;
	private String blueMac;
	private String emeil;
	private String name;
	private String email;
	private static boolean registering = false;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall,
				Exception error, Bundle data) {
			Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall,
				Bundle data) {
			Log.d("HelloFacebook", "Success!");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.page_register);
		
		loginFBButton = (LoginButton) findViewById(R.id.login_button);
		loginFBButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						Log.e("usser", "adfhd " + user);
						if (NetworkUtility.getInstance(LoginActivity.this)
								.isNetworkAvailable()) {
							LoginActivity.this.user = user;
							updateUI();
							// It's possible that we were waiting for this.user
							// to
							// be populated in order to post a
							// status update.
							handlePendingAction();
						} else {
							showToast(getString(R.string.network_unvailable));
						}
					}
				});
		mEditTextName = (EditText)findViewById(R.id.activity_register_edt_name);
		loginSPButton = (Button)findViewById(R.id.activity_register_btn_register);
		loginSPButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("adfd", "gegs");
				register();
			}
		});
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

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				GlobalValue.DISPLAY_MESSAGE_ACTION));

		retreviveGcm();
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

	@Override
	protected void onStop() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
		} catch (IllegalArgumentException e) {

		}
		super.onStop();
	}

	public void checkNetwork() {
		if (!SUtil.getInstance().isNetworkConnected(this)) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setClassName("com.android.phone",
					"com.android.phone.NetworkSetting");
			startActivityForResult(intent, REQ_NETWORK);
			return;
		}
	}

	public void onClickRegister(View v) {
		Log.e("adkjfhd", "adkjfhd");
		register();
	}

	public void createAccountByShoppie(List<NameValuePair> params) {
		AsyncHttpPost postCreateAccount = new AsyncHttpPost(this,
				new AsyncHttpResponseProcess(this) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						Log.e("response", "respone " + response);
						// Intent intent = new Intent(LoginActivity.this,
						// HomeActivity.class);
						// startActivity(intent);
						// finish();
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, params, true);
		String url = WebServiceConfig.URL_SHOPPIE_HOME
				+ WebServiceConfig.URL_REGISTER;
		postCreateAccount.execute(url);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResume();

		// Call the 'activateApp' method to log an app event for use in
		// analytics and advertising reporting. Do so in
		// the onResume methods of the primary Activities that an app may be
		// launched into.
		AppEventsLogger.activateApp(this);

		final Session session = Session.getActiveSession();
		if (session == null || session.isClosed() || !session.isOpened()) {
			uiHelper = new UiLifecycleHelper(this, callback);
		} else {
			Log.e("resume: session", "not null");
			Request request = Request.newMeRequest(session,
					new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							if (session == Session.getActiveSession()) {
								if (user != null) {
									register();
								}
							}
						}
					});
			request.executeAsync();
		}

		updateUI();
	}

	private void register() {
		// TODO Auto-generated method stub

		checkNetwork();
		// getInfo
		name = mEditTextName.getText().toString();
		if (name.equals("")) {
			showToast(getResources().getString(R.string.name_note_empty));
			return;
		}
		ActivityShoppie.myUser.custName = name;
		boolean isOn = false;
		if (regId.equals("")) {
			retreviveGcm();
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
						LoginActivity.this, true);
			}
		}
		Log.e("regid " + regId + "  " + blueMac + "  " + emeil
				+ " lat " + lat + " long " + lng, "he he name " + name);
//		return SUtilXml.getInstance().registerAccount(
//				LoginActivity.this, regId, blueMac, emeil, lat, lng,
//				name);
//		new AsyncTask<String, String, String>() {
//			boolean isOn = false;
//
//			protected void onPreExecute() {
//				// show progressbar
//				showToast("Registering...");
//			}
//
//			@Override
//			protected String doInBackground(String... params) {
//				if (registering) {
//					return ERR_STILL_REGISTER;
//				}
//				registering = true;
//				if (regId.equals("")) {
//					retreviveGcm();
//				}
//				if (regId.equals("")) {
//					return ERR_GCM;
//				}
//				String lat = ActivityShoppie.myLocation.latitude + "";
//				String lng = ActivityShoppie.myLocation.longitude + "";
//				BluetoothAdapter mAdapter = BluetoothAdapter
//						.getDefaultAdapter();
//				if (mAdapter != null && mAdapter.isEnabled()) {
//					isOn = true;
//				}
//				if (mAdapter != null) {
//					blueMac = mAdapter.getAddress();
//					while (!mAdapter.isEnabled() || blueMac == null
//							|| blueMac.equals("")) {
//
//						mAdapter.enable();
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e) {
//						}
//						blueMac = SUtil.getInstance().getBluetoothAddress(
//								LoginActivity.this, true);
//					}
//				}
//				if (blueMac == null || blueMac.equals(""))
//					return ERR_BLUETOOTH_NULL;
//				Log.e("regid " + regId + "  " + blueMac + "  " + emeil
//						+ " lat " + lat + " long " + lng, "he he name " + name);
//				return SUtilXml.getInstance().registerAccount(
//						LoginActivity.this, regId, blueMac, emeil, lat, lng,
//						name);
//			}
		
		
		
//
//			protected void onPostExecute(String result) {
//				Log.e("result " + result, "he he name ");
//				registering = false;
//				boolean startHome = true;
//				if (result.equals(ERR_UNKNOWN)) {
//					showToast("Register failed: code: " + result);
//					// tvLog.setText("i don't known this ERROR!");
//				} else if (result.equals(ERR_BLUETOOTH_NULL)) {
//					showToast("Bluetooth NULL");
//					// tvLog.setText("i can not get your bluetooth!");
//				} else if (result.equals(ERR_STILL_REGISTER)) {
//					registering = true;
//					showToast("still registering...");
//					// tvLog.setText("You're registering. Don't try more!");
//				} else if (result.equals(ERR_SERVER)) {
//					showToast("Reg faile: SERVER error");
//					// tvLog.setText("Our server having some trouble. Try later!");
//				} else if (result.equals(ERR_DATABASE_ERR)) {
//					showToast("Reg faile: DATABASE error");
//					// tvLog.setText("What did you to my DATABASE???");
//				} else {
//					SettingPreference.setFirstUse(getApplicationContext(),
//							false);
//					showToast("Register success: userId: " + result + " gcm: "
//							+ regId + " blue: " + blueMac + " emeil: " + emeil);
//					// tvLog.setText("Register success: \nuserId: " + result
//					// + " \ngcm: " + regId + " \nblue: " + blueMac +
//					// " \nemeil: " + emeil + " name: " + name);
//					startHome = false;
//					ActivityShoppie.myUser.custId = result;
//					ActivityShoppie.myUser.custName = name;
//					startActivity(new Intent(LoginActivity.this,
//							HomeActivity.class));
//					finish();
//				}
//				BluetoothAdapter mAdapter = BluetoothAdapter
//						.getDefaultAdapter();
//				if (!isOn) {
//					if (mAdapter != null) {
//						mAdapter.disable();
//					}
//				}
//				//
//				if (startHome) {
//					startActivity(new Intent(LoginActivity.this,
//							HomeActivity.class));
//					finish();
//				}
//
//			};
//		}.execute("");

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(LoginActivity.this)
					.setTitle(R.string.btn_cancel)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.btn_ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		updateUI();
	}

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());
		if (enableButtons && user != null) {
			showToast(user.getFirstName() + " logined ");
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		} else {
			showToast("log out");
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			// postPhoto();
			break;
		case POST_STATUS_UPDATE:
			// postStatusUpdate();
			break;
		}
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String type = intent.getExtras().getString(GlobalValue.EXTRA_TYPE);
			String pieQty = intent.getExtras().getString(
					GlobalValue.EXTRA_PIE_QTY);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			Toast.makeText(getApplicationContext(),
					"New Message: " + type + ":" + pieQty, Toast.LENGTH_LONG)
					.show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};
}
