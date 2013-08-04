package com.japanappstudio.IDxPassword.activities;

import java.io.File;
import java.util.ArrayList;

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
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.exportcontroller.dropbox.ReadFileViaDropBox;
import com.japanappstudio.IDxPassword.exportcontroller.ggdrive.DownloadCSVViaGGDrive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SelectFileActivity extends Activity implements OnItemClickListener {
	// If you'd like to change the access type to the full Dropbox instead of
	// an app folder, change this value.
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

	// You don't need to change these, leave them alone.
	final static private String ACCOUNT_PREFS_NAME = "prefs";
	final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";

	private ListView list;
	private ArrayList<String> mFileList;
	private DropboxAPI<AndroidAuthSession> mApi;
	private String mGGAccountName;
	public IdManagerPreference mPref;

	/* gg drive */
	private static Drive service;
	private GoogleAccountCredential credential;

	private String mSelectFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.page_select_file_read);
		list = (ListView) findViewById(R.id.list_file_selected);

		mPref = IdManagerPreference.getInstance(this);
		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		mGGAccountName = mPref.getGoogleAccNameSession();
		/* gg drive api */
		credential = GoogleAccountCredential.usingOAuth2(
				SelectFileActivity.this, DriveScopes.DRIVE);
		credential.setSelectedAccountName(mGGAccountName);
		service = getDriveService(credential);

		mFileList = getIntent().getExtras().getStringArrayList("listFile");
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mFileList);
		list.setAdapter(adapter1);
		list.setOnItemClickListener(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == list) {
			Log.e("aldfj", "mnfd " + mFileList.get(arg2));
			mSelectFile = mFileList.get(arg2);
			if (mApi.getSession().isLinked())
				startReadFileViaCloud(mSelectFile, false);
			else if (!"".equals(mGGAccountName))
				saveFileToDevice(mGGAccountName, mSelectFile);
		}
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
	 * start read file via cloud
	 */
	private void startReadFileViaCloud(CharSequence fileName,
			boolean isCheckFile) {
		Log.e("isCheckFile", "isCheckFile " + isCheckFile);
		File file = new File(Contants.PATH_ID_FILES);
		if (!file.exists())
			file.mkdirs();
		String mFilePath = file.getAbsolutePath();
		ReadFileViaDropBox readFile = new ReadFileViaDropBox(
				SelectFileActivity.this, mApi, Contants.FOLDER_ON_DROPBOX_CSV,
				mFilePath, mHandler, fileName, isCheckFile);
		readFile.execute();
	}

	private void saveFileToDevice(String accountName, CharSequence mSeletedFile) {
		String fileName = (String) mSeletedFile;
		String cachePath = Contants.PATH_ID_FILES + fileName;
		java.io.File file = new java.io.File(cachePath);
		// if (!file.exists())
		// file.mkdir();
		DownloadCSVViaGGDrive drive = new DownloadCSVViaGGDrive(this, service,
				file, mHandler, fileName, true);
		drive.execute();
	}

	/**
	 * get authen to access gg drive
	 */
	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).build();
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings({ "unchecked", "deprecation" })
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
				Intent intent = new Intent(SelectFileActivity.this,
						ChoiceCSVImportType.class);
				intent.putExtra(Contants.KEY_CHOICE_CSV_FILE, mSelectFile);
				startActivity(intent);
			}
		};
	};

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
		case Contants.DIALOG_NO_CLOUD_SETUP:
			return createExampleDialog(Contants.DIALOG_NO_CLOUD_SETUP);
		case Contants.DIALOG_EXPORT_DATA:
			return createExampleDialog(Contants.DIALOG_EXPORT_DATA);
		case Contants.DIALOG_MESSAGE_SYNC_FAILED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_FAILED);
		case Contants.DIALOG_MESSAGE_SYNC_SUCCESS:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_SUCCESS);
		case Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED);
		case Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE:
			return createExampleDialog(Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE);
		case Contants.DIALOG_NO_DATA_CLOUD:
			return createExampleDialog(Contants.DIALOG_NO_DATA_CLOUD);
		case Contants.DIALOG_MESSAGE_CHOICE_DATA_READ:
			return createExampleDialog(Contants.DIALOG_MESSAGE_CHOICE_DATA_READ);
		case Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED:
			return createExampleDialog(Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED);
		case Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD:
			return createExampleDialog(Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD);
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
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.internet_not_use));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_NO_CLOUD_SETUP:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.no_cloud_serivce_set_up));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_SUCCESS:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.sync_finish));
			builder.setCancelable(false);
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_SYNC_FAILED:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.sync_failed));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_NO_DATA_CLOUD:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.no_data_on_cloud));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							return;
						}
					});
			return builder.create();

		case Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED:
			builder.setCancelable(false);
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.sync_interrupt));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();

		default:
			return null;
		}
	}

	/**
	 * delete file csv after upload to cloud
	 */
	private void deleteFileAfterUpload() {
		// TODO Auto-generated method stub
		File file = new File(Contants.PATH_ID_FILES + "/" + mSelectFile);
		if (file.exists())
			file.delete();
	}
}
