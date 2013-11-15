package vn.com.shoppie.activity;

import java.lang.reflect.Method;
import java.util.HashMap;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.util.VisvaDialog;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public abstract class VisvaAbstractActivity extends Activity {
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
	
	public static Toast mToast;

	//SBroastcastProvider broastcast = new SBroastcastProvider(this);

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
		// Get the intent that started this Activity.
		Intent intent = this.getIntent();
		Uri uri = intent.getData();

		// Get the GoogleAnalytics singleton. Note that the SDK uses
		// the application context to avoid leaking the current context.
		mGaInstance = GoogleAnalytics.getInstance(this);

		// Use the GoogleAnalytics singleton to get a Tracker.
		mGaTracker = mGaInstance
				.getTracker(getString(R.string.ga_trackingId)); // Placeholder
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unRegisterBaseActivityReceiver();
	}

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
		return filter;
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// LST_NOTIFY_TIME = 0;
		//registerReceiver(broastcast.getBroastcast(), new IntentFilter(
		//		GlobalValue.DISPLAY_MESSAGE_ACTION));
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
}
