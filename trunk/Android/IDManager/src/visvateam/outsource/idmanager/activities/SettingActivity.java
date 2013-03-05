package visvateam.outsource.idmanager.activities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sqlcipher.database.SQLiteDatabase;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.google.api.services.drive.Drive.Apps.List;

import visvateam.outsource.idmanager.activities.export.ExportDataDropBoxActivity;
import visvateam.outsource.idmanager.activities.export.ExportDataGGDriveActivity;
import visvateam.outsource.idmanager.activities.synccloud.DropBoxSyncActivity;
import visvateam.outsource.idmanager.activities.synccloud.DropboxSettingActivity;
import visvateam.outsource.idmanager.activities.synccloud.GGDriveSettingActivity;
import visvateam.outsource.idmanager.activities.synccloud.SyncCloudActivity;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.FolderDatabase;
import visvateam.outsource.idmanager.database.IDDataBase;
import visvateam.outsource.idmanager.exportcontroller.dropbox.DropBoxController;
import visvateam.outsource.idmanager.util.NetworkUtility;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingActivity extends Activity {

	private String items[] = { "Google Drive", "Dropbox" };
	private DataBaseHandler mDataBaseHandler;
	private boolean isExportData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		/* init database */
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);
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

	@SuppressWarnings("deprecation")
	public void onReadFileviaCloud(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			isExportData = false;
			showDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
		} else
			showDialog(Contants.DIALOG_NO_NET_WORK);
	}

	@SuppressWarnings("deprecation")
	public void onExportData(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			isExportData = true;
			showDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
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
	 * Called to create a dialog to be shown.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case Contants.DIALOG_NO_NET_WORK:
			return createExampleDialog(Contants.DIALOG_NO_NET_WORK);
		case Contants.DIALOG_CHOICE_CLOUD_TYPE:
			return createExampleDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
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
			builder.setMessage("Network is unvailable");
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
					/* gen file csv */
					generateCsvFile(Contants.PATH_ID_FILES + "/" + Contants.FILE_CSV_NAME);
					if (item == 0) {
						/* export file to gg drive */
						exportDataToGGDrive();
					} else if (item == 1) {
						/* export file to dropbox */
						exportDataToDropbox();
					}

					Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				}

			});
			return builder2.create();
		default:
			return null;
		}
	}

	private void exportDataToDropbox() {
		// TODO Auto-generated method stub
		Intent intentExportData = new Intent(SettingActivity.this, ExportDataDropBoxActivity.class);
		intentExportData.putExtra(Contants.IS_EXPORT_FILE, isExportData);
		startActivity(intentExportData);
	}

	private void exportDataToGGDrive() {
		// TODO Auto-generated method stub
		Intent intentExportData = new Intent(SettingActivity.this, ExportDataGGDriveActivity.class);
		intentExportData.putExtra(Contants.IS_EXPORT_FILE, isExportData);
		startActivity(intentExportData);
	}

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
				writer.append("\n");
				writer.append("" + idList.get(i).getDataId12());
				writer.append(",");

			}

			// generate whatever data you want

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
