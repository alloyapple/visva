package visvateam.outsource.idmanager.activities;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.UserDataBase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MasterPasswordChangeActivity extends Activity {
	// =======================Control Define ====================

	private EditText editTextNewPW;
	private EditText editTextOldPW;
	private TextView txtChangePW;
	private TextView txtOldPW;
	private TextView txtNewPW;
	// ==============================Class Define================
	private DataBaseHandler mDataBaseHandler;
	// =======================Variables Define ==================
	private boolean isChangePW;
	private String masterPW;
	private String mMasterPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_master_password);
		isChangePW = getIntent().getExtras().getBoolean(Contants.IS_CHANGE_PASSWORD);
		Log.e("isChangePW", "isCHangePW " + isChangePW);

		/* init database */
		initDataBase();
		/* init control */
		initControl();
	}

	/**
	 * intialize database
	 */
	private void initDataBase() {
		/* init database */
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);
		mDataBaseHandler.getUserCount();

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
				showToast("Password retyped is not matched");
			else if ("".equals(editTextNewPW.getText().toString())
					|| "".equals(editTextOldPW.getText().toString()))
				showToast("Type all field to continue");
			else {
				Log.e("new pww", "new pww");
				/* set master pw */
				mMasterPassword = editTextOldPW.getText().toString();
				/* delete old user */
				mDataBaseHandler.deleteFolder(Contants.MASTER_PASSWORD_ID);
				/* add user to db */
				UserDataBase user = new UserDataBase(Contants.MASTER_PASSWORD_ID, mMasterPassword,
						"test");
				mDataBaseHandler.addNewUser(user);

				/* return Term of service */
				Intent intent = new Intent(MasterPasswordChangeActivity.this,
						MasterPasswordActivity.class);
				startActivity(intent);
				finish();
			}
		}

		/* change master pw */
		else {

			UserDataBase user1 = mDataBaseHandler.getUser(Contants.MASTER_PASSWORD_ID);
			masterPW = user1.getUserPassword();

			if (!masterPW.equals(editTextOldPW.getText().toString())) {
				showToast("Your old master password is not correct");
			} else if ("".equals(editTextNewPW.getText().toString())
					|| "".equals(editTextOldPW.getText().toString()))
				showToast("Type all field to continue");
			else {
				Log.e("change pww", "change pww");
				mMasterPassword = editTextNewPW.getText().toString();
				/* update this password to db */
				UserDataBase user = new UserDataBase(Contants.MASTER_PASSWORD_ID, mMasterPassword,
						"test");
				mDataBaseHandler.updateUser(user);

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
