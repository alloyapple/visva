package vn.com.shoppie.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import com.google.android.gcm.GCMRegistrar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.bluetooth.BluetoothAdapter;
import android.location.Location;
import android.location.LocationListener;
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
import vn.com.shoppie.database.sobject.UserInfo;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.util.SUtil;
import vn.com.shoppie.view.MyTextView;
import vn.com.shoppie.webconfig.WebServiceConfig;

@SuppressLint("SimpleDateFormat")
public class ActivityChangeUserInfo extends Activity implements
		LocationListener {
	private DatePickerDialog datePicker;
	private ShoppieSharePref mShopieSharePref;
	private int mChangedInfoTimes;
	private String mUserName;
	private String mUserPhone;
	private String mUserAddress;
	private String mUserBirth;
	private String mUserEmail;
	private int mUserGender;
	private String lat;
	private String lng;
	private String blueMac;
	private String emeil;
	private MyTextView mTxtTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mShopieSharePref = new ShoppieSharePref(this);
		mChangedInfoTimes = mShopieSharePref.getCoutnChangeInfoTime();
		setContentView(R.layout.page_change_user_info);
		mTxtTitle = (MyTextView) findViewById(R.id.main_personal_title);
		mTxtTitle.setLight();
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
					mUserGender = gender.getSelectedItemPosition();
					/** save user info */
					mUserAddress = address.getText().toString();
					mUserBirth = birth.getText().toString();
					mUserEmail = email.getText().toString();
					mUserName = name.getText().toString();
					mUserPhone = phone.getText().toString();
					mShopieSharePref.setCustName(mUserName);
					mShopieSharePref.setEmail(mUserEmail);
					mShopieSharePref.setPhone(mUserPhone);
					mShopieSharePref.setCustAddress(mUserAddress);
					mShopieSharePref.setBirthDay(mUserBirth);
					mShopieSharePref.setGender(mUserGender);
					mShopieSharePref
							.setCountChangeInfoTime(mChangedInfoTimes + 1);
					lat = ActivityShoppie.myLocation.latitude + "";
					lng = ActivityShoppie.myLocation.longitude + "";
					blueMac = SUtil.getInstance().getBluetoothAddress(
							ActivityChangeUserInfo.this, false);
					emeil = SUtil.getInstance().getDeviceId(
							getApplicationContext());
					updateUserInfo("" + mShopieSharePref.getCustId(),
							mUserName, mUserEmail, mUserPhone, mUserBirth,
							mUserGender, emeil, lat, lng, mUserAddress);
					finish();
				}
			}
		});
	}

	private void updateUserInfo(String custId, String custName,
			String custEmail, String custPhone, String birthday, int gender,
			String deviceId, String latitude, String longtitude,
			String custAddress) {
		// TODO Auto-generated method stub
		GCMRegistrar.setRegisteredOnServer(this, true);
		String _gender;
		if (gender == 0)
			_gender = "Male";
		else
			_gender = "Female";

		BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mAdapter != null) {
			blueMac = mAdapter.getAddress();
			while (!mAdapter.isEnabled() || blueMac == null
					|| blueMac.equals("")) {

				mAdapter.enable();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				blueMac = SUtil.getInstance().getBluetoothAddress(
						ActivityChangeUserInfo.this, false);
			}
		}
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.updateUserInfo(
				custId, custName, custEmail, custPhone, birthday, _gender,
				blueMac, deviceId, latitude, longtitude, custAddress);
		AsyncHttpPost postUpdateStt = new AsyncHttpPost(
				ActivityChangeUserInfo.this, new AsyncHttpResponseProcess(
						ActivityChangeUserInfo.this) {
					@Override
					public void processIfResponseSuccess(String response) {
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
						finish();
					}
				}, nameValuePairs, true);
		postUpdateStt.execute(WebServiceConfig.URL_UPDATE_INFO_USER);

	}

	private void showToast(String string) {
		Toast.makeText(ActivityChangeUserInfo.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public void onClickBackBtn(View v) {
		finish();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
