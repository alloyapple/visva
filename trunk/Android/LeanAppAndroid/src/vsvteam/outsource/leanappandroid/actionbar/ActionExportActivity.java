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
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ActionExportActivity extends Activity implements OnClickListener{
	private static final byte DARK = 1;
	private static final byte BRIGHT = 2;
	private int idCheckBoxElement[] = { R.id.id_checkbox_element_all,
			R.id.id_checkbox_element_stream_map,
			R.id.id_checkbox_element_cycle_time, R.id.id_checkbox_element_pqpr,
			R.id.id_checkbox_element_chart,
			R.id.id_checkbox_element_focus_improvements };
	private int idCheckBoxExportTo[] = { R.id.id_checkbox_email,
			R.id.id_checkbox_icloud, R.id.id_checkbox_dropbox,
			R.id.id_checkbox_box, R.id.id_checkbox_google_driver };
	private int idCheckBoxFormat[] = { R.id.id_checkbox_pdf,
			R.id.id_checkbox_excel };
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;
	private OnCheckedChangeListener mListenerElements = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				for (int j = 0; j < 6; j++) {
					if (buttonView.getId() == idCheckBoxElement[j]) {
						((CheckBox) findViewById(idCheckBoxElement[j]))
								.setChecked(true);
					} else {
						((CheckBox) findViewById(idCheckBoxElement[j]))
								.setChecked(false);
					}
				}
			}
		}
	};
	private OnCheckedChangeListener mListenerExportTo = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				for (int j = 0; j < 5; j++) {
					if (buttonView.getId() == idCheckBoxExportTo[j]) {
						((CheckBox) findViewById(idCheckBoxExportTo[j]))
								.setChecked(true);
					} else {
						((CheckBox) findViewById(idCheckBoxExportTo[j]))
								.setChecked(false);
					}
				}
			}
		}
	};
	private OnCheckedChangeListener mListenerFormat = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				for (int j = 0; j < 2; j++) {
					if (buttonView.getId() == idCheckBoxFormat[j]) {
						((CheckBox) findViewById(idCheckBoxFormat[j]))
								.setChecked(true);
					} else {
						((CheckBox) findViewById(idCheckBoxFormat[j]))
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
		setContentView(R.layout.page_action_export);
		visibleView((FrameLayout) findViewById(R.id.id_frame_parent_export),
				(LinearLayout) findViewById(R.id.id_linear_export), View.VISIBLE);
		for (int i = 0; i < 6; i++) {
			((CheckBox) findViewById(idCheckBoxElement[i]))
					.setOnCheckedChangeListener(mListenerElements);
		}
		for (int i = 0; i < 5; i++) {
			((CheckBox) findViewById(idCheckBoxExportTo[i]))
					.setOnCheckedChangeListener(mListenerExportTo);
		}
		for (int i = 0; i < 2; i++) {
			((CheckBox) findViewById(idCheckBoxFormat[i]))
					.setOnCheckedChangeListener(mListenerFormat);
		}

	}

	public void excuteExport(View v) {
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
}
