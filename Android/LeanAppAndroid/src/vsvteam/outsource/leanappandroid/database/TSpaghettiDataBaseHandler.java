package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TSpaghettiDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "SpaghettiManager";

	// Contacts table name
	private static final String TABLE_T_SPAGHETTI = "TABLE_T_SPAGHETTI";

	// Contacts Table Columns names
	private static final String KEY_SPAGHETTI_ID = "KEY_SPAGHETTI_ID";
	private static final String KEY_PROCESS_ID = "KEY_PROCESS_ID";
	private static final String KEY_PROJECT_ID = "KEY_PROJECT_ID";
	private static final String KEY_PROCESS_NAME = "KEY_PROCESS_NAME";
	private static final String KEY_PROJECT_NAME = "KEY_PROJECT_NAME";
	private static final String KEY_STEP_ID = "KEY_STEP_ID";
	private static final String KEY_STEP_DESCRIPTION = "KEY_STEP_DESCRIPTION";
	private static final String KEY_PRE_STEP_ID = "KEY_PRE_STEP_ID";
	private static final String KEY_NEXT_STEP_ID = "KEY_NEXT_STEP_ID";
	private static final String KEY_DISTANCE_PREVIOUS_STEP = "KEY_DISTANCE_PREVIOUS_STEP";

	private static final String KEY_DISTANCE_NEXT_STEP = "KEY_DISTANCE_NEXT_STEP";
	private static final String KEY_DISTANCE_UNIT = "KEY_DISTANCE_UNIT";
	private static final String KEY_TRAVEL_SPEED = "KEY_TRAVEL_SPEED";
	private static final String KEY_TRAVEL_UNIT = "KEY_TRAVEL_UNIT";
	private static final String KEY_TIME_TO_NEXT = "KEY_TIME_TO_NEXT";
	private static final String KEY_TIME_FROM_PREVIOUS = "KEY_TIME_FROM_PREVIOUS";
	private static final String KEY_VERSION_ID = "KEY_VERSION_ID";
	private static final String KEY_STATUS = "KEY_STATUS";
	private static final String KEY_PREV_VERS_SAVED_TIME = "KEY_PREV_VERS_SAVED_TIME";
	private static final String KEY_PREV_VERS_SAVED_DISTANCE = "KEY_PREV_VERS_SAVED_DISTANCE";

	public TSpaghettiDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create db spaghetti", "create db spaghetti");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_SPAGHETTI + " ("
				+ KEY_SPAGHETTI_ID + " INTEGER PRIMARY KEY," + KEY_PROCESS_ID + " INTEGER,"
				+ KEY_PROJECT_ID + " INTEGER," + KEY_PROCESS_NAME + " TEXT," + KEY_PROJECT_NAME
				+ " TEXT," + KEY_STEP_ID + " INTEGER," + KEY_STEP_DESCRIPTION + " TEXT,"
				+ KEY_PRE_STEP_ID + " INTEGER," + KEY_NEXT_STEP_ID + " INTEGER,"
				+ KEY_DISTANCE_PREVIOUS_STEP + " INTEGER," + KEY_DISTANCE_NEXT_STEP + " INTEGER,"
				+ KEY_DISTANCE_UNIT + " TEXT," + KEY_TRAVEL_SPEED + " INTEGER," + KEY_TRAVEL_UNIT
				+ " TEXT," + KEY_TIME_TO_NEXT + " TEXT," + KEY_TIME_FROM_PREVIOUS + " TEXT,"
				+ KEY_VERSION_ID + " INTEGER," + KEY_STATUS + " TEXT," + KEY_PREV_VERS_SAVED_TIME
				+ " TEXT," + KEY_PREV_VERS_SAVED_DISTANCE + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_SPAGHETTI);

		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new spaghetti
	public void addNewSpaghetti(TSpaghettiDataBase spaghetti) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// spaghetti id
		values.put(KEY_SPAGHETTI_ID, spaghetti.getSpaghettiID());
		values.put(KEY_PROCESS_ID, spaghetti.getProcessId());// spaghetti id
		values.put(KEY_PROJECT_ID, spaghetti.getProjectId()); // project id
		// spaghetti name
		values.put(KEY_PROCESS_NAME, spaghetti.getProcessName());
		// project name
		values.put(KEY_PROJECT_NAME, spaghetti.getProjectName());

		// step id
		values.put(KEY_STEP_ID, spaghetti.getStepId());
		// step description
		values.put(KEY_STEP_DESCRIPTION, spaghetti.getStepDescription());
		// previous step id
		values.put(KEY_PRE_STEP_ID, spaghetti.getPrevStepId());
		// next step id
		values.put(KEY_NEXT_STEP_ID, spaghetti.getNextStepId());
		// distance previous step
		values.put(KEY_DISTANCE_PREVIOUS_STEP, spaghetti.getDistancePrevStep());
		// distance next step
		values.put(KEY_DISTANCE_NEXT_STEP, spaghetti.getDistanceNextStep());
		// distance unit
		values.put(KEY_DISTANCE_UNIT, spaghetti.getDistanceUnit());
		// travel speed
		values.put(KEY_TRAVEL_SPEED, spaghetti.getTravelSpeed());
		// travel unit
		values.put(KEY_TRAVEL_UNIT, spaghetti.getTravelUnit());
		// time to next
		values.put(KEY_TIME_TO_NEXT, spaghetti.getTimeToNext());
		// time from previout
		values.put(KEY_TIME_FROM_PREVIOUS, spaghetti.getTimeFromPrevious());
		// version id
		values.put(KEY_VERSION_ID, spaghetti.getVersionId());
		// status
		values.put(KEY_STATUS, spaghetti.getStatus());
		// previous version saved time
		values.put(KEY_PREV_VERS_SAVED_TIME, spaghetti.getPreVerSavedTime());
		// previous version saved distance
		values.put(KEY_PREV_VERS_SAVED_DISTANCE, spaghetti.getPreVerSavedDistance());

		// Inserting Row
		db.insert(TABLE_T_SPAGHETTI, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting all spaghetti from a project id
	public List<TSpaghettiDataBase> getAllSpaghetti(int projectId) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<TSpaghettiDataBase> tSpaghettiList = new ArrayList<TSpaghettiDataBase>();
		Cursor cursor = db.query(TABLE_T_SPAGHETTI,
				new String[] { KEY_SPAGHETTI_ID, KEY_PROCESS_ID, KEY_PROJECT_ID, KEY_PROCESS_NAME,
						KEY_PROJECT_NAME, KEY_STEP_ID, KEY_STEP_DESCRIPTION, KEY_PRE_STEP_ID,
						KEY_NEXT_STEP_ID, KEY_DISTANCE_PREVIOUS_STEP, KEY_DISTANCE_NEXT_STEP,
						KEY_DISTANCE_UNIT, KEY_TRAVEL_SPEED, KEY_TRAVEL_UNIT, KEY_TIME_TO_NEXT,
						KEY_TIME_FROM_PREVIOUS, KEY_VERSION_ID, KEY_STATUS,
						KEY_PREV_VERS_SAVED_TIME, KEY_PREV_VERS_SAVED_DISTANCE }, KEY_PROJECT_ID
						+ "=?", new String[] { String.valueOf(projectId) }, null, null, null, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				TSpaghettiDataBase tSpaghetti = new TSpaghettiDataBase();
				tSpaghetti.setSpaghettiID(Integer.parseInt(cursor.getString(0)));
				tSpaghetti.setProcessId(Integer.parseInt(cursor.getString(1)));
				tSpaghetti.setProjectId(Integer.parseInt(cursor.getString(2)));
				tSpaghetti.setProcessName(cursor.getString(3));
				tSpaghetti.setProjectName(cursor.getString(4));

				tSpaghetti.setStepId(Integer.parseInt(cursor.getString(5)));
				tSpaghetti.setStepDescription(cursor.getString(6));
				tSpaghetti.setPrevStepId(Integer.parseInt(cursor.getString(7)));
				tSpaghetti.setNextStepId(Integer.parseInt(cursor.getString(8)));
				tSpaghetti.setDistancePrevStep(Integer.parseInt(cursor.getString(9)));
				tSpaghetti.setDistanceNextStep(Integer.parseInt(cursor.getString(10)));
				tSpaghetti.setDistanceUnit(cursor.getString(11));
				tSpaghetti.setTravelSpeed(Integer.parseInt(cursor.getString(12)));
				tSpaghetti.setTravelUnit(cursor.getString(13));
				tSpaghetti.setTimeToNext(cursor.getString(14));
				tSpaghetti.setTimeFromPrevious(cursor.getString(15));
				tSpaghetti.setVersionId(Integer.parseInt(cursor.getString(16)));
				tSpaghetti.setStatus(cursor.getString(17));
				tSpaghetti.setPreVerSavedTime(cursor.getString(18));
				tSpaghetti.setPreVerSavedDistance(cursor.getString(19));

				// Adding contact to list
				tSpaghettiList.add(tSpaghetti);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return tSpaghettiList;
	}

	// Getting All tSpaghetti
	public List<TSpaghettiDataBase> getAllSpaghetti() {
		List<TSpaghettiDataBase> spaghettiList = new ArrayList<TSpaghettiDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_SPAGHETTI;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				TSpaghettiDataBase tSpaghetti = new TSpaghettiDataBase();
				tSpaghetti.setSpaghettiID(Integer.parseInt(cursor.getString(0)));
				tSpaghetti.setProcessId(Integer.parseInt(cursor.getString(1)));
				tSpaghetti.setProjectId(Integer.parseInt(cursor.getString(2)));
				tSpaghetti.setProcessName(cursor.getString(3));
				tSpaghetti.setProjectName(cursor.getString(4));

				tSpaghetti.setStepId(Integer.parseInt(cursor.getString(5)));
				tSpaghetti.setStepDescription(cursor.getString(6));
				tSpaghetti.setPrevStepId(Integer.parseInt(cursor.getString(7)));
				tSpaghetti.setNextStepId(Integer.parseInt(cursor.getString(8)));
				tSpaghetti.setDistancePrevStep(Integer.parseInt(cursor.getString(9)));
				tSpaghetti.setDistanceNextStep(Integer.parseInt(cursor.getString(10)));
				tSpaghetti.setDistanceUnit(cursor.getString(11));
				tSpaghetti.setTravelSpeed(Integer.parseInt(cursor.getString(12)));
				tSpaghetti.setTravelUnit(cursor.getString(13));
				tSpaghetti.setTimeToNext(cursor.getString(14));
				tSpaghetti.setTimeFromPrevious(cursor.getString(15));
				tSpaghetti.setVersionId(Integer.parseInt(cursor.getString(16)));
				tSpaghetti.setStatus(cursor.getString(17));
				tSpaghetti.setPreVerSavedTime(cursor.getString(18));
				tSpaghetti.setPreVerSavedDistance(cursor.getString(19));

				// Adding spaghetti to list
				spaghettiList.add(tSpaghetti);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return spaghettiList;
	}

	// Updating single process
	public int updateSpaghetti(TSpaghettiDataBase spaghetti) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROCESS_ID, spaghetti.getProcessId());// spaghetti id
		values.put(KEY_PROJECT_ID, spaghetti.getProjectId()); // project id
		// spaghetti name
		values.put(KEY_PROCESS_NAME, spaghetti.getProcessName());
		// project name
		values.put(KEY_PROJECT_NAME, spaghetti.getProjectName());

		// step id
		values.put(KEY_STEP_ID, spaghetti.getStepId());
		// step description
		values.put(KEY_STEP_DESCRIPTION, spaghetti.getStepDescription());
		// previous step id
		values.put(KEY_PRE_STEP_ID, spaghetti.getPrevStepId());
		// next step id
		values.put(KEY_NEXT_STEP_ID, spaghetti.getNextStepId());
		// distance previous step
		values.put(KEY_DISTANCE_PREVIOUS_STEP, spaghetti.getDistancePrevStep());
		// distance next step
		values.put(KEY_DISTANCE_NEXT_STEP, spaghetti.getDistanceNextStep());
		// distance unit
		values.put(KEY_DISTANCE_UNIT, spaghetti.getDistanceUnit());
		// travel speed
		values.put(KEY_TRAVEL_SPEED, spaghetti.getTravelSpeed());
		// travel unit
		values.put(KEY_TRAVEL_UNIT, spaghetti.getTravelUnit());
		// time to next
		values.put(KEY_TIME_TO_NEXT, spaghetti.getTimeToNext());
		// time from previout
		values.put(KEY_TIME_FROM_PREVIOUS, spaghetti.getTimeFromPrevious());
		// version id
		values.put(KEY_VERSION_ID, spaghetti.getVersionId());
		// status
		values.put(KEY_STATUS, spaghetti.getStatus());
		// previous version saved time
		values.put(KEY_PREV_VERS_SAVED_TIME, spaghetti.getPreVerSavedTime());
		// previous version saved distance
		values.put(KEY_PREV_VERS_SAVED_DISTANCE, spaghetti.getPreVerSavedDistance());

		// updating row
		return db.update(TABLE_T_SPAGHETTI, values, KEY_PROCESS_ID + " = ?",
				new String[] { String.valueOf(spaghetti.getProjectId()) });
	}

	// Deleting single spaghetti
	public void deleteProcess(TSpaghettiDataBase spaghetti) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_SPAGHETTI, KEY_PROCESS_ID + " = ?",
				new String[] { String.valueOf(spaghetti.getProjectId()) });
		db.close();
	}

	// Getting spaghetti Count
	public int getSpaghettiCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_SPAGHETTI;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}
}
