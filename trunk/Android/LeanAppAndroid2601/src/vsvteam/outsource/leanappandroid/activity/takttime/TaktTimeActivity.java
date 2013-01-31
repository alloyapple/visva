package vsvteam.outsource.leanappandroid.activity.takttime;

import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ActionChangeActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionSettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionVersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.HomeActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.activity.valuestreammap.DrawMapActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TProcessDataBase;
import vsvteam.outsource.leanappandroid.database.TProcessDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TTaktTimeDataBase;
import vsvteam.outsource.leanappandroid.database.TTaktTimeDataBaseHandler;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupTaktTimeActivity;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupValueStreamMapActivity;
import android.content.Context;
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
import android.widget.Toast;

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
	private EditText editTextHourPerShift;

	private Button btnTaktTimeCancel;
	private Button btnTaktTimeDone;
	// ==============================Class Define ==============================
	private TTaktTimeDataBaseHandler tTaktTimeDataBaseHandler;
	private TProcessDataBaseHandler tProcessDataBaseHandler;
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	private List<TTaktTimeDataBase> tTaktTimeArrList;
	private List<TProcessDataBase> tProcessDataBases;
	private HomeActivity homeActivity;
	private TabGroupTaktTimeActivity tabGroupTaktTimeActivity;
	private TabGroupValueStreamMapActivity tabGroupValueStreamMapActivity;
	// ==============================Variable Define ===========================
	private static final int MODE_TAKT_TIME_CANCEL = 1;
	private static final int MODE_TAKT_TIME_DONE_OK = 2;
	private static final int MODE_TAKT_TIME_DONE_ERROR = 3;

	private String[] taktTime = { "Second", "Minutes", "Hour", "Day", "Week", "Month" };
	private int _currentProjectIdActive;
	private int _currentProcessIdActive;
	private String _currentProcessNameActive;
	private String _currentProjectNameActive;

	private int _shiftPerDay;
	private int _hourPerShift;
	private int _dayPerWeek;
	private int _dayPerMonth;
	private int _breakPerShift;
	private int _operatorPerShift;
	private int _customerDemandPerUnit;

	@Override
	public void onClick(View view) {
		if (view == btnExport) {
			gotoActivityInGroup(TaktTimeActivity.this, ActionExportActivity.class);
		} else if (view == btnSetting) {
			gotoActivityInGroup(TaktTimeActivity.this, ActionSettingActivity.class);
		} else if (view == btnVersion) {
			gotoActivityInGroup(TaktTimeActivity.this, ActionVersionActivity.class);
		} else if (view == btnChangedProject) {
			gotoActivityInGroup(TaktTimeActivity.this, ActionChangeActivity.class);
		} else if (view == btnTaktTimeCancel) {
			// go to create project

			cancelTaktTime();
		} else if (view == btnTaktTimeDone) {

			if (leanAppAndroidSharePreference.getProjectIdActive() == -1) {
				Toast.makeText(this, "No project is selected to add takt time", Toast.LENGTH_LONG)
						.show();
				cancelTaktTime();

			} else {

				TProcessDataBaseHandler tProcessDataBaseHandler = new TProcessDataBaseHandler(this);
				List<TProcessDataBase> listProcess = tProcessDataBaseHandler
						.getAllProcess(_currentProjectIdActive);
				if (listProcess.size() == 0) {
					Toast.makeText(
							this,
							"No process added in project "
									+ leanAppAndroidSharePreference.getProjectNameActive()
									+ ".Please select  project and insert a process", Toast.LENGTH_LONG).show();
					createTaktTimeErrorNoProcess();

				} else
					doneTaktTime();
				tProcessDataBaseHandler.close();
			}
		}
	}

	/**
	 * create takt time error for no process
	 */
	private void createTaktTimeErrorNoProcess() {
		Log.e("run here ","run here");
		tabGroupTaktTimeActivity = (TabGroupTaktTimeActivity) this.getParent();
		homeActivity = (HomeActivity) tabGroupTaktTimeActivity.getParent();
		homeActivity.setCurrentTab(0);
	}

	/**
	 * cancel takt time -> back to create project activity
	 */
	private void cancelTaktTime() {
		tabGroupTaktTimeActivity = (TabGroupTaktTimeActivity) this.getParent();
		homeActivity = (HomeActivity) tabGroupTaktTimeActivity.getParent();
		homeActivity.setCurrentTab(0);
	}

	/**
	 * action done takt time,insert values to takt time database
	 */
	@SuppressWarnings("deprecation")
	private void doneTaktTime() {
		if ("".equals(editTextBreakPerShift.getText().toString().trim())
				|| "".equals(editTextCustomerDemandUnits.getText().toString().trim())
				|| "".equals(editTextDaysPerMonth.getText().toString().trim())
				|| "".equals(editTextDaysPerWeek.getText().toString().trim())
				|| "".equals(editTextOperatorsPerShift.getText().toString().trim())
				|| "".equals(editTextShiftPerDay.getText().toString().trim())
				|| "".equals(editTextHourPerShift.getText().toString().trim())) {
			Toast.makeText(TaktTimeActivity.this, "Fill all fields to add new takttime",
					Toast.LENGTH_LONG).show();
		} else {
			List<TTaktTimeDataBase> tTaktTimeArrList = tTaktTimeDataBaseHandler.getAllTaktTime();
			int taktTimeId = tTaktTimeArrList.size();
			taktTimeId++;// increase project id +1

			_breakPerShift = Integer.parseInt(editTextBreakPerShift.getText().toString());
			_hourPerShift = Integer.parseInt(editTextHourPerShift.getText().toString());
			_customerDemandPerUnit = Integer.parseInt(editTextCustomerDemandUnits.getText()
					.toString());
			_dayPerMonth = Integer.parseInt(editTextDaysPerMonth.getText().toString());
			_dayPerWeek = Integer.parseInt(editTextDaysPerWeek.getText().toString());
			_operatorPerShift = Integer.parseInt(editTextOperatorsPerShift.getText().toString());
			_shiftPerDay = Integer.parseInt(editTextShiftPerDay.getText().toString());

			tTaktTimeDataBaseHandler.addNewTaktTime(new TTaktTimeDataBase(taktTimeId,
					_currentProcessIdActive, _currentProjectIdActive, _currentProcessNameActive,
					_currentProjectNameActive, _shiftPerDay, _hourPerShift, _breakPerShift,
					_hourPerShift, _breakPerShift, _customerDemandPerUnit, "test", -1));
			tTaktTimeDataBaseHandler.close();

			// go to draw stream map
			// leanAppAndroidSharePreference.setModeTaktTime(MODE_TAKT_TIME_DONE_OK);
			// tabGroupTaktTimeActivity = (TabGroupTaktTimeActivity)
			// this.getParent();
			// homeActivity = (HomeActivity)
			// tabGroupTaktTimeActivity.getParent();
			// homeActivity.setCurrentTab(0);
			gotoActivityInGroup(TaktTimeActivity.this, DrawMapActivity.class);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_takt_time);

		// initialize database
		initDataBase();

		// initialize control
		initialize();

	}

	/**
	 * intialize database
	 */
	private void initDataBase() {
		// initialize share preference
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference.getInstance(this);
		_currentProcessIdActive = leanAppAndroidSharePreference.getProcessIdActive();
		_currentProcessNameActive = leanAppAndroidSharePreference.getProcessNameActive();
		_currentProjectIdActive = leanAppAndroidSharePreference.getProjectIdActive();
		_currentProjectNameActive = leanAppAndroidSharePreference.getProjectNameActive();

		// initialize database
		tTaktTimeDataBaseHandler = new TTaktTimeDataBaseHandler(this);

	}

	/**
	 * intialize control
	 */
	private void initialize() {
		// wheel
		wheelTaktTime1 = (WheelView) findViewById(R.id.wheel_task_time_1);
		wheelTaktTime2 = (WheelView) findViewById(R.id.wheel_task_time_2);
		wheelTaktTime1.setViewAdapter(new TaktTimeArrayAdapter(this, taktTime, 0));
		wheelTaktTime2.setViewAdapter(new TaktTimeArrayAdapter(this, taktTime, 0));
		wheelTaktTime1.setVisibleItems(5);
		wheelTaktTime1.setCurrentItem(0);
		wheelTaktTime2.setVisibleItems(5);
		wheelTaktTime2.setCurrentItem(0);
		// actionbar
		btnExport = (ImageView) findViewById(R.id.img_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangedProject = (ImageView) findViewById(R.id.img_project_change_project);
		btnChangedProject.setOnClickListener(this);
		// button
		btnTaktTimeCancel = (Button) findViewById(R.id.btn_takt_time_cancel);
		btnTaktTimeCancel.setOnClickListener(this);
		btnTaktTimeDone = (Button) findViewById(R.id.btn_takt_time_done);
		btnTaktTimeDone.setOnClickListener(this);

		// edit text
		editTextBreakPerShift = (EditText) findViewById(R.id.editText_break_per_shift);
		editTextCustomerDemandUnits = (EditText) findViewById(R.id.editText_customer_demand_unit);
		editTextDaysPerMonth = (EditText) findViewById(R.id.editText_day_per_month);
		editTextDaysPerWeek = (EditText) findViewById(R.id.editText_day_per_week);
		editTextOperatorsPerShift = (EditText) findViewById(R.id.editText_operator_per_shift);
		editTextShiftPerDay = (EditText) findViewById(R.id.editText_shift_per_day);
		editTextHourPerShift = (EditText) findViewById(R.id.editText_hour_per_shift);
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
