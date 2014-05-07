package vsvteam.outsource.android.soundeffect;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import vsvteam.outsource.android.soundeffect.R;
import vsvteam.outsource.android.soundeffect.adapter.MyRecordListAdapter;
import vsvteam.outsource.android.soundeffect.layout.RecordManager;
import vsvteam.outsource.android.soundeffect.quickaction.ActionItem;
import vsvteam.outsource.android.soundeffect.quickaction.QuickAction;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyRecordActivity extends Activity implements OnItemClickListener,
		OnItemLongClickListener {
	// ==============================Control Define=================
	private ListView myRecordListView;
	private AdView layoutAds;
	// ==============================Class Define===================
	private RecordManager recordManager;
	private QuickAction mQuickAction;
	// ==============================Variable Define================
	private ArrayList<String> recordsListItem;
	private ArrayList<HashMap<String, String>> recordList = new ArrayList<HashMap<String, String>>();
	private int currentIndex;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == myRecordListView) {
			currentIndex = arg2;
			mQuickAction.show(arg1);
			mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
		}
	}

	private void initQuickAction() {
		// Play chipmunk style action item
		ActionItem addAction = new ActionItem();

		addAction.setTitle("Play ChipMunk Style");
		addAction.setIcon(getResources().getDrawable(R.drawable.ic_play));
		ActionItem playNormalAction = new ActionItem();
		playNormalAction.setTitle("Play Normal Style");
		playNormalAction.setIcon(getResources().getDrawable(R.drawable.ic_play));
		// Accept action item
		ActionItem accAction = new ActionItem();

		accAction.setTitle("Rename");
		accAction.setIcon(getResources().getDrawable(R.drawable.ic_rename));

		// Upload action item
		ActionItem upAction = new ActionItem();

		upAction.setTitle("Delete");
		upAction.setIcon(getResources().getDrawable(R.drawable.ic_delete));

		mQuickAction = new QuickAction(this);

		mQuickAction.addActionItem(addAction);
		mQuickAction.addActionItem(playNormalAction);
		mQuickAction.addActionItem(accAction);
		mQuickAction.addActionItem(upAction);

		// setup the action item click listener
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(int pos) {

				if (pos == 0) { // Play chipmunk style item selected
					playAudioFile(currentIndex, true);

				} else if (pos == 1) {// play auto tune style item seleted
					playAudioFile(currentIndex, false);
				} else if (pos == 2) { // Rename item selected
					renameAudioFile(currentIndex);
				} else if (pos == 3) { // delete item selected
					deleteAudioFile(currentIndex);
					// resetListView
					initialize();
					myRecordListView.invalidate();
				}
			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_myrecord_list);
		initialize();
		initQuickAction();
		// Look up the AdView as a resource and load a request.
		layoutAds = (AdView) this.findViewById(R.id.main_adView);
		// Initiate a generic request to load it with an ad
		AdRequest adRequest = new AdRequest();
		layoutAds.loadAd(adRequest);
		layoutAds.bringToFront();
	}

	private void initialize() {
		recordManager = new RecordManager(this);
		recordList = recordManager.getPlayList();
		recordsListItem = initRecordListItem(recordList);
		myRecordListView = (ListView) findViewById(R.id.myrecord_list);
		MyRecordListAdapter adapter = new MyRecordListAdapter(this, recordsListItem);
		myRecordListView.setAdapter(adapter);
		myRecordListView.setOnItemClickListener(this);
		myRecordListView.setOnItemLongClickListener(this);
		adapter.notifyDataSetChanged();
	}

	private ArrayList<String> initRecordListItem(ArrayList<HashMap<String, String>> recordList2) {
		ArrayList<String> arrNameList = new ArrayList<String>();
		for (int i = 0; i < recordList2.size(); i++) {
			arrNameList.add(recordList2.get(i).get("recordTitle"));
		}
		return arrNameList;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == myRecordListView) {
			currentIndex = arg2;
			mQuickAction.show(arg1);
			mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
		}
		return true;
	}

	/**
	 * delete audio file at position
	 * 
	 * @param position
	 */
	private void deleteAudioFile(int position) {
		File dir = new File(recordList.get(position).get("recordPath"));
		if (dir.exists()) {
			dir.delete();
		}
	}

	/**
	 * rename audio file at position to fileName
	 * 
	 * @param position
	 * @param fileName
	 */
	private void renameAudioFile(int position) {
		Intent intent = new Intent(MyRecordActivity.this, FileRenameActivity.class);
		intent.putExtra("fileRenamePath", recordList.get(position).get("recordPath"));
		Log.e("filePath", "file " + recordList.get(position).get("recordPath"));
		startActivity(intent);
	}

	/**
	 * play audio file at position
	 * 
	 * @param position
	 */
	private void playAudioFile(int position, boolean status) {
		Intent intent = new Intent(MyRecordActivity.this, PlayChipMunkStyleAcitivity.class);
		intent.putExtra("filePath", recordList.get(position).get("recordPath"));
		intent.putExtra("status", status);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
		myRecordListView.invalidate();
	}
}
