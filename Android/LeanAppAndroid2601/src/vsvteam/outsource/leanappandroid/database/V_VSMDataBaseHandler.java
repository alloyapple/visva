package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class V_VSMDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ProjectManager";

	// Contacts table name
	private static final String TABLE_V_VSM = "V_VSMManager";
	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_PROJECT_NAME = "KEY_PROJECT_NAME";
	private static final String KEY_VSM_NAME = "KEY_VSM_NAME";
	private static final String KEY_VALUE_ADDING_TIME = "KEY_VALUE_ADDING_TIME";
	private static final String KEY_NON_VALUE_ADDING_TIME = "KEY_NON_VALUE_ADDING_TIME";
	private static final String KEY_TOTAL_TIME = "KEY_TOTAL_TIME";
	private static final String KEY_VERSION_ID = "KEY_VERSION_ID";

	// constructor
	public V_VSMDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String V_VSM_TABLE = "CREATE TABLE " + TABLE_V_VSM + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_PROJECT_NAME + " TEXT," + KEY_VSM_NAME + " TEXT," + KEY_VALUE_ADDING_TIME
				+ " TEXT," + KEY_NON_VALUE_ADDING_TIME + " TEXT," + KEY_TOTAL_TIME + " TEXT,"
				+ KEY_VERSION_ID + " INTEGER" + ")";
		db.execSQL(V_VSM_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_V_VSM);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new VSM
	public void addNewVSM(V_VSMDataBase v_vsmDataBase) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, v_vsmDataBase.getProjectID()); // project id
		values.put(KEY_PROJECT_NAME, v_vsmDataBase.getProjectName()); // project
																		// name
		values.put(KEY_VSM_NAME, v_vsmDataBase.getVSMName());// vsm name
		// value adding time
		values.put(KEY_VALUE_ADDING_TIME, v_vsmDataBase.getValueAddingTime());
		// non value adding time
		values.put(KEY_NON_VALUE_ADDING_TIME, v_vsmDataBase.getNonValueAddingTime());
		values.put(KEY_TOTAL_TIME, v_vsmDataBase.getTotalTime());// total time
		values.put(KEY_VERSION_ID, v_vsmDataBase.getVersionId());// version id

		// insert to database
		db.insert(TABLE_V_VSM, null, values);
		db.close();
	}

	// Getting single vsm
	public V_VSMDataBase getVSM(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_V_VSM, new String[] { KEY_ID, KEY_PROJECT_NAME,
				KEY_VSM_NAME, KEY_VALUE_ADDING_TIME, KEY_NON_VALUE_ADDING_TIME, KEY_TOTAL_TIME,
				KEY_VERSION_ID }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null,
				null, null);

		if (cursor != null)
			cursor.moveToFirst();

		V_VSMDataBase vsmDataBase = new V_VSMDataBase(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), Integer.parseInt(cursor.getString(6)));

		db.close();
		return vsmDataBase;
	}

	// Getting All VSM
	public List<V_VSMDataBase> getAllVSMs() {
		List<V_VSMDataBase> vsmList = new ArrayList<V_VSMDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_V_VSM;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				V_VSMDataBase vsm = new V_VSMDataBase();
				vsm.setProjectID(Integer.parseInt(cursor.getString(0)));
				vsm.setProjectName(cursor.getString(1));
				vsm.setVSMName(cursor.getString(2));
				vsm.setValueAddingTime(cursor.getString(3));
				vsm.setNonValueAddingTime(cursor.getString(4));
				vsm.setTotalTime(cursor.getString(5));
				vsm.setVersionId(Integer.parseInt(cursor.getString(6)));
				// Adding contact to list
				vsmList.add(vsm);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return vsmList;
	}

	// Updating single vsm
	public int updateProject(V_VSMDataBase vsm) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_NAME, vsm.getProjectName());
		values.put(KEY_VSM_NAME, vsm.getVSMName());
		values.put(KEY_VALUE_ADDING_TIME, vsm.getValueAddingTime());
		values.put(KEY_NON_VALUE_ADDING_TIME, vsm.getNonValueAddingTime());
		values.put(KEY_TOTAL_TIME, vsm.getTotalTime());
		values.put(KEY_VERSION_ID, vsm.getVersionId());
		// updating row
		return db.update(TABLE_V_VSM, values, KEY_ID + " = ?",
				new String[] { String.valueOf(vsm.getProjectID()) });
	}

	// Deleting single vsm table
	public void deleteVSMTable(V_VSMDataBase vsm) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_V_VSM, KEY_ID + " = ?", new String[] { String.valueOf(vsm.getProjectID()) });
		db.close();
	}

	// Getting vsm count
	public int getVSMsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_V_VSM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
}
