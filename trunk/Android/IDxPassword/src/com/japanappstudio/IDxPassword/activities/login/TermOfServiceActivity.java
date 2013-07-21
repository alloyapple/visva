package com.japanappstudio.IDxPassword.activities.login;

import com.japanappstudio.IDxPassword.activities.MasterPasswordActivity;
import com.japanappstudio.IDxPassword.activities.MasterPasswordChangeActivity;
import com.japanappstudio.IDxPassword.activities.RegisterEmailActivity;
import com.japanappstudio.IDxPassword.activities.homescreen.HomeScreeenActivity;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;
import com.japanappstudio.IDxPassword.idxpwdatabase.UserDB;

import net.sqlcipher.database.SQLiteDatabase;
import com.japanappstudio.IDxPassword.activities.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TermOfServiceActivity extends Activity implements OnClickListener {
	// =============================Control Define ====================
	private Button btnCancel;
	private Button btnContinue;
	// =============================Class Define ======================
	private IdManagerPreference idManagerPreference;
	// private DataBaseHandler mDataBaseHandler;
	private IDxPWDataBaseHandler mIDxPWDataBaseHandler;

	// =============================Variables Define =================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		/* init database */
		SQLiteDatabase.loadLibs(this);
		// mDataBaseHandler = new DataBaseHandler(this);
		mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
		int numberUser = mIDxPWDataBaseHandler.getUserCount();

		// int userCount = mDataBaseHandler.getUserCount();
		if (numberUser < 1) {
			setContentView(R.layout.page_term_of_use);
			/* check is first installed app */
			idManagerPreference = IdManagerPreference.getInstance(this);
			/* init control */
			initControl();
		} else {
			UserDB user = mIDxPWDataBaseHandler.getUser(Contants.MASTER_PASSWORD_ID);
			Intent intentPW = null;
			if ("".equals(user.getsEmail())){
				intentPW = new Intent(TermOfServiceActivity.this, RegisterEmailActivity.class);
				intentPW.putExtra(Contants.CREATE_NEW_EMAIL, true);
			}
			else
				intentPW = new Intent(TermOfServiceActivity.this, MasterPasswordActivity.class);
			startActivity(intentPW);
			finish();
		}
	}

	/**
	 * initialize control
	 */
	private void initControl() {
		// button
		btnCancel = (Button) findViewById(R.id.btn_disagree);
		btnCancel.setOnClickListener(this);
		btnContinue = (Button) findViewById(R.id.btn_agree);
		btnContinue.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == btnCancel)
			finish();
		else if (v == btnContinue) {
			Intent intent = new Intent(TermOfServiceActivity.this,
					MasterPasswordChangeActivity.class);
			/*
			 * status:false is create new password;status :true is change
			 * password
			 */
			intent.putExtra(Contants.IS_CHANGE_PASSWORD, false);
			startActivity(intent);
			finish();
		}
	}

	@SuppressWarnings("unused")
	private void showDialogCreateNewPassWord() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// no action happened
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// Exit button clicked
					if (idManagerPreference.getSecurityMode() == Contants.KEY_OFF) {
						Intent intentHome = new Intent(TermOfServiceActivity.this,
								HomeScreeenActivity.class);
						startActivity(intentHome);
					} else {
						Intent intent = new Intent(TermOfServiceActivity.this,
								MasterPasswordActivity.class);
						startActivity(intent);
					}
					finish();
					break;
				}
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(TermOfServiceActivity.this);
		builder.setMessage(R.string.title_master_pass).setIcon(R.drawable.icon)
				.setTitle(getString(R.string.app_name)).setCancelable(false)
				.setPositiveButton(R.string.confirm_ok, dialogClickListener)
				.setNegativeButton(R.string.confirm_cancel, dialogClickListener);
		builder.show();
	}
}
