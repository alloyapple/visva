package vsvteam.outsource.leanappandroid.activity.circletiming;

import java.io.File;
import java.text.DateFormat;
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

public class CircleTimeActivity extends VSVTeamBaseActivity implements
		OnClickListener {
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
	// ==============================Variable Define ========================
	private static final String TAG = CircleTimeActivity.class.getSimpleName();
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private Uri fileUri;
	private String mCameraFileName;
	private String[] circleTime1 = { "Part Procurement", "Part Procurement",
			"Part Procurement", "Part Procurement", "Part Procurement" };
	private String[] circleTime2 = { "Open Box", "Extract Parts",
			"Put It On Table", "Open Box" };

	@Override
	public void onClick(View v) {
		if (v == btnExport) {
			gotoActivityInGroup(CircleTimeActivity.this,
					ActionExportActivity.class);
		} else if (v == btnSetting) {
			gotoActivityInGroup(CircleTimeActivity.this,
					ActionSettingActivity.class);
		} else if (v == btnVersion) {
			gotoActivityInGroup(CircleTimeActivity.this,
					ActionVersionActivity.class);

		} else if (v == btnChangeProject) {
			gotoActivityInGroup(CircleTimeActivity.this,
					ActionChangeActivity.class);
		} else if (v == btnStartRecordVideo) {
			if (toggleBtnVideoOrAudio.isChecked()) {
				// gotoActivityInGroup(CircleTimeActivity.this,
				// CircleTimeRecordingActivity.class);
				// call capture video
				onCaptureVideo();
			} else
				Toast.makeText(CircleTimeActivity.this, "Audio Note",
						Toast.LENGTH_LONG).show();
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
		wheelCircleTime1.setViewAdapter(new TaktTimeArrayAdapter(this,
				circleTime1, 0));
		wheelCircleTime2.setViewAdapter(new TaktTimeArrayAdapter(this,
				circleTime2, 0));
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
		btnExport = (ImageView) findViewById(R.id.img_choice_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_choice_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_choice_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangeProject = (ImageView) findViewById(R.id.img_choice_project_change_project);
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
	private void onCaptureVideo() {
		// create new Intent
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		// fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO); // create a file
		// to save the video in specific folder (this works for video only)
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image
		// file name
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String newPicFile = df.format(date) + ".mp4";
		String outPath = "/sdcard/" + newPicFile;
		File outFile = new File(outPath);

		mCameraFileName = outFile.toString();
		Uri outuri = Uri.fromFile(outFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
		Log.i(TAG, "Importing New Picture: " + mCameraFileName);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video
															// image quality to
															// high

		// start the Video Capture Intent
		startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v(TAG, "result code " + resultCode);
		if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				// Video captured and saved to fileUri specified in the Intent
				fileUri = (Uri) data.getData();
				Log.v("file urri	", "file URi " + fileUri);
				if (fileUri != null) {
					Log.d(TAG, "Video saved to:\n" + fileUri);
					Log.d(TAG, "Video path:\n" + fileUri.getPath());
					Log.d(TAG, "Video name:\n" + getName(fileUri)); // use
																	// uri.getLastPathSegment()
																	// if store
																	// in folder
				}

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the video capture
			} else {
				// Video capture failed, advise user
			}
		}
	}

	/**
	 * Create a file Uri for saving an image or video to specific folder
	 * https://developer.android.com/guide/topics/media/camera.html#saving-media
	 * */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted

		if (Environment.getExternalStorageState() != null) {
			// this works for Android 2.2 and above
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"AndroidCameraTestsFolder");

			// This location works best if you want the created images to be
			// shared
			// between applications and persist after your app has been
			// uninstalled.

			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					Log.d(TAG, "failed to create directory");
					return null;
				}
			}

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			File mediaFile;
			if (type == MEDIA_TYPE_IMAGE) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "IMG_" + timeStamp + ".jpg");
			} else if (type == MEDIA_TYPE_VIDEO) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "VID_" + timeStamp + ".mp4");
			} else {
				return null;
			}

			return mediaFile;
		}

		return null;
	}

	// grab the name of the media from the Uri
	protected String getName(Uri uri) {
		String filename = null;

		try {
			String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };
			Cursor cursor = managedQuery(uri, projection, null, null, null);

			if (cursor != null && cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
				filename = cursor.getString(column_index);
			} else {
				filename = null;
			}
		} catch (Exception e) {
			Log.e(TAG, "Error getting file name: " + e.getMessage());
		}

		return filename;
	}

}
