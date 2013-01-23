package vsvteam.outsource.leanappandroid.activity.circletiming;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class VariationSpotterActivity extends VSVTeamBaseActivity implements OnClickListener {
	// =====================Control Define =========================
	private WheelView wheelSpotter1;
	private WheelView wheelSpotter2;
	private WheelView wheelSpotter3;
	private Button btnCancel;
	private Button btnCompare;
	// =====================Class Define ===========================
	// =====================Variable Define ========================
	private String[] record_detail = { "Cycle 1 Open Box", "Cycle 1 Extract Goods",
			"Cycle 1 Parts On Table" };
	private String[] record_detail2 = { "Cycle 1 Operator Frank", "Cycle 2 Operator John",
			"Cycle 3 Operator Kane" };

	@Override
	public void onClick(View v) {
		if (v == btnCancel)
			finish();
		else if (v == btnCompare)
			finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_variation_spotter);

		wheelSpotter1 = (WheelView) findViewById(R.id.wheel_spotter_cycle_1);
		wheelSpotter2 = (WheelView) findViewById(R.id.wheel_spotter_cycle_2);
		wheelSpotter3 = (WheelView) findViewById(R.id.wheel_spotter_cycle_3);
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

		btnCancel = (Button) findViewById(R.id.btn_spotter_cancel);
		btnCancel.setOnClickListener(this);
		btnCompare = (Button) findViewById(R.id.btn_spotter_compare);
		btnCompare.setOnClickListener(this);
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
