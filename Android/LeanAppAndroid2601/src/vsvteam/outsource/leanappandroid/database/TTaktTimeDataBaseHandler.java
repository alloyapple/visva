package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TTaktTimeDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "TTaktTimeManager";

	// Contacts table name
	private static final String TABLE_T_TAKTTIME = "TABLE_T_TAKTTIME";
	// Contacts Table Columns names
	private static final String KEY_TAKT_TIME_ID = "KEY_TAKT_TIME_ID";
	private static final String KEY_PROCESS_ID = "KEY_PROCESS_ID";
	private static final String KEY_PROJECT_ID = "KEY_PROJECT_ID";
	private static final String KEY_PROCESS_NAME = "KEY_PROCESS_NAME";
	private static final String KEY_PROJECT_NAME = "KEY_PROJECT_NAME";
	private static final String KEY_SHIFT_PER_DAY = "KEY_SHIFT_PER_DAY";
	private static final String KEY_HOUR_PER_SHIFT = "KEY_HOUR_PER_SHIFT";
	private static final String KEY_BREAK_TIME_PER_SHIFT = "KEY_BREAK_TIME_PER_SHIFT";
	private static final String KEY_OPERATOR_PER_SHIFT = "KEY_OPERATOR_PER_SHIFT";
	private static final String KEY_CUSTOMER_DEMAND_PER_UNITS = "KEY_CUSTOMER_DEMAND_PER_UNITS";
	private static final String KEY_CALCULATED_TAKT_TIME = "KEY_CALCULATED_TAKT_TIME";
	private static final String KEY_VERSION_ID = "KEY_VERSION_ID";

	public TTaktTimeDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create db takt time", "create db takttime");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_TAKTTIME + "(" + KEY_TAKT_TIME_ID
				+ " INTEGER PRIMARY KEY," + KEY_PROCESS_ID + " INTEGER," + KEY_PROJECT_ID
				+ " INTEGER," + KEY_PROCESS_NAME + " TEXT," + KEY_PROJECT_NAME + " TEXT,"
				+ KEY_SHIFT_PER_DAY + " INTEGER," + KEY_HOUR_PER_SHIFT + " INTEGER,"
				+ KEY_BREAK_TIME_PER_SHIFT + " INTEGER," + KEY_OPERATOR_PER_SHIFT + " INTEGER,"
				+ KEY_CUSTOMER_DEMAND_PER_UNITS + " INTEGER," + KEY_CALCULATED_TAKT_TIME + " TEXT,"
				+ KEY_VERSION_ID + " INTEGER" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// upgrade database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_TAKTTIME);

		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new project
	public void addNewTaktTime(TTaktTimeDataBase taktTime) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TAKT_TIME_ID, taktTime.getTaktTimeId()); // takt time id
		values.put(KEY_PROCESS_ID, taktTime.getProcessId()); // process id
		values.put(KEY_PROJECT_ID, taktTime.getProjectId());// project id
		values.put(KEY_PROCESS_NAME, taktTime.getProcessName()); // process name
		values.put(KEY_PROJECT_NAME, taktTime.getProjectName());// project name
		// shift per day
		values.put(KEY_SHIFT_PER_DAY, taktTime.getShiftPerDay());
		// hour per shift
		values.put(KEY_HOUR_PER_SHIFT, taktTime.getHourPerShift());
		// break time per shift
		values.put(KEY_BREAK_TIME_PER_SHIFT, taktTime.getBreakTimePerShift());
		// operator per shift
		values.put(KEY_OPERATOR_PER_SHIFT, taktTime.getOperatorPerShift());
		// customer demand per shift
		values.put(KEY_CUSTOMER_DEMAND_PER_UNITS, taktTime.getCustomerDemandPerUnits());
		// calculated takt time
		values.put(KEY_CALCULATED_TAKT_TIME, taktTime.getCalculatdTaktTime());
		// version id
		values.put(KEY_VERSION_ID, taktTime.getVersionId());

		// Inserting Row
		db.insert(TABLE_T_TAKTTIME, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single project
	public TTaktTimeDataBase getTaktTime(int processId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_TAKTTIME, new String[] { KEY_TAKT_TIME_ID, KEY_PROCESS_ID,
				KEY_PROJECT_ID, KEY_PROCESS_NAME, KEY_PROJECT_NAME, KEY_SHIFT_PER_DAY,
				KEY_HOUR_PER_SHIFT, KEY_BREAK_TIME_PER_SHIFT, KEY_OPERATOR_PER_SHIFT,
				KEY_CUSTOMER_DEMAND_PER_UNITS, KEY_CALCULATED_TAKT_TIME, KEY_VERSION_ID },
				KEY_PROCESS_ID + "=?", new String[] { String.valueOf(processId) }, null, null,
				null, null);
		if (cursor != null)
			cursor.moveToFirst();

		TTaktTimeDataBase taktTime = new TTaktTimeDataBase(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
				cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)),
				Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
				Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)),
				cursor.getString(10),Integer.parseInt( cursor.getString(11)));
		db.close();
		// return contact
		return taktTime;
	}

	// Getting All takt time
	public List<TTaktTimeDataBase> getAllTaktTime() {
		List<TTaktTimeDataBase> taktTimeList = new ArrayList<TTaktTimeDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_TAKTTIME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TTaktTimeDataBase taktTime = new TTaktTimeDataBase();
				taktTime.setTaktTimeId(Integer.parseInt(cursor.getString(0)));
				taktTime.setProcessId(Integer.parseInt(cursor.getString(1)));
				taktTime.setProjectId(Integer.parseInt(cursor.getString(2)));
				taktTime.setProcessName(cursor.getString(3));
				taktTime.setProjectName(cursor.getString(4));
				taktTime.setShiftPerDay(Integer.parseInt(cursor.getString(5)));
				taktTime.setHourPerShift(Integer.parseInt(cursor.getString(6)));
				taktTime.setBreakTimePerShift(Integer.parseInt(cursor.getString(7)));
				taktTime.setOperatorPerShift(Integer.parseInt(cursor.getString(8)));
				taktTime.setCustomerDemandPerUnits(Integer.parseInt(cursor.getString(9)));
				taktTime.setCalculatdTaktTime(cursor.getString(10));
				taktTime.setVersionId(Integer.parseInt(cursor.getString(11)));
				// Adding contact to list
				taktTimeList.add(taktTime);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return taktTimeList;
	}

	// Updating single project
	public int updateTaktTime(TTaktTimeDataBase taktTime) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROCESS_ID, taktTime.getProcessId()); // process id
		values.put(KEY_PROJECT_ID, taktTime.getProjectId()); // project id
		values.put(KEY_PROCESS_NAME, taktTime.getProcessName()); // process name
		values.put(KEY_PROJECT_NAME, taktTime.getProjectName());// project name
		// shift per day
		values.put(KEY_SHIFT_PER_DAY, taktTime.getShiftPerDay());
		// hour per shift
		values.put(KEY_HOUR_PER_SHIFT, taktTime.getHourPerShift());
		// break time per shift
		values.put(KEY_BREAK_TIME_PER_SHIFT, taktTime.getBreakTimePerShift());
		// lunch time per shift
		values.put(KEY_OPERATOR_PER_SHIFT, taktTime.getOperatorPerShift());
		// customer demand per shift
		values.put(KEY_CUSTOMER_DEMAND_PER_UNITS, taktTime.getCustomerDemandPerUnits());
		// calculated takt time
		values.put(KEY_CALCULATED_TAKT_TIME, taktTime.getCalculatdTaktTime());
		// version id
		values.put(KEY_VERSION_ID, taktTime.getVersionId());

		// updating row
		return db.update(TABLE_T_TAKTTIME, values, KEY_PROCESS_ID + " = ?",
				new String[] { String.valueOf(taktTime.getProcessId()) });
	}

	// Deleting single takt time
	public void deleteTaktTime(TTaktTimeDataBase taktTime) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_TAKTTIME, KEY_TAKT_TIME_ID + " = ?",
				new String[] { String.valueOf(taktTime.getTaktTimeId()) });
		db.close();
	}

	// Getting takt time Count
	public int getTaktTimeCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_TAKTTIME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}
}
