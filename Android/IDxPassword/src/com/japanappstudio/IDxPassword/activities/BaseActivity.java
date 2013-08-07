package com.japanappstudio.IDxPassword.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.japanappstudio.IDxPassword.activities.securityservice.SecurityMasterPasswordActivity;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;
import com.japanappstudio.IDxPassword.idletime.ControlApplication;

public class BaseActivity extends Activity {
	protected boolean manualDestroy = false;
	protected boolean manualPause = false;
	protected IdManagerPreference mPrefApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getApp().setActivity(this);
		mPrefApp = IdManagerPreference.getInstance(this);
	}

	public ControlApplication getApp() {
		return (ControlApplication) this.getApplication();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getApp().setActivity(this);
		if (mPrefApp.getIsPauseApp() && mPrefApp.getSecurityMode() == 1) {
			mPrefApp.setPauseApp(false);
			Intent i = new Intent(this, SecurityMasterPasswordActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		} else {
			mPrefApp.setPauseApp(false);
			manualDestroy = manualPause = false;
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (manualDestroy)
			mPrefApp.setPauseApp(false);
		else
			mPrefApp.setPauseApp(true);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (manualPause)
			mPrefApp.setPauseApp(false);
		else
			mPrefApp.setPauseApp(true);
		manualPause=false;
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		getApp().touch();
//		Log.d("onUserInteraction", "User interaction to " + this.toString());
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		manualDestroy = true;
		manualPause = true;
		super.finish();

	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		manualPause = true;
		manualDestroy=true;
		super.startActivity(intent);
	}

}
