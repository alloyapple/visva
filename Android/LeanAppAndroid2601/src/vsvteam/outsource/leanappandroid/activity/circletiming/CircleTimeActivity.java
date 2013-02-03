package vsvteam.outsource.leanappandroid.activity.circletiming;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ActionChangeActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionSettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionVersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.database.TCycleTimeDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TProcessDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TStepsDataBaseHandler;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
	private WheelView wheelCircleTime1;
	private WheelView wheelCircleTime2;

	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;

	private EditText editTextOperatorName;
	private EditText editTextShiftNumber;
	private ToggleButton toggleBtnVideoOrAudio;
	private ToggleButton toggleBtnUseMs;
	private Button btnStartRecordVideo;
	// ==============================Class Define ===========================
	private TProcessDataBaseHandler tProcessDataBaseHandler;
	private TStepsDataBaseHandler tStepsDataBaseHandler;
	private TCycleTimeDataBaseHandler tCycleTimeDataBaseHandler;
	// ==============================Variable Define ========================
	private static final String TAG = CircleTimeActivity.class.getSimpleName();
	private static final int REQUEST_VIDEO_CAPTURED = 1002;
	private Uri fileUri;
	private String mCameraFileName;
	private int projectIdActive;
	
	private String[] circleTime1 = { "Part Procurement", "Part Procurement", "Part Procurement",
			"Part Procurement", "Part Procurement" };
	private String[] circleTime2 = { "Open Box", "Extract Parts", "Put It On Table", "Open Box" };

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
			if (toggleBtnVideoOrAudio.isChecked()) {
				// gotoActivityInGroup(CircleTimeActivity.this,
				// CircleTimeRecordingActivity.class);
				// call capture video
				onCaptureVideo();
			} else
				Toast.makeText(CircleTimeActivity.this, "Audio Note", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_circle_timing);

		// intialize control
		initialize();

	}

	private void initialize() {

		wheelCircleTime1 = (WheelView) findViewById(R.id.wheel_recording_1);
		wheelCircleTime2 = (WheelView) findViewById(R.id.wheel_recording_2);
		wheelCircleTime1.setViewAdapter(new TaktTimeArrayAdapter(this, circleTime1, 0));
		wheelCircleTime2.setViewAdapter(new TaktTimeArrayAdapter(this, circleTime2, 0));
		wheelCircleTime1.setVisibleItems(5);
		wheelCircleTime1.setCurrentItem(0);
		wheelCircleTime2.setVisibleItems(5);
		wheelCircleTime2.setCurrentItem(0);

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
		Log.e("adjfhaskdjfh " + requestCode, "adfjuahsdfkuh " + data.getFlags());
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
			gotoActivityInGroup(CircleTimeActivity.this, CircleTimeRecordingActivity.class);
		}

	}

	public void gotoActivityInGroup(Context context, Class<?> cla) {
		Intent previewMessage = new Intent(this, cla);
		// TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		this.push(cla.getSimpleName(), previewMessage);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}
}
