package visvateam.outsource.idmanager.activities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import net.sqlcipher.database.SQLiteDatabase;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import visvateam.outsource.idmanager.activities.export.ExportDataGGDriveActivity;
import visvateam.outsource.idmanager.activities.synccloud.DropboxSettingActivity;
import visvateam.outsource.idmanager.activities.synccloud.GGDriveSettingActivity;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.FolderDatabase;
import visvateam.outsource.idmanager.database.IDDataBase;
import visvateam.outsource.idmanager.exportcontroller.dropbox.DropBoxController;
import visvateam.outsource.idmanager.exportcontroller.dropbox.ReadFileViaDropBox;
import visvateam.outsource.idmanager.util.NetworkUtility;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SettingActivity extends Activity {

	private CharSequence[] mListDataChoice;
	private CharSequence mSelectedFile = "";
	private CharSequence[] mListDataChoiceTemp;
	private DataBaseHandler mDataBaseHandler;
	private boolean isExportData;

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

	private DropboxAPI<AndroidAuthSession> mApi;
	private String fileExportName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		/* init database */
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);
		initAdmod();

	}

	public void initAdmod() {
		AdView adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	public void onReturn(View v) {
		finish();
	}

	public void onChangeMasterPass(View v) {
		Intent intentChangePW = new Intent(SettingActivity.this, MasterPasswordChangeActivity.class);
		intentChangePW.putExtra("isChangePW", true);
		startActivity(intentChangePW);
		finish();
	}

	public void onSecurityMode(View v) {
		SetupSecurityModeActivity.startActivity(this);
	}

	public void onRemoveData(View v) {
		SetupRemoveDataActivity.startActivity(this);
	}

	public void onGoogle(View v) {
		Intent intentDropbox = new Intent(SettingActivity.this, GGDriveSettingActivity.class);
		startActivity(intentDropbox);
	}

	public void onDropbox(View v) {
		Intent intentDropbox = new Intent(SettingActivity.this, DropboxSettingActivity.class);
		startActivity(intentDropbox);
	}

	public void onUnlimitedItems(View v) {
		showToast("This feature is coming soon");
	}

	public void onNoAdmod(View v) {
		showToast("This feature is coming soon");
	}

	/**
	 * read file via cloud
	 * 
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	public void onReadFileviaCloud(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			if (mApi.getSession().isLinked()) {
				isExportData = false;
				String fileName = "";
				startReadFileViaCloud(fileName, false);
			} else
				showDialog(Contants.DIALOG_NO_CLOUD_SETUP);
		} else
			showDialog(Contants.DIALOG_NO_NET_WORK);
	}

	/**
	 * export data to cloud
	 * 
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	public void onExportData(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			if (mApi.getSession().isLinked()) {
				isExportData = true;
				showDialog(Contants.DIALOG_EXPORT_DATA);
			} else {
				showDialog(Contants.DIALOG_NO_CLOUD_SETUP);
			}
		} else
			showDialog(Contants.DIALOG_NO_NET_WORK);
	}

	private void showToast(final String string) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(SettingActivity.this, string, Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * If a dialog has already been created, this is called to reset the dialog
	 * before showing it a 2nd time. Optional.
	 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {

		case Contants.DIALOG_MESSAGE_CHOICE_DATA_READ:

			if (mListDataChoiceTemp.length > 0) {
				mListDataChoice = new String[mListDataChoiceTemp.length];
				mListDataChoice = mListDataChoiceTemp;

				Log.e("onResume " + mListDataChoiceTemp.length, "asdgsfgdgd  "
						+ mListDataChoice.length);
			}
			break;
		}
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
			builder.setTitle("Id Manager");
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
							deleteFileAfterUpload();
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
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_NO_DATA_CLOUD:
			builder.setTitle(getString(R.string.app_name));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.no_data_on_cloud));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							deleteFileAfterUpload();
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
							startReadFileViaCloud(mSelectedFile, true);
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
							startSyncToCloud(fileExportName, false);
							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							deleteFileAfterUpload();
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
							deleteFileAfterUpload();
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
		case Contants.DIALOG_MESSAGE_CHOICE_DATA_READ:
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle(getString(R.string.app_name) + "  " + mListDataChoice.length);
			alertBuilder.setIcon(R.drawable.icon);
			alertBuilder.setItems(mListDataChoice, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					Toast.makeText(getApplicationContext(), mListDataChoice[item],
							Toast.LENGTH_SHORT).show();
					mSelectedFile = mListDataChoice[item];
					startReadFileViaCloud(mSelectedFile, false);
				}
			});
			return alertBuilder.create();
		case Contants.DIALOG_EXPORT_DATA:
			builder.setTitle(getString(R.string.app_name));
			builder.setMessage(getString(R.string.item_export_data));
			builder.setIcon(R.drawable.icon);
			final EditText input = new EditText(this);
			input.setId(Contants.TEXT_ID);
			input.setText("idxp.idp");
			builder.setView(input);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							/* add new folder to database */
							fileExportName = input.getText().toString() + ".csv";
							/* start export file */
							if (!"".equals(fileExportName)) {
								/* gen file csv */
								generateCsvFile(Contants.PATH_ID_FILES + "/" + fileExportName);
								startSyncToCloud(fileExportName, true);
							} else
								return;
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
		default:
			return null;
		}
	}

	/**
	 * delete file csv after upload to cloud
	 */
	private void deleteFileAfterUpload() {
		// TODO Auto-generated method stub
		File file = new File(Contants.PATH_ID_FILES + "/" + fileExportName);
		if (file.exists())
			file.delete();
	}

