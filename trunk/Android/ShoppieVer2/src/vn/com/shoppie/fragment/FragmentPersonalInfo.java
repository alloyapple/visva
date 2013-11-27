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
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentPersonalInfo extends FragmentBasic {
	// =============================Constant Define=====================
	// ============================Control Define =====================
	private TextView txtName;
	private TextView txtEmail;
	private TextView txtPhone;
	private TextView txtBirth;
	private TextView txtAddress;
	private Spinner spinGender;
	// ============================Class Define =======================
	private ShopieSharePref mShopieSharePref;
	// ============================Variable Define =====================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_personal_info_fragment, null);

		txtName = (TextView) root.findViewById(R.id.txt_personal_detail_name);
		txtAddress = (TextView) root
				.findViewById(R.id.txt_personal_detail_address);
		txtBirth = (TextView) root.findViewById(R.id.txt_personal_detail_birth);
		txtEmail = (TextView) root.findViewById(R.id.txt_personal_detail_email);
		txtPhone = (TextView) root.findViewById(R.id.txt_personal_detail_phone);
		spinGender = (Spinner) root
				.findViewById(R.id.spin_personal_detail_gender);

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
}
