package com.example.ailatrieuphu_visva.activity;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ailatrieuphu_visva.R;
import com.example.ailatrieuphu_visva.db.DBConnector;
import com.example.ailatrieuphu_visva.i.FacebookListener;
import com.example.ailatrieuphu_visva.utils.Helpers;
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

public class MenuActivity extends Activity implements FacebookListener {
	private static final String LOGGED_IN_KEY = "logged_in";
	private static final String CURRENT_FB_USER_KEY = "current_fb_user";
	private MediaPlayer _sound;
	private Dialog _dialog_begin;
	private Dialog _dialog_rule;
	private Dialog _dialog_high_score;
	private Dialog _dialog_options;
	private Button _btn_menu_begin;
	private Button _btn_menu_high_score;
	private Button _btn_menu_options;
	private Button _btn_menu_exit;
	private Button _btn_facebook;
	private AnimationDrawable _anim_begin;
	private Boolean _is_begin;
	private DBConnector _db_connector;
	private Bundle _dialogParams = null;
	private WebDialog _dialog = null;
	private String _dialogAction = null;
	private Handler _handler = new Handler();
	private boolean isSessionOpened = false;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final Uri M_FACEBOOK_URL = Uri.parse("http://m.facebook.com");
	private UiLifecycleHelper mUiLifecycleHelper;
	private boolean _is_resume = false;
	private boolean _is_logged = false;
	private GraphUser _currentFBUser;
	// private FacebookRequestError _appFBRequestError = null;
	private OnClickListener _listener_onclick_begin = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickBegin();
		}
	};
	private OnClickListener _listener_onclick_high_score = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickHighScore();
		}
	};
	private OnClickListener _listener_onclick_options = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickOptions();
		}
	};
	private OnClickListener _listener_onclick_exit = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickExit();
		}
	};
	private OnClickListener _listener_onclick_oki_begin = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickOkiBegin();
		}
	};
	private OnClickListener _listener_onclick_dont_known_begin = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickDontKnownBegin();
		}
	};
	private OnClickListener _listener_onclick_oki_rule = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickOkiRuleBegin();
		}
	};
	private OnClickListener _listener_onclick_oki_high_score = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickOkiHighScore();
		}
	};
	private OnClickListener _listener_onclick_share_facebook = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onClickShareFaceBook();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout._altp_layout_menu);
		_btn_menu_begin = (Button) findViewById(R.id._layout_menu_btn_begin);
		_btn_menu_begin.setOnClickListener(_listener_onclick_begin);
		_btn_menu_high_score = (Button) findViewById(R.id._layout_menu_btn_high_score);
		_btn_menu_high_score.setOnClickListener(_listener_onclick_high_score);
		_btn_menu_options = (Button) findViewById(R.id._layout_menu_btn__options);
		_btn_menu_options.setOnClickListener(_listener_onclick_options);
		_btn_menu_exit = (Button) findViewById(R.id._layout_menu_btn_exit);
		_btn_menu_exit.setOnClickListener(_listener_onclick_exit);
		_btn_facebook = (Button) findViewById(R.id._layout_menu_btn_facebook);
		_btn_facebook.setOnClickListener(_listener_onclick_share_facebook);
		_anim_begin = (AnimationDrawable) _btn_menu_begin.getBackground().getCurrent();
		_db_connector = new DBConnector(this);
		mUiLifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				updateView();
				onSessionStateChanged(session, state, exception);
			}
		});
		mUiLifecycleHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			_is_logged = savedInstanceState.getBoolean(LOGGED_IN_KEY, false);
			if (_is_logged && _currentFBUser == null) {
				try {
					// currentFBUser
					String currentFBUserJSONString = savedInstanceState.getString(CURRENT_FB_USER_KEY);
					if (currentFBUserJSONString != null) {
						GraphUser currentFBUser = GraphObject.Factory.create(new JSONObject(currentFBUserJSONString), GraphUser.class);
						_currentFBUser = currentFBUser;
					}
				} catch (JSONException e) {
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mUiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Call onSaveInstanceState on fbUiLifecycleHelper
		mUiLifecycleHelper.onSaveInstanceState(outState);
		// Save the logged-in state
		outState.putBoolean(LOGGED_IN_KEY, _is_logged);
		// Save the currentFBUser
		if (_currentFBUser != null) {
			outState.putString(CURRENT_FB_USER_KEY, _currentFBUser.getInnerJSONObject().toString());
		}

	}

	private void updateView() {
		if (_is_resume) {
			Session session = Session.getActiveSession();
			if (session.isOpened()) {
				fetchUserInformationAndLogin();
			}
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (!_is_begin) {
			_anim_begin.stop();
			_anim_begin.selectDrawable(0);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (_dialog != null) {
			showDialogWithoutNotificationBar(_dialogAction, _dialogParams);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (_dialog != null) {
			_dialog.dismiss();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Helpers.releaseSound(_sound);
		_is_resume = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mUiLifecycleHelper.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		_is_begin = false;
		_sound = Helpers.playSound(this, R.raw._altp_sound_background, true);
		_is_resume = true;
		mUiLifecycleHelper.onResume();
		com.facebook.Settings.publishInstallAsync(this, getResources().getString(R.string.app_id));
		super.onResume();
	}

	private void onClickShareFaceBook() {
		final String link = FacebookListener.LINK;
		final String name = "Street Knight"; // ten cua feed
		final String caption = "Level";
		final int score = 1000;
		final String descrip = "" + score;
		final String picture = FacebookListener.PICTURE_URL[0];
		onFeedRequest(link, name, caption, descrip, picture);
	}

	private void onClickBegin() {
		creat_dialog_begin(this, _listener_onclick_oki_begin, _listener_onclick_dont_known_begin);
		Helpers.releaseSound(_sound);
		_sound = Helpers.playSound(this, R.raw._altp_sound_ready, false);
	}

	private void onClickHighScore() {
		creat_dialog_highscore(this);
	}

	private void onClickOptions() {
		creat_dialog_options(this);
	}

	private void onClickExit() {
		creat_dialog_exit(this);
	}

	private void onClickOkiBegin() {
		_is_begin = true;
		Helpers.releaseSound(_sound);
		_sound = Helpers.playSound(this, R.raw._altp_sound_begin_game, false);
		_anim_begin.start();
		new Thread() {
			public void run() {
				Helpers.wait_sound(_sound);
				_db_connector.checkAndCopyDatabase();
				_handler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						MainGameActivity.startActivity(MenuActivity.this);
						_anim_begin.stop();
						_anim_begin.selectDrawable(0);
					}
				});
			}
		}.start();

		_dialog_begin.dismiss();
	}

	private void onClickOkiRuleBegin() {
		_is_begin = true;
		Helpers.releaseSound(_sound);
		_sound = Helpers.playSound(this, R.raw._altp_sound_begin_game, false);
		_anim_begin.start();
		new Thread() {
			public void run() {
				Helpers.wait_sound(_sound);
				_db_connector.checkAndCopyDatabase();
				_handler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						MainGameActivity.startActivity(MenuActivity.this);
						_anim_begin.stop();
						_anim_begin.selectDrawable(0);
					}
				});
			}
		}.start();
		_dialog_rule.dismiss();
	}

	private void onClickDontKnownBegin() {
		creat_dialog_rule(this, _listener_onclick_oki_rule);
		Helpers.releaseSound(_sound);
		_sound = Helpers.playSound(this, R.raw._altp_sound_game_rules, false);
		_dialog_begin.dismiss();
	}

	private void onClickOkiHighScore() {
		_dialog_high_score.dismiss();
	}

	public void creat_dialog_begin(Context mContext, OnClickListener listener_oki, OnClickListener listener_dont_known) {
		_dialog_begin = new Dialog(mContext);
		_dialog_begin.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		_dialog_begin.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) ((Activity) mContext).getLayoutInflater();
		View dialog_view = inflater.inflate(R.layout._altp_layout_dialog_begin, null);
		Button btn_oki = (Button) dialog_view.findViewById(R.id._layout_dialog_begin_btn_ok);
		btn_oki.setOnClickListener(listener_oki);
		Button btn_dont_known = (Button) dialog_view.findViewById(R.id._layout_dialog_begin_btn_dont_known);
		btn_dont_known.setOnClickListener(listener_dont_known);
		_dialog_begin.setContentView(dialog_view);
		_dialog_begin.show();
	}

	public void creat_dialog_rule(Context mContext, OnClickListener listener_oki) {
		_dialog_rule = new Dialog(mContext);
		_dialog_rule.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		_dialog_rule.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) ((Activity) mContext).getLayoutInflater();
		View dialog_view = inflater.inflate(R.layout._altp_layout_dialog_rule, null);
		Button btn_oki = (Button) dialog_view.findViewById(R.id._layout_dialog_rule_btn_ok);
		btn_oki.setOnClickListener(listener_oki);
		_dialog_rule.setContentView(dialog_view);
		_dialog_rule.show();
	}

	public void creat_dialog_highscore(Context mContext) {
		_dialog_high_score = new Dialog(mContext);
		_dialog_high_score.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		_dialog_high_score.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) ((Activity) mContext).getLayoutInflater();
		View dialog_view = inflater.inflate(R.layout._altp_layout_dialog_high_score, null);
		Button btn_oki = (Button) dialog_view.findViewById(R.id._layout_dialog_high_score_btn_ok);
		btn_oki.setOnClickListener(_listener_onclick_oki_high_score);
		TextView _txt_name[] = new TextView[3];
		_txt_name[0] = (TextView) dialog_view.findViewById(R.id._layout_dialog_high_score_name_1);
		_txt_name[1] = (TextView) dialog_view.findViewById(R.id._layout_dialog_high_score_name_2);
		_txt_name[2] = (TextView) dialog_view.findViewById(R.id._layout_dialog_high_score_name_3);

		TextView _txt_score[] = new TextView[3];
		_txt_score[0] = (TextView) dialog_view.findViewById(R.id._layout_dialog_high_score_score_1);
		_txt_score[1] = (TextView) dialog_view.findViewById(R.id._layout_dialog_high_score_score_2);
		_txt_score[2] = (TextView) dialog_view.findViewById(R.id._layout_dialog_high_score_score_3);

		for (int i = 0; i < 3; i++) {
		}
		_dialog_high_score.setContentView(dialog_view);
		_dialog_high_score.show();
	}

	public void creat_dialog_options(Context mContext) {
		_dialog_options = new Dialog(mContext);
		_dialog_options.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		_dialog_options.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) ((Activity) mContext).getLayoutInflater();
		View dialog_view = inflater.inflate(R.layout._altp_layout_dialog_options, null);
		_dialog_options.setContentView(dialog_view);
		Button btn_oki = (Button) dialog_view.findViewById(R.id._layout_dialog_options_btn_ok);
		btn_oki.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_dialog_options.dismiss();
			}
		});
		_dialog_options.setCancelable(false);
		_dialog_options.show();
	}

	public void creat_dialog_exit(Context mContext) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(getResources().getString(R.string._txt_msg_dialog_exit));
		builder.setPositiveButton(getResources().getString(R.string._txt_btn_ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		builder.setNegativeButton(getResources().getString(R.string._txt_btn_cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.create().show();
	}

	@Override
	public void onFeedRequest(String link, String name, String captain, String descript, String pictureUrl) {
		// TODO Auto-generated method stub
		final Bundle params = new Bundle();
		params.putString("name", name);
		params.putString("message", "thu nghiem");
		params.putString("caption", captain);
		params.putString("description", descript);
		params.putString("link", link);
		params.putString("picture", pictureUrl);
		_dialogParams = params;
		if (ensureOpenSession()) {
			showDialogWithoutNotificationBar("feed", _dialogParams);
		} else {
			isSessionOpened = true;
		}
	}

	private boolean ensureOpenSession() {
		if (Session.getActiveSession() == null || !Session.getActiveSession().isOpened()) {

			Session.openActiveSession(this, true, new Session.StatusCallback() {

				@Override
				public void call(Session session, SessionState state, Exception exception) {
					// TODO Auto-generated method stub
					onSessionStateChanged(session, state, exception);
				}

			});
			return false;
		}
		return true;
	}

	private void onSessionStateChanged(Session session, SessionState state, Exception exception) {
		if (isSessionOpened && state != null && state.isOpened()) {
			isSessionOpened = false;
			if (_dialogParams != null) {
				showDialogWithoutNotificationBar("feed", _dialogParams);
			}
		}
	}

	private void showDialogWithoutNotificationBar(final String action, final Bundle params) {
		if (params == null) {
			return;
		}
		_handler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_dialog = new WebDialog.Builder(MenuActivity.this, Session.getActiveSession(), action, params).setOnCompleteListener(new WebDialog.OnCompleteListener() {
					@Override
					public void onComplete(Bundle values, FacebookException error) {
						if (error != null && !(error instanceof FacebookOperationCanceledException)) {
							showError(getResources().getString(R.string.network_error), false);
						}
						_dialog = null;
						_dialogAction = null;
						_dialogParams = null;
					}
				}).build();
				_dialogAction = action;
				_dialog.show();
			}
		});
	}

	void requestPublishPermissions(Session session) {
		if (session != null) {
			Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
			session.requestNewPublishPermissions(newPermissionsRequest);
		}
	}

	private void fetchUserInformationAndLogin() {
		final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Request meRequest = Request.newMeRequest(session, new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {
					FacebookRequestError error = response.getError();
					if (error != null) {
						handleError(error, true);
					} else if (session == Session.getActiveSession()) {
						_currentFBUser = user;
					}
				}
			});

			RequestBatch requestBatch = new RequestBatch(meRequest);
			requestBatch.addCallback(new RequestBatch.Callback() {

				@Override
				public void onBatchCompleted(RequestBatch batch) {
					if (_currentFBUser != null) {
						if (_is_resume) {
						} else {
							showError(getString(R.string.error_switching_screens), true);
						}
					} else {
						showError(getString(R.string.error_fetching_profile), true);
					}
				}
			});
			requestBatch.executeAsync();
		}
	}

	private void handleError(FacebookRequestError error, boolean logout) {
		DialogInterface.OnClickListener listener = null;
		String dialogBody = null;
		if (error == null) {
			dialogBody = getString(R.string.error_dialog_default_text);
		} else {
			switch (error.getCategory()) {
			case AUTHENTICATION_RETRY:
				String userAction = (error.shouldNotifyUser()) ? "" : getString(error.getUserActionMessageId());
				dialogBody = getString(R.string.error_authentication_retry, userAction);
				listener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(Intent.ACTION_VIEW, M_FACEBOOK_URL);
						startActivity(intent);
					}
				};
				break;

			case AUTHENTICATION_REOPEN_SESSION:
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
				dialogBody = getString(R.string.error_server);
				break;

			case BAD_REQUEST:
				dialogBody = getString(R.string.error_bad_request, error.getErrorMessage());
				break;

			case CLIENT:
				dialogBody = getString(R.string.network_error);
				break;
			case OTHER:
			default:
				dialogBody = getString(R.string.error_unknown, error.getErrorMessage());
				break;
			}
		}
		new AlertDialog.Builder(this).setPositiveButton(R.string.error_dialog_button_text, listener).setTitle(R.string.error_dialog_title).setMessage(dialogBody).show();
		if (logout) {
			logout();
		}
	}

	private void logout() {
		Session.getActiveSession().closeAndClearTokenInformation();
	}

	void showError(String error, boolean logout) {
		Toast.makeText(this, error, Toast.LENGTH_LONG).show();
		if (logout) {
			logout();
		}
	}
}
