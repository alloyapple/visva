package visvateam.outsource.idmanager.activities.securityservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.sqlcipher.database.SQLiteDatabase;

import visvateam.outsource.idmanager.activities.homescreen.HomeScreeenActivity;
import visvateam.outsource.idmanager.activities.securityservice.SecurityService;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.database.UserDataBase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import visvateam.outsource.idmanager.activities.R;

public class SecurityMasterPasswordActivity extends Activity implements
		OnClickListener {

	// =========================Control Define =====================
	private Button mBtnDone;
	private EditText mEditTextMasterPW;
	// ========================Class Define =======================
	private IdManagerPreference mIdManagerPreference;
	private DataBaseHandler mDataBaseHandler;
	private SecurityService mSecurityService;
	// ==========================Variable Define ===================
	private int mRemoveDataTimes;
	private int mNumberAtemppt = 0;
	private String mMasterPW;
	private int mSecurityValues;
	private boolean isKeyBoardNumber = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init idmanager preference
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		if (mIdManagerPreference.getSecurityMode() == Contants.KEY_OFF
				|| !mIdManagerPreference.isApplicationFirstTimeInstalled()) {
			/* go to HomeScreen activity */
			Intent intent = new Intent(SecurityMasterPasswordActivity.this,
					HomeScreeenActivity.class);
			startActivity(intent);
			finish();
		} else {
			setContentView(R.layout.page_security_masterpw);

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
			Log.e("masterpw", "master pw " + mMasterPW);

			/* init service */
			if (mSecurityService == null) {
				bindService(new Intent(SecurityMasterPasswordActivity.this,
						SecurityService.class), mConection,
						Context.BIND_AUTO_CREATE);
			}
		}
	}

	public void confirmMaster(View v) {

	}

	private void showToast(String string) {
		Toast.makeText(SecurityMasterPasswordActivity.this, string,
				Toast.LENGTH_SHORT).show();
	}

	public void onReturn(View v) {
		finish();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, SecurityMasterPasswordActivity.class);
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
				showToast("Wrong master password.Try again");
			} else {
				mNumberAtemppt++;
				if (mNumberAtemppt >= mRemoveDataTimes) {
					showToast("Wrong master password.(Attempt "
							+ mNumberAtemppt + "/" + mRemoveDataTimes
							+ ").All IdxPassword data is removed");
					mNumberAtemppt = 0;
					/* remove data */
					removeData();

				} else
					showToast("Wrong master password.(Attempt "
							+ mNumberAtemppt + "/" + mRemoveDataTimes + ")");
			}
		} else {
			/* check security service */
			mSecurityValues = mIdManagerPreference.getSecurityMode();
			int securityValues = 0;
			Log.e("security", "security " + mSecurityValues);
			if (mSecurityValues == 0)
				securityValues = 0;
			else if (mSecurityValues == 1)
				securityValues = 1;
			else if (mSecurityValues == 2)
				securityValues = 3;
			else if (mSecurityValues == 4)
				securityValues = 5;
			else if (mSecurityValues == 5)
				securityValues = 10;
			long time = 60 * securityValues * 1000;
			mSecurityService.startCountDownTimer(time);

			finish();
		}
	}

	@SuppressLint("InlinedApi")
	public void onClickChangerKeyBoard(View v) {
		if (isKeyBoardNumber) {
			mEditTextMasterPW.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
			isKeyBoardNumber = false;
		} else {
			mEditTextMasterPW.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			isKeyBoardNumber = true;
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

	private static void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);
			InputStream in = new FileInputStream(f1);

			// For Append the file.
			// OutputStream out = new FileOutputStream(f2,true);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out
					.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
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

	private ServiceConnection mConection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			mSecurityService = ((SecurityService.LocalService) service)
					.getService();
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		unbindService(mConection);
	};
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Log.e("lam cai log", "lam cai log");
			return false;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
