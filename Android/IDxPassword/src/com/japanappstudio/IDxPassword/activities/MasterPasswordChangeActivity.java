package com.japanappstudio.IDxPassword.activities;

import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.idxpwdatabase.GroupFolder;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;
import com.japanappstudio.IDxPassword.idxpwdatabase.UserDB;

import net.sqlcipher.database.SQLiteDatabase;
import com.japanappstudio.IDxPassword.activities.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MasterPasswordChangeActivity extends BaseActivity implements
		IParent {
	// =======================Control Define ====================

	private EditText editTextVerifyPW;
	private EditText editTextPW;
	private TextView txtChangePW;
	private TextView txtPW;
	private TextView txtVerifyVPW;
	// ==============================Class Define================
	// private DataBaseHandler mDataBaseHandler;
	private IDxPWDataBaseHandler mIDxPWDataBaseHandler;
	// =======================Variables Define ==================
	private boolean isChangePW;
	public Button btn_switch_keyboard;
	private String mMasterPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_master_password_2);
		isChangePW = getIntent().getExtras().getBoolean(
				Contants.IS_CHANGE_PASSWORD);
		getWindow().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.setup_master_password));

		/* init database */
		initDataBase();
		/* init control */
		initControl();
		MyRelativeLayout activityRootView = (MyRelativeLayout) findViewById(R.id.root_view);
		activityRootView.setIParent(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			onReturn(null);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * intialize database
	 */
	private void initDataBase() {
		/* init database */
		SQLiteDatabase.loadLibs(this);
		// mDataBaseHandler = new DataBaseHandler(this);
		// mDataBaseHandler.getUserCount();
		mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
	}

	private void initControl() {
		// text view
		txtChangePW = (TextView) findViewById(R.id.txt_change_master_pw);
		txtVerifyVPW = (TextView) findViewById(R.id.txt_new_pw);
		txtPW = (TextView) findViewById(R.id.txt_old_pw);

		// edit text
		editTextVerifyPW = (EditText) findViewById(R.id.edit_text_new_pw);
		editTextPW = (EditText) findViewById(R.id.edit_text_old_pw);

		/* check is change pw or create new pw */
		if (isChangePW) {
			txtChangePW.setText(getResources().getString(
					R.string.title_master_pass));
			txtVerifyVPW.setText(getResources()
					.getString(R.string.confirm_pass));
			txtPW.setText(getResources().getString(R.string.new_pass));
		} else {
			txtChangePW.setText(getResources().getString(
					R.string.title_master_pass));
			txtVerifyVPW.setText(getResources()
					.getString(R.string.confirm_pass));
			txtPW.setText(getResources().getString(R.string.new_pass));
		}
		editTextVerifyPW.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		editTextPW.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		btn_switch_keyboard = (Button) findViewById(R.id.btn_switch_keyboard);
		InputMethodManager imeManager = (InputMethodManager) getApplicationContext()
				.getSystemService(INPUT_METHOD_SERVICE);
		imeManager.showSoftInput(editTextPW,
				InputMethodManager.SHOW_IMPLICIT);
	}

	@SuppressWarnings("deprecation")
	public void confirmMaster(View v) {

		/* is create new master pw */
		if (!isChangePW) {
			if (!editTextVerifyPW.getText().toString().trim()
					.equals(editTextPW.getText().toString().trim()))
				showToast(getResources().getString(
						R.string.message_no_match_pass));
			else if ("".equals(editTextVerifyPW.getText().toString())
					|| "".equals(editTextPW.getText().toString()))
				showToast(getResources().getString(
						R.string.message_no_match_pass));
			else {
				/* set master pw */
				mMasterPassword = editTextPW.getText().toString();
				/* delete old user */
				// mDataBaseHandler.deleteFolder(Contants.MASTER_PASSWORD_ID);
				mIDxPWDataBaseHandler.deleteUser(Contants.MASTER_PASSWORD_ID);

				// mDataBaseHandler.addNewUser(user);

				UserDB userDB = new UserDB(Contants.MASTER_PASSWORD_ID,
						mMasterPassword, "");
				mIDxPWDataBaseHandler.addNewUser(userDB);

				// add general folder
				GroupFolder generalFolder = new GroupFolder(0,
						getString(R.string.list_general), 0,
						Contants.MASTER_PASSWORD_ID, 1);
				mIDxPWDataBaseHandler.addNewFolder(generalFolder);

				/* return Term of service */
				Intent intent = null;
				if ("".equals(userDB.getsEmail())) {
					intent = new Intent(MasterPasswordChangeActivity.this,
							RegisterEmailActivity.class);
					intent.putExtra(Contants.CREATE_NEW_EMAIL, true);
				} else
					intent = new Intent(MasterPasswordChangeActivity.this,
							MasterPasswordActivity.class);
				startActivity(intent);
				finish();
			}
		}

		/* change master pw */
		else {
			showDialog(Contants.DIALOG_MESSAGE_CHANGE_PW);
		}

	}

	public void onReturn(View v) {
		// if(isChangePW)
		// SettingActivity.startActivity(this, 2);
		finish();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, MasterPasswordChangeActivity.class);
		activity.startActivity(i);
	}

	private void showToast(String string) {
		Toast.makeText(MasterPasswordChangeActivity.this, string,
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case Contants.DIALOG_MESSAGE_CHANGE_PW:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setCancelable(false);
			builder.setMessage(getString(R.string.message_change_pass));
			builder.setIcon(R.drawable.icon);
			builder.setPositiveButton(getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* add new folder to database */
							if (!editTextVerifyPW
									.getText()
									.toString()
									.trim()
									.equals(editTextPW.getText().toString()
											.trim())) {
								showToast(getResources().getString(
										R.string.message_no_match_pass));
							} else if ("".equals(editTextVerifyPW.getText()
									.toString())
									|| "".equals(editTextPW.getText()
											.toString()))
								showToast(getResources().getString(
										R.string.message_no_match_pass));
							else {
								mMasterPassword = editTextVerifyPW.getText()
										.toString();
								/* update this password to db */
								// UserDataBase user = new
								// UserDataBase(Contants.MASTER_PASSWORD_ID,
								// mMasterPassword,
								// "test");
								// mDataBaseHandler.updateUser(user);

								UserDB userDB = mIDxPWDataBaseHandler
										.getUser(Contants.MASTER_PASSWORD_ID);
								userDB.setPassword(mMasterPassword);
								mIDxPWDataBaseHandler.updateUser(userDB);
								/* return setting activity */
								// Intent intent = new
								// Intent(MasterPasswordChangeActivity.this,
								// SettingActivity.class);
								// startActivity(intent);
								finish();
							}

							return;
						}
					});
			builder.setNegativeButton(getString(R.string.confirm_cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			return builder.create();
		default:
			return null;
		}
	}

	public void onChangeKeyBoard(View v) {
		EditText mEditTextMasterPW = editTextVerifyPW.isFocused() ? editTextVerifyPW
				: editTextPW;
		if (mEditTextMasterPW.getInputType() == InputType.TYPE_CLASS_NUMBER) {
			mEditTextMasterPW.setInputType(InputType.TYPE_CLASS_TEXT);
		} else {
			mEditTextMasterPW.setInputType(InputType.TYPE_CLASS_NUMBER);
		}

		InputMethodManager imeManager = (InputMethodManager) getApplicationContext()
				.getSystemService(INPUT_METHOD_SERVICE);
		imeManager.showSoftInput(mEditTextMasterPW,
				InputMethodManager.SHOW_IMPLICIT);
		mEditTextMasterPW.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
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
