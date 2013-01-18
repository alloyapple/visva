package vsvteam.outsource.leanappandroid.tabbar;

import vsvteam.outsource.leanappandroid.activity.circletiming.CircleTimeActivity;
import android.content.Intent;
import android.os.Bundle;

public class TabGroupCircleTimingActivity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		push("CircleTimeActivity", new Intent(this, CircleTimeActivity.class));
	}
}
