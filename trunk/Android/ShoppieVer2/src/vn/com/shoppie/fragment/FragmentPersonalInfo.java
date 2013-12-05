package vn.com.shoppie.fragment;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.ActivityChangeUserInfo;
import vn.com.shoppie.constant.ShopieSharePref;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	private Button btnChangeUserInfo;
	// ============================Class Define =======================
	private ShopieSharePref mShopieSharePref;

	// ============================Variable Define =====================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_personal_info_fragment, null);

		txtName = (TextView) root.findViewById(R.id.activity_register_edt_name);
		txtAddress = (TextView) root
				.findViewById(R.id.txt_personal_register_address);
		txtBirth = (TextView) root
				.findViewById(R.id.txt_personal_register_birth);
		txtEmail = (TextView) root
				.findViewById(R.id.txt_personal_register_email);
		txtPhone = (TextView) root
				.findViewById(R.id.txt_personal_register_phone);
		spinGender = (Spinner) root
				.findViewById(R.id.spin_personal_register_gender);
		btnChangeUserInfo = (Button) root
				.findViewById(R.id.btn_change_user_info);
		btnChangeUserInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ActivityChangeUserInfo.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_up,
						R.anim.slide_out_up);
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
	}
}
