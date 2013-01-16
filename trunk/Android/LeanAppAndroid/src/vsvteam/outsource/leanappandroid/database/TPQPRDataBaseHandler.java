package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.util.Log;

public class TPQPRDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "TPQPRManager";

	// Contacts table name
	private static final String TABLE_T_PQPR = "TABLE_T_PQPR";

	// Contacts Table Columns names
	private static final String KEY_T_PQPR = "KEY_T_PQPR";
	private static final String KEY_PROCESS_ID = "KEY_PROCESS_ID";
	private static final String KEY_PROJECT_ID = "KEY_PROJECT_ID";
	private static final String KEY_PROCESS_NAME = "KEY_PROCESS_NAME";
	private static final String KEY_PROJECT_NAME = "KEY_PROJECT_NAME";
	private static final String KEY_PART_NO = "KEY_PART_NO";
	private static final String KEY_DEMAND_QTY = "KEY_DEMAND_QTY";
	private static final String KEY_PERCENT_ON_TOTAL = "KEY_PERCENT_ON_TOTAL";
	private static final String KEY_SEQUENCE_NO = "KEY_SEQUENCE_NO";
	private static final String KEY_USED_IN_STEP_ID = "KEY_USED_IN_STEP_ID";
	private static final String KEY_VERSION_ID = "KEY_VERSION_ID";
	private static final String KEY_T_FOCUS = "KEY_T_FOCUS";

	public TPQPRDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create db process", "create db project");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_PQPR + "(" + KEY_T_PQPR
				+ " INTEGER PRIMARY KEY," + KEY_PROCESS_ID + " INTEGER," + KEY_PROJECT_ID
				+ " INTEGER," + KEY_PROCESS_NAME + " TEXT," + KEY_PROJECT_NAME + " TEXT,"
				+ KEY_PART_NO + " INTEGER," + KEY_DEMAND_QTY + " INTEGER," + KEY_PERCENT_ON_TOTAL
				+ " INTEGER," + KEY_SEQUENCE_NO + " INTEGER," + KEY_USED_IN_STEP_ID + " INTEGER,"
				+ KEY_VERSION_ID + " INTEGER," + KEY_T_FOCUS + " BOOLEAN" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	// upgrade db
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_PQPR);

		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new pqpr
	public void addNewPQPR(TPQPRDataBase pqpr) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_T_PQPR, pqpr.getTpqprId());// pqpr id
		// process id
		values.put(KEY_PROCESS_ID, pqpr.getProcessId());
		// project id
		values.put(KEY_PROJECT_ID, pqpr.getProjectId());
		// process id
		values.put(KEY_PROCESS_ID, pqpr.getProcessName());
		// project name
		values.put(KEY_PROJECT_NAME, pqpr.getProjectName());
		// part no
		values.put(KEY_PART_NO, pqpr.getPartNo());
		// demand qty
		values.put(KEY_DEMAND_QTY, pqpr.getDemandQty());
		// percent on total
		values.put(KEY_PERCENT_ON_TOTAL, pqpr.getPercentOnTotal());
		// sequence no
		values.put(KEY_SEQUENCE_NO, pqpr.getSequenceNo());
		// used in step id
		values.put(KEY_USED_IN_STEP_ID, pqpr.getUsedInStepId());
		// version id
		values.put(KEY_VERSION_ID, pqpr.getVersionId());
		// t focus
		values.put(KEY_T_FOCUS, pqpr.isTfocus());

		// Inserting Row
		db.insert(TABLE_T_PQPR, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single project
	public TPQPRDataBase getPQPR(int processId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_PQPR, new String[] { KEY_T_PQPR, KEY_PROCESS_ID,
				KEY_PROJECT_ID, KEY_PROCESS_NAME, KEY_PROJECT_NAME, KEY_PART_NO, KEY_DEMAND_QTY,
				KEY_PERCENT_ON_TOTAL, KEY_SEQUENCE_NO, KEY_USED_IN_STEP_ID, KEY_VERSION_ID,
				KEY_T_FOCUS }, KEY_PROCESS_ID + "=?", new String[] { String.valueOf(processId) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		TPQPRDataBase pqpr = new TPQPRDataBase(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
				cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(5)),
				Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
				Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)),
				Integer.parseInt(cursor.getString(10)), Boolean.parseBoolean(cursor.getString(11)));

		db.close();
		// return pqpr
		return pqpr;
	}

	// Getting All PQPR
	public List<TPQPRDataBase> getAllPQPR() {
		List<TPQPRDataBase> pqprList = new ArrayList<TPQPRDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_PQPR;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TPQPRDataBase pqpr = new TPQPRDataBase();
				pqpr.setTpqprId(Integer.parseInt(cursor.getString(0)));
				pqpr.setProcessId(Integer.parseInt(cursor.getString(1)));
				pqpr.setProjectId(Integer.parseInt(cursor.getString(2)));
				pqpr.setProcessName(cursor.getString(3));
				pqpr.setProjectName(cursor.getString(4));

				// Adding pqpr to list
				pqprList.add(pqpr);
			} while (cursor.moveToNext());
		}
		db.close();
		// return pqpr list
		return pqprList;
	}

	// Updating single PQPR
	public int updatePQPR(TPQPRDataBase pqpr) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// process id
		values.put(KEY_PROCESS_ID, pqpr.getProcessId());
		// project id
		values.put(KEY_PROJECT_ID, pqpr.getProjectId());
		// process id
		values.put(KEY_PROCESS_ID, pqpr.getProcessName());
		// project name
		values.put(KEY_PROJECT_NAME, pqpr.getProjectName());
		// part no
		values.put(KEY_PART_NO, pqpr.getPartNo());
		// demand qty
		values.put(KEY_DEMAND_QTY, pqpr.getDemandQty());
		// percent on total
		values.put(KEY_PERCENT_ON_TOTAL, pqpr.getPercentOnTotal());
		// sequence no
		values.put(KEY_SEQUENCE_NO, pqpr.getSequenceNo());
		// used in step id
		values.put(KEY_USED_IN_STEP_ID, pqpr.getUsedInStepId());
		// version id
		values.put(KEY_VERSION_ID, pqpr.getVersionId());
		// t focus
		values.put(KEY_T_FOCUS, pqpr.isTfocus());
		// updating row
		return db.update(TABLE_T_PQPR, values, KEY_PROCESS_ID + " = ?",
				new String[] { String.valueOf(pqpr.getProcessId()) });
	}

	// Deleting single pqpr
	public void deletePQPR(TPQPRDataBase pqpr) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_PQPR, KEY_PROCESS_ID + " = ?",
				new String[] { String.valueOf(pqpr.getProcessId()) });
		db.close();
	}

	// Getting pqpr Count
	public int getPQPRCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_PQPR;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}
}
