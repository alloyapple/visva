package com.lemon.fromangle;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;
import com.lemon.fromangle.config.WebServiceConfig;
import com.lemon.fromangle.network.AsyncHttpPost;
import com.lemon.fromangle.network.AsyncHttpResponseProcess;
import com.lemon.fromangle.network.ParameterFactory;
import com.lemon.fromangle.network.ParserUtility;
import com.lemon.fromangle.service.MessageFollowService;
import com.lemon.fromangle.utility.DialogUtility;
import com.lemon.fromangle.utility.EmailValidator;
import com.lemon.fromangle.utility.StringUtility;

@SuppressLint("SimpleDateFormat")
public class SettingActivivity extends Activity {
	private EditText txtName;
	private EditText txtEmail;
	private EditText txtTel;
	private EditText txtDateSetting;
	private EditText txtTimeSetting;
	private EditText txtDayAfter;
	private Spinner spnSelectRingTune;
	private CheckBox chkVibrate;
	private com.lemon.fromangle.utility.AutoBGButton btnSave, btnLeft,
			btnRight;
	private com.lemon.fromangle.utility.AutoBGButton btnCancel;

	private TimePickerDialog timePicker;
	private DatePickerDialog datePicker;
	private ArrayList<String> listDaysAfter;
	private FromAngleSharedPref mFromAngleSharedPref;
	private EmailValidator mEmailValidator;

	private String userName, userId, tel, email, date, time, daysafter,
			uriRingtune;
	private boolean isVibrate = false;
	private Uri[] mListUriRingTone;

	private MediaPlayer mMediaPlayer;

	private PendingIntent pendingIntent;

	public Handler mHandler = new Handler();
	public boolean checkRing = false, checkVibrate = false;

