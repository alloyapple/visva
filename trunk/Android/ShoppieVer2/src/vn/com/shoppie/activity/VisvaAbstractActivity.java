package vn.com.shoppie.activity;

import java.lang.reflect.Method;
import java.util.HashMap;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.util.SBroastcastProvider;
import vn.com.shoppie.util.SBroastcastProvider.BroastcastListener;
import vn.com.shoppie.util.VisvaDialog;
import vn.com.shoppie.view.PopupPie;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public abstract class VisvaAbstractActivity extends Activity implements
		BroastcastListener {
	// abstract method
	public static long LST_NOTIFY_TIME = 0;
	public static final long THRE_NOTIFY_TIME = 5000; /* 5 seconds */

	public static final String GA_HIT_TYPE_BUTTON = "button";
	public static final String GA_EVENT = "event";
	public static final String GA_EXCEPTION = "exception";
	public static final String GA_SOCIAL = "social";
	public static final String GA_VIEW = "view";
	public HashMap<String, String> GA_MAP_PARAMS = new HashMap<String, String>();
	private LinearLayout container;
	protected VisvaDialog progressDialog;
	protected VisvaAbstractActivity self;
	protected static String TAG = "";
	public String Tag = this.getClass().getName();
	// Google analysis
	protected Tracker mGaTracker;
	protected GoogleAnalytics mGaInstance;

	SBroastcastProvider broastcast = new SBroastcastProvider(this);
	private ShoppieSharePref mShopieSharePref;
	protected boolean isCheckin = false;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		self = this;
		TAG = getClass().getSimpleName();
		if (progressDialog == null) {
			try {
				progressDialog = new VisvaDialog(self);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				progressDialog = new VisvaDialog(self.getParent());
			}
			progressDialog.setCancelable(false);
		}

		try {
			Class strictModeClass = Class.forName("android.os.StrictMode");
			Class strictModeThreadPolicyClass = Class
					.forName("android.os.StrictMode$ThreadPolicy");
			Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(
					null);
			Method method_setThreadPolicy = strictModeClass.getMethod(
					"setThreadPolicy", strictModeThreadPolicyClass);
			method_setThreadPolicy.invoke(null, laxPolicy);
		} catch (Exception e) {

		}
		mShopieSharePref = new ShoppieSharePref(this);
		// Get the intent that started this Activity.
		Intent intent = this.getIntent();
		Uri uri = intent.getData();

		// Get the GoogleAnalytics singleton. Note that the SDK uses
		// the application context to avoid leaking the current context.
		mGaInstance = GoogleAnalytics.getInstance(this);

		// Use the GoogleAnalytics singleton to get a Tracker.
		mGaTracker = mGaInstance.getTracker(getString(R.string.ga_trackingId)); // Placeholder
																				// tracking
																				// ID.

		if (intent.getData() != null) {
			EasyTracker.getTracker().setCampaign(uri.getPath());
		}

		if (uri != null) {
			if (uri.getQueryParameter("utm_source") != null) {
				EasyTracker.getTracker().setCampaign(uri.getPath());
			} else if (uri.getQueryParameter("referrer") != null) {
				EasyTracker.getTracker().setReferrer(
						uri.getQueryParameter("referrer"));
			}
		}
		setOnBroastcast(this);
		registerBaseActivityReceiver();
		setContentView(R.layout.abstract_activity);
		init();
		onCreate();
	}

	private void setOnBroastcast(VisvaAbstractActivity visvaAbstractActivity) {
		// TODO Auto-generated method stub

	}

	private void init() {
		container = (LinearLayout) findViewById(R.id.container);

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(contentView(), container, false);

		container.addView(view, -1, -1);
	}

	public void onClickSearchActivity(View v) {
		gotoActivity(self, SearchActivity.class);
	}

	public void changeToActivity(Intent intent, boolean isFinish) {
		startActivity(intent);
		if (isFinish) {
			finish();
		}
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void goBack() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public abstract int contentView();

	public abstract void onCreate();

	protected void hideKeyboard() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// unRegisterBaseActivityReceiver();
	// }

	/**
	 * Open progress dialog
	 */
	public void showProgressDialog() {
		try {
			// showProgressDialog(getString(R.string.message_please_wait));
			if (progressDialog == null) {
				try {
					progressDialog = new VisvaDialog(self);
					progressDialog.show();
					progressDialog.setCancelable(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					progressDialog = new VisvaDialog(self.getParent());
					progressDialog.show();
					progressDialog.setCancelable(false);
					e.printStackTrace();
				}
			} else {
				if (!progressDialog.isShowing())
					progressDialog.show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				progressDialog = new VisvaDialog(self.getParent());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				progressDialog = new VisvaDialog(self);
				e1.printStackTrace();
			}
			progressDialog.show();
			progressDialog.setCancelable(false);
			e.printStackTrace();
		}
	}

	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog.cancel();
			progressDialog = null;
		}

	}

	public void onClickBtnSearch(View v) {
		Intent intent = new Intent(VisvaAbstractActivity.this,
				SearchActivity.class);
		startActivity(intent);
	}

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */
	public void gotoActivity(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	public void gotoActivityBySlideUp(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
	}

	public void gotoActivity(Context context, Class<?> cla, int flag) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(flag);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */
	public void gotoActivityForResult(Context context, Class<?> cla,
			int requestCode) {
		Intent intent = new Intent(context, cla);
		startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 */
	public void gotoActivity(Context context, Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 * @param requestCode
	 */
	public void gotoActivityForResult(Context context, Class<?> cla,
			Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}

	// ======================= TOAST MANAGER =======================

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param strId
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(int strId) {
		Toast.makeText(this, getString(strId), Toast.LENGTH_LONG).show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showShortToastMessage(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(String str, int time) {
		Toast.makeText(this, str, time).show();
	}

	/**
	 * @param str
	 *            : alert message
	 * 
	 *            Show toast message
	 */
	public void showToastMessage(int resId, int time) {
		Toast.makeText(this, resId, time).show();
	}

	/**
	 * Show comming soon toast message
	 */
	public void showComingSoonMessage() {
		showToastMessage("Coming soon!");
	}

	/**
	 * Goto web page
	 * 
	 * @param url
	 */
	public void gotoWebPage(String url) {

		if (!url.contains("http"))
			url = "http://" + url;
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
	}

	/**
	 * Goto phone call
	 * 
	 * @param telNumber
	 */
	protected void gotoPhoneCallPage(String telNumber) {
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
		startActivity(i);
	}

	// ========RECEIVER NOTIFICATION=============//

	public static final String FINISH_ALL_ACTIVITIES = "com.lemon.bneart.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";
	private BaseActivityReceiver baseActivityReceiver = new BaseActivityReceiver();
	public static final IntentFilter INTENT_FILTER = createIntentFilter();

	private static IntentFilter createIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(FINISH_ALL_ACTIVITIES);
		return filter;ShoppieSharePref
	}

	protected void registerBaseActivityReceiver() {
		registerReceiver(baseActivityReceiver, INTENT_FILTER);
	}

	protected void unRegisterBaseActivityReceiver() {
		unregisterReceiver(baseActivityReceiver);
	}

	public class BaseActivityReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(FINISH_ALL_ACTIVITIES)) {
				finish();
			}
		}
	}

	protected void closeAllActivities() {
		sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// LST_NOTIFY_TIME = 0;
		registerReceiver(broastcast.getBroastcast(), new IntentFilter(
				GlobalValue.DISPLAY_MESSAGE_ACTION));
		showLog();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broastcast.getBroastcast());
	}

	protected void showLog() {
		Log.i(Tag, "heap size: " + Debug.getNativeHeapSize());
		Log.i(Tag, "heap size alloced: " + Debug.getNativeHeapAllocatedSize());
		Log.i(Tag, "heap size free: " + Debug.getNativeHeapFreeSize());
	}

	// =======================UPDATE MY LOCATION==================//
	public void showToast(String text) {
		if (mToast == null)
			mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		mToast.cancel();
		mToast.setText(text);
		mToast.show();
	}

	public void setOnBroastcast(SBroastcastProvider.BroastcastListener listener) {
		broastcast.setOnBroastcastListener(listener);
	}

	protected android.app.AlertDialog dialogAlert = null;

	@Override
	public void onReceiveBroastcast(Context context, Intent intent) {
		String type = "", pieQty = "0";
		if (intent.hasExtra(GlobalValue.EXTRA_TYPE))
			type = intent.getStringExtra(GlobalValue.EXTRA_TYPE);
		if (intent.hasExtra(GlobalValue.EXTRA_PIE_QTY))
			pieQty = intent.getStringExtra(GlobalValue.EXTRA_PIE_QTY);

		try {
			if (type.equals("Checkin") || type.equals("Purchase")
					|| type.equals("Redeem")) {
				int _pie = Integer.parseInt(pieQty);
				int _crtPie = mShopieSharePref.getCurrentBal();
				mShopieSharePref.setCurrentBtl(_pie + _crtPie);
			}
		} catch (NumberFormatException e) {
		}

		if (intent.hasExtra(GlobalValue.EXTRA_MESSAGE)) {
			if ((System.currentTimeMillis() - LST_NOTIFY_TIME) > THRE_NOTIFY_TIME) {
				LST_NOTIFY_TIME = System.currentTimeMillis();
				fire();
			}
		}

		isCheckin = true;
	}

	private void fire() {
		if (dialogAlert != null && dialogAlert.isShowing())
			dialogAlert.dismiss();
		MediaPlayer mMediaPlayer = new MediaPlayer();
		mMediaPlayer = MediaPlayer.create(VisvaAbstractActivity.this,
				R.raw.ting_ting);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
		mMediaPlayer.setLooping(false);
		mMediaPlayer.start();
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});

		PopupPie pie = PopupPie.getInstance(this);
		pie.fire(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		EasyTracker.getInstance().activityStart(this); // Add this method.
		// Send a screen view when the Activity is displayed to the user.
		mGaTracker.sendView(this.getClass().getName());
	}

	@Override
	protected void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance().activityStop(this); // Add this method.
		LST_NOTIFY_TIME = 0;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		GA_MAP_PARAMS.clear();
		GA_MAP_PARAMS.put("OnsaveInstanceState", this.getClass().getName()
				.toString());
		mGaTracker.send(GA_EVENT, GA_MAP_PARAMS);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		GA_MAP_PARAMS.clear();
		GA_MAP_PARAMS.put("onRestart", this.getClass().getName().toString());
		mGaTracker.send(GA_EVENT, GA_MAP_PARAMS);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		GA_MAP_PARAMS.clear();
		GA_MAP_PARAMS.put("onActivityResult", this.getClass().getName()
				.toString());
		mGaTracker.send(GA_EVENT, GA_MAP_PARAMS);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		GA_MAP_PARAMS.clear();
		GA_MAP_PARAMS
				.put("onBackPressed", this.getClass().getName().toString());
		mGaTracker.send(GA_EVENT, GA_MAP_PARAMS);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		GA_MAP_PARAMS.clear();
		GA_MAP_PARAMS.put("onDestroy", this.getClass().getName().toString());
		mGaTracker.send(GA_EVENT, GA_MAP_PARAMS);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		GA_MAP_PARAMS.clear();
		GA_MAP_PARAMS.put("onLowMemory", this.getClass().getName().toString());
		mGaTracker.send(GA_EVENT, GA_MAP_PARAMS);
	}

	static Toast mToast;
}
