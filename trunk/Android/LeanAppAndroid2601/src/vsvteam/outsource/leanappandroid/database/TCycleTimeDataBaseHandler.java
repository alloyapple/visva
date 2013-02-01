package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TCycleTimeDataBaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "CycleTimeManager";

	// Contacts table name
	private static final String TABLE_T_CYCLETIME = "TABLE_T_CYCLETIME";

	// Contacts Table Columns names
	private static final String KEY_CYCLETIME_ID = "KEY_CYCLETIME_ID";
	private static final String KEY_PROCESS_ID = "KEY_PROCESS_ID";
	private static final String KEY_PROJECT_ID = "KEY_PROJECT_ID";
	private static final String KEY_PROCESS_NAME = "KEY_PROCESS_NAME";
	private static final String KEY_PROJECT_NAME = "KEY_PROJECT_NAME";
	private static final String KEY_CYCLE_COUNTER = "KEY_CYCLE_COUNTER";
	private static final String KEY_OPERATOR_NAME = "KEY_OPERATOR_NAME";
	private static final String KEY_SHIFT_NO = "KEY_SHIFT_NO";
	private static final String KEY_STEP_ID = "KEY_STEP_ID";
	private static final String KEY_STEP_DESCRIPTION = "KEY_STEP_DESCRIPTION";
	private static final String KEY_LOWEST_TIME = "KEY_LOWEST_TIME";
	private static final String KEY_ADJUSTMENT = "KEY_ADJUSTMENT";
	private static final String KEY_ADJUSTED_TIME = "KEY_ADJUSTED_TIME";
	private static final String KEY_AUDIO_NOTE = "KEY_AUDIO_NOTE";
	private static final String KEY_VIDEO_FILE_NAME = "KEY_VIDEO_FILE_NAME";
	private static final String KEY_START_TIME_STAMP = "KEY_START_TIME_STAMP";
	private static final String KEY_END_TIME_STAMP = "KEY_END_TIME_STAMP";
	private static final String KEY_VERSION_ID = "KEY_VERSION_ID";
	private static final String KEY_PREVIOUS_VERS_ID = "KEY_PREVIOUS_VERS_ID";
	private static final String KEY_PRE_VERS_DIFFERENCE_LOWEST_TIME = "KEY_PRE_VERS_DIFFERENCE_LOWEST_TIME";
	private static final String KEY_PRE_VERS_DIFFERENCE_ADJUSTED_TIME = "KEY_PRE_VERS_DIFFERENCE_ADJUSTED_TIME";
	private static final String KEY_PRE_VERS_DIFFERENCE_ADJUSTMENT = "KEY_PRE_VERS_DIFFERENCE_ADJUSTMENT";
	private static final String KEY_NO_VIDEO_ONLY_TIMING = "KEY_NO_VIDEO_ONLY_TIMING";
	private static final String KEY_USE_MILLISECONDS = "KEY_USE_MILLISECONDS";

	public TCycleTimeDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create db cycle time", "create db cycle time");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_CYCLETIME
				+ " (" + KEY_CYCLETIME_ID + " INTEGER PRIMARY KEY,"
				+ KEY_PROCESS_ID + " INTEGER," + KEY_PROJECT_ID + " INTEGER,"
				+ KEY_PROCESS_NAME + " TEXT," + KEY_PROJECT_NAME + " TEXT,"
				+ KEY_CYCLE_COUNTER + " INTEGER," + KEY_OPERATOR_NAME
				+ " TEXT," + KEY_SHIFT_NO + " INTEGER," + KEY_STEP_ID
				+ " INTEGER," + KEY_STEP_DESCRIPTION + " TEXT,"
				+ KEY_LOWEST_TIME + " TEXT," + KEY_ADJUSTMENT + " TEXT,"
				+ KEY_ADJUSTED_TIME + " TEXT," + KEY_AUDIO_NOTE + " TEXT,"
				+ KEY_VIDEO_FILE_NAME + " TEXT," + KEY_START_TIME_STAMP
				+ " TEXT," + KEY_END_TIME_STAMP + " TEXT," + KEY_VERSION_ID
				+ " INTEGER," + KEY_PREVIOUS_VERS_ID + " INTEGER,"
				+ KEY_PRE_VERS_DIFFERENCE_LOWEST_TIME + " TEXT,"
				+ KEY_PRE_VERS_DIFFERENCE_ADJUSTED_TIME + " TEXT,"
				+ KEY_PRE_VERS_DIFFERENCE_ADJUSTMENT + " TEXT,"
				+ KEY_NO_VIDEO_ONLY_TIMING + " BOOLEAN," + KEY_USE_MILLISECONDS
				+ " BOOLEAN" + ")";
		// execute sqlite
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	// upgrade database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_CYCLETIME);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new cycleTime
	public void addNewCycleTime(TCycleTimeDataBase cycleTime) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// cycle time id
		values.put(KEY_CYCLETIME_ID, cycleTime.getCycleTimeId());
		values.put(KEY_PROCESS_ID, cycleTime.getProcessId());// cycleTime id
		values.put(KEY_PROJECT_ID, cycleTime.getProjectId()); // project id
		// project name
		values.put(KEY_PROJECT_NAME, cycleTime.getProjectName());
		// cycle Time name
		values.put(KEY_PROCESS_NAME, cycleTime.getProcessName());
		// cycle counter
		values.put(KEY_CYCLE_COUNTER, cycleTime.getCycleCounter());
		// operator
		values.put(KEY_OPERATOR_NAME, cycleTime.getOperatorName());
		// shift no
		values.put(KEY_SHIFT_NO, cycleTime.getShiftNo());
		// step id
		values.put(KEY_STEP_ID, cycleTime.getStepId());
		// step description
		values.put(KEY_STEP_DESCRIPTION, cycleTime.getStepDescription());
		// lowest time
		values.put(KEY_LOWEST_TIME, cycleTime.getLowestTime());
		// adjustment
		values.put(KEY_ADJUSTMENT, cycleTime.getAdjustment());
		// adjust time
		values.put(KEY_ADJUSTED_TIME, cycleTime.getAdjustTime());
		// audio note
		values.put(KEY_AUDIO_NOTE, cycleTime.getAudioNote());
		// video file nam
		values.put(KEY_VIDEO_FILE_NAME, cycleTime.getVideoFileName());
		// start time stamp
		values.put(KEY_START_TIME_STAMP, cycleTime.getStartTimeStamp());
		// end time stamp
		values.put(KEY_END_TIME_STAMP, cycleTime.getEndTimeStamp());
		// version id
		values.put(KEY_VERSION_ID, cycleTime.getVersionId());
		// previous version id
		values.put(KEY_PREVIOUS_VERS_ID, cycleTime.getPreviousVersionId());
		// previous version diference lowest time
		values.put(KEY_PRE_VERS_DIFFERENCE_LOWEST_TIME,
				cycleTime.getPreVerDifferenceLowestTime());
		// previous version difference adjusted time
		values.put(KEY_PRE_VERS_DIFFERENCE_ADJUSTED_TIME,
				cycleTime.getPreVerDifferenceAdjustedTime());
		// next cycleTime
		values.put(KEY_PRE_VERS_DIFFERENCE_ADJUSTMENT,
				cycleTime.getPreVerDifferenceAdjustment());
		// no video only timing
		values.put(KEY_NO_VIDEO_ONLY_TIMING, cycleTime.isNoVideoOnlyTiming());
		// use milliseconds
		values.put(KEY_USE_MILLISECONDS, cycleTime.isUseMilliseconds());

		// Inserting Row
		db.insert(TABLE_T_CYCLETIME, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting all cycleTime from a project id
	public List<TCycleTimeDataBase> getAllCycleTime(int processId) {
		SQLiteDatabase db = this.getReadableDatabase();
		List<TCycleTimeDataBase> cycleTimeList = new ArrayList<TCycleTimeDataBase>();
		Cursor cursor = db.query(TABLE_T_CYCLETIME, new String[] {
				KEY_CYCLETIME_ID, KEY_PROCESS_ID, KEY_PROJECT_ID,
				KEY_PROCESS_NAME, KEY_PROJECT_NAME, KEY_CYCLE_COUNTER,
				KEY_OPERATOR_NAME, KEY_SHIFT_NO, KEY_STEP_ID,
				KEY_STEP_DESCRIPTION, KEY_LOWEST_TIME, KEY_ADJUSTMENT,
				KEY_ADJUSTED_TIME, KEY_AUDIO_NOTE, KEY_VIDEO_FILE_NAME,
				KEY_START_TIME_STAMP, KEY_END_TIME_STAMP, KEY_VERSION_ID,
				KEY_PREVIOUS_VERS_ID, KEY_PRE_VERS_DIFFERENCE_LOWEST_TIME,
				KEY_PRE_VERS_DIFFERENCE_ADJUSTED_TIME,
				KEY_PRE_VERS_DIFFERENCE_ADJUSTMENT, KEY_NO_VIDEO_ONLY_TIMING,
				KEY_USE_MILLISECONDS }, KEY_PROCESS_ID + "=?",
				new String[] { String.valueOf(processId) }, null, null, null,
				null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				TCycleTimeDataBase cycleTime = new TCycleTimeDataBase();
				cycleTime.setCycleTimeId(Integer.parseInt(cursor.getString(0)));
				cycleTime.setProcessId(Integer.parseInt(cursor.getString(1)));
				cycleTime.setProjectId(Integer.parseInt(cursor.getString(2)));
				cycleTime.setProcessName(cursor.getString(3));
				cycleTime.setProjectName(cursor.getString(4));

				cycleTime
						.setCycleCounter(Integer.parseInt(cursor.getString(5)));
				cycleTime.setOperatorName(cursor.getString(6));
				cycleTime.setShiftNo(Integer.parseInt(cursor.getString(7)));
				cycleTime.setStepId(Integer.parseInt(cursor.getString(8)));
				cycleTime.setStepDescription(cursor.getString(9));
				cycleTime.setLowestTime(cursor.getString(10));
				cycleTime.setAdjustment(cursor.getString(11));
				cycleTime.setAdjustTime(cursor.getString(12));
				cycleTime.setAudioNote(cursor.getString(13));
				cycleTime.setVideoFileName(cursor.getString(14));
				cycleTime.setStartTimeStamp(cursor.getString(15));
				cycleTime.setEndTimeStamp(cursor.getString(16));
				cycleTime.setVersionId(Integer.parseInt(cursor.getString(17)));
				cycleTime.setPreviousVersionId(Integer.parseInt(cursor
						.getString(18)));
				cycleTime.setPreVerDifferenceLowestTime(cursor.getString(19));
				cycleTime.setPreVerDifferenceAdjustedTime(cursor.getString(20));
				cycleTime.setPreVerDifferenceAdjustment(cursor.getString(21));
				cycleTime.setNoVideoOnlyTiming(Boolean.parseBoolean(cursor
						.getString(22)));
				cycleTime.setUseMilliseconds(Boolean.parseBoolean(cursor
						.getString(23)));

				// Adding contact to list
				cycleTimeList.add(cycleTime);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return cycleTimeList;
	}

	// Getting All cycle time
	public List<TCycleTimeDataBase> getAllCycleTime() {
		List<TCycleTimeDataBase> cycleTimeList = new ArrayList<TCycleTimeDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_CYCLETIME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				TCycleTimeDataBase cycleTime = new TCycleTimeDataBase();
				cycleTime.setCycleTimeId(Integer.parseInt(cursor.getString(0)));
				cycleTime.setProcessId(Integer.parseInt(cursor.getString(1)));
				cycleTime.setProjectId(Integer.parseInt(cursor.getString(2)));
				cycleTime.setProcessName(cursor.getString(3));
				cycleTime.setProjectName(cursor.getString(4));

				cycleTime
						.setCycleCounter(Integer.parseInt(cursor.getString(5)));
				cycleTime.setOperatorName(cursor.getString(6));
				cycleTime.setShiftNo(Integer.parseInt(cursor.getString(7)));
				cycleTime.setStepId(Integer.parseInt(cursor.getString(8)));
				cycleTime.setStepDescription(cursor.getString(9));
				cycleTime.setLowestTime(cursor.getString(10));
				cycleTime.setAdjustment(cursor.getString(11));
				cycleTime.setAdjustTime(cursor.getString(12));
				cycleTime.setAudioNote(cursor.getString(13));
				cycleTime.setVideoFileName(cursor.getString(14));
				cycleTime.setStartTimeStamp(cursor.getString(15));
				cycleTime.setEndTimeStamp(cursor.getString(16));
				cycleTime.setVersionId(Integer.parseInt(cursor.getString(17)));
				cycleTime.setPreviousVersionId(Integer.parseInt(cursor
						.getString(18)));
				cycleTime.setPreVerDifferenceLowestTime(cursor.getString(19));
				cycleTime.setPreVerDifferenceAdjustedTime(cursor.getString(20));
				cycleTime.setPreVerDifferenceAdjustment(cursor.getString(21));
				cycleTime.setNoVideoOnlyTiming(Boolean.parseBoolean(cursor
						.getString(22)));
				cycleTime.setUseMilliseconds(Boolean.parseBoolean(cursor
						.getString(23)));

				// Adding cylce time to list
				cycleTimeList.add(cycleTime);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return cycleTimeList;
	}

	// Updating single cycle time
	public int updateCycleTime(TCycleTimeDataBase cycleTime) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// cycle time id
		values.put(KEY_CYCLETIME_ID, cycleTime.getCycleTimeId());
		values.put(KEY_PROCESS_ID, cycleTime.getProcessId());// cycleTime id
		values.put(KEY_PROJECT_ID, cycleTime.getProjectId()); // project id
		// project name
		values.put(KEY_PROJECT_NAME, cycleTime.getProjectName());
		// cycle Time name
		values.put(KEY_PROCESS_NAME, cycleTime.getProcessName());
		// cycle counter
		values.put(KEY_CYCLE_COUNTER, cycleTime.getCycleCounter());
		// operator
		values.put(KEY_OPERATOR_NAME, cycleTime.getOperatorName());
		// shift no
		values.put(KEY_SHIFT_NO, cycleTime.getShiftNo());
		// step id
		values.put(KEY_STEP_ID, cycleTime.getStepId());
		// step description
		values.put(KEY_STEP_DESCRIPTION, cycleTime.getStepDescription());
		// lowest time
		values.put(KEY_LOWEST_TIME, cycleTime.getLowestTime());
		// adjustment
		values.put(KEY_ADJUSTMENT, cycleTime.getAdjustment());
		// adjust time
		values.put(KEY_ADJUSTED_TIME, cycleTime.getAdjustTime());
		// audio note
		values.put(KEY_AUDIO_NOTE, cycleTime.getAudioNote());
		// video file nam
		values.put(KEY_VIDEO_FILE_NAME, cycleTime.getVideoFileName());
		// start time stamp
		values.put(KEY_START_TIME_STAMP, cycleTime.getStartTimeStamp());
		// end time stamp
		values.put(KEY_END_TIME_STAMP, cycleTime.getEndTimeStamp());
		// version id
		values.put(KEY_VERSION_ID, cycleTime.getVersionId());
		// previous version id
		values.put(KEY_PREVIOUS_VERS_ID, cycleTime.getPreviousVersionId());
		// previous version diference lowest time
		values.put(KEY_PRE_VERS_DIFFERENCE_LOWEST_TIME,
				cycleTime.getPreVerDifferenceLowestTime());
		// previous version difference adjusted time
		values.put(KEY_PRE_VERS_DIFFERENCE_ADJUSTED_TIME,
				cycleTime.getPreVerDifferenceAdjustedTime());
		// next cycleTime
		values.put(KEY_PRE_VERS_DIFFERENCE_ADJUSTMENT,
				cycleTime.getPreVerDifferenceAdjustment());
		// no video only timing
		values.put(KEY_NO_VIDEO_ONLY_TIMING, cycleTime.isNoVideoOnlyTiming());
		// use milliseconds
		values.put(KEY_USE_MILLISECONDS, cycleTime.isUseMilliseconds());

		// updating row
		return db.update(TABLE_T_CYCLETIME, values, KEY_PROCESS_ID + " = ?",
				new String[] { String.valueOf(cycleTime.getCycleTimeId()) });
	}

	// Deleting single cycle time
	public void deleteCycleTime(TCycleTimeDataBase cycleTime) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_CYCLETIME, KEY_CYCLETIME_ID + " = ?",
				new String[] { String.valueOf(cycleTime.getCycleTimeId()) });
		db.close();
	}

	// Getting cycle time Count
	public int getCycleTimeCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_CYCLETIME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
