package vsvteam.outsource.leanappandroid.activity.spaghettichart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ActionChangeActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionSettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionVersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DetailChartActivity extends VSVTeamBaseActivity implements
		OnClickListener {
	// =============================Control Define
	// ==============================
	private static final int ROWS = 5;
	private static final int COMLUNS = 4;
	private WheelView wheelViewVersion1;
	private WheelView wheelViewVerision2;
	private WheelView wheelViewProject;
	// =============================Class Define
	// ================================
	// =============================Variable Define
	// =============================
	private String[] project = { "Project 1", "Project 2", "Project 3",
			"Project 4" };
	private TableLayout mTable;
	private ImageView btnSetting;
	private ImageView btnExport;
	private ImageView btnVersion;
	private ImageView btnChangedProject;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnExport) {
			gotoActivityInGroup(DetailChartActivity.this, ActionExportActivity.class);
		} else if (v == btnSetting) {
			gotoActivityInGroup(DetailChartActivity.this, ActionSettingActivity.class);
		} else if (v == btnVersion) {
			gotoActivityInGroup(DetailChartActivity.this, ActionVersionActivity.class);
		} else if (v == btnChangedProject) {
			gotoActivityInGroup(DetailChartActivity.this, ActionChangeActivity.class);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_chart_detail);
		//
		wheelViewProject = (WheelView) findViewById(R.id.wheel_chart_detail_3);
		wheelViewVersion1 = (WheelView) findViewById(R.id.wheel_chart_detail_1);
		wheelViewVerision2 = (WheelView) findViewById(R.id.wheel_chart_detail_2);
		//
		wheelViewProject.setVisibleItems(5);
		wheelViewProject.setCurrentItem(0);
		wheelViewProject.setViewAdapter(new ChartDetailArrayAdapter(this,
				project, 0));
		//
		wheelViewVersion1.setVisibleItems(5);
		wheelViewVersion1.setCurrentItem(0);
		wheelViewVersion1.setViewAdapter(new ChartDetailArrayAdapter(this,
				project, 0));
		//
		wheelViewVerision2.setVisibleItems(5);
		wheelViewVerision2.setCurrentItem(0);
		wheelViewVerision2.setViewAdapter(new ChartDetailArrayAdapter(this,
				project, 0));
		btnExport = (ImageView) findViewById(R.id.img_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangedProject = (ImageView) findViewById(R.id.img_project_change_project);
		btnChangedProject.setOnClickListener(this);
		((FrameLayout) findViewById(R.id.id_fra_chart1))
				.addView(addCombineChartView(new int[] { 1, 2, 3, 4, 5 },
						new double[] { 10, 20, 30, 40, 50 }, new double[] { 10,
								20, 38, 21, 50 }));
		((FrameLayout) findViewById(R.id.id_fra_chart2))
				.addView(addCombineChartView(new int[] { 1, 2, 3, 4, 5 },
						new double[] { 10, 20, 30, 40, 50 }, new double[] { 10,
								20, 38, 21, 50 }));
		((FrameLayout) findViewById(R.id.id_fra_chart3))
				.addView(addCombineChartView(new int[] { 1, 2, 3, 4, 5 },
						new double[] { 10, 20, 30, 40, 50 }, new double[] { 10,
								20, 38, 21, 50 }));
		mTable = (TableLayout) findViewById(R.id.id_table_layout_chart);
		addTable();
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
		public ChartDetailArrayAdapter(Context context, String[] items,
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

	public GraphicalView addCombineChartView(int[] x, double[] value1,
			double[] value2) {
		XYSeriesRenderer Y1Renderer = new XYSeriesRenderer();
		Y1Renderer.setColor(Color.argb(250, 0, 210, 250));
		Y1Renderer.setPointStyle(PointStyle.DIAMOND);
		Y1Renderer.setFillPoints(true);
		Y1Renderer.setLineWidth(1);

		XYSeriesRenderer Y2Renderer = new XYSeriesRenderer();
		Y2Renderer.setColor(Color.BLUE);
		Y2Renderer.setPointStyle(PointStyle.DIAMOND);
		Y2Renderer.setFillPoints(true);
		Y2Renderer.setLineWidth(1);

		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer(2);
		multiRenderer.setChartTitle("Pareto Chart");
		multiRenderer.setTextTypeface(null, Typeface.BOLD);
		multiRenderer.setXLabels(1);
		multiRenderer.setYLabels(8);
		multiRenderer.setXTitle("");
		multiRenderer.setShowGridY(true);
		multiRenderer.setXLabelsAlign(Align.RIGHT);
		multiRenderer.setYLabelsAlign(Align.RIGHT, 0);
		multiRenderer.setYLabelsAlign(Align.LEFT, 1);
		multiRenderer.setYLabelsAngle(5f);
		multiRenderer.setStartAngle(5f);
		multiRenderer.setYAxisAlign(Align.RIGHT, 1);
		multiRenderer.setYTitle("Frequency", 0);

		multiRenderer.setBarSpacing(0.2);
		multiRenderer.setZoomButtonsVisible(false);
		multiRenderer.setYLabelsColor(0, Color.BLACK);
		multiRenderer.setYLabelsColor(1, Color.BLACK);
		multiRenderer.setLabelsColor(Color.BLACK);
		multiRenderer.addSeriesRenderer(Y1Renderer);
		multiRenderer.addSeriesRenderer(Y2Renderer);
		multiRenderer.addYTextLabel(10, "10%", 1);
		multiRenderer.addYTextLabel(20, "20%", 1);
		multiRenderer.addYTextLabel(30, "30%", 1);
		multiRenderer.addYTextLabel(40, "40%", 1);
		multiRenderer.addYTextLabel(50, "50%", 1);
		multiRenderer.setGridColor(Color.BLACK);
		multiRenderer.setMarginsColor(Color.WHITE);
		multiRenderer.setGridColor(0x606060);
		multiRenderer.setShowGridX(true);
		multiRenderer.setAxesColor(Color.BLACK);
		multiRenderer.setShowAxes(true);
		multiRenderer.setRange(new double[] { 0.5, 6, 0, 50 }, 0);
		multiRenderer.setRange(new double[] { 0.5, 6, 0, 50 }, 1);
		multiRenderer.setPanEnabled(false, false);
		multiRenderer.setMargins(new int[] { multiRenderer.getMargins()[0], 45,
				-20, 45 });

		multiRenderer.setShowLegend(false);
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries Y1 = new XYSeries("", 0);
		XYSeries Y2 = new XYSeries("", 1);
		for (int i = 0; i < x.length; i++) {
			Y1.add(x[i], value1[i]);
			Y2.add(x[i], value2[i]);
		}
		dataset.addSeries(Y1);
		dataset.addSeries(Y2);
		String[] types = new String[] { BarChart.TYPE, LineChart.TYPE };
		GraphicalView mChartView = (GraphicalView) ChartFactory
				.getCombinedXYChartView(getBaseContext(), dataset,
						multiRenderer, types);
		mChartView.setLayoutParams(new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		mChartView.setBackgroundColor(Color.LTGRAY);
		return mChartView;

	}

	public void addTable() {
		for (int i = 0; i < ROWS + 1; i++) {
			TableRow row = new TableRow(this);
			TableLayout.LayoutParams parms = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.FILL_PARENT,
					TableLayout.LayoutParams.FILL_PARENT, 1);
			row.setLayoutParams(parms);
			for (int j = 0; j < COMLUNS; j++) {
				TextView textView = new TextView(this);
				TableRow.LayoutParams parmChild = new TableRow.LayoutParams(
						TableLayout.LayoutParams.FILL_PARENT,
						TableLayout.LayoutParams.FILL_PARENT, 1);
				parmChild.setMargins(0, 0, 1, 1);
				textView.setLayoutParams(parmChild);

				if (i == 0) 
					textView.setBackgroundColor(Color.LTGRAY);
				else
					textView.setBackgroundColor(Color.WHITE);
				row.addView(textView);
			}
			mTable.addView(row);
		}
	}
}
