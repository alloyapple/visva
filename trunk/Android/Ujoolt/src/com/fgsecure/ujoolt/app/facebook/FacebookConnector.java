package com.fgsecure.ujoolt.app.facebook;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.fgsecure.ujoolt.app.define.LoginType;
import com.fgsecure.ujoolt.app.facebook.AsyncFacebookRunner.RequestListener;
import com.fgsecure.ujoolt.app.facebook.Facebook.DialogListener;
import com.fgsecure.ujoolt.app.facebook.SessionEvents.AuthListener;
import com.fgsecure.ujoolt.app.facebook.SessionEvents.LogoutListener;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.utillity.Utility;
import com.google.android.maps.GeoPoint;

public class FacebookConnector {

	String TAG = getClass().getSimpleName();

	String[] permissions;
	Handler mHandler;
	MainScreenActivity mainScreenActivity;

	private static final String TOKEN = "access_token";

	SessionListener mSessionListener = new SessionListener();

	public FacebookConnector(String appId, MainScreenActivity activity, String[] permissions) {
		this.permissions = permissions;
		this.mHandler = new Handler();
		this.mainScreenActivity = activity;
		UtilityFacebook.mFacebook = new Facebook(appId, mainScreenActivity);
	}

	/*
	 * Request user name to show on the main screen.
	 */
	// public void requestUserData() {
	// Bundle params = new Bundle();
	// params.putString("fields", "name");
	// UtilityFacebook.mAsyncRunner.request("me", params, new
	// UserRequestListener());
	// }

