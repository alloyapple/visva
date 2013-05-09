package com.lemon.fromangle;

import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lemon.fromangle.DialogDateTimePicker.DateTimeDialogListerner;
import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;
import com.lemon.fromangle.config.WebServiceConfig;
import com.lemon.fromangle.network.AsyncHttpPost;
import com.lemon.fromangle.network.AsyncHttpResponseProcess;
import com.lemon.fromangle.network.ParameterFactory;
import com.lemon.fromangle.network.ParserUtility;
import com.lemon.fromangle.utility.DialogUtility;
import com.lemon.fromangle.utility.StringUtility;
import com.lemon.fromangle.utility.TimeUtility;

public class TopScreenActivity extends Activity {

	private RelativeLayout layoutHeader;
	private com.lemon.fromangle.utility.AutoBGButton btnHome;
	private LinearLayout layoutTopStatus;
	private ImageView imgTopStatus;
	private LinearLayout layoutLastValidationDate;
	private EditText txtFinalValidation;
	private TextView lblStatusFinalValidate;
	private LinearLayout layoutNextValidationDate;
	private EditText txtNextValidation;
	private TextView lblStatusNextValidate;
	private LinearLayout layoutSetting;
	private ImageView imgMessageStatus;
	private LinearLayout layoutMessageSetting;
	private ImageView imgMessageSettingStatus;
	private LinearLayout layoutValidate;
	private ImageView imgValidateStatus;

	private TopScreenActivity self;

	private DialogDateTimePicker dateTimePicker;

