package com.lemon.fromangle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.WebServiceConfig;
import com.lemon.fromangle.network.AsyncHttpPost;
import com.lemon.fromangle.network.AsyncHttpResponseProcess;
import com.lemon.fromangle.network.ParameterFactory;
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
			lblMessage.setText(getString(R.string.mr_ms_name, userName));
		} else {
			lblMessage.setText(getString(R.string.mr_ms_name, ""));
		}
		if (!mFromAngleSharedPref.getRunFromActivity()) {
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
		final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
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

//	public static Date addDaysToDate(Date input, int numberDay) {
//		Log.e("date to string", "date to string "+input.toLocaleString());
//		Calendar defaulCalender = Calendar.getInstance();
//		defaulCalender.setTime(input);
//		defaulCalender.add(Calendar.MINUTE, numberDay);
//		Date resultdate = new Date(defaulCalender.getTimeInMillis());
//		return resultdate;
//	}

	public static Date addDaysToDate(Date input, int numberDay) {
		Log.e("dateintpu", "date input "+input.toLocaleString());
		Calendar defaulCalender = Calendar.getInstance();
		defaulCalender.setTime(input);
		long time1 = defaulCalender.getTimeInMillis();
		long time2 = numberDay * 60 *1000;
		long resultTime = time1 + time2;
		defaulCalender.add(Calendar.YEAR, 0);
		defaulCalender.add(Calendar.MINUTE, numberDay);

		Date resultdate = new Date(resultTime);
		return resultdate;
	}
	public void onOKClick(View v) {
		startRunAlarmManager();
		mFromAngleSharedPref.setStopAlarm(true);
		mFromAngleSharedPref.setValidationMode(0);
		if (mFromAngleSharedPref.getRunOnBackGround()
				&& mFromAngleSharedPref.getExistByTopScreen()) {
			Intent intent = new Intent(ValidateScreenActivity.this,
					TopScreenActivity.class);
			startActivity(intent);
		}
		/* send update status to server */
		if (!StringUtility.isEmpty(userId)) {
			sendUpdateStatusToServer("1");
		} else
			finish();
	}

	private void sendUpdateStatusToServer(String status) {
		// TODO Auto-generated method stub
		List<NameValuePair> params = ParameterFactory.updateStatusForServer(
				userId, status);
		AsyncHttpPost postUpdateStt = new AsyncHttpPost(
				ValidateScreenActivity.this, new AsyncHttpResponseProcess(
						ValidateScreenActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("my response", "my response " + response);
						finish();
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, params, true);
		postUpdateStt.execute(WebServiceConfig.URL_MESSAGE_SETTING);
	}

	public void onCancelClick(View v) {
		mFromAngleSharedPref.setStopAlarm(true);
		if (mFromAngleSharedPref.getValidationMode() < 1){
			mFromAngleSharedPref.setValidationMode(1);
			startRunAlarmManager();
		}
		else
			mFromAngleSharedPref.setValidationMode(2);
		if (mFromAngleSharedPref.getRunOnBackGround()
				&& mFromAngleSharedPref.getExistByTopScreen()) {
			Intent intent = new Intent(ValidateScreenActivity.this,
					TopScreenActivity.class);
			startActivity(intent);
		}
		/* send update status to server */
//		if (!StringUtility.isEmpty(userId)) {
//			sendUpdateStatusToServer("0");
//		} else
			finish();
	}

//	private void startRunAlarmManager() {
//
//		Date date1 = new Date();
//		String dateStr = mFromAngleSharedPref.getTopScreenNextValidation();
//		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		try {
//			date1 = df.parse(dateStr);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		long timeOfDate = date1.getTime();
//		long totalDelayTime = timeOfDate;
//		long currenttime = System.currentTimeMillis();
//		long delayTime = totalDelayTime - currenttime;
//		int timeDelay = (int) (delayTime / 1000);
//		Intent myIntent = new Intent(ValidateScreenActivity.this,
//				MessageFollowService.class);
//
//		pendingIntent = PendingIntent.getService(ValidateScreenActivity.this,
//				0, myIntent, 0);
//
//		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//		Calendar calendar = Calendar.getInstance();
//
//		calendar.setTimeInMillis(System.currentTimeMillis());
//
//		calendar.add(Calendar.SECOND, timeDelay);
//
//		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//				pendingIntent);
//
//	}
	
	private void startRunAlarmManager(){
		int daysAfter = Integer.parseInt(mFromAngleSharedPref.getValidationDaysAfter());
		int delayTime = daysAfter * 60 ;
		Intent myIntent = new Intent(ValidateScreenActivity.this,
				MessageFollowService.class);

		pendingIntent = PendingIntent.getService(ValidateScreenActivity.this,
				0, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();

		calendar.setTimeInMillis(System.currentTimeMillis());

		calendar.add(Calendar.SECOND, delayTime);

		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pendingIntent);
	}
}
