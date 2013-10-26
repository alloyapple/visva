package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbException;
import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.LinkPost;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class LinkMng extends adbManager{
	public static final String TAG = "Class: LinkMng";

	Context context;

	public static final String TABLE = "Link";

	public static final String link = "link";
	public static final String xml = "xml";
	public static final String time = "time";
	
	public static final String[] COLUMNS = { adbManager._id, link, xml,time };

	public LinkMng(Context context) {
		super(TABLE, COLUMNS);
		help = new SDataBaseHelper(context);// IMPORTANT!! must be called before
											// init()
		
		super.init(help); // IMPORTANT!!
	}

	@Override
	public ArrayList<LinkPost> select() throws SQLiteException {
		ArrayList<LinkPost> array = new ArrayList<LinkPost>();
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
	public LinkPost select(int id) throws SQLiteException {
		LinkPost object = null;
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
	public ArrayList<LinkPost> select(String field, String value)
			throws SQLiteException {
		ArrayList<LinkPost> array = new ArrayList<LinkPost>();
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
	public ArrayList<LinkPost> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<LinkPost> array = new ArrayList<LinkPost>();
		if (cursor.getCount() == 0) {
			Log.e(TAG, "cursor is null. Get data from network");
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
			LinkPost t = new LinkPost(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public LinkPost getObjectFromCusor(Cursor cursor) {
		LinkPost t = null;
		cursor.moveToFirst();

		if (cursor.getCount() == 0) {
			Log.e(TAG, "cursor is null. Get data from network");
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
			t = new LinkPost(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}
}