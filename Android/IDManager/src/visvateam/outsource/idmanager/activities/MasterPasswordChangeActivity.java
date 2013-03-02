package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.activities.login.TermOfServiceActivity;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MasterPasswordChangeActivity extends Activity {
	// =======================Control Define ====================
	private Button btnReturn;
	private Button btnDone;
	private EditText editTextNewPW;
	private EditText editTextOldPW;
	private TextView txtChangePW;
	private TextView txtOldPW;
	private TextView txtNewPW;
	private IdManagerPreference idManagerPreference;
	private DataBaseHandler mDataBaseHandler;
	// =======================Variables Define ==================
	private boolean isChangePW;
	private String masterPW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_master_password);
		isChangePW = getIntent().getExtras().getBoolean("isChangePW");
		Log.e("isChangePW", "isCHangePW " + isChangePW);

		/* init database */
		initDataBase();
		/* init control */
		initControl();
	}

	private void initDataBase() {

		/* init share preference */
		idManagerPreference = IdManagerPreference.getInstance(this);
		masterPW = idManagerPreference.getMasterPW();

	}

	private void initControl() {
		// text view
		txtChangePW = (TextView) findViewById(R.id.txt_change_master_pw);
		txtNewPW = (TextView) findViewById(R.id.txt_new_pw);
		txtOldPW = (TextView) findViewById(R.id.txt_old_pw);

		// edit text
		editTextNewPW = (EditText) findViewById(R.id.edit_text_new_pw);
		editTextOldPW = (EditText) findViewById(R.id.edit_text_old_pw);

		/* check is change pw or create new pw */
		if (isChangePW) {
			txtChangePW.setText("Change Your Master PassWord");
			txtNewPW.setText("New:");
			txtOldPW.setText("Old:");
		} else {
			txtChangePW.setText("Create New Your Master PassWord");
			txtNewPW.setText("Retype:");
			txtOldPW.setText("New:");
		}
	}

	public void confirmMaster(View v) {

		/* is create new master pw */
		if (!isChangePW) {
			if (!editTextNewPW.getText().toString().trim()
					.equals(editTextOldPW.getText().toString().trim()))
				showToast("Password retyped is not correct");
			else {
				/* set master pw */
				masterPW = editTextOldPW.getText().toString();
				idManagerPreference.setMasterPW(masterPW);

				/* return Term of service */
				Intent intent = new Intent(MasterPasswordChangeActivity.this,
						TermOfServiceActivity.class);
				startActivity(intent);
				finish();
			}
		} else {

			/* change master pw */
			if (!masterPW.equals(editTextOldPW.getText().toString())) {
				showToast("Your old master password is not correct");
			} else {
				masterPW = editTextNewPW.getText().toString();
				idManagerPreference.setMasterPW(masterPW);

				/* return setting activity */
				Intent intent = new Intent(MasterPasswordChangeActivity.this, SettingActivity.class);
				startActivity(intent);
				finish();
			}
		}

	}

	public void onReturn(View v) {
		finish();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, MasterPasswordChangeActivity.class);
		activity.startActivity(i);
	}

	private void showToast(String string) {
		Toast.makeText(MasterPasswordChangeActivity.this, string, Toast.LENGTH_LONG).show();
	}
}
