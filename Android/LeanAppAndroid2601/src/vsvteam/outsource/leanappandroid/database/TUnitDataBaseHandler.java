package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TUnitDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "TUnitManager";

	// Contacts table name
	private static final String TABLE_T_UNIT = "TABLE_T_UNIT";
	// Contacts Table Columns names
	private static final String KEY_UNIT_ID = "KEY_UNIT_ID";
	private static final String KEY_OPERATOR_SPEED = "KEY_OPERATOR_SPEED";

	public TUnitDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create db process", "create db project");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_UNIT + "("
				+ KEY_UNIT_ID + " INTEGER PRIMARY KEY," + KEY_OPERATOR_SPEED
				+ " INTEGER" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_UNIT);

		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new project
	public void addNewProject(TUnitDataBase unit) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_UNIT_ID, unit.gettUnitId()); // unit id
		values.put(KEY_OPERATOR_SPEED, unit.getOperatorSpeed());

		// Inserting Row
		db.insert(TABLE_T_UNIT, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single project
	public TUnitDataBase getUnit(int unitId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db
				.query(TABLE_T_UNIT, new String[] { KEY_UNIT_ID,
						KEY_OPERATOR_SPEED }, KEY_UNIT_ID + "=?",
						new String[] { String.valueOf(unitId) }, null, null,
						null, null);
		if (cursor != null)
			cursor.moveToFirst();

		TUnitDataBase unit = new TUnitDataBase(Integer.parseInt(cursor
				.getString(0)), Integer.parseInt(cursor.getString(1)));

		db.close();
		// return contact
		return unit;
	}

	// Getting All unit
	public List<TUnitDataBase> getAllUnits() {
		List<TUnitDataBase> unitList = new ArrayList<TUnitDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_UNIT;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TUnitDataBase unit = new TUnitDataBase();
				unit.settUnitId(Integer.parseInt(cursor.getString(0)));
				unit.setOperatorSpeed(Integer.parseInt(cursor.getString(1)));
				// Adding unit to list
				unitList.add(unit);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return unitList;
	}

	// Updating single unit
	public int updateProject(TUnitDataBase unit) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_OPERATOR_SPEED, unit.getOperatorSpeed());
		// updating row
		return db.update(TABLE_T_UNIT, values, KEY_UNIT_ID + " = ?",
				new String[] { String.valueOf(unit.gettUnitId()) });
	}

	// Deleting single unit
	public void deleteProject(TUnitDataBase unit) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_UNIT, KEY_UNIT_ID + " = ?",
				new String[] { String.valueOf(unit.gettUnitId()) });
		db.close();
	}

	// Getting units Count
	public int getUnitsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_UNIT;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}

}
