package vsvteam.outsource.leanappandroid.actionbar;

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

public class ExportActivity extends VSVTeamBaseActivity implements OnClickListener {
	// ============================Control Define============================
	private WheelView wheelViewExport1;
	private WheelView wheelViewExport2;
	private WheelView wheelViewExport3;
	private WheelView wheelViewExport4;
	private Button btnDone;
	private Button btnExport;
	// ============================Class Define =============================
	// ============================Variable Define===========================
	private String[] export1 = { "Part Document", "Part Document", "Part Document", "Part Document" };
	private String[] export2 = { "Open Box", "Extract Parts", "Put It On Table" };
	private String[] export3 = { "Project 1", "Project 2", "Project 3", "Project 4" };
	private String[] export4 = { "Version 1", "Version 2", "Version 3", "Version 4" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_export);

		btnDone = (Button) findViewById(R.id.btn_export_done);
		btnDone.setOnClickListener(this);
		btnExport = (Button) findViewById(R.id.btn_export);
		btnExport.setOnClickListener(this);

		wheelViewExport1 = (WheelView) findViewById(R.id.wheel_export_1);
		wheelViewExport2 = (WheelView) findViewById(R.id.wheel_export_2);
		wheelViewExport3 = (WheelView) findViewById(R.id.wheel_export_3);
		wheelViewExport4 = (WheelView) findViewById(R.id.wheel_export_4);
		//
		wheelViewExport1.setVisibleItems(5);
		wheelViewExport1.setCurrentItem(0);
		wheelViewExport1.setViewAdapter(new ProcessArrayAdapter(this, export1, 0));
		//
		wheelViewExport2.setVisibleItems(5);
		wheelViewExport2.setCurrentItem(0);
		wheelViewExport2.setViewAdapter(new ProcessArrayAdapter(this, export2, 0));
		//
		wheelViewExport3.setVisibleItems(5);
		wheelViewExport3.setCurrentItem(0);
		wheelViewExport3.setViewAdapter(new ProcessArrayAdapter(this, export3, 0));
		//
		wheelViewExport4.setVisibleItems(5);
		wheelViewExport4.setCurrentItem(0);
		wheelViewExport4.setViewAdapter(new ProcessArrayAdapter(this, export4, 0));
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class ProcessArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public ProcessArrayAdapter(Context context, String[] items, int current) {
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
	public void onClick(View v) {
		if (v == btnDone) {
			finish();
		} else if (v == btnExport) {
			finish();
		}
	}
}
