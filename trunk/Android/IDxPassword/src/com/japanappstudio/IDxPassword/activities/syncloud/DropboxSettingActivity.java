package com.japanappstudio.IDxPassword.activities.syncloud;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;

import com.japanappstudio.IDxPassword.activities.R;
import com.japanappstudio.IDxPassword.activities.R.drawable;
import com.japanappstudio.IDxPassword.activities.R.id;
import com.japanappstudio.IDxPassword.activities.R.layout;
import com.japanappstudio.IDxPassword.activities.R.string;

@SuppressLint("HandlerLeak")
public class DropboxSettingActivity extends Activity {
	private static final String TAG = "Dropbox";

	// /////////////////////////////////////////////////////////////////////////
	// Your app-specific settings. //
	// /////////////////////////////////////////////////////////////////////////

	// Replace this with your app key and secret assigned by Dropbox.
	// Note that this is a really insecure way to do this, and you shouldn't
	// ship code which contains your key & secret in such an obvious way.
	// Obfuscation is good.
	// final static private String APP_KEY = "fxh7pnxcqbg3qwy";
	// final static private String APP_SECRET = "fjk6z73ot28n1t3";

	// If you'd like to change the access type to the full Dropbox instead of
	// an app folder, change this value.
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

	// /////////////////////////////////////////////////////////////////////////
	// End app-specific settings. //
	// /////////////////////////////////////////////////////////////////////////

	// You don't need to change these, leave them alone.
	final static private String ACCOUNT_PREFS_NAME = "prefs";
	final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
	DropboxAPI<AndroidAuthSession> mApi;
	boolean isCreat = false;

	private boolean mLoggedIn;

	// Android widgets
	private Button btnLinkToDropbox;

	private IdManagerPreference mIdManagerPreference;
	private String mGGAccountName;
	private boolean isCheckLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* share preference */
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		mGGAccountName = mIdManagerPreference.getGoogleAccNameSession();
		mIdManagerPreference.setDropboxLogin(false);
		/* create file if not exist */
		File file = new File(Contants.PATH_ID_FILES);
		if (!file.exists())
			file.mkdirs();

		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		// Basic Android widgets
		setContentView(R.layout.page_dropbox_setting);

		checkAppKeySetup();

		btnLinkToDropbox = (Button) findViewById(R.id.btn_link_to_dropbox);