	private FromAngleSharedPref mFromAngleSharedPref;

	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_top_screen);

		mFromAngleSharedPref = new FromAngleSharedPref(this);
		initUI();

		userId = mFromAngleSharedPref.getUserId();
		if (!StringUtility.isEmpty(userId))
			checkPayment(userId);
		self = this;
	}

	private void initUI() {
		layoutHeader = (RelativeLayout) findViewById(R.id.layoutHeader);
		btnHome = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnHome);
		layoutTopStatus = (LinearLayout) findViewById(R.id.layoutTopStatus);
		imgTopStatus = (ImageView) findViewById(R.id.imgTopStatus);
		layoutLastValidationDate = (LinearLayout) findViewById(R.id.layoutLastValidationDate);
		txtFinalValidation = (EditText) findViewById(R.id.txtFinalValidation);
		lblStatusFinalValidate = (TextView) findViewById(R.id.lblStatusFinalValidate);
		layoutNextValidationDate = (LinearLayout) findViewById(R.id.layoutNextValidationDate);
		txtNextValidation = (EditText) findViewById(R.id.txtNextValidation);
		lblStatusNextValidate = (TextView) findViewById(R.id.lblStatusNextValidate);
		layoutSetting = (LinearLayout) findViewById(R.id.layoutSetting);
		imgMessageStatus = (ImageView) findViewById(R.id.imgMessageStatus);
		layoutMessageSetting = (LinearLayout) findViewById(R.id.layoutMessageSetting);
		imgMessageSettingStatus = (ImageView) findViewById(R.id.imgMessageSettingStatus);
		layoutValidate = (LinearLayout) findViewById(R.id.layoutValidate);
		imgValidateStatus = (ImageView) findViewById(R.id.imgValidateStatus);

		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(getString(R.string.url_home));
				startActivity(new Intent(Intent.ACTION_VIEW, uri));

			}
		});

		layoutSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoActivity(self, SettingActivivity.class);

			}
		});
		layoutMessageSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoActivity(self, MessageSettingActivity.class);
			}
		});
		layoutValidate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				gotoActivity(self, ValidateScreenActivity.class);
			}
		});

		// txtFinalValidation.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// Date prevDate = new Date();
		// int preHour = 10, preMinute = 0;
		//
		// if (!StringUtility.isEmpty(txtFinalValidation)) {
		// prevDate = TimeUtility.getDate("yyyy/MM/dd",
		// txtFinalValidation.getText().toString()
		// .substring(0, 10));
		// preHour = Integer.parseInt(txtFinalValidation.getText()
		// .toString().substring(11, 13));
		// preMinute = Integer.parseInt(txtFinalValidation
		// .getText().toString().substring(14, 15));
		//
		// }
		//
		// dateTimePicker = new DialogDateTimePicker(self, prevDate,
		// preHour, preMinute, new DateTimeDialogListerner() {
		// @Override
		// public void onSelectDateTime(Date date,
		// int hour, int minute) {
		// String strResult = TimeUtility
		// .formatDateStr("yyyy/MM/dd", date)
		// + " "
		// + TimeUtility.formatTimeStr(hour,
		// minute);
		// txtFinalValidation.setText(strResult);
		// }
		// });
		// dateTimePicker.show();
		// }
		// return false;
		// }
		// });
		//
		// txtNextValidation.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// // TODO Auto-generated method stub
		// Date prevDate = new Date();
		// int preHour = 10, preMinute = 0;
		//
		// if (!StringUtility.isEmpty(txtNextValidation)) {
		// prevDate = TimeUtility.getDate("yyyy/MM/dd",
		// txtNextValidation.getText().toString()
		// .substring(0, 10));
		// preHour = Integer.parseInt(txtNextValidation.getText()
		// .toString().substring(11, 13));
		// preMinute = Integer.parseInt(txtNextValidation
		// .getText().toString().substring(14, 15));
		//
		// }
		//
		// dateTimePicker = new DialogDateTimePicker(self, prevDate,
		// preHour, preMinute, new DateTimeDialogListerner() {
		// @Override
		// public void onSelectDateTime(Date date,
		// int hour, int minute) {
		// String strResult = TimeUtility
		// .formatDateStr("yyyy/MM/dd", date)
		// + " "
		// + TimeUtility.formatTimeStr(hour,
		// minute);
		// txtNextValidation.setText(strResult);
		// }
		// });
		// dateTimePicker.show();
		// }
		// return false;
		// }
		// });
	}

	public void gotoActivity(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (!"".equals(mFromAngleSharedPref.getUserId())) {
			imgMessageStatus.setImageResource(R.drawable.bar_green);
			txtFinalValidation.setText(mFromAngleSharedPref
					.getTopScreenFinalValidation());
			txtNextValidation.setText(mFromAngleSharedPref
					.getTopScreenNextValidation());
			String statusMsg = mFromAngleSharedPref.getMessageSettingStatus();
			if (!StringUtility.isEmpty(statusMsg)) {
				int status = Integer.parseInt(statusMsg);
				if (status == GlobalValue.MSG_RESPONSE_MSG_SETING_CHANGE_SUCESS
						|| status == GlobalValue.MSG_RESPONSE_MSG_SETTING_SUCESS)
					imgMessageSettingStatus
							.setImageResource(R.drawable.bar_green);
				else
					imgMessageSettingStatus
							.setImageResource(R.drawable.bar_gray);
			}
		} else
			imgMessageStatus.setImageResource(R.drawable.bar_gray);
		super.onResume();
	}

	private void checkPayment(String userId) {
		List<NameValuePair> params = ParameterFactory
				.createCheckPayment(userId);
		AsyncHttpPost postCheckPayMent = new AsyncHttpPost(
				TopScreenActivity.this, new AsyncHttpResponseProcess(
						TopScreenActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						checkInfoReponseFromServer(response);
					}

					@Override
					public void processIfResponseFail() {
						// TODO Auto-generated method stub
						Log.e("failed ", "failed");
					}
				}, params, true);
		postCheckPayMent.execute(WebServiceConfig.URL_CHECK_PAYMENT);
	}

	private void checkInfoReponseFromServer(String response) {
		// TODO Auto-generated method stub
		Log.e("reponse", "reponse " + response);
		JSONObject jsonObject = null;
		String errorMsg = null;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject != null && jsonObject.length() > 0) {
				errorMsg = ParserUtility.getStringValue(jsonObject,
						GlobalValue.PARAM_ERROR);
				int error = Integer.parseInt(errorMsg);
				if (error == GlobalValue.MSG_REPONSE_PAID_NOT_EXPIRED) {
					/* paid not expired */
					checkPaymentPaidNotExpired();
				} else if (error == GlobalValue.MSG_REPONSE_PAID_EXPIRED) {
					/* paid expired */
					checkPaymentPaidExpired();
				} else if (error == GlobalValue.MSG_REPONSE_NOT_PAID) {
					/* not paid */
					checkPaymentNotPaid();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			DialogUtility.alert(TopScreenActivity.this,
					getString(R.string.failed_to_conect_server));
			e.printStackTrace();
		}

	}

	/**
	 * check payment user not paid
	 */
	private void checkPaymentNotPaid() {
		// TODO Auto-generated method stub
		showToast("user not paid");
	}

	/**
	 * check payment user paid expired
	 */
	private void checkPaymentPaidExpired() {
		// TODO Auto-generated method stub
		showToast("paid expired");
	}

	/**
	 * check payment user paid not expired
	 */
	private void checkPaymentPaidNotExpired() {
		// TODO Auto-generated method stub
		showToast("paid not expired");
	}

	private void showToast(String string) {
		Toast.makeText(TopScreenActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}
}
