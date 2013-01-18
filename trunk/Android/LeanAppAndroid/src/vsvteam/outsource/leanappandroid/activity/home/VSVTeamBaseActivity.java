package vsvteam.outsource.leanappandroid.activity.home;

import java.util.List;
import java.util.Locale;

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.R.anim;
import vsvteam.outsource.leanappandroid.R.string;
import vsvteam.outsource.leanappandroid.enums.SHARETYPE;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupActivity;
import vsvteam.outsource.leanappandroid.utilities.DialogUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Fruity base activity
 * 
 * @author vsvteam
 */
public class VSVTeamBaseActivity extends Activity {
	public static String TAG;

	public static final int REQUEST_CODE = 1;

	protected static VSVTeamBaseActivity self;

	protected ProgressDialog progressDialog, refreshDialog;
	protected Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		TAG = self.getClass().getSimpleName();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// preventing default implementation previous to
			// android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		DialogUtility.alert(self.getParent(), "Out of memory");
	}

	/**
	 * Overrides the default implementation for KeyEvent.KEYCODE_BACK so that
	 * all systems call onBackPressed().
	 */
	//TODO
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();
				return true;
			} catch (Exception e) {
				finish();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	public void changeInputKeyBoad() {

		Locale.setDefault(Locale.JAPANESE);
		Configuration config = getResources().getConfiguration();
		config.locale = Locale.JAPANESE;
		getBaseContext().getResources().updateConfiguration(config, null);

	}

	public void showDialogComingSoon(Context c) {

		DialogUtility.alert(c, "Coming soon");

	}

	// ======================= INTENT MANAGER =======================

	public void shareContent(String content) {

		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

		// set the type
		shareIntent.setType("text/plain");

		// add a subject
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Tin nĂ y hay láº¯m , Ä‘á»�c thá»­ xem nhĂ©  :  ");
		// build the body of the message to be shared
		String shareMessage = content;
		// add the messagelÄƒm
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
		// start the chooser for sharing
		startActivity(Intent.createChooser(shareIntent, "Chia sáº» "));
	}

	public void reLoadActivity(Activity c) {

		Intent intent = c.getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();

		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public void gotoActivityInGroup(Context context, Class<?> cla) {
		Intent previewMessage = new Intent(getParent(), cla);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.push(cla.getSimpleName(), previewMessage);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	public void gotoActivityInGroup(Context context, Class<?> cla, String key,
			CharSequence value) {
		Intent previewMessage = new Intent(getParent(), cla);
		previewMessage.putExtra(key, value);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.push(cla.getSimpleName(), previewMessage);
	}

	public void gotoActivityInGroup(Context context, Class<?> cla,
			String[] key, CharSequence[] value) {
		Intent previewMessage = new Intent(getParent(), cla);
		for (int i = 0; i < key.length; i++) {
			previewMessage.putExtra(key[i], value[i]);
		}
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.push(cla.getSimpleName(), previewMessage);
	}

	public void gotoActivityInGroup(Context context, Class<?> cla, Bundle b) {
		Intent previewMessage = new Intent(getParent(), cla);
		previewMessage.putExtras(b);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.push(cla.getSimpleName(), previewMessage);
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
	}

	public void gotoActivity(Context context, Class<?> cla, int flag) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(flag);
		startActivity(intent);
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

	/**
	 * Goto web page
	 * 
	 * @param url
	 */
	public void gotoWebPage(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
	}

	/**
	 * Goto web page inside tabGroup
	 * 
	 * @param url
	 */
	public void gotoWebPageInTabbar(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.startActivity(i);

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

	/**
	 * Close activity
	 */
	public void closeActivity(View v) {
		finish();
	}

	// ======================= PROGRESS DIALOG ======================

	/**
	 * Open progress dialog
	 * 
	 * @param text
	 */
	public void showProgressDialog(String text) {
		if (progressDialog == null) {
			try {
				progressDialog = ProgressDialog.show(this.getParent(),
						getString(R.string.app_name), text, true);
				progressDialog.setCancelable(false);
			} catch (Exception e) {
				progressDialog = ProgressDialog.show(this,
						getString(R.string.app_name), text, true);
				progressDialog.setCancelable(false);
			}
		}
	}

	/**
	 * Open progress dialog
	 */
	public void showProgressDialog() {
		showProgressDialog(getString(R.string.message_please_wait));
	}

	/**
	 * Close progress dialog
	 */
	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			// progressDialog.cancel();
			progressDialog = null;
		}
	}

	// ======================= KEYBOARD MANAGER =======================

	/**
	 * Show soft keyboard
	 * 
	 * @param editText
	 */
	protected void showKeyboard(EditText editText) {
		InputMethodManager inputMethodManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					0);
		}
	}

	/**
	 * Hide soft keyboard
	 * 
	 * @param editText
	 */
	protected void hideKeyboard(EditText editText) {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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

	// ======================= DIALOG MANAGER =======================

	/**
	 * Show confirm dialog
	 * 
	 * @param messageId
	 * @param positiveLabelId
	 * @param negativeLabelId
	 * @param positiveOnClick
	 * @param negativeOnClick
	 */
	protected void showDialog(int messageId, int positiveLabelId,
			int negativeLabelId,
			DialogInterface.OnClickListener positiveOnClick,
			DialogInterface.OnClickListener negativeOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(messageId));
		builder.setPositiveButton(getString(positiveLabelId), positiveOnClick);
		builder.setNegativeButton(getString(negativeLabelId), negativeOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show confirm dialog
	 * 
	 * @param title
	 * @param messageId
	 * @param positiveLabelId
	 * @param negativeLabelId
	 * @param positiveOnClick
	 * @param negativeOnClick
	 */
	protected void showDialog(String title, int messageId, int positiveLabelId,
			int negativeLabelId,
			DialogInterface.OnClickListener positiveOnClick,
			DialogInterface.OnClickListener negativeOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(getString(messageId));
		builder.setPositiveButton(getString(positiveLabelId), positiveOnClick);
		builder.setNegativeButton(getString(negativeLabelId), negativeOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show information dialog
	 * 
	 * @param messageId
	 * @param positiveLabelId
	 * @param positiveOnClick
	 */
	protected void showDialog(int messageId, int positiveLabelId,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(messageId));
		builder.setPositiveButton(getString(positiveLabelId), positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show single option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param positiveLabelId
	 * @param itemOnClick
	 * @param positiveOnClick
	 */
	protected void showSingleOptionDialog(int titleId, String[] items,
			int positiveLabelId, DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(titleId));
		builder.setSingleChoiceItems(items, 0, itemOnClick);
		builder.setPositiveButton(getString(positiveLabelId), positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show single option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param selectPosition
	 * @param positiveLabelId
	 * @param itemOnClick
	 * @param positiveOnClick
	 */
	protected void showSingleOptionDialog(int titleId, String[] items,
			int selectPosition, int positiveLabelId,
			DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(titleId));
		builder.setSingleChoiceItems(items, selectPosition, itemOnClick);
		builder.setPositiveButton(getString(positiveLabelId), positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show multiple option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param selects
	 * @param selectPosition
	 * @param positiveLabelId
	 * @param itemsOnClick
	 * @param positiveOnClick
	 */
	protected void showMultipleOptionDialog(int titleId, String[] items,
			boolean[] selects, int positiveLabelId,
			DialogInterface.OnClickListener positiveOnClick,
			DialogInterface.OnMultiChoiceClickListener itemsOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(titleId));
		builder.setMultiChoiceItems(items, selects, itemsOnClick);
		builder.setPositiveButton(getString(positiveLabelId), positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show simple option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param positiveLabelId
	 * @param itemOnClick
	 * @param positiveOnClick
	 */
	protected void showSimpleOptionDialog(int titleId, String[] items,
			int positiveLabelId, DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(titleId));
		builder.setItems(items, itemOnClick);
		builder.setPositiveButton(getString(positiveLabelId), positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show simple option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param positiveLabelId
	 * @param itemOnClick
	 * @param positiveOnClick
	 */
	protected void showSimpleOptionGroupDialog(int titleId, String[] items,
			int positiveLabelId, DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(titleId));
		builder.setItems(items, itemOnClick);
		builder.setPositiveButton(getString(positiveLabelId), positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	// =========================SHARE ACTION========================//

	public void share(SHARETYPE type, String content) {
		switch (type) {
		case FACEBOOK:
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					getString(R.string.app_name));
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
			PackageManager pm = getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(
					shareIntent, 0);
			for (final ResolveInfo app : activityList) {
				if ((app.activityInfo.name).contains("facebook")) {
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					shareIntent.setComponent(name);
					startActivity(shareIntent);
					break;
				}
			}
			break;
		case EMAIL:
			// Intent shareIntent = new
			// Intent(android.content.Intent.ACTION_SEND);
			// shareIntent.setType("text/plain");
			// shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
			// (String) v.getTag(R.string.app_name));
			// shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
			// (String) v.getTag(R.drawable.ic_launcher));
			// PackageManager pm = v.getContext().getPackageManager();
			// List activityList = pm.queryIntentActivities(shareIntent, 0);
			// for (final ResolveInfo app : activityList) {
			// if ((app.activityInfo.name).contain("gmail")) {
			// final ActivityInfo activity = app.activityInfo;
			// final ComponentName name = new ComponentName(
			// activity.applicationInfo.packageName, activity.name);
			// shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			// shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			// shareIntent.setComponent(name);
			// startActivity(shareIntent);
			// break;
			// }
			// }
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("plain/text");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
			startActivity(sharingIntent);

			break;
		case SMS:

			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setType("vnd.android-dir/mms-sms");
			// smsIntent.putExtra("address", "12125551212");
			smsIntent.putExtra("sms_body", content);
			startActivity(smsIntent);
			break;
		case TWITTER:
			Intent shareIntent2 = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent2.setType("text/plain");
			shareIntent2.putExtra(android.content.Intent.EXTRA_SUBJECT,
					getString(R.string.app_name));
			shareIntent2.putExtra(android.content.Intent.EXTRA_TEXT, content);
			PackageManager pm2 = getPackageManager();
			List<ResolveInfo> activityList2 = pm2.queryIntentActivities(
					shareIntent2, 0);

			for (final ResolveInfo app : activityList2) {
				if ("com.twitter.android.PostActivity"
						.equals(app.activityInfo.name)) {
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					shareIntent2.addCategory(Intent.CATEGORY_LAUNCHER);
					shareIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					shareIntent2.setComponent(name);
					startActivity(shareIntent2);
					break;
				}
			}
			break;
		default:
			break;
		}

	}
}
