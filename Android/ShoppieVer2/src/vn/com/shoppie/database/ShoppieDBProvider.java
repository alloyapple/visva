package vn.com.shoppie.database;

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
	private static final String FAVOURITE_ID = "favourite_id";
	private static final String FAVOURITE_IMAGE = "image_url";
	private static final String FAVOURITE_TYPE = "favourite_type";

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
				+ FAVOURITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ FAVOURITE_IMAGE + " TEXT," + FAVOURITE_TYPE + " TEXT" + ")";
		db.execSQL(CREATE_JSON_TABLE);
		db.execSQL(CREATE_FAVOURITE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists " + TABLE_JSON);
		db.execSQL("drop table if exists " + TABLE_FAVOURITE);
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

		mdb.insert(TABLE_FAVOURITE, null, mValue);
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
		mValue.put(FAVOURITE_ID, favouriteDataObject.get_id());
		mValue.put(FAVOURITE_IMAGE, favouriteDataObject.getImage_url());
		mValue.put(FAVOURITE_TYPE, favouriteDataObject.getType());

		mdb.update(TABLE_FAVOURITE, mValue, FAVOURITE_ID + " = ?",
				new String[] { String.valueOf(favouriteDataObject.get_id()) });
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
		return jsonDataObject;
	}

	public FavouriteDataObject getFavouriteData(String type) {
		SQLiteDatabase mdb = getReadableDatabase();
		FavouriteDataObject favouriteDataObject = new FavouriteDataObject();
		Cursor mCursor = mdb.query(TABLE_FAVOURITE, null,
				FAVOURITE_ID + " = ?", new String[] { String.valueOf(type) },
				null, null, null);
		if (mCursor.moveToFirst()) {
			favouriteDataObject = new FavouriteDataObject(
					Integer.parseInt(mCursor.getString(0)),
					mCursor.getString(1), mCursor.getString(2));
		}
		mCursor.close();
		return favouriteDataObject;
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

	/*-------------------------- COUNT FUNCTION--------------------------*/

	public int countJsonData(String type) {
		SQLiteDatabase mdb = getReadableDatabase();
		int count = 0;
		String querry = "select * from " + TABLE_JSON + " where "+JSON_TYPE+" ='"+type+"'";
		Cursor cursor = mdb.rawQuery(querry, null);
		count = cursor.getCount();
		cursor.close();
		mdb.close();
		return count;
	}
	public int countFavouriteData(String type) {
		SQLiteDatabase mdb = getReadableDatabase();
		int count = 0;
		String querry = "select * from " + TABLE_FAVOURITE + " where "+FAVOURITE_TYPE+" ='"+type+"'";
		Cursor cursor = mdb.rawQuery(querry, null);
		count = cursor.getCount();
		cursor.close();
		mdb.close();
		return count;
	}
}
