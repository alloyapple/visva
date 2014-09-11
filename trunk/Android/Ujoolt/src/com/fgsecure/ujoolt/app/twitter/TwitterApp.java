package com.fgsecure.ujoolt.app.twitter;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.http.AccessToken;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.google.android.maps.GeoPoint;

/**
 * Twitter main class.
 * 
 * 
 */
public class TwitterApp {
	public Twitter mTwitter;
	private TwitterSession mSession;
	public AccessToken mAccessToken;
	public CommonsHttpOAuthConsumer mHttpOauthConsumer;
	public OAuthProvider mHttpOauthprovider;
	public String mConsumerKey;
	public String mSecretKey;
	private ProgressDialog mProgressDlg;
	public TwDialogListener mListener;
	public int flag = 0;
	public static final String CALLBACK_URL = "twitterapp://connect";
	private static final String TAG = "TwitterApp";
	MainScreenActivity mainScreenActivity;

	public TwitterApp(MainScreenActivity mainScreenActivity, String consumerKey, String secretKey) {
		this.mainScreenActivity = mainScreenActivity;
		mTwitter = new TwitterFactory().getInstance();
		mSession = new TwitterSession(mainScreenActivity);
		mProgressDlg = new ProgressDialog(mainScreenActivity);

		mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mConsumerKey = consumerKey;
		mSecretKey = secretKey;

		initOauthConsumer();

		mHttpOauthprovider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token",
				"http://twitter.com/oauth/access_token", "http://twitter.com/oauth/authorize");
		mAccessToken = mSession.getAccessToken();

		// configureToken();
	}

	public void initOauthConsumer() {
		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey, mSecretKey);
	}

	public void setListener(TwDialogListener listener) {
		mListener = listener;
	}

	@SuppressWarnings("deprecation")
	public void configureToken() {
		if (mAccessToken != null) {
			// int i =
			mAccessToken.getUserId();
			if (flag == 0) {
				mTwitter.setOAuthConsumer(mConsumerKey, mSecretKey);
				mTwitter.setOAuthAccessToken(mAccessToken);
				flag++;
			} else {
				mTwitter = new TwitterFactory().getInstance();
				mTwitter.setOAuthConsumer(mConsumerKey, mSecretKey);
				mTwitter.setOAuthAccessToken(mAccessToken);
			}
			// try {
			// String screenName = mTwitter.getScreenName();
			// // mTwitter.getU
			// } catch (IllegalStateException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (TwitterException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
	}

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void resetAccessToken() {
		mSession.resetAccessToken();
		mAccessToken = null;
		// if (mAccessToken != null) {
		//
		//
		//
		// }
		// mHttpOauthConsumer = null;
		//
		// mHttpOauthprovider = null;

		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey, mSecretKey);

		mHttpOauthprovider = new DefaultOAuthProvider("http://twitter.com/oauth/request_token",
				"http://twitter.com/oauth/access_token", "http://twitter.com/oauth/authorize");
	}

	public String getUsername() {
		return mSession.getUsername();
	}

	public String getUserId() {
		return mSession.getUserId();
	}

	public void updateStatus(String status) throws Exception {
		try {
			mTwitter.updateStatus(status);
		} catch (TwitterException e) {
			throw e;
		}
	}

	public void authorize() {
		mProgressDlg.setMessage("Initializing...");
		mProgressDlg.show();
		// mainScreenActivity.view_login.setVisibility(View.INVISIBLE);

		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;

				try {
					authUrl = mHttpOauthprovider.retrieveRequestToken(mHttpOauthConsumer,
							CALLBACK_URL);

					what = 0;

					Log.e("Request token url ", "" + authUrl);
				} catch (Exception e) {
					Log.d(TAG, "Failed to get request token");
//					mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}

	public void processToken(String callbackUrl) {
		mProgressDlg.setMessage("Finalizing...");
		mProgressDlg.show();
		mainScreenActivity.viewLogin.setVisibility(View.INVISIBLE);
		mainScreenActivity.mapController.animateTo(new GeoPoint(mainScreenActivity.mLatitudeE6,
				mainScreenActivity.mLongitudeE6));

		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;

				try {
					mHttpOauthprovider.retrieveAccessToken(mHttpOauthConsumer, verifier);
					String token = mHttpOauthConsumer.getToken();
					String tkSecret = mHttpOauthConsumer.getTokenSecret();
					mAccessToken = new AccessToken(token, tkSecret);
					configureToken();
					User user = mTwitter.verifyCredentials();
					mSession.storeAccessToken(mAccessToken, user.getName(), "" + user.getId());
					Log.e(TAG, "store ok, name: " + user.getName());
					Log.e(TAG, "store ok, token: " + mAccessToken.getToken());
					Log.e(TAG, "store ok, token secret: " + mAccessToken.getTokenSecret());

					what = 0;
				} catch (Exception e) {
					Log.e(TAG, "Error getting access token");
					mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	public String getVerifier(String callbackUrl) {
		String verifier = "";
		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");

			URL url = new URL(callbackUrl);
			String query = url.getQuery();

			String array[] = query.split("&");

			for (String parameter : array) {
				String v[] = parameter.split("=");
				String s1 = URLDecoder.decode(v[0]);
				String s2 = oauth.signpost.OAuth.OAUTH_VERIFIER;
				if (s1.equalsIgnoreCase(s2)) {
					// if (URLDecoder.decode(v[0]).equalsIgnoreCase(
					// oauth.signpost.OAuth.OAUTH_VERIFIER)) {
					verifier = URLDecoder.decode(v[1]);
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return verifier;
	}

	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {
			@Override
			public void onComplete(String value) {
				processToken(value);
			}

			@Override
			public void onError(String value) {
				Log.e("Error", "=_=");
				mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
				mListener.onError("Failed opening authorization page");
			}

			@Override
			public void onCancel() {
				Log.d("Twitter-authorize", "Login canceled");
				mListener.onCancel();
				mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
			}
		};

		new TwitterDialog(mainScreenActivity, url, listener).show();
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressDlg.dismiss();

			if (msg.what == 1) {
				if (msg.arg1 == 1) {
					mListener.onError("Error getting request token");
					mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
				} else {
					mListener.onError("Error getting access token");
					mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
				}
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					mListener.onComplete("");
			}
		}
	};

	public interface TwDialogListener {

		public void onComplete(String value);

		public void onError(String value);

		public void onCancel();
	}

}