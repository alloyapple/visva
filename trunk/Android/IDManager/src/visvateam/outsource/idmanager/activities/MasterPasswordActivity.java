package visvateam.outsource.idmanager.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sqlcipher.database.SQLiteDatabase;

import visvateam.outsource.idmanager.activities.homescreen.HomeScreeenActivity;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import visvateam.outsource.idmanager.database.UserDataBase;
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
	private Button mBtnDone;
	private EditText mEditTextMasterPW;
	// ========================Class Define =======================
	private IdManagerPreference mIdManagerPreference;
	private DataBaseHandler mDataBaseHandler;
	// =========================Variable Define ====================
	private String mMasterPWPre;
	private int mRemoveDataTimes;
	private int mNumberAtemppt = 0;
	private int mMasterPWId;
	private String mMasterPW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.master_password);

		// button
		mBtnDone = (Button) findViewById(R.id.btn_confirm_master_pw);
		mBtnDone.setOnClickListener(this);
		mEditTextMasterPW = (EditText) findViewById(R.id.editText_master_pw);

		// init idmanager preference
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		/* init database */
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);

		mMasterPWId = Contants.MASTER_PASSWORD_ID;

		UserDataBase user = mDataBaseHandler.getUser(Contants.MASTER_PASSWORD_ID);
		mMasterPW = user.getUserPassword();
	}

	public void confirmMaster(View v) {

	}

	private void showToast(String string) {
		Toast.makeText(MasterPasswordActivity.this, string, Toast.LENGTH_SHORT).show();
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
				showToast("Wrong master password.Try again");
			} else {
				mNumberAtemppt++;
				if (mNumberAtemppt >= mRemoveDataTimes) {
					showToast("Wrong master password.(Attempt " + mNumberAtemppt + "/"
							+ mRemoveDataTimes + ").All IdxPassword data is removed");
					mNumberAtemppt = 0;
					/* remove data */
					removeData();

				} else
					showToast("Wrong master password.(Attempt " + mNumberAtemppt + "/"
							+ mRemoveDataTimes + ")");
			}
		} else {
			/* go to HomeScreen activity */
			Intent intent = new Intent(MasterPasswordActivity.this, HomeScreeenActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * remove data after type master password times limit to remove data values
	 */
	private void removeData() {
		/*remove database*/
		File db = getDatabasePath(Contants.DATA_IDMANAGER_NAME);
		if (db.exists())
			db.delete();
		
		/*reset share preference*/
		mIdManagerPreference.setApplicationFirstTimeInstalled(true);
		mIdManagerPreference.setMasterPW("");
		mIdManagerPreference.setSecurityMode(Contants.KEY_OFF);
		mIdManagerPreference.setValuesremoveData(Contants.KEY_OFF);
		
		//finish activity
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
			System.out.println(ex.getMessage() + " in the specified directory.");
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
		mMasterPWPre = mIdManagerPreference.getMasterPW();
		mNumberAtemppt = 0;
	}
}
