package vsvteam.outsource.leanappandroid.tabbar;

import vsvteam.outsource.leanappandroid.activity.valuestreammap.CreateProjectActivity;
import android.content.Intent;
import android.os.Bundle;

public class TabGroupProcessActivity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		push("CreateProjectActivity", new Intent(this, CreateProjectActivity.class));
	}
}
