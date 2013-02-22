package vsvteam.outsource.leanappandroid.actionbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class GGDriveSyncActivity extends Activity {

	private static final String SEND_FILE_TYPE = "isSendFileFormat";
	private static final int REQUEST_ACCOUNT_PICKER = 1;
	private static final int REQUEST_AUTHORIZATION = 2;
	private static final int CAPTURE_IMAGE = 3;
	private static final String FILE_NAME_EXCEL = "test.xls";
	private static final String FILE_NAME_PDF = "test.pdf";
	public static final String PATH_FOLDER_DOCUMENT = Environment.getExternalStorageDirectory()
			.getPath() + "/LeanApp/Documents/";

	private static Uri fileUri;
	private static Drive service;
	
	private GoogleAccountCredential credential;
	
	private boolean isSendPdfFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		/**/
		isSendPdfFile = getIntent().getExtras().getBoolean(SEND_FILE_TYPE);
		
		/**/
		credential = GoogleAccountCredential.usingOAuth2(this, DriveScopes.DRIVE);
		startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
				String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					credential.setSelectedAccountName(accountName);
					service = getDriveService(credential);
					startCameraIntent();
				}
			}
			break;
		case REQUEST_AUTHORIZATION:
			if (resultCode == Activity.RESULT_OK) {
				saveFileToDrive();
			} else {
				startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
			}
			break;
		case CAPTURE_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				saveFileToDrive();
			}
		}
	}

	private void startCameraIntent() {
		String mediaStorageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getPath();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
		fileUri = Uri.fromFile(new java.io.File(mediaStorageDir + java.io.File.separator + "IMG_"
				+ "kieuducthang" + ".jpg"));

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(cameraIntent, CAPTURE_IMAGE);
	}

	private void saveFileToDrive() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// File's binary content
//					java.io.File fileContent = new java.io.File(fileUri.getPath());
//					FileContent mediaContent = new FileContent("image/jpeg", fileContent);
					
					java.io.File fileUpload = null;
					FileContent fileContentUpload = null;
					Log.e("adfkjhsdf", "adfkjdfl "+isSendPdfFile);
					if(isSendPdfFile){
						fileUpload =new java.io.File(PATH_FOLDER_DOCUMENT + FILE_NAME_PDF);
						fileContentUpload = new FileContent("application/pdf", fileUpload);
					}else{
						fileUpload = new java.io.File(PATH_FOLDER_DOCUMENT + FILE_NAME_EXCEL);
						fileContentUpload = new FileContent("application/ms-excel", fileUpload);
					}

					Log.e("adjfhasdklf "+fileContentUpload.getEncoding(), "adkfjasd "+fileUpload.getAbsolutePath());
					// File's metadata.
					File body = new File();
					body.setTitle(fileUpload.getName()); 
					body.setMimeType("text/plain");

					File file = service.files().insert(body, fileContentUpload).execute();
					if (file != null) {
						showToast("Photo uploaded: " + file.getTitle());
//						startCameraIntent();
					}
				} catch (UserRecoverableAuthIOException e) {
					startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential).build();
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
