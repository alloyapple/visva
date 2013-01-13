package vsvteam.outsource.leanappandroid.activity.valuestreammap;

import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ChangeProjectActivity;
import vsvteam.outsource.leanappandroid.actionbar.ExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.SettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.VersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TProjectDatabaseHandler;
import vsvteam.outsource.leanappandroid.database.TProjectDataBase;

public class ChoiceProjectActivity extends VSVTeamBaseActivity implements OnClickListener {

	// ==========================Control Define ==========================
	private Button btnCreatedProject;
	private Button btnSelectedProject;
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;

	private EditText editTextProjectName;
	private EditText editTextCompanyName;
	private EditText editTextProjectDescription;
	private EditText editTextCompanyAddress;
	private EditText editTextNotes;

	private WheelView projectWheel;
	// ==========================Class Define ============================
	private TProjectDatabaseHandler databaseHandler;
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	private List<TProjectDataBase> projectArrList;
	// ==========================Variable Define =========================
	private boolean scrolling = false;
	private String project[];
	private String projectName[];
	private int projectId[];
	private int _projectCurrentId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.page_choice_project);

		createDataBase();

		initialize();

		// city.addChangingListener(new OnWheelChangedListener() {
		// public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// if (!scrolling) {
		// updateCities(city, cities, newValue);
		// }
		// }
		// });

		// country.addScrollingListener(new OnWheelScrollListener() {
		// public void onScrollingStarted(WheelView wheel) {
		// scrolling = true;
		// }
		//
		// public void onScrollingFinished(WheelView wheel) {
		// scrolling = false;
		// updateCities(city, cities, country.getCurrentItem());
		// }
		// });
		//
		// country.setCurrentItem(1);

	}

	private void createDataBase() {
		// init sharePreference
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference.getInstance(this);

		// init database
		databaseHandler = new TProjectDatabaseHandler(this);
		project = new String[5];
		projectName = new String[5];
		projectId = new int[5];

		// Reading all projects
		Log.d("Reading: ", "Reading all projects..");
		List<TProjectDataBase> projectArrList = databaseHandler.getAllProjects();

		if (projectArrList.size() > 0) {
			project = new String[projectArrList.size()];
			projectName = new String[projectArrList.size()];
			projectId = new int[projectArrList.size()];
			for (int i = 0; i < projectArrList.size(); i++) {
				project[i] = projectArrList.get(i).getCompanyName().toString() + " "
						+ projectArrList.get(i).getProjectName().toString();
				projectName[i] = projectArrList.get(i).getProjectName().toString();
				projectId[i] = projectArrList.get(i).getProjectID();
			}
		} else {
			project = new String[] { "No Project", "No Project", "No Project", "No Project",
					"No Project" };
			projectName = new String[] { "No Project", "No Project", "No Project", "No Project",
					"No Project" };
		}
		// get current id for project to add new
		_projectCurrentId = projectArrList.size();
		Log.e("aaaaaaaaaa", "currentId " + _projectCurrentId);

	}

	private void initialize() {
		//
		btnCreatedProject = (Button) findViewById(R.id.btn_created);
		btnSelectedProject = (Button) findViewById(R.id.btn_select);
		btnCreatedProject.setOnClickListener(this);
		btnSelectedProject.setOnClickListener(this);
		//
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

		editTextProjectName = (EditText) findViewById(R.id.editText_CreatdProjectName);
		editTextCompanyName = (EditText) findViewById(R.id.editText_CreatedCompanyName);
		editTextProjectDescription = (EditText) findViewById(R.id.editText_CreatedProjectDescription);
		editTextCompanyAddress = (EditText) findViewById(R.id.editText_CreatedCompanyAddress);
		editTextNotes = (EditText) findViewById(R.id.editText_CreatedNotes);

		// wheel android
		projectWheel = (WheelView) findViewById(R.id.wheel_project);
		projectWheel.setVisibleItems(5);
		ProjectArrayAdapter projectAdapter = new ProjectArrayAdapter(this, project, 3);
		projectWheel.setViewAdapter(projectAdapter);

	}

	/**
	 * Updates the city wheel
	 */
	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this, cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(cities[index].length / 2);
	}

	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	@Override
	public void onClick(View view) {
		if (view == btnCreatedProject) {
			insertToDataBase();
		} else if (view == btnSelectedProject) {
			// insert to share preference
			leanAppAndroidSharePreference.setProjectCreatedOrSelectedExist(false);
			leanAppAndroidSharePreference.setProjectNameActive(projectName[projectWheel
					.getCurrentItem()]);
			leanAppAndroidSharePreference.setProjectIdActive(projectId[projectWheel
					.getCurrentItem()]);

			Log.e("is Item selected ",
					"is project selected " + projectName[projectWheel.getCurrentItem()]);
			//
			gotoActivityInGroup(this, CreateProjectActivity.class);
		} else if (view == btnExport) {
//			Intent intentExport = new Intent(ChoiceProjectActivity.this, ExportActivity.class);
//			startActivity(intentExport);
			gotoActivityInGroup(ChoiceProjectActivity.this, ExportActivity.class);
		} else if (view == btnSetting) {
//			Intent intentSetting = new Intent(ChoiceProjectActivity.this, SettingActivity.class);
//			startActivity(intentSetting);
			gotoActivityInGroup(ChoiceProjectActivity.this, SettingActivity.class);
			Log.e("click setting", "ok");
		} else if (view == btnVersion) {
			Intent intentVersion = new Intent(ChoiceProjectActivity.this, VersionActivity.class);
			startActivity(intentVersion);
		} else if (view == btnChangeProject) {
			Intent intentChangeProject = new Intent(ChoiceProjectActivity.this,
					ChangeProjectActivity.class);
			startActivity(intentChangeProject);
		}
	}

	private void insertToDataBase() {
		if ("".equals(editTextProjectName.getText().toString().trim())
				|| "".equals(editTextCompanyName.getText().toString().trim())
				|| "".equals(editTextProjectDescription.getText().toString().trim())
				|| "".equals(editTextCompanyAddress.getText().toString().trim())
				|| "".equals(editTextNotes.getText().toString().trim())) {
			Toast.makeText(ChoiceProjectActivity.this, "Fill all fields to create new project",
					Toast.LENGTH_LONG).show();
		} else {
			boolean isDuplicate = false;
			projectArrList = databaseHandler.getAllProjects();
			int size = projectArrList.size();
			Log.e("size", "size " + size);
			for (int i = 0; i < size; i++) {
				if (editTextCompanyName.getText().toString()
						.equals(projectArrList.get(i).getCompanyName())
						&& editTextProjectName.getText().toString()
								.equals(projectArrList.get(i).getProjectName())) {
					isDuplicate = true;
				}
			}
			if (!isDuplicate) {
				_projectCurrentId++;// increase project id +1
				databaseHandler.addNewProject(new TProjectDataBase(_projectCurrentId,
						editTextProjectName.getText().toString(), editTextCompanyName.getText()
								.toString(), editTextProjectDescription.getText().toString(),
						editTextProjectDescription.getText().toString(), editTextCompanyAddress
								.getText().toString()));
				databaseHandler.close();

				// insert to share preference
				leanAppAndroidSharePreference.setProjectIdActive(_projectCurrentId);
				leanAppAndroidSharePreference.setProjectNameActive(editTextProjectName.getText()
						.toString());
				leanAppAndroidSharePreference.setProjectCreatedOrSelectedExist(true);

				// start create process activity
				gotoActivityInGroup(this, CreateProjectActivity.class);
			} else {
				Toast.makeText(ChoiceProjectActivity.this,
						"Duplicate project.Rename the project name", Toast.LENGTH_LONG).show();
				isDuplicate = false;
			}
		}

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

	public void onResume() {
		super.onResume();
		Log.e("onResume", "onResume");
		updateProjectWheel();

	}

	private void updateProjectWheel() {
		// Reading all contacts
		Log.d("Reading: ", "Reading all contacts..");
		List<TProjectDataBase> projectArrList = databaseHandler.getAllProjects();

		if (projectArrList.size() > 0) {
			project = new String[projectArrList.size()];
			for (int i = 0; i < projectArrList.size(); i++) {
				project[i] = projectArrList.get(i).getCompanyName().toString() + " "
						+ projectArrList.get(i).getProjectName().toString();
			}
		} else
			project = new String[] { "No Project", "No Project", "No Project", "No Project",
					"No Project" };

		// get current id for project to add new
		_projectCurrentId = projectArrList.size();
		//
		projectWheel.setVisibleItems(5);
		ProjectArrayAdapter projectAdapter = new ProjectArrayAdapter(this, project, 3);
		projectWheel.setViewAdapter(projectAdapter);
	}
}
