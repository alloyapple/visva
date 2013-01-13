package vsvteam.outsource.leanappandroid.actionbar;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ChangeProjectActivity extends VSVTeamBaseActivity implements OnClickListener {
	// =========================== Control Define ============================
	private WheelView wheelChangeProject;
	private WheelView wheelChangeProcess;
	private Button btnDoneChangeProject;
	private Button btnNewProject;
	// =========================== Class Define ==============================
	// =========================== Variable Define ===========================
	private String[] changeProject = { "Project 1", "Project 2", "Project 3", "Project 4" };
	private String[] changeProcess = { "Project 1", "Project 2", "Project 3", "Project 4" };

	@Override
	public void onClick(View v) {
		if (v == btnDoneChangeProject) {
			finish();
		} else if (v == btnNewProject) {
			finish();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_change_project);

		btnDoneChangeProject = (Button) findViewById(R.id.btn_change_project_done);
		btnDoneChangeProject.setOnClickListener(this);
		btnNewProject = (Button) findViewById(R.id.btn_change_project_new_project);
		btnNewProject.setOnClickListener(this);

		wheelChangeProcess = (WheelView) findViewById(R.id.wheel_change_process);
		wheelChangeProject = (WheelView) findViewById(R.id.wheel_change_project);

		wheelChangeProcess.setVisibleItems(5);
		wheelChangeProcess.setCurrentItem(0);
		wheelChangeProcess.setViewAdapter(new RecoredDetailArrayAdapter(this, changeProcess, 0));

		wheelChangeProject.setVisibleItems(5);
		wheelChangeProject.setCurrentItem(0);
		wheelChangeProject.setViewAdapter(new RecoredDetailArrayAdapter(this, changeProject, 0));
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class RecoredDetailArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public RecoredDetailArrayAdapter(Context context, String[] items, int current) {
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
}
