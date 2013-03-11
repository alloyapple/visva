package visvateam.outsource.idmanager.activities.synccloud;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.exportcontroller.dropbox.DropBoxController;
import visvateam.outsource.idmanager.exportcontroller.dropbox.DropBoxDownloadFile;
import visvateam.outsource.idmanager.exportcontroller.excelcreator.ExcelDocumentController;
import visvateam.outsource.idmanager.util.NetworkUtility;

public class DropBoxSyncActivity extends Activity {
	private static final String TAG = "DBRoulette";

	// /////////////////////////////////////////////////////////////////////////
	// Your app-specific settings. //
	// /////////////////////////////////////////////////////////////////////////

	// Replace this with your app key and secret assigned by Dropbox.
	// Note that this is a really insecure way to do this, and you shouldn't
	// ship code which contains your key & secret in such an obvious way.
	// Obfuscation is good.
	final static private String APP_KEY = "fxh7pnxcqbg3qwy";
	final static private String APP_SECRET = "fjk6z73ot28n1t3";

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

	private boolean mLoggedIn;

	// Android widgets
	private Button btnLinkToDropbox;
	private Button btnStartSync;

	private boolean isSyncToCloud;

	private IdManagerPreference mIdManagerPreference;
	private long mLastTimeSync;
	private long mLastTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* share preference */
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		mLastTimeSync = mIdManagerPreference.getLastTimeSyncCloud();

		/* create file if not exist */
		File file = new File(Contants.PATH_ID_FILES);
		if (!file.exists())
			file.mkdirs();

		isSyncToCloud = getIntent().getExtras().getBoolean(Contants.IS_SYNC_TO_CLOUD);
		Log.e("isSyncTOCLoud", "isSyncCloud " + isSyncToCloud);
		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		// Basic Android widgets
		setContentView(R.layout.page_sync_dropbox);

		checkAppKeySetup();

		btnLinkToDropbox = (Button) findViewById(R.id.btn_link_to_dropbox);

