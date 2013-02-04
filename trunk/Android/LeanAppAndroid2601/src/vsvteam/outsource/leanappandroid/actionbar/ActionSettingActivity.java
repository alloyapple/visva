package vsvteam.outsource.leanappandroid.actionbar;

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupValueStreamMapActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class ActionSettingActivity extends Activity implements OnClickListener {
	public static final int DISTANCE_TIME_M_S = 0;
	public static final int DISTANCE_TIME_KM_H = 2;
	public static final int DISTANCE_TIME_YA_SE = 3;
	public static final int DISTANCE_TIME_MILE_H = 4;
	public static final String PREF_SETTING = "Setting Lean App";
	public static final String KEY_DISTANCE = "Distance time";
	public static final String KEY_SPEED = "Speed";
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			saveSetting();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

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

	public void saveSetting() {
		Toast.makeText(TabGroupValueStreamMapActivity.instance, "saved setting", Toast.LENGTH_LONG).show();
		SharedPreferences preferenceManager = getSharedPreferences(
				PREF_SETTING, 0);
		SharedPreferences.Editor editor = preferenceManager.edit();
		for (int j = 0; j < 4; j++) {
			if (((CheckBox) findViewById(idCheckBoxDistanceTime[j]))
					.isChecked()) {
				editor.putInt(KEY_DISTANCE, j);
				break;
			}
		}
		try {
			editor.putFloat(
					KEY_SPEED,
					Float.parseFloat(((EditText) findViewById(R.id.id_editText_speed))
							.getText().toString()));
		} catch (Exception e) {
			editor.putFloat(KEY_SPEED, 0);
		}
		editor.commit();

	}
}
