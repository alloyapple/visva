package vsvteam.outsource.leanappandroid.exportcontrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupValueStreamMapActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxFileSizeException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.TokenPair;
import com.dropbox.client2.session.Session.AccessType;

public class SendDropboxController {
	final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
	private final static String APP_KEY = "hjbjrmiite1q77n";
	final static private String APP_SECRET = "qdx8opx1f11nktp";

	final static private String ACCOUNT_PREFS_NAME = "prefs";
	final static private String ACCESS_KEY_NAME = "ACCESS_KEY";
	final static private String ACCESS_SECRET_NAME = "ACCESS_SECRET";
	private DropboxAPI<AndroidAuthSession> mApi;

	private File mFile;
	private long mFileLen;
	private String mDropBoxPath;
	private UploadRequest mRequest;
	private ProgressDialog mDialog;
	private String mErrorMsg;

	private Context mContext;

	public SendDropboxController(Context pContext) {
		// TODO Auto-generated constructor stub
		mContext = pContext;
		AndroidAuthSession session = buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		mDialog = new ProgressDialog(mContext);
		mDialog.setMax(100);
		mDialog.setMessage("Uploading ");
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setProgress(0);
		mDialog.setButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// This will cancel the putFile operation
				mRequest.abort();
			}
		});
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session;
		String[] stored = getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0],
					stored[1]);
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE,
					accessToken);
		} else {
			session = new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
		}

		return session;
	}

	private String[] getKeys() {
		SharedPreferences prefs = mContext.getSharedPreferences(
				ACCOUNT_PREFS_NAME, 0);
		String key = prefs.getString(ACCESS_KEY_NAME, null);
		String secret = prefs.getString(ACCESS_SECRET_NAME, null);
		if (key != null && secret != null) {
			String[] ret = new String[2];
			ret[0] = key;
			ret[1] = secret;
			return ret;
		} else {
			return null;
		}
	}

	private void storeKeys(String key, String secret) {
		// Save the access key for later
		SharedPreferences prefs = mContext.getSharedPreferences(
				ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.putString(ACCESS_KEY_NAME, key);
		edit.putString(ACCESS_SECRET_NAME, secret);
		edit.commit();
	}

	private void clearKeys() {
		SharedPreferences prefs = mContext.getSharedPreferences(
				ACCOUNT_PREFS_NAME, 0);
		Editor edit = prefs.edit();
		edit.clear();
		edit.commit();
	}

	public void upload(File pFile, String dropBoxPath) {
		mFile = pFile;
		mFileLen = mFile.length();
		mDropBoxPath = dropBoxPath;
		AndroidAuthSession session = mApi.getSession();
		if (session.authenticationSuccessful()) {
			try {
				// Mandatory call to complete the auth
				session.finishAuthentication();
				// Store it locally in our app for later use
				TokenPair tokens = session.getAccessTokenPair();
				storeKeys(tokens.key, tokens.secret);
				new AlertDialog.Builder(TabGroupValueStreamMapActivity.instance)
						.setMessage("You are logged in as &$#")
						.setPositiveButton("Continue",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method
										// stub
									}
								})
						.setNegativeButton("Log out",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method
										// stub
										logOut();
										authentication();
									}
								}).create().show();
			} catch (IllegalStateException e) {
				Log.i("DropBoxController", "Error authenticating", e);
			}
		} else {
			authentication();
		}
	}

	public void authentication() {
		LeanAppAndroidSharePreference preference = LeanAppAndroidSharePreference
				.getInstance(mContext);
		preference.setModeExport(LeanAppAndroidSharePreference.TYPE_EXPORT,
				ActionExportActivity.SEND_DROP_BOX);
		mApi.getSession().startAuthentication(mContext);
	}

	public void sendFile() {
		AndroidAuthSession session = mApi.getSession();
		if (session.authenticationSuccessful()) {
			new UploadAsyncTask().execute(new String[] {});
		}else{
			Toast.makeText(mContext, "Not Logged in", Toast.LENGTH_LONG).show();
		}
	}

	public boolean checkAppKeySetup() {
		// Check to make sure that we have a valid app key
		if (APP_KEY.startsWith("") || APP_SECRET.startsWith("")) {
			return false;
		}
		// Check if the app has set up its manifest properly.
		Intent testIntent = new Intent(Intent.ACTION_VIEW);
		String scheme = "db-" + APP_KEY;
		String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
		testIntent.setData(Uri.parse(uri));
		PackageManager pm = mContext.getPackageManager();
		if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
			return false;
		}
		return true;
	}

	public void logOut() {
		// Remove credentials from the session
		mApi.getSession().unlink();
		// Clear our stored keys
		clearKeys();
	}

	class UploadAsyncTask extends AsyncTask<String, Long, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				// By creating a request, we get a handle to the putFile
				// operation,
				// so we can cancel it later if we want to
				FileInputStream fis = new FileInputStream(mFile);
				String path = mDropBoxPath + mFile.getName();
				mRequest = mApi.putFileOverwriteRequest(path, fis,
						mFile.length(), new ProgressListener() {
							@Override
							public long progressInterval() {
								// Update the progress bar every half-second or
								// so
								return 500;
							}

							@Override
							public void onProgress(long bytes, long total) {
								publishProgress(bytes);
							}
						});

				if (mRequest != null) {
					mRequest.upload();
					return true;
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
				// Server-side exception. These are examples of what could
				// happen,
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
				// This gets the Dropbox error, translated into the user's
				// language
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
			} catch (FileNotFoundException e) {
			}
			return false;
		}

	}
}
