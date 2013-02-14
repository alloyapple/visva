package visvateam.outsource.idmanager.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "UserManager";

	// Contacts table name
	private static final String TABLE_T_USERS = "TABLE_T_USERS";

	// Contacts Table Columns names
	private static final String KEY_USER_ID = "KEY_USER_ID";
	private static final String KEY_USER_PASSWORD = "KEY_USER_PASSWORD";
	private static final String KEY_LAST_TIME_SIGN_IN = "KEY_LAST_TIME_SIGN_IN";

	/* CREATE DATABASE */
	public UserDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_USERS + "(" + KEY_USER_ID
				+ " INTEGER PRIMARY KEY," + KEY_USER_PASSWORD + " TEXT," + KEY_LAST_TIME_SIGN_IN
				+ " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_USERS);
		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new user
	public void addNewUser(UserDataBase user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user.getUserId()); // user id
		values.put(KEY_USER_PASSWORD, user.getUserPassword()); // user pw\
		// last time sign in
		values.put(KEY_LAST_TIME_SIGN_IN, user.getLastTimeSignIn());

		// Inserting Row
		db.insert(TABLE_T_USERS, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single user
	public UserDataBase getFolder(int userId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_USERS, new String[] { KEY_USER_ID, KEY_USER_PASSWORD,
				KEY_LAST_TIME_SIGN_IN }, KEY_USER_ID + "=?",
				new String[] { String.valueOf(userId) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		UserDataBase user = new UserDataBase(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		db.close();
		// return folder
		return user;
	}

	// Getting All users
	public List<UserDataBase> getAllUsers() {
		List<UserDataBase> userList = new ArrayList<UserDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_USERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				UserDataBase user = new UserDataBase();
				user.setUserId(Integer.parseInt(cursor.getString(0)));
				user.setUserPassword(cursor.getString(1));
				user.setLastTimeSignIn(cursor.getString(2));

				// Adding user to list
				userList.add(user);
			} while (cursor.moveToNext());
		}
		db.close();
		// return user list
		return userList;
	}

	// Updating single user
	public int updateUser(UserDataBase user) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user.getUserId());
		values.put(KEY_USER_PASSWORD, user.getUserPassword());
		values.put(KEY_LAST_TIME_SIGN_IN, user.getLastTimeSignIn());

		// updating row
		return db.update(TABLE_T_USERS, values, KEY_USER_ID + " = ?",
				new String[] { String.valueOf(user.getUserId()) });
	}

	// Deleting single user
	public void deleteProject(UserDataBase user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_USERS, KEY_USER_ID + " = ?",
				new String[] { String.valueOf(user.getUserId()) });
		db.close();
	}

	// Getting users Count
	public int getProjectsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_USERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}
}
