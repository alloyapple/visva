package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.GcmNotify;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class GcmNotifyMng extends adbManager {

	Context context;

	public static final String TABLE = "GcmNotify";

	public static final String message = "message";
	public static final String type = "type";
	public static final String pieQty = "pieQty";
	public static final String time = "time";

	public static final String[] COLUMNS = { adbManager._id, message, type, pieQty, time };

	public GcmNotifyMng(Context context) {
		super(TABLE, COLUMNS);
		help = new SDataBaseHelper(context); // IMPORTANT!! must be called before  init()
		super.init(help); // IMPORTANT!!
	}

	@Override
	public ArrayList<GcmNotify> select() throws SQLiteException {
		ArrayList<GcmNotify> array = new ArrayList<GcmNotify>();
		if (help.getReadableDatabase().isOpen())
			help.getReadableDatabase().close();
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cursor = db.query(TABLE, COLUMNS, null, null, null, null, adbManager._id);
		array = getArrayObjectFromCursor(cursor);
		db.close();
		help.close();
		return array;
	}

	@Override
	public GcmNotify select(int id) throws SQLiteException {
		GcmNotify object = null;
		if (id >= 0) {
			if (help.getReadableDatabase().isOpen())
				help.getReadableDatabase().close();
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cursor = db.query(TABLE, COLUMNS, adbManager._id + "='" + id + "'", null, null, null, adbManager._id);
			cursor.moveToFirst();

			object = getObjectFromCusor(cursor);
			db.close();
			help.close();
		}
		return object;
	}

	@Override
	public ArrayList<GcmNotify> select(String field, String value) throws SQLiteException {
		ArrayList<GcmNotify> array = new ArrayList<GcmNotify>();
		if (help.getReadableDatabase().isOpen())
			help.getReadableDatabase().close();
		SQLiteDatabase db = help.getReadableDatabase();
		boolean hasField = false;
		for (String tmp : COLUMNS)
			if (tmp.equals(field)) {
				hasField = true;
				break;
			}
		if (!hasField) {
			Log.e(TAG, "not matching field to SELECT");
			return array;
		}
		Cursor cursor = db.query(TABLE, COLUMNS, field + "='" + value + "'", null, null, null, adbManager._id);

		array = getArrayObjectFromCursor(cursor);
		db.close();
		help.close();
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<GcmNotify> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<GcmNotify> array = new ArrayList<GcmNotify>();
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
			GcmNotify t = new GcmNotify(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public GcmNotify getObjectFromCusor(Cursor cursor) {
		GcmNotify t = null;
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
			t = new GcmNotify(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}

}
