package com.visva.android.shoppieandroid.activity;

import android.content.Intent;
import android.view.View;

import com.visva.android.shoppieandroid.R;

public class LoginActivity extends VisvaAbstractActivity{

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		 return R.layout.page_register;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
	}
	
	public void onClickedLogin(View v){
		Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
		startActivity(intent);
		finish();
	}

}
