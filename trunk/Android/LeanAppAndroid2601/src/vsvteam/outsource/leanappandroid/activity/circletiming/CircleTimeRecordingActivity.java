package vsvteam.outsource.leanappandroid.activity.circletiming;

import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TCycleTimeDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TProcessDataBase;
import vsvteam.outsource.leanappandroid.database.TProcessDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TStepsDataBase;
import vsvteam.outsource.leanappandroid.database.TStepsDataBaseHandler;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CircleTimeRecordingActivity extends VSVTeamBaseActivity implements
		OnClickListener {
	// =============================Control Define =============================
	private WheelView wheelCircleTimeProcess;
	private WheelView wheelCircleTimeStep;

	private TextView textViewTimeRecording;
	private TextView textViewNextStep;
	private TextView textViewStepName_Time;
	private TextView textViewRecordingDetail;
	private Button btnSaveStop;
	private Button btnAudioNote;
	private Button btnNextStep;
	private Button btnStartRecording;
	// =============================Class Define ===============================
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	private TProcessDataBaseHandler tProcessDataBaseHandler;
	private TStepsDataBaseHandler tStepsDataBaseHandler;
	private TCycleTimeDataBaseHandler tCycleTimeDataBaseHandler;
	// =============================Variable Define ===========================
	private String[] circleTimeProcess = {};
	private String[] circleTimeStep = {};
	private int _currentProjectIdActive;
	private int _currentProcessIdActive;
	private String _currentProjectNameActive;
	private String _currentProcessNameActive;

	@Override
	public void onClick(View v) {
		if (v == btnSaveStop) {
			gotoActivityInGroup(CircleTimeRecordingActivity.this,
					CircleTimeDetailActivity.class);
		} else if (v == btnNextStep) {
			Toast.makeText(CircleTimeRecordingActivity.this,
					"This feature is coming soon!", Toast.LENGTH_LONG).show();
		} else if (v == btnAudioNote) {
			Toast.makeText(CircleTimeRecordingActivity.this,
					"This feature is coming soon!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.page_start_recording);
		//
		initDataBase();
		//
		initControl();
	}

	/**
	 * initialize database
	 */
	private void initDataBase() {
		// share preference
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference
				.getInstance(this);
		_currentProcessIdActive = leanAppAndroidSharePreference
				.getProcessIdActive();
		_currentProcessNameActive = leanAppAndroidSharePreference
				.getProcessNameActive();
		_currentProjectIdActive = leanAppAndroidSharePreference
				.getProjectIdActive();
		_currentProjectNameActive = leanAppAndroidSharePreference
				.getProjectNameActive();
		// init database
		tProcessDataBaseHandler = new TProcessDataBaseHandler(this);
		tStepsDataBaseHandler = new TStepsDataBaseHandler(this);
		tCycleTimeDataBaseHandler = new TCycleTimeDataBaseHandler(this);

	}

	/**
	 * initialize control
	 */
	private void initControl() {
		// wheel
		wheelCircleTimeProcess = (WheelView) findViewById(R.id.wheel_recording_1);
		wheelCircleTimeStep = (WheelView) findViewById(R.id.wheel_recording_2);
		wheelCircleTimeProcess
				.setViewAdapter(new CircleTimeRecordingArrayAdapter(this,
						circleTimeProcess, 0));
		wheelCircleTimeStep.setViewAdapter(new CircleTimeRecordingArrayAdapter(
				this, circleTimeStep, 0));
		wheelCircleTimeProcess.setVisibleItems(5);
		wheelCircleTimeProcess.setCurrentItem(0);
		wheelCircleTimeStep.setVisibleItems(5);
		wheelCircleTimeStep.setCurrentItem(0);

		// textview
		textViewNextStep = (TextView) findViewById(R.id.textView_recording_next_step);
		textViewStepName_Time = (TextView) findViewById(R.id.textView_recording_step_name_time);
		textViewTimeRecording = (TextView) findViewById(R.id.textView_recording_time);
		textViewRecordingDetail = (TextView) findViewById(R.id.textView_recording_info);
		// button
		btnAudioNote = (Button) findViewById(R.id.btn_recording_audio_note);
		btnAudioNote.setOnClickListener(this);
		btnNextStep = (Button) findViewById(R.id.btn_recording_next_step);
		btnNextStep.setOnClickListener(this);
		btnSaveStop = (Button) findViewById(R.id.btn_recording_save_stop);
		btnSaveStop.setOnClickListener(this);
		btnStartRecording = (Button) findViewById(R.id.btn_recording_start);
		btnStartRecording.setOnClickListener(this);
	}
  
	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class CircleTimeRecordingArrayAdapter extends
			ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public CircleTimeRecordingArrayAdapter(Context context, String[] items,
				int current) {
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

	@Override
	public void onResume() {
		super.onResume();
		// reset data
		textViewRecordingDetail.setText("		Select step to be recorded.		"
				+ _currentProjectNameActive + "-" + _currentProcessNameActive
				+ "-Version 1");
		// set values for wheel
		setDataForWheel();
	}

	/**
	 * refresh data for wheel
	 */
	private void setDataForWheel() {
		List<TProcessDataBase> listProcess = tProcessDataBaseHandler
				.getAllProcess(_currentProjectIdActive);
		circleTimeProcess = new String[listProcess.size()];
		for (int i = 0; i < listProcess.size(); i++) {
			circleTimeProcess[i] = listProcess.get(i).getProcessName();
		}
		List<TStepsDataBase> listStep = tStepsDataBaseHandler
				.getAllStep(_currentProcessIdActive);
		circleTimeStep = new String[listStep.size()];
		for (int i = 0; i < listStep.size(); i++) {
			circleTimeStep[i] = listStep.get(i).getStepDescription();
		}

		// refresh data for wheel process
		CircleTimeRecordingArrayAdapter circleTimeArrayAdapter = new CircleTimeRecordingArrayAdapter(
				this, circleTimeProcess, 0);
		wheelCircleTimeProcess.setViewAdapter(circleTimeArrayAdapter);
		wheelCircleTimeProcess.setCurrentItem(0);
		// refresh data for wheel step
		CircleTimeRecordingArrayAdapter circleTimeArrayAdapter2 = new CircleTimeRecordingArrayAdapter(
				this, circleTimeStep, 0);
		wheelCircleTimeStep.setViewAdapter(circleTimeArrayAdapter2);
		wheelCircleTimeStep.setCurrentItem(0);
	}

}
