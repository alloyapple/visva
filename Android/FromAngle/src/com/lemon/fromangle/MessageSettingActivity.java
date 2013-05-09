package com.lemon.fromangle;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;
import com.lemon.fromangle.config.WebServiceConfig;
import com.lemon.fromangle.controls.PaymentAcitivty;
import com.lemon.fromangle.controls.PaymentService;
import com.lemon.fromangle.network.AsyncHttpPost;
import com.lemon.fromangle.network.AsyncHttpResponseProcess;
import com.lemon.fromangle.network.ParameterFactory;
import com.lemon.fromangle.network.ParserUtility;
import com.lemon.fromangle.utility.DialogUtility;
import com.lemon.fromangle.utility.StringUtility;

public class MessageSettingActivity extends PaymentAcitivty {

	private Button btn1;
	private Button btn2;
	private Button btn3;
	private LinearLayout layoutInfo1;
	private EditText txtName1;
	private EditText txtEmail1;
	private EditText txtTel1;
	private EditText txtMessage1;
	private LinearLayout layoutInfo2;
	private EditText txtName2;
	private EditText txtEmail2;
	private EditText txtTel2;
	private EditText txtMessage2;
	private LinearLayout layoutInfo3;
	private EditText txtName3;
	private EditText txtEmail3;
	private EditText txtTel3;
	private EditText txtMessage3;
	private com.lemon.fromangle.utility.AutoBGButton btnStart;
	private com.lemon.fromangle.utility.AutoBGButton btnStop;
	private com.lemon.fromangle.utility.AutoBGButton btnReturn;

