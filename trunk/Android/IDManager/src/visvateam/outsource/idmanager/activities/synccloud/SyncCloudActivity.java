package visvateam.outsource.idmanager.activities.synccloud;

import visvateam.outsource.idmanager.activities.GGDriveSyncActivity;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.util.NetworkUtility;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SyncCloudActivity extends Activity {
	private TextView mTextViewCloudType;
	private TextView mTextViewLastTimeSync;
	private String items[] = { "Google Drive", "Dropbox" };
	private boolean isSyncToCloud = true;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_sync);

		/* init control */
		mTextViewCloudType = (TextView) findViewById(R.id.cloud_type);
		mTextViewLastTimeSync = (TextView) findViewById(R.id.last_time_sync);

		/* check netword */
		if (!NetworkUtility.getInstance(this).isNetworkAvailable())
			showDialog(Contants.DIALOG_NO_NET_WORK);
	}

	@SuppressWarnings("deprecation")
	public void onSyncToCloud(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			isSyncToCloud = true;
			showDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
		} else{
			showDialog(Contants.DIALOG_NO_NET_WORK);
		}
	}

	@SuppressWarnings("deprecation")
	public void OnSyncToDevice(View v) {
		if (NetworkUtility.getInstance(this).isNetworkAvailable()) {
			isSyncToCloud = false;
			showDialog(Contants.DIALOG_CHOICE_CLOUD_TYPE);
		} else{
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
					Intent intent = null;
					if (item == 0) {
						intent = new Intent(SyncCloudActivity.this, GGDriveSyncActivity.class);
						startSyncActivity(intent, isSyncToCloud);
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
		default:
			return null;
		}
	}

	private void startSyncActivity(Intent intent, boolean isSyncToCloud) {
		intent.putExtra(Contants.IS_SYNC_TO_CLOUD, isSyncToCloud);
		startActivity(intent);
	}
}
