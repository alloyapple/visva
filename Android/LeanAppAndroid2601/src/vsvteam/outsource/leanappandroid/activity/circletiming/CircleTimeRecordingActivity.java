package vsvteam.outsource.leanappandroid.activity.circletiming;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CircleTimeRecordingActivity extends VSVTeamBaseActivity implements OnClickListener {
	// =============================Control Define =============================
	private WheelView wheelCircleTime1;
	private WheelView wheelCircleTime2;
	private ImageView imgRecording;
	private TextView textViewTimeRecording;
	private TextView textViewNextStep;
	private TextView textViewStepName_Time;
	private Button btnSaveStop;
	private Button btnAudioNote;
	private Button btnNextStep;
	private Button btnStartRecording;
	// =============================Class Define ===============================
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	// =============================Variable Define ===========================
	private String[] circleTime1 = { "Part Procurement", "Part Procurement", "Part Procurement",
			"Part Procurement", "Part Procurement" };
	private String[] circleTime2 = { "Open Box", "Extract Parts", "Put It On Table", "Open Box" };

	@Override
	public void onClick(View v) {
		if (v == btnSaveStop) {

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
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference.getInstance(this);
	}

	/**
	 * initialize control
	 */
	private void initControl() {
		// wheel
		wheelCircleTime1 = (WheelView) findViewById(R.id.wheel_recording_1);
		wheelCircleTime2 = (WheelView) findViewById(R.id.wheel_recording_2);
		wheelCircleTime1.setViewAdapter(new TaktTimeArrayAdapter(this, circleTime1, 0));
		wheelCircleTime2.setViewAdapter(new TaktTimeArrayAdapter(this, circleTime2, 0));
		wheelCircleTime1.setVisibleItems(5);
		wheelCircleTime1.setCurrentItem(0);
		wheelCircleTime2.setVisibleItems(5);
		wheelCircleTime2.setCurrentItem(0);

		// textview
		textViewNextStep = (TextView) findViewById(R.id.textView_recording_next_step);
		textViewStepName_Time = (TextView) findViewById(R.id.textView_recording_step_name_time);
		textViewTimeRecording = (TextView) findViewById(R.id.textView_recording_time);
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
