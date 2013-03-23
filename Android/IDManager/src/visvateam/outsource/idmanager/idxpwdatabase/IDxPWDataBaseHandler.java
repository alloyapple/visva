package visvateam.outsource.idmanager.idxpwdatabase;

import java.util.ArrayList;
import java.util.List;
import visvateam.outsource.idmanager.contants.Contants;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class IDxPWDataBaseHandler extends SQLiteOpenHelper {
	// User table name
	private static final String TABLE_USERS = "User";
	// User Table Columns names
	private static final String KEY_USER_ID = "userId";
	private static final String KEY_USER_PASSWORD = "password";
	private static final String KEY_USER_EMAIL = "sEmail";

	// folders table name
	private static final String TABLE_GROUP_FOLDER = "GroupFolder";
	// folders Table Columns names
	private static final String KEY_GROUP_ID = "gId";
	private static final String KEY_GROUP_NAME = "gName";
	private static final String KEY_GROUP_TYPE = "gType";
	private static final String KEY_GROUP_USER_ID = "gUserId";
	private static final String KEY_GROUP_ORDER = "gOrder";

	// password table name
	private static final String TABLE_PASSWORD = "Password";
	// password table Columns names
	private static final String KEY_PASSWORD_ID = "PasswordId";
	private static final String KEY_ELEMENT_ID = "eId";
	private static final String KEY_TITLE_NAME_ID = "titleNameId";
	private static final String KEY_PASSWORD = "Password";

	// elementId table name
	private static final String TABLE_ELEMENT_ID = "ElementID";
	// element id table Columns names
	private static final String KEY_E_ID = "eId";
	private static final String KEY_E_GROUP_ID = "eGroupId";
	private static final String KEY_E_TITLE = "eTitle";
	private static final String KEY_E_ICON = "eIcon";
	private static final String KEY_E_TIME_STAMP = "eTimeStamp";
	private static final String KEY_E_FAVOURITE = "eFavourite";
	private static final String KEY_E_FLAG = "eFlag";
	private static final String KEY_E_URL = "eUrl";
	private static final String KEY_E_NOTE = "eNote";
	private static final String KEY_E_IMAGE = "eImage";
	private static final String KEY_E_ORDER = "eOrder";

	public IDxPWDataBaseHandler(Context context) {
		super(context, Contants.DATA_IDMANAGER_NAME, null, Contants.DATA_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + KEY_USER_ID
				+ " INTEGER PRIMARY KEY," + KEY_USER_PASSWORD + " TEXT," + KEY_USER_EMAIL + " TEXT"
				+ ")";
		db.execSQL(CREATE_USER_TABLE);

		/* create group folder db */
		String CREATE_GROUP_FOLDER_TABLE = "CREATE TABLE " + TABLE_GROUP_FOLDER + "("
				+ KEY_GROUP_ID + " INTEGER PRIMARY KEY," + KEY_GROUP_NAME + " TEXT,"
				+ KEY_GROUP_TYPE + " INTEGER," + KEY_GROUP_USER_ID + " INTEGER," + KEY_GROUP_ORDER
				+ " INTEGER" + ")";
		db.execSQL(CREATE_GROUP_FOLDER_TABLE);

		/* create password table db */
		String CREATE_PASSWORD_TABLE = "CREATE TABLE " + TABLE_PASSWORD + "(" + KEY_PASSWORD_ID
				+ " INTEGER PRIMARY KEY," + KEY_ELEMENT_ID + " INTEGER," + KEY_TITLE_NAME_ID
				+ " TEXT," + KEY_PASSWORD + " TEXT" + ")";
		db.execSQL(CREATE_PASSWORD_TABLE);

		/* create element id table db */
		String CREATE_ELEMENT_ID_TABLE = "CREATE TABLE " + TABLE_ELEMENT_ID + "(" + KEY_E_ID
				+ " INTEGER PRIMARY KEY," + KEY_E_GROUP_ID + " INTEGER," + KEY_E_TITLE + " TEXT,"
				+ KEY_E_ICON + " TEXT," + KEY_E_TIME_STAMP + " LONG," + KEY_E_FAVOURITE
				+ " INTEGER," + KEY_E_FLAG + " INTEGER," + KEY_E_URL + " TEXT," + KEY_E_NOTE
				+ " TEXT," + KEY_E_IMAGE + " TEXT," + KEY_E_ORDER + " INTEGER" + ")";
		db.execSQL(CREATE_ELEMENT_ID_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new user
	public void addNewUser(UserDB user) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user.getUserId()); // user id
		values.put(KEY_USER_PASSWORD, user.getPassword()); // user pw\
		values.put(KEY_USER_EMAIL, user.getsEmail());

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single user
	public UserDB getUser(int userId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_USER_ID, KEY_USER_PASSWORD,
				KEY_USER_EMAIL }, KEY_USER_ID + "=?", new String[] { String.valueOf(userId) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		UserDB user = new UserDB(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2));
		cursor.close();
		db.close();
		// return folder
		return user;
	}

	// Getting All users
	public List<UserDB> getAllUsers() {
		List<UserDB> userList = new ArrayList<UserDB>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_USERS;

		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				UserDB user = new UserDB();
				user.setUserId(Integer.parseInt(cursor.getString(0)));
				user.setPassword(cursor.getString(1));
				user.setsEmail(cursor.getString(2));

				// Adding user to list
				userList.add(user);
			} while (cursor.moveToNext());
		}
		db.close();
		// return user list
		return userList;
	}

	// Updating single user
	public int updateUser(UserDB user) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user.getUserId());
		values.put(KEY_USER_PASSWORD, user.getPassword());
		values.put(KEY_USER_EMAIL, user.getsEmail());

		// updating row
		return db.update(TABLE_USERS, values, KEY_USER_ID + " = ?",
				new String[] { String.valueOf(user.getUserId()) });
	}

	// Deleting single user
	public void deleteUser(int userId) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_USERS, KEY_USER_ID + " = ?", new String[] { String.valueOf(userId) });
		db.close();
	}

	// Getting users Count
	public int getUserCount() {
		int count = 0;
		String countQuery = "SELECT  * FROM " + TABLE_USERS;
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(countQuery, null);
		// return count
		count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new folder
	public void addNewFolder(GroupFolder folder) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_GROUP_ID, folder.getgId()); // folder id
		values.put(KEY_GROUP_NAME, folder.getgName()); // folder name
		values.put(KEY_GROUP_TYPE, folder.getgType()); // folder type
		values.put(KEY_GROUP_USER_ID, folder.getgUserId()); // folder name
		// image folder icon id
		values.put(KEY_GROUP_ORDER, folder.getgOrder());
		// Inserting Row
		db.insert(TABLE_GROUP_FOLDER, null, values);

		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single folder
	public GroupFolder getFolder(int folderId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);

		Cursor cursor = db.query(TABLE_GROUP_FOLDER, new String[] { KEY_GROUP_ID, KEY_GROUP_NAME,
				KEY_GROUP_TYPE, KEY_GROUP_USER_ID, KEY_GROUP_ORDER }, KEY_GROUP_ID + "=?",
				new String[] { String.valueOf(folderId) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		GroupFolder folder = new GroupFolder(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor
						.getString(3)), Integer.parseInt(cursor.getString(4)));
		db.close();
		// return folder
		return folder;
	}

	// Getting All folers
	public List<GroupFolder> getAllFolders() {
		List<GroupFolder> foldertList = new ArrayList<GroupFolder>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_GROUP_FOLDER;

		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				GroupFolder folder = new GroupFolder();
				folder.setgId(Integer.parseInt(cursor.getString(0)));
				folder.setgName(cursor.getString(1));
				folder.setgType(Integer.parseInt(cursor.getString(2)));
				folder.setgUserId(Integer.parseInt(cursor.getString(3)));
				folder.setgOrder(Integer.parseInt(cursor.getString(4)));
				// Adding folder to list
				foldertList.add(folder);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return folder list
		return foldertList;
	}

	// get all folder id from user id
	public List<GroupFolder> getAllFolderByUserId(int userId) {
		List<GroupFolder> foldertList = new ArrayList<GroupFolder>();
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.query(TABLE_GROUP_FOLDER, new String[] { KEY_GROUP_ID, KEY_GROUP_NAME,
				KEY_GROUP_TYPE, KEY_GROUP_USER_ID, KEY_GROUP_ORDER }, KEY_GROUP_USER_ID + "=?",
				new String[] { String.valueOf(userId) }, null, null, null, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				GroupFolder folder = new GroupFolder();
				folder.setgId(Integer.parseInt(cursor.getString(0)));
				folder.setgName(cursor.getString(1));
				folder.setgType(Integer.parseInt(cursor.getString(2)));
				folder.setgUserId(Integer.parseInt(cursor.getString(3)));
				folder.setgOrder(Integer.parseInt(cursor.getString(4)));
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
	public int updateFolder(GroupFolder folder) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_GROUP_ID, folder.getgId());
		values.put(KEY_GROUP_NAME, folder.getgName());
		values.put(KEY_GROUP_TYPE, folder.getgType());
		values.put(KEY_GROUP_USER_ID, folder.getgUserId());
		values.put(KEY_GROUP_ORDER, folder.getgOrder());
		// updating row
		return db.update(TABLE_GROUP_FOLDER, values, KEY_GROUP_ID + " = ?",
				new String[] { String.valueOf(folder.getgId()) });
	}

	// Deleting single folder
	public void deleteFolder(int folderId) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_GROUP_FOLDER, KEY_GROUP_ID + " = ?",
				new String[] { String.valueOf(folderId) });
		db.close();
	}

	// Getting folders count
	public int getFoldersCount() {
		int count;
		String countQuery = "SELECT  * FROM " + TABLE_GROUP_FOLDER;
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		count = cursor.getCount();
		cursor.close();
		return count;
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new folder
	public void addNewPassword(Password password) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_PASSWORD_ID, password.getPasswordId()); // password id
		values.put(KEY_ELEMENT_ID, password.geteId()); // user id
		values.put(KEY_TITLE_NAME_ID, password.getTitleNameId()); // folder name
		values.put(KEY_PASSWORD, password.getPassword()); // folder name
		// Inserting Row
		db.insert(TABLE_PASSWORD, null, values);

		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single folder
	public Password getPassword(int passwordId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);

		Cursor cursor = db.query(TABLE_PASSWORD, new String[] { KEY_PASSWORD_ID, KEY_ELEMENT_ID,
				KEY_TITLE_NAME_ID, KEY_PASSWORD, }, KEY_PASSWORD_ID + "=?",
				new String[] { String.valueOf(passwordId) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Password password = new Password(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3));
		db.close();
		// return folder
		return password;
	}

	// Getting All folers
	public List<Password> getAllPasswords() {
		List<Password> passwordList = new ArrayList<Password>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PASSWORD;

		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Password password = new Password();
				password.setPasswordId(Integer.parseInt(cursor.getString(0)));
				password.seteId(Integer.parseInt(cursor.getString(1)));
				password.setTitleNameId(cursor.getString(2));
				password.setPassword(cursor.getString(3));

				// Adding folder to list
				passwordList.add(password);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return folder list
		return passwordList;
	}

	// get all password in an element id
	public List<Password> getAllPasswordByElementId(int elementId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		List<Password> passwordList = new ArrayList<Password>();
		Cursor cursor = db.query(TABLE_PASSWORD, new String[] { KEY_PASSWORD_ID, KEY_ELEMENT_ID,
				KEY_TITLE_NAME_ID, KEY_PASSWORD, }, KEY_ELEMENT_ID + "=?",
				new String[] { String.valueOf(elementId) }, null, null, null, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Password password = new Password();
				password.setPasswordId(Integer.parseInt(cursor.getString(0)));
				password.seteId(Integer.parseInt(cursor.getString(1)));
				password.setTitleNameId(cursor.getString(2));
				password.setPassword(cursor.getString(3));
				// Adding folder to list
				passwordList.add(password);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return folder list
		return passwordList;
	}

	// Updating single folder
	public int updatePassword(Password password) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_PASSWORD_ID, password.getPasswordId());
		values.put(KEY_ELEMENT_ID, password.geteId());
		values.put(KEY_TITLE_NAME_ID, password.getTitleNameId());
		values.put(KEY_PASSWORD, password.getPassword());
		// updating row
		return db.update(TABLE_PASSWORD, values, KEY_PASSWORD_ID + " = ?",
				new String[] { String.valueOf(password.getPasswordId()) });
	}

	// Deleting single folder
	public void deletePassword(int passwordId) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_PASSWORD, KEY_PASSWORD_ID + " = ?",
				new String[] { String.valueOf(passwordId) });
		db.close();
	}

	// Deleting single password from elementId
	public void deletePasswordByElementId(int elementId) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_PASSWORD, KEY_ELEMENT_ID + " = ?",
				new String[] { String.valueOf(elementId) });
		db.close();
	}

	// Getting folders count
	public int getPasswordsCount() {
		int count;
		String countQuery = "SELECT  * FROM " + TABLE_PASSWORD;
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		count = cursor.getCount();
		cursor.close();
		return count;
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new ELEMENT ID
	public void addNewElementId(ElementID elementID) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_E_ID, elementID.geteId()); // folder id
		values.put(KEY_E_GROUP_ID, elementID.geteGroupId()); // user id
		values.put(KEY_E_TITLE, elementID.geteTitle()); // folder name
		values.put(KEY_E_ICON, elementID.geteIcon()); // folder name
		// image folder icon id
		values.put(KEY_E_TIME_STAMP, elementID.geteTimeStamp());
		// image folder icon edit
		values.put(KEY_E_FAVOURITE, elementID.geteFavourite());
		values.put(KEY_E_FLAG, elementID.geteFlag());
		values.put(KEY_E_URL, elementID.geteUrl());
		values.put(KEY_E_NOTE, elementID.geteNote());
		values.put(KEY_E_IMAGE, elementID.geteImage());
		values.put(KEY_E_ORDER, elementID.geteOrder());
		// Inserting Row
		db.insert(TABLE_ELEMENT_ID, null, values);

		// close db after use
		db.close(); // Closing database connection
	}

	// Getting single folder
	public ElementID getElementID(int elementId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);

		Cursor cursor = db.query(TABLE_ELEMENT_ID, new String[] { KEY_E_ID, KEY_E_GROUP_ID,
				KEY_E_TITLE, KEY_E_ICON, KEY_E_TIME_STAMP, KEY_E_FAVOURITE, KEY_E_FLAG, KEY_E_URL,
				KEY_E_NOTE, KEY_E_IMAGE, KEY_E_ORDER }, KEY_E_ID + "=?",
				new String[] { String.valueOf(elementId) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		ElementID elementID = new ElementID(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), cursor.getString(2), cursor.getString(3),
				Long.parseLong(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
				Integer.parseInt(cursor.getString(6)), cursor.getString(7), cursor.getString(8),
				cursor.getString(9), Integer.parseInt(cursor.getString(10)));
		cursor.close();
		db.close();
		// return folder
		return elementID;
	}

	// Getting All folers
	public List<ElementID> getAllElmentIds() {
		List<ElementID> elementIdList = new ArrayList<ElementID>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ELEMENT_ID;

		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ElementID elementID = new ElementID();
				elementID.seteId(Integer.parseInt(cursor.getString(0)));
				elementID.seteGroupId(Integer.parseInt(cursor.getString(1)));
				elementID.seteTitle(cursor.getString(2));
				elementID.seteIcon(cursor.getString(3));
				elementID.seteTimeStamp(Long.parseLong(cursor.getString(4)));
				elementID.seteFavourite(Integer.parseInt(cursor.getString(5)));
				elementID.seteFlag(Integer.parseInt(cursor.getString(6)));
				elementID.seteUrl(cursor.getString(7));
				elementID.seteNote(cursor.getString(8));
				elementID.seteImage(cursor.getString(9));
				elementID.seteOrder(Integer.parseInt(cursor.getString(10)));

				// Adding folder to list
				elementIdList.add(elementID);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return folder list
		return elementIdList;
	}

	// get all element id from group folder id
	public List<ElementID> getAllElementIdByGroupFolderId(int groupFolderId) {
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		List<ElementID> elementIdList = new ArrayList<ElementID>();

		Cursor cursor = db.query(TABLE_ELEMENT_ID, new String[] { KEY_E_ID, KEY_E_GROUP_ID,
				KEY_E_TITLE, KEY_E_ICON, KEY_E_TIME_STAMP, KEY_E_FAVOURITE, KEY_E_FLAG, KEY_E_URL,
				KEY_E_NOTE, KEY_E_IMAGE, KEY_E_ORDER }, KEY_E_GROUP_ID + "=?",
				new String[] { String.valueOf(groupFolderId) }, null, null, null, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ElementID elementID = new ElementID();
				elementID.seteId(Integer.parseInt(cursor.getString(0)));
				elementID.seteGroupId(Integer.parseInt(cursor.getString(1)));
				elementID.seteTitle(cursor.getString(2));
				elementID.seteIcon(cursor.getString(3));
				elementID.seteTimeStamp(Long.parseLong(cursor.getString(4)));
				elementID.seteFavourite(Integer.parseInt(cursor.getString(5)));
				elementID.seteFlag(Integer.parseInt(cursor.getString(6)));
				elementID.seteUrl(cursor.getString(7));
				elementID.seteNote(cursor.getString(8));
				elementID.seteImage(cursor.getString(9));
				elementID.seteOrder(Integer.parseInt(cursor.getString(10)));

				// Adding folder to list
				elementIdList.add(elementID);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return folder list
		return elementIdList;
	}

	// Updating single folder
	public int updateElementId(ElementID elementID) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);

		ContentValues values = new ContentValues();
		values.put(KEY_E_ID, elementID.geteId()); // folder id
		values.put(KEY_E_GROUP_ID, elementID.geteGroupId()); // user id
		values.put(KEY_E_TITLE, elementID.geteTitle()); // folder name
		values.put(KEY_E_ICON, elementID.geteIcon()); // folder name
		// image folder icon id
		values.put(KEY_E_TIME_STAMP, elementID.geteTimeStamp());
		// image folder icon edit
		values.put(KEY_E_FAVOURITE, elementID.geteFavourite());
		values.put(KEY_E_FLAG, elementID.geteFlag());
		values.put(KEY_E_URL, elementID.geteUrl());
		values.put(KEY_E_NOTE, elementID.geteNote());
		values.put(KEY_E_IMAGE, elementID.geteImage());
		values.put(KEY_E_ORDER, elementID.geteOrder());
		// updating row
		return db.update(TABLE_ELEMENT_ID, values, KEY_E_ID + " = ?",
				new String[] { String.valueOf(elementID.geteId()) });
	}

	// Deleting single folder
	public void deleteElementId(int elementId) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_ELEMENT_ID, KEY_E_ID + " = ?", new String[] { String.valueOf(elementId) });
		db.close();
	}

	// Deleting elements from folder id
	public void deleteElementIdInFolderId(int folderId) {
		SQLiteDatabase db = this.getWritableDatabase(Contants.KEY_DATA_PW);
		db.delete(TABLE_ELEMENT_ID, KEY_E_GROUP_ID + " = ?",
				new String[] { String.valueOf(folderId) });
		db.close();
	}

	// Getting folders count
	public int getElementsCount() {
		int count;
		String countQuery = "SELECT  * FROM " + TABLE_ELEMENT_ID;
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		count = cursor.getCount();
		cursor.close();
		return count;
	}

	// Getting folders count
	public int getElementsCountFromFolder(int groupFolder) {
		int count;
		String countQuery = "SELECT  * FROM " + TABLE_ELEMENT_ID + " WHERE " + KEY_E_GROUP_ID + "="
				+ String.valueOf(groupFolder);
		SQLiteDatabase db = this.getReadableDatabase(Contants.KEY_DATA_PW);
		Cursor cursor = db.rawQuery(countQuery, null);

		// return count
		count = cursor.getCount();
		cursor.close();
		return count;
	}
}
