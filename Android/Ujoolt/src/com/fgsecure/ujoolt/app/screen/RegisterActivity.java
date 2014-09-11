package com.fgsecure.ujoolt.app.screen;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.info.WebServiceConfig;
import com.fgsecure.ujoolt.app.network.AsyncHttpBase;
import com.fgsecure.ujoolt.app.network.AsyncHttpGet;
import com.fgsecure.ujoolt.app.network.AsyncHttpResponseListener;
import com.fgsecure.ujoolt.app.utillity.Language;
import com.fgsecure.ujoolt.app.utillity.StringUtility;
import com.fgsecure.ujoolt.app.utillity.Utility;

public class RegisterActivity extends Activity implements OnClickListener {

	private EditText txtRegisterUser, txtRegisterMail, txtRegisterPass, txtRegisterRepeat;
	private TextView lblRegisterUser, lblRegisterMail;
	private Button bthConfirmRegister, btnCancelRegister;
	private Typeface typeface;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);
		setTitle("Register");
		initComponent();
	}

	@Override
	public void onClick(View v) {
		if (v == bthConfirmRegister) {
			onClickConfirmRegister();

		} else if (v == btnCancelRegister) {
			onClickButtonCancelRegister();
		}
	}

	private void initComponent() {
		typeface = Typeface.createFromAsset(getAssets(), "fonts/Quicksand_Bold.ttf");

		txtRegisterUser = (EditText) findViewById(R.id.txtRegisterUser);
		txtRegisterMail = (EditText) findViewById(R.id.txtRegisterMail);
		txtRegisterPass = (EditText) findViewById(R.id.txtRegisterPassword);
		txtRegisterRepeat = (EditText) findViewById(R.id.txtRegisterPassword2);

		lblRegisterUser = (TextView) findViewById(R.id.lblRegisterUser);
		lblRegisterMail = (TextView) findViewById(R.id.lblRegisterMail);

		if (bthConfirmRegister == null) {
			bthConfirmRegister = (Button) findViewById(R.id.btnConfirmRegister);
		}
		bthConfirmRegister.setOnClickListener(this);
		bthConfirmRegister.setTypeface(typeface);
		bthConfirmRegister.setTextSize(19);

		if (btnCancelRegister == null) {
			btnCancelRegister = (Button) findViewById(R.id.btnCancelRegister);
		}
		btnCancelRegister.setOnClickListener(this);
		btnCancelRegister.setTypeface(typeface);
		btnCancelRegister.setTextSize(19);

		bthConfirmRegister.setText(Language.buttonConfirmRegisterName);
		btnCancelRegister.setText(Language.cancel);
		lblRegisterUser.setText(Language.userName);
		lblRegisterMail.setText(Language.eMail);
		txtRegisterUser.setHint(Language.userName);
	}

	private void onClickConfirmRegister() {
		if (isEmptyRegisterView()) {
			showDialogAlert(Language.signUp, Language.notifyNotEnoughInput, Language.tryAgain);

		} else if (!Utility.checkMailValid(txtRegisterMail.getText().toString())) {
			showDialogAlert(Language.signUp, Language.notifyEmailNotValid, "OK");

		} else if (!txtRegisterPass.getText().toString()
				.equals(txtRegisterRepeat.getText().toString())) {
			showDialogAlert(Language.signUp, Language.notifyPassNotMatch, Language.tryAgain);

		} else {
			String registerUser = txtRegisterUser.getText().toString();
			String registerMail = txtRegisterMail.getText().toString();
			String registerPass = txtRegisterPass.getText().toString();
			registerPass = Utility.encodeMD5(registerPass);

			registerUjooltAcount(registerMail, registerPass, "UJ", registerUser);
		}
	}

	public void onClickButtonCancelRegister() {
		finish();
	}

	private boolean isEmptyRegisterView() {
		if (Utility.checkEmptyEditText(txtRegisterMail)
				|| Utility.checkEmptyEditText(txtRegisterUser)
				|| Utility.checkEmptyEditText(txtRegisterPass)
				|| Utility.checkEmptyEditText(txtRegisterRepeat)) {
			return true;
		} else {
			return false;
		}
	}

	private void showDialogAlert(String tittle, String message, String buttonLabel) {
		AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
		builder.setTitle(tittle);
		builder.setMessage(message);
		builder.setPositiveButton(buttonLabel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	private void registerUjooltAcount(String email, String password, String loginType, String nick) {

		showProgressDialog("Please wait...");
		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(this, new AsyncHttpResponseListener() {

			@Override
			public void after(int statusCode, HttpResponse response) {
				closeProgressDialog();

				if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
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
							if (json.equalsIgnoreCase(WebServiceConfig.RESPONSE_REGISTER_SUCCESS)) {
								showDialogAlert(Language.signUp, Language.notifyCheckEmail, "OK");
								finish();
							} else if (json
									.equalsIgnoreCase(WebServiceConfig.RESPONSE_REGISTER_EXIST_EMAIL)) {
								showDialogAlert(Language.signUp, Language.notifyExistEmail,
										Language.tryAgain);
							} else {

							}
						}
					} else {
						showToastNetworkError();
					}
				} else {
					showToastNetworkError();
				}
			}

			@Override
			public void before() {
			}
		});
		try {

			asyncHttpGet.execute(WebServiceConfig.URL_REGISTER_UJOOLT + "?email=" + encode(email)
					+ "&password=" + encode(password) + "&logintype=" + encode(loginType)
					+ "&nick=" + encode(nick));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void showProgressDialog(String text) {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "Ujoolt", text, true);
			progressDialog.setCancelable(false);
		}
	}

	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	private void showToastNetworkError() {
		Toast.makeText(getBaseContext(), Language.notifyNoNetwork, Toast.LENGTH_SHORT).show();
	}

	private String encode(String value) throws UnsupportedEncodingException {
		if (value != null) {
			return StringUtility.encode(value);
		} else {
			return "";
		}
	}
}
