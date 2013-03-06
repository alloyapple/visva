package visvateam.outsource.idmanager.activities;

import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IDDataBase;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CopyItemActivity extends Activity {
	private int currentPasswordId;
	public final static int COPY_ID = 0;
	public final static int COPY_PASS = 1;
	public static int MAX_ID = 12;
	private IDDataBase id;
	private DataBaseHandler mDataBaseHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentPasswordId = getIntent().getExtras().getInt(
				Contants.CURRENT_PASSWORD_ID);
		setContentView(R.layout.page_choice_id_pass_item);
		initDataBase();
	}

	private void initDataBase() {
		// TODO Auto-generated method stub
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);
		id = mDataBaseHandler.getId(currentPasswordId);
	}

	public static void startActivity(Activity activity, int dataTranfer) {
		Intent i = new Intent(activity, CopyItemActivity.class);
		i.putExtra(Contants.CURRENT_PASSWORD_ID, dataTranfer);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onCopyId(View v) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(id.getDataId(1));
		// for (int i = 0; i < MAX_ID; i += 2) {
		// ClipData clip = ClipData.newPlainText("IDM" + i / 2,
		// id.getDataId(i));
		// clipboard.setPrimaryClip(clip);
		// }
		showDialog(COPY_ID);
	}

	public void onCopyPassword(View v) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(id.getDataId2());
		// for (int i = 1; i < MAX_ID; i += 2) {
		// ClipData clip = ClipData.newPlainText("PASSM" + i / 2 + 1,
		// id.getDataId(i));
		// clipboard.setPrimaryClip(clip);
		// }
		showDialog(COPY_PASS);
	}

	public void onPasteBrowse(View v) {
		Intent intentBrowser = new Intent(CopyItemActivity.this,
				BrowserActivity.class);
		intentBrowser.putExtra(Contants.KEY_TO_BROWSER, Contants.PASTE_TO);
		intentBrowser.putExtra(Contants.CURRENT_PASSWORD_ID, currentPasswordId);
		startActivity(intentBrowser);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		switch (id) {

		case COPY_ID:
			alert.setTitle(R.string.copy_title)
					.setMessage(getResources().getString(R.string.coppied_id))
					.setPositiveButton(
							getResources().getString(R.string.confirm_ok),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
			return alert.create();

		case COPY_PASS:
			alert.setTitle(R.string.copy_title)
					.setMessage(R.string.coppied_pass)
					.setPositiveButton(
							getResources().getString(R.string.confirm_ok),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
			return alert.create();

		default:
			break;
		}
		return null;
	}

}
