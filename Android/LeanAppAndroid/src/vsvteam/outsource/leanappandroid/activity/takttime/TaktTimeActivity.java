package vsvteam.outsource.leanappandroid.activity.takttime;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ChangeProjectActivity;
import vsvteam.outsource.leanappandroid.actionbar.ExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.SettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.VersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TTaktTimeDataBase;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TaktTimeActivity extends VSVTeamBaseActivity implements OnClickListener {
	// ==============================Control Define ============================
	private WheelView wheelTaktTime1;
	private WheelView wheelTaktTime2;

	private ImageView btnSetting;
	private ImageView btnExport;
	private ImageView btnVersion;
	private ImageView btnChangedProject; 
	
	private EditText editTextShiftPerDay;
	private EditText editTextBreakPerShift;
	private EditText editTextDaysPerMonth;
	private EditText editTextDaysPerWeek;
	private EditText editTextOperatorsPerShift;
	private EditText editTextCustomerDemandUnits;
	
	private Button btnTaktTimeCancel;
	private Button btnTaktTimeDone;
	// ==============================Class Define ==============================
	private TTaktTimeDataBase tTaktTimeDataBase;
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	// ==============================Variable Define ===========================
	private String[] taktTime = { "Second", "Minutes", "Hour", "Day", "Week", "Month" };

	@Override
	public void onClick(View view) {
		if (view == btnExport) {
			Intent intentExport = new Intent(TaktTimeActivity.this, ExportActivity.class);
			startActivity(intentExport);
		} else if (view == btnSetting) {
			Intent intentSetting = new Intent(TaktTimeActivity.this, SettingActivity.class);
			startActivity(intentSetting);
			Log.e("click setting", "ok");
		} else if (view == btnVersion) {
			Intent intentVersion = new Intent(TaktTimeActivity.this, VersionActivity.class);
			startActivity(intentVersion);
		} else if (view == btnChangedProject) {
			Intent intentChangeProject = new Intent(TaktTimeActivity.this,
					ChangeProjectActivity.class);
			startActivity(intentChangeProject);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_takt_time);

		initialize();

	}

	private void initialize() {
		//wheel
		wheelTaktTime1 = (WheelView) findViewById(R.id.wheel_task_time_1);
		wheelTaktTime2 = (WheelView) findViewById(R.id.wheel_task_time_2);
		wheelTaktTime1.setViewAdapter(new TaktTimeArrayAdapter(this, taktTime, 0));
		wheelTaktTime2.setViewAdapter(new TaktTimeArrayAdapter(this, taktTime, 0));
		wheelTaktTime1.setVisibleItems(5);
		wheelTaktTime1.setCurrentItem(0);
		wheelTaktTime2.setVisibleItems(5);
		wheelTaktTime2.setCurrentItem(0);
		//actionbar
		btnChangedProject = (ImageView) findViewById(R.id.img_takt_time_change_project);
		btnChangedProject.setOnClickListener(this);
		btnExport = (ImageView) findViewById(R.id.img_takt_time_export);
		btnExport.setOnClickListener(this);
		btnSetting = (ImageView) findViewById(R.id.img_takt_time_setting);
		btnSetting.setOnClickListener(this);
		btnVersion = (ImageView) findViewById(R.id.img_takt_time_version);
		btnVersion.setOnClickListener(this);
		//button
		btnTaktTimeCancel = (Button)findViewById(R.id.btn_takt_time_cancel);
		btnTaktTimeCancel.setOnClickListener(this);
		btnTaktTimeDone = (Button)findViewById(R.id.btn_takt_time_done);
		btnTaktTimeDone.setOnClickListener(this);
		//edit text

	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class TaktTimeArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public TaktTimeArrayAdapter(Context context, String[] items, int current) {
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
}
