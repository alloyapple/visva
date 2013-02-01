package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VImprovementDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ImprovementtManager";

	// Contacts table name
	private static final String TABLE_T_IPROVEMENT = "TABLE_T_IPROVEMENT";

	// Contacts Table Columns names
	private static final String KEY_IMPROVEMENT_ID = "";
	private static final String KEY_STEP_ID = "";
	private static final String KEY_PROCESS_ID = "";
	private static final String KEY_PROJECT_ID = "id";
	private static final String KEY_STEP_NAME = "";
	private static final String KEY_PROCESS_NAME = "";
	private static final String KEY_PROJECT_NAME = "KEY_PROJECT_NAME";

	private static final String KEY_VERSION_ID = "";
	private static final String KEY_PREVIOUS_VERS_ID = "";
	private static final String KEY_SUM_OF_PREV_VERS_DIFFERENCE_LOWEST_TIME = "";
	private static final String KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUSTED_TIME = "";
	private static final String KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUTSTMEN = "";
	private static final String KEY_SUM_OF_LOWEST_TIME = "";
	private static final String KEY_SUM_OF_ADJUSTMENT = "";
	private static final String KEY_SUM_OF_ADJUSTED_TIME = "";
	private static final String KEY_THROUGH_TIME = "";
	private static final String KEY_PREV_VERS_DIFFERENCE_THROUGHPUT = "";
	private static final String KEY_VOL_X_TIME = "";
	private static final String KEY_SAVED_TRAVEL_DISTANCE = "KEY_SAVED_TRAVEL_DISTANCE";
	private static final String KEY_SAVED_TRAVEL_TIME = "KEY_SAVED_TRAVEL_TIME";
	private static final String KEY_UNIT_ID = "UNIT_ID";

	// constructor
	public VImprovementDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create db improvement", "create db improvement");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_IPROVEMENT
				+ "(" + KEY_IMPROVEMENT_ID + " INTEGER PRIMARY KEY,"
				+ KEY_STEP_ID + " INTEGER," + KEY_PROCESS_ID + " INTEGER,"
				+ KEY_PROJECT_ID + " INTEGER," + KEY_STEP_NAME + " INTEGER,"
				+ KEY_PROCESS_NAME + " TEXT" + KEY_PROJECT_NAME + " TEXT,"
				+ KEY_VERSION_ID + " TEXT," + KEY_PREVIOUS_VERS_ID
				+ " INTEGER," + KEY_SUM_OF_PREV_VERS_DIFFERENCE_LOWEST_TIME
				+ " TEXT," + KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUSTED_TIME
				+ " TEXT," + KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUTSTMEN
				+ " TEXT," + KEY_SUM_OF_LOWEST_TIME + " TEXT,"
				+ KEY_SUM_OF_ADJUSTMENT + " TEXT," + KEY_SUM_OF_ADJUSTED_TIME
				+ " TEXT," + KEY_THROUGH_TIME + " INTEGER,"
				+ KEY_PREV_VERS_DIFFERENCE_THROUGHPUT + " INTEGER,"
				+ KEY_VOL_X_TIME + " INTEGER," + KEY_SAVED_TRAVEL_DISTANCE
				+ " INTEGER," + KEY_SAVED_TRAVEL_TIME + " TEXT," + KEY_UNIT_ID
				+ " INTEGER" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	// upgrade database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_IPROVEMENT);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new improvement
	public void addNewImprovement(VImprovementDataBase improvement) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IMPROVEMENT_ID, improvement.getvImprovementId());
		// step id
		values.put(KEY_STEP_ID, improvement.getStepId());
		// process id
		values.put(KEY_PROCESS_ID, improvement.getProcessId());
		// project id
		values.put(KEY_PROJECT_ID, improvement.getProjectId());
		// step name
		values.put(KEY_STEP_NAME, improvement.getStepName());
		// process name
		values.put(KEY_PROCESS_NAME, improvement.getProcessName());
		// project name
		values.put(KEY_PROJECT_NAME, improvement.getProjectName());
		// version id
		values.put(KEY_VERSION_ID, improvement.getVersionId());
		// previous version id
		values.put(KEY_PREVIOUS_VERS_ID, improvement.getPreVerionId());
		// sum of previous version difference lowest time
		values.put(KEY_SUM_OF_PREV_VERS_DIFFERENCE_LOWEST_TIME,
				improvement.getSumOfPreVersDifferenceLowestTime());
		// sum of previous version difference adjusted time
		values.put(KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUSTED_TIME,
				improvement.getSumOfPreVersDifferenceAdjustedTime());
		// sum of previous version difference adjustment
		values.put(KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUTSTMEN,
				improvement.getSumOfPreVersDifferenceAdjustment());

		// sum of lowest time
		values.put(KEY_SUM_OF_LOWEST_TIME, improvement.getSumOfLowesTime());
		// sum of adjustment
		values.put(KEY_SUM_OF_ADJUSTMENT, improvement.getSumOfAdjustment());
		// sum of adjusted time
		values.put(KEY_SUM_OF_ADJUSTED_TIME, improvement.getSumOfAdjustedTime());
		// through put
		values.put(KEY_THROUGH_TIME, improvement.getThroughPut());
		// previous version difference through put
		values.put(KEY_PREV_VERS_DIFFERENCE_THROUGHPUT,
				improvement.getPreVersDifferenceThroughPut());
		// volume x time( adjustment * demand)
		values.put(KEY_VOL_X_TIME, improvement.getVolXTime());
		// saved travel distance
		values.put(KEY_SAVED_TRAVEL_DISTANCE,
				improvement.getSavedTravelDistance());
		// saved travel time
		values.put(KEY_SAVED_TRAVEL_TIME, improvement.getSavedTravelTime());
		// unit id
		values.put(KEY_UNIT_ID, improvement.gettUnitid());

		// Inserting Row
		db.insert(TABLE_T_IPROVEMENT, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single VImprovement
	public VImprovementDataBase getImprovement(int stepId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db
				.query(TABLE_T_IPROVEMENT, new String[] { KEY_IMPROVEMENT_ID,
						KEY_STEP_ID, KEY_PROCESS_ID, KEY_PROJECT_ID,
						KEY_STEP_NAME, KEY_PROCESS_NAME, KEY_PROJECT_NAME,
						KEY_VERSION_ID, KEY_PREVIOUS_VERS_ID,
						KEY_SUM_OF_PREV_VERS_DIFFERENCE_LOWEST_TIME,
						KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUSTED_TIME,
						KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUTSTMEN,
						KEY_SUM_OF_LOWEST_TIME, KEY_SUM_OF_ADJUSTMENT,
						KEY_SUM_OF_ADJUSTED_TIME, KEY_THROUGH_TIME,
						KEY_PREV_VERS_DIFFERENCE_THROUGHPUT, KEY_VOL_X_TIME,
						KEY_SAVED_TRAVEL_DISTANCE, KEY_SAVED_TRAVEL_TIME,
						KEY_UNIT_ID }, KEY_PROJECT_ID + "=?",
						new String[] { String.valueOf(stepId) }, null, null,
						null, null);
		if (cursor != null)
			cursor.moveToFirst();

		VImprovementDataBase improvement = new VImprovementDataBase(
				Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor
						.getString(1)), Integer.parseInt(cursor.getString(2)),
				Integer.parseInt(cursor.getString(3)), cursor.getString(4),
				cursor.getString(5), cursor.getString(6),
				Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor
						.getString(8)), cursor.getString(9),
				cursor.getString(10), cursor.getString(11),
				cursor.getString(12), cursor.getString(13),
				cursor.getString(14), Integer.parseInt(cursor.getString(15)),
				Integer.parseInt(cursor.getString(16)), Integer.parseInt(cursor
						.getString(17)),
				Integer.parseInt(cursor.getString(18)), cursor.getString(19),
				Integer.parseInt(cursor.getString(20)));

		db.close();
		// return improvement
		return improvement;
	}

	// Getting All improvement
	public List<VImprovementDataBase> getAllImprovements() {
		List<VImprovementDataBase> improvementList = new ArrayList<VImprovementDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_IPROVEMENT;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				VImprovementDataBase improvement = new VImprovementDataBase();
				improvement.setvImprovementId(Integer.parseInt(cursor
						.getString(0)));
				improvement.setStepId(Integer.parseInt(cursor.getString(1)));
				improvement.setProcessId(Integer.parseInt(cursor.getString(2)));
				improvement.setProjectId(Integer.parseInt(cursor.getString(3)));
				improvement.setStepName(cursor.getString(4));
				improvement.setProcessName(cursor.getString(5));
				improvement.setProjectName(cursor.getString(6));
				improvement.setVersionId(Integer.parseInt(cursor.getString(7)));
				improvement
						.setPreVerionId(Integer.parseInt(cursor.getString(8)));
				improvement.setSumOfPreVersDifferenceLowestTime(cursor
						.getString(9));
				improvement.setSumOfPreVersDifferenceAdjustedTime(cursor
						.getString(10));
				improvement.setSumOfPreVersDifferenceAdjustment(cursor
						.getString(11));
				improvement.setSumOfLowesTime(cursor.getString(12));
				improvement.setSumOfAdjustment(cursor.getString(13));
				improvement.setSumOfAdjustedTime(cursor.getString(14));
				improvement
						.setThroughPut(Integer.parseInt(cursor.getString(15)));
				improvement.setPreVersDifferenceThroughPut(Integer
						.parseInt(cursor.getString(16)));
				improvement.setVolXTime(Integer.parseInt(cursor.getString(17)));
				improvement.setSavedTravelDistance(Integer.parseInt(cursor
						.getString(18)));
				improvement.setSavedTravelTime(cursor.getString(19));
				improvement.settUnitid(Integer.parseInt(cursor.getString(20)));
				// Adding improvement to list
				improvementList.add(improvement);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return improvementList;
	}

	// Updating single vimprovement
	public int updateImprovement(VImprovementDataBase improvement) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// step id
		values.put(KEY_STEP_ID, improvement.getStepId());
		// process id
		values.put(KEY_PROCESS_ID, improvement.getProcessId());
		// project id
		values.put(KEY_PROJECT_ID, improvement.getProjectId());
		// step name
		values.put(KEY_STEP_NAME, improvement.getStepName());
		// process name
		values.put(KEY_PROCESS_NAME, improvement.getProcessName());
		// project name
		values.put(KEY_PROJECT_NAME, improvement.getProjectName());
		// version id
		values.put(KEY_VERSION_ID, improvement.getVersionId());
		// previous version id
		values.put(KEY_PREVIOUS_VERS_ID, improvement.getPreVerionId());
		// sum of previous version difference lowest time
		values.put(KEY_SUM_OF_PREV_VERS_DIFFERENCE_LOWEST_TIME,
				improvement.getSumOfPreVersDifferenceLowestTime());
		// sum of previous version difference adjusted time
		values.put(KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUSTED_TIME,
				improvement.getSumOfPreVersDifferenceAdjustedTime());
		// sum of previous version difference adjustment
		values.put(KEY_SUM_OF_PREV_VERS_DIFFERENCE_ADJUTSTMEN,
				improvement.getSumOfPreVersDifferenceAdjustment());

		// sum of lowest time
		values.put(KEY_SUM_OF_LOWEST_TIME, improvement.getSumOfLowesTime());
		// sum of adjustment
		values.put(KEY_SUM_OF_ADJUSTMENT, improvement.getSumOfAdjustment());
		// sum of adjusted time
		values.put(KEY_SUM_OF_ADJUSTED_TIME, improvement.getSumOfAdjustedTime());
		// through put
		values.put(KEY_THROUGH_TIME, improvement.getThroughPut());
		// previous version difference through put
		values.put(KEY_PREV_VERS_DIFFERENCE_THROUGHPUT,
				improvement.getPreVersDifferenceThroughPut());
		// volume x time( adjustment * demand)
		values.put(KEY_VOL_X_TIME, improvement.getVolXTime());
		// saved travel distance
		values.put(KEY_SAVED_TRAVEL_DISTANCE,
				improvement.getSavedTravelDistance());
		// saved travel time
		values.put(KEY_SAVED_TRAVEL_TIME, improvement.getSavedTravelTime());
		// unit id
		values.put(KEY_UNIT_ID, improvement.gettUnitid());

		// updating row
		return db
				.update(TABLE_T_IPROVEMENT, values,
						KEY_IMPROVEMENT_ID + " = ?", new String[] { String
								.valueOf(improvement.getvImprovementId()) });
	}

	// Deleting single improvement
	public void deleteImprovement(VImprovementDataBase improvement) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(
				TABLE_T_IPROVEMENT,
				KEY_IMPROVEMENT_ID + " = ?",
				new String[] { String.valueOf(improvement.getvImprovementId()) });
		db.close();
	}

	// Getting improvements Count
	public int getProjectsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_IPROVEMENT;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}
}
