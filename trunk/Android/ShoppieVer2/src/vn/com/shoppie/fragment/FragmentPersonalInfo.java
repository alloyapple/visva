package vn.com.shoppie.fragment;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.ShopieSharePref;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class FragmentPersonalInfo extends FragmentBasic {
	// =============================Constant Define=====================
	// ============================Control Define =====================
	private EditText txtName;
	private EditText txtEmail;
	private EditText txtPhone;
	private EditText txtBirth;
	private EditText txtAddress;
	private Spinner spinGender;
	private Button btnChangeUserInfo;
	// ============================Class Define =======================
	private ShopieSharePref mShopieSharePref;
	// ============================Variable Define =====================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_personal_info_fragment, null);

		txtName = (EditText) root.findViewById(R.id.activity_register_edt_name);
		txtAddress = (EditText) root
				.findViewById(R.id.txt_personal_register_address);
		txtBirth = (EditText) root.findViewById(R.id.txt_personal_register_birth);
		txtEmail = (EditText) root.findViewById(R.id.txt_personal_register_email);
		txtPhone = (EditText) root.findViewById(R.id.txt_personal_register_phone);
		spinGender = (Spinner) root
				.findViewById(R.id.spin_personal_register_gender);
		btnChangeUserInfo = (Button)root.findViewById(R.id.btn_change_user_info);
		btnChangeUserInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					if ("".equals(txtName.getText().toString()))
						showToast(getActivity().getString(R.string.leck_name));
					else if ("".equals(txtPhone.getText().toString()))
						showToast(getActivity().getString(R.string.leck_phone));
					else if ("".equals(txtAddress.getText().toString()))
						showToast(getActivity().getString(R.string.leck_address));
					else {
						mShopieSharePref.setCustName(txtName.getText().toString());
						mShopieSharePref.setCustAddress(txtAddress.getText().toString());
						mShopieSharePref.setBirthDay(txtBirth.getText().toString());
						mShopieSharePref.setEmail(txtEmail.getText().toString());
						mShopieSharePref.setGender(spinGender.getSelectedItemPosition());
						mShopieSharePref.setPhone(txtPhone.getText().toString());
					}
			}
		});

		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.gender_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// // Apply the adapter to the spinner
		spinGender.setAdapter(adapter);
		txtAddress.setText(mShopieSharePref.getCustAddress());
		txtBirth.setText(mShopieSharePref.getBirthDay());
		txtEmail.setText(mShopieSharePref.getEmail());
		txtName.setText(mShopieSharePref.getCustName());
		txtPhone.setText(mShopieSharePref.getPhone());
		int gender = mShopieSharePref.getGender();
		if (gender == 0)
			spinGender.setSelection(0);
		else
			spinGender.setSelection(1);
		spinGender.setOnItemSelectedListener(new OnItemSelectedListener() {

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
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mShopieSharePref = new ShopieSharePref(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	private void showToast(String string){
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
	}
}
