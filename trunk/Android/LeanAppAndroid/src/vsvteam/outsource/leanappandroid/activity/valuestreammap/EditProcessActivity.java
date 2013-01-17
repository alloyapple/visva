package vsvteam.outsource.leanappandroid.activity.valuestreammap;

import java.util.ArrayList;
import java.util.List;

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TProcessDataBase;
import vsvteam.outsource.leanappandroid.database.TProcessDataBaseHandler;
import android.R.integer;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class EditProcessActivity extends VSVTeamBaseActivity implements OnClickListener {

	// ============================Control Define =====================
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

	private TextView txtProcessName;

	private ToggleButton toggleBtnProcessDetail;
	private Button btnPreviousItem;
	private Button btnDoneEditProcess;
	private Button btnNextItem;

	// ============================Class Define =======================
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	private TProcessDataBaseHandler tProcessDataBaseHandler;
	private List<TProcessDataBase> tprocessList;

	// ============================Variable Define ====================

	private int _currentProcessId;
	private String _currentProcessName;
	private String _currentProjectNameActive;
	private int _currentProjectIdActive;

	// all fields of process tables database
	private int _projectId;
	private String _processName;
	private String _projectName;
	private String _processDescription;
	private String _processStartPoint;
	private String _processEndPoint;
	private String _processOutPutInventory;
	private String _processDefectNote;

	private int _processDefectPercent;
	private int _processUpTime;
	private int _processCommunication;
	private int _processValueAddingTime;
	private int _processNonValueAddingTime;
	private int _processTotalCycleTime;
	private int _processTotOperators;
	private int _processShifts;
	private int _processAvailability;
	private int _processTotDistanceTraveled;
	private int _processChangeOverTime;
	private int _processTaktTime;
	private int _processVersionId;
	private String _processPreviousProcess;
	private String _processNextProcess;
	private String _processVsmName;

	// array process id
	private ArrayList<Integer> processId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_edit_process);
		// intialize database
		initDataBase();

		// initialize control
		initializeControl();

		// intialize ActionBar
		intializeActionBar();

	}

	private void intializeActionBar() {
		// TODO Auto-generated method stub

	}

	/**
	 * initialize all control
	 */
	private void initializeControl() {
		// Button
		btnExport = (ImageView) findViewById(R.id.img_create_project_export);
		btnExport.setOnClickListener(this);
		btnSetting = (ImageView) findViewById(R.id.img_create_project_setting);
		btnSetting.setOnClickListener(this);
		btnVersion = (ImageView) findViewById(R.id.img_create_project_version);
		btnVersion.setOnClickListener(this);
		btnChangedProject = (ImageView) findViewById(R.id.img_create_project_change_project);
		btnChangedProject.setOnClickListener(this);
		btnDoneEditProcess = (Button) findViewById(R.id.btn_edit_process_done);
		btnDoneEditProcess.setOnClickListener(this);
		btnNextItem = (Button) findViewById(R.id.btn_edit_process_next_item);
		btnNextItem.setOnClickListener(this);
		btnPreviousItem = (Button) findViewById(R.id.btn_edit_process_previous_item);
		btnPreviousItem.setOnClickListener(this);

		// EditText
		editTextProcessCommunication = (EditText) findViewById(R.id.editText_edit_proces_communication);
		editTextProcessCommunication.setText("" + Integer.parseInt("15"));
		editTextProcessDefectNotes = (EditText) findViewById(R.id.editText_edit_process_defect_notes);
		editTextProcessDefectPercent = (EditText) findViewById(R.id.editText_edit_process_defect_percent);
		editTextProcessDescription = (EditText) findViewById(R.id.editText_edit_process_description);
		editTextProcessEndPoint = (EditText) findViewById(R.id.editText_edit_process_customer);
		editTextProcessName = (EditText) findViewById(R.id.editText_edit_process_name);
		editTextProcessNonValueAddingTime = (EditText) findViewById(R.id.editText_edit_process_non_value_adding_time);
		editTextProcessOutPutInventory = (EditText) findViewById(R.id.editText_edit_process_output_inventory);
		editTextProcessStartPoint = (EditText) findViewById(R.id.editText_edit_process_supplier);
		editTextProcessUpTime = (EditText) findViewById(R.id.editText_edit_process_uptime);
		editTextProcessValueAddingTime = (EditText) findViewById(R.id.editText_edit_process_value_adding_time);

		// Toggle Button
		toggleBtnProcessDetail = (ToggleButton) findViewById(R.id.switch_process);

		// TextView project name
		txtProcessName = (TextView) findViewById(R.id.txt_edit_process_projectName);
		txtProcessName.setText("Process " + _currentProcessName + " is selected.");

		// refresh all editext of process id
		refreshEditText(_currentProcessId);
	}

	private void initDataBase() {
		// init share preference
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference.getInstance(this);
		_currentProcessId = leanAppAndroidSharePreference.getProcessIdActive();
		_currentProcessName = leanAppAndroidSharePreference.getProcessNameActive();
		_currentProjectIdActive = leanAppAndroidSharePreference.getProjectIdActive();
		_currentProjectNameActive = leanAppAndroidSharePreference.getProjectNameActive();

		// init database
		tProcessDataBaseHandler = new TProcessDataBaseHandler(this);
		Log.e("_projectId", "_projectId " + _currentProjectIdActive);
		Log.e("_projectName", "_projectName " + _currentProjectNameActive);
		tprocessList = tProcessDataBaseHandler.getAllProcess(_currentProjectIdActive);

		processId = new ArrayList<Integer>();
		Log.e("size of tprocessList", "size of tprocessList " + tprocessList.size());
		int size = tprocessList.size();
		for (int i = 0; i < size; i++) {
			processId.add(tprocessList.get(i).getProcessId());
		}
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v == btnDoneEditProcess) {
			updateDataBase(_currentProcessId);
		} else if (v == btnPreviousItem) {
			int previousId = 0;
			Log.e("number process id ", "number process id " + processId.size());
			for (int i = 0; i < processId.size(); i++) {
				if (processId.get(i).equals(_currentProcessId)) {
					if (i >= 1) {
						previousId = processId.get(i - 1);
					} else
						previousId = processId.get(i);
				}
			}
			_currentProcessId = previousId;
			// refresh all edit text
			refreshEditText(previousId);
		} else if (v == btnNextItem) {
			int nextId = 0;
			Log.e("number process id ", "number process id " + processId.size());
			for (int i = 0; i < processId.size(); i++) {
				if (processId.get(i).equals(_currentProcessId)) {
					if (i < processId.size() - 1) {
						nextId = processId.get(i + 1);
					} else
						nextId = processId.get(processId.size() - 1);
				}
			}

			Log.e("nextId", "nextId " + nextId);
			_currentProcessId = nextId;
			// refresh all edit text
			refreshEditText(nextId);
		}
	}

	/**
	 * update database at process id
	 * 
	 * @param _currentProcessId
	 */
	private void updateDataBase(int _currentProcessId) {
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
			Toast.makeText(EditProcessActivity.this, "Fill all fields to edit process",
					Toast.LENGTH_LONG).show();
		} else {
			// check if add duplicate processes
			boolean isDuplicatedProcess = false;
			tprocessList = tProcessDataBaseHandler.getAllProcess(leanAppAndroidSharePreference
					.getProjectIdActive());
			int size = tprocessList.size();
			for (int i = 0; i < size; i++) {
				if (editTextProcessName.getText().toString()
						.equals(tprocessList.get(i).getProcessName())) {
					isDuplicatedProcess = true;
				}
			}
			// no duplicate process
			if (!isDuplicatedProcess) {
				_processName = editTextProcessName.getText().toString();
				_processDescription = editTextProcessDescription.getText().toString();
				_processStartPoint = editTextProcessStartPoint.getText().toString();
				_processEndPoint = editTextProcessEndPoint.getText().toString();
				_processCommunication = Integer.parseInt(editTextProcessCommunication.getText()
						.toString());
				_processDefectNote = editTextProcessDefectNotes.getText().toString();
				_processDefectPercent = Integer.parseInt(editTextProcessDefectPercent.getText()
						.toString());
				_processNonValueAddingTime = Integer.parseInt(editTextProcessNonValueAddingTime
						.getText().toString());
				_processOutPutInventory = editTextProcessOutPutInventory.getText().toString();
				_processUpTime = Integer.parseInt(editTextProcessUpTime.getText().toString());
				_processValueAddingTime = Integer.parseInt(editTextProcessValueAddingTime.getText()
						.toString());

				// update to database
				/**
				 * defectNotes
				 */
				tProcessDataBaseHandler.updateProcess(new TProcessDataBase(_currentProcessId,
						_currentProjectIdActive, _currentProjectNameActive, _processName,
						_processDescription, _processDefectNote, 0, _processValueAddingTime,
						_processNonValueAddingTime, _processDefectPercent, 0, 0, 0, 0,
						_processUpTime, 0, 0, 0, "test", "test", "test"));
				
				//return process activity
				finish();

			} else {// duplicate process
				Toast.makeText(EditProcessActivity.this,
						"Duplicate process.Rename the process name", Toast.LENGTH_LONG).show();
				isDuplicatedProcess = false;
			}
		}
	}

	/**
	 * refresh all edit text
	 */
	private void refreshEditText(int currentProcessId) {
		TProcessDataBase tProcessDataBase = tProcessDataBaseHandler.getProcess(_currentProcessId);

		// get all values of this row database
		_processName = tProcessDataBase.getProcessName();
		_projectName = tProcessDataBase.getProjectName();
		_projectId = tProcessDataBase.getProjectId();
		_processDescription = tProcessDataBase.getProcessDescription();
		_processDefectNote = tProcessDataBase.getProcessNotes();
		_processTotalCycleTime = tProcessDataBase.getTotalCycleTime();
		_processValueAddingTime = tProcessDataBase.getValueAddingTime();
		_processNonValueAddingTime = tProcessDataBase.getNonValueAddingTime();
		_processDefectPercent = tProcessDataBase.getDefectPercent();
		_processTotOperators = tProcessDataBase.getTotOperators();
		_processShifts = tProcessDataBase.getShifts();
		_processAvailability = tProcessDataBase.getAvailability();
		_processTotDistanceTraveled = tProcessDataBase.getTotDistanceTraveled();
		_processUpTime = tProcessDataBase.getUpTime();
		_processChangeOverTime = tProcessDataBase.getChangeOverTime();
		_processTaktTime = tProcessDataBase.getTaktTime();
		_processVersionId = tProcessDataBase.getVersionId();
		_processPreviousProcess = tProcessDataBase.getPreviousProcess();
		_processNextProcess = tProcessDataBase.getNextProcess();
		_processVsmName = tProcessDataBase.getVsmName();

		// add value to edit text
		Log.e("_processDefectPercent ", " _processDefectPercent " + _processDefectPercent);
		Log.e("_processNonValueAddingTime", "_processNonValueAddingTime "
				+ _processNonValueAddingTime);
		editTextProcessCommunication.setText("" + _processCommunication);
		editTextProcessDefectNotes.setText("" + _processDefectNote);
		editTextProcessDefectPercent.setText("" + _processDefectPercent);
		editTextProcessDescription.setText("" + _processDescription);
		editTextProcessEndPoint.setText("" + _processEndPoint);
		editTextProcessName.setText("" + _processName);
		editTextProcessNonValueAddingTime.setText("" + _processNonValueAddingTime);
		editTextProcessOutPutInventory.setText("" + _processOutPutInventory);
		editTextProcessStartPoint.setText("" + _processStartPoint);
		editTextProcessUpTime.setText("" + _processUpTime);
		editTextProcessValueAddingTime.setText("" + _processValueAddingTime);
	}
}
