package com.japanappstudio.IDxPassword.activities;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.japanappstudio.IDxPassword.database.IdManagerPreference;

import com.japanappstudio.IDxPassword.activities.R;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SetupRemoveDataActivity extends BaseActivity {
	private final static int SETTING_CHANGE = 0;
	private String choice[] = { "Off", "1", "2", "3", "4", "5", "6", "7", "10",
			"20", "40", "80" };
	private int mChoied;
	private WheelView mWheelViewRemoveData;
	private IdManagerPreference mIdManagerPreference;
	private AdView adview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remove_data);
		choice[0] = getResources().getString(R.string.text_off);
		mWheelViewRemoveData = (WheelView) findViewById(R.id.id_wheelview_remove_data);
		mWheelViewRemoveData.setVisibility(View.VISIBLE);
		mWheelViewRemoveData.setViewAdapter(new RemoveDataAdapter(this, choice,
				0));

		mIdManagerPreference = IdManagerPreference.getInstance(this);
		mWheelViewRemoveData.setCurrentItem(mIdManagerPreference
				.getValuesRemoveData());
		mChoied = mIdManagerPreference.getValuesRemoveData();
		initAdmod();
	}

	public void initAdmod() {
		adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			if (!mIdManagerPreference.getIsPaymentNoAd())
				adview.setVisibility(View.VISIBLE);
			else
				adview.setVisibility(View.GONE);
		}
	}

	private class RemoveDataAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public RemoveDataAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(24);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	@SuppressWarnings("deprecation")
	public void onReturn(View v) {
		int position = mWheelViewRemoveData.getCurrentItem();
		if (position != mChoied) {
			setValuesRemoveData();
			showDialog(SETTING_CHANGE);
		} else {
			finish();
		}

	}

	private void setValuesRemoveData() {
		int position = mWheelViewRemoveData.getCurrentItem();
		if (position == 0)
			mIdManagerPreference.setValuesremoveData(position);
		else {
			mIdManagerPreference.setValuesremoveData(Integer
					.parseInt(choice[position]));
		}
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, SetupRemoveDataActivity.class);
		activity.startActivity(i);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		switch (id) {

		case SETTING_CHANGE:
			alert.setTitle(R.string.setting_title)
					.setMessage(R.string.message_change_remove_data)
					.setPositiveButton(
							getResources().getString(R.string.confirm_ok),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									finish();
								}
							});
			return alert.create();

		default:
			break;
		}
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			onReturn(null);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
