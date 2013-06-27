package com.japanappstudio.IDxPassword.activities.syncloud;

import com.japanappstudio.IDxPassword.activities.R;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.GoogleKeyInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;

@SuppressWarnings("deprecation")
public class GGDriveSettingActivity extends Activity {
	private static final int REQUEST_ACCOUNT_PICKER = 1;
	private static Uri fileUri;
	private static Drive service;
	private GoogleAccountCredential credential;
	private Button mBtnLinkToGGDrive;
	private String mAccountName = "";

	// https://developers.google.com/drive/scopes
	private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/drive";

	// https://code.google.com/apis/console/
	private static final String CLIENT_ID = "863640288546-6m1t1cab8f3vv7o73h5q5lkod0dm5rvq.apps.googleusercontent.com";

	private AccountManager accountManager;
	private Account[] accounts;
	private String authToken;

	// /////////////////////////////////////////////////////////////////////////
	// Your app-specific settings. //
	// /////////////////////////////////////////////////////////////////////////

	// Replace this with your app key and secret assigned by Dropbox.
	// Note that this is a really insecure way to do this, and you shouldn't
	// ship code which contains your key & secret in such an obvious way.
	// Obfuscation is good.

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

	private IdManagerPreference mIdManagerPreference;

	private boolean isLogIn = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_gg_drive_setting);

		/* init share preference */
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		mAccountName = mIdManagerPreference.getGoogleAccNameSession();

		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		/* create file if not exist */
		java.io.File file = new java.io.File(Contants.PATH_ID_FILES);
		if (!file.exists())
			file.mkdirs();

		credential = GoogleAccountCredential.usingOAuth2(this,
				DriveScopes.DRIVE);
		accountManager = AccountManager.get(this);
		accounts = accountManager.getAccountsByType("com.google");

		mBtnLinkToGGDrive = (Button) findViewById(R.id.btn_link_to_gg_drive);
		mBtnLinkToGGDrive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mApi.getSession().isLinked())
					showChoiceDialog();
				else
					startActivityForResult(credential.newChooseAccountIntent(),
							REQUEST_ACCOUNT_PICKER);
			}
		});

		if (!"".equals(mAccountName)) {
			creatDialog(null, getString(R.string.gg_drive_already_use)).show();
			mBtnLinkToGGDrive.setText(getString(R.string.dropbox_reset_sync));
		} else {
			mBtnLinkToGGDrive.setText(getString(R.string.dropbox_start_to_use));
		}
	}
	private AlertDialog creatDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null)
			builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(getResources().getString(R.string.confirm_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		return builder.create();
	}
	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (resultCode == RESULT_OK && data != null
					&& data.getExtras() != null) {
				String accountName = data
						.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

				if (accountName != null) {
					credential.setSelectedAccountName(accountName);

					accountManager.getAuthToken(accounts[1], AUTH_TOKEN_TYPE,
							null, this, new AccountManagerCallback<Bundle>() {

								public void run(
										final AccountManagerFuture<Bundle> future) {
									try {
										authToken = future
												.getResult()
												.getString(
														AccountManager.KEY_AUTHTOKEN);
										mIdManagerPreference
												.setKeyAuthenGGDrive(authToken);
										service = getDriveService(credential);
									} catch (OperationCanceledException exception) {
										// TODO
									} catch (Exception exception) {
//										Log.d(this.getClass().getName(),
//												exception.getMessage());
									}
								}
							}, null);

					mBtnLinkToGGDrive
							.setText(getString(R.string.gg_drive_already_use));
					mIdManagerPreference.setGoogleAccNameSession(accountName);
					if (mApi.getSession().isLinked())
						logOutDropbox();
					// finish();
				}
			}
			break;
		}
	}

	private void logOutDropbox() {
		// Remove credentials from the session
		mApi.getSession().unlink();

		// Clear our stored keys
		clearKeys();
	}

	private void clearKeys() {
		SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	private Drive getDriveService(GoogleAccountCredential credential) {
		final HttpTransport transport = AndroidHttp.newCompatibleTransport();
		final JsonFactory jsonFactory = new GsonFactory();
		GoogleCredential credential1 = new GoogleCredential();
		credential1.setAccessToken(authToken);
		Drive drive = new Drive.Builder(transport, jsonFactory, credential1)
				.setApplicationName(getString(R.string.app_name))
				.setGoogleClientRequestInitializer(
						new GoogleKeyInitializer(CLIENT_ID)).build();
		return drive;
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast,
						Toast.LENGTH_SHORT).show();
			}
		});
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

	private void showChoiceDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon);
		builder.setMessage(R.string.gg_drive_use_instead);
		builder.setPositiveButton(R.string.confirm_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivityForResult(
								credential.newChooseAccountIntent(),
								REQUEST_ACCOUNT_PICKER);
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
	
	  @Override
	    protected Dialog onCreateDialog(final int id) {
	        switch (id) {
	            case 1:
	                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GGDriveSettingActivity.this);

	                String[] names = new String[accounts.length];

	                for (int i = 0; i < accounts.length; i++) {
	                    names[i] = accounts[i].name;
	                }

//	                alertDialogBuilder.setItems(names, new DialogInterface.);
	                alertDialogBuilder.setTitle("Select a Google account");
	                return alertDialogBuilder.create();
	        }

	        return null;
	    }
}