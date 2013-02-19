package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.activities.homescreen.HomeScreeenActivity;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MasterPasswordActivity extends Activity implements OnClickListener {

	// =========================Control Define =====================
	private Button btnDone;
	private Button btnBack;
	private EditText editTextMasterPW;
	// ========================Class Define =======================
	private IdManagerPreference idManagerPreference;
	// =========================Variable Define ====================
	private String masterPW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.master_password);

		//button
		btnDone = (Button) findViewById(R.id.btn_confirm_master_pw);
		btnDone.setOnClickListener(this);
		editTextMasterPW = (EditText) findViewById(R.id.editText_master_pw);

		// init idmanager preference
		idManagerPreference = IdManagerPreference.getInstance(this);
		masterPW = idManagerPreference.getMasterPW();
	}

	public void confirmMaster(View v) {

	}

	private void showToast(String string) {
		Toast.makeText(MasterPasswordActivity.this, string, Toast.LENGTH_LONG)
				.show();
	}

	public void onReturn(View v) {
		finish();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, MasterPasswordActivity.class);
		activity.startActivity(i);
	}

	@Override
	public void onClick(View v) {
		if (v == btnDone) {
			if ("".equals(editTextMasterPW.getText().toString())) {
				showToast("Type your master password to continue");
			} else {
				if (!masterPW.equals(editTextMasterPW.getText().toString()))
					showToast("Your password is not correct");
				else {
					/* reset edit text master pw */
					editTextMasterPW.setText("");
					
					/* go to HomeScreen activity */
					Intent intent = new Intent(MasterPasswordActivity.this,
							HomeScreeenActivity.class);
					startActivity(intent);
				}
			}
		}

	}
}
