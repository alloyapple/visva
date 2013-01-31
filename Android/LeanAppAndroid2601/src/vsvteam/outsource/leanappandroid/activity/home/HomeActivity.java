package vsvteam.outsource.leanappandroid.activity.home;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.pqpr.PQPRActivity;
import vsvteam.outsource.leanappandroid.activity.spaghettichart.SpaghettiChartActivity;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupActivity;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupCircleTimingActivity;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupPQPRActivity;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupProcessActivity;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupSpaghettiChartActivity;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupTaktTimeActivity;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupValueStreamMapActivity;

@SuppressWarnings("deprecation")
public class HomeActivity extends TabActivity {

	// ==========================Control Define ===================
	// ==========================Class Define =====================
	public static HomeActivity instance;
	// ==========================Variable Define ==================
	private String TAG = "HomeActivity";
	private int selecttab = 0;
	private int currenttab;
	public static boolean isFirstRun = true;
	private static TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		instance = this;
		setContentView(R.layout.page_home_tabbar);

		setTabs();
		tabHost = getTabHost();

		tabHost.setCurrentTab(0);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				selecttab = tabHost.getCurrentTab();
			}
		});

		// reset current tab
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			final int tabIndex = i;
			getTabWidget().getChildAt(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setCurrentTab(tabIndex);
					if (TabGroupActivity.getInstance() != null) {
						TabGroupActivity.getInstance().resetChildActivities();
					}
				}
			});
		}
	}

	public static HomeActivity getInstance() {
		return instance;
	}

	private void setTabs() {
		// addTab(this.getString(R.string.text_tab_home),
		// R.drawable.ic_tab_home,
		// TabGroupHomeActivity.class, false);
		addTab(this.getString(R.string.value_stream_map), R.drawable.ic_stream_map,
				TabGroupValueStreamMapActivity.class, false);
		addTab(this.getString(R.string.task_time), R.drawable.ic_task_time,
				TabGroupTaktTimeActivity.class, false);
		addTab(this.getString(R.string.cycle_time), R.drawable.ic_cycle_time,
				TabGroupCircleTimingActivity.class, false);
		addTab(this.getString(R.string.spaghetti_chart), R.drawable.ic_chart,
				TabGroupSpaghettiChartActivity.class, false);
		addTab(this.getString(R.string.pqpr), R.drawable.ic_qprn, TabGroupPQPRActivity.class, false);
	}

	/*
	 * refresh all tabs host for creating error takt time as no process
	 */
	public void setTabsForCreateErrorTaktTimeNoProcess() {
		// addTab(this.getString(R.string.text_tab_home),
		// R.drawable.ic_tab_home,
		// TabGroupHomeActivity.class, false);
		addTab(this.getString(R.string.value_stream_map), R.drawable.ic_stream_map,
				TabGroupProcessActivity.class, false);
		addTab(this.getString(R.string.task_time), R.drawable.ic_task_time,
				TabGroupTaktTimeActivity.class, false);
		addTab(this.getString(R.string.cycle_time), R.drawable.ic_cycle_time,
				TabGroupCircleTimingActivity.class, false);
		addTab(this.getString(R.string.spaghetti_chart), R.drawable.ic_chart,
				TabGroupSpaghettiChartActivity.class, false);
		addTab(this.getString(R.string.pqpr), R.drawable.ic_qprn, TabGroupPQPRActivity.class, false);
	}

	private void addTab(String labelId, int drawableId, Class<?> c, boolean isShowBadge) {

		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator,
				getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

	public void setCurrentTab(int number) {
		tabHost.setCurrentTab(number);
	}

	public void hideTabBar() {
		tabHost.getTabWidget().setVisibility(ViewGroup.GONE);
	}

	public void showTabBar() {
		tabHost.getTabWidget().setVisibility(ViewGroup.VISIBLE);
	}

	public void removeAllTabs() {
		tabHost.clearAllTabs();
	}

	public Context getContext() {
		return HomeActivity.this;
	}
}
