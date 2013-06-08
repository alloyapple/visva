package visvateam.outsource.idmanager.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.activities.export.ExportDataGGDriveActivity;
import visvateam.outsource.idmanager.activities.synccloud.DropboxSettingActivity;
import visvateam.outsource.idmanager.activities.synccloud.GGDriveSettingActivity;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.exportcontroller.dropbox.DropBoxController;
import visvateam.outsource.idmanager.exportcontroller.dropbox.ReadFileViaDropBox;
import visvateam.outsource.idmanager.idxpwdatabase.ElementID;
import visvateam.outsource.idmanager.idxpwdatabase.GroupFolder;
import visvateam.outsource.idmanager.idxpwdatabase.IDxPWDataBaseHandler;
import visvateam.outsource.idmanager.idxpwdatabase.Password;
import visvateam.outsource.idmanager.util.NetworkUtility;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.idmanager.BillingHelper;
import com.idmanager.BillingService;

@SuppressLint("HandlerLeak")
public class SettingActivity extends BaseActivity {
	private static final String ID_ITEMS_PAYMENT_TO_UN_LIMIT = "0000000000000001";
	private static final String ID_ITEMS_PAYMENT_TO_NO_AD = "0000000000000002";
	private static final String ID_ITEMS_PAYMENT_TO_EXPORT = "0000000000000003";
	private CharSequence[] mListDataChoice;
	private CharSequence mSelectedFile = "";
	private CharSequence[] mListDataChoiceTemp;
	private IDxPWDataBaseHandler mDataBaseHandler;
	private boolean isExportData;

	// /////////////////////////////////////////////////////////////////////////
	// Your app-specific settings. //
	// /////////////////////////////////////////////////////////////////////////

	// Replace this with your app key and secret assigned by Dropbox.
	// Note that this is a really insecure way to do this, and you shouldn't
	// ship code which contains your key & secret in such an obvious way.
	// Obfuscation is good.
	// final static private String APP_KEY = "667sgm6m2mdu384";
	// final static private String APP_SECRET = "0ozy2rvw6ktyrnt";

	// If you'd like to change the access type to the full Dropbox instead of
	// an app folder, change this value.
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

	// You don't need to change these, leave them alone.
	final static private String ACCOUNT_PREFS_NAME = "prefs";
	final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";

	private DropboxAPI<AndroidAuthSession> mApi;
	private String fileExportName;
	private static final int PAYMENT_TO_UNLIMIT_ITEMS = 0;
	private static final int PAYMENT_TO_NO_AD = 1;
	private static final int PAYMENT_TO_EXPORT = 2;
	public int modePayment;
	
	public IdManagerPreference mPref;
	private List<GroupFolder> mGList;
	private List<ElementID> mEList;
	private List<Password> mPList;
	private int sizeOfGList;
	private int sizeOfEList;
	private int sizeOfPList;
	private String mGGAccountName;

	private ImageView mImgGGDrive;
	private ImageView mImgDropbox;

