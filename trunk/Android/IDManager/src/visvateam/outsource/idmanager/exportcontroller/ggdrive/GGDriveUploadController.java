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
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GGDriveUploadController extends AsyncTask<Void, Long, Integer> {
	private Context mContext;
	private final ProgressDialog mDialog;
	private Drive mService;
	private java.io.File mFileDb;
	private long mFileLength;
	private Handler mHandler;
	private IdManagerPreference mIdManagerPreference;
	private long mLastTimeSync;
	private boolean isCheckedTime;
	private UserRecoverableAuthIOException event;
	private boolean isExistFolder = false;

	public GGDriveUploadController(Activity context, Drive service,
			java.io.File fileDb, Handler mHandler, String acountName,
			boolean isCheckedTime) {
		this.mContext = context;
		this.mService = service;
		this.mFileDb = fileDb;
		this.mFileLength = fileDb.length();
		this.mHandler = mHandler;
		this.isCheckedTime = isCheckedTime;
		mIdManagerPreference = IdManagerPreference.getInstance(mContext);
		mLastTimeSync = mIdManagerPreference.getLastTimeSyncCloud();

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
		File myBody = null;
		try {
			Files.List request = null;
			File gDriveFile = null;
			request = mService.files().list();
			FileList files = request.execute();
			Log.e("file.size", "file.size " + files.size());
			for (File file : files.getItems()) {
				if (Contants.DATA_IDMANAGER_NAME.equals(file.getTitle())) {
					gDriveFile = file;
				}
				if (Contants.DATA_IDMANAGER_FOLDER_CLOUD
						.equals(file.getTitle())) {
					isExistFolder = true;
				}
				String fieldId = file.getId();
				String title = file.getTitle();
				Log.e("MS", "MSV::  Title-->" + title + "  FieldID-->"
						+ fieldId + " DownloadURL-->" + file.getDownloadUrl());
			}

			if (!isExistFolder) {
				myBody = new File();
				myBody.setTitle(Contants.DATA_IDMANAGER_FOLDER_CLOUD);
				myBody.setMimeType("application/vnd.google-apps.folder");
				mService.files().insert(myBody).execute();
			}

			if (!isCheckedTime) {
				if (null != gDriveFile) {
					DateTime dateModify = gDriveFile.getModifiedDate();
					long timeModify = dateModify.getValue();
					if (timeModify > mLastTimeSync)
						return Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_CLOUD_NEWER;
					else
						return Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER;
				}
			} else {
				/*delete file on gg drive*/
				deleteFile(mService, gDriveFile.getId());
			}
			request.setPageToken(files.getNextPageToken());

			FileContent mediaContent = new FileContent("image/text", mFileDb);

			// File's metadata.
			File body = new File();
			body.setTitle(mFileDb.getName());
			body.setMimeType("image/text");

			if (null != mService) {
				File file = mService.files().insert(body, mediaContent)
						.execute();
				if (file != null) {
					Log.e("finish", "finish");
//					insertFileIntoFolder(mService, myBody.getId(),
//							gDriveFile.getId());
					return Contants.DIALOG_MESSAGE_SYNC_SUCCESS;
				}
			} else {
				Log.e("adjfhkldhf", "mService null roi : "+mService);
				return Contants.DIALOG_MESSAGE_AUTHEN_GG_FAILED;
			}
		} catch (UserRecoverableAuthIOException e) {
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
			msg.arg1 = Contants.DIALOG_MESSAGE_AUTHEN_GG_FAILED;
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
		} else if (result == Contants.DIALOG_MESSAGE_AUTHEN_GG_FAILED) {
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

	/**
	 * Permanently delete a file, skipping the trash.
	 * 
	 * @param service
	 *            Drive API service instance.
	 * @param fileId
	 *            ID of the file to delete.
	 */
	private static void deleteFile(Drive service, String fileId) {
		try {
			service.files().delete(fileId).execute();
		} catch (IOException e) {
			System.out.println("An error occurred: " + e);
		}
	}
}
