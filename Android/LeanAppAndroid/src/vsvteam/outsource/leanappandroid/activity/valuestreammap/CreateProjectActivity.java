package vsvteam.outsource.leanappandroid.activity.valuestreammap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.actionbar.ChangeProjectActivity;
import vsvteam.outsource.leanappandroid.actionbar.ExportActivity;
import vsvteam.outsource.leanappandroid.actionbar.SettingActivity;
import vsvteam.outsource.leanappandroid.actionbar.VersionActivity;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.adapter.ListProcessAdapter;
import vsvteam.outsource.leanappandroid.adapter.ListStepAdapter;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TProcessDataBase;
import vsvteam.outsource.leanappandroid.database.TProcessDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TProjectDatabaseHandler;
import vsvteam.outsource.leanappandroid.database.TProjectDataBase;
import vsvteam.outsource.leanappandroid.database.TStepsDataBase;
import vsvteam.outsource.leanappandroid.database.TStepsDataBaseHandler;
import vsvteam.outsource.leanappandroid.define.Constant;
import vsvteam.outsource.leanappandroid.quickaction.ActionItem;
import vsvteam.outsource.leanappandroid.quickaction.QuickAction;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CreateProjectActivity extends VSVTeamBaseActivity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {

	// =========================Control Define ==========================
	private WheelView wheelProcess;
	private Button btnAddProcess;
	private Button btnAddStep;
	private Button btnDoneCreatedProject;

	private ImageView btnSetting;
	private ImageView btnExport;
	private ImageView btnVersion;
	private ImageView btnChangedProject;

	private EditText editTextProcessName;
	private EditText editTextProcessDescription;
	private EditText editTextProcessStartPoint;
	private EditText editTextProcessEndPoint;
	private EditText editTextProcessOutPutInventory;
	private EditText editTextProcessDefectPercent;
	private EditText editTextProcessDefectNotes;
	private EditText editTextProcessUpTime;
	private EditText editTextProcessCommunication;
	private EditText editTextProcessValueAddingTime;
	private EditText editTextProcessNonValueAddingTime;
	private EditText editTextProcessStepName;

	private ToggleButton toggleBtnProcessDetail;

	private TextView txtProjectName;

	private ListView listProcess;
	private ListView listStep;
	// =========================Class Define ============================
	private TProjectDatabaseHandler tProjectDataBaseHandler;
	private TProcessDataBaseHandler tProcessDataBaseHandler;
	private TStepsDataBaseHandler tStepsDataBaseHandler;
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	private QuickAction mQuickAction;
	private QuickAction mQuickActionStep;
	private List<TProcessDataBase> processArrList;
	private List<TStepsDataBase> stepArrList;
	// =========================Variable Define =========================
	private String[] processName = {};
	private String[] stepName = {};
	private int[] stepId = {};
	private int[] processId = {};
	private String[] project = new String[5];

	// Process field in database
	private String _processName;
	private String _processDescription;
	private String _processStartPoint;
	private String _processEndPoint;
	private String _outputInventory;
	private int _defectPercent;
	private String _defectNotes;
	private int _upTime;
	private String _communication;
	private int _valueAddingTime;
	private int _nonValueAddingTime;
	private String _stepName;

	private int _projectId;
	private String _projectName;
	private int _processCurrentId;
	private int _stepCurrentId;
	private int _currentWheelProcessItem = 0;
	// index of process,step listview
	private int _currentProcessListViewIndex;
	private int _currentStepListViewIndex;

	private ArrayList<HashMap<String, String>> ArrListProcess;
	private ArrayList<HashMap<String, String>> ArrListStep;
	private static final int DIALOG_DELETE_PROCESS = 1;
	private static final int DIALOG_DELETE_STEP = 2;

	/*
	 * handler to control the dialog win game
	 */
	public final Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case DIALOG_DELETE_PROCESS:
			// Level Up
			{
				showDialog(DIALOG_DELETE_PROCESS);
				break;
			}
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btnAddProcess) {
			// add new process
			addNewProcess();
		} else if (view == btnAddStep) {
			// add new step
			addNewStep();
		} else if (view == btnDoneCreatedProject) {
			gotoActivityInGroup(this, DrawMapActivity.class);
		} else if (view == btnExport) {

			gotoActivityInGroup(CreateProjectActivity.this, ExportActivity.class);
		} else if (view == btnSetting) {
			Intent intentSetting = new Intent(CreateProjectActivity.this, SettingActivity.class);
			startActivity(intentSetting);
			gotoActivityInGroup(CreateProjectActivity.this, SettingActivity.class);
		} else if (view == btnVersion) {

			gotoActivityInGroup(CreateProjectActivity.this, VersionActivity.class);
		} else if (view == btnChangedProject) {

			gotoActivityInGroup(CreateProjectActivity.this, ChangeProjectActivity.class);
		}
	}

	/**
	 * add new step
	 */
	private void addNewStep() {
		if ("".equals(editTextProcessStepName.getText().toString().trim())) {
			Toast.makeText(CreateProjectActivity.this, "Fill the step name to add new step",
					Toast.LENGTH_LONG).show();
		} else {
			if (processName.length == 0) {
				// no process added
				Toast.makeText(CreateProjectActivity.this, "No process added", Toast.LENGTH_LONG)
						.show();
			} else {
				// check if add duplicated step
				boolean isDuplicatedStep = false;
				int currentProcessId;
				_currentWheelProcessItem = wheelProcess.getCurrentItem();
				currentProcessId = processId[_currentWheelProcessItem];
				Log.e("current id " + currentProcessId, "process current id "
						+ _currentWheelProcessItem + " process Id size " + processId.length);
				stepArrList = tStepsDataBaseHandler.getAllStep(currentProcessId);
				int size = stepArrList.size();
				for (int i = 0; i < size; i++) {
					if (editTextProcessStepName.getText().toString().trim()
							.equals(stepArrList.get(i).getStepDescription())) {
						isDuplicatedStep = true;
					}
				}
				// no duplicate step
				if (!isDuplicatedStep) {
					// get current id for step to add new
					List<TStepsDataBase> listAllStep = tStepsDataBaseHandler.getAllSteps();
					int sizeAllStep = listAllStep.size();
					if (sizeAllStep > 0)
						_stepCurrentId = listAllStep.get(sizeAllStep - 1).getStepID();
					else
						_stepCurrentId = 0;

					Log.e("size All Step", "size all step " + sizeAllStep);

					_stepCurrentId++;
					_stepName = editTextProcessStepName.getText().toString().trim();
					String _videoFileName = new SimpleDateFormat("yyyyMMdd_HHmmss")
							.format(new Date()) + ".mp4".toString();

					// insert to database
					tStepsDataBaseHandler.addNewStep(new TStepsDataBase(_stepCurrentId,
							currentProcessId, _projectId, _stepCurrentId, _stepName, -1, -1,
							_videoFileName));
					// refresh list step after add new step name
					List<TStepsDataBase> listStep = tStepsDataBaseHandler
							.getAllStep(currentProcessId);
					Log.e("list step of process id " + currentProcessId,
							"size of lits " + listStep.size());
					refreshListViewStep();
				} else {// duplicate process
					Toast.makeText(CreateProjectActivity.this,
							"Duplicate step.Rename the step name", Toast.LENGTH_LONG).show();
					isDuplicatedStep = false;
				}
			}
		}
	}

	/**
	 * add new process
	 */
	private void addNewProcess() {
		// check if some field is empty
		if ("".equals(editTextProcessCommunication.getText().toString())
				|| "".equals(editTextProcessDefectNotes.getText().toString())
				|| "".equals(editTextProcessDefectPercent.getText().toString())
				|| "".equals(editTextProcessDescription.getText().toString())
				|| "".equals(editTextProcessEndPoint.getText().toString())
				|| "".equals(editTextProcessName.getText().toString())
				|| "".equals(editTextProcessNonValueAddingTime.getText().toString())
				|| "".equals(editTextProcessOutPutInventory.getText().toString())
				|| "".equals(editTextProcessStartPoint.getText().toString())
				|| "".equals(editTextProcessUpTime.getText().toString())
				|| "".equals(editTextProcessValueAddingTime.getText().toString())) {
			Toast.makeText(CreateProjectActivity.this, "Fill all fields to add new process",
					Toast.LENGTH_LONG).show();
		} else {
			// check if add duplicate processes
			boolean isDuplicatedProcess = false;
			processArrList = tProcessDataBaseHandler.getAllProcess(leanAppAndroidSharePreference
					.getProjectIdActive());
			int size = processArrList.size();
			Log.e("project in project " + _projectName, "size of all process " + size);
			for (int i = 0; i < size; i++) {
				if (editTextProcessName.getText().toString()
						.equals(processArrList.get(i).getProcessName())) {
					isDuplicatedProcess = true;
				}
			}
			// no duplicate process
			if (!isDuplicatedProcess) {

				// get current id for project to add new
				List<TProcessDataBase> listAllProcess = tProcessDataBaseHandler.getAllProcess();
				int sizeAllProcess = listAllProcess.size();
				if (sizeAllProcess > 0) {
					_processCurrentId = listAllProcess.get(sizeAllProcess - 1).getProcessId();
				} else
					_processCurrentId = 0;

				// set values for all fields to add new process
				_processCurrentId++;
				_processName = editTextProcessName.getText().toString();
				_processDescription = editTextProcessDescription.getText().toString();
				_processStartPoint = editTextProcessStartPoint.getText().toString();
				_processEndPoint = editTextProcessEndPoint.getText().toString();
				_communication = editTextProcessCommunication.getText().toString();
				_defectNotes = editTextProcessDefectNotes.getText().toString();
				_defectPercent = Integer
						.parseInt(editTextProcessDefectPercent.getText().toString());
				_nonValueAddingTime = Integer.parseInt(editTextProcessNonValueAddingTime.getText()
						.toString());
				_outputInventory = editTextProcessOutPutInventory.getText().toString();
				_upTime = Integer.parseInt(editTextProcessUpTime.getText().toString());
				_valueAddingTime = Integer.parseInt(editTextProcessValueAddingTime.getText()
						.toString());

				// insert to database
				/**
				 * defectNotes
				 */
				tProcessDataBaseHandler.addNewProject(new TProcessDataBase(_processCurrentId,
						_projectId, _projectName, _processName, _processDescription, _defectNotes,
						0, _valueAddingTime, _nonValueAddingTime, _defectPercent, 0, 0, 0, 0,
						_upTime, 0, 0, 0, "test", "test", "test"));

				// refresh listview process
				refreshListViewProcess();

			} else {// duplicate process
				Toast.makeText(CreateProjectActivity.this,
						"Duplicate process.Rename the process name", Toast.LENGTH_LONG).show();
				isDuplicatedProcess = false;
			}
		}
	}

	/**
	 * refresh list view process after add new process
	 */
	private void refreshListViewProcess() {
		populateListProcess();
		ListProcessAdapter listProcessAdapter = new ListProcessAdapter(this, ArrListProcess);
		listProcess.setAdapter(listProcessAdapter);
		listProcessAdapter.notifyDataSetChanged();
		listProcess.invalidate();

		// Reading all process
		List<TProcessDataBase> listProcess = tProcessDataBaseHandler.getAllProcess(_projectId);
		int size = listProcess.size();
		if (size > 0) {
			processName = new String[size];
			processId = new int[size];
			for (int i = 0; i < size; i++) {
				processName[i] = listProcess.get(i).getProcessName().toString();
				processId[i] = listProcess.get(i).getProcessId();
			}
		} else {
			processName = new String[0];
			processId = new int[0];
		}

		// refresh process wheel
		ProcessArrayAdapter processArrayAdapter = new ProcessArrayAdapter(this, processName, 0);
		wheelProcess.setViewAdapter(processArrayAdapter);
	}

	/**
	 * refresh list view step after add new step
	 */
	private void refreshListViewStep() {
		populateListStep();
		ListStepAdapter listStepAdapter = new ListStepAdapter(this, ArrListStep);
		listStep.setAdapter(listStepAdapter);
		listStepAdapter.notifyDataSetChanged();
		listStep.invalidate();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_create_project);

		initDataBase();

		initialize();

		initQuickAction();

		initQuickActionStep();

	}

	private void initQuickActionStep() {
		// delete action
		ActionItem deleteAction = new ActionItem();
		deleteAction.setTitle("Delete");
		// edit action
		ActionItem editAction = new ActionItem();
		editAction.setTitle("Edit");

		mQuickActionStep = new QuickAction(this);

		mQuickActionStep.addActionItem(deleteAction);
		mQuickActionStep.addActionItem(editAction);

		// setup the action item click listener
		mQuickActionStep.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(int pos) {
				if (pos == 0) { // delete item selected
					Toast.makeText(CreateProjectActivity.this,
							"delete step " + stepName[_currentStepListViewIndex - 1],
							Toast.LENGTH_LONG).show();

					// delete row of table process id
					tStepsDataBaseHandler.deleteStepByStepId(stepId[_currentStepListViewIndex - 1]);

					// refresh ListView step
					refreshListViewStep();

				} else if (pos == 1) {// edit item seleted

					Toast.makeText(CreateProjectActivity.this,
							"Edit step " + stepName[_currentStepListViewIndex - 1],
							Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	/**
	 * initialize Quick Action
	 */
	private void initQuickAction() {
		// delete action
		ActionItem deleteAction = new ActionItem();
		deleteAction.setTitle("Delete");
		// edit action
		ActionItem editAction = new ActionItem();
		editAction.setTitle("Edit");

		mQuickAction = new QuickAction(this);

		mQuickAction.addActionItem(deleteAction);
		mQuickAction.addActionItem(editAction);

		// setup the action item click listener
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(int pos) {
				if (pos == 0) { // delete item selected
					Toast.makeText(CreateProjectActivity.this,
							"delete process " + processName[_currentProcessListViewIndex - 1],
							Toast.LENGTH_LONG).show();

					// delete row of table process id
					tProcessDataBaseHandler
							.deleteProcess(processId[_currentProcessListViewIndex - 1]);
					// delete row of table step at process id
					tStepsDataBaseHandler.deleteStep(processId[_currentProcessListViewIndex - 1]);

					// refrest ListView process
					refreshListViewProcess();
					// refresh ListView step
					refreshListViewStep();
				} else if (pos == 1) {// edit item seleted
					// add process actived to share preference
					leanAppAndroidSharePreference
							.setProcessIdActive(processId[_currentProcessListViewIndex - 1]);
					leanAppAndroidSharePreference
							.setProcessNameActive(processName[_currentProcessListViewIndex - 1]);
					gotoActivityInGroup(CreateProjectActivity.this, EditProcessActivity.class);
				}
			}
		});

	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case DIALOG_DELETE_PROCESS: {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						// Share button clicked
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						// Exit button clicked
						finish();
						break;
					}
				}
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(CreateProjectActivity.this);
			builder.setMessage(R.string.text_delete_process).setIcon(R.drawable.ic_launcher)
					.setTitle(getString(R.string.app_name)).setCancelable(false)
					.setPositiveButton(R.string.text_delete_positive, dialogClickListener)
					.setNegativeButton(R.string.text_delete_negative, dialogClickListener);
			return builder.create();
		}
		case DIALOG_DELETE_STEP: {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						// Share button clicked
						break;
					case DialogInterface.BUTTON_NEGATIVE:
						// Exit button clicked
						finish();
						break;
					}
				}
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(CreateProjectActivity.this);
			builder.setMessage(R.string.text_delete_step).setIcon(R.drawable.ic_launcher)
					.setTitle(getString(R.string.app_name)).setCancelable(false)
					.setPositiveButton(R.string.text_delete_positive, dialogClickListener)
					.setNegativeButton(R.string.text_delete_negative, dialogClickListener);
			return builder.create();
		}

		default:
			dialog = super.onCreateDialog(id);
			break;
		}
		return dialog;
	}

	private void sendMS(final int wh) {
		new CountDownTimer(1000, 100) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				Message ms = new Message();
				ms.what = wh;
				handler.sendMessage(ms);
			}
		}.start();
	}

	/**
	 * initialize variable
	 */
	private void initialize() {
		// wheel
		wheelProcess = (WheelView) findViewById(R.id.wheel_process);
		wheelProcess.setVisibleItems(5);
		wheelProcess.setCurrentItem(0);
		ProcessArrayAdapter processArrayAdapter = new ProcessArrayAdapter(this, processName, 0);
		wheelProcess.setViewAdapter(processArrayAdapter);
		_currentWheelProcessItem = wheelProcess.getCurrentItem();
		wheelProcess.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				_currentWheelProcessItem = newValue;
				refreshListViewStep();
			}
		});

		// Button
		btnAddProcess = (Button) findViewById(R.id.btn_add_process);
		btnAddProcess.setOnClickListener(this);
		btnAddStep = (Button) findViewById(R.id.btn_process_add_step);
		btnAddStep.setOnClickListener(this);
		btnSetting = (ImageView) findViewById(R.id.img_create_project_setting);
		btnSetting.setOnClickListener(this);
		btnDoneCreatedProject = (Button) findViewById(R.id.btn_process_i_am_done);
		btnDoneCreatedProject.setOnClickListener(this);
		btnExport = (ImageView) findViewById(R.id.img_create_project_export);
		btnExport.setOnClickListener(this);
		btnVersion = (ImageView) findViewById(R.id.img_create_project_version);
		btnVersion.setOnClickListener(this);
		btnChangedProject = (ImageView) findViewById(R.id.img_create_project_change_project);
		btnChangedProject.setOnClickListener(this);

		// EditText
		editTextProcessStepName = (EditText) findViewById(R.id.editText_process_step_name);
		editTextProcessCommunication = (EditText) findViewById(R.id.editText_communication);
		editTextProcessDefectNotes = (EditText) findViewById(R.id.editText_defect_notes);
		editTextProcessDefectPercent = (EditText) findViewById(R.id.editText_defect_percent);
		editTextProcessDescription = (EditText) findViewById(R.id.editText_process_description);
		editTextProcessEndPoint = (EditText) findViewById(R.id.editText_process_customer);
		editTextProcessName = (EditText) findViewById(R.id.editText_process_name);
		editTextProcessNonValueAddingTime = (EditText) findViewById(R.id.editText_non_value_adding_time);
		editTextProcessOutPutInventory = (EditText) findViewById(R.id.editText_output_inventory);
		editTextProcessStartPoint = (EditText) findViewById(R.id.editText_process_supplier);
		editTextProcessUpTime = (EditText) findViewById(R.id.editText_uptime);
		editTextProcessValueAddingTime = (EditText) findViewById(R.id.editText_value_adding_time);

		// Toggle Button
		toggleBtnProcessDetail = (ToggleButton) findViewById(R.id.switch_process);

		// TextView project name
		txtProjectName = (TextView) findViewById(R.id.txt_projectName);
		txtProjectName.setText("Project " + leanAppAndroidSharePreference.getProjectNameActive()
				+ " is created.");

		// ArrListProcess process
		listProcess = (ListView) findViewById(R.id.list_process);
		// set header for ArrListProcess view
		LayoutInflater inflater = this.getLayoutInflater();
		View header = inflater.inflate(R.layout.listprocess_header, null);
		listProcess.addHeaderView(header);
		// calculate for list process
		populateListProcess();
		ListProcessAdapter listProcessAdapter = new ListProcessAdapter(this, ArrListProcess);
		listProcess.setAdapter(listProcessAdapter);
		listProcessAdapter.notifyDataSetChanged();
		listProcess.setOnItemClickListener(this);
		listProcess.setOnItemLongClickListener(this);
		listProcess.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// ArrListProcess step
		listStep = (ListView) findViewById(R.id.list_step);
		LayoutInflater inflaterStep = this.getLayoutInflater();
		View headerStep = inflaterStep.inflate(R.layout.liststep_header, null);
		listStep.addHeaderView(headerStep);
		// calculate for list step
		populateListStep();
		ListStepAdapter listStepAdapter = new ListStepAdapter(this, ArrListStep);
		listStep.setAdapter(listStepAdapter);
		listStepAdapter.notifyDataSetChanged();
		listStep.setOnItemClickListener(this);
		listStep.setOnItemLongClickListener(this);
		listStep.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	/**
	 * initialize all database share preference,database process,database step
	 */
	private void initDataBase() {
		// init share preference
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference.getInstance(this);
		_projectId = leanAppAndroidSharePreference.getProjectIdActive();
		_projectName = leanAppAndroidSharePreference.getProjectNameActive();

		// init database process
		initDataBaseProcess();

		// init database step
		initDataBaseStep();
	}

	/**
	 * initialize database Step
	 */
	private void initDataBaseStep() {
		tStepsDataBaseHandler = new TStepsDataBaseHandler(this);
		// Reading all process
		if (processId.length > 0) {
			List<TStepsDataBase> listSteps = tStepsDataBaseHandler.getAllStep(processId[0]);
			int sizeOfSteps = listSteps.size();
			if (sizeOfSteps > 0) {
				stepName = new String[sizeOfSteps];
				stepId = new int[sizeOfSteps];
				for (int i = 0; i < sizeOfSteps; i++) {
					stepName[i] = listSteps.get(i).getStepDescription().toString();
					stepId[i] = listSteps.get(i).getStepID();
				}
			}
			// get current id for step to add new
			List<TStepsDataBase> listAllStep = tStepsDataBaseHandler.getAllSteps();
			int sizeAllStep = listAllStep.size();
			if (sizeAllStep > 0)
				_stepCurrentId = listAllStep.get(sizeAllStep - 1).getStepID();
			else
				_stepCurrentId = 0;
		}
	}

	/**
	 * initialize data base process
	 */
	private void initDataBaseProcess() {
		tProcessDataBaseHandler = new TProcessDataBaseHandler(this);
		// Reading all process
		List<TProcessDataBase> listProcess = tProcessDataBaseHandler.getAllProcess(_projectId);
		int size = listProcess.size();
		if (size > 0) {
			processName = new String[size];
			processId = new int[size];
			for (int i = 0; i < size; i++) {
				processName[i] = listProcess.get(i).getProcessName().toString();
				processId[i] = listProcess.get(i).getProcessId();
			}
		}

		// get current id for project to add new
		List<TProcessDataBase> listAllProcess = tProcessDataBaseHandler.getAllProcess();
		int sizeAllProcess = listAllProcess.size();
		if (sizeAllProcess > 0)
			_processCurrentId = listAllProcess.get(sizeAllProcess - 1).getProcessId();
		else
			_processCurrentId = 0;
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class ProcessArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public ProcessArrayAdapter(Context context, String[] items, int current) {
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

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == listProcess && arg2 > 0) {
			mQuickAction.show(arg1, true);
			_currentProcessListViewIndex = arg2;
			listProcess.requestFocus(arg2);
			mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
		} else if (arg0 == listStep && arg2 > 0) {
			mQuickActionStep.show(arg1, false);
			_currentStepListViewIndex = arg2;
			listStep.requestFocus(arg2);
			mQuickActionStep.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == listProcess && arg2 > 0) {
			mQuickAction.show(arg1, true);
			_currentProcessListViewIndex = arg2;
			mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
		} else if (arg0 == listStep && arg2 > 0) {
			mQuickActionStep.show(arg1, false);
			_currentStepListViewIndex = arg2;
			mQuickActionStep.setAnimStyle(QuickAction.ANIM_GROW_FROM_CENTER);
		}
	}

	/**
	 * calculate list step value
	 */
	private void populateListStep() {
		ArrListStep = new ArrayList<HashMap<String, String>>();
		// get process wheel current id
		_currentWheelProcessItem = wheelProcess.getCurrentItem();

		if (processName.length > 0) {
			int _currentProcessId = 0;
			if (processId.length > 0)
				_currentProcessId = processId[_currentWheelProcessItem];

			// Reading all step
			List<TStepsDataBase> listStep = tStepsDataBaseHandler.getAllStep(_currentProcessId);
			if (listStep.size() > 0) {
				stepName = new String[listStep.size()];
				stepId = new int[listStep.size()];
				for (int i = 0; i < listStep.size(); i++) {
					stepName[i] = listStep.get(i).getStepDescription().toString();
					stepId[i] = listStep.get(i).getStepID();
					//
					HashMap<String, String> temp = new HashMap<String, String>();
					temp.put(Constant.FIRST_COLUMN, "" + listStep.get(i).getStepID());
					temp.put(Constant.SECOND_COLUMN, ""
							+ listStep.get(i).getStepDescription().toString());
					ArrListStep.add(temp);
				}
			}
		} else {

		}
	}

	/**
	 * calculate list process value
	 */
	private void populateListProcess() {
		ArrListProcess = new ArrayList<HashMap<String, String>>();
		// Reading all process
		List<TProcessDataBase> listProcess = tProcessDataBaseHandler
				.getAllProcess(leanAppAndroidSharePreference.getProjectIdActive());

		if (listProcess.size() > 0) {
			processName = new String[listProcess.size()];
			for (int i = 0; i < listProcess.size(); i++) {
				processName[i] = listProcess.get(i).getProcessName().toString();

				//
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put(Constant.FIRST_COLUMN, "" + listProcess.get(i).getProcessId());
				temp.put(Constant.SECOND_COLUMN, ""
						+ listProcess.get(i).getProcessName().toString());
				temp.put(Constant.THIRD_COLUMN, ""
						+ listProcess.get(i).getProcessDescription().toString());
				ArrListProcess.add(temp);
			}
		}
	}

	protected void onPause() {
		super.onPause();
		mQuickAction.dismiss();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		tProcessDataBaseHandler.close();
	}
}
