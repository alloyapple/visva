package com.visva.android.shoppieandroid.activity;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.visva.android.shoppieandroid.R;
import com.visva.android.shoppieandroid.network.AsyncHttpPost;
import com.visva.android.shoppieandroid.network.AsyncHttpResponseProcess;
import com.visva.android.shoppieandroid.network.NetworkUtility;
import com.visva.android.shoppieandroid.network.ParameterFactory;
import com.visva.android.shoppieandroid.webconfig.WebServiceConfig;

public class LoginActivity extends Activity {

	private final String PENDING_ACTION_BUNDLE_KEY = "com.visva.android.shoppieandroid:PendingAction";

	// private Button postStatusUpdateButton;
	// private Button postPhotoButton;
	// private Button pickFriendsButton;
	// private Button pickPlaceButton;
	private LoginButton loginButton;
	private PendingAction pendingAction = PendingAction.NONE;
	private GraphUser user;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private UiLifecycleHelper uiHelper;

	public static String regId;
	private String blueMac;
	private String emeil;
	private String name;
	private String email;
	private static boolean registering = false;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall,
				Exception error, Bundle data) {
			Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall,
				Bundle data) {
			Log.d("HelloFacebook", "Success!");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		setContentView(R.layout.page_register);

		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton
				.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
					@Override
					public void onUserInfoFetched(GraphUser user) {
						Log.e("usser", "adfhd " + user);
						if (NetworkUtility.getInstance(LoginActivity.this)
								.isNetworkAvailable()) {
							LoginActivity.this.user = user;
							updateUI();
							// It's possible that we were waiting for this.user
							// to
							// be populated in order to post a
							// status update.
							handlePendingAction();
						} else {
							showToast(getString(R.string.network_unvailable));
						}
					}
				});
	}

	public void onClickedLogin(View v) {

		//List<NameValuePair> params = ParameterFactory.checkUserExist(device_id);
		//createAccountByShoppie(params);
//		public void shareGift(final DialogShareFacebook dialogShare, Gift gift) {
//			if (Session.getActiveSession() == null) {
//				Session session = new Session.Builder(this).build();
//			    Session.setActiveSession(session);
////			    currentSession = session;
//				showToast("Báº¡n pháº£i login facebook trÆ°á»›c");
//				Log.e("ActivityGift line 130", "Session null");
//				return;
//			}
//			String message;
//			String desc;
//			String link;
//			String pic;
//			String name;
//			// loginFacebook();
//			message = "Ä�á»•i quÃ : " + gift.giftName + " chá»‰ vá»›i " + gift.pieQty + " Pie.";
//			desc = gift.description + "\ná»¨ng dá»¥ng di Ä‘á»™ng shoppie.\nDÃ¹ng thá»­ ngay Ä‘á»ƒ nháº­n quÃ .";
//			link = "http://shoppie.com.vn/";
//			pic = WebServiceConfig.HEAD_IMAGE + gift.giftImage;
//			Log.e("FB: link image", pic);
//			name = gift.giftName;
//			// SUtilFacebook.getInstance(ActivityProductGift.this).shareStatus(dialogShare.address.getText()
//			// + " \n" + message, name, desc, link, pic, dialogShare);
//
//			Bundle param = new Bundle();
//			param.putString("message", message);
//			param.putString("name", name);
//			param.putString("description", desc);
//			param.putString("link", link);
//			param.putString("picture", pic);
//			Session session = Session.getActiveSession();
//			if (session == null || !session.isOpened()) {
//				// load session
//				Log.e("ActivityProductGift line 220: Session is null", "NULL");
//				return;
//			}
//			try {
//				WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(LoginActivity.this, Session.getActiveSession(), param)).setOnCompleteListener(new OnCompleteListener() {
//
//					public void onComplete(Bundle values, FacebookException error) {
//						if (error != null) {
//							showToast("ChÆ°a Ä‘Äƒng Ä‘Æ°á»£c lÃªn Facebook.");
//						} else {
//							// showToast("Ä�Äƒng thÃ nh cÃ´ng.");
//						}
//					}
//				}).build();
//				feedDialog.show();
//			} catch (FacebookException e) {
//				e.printStackTrace();
//			}
//		}
	}

	public void createAccountByShoppie(List<NameValuePair> params) {
		AsyncHttpPost postCreateAccount = new AsyncHttpPost(this,
				new AsyncHttpResponseProcess(this) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						Log.e("response", "respone " + response);
						// Intent intent = new Intent(LoginActivity.this,
						// HomeActivity.class);
						// startActivity(intent);
						// finish();
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, params, true);
		String url = WebServiceConfig.URL_SHOPPIE_HOME
				+ WebServiceConfig.URL_REGISTER;
		postCreateAccount.execute(url);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResume();

		// Call the 'activateApp' method to log an app event for use in
		// analytics and advertising reporting. Do so in
		// the onResume methods of the primary Activities that an app may be
		// launched into.
		AppEventsLogger.activateApp(this);

		updateUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(LoginActivity.this)
					.setTitle(R.string.btn_cancel)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.btn_ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
		updateUI();
	}

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());
		if (enableButtons && user != null) {
			showToast(user.getFirstName() + " logined ");
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		} else {
			showToast("log out");
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			// postPhoto();
			break;
		case POST_STATUS_UPDATE:
			// postStatusUpdate();
			break;
		}
	}

	private void showToast(final String str) {
		// TODO Auto-generated method stub
		Toast.makeText(LoginActivity.this, str, Toast.LENGTH_SHORT).show();
	}
}
