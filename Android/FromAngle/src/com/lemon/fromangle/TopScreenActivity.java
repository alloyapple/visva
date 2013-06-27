package com.lemon.fromangle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;
import com.lemon.fromangle.controls.PaymentActivity;
import com.lemon.fromangle.controls.PaymentService;
import com.lemon.fromangle.utility.StringUtility;
import com.payment.BillingHelper;
import com.payment.BillingService;

public class TopScreenActivity extends PaymentActivity {

	// private RelativeLayout layoutHeader;
	private com.lemon.fromangle.utility.AutoBGButton btnHome;
	// private LinearLayout layoutTopStatus;
	private ImageView imgTopStatus;
	// private LinearLayout layoutLastValidationDate;
	private TextView txtFinalValidation;
	private TextView lblStatusFinalValidate;
	// private LinearLayout layoutNextValidationDate;
	private TextView txtNextValidation;
	private TextView lblStatusNextValidate;
	private LinearLayout layoutSetting;
	private ImageView imgMessageStatus;
	private LinearLayout layoutMessageSetting;
	private ImageView imgMessageSettingStatus;
	private LinearLayout layoutValidate;
	private ImageView imgValidateStatus;
	private boolean checkDialogReminder = false;
	private TopScreenActivity self;
	private PaymentService paymentService;

