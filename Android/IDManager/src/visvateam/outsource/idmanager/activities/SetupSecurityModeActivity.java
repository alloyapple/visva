package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.database.IdManagerPreference;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SetupSecurityModeActivity extends Activity {
	private WheelView mWheelViewModeSecurity;
	private IdManagerPreference mIdManagerPreference;
	private String modes[] = { "Off", "1 minute", "3 minute", "5 minute", "10 minute" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sercurity_mode);
		mWheelViewModeSecurity = (WheelView) findViewById(R.id.id_wheelview_security_mode);
		mWheelViewModeSecurity.setVisibility(View.VISIBLE);
		mWheelViewModeSecurity.setViewAdapter(new SecurityModeAdapter(this, modes, 0));
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		mWheelViewModeSecurity.setCurrentItem(mIdManagerPreference.getSecurityMode());
	}

	public void onReturn(View v) {
		setSecurityMode();
		finish();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			setSecurityMode();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}