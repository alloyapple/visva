package com.japanappstudio.IDxPassword.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import com.japanappstudio.IDxPassword.activities.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.idxpwdatabase.ElementID;
import com.japanappstudio.IDxPassword.idxpwdatabase.GroupFolder;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;
import com.japanappstudio.IDxPassword.idxpwdatabase.Password;

public class ChoiceCSVImportType extends Activity implements OnClickListener {

	// You don't need to change these, leave them alone.
	final static private String ACCOUNT_PREFS_NAME = "prefs";
	final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
	// If you'd like to change the access type to the full Dropbox instead of
	// an app folder, change this value.
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;

	private Button btnIDxPassword;
	private Button btnLastPass;
	private Button btnKeyPass;
	private Button btnPasswordManager;
	private Button btnIDmanager;

	private String fileCSVToImport;
	private IDxPWDataBaseHandler mDataBaseHandler;

	public IdManagerPreference mPref;
	private List<GroupFolder> mGList;
	private List<ElementID> mEList;
	private List<Password> mPList;
	private int sizeOfGList;
	private int sizeOfEList;
	private int sizeOfPList;

	@SuppressWarnings("unused")
	private DropboxAPI<AndroidAuthSession> mApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_choice_csv_import_type);

		fileCSVToImport = getIntent().getExtras().getString(
				Contants.KEY_CHOICE_CSV_FILE);
		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);
		mPref = IdManagerPreference.getInstance(this);
		initDatabase();
		initControl();

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

	private void initControl() {
		// TODO Auto-generated method stub
		btnIDmanager = (Button) findViewById(R.id.btn_csv_format_idmanager);
		btnIDmanager.setOnClickListener(this);
		btnIDxPassword = (Button) findViewById(R.id.btn_csv_format_id_pw);
		btnIDxPassword.setOnClickListener(this);
		btnKeyPass = (Button) findViewById(R.id.btn_csv_format_key_pass);
		btnKeyPass.setOnClickListener(this);
		btnLastPass = (Button) findViewById(R.id.btn_csv_format_last_pass);
		btnLastPass.setOnClickListener(this);
		btnPasswordManager = (Button) findViewById(R.id.btn_csv_format_password_manager);
		btnPasswordManager.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnIDmanager) {
			importFileCSVToDatabaseFormatIDManager(fileCSVToImport);
		} else if (v == btnIDxPassword) {
			importFileCSVToDatabaseFormatIDPassword(fileCSVToImport);
		} else if (v == btnKeyPass) {
			importFileCSVToDatabaseFormatKeePass2(fileCSVToImport);
		} else if (v == btnLastPass) {
			importFileCSVToDatabaseFormatLastPass(fileCSVToImport);
		} else if (v == btnPasswordManager) {

		}
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private Handler mHandler = new Handler() {
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
			}
		};
	};

	protected void importFileCSVToDatabaseFormatIDPassword(String mSelectedFile) {
		// TODO Auto-generated method stub
		File file = new File(Contants.PATH_ID_FILES + mSelectedFile);
		// ArrayList<PasswordItem> mItems = new
		// ArrayList<SettingActivity.PasswordItem>();
		String group = null, title = null, icon = null, url = null, note = null, image = null;
		String[] id = new String[Contants.MAX_ITEM_PASS_ID];
		String[] password = new String[Contants.MAX_ITEM_PASS_ID];
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

							// for (int i = 0; i < rowDataList.size(); i++) {
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
							// }

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
													title, new byte[] {},
													timeStamp, fav, 0, url,
													note, new byte[] {}, 0);
											mDataBaseHandler
													.addElement(element);

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
											new byte[] {}, timeStamp, fav, 0,
											url, note, new byte[] {}, 0);
									mDataBaseHandler.addElement(element);
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
							// Log.e("item", "item.size " + mItems.size());
							for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
								int pwId = sizeOfPList;
								for (int k = 0; k < sizeOfPList; k++)
									if (pwId < mPList.get(k).getPasswordId())
										pwId = mPList.get(k).getPasswordId();
								pwId++;
								Password passWord = new Password(pwId,
										elementId, id[i], password[i]);
								mDataBaseHandler.addNewPassword(passWord);
								mPList.add(passWord);
								sizeOfPList++;
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
		finish();
	}

	protected void importFileCSVToDatabaseFormatIDManager(String mSelectedFile) {
		// TODO Auto-generated method stub
		if (mPref.getNumberItems() >= Contants.MAX_ELEMENT)
			return;
		File file = new File(Contants.PATH_ID_FILES + mSelectedFile);
		String group = null, title = null, icon = null, url = null, note = null, image = null;
		String[] titleItem = new String[Contants.MAX_ITEM_PASS_ID];
		String[] contentItem = new String[Contants.MAX_ITEM_PASS_ID];
		int fav = 0;
		if (file.exists()) {
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(file));
				String reader = "";
				int row = 0;
				try {
					while ((reader = in.readLine()) != null) {
						String[] rowData = reader.split(",");
						Log.i("groupName", "+" + rowData[0] + "+");
						for (int i = 0; i < rowData.length; i++) {
							if (rowData[i].startsWith("\"")
									&& rowData[i].endsWith("\"")) {
								String tmp = rowData[i].substring(1,
										rowData[i].length() - 1);
								rowData[i] = tmp.toString();
								rowData[i] = new String(rowData[i].getBytes(),
										"Shift-JIS");
							}
						}
						if (row == 0) {
							if (!rowData[0].equals("Folder or Item")) {
								showDialogInvalidFomat();
								return;
							}
						}
						if (row > 0) {
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

							group = rowDataList.get(0);
							title = rowDataList.get(1);
							titleItem[0] = rowDataList.get(2);
							contentItem[0] = rowDataList.get(3);
							titleItem[1] = rowDataList.get(4);
							contentItem[1] = rowDataList.get(5);
							titleItem[2] = rowDataList.get(6);
							contentItem[2] = rowDataList.get(7);
							titleItem[3] = rowDataList.get(8);
							contentItem[3] = rowDataList.get(9);
							titleItem[4] = "";
							contentItem[4] = "";

							url = rowDataList.get(10);
							note = rowDataList.get(13);
							fav = 0;

							/* update to database */
							boolean isGExist = false;
							boolean isEExist = false;
							int groupID = 0;
							int elementId = 0;
							/* insert folder */
							if (sizeOfGList > 0)
								for (int i = 0; i < sizeOfGList; i++) {
									// Log.i("groupName", "+"+group+"+");
									if (!group.equals(mGList.get(i).getgName()))
										isGExist = false;
									else {
										isGExist = true;
										groupID = mGList.get(i).getgId();
										break;
									}
									if (i == (sizeOfGList - 1) && !isGExist) {

										int gId = 0;
										for (int j = 0; j < sizeOfGList; j++)
											if (gId < mGList.get(j).getgId())
												gId = mGList.get(j).getgId();
										gId++;
										isGExist = true;
										GroupFolder folder = new GroupFolder(
												gId, group, 0,
												Contants.MASTER_PASSWORD_ID, 0);
										mDataBaseHandler.addNewFolder(folder);
										mGList.add(folder);
										sizeOfGList++;
										groupID = gId;
										// Log.i("addFolder", ""+gId);
										break;
									}
								}
							else if (!isGExist) {
								isGExist = true;
								int gId = 0;
								for (int j = 0; j < sizeOfGList; j++)
									if (gId < mGList.get(j).getgId())
										gId = mGList.get(j).getgId();
								gId++;
								groupID = gId;
								GroupFolder folder = new GroupFolder(gId,
										group, 0, Contants.MASTER_PASSWORD_ID,
										0);
								mDataBaseHandler.addNewFolder(folder);
								mGList.add(folder);
								sizeOfGList++;
							}

							/* insert element */

							List<ElementID> elementList = mDataBaseHandler
									.getAllElementIdByGroupFolderId(groupID);
							if (elementList.size() > 0)
								for (int j = 0; j < elementList.size(); j++) {
									if (title.equals(elementList.get(j)
											.geteTitle())) {
										isEExist = true;
										elementId = elementList.get(j).geteId();
										break;
									}

									if (j == sizeOfEList - 1 && !isEExist) {
										int eId = sizeOfEList;
										elementId = eId;
										long timeStamp = System
												.currentTimeMillis();
										for (int k = 0; k < mEList.size(); k++)
											if (eId < mEList.get(k).geteId())
												eId = mEList.get(k).geteId();
										eId++;
										ElementID element = new ElementID(eId,
												groupID, title, new byte[] {},
												timeStamp, fav, 0, url, note,
												new byte[] {}, 0);
										mDataBaseHandler.addElement(element);

										mEList.add(element);
										sizeOfEList++;
									}
								}
							else if (!isEExist) {
								isEExist = true;
								int eId = sizeOfEList;
								elementId = eId;
								long timeStamp = System.currentTimeMillis();
								eId++;
								ElementID element = new ElementID(eId, groupID,
										title, new byte[] {}, timeStamp, fav,
										0, url, note, new byte[] {}, 0);
								mDataBaseHandler.addElement(element);
								mEList.add(element);
								sizeOfEList++;
							}
							/* insert to password */
							mDataBaseHandler
									.deletePasswordByElementId(elementId);
							for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
								int pwId = sizeOfPList;
								for (int k = 0; k < sizeOfPList; k++)
									if (pwId < mPList.get(k).getPasswordId())
										pwId = mPList.get(k).getPasswordId();
								pwId++;
								Password passWord = new Password(pwId,
										elementId, titleItem[i], contentItem[i]);
								mDataBaseHandler.addNewPassword(passWord);
								mPList.add(passWord);
								sizeOfPList++;
							}
						}
						row++;

					}
					finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					showDialogInvalidFomat();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	protected void importFileCSVToDatabaseFormatKeePass2(String mSelectedFile) {
		// TODO Auto-generated method stub
		// File sdcard = Environment.getExternalStorageDirectory();
		if (mPref.getNumberItems() >= Contants.MAX_ELEMENT)
			return;
		File file = new File(Contants.PATH_ID_FILES + mSelectedFile);
		String group = null, title = null, icon = null, url = null, note = null, image = null;
		String[] titleItem = new String[Contants.MAX_ITEM_PASS_ID];
		String[] contentItem = new String[Contants.MAX_ITEM_PASS_ID];
		String buffer = null;
		int fav = 0;
		if (file.exists()) {
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(file));
				String reader = "";
				int row = 0;
				try {
					while (true) {
						reader = in.readLine();
//						 Log.i("account", row+"");
//						 Log.i("Keypass", reader);
						String[] tmpData = new String[] {};
						if (reader != null) {
							tmpData = reader.split(",");
							for (int i = 0; i < tmpData.length; i++) {
								if (tmpData[i].startsWith("\"")
										&& tmpData[i].endsWith("\"")) {
									String tmp = tmpData[i].substring(1,
											tmpData[i].length() - 1);
									tmpData[i] = tmp.toString();
									tmpData[i] = new String(
											tmpData[i].getBytes(), "UTF8");
								}
							}
						}

						if (row == 0) {
							if (!tmpData[0].equals("Account")) {
								showDialogInvalidFomat();
								return;
							}
						}
						if (tmpData.length>0&&tmpData.length < 5) {
							buffer += "\n" + reader;
							row++;
							continue;
						}
						String[] rowData = new String[] {};
						if (buffer != null) {
							rowData = buffer.split(",");
							for (int i = 0; i < rowData.length; i++) {
								if (rowData[i].startsWith("\"")
										&& rowData[i].endsWith("\"")) {
									String tmp = rowData[i].substring(1,
											rowData[i].length() - 1);
									rowData[i] = tmp.toString();
									rowData[i] = new String(
											rowData[i].getBytes(), "UTF8");
								}
							}

							if (row > 0) {

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

								group = "KeyPass";
								title = rowDataList.get(0);
								titleItem[0] = "ID";
								contentItem[0] = rowDataList.get(1);
								titleItem[1] = "Password";
								contentItem[1] = rowDataList.get(2);
								titleItem[2] = "";
								contentItem[2] = "";
								titleItem[3] = "";
								contentItem[3] = "";
								titleItem[4] = "";
								contentItem[4] = "";

								url = rowDataList.get(3);
								note = rowDataList.get(4);
								fav = 0;

								/* update to database */
								boolean isGExist = false;
								boolean isEExist = false;
								int groupID = 0;
								int elementId = 0;
								/* insert folder */
								if (sizeOfGList > 0)
									for (int i = 0; i < sizeOfGList; i++) {
										if (!group.equals(mGList.get(i)
												.getgName()))
											isGExist = false;
										else {
											isGExist = true;
											groupID = mGList.get(i).getgId();
											break;
										}
										if (i == (sizeOfGList - 1) && !isGExist) {
											int gId = 0;
											for (int j = 0; j < sizeOfGList; j++)
												if (gId < mGList.get(j)
														.getgId())
													gId = mGList.get(j)
															.getgId();
											gId++;
											GroupFolder folder = new GroupFolder(
													gId,
													group,
													0,
													Contants.MASTER_PASSWORD_ID,
													0);
											mDataBaseHandler
													.addNewFolder(folder);
											mGList.add(folder);
											sizeOfGList++;
											groupID = gId;
										}
									}
								else if (!isGExist) {
									isGExist = true;
									int gId = 0;
									for (int j = 0; j < sizeOfGList; j++)
										if (gId < mGList.get(j).getgId())
											gId = mGList.get(j).getgId();
									gId++;
									groupID = gId;
									GroupFolder folder = new GroupFolder(gId,
											group, 0,
											Contants.MASTER_PASSWORD_ID, 0);
									mDataBaseHandler.addNewFolder(folder);
									mGList.add(folder);
									sizeOfGList++;
								}

								/* insert element */

								List<ElementID> elementList = mDataBaseHandler
										.getAllElementIdByGroupFolderId(groupID);
								if (elementList.size() > 0)
									for (int j = 0; j < elementList.size(); j++) {
										if (title.equals(elementList.get(j)
												.geteTitle())) {
											isEExist = true;
											elementId = elementList.get(j)
													.geteId();
											break;
										}

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
											elementId = eId;
											ElementID element = new ElementID(
													eId, groupID, title,
													new byte[] {}, timeStamp,
													fav, 0, url, note,
													new byte[] {}, 0);
											mDataBaseHandler
													.addElement(element);
											mEList.add(element);
											sizeOfEList++;
										}
									}
								else if (!isEExist) {
									isEExist = true;
									int eId = sizeOfEList;
									long timeStamp = System.currentTimeMillis();
									eId++;
									elementId = eId;
									ElementID element = new ElementID(eId,
											groupID, title, new byte[] {},
											timeStamp, fav, 0, url, note,
											new byte[] {}, 0);
									mDataBaseHandler.addElement(element);
									mEList.add(element);
									sizeOfEList++;
								}
								/* insert to password */
								mDataBaseHandler
										.deletePasswordByElementId(elementId);
								for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
									int pwId = sizeOfPList;
									for (int k = 0; k < sizeOfPList; k++)
										if (pwId < mPList.get(k)
												.getPasswordId())
											pwId = mPList.get(k)
													.getPasswordId();
									pwId++;
									Password passWord = new Password(pwId,
											elementId, titleItem[i],
											contentItem[i]);
									mDataBaseHandler.addNewPassword(passWord);
									mPList.add(passWord);
									sizeOfPList++;
								}
							}
						}
						if (tmpData.length == 5) {
							buffer = reader;
						}
						if (reader == null)
							break;
						row++;
					}
					finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					showDialogInvalidFomat();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	protected void importFileCSVToDatabaseFormatKeePass(String mSelectedFile) {
		// TODO Auto-generated method stub
		// File sdcard = Environment.getExternalStorageDirectory();
		if (mPref.getNumberItems() >= Contants.MAX_ELEMENT)
			return;
		File file = new File(Contants.PATH_ID_FILES + mSelectedFile);
		String group = null, title = null, icon = null, url = null, note = null, image = null;
		String[] titleItem = new String[Contants.MAX_ITEM_PASS_ID];
		String[] contentItem = new String[Contants.MAX_ITEM_PASS_ID];
		int fav = 0;
		if (file.exists()) {
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(file));
				String reader = "";
				int row = 0;
				try {
					while ((reader = in.readLine()) != null) {
						// Log.i("account", row+"");
						// Log.i("Keypass", reader);
						String[] rowData = reader.split(",");
						for (int i = 0; i < rowData.length; i++) {
							if (rowData[i].startsWith("\"")
									&& rowData[i].endsWith("\"")) {
								String tmp = rowData[i].substring(1,
										rowData[i].length() - 1);
								rowData[i] = tmp.toString();
								rowData[i] = new String(rowData[i].getBytes(),
										"UTF8");
							}
						}
						if (row == 0) {

							if (!rowData[0].equals("Account")) {
								showDialogInvalidFomat();
								return;
							}
						}
						if (row > 0) {

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

							group = "KeyPass";
							title = rowDataList.get(0);
							titleItem[0] = "ID";
							contentItem[0] = rowDataList.get(1);
							titleItem[1] = "Password";
							contentItem[1] = rowDataList.get(2);
							titleItem[2] = "";
							contentItem[2] = "";
							titleItem[3] = "";
							contentItem[3] = "";
							titleItem[4] = "";
							contentItem[4] = "";

							url = rowDataList.get(3);
							note = rowDataList.get(4);
							fav = 0;

							/* update to database */
							boolean isGExist = false;
							boolean isEExist = false;
							int groupID = 0;
							int elementId = 0;
							/* insert folder */
							if (sizeOfGList > 0)
								for (int i = 0; i < sizeOfGList; i++) {
									if (!group.equals(mGList.get(i).getgName()))
										isGExist = false;
									else {
										isGExist = true;
										groupID = mGList.get(i).getgId();
										break;
									}
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
										groupID = gId;
									}
								}
							else if (!isGExist) {
								isGExist = true;
								int gId = 0;
								for (int j = 0; j < sizeOfGList; j++)
									if (gId < mGList.get(j).getgId())
										gId = mGList.get(j).getgId();
								gId++;
								groupID = gId;
								GroupFolder folder = new GroupFolder(gId,
										group, 0, Contants.MASTER_PASSWORD_ID,
										0);
								mDataBaseHandler.addNewFolder(folder);
								mGList.add(folder);
								sizeOfGList++;
							}

							/* insert element */

							List<ElementID> elementList = mDataBaseHandler
									.getAllElementIdByGroupFolderId(groupID);
							if (elementList.size() > 0)
								for (int j = 0; j < elementList.size(); j++) {
									if (title.equals(elementList.get(j)
											.geteTitle())) {
										isEExist = true;
										elementId = elementList.get(j).geteId();
										break;
									}

									if (j == sizeOfEList - 1 && !isEExist) {
										int eId = sizeOfEList;
										long timeStamp = System
												.currentTimeMillis();
										for (int k = 0; k < mEList.size(); k++)
											if (eId < mEList.get(k).geteId())
												eId = mEList.get(k).geteId();
										eId++;
										elementId = eId;
										ElementID element = new ElementID(eId,
												groupID, title, new byte[] {},
												timeStamp, fav, 0, url, note,
												new byte[] {}, 0);
										mDataBaseHandler.addElement(element);
										mEList.add(element);
										sizeOfEList++;
									}
								}
							else if (!isEExist) {
								isEExist = true;
								int eId = sizeOfEList;
								long timeStamp = System.currentTimeMillis();
								eId++;
								elementId = eId;
								ElementID element = new ElementID(eId, groupID,
										title, new byte[] {}, timeStamp, fav,
										0, url, note, new byte[] {}, 0);
								mDataBaseHandler.addElement(element);
								mEList.add(element);
								sizeOfEList++;
							}
							/* insert to password */
							mDataBaseHandler
									.deletePasswordByElementId(elementId);
							for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
								int pwId = sizeOfPList;
								for (int k = 0; k < sizeOfPList; k++)
									if (pwId < mPList.get(k).getPasswordId())
										pwId = mPList.get(k).getPasswordId();
								pwId++;
								Password passWord = new Password(pwId,
										elementId, titleItem[i], contentItem[i]);
								mDataBaseHandler.addNewPassword(passWord);
								mPList.add(passWord);
								sizeOfPList++;
							}
						}
						row++;
					}
					finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					showDialogInvalidFomat();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	protected void importFileCSVToDatabaseFormatLastPass(String mSelectedFile) {
		// TODO Auto-generated method stub
		// File sdcard = Environment.getExternalStorageDirectory();
		if (mPref.getNumberItems() >= Contants.MAX_ELEMENT)
			return;
		File file = new File(Contants.PATH_ID_FILES + mSelectedFile);
		// ArrayList<PasswordItem> mItems = new
		// ArrayList<SettingActivity.PasswordItem>();
		String group = null, title = null, icon = null, url = null, note = null, image = null;

		String[] titleItem = new String[Contants.MAX_ITEM_PASS_ID];
		String[] contentItem = new String[Contants.MAX_ITEM_PASS_ID];
		int fav = 0;
		if (file.exists()) {
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(file));
				String reader = "";
				int row = 0;
				try {
					while ((reader = in.readLine()) != null) {
						if (row == 0) {
							String[] rowData = reader.split(",");
							if (!rowData[0].equals("url")) {
								showDialogInvalidFomat();
								return;
							}
						}
						if (row > 0) {
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

							url = rowDataList.get(0);
							titleItem[0] = "ID2";
							contentItem[0] = rowDataList.get(1);
							titleItem[1] = "Password2";
							contentItem[1] = rowDataList.get(2);
							titleItem[2] = "";
							contentItem[2] = "";
							titleItem[3] = "";
							contentItem[3] = "";
							titleItem[4] = "";
							contentItem[4] = "";

							note = "";
							title = rowDataList.get(4);
							group = rowDataList.get(5);
							fav = Integer.parseInt(rowDataList.get(6));

							/* update to database */
							boolean isGExist = false;
							boolean isEExist = false;
							int groupID = 0;
							int elementId = 0;
							/* insert folder */
							if (sizeOfGList > 0)
								for (int i = 0; i < sizeOfGList; i++) {
									if (!group.equals(mGList.get(i).getgName()))
										isGExist = false;
									else {
										isGExist = true;
										groupID = mGList.get(i).getgId();
										break;
									}
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
										groupID = gId;
									}
								}
							else if (!isGExist) {
								isGExist = true;
								int gId = 0;
								for (int j = 0; j < sizeOfGList; j++)
									if (gId < mGList.get(j).getgId())
										gId = mGList.get(j).getgId();
								gId++;
								groupID = gId;
								GroupFolder folder = new GroupFolder(gId,
										group, 0, Contants.MASTER_PASSWORD_ID,
										0);
								mDataBaseHandler.addNewFolder(folder);
								mGList.add(folder);
								sizeOfGList++;
							}

							/* insert element */

							List<ElementID> elementList = mDataBaseHandler
									.getAllElementIdByGroupFolderId(groupID);
							if (elementList.size() > 0)
								for (int j = 0; j < elementList.size(); j++) {
									if (title.equals(elementList.get(j)
											.geteTitle())) {
										isEExist = true;
										elementId = elementList.get(j).geteId();
										break;
									}

									if (j == sizeOfEList - 1 && !isEExist) {
										int eId = sizeOfEList;
										long timeStamp = System
												.currentTimeMillis();
										for (int k = 0; k < mEList.size(); k++)
											if (eId < mEList.get(k).geteId())
												eId = mEList.get(k).geteId();
										eId++;
										elementId = eId;
										ElementID element = new ElementID(eId,
												groupID, title, new byte[] {},
												timeStamp, fav, 0, url, note,
												new byte[] {}, 0);
										mDataBaseHandler.addElement(element);
										mEList.add(element);
										sizeOfEList++;
									}
								}
							else if (!isEExist) {
								isEExist = true;
								int eId = sizeOfEList;
								long timeStamp = System.currentTimeMillis();
								eId++;
								elementId = eId;
								ElementID element = new ElementID(eId, groupID,
										title, new byte[] {}, timeStamp, fav,
										0, url, note, new byte[] {}, 0);
								mDataBaseHandler.addElement(element);
								mEList.add(element);
								sizeOfEList++;
							}
							/* insert to password */
							mDataBaseHandler
									.deletePasswordByElementId(elementId);
							for (int i = 0; i < Contants.MAX_ITEM_PASS_ID; i++) {
								int pwId = sizeOfPList;
								for (int k = 0; k < sizeOfPList; k++)
									if (pwId < mPList.get(k).getPasswordId())
										pwId = mPList.get(k).getPasswordId();
								pwId++;
								Password passWord = new Password(pwId,
										elementId, titleItem[i], contentItem[i]);
								mDataBaseHandler.addNewPassword(passWord);
								mPList.add(passWord);
								sizeOfPList++;
							}
						}
						row++;
					}
					finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
					showDialogInvalidFomat();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void showDialogInvalidFomat() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Invalid format CSV");
		builder.setTitle("Error");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		builder.create().show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		deleteFileAfterUpload();
		super.onDestroy();
	}

	/**
	 * delete file csv after upload to cloud
	 */
	private void deleteFileAfterUpload() {
		// TODO Auto-generated method stub
		File file = new File(Contants.PATH_ID_FILES + "/" + fileCSVToImport);
		if (file.exists())
			file.delete();
	}

}
