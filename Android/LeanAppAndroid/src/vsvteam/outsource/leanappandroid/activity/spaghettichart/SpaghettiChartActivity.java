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

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ActionChangeActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionSettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionVersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.activity.valuestreammap.ChoiceProjectActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

public class SpaghettiChartActivity extends VSVTeamBaseActivity implements
		OnClickListener {
	private ListView mListViewCompareStep;
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v==btnExport) {
			gotoActivityInGroup(SpaghettiChartActivity.this,
					ActionExportActivity.class);
		} else if (v==btnSetting) {
			gotoActivityInGroup(SpaghettiChartActivity.this,
					ActionSettingActivity.class);
		} else if (v==btnVersion) {
			gotoActivityInGroup(SpaghettiChartActivity.this,
					ActionVersionActivity.class);
		} else if (v==btnChangeProject) {
			gotoActivityInGroup(SpaghettiChartActivity.this,
					ActionChangeActivity.class);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_menu_bar_chart);
		mListViewCompareStep = (ListView) findViewById(R.id.id_listview_compare_step);
		mListViewCompareStep.setAdapter(new ListCompareStepAdapter(this));
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

	class ListCompareStepAdapter extends BaseAdapter {

		private Context mContext;

		public ListCompareStepAdapter(Context pContext) {
			// TODO Auto-generated constructor stub
			mContext = pContext;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View v = convertView;
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			v = mInflater.inflate(R.layout.item_list_compare_chart, null);
			return v;
		}
	}

}
