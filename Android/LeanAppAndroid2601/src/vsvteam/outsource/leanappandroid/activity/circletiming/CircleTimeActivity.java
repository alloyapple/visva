package vsvteam.outsource.leanappandroid.activity.circletiming;

import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ActionChangeActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionSettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionVersionActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TCycleTimeDataBase;
import vsvteam.outsource.leanappandroid.database.TCycleTimeDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TProcessDataBase;
import vsvteam.outsource.leanappandroid.database.TProcessDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TStepsDataBase;
import vsvteam.outsource.leanappandroid.database.TStepsDataBaseHandler;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.ToggleButton;

public class CircleTimeActivity extends TabGroupActivity implements OnClickListener {
	// ==============================Control Define =========================
	private WheelView wheelCircleTimeProcess;
	private WheelView wheelCircleTimeStep;

	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;

	private EditText editTextOperatorName;
	private EditText editTextShiftNumber;
	private ToggleButton toggleBtnVideoOrAudio;
	private ToggleButton toggleBtnUseMs;
	private Button btnStartRecordVideo;
	private TextView textViewRecordingDetail;

	// ==============================Class Define ===========================
	private TProcessDataBaseHandler tProcessDataBaseHandler;
	private TStepsDataBaseHandler tStepsDataBaseHandler;
	private TCycleTimeDataBaseHandler tCycleTimeDataBaseHandler;
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	// ==============================Variable Define ========================
	private static final String TAG = CircleTimeActivity.class.getSimpleName();
	private static final int REQUEST_VIDEO_CAPTURED = 1002;
	private Uri fileUri;
	private String _currentProjectNameActive;
	private int _currentProjectIdActive;
	private String _currentProcessNameActice;
	private int _currentProcessIdActice;

	private String[] circleTimeProcess = {};
	private int[] circleTimeProcessId = {};
	private String[] circleTimeStep = {};
	private int[] circleStepId = {};
	// cycle time values
	private int cycleCounter;
	private String operatorName;
	private int shiftNo;
	private int stepId;
	private String stepDescription;
	private String lowestTime;
	private String adjustment;
	private String adjustTime;
	private String audioNote;
	private String videoFileName;
	private String startTimeStamp;
	private String endTimeStamp;
	private int versionId;
	private int previousVersionId;
	private String preVerDifferenceLowestTime;
	private String preVerDifferenceAdjustedTime;
	private String preVerDifferenceAdjustment;
	private boolean noVideoOnlyTiming;
	private boolean useMilliseconds;

