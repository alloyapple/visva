package vsvteam.outsource.leanappandroid.tabbar;

import vsvteam.outsource.leanappandroid.activity.circletiming.CircleTimeActivity;
import vsvteam.outsource.leanappandroid.activity.spaghettichart.SpaghettiChartActivity;
import android.content.Intent;
import android.os.Bundle;

public class TabGroupSpaghettiChartActivity extends TabGroupActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		push("SpaghettiChartActivity", new Intent(this, SpaghettiChartActivity.class));
	}
}
