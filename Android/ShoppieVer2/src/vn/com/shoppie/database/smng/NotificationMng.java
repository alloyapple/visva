package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.Notification;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class NotificationMng extends adbManager{
	public static final String TAG = "Class: NotificationMng";

	Context context;

	public static final String TABLE = "Notification";

	public static final String notificationId = "notificationId";
	public static final String merchId = "merchId";
	public static final String storeId = "storeId";
	public static final String content = "content";
	public static final String status = "status";
	public static final String regdate = "regdate";
	
	public static final String[] COLUMNS = { adbManager._id, notificationId, merchId, storeId,
		content, status,regdate };

	public NotificationMng(Context context) {
		super(TABLE, COLUMNS);
		help = new SDataBaseHelper(context);// IMPORTANT!! must be called before
											// init()
		super.init(help); // IMPORTANT!!
	}

	@Override
	public ArrayList<Notification> select() throws SQLiteException {
		ArrayList<Notification> array = new ArrayList<Notification>();
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
	public Notification select(int id) throws SQLiteException {
		Notification object = null;
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
	public ArrayList<Notification> select(String field, String value)
			throws SQLiteException {
		ArrayList<Notification> array = new ArrayList<Notification>();
		if(help.getReadableDatabase().isOpen())
			help.getReadableDatabase().close();
		SQLiteDatabase db = help.getReadableDatabase();
		for (String tmp : COLUMNS)
			if (tmp.equals(field)) {
				break;
			} else {
				Log.e(TAG, "not matching field to SELECT");
				return array;
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
	public ArrayList<Notification> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<Notification> array = new ArrayList<Notification>();
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
			Notification t = new Notification(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public Notification getObjectFromCusor(Cursor cursor) {
		Notification t = null;
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
			t = new Notification(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}
}
