package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TStepsDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "StepManager";

	// Contacts table name
	private static final String TABLE_T_STEPS = "TABLE_T_STEPS";

	// Contacts Table Columns names
	private static final String KEY_STEP_ID = "KEY_STEP_ID";
	private static final String KEY_PROCESS_ID = "KEY_PROCESS_ID";
	private static final String KEY_PROJECT_ID = "KEY_PROJECT_ID";
	private static final String KEY_STEP_NO = "KEY_STEP_NO";
	private static final String KEY_STEP_DESCRIPTION = "KEY_STEP_DESCRIPTION";
	private static final String KEY_VERSION_ID = "KEY_VERSION_ID";
	private static final String KEY_PREVIOUS_VERS_ID = "KEY_PREVIOUS_VERS_ID";
	private static final String KEY_VIDEO_FILE_NAME = "KEY_VIDEO_FILE_NAME";

	public TStepsDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_STEPS + "(" + KEY_STEP_ID
				+ " INTEGER PRIMARY KEY," + KEY_PROCESS_ID + " INTEGER," + KEY_PROJECT_ID
				+ " INTEGER," + KEY_STEP_NO + " INTEGER," + KEY_STEP_DESCRIPTION + " TEXT,"
				+ KEY_VERSION_ID + " INTEGER," + KEY_PREVIOUS_VERS_ID + " TEXT,"
				+ KEY_VIDEO_FILE_NAME + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// update database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_STEPS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new step
	public void addNewStep(TStepsDataBase tSteps) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_STEP_ID, tSteps.getStepID()); // step id
		values.put(KEY_PROCESS_ID, tSteps.getProcessId()); // process name
		values.put(KEY_PROJECT_ID, tSteps.getProjectID()); // project id
		values.put(KEY_STEP_NO, tSteps.getStepNo());// step no
		// step description
		values.put(KEY_STEP_DESCRIPTION, tSteps.getStepDescription());
		values.put(KEY_VERSION_ID, tSteps.getVersionID());// version id
		// previous version id
		values.put(KEY_PREVIOUS_VERS_ID, tSteps.getPreviousVesID());
		// video file name
		values.put(KEY_VIDEO_FILE_NAME, tSteps.getVideoFileName());
		// Inserting Row
		db.insert(TABLE_T_STEPS, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single step
	public TStepsDataBase getStep(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_STEPS, new String[] { KEY_STEP_ID, KEY_PROCESS_ID,
				KEY_PROJECT_ID, KEY_STEP_NO, KEY_STEP_DESCRIPTION, KEY_VERSION_ID,
				KEY_PREVIOUS_VERS_ID, KEY_VIDEO_FILE_NAME }, KEY_STEP_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		TStepsDataBase tStep = new TStepsDataBase(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
				Integer.parseInt(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor
						.getString(5)), Integer.parseInt(cursor.getString(6)), cursor.getString(7));

		db.close();
		// return contact
		return tStep;
	}

	// Getting all process from a process id
	public List<TStepsDataBase> getAllStep(int processId) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<TStepsDataBase> stepList = new ArrayList<TStepsDataBase>();
		Cursor cursor = db.query(TABLE_T_STEPS, new String[] { KEY_STEP_ID, KEY_PROCESS_ID,
				KEY_PROJECT_ID, KEY_STEP_NO, KEY_STEP_DESCRIPTION, KEY_VERSION_ID,
				KEY_PREVIOUS_VERS_ID, KEY_VIDEO_FILE_NAME }, KEY_PROCESS_ID + "=?",
				new String[] { String.valueOf(processId) }, null, null, null, null);
		// looping through all rows and adding to list
		Log.e("cursor count", "count "+cursor.getCount());
		if (cursor.moveToFirst()) {
			do {

				TStepsDataBase step = new TStepsDataBase();
				step.setStepID(Integer.parseInt(cursor.getString(0)));
				step.setProcessId(Integer.parseInt(cursor.getString(1)));
				step.setProjectID(Integer.parseInt(cursor.getString(2)));
				step.setStepNo(Integer.parseInt(cursor.getString(3)));
				step.setStepDescription(cursor.getString(4));
				step.setVersionID(Integer.parseInt(cursor.getString(5)));
				step.setPreviousVesID(Integer.parseInt(cursor.getString(6)));
				step.setVideoFileName(cursor.getString(7));

				// Adding contact to list
				stepList.add(step);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return stepList;
	}

	// Getting All step
	public List<TStepsDataBase> getAllSteps() {
		List<TStepsDataBase> stepLists = new ArrayList<TStepsDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_STEPS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TStepsDataBase step = new TStepsDataBase();
				step.setStepID(Integer.parseInt(cursor.getString(0)));
				step.setProcessId(Integer.parseInt(cursor.getString(1)));
				step.setProjectID(Integer.parseInt(cursor.getString(2)));
				step.setStepNo(Integer.parseInt(cursor.getString(3)));
				step.setStepDescription(cursor.getString(4));
				step.setVersionID(Integer.parseInt(cursor.getString(5)));
				step.setPreviousVesID(Integer.parseInt(cursor.getString(6)));
				step.setVideoFileName(cursor.getString(7));
				// Adding step to list steps
				stepLists.add(step);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return stepLists;
	}

	// Updating single step
	public int updateProject(TStepsDataBase step) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROCESS_ID, step.getProcessId()); // process name
		values.put(KEY_PROJECT_ID, step.getProjectID()); // project id
		values.put(KEY_STEP_NO, step.getStepNo());// step no
		// step description
		values.put(KEY_STEP_DESCRIPTION, step.getStepDescription());
		values.put(KEY_VERSION_ID, step.getVersionID());// version id
		// previous version id
		values.put(KEY_PREVIOUS_VERS_ID, step.getPreviousVesID());
		// video file name
		values.put(KEY_VIDEO_FILE_NAME, step.getVideoFileName());
		// updating row
		return db.update(TABLE_T_STEPS, values, KEY_STEP_ID + " = ?",
				new String[] { String.valueOf(step.getStepID()) });
	}

	// Deleting single step
	public void deleteStep(TStepsDataBase step) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_STEPS, KEY_STEP_ID + " = ?",
				new String[] { String.valueOf(step.getStepID()) });
		db.close();
	}

	// Getting steps Count
	public int getStepsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_STEPS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
	
		// return count
		return cursor.getCount();
	}
}
