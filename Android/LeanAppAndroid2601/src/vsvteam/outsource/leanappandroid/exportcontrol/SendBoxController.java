package vsvteam.outsource.leanappandroid.exportcontrol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import vsvteam.outsource.leanappandroid.actionbar.AuthenticationActivity;
import com.box.androidlib.Box;
import com.box.androidlib.DAO.BoxFile;
import com.box.androidlib.DAO.User;
import com.box.androidlib.ResponseListeners.FileUploadListener;
import com.box.androidlib.ResponseListeners.GetAccountInfoListener;
import com.box.androidlib.Utils.Cancelable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

public class SendBoxController {
	public static final String PREFS_FILE_NAME = "LeanApp";
	public static final String PREFS_KEY_AUTH_TOKEN = "AUTH_TOKEN";
	public static final String API_KEY = "0mjv7mlwtfkk4z2d0jfmkkhdkzdfaqj0";
	private Context mContext;
	private Activity mActivity;

	public SendBoxController(Activity pActivity, Context pContext) {
		mContext = pContext;
		mActivity = pActivity;
	}

	public void upload(String mFilePath) {
		SharedPreferences mPreference = mContext.getSharedPreferences(
				PREFS_FILE_NAME, 0);
		String mToken = mPreference.getString(PREFS_KEY_AUTH_TOKEN, null);
		if (mToken == null) {
			Intent intent = new Intent(
					((Activity) mContext),
					AuthenticationActivity.class);
			((Activity) mContext)
					.startActivity(intent);
		} else {
			Box box = Box.getInstance(API_KEY);
			box.getAccountInfo(mToken, new GetAccountInfoListener() {
				@Override
				public void onComplete(final User boxUser, final String status) {
					// see
					// http://developers.box.net/w/page/12923928/ApiFunction_get_account_info
					// for possible status codes
					if (status
							.equals(GetAccountInfoListener.STATUS_GET_ACCOUNT_INFO_OK)
							&& boxUser != null) {
						new AlertDialog.Builder(mContext)
								.setMessage(
										"You logged in as "
												+ boxUser.getEmail())
								.setNegativeButton("Other",
										new OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												Intent intent = new Intent(
														((Activity) mContext),
														AuthenticationActivity.class);
												((Activity) mContext)
														.startActivity(intent);
											}
										})
								.setPositiveButton("Continue",
										new OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub

											}
										}).show();
					} else {
					}
				}

				@Override
				public void onIOException(IOException e) {
					// No network connection?
					e.printStackTrace();
				}
			});
		}
	}
	public void up(String pPAthFile){
		 final SharedPreferences prefs = ((Activity) mContext).getSharedPreferences(PREFS_FILE_NAME, 0);
	       String mToken = prefs.getString(PREFS_KEY_AUTH_TOKEN, null);
	        if (mToken == null) {
	            Toast.makeText(((Activity) mContext).getApplicationContext(), "You are not logged in.", Toast.LENGTH_SHORT).show();
	            return;
	        }

		final ProgressDialog uploadDialog = new ProgressDialog((Activity) mContext);
        final Uri uri = Uri.parse(pPAthFile);
        InputStream inputStream;
        try {
            inputStream = ((Activity) mContext).getContentResolver().openInputStream(uri);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(((Activity) mContext).getApplicationContext(), "Unable to read file for upload - " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        if (uri.getScheme() == null) {
            Toast.makeText(((Activity) mContext).getApplicationContext(), "Unable to read file for upload - " + uri, Toast.LENGTH_SHORT).show();
            return;
        }

        final String filename;
        final int size;
        if (uri.getScheme().equals("file")) {
            File file = new File(uri.getPath());
            filename = file.getName();
            size = (int) file.length();
        }
        else if (uri.getScheme().equals("content")) {
            String[] proj = {MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE};
            Cursor cursor = ((Activity) mContext).getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
                int sizeIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                cursor.moveToFirst();
                if (nameIndex > -1 && cursor.getString(nameIndex) != null && cursor.getString(nameIndex).length() > 0) {
                    filename = cursor.getString(nameIndex);
                }
                else {
                    filename = uri.getLastPathSegment();
                }
                if (sizeIndex > -1 && cursor.getInt(sizeIndex) > 0) {
                    size = cursor.getInt(sizeIndex);
                }
                else {
                    size = -1;
                }
            }
            else {
                filename = uri.getLastPathSegment();
                size = -1;
            }
        }
        else {
            Toast.makeText(((Activity) mContext).getApplicationContext(), "Unable to read file for upload", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadDialog.setMessage("Uploading " + filename);
        if (size > 0) {
            uploadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            uploadDialog.setMax(size);
        }
        else {
            uploadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        uploadDialog.show();

        final Box boxServiceHandler = Box.getInstance(API_KEY);
        final Cancelable cancelable = boxServiceHandler.upload(mToken, Box.UPLOAD_ACTION_UPLOAD, inputStream, filename, 5,
            new FileUploadListener() {

                @Override
                public void onComplete(BoxFile file, final String status) {
                    if (status.equals(FileUploadListener.STATUS_UPLOAD_OK)) {
                        Toast.makeText(((Activity) mContext).getApplicationContext(), "Successfully uploaded " + filename, Toast.LENGTH_SHORT).show();
//                        refresh();
                    }
                    else if (status.equals(FileUploadListener.STATUS_CANCELLED)) {
                        Toast.makeText(((Activity) mContext).getApplicationContext(), "Upload cancelled.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(((Activity) mContext).getApplicationContext(), "Upload failed - " + status, Toast.LENGTH_SHORT).show();
                    }
                    uploadDialog.dismiss();
                }

                @Override
                public void onIOException(final IOException e) {
                    e.printStackTrace();
                    Toast.makeText(((Activity) mContext).getApplicationContext(), "Upload failed - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    uploadDialog.dismiss();
                }

                @Override
                public void onMalformedURLException(final MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(((Activity) mContext).getApplicationContext(), "Upload failed - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    uploadDialog.dismiss();
                }

                @Override
                public void onFileNotFoundException(final FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(((Activity) mContext).getApplicationContext(), "Upload failed - " + e.getMessage(), Toast.LENGTH_LONG).show();
                    uploadDialog.dismiss();
                }

                @Override
                public void onProgress(final long bytesUploaded) {
                    uploadDialog.setProgress((int) bytesUploaded);
                    if (uri.getScheme().equals("content")) {
                        uploadDialog.setMessage("Uploading " + filename + " (" + bytesUploaded + ")");
                    }
                }
            });

        uploadDialog.setCancelable(true);
        uploadDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                cancelable.cancel();
            }
        });
        Toast.makeText(((Activity) mContext).getApplicationContext(), "Click BACK to cancel the upload.", Toast.LENGTH_SHORT).show();
    

	}
}
