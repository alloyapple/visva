package visvateam.outsource.idmanager.activities;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.activities.homescreen.HomeScreeenActivity;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.idxpwdatabase.IDxPWDataBaseHandler;
import visvateam.outsource.idmanager.idxpwdatabase.UserDB;
import visvateam.outsource.idmanager.info.EmailValidator;
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
import android.widget.EditText;

public class RegisterEmailActivity extends Activity {
	private EditText mEditTextEmail;
	private Button mBtnClearText;
	private IDxPWDataBaseHandler mIDxPWDataBaseHandler;
	private EmailValidator mEmailValidator;
	private boolean isCreateNew = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_email_register);

		SQLiteDatabase.loadLibs(this);
		mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
		mEmailValidator = new EmailValidator();

		isCreateNew = getIntent().getExtras().getBoolean(
				Contants.CREATE_NEW_EMAIL);
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
		mBtnClearText = (Button) findViewById(R.id.btn_close);
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
	}

	public void onReturn(View v) {
		finish();
	}

	public void onClickRegisterEmail(View v) {
		String emailAddress = mEditTextEmail.getText().toString();
		if (!"".equals(emailAddress))
			if (mEmailValidator.validate(emailAddress)) {
				UserDB user = mIDxPWDataBaseHandler
						.getUser(Contants.MASTER_PASSWORD_ID);
				user.setsEmail(emailAddress);
				mIDxPWDataBaseHandler.updateUser(user);
				Intent intent = null;
				if (isCreateNew) {
					intent = new Intent(RegisterEmailActivity.this,
							HomeScreeenActivity.class);
					startActivity(intent);

				} else {
					UserDB usera = mIDxPWDataBaseHandler
							.getUser(Contants.MASTER_PASSWORD_ID);
					Log.e("user email", "eamil " + usera.getsEmail());
					sendMailConfirm(usera.getsEmail());
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
}
