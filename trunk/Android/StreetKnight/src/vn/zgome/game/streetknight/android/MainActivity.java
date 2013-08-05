package vn.zgome.game.streetknight.android;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import vn.zgome.game.streetknight.core.FacebookListener;
import vn.zgome.game.streetknight.core.GameOS;
import vn.zgome.game.streetknight.core.IAPListener;
import vn.zgome.game.streetknight.core.ISmsEvent;
import vn.zgome.game.streetknight.util.Contants;
import vn.zgome.streetknight.android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appota.asdk.callback.TransactionStatusCallback;
import com.appota.asdk.commons.Constants;
import com.appota.asdk.inapp.InappAPI;
import com.appota.asdk.model.CardPayment;
import com.appota.asdk.model.TransactionResult;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;

public class MainActivity extends AndroidApplication implements
		FacebookListener, IAPListener, ISmsEvent {
	private static final String SMS_NUMBER = "6765";
	private static final String SMS_MESSAGE = "NAP 42G3";

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");

	// Uri used in handleError() below
	private static final Uri M_FACEBOOK_URL = Uri
			.parse("http://m.facebook.com");

	private UiLifecycleHelper mUiLifecycleHelper;

	// Parameters of a WebDialog that should be displayed
	private WebDialog dialog = null;
	private String dialogAction = null;
	private Bundle dialogParams = null;

	private boolean isResumed = false;

	/** Called when the activity is first created. */
	GameOS game;
	View gameView;
	// **** appota ****//
	InappAPI mIAP;
	SmsCreate mSmsCreate;
	static SharedPreferences mPreference;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().addFlags(6815872);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		// Create the layout
		RelativeLayout layout = new RelativeLayout(this);

		game = new GameOS(this);
		game.android = new AndroidFunctionImp(this);
		game.iapListener = this;
		game.facebookListener = this;
		// Create the libgdx View
		gameView = initializeForView(game, false);

		// Add the libgdx view
		layout.addView(gameView);

		// Hook it all up
		setContentView(layout);

		mIAP = InappAPI.getInstance().setContext(this);
		mPreference = getSharedPreferences(Constants.PREF_ACCESS_TOKEN,
				Context.MODE_PRIVATE);
		mSmsCreate = new SmsCreate(this, this);

		mUiLifecycleHelper = new UiLifecycleHelper(this,
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						updateView();
						onSessionStateChanged(session, state, exception);
					}
				});
		mUiLifecycleHelper.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			boolean loggedInState = savedInstanceState.getBoolean(
					MainApplication.getLoggedInKey(), false);
			((MainApplication) getApplication()).setLoggedIn(loggedInState);

			if (((MainApplication) getApplication()).isLoggedIn()
					&& (((MainApplication) getApplication()).getCurrentFBUser() == null)) {
				try {
					// currentFBUser
					String currentFBUserJSONString = savedInstanceState
							.getString(MainApplication.getCurrentFbUserKey());
					if (currentFBUserJSONString != null) {
						GraphUser currentFBUser = GraphObject.Factory.create(
								new JSONObject(currentFBUserJSONString),
								GraphUser.class);
						((MainApplication) getApplication())
								.setCurrentFBUser(currentFBUser);
					}

				} catch (JSONException e) {
					Log.e(MainApplication.TAG, e.toString());
				}
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		if (dialog != null) {
			showDialogWithoutNotificationBar(dialogAction, dialogParams);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isResumed = false;

		mUiLifecycleHelper.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		isResumed = true;

		mUiLifecycleHelper.onResume();

		// if (dialogParams != null) {
		// // if (ensureOpenSession()) {
		// showDialogWithoutNotificationBar("feed", dialogParams);
		// // }
		// }

		// Measure mobile app install ads
		// Ref: https://developers.facebook.com/docs/tutorials/mobile-app-ads/
		com.facebook.Settings
				.publishInstallAsync(this, ((MainApplication) getApplication())
						.getString(R.string.app_id));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Call onSaveInstanceState on fbUiLifecycleHelper
		mUiLifecycleHelper.onSaveInstanceState(outState);

		// Save the logged-in state
		outState.putBoolean(MainApplication.getLoggedInKey(),
				((MainApplication) getApplication()).isLoggedIn());

		// Save the currentFBUser
		if (((MainApplication) getApplication()).getCurrentFBUser() != null) {
			outState.putString(MainApplication.getCurrentFbUserKey(),
					((MainApplication) getApplication()).getCurrentFBUser()
							.getInnerJSONObject().toString());
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mUiLifecycleHelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		mUiLifecycleHelper.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.INAPP_BANK_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			String transactionId = data
					.getStringExtra(Constants.TRANSACTION_ID_KEY);
			Toast.makeText(this, transactionId, Toast.LENGTH_SHORT).show();
			mIAP.checkTransactionStatus(transactionId,
					new TransactionStatusCallback() {

						public void onTransactionSuccess(TransactionResult trans) {
							// TODO Auto-generated method stub
							onToast("transacetion success bank");
							Log.e("transaction", "success bank");
							game.processLib.onTransactionSuccess(
									trans.getMessage(), trans.getAmount(),
									trans.getCurrency(), trans.getErrorCode());
						}

						@Override
						public void onTransactionError(int errorCode,
								String message) {
							// TODO Auto-generated method stub
							onToast("transacetion error bank");
							Log.e("transaction", "error bank ");
							game.processLib.onTransactionError(errorCode,
									message);
						}
					});
		} else if (requestCode == Constants.INAPP_SMS_REQUEST_CODE
				&& resultCode == Constants.RESULT_INAPP_SMS) {
			String transactionId = data
					.getStringExtra(Constants.TRANSACTION_ID_KEY);
			System.err.println(transactionId);
			mIAP.checkTransactionStatus(transactionId,
					new TransactionStatusCallback() {

						public void onTransactionSuccess(TransactionResult trans) {
							// TODO Auto-generated method stub
							Log.e("transaction", "success sms");
							game.processLib.onTransactionSuccess(
									trans.getMessage(), trans.getAmount(),
									trans.getCurrency(), trans.getErrorCode());
						}

						@Override
						public void onTransactionError(int errorCode,
								String message) {
							// TODO Auto-generated method stub
							game.processLib.onTransactionError(errorCode,
									message);
						}
					});
		} else if (requestCode == Constants.IN_APP_PAYPAL_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			String transactionId = data
					.getStringExtra(Constants.TRANSACTION_ID_KEY);
			mIAP.checkTransactionStatus(transactionId,
					new TransactionStatusCallback() {

						public void onTransactionSuccess(TransactionResult trans) {
							// TODO Auto-generated method stub
							onToast("transacetion success paypal");
							Log.e("transaction", "success paypal");
							game.processLib.onTransactionSuccess(
									trans.getMessage(), trans.getAmount(),
									trans.getCurrency(), trans.getErrorCode());
						}

						@Override
						public void onTransactionError(int errorCode,
								String message) {
							// TODO Auto-generated method stub
							onToast("transacetion error paypal");
							Log.e("transaction", "error paypal ");
							game.processLib.onTransactionError(errorCode,
									message);
						}
					});
		} else if (requestCode == Constants.INAPP_CARD_REQUEST_CODE
				&& resultCode == Constants.RESULT_INAPP_CARD) {
			CardPayment card = (CardPayment) data.getExtras().getSerializable(
					Constants.CARD_PAYMENT_DATA);

			mIAP.checkTransactionStatus(card.getTransactionId(),
					new TransactionStatusCallback() {

						public void onTransactionSuccess(TransactionResult trans) {
							// TODO Auto-generated method stub
							onToast("transacetion success card");
							Log.e("transaction", "success card");
							game.processLib.onTransactionSuccess(
									trans.getMessage(), trans.getAmount(),
									trans.getCurrency(), trans.getErrorCode());
						}

						@Override
						public void onTransactionError(int errorCode,
								String message) {
							// TODO Auto-generated method stub
							Log.e("transaction", "error card ");
							onToast("transacetion error card");
							game.processLib.onTransactionError(errorCode,
									message);
						}
					});
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				gameView.setFocusable(true);
				gameView.requestFocus();
				gameView.onKeyDown(keyCode, event);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private void updateView() {
		if (isResumed) {
			Session session = Session.getActiveSession();
			if (session.isOpened()
					&& !((MainApplication) getApplication()).isLoggedIn()) {
				fetchUserInformationAndLogin();
			}
		}
	}

	// Fetch user information and login (i.e switch to the personalized
	// HomeFragment)
	private void fetchUserInformationAndLogin() {
		final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {

			// Get current logged in user information
			Request meRequest = Request.newMeRequest(session,
					new Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							FacebookRequestError error = response.getError();
							if (error != null) {
								Log.e(MainApplication.TAG, error.toString());
								handleError(error, true);
							} else if (session == Session.getActiveSession()) {
								// Set the currentFBUser attribute
								((MainApplication) getApplication())
										.setCurrentFBUser(user);
							}
						}
					});

			// Create a RequestBatch and add a callback once the batch of
			// requests completes
			RequestBatch requestBatch = new RequestBatch(meRequest);
			requestBatch.addCallback(new RequestBatch.Callback() {

				@Override
				public void onBatchCompleted(RequestBatch batch) {
					if (((MainApplication) getApplication()).getCurrentFBUser() != null) {
						// Login by switching to the personalized HomeFragment
						loadPersonalizedFragment();
					} else {
						showError(getString(R.string.error_fetching_profile),
								true);
					}
				}
			});

			// Execute the batch of requests asynchronously
			requestBatch.executeAsync();
		}
	}

	// Switches to the personalized HomeFragment as the user has just logged in
	private void loadPersonalizedFragment() {
		if (isResumed) {

		} else {
			showError(getString(R.string.error_switching_screens), true);
		}
	}

	void handleError(FacebookRequestError error, boolean logout) {
		DialogInterface.OnClickListener listener = null;
		String dialogBody = null;

		if (error == null) {
			dialogBody = getString(R.string.error_dialog_default_text);
		} else {
			switch (error.getCategory()) {
			case AUTHENTICATION_RETRY:
				// tell the user what happened by getting the message id, and
				// retry the operation later
				String userAction = (error.shouldNotifyUser()) ? ""
						: getString(error.getUserActionMessageId());
				dialogBody = getString(R.string.error_authentication_retry,
						userAction);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(Intent.ACTION_VIEW,
								M_FACEBOOK_URL);
						startActivity(intent);
					}
				};
				break;

			case AUTHENTICATION_REOPEN_SESSION:
				// close the session and reopen it.
				dialogBody = getString(R.string.error_authentication_reopen);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Session session = Session.getActiveSession();
						if (session != null && !session.isClosed()) {
							session.closeAndClearTokenInformation();
						}
					}
				};
				break;

			case PERMISSION:
				// request the publish permission
				dialogBody = getString(R.string.error_permission);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						requestPublishPermissions(Session.getActiveSession());
					}
				};
				break;

			case SERVER:
			case THROTTLING:
				// this is usually temporary, don't clear the fields, and
				// ask the user to try again
				dialogBody = getString(R.string.error_server);
				break;

			case BAD_REQUEST:
				// this is likely a coding error, ask the user to file a bug
				dialogBody = getString(R.string.error_bad_request,
						error.getErrorMessage());
				break;

			case CLIENT:
				// this is likely an IO error, so tell the user they have a
				// network issue
				dialogBody = getString(R.string.network_error);
				break;

			case OTHER:
			default:
				// an unknown issue occurred, this could be a code error, or
				// a server side issue, log the issue, and either ask the
				// user to retry, or file a bug
				dialogBody = getString(R.string.error_unknown,
						error.getErrorMessage());
				break;
			}
		}

		new AlertDialog.Builder(this)
				.setPositiveButton(R.string.error_dialog_button_text, listener)
				.setTitle(R.string.error_dialog_title).setMessage(dialogBody)
				.show();

		if (logout) {
			logout();
		}
	}

	private boolean ensureOpenSession() {
		if (Session.getActiveSession() == null
				|| !Session.getActiveSession().isOpened()) {

			Session.openActiveSession(this, true, new Session.StatusCallback() {
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {

					onSessionStateChanged(session, state, exception);
				}
			});
			return false;
		}
		// Log.e("ensureOpenSession", "ensureOpenSession" + "true");
		return true;
	}

	private void onSessionStateChanged(Session session, SessionState state,
			Exception exception) {
		if (isSessionOpened && state != null && state.isOpened()) {
			isSessionOpened = false;
			if (dialogParams != null) {
				showDialogWithoutNotificationBar("feed", dialogParams);
			}
		}
	}

	boolean isSessionOpened = false;

	@Override
	public void onFeedRequest(String link, String name, String caption,
			String descript, String pictureUrl) {
		final Bundle params = new Bundle();
		params.putString("name", name);
		params.putString("message", "thu nghiem");
		params.putString("caption", caption);
		params.putString("description", descript);
		params.putString("link", link);
		params.putString("picture", pictureUrl);

		dialogParams = params;
		if (ensureOpenSession()) {
			showDialogWithoutNotificationBar("feed", dialogParams);
		} else {
			isSessionOpened = true;
		}
	}

	private void showDialogWithoutNotificationBar(final String action,
			final Bundle params) {
		// Create the dialog

		if (params == null) {
			Log.e("params", "fsdfsofnsodnfosdnfds");
			return;
		}
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dialog = new WebDialog.Builder(MainActivity.this, Session
						.getActiveSession(), action, params)
						.setOnCompleteListener(
								new WebDialog.OnCompleteListener() {

									@Override
									public void onComplete(Bundle values,
											FacebookException error) {
										if (error != null
												&& !(error instanceof FacebookOperationCanceledException)) {
											showError(
													getResources()
															.getString(
																	R.string.network_error),
													false);
										}
										dialog = null;
										dialogAction = null;
										dialogParams = null;
									}
								}).build();

				// Store the dialog information in attributes
				dialogAction = action;

				// Show the dialog
				dialog.show();
			}
		});

	}

	void requestPublishPermissions(Session session) {
		if (session != null) {
			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
					this, PERMISSIONS);
			session.requestNewPublishPermissions(newPermissionsRequest);
		}
	}

	// Show user error message as a toast
	void showError(String error, boolean logout) {
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
		if (logout) {
			logout();
		}
	}

	private void logout() {
		Session.getActiveSession().closeAndClearTokenInformation();
	}

	// **** Preferrences **** //

	public static SharedPreferences getPreference() {
		return mPreference;
	}

	public static void saveString(final String key, final String value) {
		SharedPreferences.Editor editor = mPreference.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void saveInt(final String key, final int value) {
		SharedPreferences.Editor editor = mPreference.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void saveBoolean(final String key, final boolean value) {
		SharedPreferences.Editor editor = mPreference.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static String getString(final String key, final String defValue) {
		return mPreference.getString(key, defValue);
	}

	public static boolean getBoolean(final String key, final boolean defValue) {
		return mPreference.getBoolean(key, defValue);
	}

	public static int getInt(final String key, final int defValue) {
		return mPreference.getInt(key, defValue);
	}

	@Override
	public void callSmsPurchase() {
		// TODO Auto-generated method stub
		// mIAP.inAppSMS(15000);
		handler.post(new Runnable() {

			@Override
			public void run() {
				mSmsCreate.prepareSms(false);
				mSmsCreate.sendSMS(SMS_NUMBER, SMS_MESSAGE);
			}
		});
	}

	@Override
	public void callBankPurchase() {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				onToast("call bank");
				mIAP.inAppBank(null, null, null);
			}
		});
	}

	@Override
	public void callPaypalPurchase() {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				onToast("call paypal");
				mIAP.inAppPaypal(null, null, null);
			}
		});
	}

	public void onToast(final CharSequence notify) {
		Toast.makeText(MainActivity.this, notify, Toast.LENGTH_LONG);
	}

	@Override
	public void callCardPurchase() {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				onToast("call card");
				mIAP.inAppCard(null, null, null);
			}
		});
	}

	@Override
	public void callSmsAuto() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				mSmsCreate.prepareSms(true);
				mSmsCreate.sendSMS(SMS_NUMBER, SMS_MESSAGE);
			}
		});
	}

	@Override
	public void onSendSMSSuccess() {
		// TODO Auto-generated method stub
		onToast("sendsms success");
		game.processLib.onSendSMSSuccess();
	}

	@Override
	public void onSendSMSFailure() {
		// TODO Auto-generated method stub
		game.processLib.onSendSMSFailure();
	}

	@Override
	public void onSmsAutoSuccess() {
		// TODO Auto-generated method stub
		game.processLib.onSmsAutoSuccess();
	}

	@Override
	public void onSmsAutoError() {
		// TODO Auto-generated method stub
		game.processLib.onSmsAutoError();
	}
}