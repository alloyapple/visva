package com.lemon.fromangle;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lemon.fromangle.DialogDateTimePicker.DateTimeDialogListerner;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_top_screen);
		initUI();

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

		txtFinalValidation.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Date prevDate = new Date();
					int preHour = 10, preMinute = 0;

					if (!StringUtility.isEmpty(txtFinalValidation)) {
						prevDate = TimeUtility.getDate("yyyy/MM/dd",
								txtFinalValidation.getText().toString()
										.substring(0, 10));
						preHour = Integer.parseInt(txtFinalValidation.getText()
								.toString().substring(11, 13));
						preMinute = Integer.parseInt(txtFinalValidation
								.getText().toString().substring(14, 15));

					}

					dateTimePicker = new DialogDateTimePicker(self, prevDate,
							preHour, preMinute, new DateTimeDialogListerner() {
								@Override
								public void onSelectDateTime(Date date,
										int hour, int minute) {
									String strResult = TimeUtility
											.formatDateStr("yyyy/MM/dd", date)
											+ " "
											+ TimeUtility.formatTimeStr(hour,
													minute);
									txtFinalValidation.setText(strResult);
								}
							});
					dateTimePicker.show();
				}
				return false;
			}
		});

		txtNextValidation.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// TODO Auto-generated method stub
					Date prevDate = new Date();
					int preHour = 10, preMinute = 0;

					if (!StringUtility.isEmpty(txtNextValidation)) {
						prevDate = TimeUtility.getDate("yyyy/MM/dd",
								txtNextValidation.getText().toString()
										.substring(0, 10));
						preHour = Integer.parseInt(txtNextValidation.getText()
								.toString().substring(11, 13));
						preMinute = Integer.parseInt(txtNextValidation
								.getText().toString().substring(14, 15));

					}

					dateTimePicker = new DialogDateTimePicker(self, prevDate,
							preHour, preMinute, new DateTimeDialogListerner() {
								@Override
								public void onSelectDateTime(Date date,
										int hour, int minute) {
									String strResult = TimeUtility
											.formatDateStr("yyyy/MM/dd", date)
											+ " "
											+ TimeUtility.formatTimeStr(hour,
													minute);
									txtNextValidation.setText(strResult);
								}
							});
					dateTimePicker.show();
				}
				return false;
			}
		});
	}

	public void gotoActivity(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		startActivity(intent);
	}
}
