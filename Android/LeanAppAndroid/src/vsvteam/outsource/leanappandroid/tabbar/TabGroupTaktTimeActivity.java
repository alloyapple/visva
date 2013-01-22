package vsvteam.outsource.leanappandroid.tabbar;

import vsvteam.outsource.leanappandroid.activity.takttime.TaktTimeActivity;
import android.content.Intent;
import android.os.Bundle;

public class TabGroupTaktTimeActivity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		push("TaktTimeActivity", new Intent(this, TaktTimeActivity.class));
	}
}
