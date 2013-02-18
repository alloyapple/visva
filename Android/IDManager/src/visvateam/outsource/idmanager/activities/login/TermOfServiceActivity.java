package visvateam.outsource.idmanager.activities.login;

import visvateam.outsource.idmanager.activities.MasterPasswordActivity;
import visvateam.outsource.idmanager.activities.MasterPasswordChangeActivity;
import visvateam.outsource.idmanager.activities.R;
import visvateam.outsource.idmanager.database.IdManagerPreference;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TermOfServiceActivity extends Activity implements OnClickListener {
	// =============================Control Define ====================
	private Button btnCancel;
	private Button btnContinue;
	// =============================Class Define ======================
	private IdManagerPreference idManagerPreference;

	// =============================Variables Define ==================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_term_of_use);
		/* check is first installed app */
		idManagerPreference = IdManagerPreference.getInstance(this);
		if (!idManagerPreference.isApplicationFirstTimeInstalled()) {
			showDialogCreateNewPassWord();
		} else {
			idManagerPreference.setApplicationFirstTimeInstalled(false);
		}
		/* init control */
		initControl();
	}

	/**
	 * initialize control
	 */
	private void initControl() {
		// button
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		btnContinue = (Button) findViewById(R.id.btn_continue);
		btnContinue.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == btnCancel)
			finish();
		else if (v == btnContinue) {
			Intent intent = new Intent(TermOfServiceActivity.this,
					MasterPasswordChangeActivity.class);
			/*
			 * status:false is create new password;status :true is change
			 * password
			 */
			intent.putExtra("isChangePW", false);
			startActivity(intent);
		}
	}

	private void showDialogCreateNewPassWord() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// no action happened
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// Exit button clicked
					Intent intent = new Intent(TermOfServiceActivity.this,
							MasterPasswordActivity.class);
					startActivity(intent);
					finish();
					break;
				}
			}
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(TermOfServiceActivity.this);
		builder.setMessage(R.string.create_new_password_msg).setIcon(R.drawable.icon)
				.setTitle(getString(R.string.app_name)).setCancelable(false)
				.setPositiveButton(R.string.create_new_password_confirm, dialogClickListener)
				.setNegativeButton(R.string.create_new_password_cancel, dialogClickListener);
		builder.show();
	}
}
