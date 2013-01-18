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
		OnClickListener, IControl {

	private ImageView btnSetting;
	private ImageView btnExport;
	private ImageView btnVersion;
	private ImageView btnChangedProject;
	private RelativeLayout frDrawStreamMap;
	private int mControlId;
	private int idSrc[] = { R.drawable.arrow, R.drawable.line, R.drawable.box1,
			R.drawable.box2, R.drawable.box3, R.drawable.square };
	private int idSrcPress[] = { R.drawable.arrow_press, R.drawable.line_press,
			R.drawable.box1_press, R.drawable.box2_press,
			R.drawable.box3_press, R.drawable.square_press };
	private int idObject[] = { R.id.id_img_arrow, R.id.id_img_line,
			R.id.id_img_class_box, R.id.id_img_rectangle, R.id.id_img_file_box,
			R.id.id_img_square };
	private ImageView btnMapObjects[];
	private boolean flagSelect[];
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
		btnMapObjects = new ImageView[NUM_OBJECTS];
		flagSelect = new boolean[NUM_OBJECTS];
		for (int i = 0; i < NUM_OBJECTS; i++) {
			btnMapObjects[i] = (ImageView) findViewById(idObject[i]);
			btnMapObjects[i].setOnClickListener(this);
			flagSelect[i] = false;
		}
		btnChangedProject = (ImageView) findViewById(R.id.img_draw_map_change_project);
		btnChangedProject.setOnClickListener(this);
		btnExport = (ImageView) findViewById(R.id.img_draw_map_export);
		btnExport.setOnClickListener(this);
		btnSetting = (ImageView) findViewById(R.id.img_draw_map_setting);
		btnSetting.setOnClickListener(this);
		btnVersion = (ImageView) findViewById(R.id.img_draw_map_version);
		btnVersion.setOnClickListener(this);

	}

	public void initDrawFrame() {
		mSurfaceView = new StreamMapSurfaceView(this);
		mSurfaceView.mControl = this;
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
		}else {
			// for (int i = 0; i < NUM_OBJECTS; i++) {
			for (int i = 0; i < NUM_OBJECTS; i++) {
				if (view.getId() == idObject[i]) {
					flagSelect[i] = !flagSelect[i];
				} else {
					flagSelect[i] = false;
				}
				if (flagSelect[i])
					btnMapObjects[i].setImageResource(idSrcPress[i]);
				else
					btnMapObjects[i].setImageResource(idSrc[i]);
			}
			switch (view.getId()) {
			case R.id.id_img_arrow:
				if (flagSelect[0])
					mControlId = DfObjects.ARROW;
				else
					mControlId = DfObjects.NONE;
				break;
			case R.id.id_img_line:
				if (flagSelect[1])
					mControlId = DfObjects.LINE;
				else
					mControlId = DfObjects.NONE;
				break;
			case R.id.id_img_class_box:

				if (flagSelect[2])
					mControlId = DfObjects.CLASSBOX;
				else
					mControlId = DfObjects.NONE;
				break;
			case R.id.id_img_rectangle:

				if (flagSelect[3])
					mControlId = DfObjects.RECTANGLE;
				else
					mControlId = DfObjects.NONE;
				break;
			case R.id.id_img_file_box:

				if (flagSelect[4])
					mControlId = DfObjects.FILEBOX;
				else
					mControlId = DfObjects.NONE;
				break;
			case R.id.id_img_square:
				if (flagSelect[5])
					mControlId = DfObjects.SQUARE;
				else
					mControlId = DfObjects.NONE;
				break;

			default:
				break;
			}

			// }
		}
	}

	@Override
	public int getCurrentControl() {
		// TODO Auto-generated method stub
		return mControlId;
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
