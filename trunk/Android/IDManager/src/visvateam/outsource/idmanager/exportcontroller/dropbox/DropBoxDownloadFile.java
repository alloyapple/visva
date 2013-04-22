package visvateam.outsource.idmanager.exportcontroller.dropbox;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IdManagerPreference;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

/**
 * Here we show getting metadata for a directory and downloading a file in a
 * background thread, trying to show typical exception handling and flow of
 * control for an app that downloads a file from Dropbox.
 */

public class DropBoxDownloadFile extends AsyncTask<Void, Long, Integer> {

	private Context mContext;
	private final ProgressDialog mDialog;
	private DropboxAPI<AndroidAuthSession> mApi;
	private String mPath;
	private FileOutputStream mFos;
	private Long mFileLen;
	private String mErrorMsg;
	private String mDbFilePath;
	private Handler mHandler;
	private boolean isCheckedTime;
	private IdManagerPreference mIdManagerPreference;
	private long mLastTimeSync;

	public DropBoxDownloadFile(Context context, DropboxAPI<AndroidAuthSession> api, String dropboxPath,
			String dbFilePath, Handler mHandler, boolean isCheckTime) {
		// We set the context this way so we don't accidentally leak activities
		mContext = context.getApplicationContext();

		mApi = api;
		mPath = dropboxPath;
		mDbFilePath = dbFilePath;
		this.mHandler = mHandler;
		this.isCheckedTime = isCheckTime;

		mIdManagerPreference = IdManagerPreference.getInstance(mContext);
		mLastTimeSync = mIdManagerPreference.getLastTimeSyncCloud();

		mDialog = new ProgressDialog(context);
		mDialog.setTitle(mContext.getString(R.string.app_name));
		mDialog.setIcon(R.drawable.icon);
		mDialog.setMessage("Loading...");
		mDialog.show();

	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			// Get the metadata for a directory
			Entry dirent = mApi.metadata(mPath, 1000, null, true, null);
			Entry entry = null;
			if (!dirent.isDir || dirent.contents == null) {
				// It's not a directory, or there's nothing in it
				mErrorMsg = "File or empty directory";
				return Contants.DIALOG_NO_DATA_CLOUD;
			}

			// Make a list of everything in it that we can get a thumbnail for
			ArrayList<Entry> thumbs = new ArrayList<Entry>();
			for (Entry ent : dirent.contents) {
				Log.d("file " + ent.thumbExists, "file ent " + ent.fileName());
				if (Contants.DATA_IDMANAGER_NAME.equals(ent.fileName().toString())) {
					// Add it to the list of thumbs we can choose from
					// thumbs.add(ent);
					entry = ent;
				}
			}

			if (entry == null) {
				// No thumbs in that directory
				return Contants.DIALOG_NO_DATA_CLOUD;
			} else {
				String path = entry.path;
				if (!isCheckedTime) {
					mFileLen = entry.bytes;
					Log.e("modify ", "modify time " + entry.modified);
					String modify = entry.modified;
					@SuppressWarnings("deprecation")
					Date date = new Date(modify);
					long modifyTime = date.getTime();
					if (modifyTime > mLastTimeSync)
						return Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_CLOUD_NEWER;
					else
						return Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER;
				}
				String cachePath = mDbFilePath + "/" + Contants.DATA_IDMANAGER_NAME;
				Log.e("file path", "file Path " + cachePath);
				try {
					mFos = new FileOutputStream(cachePath);
				} catch (FileNotFoundException e) {
					return Contants.DIALOG_MESSAGE_SYNC_INTERRUPTED;
				}

				mApi.getFile(path, "idmanager", mFos, null);
				// mDrawable = Drawable.createFromPath(cachePath);
				// We must have a legitimate picture
				return Contants.DIALOG_MESSAGE_SYNC_SUCCESS;
			}
		} catch (DropboxUnlinkedException e) {
			// The AuthSession wasn't properly authenticated or user unlinked.
		} catch (DropboxPartialFileException e) {
			// We canceled the operation
			mErrorMsg = "Download canceled";
		} catch (DropboxServerException e) {
			// Server-side exception. These are examples of what could happen,
			// but we don't do anything special with them here.
			if (e.error == DropboxServerException._304_NOT_MODIFIED) {
				// won't happen since we don't pass in revision with metadata
			} else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
				// Unauthorized, so we should unlink them. You may want to
				// automatically log the user out in this case.
			} else if (e.error == DropboxServerException._403_FORBIDDEN) {
				// Not allowed to access this
			} else if (e.error == DropboxServerException._404_NOT_FOUND) {
				// path not found (or if it was the thumbnail, can't be
				// thumbnailed)
			} else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
				// too many entries to return
			} else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
				// can't be thumbnailed
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
		} catch (DropboxIOException e) {
			// Happens all the time, probably want to retry automatically.
			mErrorMsg = "Network error.  Try again.";
		} catch (DropboxParseException e) {
			// Probably due to Dropbox server restarting, should retry
			mErrorMsg = "Dropbox error.  Try again.";
		} catch (DropboxException e) {
			// Unknown error
			mErrorMsg = "Unknown error.  Try again.";
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
		} else if (result == Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_DEVICE_DATA_DEVICE_NEWER;
			mHandler.sendMessage(msg);
		}
	}

}
