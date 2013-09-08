package com.example.ailatrieuphu_visva.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ailatrieuphu_visva.R;
import com.example.ailatrieuphu_visva.db.DBConnector;
import com.example.ailatrieuphu_visva.facebook.AsyncFacebookRunner;
import com.example.ailatrieuphu_visva.facebook.BaseRequestListener;
import com.example.ailatrieuphu_visva.facebook.DialogError;
import com.example.ailatrieuphu_visva.facebook.Facebook;
import com.example.ailatrieuphu_visva.facebook.Facebook.DialogListener;
import com.example.ailatrieuphu_visva.facebook.FacebookError;
import com.example.ailatrieuphu_visva.facebook.SessionStore;
import com.example.ailatrieuphu_visva.utils.Helpers;

public class MenuActivity_2 extends Activity {
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

	private Facebook _mFacebook;
	private ProgressDialog mProgress;
	private Handler mRunOnUi;
	private EditText et_description;
	private static final String APP_ID = "361292540665890";
	private Dialog dialog;

	private Handler _handler = new Handler();
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
		_mFacebook = new Facebook(APP_ID);
		mRunOnUi = new Handler();
		SessionStore.restore(_mFacebook, this);
		if (_mFacebook.isSessionValid()) {
			// String name = SessionStore.getName(this);
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
	protected void onPause() {
		// TODO Auto-generated method stub
		Helpers.releaseSound(_sound);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		_is_begin = false;
		_sound = Helpers.playSound(this, R.raw._altp_sound_background, true);
		super.onResume();
	}

	protected void showCustomDialog() {
		dialog = new Dialog(this);
		dialog.setCancelable(true);
		dialog.setContentView(R.layout._altp_layout_dialog_share_facebook);
		dialog.setTitle(R.string.share_fb);
		et_description = (EditText) dialog.findViewById(R.id.et_description);
		Button btnOK = (Button) dialog.findViewById(R.id.btnok);
		Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doPost();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void doPost() {
		InputStream is = null;
		byte[] data = null;
		String message = et_description.getText().toString();
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("Posting ...");
		mProgress.show();
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(_mFacebook);
		// try {
		Bundle params = new Bundle();
		// is = new FileInputStream(path);
		// data = readBytes(is);
		params.putString("message", message);
		params.putString("filename", "test1.mp4");
		params.putByteArray("video", data);
		mAsyncFbRunner.request("me/videos", params, "POST", new WallPostListener());
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public byte[] readBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}

	private final class WallPostListener extends BaseRequestListener {

		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {
				@Override
				public void run() {
					mProgress.cancel();
					dialog.dismiss();
				}
			});
		}
	}

	private void onClickShareFaceBook() {
		if (_mFacebook.isSessionValid()) {
			showCustomDialog();
		} else {
			_mFacebook.authorize(this, new String[] { "user_photos,publish_checkins,publish_actions,publish_stream" }, -1, new FbLoginDialogListener());
		}
	}

	private final class FbLoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionStore.save(_mFacebook, MenuActivity_2.this);
			// mFacebookBtn.setChecked(true);
			getFbName();
		}

		public void onFacebookError(FacebookError error) {
			// mFacebookBtn.setChecked(false);
		}

		public void onError(DialogError error) {
			// mFacebookBtn.setChecked(false);
		}

		public void onCancel() {
			// mFacebookBtn.setChecked(false);
		}
	}

	private void getFbName() {
		mProgress.setMessage("Loading...");
		mProgress.show();
		mProgress.setCanceledOnTouchOutside(false);

		new Thread() {
			@Override
			public void run() {
				String name = "";
				int what = 1;
				try {
					String me = _mFacebook.request("me");
					JSONObject jsonObj = (JSONObject) new JSONTokener(me).nextValue();
					name = jsonObj.getString("name");
					what = 0;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler mFbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			if (msg.what == 0) {
				String username = (String) msg.obj;
				username = (username.equals("")) ? "No Name" : username;
				SessionStore.saveName(username, MenuActivity_2.this);
				Toast.makeText(MenuActivity_2.this, "Bạn đã đăng nhập với tài khoản " + username, Toast.LENGTH_LONG).show();
			} else {

			}
		}
	};

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
						MainGameActivity.startActivity(MenuActivity_2.this);
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
						MainGameActivity.startActivity(MenuActivity_2.this);
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

}
