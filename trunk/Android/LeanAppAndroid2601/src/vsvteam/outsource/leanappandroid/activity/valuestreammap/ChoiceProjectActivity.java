package vsvteam.outsource.leanappandroid.activity.valuestreammap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ActionChangeActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionSettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.ActionVersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TProjectDataBase;
import vsvteam.outsource.leanappandroid.database.TProjectDatabaseHandler;
import vsvteam.outsource.leanappandroid.database.TVersionDataBase;
import vsvteam.outsource.leanappandroid.database.TVersionDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.V_VSMDataBaseHandler;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChoiceProjectActivity extends VSVTeamBaseActivity implements OnClickListener {

	// ==========================Control Define ==========================
	private Button btnCreatedProject;
	private Button btnSelectedProject;
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;
	private ImageView btnBookChoice;
	private ImageView btnMoney;

	private EditText editTextProjectName;
	private EditText editTextCompanyName;
	private EditText editTextProjectDescription;
	private EditText editTextCompanyAddress;
	private EditText editTextNotes;

	private WheelView projectWheel;
	// ==========================Class Define ============================
	private TProjectDatabaseHandler databaseHandler;
	private V_VSMDataBaseHandler vsmDataBaseHandler;
	private TVersionDataBaseHandler tVersionDataBaseHandler;
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	private List<TProjectDataBase> projectArrList;
	// ==========================Variable Define =========================
	private String project[] = {};
	private String projectName[] = {};
	private int projectId[] = {};
	private int _projectCurrentId;
	private int _versionCurrentNo;
	private int _versionCurrentId;
	private String _versionCurrentNote;
	private String _versionCurentDateTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// intialize database
		createDataBase();
		setContentView(R.layout.page_choice_project);
		initialize();
	}

	private void createDataBase() {
		// init sharePreference
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference.getInstance(this);
		leanAppAndroidSharePreference.setProjectIdActive(-1);
		leanAppAndroidSharePreference.setProjectNameActive("");
		leanAppAndroidSharePreference.setProcessIdActive(-1);
		leanAppAndroidSharePreference.setProcessNameActive("");
		// init database
		databaseHandler = new TProjectDatabaseHandler(this);

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
		}
		// get current id for project to add new
		_projectCurrentId = projectArrList.size();

		// init vsm databasehandler
		vsmDataBaseHandler = new V_VSMDataBaseHandler(this);

		// init version databasehandler
		tVersionDataBaseHandler = new TVersionDataBaseHandler(this);
		List<TVersionDataBase> tVersionDataBases = tVersionDataBaseHandler.getAllVersions();
		_versionCurrentNo = tVersionDataBases.size();
	}

	/**
	 * initialize all control
	 */
	private void initialize() {
		//
		btnCreatedProject = (Button) findViewById(R.id.btn_created);
		btnSelectedProject = (Button) findViewById(R.id.btn_select);
		btnCreatedProject.setOnClickListener(this);
		btnSelectedProject.setOnClickListener(this);
		btnBookChoice = (ImageView) findViewById(R.id.img_project_book);
		btnBookChoice.setOnClickListener(this);
		//
		btnExport = (ImageView) findViewById(R.id.img_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangeProject = (ImageView) findViewById(R.id.img_project_change_project);
		btnChangeProject.setOnClickListener(this);
		btnMoney = (ImageView) findViewById(R.id.img_project_currency);
		btnMoney.setOnClickListener(this);
		editTextProjectName = (EditText) findViewById(R.id.editText_CreatdProjectName);
		editTextCompanyName = (EditText) findViewById(R.id.editText_CreatedCompanyName);
		editTextProjectDescription = (EditText) findViewById(R.id.editText_CreatedProjectDescription);
		editTextCompanyAddress = (EditText) findViewById(R.id.editText_CreatedCompanyAddress);
		editTextNotes = (EditText) findViewById(R.id.editText_CreatedNotes);

		// wheel android
		projectWheel = (WheelView) findViewById(R.id.wheel_project);
		projectWheel.setVisibleItems(5);
		ProjectArrayAdapter projectAdapter = new ProjectArrayAdapter(this, project, 0);
		projectWheel.setViewAdapter(projectAdapter);

	}

	@Override
	public void onClick(View view) {
		if (view == btnCreatedProject) {
			insertToDataBase();
		} else if (view == btnSelectedProject) {
			if (projectId.length > 0) {
				// insert to share preference
				leanAppAndroidSharePreference.setProjectCreatedOrSelectedExist(false);
				leanAppAndroidSharePreference.setProjectNameActive(projectName[projectWheel
						.getCurrentItem()]);
				leanAppAndroidSharePreference.setProjectIdActive(projectId[projectWheel
						.getCurrentItem()]);

				gotoActivityInGroup(this, CreateProjectActivity.class);
			} else {
				Toast.makeText(ChoiceProjectActivity.this, "No project is added to select",
						Toast.LENGTH_LONG).show();
			}
		} else if (view == btnBookChoice) {
			gotoActivityInGroup(ChoiceProjectActivity.this, DrawMapActivity.class);
		} else if (view == btnExport) {
			gotoActivityInGroup(ChoiceProjectActivity.this, ActionExportActivity.class);
		} else if (view == btnSetting) {
			gotoActivityInGroup(ChoiceProjectActivity.this, ActionSettingActivity.class);
		} else if (view == btnVersion) {
			gotoActivityInGroup(ChoiceProjectActivity.this, ActionVersionActivity.class);

		} else if (view == btnChangeProject) {
			gotoActivityInGroup(ChoiceProjectActivity.this, ActionChangeActivity.class);
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

				// insert values to version database
				_versionCurrentNo++;
				_versionCurentDateTime = (new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date()) + ".mp3").toString();
				Date dateTime = new Date();
				Log.e("datetime", "date time "+dateTime.getDate());
				_versionCurrentNote = "Project " + editTextProjectName.getText().toString().trim();
				
				tVersionDataBaseHandler.addNewVersion(new TVersionDataBase(_projectCurrentId,
						_projectCurrentId, _versionCurrentNo, _versionCurentDateTime,
						_versionCurrentNote));
				// close database
				databaseHandler.close();
				tVersionDataBaseHandler.close();
				vsmDataBaseHandler.close();

				// insert to share preference
				leanAppAndroidSharePreference.setProjectIdActive(_projectCurrentId);
				leanAppAndroidSharePreference.setProjectNameActive(editTextProjectName.getText()
						.toString());
				leanAppAndroidSharePreference.setProjectCreatedOrSelectedExist(true);

				// reset all field
				resetField();

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

	/*
	 * reset all field after create project
	 */
	private void resetField() {
		editTextCompanyAddress.setText("");
		editTextCompanyName.setText("");
		editTextNotes.setText("");
		editTextProjectDescription.setText("");
		editTextProjectName.setText("");
	}

	public void onResume() {
		super.onResume();
		Log.e("onResume", "onResume " + leanAppAndroidSharePreference.getModeTaktTimee());
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

			// get current id for project to add new
			_projectCurrentId = projectArrList.size();
		//
		projectWheel.setVisibleItems(5);
		ProjectArrayAdapter projectAdapter = new ProjectArrayAdapter(this, project, 3);
		projectWheel.setViewAdapter(projectAdapter);
	}
}
