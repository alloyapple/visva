package vsvteam.outsource.leanappandroid.actionbar;

import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.activity.valuestreammap.ChoiceProjectActivity;
import vsvteam.outsource.leanappandroid.activity.valuestreammap.CreateProjectActivity;
import vsvteam.outsource.leanappandroid.database.TProcessDataBase;
import vsvteam.outsource.leanappandroid.database.TProcessDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TProjectDataBase;
import vsvteam.outsource.leanappandroid.database.TProjectDatabaseHandler;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionChangeActivity extends VSVTeamBaseActivity implements
		OnClickListener {
	private static final byte DARK = 1;
	private static final byte BRIGHT = 2;
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;

	private WheelView mWheelProject;
	private WheelView mWheelProcess;
	private TProjectDatabaseHandler mProjectDataBaseHandler;
	private TProcessDataBaseHandler mProcessDataBaseHandler;
	private List<TProjectDataBase> listProjectDatabase;
	private List<TProcessDataBase> listProcessDatabase;
	private int idProjectSelect = -1;
	private int idProcessSelect = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_action_change);
		visibleView((FrameLayout) findViewById(R.id.id_frame_parent_change),
				(FrameLayout) findViewById(R.id.id_fr_change),
				View.VISIBLE);
		btnExport = (ImageView) findViewById(R.id.img_choice_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_choice_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_choice_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangeProject = (ImageView) findViewById(R.id.img_choice_project_change_project);
		btnChangeProject.setOnClickListener(this);
		((Button) findViewById(R.id.id_done_change)).setOnClickListener(this);
		((Button) findViewById(R.id.id_new_pro_change))
				.setOnClickListener(this);
		mWheelProject = (WheelView) findViewById(R.id.id_wheelview_change_1);
		mProjectDataBaseHandler = new TProjectDatabaseHandler(this);
		listProjectDatabase = mProjectDataBaseHandler.getAllProjects();
		String[] mProjects = new String[listProjectDatabase.size()];
		for (int i = 0; i < listProjectDatabase.size(); i++) {
			mProjects[i] = listProjectDatabase.get(i).getProjectName();
		}
		mWheelProject.setVisibleItems(5);
		mWheelProject
				.setViewAdapter(new ProjectArrayAdapter(this, mProjects, 1));
		mWheelProject.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					setAdapterWheelProcess();
					break;
				default:
					break;
				}
				return false;
			}
		});
		mWheelProcess = (WheelView) findViewById(R.id.id_wheelview_change_2);
		mProcessDataBaseHandler = new TProcessDataBaseHandler(this);
		listProcessDatabase = mProcessDataBaseHandler
				.getAllProcess(idProjectSelect);
		String[] mProcess = new String[listProcessDatabase.size()];
		for (int i = 0; i < listProcessDatabase.size(); i++) {
			mProcess[i] = listProcessDatabase.get(i).getProcessName();
		}
		mWheelProcess.setVisibleItems(5);
		mWheelProcess
				.setViewAdapter(new ProjectArrayAdapter(this, mProcess, 0));
	}

	public void setAdapterWheelProcess() {
		idProjectSelect = mWheelProject.getCurrentItem();
		if (listProjectDatabase.size() > 0)
			listProcessDatabase = mProcessDataBaseHandler
					.getAllProcess(listProjectDatabase.get(idProjectSelect)
							.getProjectID());
		String[] mProcess = new String[listProcessDatabase.size()];
		for (int i = 0; i < listProcessDatabase.size(); i++) {
			mProcess[i] = listProcessDatabase.get(i).getProcessName();
		}
		mWheelProcess.setVisibleItems(5);
		mWheelProcess
				.setViewAdapter(new ProjectArrayAdapter(this, mProcess, 0));
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class ProjectArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public ProjectArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(24);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	public void setTheme(View v, byte type) {
		AlphaAnimation alpha;
		if (type == DARK)
			alpha = new AlphaAnimation(0.3F, 0.3F);
		else
			alpha = new AlphaAnimation(1.0F, 1.0F);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		// And then on your layout
		v.startAnimation(alpha);

	}

	public void visibleView(View parent, View v, int visible) {
		if (visible == View.VISIBLE) {
			setTheme(parent, DARK);
			v.setVisibility(View.VISIBLE);
			ScaleAnimation scale = new ScaleAnimation(1, 1, 0, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0f);
			scale.setDuration(500);
			v.startAnimation(scale);
		} else {
			ScaleAnimation scale = new ScaleAnimation(1, 1, 1, 0,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1f);
			scale.setDuration(300);
			v.startAnimation(scale);
			v.setVisibility(View.GONE);
			setTheme(parent, BRIGHT);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_done_change:
			gotoActivityInGroup(this, ChoiceProjectActivity.class);
			break;
		case R.id.id_new_pro_change:
			gotoActivityInGroup(this, CreateProjectActivity.class);
			break;
		default:
			break;
		}
	}
}
