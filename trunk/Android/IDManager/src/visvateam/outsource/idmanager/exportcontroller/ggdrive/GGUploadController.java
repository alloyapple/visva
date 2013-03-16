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
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class GGUploadController extends AsyncTask<Void, Long, Boolean> {
	private static final int REQUEST_ACCOUNT_PICKER = 1;
	private static final int REQUEST_AUTHORIZATION = 2;

	private Context mContext;
	private final ProgressDialog mDialog;
	private Drive mService;
	private java.io.File mFileDb;
	private long mFileLength;

	public GGUploadController(Context context, Drive service, java.io.File fileDb) {
		this.mContext = context;
		this.mService = service;
		this.mFileDb = fileDb;
		this.mFileLength = fileDb.length();

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
			// File's binary content
			// java.io.File fileContent = new
			// java.io.File(fileUri.getPath());
			//
			File myBody = new File();

			myBody.setTitle("title");

			myBody.setMimeType("application/vnd.google-apps.folder ");

			File file2 = mService.files().insert(myBody).execute();
			Files.List request = mService.files().list()
					.setQ("mimeType='application/vnd.google-apps.folder' and trashed=false");
			FileList files = request.execute();
			request.setPageToken(files.getNextPageToken());
			//

			FileContent mediaContent = new FileContent("image/text", mFileDb);

			// File's metadata.
			File body = new File();
			body.setTitle(mFileDb.getName());
			body.setMimeType("image/text");
			if (mService != null) {
				File file = mService.files().insert(body, mediaContent).execute();
				if (file != null) {
					Log.e("finish", "finish");
					return true;
				}
			}
		} catch (UserRecoverableAuthIOException e) {
			showToast("Sync error");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		mDialog.dismiss();
		if (result) {
			showToast("Sync completed");
		} else
			showToast("You must authenticate your google account");
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

	public void showToast(final String toast) {
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	}
}
