package visvateam.outsource.idmanager.activities.synccloud;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.exportcontroller.dropbox.DBDropboxAutoSyncController;
import visvateam.outsource.idmanager.exportcontroller.dropbox.DBDropboxController;
import visvateam.outsource.idmanager.exportcontroller.dropbox.DropBoxDownloadFile;
import visvateam.outsource.idmanager.exportcontroller.ggdrive.GGUploadController;
import visvateam.outsource.idmanager.util.NetworkUtility;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class SyncCloudActivity extends Activity {
	private TextView mTextViewCloudType;
	private TextView mTextViewLastTimeSync;
	private boolean isSyncToCloud = true;

	private static final int NO_CLOUD_LOGIN = 0;
	private static final int GG_DRIVE_LOGIN_SESSION = 1;
	private static final int DROPBOX_LOGIN_SESSION = 2;

	private static final int REQUEST_ACCOUNT_PICKER = 1;
	private int mCloudType = 0;

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

	private IdManagerPreference mIdManagerPreference;
	private String mGGAccountName;

	private static Drive service;
	private GoogleAccountCredential credential;
	private long mLastTimeSync;
	private String accountName;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_sync);

		/* share preference */
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		mGGAccountName = mIdManagerPreference.getGoogleAccNameSession();
		mLastTimeSync = mIdManagerPreference.getLastTimeSyncCloud();
		/* gg drive api */
		credential = GoogleAccountCredential.usingOAuth2(this, DriveScopes.DRIVE);

		/* init control */
		mTextViewCloudType = (TextView) findViewById(R.id.cloud_type);
		mTextViewLastTimeSync = (TextView) findViewById(R.id.last_time_sync);

		Date date = new Date(mLastTimeSync);
		String mLastDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		if (mLastTimeSync > 0)
			mTextViewLastTimeSync.setText(getString(R.string.last_sync) + mLastDate);

		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		/* check netword */
		if (!NetworkUtility.getInstance(this).isNetworkAvailable())
			showDialog(Contants.DIALOG_NO_NET_WORK);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mApi.getSession().isLinked()) {
			mTextViewCloudType.setText(getString(R.string.cloud_service_name) + " Dropbox");
			mCloudType = DROPBOX_LOGIN_SESSION;
		} else if (!"".equals(mGGAccountName)) {
			mTextViewCloudType.setText(getString(R.string.cloud_service_name) + " Google Drive");
			mCloudType = GG_DRIVE_LOGIN_SESSION;
		} else {
			mTextViewCloudType.setText(getString(R.string.cloud_service_name));
			mCloudType = NO_CLOUD_LOGIN;
		}
	}

	/**
	 * auto sync data between device and cloud
	 * 
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	public void onSyncAuto(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			if (mCloudType == NO_CLOUD_LOGIN) {
				showDialog(Contants.DIALOG_NO_CLOUD_SETUP);
			} else {
				startAutoSyncData(mCloudType);
			}
		} else {
			showDialog(Contants.DIALOG_NO_NET_WORK);
		}
	}

	@SuppressWarnings("deprecation")
	public void onSyncToCloud(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			if (mCloudType == NO_CLOUD_LOGIN) {
				showDialog(Contants.DIALOG_NO_CLOUD_SETUP);
			} else {
				isSyncToCloud = true;
				// showDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
				startSyncToCloud(mCloudType);
			}
		} else {
			showDialog(Contants.DIALOG_NO_NET_WORK);
		}
	}

	/**
	 * auto sync data
	 * 
	 * @param mCloudType
	 */
	private void startAutoSyncData(int mCloudType) {
		if (mCloudType == GG_DRIVE_LOGIN_SESSION) {
			startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
		} else if (mCloudType == DROPBOX_LOGIN_SESSION) {
			boolean isCheckedTime = false;
			startAutoSyncByDropbox(isCheckedTime);
		}
	}

	/**
	 * start sync data by dropbox
	 */
	private void startAutoSyncByDropbox(boolean isCheckedTime) {
		File dbFile = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		DBDropboxAutoSyncController autoSync = new DBDropboxAutoSyncController(
				SyncCloudActivity.this, mApi, Contants.FOLDER_ON_DROPBOX_DB, dbFile, mHandler,
				isCheckedTime);
		autoSync.execute();
	}

	/**
	 * sync data to cloud
	 * 
	 * @param mCloudType
	 */
	private void startSyncToCloud(int mCloudType) {
		if (mCloudType == GG_DRIVE_LOGIN_SESSION) {
			startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
		} else if (mCloudType == DROPBOX_LOGIN_SESSION) {
			boolean isCheckedTime = false;
			syncToCloudByDropbox(isCheckedTime);
		}
	}

	private void syncToCloudByDropbox(boolean isCheckedTime) {
		// TODO Auto-generated method stub
		File dbFile = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		DBDropboxController newFile = new DBDropboxController(SyncCloudActivity.this, mApi,
				Contants.FOLDER_ON_DROPBOX_DB, dbFile, mHandler, isCheckedTime);
		newFile.execute();
	}

	@SuppressWarnings("deprecation")
	public void OnSyncToDevice(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			isSyncToCloud = false;
			if (mCloudType == NO_CLOUD_LOGIN) {
				showDialog(Contants.DIALOG_NO_CLOUD_SETUP);
			} else if (mCloudType == GG_DRIVE_LOGIN_SESSION) {
				startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
			} else if (mCloudType == DROPBOX_LOGIN_SESSION) {
				boolean isCheckTime = false;
				startSyncToDeviceByDropBox(isCheckTime);
			}
		} else {
			showDialog(Contants.DIALOG_NO_NET_WORK);
		}
	}

	private void startSyncToDeviceByDropBox(boolean isCheckedTime) {
		File dbFile = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		String dbFilePath = dbFile.getParent();
		DropBoxDownloadFile downloadFile = new DropBoxDownloadFile(this, mApi,
				Contants.FOLDER_ON_DROPBOX_DB, dbFilePath, mHandler, isCheckedTime);
		downloadFile.execute();
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
		case Contants.DIALOG_NO_CLOUD_SETUP:
			return createExampleDialog(Contants.DIALOG_NO_CLOUD_SETUP);
		case Contants.DIALOG_MESSAGE_SYNC_FAILED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_FAILED);
		case Contants.DIALOG_MESSAGE_SYNC_SUCCESS:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_SUCCESS);
		case Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED);
		case Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE);
		case Contants.DIALOG_MESSAGE_CHOICE_DATA_READ:
			return createExampleDialog(Contants.DIALOG_MESSAGE_CHOICE_DATA_READ);
		case Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED);
		case Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD:
			return createExampleDialog(Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD);
		case Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER);
		case Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER);
		case Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER);
		case Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER);
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
		case Contants.DIALOG_NO_CLOUD_SETUP:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.no_cloud_serivce_set_up));
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
		case Contants.DIALOG_MESSAGE_SYNC_SUCCESS:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.sync_finish));
			builder.setCancelable(false);
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							mLastTimeSync = mIdManagerPreference.getLastTimeSyncCloud();
							Date date = new Date(mLastTimeSync);
							String mLastDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
							mTextViewLastTimeSync.setText(SyncCloudActivity.this
									.getString(R.string.last_sync) + mLastDate);
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_FAILED:
			builder.setTitle(getString(R.string.app_name));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.sync_failed));
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

		case Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD:
			builder.setTitle(getString(R.string.app_name));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.data_rewritten));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.sync_data_duplicate_msg));
			builder.setIcon(R.drawable.icon);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.data_cloud_newer));
			builder.setIcon(R.drawable.icon);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							if (mCloudType == DROPBOX_LOGIN_SESSION)
								syncToCloudByDropbox(true);
							else if (mCloudType == GG_DRIVE_LOGIN_SESSION)
								saveFileToDrive(accountName, true);
								return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Message msg = mHandler.obtainMessage();
							msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_FAILED;
							mHandler.sendMessage(msg);
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.sync_to_cloud_data_device_newer));
			builder.setIcon(R.drawable.icon);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							if (mCloudType == DROPBOX_LOGIN_SESSION)
								syncToCloudByDropbox(true);
							else if(mCloudType == GG_DRIVE_LOGIN_SESSION)
								saveFileToDrive(accountName, true);
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Message msg = mHandler.obtainMessage();
							msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_FAILED;
							mHandler.sendMessage(msg);
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.sync_to_device_data_cloud_newer));
			builder.setIcon(R.drawable.icon);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							if (mCloudType == DROPBOX_LOGIN_SESSION)
								startSyncToDeviceByDropBox(true);
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Message msg = mHandler.obtainMessage();
							msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_FAILED;
							mHandler.sendMessage(msg);
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.sync_data_duplicate_msg));
			builder.setIcon(R.drawable.icon);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							if (mCloudType == DROPBOX_LOGIN_SESSION)
								startSyncToDeviceByDropBox(true);
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Message msg = mHandler.obtainMessage();
							msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_FAILED;
							mHandler.sendMessage(msg);
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED:
			builder.setCancelable(false);
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.sync_interrupt));
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
		case Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED:
			builder.setCancelable(false);
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.success));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							return;
						}
					});
			return builder.create();

		default:
			return null;
		}
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

	/**
	 * activity on result get data to sync to gg drive
	 */
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
				accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					Log.e("accname", "acc Name " + accountName);
					credential.setSelectedAccountName(accountName);
					service = getDriveService(credential);
					boolean isCheckedTime = false;
					saveFileToDrive(accountName, isCheckedTime);
				}
			}
			break;
		// case REQUEST_AUTHORIZATION:
		// if (resultCode == Activity.RESULT_OK) {
		// saveFileToDrive();
		// } else {
		// startActivityForResult(credential.newChooseAccountIntent(),
		// REQUEST_ACCOUNT_PICKER);
		// }
		// break;
		default:
			break;
		}
	}

	private void saveFileToDrive(String accountName, boolean isCheckedTime) {
		java.io.File fileDb = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		GGUploadController drive = new GGUploadController(this, service, fileDb, mHandler,
				accountName, isCheckedTime);
		drive.execute();
	}

	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential).build();
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings({ "deprecation" })
		public void handleMessage(android.os.Message msg) {
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
		};
	};

}
