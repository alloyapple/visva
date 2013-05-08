package com.lemon.fromangle;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ValidateScreenActivity extends LemonBaseActivity {

	private TextView lblMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_validate);
		lblMessage = (TextView) findViewById(R.id.lblMessage);
	}

	public void onOKClick(View v) {
		showToastMessage("Ok Click");

	}

	public void onCancelClick(View v) {
		showToastMessage("Cancel Click");
	}

}
