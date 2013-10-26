package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbException;
import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.Store;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class StoreMng extends adbManager{
	public static final String TAG = "Class: StoreMng";

	Context context;

	public static final String TABLE = "Store";

	public static final String storeName = "storeName";
	public static final String merchId = "merchId";
	public static final String storeId = "storeId";
	public static final String storeCode = "storeCode";
	public static final String storeAddress = "storeAddress";
	public static final String latitude = "latitude";
	public static final String longtitude = "longtitude";
	public static final String thumbnail = "thumbnail";
	public static final String merchCatId = "merchCatId";
	
	public static final String[] COLUMNS = { adbManager._id, storeName, merchId, storeId,
		storeCode, storeAddress,latitude,longtitude,thumbnail,merchCatId };

	public StoreMng(Context context) {
		super(TABLE, COLUMNS);
		help = new SDataBaseHelper(context);// IMPORTANT!! must be called before
											// init()
		super.init(help); // IMPORTANT!!
	}

	@Override
	public ArrayList<Store> select() throws SQLiteException {
		ArrayList<Store> array = new ArrayList<Store>();
		if(help.getReadableDatabase().isOpen())
			help.getReadableDatabase().close();
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cursor = db.query(TABLE, COLUMNS, null, null, null, null,
				adbManager._id);
		array = getArrayObjectFromCursor(cursor);
		db.close();
		help.close();
		return array;
	}

	@Override
	public Store select(int id) throws SQLiteException {
		Store object = null;
		if (id >= 0) {
			if(help.getReadableDatabase().isOpen())
				help.getReadableDatabase().close();
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cursor = db.query(TABLE, COLUMNS, adbManager._id + "='" + id
					+ "'", null, null, null, adbManager._id);
			cursor.moveToFirst();

			object = getObjectFromCusor(cursor);
			db.close();
			help.close();
		}
		return object;
	}

	@Override
	public ArrayList<Store> select(String field, String value)
			throws SQLiteException {
		ArrayList<Store> array = new ArrayList<Store>();
		if(help.getReadableDatabase().isOpen())
			help.getReadableDatabase().close();
		SQLiteDatabase db = help.getReadableDatabase();
		boolean hasFields=false;
		for (String tmp : COLUMNS)
			if (tmp.equals(field)) {
				hasFields=true;
				break;
			}
		if(!hasFields)
			try {
				throw new adbException("Not matching Fileds to SELECT");
			} catch (adbException e) {
				e.printStackTrace();
			}
		Cursor cursor = db.query(TABLE, COLUMNS, field + "='" + value + "'",
				null, null, null, adbManager._id);

		array = getArrayObjectFromCursor(cursor);
		db.close();
		help.close();
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Store> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<Store> array = new ArrayList<Store>();
		if (cursor.getCount() == 0) {
			Log.e(TAG, "cursor is null");
			cursor.close();
			return array;
		}

		cursor.moveToFirst();
		do {
			int _id = cursor.getInt(cursor.getColumnIndex(adbManager._id));
			int size = COLUMNS.length - 1;
			String[] values = new String[size];
			for (int i = 0; i < size; ++i) {
				values[i] = cursor.getString(i + 1);
			}
			Store t = new Store(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public Store getObjectFromCusor(Cursor cursor) {
		Store t = null;
		cursor.moveToFirst();

		if (cursor.getCount() == 0) {
			Log.e(TAG, "cursor is null");
			cursor.close();
			return t;
		}
		do {
			int _id = cursor.getInt(cursor.getColumnIndex(adbManager._id));
			int size = COLUMNS.length - 1;
			String[] values = new String[size];
			for (int i = 0; i < size; ++i) {
				values[i] = cursor.getString(i + 1);
			}
			t = new Store(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}
}