package visvateam.outsource.idmanager.activities.synccloud;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

import visvateam.outsource.idmanager.activities.GGDriveSyncActivity;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.util.NetworkUtility;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SyncCloudActivity extends Activity {
	private TextView mTextViewCloudType;
	private TextView mTextViewLastTimeSync;
	private String items[] = { "Google Drive", "Dropbox" };
	private boolean isSyncToCloud = true;

	// =======================================
	// Dropbox API
	// =======================================
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_sync);

		/* init control */
		mTextViewCloudType = (TextView) findViewById(R.id.cloud_type);
		mTextViewLastTimeSync = (TextView) findViewById(R.id.last_time_sync);

		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		/* check netword */
		if (!NetworkUtility.getInstance(this).isNetworkAvailable())
			showDialog(Contants.DIALOG_NO_NET_WORK);

//		if (mApi.getSession().isLinked())
//			mTextViewCloudType.setText(getString(R.string.) + " Dropbox");
//		else
//			mTextViewCloudType.setText(getString(R.string.cloud_service_name) + " Google Drive");
	}

	@SuppressWarnings("deprecation")
	public void onSyncToCloud(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			isSyncToCloud = true;
			showDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
		} else {
			showDialog(Contants.DIALOG_NO_NET_WORK);
		}
	}

	@SuppressWarnings("deprecation")
	public void OnSyncToDevice(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			isSyncToCloud = false;
			showDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
		} else {
			showDialog(Contants.DIALOG_NO_NET_WORK);
		}
	}

	public void onReturn(View v) {
		finish();
	}

	/**
	 * Called to create a dialog to be shown.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case Contants.DIALOG_NO_NET_WORK:
			return createExampleDialog(Contants.DIALOG_NO_NET_WORK);
		case Contants.DIALOG_CHOICE_CLOUD_TYPE:
			return createExampleDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
		case Contants.DIALOG_NO_DATA_CLOUD:
			return createExampleDialog(Contants.DIALOG_NO_DATA_CLOUD);
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
			builder.setTitle(R.string.app_name);
			builder.setMessage(R.string.internet_not_use);
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
		case Contants.DIALOG_CHOICE_CLOUD_TYPE:
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("Choice One Cloud Type");
			builder2.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int item) {
					Intent intent = null;
					if (item == 0) {
						intent = new Intent(SyncCloudActivity.this, GGDriveSyncActivity.class);
					} else if (item == 1) {
						intent = new Intent(SyncCloudActivity.this, DropBoxSyncActivity.class);
					}

					Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
					mTextViewCloudType.setText("Cloud Sync :" + items[item]);
					dialog.dismiss();
					startSyncActivity(intent, isSyncToCloud);
				}
			});
			return builder2.create();
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
			return builder.create();
		default:
			return null;
		}
	}

	private void startSyncActivity(Intent intent, boolean isSyncToCloud) {
		intent.putExtra(Contants.IS_SYNC_TO_CLOUD, isSyncToCloud);
		startActivity(intent);
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

}
