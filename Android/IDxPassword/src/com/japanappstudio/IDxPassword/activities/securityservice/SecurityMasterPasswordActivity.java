package com.japanappstudio.IDxPassword.activities.securityservice;

import java.io.File;

import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.idletime.ControlApplication;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;
import com.japanappstudio.IDxPassword.idxpwdatabase.UserDB;

import net.sqlcipher.database.SQLiteDatabase;

import com.japanappstudio.IDxPassword.activities.IParent;
import com.japanappstudio.IDxPassword.activities.MyRelativeLayout;
import com.japanappstudio.IDxPassword.activities.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecurityMasterPasswordActivity extends Activity implements
		OnClickListener,IParent {

	// =========================Control Define =====================
	private Button mBtnDone;
	private EditText mEditTextMasterPW;
	// ========================Class Define =======================
	private IdManagerPreference mIdManagerPreference;
	private IDxPWDataBaseHandler mDataBaseHandler;

	// ==========================Variable Define ===================
	private int mRemoveDataTimes;
	private int mNumberAtemppt = 0;
	private String mMasterPW="";
	private boolean isKeyBoardNumber = false;
	public Button btn_switch_keyboard;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init idmanager preference
		mIdManagerPreference = IdManagerPreference.getInstance(this);

		setContentView(R.layout.master_password_2);
		getWindow().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.setup_master_password));
		/* init service */
		// button
		mBtnDone = (Button) findViewById(R.id.btn_confirm_master_pw);
		mBtnDone.setOnClickListener(this);
		mEditTextMasterPW = (EditText) findViewById(R.id.editText_master_pw);
		mEditTextMasterPW.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (mMasterPW.equals(mEditTextMasterPW.getText().toString())) {
					getApp().setIdle(0);
					finish();
				}
			}
		});
//		mEditTextMasterPW.setTransformationMethod(PasswordTransformationMethod.getInstance());
		btn_switch_keyboard = (Button) findViewById(R.id.btn_switch_keyboard);
//		InputMethodManager imeManager = (InputMethodManager) getApplicationContext()
//				.getSystemService(INPUT_METHOD_SERVICE);
//		imeManager.showSoftInput(mEditTextMasterPW,
//				InputMethodManager.SHOW_IMPLICIT);
		MyRelativeLayout activityRootView = (MyRelativeLayout) findViewById(R.id.root_view);
		activityRootView.setIParent(this);
		
		/* init database */
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new IDxPWDataBaseHandler(this);
		UserDB user = mDataBaseHandler
				.getUser(Contants.MASTER_PASSWORD_ID);
		mMasterPW = user.getPassword();

		
	}

	public void confirmMaster(View v) {

	}

	private void showToast(String string) {
		Toast.makeText(SecurityMasterPasswordActivity.this, string,
				Toast.LENGTH_SHORT).show();
	}

	public void onReturn(View v) {
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, SecurityMasterPasswordActivity.class);
		activity.startActivity(i);
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnDone) {
			if ("".equals(mEditTextMasterPW.getText().toString())) {
				showToast("Please enter your master password");
			} else {
				/* check remove data values */
				checkRemoveDataValues();
			}
			/* reset edit text master pw */
			mEditTextMasterPW.setText("");
		}
	}

	@SuppressWarnings("deprecation")
	private void checkRemoveDataValues() {
		// TODO Auto-generated method stub
		if (!mMasterPW.equals(mEditTextMasterPW.getText().toString())) {
			if (mRemoveDataTimes == Contants.KEY_OFF) {
				showDialog(Contants.DIALOG_WRONG_PASS_NO_SECURE);
			} else {
				mNumberAtemppt++;
				if (mNumberAtemppt >= mRemoveDataTimes) {
					showDialog(Contants.DIALOG_REMOVED_DATA);
					mNumberAtemppt = 0;

				} else {
					showDialog(Contants.DIALOG_LOGIN_WRONG_PASS);
				}
			}
		} else {
			getApp().setIdle(0);
			finish();
		}
	}

	@SuppressLint("InlinedApi")
	public void onClickChangerKeyBoard(View v) {
		if (isKeyBoardNumber) {
			mEditTextMasterPW
					.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			isKeyBoardNumber = false;
		} else {
			mEditTextMasterPW
					.setInputType(InputType.TYPE_CLASS_NUMBER);
			isKeyBoardNumber = true;
		}
	}

	/**
	 * remove data after type master password times limit to remove data values
	 */
	private void removeData() {
		/* remove database */
		File db = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		if (db.exists())
			db.delete();

		/* reset share preference */
		mIdManagerPreference.setApplicationFirstTimeInstalled(true);
		mIdManagerPreference.setMasterPW("");
		mIdManagerPreference.setSecurityMode(Contants.KEY_OFF);
		mIdManagerPreference.setValuesremoveData(Contants.KEY_OFF);
		System.exit(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mRemoveDataTimes = mIdManagerPreference.getValuesRemoveData();
		mMasterPW = mDataBaseHandler.getUser(Contants.MASTER_PASSWORD_ID).getPassword();
		mNumberAtemppt = 0;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Log.e("lam cai log", "lam cai log");
			return false;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case Contants.DIALOG_LOGIN_WRONG_PASS:
			return createDialog(Contants.DIALOG_LOGIN_WRONG_PASS);
		case Contants.DIALOG_WRONG_PASS_NO_SECURE:
			return createDialog(Contants.DIALOG_WRONG_PASS_NO_SECURE);
		case Contants.DIALOG_REMOVED_DATA:
			return createDialog(Contants.DIALOG_REMOVED_DATA);
		default:
			return null;
		}
	}

	/**
	 * Create and return an example alert dialog with an edit text box.
	 */
	private Dialog createDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case Contants.DIALOG_LOGIN_WRONG_PASS:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources().getString(
					R.string.limit_login_msg, 2));
			// builder.setMessage("Type the name of new folder:");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}

					});
			return builder.create();
		case Contants.DIALOG_WRONG_PASS_NO_SECURE:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources()
					.getString(R.string.wrong_pw_login));
			// builder.setMessage("Type the name of new folder:");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}

					});
			return builder.create();
		case Contants.DIALOG_REMOVED_DATA:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources().getString(
					R.string.data_erased_msg));
			// builder.setMessage("Type the name of new folder:");
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* remove data */
							removeData();
							return;
						}

					});
			return builder.create();
		default:
			return null;
		}
	}
	public ControlApplication getApp() {
		return (ControlApplication) this.getApplication();
	}
	public void onChangeKeyBoard(View v) {
		if (mEditTextMasterPW.getInputType() == InputType.TYPE_CLASS_NUMBER) {
			mEditTextMasterPW.setInputType(InputType.TYPE_CLASS_TEXT);
		} else {
			mEditTextMasterPW.setInputType(InputType.TYPE_CLASS_NUMBER);
		}

		InputMethodManager imeManager = (InputMethodManager) getApplicationContext()
				.getSystemService(INPUT_METHOD_SERVICE);
		imeManager.showSoftInput(mEditTextMasterPW,
				InputMethodManager.SHOW_IMPLICIT);
		mEditTextMasterPW.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}
	@Override
	public void show_keyboard() {
		// TODO Auto-generated method stub
		btn_switch_keyboard.setVisibility(View.VISIBLE);
	}

	@Override
	public void hide_keyboard() {
		// TODO Auto-generated method stub
		btn_switch_keyboard.setVisibility(View.GONE);
	}
}