	private MessageSettingActivity self;
	private PaymentService paymentService;
	private FromAngleSharedPref mFromAngleSharedPref;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_setting_detail);
		paymentService = new PaymentService(this);
		mFromAngleSharedPref = new FromAngleSharedPref(this);
		initUI();
		self = this;

		userId = GlobalValue.prefs.getUserId();
	}

	// private void checkMessageInfoFromServer(String userId) {
	// // TODO Auto-generated method stub
	// List<NameValuePair> params = ParameterFactory
	// .getMessageInfoFromServer(userId);
	// AsyncHttpPost postRegister = new AsyncHttpPost(
	// MessageSettingActivity.this, new AsyncHttpResponseProcess(
	// MessageSettingActivity.this) {
	// @Override
	// public void processIfResponseSuccess(String response) {
	// /* check info response from server */
	// Log.e("message", "nesssage "+response);
	// }
	//
	// @Override
	// public void processIfResponseFail() {
	// // TODO Auto-generated method stub
	// Log.e("failed ", "failed");
	// }
	// }, params, true);
	// postRegister.execute(WebServiceConfig.URL_MESSAGE_SETTING);
	// }

	private void initUI() {
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		layoutInfo1 = (LinearLayout) findViewById(R.id.layoutInfo1);
		txtName1 = (EditText) findViewById(R.id.txtName1);
		txtEmail1 = (EditText) findViewById(R.id.txtEmail1);
		txtTel1 = (EditText) findViewById(R.id.txtTel1);
		txtMessage1 = (EditText) findViewById(R.id.txtMessage1);
		layoutInfo2 = (LinearLayout) findViewById(R.id.layoutInfo2);
		txtName2 = (EditText) findViewById(R.id.txtName2);
		txtEmail2 = (EditText) findViewById(R.id.txtEmail2);
		txtTel2 = (EditText) findViewById(R.id.txtTel2);
		txtMessage2 = (EditText) findViewById(R.id.txtMessage2);
		layoutInfo3 = (LinearLayout) findViewById(R.id.layoutInfo3);
		txtName3 = (EditText) findViewById(R.id.txtName3);
		txtEmail3 = (EditText) findViewById(R.id.txtEmail3);
		txtTel3 = (EditText) findViewById(R.id.txtTel3);
		txtMessage3 = (EditText) findViewById(R.id.txtMessage3);
		btnStart = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnStart);
		btnStop = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnStop);
		btnReturn = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnReturn);

		setActiveButton(1);
		// Event

		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				layoutInfo1.setVisibility(View.VISIBLE);
				layoutInfo2.setVisibility(View.GONE);
				layoutInfo3.setVisibility(View.GONE);
				setActiveButton(1);
			}
		});
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkValidateInfo1()) {
					layoutInfo1.setVisibility(View.GONE);
					layoutInfo3.setVisibility(View.GONE);
					layoutInfo2.setVisibility(View.VISIBLE);
					setActiveButton(2);
				}

			}
		});
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkValidateInfo2()) {
					layoutInfo1.setVisibility(View.GONE);
					layoutInfo2.setVisibility(View.GONE);
					layoutInfo3.setVisibility(View.VISIBLE);
					setActiveButton(3);
				}
			}
		});
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
<<<<<<< .mine
				if (checkValidateInfo1()) {
					String userId = mFromAngleSharedPref.getUserId();
					if (!StringUtility.isEmpty(userId))
						paymentService.checkPayment(userId);
=======
				if (checkValidateInfo3()) {
					if (!StringUtility.isEmpty(userId)) {
						Toast.makeText(self, "On Start", Toast.LENGTH_LONG)
								.show();
						onStartSave();
					} else {
						showToast(getString(R.string.setting_user_first));
					}
>>>>>>> .r392
				}
			}
		});

	}

	private void setActiveButton(int index) {
		btn1.setBackgroundColor(this.getResources().getColor(R.color.blue));
		btn2.setBackgroundColor(this.getResources().getColor(R.color.blue));
		btn3.setBackgroundColor(this.getResources().getColor(R.color.blue));
		switch (index) {
		case 1:
			btn1.setBackgroundColor(this.getResources().getColor(
					R.color.blue_light));
			break;
		case 2:
			btn2.setBackgroundColor(this.getResources().getColor(
					R.color.blue_light));
			break;
		case 3:
			btn3.setBackgroundColor(this.getResources().getColor(
					R.color.blue_light));
			break;

		default:
			break;
		}
	}

	private void onStartSave() {
		String name1 = txtName1.getText().toString();
		String name2 = txtName2.getText().toString();
		String name3 = txtName3.getText().toString();
		String email1 = txtEmail1.getText().toString();
		String email2 = txtEmail2.getText().toString();
		String email3 = txtEmail3.getText().toString();
		String message1 = txtMessage1.getText().toString();
		String message2 = txtMessage2.getText().toString();
		String message3 = txtMessage3.getText().toString();
		String tel1 = txtTel1.getText().toString();
		String tel2 = txtTel2.getText().toString();
		String tel3 = txtTel3.getText().toString();
		String status = "1";

		List<NameValuePair> params = ParameterFactory
				.createMessageSettingParams(userId, name1, email1, message1,
						name2, email2, message2, name3, email3, message3,
						status);

		AsyncHttpPost get = new AsyncHttpPost(MessageSettingActivity.this,
				new AsyncHttpResponseProcess(MessageSettingActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check response */
						checkResponseFromServer(response);
					}
				}, params, true);
		get.execute(WebServiceConfig.URL_MESSAGE_SETTING);

	}

	private void checkResponseFromServer(String response) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		String errorMsg = null;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject != null && jsonObject.length() > 0) {
				errorMsg = ParserUtility.getStringValue(jsonObject,
						GlobalValue.PARAM_ERROR);
				int error = Integer.parseInt(errorMsg);
				if (error == GlobalValue.MSG_RESPONSE_MSG_SETING_CHANGE_SUCESS) {
					showToast(getString(R.string.sucess));
					GlobalValue.prefs.setMessageSettingStatus(errorMsg);
				} else if (error == GlobalValue.MSG_RESPONSE_MSG_SETTING_SUCESS) {
					showToast(getString(R.string.change_info_sucess));
					GlobalValue.prefs.setMessageSettingStatus(errorMsg);
				} else
					DialogUtility.alert(MessageSettingActivity.this,
							getString(R.string.failed_to_conect_server));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			DialogUtility.alert(MessageSettingActivity.this,
					getString(R.string.failed_to_conect_server));
			e.printStackTrace();
		}
	}

	private boolean checkValidateInfo1() {
		if (StringUtility.isEmpty(txtEmail1)
				|| StringUtility.isEmpty(txtMessage1)
				|| StringUtility.isEmpty(txtName1)) {
			Toast.makeText(self, R.string.plz_input_required_field,
					Toast.LENGTH_LONG).show();
			return false;
		} else if (!StringUtility.checkEmail2(txtEmail1.getText().toString())) {
			Toast.makeText(self, R.string.incorrect_email_address,
					Toast.LENGTH_LONG).show();
			return false;
		} else
			return true;

	}

	private boolean checkValidateInfo2() {
		if (StringUtility.isEmpty(txtEmail2)
				|| StringUtility.isEmpty(txtMessage2)
				|| StringUtility.isEmpty(txtName2)) {
			// Toast.makeText(
			// self,
			// txtEmail2.getText().toString() + " : "
			// + txtMessage2.getText().toString() + " : "
			// + txtName2.getText().toString(), Toast.LENGTH_LONG)
			// .show();
			Toast.makeText(self, R.string.plz_input_required_field,
					Toast.LENGTH_LONG).show();
			return false;
		} else if (!StringUtility.checkEmail2(txtEmail2.getText().toString())) {
			Toast.makeText(self, R.string.incorrect_email_address,
					Toast.LENGTH_LONG).show();
			return false;
		} else
			return true;

	}

	private boolean checkValidateInfo3() {
		if (StringUtility.isEmpty(txtEmail3)
				|| StringUtility.isEmpty(txtMessage3)
				|| StringUtility.isEmpty(txtName3)) {
			Toast.makeText(self, R.string.plz_input_required_field,
					Toast.LENGTH_LONG).show();
			return false;
		} else if (!StringUtility.checkEmail2(txtEmail3.getText().toString())) {
			Toast.makeText(self, R.string.incorrect_email_address,
					Toast.LENGTH_LONG).show();
			return false;
		} else
			return true;
	}
<<<<<<< .mine

	@Override
	public void onPaymentSuccess() {
		// TODO Auto-generated method stub
		Toast.makeText(self, "On Start", Toast.LENGTH_LONG).show();
		onStartSave();
	}
=======

	private void showToast(String string) {
		Toast.makeText(MessageSettingActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}
>>>>>>> .r392
}
