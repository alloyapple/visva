package visvateam.outsource.idmanager.database;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IDDataBaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "IDManager";

	// Contacts table name
	private static final String TABLE_T_IDS = "TABLE_T_IDS";

	// Contacts Table Columns names
	private static final String KEY_PASS_WORD_ID = "KEY_PASS_WORD_ID";
	private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
	private static final String KEY_TITLE_RECORD = "KEY_TITLE_RECORD";
	private static final String KEY_ICON = "KEY_ICON";
	private static final String KEY_FAVOURITE_GROUP = "KEY_FAVOURITE_GROUP";
	private static final String KEY_TITLE_ID_1 = "KEY_TITLE_ID_1";
	private static final String KEY_DATA_ID_1 = "KEY_DATA_ID_1";
	private static final String KEY_TITLE_ID_2 = "KEY_TITLE_ID_2";
	private static final String KEY_DATA_ID_2 = "KEY_DATA_ID_2";
	private static final String KEY_TITLE_ID_3 = "KEY_TITLE_ID_3";
	private static final String KEY_DATA_ID_3 = "KEY_DATA_ID_3";
	private static final String KEY_TITLE_ID_4 = "KEY_TITLE_ID_4";
	private static final String KEY_DATA_ID_4 = "KEY_DATA_ID_4";
	private static final String KEY_TITLE_ID_5 = "KEY_TITLE_ID_5";
	private static final String KEY_DATA_ID_5 = "KEY_DATA_ID_5";
	private static final String KEY_TITLE_ID_6 = "KEY_TITLE_ID_6";
	private static final String KEY_DATA_ID_6 = "KEY_DATA_ID_6";
	private static final String KEY_TITLE_ID_7 = "KEY_TITLE_ID_7";
	private static final String KEY_DATA_ID_7 = "KEY_DATA_ID_7";
	private static final String KEY_TITLE_ID_8 = "KEY_TITLE_ID_8";
	private static final String KEY_DATA_ID_8 = "KEY_DATA_ID_8";
	private static final String KEY_TITLE_ID_9 = "KEY_TITLE_ID_9";
	private static final String KEY_DATA_ID_9 = "KEY_DATA_ID_9";
	private static final String KEY_TITLE_ID_10 = "KEY_TITLE_ID_10";
	private static final String KEY_DATA_ID_10 = "KEY_DATA_ID_10";
	private static final String KEY_TITLE_ID_11 = "KEY_TITLE_ID_11";
	private static final String KEY_DATA_ID_11 = "KEY_DATA_ID_11";
	private static final String KEY_TITLE_ID_12 = "KEY_TITLE_ID_12";
	private static final String KEY_DATA_ID_12 = "KEY_DATA_ID_12";
	private static final String KEY_URL = "KEY_URL";
	private static final String KEY_NOTE = "KEY_NOTE";
	private static final String KEY_IMAGE_MEMO = "KEY_IMAGE_MEMO";
	private static final String KEY_FLAG = "KEY_FLAG";
	private static final String KEY_TIMES_STAMP = "KEY_TIMES_STAMP";
	private static final String KEY_IS_ENCRYPTED = "KEY_IS_ENCRYPTED";
	private static final String KEY_USER_ID = "KEY_USER_ID";

	public IDDataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	/**
	 * create database
	 * 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_T_IDS + "(" + KEY_PASS_WORD_ID
				+ " INTEGER PRIMARY KEY," + KEY_FOLDER_ID + " INTEGER," + KEY_TITLE_RECORD
				+ " TEXT," + KEY_ICON + " INTEGER," + KEY_FAVOURITE_GROUP + " TEXT,"
				+ KEY_TITLE_ID_1 + " TEXT," + KEY_DATA_ID_1 + " TEXT," + KEY_TITLE_ID_2 + " TEXT,"
				+ KEY_DATA_ID_2 + " TEXT," + KEY_TITLE_ID_3 + " TEXT," + KEY_DATA_ID_3 + " TEXT,"
				+ KEY_TITLE_ID_4 + " TEXT," + KEY_DATA_ID_4 + " TEXT," + KEY_TITLE_ID_5 + " TEXT,"
				+ KEY_DATA_ID_5 + " TEXT," + KEY_TITLE_ID_6 + " TEXT," + KEY_DATA_ID_6 + " TEXT,"
				+ KEY_TITLE_ID_7 + " TEXT," + KEY_DATA_ID_7 + " TEXT," + KEY_TITLE_ID_8 + " TEXT,"
				+ KEY_DATA_ID_8 + " TEXT," + KEY_TITLE_ID_9 + " TEXT," + KEY_DATA_ID_9 + " TEXT,"
				+ KEY_TITLE_ID_10 + " TEXT," + KEY_DATA_ID_10 + " TEXT," + KEY_TITLE_ID_11
				+ " TEXT," + KEY_DATA_ID_11 + " TEXT," + KEY_TITLE_ID_12 + " TEXT,"
				+ KEY_DATA_ID_12 + " TEXT," + KEY_URL + " TEXT," + KEY_NOTE + " TEXT,"
				+ KEY_IMAGE_MEMO + " TEXT," + KEY_FLAG + " TEXT," + KEY_TIMES_STAMP + " Date,"
				+ KEY_IS_ENCRYPTED + " Boolean," + KEY_USER_ID + " INTEGER" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_IDS);
		// Create tables again
		onCreate(db);

	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new id
	public void addNewID(IDDataBase id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PASS_WORD_ID, id.getPassWordId()); // password id
		values.put(KEY_FOLDER_ID, id.getFolderId()); // folder id
		// title record
		values.put(KEY_TITLE_RECORD, id.getTitleRecord());
		// icon
		values.put(KEY_ICON, id.getIcon());
		// favourite group
		values.put(KEY_FAVOURITE_GROUP, id.getFavouriteGroup());

		// id 1
		values.put(KEY_TITLE_ID_1, id.getTitleId1());
		values.put(KEY_DATA_ID_1, id.getDataId1());
		// id 2
		values.put(KEY_TITLE_ID_2, id.getTitleId2());
		values.put(KEY_DATA_ID_2, id.getDataId2());
		// id 3
		values.put(KEY_TITLE_ID_3, id.getTitleId3());
		values.put(KEY_DATA_ID_3, id.getDataId3());
		// id 4
		values.put(KEY_TITLE_ID_4, id.getTitleId4());
		values.put(KEY_DATA_ID_4, id.getDataId4());
		// id 5
		values.put(KEY_TITLE_ID_5, id.getTitleId5());
		values.put(KEY_DATA_ID_5, id.getDataId5());
		// id 6
		values.put(KEY_TITLE_ID_6, id.getTitleId6());
		values.put(KEY_DATA_ID_6, id.getDataId6());
		// id 7
		values.put(KEY_TITLE_ID_7, id.getTitleId7());
		values.put(KEY_DATA_ID_7, id.getDataId7());
		// id 8
		values.put(KEY_TITLE_ID_8, id.getTitleId8());
		values.put(KEY_DATA_ID_8, id.getDataId8());
		// id 9
		values.put(KEY_TITLE_ID_9, id.getTitleId9());
		values.put(KEY_DATA_ID_9, id.getDataId9());
		// id 10
		values.put(KEY_TITLE_ID_10, id.getTitleId10());
		values.put(KEY_DATA_ID_10, id.getDataId10());
		// id 11
		values.put(KEY_TITLE_ID_11, id.getTitleId11());
		values.put(KEY_DATA_ID_11, id.getDataId11());
		// id 12
		values.put(KEY_TITLE_ID_12, id.getTitleId12());
		values.put(KEY_DATA_ID_12, id.getDataId12());
		// url
		values.put(KEY_URL, id.getUrl());
		// note
		values.put(KEY_NOTE, id.getNote());
		// image memo
		values.put(KEY_IMAGE_MEMO, id.getImageMemo());
		// flag
		values.put(KEY_FLAG, id.getFlag());
		// timestamp
		values.put(KEY_TIMES_STAMP, id.getTimeStamp());
		// is encrypted
		values.put(KEY_IS_ENCRYPTED, id.isEncrypted());
		// user id
		values.put(KEY_USER_ID, id.getUserId());

		// Inserting Row
		db.insert(TABLE_T_IDS, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single id
	public IDDataBase getId(int folderId) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_IDS, new String[] { KEY_PASS_WORD_ID, KEY_FOLDER_ID,
				KEY_TITLE_RECORD, KEY_ICON, KEY_FAVOURITE_GROUP, KEY_TITLE_ID_1, KEY_DATA_ID_1,
				KEY_TITLE_ID_2, KEY_DATA_ID_2, KEY_TITLE_ID_3, KEY_DATA_ID_3, KEY_TITLE_ID_4,
				KEY_DATA_ID_4, KEY_TITLE_ID_5, KEY_DATA_ID_5, KEY_TITLE_ID_6, KEY_DATA_ID_6,
				KEY_TITLE_ID_7, KEY_DATA_ID_7, KEY_TITLE_ID_8, KEY_DATA_ID_8, KEY_TITLE_ID_9,
				KEY_DATA_ID_9, KEY_TITLE_ID_10, KEY_DATA_ID_10, KEY_TITLE_ID_11, KEY_DATA_ID_11,
				KEY_TITLE_ID_12, KEY_DATA_ID_12, KEY_URL, KEY_NOTE, KEY_IMAGE_MEMO, KEY_FLAG,
				KEY_TIMES_STAMP, KEY_IS_ENCRYPTED, KEY_USER_ID }, KEY_PASS_WORD_ID + "=?",
				new String[] { String.valueOf(folderId) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		// IDDataBase id = new
		// FolderDatabase(Integer.parseInt(cursor.getString(0)),
		// cursor.getString(1), Integer.parseInt(cursor.getString(2)),
		// Integer.parseInt(cursor
		// .getString(3)), Integer.parseInt(cursor.getString(4)));
		IDDataBase id = new IDDataBase(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getString(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11), cursor.getString(12), cursor.getString(13),
				cursor.getString(14), cursor.getString(15), cursor.getString(16),
				cursor.getString(17), cursor.getString(18), cursor.getString(19),
				cursor.getString(20), cursor.getString(21), cursor.getString(22),
				cursor.getString(23), cursor.getString(24), cursor.getString(25),
				cursor.getString(26), cursor.getString(27), cursor.getString(28),
				cursor.getString(29), cursor.getString(30), cursor.getString(31),
				cursor.getString(32), cursor.getString(33), Boolean.parseBoolean(cursor
						.getString(34)), Integer.parseInt(cursor.getString(35)));
		db.close();
		// return folder
		return id;
	}

	// Getting All ids
	public List<IDDataBase> getAllIDs() {
		List<IDDataBase> idsList = new ArrayList<IDDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_IDS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				IDDataBase id = new IDDataBase();
				id.setPassWordId(Integer.parseInt(cursor.getString(0)));
				id.setFolderId(Integer.parseInt(cursor.getString(1)));
				id.setTitleRecord(cursor.getString(2));
				id.setIcon(cursor.getString(3));
				id.setFavouriteGroup(cursor.getString(4));
				// id 1
				id.setTitleId1(cursor.getString(5));
				id.setDataId1(cursor.getString(6));
				// id 2
				id.setTitleId2(cursor.getString(7));
				id.setDataId2(cursor.getString(8));
				// id 3
				id.setTitleId3(cursor.getString(9));
				id.setDataId3(cursor.getString(10));
				// id 4
				id.setTitleId4(cursor.getString(11));
				id.setDataId4(cursor.getString(12));
				// id 5
				id.setTitleId5(cursor.getString(13));
				id.setDataId5(cursor.getString(14));
				// id 6
				id.setTitleId6(cursor.getString(15));
				id.setDataId6(cursor.getString(16));
				// id 7
				id.setTitleId7(cursor.getString(17));
				id.setDataId7(cursor.getString(18));
				// id 8
				id.setTitleId8(cursor.getString(19));
				id.setDataId8(cursor.getString(20));
				// id 9
				id.setTitleId9(cursor.getString(21));
				id.setDataId9(cursor.getString(22));
				// id 10
				id.setTitleId10(cursor.getString(23));
				id.setDataId10(cursor.getString(24));
				// id 11
				id.setTitleId11(cursor.getString(25));
				id.setDataId11(cursor.getString(26));
				// id 12
				id.setTitleId12(cursor.getString(27));
				id.setDataId12(cursor.getString(28));

				//
				id.setUrl(cursor.getString(29));
				id.setNote(cursor.getString(30));
				id.setImageMemo(cursor.getString(31));
				id.setFlag(cursor.getString(32));
				id.setTimeStamp(cursor.getString(33));
				id.setEncrypted(Boolean.getBoolean(cursor.getString(34)));
				id.setUserId(Integer.parseInt(cursor.getString(35)));

				// Adding id to list
				idsList.add(id);
			} while (cursor.moveToNext());
		}
		db.close();
		// return id list
		return idsList;
	}

	/**
	 * get all ids from folder id
	 * 
	 * @param id
	 * @return
	 */
	public List<IDDataBase> getAllIDsFromFolderId(int folderId) {
		List<IDDataBase> idsList = new ArrayList<IDDataBase>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_T_IDS, new String[] { KEY_PASS_WORD_ID, KEY_FOLDER_ID,
				KEY_TITLE_RECORD, KEY_ICON, KEY_FAVOURITE_GROUP, KEY_TITLE_ID_1, KEY_DATA_ID_1,
				KEY_TITLE_ID_2, KEY_DATA_ID_2, KEY_TITLE_ID_3, KEY_DATA_ID_3, KEY_TITLE_ID_4,
				KEY_DATA_ID_4, KEY_TITLE_ID_5, KEY_DATA_ID_5, KEY_TITLE_ID_6, KEY_DATA_ID_6,
				KEY_TITLE_ID_7, KEY_DATA_ID_7, KEY_TITLE_ID_8, KEY_DATA_ID_8, KEY_TITLE_ID_9,
				KEY_DATA_ID_9, KEY_TITLE_ID_10, KEY_DATA_ID_10, KEY_TITLE_ID_11, KEY_DATA_ID_11,
				KEY_TITLE_ID_12, KEY_DATA_ID_12, KEY_URL, KEY_NOTE, KEY_IMAGE_MEMO, KEY_FLAG,
				KEY_TIMES_STAMP, KEY_IS_ENCRYPTED, KEY_USER_ID }, KEY_FOLDER_ID + "=?",
				new String[] { String.valueOf(folderId) }, null, null, null, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				IDDataBase id = new IDDataBase();
				id.setPassWordId(Integer.parseInt(cursor.getString(0)));
				id.setFolderId(Integer.parseInt(cursor.getString(1)));
				id.setTitleRecord(cursor.getString(2));
				id.setIcon(cursor.getString(3));
				id.setFavouriteGroup(cursor.getString(4));
				// id 1
				id.setTitleId1(cursor.getString(5));
				id.setDataId1(cursor.getString(6));
				// id 2
				id.setTitleId2(cursor.getString(7));
				id.setDataId2(cursor.getString(8));
				// id 3
				id.setTitleId3(cursor.getString(9));
				id.setDataId3(cursor.getString(10));
				// id 4
				id.setTitleId4(cursor.getString(11));
				id.setDataId4(cursor.getString(12));
				// id 5
				id.setTitleId5(cursor.getString(13));
				id.setDataId5(cursor.getString(14));
				// id 6
				id.setTitleId6(cursor.getString(15));
				id.setDataId6(cursor.getString(16));
				// id 7
				id.setTitleId7(cursor.getString(17));
				id.setDataId7(cursor.getString(18));
				// id 8
				id.setTitleId8(cursor.getString(19));
				id.setDataId8(cursor.getString(20));
				// id 9
				id.setTitleId9(cursor.getString(21));
				id.setDataId9(cursor.getString(22));
				// id 10
				id.setTitleId10(cursor.getString(23));
				id.setDataId10(cursor.getString(24));
				// id 11
				id.setTitleId11(cursor.getString(25));
				id.setDataId11(cursor.getString(26));
				// id 12
				id.setTitleId12(cursor.getString(27));
				id.setDataId12(cursor.getString(28));

				//
				id.setUrl(cursor.getString(29));
				id.setNote(cursor.getString(30));
				id.setImageMemo(cursor.getString(31));
				id.setFlag(cursor.getString(32));
				id.setTimeStamp(cursor.getString(33));
				id.setEncrypted(Boolean.getBoolean(cursor.getString(34)));
				id.setUserId(Integer.parseInt(cursor.getString(35)));

				// Adding id to list
				idsList.add(id);
			} while (cursor.moveToNext());
		}
		db.close();
		// return id list
		return idsList;
	}

	// Updating single id
	public int updateId(IDDataBase id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PASS_WORD_ID, id.getPassWordId()); // password id
		values.put(KEY_FOLDER_ID, id.getFolderId()); // folder id
		// title record
		values.put(KEY_TITLE_RECORD, id.getTitleRecord());
		// icon
		values.put(KEY_ICON, id.getIcon());
		// favourite group
		values.put(KEY_FAVOURITE_GROUP, id.getFavouriteGroup());

		// id 1
		values.put(KEY_TITLE_ID_1, id.getTitleId1());
		values.put(KEY_DATA_ID_1, id.getDataId1());
		// id 2
		values.put(KEY_TITLE_ID_2, id.getTitleId2());
		values.put(KEY_DATA_ID_2, id.getDataId2());
		// id 3
		values.put(KEY_TITLE_ID_3, id.getTitleId3());
		values.put(KEY_DATA_ID_3, id.getDataId3());
		// id 4
		values.put(KEY_TITLE_ID_4, id.getTitleId4());
		values.put(KEY_DATA_ID_4, id.getDataId4());
		// id 5
		values.put(KEY_TITLE_ID_5, id.getTitleId5());
		values.put(KEY_DATA_ID_5, id.getDataId5());
		// id 6
		values.put(KEY_TITLE_ID_6, id.getTitleId6());
		values.put(KEY_DATA_ID_6, id.getDataId6());
		// id 7
		values.put(KEY_TITLE_ID_7, id.getTitleId7());
		values.put(KEY_DATA_ID_7, id.getDataId7());
		// id 8
		values.put(KEY_TITLE_ID_8, id.getTitleId8());
		values.put(KEY_DATA_ID_8, id.getDataId8());
		// id 9
		values.put(KEY_TITLE_ID_9, id.getTitleId9());
		values.put(KEY_DATA_ID_9, id.getDataId9());
		// id 10
		values.put(KEY_TITLE_ID_10, id.getTitleId10());
		values.put(KEY_DATA_ID_10, id.getDataId10());
		// id 11
		values.put(KEY_TITLE_ID_11, id.getTitleId11());
		values.put(KEY_DATA_ID_11, id.getDataId11());
		// id 12
		values.put(KEY_TITLE_ID_12, id.getTitleId12());
		values.put(KEY_DATA_ID_12, id.getDataId12());
		// url
		values.put(KEY_URL, id.getUrl());
		// note
		values.put(KEY_NOTE, id.getNote());
		// image memo
		values.put(KEY_IMAGE_MEMO, id.getImageMemo());
		// flag
		values.put(KEY_FLAG, id.getFlag());
		// timestamp
		values.put(KEY_TIMES_STAMP, id.getTimeStamp());
		// is encrypted
		values.put(KEY_IS_ENCRYPTED, id.isEncrypted());
		// user id
		values.put(KEY_USER_ID, id.getUserId());

		// updating row
		return db.update(TABLE_T_IDS, values, KEY_PASS_WORD_ID + " = ?",
				new String[] { String.valueOf(id.getPassWordId()) });
	}

	// Deleting single id
	public void deleteIDPassword(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_IDS, KEY_PASS_WORD_ID + " = ?", new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting single id from folderId
	public void deleteIDPasswordFromFolderId(int folderId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_T_IDS, KEY_FOLDER_ID + " = ?", new String[] { String.valueOf(folderId) });
		db.close();
	}

	// Getting ids Count
	public int getIDsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_IDS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}
}
