package com.fgsecure.ujoolt.app.twitter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import twitter4j.util.ImageUpload;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.fgsecure.ujoolt.app.define.LoginType;
import com.fgsecure.ujoolt.app.info.WebServiceConfig;
import com.fgsecure.ujoolt.app.network.AsyncHttpBase;
import com.fgsecure.ujoolt.app.network.AsyncHttpGet;
import com.fgsecure.ujoolt.app.network.AsyncHttpResponseListener;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.twitter.TwitterApp.TwDialogListener;
import com.fgsecure.ujoolt.app.utillity.StringUtility;
import com.google.android.maps.GeoPoint;

public class TwitterConnector {

	// private static final String twitter_consumer_key_authen =
	// "tZiQRdFeZvBd3nnHT2zqhg";
	// private static final String twitter_secret_key_authen =
	// "4lWOXdK1qVeVZMxUXnY9XDVkrKfqQQQE9Ar2HuE";
	// private static final String twitpic_api_key_authen =
	// "b110fe2723d0da083a9cb19004fdf640";

	private final String twitter_consumer_key_authen = "qVnlQV5cAo7Wm6hTh98A";
	private final String twitter_secret_key_authen = "33TW2I6oyVg76nY0nLmaaFWUOjd5YKTOpn5yrfU";
	private final String twitpic_api_key_authen = "7f1b8af913739dccb21572f4f9a15b54";

	public MainScreenActivity mainScreenActivity;

	public TwitterApp mTwitterAuthen;
	public String urlMaptwit;
	public String urlTwipic;
	public String urlToPost;
	public Handler handler;
	public String token;
	public String tokenSecret;
	public boolean isStartAuthen;
	public boolean isConfirm;

	AccountManager mAccountManager;
	AccountManagerFuture<Bundle> c;

	public TwitterConnector(MainScreenActivity mainScreenActivity) {
		this.mainScreenActivity = mainScreenActivity;
		mTwitterAuthen = new TwitterApp(mainScreenActivity, twitter_consumer_key_authen,
				twitter_secret_key_authen);
		mTwitterAuthen.setListener(mTwLoginDialogListener);
		mAccountManager = AccountManager.get(mainScreenActivity);
		handler = new Handler();
	}

	public Runnable updateTokenTask = new Runnable() {

		@Override
		public void run() {

			if (token != null && tokenSecret != null) {
				if (!isStartAuthen) {
					isStartAuthen = true;
					mTwitterAuthen.mAccessToken = new AccessToken(token, tokenSecret);
					getUserInfo(token, tokenSecret);
					try {
						String authUrl = mTwitterAuthen.mHttpOauthprovider.retrieveRequestToken(
								mTwitterAuthen.mHttpOauthConsumer, TwitterApp.CALLBACK_URL);
						final String verifier = mTwitterAuthen.getVerifier(authUrl);
						mTwitterAuthen.mHttpOauthprovider.retrieveAccessToken(
								mTwitterAuthen.mHttpOauthConsumer, verifier);
					} catch (OAuthMessageSignerException e) {
						e.printStackTrace();
						mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
					} catch (OAuthNotAuthorizedException e) {
						e.printStackTrace();
						mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
					} catch (OAuthExpectationFailedException e) {
						e.printStackTrace();
						mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
					} catch (OAuthCommunicationException e) {
						e.printStackTrace();
						mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
					}
				}
			}
			handler.postDelayed(this, 25);
		}
	};

	public void connect() {
		CookieSyncManager.createInstance(mainScreenActivity);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		if (mTwitterAuthen.hasAccessToken()) {
			mTwitterAuthen.resetAccessToken();
			mTwitterAuthen.authorize();
		} else {
			mTwitterAuthen.authorize();
		}
		// mainScreenActivity.view_login.setVisibility(View.INVISIBLE);
		// tokenForTwitter();
	}

	public void logout() {
		mTwitterAuthen.resetAccessToken();
	}

