package vsvteam.outsource.leanappandroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TProjectDatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ProjectManager";

	// Contacts table name
	private static final String TABLE_T_PROJECTS = "T_PROJECTS";

	// Contacts Table Columns names
	private static final String KEY_PROJECT_ID = "id";
	private static final String KEY_PROJECT_NAME = "KEY_PROJECT_NAME";
	private static final String KEY_COMPANY_NAME = "KEY_COMPANY_NAME";
	private static final String KEY_PROJECT_DESCRIPTION = "KEY_PROJECT_DESCRIPTION";
	private static final String KEY_COMPANY_ADDRESS = "KEY_COMPANY_ADDRESS";
	private static final String KEY_NOTES = "KEY_NOTES";

	public TProjectDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.e("he he", "ha ha");
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("create db process", "create db project");
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_PROJECTS + "(" + KEY_PROJECT_ID
				+ " INTEGER PRIMARY KEY," + KEY_PROJECT_NAME + " TEXT," + KEY_COMPANY_NAME
				+ " TEXT," + KEY_PROJECT_DESCRIPTION + " TEXT," + KEY_COMPANY_ADDRESS + " TEXT,"
				+ KEY_NOTES + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_PROJECTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new project
	public void addNewProject(TProjectDataBase contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_ID, contact.getProjectID()); // project id
		values.put(KEY_PROJECT_NAME, contact.getProjectName()); // project name
		values.put(KEY_COMPANY_NAME, contact.getCompanyName()); // company name
		values.put(KEY_PROJECT_DESCRIPTION, contact.getProjectDescription());// project
																				// description
		values.put(KEY_COMPANY_ADDRESS, contact.getCompanyAddress());// company
																		// address
		values.put(KEY_NOTES, contact.getNotes());// notes
		// Inserting Row
		db.insert(TABLE_T_PROJECTS, null, values);
		//close db after use
		db.close(); // Closing database connection
	}

	// Getting single project
	public TProjectDataBase getProject(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_PROJECTS, new String[] { KEY_PROJECT_ID, KEY_PROJECT_NAME,
				KEY_COMPANY_NAME, KEY_PROJECT_DESCRIPTION, KEY_COMPANY_ADDRESS, KEY_NOTES }, KEY_PROJECT_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		TProjectDataBase contact = new TProjectDataBase(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5));
		
		db.close();
		// return contact
		return contact;
	}

	// Getting All project
	public List<TProjectDataBase> getAllProjects() {
		List<TProjectDataBase> contactList = new ArrayList<TProjectDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_PROJECTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TProjectDataBase contact = new TProjectDataBase();
				contact.setProjectID(Integer.parseInt(cursor.getString(0)));
				contact.setProjectName(cursor.getString(1));
				contact.setCompanyName(cursor.getString(2));
				contact.setProjectDescription(cursor.getString(3));
				contact.setCompanyAddress(cursor.getString(4));
				contact.setNotes(cursor.getString(5));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return contactList;
	}

	// Updating single project
	public int updateProject(TProjectDataBase contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_NAME, contact.getProjectName());
		values.put(KEY_COMPANY_NAME, contact.getCompanyName());
		values.put(KEY_PROJECT_DESCRIPTION, contact.getProjectDescription());
		values.put(KEY_COMPANY_ADDRESS, contact.getCompanyAddress());
		values.put(KEY_NOTES, contact.getNotes());
		// updating row
		return db.update(TABLE_T_PROJECTS, values, KEY_PROJECT_ID + " = ?",
				new String[] { String.valueOf(contact.getProjectID()) });
	}

	// Deleting single project
	public void deleteProject(TProjectDataBase contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_PROJECTS, KEY_PROJECT_ID + " = ?",
				new String[] { String.valueOf(contact.getProjectID()) });
		db.close();
	}

	// Getting projects Count
	public int getProjectsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_PROJECTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