		btnLinkToDropbox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// This logs you out if you're logged in, or vice versa
				if (mLoggedIn) {
					logOut();
				} else {
					// Start the remote authentication
					// mIdManagerPreference.setGoogleAccNameSession("");
					mIdManagerPreference.setDropboxLogin(true);
					if (!"".equals(mIdManagerPreference
							.getGoogleAccNameSession()))
						showChoiceDialog();
					else
						mApi.getSession().startAuthentication(
								DropboxSettingActivity.this);
				}
			}
		});

		if ("".equals(mGGAccountName)) {
			// Display the proper UI state if logged in or not
			setLoggedIn(mApi.getSession().isLinked());
		} else {
			logOut();
		}
	}

	private AlertDialog creatDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null)
			builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(
				getResources().getString(R.string.confirm_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		return builder.create();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if ("".equals(mIdManagerPreference.getGoogleAccNameSession())) {
			AndroidAuthSession session = mApi.getSession();

			// The next part must be inserted in the onResume() method of the
			// activity from which session.startAuthentication() was called, so
			// that Dropbox authentication completes properly.
			if (null != session && session.authenticationSuccessful()) {
				try {
					// Mandatory call to complete the auth
					session.finishAuthentication();
					// Store it locally in our app for later use
					TokenPair tokens = session.getAccessTokenPair();
					storeKeys(tokens.key, tokens.secret);
					setLoggedIn(true);
					if (mIdManagerPreference.isDropboxLogin()) {
						mIdManagerPreference.setDropboxLogin(false);
						finish();
					}
				} catch (IllegalStateException e) {
					showToast("Couldn't authenticate with Dropbox:"
							+ e.getLocalizedMessage());
				}
			}
		} else
			logOut();
	}

	private void logOut() {
		Log.e("log out", "log out");
		// Remove credentials from the session
		mApi.getSession().unlink();

		// Clear our stored keys
		clearKeys();
		// Change UI state to display logged out version
		setLoggedIn(false);
	}

	/**
	 * Convenience function to change UI state based on being logged in
	 */
	private void setLoggedIn(boolean loggedIn) {
		Log.e("login", "login " + loggedIn);
		mLoggedIn = loggedIn;
		if (loggedIn) {
			if (!isCreat) {
				creatDialog(null, getString(R.string.dropbox_already_use))
						.show();

				isCreat = true;
			}
			btnLinkToDropbox.setText(getString(R.string.dropbox_reset_sync));
			mIdManagerPreference.setGoogleAccNameSession("");
		} else {
			btnLinkToDropbox.setText(getString(R.string.dropbox_start_to_use));
		}
	}

	private void checkAppKeySetup() {
		// Check to make sure that we have a valid app key
		if (Contants.APP_KEY.startsWith("CHANGE")
				|| Contants.APP_SECRET.startsWith("CHANGE")) {
			showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
			finish();
			return;
		}

		// Check if the app has set up its manifest properly.
		Intent testIntent = new Intent(Intent.ACTION_VIEW);
		String scheme = "db-" + Contants.APP_KEY;
		String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
		testIntent.setData(Uri.parse(uri));
		PackageManager pm = getPackageManager();
		if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
			showToast("URL scheme in your app's "
					+ "manifest is not set up correctly. You should have a "
					+ "com.dropbox.client2.android.AuthActivity with the "
					+ "scheme: " + scheme);
			finish();
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		error.show();
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a
	 * local store, rather than storing user name & password, and
	 * re-authenticating each time (which is not to be done, ever).
	 * 
	 * @return Array of [access_key, access_secret], or null if none stored
	 */
	private String[] getKeys() {
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(ACCESS_KEY_NAME, null);
		String secret = prefs.getString(ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) {
			String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		} else {
			return null;
		}
	}

	/**
	 * Shows keeping the access keys returned from Trusted Authenticator in a
	 * local store, rather than storing user name & password, and
	 * re-authenticating each time (which is not to be done, ever).
	 */
	private void storeKeys(String key, String secret) {
		// Save the access key for later
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.putString(ACCESS_KEY_NAME, key);
		edit.putString(ACCESS_SECRET_NAME, secret);
		edit.commit();
	}

	private void clearKeys() {
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(Contants.APP_KEY,
				Contants.APP_SECRET);
		AndroidAuthSession session;

		String[] stored = getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0],
					stored[1]);
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE,
					accessToken);
		} else {
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
		}

		return session;
	}

	/**
	 * check sdcard is read only
	 * 
	 * @return
	 */
	public boolean isExternalStorageReadOnly() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	/**
	 * check sdcard is available or not
	 * 
	 * @return
	 */
	public boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	private void showChoiceDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon);
		builder.setMessage(R.string.dropbox_use_instead);
		builder.setPositiveButton(R.string.confirm_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mIdManagerPreference.setGoogleAccNameSession("");
						mApi.getSession().startAuthentication(
								DropboxSettingActivity.this);
						return;
					}
				});
		builder.setNegativeButton(R.string.confirm_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
		builder.show();
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			Log.e("result", "result " + msg.arg1);
			if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_FAILED)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_FAILED);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_SUCCESS)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_SUCCESS);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED);
			else if (msg.arg1 == Contants.DIALOG_NO_DATA_CLOUD)
				showDialog(Contants.DIALOG_NO_DATA_CLOUD);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD) {
				showDialog(Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD);
			} else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED) {
				showDialog(Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED);
			} else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER)
				showDialog(Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER);
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_AUTHEN_GG_FAILED) {
				Log.e("adjfhkldhf", "adfkjhkd ");
				UserRecoverableAuthIOException e = (UserRecoverableAuthIOException) msg.obj;
				// startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
			}
		};
	};
}
