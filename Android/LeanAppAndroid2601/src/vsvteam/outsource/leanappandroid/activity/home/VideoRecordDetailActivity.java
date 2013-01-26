package vsvteam.outsource.leanappandroid.activity.home;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.circletiming.VariationSpotterActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VideoRecordDetailActivity extends VSVTeamBaseActivity implements OnClickListener {
	// =============================Control Define =========================
	private WheelView wheelRecordDetail1;
	private WheelView wheelRecordDetail2;
	private WheelView wheelRecordDetail3;
	private Button btnCompare;
	// ==========================Class Define ==============================
	// ==========================Variable Define ===========================
	private String[] record_detail = { "Cycle 1 Open Box", "Cycle 1 Extract Goods",
			"Cycle 1 Parts On Table" };
	private String[] record_detail2 = { "Cycle 1 Operator Frank", "Cycle 2 Operator John",
			"Cycle 3 Operator Kane" };

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnCompare) {
			Intent i = new Intent(VideoRecordDetailActivity.this, VariationSpotterActivity.class);
			startActivity(i);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_variation_spotter);
		// btnCompare = (Button)findViewById(R.id.btn_recording_compare_steps);
		// btnCompare.setOnClickListener(this);

		wheelRecordDetail1 = (WheelView) findViewById(R.id.wheel_spotter_cycle_1);
		wheelRecordDetail2 = (WheelView) findViewById(R.id.wheel_spotter_cycle_2);
		wheelRecordDetail3 = (WheelView) findViewById(R.id.wheel_spotter_cycle_3);
		//
		wheelRecordDetail1.setVisibleItems(5);
		wheelRecordDetail1.setCurrentItem(0);
		wheelRecordDetail1.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail, 0));

		wheelRecordDetail2.setVisibleItems(5);
		wheelRecordDetail2.setCurrentItem(0);
		wheelRecordDetail2.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail, 0));

		wheelRecordDetail3.setVisibleItems(5);
		wheelRecordDetail3.setCurrentItem(0);
		wheelRecordDetail3.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail2, 0));
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class RecoredDetailArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public RecoredDetailArrayAdapter(Context context, String[] items, int current) {
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
