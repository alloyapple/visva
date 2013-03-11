package visvateam.outsource.idmanager.exportcontroller.dropbox;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
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

public class DropBoxDownloadFile extends AsyncTask<Void, Long, Boolean> {

	private Context mContext;
	private final ProgressDialog mDialog;
	private DropboxAPI<?> mApi;
	private String mPath;
	private FileOutputStream mFos;
	private boolean mCanceled;
	private Long mFileLen;
	private String mErrorMsg;
	private String mDbFilePath;

	@SuppressWarnings("deprecation")
	public DropBoxDownloadFile(Context context, DropboxAPI<?> api, String dropboxPath,String dbFilePath) {
		// We set the context this way so we don't accidentally leak activities
		mContext = context.getApplicationContext();

		mApi = api;
		mPath = dropboxPath;
		mDbFilePath = dbFilePath;
		mDialog = new ProgressDialog(context);
		mDialog.setMessage("Downloading Image");
		mDialog.setButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mCanceled = true;
				mErrorMsg = "Canceled";

				// This will cancel the getThumbnail operation by closing
				// its stream
				if (mFos != null) {
					try {
						mFos.close();
					} catch (IOException e) {
					}
				}
			}
		});

		mDialog.show();

		createExampleDialog(Contants.DIALOG_NO_NET_WORK);
	}

	/**
	 * Create and return an example alert dialog with an edit text box.
	 */
	private Dialog createExampleDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		switch (id) {
		case Contants.DIALOG_NO_NET_WORK:
			builder.setTitle("Id Manager");
			builder.setMessage("Network is unvailable");
			builder.setIcon(R.drawable.icon);
			builder.setCancelable(false);
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					/* add new folder to database */
					return;
				}
			});
			return builder.create();
		default:
			return null;
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			if (mCanceled) {
				return false;
			}

			// Get the metadata for a directory
			Entry dirent = mApi.metadata(mPath, 1000, null, true, null);

			if (!dirent.isDir || dirent.contents == null) {
				// It's not a directory, or there's nothing in it
				mErrorMsg = "File or empty directory";
				return false;
			}

			// Make a list of everything in it that we can get a thumbnail for
			ArrayList<Entry> thumbs = new ArrayList<Entry>();
			for (Entry ent : dirent.contents) {
				Log.d("file " + ent.thumbExists, "file ent " + ent.fileName());
				if (Contants.DATA_IDMANAGER_NAME.equals(ent.fileName().toString())) {
					// Add it to the list of thumbs we can choose from
					thumbs.add(ent);
				} 
			}

			if (mCanceled) {
				return false;
			}

			if (thumbs.size() == 0) {
				// No thumbs in that directory
				mErrorMsg = "No pictures in that directory";
				return false;
			}

			// Now pick a random one
			int index = (int) (Math.random() * thumbs.size());
			Entry ent = thumbs.get(index);
			String path = ent.path;
			mFileLen = ent.bytes;
			Log.e("modify ", "modify time " + ent.modified);
			String modify = ent.modified;
			@SuppressWarnings("deprecation")
			Date date = new Date(modify);
			long modifyTime = date.getTime();
			Log.e("time", "time " + modifyTime);

			String cachePath = mDbFilePath + "/" + Contants.DATA_IDMANAGER_NAME;
			Log.e("file path", "file Path " + cachePath);
			try {
				mFos = new FileOutputStream(cachePath);
			} catch (FileNotFoundException e) {
				mErrorMsg = "Couldn't create a local file to store the image";
				return false;
			}

			mApi.getFile(path, "test", mFos, null);
			if (mCanceled) {
				return false;
			}

			// mDrawable = Drawable.createFromPath(cachePath);
			// We must have a legitimate picture
			return true;

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
		return false;
	}

	@Override
	protected void onProgressUpdate(Long... progress) {
		int percent = (int) (100.0 * (double) progress[0] / mFileLen + 0.5);
		mDialog.setProgress(percent);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mDialog.dismiss();
		if (result) {
			showToast("Sucess");
		} else {
			// Couldn't download it, so show an error
			showToast(mErrorMsg);
		}
	}

	private void showToast(String msg) {
		Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		error.show();
	}

}
