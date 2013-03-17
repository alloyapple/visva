package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.exportcontroller.ggdrive.GGUploadController;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class GGDriveSyncActivity extends Activity {
	private static final int REQUEST_ACCOUNT_PICKER = 1;
	private static final int REQUEST_AUTHORIZATION = 2;
	private static Drive service;
	private GoogleAccountCredential credential;
	private Button mBtnLinkToGG;
	private Button mBtnStartSync;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_sync_gg_drive);
		/* create file if not exist */
		java.io.File file = new java.io.File(Contants.PATH_ID_FILES);
		if (!file.exists())
			file.mkdirs();

		credential = GoogleAccountCredential.usingOAuth2(this, DriveScopes.DRIVE);
		mBtnLinkToGG = (Button) findViewById(R.id.btn_link_to_gg_drive);
		mBtnLinkToGG.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
			}
		});
		mBtnStartSync = (Button) findViewById(R.id.btn_start_sync_gg);
		mBtnStartSync.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				saveFileToDrive();
			}
		});
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					Log.e("accname", "acc Name "+accountName);
					credential.setSelectedAccountName(accountName);
					service = getDriveService(credential);
				}
			}
			break;
		case REQUEST_AUTHORIZATION:
			if (resultCode == Activity.RESULT_OK) {
//				saveFileToDrive();
			} else {
				startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
			}
			break;
		default:
			break;
		}
	}

//	private void saveFileToDrive() {
//		java.io.File fileDb = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
//		GGUploadController drive = new GGUploadController(this, service, fileDb);
//		drive.execute();
//	}

	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential).build();
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast,Toast.LENGTH_SHORT).show();
			}
		});
	}
}