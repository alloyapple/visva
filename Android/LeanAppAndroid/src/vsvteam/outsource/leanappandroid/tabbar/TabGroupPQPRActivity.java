package vsvteam.outsource.leanappandroid.tabbar;

import vsvteam.outsource.leanappandroid.activity.circletiming.CircleTimeActivity;
import vsvteam.outsource.leanappandroid.activity.pqpr.PQPRActivity;
import android.content.Intent;
import android.os.Bundle;

public class TabGroupPQPRActivity extends TabGroupActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		push("PQPRActivity", new Intent(this, PQPRActivity.class));
	}
}
