package vsvteam.outsource.leanappandroid.activity.valuestreammap;

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ActionChangeActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionSettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionVersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.mapobjects.DfObjects;
import vsvteam.outsource.leanappandroid.mapobjects.IControl;
import vsvteam.outsource.leanappandroid.mapobjects.StreamMapSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DrawMapActivity extends VSVTeamBaseActivity implements
		OnClickListener{

	private ImageView btnSetting;
	private ImageView btnExport;
	private ImageView btnVersion;
	private ImageView btnChangedProject;
	private RelativeLayout frDrawStreamMap;

	public static final int NUM_OBJECTS = 6;
	private StreamMapSurfaceView mSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_draw_map);
		intitalize();
		initDrawFrame();
	}

	private void intitalize() {
		frDrawStreamMap = (RelativeLayout) findViewById(R.id.id_fr_draw_map);
		
		btnExport = (ImageView) findViewById(R.id.img_choice_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_choice_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_choice_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangedProject = (ImageView) findViewById(R.id.img_choice_project_change_project);
		btnChangedProject.setOnClickListener(this);

	}

	public void initDrawFrame() {
		mSurfaceView = new StreamMapSurfaceView(this);
		frDrawStreamMap.addView(mSurfaceView);

	}

	@Override
	public void onClick(View view) {
		if (view == btnExport) {
			gotoActivityInGroup(DrawMapActivity.this,
					ActionExportActivity.class);
		} else if (view == btnSetting) {
			gotoActivityInGroup(DrawMapActivity.this,
					ActionSettingActivity.class);
		} else if (view == btnVersion) {
			gotoActivityInGroup(DrawMapActivity.this,
					ActionVersionActivity.class);
		} else if (view == btnChangedProject) {
			gotoActivityInGroup(DrawMapActivity.this,
					ActionChangeActivity.class);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.e("action down", "ok");
			mSurfaceView.onTouchDown(event);
			break;
		case MotionEvent.ACTION_UP:
			mSurfaceView.onTouchUp(event);
			Log.e("action up", "ok");
			break;
		case MotionEvent.ACTION_MOVE:
			mSurfaceView.onTouchMove(event);
			Log.e("action move", "ok");
			break;
		default:
			break;
		}
		return true;
	}
}