	public boolean postMessageOnWall(String msg) {
		if (UtilityFacebook.mFacebook.isSessionValid()) {
			Log.e("session valid", "ok");
			Bundle parameters = new Bundle();
			parameters.putString("message", msg);
			try {
				UtilityFacebook.mFacebook.request("me/feed", parameters, "POST");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			Log.e("session is not valid", "ok");
			return false;
		}
	}

	protected void postToFriendWall(String userID) {
		try {
			if (UtilityFacebook.mFacebook.isSessionValid()) {
				String response = UtilityFacebook.mFacebook.request((userID == null) ? "me"
						: userID);

				Bundle params = new Bundle();
				params.putString("message", "put message here");
				params.putString("link", "http://mylink.com");
				params.putString("caption", "{*actor*} just posted this!");
				params.putString("description",
						"description of my link.  Click the link to find out more.");
				params.putString("name", "Name of this link!");
				params.putString("picture", "http://mysite.com/picture.jpg");

				response = UtilityFacebook.mFacebook.request(((userID == null) ? "me" : userID)
						+ "/feed", params, "POST");

				Log.d("Tests", response);
				if (response == null || response.equals("") || response.equals("false")) {
					Log.v("Error", "Blank response");
				}
			} else {
				// no logged in, so relogin
				UtilityFacebook.mFacebook.authorize(this.mainScreenActivity, this.permissions,
						new LoginDialogListener());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login() {
		if (!UtilityFacebook.mFacebook.isSessionValid()) {
			UtilityFacebook.mFacebook.authorize(this.mainScreenActivity, this.permissions,
					new LoginDialogListener());
		}
	}

	public void logout() {
		try {
			UtilityFacebook.mFacebook.logout(mainScreenActivity);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void postImage(Uri photoUri, String description) {
		// Uri photoUri = data.getData();
		if (photoUri != null) {
			Bundle params = new Bundle();

			try {
				params.putByteArray("photo",
						UtilityFacebook.scaleImage(mainScreenActivity, photoUri));
			} catch (IOException e) {
				e.printStackTrace();
			}

			params.putString("caption", description);
			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(UtilityFacebook.mFacebook);
			mAsyncRunner.request("me/photos", params, "POST", new SampleUploadListener(), null);
		} else {

		}
	}

	public void getFacebookFriend() {
		mainScreenActivity.arrFriendsFacebook = new ArrayList<String>();
		Bundle parameters = new Bundle();
		String mAccessToken = UtilityFacebook.mFacebook.getAccessToken();

		parameters.putString("format", "json");
		parameters.putString(TOKEN, mAccessToken);

		String url = "https://graph.facebook.com/me/friends";
		String response = null;
		try {
			response = Util.openUrl(url, "GET", parameters);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject obj = null;
		try {
			obj = Util.parseJson(response);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FacebookError e) {
			e.printStackTrace();
		}

		JSONArray array = null;
		if (obj != null) {
			array = obj.optJSONArray("data");
		}

		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				String id = null;
				try {
					id = array.getJSONObject(i).getString("id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mainScreenActivity.arrFriendsFacebook.add(id);
			}
		}
	}

	public void postInfoOnWall(Uri photoUri, String status, int lati, int longi) {
		final int lat = lati;
		final int longitude = longi;
		final String message = status;
		Log.e("thang:facebook post", "uri " + photoUri);
		Log.i("thang:facebook postaaaaaaaaaaaaaaaaaaaaaaaaaa", "status " + status);
		if (photoUri != null) {
			new PostInfoTask(message, photoUri, lat, longitude).execute();
		} else {
			new PostInfoTask(message, lat, longitude).execute();
		}
	}

	public String postLocation(PlaceInfo placeInfo) throws JSONException, FileNotFoundException,
			MalformedURLException, IOException {
		Bundle params = new Bundle();
		params.putString("access_token", UtilityFacebook.mFacebook.getAccessToken());
		params.putString("place", "" + placeInfo.getPlaceID());
		params.putString("Message", "I m here in this place");
		JSONObject coordinates = new JSONObject();
		coordinates.put("latitude", "" + placeInfo.getLatitude());
		coordinates.put("longitude", "" + placeInfo.getLongitude());
		coordinates.toString();
		params.putString("coordinates", coordinates.toString());
		String res = UtilityFacebook.mFacebook.request("me/checkins", params, "POST");
		Log.e("response post location", "" + res);
		return res;
	}

	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
			SessionStore
					.save(UtilityFacebook.mFacebook, mainScreenActivity.getApplicationContext());
			mainScreenActivity.viewLogin.setVisibility(View.INVISIBLE);
			mainScreenActivity.mapController.animateTo(new GeoPoint(mainScreenActivity.mLatitudeE6,
					mainScreenActivity.mLongitudeE6));

			JSONObject json;
			String userName = "";
			String loginUserId = "";
			try {
				json = Util.parseJson(UtilityFacebook.mFacebook.request("me"));
				userName = json.getString("name");
				loginUserId = json.getString("id");
			} catch (FacebookError e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (mainScreenActivity.myLoginType != LoginType.NONE) {
				mainScreenActivity.joltHolder.syncUser(mainScreenActivity.myUserIdUjoolt,
						loginUserId, mainScreenActivity.myUserIdTwitter, LoginType.FACEBOOK,
						new GeoPoint(mainScreenActivity.mLatitudeE6, mainScreenActivity.mLongitudeE6));
			}

			mainScreenActivity.setAccount(userName, loginUserId, LoginType.FACEBOOK);

			if (!mainScreenActivity.isPublicPostJolt) {
				mainScreenActivity.setMainAccount(userName, loginUserId, LoginType.FACEBOOK);
			}

			Log.e(TAG, "Public: " + mainScreenActivity.isPublicPostJolt + ", user: " + userName);
			mainScreenActivity.callJoltView();

			if (!mainScreenActivity.viewLogin.isLogin) {
				mainScreenActivity.finishAuthen();
			} else {
				mainScreenActivity.finishLogin();
			}
		}

		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			SessionStore
					.save(UtilityFacebook.mFacebook, mainScreenActivity.getApplicationContext());
		}

		public void onAuthFail(String error) {
		}

		public void onLogoutBegin() {
		}

		public void onLogoutFinish() {
			SessionStore.clear(mainScreenActivity.getApplicationContext());
		}
	}

	public class SampleUploadListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			try {
				// process the response here: (executed in background thread)
				Log.e("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				json.getString("src");

				// then post the processed result back to the UI thread
				// if we do not do this, an runtime exception will be generated
				// e.g. "CalledFromWrongThreadException: Only the original
				// thread that created a view hierarchy can touch its views."

			} catch (JSONException e) {
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
		}
	}

	/*
	 * Callback for fetching current user's name, picture, uid.
	 */
	// public class UserRequestListener extends BaseRequestListener {
	//
	// public void onComplete(final String response, final Object state) {
	// JSONObject jsonObject;
	// try {
	// jsonObject = new JSONObject(response);
	//
	// String userName = jsonObject.getString("name");
	// UtilityFacebook.userUID = jsonObject.getString("id");
	// String loginUserId = UtilityFacebook.userUID;
	// f
	// mainScreenActivity.setAccount(userName, loginUserId, LoginType.FACEBOOK);
	// if (!mainScreenActivity.isPublicPostJolt) {
	// mainScreenActivity.setMainAccount(userName, loginUserId,
	// LoginType.FACEBOOK);
	// }
	// Log.e(TAG,
	// "Public: "+mainScreenActivity.isPublicPostJolt+", user: "+userName);
	// mainScreenActivity.callJoltView();
	//
	// mHandler.post(new Runnable() {
	//
	// public void run() {
	//
	// if (!mainScreenActivity.viewLogin.isLogin) {
	// mainScreenActivity.finishAuthen();
	// } else {
	// mainScreenActivity.finishLogin();
	// }
	// }
	// });
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	public class PostInfoTask extends AsyncTask<String, String, String> {
		int type; // 1 post image and discript, 0 only post message
		// public Bitmap bitmap;
		public Uri photoUri;
		public String status;
		public int lati;
		public int longi;

		public PostInfoTask(String status, int lati, int longi) {
			type = 0;
			this.status = status;
			this.lati = lati;
			this.longi = longi;
		}

		public PostInfoTask(String status, Uri photoUri, int lati, int longi) {
			type = 1;
			this.status = status;
			this.photoUri = photoUri;
			this.lati = lati;
			this.longi = longi;
		}

		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			if (type == 0) {
				Log.e("Post Message", "ok");
				postMessageOnWall(status);
			} else {
				postImage(photoUri, status);
			}
			return null;
		}
	}

	public PlaceInfo getMinLocation(ArrayList<PlaceInfo> arrayList, float lati, float longi) {
		PlaceInfo placeInfo = null;
		if (arrayList.size() > 0) {
			int size = arrayList.size();
			placeInfo = arrayList.get(0);
			float min = Utility.getDistance(lati, longi, placeInfo.getLatitude(),
					placeInfo.getLongitude());
			for (int i = 1; i < size; i++) {
				PlaceInfo placeInfo2 = arrayList.get(i);
				float d = Utility.getDistance(lati, longi, placeInfo2.getLatitude(),
						placeInfo2.getLongitude());
				if (d < min) {
					min = d;
					placeInfo = placeInfo2;
				}
			}
			return placeInfo;
		} else {
			return null;
		}
	}

	public void uploadVideo(String fileName, String message) {
		if (fileName == null || fileName.equalsIgnoreCase("")) {
			Log.e("Lemon ", "Facebook Video upload path : null");

		} else {
			Log.i("Lemon ", "Facebook Video upload path : " + fileName);
			Log.i("Lemon ", "Facebook Video upload message : " + message);
			byte[] data = null;
			String dataPath = fileName;
			String dataMsg = message;
			Bundle param;
			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(UtilityFacebook.mFacebook);
			InputStream is = null;
			try {
				is = new FileInputStream(dataPath);
				data = readBytes(is);
				param = new Bundle();
				// title default: currente datetime
				// param.putString("title", dataMsg);
				param.putString("description", dataMsg);
				param.putString("filename", "test.3gp");
				param.putByteArray("video", data);
				mAsyncRunner.request("me/videos", param, "POST", new fbRequestListener(), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

			}
		}
	}

	public class fbRequestListener implements RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			Log.d("Video Upload RESPONSE", "" + response);

		}

		@Override
		public void onIOException(IOException e, Object state) {
			Log.d("Video Upload RESPONSE", "" + e);

		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			Log.d("Video Upload RESPONSE", "" + e);

		}

		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			Log.d("Video Upload RESPONSE", "" + e);
		}

	}

	public byte[] readBytes(InputStream inputStream) throws IOException {
		// This dynamically extends to take the bytes you read.
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		// This is storage overwritten on each iteration with bytes.
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		// We need to know how may bytes were read to write them to the
		// byteBuffer.
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		Log.i(TAG, "Byte array length video upload : " + byteBuffer.size());
		// And then we can return your byte array.
		return byteBuffer.toByteArray();
	}
}
