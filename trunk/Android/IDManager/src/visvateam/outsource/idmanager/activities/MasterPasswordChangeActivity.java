package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.activities.login.TermOfServiceActivity;
import visvateam.outsource.idmanager.database.UserDataBase;
import visvateam.outsource.idmanager.database.UserDataBaseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MasterPasswordChangeActivity extends Activity {
	// =======================Control Define ====================
	private Button btnReturn;
	private Button btnDone;
	private EditText editTextNewPW;
	private EditText editTextOldPW;
	private TextView txtChangePW;
	private TextView txtOldPW;
	private TextView txtNewPW;
	// =======================Class Define ======================
	private UserDataBaseHandler userDataBaseHandler;
	// =======================Variables Define ==================
	private boolean isChangePW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_master_password);
		isChangePW = getIntent().getExtras().getBoolean("isChangePW");

		/*init database*/
		initDataBase();
		/* init control */
		initControl();
	}

	private void initDataBase() {
		userDataBaseHandler = new UserDataBaseHandler(this);
		
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
		if (!isChangePW) {
			Intent intent = new Intent(MasterPasswordChangeActivity.this,
					TermOfServiceActivity.class);
			startActivity(intent);
		}else{
			
		}
	}

	public void onReturn(View v) {
		finish();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, MasterPasswordChangeActivity.class);
		activity.startActivity(i);
	}
}
