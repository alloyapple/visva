package visvateam.outsource.idmanager.activities;

import java.io.File;
import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.activities.homescreen.HomeScreeenActivity;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.idxpwdatabase.IDxPWDataBaseHandler;
import visvateam.outsource.idmanager.idxpwdatabase.UserDB;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MasterPasswordActivity extends BaseActivity implements
		OnClickListener {

	// =========================Control Define =====================
	private Button mBtnDone;
	private EditText mEditTextMasterPW;
	// private EditText mEditText
	// ========================Class Define =======================
	private IdManagerPreference mIdManagerPreference;
	// private DataBaseHandler mDataBaseHandler;
	private IDxPWDataBaseHandler mIDxPWDataBaseHandler;
	// ==========================Variable Define ===================
	private int mRemoveDataTimes;
	private int mNumberAtemppt = 0;
	private String mMasterPW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init idmanager preference
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		if (mIdManagerPreference.getValuesRemoveData() == Contants.KEY_OFF
				|| !mIdManagerPreference.isApplicationFirstTimeInstalled()) {
			/* go to HomeScreen activity */
			Intent intent = new Intent(MasterPasswordActivity.this,
					HomeScreeenActivity.class);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			startActivity(intent);
			finish();
		} else {
			setContentView(R.layout.master_password);

			mIdManagerPreference.setSecurityLoop(true);
			// button
			mBtnDone = (Button) findViewById(R.id.btn_confirm_master_pw);
			mBtnDone.setOnClickListener(this);
			mEditTextMasterPW = (EditText) findViewById(R.id.editText_master_pw);
			/* init database */
			SQLiteDatabase.loadLibs(this);
			// mDataBaseHandler = new DataBaseHandler(this);
			mIDxPWDataBaseHandler = new IDxPWDataBaseHandler(this);
			UserDB userTemp = mIDxPWDataBaseHandler
					.getUser(Contants.MASTER_PASSWORD_ID);
			mMasterPW = userTemp.getPassword();
		}
	}

	public void confirmMaster(View v) {

	}

	private void showToast(String string) {
		Toast.makeText(MasterPasswordActivity.this, string, Toast.LENGTH_SHORT)
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
		if (v == mBtnDone) {
			if ("".equals(mEditTextMasterPW.getText().toString())) {
				showToast("Please enter your master password");
			} else {
				/* check remove data values */
				checkRemoveDataValues();
			}
			/* reset edit text master pw */
			mEditTextMasterPW.setText("");
		}
	}

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
			/* check security service */
			Intent intent = new Intent(MasterPasswordActivity.this,
					HomeScreeenActivity.class);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
			startActivity(intent);
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
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText("");
		// finish activity
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mRemoveDataTimes = mIdManagerPreference.getValuesRemoveData();
		// mMasterPW = mDataBaseHandler.getUser(Contants.MASTER_PASSWORD_ID)
		// .getUserPassword();
		mMasterPW = mIDxPWDataBaseHandler.getUser(Contants.MASTER_PASSWORD_ID)
				.getPassword();
		mNumberAtemppt = 0;
	}

	/**
	 * connection to service to add security mode
	 */

	protected void onDestroy() {
		super.onDestroy();
		mIdManagerPreference.setSecurityLoop(false);
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
					R.string.limit_login_msg, mNumberAtemppt));
			// builder.setMessage("Type the name of new folder:");
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
			// builder.setMessage("Type the name of new folder:");
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
			// builder.setMessage("Type the name of new folder:");
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
