package visvateam.outsource.idmanager.activities;

import java.util.ArrayList;
import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IDDataBase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class CopyItemActivity extends Activity {
	private int currentPasswordId;
	public final static int COPY_ID = 0;
	public final static int COPY_PASS = 1;
	public static int MAX_ID = 12;
	private IDDataBase id;
	private String nameCopy;
	private ArrayList<Item> items = new ArrayList<Item>();
	private DataBaseHandler mDataBaseHandler;
	private LinearLayout mLinear;
	public int indexSelect;

	class Item {
		String name;
		String content;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentPasswordId = getIntent().getExtras().getInt(
				Contants.CURRENT_PASSWORD_ID);
		setContentView(R.layout.page_choice_id_pass_item);
		mLinear = (LinearLayout) findViewById(R.id.id_linear_copy_item);
		initDataBase();

	}

	private void initDataBase() {
		// TODO Auto-generated method stub
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);
		id = mDataBaseHandler.getId(currentPasswordId);
		for (int i = 0; i < MAX_ID; i++) {
			if (!id.getDataId(i + 1).equals("")) {
				indexSelect = i;
				Button btnItem = new Button(this);
				btnItem.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				((LinearLayout.LayoutParams) btnItem.getLayoutParams()).topMargin = 10;
				btnItem.setBackgroundResource(R.drawable.btn_copy_item);
				btnItem.setTextColor(Color.WHITE);
				btnItem.setGravity(Gravity.CENTER);
				btnItem.setText(id.getTitleId(i + 1));
				btnItem.setOnClickListener(new OnClickListener() {
					int index = indexSelect;

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onCopy(id.getTitleId(index + 1),
								id.getDataId(index + 1));
					}
				});
				mLinear.addView(btnItem);
			}
		}

	}

	public void onCopy(String nameCopy, String contentCopy) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(contentCopy);
		this.nameCopy = nameCopy;
		Log.i("<------name------->", this.nameCopy);
		showDialogCopy();
	}

	public static void startActivity(Activity activity, int dataTranfer) {
		Intent i = new Intent(activity, CopyItemActivity.class);
		i.putExtra(Contants.CURRENT_PASSWORD_ID, dataTranfer);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onPasteBrowse(View v) {
		Intent intentBrowser = new Intent(CopyItemActivity.this,
				BrowserActivity.class);
		intentBrowser.putExtra(Contants.KEY_TO_BROWSER, Contants.PASTE_TO);
		intentBrowser.putExtra(Contants.CURRENT_PASSWORD_ID, currentPasswordId);
		startActivity(intentBrowser);
	}
	public void showDialogCopy() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.copy_title)
				.setMessage(nameCopy + " is coppied")
				.setPositiveButton(
						getResources().getString(R.string.confirm_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
		alert.create().show();
	}
}