		btnLinkToDropbox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// This logs you out if you're logged in, or vice versa
				if (mLoggedIn) {
					logOut();
				} else {
					// Start the remote authentication
					mApi.getSession().startAuthentication(DropBoxSyncActivity.this);
				}
			}
		});

		// This is the button to take a photo
		btnStartSync = (Button) findViewById(R.id.btn_start_sync);

		btnStartSync.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				if (!isExternalStorageAvailable()) {
					showToast("Sdcard is unvailable.Check it again");
				} else {
					if (NetworkUtility.getInstance(DropBoxSyncActivity.this).isNetworkAvailable())
						if (mApi.getSession().isLinked()) {
							if (isSyncToCloud) {
								/* upload file to cloud */
								startSyncToCloud();
							} else {
								/* download file to device */
								checkDataOnDropbox();
								// startSyncToDevice();

							}
						} else {
							showToast("You must authenticate with Dropbox first");
						}
					else
						showDialog(Contants.DIALOG_NO_NET_WORK);
				}
			}

		});

		// Display the proper UI state if logged in or not
		setLoggedIn(mApi.getSession().isLinked());

	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void checkDataOnDropbox() {
		// TODO Auto-generated method stub
		// Get the metadata for a directory
		Entry dirent = null;
		try {
			dirent = mApi.metadata(Contants.FOLDER_ON_DROPBOX, 1000, null, true, null);
		} catch (DropboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!dirent.isDir || dirent.contents == null) {
			// It's not a directory, or there's nothing in it
			showDialog(Contants.DIALOG_NO_DATA_CLOUD);

		}

		// Make a list of everything in it that we can get a thumbnail for
		Entry entry = null;
		for (Entry ent : dirent.contents) {
			if (Contants.DATA_IDMANAGER_NAME.equals(ent.fileName().toString())) {
				// Add it to the list of thumbs we can choose from
				entry = ent;
			}
		}

		// Now pick a random one
		if (null != entry) {
			String modify = entry.modified;
			Date date = new Date(modify);
			mLastTime = date.getTime();
			showDialog(Contants.DIALOG_DATA_REWRITTEN);
		} else
			showDialog(Contants.DIALOG_NO_DATA_CLOUD);

	}

	private void startSyncToDevice() {
		// TODO Auto-generated method stub
		mIdManagerPreference.setLastTimeSyncCloud(mLastTime);
		File dbFile = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		String dbFilePath = dbFile.getParent();
		
		DropBoxDownloadFile download = new DropBoxDownloadFile(DropBoxSyncActivity.this, mApi,
				Contants.FOLDER_ON_DROPBOX, dbFilePath);
		download.execute();
	}

	private void startSyncToCloud() {
		// TODO Auto-generated method stub
		File dbFile = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		DropBoxController newFile = new DropBoxController(DropBoxSyncActivity.this, mApi,
				Contants.FOLDER_ON_DROPBOX, dbFile);
		newFile.execute();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AndroidAuthSession session = mApi.getSession();

		// The next part must be inserted in the onResume() method of the
		// activity from which session.startAuthentication() was called, so
		// that Dropbox authentication completes properly.
		if (session.authenticationSuccessful()) {
			try {
				// Mandatory call to complete the auth
				session.finishAuthentication();

				// Store it locally in our app for later use
				TokenPair tokens = session.getAccessTokenPair();
				storeKeys(tokens.key, tokens.secret);
				setLoggedIn(true);
			} catch (IllegalStateException e) {
				showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
				Log.i(TAG, "Error authenticating", e);
			}
		}
	}

	private void logOut() {
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
		mLoggedIn = loggedIn;
		if (loggedIn) {
			btnLinkToDropbox.setText("Unlink from Dropbox");
		} else {
			btnLinkToDropbox.setText("Link with Dropbox");
		}
	}

	private void checkAppKeySetup() {
		// Check to make sure that we have a valid app key
		if (APP_KEY.startsWith("CHANGE") || APP_SECRET.startsWith("CHANGE")) {
			showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
			finish();
			return;
		}

		// Check if the app has set up its manifest properly.
		Intent testIntent = new Intent(Intent.ACTION_VIEW);
		String scheme = "db-" + APP_KEY;
		String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
		testIntent.setData(Uri.parse(uri));
		PackageManager pm = getPackageManager();
		if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
			showToast("URL scheme in your app's "
					+ "manifest is not set up correctly. You should have a "
					+ "com.dropbox.client2.android.AuthActivity with the " + "scheme: " + scheme);
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
		AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session;

		String[] stored = getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0], stored[1]);
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE, accessToken);
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
	 * Called to create a dialog to be shown.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case Contants.DIALOG_NO_NET_WORK:
			return createExampleDialog(Contants.DIALOG_NO_NET_WORK);
		case Contants.DIALOG_NO_DATA_CLOUD:
			return createExampleDialog(Contants.DIALOG_NO_DATA_CLOUD);
		case Contants.DIALOG_DATA_REWRITTEN:
			return createExampleDialog(Contants.DIALOG_DATA_REWRITTEN);
		default:
			return null;
		}
	}

	/**
	 * Create and return an example alert dialog with an edit text box.
	 */
	private Dialog createExampleDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case Contants.DIALOG_NO_NET_WORK:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.internet_not_use));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_NO_DATA_CLOUD:
			AlertDialog.Builder builderNoData = new AlertDialog.Builder(this);
			builderNoData.setTitle(R.string.app_name);
			builderNoData.setMessage(R.string.no_data_on_cloud);
			builderNoData.setIcon(R.drawable.icon);
			builderNoData.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			return builderNoData.create();
		case Contants.DIALOG_DATA_REWRITTEN:
			AlertDialog.Builder builderDataRewritten = new AlertDialog.Builder(this);
			builderDataRewritten.setTitle(R.string.app_name);
			builderDataRewritten.setMessage(R.string.data_rewritten);
			builderDataRewritten.setIcon(R.drawable.icon);
			builderDataRewritten.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							startSyncToDevice();
							return;
						}
					});
			builderDataRewritten.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
			return builderDataRewritten.create();
		default:
			return null;
		}
	}
}
