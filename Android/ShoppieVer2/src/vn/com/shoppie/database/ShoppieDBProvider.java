package vn.com.shoppie.database;

import java.util.ArrayList;

import vn.com.shoppie.object.Collection;
import vn.com.shoppie.object.FavouriteDataObject;
import vn.com.shoppie.object.JsonDataObject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppieDBProvider extends SQLiteOpenHelper {
	private static final String DB_NAME = "shoppie.db";
	private static final int DATABASE_VERSION = 1;

	// json table
	private static final String TABLE_JSON = "table_json";
	private static final String JSON_ID = "json_id";
	private static final String JSON_TYPE = "json_type";
	private static final String JSON_DATA = "json_data";

	// favourite table
	private static final String TABLE_FAVOURITE = "table_favourite";
	private static final String _ID = "_id";
	private static final String FAVOURITE_IMAGE = "image_url";
	private static final String FAVOURITE_TYPE = "favourite_type";
	private static final String FAVOURITE_ID = "favourite_id";

	// collection
	private static final String TABLE_COLLECTION = "table_collection";
	private static final String COL_ID = "col_id";
	private static final String MERCH_ID = "merch_id";
	private static final String COLLECTION_ID = "collection_id";
	private static final String ISVIEWED = "is_viewed";

	public ShoppieDBProvider(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_JSON_TABLE = "CREATE TABLE " + TABLE_JSON + "(" + JSON_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + JSON_DATA + " TEXT,"
				+ JSON_TYPE + " TEXT" + ")";
		String CREATE_FAVOURITE_TABLE = "CREATE TABLE " + TABLE_FAVOURITE + "("
				+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + FAVOURITE_IMAGE
				+ " TEXT," + FAVOURITE_TYPE + " TEXT," + FAVOURITE_ID + " TEXT"
				+ ")";
		String CREATE_COLLECTION_TABLE = "CREATE TABLE " + TABLE_COLLECTION
				+ "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ MERCH_ID + " INTEGER," + COLLECTION_ID + " INTEGER,"
				+ ISVIEWED + " BOOLEAN" + ")";
		db.execSQL(CREATE_JSON_TABLE);
		db.execSQL(CREATE_FAVOURITE_TABLE);
		db.execSQL(CREATE_COLLECTION_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists " + TABLE_JSON);
		db.execSQL("drop table if exists " + TABLE_FAVOURITE);
		db.execSQL("drop table if exists " + TABLE_COLLECTION);
		onCreate(db);
	}

	/*-------------------------- ADD FUNCTION--------------------------*/
	public void addNewJsonData(JsonDataObject jsonDataObject) {
		SQLiteDatabase mdb = getWritableDatabase();
		ContentValues mValue = new ContentValues();
		mValue.put(JSON_DATA, jsonDataObject.getJsonData());
		mValue.put(JSON_TYPE, jsonDataObject.getType());

		mdb.insert(TABLE_JSON, null, mValue);
		mdb.close();
	}

	public void addNewFavouriteData(FavouriteDataObject favouriteDataObject) {
		SQLiteDatabase mdb = getWritableDatabase();
		ContentValues mValue = new ContentValues();
		mValue.put(FAVOURITE_IMAGE, favouriteDataObject.getImage_url());
		mValue.put(FAVOURITE_TYPE, favouriteDataObject.getType());
		mValue.put(FAVOURITE_ID, favouriteDataObject.getFavourite_id());
		mdb.insert(TABLE_FAVOURITE, null, mValue);
		mdb.close();
	}

	public void addNewCollection(Collection collection) {
		SQLiteDatabase mdb = getWritableDatabase();
		ContentValues mValue = new ContentValues();
		mValue.put(COLLECTION_ID, collection.getCollectionId());
		mValue.put(MERCH_ID, collection.getMerchId());
		mValue.put(ISVIEWED, collection.isViewed());
		mdb.insert(TABLE_COLLECTION, null, mValue);
		mdb.close();
	}

	/*-------------------------- INSERT - UPDATE FUNCTION--------------------------*/
	public void updateJsonData(JsonDataObject jsonDataObject) {

		SQLiteDatabase mdb = getWritableDatabase();
		ContentValues mValue = new ContentValues();
		mValue.put(JSON_ID, jsonDataObject.get_id());
		mValue.put(JSON_DATA, jsonDataObject.getJsonData());
		mValue.put(JSON_TYPE, jsonDataObject.getType());

		mdb.update(TABLE_JSON, mValue, JSON_ID + " = ?",
				new String[] { String.valueOf(jsonDataObject.get_id()) });
		mdb.close();
	}

	public void updateFavouriteData(FavouriteDataObject favouriteDataObject) {
		SQLiteDatabase mdb = getWritableDatabase();
		ContentValues mValue = new ContentValues();
		mValue.put(_ID, favouriteDataObject.get_id());
		mValue.put(FAVOURITE_IMAGE, favouriteDataObject.getImage_url());
		mValue.put(FAVOURITE_TYPE, favouriteDataObject.getType());
		mValue.put(FAVOURITE_ID, favouriteDataObject.getFavourite_id());

		mdb.update(TABLE_FAVOURITE, mValue, _ID + " = ?",
				new String[] { String.valueOf(favouriteDataObject.get_id()) });
		mdb.close();
	}

	public void updateCollection(Collection collection) {
		SQLiteDatabase mdb = getWritableDatabase();
		ContentValues mValue = new ContentValues();
		mValue.put(COLLECTION_ID, collection.getCollectionId());
		mValue.put(MERCH_ID, collection.getMerchId());
		mValue.put(ISVIEWED, collection.isViewed());

		mdb.update(TABLE_COLLECTION, mValue, COL_ID + " = ?",
				new String[] { String.valueOf(collection.get_id()) });
		mdb.close();
	}

	/*-------------------------- GET FUNCTION--------------------------*/

	public JsonDataObject getJsonData(String type) {
		SQLiteDatabase mdb = getReadableDatabase();
		JsonDataObject jsonDataObject = new JsonDataObject();
		Cursor mCursor = mdb.query(TABLE_JSON, null, JSON_TYPE + " = ?",
				new String[] { String.valueOf(type) }, null, null, null);
		if (mCursor.moveToFirst()) {
			jsonDataObject = new JsonDataObject(Integer.parseInt(mCursor
					.getString(0)), mCursor.getString(1), mCursor.getString(2));
		}
		mCursor.close();
		mdb.close();
		return jsonDataObject;
	}

	public ArrayList<FavouriteDataObject> getFavouriteData(String type) {
		SQLiteDatabase mdb = getReadableDatabase();
		ArrayList<FavouriteDataObject> favouriteList = new ArrayList<FavouriteDataObject>();
		Cursor mCursor = mdb.query(TABLE_FAVOURITE, null, FAVOURITE_TYPE
				+ " = ?", new String[] { String.valueOf(type) }, null, null,
				null);
		if (mCursor.moveToFirst()) {
			do {
				FavouriteDataObject favouriteDataObject = new FavouriteDataObject();
				favouriteDataObject = new FavouriteDataObject(
						Integer.parseInt(mCursor.getString(0)),
						mCursor.getString(1), mCursor.getString(2),
						mCursor.getString(3));
				favouriteList.add(favouriteDataObject);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		mdb.close();
		return favouriteList;
	}

	public ArrayList<Collection> getCollectionData(int merchId,
			int collectionId) {
		SQLiteDatabase mdb = getReadableDatabase();
		ArrayList<Collection> collections = new ArrayList<Collection>();
		String query = "SELECT * FROM " + TABLE_COLLECTION + " WHERE "
				+ MERCH_ID + "='" + merchId + "' and " + COLLECTION_ID + " = '"
				+ collectionId + "'";
		Cursor mCursor = mdb.rawQuery(query, null);
		if (mCursor.moveToFirst()) {
			do {
				Collection collection = new Collection();
				collection = new Collection(
						Integer.parseInt(mCursor.getString(0)),
						Integer.parseInt(mCursor.getString(0)), Integer.parseInt(mCursor.getString(0)),
						Boolean.parseBoolean(mCursor.getString(3)));
				collections.add(collection);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		mdb.close();
		return collections;
	}

	/*-------------------------- DELETE FUNCTION--------------------------*/

	// delete jsondata
	public void deleteJsonData(String type) {
		SQLiteDatabase mdb = getWritableDatabase();
		mdb.delete(TABLE_JSON, JSON_TYPE + " = ?",
				new String[] { String.valueOf(type) });
		mdb.close();
	}

	// delete favourite data
	public void deleteFavouriteData(String type) {
		SQLiteDatabase mdb = getWritableDatabase();
		mdb.delete(TABLE_FAVOURITE, FAVOURITE_TYPE + " = ?",
				new String[] { String.valueOf(type) });
		mdb.close();
	}

	// delete favourite data
	public void deleteFavouriteDataById(String favouriteId) {
		SQLiteDatabase mdb = getWritableDatabase();
		mdb.delete(TABLE_FAVOURITE, FAVOURITE_ID + " = ?",
				new String[] { String.valueOf(favouriteId) });
		mdb.close();
	}

	// delete favourite data
	public void deleteCollectionById(String colId) {
		SQLiteDatabase mdb = getWritableDatabase();
		mdb.delete(TABLE_FAVOURITE, COL_ID + " = ?",
				new String[] { String.valueOf(colId) });
		mdb.close();
	}

	/*-------------------------- COUNT FUNCTION--------------------------*/

	public int countJsonData(String type) {
		SQLiteDatabase mdb = getReadableDatabase();
		int count = 0;
		String querry = "select * from " + TABLE_JSON + " where " + JSON_TYPE
				+ " ='" + type + "'";
		Cursor cursor = mdb.rawQuery(querry, null);
		count = cursor.getCount();
		cursor.close();
		mdb.close();
		return count;
	}

	public int countFavouriteData(String type) {
		SQLiteDatabase mdb = getReadableDatabase();
		int count = 0;
		String querry = "select * from " + TABLE_FAVOURITE + " where "
				+ FAVOURITE_TYPE + " ='" + type + "'";
		Cursor cursor = mdb.rawQuery(querry, null);
		count = cursor.getCount();
		cursor.close();
		mdb.close();
		return count;
	}
}
