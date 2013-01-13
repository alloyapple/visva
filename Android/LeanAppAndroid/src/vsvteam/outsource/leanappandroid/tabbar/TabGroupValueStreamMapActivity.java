package vsvteam.outsource.leanappandroid.tabbar;

import vsvteam.outsource.leanappandroid.activity.valuestreammap.ChoiceProjectActivity;
import android.content.Intent;
import android.os.Bundle;


public class TabGroupValueStreamMapActivity extends TabGroupActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		push("ChoiceProjectActivity", new Intent(this, ChoiceProjectActivity.class));
	}
}
