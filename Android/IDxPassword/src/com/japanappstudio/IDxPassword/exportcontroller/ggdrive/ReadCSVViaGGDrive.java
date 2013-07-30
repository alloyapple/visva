package com.japanappstudio.IDxPassword.exportcontroller.ggdrive;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.japanappstudio.IDxPassword.activities.R;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.japanappstudio.IDxPassword.contants.Contants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReadCSVViaGGDrive extends AsyncTask<Void, Long, Integer> {
	private Context mContext;
	private final ProgressDialog mDialog;
	private Drive mService;
	private Handler mHandler;

	private ArrayList<String> mFileListOnCloud = new ArrayList<String>();

	public ReadCSVViaGGDrive(Activity context, Drive service, Handler mHandler) {
		this.mContext = context;
		this.mService = service;
		this.mHandler = mHandler;

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
			Files.List request = null;
			request = mService.files().list();
			FileList files = request.execute();
			Log.e("file.size", "file.size " + files.size());
			for (File file : files.getItems()) {
				if (file.getTitle().endsWith(".csv")
						|| file.getTitle().endsWith(".CSV"))
					mFileListOnCloud.add(file.getTitle());
				// if (Contants.DATA_IDMANAGER_NAME.equals(file.getTitle())) {
				// gDriveFile = file;
				// isExitFile = true;
				// }
				// if (Contants.DATA_IDMANAGER_FOLDER_CLOUD
				// .equals(file.getTitle())) {
				// isExistFolder = true;
				// }
				String fieldId = file.getId();
				String title = file.getTitle();
				Log.e("MS", "MSV::  Title-->" + title + "  FieldID-->"
						+ fieldId + " DownloadURL-->" + file.getDownloadUrl());
			}
			if (mFileListOnCloud.size() == 0) {
				return Contants.DIALOG_NO_DATA_CLOUD;
			}
			// if (!isCheckedTime)
			// if (null != gDriveFile) {
			// DateTime dateModify = gDriveFile.getModifiedDate();
			// long timeModify = dateModify.getValue();
			// if (timeModify > mLastTimeSync)
			// return Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER;
			// else
			// return Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER;
			// }
			//
			// request.setPageToken(files.getNextPageToken());
			//
			// FileContent mediaContent = new FileContent("image/text",
			// mFileDb);
			//
			// // File's metadata.
			// File body = new File();
			// body.setTitle(mFileDb.getName());
			// body.setMimeType("image/text");
			//
			// if (null != mService) {
			// InputStream input = downloadFile(mService, gDriveFile);
			// if (input != null) {
			// // Log.e("finish", "finish "+input.getTitle());
			// // file.
			// // // java.io.File fileDb = file.
			// // Java.io.File fileDb =
			// // java.io.File fileDb = new java.io.File(input.r);
			// OutputStream outputStream = new FileOutputStream(mFileDb);
			// rewrite(input, outputStream);
			// return Contants.DIALOG_MESSAGE_SYNC_SUCCESS;
			// }
			// } else {
			// return Contants.DIALOG_MESSAGE_SYNC_FAILED;
			// }
		} catch (UserRecoverableAuthIOException e) {
			// showToast("Sync error");
			Log.e("error", "error");
			return Contants.DIALOG_MESSAGE_AUTHEN_GG_FAILED;
		} catch (IOException e) {
			e.printStackTrace();
			return Contants.DIALOG_MESSAGE_SYNC_FAILED;
		}
		return Contants.DIALOG_MESSAGE_READ_LIST_DATA;
	}

	// private static void rewrite(InputStream input, OutputStream outputStream)
	// throws IOException {
	// // TODO Auto-generated method stub
	// byte[] buffer = new byte[256];
	// int bytesRead = 0;
	// while ((bytesRead = input.read(buffer)) != -1) {
	// System.out.println(bytesRead);
	// outputStream.write(buffer, 0, bytesRead);
	// }
	// }

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		Log.e("result", "result " + result);
		mDialog.dismiss();
		Message msg = mHandler.obtainMessage();
		if (result == Contants.DIALOG_MESSAGE_READ_LIST_DATA) {
			msg.arg1 = Contants.DIALOG_MESSAGE_READ_LIST_DATA;
			msg.obj = mFileListOnCloud;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_NO_DATA_CLOUD) {
			msg.arg1 = Contants.DIALOG_NO_DATA_CLOUD;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_FAILED) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_FAILED;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD) {
			msg.arg1 = Contants.DIALOG_MESSAGE_READ_DATA_DUPLICATED_SDCARD;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED) {
			msg.arg1 = Contants.DIALOG_MESSAGE_READ_DATA_SUCCESSED;
			mHandler.sendMessage(msg);
		}

		super.onPostExecute(result);
	}

	/**
	 * Download a file's content.
	 * 
	 * @param service
	 *            Drive API service instance.
	 * @param file
	 *            Drive File instance.
	 * @return InputStream containing the file's content if successful,
	 *         {@code null} otherwise.
	 */
	private static InputStream downloadFile(Drive service, File file) {
		if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
			try {
				HttpResponse resp = service.getRequestFactory()
						.buildGetRequest(new GenericUrl(file.getDownloadUrl()))
						.execute();
				return resp.getContent();
			} catch (IOException e) {
				// An error occurred.
				e.printStackTrace();
				return null;
			}
		} else {
			// The file doesn't have any content stored on Drive.
			return null;
		}
	}
}
