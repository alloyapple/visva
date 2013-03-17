package visvateam.outsource.idmanager.activities;

import java.io.File;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.database.UserDataBase;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnterOldPasswordActivity extends BaseActivity implements
		OnClickListener {

	// =========================Control Define =====================
	private Button mBtnDone;
	private EditText mEditTextMasterPW;
	// private EditText mEditText
	// ========================Class Define =======================
	private IdManagerPreference mIdManagerPreference;
	private DataBaseHandler mDataBaseHandler;

	// ==========================Variable Define ===================
	private int mRemoveDataTimes;
	private int mNumberAtemppt = 0;
	private String mMasterPW;
	private int mode;
	public static int FROM_SETTING = 0;
	public static int FROM_ENTER_OLD_PASS = 1;
	public static String KEY_MODE = "KEY_TO";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		mode = b.getInt(KEY_MODE);
		setContentView(R.layout.master_password);
		if (mode == FROM_SETTING) {
			((TextView) findViewById(R.id.enter_old_pass))
					.setText(getResources().getString(
							R.string.title_master_pass));
		} else if (mode == FROM_ENTER_OLD_PASS) {
			((TextView) findViewById(R.id.enter_old_pass))
					.setText(getResources().getString(
							R.string.title_current_pass));
		}
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		mIdManagerPreference.setSecurityLoop(true);
		// button
		mBtnDone = (Button) findViewById(R.id.btn_confirm_master_pw);
		mBtnDone.setOnClickListener(this);
		mEditTextMasterPW = (EditText) findViewById(R.id.editText_master_pw);
		/* init database */
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);

		UserDataBase user = mDataBaseHandler
				.getUser(Contants.MASTER_PASSWORD_ID);
		mMasterPW = user.getUserPassword();

	}

	public void confirmMaster(View v) {

	}

	public void onReturn(View v) {
		finish();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, EnterOldPasswordActivity.class);
		activity.startActivity(i);
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnDone) {
			if ("".equals(mEditTextMasterPW.getText().toString())) {

			} else {
				/* check remove data values */
				checkRemoveDataValues();
			}
			/* reset edit text master pw */
			mEditTextMasterPW.setText("");
		}
	}

	@SuppressWarnings("deprecation")
	private void checkRemoveDataValues() {
		// TODO Auto-generated method stub
		if (!mMasterPW.equals(mEditTextMasterPW.getText().toString())) {
			if (mRemoveDataTimes == Contants.KEY_OFF) {
				showDialog(Contants.DIALOG_WRONG_PASS_NO_SECURE);
			} else {
				mNumberAtemppt++;
				if (mNumberAtemppt >= mRemoveDataTimes) {
					showDialog(Contants.DIALOG_REMOVED_DATA);
					mNumberAtemppt = 0;

				} else {
					showDialog(Contants.DIALOG_LOGIN_WRONG_PASS);
				}
			}
		} else {

			/* go to HomeScreen activity */
			if (mode == FROM_SETTING) {
				Intent intent = new Intent(EnterOldPasswordActivity.this,
						EnterOldPasswordActivity.class);
				intent.putExtra(KEY_MODE, FROM_ENTER_OLD_PASS);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(EnterOldPasswordActivity.this,
						MasterPasswordChangeActivity.class);
				intent.putExtra(Contants.IS_CHANGE_PASSWORD, true);
				startActivity(intent);
				finish();
			}
		}
	}

	/**
	 * remove data after type master password times limit to remove data values
	 */
	private void removeData() {
		/* remove database */
		File db = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		if (db.exists())
			db.delete();

		/* reset share preference */
		mIdManagerPreference.setApplicationFirstTimeInstalled(true);
		mIdManagerPreference.setMasterPW("");
		mIdManagerPreference.setSecurityMode(Contants.KEY_OFF);
		mIdManagerPreference.setValuesremoveData(Contants.KEY_OFF);

		// finish activity
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mRemoveDataTimes = mIdManagerPreference.getValuesRemoveData();
		mMasterPW = mDataBaseHandler.getUser(Contants.MASTER_PASSWORD_ID)
				.getUserPassword();
		mNumberAtemppt = 0;
	}

	/**
	 * connection to service to add security mode
	 */

	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case Contants.DIALOG_LOGIN_WRONG_PASS:
			return createDialog(Contants.DIALOG_LOGIN_WRONG_PASS);
		case Contants.DIALOG_WRONG_PASS_NO_SECURE:
			return createDialog(Contants.DIALOG_WRONG_PASS_NO_SECURE);
		case Contants.DIALOG_REMOVED_DATA:
			return createDialog(Contants.DIALOG_REMOVED_DATA);
		default:
			return null;
		}
	}

	/**
	 * Create and return an example alert dialog with an edit text box.
	 */
	private Dialog createDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		switch (id) {
		case Contants.DIALOG_LOGIN_WRONG_PASS:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources().getString(
					R.string.limit_login_msg, 2));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}

					});
			return builder.create();
		case Contants.DIALOG_WRONG_PASS_NO_SECURE:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources()
					.getString(R.string.wrong_pw_login));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							return;
						}

					});
			return builder.create();
		case Contants.DIALOG_REMOVED_DATA:
			builder.setTitle(getResources().getString(R.string.app_name));
			builder.setMessage(getResources().getString(
					R.string.data_erased_msg));
			builder.setIcon(R.drawable.icon);

			builder.setPositiveButton(
					getResources().getString(R.string.confirm_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							/* remove data */
							removeData();
							return;
						}

					});
			return builder.create();
		default:
			return null;
		}
	}
	
}
