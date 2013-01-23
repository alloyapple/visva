package vsvteam.outsource.leanappandroid.activity.circletiming;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;

public class CircleTimeDetailActivity extends VSVTeamBaseActivity implements OnClickListener {
	//=============================Control Define ==========================
	private Button btnCompare;
	private Button btnPlay;
	private WheelView wheelSpotter1;
	private WheelView wheelSpotter2;
	private WheelView wheelSpotter3;
	//=============================Class Define =============================
	//=============================Variable Define ==========================
	private String[] record_detail = { "Cycle 1 Open Box", "Cycle 1 Extract Goods",
			"Cycle 1 Parts On Table" };
	private String[] record_detail2 = { "Cycle 1 Operator Frank", "Cycle 2 Operator John",
			"Cycle 3 Operator Kane" };

	@Override
	public void onClick(View v) {
		if (v == btnCompare) {
			Log.e("orientation", "orientaion " + _getScreenOrientation());
			if (_getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
				Intent intentSpotter = new Intent(CircleTimeDetailActivity.this,
						VariationSpotterActivity.class);
				startActivity(intentSpotter);
			}
		} else if (v == btnPlay) {
			gotoActivityInGroup(CircleTimeDetailActivity.this, CircleTimeVideoViewActivity.class);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_recording_detail);
		btnCompare = (Button) findViewById(R.id.btn_recording_detail_compare);
		btnPlay = (Button) findViewById(R.id.btn_recording_detail_play);
		btnCompare.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		if (_getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
			wheelSpotter1 = (WheelView) findViewById(R.id.wheel_recording_cycle_1);
			wheelSpotter2 = (WheelView) findViewById(R.id.wheel_recording_cycle_2);
			wheelSpotter3 = (WheelView) findViewById(R.id.wheel_recording_cycle_3);
			//
			wheelSpotter1.setVisibleItems(5);
			wheelSpotter1.setCurrentItem(0);
			wheelSpotter1.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail, 0));

			wheelSpotter2.setVisibleItems(5);
			wheelSpotter2.setCurrentItem(0);
			wheelSpotter2.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail, 0));

			wheelSpotter3.setVisibleItems(5);
			wheelSpotter3.setCurrentItem(0);
			wheelSpotter3.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail2, 0));
		}
	}

	private int _getScreenOrientation() {
		return getResources().getConfiguration().orientation;
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
