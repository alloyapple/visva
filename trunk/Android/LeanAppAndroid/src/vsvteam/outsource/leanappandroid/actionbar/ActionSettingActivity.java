package vsvteam.outsource.leanappandroid.actionbar;

import vsvteam.outsource.leanappandroid.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ActionSettingActivity extends Activity implements OnClickListener{
	private static final byte DARK = 1;
	private static final byte BRIGHT = 2;
	private int idCheckBoxDistanceTime[] = {
			R.id.id_checkbox_distance_time_m_s,
			R.id.id_checkbox_distance_time_km_h,
			R.id.id_checkbox_distance_time_ya_se,
			R.id.id_checkbox_distance_time_mile_h };
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;
	private OnCheckedChangeListener mListenerDistanceTime = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				for (int j = 0; j < 4; j++) {
					if (buttonView.getId() == idCheckBoxDistanceTime[j]) {
						((CheckBox) findViewById(idCheckBoxDistanceTime[j]))
								.setChecked(true);
					} else {
						((CheckBox) findViewById(idCheckBoxDistanceTime[j]))
								.setChecked(false);
					}
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_action_setting);
		visibleView((FrameLayout) findViewById(R.id.id_frame_parent_setting),
				(LinearLayout) findViewById(R.id.id_linear_setting),
				View.VISIBLE);
		for (int i = 0; i < 4; i++) {
			((CheckBox) findViewById(idCheckBoxDistanceTime[i]))
					.setOnCheckedChangeListener(mListenerDistanceTime);
		}
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
		
	}
}
