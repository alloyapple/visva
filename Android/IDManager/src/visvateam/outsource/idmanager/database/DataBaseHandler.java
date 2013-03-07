package visvateam.outsource.idmanager.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.formula.udf.UDFFinder;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import visvateam.outsource.idmanager.contants.Contants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper {

	// folders table name
	private static final String TABLE_T_FOLDERS = "TABLE_T_FOLDERS";
	// folders Table Columns names
	private static final String KEY_FOLDER_ID = "KEY_FOLDER_ID";
	private static final String KEY_USER_ID = "KEY_USER_ID";
	private static final String KEY_FOLDER_NAME = "KEY_FOLDER_NAME";
	private static final String KEY_IMG_FOLDER_ID = "KEY_IMG_FOLDER_ID";
	private static final String KEY_IMG_FOLDER_ICON_ID = "KEY_IMG_FOLDER_ICON_ID";
	private static final String KEY_IS_NORMAL_FOLDER = "KEY_IS_NORMAL_FOLDER";

	// id table name
	private static final String TABLE_T_IDS = "TABLE_T_IDS";
	// id Table Columns names
	private static final String KEY_PASS_WORD_ID = "KEY_PASS_WORD_ID";
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
	private static final String KEY_IS_LIKE = "KEY_IS_LIKE";

	// User table name
	private static final String TABLE_T_USERS = "TABLE_T_USERS";

	// User Table Columns names
	private static final String KEY_USER_PASSWORD = "KEY_USER_PASSWORD";
	private static final String KEY_LAST_TIME_SIGN_IN = "KEY_LAST_TIME_SIGN_IN";

	public DataBaseHandler(Context context) {
		super(context, Contants.DATA_IDMANAGER_NAME, null, Contants.DATA_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// TODO Auto-generated method stub
		String CREATE_FOLDERS_TABLE = "CREATE TABLE " + TABLE_T_FOLDERS + "(" + KEY_FOLDER_ID
				+ " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER," + KEY_FOLDER_NAME + " TEXT,"
				+ KEY_IMG_FOLDER_ID + " INTEGER," + KEY_IMG_FOLDER_ICON_ID + " INTEGER,"
				+ KEY_IS_NORMAL_FOLDER + " INTEGER" + ")";
		db.execSQL(CREATE_FOLDERS_TABLE);

		String CREATE_IDS_TABLE = "CREATE TABLE " + TABLE_T_IDS + "(" + KEY_PASS_WORD_ID
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
				+ KEY_IMAGE_MEMO + " TEXT," + KEY_FLAG + " TEXT," + KEY_TIMES_STAMP + " TEXT,"
				+ KEY_IS_ENCRYPTED + " Boolean," + KEY_USER_ID + " INTEGER," + KEY_IS_LIKE
				+ " Boolean" + ")";
		db.execSQL(CREATE_IDS_TABLE);

		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_T_USERS + "(" + KEY_USER_ID
				+ " INTEGER PRIMARY KEY," + KEY_USER_PASSWORD + " TEXT," + KEY_LAST_TIME_SIGN_IN
				+ " TEXT" + ")";
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_FOLDERS);
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_IDS);
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_T_USERS);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new folder
	public void addNewFolder(FolderDatabase folder) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

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

	// Getting single folder
	public FolderDatabase getFolder(int folderId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);

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

	// Getting All folers
	public List<FolderDatabase> getAllFolders() {
		List<FolderDatabase> foldertList = new ArrayList<FolderDatabase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_FOLDERS;

		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
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
		cursor.close();
		db.close();
		// return folder list
		return foldertList;
	}

	// Updating single folder
	public int updateFolder(FolderDatabase folder) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

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
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_T_FOLDERS, KEY_FOLDER_ID + " = ?",
				new String[] { String.valueOf(folderId) });
		db.close();
	}

	// Getting folders count
	public int getFoldersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_FOLDERS;
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new id
	public void addNewID(IDDataBase id) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

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
		// is farourite
		values.put(KEY_IS_LIKE, id.isLike());
		Log.e("s issksdl", "aksdhflskj "+id.isLike());
		// Inserting Row
		db.insert(TABLE_T_IDS, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single id
	public IDDataBase getId(int folderId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);

		Cursor cursor = db.query(TABLE_T_IDS, new String[] { KEY_PASS_WORD_ID, KEY_FOLDER_ID,
				KEY_TITLE_RECORD, KEY_ICON, KEY_FAVOURITE_GROUP, KEY_TITLE_ID_1, KEY_DATA_ID_1,
				KEY_TITLE_ID_2, KEY_DATA_ID_2, KEY_TITLE_ID_3, KEY_DATA_ID_3, KEY_TITLE_ID_4,
				KEY_DATA_ID_4, KEY_TITLE_ID_5, KEY_DATA_ID_5, KEY_TITLE_ID_6, KEY_DATA_ID_6,
				KEY_TITLE_ID_7, KEY_DATA_ID_7, KEY_TITLE_ID_8, KEY_DATA_ID_8, KEY_TITLE_ID_9,
				KEY_DATA_ID_9, KEY_TITLE_ID_10, KEY_DATA_ID_10, KEY_TITLE_ID_11, KEY_DATA_ID_11,
				KEY_TITLE_ID_12, KEY_DATA_ID_12, KEY_URL, KEY_NOTE, KEY_IMAGE_MEMO, KEY_FLAG,
				KEY_TIMES_STAMP, KEY_IS_ENCRYPTED, KEY_USER_ID, KEY_IS_LIKE }, KEY_PASS_WORD_ID
				+ "=?", new String[] { String.valueOf(folderId) }, null, null, null, null);
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
						.getString(34)), Integer.parseInt(cursor.getString(35)),
				Boolean.parseBoolean(cursor.getString(36)));
		cursor.close();
		db.close();
		// return folder
		return id;
	}

	// Getting All ids
	public List<IDDataBase> getAllIDs() {
		List<IDDataBase> idsList = new ArrayList<IDDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_IDS;

		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
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
				id.setLike(Boolean.parseBoolean(cursor.getString(36)));
				// Adding id to list
				idsList.add(id);
			} while (cursor.moveToNext());
		}
		cursor.close();
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
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);

		Cursor cursor = db.query(TABLE_T_IDS, new String[] { KEY_PASS_WORD_ID, KEY_FOLDER_ID,
				KEY_TITLE_RECORD, KEY_ICON, KEY_FAVOURITE_GROUP, KEY_TITLE_ID_1, KEY_DATA_ID_1,
				KEY_TITLE_ID_2, KEY_DATA_ID_2, KEY_TITLE_ID_3, KEY_DATA_ID_3, KEY_TITLE_ID_4,
				KEY_DATA_ID_4, KEY_TITLE_ID_5, KEY_DATA_ID_5, KEY_TITLE_ID_6, KEY_DATA_ID_6,
				KEY_TITLE_ID_7, KEY_DATA_ID_7, KEY_TITLE_ID_8, KEY_DATA_ID_8, KEY_TITLE_ID_9,
				KEY_DATA_ID_9, KEY_TITLE_ID_10, KEY_DATA_ID_10, KEY_TITLE_ID_11, KEY_DATA_ID_11,
				KEY_TITLE_ID_12, KEY_DATA_ID_12, KEY_URL, KEY_NOTE, KEY_IMAGE_MEMO, KEY_FLAG,
				KEY_TIMES_STAMP, KEY_IS_ENCRYPTED, KEY_USER_ID,KEY_IS_LIKE }, KEY_FOLDER_ID + "=?",
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
				id.setLike(Boolean.parseBoolean(cursor.getString(36)));
				
				// Adding id to list
				idsList.add(id);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return id list
		return idsList;
	}

	// Updating single id
	public int updateId(IDDataBase id) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

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
		// is favourite
		values.put(KEY_IS_LIKE, id.isLike());

		// updating row
		return db.update(TABLE_T_IDS, values, KEY_PASS_WORD_ID + " = ?",
				new String[] { String.valueOf(id.getPassWordId()) });
	}

	// Deleting single id
	public void deleteIDPassword(int id) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_T_IDS, KEY_PASS_WORD_ID + " = ?", new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting single id from folderId
	public void deleteIDPasswordFromFolderId(int folderId) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_T_IDS, KEY_FOLDER_ID + " = ?", new String[] { String.valueOf(folderId) });
		db.close();
	}

	// Getting ids Count
	public int getIDsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_T_IDS;
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		return cursor.getCount();
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new user
	public void addNewUser(UserDataBase user) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

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
	public UserDataBase getUser(int userId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);

		Cursor cursor = db.query(TABLE_T_USERS, new String[] { KEY_USER_ID, KEY_USER_PASSWORD,
				KEY_LAST_TIME_SIGN_IN }, KEY_USER_ID + "=?",
				new String[] { String.valueOf(userId) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		UserDataBase user = new UserDataBase(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		cursor.close();
		db.close();
		// return folder
		return user;
	}

	// Getting All users
	public List<UserDataBase> getAllUsers() {
		List<UserDataBase> userList = new ArrayList<UserDataBase>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_T_USERS;

		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
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
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user.getUserId());
		values.put(KEY_USER_PASSWORD, user.getUserPassword());
		values.put(KEY_LAST_TIME_SIGN_IN, user.getLastTimeSignIn());

		// updating row
		return db.update(TABLE_T_USERS, values, KEY_USER_ID + " = ?",
				new String[] { String.valueOf(user.getUserId()) });
	}

	// Deleting single user
	public void deleteUser(UserDataBase user) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_T_USERS, KEY_USER_ID + " = ?",
				new String[] { String.valueOf(user.getUserId()) });
		db.close();
	}

	// Getting users Count
	public int getUserCount() {
		int count = 0;
		String countQuery = "SELECT  * FROM " + TABLE_T_USERS;
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(countQuery, null);
		// return count
		count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}
}
