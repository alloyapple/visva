package com.japanappstudio.IDxPassword.activities;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;

import com.japanappstudio.IDxPassword.activities.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.japanappstudio.IDxPassword.contants.Contants;
import com.japanappstudio.IDxPassword.idxpwdatabase.ElementID;
import com.japanappstudio.IDxPassword.idxpwdatabase.IDxPWDataBaseHandler;
import com.japanappstudio.IDxPassword.idxpwdatabase.Password;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class CopyItemActivity extends BaseActivity {
	public static final String KEY_LIST_ITEM = "listItem";
	public static final String KEY_URL = "keyUrl";
	public static final String KEY_NOTE = "keyNote";
	private int currentElementId;
	public final static int COPY_ID = 0;
	public final static int COPY_PASS = 1;
	public static int MAX_ID = 12;
	private ElementID element;
	private IDxPWDataBaseHandler mDataBaseHandler;
	private LinearLayout mLinear;
	public int indexSelect;
	public static ArrayList<Item> itemList = new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentElementId = getIntent().getExtras().getInt(
				Contants.CURRENT_PASSWORD_ID);
		setContentView(R.layout.page_choice_id_pass_item);
		mLinear = (LinearLayout) findViewById(R.id.id_linear_copy_item);
		initDataBase();
		initAdmod();

	}

	public void initAdmod() {
		AdView adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	private void initDataBase() {
		// TODO Auto-generated method stub
		SQLiteDatabase.loadLibs(this);
		itemList.clear();
		mDataBaseHandler = new IDxPWDataBaseHandler(this);
		element = mDataBaseHandler.getElementID(currentElementId);
		List<Password> listPass = mDataBaseHandler
				.getAllPasswordByElementId(currentElementId);
		for (int i = 0; i < listPass.size(); i++) {
			Item item = new Item();
			item.mNameItem = listPass.get(i).getTitleNameId();
			item.mContentItem = listPass.get(i).getPassword();
			itemList.add(item);
		}
		for (int i = 0; i < itemList.size(); i++) {
			if (!itemList.get(i).mContentItem.equals("")) {
				indexSelect = i;
				Button btnItem = new Button(this);
				btnItem.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 60));
				((LinearLayout.LayoutParams) btnItem.getLayoutParams()).topMargin = 10;
				btnItem.setBackgroundResource(R.drawable.btn_copy_item);
				btnItem.setTextColor(Color.WHITE);
				btnItem.setGravity(Gravity.CENTER);
				btnItem.setText(itemList.get(i).mNameItem);
				btnItem.setOnClickListener(new OnClickListener() {
					int index = indexSelect;

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onCopy(itemList.get(index).mContentItem,
								itemList.get(index).mNameItem);
					}
				});
				mLinear.addView(btnItem);
			}
		}
		Button btnCopy = new Button(this);
		btnCopy.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 60));
		((LinearLayout.LayoutParams) btnCopy.getLayoutParams()).topMargin = 10;
		btnCopy.setBackgroundResource(R.drawable.btn_copy_item);
		btnCopy.setTextColor(Color.WHITE);
		btnCopy.setGravity(Gravity.CENTER);
		btnCopy.setText(getResources().getString(R.string.btn_paste_browse));
		btnCopy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPasteBrowse(null);
			}
		});
		mLinear.addView(btnCopy);
		Button btnEdit = new Button(this);
		btnEdit.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 60));
		((LinearLayout.LayoutParams) btnEdit.getLayoutParams()).topMargin = 10;
		btnEdit.setBackgroundResource(R.drawable.btn_edit_item);
		btnEdit.setTextColor(Color.WHITE);
		btnEdit.setGravity(Gravity.CENTER);
		btnEdit.setText(getResources().getString(R.string.title_edit));
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onEdit();
			}
		});
		mLinear.addView(btnEdit);

	}

	public void onCopy(String contentCopy, String titleCopy) {
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(contentCopy);
		element.seteTimeStamp(System.currentTimeMillis());
		AlertDialog dialog = showDialogCopy(titleCopy);
		dialog.show();
		TextView messageText = (TextView) dialog
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);
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
				BrowserJogdialActivity.class);
		// intentBrowser.putExtra(KEY_LIST_ITEM, itemList);
		intentBrowser.putExtra(KEY_URL, element.geteUrl());
		intentBrowser.putExtra(KEY_NOTE, element.geteNote());
		startActivity(intentBrowser);
	}

	public void onEdit() {
		Intent intent = new Intent(this, EditIdPasswordActivity.class);
		intent.putExtra(Contants.IS_INTENT_CREATE_NEW_ID, 0);
		intent.putExtra(Contants.IS_SRC_ACTIVITY, 1);
		intent.putExtra(Contants.CURRENT_PASSWORD_ID, currentElementId);
		startActivity(intent);
		finish();
	}

	public AlertDialog showDialogCopy(String titleCopy) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage(
				getResources().getString(R.string.message_coppied_clipboard)
						+ "\n" + titleCopy).setPositiveButton(
				getResources().getString(R.string.confirm_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						element.seteFlag(EditIdPasswordActivity.ELEMENT_FLAG_TRUE);
					}
				});
		return alert.create();
	}
}
