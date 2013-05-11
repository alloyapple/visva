package com.lemon.fromangle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.service.MessageFollowService;
import com.lemon.fromangle.utility.StringUtility;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ValidateScreenActivity extends LemonBaseActivity {

	private TextView lblMessage;
	private FromAngleSharedPref mFromAngleSharedPref;
	private String userId = null, userName = null;

	private PendingIntent pendingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_validate);
		lblMessage = (TextView) findViewById(R.id.lblMessage);

		mFromAngleSharedPref = new FromAngleSharedPref(this);
		userId = mFromAngleSharedPref.getUserId();
		if (!StringUtility.isEmpty(userId)) {
			userName = mFromAngleSharedPref.getUserName();
			lblMessage.setText(getString(R.string.mr_ms_name,userName));
		}else{
			lblMessage.setText(getString(R.string.mr_ms_name,""));
		}
		if (!mFromAngleSharedPref.getRunFromActivity()) {
			startRunAlarmManager();
			shiftValueForValidation();
		}
	}

	private void shiftValueForValidation() {
		mFromAngleSharedPref.setFirstTimeSetting(false);
		mFromAngleSharedPref.setTopScreenFinalValidation(mFromAngleSharedPref
				.getTopScreenNextValidation());
		String dateStr = mFromAngleSharedPref.getTopScreenNextValidation();
		Date date1 = new Date();
		int daysAfter = Integer.parseInt(mFromAngleSharedPref
				.getValidationDaysAfter().toString());
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			date1 = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date nextValidationDate = addDaysToDate(date1, daysAfter);
		String nextValidationDateStr;
		nextValidationDateStr = df.format(nextValidationDate);

		mFromAngleSharedPref.setTopScreenNextValidation(nextValidationDateStr);
	}

	public static Date addDaysToDate(Date input, int numberDay) {

		Calendar defaulCalender = Calendar.getInstance();
		defaulCalender.setTime(input);
		defaulCalender.add(Calendar.DATE, numberDay);
		Date resultdate = new Date(defaulCalender.getTimeInMillis());
		return resultdate;
	}

	public void onOKClick(View v) {
		mFromAngleSharedPref.setStopAlarm(true);
		mFromAngleSharedPref.setValidationMode(0);
		if (!mFromAngleSharedPref.getRunFromActivity()) {
			Intent intent = new Intent(ValidateScreenActivity.this,
					TopScreenActivity.class);
			startActivity(intent);
		}
		finish();
	}

	public void onCancelClick(View v) {
		mFromAngleSharedPref.setStopAlarm(true);
		if (mFromAngleSharedPref.getValidationMode() < 1)
			mFromAngleSharedPref.setValidationMode(1);
		else
			mFromAngleSharedPref.setValidationMode(2);
		if (!mFromAngleSharedPref.getRunFromActivity()) {
			Intent intent = new Intent(ValidateScreenActivity.this,
					TopScreenActivity.class);
			startActivity(intent);
		}
		finish();
	}

	private void startRunAlarmManager() {

		Date date1 = new Date();
		String dateStr = mFromAngleSharedPref.getTopScreenNextValidation();
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			date1 = df.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long timeOfDate = date1.getTime();
		long totalDelayTime = timeOfDate;
		long currenttime = System.currentTimeMillis();
		int delayTime = (int) (totalDelayTime - currenttime);
		int timeDelay = delayTime / 1000;
		Log.e("delay time", "delay time " + delayTime);
		Intent myIntent = new Intent(ValidateScreenActivity.this,
				MessageFollowService.class);

		pendingIntent = PendingIntent.getService(ValidateScreenActivity.this,
				0, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(System.currentTimeMillis());

		calendar.add(Calendar.SECOND, timeDelay);

		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pendingIntent);

	}
}
