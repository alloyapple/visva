package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbException;
import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.Shop;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class ShopMng extends adbManager{
	public static final String TAG = "Class: ShopMng";

	Context context;

	public static final String TABLE = "Shop";

	public static final String idAvatarDef = "idAvatarDef";
	public static final String title = "title";
	public static final String score = "score";
	public static final String desc = "desc";
	public static final String discount = "discount";
	
	public static final String[] COLUMNS = { adbManager._id, idAvatarDef, title, score,
		desc, discount };

	public ShopMng(Context context) {
		super(TABLE, COLUMNS);
		help = new SDataBaseHelper(context);// IMPORTANT!! must be called before
											// init()
		super.init(help); // IMPORTANT!!
	}

	@Override
	public ArrayList<Shop> select() throws SQLiteException {
		ArrayList<Shop> array = new ArrayList<Shop>();
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
	public Shop select(int id) throws SQLiteException {
		Shop object = null;
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
	public ArrayList<Shop> select(String field, String value)
			throws SQLiteException {
		ArrayList<Shop> array = new ArrayList<Shop>();
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
	public ArrayList<Shop> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<Shop> array = new ArrayList<Shop>();
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
			Shop t = new Shop(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public Shop getObjectFromCusor(Cursor cursor) {
		Shop t = null;
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
			t = new Shop(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}
}