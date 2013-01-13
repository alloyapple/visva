package vsvteam.outsource.leanappandroid.actionbar;

import java.util.ArrayList;

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.adapter.SettingListAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SettingActivity extends VSVTeamBaseActivity implements OnClickListener {
	// ==========================Control Define ========================
	private ListView mySettingListView;
	private Button btnDoneSetting;
	// ==========================Class Define ==========================
	// ==========================Variable Define =======================
	private ArrayList<String> mTitleListPlaylist = new ArrayList<String>();

	@Override
	public void onClick(View v) {
		if (v == btnDoneSetting) {
			finish();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_setting);

		btnDoneSetting = (Button) findViewById(R.id.btn_done_setting);
		btnDoneSetting.setOnClickListener(this);

		mySettingListView = (ListView) findViewById(R.id.list_choice_setting);
		mTitleListPlaylist.add("			Meters/second");
		mTitleListPlaylist.add("			Kilometers/hour");
		mTitleListPlaylist.add("			Yards/hour");
		mTitleListPlaylist.add("			Miles/hour");
		SettingListAdapter adapter = new SettingListAdapter(this, mTitleListPlaylist);
		mySettingListView.setAdapter(adapter);
	}

}
