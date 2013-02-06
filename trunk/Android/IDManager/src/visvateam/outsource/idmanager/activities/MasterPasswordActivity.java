package visvateam.outsource.idmanager.activities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MasterPasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.master_password);
	}

	public void confirmMaster(View v) {
	}

	public void onReturn(View v) {

	}
	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, MasterPasswordActivity.class);
		activity.startActivity(i);
	}
}
