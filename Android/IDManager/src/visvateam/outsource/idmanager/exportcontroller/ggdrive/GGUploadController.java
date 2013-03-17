package visvateam.outsource.idmanager.exportcontroller.ggdrive;

import java.io.IOException;

import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.contants.Contants;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GGUploadController extends AsyncTask<Void, Long, Boolean> {
	private Context mContext;
	private final ProgressDialog mDialog;
	private Drive mService;
	private java.io.File mFileDb;
	private long mFileLength;
	private GoogleAccountCredential credential;
	private Handler mHandler;

	public GGUploadController(Activity context, Drive service, java.io.File fileDb,
			Handler mHandler, String acountName) {
		this.mContext = context;
		this.mService = service;
		this.mFileDb = fileDb;
		this.mFileLength = fileDb.length();
		this.mHandler = mHandler;

		credential = GoogleAccountCredential.usingOAuth2(mContext, DriveScopes.DRIVE);
		credential.setSelectedAccountName(acountName);
		mService = getDriveService(credential);

		mDialog = new ProgressDialog(context);
		mDialog.setTitle(mContext.getString(R.string.app_name));
		mDialog.setIcon(R.drawable.icon);
		mDialog.setMessage("Syncing data... ");
		mDialog.setProgress(0);
		mDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {

			File myBody = new File();

			myBody.setTitle("title1");

			myBody.setMimeType("application/vnd.google-apps.folder");

			mService.files().insert(myBody).execute();

			FileContent mediaContent = new FileContent("image/text", mFileDb);

			// File's metadata.
			File body = new File();
			body.setTitle(mFileDb.getName());
			body.setMimeType("image/text");
			if (null != mService) {
				File file = mService.files().insert(body, mediaContent).execute();
				if (file != null) {
					Log.e("finish", "finish");
					return true;
				}
			} else {
				Log.e("service null", "service null");
			}
		} catch (UserRecoverableAuthIOException e) {
			// showToast("Sync error");
			Log.e("error", "error");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		mDialog.dismiss();
		Message msg = mHandler.obtainMessage();
		if (result) {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_SUCCESS;
			mHandler.sendMessage(msg);
		} else {
			msg.arg1 = Contants.DIALOG_MESSAGE_SYNC_FAILED;
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

	// public void showToast(final String toast) {
	// Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	// }
}
