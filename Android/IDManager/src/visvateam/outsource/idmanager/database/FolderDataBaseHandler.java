package visvateam.outsource.idmanager.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FolderDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "FolderManager";

	// folders table name
	private static final String TABLE_T_FOLDERS = "TABLE_T_FOLDERS";

	// folders Table Columns names
	private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
	private static final String KEY_USER_ID = "KEY_USER_ID";
	private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
	private static final String KEY_IMG_FOLDER_ID = "KEY_IMG_FOLDER_ID";
	private static final String KEY_IMG_FOLDER_ICON_ID = "KEY_IMG_FOLDER_ICON_ID";
	private static final String KEY_IS_NORMAL_FOLDER = "KEY_IS_NORMAL_FOLDER";

	public FolderDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// create database
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_folderS_TABLE = "CREATE TABLE " + TABLE_T_FOLDERS + "(" + KEY_FOLDER_ID
				+ " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER," + KEY_FOLDER_NAME + " TEXT,"
				+ KEY_IMG_FOLDER_ID + " INTEGER," + KEY_IMG_FOLDER_ICON_ID + " INTEGER,"
				+ KEY_IS_NORMAL_FOLDER + " INTEGER" + ")";
		db.execSQL(CREATE_folderS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_FOLDERS);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new project
	public void addNewFolder(FolderDatabase folder) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FOLDER_ID, folder.getFolderId()); // folder id
		values.put(KEY_USER_ID, folder.getUserId()); // user id
		values.put(KEY_FOLDER_NAME, folder.getFolderName()); // folder name
		values.put(KEY_IMG_FOLDER_ID, folder.getImgFolderId()); // folder name
		// image folder icon id
		values.put(KEY_IMG_FOLDER_ICON_ID, folder.getImgFolderIconId());
		// image folder icon edit
		values.put(KEY_IS_NORMAL_FOLDER, folder.getTypeOfFolder());
		Log.e("adkfjalsdfj", "asdfjahsdlfjd " + folder.getTypeOfFolder());
		// Inserting Row
		db.insert(TABLE_T_FOLDERS, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single project
	public FolderDatabase getFolder(int folderId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_FOLDERS, new String[] { KEY_FOLDER_ID, KEY_USER_ID,
				KEY_FOLDER_NAME, KEY_IMG_FOLDER_ID, KEY_IMG_FOLDER_ICON_ID, KEY_IS_NORMAL_FOLDER },
				KEY_FOLDER_ID + "=?", new String[] { String.valueOf(folderId) }, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		FolderDatabase folder = new FolderDatabase(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), cursor.getString(2), Integer.parseInt(cursor
						.getString(3)), Integer.parseInt(cursor.getString(4)),
				Integer.parseInt(cursor.getString(5)));
		db.close();
		// return folder
		return folder;
	}

	// Getting All project
	public List<FolderDatabase> getAllFolders() {
		List<FolderDatabase> foldertList = new ArrayList<FolderDatabase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_FOLDERS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				FolderDatabase folder = new FolderDatabase();
				folder.setFolderId(Integer.parseInt(cursor.getString(0)));
				folder.setUserId(Integer.parseInt(cursor.getString(1)));
				folder.setFolderName(cursor.getString(2));
				folder.setImgFolderId(Integer.parseInt(cursor.getString(3)));
				folder.setImgFolderIconId(Integer.parseInt(cursor.getString(4)));
				folder.setTypeOfFolder(Integer.parseInt(cursor.getString(5)));

				Log.e("adsifhdkf", "adjfhdkh " + Integer.parseInt(cursor.getString(5)));
				// Adding folder to list
				foldertList.add(folder);
			} while (cursor.moveToNext());
		}
		db.close();
		// return folder list
		return foldertList;
	}

	// Updating single folder
	public int updateFolder(FolderDatabase folder) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FOLDER_ID, folder.getFolderId());
		values.put(KEY_USER_ID, folder.getUserId());
		values.put(KEY_FOLDER_NAME, folder.getFolderName());
		values.put(KEY_IMG_FOLDER_ID, folder.getImgFolderId());
		values.put(KEY_IMG_FOLDER_ICON_ID, folder.getImgFolderIconId());
		values.put(KEY_IS_NORMAL_FOLDER, folder.getTypeOfFolder());
		// updating row
		return db.update(TABLE_T_FOLDERS, values, KEY_FOLDER_ID + " = ?",
				new String[] { String.valueOf(folder.getFolderId()) });
	}

	// Deleting single folder
	public void deleteFolder(int folderId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_FOLDERS, KEY_FOLDER_ID + " = ?",
				new String[] { String.valueOf(folderId) });
		db.close();
	}

	// Getting folders count
	public int getProjectsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_FOLDERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}
}
