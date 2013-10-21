package com.visva.android.shopiever2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.visva.android.shopiever2.R;

public abstract class AbstractActivity extends Activity{
	private LinearLayout container;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.abstract_activity);
		init();
		onCreate();
	}
	
	private void init() {
		container = (LinearLayout) findViewById(R.id.container);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(contentView(), container, false);
		
		container.addView(view , -1 , -1);
	}
	
	public void changeToActivity(Intent intent , boolean isFinish){
		startActivity(intent);
		if(isFinish){
			finish();
		}
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
	public void goBack(){
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	public abstract int  contentView();
	public abstract void onCreate();
}
