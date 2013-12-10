package vn.com.shoppie.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import vn.com.shoppie.R;
import vn.com.shoppie.activity.ActivityWelcome;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.util.SUtilBitmap;
import vn.com.shoppie.util.SUtilText;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterWelcomeImage extends PagerAdapter {
	Context context;
	ArrayList<Integer> data;
	private DatePickerDialog datePicker;

	public AdapterWelcomeImage(Context context, ArrayList<Integer> data) {
		this.context = context;
		this.data = data;
		mShopieSharePref = new ShoppieSharePref(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	static Drawable drb;
	private String friendId = "";
	private ShoppieSharePref mShopieSharePref;

	@Override
	public Object instantiateItem(View container, int position) {
		LayoutInflater inflater = (LayoutInflater) container.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v;
		if (data.get(position) == ActivityWelcome.PAGE_REGISTER) {
			try {
				v = inflater.inflate(R.layout.page_register, null);

			} catch (OutOfMemoryError e) {
				Log.e("out of memory", "wellcome Image Adapter");
				return null;
			}
			final View layoutRegister  = (RelativeLayout)v.findViewById(R.id.layout_register);
			final TextView textIntroductionTip = (TextView)v.findViewById(R.id.text_introduction_tip);
			if(!"".equals(mShopieSharePref.getValueParamMobile())){
				textIntroductionTip.setText(mShopieSharePref.getValueParamMobile());
			}else
				textIntroductionTip.setText(context.getString(R.string.introduct_tip));
			final EditText name = (EditText) v
					.findViewById(R.id.activity_register_edt_name);
			final EditText email = (EditText) v
					.findViewById(R.id.txt_personal_register_email);
			final EditText phone = (EditText) v
					.findViewById(R.id.txt_personal_register_phone);
			final EditText address = (EditText) v
					.findViewById(R.id.txt_personal_register_address);
			final EditText birth = (EditText) v
					.findViewById(R.id.txt_personal_register_birth);
			final Spinner gender = (Spinner) v
					.findViewById(R.id.spin_personal_register_gender);
			final EditText friendCode = (EditText) v
					.findViewById(R.id.introduce_code_edit_text);
			friendCode.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					friendId += s.toString();
					mShopieSharePref.setFriendId(friendId);
				}
			});
			// Create an ArrayAdapter using the string array and a default
			// spinner
			// layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(context, R.array.gender_array,
							android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// // Apply the adapter to the spinner
			gender.setAdapter(adapter);
			gender.setSelection(0);
			gender.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

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
						final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
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
								context,
								new OnDateSetListener() {
									@Override
									public void onDateSet(DatePicker view, int year,
											int monthOfYear, int dayOfMonth) {
										Date d = new Date(year - 1900, monthOfYear,
												dayOfMonth);
										birth.setText(df.format(d).toString());
									}
								}, dateCurrent.getYear() + 1900,
								dateCurrent.getMonth(), dateCurrent.getDate());
						datePicker.show();
					}
					return false;
				}
			});
			name.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					name.removeTextChangedListener(this);
					String txt = SUtilText.removeAccent(s.toString());
					s.clear();
					s.append(txt);
					name.setText(s);
					name.addTextChangedListener(this);
					name.setSelection(name.getText().length());
				}
			});
			final Button btnRegister = (Button) v
					.findViewById(R.id.activity_register_btn_register);
			final Button btnAccpetRegister= (Button)v.findViewById(R.id.btn_register_accept);
			btnRegister.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(layoutRegister.getVisibility() == View.GONE)
						layoutRegister.setVisibility(View.VISIBLE);
					else
						layoutRegister.setVisibility(View.GONE);
				}
			});
			btnAccpetRegister.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (_listener != null) {
						if ("".equals(name.getText().toString()))
							showToast(context.getString(R.string.leck_name));
						else if ("".equals(phone.getText().toString()))
							showToast(context.getString(R.string.leck_phone));
						else if ("".equals(address.getText().toString()))
							showToast(context.getString(R.string.leck_address));
						else {
							int _gender = gender.getSelectedItemPosition();
							_listener.btnRegisterClick(btnRegister, name
									.getText().toString(), email.getText()
									.toString(), phone.getText().toString(),
									address.getText().toString(), _gender,
									birth.getText().toString());
						}
					}
				}
			});
		} else {
			v = inflater.inflate(R.layout.item_welcome, null);
			ImageView imv = (ImageView) v.findViewById(R.id.iv_welcome);
			try {
				int width = context.getResources().getInteger(
						R.integer.display_width);
				int height = context.getResources().getInteger(
						R.integer.display_height);
				imv.setImageBitmap(SUtilBitmap.decodeSampledBitmapFromResource(
						context.getResources(), data.get(position), width,
						height));

			} catch (OutOfMemoryError e) {
				System.gc();
				imv.setImageDrawable(drb);
			}
		}
		
		View indi[] = new View[7];
		indi[0] = (View) v.findViewById(R.id.indicator0);
		indi[1] = (View) v.findViewById(R.id.indicator1);
		indi[2] = (View) v.findViewById(R.id.indicator2);
		indi[3] = (View) v.findViewById(R.id.indicator3);
		indi[4] = (View) v.findViewById(R.id.indicator4);
		indi[5] = (View) v.findViewById(R.id.indicator5);
		indi[6] = (View) v.findViewById(R.id.indicator6);
		indi[0].setBackgroundResource(R.drawable.ic_indicator_off);
		indi[1].setBackgroundResource(R.drawable.ic_indicator_off);
		indi[2].setBackgroundResource(R.drawable.ic_indicator_off);
		indi[3].setBackgroundResource(R.drawable.ic_indicator_off);
		indi[4].setBackgroundResource(R.drawable.ic_indicator_off);
		indi[5].setBackgroundResource(R.drawable.ic_indicator_off);
		indi[6].setBackgroundResource(R.drawable.ic_indicator_off);
		
		indi[position].setBackgroundResource(R.drawable.ic_indicator_on);
		
		((ViewPager) container).addView(v, 0);
		return v;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	OnWelcomeRegisterListener _listener;

	public void setOnRegisterListener(OnWelcomeRegisterListener listener) {
		this._listener = listener;
	}

	public interface OnWelcomeRegisterListener {
		public void btnRegisterClick(View v, String name, String email,
				String phone, String address, int gender, String birth);
	}

	public void updateData(ArrayList<Integer> mData) {
		// TODO Auto-generated method stub
		this.data = mData;
		notifyDataSetChanged();
	}

	private void showToast(String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}

}
