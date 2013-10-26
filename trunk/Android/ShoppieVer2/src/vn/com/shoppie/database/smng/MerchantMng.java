package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.Merchant;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class MerchantMng extends adbManager {
	public static final String TAG = "Class: MerchantMng";

	Context context;

	public static final String TABLE = "Merchant";

	public static final String merchId = "merchId";
	public static final String merchName = "merchName";
	public static final String merchCatId = "merchCatId";
	public static final String merchImage = "merchImage";
	public static final String merchBanner = "merchBanner";
	public static final String merchDescription = "merchDescription";

	public static final String[] COLUMNS = { adbManager._id, merchId, merchName, merchCatId, merchImage, merchBanner, merchDescription };

	public MerchantMng(Context context) {
		super(TABLE, COLUMNS);
		help = new SDataBaseHelper(context);// IMPORTANT!! must be called before
											// init()
		super.init(help); // IMPORTANT!!
	}

	@Override
	public ArrayList<Merchant> select() throws SQLiteException {
		ArrayList<Merchant> array = new ArrayList<Merchant>();
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
	public Merchant select(int id) throws SQLiteException {
		Merchant object = null;
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
	public ArrayList<Merchant> select(String field, String value) throws SQLiteException {
		ArrayList<Merchant> array = new ArrayList<Merchant>();
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
	public ArrayList<Merchant> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<Merchant> array = new ArrayList<Merchant>();
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
			Merchant t = new Merchant(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public Merchant getObjectFromCusor(Cursor cursor) {
		Merchant t = null;
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
			t = new Merchant(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}
}