package com.lemon.fromangle;

import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.utility.StringUtility;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ValidateScreenActivity extends LemonBaseActivity {

	private TextView lblMessage;
	private FromAngleSharedPref mFromAngleSharedPref;
	private String userId = null, userName = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_validate);
		lblMessage = (TextView) findViewById(R.id.lblMessage);

		mFromAngleSharedPref = new FromAngleSharedPref(this);
		userId = mFromAngleSharedPref.getUserId();
		if (!StringUtility.isEmpty(userId)) {
			userName = mFromAngleSharedPref.getUserName();
			lblMessage.setText("Mr/Ms " + userName);
		}
	}

	public void onOKClick(View v) {

		mFromAngleSharedPref.setValidationMode(0);
		if (!mFromAngleSharedPref.getRunFromActivity()) {
			Intent intent = new Intent(ValidateScreenActivity.this,
					TopScreenActivity.class);
			startActivity(intent);
		}
		finish();
	}

	public void onCancelClick(View v) {
		if (mFromAngleSharedPref.getValidationMode() < 1)
			mFromAngleSharedPref.setValidationMode(1);
		else
			mFromAngleSharedPref.setValidationMode(2);
		if (!mFromAngleSharedPref.getRunFromActivity()) {
			Intent intent = new Intent(ValidateScreenActivity.this,
					TopScreenActivity.class);
			startActivity(intent);
		}
		finish();
	}

}
