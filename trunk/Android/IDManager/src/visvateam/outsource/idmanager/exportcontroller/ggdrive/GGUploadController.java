package visvateam.outsource.idmanager.exportcontroller.ggdrive;

import java.io.IOException;
import java.util.Date;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GGUploadController extends AsyncTask<Void, Long, Integer> {
	private Context mContext;
	private final ProgressDialog mDialog;
	private Drive mService;
	private java.io.File mFileDb;
	private long mFileLength;
	private GoogleAccountCredential credential;
	private Handler mHandler;
	private IdManagerPreference mIdManagerPreference;
	private long mLastTimeSync;
	private boolean isCheckedTime;
	private UserRecoverableAuthIOException event;

	public GGUploadController(Activity context, Drive service, java.io.File fileDb,
			Handler mHandler, String acountName, boolean isCheckedTime) {
		this.mContext = context;
		this.mService = service;
		this.mFileDb = fileDb;
		this.mFileLength = fileDb.length();
		this.mHandler = mHandler;
		this.isCheckedTime = isCheckedTime;
		mIdManagerPreference = IdManagerPreference.getInstance(mContext);
		mLastTimeSync = mIdManagerPreference.getLastTimeSyncCloud();

//		credential = GoogleAccountCredential.usingOAuth2(mContext, DriveScopes.DRIVE);
//		credential.setSelectedAccountName(acountName);
//		mService = getDriveService(credential);

		mDialog = new ProgressDialog(context);
		mDialog.setTitle(mContext.getString(R.string.app_name));
		mDialog.setIcon(R.drawable.icon);
		mDialog.setMessage("Loading... ");
		mDialog.setProgress(0);
		mDialog.show();
	}


	@Override
	protected Integer doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {

			File myBody = new File();

			myBody.setTitle("IDxPassword");

			myBody.setMimeType("application/vnd.google-apps.folder");

			mService.files().insert(myBody).execute();

			Files.List request = null;
			File gDriveFile = null;
			request = mService.files().list();
			FileList files = request.execute();
			Log.e("file.size", "file.size " + files.size());
			for (File file : files.getItems()) {
				if (Contants.DATA_IDMANAGER_NAME.endsWith(file.getTitle())) {
					gDriveFile = file;
				}
				String fieldId = file.getId();
				String title = file.getTitle();
				DateTime timeModify = file.getModifiedDate();
				long time = timeModify.getValue();
				Log.e("MS", "MSV::  Title-->" + title + "  FieldID-->" + fieldId
						+ " DownloadURL-->" + file.getDownloadUrl());
				// if (file.getDownloadUrl() != null &&
				// file.getDownloadUrl().length() > 0) {
				// GenericUrl url = new GenericUrl(file.getDownloadUrl());
				// HttpResponse resp =
				// mService.getRequestFactory().buildGetRequest(url).execute();
				// InputStream isd = resp.getContent();
				// // Log.e("MS",
				// //
				// "MSV:: FileOutPutStream--->"+getFilesDir().getAbsolutePath()+"/downloaded.txt");
				//
				// } else {
				// Log.e("MS", "MSV:: downloadURL for this file is null");
				// }
			}
			if (!isCheckedTime)
				if (null != gDriveFile) {
					DateTime dateModify = gDriveFile.getModifiedDate();
					long timeModify = dateModify.getValue();
					if (timeModify > mLastTimeSync)
						return Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER;
					else
						return Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER;
				}

			request.setPageToken(files.getNextPageToken());

			FileContent mediaContent = new FileContent("image/text", mFileDb);

			// File's metadata.
			File body = new File();
			body.setTitle(mFileDb.getName());
			body.setMimeType("image/text");

			if (null != mService) {
				File file = mService.files().insert(body, mediaContent).execute();
				if (file != null) {
					Log.e("finish", "finish");
					return Contants.DIALOG_MESSAGE_SYNC_SUCCESS;
				}
			} else {
				return Contants.DIALOG_MESSAGE_SYNC_FAILED;
			}
		} catch (UserRecoverableAuthIOException e) {
			// showToast("Sync error");
			Log.e("error", "error");
			event = e;
			return Contants.DIALOG_MESSAGE_AUTHEN_GG_FAILED;
		} catch (IOException e) {
			e.printStackTrace();
			return Contants.DIALOG_MESSAGE_SYNC_FAILED;
		}
		return Contants.DIALOG_MESSAGE_SYNC_FAILED;
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		mDialog.dismiss();
		Log.e("result", "result " + result);
		Message msg = mHandler.obtainMessage();
		if (result == Contants.DIALOG_MESSAGE_SYNC_SUCCESS) {
			Date date = new Date();
			mLastTimeSync = date.getTime();
			mIdManagerPreference.setLastTimeSyncCloud(mLastTimeSync);
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_SUCCESS;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_FAILED) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_FAILED;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_DUPLICATED_FILE;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER;
			mHandler.sendMessage(msg);
		}else if(result == Contants.DIALOG_MESSAGE_AUTHEN_GG_FAILED){
			msg.arg1 = Contants.DIALOG_MESSAGE_AUTHEN_GG_FAILED;
			msg.obj = event;
			mHandler.sendMessage(msg);
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Long... progress) {
		// TODO Auto-generated method stub
		int percent = (int) (100.0 * (double) progress[0] / mFileLength + 0.5);
		mDialog.setProgress(percent);
	}

	private Drive getDriveService(GoogleAccountCredential credential) {
		return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(),
				credential).build();
	}
}