//	private void exportDataToGGDrive() {
//		// TODO Auto-generated method stub
//		Intent intentExportData = new Intent(SettingActivity.this, ExportDataGGDriveActivity.class);
//		intentExportData.putExtra(Contants.IS_EXPORT_FILE, isExportData);
//		startActivity(intentExportData);
//	}

	private void generateCsvFile(String sFileName) {
		java.util.List<FolderDatabase> folderList = mDataBaseHandler.getAllFolders();
		java.util.List<IDDataBase> idList = mDataBaseHandler.getAllIDs();
		try {
			FileWriter writer = new FileWriter(sFileName);
			writer.append("");
			writer.append(",");
			writer.append("Folder Tables");
			writer.append("\n");
			writer.append("Folder Name");
			writer.append("\n");
			for (int i = 0; i < folderList.size(); i++) {
				writer.append("" + folderList.get(i).getFolderName());
				writer.append("\n");
			}
			writer.append("\n");
			writer.append("");
			writer.append(",");
			writer.append("IDxPassword tables");
			writer.append("\n");
			writer.append("Name");
			writer.append(",");
			writer.append("Url");
			writer.append(",");
			writer.append("Note");
			writer.append(",");
			writer.append("Image memo");
			writer.append(",");

			writer.append("ID1");
			writer.append(",");
			writer.append("Pass1");
			writer.append(",");

			writer.append("ID2");
			writer.append(",");
			writer.append("Pass2");
			writer.append(",");

			writer.append("ID3");
			writer.append(",");
			writer.append("Pass3");
			writer.append(",");

			writer.append("ID4");
			writer.append(",");
			writer.append("Pass4");
			writer.append(",");

			writer.append("ID5");
			writer.append(",");
			writer.append("Pass5");
			writer.append(",");

			writer.append("ID6");
			writer.append(",");
			writer.append("Pass6");
			writer.append(",");

			writer.append("ID7");
			writer.append(",");
			writer.append("Pass7");
			writer.append(",");

			writer.append("ID8");
			writer.append(",");
			writer.append("Pass8");
			writer.append(",");

			writer.append("ID9");
			writer.append(",");
			writer.append("Pass9");
			writer.append(",");

			writer.append("ID10");
			writer.append(",");
			writer.append("Pass10");
			writer.append(",");

			writer.append("ID11");
			writer.append(",");
			writer.append("Pass11");
			writer.append(",");

			writer.append("ID12");
			writer.append(",");
			writer.append("Pass12");
			writer.append("\n");

			for (int i = 0; i < idList.size(); i++) {

				writer.append("" + idList.get(i).getTitleRecord());
				writer.append(",");
				writer.append("" + idList.get(i).getUrl());
				writer.append(",");
				writer.append("" + idList.get(i).getNote());
				writer.append(",");
				writer.append("" + idList.get(i).getImageMemo());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId1());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId1());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId2());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId2());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId3());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId3());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId4());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId4());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId5());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId5());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId6());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId6());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId7());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId7());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId8());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId8());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId9());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId9());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId10());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId10());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId11());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId11());
				writer.append(",");

				writer.append("" + idList.get(i).getTitleId12());
				writer.append(",");
				writer.append("" + idList.get(i).getDataId12());
				writer.append("\n");

			}

			// generate whatever data you want

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		// Log.e("onResume", "inasdf");
		// if(mListDataChoiceTemp.length > 0){
		// mListDataChoice = new String[mListDataChoiceTemp.length];
		// mListDataChoice = mListDataChoiceTemp;
		// }
	}

	private void startSyncToCloud(String fileExportName, boolean isCheckDuplicated) {
		// TODO Auto-generated method stub
		File fileExport = new File(Contants.PATH_ID_FILES + "/" + fileExportName);
		if (fileExport.exists()) {
			DropBoxController newFile = new DropBoxController(SettingActivity.this, mApi,
					Contants.FOLDER_ON_DROPBOX_CSV, fileExport, mHandler, isCheckDuplicated);
			newFile.execute();
		} else {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED;
			mHandler.sendMessage(msg);
			showToast("file not found");
		}
	}

	/**
	 * start read file via cloud
	 */
	private void startReadFileViaCloud(CharSequence fileName, boolean isCheckFile) {
		// DropBoxDownloadFile download = new
		// DropBoxDownloadFile(DropBoxSyncActivity.this, mApi,
		// Contants.FOLDER_ON_DROPBOX_DB, dbFilePath);
		// download.execute();
		Log.e("isCheckFile", "isCheckFile "+isCheckFile);
		File file = new File(Contants.PATH_ID_FILES);
		if (!file.exists())
			file.mkdirs();
		String mFilePath = file.getAbsolutePath();
		ReadFileViaDropBox readFile = new ReadFileViaDropBox(SettingActivity.this, mApi,
				Contants.FOLDER_ON_DROPBOX_CSV, mFilePath, mHandler, fileName, isCheckFile);
		readFile.execute();
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings({ "deprecation", "unchecked" })
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
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_LIST_DATA) {
				Object object = msg.obj;
				Log.e("adfadf", "adsfas ");
				ArrayList<String> mFileList = (ArrayList<String>) object;
				mListDataChoice = new String[mFileList.size()];
				mListDataChoiceTemp = new String[mFileList.size()];
				for (int i = 0; i < mFileList.size(); i++) {
					mListDataChoice[i] = mFileList.get(i);
					mListDataChoiceTemp[i] = mFileList.get(i);
				}
				if (mListDataChoiceTemp.length > 0)
					showDialog(Contants.DIALOG_MESSAGE_CHOICE_DATA_READ);
			} else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD) {
				showDialog(Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD);
			} else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED) {
				showDialog(Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED);
			}
		};
	};

}