	@Override
	public void onClick(View v) {
		if (v == btnExport) {
			gotoActivityInGroup(CircleTimeActivity.this, ActionExportActivity.class);
		} else if (v == btnSetting) {
			gotoActivityInGroup(CircleTimeActivity.this, ActionSettingActivity.class);
		} else if (v == btnVersion) {
			gotoActivityInGroup(CircleTimeActivity.this, ActionVersionActivity.class);

		} else if (v == btnChangeProject) {
			gotoActivityInGroup(CircleTimeActivity.this, ActionChangeActivity.class);
		} else if (v == btnStartRecordVideo) {
			if ("".equals(editTextOperatorName.getText().toString().trim())
					|| "".equals(editTextShiftNumber.getText().toString().trim())) {
				Toast.makeText(CircleTimeActivity.this, "Fill all field to recording",
						Toast.LENGTH_LONG).show();
			} else {
				if (!toggleBtnVideoOrAudio.isChecked()) {

					if (circleTimeStep.length > 0)
						// call capture video
						onCaptureVideo();
					else
						Toast.makeText(CircleTimeActivity.this,
								"No Step is selected.Create a step to continue", Toast.LENGTH_LONG)
								.show();
				} else {
					Toast.makeText(CircleTimeActivity.this,
							"Only Timing.No Video and audio note is recorded", Toast.LENGTH_LONG)
							.show();
					gotoActivityInGroup(CircleTimeActivity.this, CircleTimeRecordingActivity.class);
				}
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_circle_timing);
		// initialize database
		initDataBase();
		// intialize control
		initControl();
	}

	/**
	 * initialize database
	 */
	private void initDataBase() {
		// share preference
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference.getInstance(this);
		_currentProcessIdActice = leanAppAndroidSharePreference.getProcessIdActive();
		_currentProcessNameActice = leanAppAndroidSharePreference.getProcessNameActive();
		_currentProjectIdActive = leanAppAndroidSharePreference.getProjectIdActive();
		_currentProjectNameActive = leanAppAndroidSharePreference.getProjectNameActive();
		Log.e("project id " + _currentProjectIdActive, "project name " + _currentProjectNameActive);
		// init database
		tStepsDataBaseHandler = new TStepsDataBaseHandler(this);
		tProcessDataBaseHandler = new TProcessDataBaseHandler(this);
		tCycleTimeDataBaseHandler = new TCycleTimeDataBaseHandler(this);
		//
		List<TCycleTimeDataBase> cycleList = tCycleTimeDataBaseHandler.getAllCycleTime();
		cycleCounter = cycleList.size();

	}

	/**
	 * initialize control
	 */
	private void initControl() {

		wheelCircleTimeProcess = (WheelView) findViewById(R.id.wheel_recording_1);
		wheelCircleTimeStep = (WheelView) findViewById(R.id.wheel_recording_2);
		wheelCircleTimeProcess
				.setViewAdapter(new CircleTimeArrayAdapter(this, circleTimeProcess, 0));
		wheelCircleTimeStep.setViewAdapter(new CircleTimeArrayAdapter(this, circleTimeStep, 0));
		wheelCircleTimeProcess.setVisibleItems(5);
		wheelCircleTimeProcess.setCurrentItem(0);
		wheelCircleTimeStep.setVisibleItems(5);
		wheelCircleTimeStep.setCurrentItem(0);

		// edit text
		editTextOperatorName = (EditText) findViewById(R.id.editext_operator_name);
		editTextShiftNumber = (EditText) findViewById(R.id.editext_shift_info);
		// toggle button
		toggleBtnVideoOrAudio = (ToggleButton) findViewById(R.id.toggle_btn_novideo);
		toggleBtnUseMs = (ToggleButton) findViewById(R.id.toggle_btn_use_ms);
		// button
		btnStartRecordVideo = (Button) findViewById(R.id.btn_cycle_time_video_record);
		btnStartRecordVideo.setOnClickListener(this);

		//
		btnExport = (ImageView) findViewById(R.id.img_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangeProject = (ImageView) findViewById(R.id.img_project_change_project);
		btnChangeProject.setOnClickListener(this);

		// textView
		textViewRecordingDetail = (TextView) findViewById(R.id.textView_recording_info);

	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class CircleTimeArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public CircleTimeArrayAdapter(Context context, String[] items, int current) {
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

	/**
	 * https://developer.android.com/guide/topics/media/camera.html
	 * **/
	@SuppressLint({ "SdCardPath", "SimpleDateFormat" })
	private void onCaptureVideo() {
		// start the Video Capture Intent
		Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(intent, REQUEST_VIDEO_CAPTURED);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_VIDEO_CAPTURED) {
				fileUri = data.getData();
				Toast.makeText(CircleTimeActivity.this, fileUri.getPath(), Toast.LENGTH_LONG)
						.show();
			}
		} else if (resultCode == RESULT_CANCELED) {
			fileUri = null;
			Toast.makeText(CircleTimeActivity.this, "Cancelled!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (fileUri != null) {

			cycleCounter++;
			operatorName = editTextOperatorName.getText().toString().trim();
			shiftNo = Integer.parseInt(editTextShiftNumber.getText().toString());
			// get wheel item
			if (circleStepId.length > 0) {
				int currentWheel = wheelCircleTimeStep.getCurrentItem();
				stepId = circleStepId[currentWheel];
				stepDescription = circleTimeStep[currentWheel];
			}

			// cycle time db values
			lowestTime = "";
			adjustment = "";
			adjustTime = "";
			audioNote = "";
			videoFileName = fileUri.getPath();
			startTimeStamp = "";
			endTimeStamp = "";
			versionId = -1;
			previousVersionId = -1;
			preVerDifferenceAdjustedTime = "";
			preVerDifferenceAdjustment = "";
			preVerDifferenceLowestTime = "";
			noVideoOnlyTiming = toggleBtnVideoOrAudio.isChecked();
			useMilliseconds = toggleBtnUseMs.isChecked();

			//
			/**
			 * 
			 */
			leanAppAndroidSharePreference.setFileName(videoFileName);

			// insert to cycle timedatabase
			tCycleTimeDataBaseHandler.addNewCycleTime(new TCycleTimeDataBase(cycleCounter,
					_currentProcessIdActice, _currentProjectIdActive, _currentProcessNameActice,
					_currentProjectNameActive, cycleCounter, operatorName, shiftNo, stepId,
					stepDescription, lowestTime, adjustment, adjustTime, audioNote, videoFileName,
					startTimeStamp, endTimeStamp, versionId, previousVersionId,
					preVerDifferenceLowestTime, preVerDifferenceAdjustedTime,
					preVerDifferenceAdjustment, noVideoOnlyTiming, useMilliseconds));
			gotoActivityInGroup(CircleTimeActivity.this, CircleTimeRecordingActivity.class);
		}
		if (_currentProjectIdActive != -1 && _currentProcessIdActice != -1) {
			textViewRecordingDetail.setText("		Select step to be recorded.		"
					+ _currentProjectNameActive + "-" + _currentProcessNameActice + "-Version 1");
			// set values for wheel
			setDataForWheel();

		} else {
			Toast.makeText(CircleTimeActivity.this,
					"No project or no process selected to continue", Toast.LENGTH_LONG).show();
		}
		Log.e("onResume", "onResume");

	}

	/**
	 * refresh data for wheel
	 */
	private void setDataForWheel() {
		List<TProcessDataBase> listProcess = tProcessDataBaseHandler
				.getAllProcess(_currentProjectIdActive);
		circleTimeProcess = new String[listProcess.size()];
		circleTimeProcessId = new int[listProcess.size()];
		for (int i = 0; i < listProcess.size(); i++) {
			circleTimeProcess[i] = listProcess.get(i).getProcessName();
			circleTimeProcessId[i] = listProcess.get(i).getProcessId();
		}
		List<TStepsDataBase> listStep = tStepsDataBaseHandler.getAllStep(_currentProcessIdActice);
		circleTimeStep = new String[listStep.size()];
		circleStepId = new int[listStep.size()];
		for (int i = 0; i < listStep.size(); i++) {
			circleTimeStep[i] = listStep.get(i).getStepDescription();
			circleStepId[i] = listStep.get(i).getStepID();
		}

		// refresh data for wheel process
		CircleTimeArrayAdapter circleTimeArrayAdapter = new CircleTimeArrayAdapter(this,
				circleTimeProcess, 0);
		wheelCircleTimeProcess.setViewAdapter(circleTimeArrayAdapter);
		wheelCircleTimeProcess.setCurrentItem(0);
		// refresh data for wheel step
		CircleTimeArrayAdapter circleTimeArrayAdapter2 = new CircleTimeArrayAdapter(this,
				circleTimeStep, 0);
		wheelCircleTimeStep.setViewAdapter(circleTimeArrayAdapter2);
		wheelCircleTimeStep.setCurrentItem(0);
	}

	public void gotoActivityInGroup(Context context, Class<?> cla) {
		Intent previewMessage = new Intent(this, cla);
		// TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		this.push(cla.getSimpleName(), previewMessage);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
}