	private FromAngleSharedPref mFromAngleSharedPref;
	public Handler mTransactionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (BillingHelper.latestPurchase.isPurchased()) {
				paymentService.updatePayment(mFromAngleSharedPref.getUserId());
			}
		};

	};

	// private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_top_screen_2);

		mFromAngleSharedPref = new FromAngleSharedPref(this);
		mFromAngleSharedPref.setExistByTopScreen(false);
		initUI();
		startService(new Intent(this, BillingService.class));
		BillingHelper.setCompletedHandler(mTransactionHandler);
		paymentService = new PaymentService(this);
		String userId = mFromAngleSharedPref.getUserId();
		if (!StringUtility.isEmpty(userId))
			// onPaymentSuccess();
			paymentService.checkPaymentTopScreeen(userId);
		// userId = mFromAngleSharedPref.getUserId();

		self = this;
	}

	private void initUI() {
		// layoutHeader = (RelativeLayout) findViewById(R.id.layoutHeader);
		btnHome = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnHome);
		// layoutTopStatus = (LinearLayout) findViewById(R.id.layoutTopStatus);
		imgTopStatus = (ImageView) findViewById(R.id.imgTopStatus);
		// layoutLastValidationDate = (LinearLayout)
		// findViewById(R.id.layoutLastValidationDate);
		txtFinalValidation = (TextView) findViewById(R.id.txtFinalValidation);
		lblStatusFinalValidate = (TextView) findViewById(R.id.lblStatusFinalValidate);
		// layoutNextValidationDate = (LinearLayout)
		// findViewById(R.id.layoutNextValidationDate);
		txtNextValidation = (TextView) findViewById(R.id.txtNextValidation);
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
				mFromAngleSharedPref.setRunFromActivity(true);
				Intent intent = new Intent(TopScreenActivity.this,
						ValidateScreenActivity.class);
				intent.putExtra(GlobalValue.IS_RUN_FROM_ACTIVITY, true);
				startActivity(intent);

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
		BillingHelper.stopService(this);
		mFromAngleSharedPref.setExistByTopScreen(true);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Log.e("user id " + mFromAngleSharedPref.getKeyRunAlarm(), "user id "
				+ mFromAngleSharedPref.getUserId());
		Log.e("onDestroyedrr",
				"onDestroyedme me "
						+ mFromAngleSharedPref.getModeDestroyedService());
		if (mFromAngleSharedPref.getModeDestroyedService() == GlobalValue.KEY_DESTROYED_SERVICE_BY_FORCE_CLOSE) {
			mFromAngleSharedPref.setKeyRunAlarm(false);
			mFromAngleSharedPref.setMessageSettingStatus("");
			mFromAngleSharedPref.setValidationMode(0);
			mFromAngleSharedPref.putAppStatus(""
					+ GlobalValue.MSG_RESPONSE_MSG_SETTING_FAILED);
			mFromAngleSharedPref.putModeDestroyedService(0);
		} else {
			mFromAngleSharedPref.putModeDestroyedService(0);
		}
		if (mFromAngleSharedPref.getKeyRunAlarm()) {
			imgMessageStatus.setImageResource(R.drawable.bar_green);
			if (mFromAngleSharedPref.getFirstTimeSetting())
				txtFinalValidation.setText("------------------");
			else
				txtFinalValidation.setText(mFromAngleSharedPref
						.getTopScreenFinalValidation());
			txtNextValidation.setText(mFromAngleSharedPref
					.getTopScreenNextValidation());
			String statusMsg = mFromAngleSharedPref.getMessageSettingStatus();
			Log.e("status", "statusMsg " + statusMsg);
			if (!StringUtility.isEmpty(statusMsg) && !"".equals(statusMsg)) {
				int status = Integer.parseInt(statusMsg);
				int modeValidation = mFromAngleSharedPref.getValidationMode();
				if (status == GlobalValue.MSG_RESPONSE_MSG_SETING_CHANGE_SUCESS
						|| status == GlobalValue.MSG_RESPONSE_MSG_SETTING_SUCESS) {
					imgMessageSettingStatus
							.setImageResource(R.drawable.bar_green);

					// ////////////////////////////////////////////////////////
					if (modeValidation == 0) {
						imgValidateStatus
								.setImageResource(R.drawable.bar_green);
						imgTopStatus.setImageResource(R.drawable.bg_working);
						lblStatusFinalValidate.setText(getString(R.string.ok));
						lblStatusFinalValidate.setTextColor(Color.WHITE);
						lblStatusNextValidate.setText("---");
						lblStatusNextValidate.setTextColor(Color.WHITE);
					} else if (modeValidation == 1) {
						imgValidateStatus.setImageResource(R.drawable.bar_red);
						imgTopStatus.setImageResource(R.drawable.bg_safety);
						lblStatusFinalValidate.setText(getString(R.string.ng));
						lblStatusFinalValidate.setTextColor(Color.RED);
						lblStatusNextValidate.setText("---");
						lblStatusNextValidate.setTextColor(Color.BLACK);
					} else if (modeValidation == 2) {
						imgValidateStatus.setImageResource(R.drawable.bar_grey);
						imgTopStatus.setImageResource(R.drawable.bg_stop);
						imgMessageSettingStatus
								.setImageResource(R.drawable.bar_grey);
						lblStatusFinalValidate.setText(getString(R.string.ng));
						lblStatusFinalValidate.setTextColor(Color.RED);
						lblStatusNextValidate.setText(getString(R.string.ng));
						lblStatusNextValidate.setTextColor(Color.RED);

						if (mFromAngleSharedPref.getOpenDialogReminder()) {
							checkDialogReminder = true;
							mFromAngleSharedPref.setOpenDialogReminder(false);
						} else
							checkDialogReminder = false;
						if (checkDialogReminder) {
							creatDialogReminder(
									null,
									getResources().getString(
											R.string.title_reminder),
									R.layout.dialog_reminder, null).show();
							checkDialogReminder = true;
						}
					}
					// ///////////////////////////////////////
				} else {
					imgValidateStatus.setImageResource(R.drawable.bar_grey);
					imgMessageSettingStatus
							.setImageResource(R.drawable.bar_grey);
					imgTopStatus.setImageResource(R.drawable.bg_stop);
					lblStatusFinalValidate.setText("---");
					lblStatusFinalValidate.setTextColor(Color.BLACK);
					lblStatusNextValidate.setText("---");
					lblStatusNextValidate.setTextColor(Color.BLACK);
				}

			} else {
				imgMessageSettingStatus.setImageResource(R.drawable.bar_grey);
				imgTopStatus.setImageResource(R.drawable.bg_stop);
				imgValidateStatus.setImageResource(R.drawable.bar_grey);
				lblStatusFinalValidate.setText("---");
				lblStatusFinalValidate.setTextColor(Color.BLACK);
				lblStatusNextValidate.setText("---");
				lblStatusNextValidate.setTextColor(Color.BLACK);
			}
		} else {
			imgValidateStatus.setImageResource(R.drawable.bar_grey);
			imgMessageStatus.setImageResource(R.drawable.bar_grey);
			imgMessageSettingStatus.setImageResource(R.drawable.bar_grey);
			imgTopStatus.setImageResource(R.drawable.bg_stop);
			imgValidateStatus.setImageResource(R.drawable.bar_grey);
			lblStatusFinalValidate.setText("---");
			lblStatusFinalValidate.setTextColor(Color.BLACK);
			lblStatusNextValidate.setText("---");
			lblStatusNextValidate.setTextColor(Color.BLACK);
		}
		super.onResume();
	}

	private AlertDialog creatDialogReminder(String message, String title,
			int layout, DialogInterface.OnClickListener listener) {
		final String userName = mFromAngleSharedPref.getUserName();
		final String[] msgSettingTab1 = mFromAngleSharedPref
				.getMessageSettingTab1();
		final String[] msgSettingTab2 = mFromAngleSharedPref
				.getMessageSettingTab2();
		final String[] msgSettingTab3 = mFromAngleSharedPref
				.getMessageSettingTab3();
		String receivers = "", receiver1 = msgSettingTab1[0], receiver2 = msgSettingTab2[0], receiver3 = msgSettingTab3[0];
		if (StringUtility.isEmpty(msgSettingTab1[0])) {
			receiver1 = "";
		} else {
			receiver1 = msgSettingTab1[0];
			receivers += receiver1;
			if (StringUtility.isEmpty(msgSettingTab2[0])) {
				receiver2 = "";
			} else {
				receiver2 = "," + msgSettingTab2[0];
				receivers += receiver2;
				if (StringUtility.isEmpty(msgSettingTab3[0])) {
					receiver3 = "";
				} else {
					receiver3 = "," + msgSettingTab3[0];
					receivers += receiver3;
				}
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null)
			builder.setTitle(title);
		LayoutInflater inflater2 = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout layoutParent = (LinearLayout) inflater2.inflate(layout,
				(ViewGroup) findViewById(R.id.id_layout_reminder));
		TextView textViewMesage = (TextView) (layoutParent
				.findViewById(R.id.id_msg_stop_service));
		String s = getResources().getString(R.string.msg_stop_service,
				userName, receivers);
		// s.replace("%1$s", userName);
		// s.replace("%2$s", receivers);
		// textViewMesage.setText(getString(R.string.msg_stop_service));
		textViewMesage.setText(s);
		Log.e("receiver", "receiver " + receivers);
		TextView textView = (TextView) (layoutParent
				.findViewById(R.id.email_contact));
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sent = new Intent(android.content.Intent.ACTION_SEND);
				sent.setType("text/plain");
				sent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "ga@angelsmails.com" });
				sent.setType("vnd.android.cursor.dir/email");
				TopScreenActivity.this.startActivity(Intent.createChooser(sent,
						"Select"));

			}
		});
		builder.setPositiveButton(getResources().getString(R.string.btn_close),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						checkDialogReminder = false;
					}
				});
		builder.setView(layoutParent);
		return builder.create();
	}

	@Override
	public void onPaymentSuccess() {
		// TODO Auto-generated method stub
		paymentService.updatePayment(mFromAngleSharedPref.getUserId());
	}

	@Override
	public void onStartSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrialCase() {
		// TODO Auto-generated method stub
		creatDialog(null, getResources().getString(R.string.message_trial_case)).show();
	}

	private AlertDialog creatDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null)
			builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(getResources().getString(R.string.btn_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		return builder.create();
	}

	@Override
	public void onDeniedPayment() {
		// TODO Auto-generated method stub
		
	}
}