	public void getUserInfo(String token, String secret) {
		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void after(int statusCode, HttpResponse response) {

						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							// MainScreenActivity.isLoading = false;
							// mainScreenActivity.closeProgressDialog();
							if (response != null) {
								String json = null;
								try {
									json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
								} catch (ParseException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								if (json != null) {
									Log.e("reponse get jolts", "" + json);
								}
							} else {
								mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
							}
						} else {
							mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
						}
					}

					@Override
					public void before() {
						// if (mainScreenActivity.language == Language.ENGLISH)
						// {
						// mainScreenActivity.isLoading = true;
						// mainScreenActivity.showProgressDialog("Loading...");
						// } else {
						// mainScreenActivity.showProgressDialog("Chargement...");
						// }
					}
				});

		asyncHttpGet
				.execute("https://api.twitter.com/1/account/verify_credentials.json?include_entities=1"
						+ "&skip_status=1");
	}

	public void tokenForTwitter() {
		Account[] accts = mAccountManager.getAccountsByType("com.twitter.android.auth.login");
		if (accts.length > 0) {
			System.out.println("here");
			Account acct = accts[0];
			// getUserInfo(acct);
			// final String[] consumerToken = { "" };
			// final String[] secretToken = { "" };
			// Bundle bundle = new Bundle();
			c = mAccountManager.getAuthToken(acct, "com.twitter.android.oauth.token", null,
					mainScreenActivity, new AccountManagerCallback<Bundle>() {

						@Override
						public void run(AccountManagerFuture<Bundle> arg0) {
							try {
								Bundle b = arg0.getResult();
								token = b.getString(AccountManager.KEY_AUTHTOKEN);

								Log.e("TW", "twitter THIS AUHTOKEN: " + token);
								Intent launch = (Intent) b.get(AccountManager.KEY_INTENT);
								if (launch != null) {
									mainScreenActivity.startActivityForResult(launch, 0);
									return;
								}
							} catch (Exception e) {
							}
						}
					}, null);

			c = mAccountManager.getAuthToken(acct, "com.twitter.android.oauth.token.secret", null,
					mainScreenActivity, new AccountManagerCallback<Bundle>() {

						@Override
						public void run(AccountManagerFuture<Bundle> arg0) {
							try {
								Bundle b = arg0.getResult();
								tokenSecret = b.getString(AccountManager.KEY_AUTHTOKEN);
								Log.e("TW", "twitter THIS AUHTOKEN secret : " + tokenSecret);
								Intent launch = (Intent) b.get(AccountManager.KEY_INTENT);
								if (launch != null) {
									mainScreenActivity.startActivityForResult(launch, 0);
									return;
								}
							} catch (Exception e) {
							}
						}
					}, null);

		} else {
			try {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.putExtra(Intent.EXTRA_TEXT, "this is a tweet");
				intent.setType("text/plain");
				final PackageManager pm = mainScreenActivity.getPackageManager();
				final List<?> activityList = pm.queryIntentActivities(intent, 0);
				int len = activityList.size();
				for (int i = 0; i < len; i++) {
					final ResolveInfo app = (ResolveInfo) activityList.get(i);
					if ("com.twitter.android.PostActivity".equals(app.activityInfo.name)) {
						final ActivityInfo activity = app.activityInfo;
						final ComponentName name = new ComponentName(
								activity.applicationInfo.packageName, activity.name);
						intent.addCategory(Intent.CATEGORY_LAUNCHER);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
						intent.setComponent(name);
						mainScreenActivity.startActivityForResult(intent, 100);
						break;
					}
				}
			} catch (final ActivityNotFoundException e) {
				Log.i("twitter", "no twitter native", e);
			}
		}
	}

	public final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		@Override
		public void onComplete(String value) {

			String userName = mTwitterAuthen.getUsername();
			String loginUserId = mTwitterAuthen.getUserId();

			if (mainScreenActivity.myLoginType != LoginType.NONE) {
				mainScreenActivity.joltHolder.syncUser(mainScreenActivity.myUserIdUjoolt,
						mainScreenActivity.myUserIdFacebook, loginUserId, LoginType.TWITTER,
						new GeoPoint(mainScreenActivity.mLatitudeE6,
								mainScreenActivity.mLongitudeE6));
			}

			mainScreenActivity.setAccount(userName, loginUserId, LoginType.TWITTER);
			mainScreenActivity.callJoltView();

			if (mainScreenActivity.viewLogin.isLogin) {
				mainScreenActivity.finishLogin();
			} else {
				mainScreenActivity.finishAuthen();
			}
		}

		@Override
		public void onError(String value) {
			Log.e("Error Info ", "" + value);
			mainScreenActivity.isStartTwitterConnect = false;
			mainScreenActivity.isTwitterConnected = false;
			mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
			mainScreenActivity.setIconSocialNetwork();
			mainScreenActivity.showDialogAuthenError();
		}

		@Override
		public void onCancel() {
			Log.d("Twitter-authorize", "Login canceled");
			mTwitterAuthen.mListener.onCancel();
			mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
		}
	};

	public void postImage(String path, String status) {
		new ImageSender(path, status).execute();
	}

	public class ImageSender extends AsyncTask<URL, Integer, Long> {
		private String url;
		public String path;
		public String status;

		public ImageSender(String p, String status) {
			path = p;
			this.status = status;
			Log.e("Path url ", "" + path);
		}

		protected void onPreExecute() {
		}

		protected Long doInBackground(URL... urls) {
			long result = 0;

			TwitterSession twitterSession = new TwitterSession(mainScreenActivity);
			AccessToken accessToken = twitterSession.getAccessToken();
			Log.e("Access token ", "" + accessToken.getToken());
			Log.e("Access token secret ", "" + accessToken.getTokenSecret());
			Configuration conf = new ConfigurationBuilder()
					.setOAuthConsumerKey(twitpic_api_key_authen)
					.setOAuthAccessToken(accessToken.getToken())
					.setOAuthAccessTokenSecret(accessToken.getTokenSecret()).build();

			OAuthAuthorization auth = new OAuthAuthorization(conf, conf.getOAuthConsumerKey(),
					conf.getOAuthConsumerSecret(), new AccessToken(conf.getOAuthAccessToken(),
							conf.getOAuthAccessTokenSecret()));

			// Toast.makeText(SendImageActivity.this, ""+auth,
			// Toast.LENGTH_LONG);
			ImageUpload upload = ImageUpload.getTwitpicUploader(twitpic_api_key_authen, auth);
			// ImageUpload u = ImageUpload.get

			try {
				Log.e("path ", "" + path);
				if (path != null && !path.equalsIgnoreCase("")) {
					url = upload.upload(new File(path));
					result = 1;
					// mTwitter.updateStatus(""+url);

				}

			} catch (Exception e) {

				e.printStackTrace();
			}

			return result;
		}

		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Long result) {

			String text = (result == 1) ? "Image sent successfully.\n Twitpic url is: " + url
					: "Failed to send image";

			if (result == 1) {
				String message = status + " with picture " + url + " at location " + urlMaptwit;
				new TextSender(message).execute();
				Log.e("URL", "" + text);
			} else {
				String message = status + " at " + urlMaptwit;
				new TextSender(message).execute();
				Log.e("URL", "" + text);
			}

		}
	}

	public class TextSender extends AsyncTask<URL, Integer, Long> {
		private String text;

		public TextSender(String text) {
			this.text = text;
		}

		@Override
		protected Long doInBackground(URL... params) {
			try {
				mTwitterAuthen.updateStatus(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	public void postInfo(int lati, int longi, String status, String urlImage) {
		final String path = urlImage;
		final String message = status;
		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void after(int statusCode, HttpResponse response) {

						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							if (response != null) {

								try {
									HttpEntity entity = response.getEntity();
									InputStream is = entity.getContent();
									String s = StringUtility.convertStreamToString(is);
									Log.e("response get", "" + s);
									urlMaptwit = s;

									// if(mainScreenActivity.myLoginType.equalsIgnoreCase("TW")){
									// if(s != null){
									// mainScreenActivity.jolter.setAddress(urlMaptwit);
									// } else {
									// mainScreenActivity.jolter.setAddress("");
									// }
									// mainScreenActivity.jolter.setLoginUserid(mainScreenActivity.myLoginUserid);
									// mainScreenActivity.joltHolder.postJolt(mainScreenActivity.jolter);
									// }
									// postMessage(s);
									if (!path.equalsIgnoreCase("") && !message.equalsIgnoreCase("")) {
										postImage(path, message);
									} else if (path.equalsIgnoreCase("")
											&& !message.equalsIgnoreCase("")) {
										postMessage(message + " at " + urlMaptwit);
									} else if (path.equalsIgnoreCase("")
											&& message.equalsIgnoreCase("")) {
										postMessage("I'm here " + urlMaptwit);
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								mainScreenActivity.showDialogNetworkError();
							}
						} else {
							mainScreenActivity.showDialogNetworkError();
						}
					}

					@Override
					public void before() {
					}
				});
		asyncHttpGet.execute(WebServiceConfig.URL_GET_LOCATION_URL_TWITPIC + "?lat=" + lati / 1E6
				+ "&long=" + longi / 1E6);
	}

	public void postMessage(String message) {
		new TextSender(message).execute();
	}

	public void postLocation(int lati, int longi) {
		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void after(int statusCode, HttpResponse response) {

						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							if (response != null) {

								try {
									HttpEntity entity = response.getEntity();
									InputStream is = entity.getContent();
									String s = StringUtility.convertStreamToString(is);
									Log.e("response get", "" + s);
									urlMaptwit = s;
									// postMessage(s);

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								mainScreenActivity.showDialogNetworkError();
							}
						} else {
							mainScreenActivity.showDialogNetworkError();
						}
					}

					@Override
					public void before() {
					}
				});
		asyncHttpGet.execute(WebServiceConfig.URL_GET_LOCATION_URL_TWITPIC + "?lat=" + lati / 1E6
				+ "&long=" + longi / 1E6);
	}
}
