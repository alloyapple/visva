package com.japanappstudio.IDxPassword.activities;

import com.japanappstudio.IDxPassword.idletime.ControlApplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getApp().setActivity(this);
	}

	public ControlApplication getApp() {
		return (ControlApplication) this.getApplication();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		getApp().touch();
		Log.d("onUserInteraction", "User interaction to " + this.toString());
	}
}
