/*
 * Copyright (c) 2011 Dropbox, Inc.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.japanappstudio.IDxPassword.exportcontroller.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import com.japanappstudio.IDxPassword.activities.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxFileSizeException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;

/**
 * Here we show uploading a file in a background thread, trying to show typical
 * exception handling and flow of control for an app that uploads a file from
 * Dropbox.
 */
public class DBDropboxAutoSyncController extends AsyncTask<Void, Long, Integer> {

	DropboxAPI<AndroidAuthSession> mApi;
	private String mPath;
	private File mFile;

	private long mFileLen;
	private UploadRequest mRequest;
	private Context mContext;
	private final ProgressDialog mDialog;
	private String mErrorMsg;
	private String mFileName;
	private Handler mHandler;
	private IdManagerPreference mIdManagerPreference;
	private long mLastTimeSync;
	private boolean isCheckedTime;

	public DBDropboxAutoSyncController(Context context,
			DropboxAPI<AndroidAuthSession> api, String dropboxPath, File file,
			Handler mHandler, boolean isCheckedTime) {
		// We set the context this way so we don't accidentally leak activities
		mContext = context.getApplicationContext();
		mIdManagerPreference = IdManagerPreference.getInstance(mContext);
		mLastTimeSync = mIdManagerPreference.getLastTimeSyncCloud();
		mFileLen = file.length();
		mApi = api;
		mPath = dropboxPath;
		mFile = file;
		this.mFileName = mFile.getName();
		this.mHandler = mHandler;
		this.isCheckedTime = isCheckedTime;

		mDialog = new ProgressDialog(context);
		mDialog.setMax(100);
		mDialog.setTitle(mContext.getString(R.string.app_name));
		mDialog.setIcon(R.drawable.icon);
		mDialog.setMessage("Loading...");
		mDialog.setProgress(0);
		mDialog.show();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Integer doInBackground(Void... params) {
		try {
			// Get the metadata for a directory
			Entry dirent = mApi.metadata(mPath, 1000, null, true, null);
			Entry entry = null;
			if (!dirent.isDir || dirent.contents == null) {
				// It's not a directory, or there's nothing in it
				mErrorMsg = "File or empty directory";
			}

			for (Entry ent : dirent.contents) {
				Log.d("file " + mFile, "file ent " + ent.fileName());
				if (mFileName.equals(ent.fileName().toString())) {
					// Add it to the list of thumbs we can choose from
					entry = ent;
				}

			}
			if (!isCheckedTime)
				if (null != entry) {
					mFileLen = entry.bytes;
					Log.e("modify ", "modify time " + entry.modified);
					String modify = entry.modified;
					Date date = new Date(modify);
					long modifyTime = date.getTime();
					Log.e("time", "time " + modifyTime);
					if (modifyTime > mLastTimeSync)
						return Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER;
					else
						return Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER;
				}
			// By creating a request, we get a handle to the putFile operation,
			// so we can cancel it later if we want to
			FileInputStream fis = new FileInputStream(mFile);
			String path = mPath + mFile.getName();
			mRequest = mApi.putFileOverwriteRequest(path, fis, mFile.length(),
					new ProgressListener() {
						@Override
						public long progressInterval() {
							// Update the progress bar every half-second or so
							return 500;
						}

						@Override
						public void onProgress(long bytes, long total) {
							publishProgress(bytes);
						}
					});

			if (mRequest != null) {
				mRequest.upload();
				return Contants.DIALOG_MESSAGE_SYNC_SUCCESS;
			}

		} catch (DropboxUnlinkedException e) {
			// This session wasn't authenticated properly or user unlinked
			mErrorMsg = "This app wasn't authenticated properly.";
		} catch (DropboxFileSizeException e) {
			// File size too big to upload via the API
			mErrorMsg = "This file is too big to upload";
		} catch (DropboxPartialFileException e) {
			// We canceled the operation
			mErrorMsg = "Upload canceled";
		} catch (DropboxServerException e) {
			// Server-side exception. These are examples of what could happen,
			// but we don't do anything special with them here.
			if (e.error == DropboxServerException._401_UNAUTHORIZED) {
				// Unauthorized, so we should unlink them. You may want to
				// automatically log the user out in this case.
			} else if (e.error == DropboxServerException._403_FORBIDDEN) {
				// Not allowed to access this
			} else if (e.error == DropboxServerException._404_NOT_FOUND) {
				// path not found (or if it was the thumbnail, can't be
				// thumbnailed)
			} else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
				// user is over quota
			} else {
				// Something else
			}
			// This gets the Dropbox error, translated into the user's language
			mErrorMsg = e.body.userError;
			if (mErrorMsg == null) {
				mErrorMsg = e.body.error;
			}
			return Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED;
		} catch (DropboxIOException e) {
			// Happens all the time, probably want to retry automatically.
			mErrorMsg = "Network error.  Try again.";
		} catch (DropboxParseException e) {
			// Probably due to Dropbox server restarting, should retry
			mErrorMsg = "Dropbox error.  Try again.";
		} catch (DropboxException e) {
			// Unknown error
			mErrorMsg = "Unknown error.  Try again.";
		} catch (FileNotFoundException e) {
		}
		return Contants.DIALOG_MESSAGE_SYNC_FAILED;
	}

	@Override
	protected void onProgressUpdate(Long... progress) {
		int percent = (int) (100.0 * (double) progress[0] / mFileLen + 0.5);
		mDialog.setProgress(percent);
	}

	@Override
	protected void onPostExecute(Integer result) {
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
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER;
			mHandler.sendMessage(msg);
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_CLOUD_DATA_DEVICE_NEWER;
			mHandler.sendMessage(msg);
		}

	}
}
