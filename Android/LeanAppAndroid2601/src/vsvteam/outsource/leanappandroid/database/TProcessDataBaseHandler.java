package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TProcessDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ProcessManager";

	// Contacts table name
	private static final String TABLE_T_PROCESS = "T_PROCESS";

	// Contacts Table Columns names
	private static final String KEY_PROCESS_ID = "KEY_PROCESS_ID";
	private static final String KEY_PROJECT_ID = "KEY_PROJECT_ID";
	private static final String KEY_PROJECT_NAME = "KEY_PROJECT_NAME";
	private static final String KEY_PROCESS_NAME = "KEY_PROCESS_NAME";
	private static final String KEY_PROCESS_DESCRIPTION = "KEY_PROCESS_DESCRIPTION";
	private static final String KEY_DEFECT_NOTES = "KEY_DEFECT_NOTES";
	private static final String KEY_TOTAL_CYCLE_TIME = "KEY_TOTAL_CYCLE_TIME";
	private static final String KEY_VALUE_ADDING_TIME = "KEY_VALUE_ADDING_TIME";
	private static final String KEY_NON_VALUE_ADDING_TIME = "KEY_NON_VALUE_ADDING_TIME";
	private static final String KEY_DEFECT_PERCENT = "KEY_DEFECT_PERCENT";

	private static final String KEY_SHIFTS = "KEY_SHIFTS";
	private static final String KEY_TOT_DISTANCE_TRAVELED = "KEY_TOT_DISTANCE_TRAVELED";
	private static final String KEY_UPTIME = "KEY_UPTIME";
	private static final String KEY_TAKT_TIME = "KEY_TAKT_TIME";
	private static final String KEY_VERSION_ID = "KEY_VERSION_ID";
	private static final String KEY_PREVIOUS_PROCESS = "KEY_PREVIOUS_PROCESS";
	private static final String KEY_NEXT_PROCESS = "KEY_NEXT_PROCESS";
	private static final String KEY_VSM_NAME = "KEY_VSM_NAME";
	private static final String KEY_VERIFY_DETAILS = "KEY_VERIFY_DETAILS";
	private static final String KEY_OUTPUT_INVENTORY = "KEY_OUTPUT_INVENTORY";
	private static final String KEY_SUPPLIER_START_POINT = "KEY_SUPPLIER_START_POINT";
	private static final String KEY_CUSTOMER_END_POINT = "KEY_CUSTOMER_END_POINT";
	private static final String KEY_COMMUNICATION = "KEY_COMMUNICATION";

	public TProcessDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create db process", "create db process");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_PROCESS + " (" + KEY_PROCESS_ID
				+ " INTEGER PRIMARY KEY," + KEY_PROJECT_ID + " INTEGER," + KEY_PROJECT_NAME
				+ " TEXT," + KEY_PROCESS_NAME + " TEXT," + KEY_PROCESS_DESCRIPTION + " TEXT,"
				+ KEY_DEFECT_NOTES + " TEXT," + KEY_TOTAL_CYCLE_TIME + " INTEGER,"
				+ KEY_VALUE_ADDING_TIME + " INTEGER," + KEY_NON_VALUE_ADDING_TIME + " INTEGER,"
				+ KEY_DEFECT_PERCENT + " INTEGER," + KEY_SHIFTS + " INTEGER,"
				+ KEY_TOT_DISTANCE_TRAVELED + " INTEGER," + KEY_UPTIME + " INTEGER,"
				+ KEY_TAKT_TIME + " INTEGER," + KEY_VERSION_ID + " INTEGER," + KEY_PREVIOUS_PROCESS
				+ " TEXT," + KEY_NEXT_PROCESS + " TEXT," + KEY_VSM_NAME + " TEXT,"
				+ KEY_VERIFY_DETAILS + " TEXT," + KEY_OUTPUT_INVENTORY + " TEXT,"
				+ KEY_SUPPLIER_START_POINT + " TEXT," + KEY_CUSTOMER_END_POINT + " TEXT,"
				+ KEY_COMMUNICATION + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_PROCESS);

		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new process
	public void addNewProject(TProcessDataBase process) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROCESS_ID, process.getProcessId());// process id
		values.put(KEY_PROJECT_ID, process.getProjectId()); // project id
		values.put(KEY_PROJECT_NAME, process.getProjectName()); // project name
		values.put(KEY_PROCESS_NAME, process.getProcessName()); // process name
		// process description
		values.put(KEY_PROCESS_DESCRIPTION, process.getProcessDescription());
		// defect notes
		values.put(KEY_DEFECT_NOTES, process.getDefectNotes());
		// total cycle time
		values.put(KEY_TOTAL_CYCLE_TIME, process.getTotalCycleTime());
		// value adding time
		values.put(KEY_VALUE_ADDING_TIME, process.getValueAddingTime());
		// non value adding time
		values.put(KEY_NON_VALUE_ADDING_TIME, process.getNonValueAddingTime());
		// defect percent
		values.put(KEY_DEFECT_PERCENT, process.getDefectPercent());
		// shift
		values.put(KEY_SHIFTS, process.getShifts());
		// total distance traveled
		values.put(KEY_TOT_DISTANCE_TRAVELED, process.getTotDistanceTraveled());
		// uptime
		values.put(KEY_UPTIME, process.getUpTime());
		// takt time
		values.put(KEY_TAKT_TIME, process.getTaktTime());
		// version id
		values.put(KEY_VERSION_ID, process.getVersionId());
		// previous process
		values.put(KEY_PREVIOUS_PROCESS, process.getPreviousProcess());
		// next process
		values.put(KEY_NEXT_PROCESS, process.getNextProcess());
		// vsm name
		values.put(KEY_VSM_NAME, process.getVsmName());
		// verify details
		values.put(KEY_VERIFY_DETAILS, process.isVerify_details());
		// output inventory
		values.put(KEY_OUTPUT_INVENTORY, process.getOutputInventory());
		// supllier start point
		values.put(KEY_SUPPLIER_START_POINT, process.getSupplierStartPoint());
		// customer end point
		values.put(KEY_CUSTOMER_END_POINT, process.getCustomerEndPoint());
		// communication
		values.put(KEY_COMMUNICATION, process.getCommunication());

		// Inserting Row
		db.insert(TABLE_T_PROCESS, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single process
	public TProcessDataBase getProcess(int processId) {

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_PROCESS, new String[] { KEY_PROCESS_ID, KEY_PROJECT_ID,
				KEY_PROJECT_NAME, KEY_PROCESS_NAME, KEY_PROCESS_DESCRIPTION, KEY_DEFECT_NOTES,
				KEY_TOTAL_CYCLE_TIME, KEY_VALUE_ADDING_TIME, KEY_NON_VALUE_ADDING_TIME,
				KEY_DEFECT_PERCENT, KEY_SHIFTS, KEY_TOT_DISTANCE_TRAVELED, KEY_UPTIME,
				KEY_TAKT_TIME, KEY_VERSION_ID, KEY_PREVIOUS_PROCESS, KEY_NEXT_PROCESS,
				KEY_VSM_NAME, KEY_VERIFY_DETAILS, KEY_OUTPUT_INVENTORY, KEY_SUPPLIER_START_POINT,
				KEY_CUSTOMER_END_POINT, KEY_COMMUNICATION }, KEY_PROCESS_ID + "=?",
				new String[] { String.valueOf(processId) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		TProcessDataBase process = new TProcessDataBase(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)),
				Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)),
				Integer.parseInt(cursor.getString(9)), Integer.parseInt(cursor.getString(10)),
				Integer.parseInt(cursor.getString(11)), Integer.parseInt(cursor.getString(12)),
				Integer.parseInt(cursor.getString(13)), Integer.parseInt(cursor.getString(14)),
				cursor.getString(15), cursor.getString(16), cursor.getString(17),
				Boolean.parseBoolean(cursor.getString(18)), Integer.parseInt(cursor.getString(19)),
				cursor.getString(20), cursor.getString(21), cursor.getString(22));

		db.close();
		// return process
		return process;
	}

	// Getting all process from a project id
	public List<TProcessDataBase> getAllProcess(int projectId) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<TProcessDataBase> processList = new ArrayList<TProcessDataBase>();
		Cursor cursor = db.query(TABLE_T_PROCESS, new String[] { KEY_PROCESS_ID, KEY_PROJECT_ID,
				KEY_PROJECT_NAME, KEY_PROCESS_NAME, KEY_PROCESS_DESCRIPTION, KEY_DEFECT_NOTES,
				KEY_TOTAL_CYCLE_TIME, KEY_VALUE_ADDING_TIME, KEY_NON_VALUE_ADDING_TIME,
				KEY_DEFECT_PERCENT, KEY_SHIFTS, KEY_TOT_DISTANCE_TRAVELED, KEY_UPTIME,
				KEY_TAKT_TIME, KEY_VERSION_ID, KEY_PREVIOUS_PROCESS, KEY_NEXT_PROCESS,
				KEY_VSM_NAME, KEY_VERIFY_DETAILS, KEY_OUTPUT_INVENTORY, KEY_SUPPLIER_START_POINT,
				KEY_CUSTOMER_END_POINT, KEY_COMMUNICATION }, KEY_PROJECT_ID + "=?",
				new String[] { String.valueOf(projectId) }, null, null, null, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				TProcessDataBase process = new TProcessDataBase();
				process.setProcessId(Integer.parseInt(cursor.getString(0)));
				process.setProjectId(Integer.parseInt(cursor.getString(1)));
				process.setProjectName(cursor.getString(2));
				process.setProcessName(cursor.getString(3));
				process.setProcessDescription(cursor.getString(4));
				process.setDefectNotes(cursor.getString(5));
				process.setTotalCycleTime(Integer.parseInt(cursor.getString(6)));
				process.setValueAddingTime(Integer.parseInt(cursor.getString(7)));
				process.setNonValueAddingTime(Integer.parseInt(cursor.getString(8)));
				process.setDefectPercent(Integer.parseInt(cursor.getString(9)));

				process.setShifts(Integer.parseInt(cursor.getString(10)));
				process.setTotDistanceTraveled(Integer.parseInt(cursor.getString(11)));
				process.setUpTime(Integer.parseInt(cursor.getString(12)));
				process.setTaktTime(Integer.parseInt(cursor.getString(13)));
				process.setVersionId(Integer.parseInt(cursor.getString(14)));
				process.setPreviousProcess(cursor.getString(15));
				process.setNextProcess(cursor.getString(16));
				process.setVsmName(cursor.getString(17));

				process.setVerify_details(Boolean.parseBoolean(cursor.getString(18)));
				process.setOutputInventory(Integer.parseInt(cursor.getString(19)));
				process.setSupplierStartPoint(cursor.getString(20));
				process.setCustomerEndPoint(cursor.getString(21));
				process.setCommunication(cursor.getString(22));

				// Adding contact to list
				processList.add(process);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return processList;
	}

	// Getting All process
	public List<TProcessDataBase> getAllProcess() {
		List<TProcessDataBase> processList = new ArrayList<TProcessDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_PROCESS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				TProcessDataBase process = new TProcessDataBase();
				process.setProcessId(Integer.parseInt(cursor.getString(0)));
				process.setProjectId(Integer.parseInt(cursor.getString(1)));
				process.setProjectName(cursor.getString(2));
				process.setProcessName(cursor.getString(3));
				process.setProcessDescription(cursor.getString(4));
				process.setDefectNotes(cursor.getString(5));
				process.setTotalCycleTime(Integer.parseInt(cursor.getString(6)));
				process.setValueAddingTime(Integer.parseInt(cursor.getString(7)));
				process.setNonValueAddingTime(Integer.parseInt(cursor.getString(8)));
				process.setDefectPercent(Integer.parseInt(cursor.getString(9)));

				process.setShifts(Integer.parseInt(cursor.getString(10)));
				process.setTotDistanceTraveled(Integer.parseInt(cursor.getString(11)));
				process.setUpTime(Integer.parseInt(cursor.getString(12)));
				process.setTaktTime(Integer.parseInt(cursor.getString(13)));
				process.setVersionId(Integer.parseInt(cursor.getString(14)));
				process.setPreviousProcess(cursor.getString(15));
				process.setNextProcess(cursor.getString(16));
				process.setVsmName(cursor.getString(17));

				process.setVerify_details(Boolean.parseBoolean(cursor.getString(18)));
				process.setOutputInventory(Integer.parseInt(cursor.getString(19)));
				process.setSupplierStartPoint(cursor.getString(20));
				process.setCustomerEndPoint(cursor.getString(21));
				process.setCommunication(cursor.getString(22));

				// Adding contact to list
				processList.add(process);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return processList;
	}

	// Updating single process
	public int updateProcess(TProcessDataBase process) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROCESS_ID, process.getProcessId());// process id
		values.put(KEY_PROJECT_ID, process.getProjectId()); // project id
		values.put(KEY_PROJECT_NAME, process.getProjectName()); // project name
		values.put(KEY_PROCESS_NAME, process.getProcessName()); // process name
		// process description
		values.put(KEY_PROCESS_DESCRIPTION, process.getProcessDescription());
		// defect notes
		values.put(KEY_DEFECT_NOTES, process.getDefectNotes());
		// total cycle time
		values.put(KEY_TOTAL_CYCLE_TIME, process.getTotalCycleTime());
		// value adding time
		values.put(KEY_VALUE_ADDING_TIME, process.getValueAddingTime());
		// non value adding time
		values.put(KEY_NON_VALUE_ADDING_TIME, process.getNonValueAddingTime());
		// defect percent
		values.put(KEY_DEFECT_PERCENT, process.getDefectPercent());
		// shift
		values.put(KEY_SHIFTS, process.getShifts());
		// total distance traveled
		values.put(KEY_TOT_DISTANCE_TRAVELED, process.getTotDistanceTraveled());
		// uptime
		values.put(KEY_UPTIME, process.getUpTime());
		// takt time
		values.put(KEY_TAKT_TIME, process.getTaktTime());
		// version id
		values.put(KEY_VERSION_ID, process.getVersionId());
		// previous process
		values.put(KEY_PREVIOUS_PROCESS, process.getPreviousProcess());
		// next process
		values.put(KEY_NEXT_PROCESS, process.getNextProcess());
		// vsm name
		values.put(KEY_VSM_NAME, process.getVsmName());
		// verify details
		values.put(KEY_VERIFY_DETAILS, process.isVerify_details());
		// output inventory
		values.put(KEY_OUTPUT_INVENTORY, process.getOutputInventory());
		// supllier start point
		values.put(KEY_SUPPLIER_START_POINT, process.getSupplierStartPoint());
		// customer end point
		values.put(KEY_CUSTOMER_END_POINT, process.getCustomerEndPoint());
		// communication
		values.put(KEY_COMMUNICATION, process.getCommunication());

		// updating row
		return db.update(TABLE_T_PROCESS, values, KEY_PROCESS_ID + " = ?",
				new String[] { String.valueOf(process.getProcessId()) });
	}

	// Deleting single process
	public void deleteProcess(int processId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_PROCESS, KEY_PROCESS_ID + " = ?",
				new String[] { String.valueOf(processId) });
		db.close();
	}

	// Getting process Count
	public int getProcessCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_PROCESS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}

}
