package visvateam.outsource.idmanager.activities;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.idxpwdatabase.GroupFolder;
import visvateam.outsource.idmanager.idxpwdatabase.IDxPWDataBaseHandler;
import visvateam.outsource.idmanager.idxpwdatabase.UserDB;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MasterPasswordChangeActivity extends BaseActivity {
	// =======================Control Define ====================

	private EditText editTextVerifyPW;
	private EditText editTextPW;
	private TextView txtChangePW;
	private TextView txtPW;
	private TextView txtVerifyVPW;
	// ==============================Class Define================
	// private DataBaseHandler mDataBaseHandler;
	private IDxPWDataBaseHandler mIDxPWDataBaseHandler;
	// =======================Variables Define ==================
	private boolean isChangePW;
	private String mMasterPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_master_password);
		isChangePW = getIntent().getExtras().getBoolean(Contants.IS_CHANGE_PASSWORD);

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
		// mDataBaseHandler = new DataBaseHandler(this);
		// mDataBaseHandler.getUserCount();
		mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
	}

	private void initControl() {
		// text view
		txtChangePW = (TextView) findViewById(R.id.txt_change_master_pw);
		txtVerifyVPW = (TextView) findViewById(R.id.txt_new_pw);
		txtPW = (TextView) findViewById(R.id.txt_old_pw);

		// edit text
		editTextVerifyPW = (EditText) findViewById(R.id.edit_text_new_pw);
		editTextPW = (EditText) findViewById(R.id.edit_text_old_pw);

		/* check is change pw or create new pw */
		if (isChangePW) {
			txtChangePW.setText(getResources().getString(R.string.title_master_pass));
			txtVerifyVPW.setText(getResources().getString(R.string.confirm_pass));
			txtPW.setText(getResources().getString(R.string.new_pass));
		} else {
			txtChangePW.setText(getResources().getString(R.string.title_master_pass));
			txtVerifyVPW.setText(getResources().getString(R.string.confirm_pass));
			txtPW.setText(getResources().getString(R.string.new_pass));
		}
	}

	public void confirmMaster(View v) {

		/* is create new master pw */
		if (!isChangePW) {
			if (!editTextVerifyPW.getText().toString().trim()
					.equals(editTextPW.getText().toString().trim()))
				showToast(getResources().getString(R.string.message_no_match_pass));
			else if ("".equals(editTextVerifyPW.getText().toString())
					|| "".equals(editTextPW.getText().toString()))
				showToast(getResources().getString(R.string.message_no_match_pass));
			else {
				/* set master pw */
				mMasterPassword = editTextPW.getText().toString();
				/* delete old user */
				// mDataBaseHandler.deleteFolder(Contants.MASTER_PASSWORD_ID);
				mIDxPWDataBaseHandler.deleteUser(Contants.MASTER_PASSWORD_ID);

				// mDataBaseHandler.addNewUser(user);

				UserDB userDB = new UserDB(Contants.MASTER_PASSWORD_ID, mMasterPassword, "");
				mIDxPWDataBaseHandler.addNewUser(userDB);

				// add general folder
				GroupFolder generalFolder = new GroupFolder(0, getString(R.string.list_general), 0,
						Contants.MASTER_PASSWORD_ID, 1);
				mIDxPWDataBaseHandler.addNewFolder(generalFolder);

				/* return Term of service */
				Intent intent = null;
				if ("".equals(userDB.getsEmail())){
					intent = new Intent(MasterPasswordChangeActivity.this,
							RegisterEmailActivity.class);
					intent.putExtra(Contants.CREATE_NEW_EMAIL, true);
				}
				else
					intent = new Intent(MasterPasswordChangeActivity.this,
							MasterPasswordActivity.class);
				startActivity(intent);
				finish();
			}
		}

		/* change master pw */
		else {

			if (!editTextVerifyPW.getText().toString().trim()
					.equals(editTextPW.getText().toString().trim())) {
				showToast(getResources().getString(R.string.message_no_match_pass));
			} else if ("".equals(editTextVerifyPW.getText().toString())
					|| "".equals(editTextPW.getText().toString()))
				showToast(getResources().getString(R.string.message_no_match_pass));
			else {
				mMasterPassword = editTextVerifyPW.getText().toString();
				/* update this password to db */
				// UserDataBase user = new
				// UserDataBase(Contants.MASTER_PASSWORD_ID, mMasterPassword,
				// "test");
				// mDataBaseHandler.updateUser(user);

				UserDB userDB = new UserDB(Contants.MASTER_PASSWORD_ID, mMasterPassword, "");
				mIDxPWDataBaseHandler.updateUser(userDB);
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