	// private IDxPWDataBaseHandler mIDxPWDataBaseHandler;
	private static final String TAG = "BillingService";
	public Handler mTransactionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "Transaction complete");
			Log.i(TAG, "Transaction status: "
					+ BillingHelper.latestPurchase.purchaseState);
			Log.i(TAG, "Item purchased is: "
					+ BillingHelper.latestPurchase.productId);

			if (BillingHelper.latestPurchase.isPurchased()) {
				if (modePayment == PAYMENT_TO_UNLIMIT_ITEMS) {
					mPref.setIsPaymentUnlimit(true);
				} else if (modePayment == PAYMENT_TO_NO_AD) {
					mPref.setIsPaymentNoAd(true);
				} else if (modePayment == PAYMENT_TO_EXPORT) {
					mPref.setIsPaymentExport(true);
				}
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		/* init database */
		initDatabase();

		mPref = IdManagerPreference.getInstance(this);
		initAdmod();
		startService(new Intent(this, BillingService.class));
		BillingHelper.setCompletedHandler(mTransactionHandler);

		mImgDropbox = (ImageView) findViewById(R.id.img_dropbox_logo);
		mImgGGDrive = (ImageView) findViewById(R.id.img_gg_drive_logo);

	}

	private void initDatabase() {
		// TODO Auto-generated method stub
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new IDxPWDataBaseHandler(this);
		mGList = mDataBaseHandler.getAllFolders();
		mEList = mDataBaseHandler.getAllElmentIds();
		mPList = mDataBaseHandler.getAllPasswords();
		sizeOfGList = mGList.size();
		sizeOfEList = mEList.size();
		sizeOfPList = mPList.size();
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
		Intent intentChangePW = new Intent(SettingActivity.this,
				EnterOldPasswordActivity.class);
		intentChangePW.putExtra(EnterOldPasswordActivity.KEY_MODE,
				EnterOldPasswordActivity.FROM_SETTING);
		startActivity(intentChangePW);
		finish();
	}

	public static void startActivity(Activity activity, int valueExtra) {
		Intent i = new Intent(activity, SettingActivity.class);
		i.putExtra("modeBundleSetting", valueExtra);
		activity.startActivity(i);
	}

	public void onSecurityMode(View v) {
		SetupSecurityModeActivity.startActivity(this);
	}

	public void onRemoveData(View v) {
		SetupRemoveDataActivity.startActivity(this);
	}

	public void onGoogle(View v) {
		Intent intentDropbox = new Intent(SettingActivity.this,
				GGDriveSettingActivity.class);
		startActivity(intentDropbox);
	}

	public void onRegisterEmailAddress(View v) {
		Intent intentRegisterEmail = new Intent(SettingActivity.this,
				RegisterEmailActivity.class);
		intentRegisterEmail.putExtra(Contants.CREATE_NEW_EMAIL, false);
		startActivity(intentRegisterEmail);
	}

	public void onDropbox(View v) {
		Intent intentDropbox = new Intent(SettingActivity.this,
				DropboxSettingActivity.class);
		startActivity(intentDropbox);
	}

	public void onUnlimitedItems(View v) {
		modePayment = PAYMENT_TO_UNLIMIT_ITEMS;
		if (!mPref.getIsPaymentUnlimit())
			showDialogRequestPayment(getResources().getString(
					R.string.message_pay_to_unlimit_item));
	}

	public void onNoAdmod(View v) {
		modePayment = PAYMENT_TO_NO_AD;
		if (!mPref.getIsPaymentNoAd())
			showDialogRequestPayment(getResources().getString(
					R.string.message_pay_to_no_ad));
	}

	public void showDialogRequestPayment(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon);
		builder.setTitle(getResources().getString(R.string.item_payment));
		builder.setMessage(message);
		builder.setPositiveButton(
				getResources().getString(R.string.confirm_ok),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (modePayment == PAYMENT_TO_UNLIMIT_ITEMS) {
							BillingHelper.requestPurchase(SettingActivity.this,
									ID_ITEMS_PAYMENT_TO_UN_LIMIT);
						} else if (modePayment == PAYMENT_TO_NO_AD) {
							// mPref.setIsPaymentUnlimit(IdManagerPreference.IS_PAYMENT_UNLIMIT,
							// true);
							if (BillingHelper.isBillingSupported()) {
								BillingHelper.requestPurchase(
										SettingActivity.this,
										ID_ITEMS_PAYMENT_TO_NO_AD);
							} else {
								Log.i(TAG, "Can't purchase on this device");

							}

						} else if (modePayment == PAYMENT_TO_EXPORT) {
							BillingHelper.requestPurchase(SettingActivity.this,
									ID_ITEMS_PAYMENT_TO_EXPORT);
						}
					}
				});
		builder.setNegativeButton(
				getResources().getString(R.string.confirm_cancel),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		builder.create().show();
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
			modePayment = PAYMENT_TO_EXPORT;
			// if (!mPref.getIsPaymentExport())
			// showDialogRequestPayment(getResources().getString(
			// R.string.message_pay_to_export));
			// else {
			if (mApi.getSession().isLinked()) {
				isExportData = true;
				showDialog(Contants.DIALOG_EXPORT_DATA);
			} else {
				showDialog(Contants.DIALOG_NO_CLOUD_SETUP);
			}
			// }

		} else
			showDialog(Contants.DIALOG_NO_NET_WORK);
	}

	private void showToast(final String string) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(SettingActivity.this, string, Toast.LENGTH_LONG)
						.show();
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

			if (mListDataChoiceTemp != null && mListDataChoiceTemp.length > 0) {
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
							deleteFileAfterUpload();
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.data_rewritten));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
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
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.sync_data_duplicate_msg));
			builder.setIcon(R.drawable.icon);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
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
		case Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED:
			builder.setCancelable(false);
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.success));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String file = mSelectedFile.toString();
							importFileCSVToDatabase(file);
							return;
						}
					});
			return builder.create();
		case Contants.DIALOG_MESSAGE_CHOICE_DATA_READ:
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle(getResources().getString(R.string.item_sync));
			alertBuilder.setIcon(R.drawable.icon);
			alertBuilder.setItems(mListDataChoice,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int item) {
							Toast.makeText(getApplicationContext(),
									mListDataChoice[item], Toast.LENGTH_SHORT)
									.show();
							mSelectedFile = mListDataChoice[item];
							startReadFileViaCloud(mSelectedFile, false);
						}
					});
			return alertBuilder.create();
		case Contants.DIALOG_EXPORT_DATA:
			builder.setTitle(getResources().getString(R.string.item_sync));
			builder.setMessage(getString(R.string.item_export_data));
			builder.setIcon(R.drawable.icon);
			final EditText input = new EditText(this);
			input.setId(Contants.TEXT_ID);
			input.setText("idxp.idp");
			builder.setView(input);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							fileExportName = input.getText().toString()
									+ ".csv";
							/* start export file */
							if (!"".equals(fileExportName)) {
								/* gen file csv */
								generateCsvFile(Contants.PATH_ID_FILES + "/"
										+ fileExportName);
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

	protected void importFileCSVToDatabase(String mSelectedFile) {
		// TODO Auto-generated method stub
		// File sdcard = Environment.getExternalStorageDirectory();
		File file = new File(Contants.PATH_ID_FILES + mSelectedFile);
		ArrayList<PasswordItem> mItems = new ArrayList<SettingActivity.PasswordItem>();
		String group = null, title = null, icon = null, url = null, note = null, image = null;
		String[] id = new String[Contants.MAX_ITEM_PASS_ID];
		String[] password = new String[Contants.MASTER_PASSWORD_ID];
		int fav = 0;
		if (file.exists()) {
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(file));
				String reader = "";
				int row = 0;
				try {
					while ((reader = in.readLine()) != null) {
						if (row > 0) {
							Log.e("xem chay may kan", "xem chay may kab " + row);
							String[] rowData = reader.split(",");
							ArrayList<String> rowDataList = new ArrayList<String>();
							// for(int )

							for (int i = 0; i < rowData.length; i++) {
								rowDataList.add(rowData[i]);
							}

							int size = rowDataList.size();
							if (size < 17)
								for (int i = size; i < 17; i++) {
									rowDataList.add("");
								}

							for (int i = 0; i < rowDataList.size(); i++) {
								Log.e("adjhfkshdf",
										"adsfkjh " + rowDataList.get(i));
								group = rowDataList.get(0);
								title = rowDataList.get(1);
								icon = rowDataList.get(2);
								fav = Integer.parseInt(rowDataList.get(3));
								url = rowDataList.get(4);
								note = rowDataList.get(5);
								image = rowDataList.get(6);

								id[0] = rowDataList.get(7);
								password[0] = rowDataList.get(8);
								id[1] = rowDataList.get(9);
								password[1] = rowDataList.get(10);
								id[2] = rowDataList.get(11);
								password[2] = rowDataList.get(12);
								id[3] = rowDataList.get(13);
								password[3] = rowDataList.get(14);
								id[4] = rowDataList.get(15);
								password[4] = rowDataList.get(16);
							}

							/* update to database */
							boolean isGExist = false;
							boolean isEExist = false;
							/* insert folder */
							if (sizeOfGList > 0)
								for (int i = 0; i < sizeOfGList; i++) {
									if (!group.equals(mGList.get(i).getgName()))
										isGExist = false;
									else
										isGExist = true;
									if (i == (sizeOfGList - 1) && !isGExist) {
										int gId = 0;
										for (int j = 0; j < sizeOfGList; j++)
											if (gId < mGList.get(j).getgId())
												gId = mGList.get(j).getgId();
										gId++;
										GroupFolder folder = new GroupFolder(
												gId, group, 0,
												Contants.MASTER_PASSWORD_ID, 0);
										mDataBaseHandler.addNewFolder(folder);
										mGList.add(folder);
										sizeOfGList++;
									}
								}
							else if (!isGExist) {
								isGExist = true;
								int gId = 0;
								for (int j = 0; j < sizeOfGList; j++)
									if (gId < mGList.get(j).getgId())
										gId = mGList.get(j).getgId();
								gId++;
								GroupFolder folder = new GroupFolder(gId,
										group, 0, Contants.MASTER_PASSWORD_ID,
										0);
								mDataBaseHandler.addNewFolder(folder);
								mGList.add(folder);
								sizeOfGList++;
							}

							/* insert element */
							for (int i = 0; i < sizeOfGList; i++) {
								List<ElementID> elementList = mDataBaseHandler
										.getAllElementIdByGroupFolderId(mGList
												.get(i).getgId());
								if (elementList.size() > 0)
									for (int j = 0; j < elementList.size(); j++) {
										if (title.equals(elementList.get(j)
												.geteTitle()))
											isEExist = true;

										if (j == sizeOfEList - 1 && !isEExist) {
											int eId = sizeOfEList;
											long timeStamp = System
													.currentTimeMillis();
											for (int k = 0; k < mEList.size(); k++)
												if (eId < mEList.get(k)
														.geteId())
													eId = mEList.get(k)
															.geteId();
											eId++;
											ElementID element = new ElementID(
													eId,
													mGList.get(i).getgId(),
													title, icon, timeStamp,
													fav, 0, url, note, image, 0);
											mDataBaseHandler
													.addNewElementId(element);

											mEList.add(element);
											sizeOfEList++;
										}
									}
								else if (!isEExist) {
									isEExist = true;
									int eId = sizeOfEList;
									long timeStamp = System.currentTimeMillis();
									eId++;
									ElementID element = new ElementID(eId,
											mGList.get(i).getgId(), title,
											icon, timeStamp, fav, 0, url, note,
											image, 0);
									mDataBaseHandler.addNewElementId(element);
									mEList.add(element);
									sizeOfEList++;
								}

							}

							/* insert to password */
							int elementId = 0;
							for (int i = 0; i < sizeOfEList; i++) {
								if (title.equals(mEList.get(i).geteTitle()))
									elementId = mEList.get(i).geteId();
							}
							mDataBaseHandler
									.deletePasswordByElementId(elementId);
							Log.e("item", "item.size " + mItems.size());
							for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
								if (!"".equals(id[i])
										|| !"".equals(password[i])) {
									int pwId = sizeOfPList;
									for (int k = 0; k < sizeOfPList; k++)
										if (pwId < mPList.get(k)
												.getPasswordId())
											pwId = mPList.get(k)
													.getPasswordId();
									Password passWord = new Password(pwId,
											elementId, id[i], password[i]);
									mDataBaseHandler.addNewPassword(passWord);
									mPList.add(passWord);
									sizeOfPList++;
								}
							}
						}
						row++;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

	@SuppressWarnings("unused")
	private void exportDataToGGDrive() {
		// TODO Auto-generated method stub
		Intent intentExportData = new Intent(SettingActivity.this,
				ExportDataGGDriveActivity.class);
		intentExportData.putExtra(Contants.IS_EXPORT_FILE, isExportData);
		startActivity(intentExportData);
	}

	private void generateCsvFile(String sFileName) {
		List<GroupFolder> groupList = mDataBaseHandler.getAllFolders();
		List<ElementID> elementList = mDataBaseHandler.getAllElmentIds();

		int sizeOfElementList = elementList.size();
		int sizeOfgroupList = groupList.size();
		try {
			FileWriter writer = new FileWriter(sFileName);
			writer.append("group");
			writer.append(",");
			writer.append("title");
			writer.append(",");
			writer.append("icon");
			writer.append(",");
			writer.append("fav");
			writer.append(",");
			writer.append("url");
			writer.append(",");
			writer.append("note");
			writer.append(",");
			writer.append("image");
			writer.append(",");

			writer.append("id1");
			writer.append(",");
			writer.append("pa1");
			writer.append(",");

			writer.append("id2");
			writer.append(",");
			writer.append("pa2");
			writer.append(",");

			writer.append("id3");
			writer.append(",");
			writer.append("pa3");
			writer.append(",");

			writer.append("id4");
			writer.append(",");
			writer.append("pa4");
			writer.append(",");

			writer.append("id5");
			writer.append(",");
			writer.append("pa5");
			writer.append(",");

			writer.append("\n");
			// generate whatever data you want
			for (int i = 0; i < sizeOfElementList; i++) {
				GroupFolder groupFolder = null;
				int groupFolderId = elementList.get(i).geteGroupId();
				for (int j = 0; j < sizeOfgroupList; j++)
					if (groupFolderId == groupList.get(j).getgId())
						groupFolder = groupList.get(j);
				writer.append("" + groupFolder.getgName());
				writer.append(",");
				writer.append("" + elementList.get(i).geteTitle());
				writer.append(",");
				writer.append("");
				writer.append(",");
				writer.append("" + elementList.get(i).geteFavourite());
				writer.append(",");
				writer.append("" + elementList.get(i).geteUrl());
				writer.append(",");
				writer.append("" + elementList.get(i).geteNote());
				writer.append(",");
				writer.append("");
				writer.append(",");

				List<Password> passwordList = mDataBaseHandler
						.getAllPasswordByElementId(elementList.get(i).geteId());
				int sizeOfPassWordList = passwordList.size();
				for (int k = 0; k < sizeOfPassWordList; k++) {
					writer.append("" + passwordList.get(k).getTitleNameId());
					writer.append(",");
					writer.append("" + passwordList.get(k).getPassword());
					writer.append(",");
				}

				writer.append("\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);
		mGGAccountName = mPref.getGoogleAccNameSession();
		if (mApi.getSession().isLinked()) {
			mImgDropbox.setBackgroundResource(R.drawable.logo_dropbox_selected);
			mImgGGDrive.setBackgroundResource(R.drawable.logo_google);
		} else if (!"".equals(mGGAccountName)) {
			mImgDropbox.setBackgroundResource(R.drawable.logo_dropbox);
			mImgGGDrive
					.setBackgroundResource(R.drawable.logo_google_drive_selected);
		} else {
			mImgDropbox.setBackgroundResource(R.drawable.logo_dropbox);
			mImgGGDrive.setBackgroundResource(R.drawable.logo_google);
		}

	}

	private void startSyncToCloud(String fileExportName,
			boolean isCheckDuplicated) {
		// TODO Auto-generated method stub
		File fileExport = new File(Contants.PATH_ID_FILES + "/"
				+ fileExportName);
		if (fileExport.exists()) {
			DropBoxController newFile = new DropBoxController(
					SettingActivity.this, mApi, Contants.FOLDER_ON_DROPBOX_CSV,
					fileExport, mHandler, isCheckDuplicated);
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
	private void startReadFileViaCloud(CharSequence fileName,
			boolean isCheckFile) {
		Log.e("isCheckFile", "isCheckFile " + isCheckFile);
		File file = new File(Contants.PATH_ID_FILES);
		if (!file.exists())
			file.mkdirs();
		String mFilePath = file.getAbsolutePath();
		ReadFileViaDropBox readFile = new ReadFileViaDropBox(
				SettingActivity.this, mApi, Contants.FOLDER_ON_DROPBOX_CSV,
				mFilePath, mHandler, fileName, isCheckFile);
		readFile.execute();
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
			else if (msg.arg1 == Contants.DIALOG_MESSAGE_READ_LIST_DATA) {
				Object object = msg.obj;

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
				Intent intent = new Intent(SettingActivity.this, ChoiceCSVImportType.class);
				intent.putExtra(Contants.KEY_CHOICE_CSV_FILE, mSelectedFile);
				startActivity(intent);
//				showDialog(Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED);
			}
		};
	};

	class PasswordItem {
		String titleId;
		String password;
	}

	@Override
	protected void onDestroy() {
		BillingHelper.stopService();
		super.onDestroy();
	}

}
