package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.database.IdManagerPreference;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SetupSecurityModeActivity extends Activity {
	private final static int SETTING_CHANGE = 0;
	private WheelView mWheelViewModeSecurity;
	private IdManagerPreference mIdManagerPreference;
	private String modes[] = { "Off", "1 minute", "3 minute", "5 minute",
			"10 minute" };
	private int mChoied;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sercurity_mode);
		mWheelViewModeSecurity = (WheelView) findViewById(R.id.id_wheelview_security_mode);
		mWheelViewModeSecurity.setVisibility(View.VISIBLE);
		mWheelViewModeSecurity.setViewAdapter(new SecurityModeAdapter(this,
				modes, 0));
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		mWheelViewModeSecurity.setCurrentItem(mIdManagerPreference
				.getSecurityMode());
		mChoied = mIdManagerPreference.getSecurityMode();
	}

	public void onReturn(View v) {
		int position = mWheelViewModeSecurity.getCurrentItem();
		if (mChoied != position) {
			showDialog(SETTING_CHANGE);
		} else {
			finish();
		}

	}

	private void setSecurityMode() {
		// TODO Auto-generated method stub
		int position = mWheelViewModeSecurity.getCurrentItem();
		mIdManagerPreference.setSecurityMode(position);
		Log.e("position " + position, "values " + modes[position]);
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class SecurityModeAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public SecurityModeAdapter(Context context, String[] items, int current) {
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

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, SetupSecurityModeActivity.class);
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
					.setMessage(R.string.message_setting_chage)
					.setPositiveButton(
							getResources().getString(R.string.confirm_ok),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									setSecurityMode();
									finish();
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.confirm_cancel),
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
			// setSecurityMode();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}