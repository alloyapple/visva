package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.Campaign;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class CampaignMng extends adbManager {
	public static final String TAG = "Class: CampaignMng";

	Context context;

	public static final String TABLE = "Campaign";

	public static final String id = "id";
	public static final String merchId = "merchId";
	public static final String name = "name";
	public static final String desc = "desc";
	public static final String linkImage = "linkImage";
	public static final String campaignType = "campaignType";
	public static final String newStatus = "newStatus";

	public static final String[] COLUMNS = { adbManager._id, id, merchId, name, desc, linkImage, campaignType,newStatus };

	public CampaignMng(Context context) {
		super(TABLE, COLUMNS);
		help = new SDataBaseHelper(context);// IMPORTANT!! must be called before
											// init()
		super.init(help); // IMPORTANT!!
	}

	@Override
	public ArrayList<Campaign> select() throws SQLiteException {
		ArrayList<Campaign> array = new ArrayList<Campaign>();
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
	public Campaign select(int id) throws SQLiteException {
		Campaign object = null;
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
	public ArrayList<Campaign> select(String field, String value) throws SQLiteException {
		ArrayList<Campaign> array = new ArrayList<Campaign>();
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
	public ArrayList<Campaign> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<Campaign> array = new ArrayList<Campaign>();
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
			Campaign t = new Campaign(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public Campaign getObjectFromCusor(Cursor cursor) {
		Campaign t = null;
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
			t = new Campaign(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}

}
