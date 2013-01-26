package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TVersionDataBaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ProjectManager";

	// Contacts table name
	private static final String TABLE_T_VERSION = "T_VERSIONS";
	// Contacts Table Columns names
	private static final String KEY_PROJECT_ID = "KEY_PROJECT_ID";
	private static final String KEY_VERSION_ID = "KEY_VERSION_ID";
	private static final String KEY_VERSION_NO = "KEY_VERSION_NO";
	private static final String KEY_VERSION_DATE_TIME = "KEY_VERSION_DATE_TIME";
	private static final String KEY_VERSION_NOTES = "KEY_VERSION_NOTES";

	// constructor
	public TVersionDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create table
	@Override
	public void onCreate(SQLiteDatabase db) {
		String V_VSM_TABLE = "CREATE TABLE " + TABLE_T_VERSION + "(" + KEY_PROJECT_ID
				+ " INTEGER PRIMARY KEY," + KEY_VERSION_ID + " INTEGER," + KEY_VERSION_NO
				+ " INTEGER," + KEY_VERSION_DATE_TIME + " TEXT," + KEY_VERSION_NOTES + " TEXT"
				+ ")";
		db.execSQL(V_VSM_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_VERSION);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */
	// Adding new Version
	public void addNewVersion(TVersionDataBase tVersion) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_PROJECT_ID, tVersion.getProjectId()); // project id
		values.put(KEY_VERSION_ID, tVersion.getVersionId()); // version id
		values.put(KEY_VERSION_NO, tVersion.getVersionNo());// version no
		// version date time
		values.put(KEY_VERSION_DATE_TIME, tVersion.getVersionDateTime());
		// non version notes
		values.put(KEY_VERSION_NOTES, tVersion.getVersionNote());

		// insert to database
		db.insert(TABLE_T_VERSION, null, values);
		db.close();
	}

	// Getting single version
	public TVersionDataBase getVersion(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_VERSION, new String[] { KEY_PROJECT_ID, KEY_VERSION_ID,
				KEY_VERSION_NO, KEY_VERSION_DATE_TIME, KEY_VERSION_NOTES }, KEY_PROJECT_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		TVersionDataBase tVersion = new TVersionDataBase(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
				cursor.getString(3), cursor.getString(4));

		db.close();
		return tVersion;
	}

	// Getting All version
	public List<TVersionDataBase> getAllVersions() {
		List<TVersionDataBase> tVersionList = new ArrayList<TVersionDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_VERSION;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TVersionDataBase tVersion = new TVersionDataBase();
				tVersion.setProjectId(Integer.parseInt(cursor.getString(0)));
				tVersion.setVersionId(Integer.parseInt(cursor.getString(1)));
				tVersion.setVersionNo(Integer.parseInt(cursor.getString(2)));
				tVersion.setVersionDateTime(cursor.getString(3));
				tVersion.setVersionNote(cursor.getString(4));
				// Adding contact to list
				tVersionList.add(tVersion);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return tVersionList;
	}

	// Updating single version
	public int updateVersion(TVersionDataBase tVersion) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_ID, tVersion.getProjectId());
		values.put(KEY_VERSION_ID, tVersion.getVersionId());
		values.put(KEY_VERSION_NO, tVersion.getVersionNo());
		values.put(KEY_VERSION_DATE_TIME, tVersion.getVersionDateTime());
		values.put(KEY_VERSION_NOTES, tVersion.getVersionNote());
		// updating row
		return db.update(TABLE_T_VERSION, values, KEY_PROJECT_ID + " = ?",
				new String[] { String.valueOf(tVersion.getProjectId()) });
	}

	// Deleting single version table
	public void deleteVSMTable(V_VSMDataBase vsm) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_VERSION, KEY_PROJECT_ID + " = ?",
				new String[] { String.valueOf(vsm.getProjectID()) });
		db.close();
	}

	// Getting version count
	public int getVSMsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_VERSION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		db.close();
		// return count
		return cursor.getCount();
	}
}
