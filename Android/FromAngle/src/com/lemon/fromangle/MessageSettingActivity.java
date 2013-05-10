package com.lemon.fromangle;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import com.lemon.fromangle.utility.TimeUtility;
import com.payment.BillingHelper;
import com.payment.BillingService;

@SuppressLint("HandlerLeak")
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
	private com.lemon.fromangle.utility.AutoBGButton btnSave;
	private com.lemon.fromangle.utility.AutoBGButton btnLeft, btnRight;

	private MessageSettingActivity self;
	private PaymentService paymentService;
	private FromAngleSharedPref mFromAngleSharedPref;
	private String userId;
	private static final String TAG = "BillingService";
	public static final int STATUS_NOT_PAID = 2;
	public static final int STATUS_EXPIRED = 1;
	public static final int STATUS_NOT_EXPIRED = 0;
	public Handler mTransactionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "Transaction complete");
			Log.i(TAG, "Transaction status: "
					+ BillingHelper.latestPurchase.purchaseState);
			Log.i(TAG, "Item purchased is: "
					+ BillingHelper.latestPurchase.productId);

			if (BillingHelper.latestPurchase.isPurchased()) {
				showToast("Payment success");
				Log.i(TAG, "update payment to server");
				paymentService.updatePayment(mFromAngleSharedPref.getUserId(),
						TimeUtility.getCurentDate(),
						TimeUtility.getDateExpiry(30));
			}
		};

	};

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
		startService(new Intent(this, BillingService.class));
		BillingHelper.setCompletedHandler(mTransactionHandler);
	}

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
		btnSave = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnSave);
		if (mFromAngleSharedPref.getSaveInputMassage()) {
			String[] inputTab1 = mFromAngleSharedPref.getMessageSettingTab1();
			txtName1.setText(inputTab1[0]);
			txtEmail1.setText(inputTab1[1]);
			txtTel1.setText(inputTab1[2]);
			txtMessage1.setText(inputTab1[3]);
			String[] inputTab2 = mFromAngleSharedPref.getMessageSettingTab2();
			txtName2.setText(inputTab2[0]);
			txtEmail2.setText(inputTab2[1]);
			txtTel2.setText(inputTab2[2]);
			txtMessage2.setText(inputTab2[3]);
			String[] inputTab3 = mFromAngleSharedPref.getMessageSettingTab3();
			txtName3.setText(inputTab3[0]);
			txtEmail3.setText(inputTab3[1]);
			txtTel3.setText(inputTab3[2]);
			txtMessage3.setText(inputTab3[3]);

		}
		mFromAngleSharedPref.saveInputMessage(false);
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
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveInputPref();
				finish();
			}
		});
		btnStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkValidateInfo1()) {
					if (!StringUtility.isEmpty(userId)) {
						onPostStopToService("0");
					}
				}
			}

		});
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkValidateInfo1()) {
					if (!StringUtility.isEmpty(userId)) {
						// onPaymentSuccess();
						String userId = mFromAngleSharedPref.getUserId();
						if (!StringUtility.isEmpty(userId))
							paymentService.checkPayment(userId);
					} else {
						showToast(getString(R.string.setting_user_first));
					}

				}
			}
		});
		btnLeft = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
		btnRight = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnRight);
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(getString(R.string.url_home));
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});
	}

	private void onPostStopToService(String status) {
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

		List<NameValuePair> params = ParameterFactory
				.createMessageSettingParams(userId, name1, email1, message1,
						name2, email2, message2, name3, email3, message3,
						status);

		AsyncHttpPost get = new AsyncHttpPost(MessageSettingActivity.this,
				new AsyncHttpResponseProcess(MessageSettingActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check response */
						checkResponseStopFromServer(response);
						saveInputPref();
					}
				}, params, true);
		get.execute(WebServiceConfig.URL_MESSAGE_SETTING);
	}

	private void checkResponseStopFromServer(String response) {
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
					GlobalValue.prefs.setMessageSettingStatus("");
				} else if (error == GlobalValue.MSG_RESPONSE_MSG_SETTING_SUCESS) {
					showToast(getString(R.string.change_info_sucess));
					GlobalValue.prefs.setMessageSettingStatus("");
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

	private void onStartSave(String status) {
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
						saveInputPref();
					}
				}, params, true);
		get.execute(WebServiceConfig.URL_MESSAGE_SETTING);
	}

	private void checkResponseFromServer(String response) {
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
			if (StringUtility.isEmpty(txtName1))
				txtName1.setError(getSpanError(getString(R.string.error_name)));
			if (StringUtility.isEmpty(txtEmail1))
				txtEmail1
						.setError(getSpanError(getString(R.string.error_email)));
			if (StringUtility.isEmpty(txtTel1))
				txtTel1.setError(getSpanError(getString(R.string.error_phone)));
			if (StringUtility.isEmpty(txtMessage1))
				txtMessage1
						.setError(getSpanError(getString(R.string.error_message)));
			Toast.makeText(self, R.string.plz_input_required_field,
					Toast.LENGTH_LONG).show();
			return false;
		} else if (!StringUtility.checkEmail2(txtEmail1.getText().toString())) {
			txtEmail1.setError(getSpanError(getString(R.string.error_email)));
			Toast.makeText(this, R.string.email_not_validate, Toast.LENGTH_LONG)
					.show();
			return false;
		} else
			return true;

	}

	private boolean checkValidateInfo2() {
		if (StringUtility.isEmpty(txtEmail2)
				|| StringUtility.isEmpty(txtMessage2)
				|| StringUtility.isEmpty(txtName2)) {
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

	@Override
	public void onPaymentSuccess() {
		// TODO Auto-generated method stub
		onStartSave("1");
	}

	private void showToast(String string) {
		Toast.makeText(MessageSettingActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public void saveInputPref() {
		mFromAngleSharedPref.setMessageSettingTab1(txtName1.getText()
				.toString(), txtEmail1.getText().toString(), txtTel1.getText()
				.toString(), txtMessage1.getText().toString());
		mFromAngleSharedPref.setMessageSettingTab2(txtName2.getText()
				.toString(), txtEmail2.getText().toString(), txtTel2.getText()
				.toString(), txtMessage2.getText().toString());
		mFromAngleSharedPref.setMessageSettingTab3(txtName3.getText()
				.toString(), txtEmail3.getText().toString(), txtTel3.getText()
				.toString(), txtMessage3.getText().toString());
		mFromAngleSharedPref.saveInputMessage(true);

	}

	@Override
	protected void onDestroy() {
		BillingHelper.stopService();
		super.onDestroy();
	}

	public SpannableStringBuilder getSpanError(String s) {
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(s);
		ssbuilder.setSpan(fgcspan, 0, s.length(), 0);
		return ssbuilder;
	}
}
