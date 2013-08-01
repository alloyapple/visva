package com.japanappstudio.IDxPassword.activities;

import com.japanappstudio.IDxPassword.activities.homescreen.HomeScreeenActivity;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;
import com.japanappstudio.IDxPassword.idxpwdatabase.UserDB;
import com.japanappstudio.IDxPassword.info.EmailValidator;

import net.sqlcipher.database.SQLiteDatabase;
import com.japanappstudio.IDxPassword.activities.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterEmailActivity extends Activity {
	private EditText mEditTextEmail;
	private Button mBtnClearText;
	private IDxPWDataBaseHandler mIDxPWDataBaseHandler;
	private EmailValidator mEmailValidator;
	private boolean isCreateNew = false;
	private CheckBox chBUpdateNewInfo;
	private CheckBox chBUpdateImportanInfo;
	private String sentUpdateNewInfoMsg = "NO";
	private String sentImportantInfoMsg = "NO";
	private String newEmail = "", oldEmail = "";
	private UserDB myUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_email_register);

		SQLiteDatabase.loadLibs(this);
		mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
		myUser = mIDxPWDataBaseHandler.getUser(Contants.MASTER_PASSWORD_ID);
		oldEmail = myUser.getsEmail();
		mEmailValidator = new EmailValidator();

		isCreateNew = getIntent().getExtras().getBoolean(
				Contants.CREATE_NEW_EMAIL);
		if(!isCreateNew)
			((TextView) findViewById(R.id.title_register_mail)).setText(getResources().getString(R.string.re_register_email_title));
		mEditTextEmail = (EditText) findViewById(R.id.edit_text_email);
		mEditTextEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				mBtnClearText.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		mBtnClearText = (Button) findViewById(R.id.btn_close_email);
		mBtnClearText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mEditTextEmail.setText("");
				mBtnClearText.setVisibility(View.GONE);
			}
		});

		UserDB user = mIDxPWDataBaseHandler
				.getUser(Contants.MASTER_PASSWORD_ID);
		if (!"".equals(user.getsEmail().toString()))
			showEmailDialog();
		mEditTextEmail.setText(oldEmail);
		chBUpdateNewInfo = (CheckBox) findViewById(R.id.check_box_sent_email_update_info);
		chBUpdateImportanInfo = (CheckBox) findViewById(R.id.check_box_sent_email_update_impotant_infp);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onClickRegisterEmail(View v) {
		newEmail = mEditTextEmail.getText().toString();
		if (chBUpdateImportanInfo.isChecked())
			sentImportantInfoMsg = "OK";
		else
			sentImportantInfoMsg = "NO";
		if (chBUpdateNewInfo.isChecked())
			sentUpdateNewInfoMsg = "OK";
		else
			sentUpdateNewInfoMsg = "NO";
		if (!"".equals(newEmail))
			if (mEmailValidator.validate(newEmail)) {
				myUser.setsEmail(newEmail);
				mIDxPWDataBaseHandler.updateUser(myUser);
				Intent intent = null;
				if (isCreateNew) {
					intent = new Intent(RegisterEmailActivity.this,
							HomeScreeenActivity.class);

					startActivity(intent);

					initShareItent(
							getString(R.string.email_subject),
							getString(R.string.new_email_create_content,
									sentUpdateNewInfoMsg, sentImportantInfoMsg,
									myUser.getsEmail()),
							getString(R.string.email_to_sent));

				} else {
					UserDB usera = mIDxPWDataBaseHandler
							.getUser(Contants.MASTER_PASSWORD_ID);
					Log.e("user email", "eamil " + usera.getsEmail());
					// sendMailConfirm(usera.getsEmail());
					initShareItent(
							getString(R.string.email_update),
							getString(R.string.change_email_content,
									sentUpdateNewInfoMsg, sentImportantInfoMsg,
									myUser.getsEmail(), oldEmail),
							getString(R.string.email_to_sent));
				}
				finish();

			} else {
				mEditTextEmail.setText("");
				mBtnClearText.setVisibility(View.GONE);
			}
	}

	private void sendMailConfirm(String getsEmail) {
		// TODO Auto-generated method stub
		Intent gmail = new Intent(Intent.ACTION_VIEW);
		gmail.setClassName("com.google.android.gm",
				"com.google.android.gm.ComposeActivityGmail");
		gmail.putExtra(Intent.EXTRA_EMAIL, new String[] { getsEmail });
		gmail.setData(Uri.parse(getsEmail));
		gmail.putExtra(Intent.EXTRA_SUBJECT,
				"[IDxPassword]Change email address");
		gmail.setType("plain/text");
		gmail.putExtra(Intent.EXTRA_TEXT,
				"You have changed your email address for IDxPassword");
		startActivity(gmail);
	}

	private void showEmailDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon);
		builder.setMessage("ID e-mail address is already registered. Do you want to register a new email address?");
		builder.setPositiveButton(R.string.confirm_ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
		builder.setNegativeButton(R.string.confirm_cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
						return;
					}
				});
	}

	private void initShareItent(String subject, String content, String email) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, "" + subject);
		share.putExtra(Intent.EXTRA_TEXT, "" + content);
		share.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		// if (pFilePath != null)
		// share.putExtra(Intent.EXTRA_STREAM,
		// Uri.fromFile(new File(pFilePath)));
		share.setType("vnd.android.cursor.dir/email");
		startActivity(Intent.createChooser(share, "Select"));

	}
}
