package vsvteam.outsource.leanappandroid.activity.spaghettichart;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SpaghettiChartActivity extends VSVTeamBaseActivity implements OnClickListener {
	//=============================Control Define ==============================
	private WheelView wheelViewVersion1;
	private WheelView wheelViewVerision2;
	private WheelView wheelViewProject;
	//=============================Class Define ================================
	//=============================Variable Define =============================
	private String[] version1 = { "Version 1", "Version 2", "Version 3", "Version 4" };
	private String[] version2 = { "Version 1", "Version 2", "Version 3", "Version 4" };
	private String[] project = { "Project 1", "Project 2", "Project 3", "Project 4" };
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_chart_detail);
		//
		wheelViewProject = (WheelView)findViewById(R.id.wheel_chart_detail_3);
		wheelViewVersion1 = (WheelView)findViewById(R.id.wheel_chart_detail_1);
		wheelViewVerision2 = (WheelView)findViewById(R.id.wheel_chart_detail_2);
		//
		wheelViewProject.setVisibleItems(5);
		wheelViewProject.setCurrentItem(0);
		wheelViewProject.setViewAdapter(new ChartDetailArrayAdapter(this, project, 0));
		//
		wheelViewVersion1.setVisibleItems(5);
		wheelViewVersion1.setCurrentItem(0);
		wheelViewVersion1.setViewAdapter(new ChartDetailArrayAdapter(this, project, 0));
		//
		wheelViewVerision2.setVisibleItems(5);
		wheelViewVerision2.setCurrentItem(0);
		wheelViewVerision2.setViewAdapter(new ChartDetailArrayAdapter(this, project, 0));
		
	}
	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class ChartDetailArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public ChartDetailArrayAdapter(Context context, String[] items, int current) {
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
