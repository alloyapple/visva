package visvateam.outsource.idmanager.activities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MasterPasswordChangeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_master_password);
	}

	public void confirmMaster(View v) {
	}

	public void onReturn(View v) {

	}
	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, MasterPasswordChangeActivity.class);
		activity.startActivity(i);
	}
}
