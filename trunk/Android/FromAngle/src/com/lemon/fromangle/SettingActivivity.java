package com.lemon.fromangle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
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
import com.lemon.fromangle.utility.DialogUtility;
import com.lemon.fromangle.utility.EmailValidator;
import com.lemon.fromangle.utility.StringUtility;


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
	private String uriRingtune;
	private ArrayList<String> listDaysAfter;
	private FromAngleSharedPref mFromAngleSharedPref;
	private EmailValidator mEmailValidator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_setting);

		mFromAngleSharedPref = new FromAngleSharedPref(this);
		mEmailValidator = new EmailValidator();
		initUI();
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
		Log.d(null, test, null);

		String[] from = { mCursor2
				.getColumnName(RingtoneManager.TITLE_COLUMN_INDEX) };
		final String[] listUri = { mCursor2
				.getColumnName(RingtoneManager.URI_COLUMN_INDEX) };

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
						int pos = spnSelectRingTune.getSelectedItemPosition();
						// uriRingtune = listUri[pos];
						Toast.makeText(SettingActivivity.this, pos + "",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
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
			if (!checkValidateField()) {
				String email = txtEmail.getText().toString();
				if (checkValidateEmail(email))
					onSaveClick();
				else
					DialogUtility.alert(SettingActivivity.this,
							getString(R.string.email_not_validate));
			} else

				Toast.makeText(SettingActivivity.this,
						R.string.plz_input_required_field, Toast.LENGTH_LONG)
						.show();
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
		GlobalValue.prefs.setVibrateMode(chkVibrate.isChecked());
		GlobalValue.prefs.setRingTuneFile(uriRingtune);
		GlobalValue.prefs.setUserName(txtName.getText().toString());
		GlobalValue.prefs.setEmail(txtEmail.getText().toString());
		GlobalValue.prefs.setPhone(txtTel.getText().toString());

		String userName = txtName.getText().toString();
		String tel = txtTel.getText().toString();
		String email = txtEmail.getText().toString();
		String days = txtDateSetting.getText().toString();
		String times = txtTimeSetting.getText().toString();
		String daysAfter = txtDayAfter.getText().toString();

		List<NameValuePair> params = ParameterFactory
				.createRegisterSettingParam(userName, tel, email, days, times,
						daysAfter);
		AsyncHttpPost postRegister = new AsyncHttpPost(SettingActivivity.this,
				new AsyncHttpResponseProcess(SettingActivivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("string reponse", "string reponse " + response);
						/* check info response from server */
						checkInfoReponseFromServer(response);
					}

					@Override
					public void processIfResponseFail() {
						// TODO Auto-generated method stub
						Log.e("failed ", "failed");
					}
				}, params, true);
		postRegister.execute(WebServiceConfig.URL_REGISTER_SETTING);
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
		String userName = null;
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
						mFromAngleSharedPref.setUserName(userName);
						mFromAngleSharedPref.setUserId(userId);
						/* clear all field */
						resetAllField();
						showToast("Sucessfully");
					}
				} else if (error == GlobalValue.MSG_REPONSE_FAILED) {
					DialogUtility.alert(SettingActivivity.this,
							getString(R.string.duplicated_email));
				} else
					DialogUtility.alert(SettingActivivity.this,
							getString(R.string.failed_to_conect_server));
			}
			Log.e("id usser", "jsonobJect " + userId);
		} catch (JSONException e) {
			e.printStackTrace();
			DialogUtility.alert(SettingActivivity.this,
					getString(R.string.failed_to_conect_server));
		}
	}

	private void resetAllField() {
		// TODO Auto-generated method stub
		txtDateSetting.setText("");
		txtDayAfter.setText("");
		txtEmail.setText("");
		txtName.setText("");
		txtTel.setText("");
		txtTimeSetting.setText("");
		chkVibrate.setChecked(false);
	}

	OnTouchListener showTimePicker = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int hourStr = 10;
				int minuteStr = 0;
				// if (!StringUtility.isEmpty(txtDateSetting)) {
				// hourStr = Integer.parseInt(txtTimeSetting.getText()
				// .toString().substring(0, 2));
				// minuteStr = Integer.parseInt(txtTimeSetting.getText()
				// .toString().substring(2, 4));
				// }

				timePicker = new TimePickerDialog(SettingActivivity.this,
						new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								String hourStr = hourOfDay < 10 ? hourOfDay
										+ "0" : hourOfDay + "";
								String minuteStr = minute < 10 ? minute + "0"
										: minute + "";
								txtTimeSetting.setText(hourOfDay + "/"
										+ minuteStr);

							}
						}, hourStr, minuteStr, true);
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

		int selectIndex = 7;
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
//			DialogUtility.alert(SettingActivivity.this, "Which : " + which
//					+ " : " + selectedPosition);
			txtDayAfter.setText(listDaysAfter.get(selectedPosition));

		}
	};

	OnTouchListener showDatePicker = new OnTouchListener() {

		@SuppressWarnings("deprecation")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// TODO Auto-generated method stub
				final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
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
}
