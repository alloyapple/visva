package vn.com.shoppie.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import vn.com.shoppie.R;
import vn.com.shoppie.constant.ShoppieSharePref;

@SuppressLint("SimpleDateFormat")
public class ActivityChangeUserInfo extends Activity {
	private DatePickerDialog datePicker;
	private ShoppieSharePref mShopieSharePref;
	private int mChangedInfoTimes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mShopieSharePref = new ShoppieSharePref(this);
		mChangedInfoTimes = mShopieSharePref.getCoutnChangeInfoTime();
		setContentView(R.layout.page_change_user_info);
		final EditText name = (EditText) findViewById(R.id.activity_register_edt_name);
		final EditText email = (EditText) findViewById(R.id.txt_personal_register_email);
		final EditText phone = (EditText) findViewById(R.id.txt_personal_register_phone);
		final EditText address = (EditText) findViewById(R.id.txt_personal_register_address);
		final EditText birth = (EditText) findViewById(R.id.txt_personal_register_birth);
		final Spinner gender = (Spinner) findViewById(R.id.spin_personal_register_gender);
		// Create an ArrayAdapter using the string array and a default
		// spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.gender_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gender.setAdapter(adapter);

		address.setText(mShopieSharePref.getCustAddress());
		birth.setText(mShopieSharePref.getBirthDay());
		birth.setFocusable(false);
		email.setText(mShopieSharePref.getEmail());
		name.setText(mShopieSharePref.getCustName());
		phone.setText(mShopieSharePref.getPhone());
		int _gender = mShopieSharePref.getGender();
		if (_gender == 0)
			gender.setSelection(0);
		else
			gender.setSelection(1);
		address.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showToast(getString(R.string.address_notice));
				return false;
			}
		});
		phone.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				showToast(getString(R.string.phone_notice));
				return false;
			}
		});
		birth.setOnTouchListener(new View.OnTouchListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// TODO Auto-generated method stub
					Log.e("txtDAte", "date " + birth.toString());
					long currentTime = System.currentTimeMillis();
					Date myDate = new Date(currentTime);
					String myDateStr = "";
					final SimpleDateFormat df = new SimpleDateFormat(
							"dd/MM/yyyy");
					Date dateCurrent = new Date();
					try {
						myDateStr = df.format(myDate);
						dateCurrent = df.parse(myDateStr);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						dateCurrent = new Date();
						e.printStackTrace();
					}

					datePicker = new DatePickerDialog(
							ActivityChangeUserInfo.this,
							new OnDateSetListener() {
								@Override
								public void onDateSet(DatePicker view,
										int year, int monthOfYear,
										int dayOfMonth) {
									Date d = new Date(year - 1900, monthOfYear,
											dayOfMonth);
									birth.setText(df.format(d).toString());
								}
							}, dateCurrent.getYear() + 1900, dateCurrent
									.getMonth(), dateCurrent.getDate());
					datePicker.show();
				}
				return false;
			}
		});
		final Button btnAccpetRegister = (Button) findViewById(R.id.btn_change_user_info);
		btnAccpetRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(name.getText().toString()))
					showToast(getString(R.string.leck_name));
				else if ("".equals(phone.getText().toString()))
					showToast(getString(R.string.leck_phone));
				else if ("".equals(address.getText().toString()))
					showToast(getString(R.string.leck_address));
				else {
					int _gender = gender.getSelectedItemPosition();
					/** save user info */
					mShopieSharePref.setCustName(name.getText().toString());
					mShopieSharePref.setEmail(email.getText().toString());
					mShopieSharePref.setPhone(phone.getText().toString());
					mShopieSharePref.setCustAddress(address.getText()
							.toString());
					mShopieSharePref.setBirthDay(birth.getText().toString());
					mShopieSharePref.setGender(_gender);
					mShopieSharePref
							.setCountChangeInfoTime(mChangedInfoTimes + 1);
					finish();
				}
			}
		});
	}

	private void showToast(String string) {
		Toast.makeText(ActivityChangeUserInfo.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public void onClickBackBtn(View v) {
		finish();
	}
}
