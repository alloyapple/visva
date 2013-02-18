package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
	}

	public void onReturn(View v) {

	}

	public void onChangeMasterPass(View v) {
		Intent intentChangePW = new Intent(SettingActivity.this, MasterPasswordChangeActivity.class);
		intentChangePW.putExtra("isChangePW", true);
		startActivity(intentChangePW);
	}

	public void onSecurityMode(View v) {
		SetupSecurityModeActivity.startActivity(this);
	}

	public void onRemoveData(View v) {
		SetupRemoveDataActivity.startActivity(this);
	}

	public void onGoogle(View v) {

	}

	public void onDropbox(View v) {

	}

	public void onCloud(View v) {

	}

	public void onUnlimitedItems(View v) {

	}

	public void onNoAdmod(View v) {
	}

	public void onExportData(View v) {
	}
}
