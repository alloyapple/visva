package com.lemon.fromangle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;
import com.lemon.fromangle.config.WebServiceConfig;
import com.lemon.fromangle.network.AsyncHttpPost;
import com.lemon.fromangle.network.AsyncHttpResponseProcess;
import com.lemon.fromangle.network.NetworkUtility;
import com.lemon.fromangle.network.ParameterFactory;
import com.lemon.fromangle.network.ParserUtility;
import com.lemon.fromangle.service.MessageFollowService;

@SuppressLint("SimpleDateFormat")
public class SplashActivity extends LemonBaseActivity {

	private static int TIME_SHOW_SPLASH = 3000;
	private boolean isTouch = false;
	private String device_id;
	private String userName;
	private String user_id;
	private String mail;
	private String tel;
	private String date;
	private String time;
	private String after_date;
	private FromAngleSharedPref pref;
	private PendingIntent pendingIntent;
	private String txtUserName1 = "", txtUserName2 = "", txtUserName3 = "",
			txtEmail1 = "", txtEmail2 = "", txtEmail3 = "", txtTel1 = "",
			txtTel2 = "", txtTel3 = "", txtMessage1 = "", txtMessage2 = "",
			txtMessage3 = "", message = "", status = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_splash);

		device_id = Secure.getString(
				this.getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		Log.i("Device_id", device_id);
		pref = new FromAngleSharedPref(this);
		if ("".equals(pref.getUserId())
				&& NetworkUtility.getInstance(this).isNetworkAvailable())
			checkUserExist();
		else {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					if (!isTouch) {
						gotoActivity(self, TopScreenActivity.class);
						finish();
					}
				}
			}, TIME_SHOW_SPLASH);
		}
	}

	public void checkUserExist() {
		List<NameValuePair> params = ParameterFactory.checkUserExist(device_id);
		AsyncHttpPost postCheckUserExist = new AsyncHttpPost(this,
				new AsyncHttpResponseProcess(this) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						checkInfoReponseFromServer(response);
						gotoActivity(self, TopScreenActivity.class);
						finish();
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, params, true);
		postCheckUserExist.execute(WebServiceConfig.URL_CHECK_USER_EXIT);
	}

	public void checkInfoReponseFromServer(String response) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		JSONObject jsonId = null;
		String paramData = null;
		JSONObject jsonObjectMessageSetting = null;

		String errorMsg = null;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject != null && jsonObject.length() > 0) {
				errorMsg = ParserUtility.getStringValue(jsonObject,
						GlobalValue.PARAM_ERROR);

				int error = Integer.parseInt(errorMsg);
				if (error == GlobalValue.MSG_REPONSE_SUCESS) {
					paramData = ParserUtility.getStringValue(jsonObject,
							GlobalValue.PARAM_DATA);
					if (paramData != null) {
						jsonId = new JSONObject(paramData);
						user_id = ParserUtility.getStringValue(jsonId,
								"user_id");
						userName = ParserUtility.getStringValue(jsonId,
								GlobalValue.PARAM_USER_NAME);

						mail = ParserUtility.getStringValue(jsonId, "mail");
						tel = ParserUtility.getStringValue(jsonId, "tel");
						date = ParserUtility.getStringValue(jsonId, "day");
						String dateSplit[] = date.split("-");
						if (dateSplit.length > 0) {
							date = dateSplit[0] + "/" + dateSplit[1] + "/"
									+ dateSplit[2];
						}
						time = ParserUtility.getStringValue(jsonId, "time");
						after_date = ParserUtility.getStringValue(jsonId,
								"days_after");

						savePreference();
						// startRunAlarmManager();
					}

				} else if (error == GlobalValue.MSG_CHECK_USER_EXIST) {
					// showToast(getString(R.string.duplicated_email));
					Log.i("failed", "failed");
				} else {
					// DialogUtility.alert(this,
					// getString(R.string.failed_to_conect_server));
				}

				/* parse in message setting */
				message = ParserUtility.getStringValue(jsonObject, "message");

				Log.e("mesage", "mesage" + message);
				if (!"false".equalsIgnoreCase(message) && !"[]".equals(message)) {
					jsonObjectMessageSetting = new JSONObject(message);
					if (jsonObjectMessageSetting != null
							&& jsonObjectMessageSetting.length() > 0) {
						txtUserName1 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "receiver_1");
						txtEmail1 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "mail_1");
						txtMessage1 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "message_1");

						txtUserName2 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "receiver_2");
						txtEmail2 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "mail_2");
						txtMessage2 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "message_2");

						txtUserName3 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "receiver_3");
						txtEmail3 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "mail_3");
						txtMessage3 = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "message_3");

						status = ParserUtility.getStringValue(
								jsonObjectMessageSetting, "status");
						int statusMode = Integer.parseInt(status);
						if (statusMode == GlobalValue.MSG_RESPONSE_MSG_SETTING_SUCESS) {
							startRunAlarmManager();
							pref.setKeyRunAlarm(true);
						} else{
							stopAlarmManager();
							pref.setKeyRunAlarm(false);
						}
						Log.e("user " + status, "useraaa " + txtUserName1);
						saveInputPref();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			// DialogUtility.alert(this,
			// getString(R.string.failed_to_conect_server));
		}
	}

	private void savePreference() {
		String[] times = time.split(":");
		time = times[0] + ":" + times[1];
		Log.i("date", "add " + date);
		Log.i("time", time);
		Log.i("user_id", user_id);
		
		pref.setUserName(userName);
		pref.setValidationDate(date);
		pref.setFirstTimeSetting(true);
		pref.setUserId(user_id);
		pref.setValidationTime(time);
		pref.setPhone(tel);
		pref.setValidationDaysAfter(after_date);
		pref.setEmail(mail);

		String dateSetByUserStr = date + " " + time;
		Log.e("date by user", "date by user " + dateSetByUserStr);
		Date dateSetByUser = new Date();
		pref.setTopScreenFinalValidation("----------");
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		try {
			dateSetByUser = dateFormat.parse(dateSetByUserStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String timeClockStr[] = time.split(":");
		int hour = Integer.parseInt(timeClockStr[0]);
		int minute = Integer.parseInt(timeClockStr[1]);
		long timeClock = hour * 3600 + minute * 60;
		long timeCompare = dateSetByUser.getTime() + timeClock * 1000;
		long currentTime = System.currentTimeMillis();
		Log.e("timeCompare " + timeCompare + "  currentTime " + currentTime,
				"he heh " + (timeCompare - currentTime));
		if (timeCompare - currentTime > 0) {
			pref.setTopScreenNextValidation(dateSetByUserStr);
		} else {
			Log.e("date", "dateidjf " + dateSetByUserStr);
			Date date1 = new Date();
			int daysAfter = Integer.parseInt(after_date);
			final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			try {
				date1 = df.parse(dateSetByUserStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Date nextValidationDate = addDaysToDate(date1, daysAfter);
			String nextValidationDateStr;
			nextValidationDateStr = df.format(nextValidationDate);
			pref.setTopScreenNextValidation(nextValidationDateStr);
		}
	}

	public static Date addDaysToDate(Date input, int numberDay) {
		Log.e("dateintpu", "date input " + input.toLocaleString());
		Calendar defaulCalender = Calendar.getInstance();
		defaulCalender.setTime(input);
		long time1 = defaulCalender.getTimeInMillis();
		long time2 = numberDay * 60 * 1000;
		long resultTime = time1 + time2;
		defaulCalender.add(Calendar.YEAR, 0);
		defaulCalender.add(Calendar.MINUTE, numberDay);

		Date resultdate = new Date(resultTime);
		return resultdate;
	}

	public void start(View v) {
		isTouch = true;
		gotoActivity(self, TopScreenActivity.class);
		finish();
	}

	private void startRunAlarmManager() {
		Log.e("stgart run alarm", "start alarm");
		Date date1 = new Date();
		String dateStr = date;
		final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		try {
			date1 = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long timeOfDate = date1.getTime();
		String timeStr[] = time.split(":");
		int hour = Integer.parseInt(timeStr[0]);
		int minute = Integer.parseInt(timeStr[1]);
		long timeOfClock = hour * 3600 + minute * 60;
		long totalDelayTime = timeOfDate + timeOfClock * 1000;
		long currenttime = System.currentTimeMillis();
		int delayTime = (int) (totalDelayTime - currenttime);
		if (delayTime > 0) {
			pref.setMessageSettingStatus("1");
			pref.setValidationMode(0);
			int timeDelay = delayTime / 1000;
			Log.e("delay time", "delay time " + delayTime);
			Intent myIntent = new Intent(this, MessageFollowService.class);
			pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, timeDelay);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pendingIntent);
		}
	}

	private void saveInputPref() {
		pref.setMessageSettingTab1(txtUserName1.toString(),
				txtEmail1.toString(), txtTel1.toString(),
				txtMessage1.toString());
		pref.setMessageSettingTab2(txtUserName2.toString(),
				txtEmail2.toString(), txtTel2.toString(),
				txtMessage2.toString());
		pref.setMessageSettingTab3(txtUserName3.toString(),
				txtEmail3.toString(), txtTel3.toString(),
				txtMessage3.toString());
		pref.saveInputMessage(true);

		pref.putAppStatus(status);
	}

	private void stopAlarmManager() {
		// TODO Auto-generated method stub
		// int timeDelay = -5000;
		// Log.e("delay time", "delay time " + timeDelay);
		Intent myIntent = new Intent(SplashActivity.this,
				MessageFollowService.class);
		pendingIntent = PendingIntent.getService(SplashActivity.this, 0,
				myIntent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTimeInMillis(System.currentTimeMillis());
		// calendar.add(Calendar.SECOND, timeDelay);
		// alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
		// pendingIntent);
		alarmManager.cancel(pendingIntent);
	}
	// private void startRunAlarmManager() {
	//
	// Date date1 = new Date();
	// String dateStr = mFromAngleSharedPref.getTopScreenNextValidation();
	// final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	// try {
	// date1 = df.parse(dateStr);
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// long timeOfDate = date1.getTime();
	// long totalDelayTime = timeOfDate;
	// long currenttime = System.currentTimeMillis();
	// long delayTime = totalDelayTime - currenttime;
	// int timeDelay = (int) (delayTime / 1000);
	// Intent myIntent = new Intent(ValidateScreenActivity.this,
	// MessageFollowService.class);
	//
	// pendingIntent = PendingIntent.getService(ValidateScreenActivity.this,
	// 0, myIntent, 0);
	//
	// AlarmManager alarmManager = (AlarmManager)
	// getSystemService(ALARM_SERVICE);
	//
	// Calendar calendar = Calendar.getInstance();
	//
	// calendar.setTimeInMillis(System.currentTimeMillis());
	//
	// calendar.add(Calendar.SECOND, timeDelay);
	//
	// alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
	// pendingIntent);
	//
	// }

}