	private boolean isFirstTime = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_setting);
		mEmailValidator = new EmailValidator();

		/* inti ui */
		initUI();

		/* check is update or register */
		checkIsUpdateOrRegister();

	}

	private void checkIsUpdateOrRegister() {
		String filePath = null;
		mFromAngleSharedPref = new FromAngleSharedPref(this);
		userId = mFromAngleSharedPref.getUserId();
		if (userId != null && !"".equals(userId)) {
			userName = mFromAngleSharedPref.getUserName();
			email = mFromAngleSharedPref.getEmail();
			tel = mFromAngleSharedPref.getPhone();
			date = mFromAngleSharedPref.getValidationDate();
			daysafter = mFromAngleSharedPref.getValidationDaysAfter();
			time = mFromAngleSharedPref.getValidationTime();
			uriRingtune = mFromAngleSharedPref.getRingTuneFile();
			isVibrate = mFromAngleSharedPref.getVibrateMode();

			txtDateSetting.setText(date);
			txtDayAfter.setText(daysafter);
			txtEmail.setText(email);
			txtName.setText(userName);
			txtTel.setText(tel);
			txtTimeSetting.setText(time);
			chkVibrate.setChecked(isVibrate);

			mMediaPlayer = new MediaPlayer();
			for (int i = 0; i < mListUriRingTone.length; i++) {
				if (uriRingtune.equals(mListUriRingTone[i].toString())) {
					spnSelectRingTune.setSelection(i);
					filePath = convertMediaUriToPath(mListUriRingTone[i]);
				}
			}
			try {
				mMediaPlayer.setDataSource(filePath);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mMediaPlayer.start();
		}
	}

	protected String convertMediaUriToPath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();
		return path;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		checkRing = false;
		checkVibrate = false;
		super.onRestart();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		checkRing = false;
		checkVibrate = false;
		super.onStart();
	}

	private void initUI() {
		txtName = (EditText) findViewById(R.id.txtName);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtTel = (EditText) findViewById(R.id.txtTel);
		txtDateSetting = (EditText) findViewById(R.id.txtDateSetting);
		txtTimeSetting = (EditText) findViewById(R.id.txtTimeSetting);
		txtDayAfter = (EditText) findViewById(R.id.txtDayAfter);
		spnSelectRingTune = (Spinner) findViewById(R.id.spnSelectRingTune);
		chkVibrate = (CheckBox) findViewById(R.id.chkVibrate);
		chkVibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (!checkVibrate) {
					checkVibrate = true;
					return;
				}

				if (chkVibrate.isChecked()) {
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					// Vibrate for 500 milliseconds
					v.vibrate(1000);
				}
			}
		});
		btnSave = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnSave);
		btnCancel = (com.lemon.fromangle.utility.AutoBGButton) findViewById(R.id.btnCancel);
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
		btnCancel.setOnClickListener(onCancelClick);
		btnSave.setOnClickListener(onSaveClick);

		txtDateSetting.setOnTouchListener(showDatePicker);

		txtTimeSetting.setOnTouchListener(showTimePicker);

		txtDayAfter.setOnTouchListener(showDialogSelectDateAfter);

		txtDayAfter.setText("7");

		initSpinnerDaysAfter();
		initSpinnerRingtune();

	}

	@SuppressWarnings("deprecation")
	private void initSpinnerRingtune() {
		RingtoneManager mRingtoneManager2 = new RingtoneManager(this); // adds
		// ringtonemanager
		mRingtoneManager2.setType(RingtoneManager.TYPE_RINGTONE); // sets the
		// type to
		// ringtones
		mRingtoneManager2.setIncludeDrm(true); // get list of ringtones to
		// include DRM

		Cursor mCursor2 = mRingtoneManager2.getCursor(); // appends my cursor to
		// the
		// ringtonemanager
		startManagingCursor(mCursor2); // starts the cursor query

		// prints output for diagnostics
		String test = mCursor2.getString(mCursor2
				.getColumnIndexOrThrow(RingtoneManager.EXTRA_RINGTONE_TITLE));

		/* check list uri of cursor */
		mListUriRingTone = checkListUri(mRingtoneManager2, mCursor2);
		String[] from = { mCursor2
				.getColumnName(RingtoneManager.TITLE_COLUMN_INDEX) };

		int[] to = { android.R.id.text1 };

		// create simple cursor adapter
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_spinner_item, mCursor2, from, to);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// get reference to our spinner
		spnSelectRingTune.setAdapter(adapter);
		spnSelectRingTune
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						if (!checkRing) {
							checkRing = true;
							return;
						}
						int pos = spnSelectRingTune.getSelectedItemPosition();
						uriRingtune = mListUriRingTone[pos].toString();
						Uri uri = Uri.parse(uriRingtune);
						final Ringtone r = RingtoneManager.getRingtone(
								getApplicationContext(), uri);
						r.play();
						mHandler.postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								r.stop();

							}
						}, 3000);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	private Uri[] checkListUri(RingtoneManager mRingTone, Cursor mCursor2) {
		int alarmsCount = mCursor2.getCount();
		if (alarmsCount == 0 && !mCursor2.moveToFirst()) {
			return null;
		}
		Uri[] alarms = new Uri[alarmsCount];
		while (!mCursor2.isAfterLast() && mCursor2.moveToNext()) {
			int currentPosition = mCursor2.getPosition();
			alarms[currentPosition] = mRingTone.getRingtoneUri(currentPosition);
		}
		return alarms;
	}

	private void initSpinnerDaysAfter() {
		listDaysAfter = new ArrayList<String>();
		for (int i = 1; i < 100; i++) {
			listDaysAfter.add(i + "");
		}

	}

	OnClickListener onCancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();

		}
	};
	OnClickListener onSaveClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (checkValidateField()) {
				if (StringUtility.isEmpty(txtName))
					txtName.setError(getSpanError(getString(R.string.error_name)));
				if (StringUtility.isEmpty(txtEmail))
					txtEmail.setError(getSpanError(getString(R.string.error_email)));
				if (StringUtility.isEmpty(txtTel))
					txtTel.setError(getSpanError(getString(R.string.error_phone)));
				Toast.makeText(SettingActivivity.this,
						R.string.plz_input_required_field, Toast.LENGTH_LONG)
						.show();
			} else {
				String email = txtEmail.getText().toString();
				if (checkValidateEmail(email))
					onSaveClick();
				else {
					txtEmail.setError(getSpanError(getString(R.string.error_email)));
					Toast.makeText(SettingActivivity.this,
							R.string.email_not_validate, Toast.LENGTH_LONG)
							.show();
				}
				// DialogUtility.creatDialog(SettingActivivity.this,
				// getString(R.string.email_not_validate), null)
				// .show();
			}
		}
	};

	private boolean checkValidateEmail(String email) {
		// TODO Auto-generated method stub
		if (mEmailValidator.validate(email))
			return true;
		else
			return false;
	}

	private void onSaveClick() {

		String userName = txtName.getText().toString();
		String tel = txtTel.getText().toString();
		String email = txtEmail.getText().toString();
		String days = txtDateSetting.getText().toString();
		String times = txtTimeSetting.getText().toString();
		String daysAfter = txtDayAfter.getText().toString();
		String userId = mFromAngleSharedPref.getUserId();
		List<NameValuePair> params = null;
		if (userId != null && !StringUtility.isEmpty(userId)) {
			params = ParameterFactory.createUpdateSettingParam(userId,
					userName, tel, email, days, times, daysAfter);
		} else
			params = ParameterFactory.createRegisterSettingParam(userName, tel,
					email, days, times, daysAfter);
		AsyncHttpPost postRegister = new AsyncHttpPost(SettingActivivity.this,
				new AsyncHttpResponseProcess(SettingActivivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						if (StringUtility
								.isEmpty(SettingActivivity.this.userId)) {
							checkInfoReponseFromServer(response);
							mFromAngleSharedPref.setFirstTimeSetting(true);
							isFirstTime = true;
						} else {
							checkInfoUserUpdate(response);
							isFirstTime = false;
							mFromAngleSharedPref.setFirstTimeSetting(false);
						}
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, params, true);
		if (userId != null && !StringUtility.isEmpty(userId))
			postRegister.execute(WebServiceConfig.URL_UPDATE_REGISTER_SETTING);
		else
			postRegister.execute(WebServiceConfig.URL_REGISTER_SETTING);
	}

	/**
	 * check update info
	 * 
	 * @param response
	 */
	private void checkInfoUserUpdate(String response) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		String errorMsg = null;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject != null && jsonObject.length() > 0) {
				errorMsg = ParserUtility.getStringValue(jsonObject,
						GlobalValue.PARAM_ERROR);
				int error = Integer.parseInt(errorMsg);
				if (error == GlobalValue.MSG_RESPONSE_UPDATE_INFO_SUCESS) {
					showToast(getString(R.string.change_info_sucess));
					addDataToPreference();
					startRunAlarmManager();
				} else if (error == GlobalValue.MSG_RESPONSE_UPDATE_INFO_FAILED) {
					showToast(getString(R.string.duplicated_email));
				} else
					showToast(getString(R.string.failed_to_conect_server));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			DialogUtility.alert(SettingActivivity.this,
					getString(R.string.failed_to_conect_server));
		}
	}

	/**
	 * check validate info response from server
	 * 
	 * @param response
	 */
	private void checkInfoReponseFromServer(String response) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = null;
		JSONObject jsonId = null;
		String paramData = null;
		String userId = null;
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
						userId = ParserUtility.getStringValue(jsonId,
								GlobalValue.PARAM_USER_ID);
						userName = ParserUtility.getStringValue(jsonId,
								GlobalValue.PARAM_USER_NAME);
					}
					if (userId != null) {
						/* add to preference */
						mFromAngleSharedPref.setUserId(userId);

						addDataToPreference();

						/* start run alarmmanager */
						startRunAlarmManager();

						showToast("Sucessfully");
					}
				} else if (error == GlobalValue.MSG_REPONSE_FAILED) {
					showToast(getString(R.string.duplicated_email));
				} else
					DialogUtility.alert(SettingActivivity.this,
							getString(R.string.failed_to_conect_server));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			DialogUtility.alert(SettingActivivity.this,
					getString(R.string.failed_to_conect_server));
		}
	}

	private void startRunAlarmManager() {
		Log.e("stgart run alarm", "start alarm");
		Date date1 = new Date();
		String dateStr = txtDateSetting.getText().toString();
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date1 = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long timeOfDate = date1.getTime();
		String timeStr[] = txtTimeSetting.getText().toString().split(":");
		int hour = Integer.parseInt(timeStr[0]);
		int minute = Integer.parseInt(timeStr[1]);
		long timeOfClock = hour * 3600 + minute * 60;
		long totalDelayTime = timeOfDate + timeOfClock * 1000;
		long currenttime = System.currentTimeMillis();
		int delayTime = (int) (totalDelayTime - currenttime);
		if (delayTime > 0) {
			int timeDelay = delayTime / 1000;
			Log.e("delay time", "delay time " + delayTime);
			Intent myIntent = new Intent(SettingActivivity.this,
					MessageFollowService.class);
			pendingIntent = PendingIntent.getService(SettingActivivity.this, 0,
					myIntent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, timeDelay);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pendingIntent);
		}
	}

	private void addDataToPreference() {
		mFromAngleSharedPref.setVibrateMode(chkVibrate.isChecked());
		mFromAngleSharedPref.setRingTuneFile(uriRingtune);
		mFromAngleSharedPref.setUserName(txtName.getText().toString());
		mFromAngleSharedPref.setEmail(txtEmail.getText().toString());
		mFromAngleSharedPref.setPhone(txtTel.getText().toString());
		mFromAngleSharedPref
				.setValidationDate(txtDateSetting.getText().toString());
		mFromAngleSharedPref.setValidationDaysAfter(txtDayAfter.getText()
				.toString());
		mFromAngleSharedPref
				.setValidationTime(txtTimeSetting.getText().toString());
		mFromAngleSharedPref.setRingTuneFile(uriRingtune);
		if (isFirstTime) {
			mFromAngleSharedPref.setTopScreenFinalValidation("----------");
		}
		String dateSetByUserStr = txtDateSetting.getText().toString() + " "
				+ txtTimeSetting.getText().toString();
		Date dateSetByUser = new Date();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateSetByUser = dateFormat.parse(dateSetByUserStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String timeClockStr[] = txtTimeSetting.getText().toString().split(":");
		int hour = Integer.parseInt(timeClockStr[0]);
		int minute = Integer.parseInt(timeClockStr[1]);
		long timeClock = hour * 3600 + minute * 60;
		long timeCompare = dateSetByUser.getTime() + timeClock * 1000;
		long currentTime = System.currentTimeMillis();
		Log.e("timeCompare " + timeCompare + "  currentTime " + currentTime,
				"he heh " + (timeCompare - currentTime));
		if (timeCompare - currentTime > 0) {
			mFromAngleSharedPref.setTopScreenNextValidation(dateSetByUserStr);
		} else {
			String dateStr = txtDateSetting.getText().toString();

			// Date date1 = new Date(txtDateSetting.getText().toString());
			Date date1 = new Date();
			int daysAfter = Integer.parseInt(txtDayAfter.getText().toString());
			final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date1 = df.parse(dateStr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Date nextValidationDate = addDaysToDate(date1, daysAfter);
			String nextValidationDateStr;
			nextValidationDateStr = df.format(nextValidationDate);

			mFromAngleSharedPref.setTopScreenNextValidation(nextValidationDateStr
					+ " " + txtTimeSetting.getText().toString());
		}
	}

	public static Date addDaysToDate(Date input, int numberDay) {

		Calendar defaulCalender = Calendar.getInstance();
		defaulCalender.setTime(input);
		defaulCalender.add(Calendar.DATE, numberDay);
		Date resultdate = new Date(defaulCalender.getTimeInMillis());
		return resultdate;
	}

	OnTouchListener showTimePicker = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				String timeStr = mFromAngleSharedPref.getValidationTime();
				int hour,min;
				if(StringUtility.isEmpty(timeStr)){
					Calendar cal = Calendar.getInstance();
					 hour = cal.get(Calendar.HOUR_OF_DAY);
					 min = cal.get(Calendar.MINUTE);
				}else{
					String timeArrStr[] = timeStr.split(":");
					hour = Integer.parseInt(timeArrStr[0]);
					min = Integer.parseInt(timeArrStr[1]);
				}

				timePicker = new TimePickerDialog(SettingActivivity.this,
						new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								String hourStr = hourOfDay < 10 ? hourOfDay
										+ "0" : hourOfDay + "";
								String minuteStr = minute < 10 ? "0" + minute
										: minute + "";
								txtTimeSetting.setText(hourOfDay + ":"
										+ minuteStr);

							}
						}, hour, min, true);
				timePicker.show();
			}
			return false;
		}
	};
	OnTouchListener showDialogSelectDateAfter = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN)

				showDialogSelectDateAfter();
			return false;
		}
	};

	private void showDialogSelectDateAfter() {

		int selectIndex = 6;
		if (!txtDayAfter.getText().toString().equalsIgnoreCase("")) {
			selectIndex = listDaysAfter.indexOf(txtDayAfter.getText()
					.toString());
		}
		DialogUtility.showSimpleOptionDialog(SettingActivivity.this,
				R.string.app_name,
				listDaysAfter.toArray(new String[listDaysAfter.size()]),
				getString(R.string.btn_ok), selectIndex, onSelectGenre);

	}

	DialogInterface.OnClickListener onSelectGenre = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

			int selectedPosition = ((AlertDialog) dialog).getListView()
					.getCheckedItemPosition();
			// DialogUtility.alert(SettingActivivity.this, "Which : " + which
			// + " : " + selectedPosition);
			txtDayAfter.setText(listDaysAfter.get(selectedPosition));

		}
	};

	OnTouchListener showDatePicker = new OnTouchListener() {

		@SuppressWarnings("deprecation")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// TODO Auto-generated method stub
				final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dateCurrent = new Date();
				try {
					dateCurrent = df.parse(txtDateSetting.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					dateCurrent = new Date();
					e.printStackTrace();
				}

				datePicker = new DatePickerDialog(SettingActivivity.this,
						new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								Date d = new Date(year - 1900, monthOfYear,
										dayOfMonth);
								txtDateSetting.setText(df.format(d));
							}
						}, dateCurrent.getYear() + 1900,
						dateCurrent.getMonth(), dateCurrent.getDate());
				datePicker.show();
			}
			return false;
		}
	};

	private boolean checkValidateField() {
		return (StringUtility.isEmpty(txtName)
				|| StringUtility.isEmpty(txtEmail)
				|| StringUtility.isEmpty(txtTel)
				|| StringUtility.isEmpty(txtDateSetting)
				|| StringUtility.isEmpty(txtTimeSetting) || StringUtility
				.isEmpty(txtDayAfter));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case GlobalValue.DIALOG_FAILED_TO_CONNECT_SERVER:
			builder.setMessage(getString(R.string.failed_to_conect_server));
			builder.setPositiveButton(getString(R.string.btn_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}
					});
			return builder.create();

		default:
			return null;
		}
	}

	private void showToast(String string) {
		Toast.makeText(SettingActivivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public SpannableStringBuilder getSpanError(String s) {
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.BLACK);
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(s);
		ssbuilder.setSpan(fgcspan, 0, s.length(), 0);
		return ssbuilder;
	}
}
