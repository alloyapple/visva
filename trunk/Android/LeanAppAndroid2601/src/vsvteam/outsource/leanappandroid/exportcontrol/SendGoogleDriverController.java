package vsvteam.outsource.leanappandroid.exportcontrol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.google.api.client.http.FileContent;
//import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.DriveScopes;

@SuppressWarnings("deprecation")
public class SendGoogleDriverController {
	private Context mContext;
	private AccountManager mAccountManager;
	private String mApiKey="AIzaSyC7bmvtm7i0o5BrOGhQ5TuF3IZp_QcJ7Jg";

	public SendGoogleDriverController(Context pContext, String pApiKey) {
		// TODO Auto-generated constructor stub
		mContext = pContext;

	}

	public boolean uploadFile(String pFilePath) {
		mAccountManager = AccountManager.get(mContext);
		mAccountManager.getAuthToken((mAccountManager.getAccounts())[0],
				"ouath2:" + DriveScopes.DRIVE, new Bundle(), true,
				new OnTokenAcquired(mContext, mApiKey, pFilePath), null);
		return true;
	}

	public java.io.File downloadGFileToJFolder(Drive drive, String token,
			com.google.api.services.drive.model.File gFile, java.io.File jFolder)
			throws IOException {
		if (gFile.getDownloadUrl() != null
				&& gFile.getDownloadUrl().length() > 0) {
			if (jFolder == null) {
				jFolder = Environment.getExternalStorageDirectory();
				jFolder.mkdirs();
			}
			try {

				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(gFile.getDownloadUrl());
				get.setHeader("Authorization", "Bearer " + token);
				HttpResponse response = client.execute(get);

				InputStream inputStream = response.getEntity().getContent();
				jFolder.mkdirs();
				java.io.File jFile = new java.io.File(jFolder.getAbsolutePath()
						+ "/" + getGFileName(gFile)); // getGFileName() is my
														// own method... it just
														// grabs
														// originalFilename if
														// it exists or title if
														// it doesn't.
				FileOutputStream fileStream = new FileOutputStream(jFile);
				byte buffer[] = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) > 0) {
					fileStream.write(buffer, 0, length);
				}
				fileStream.close();
				inputStream.close();
				return jFile;
			} catch (IOException e) {
				// Handle IOExceptions here...
				return null;
			}
		} else {
			// Handle the case where the file on Google Drive has no length
			// here.
			return null;
		}
	}

	public String getGFileName(com.google.api.services.drive.model.File gFile) {
		return gFile.toString();
	}

	public void updateGFileFromJFile(Drive drive,
			com.google.api.services.drive.model.File gFile, java.io.File jFile)
			throws IOException {
		FileContent gContent = new FileContent("text/csv", jFile);
		gFile.setModifiedDate(new DateTime(false, jFile.lastModified(), 0));
		gFile = drive.files().update(gFile.getId(), gFile, gContent)
				.setSetModifiedDate(true).execute();
	}
}

class OnTokenAcquired implements AccountManagerCallback<Bundle> {
	private Context mContext;
	private String mApiKey;
	private String mFilePath;
	private boolean alreadyTriedAgain;

	public OnTokenAcquired(Context pContext, String pApiKey, String pFilePath) {
		// TODO Auto-generated constructor stub
		mApiKey = pApiKey;
		mFilePath = pFilePath;
		mContext = pContext;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(AccountManagerFuture<Bundle> result) {
		try {
			final String token = result.getResult().getString(
					AccountManager.KEY_AUTHTOKEN);
			HttpTransport httpTransport = new NetHttpTransport();
			JacksonFactory jsonFactory = new JacksonFactory();
			Drive.Builder b = new Drive.Builder(httpTransport, jsonFactory,
					null);
			b.setJsonHttpRequestInitializer(new JsonHttpRequestInitializer() {

				@Override
				public void initialize(JsonHttpRequest arg0) throws IOException {
					// TODO Auto-generated method stub
					DriveRequest driveRequest = (DriveRequest) arg0;
					driveRequest.setPrettyPrint(true);
					driveRequest.setKey(mApiKey);
					driveRequest.setOauthToken(token);
				}
			});

			final Drive drive = b.build();
			final com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
			body.setTitle("My Test File");
			body.setDescription("A Test File");
			body.setMimeType("text/plain");
			final FileContent mediaContent = new FileContent("text/plain",
					new File(mFilePath));
			new Thread(new Runnable() {
				public void run() {
					try {
						drive.files().insert(body, mediaContent).execute();
						alreadyTriedAgain = false; // Global boolean to make
													// sure you don't repeatedly
													// try too many times when
													// the server is down or
													// your code is faulty...
													// they'll block requests
													// until the next day if you
													// make 10 bad requests, I
													// found.
					} catch (IOException e) {
						if (!alreadyTriedAgain) {
							alreadyTriedAgain = true;
							AccountManager am = AccountManager.get(mContext);
							am.invalidateAuthToken(am.getAccounts()[0].type,
									null); // Requires the permissions
											// MANAGE_ACCOUNTS & USE_CREDENTIALS
											// in the Manifest
							am.getAuthToken((am.getAccounts())[0], "ouath2:"
									+ DriveScopes.DRIVE, new Bundle(), true,
									new OnTokenAcquired(mContext, mApiKey, ""),
									null);
						} else {
							// Give up. Crash or log an error or whatever you
							// want.
						}
					}
				}
			}).start();
			// insert code when upload success
		} catch (OperationCanceledException e) {
			// Handle it...
		} catch (AuthenticatorException e) {
			// Handle it...
		} catch (IOException e) {
			// Handle it...
		}
	}
}