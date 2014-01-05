package vn.com.shoppie.database;

import java.util.ArrayList;

import vn.com.shoppie.util.log;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public abstract class adbManager {
	public String TAG = "Class: aDbManager";

	public static final String _id = "_id";

	public adbSqlHelper help;

	public String TABLE = null;
	public String[] COLUMNS = null;

	/**
	 * constructor Database Manager
	 * 
	 * @see init(Context, DataBaseHelper) must be called after constructor
	 * */
	public adbManager(String TABLE, String[] COLUMNS) {
		this.COLUMNS = COLUMNS;
		this.TABLE = TABLE;
		this.TAG = this.getClass().getSimpleName();
	}

	/**
	 * initial SqlHelper and Context. Must be called when constructor child
	 * class of aDbManager called
	 * */
	public void init(adbSqlHelper help) {
		// if (help != null)
		// try {
		// help.createDataBase();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * @see check initial for TABLE and COLUMNS
	 * @return true: success or false: failed
	 * */
	private boolean checkInit() {
		if (null == TABLE || TABLE.equals("")) {
			try {
				throw new Throwable("not init TABLE");
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return false;
		}
		if (null == COLUMNS || COLUMNS.equals("")) {
			try {
				throw new Throwable("not init COLUMN");
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return false;
		} else
			return true;
	}

	/**
	 * @return select * from test
	 * */
	public abstract ArrayList<?> select() throws SQLiteException;

	/**
	 * @return aObject class
	 * @see should override
	 * */
	public abstract adbObject select(int id) throws SQLiteException;

	/**
	 * WHERE field=value CLAUSE
	 * 
	 * @return aObject class
	 * @see should override
	 * */
	public abstract ArrayList<?> select(String field, String value)
			throws SQLiteException;

	/**
	 * @return size records of table
	 * */
	public int getLastIndex() throws SQLiteException {
		if (!checkInit())
			return -1;
		int id = -1;
		if (help.getReadableDatabase().isOpen()) {
			help.getReadableDatabase().close();
		}

		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cursor = db.query(TABLE, new String[] { _id }, null, null, null,
				null, _id);
		cursor.moveToFirst();
		if (cursor.getCount() == 0) {
			return 0;
		}
		do {
			id = cursor.getInt(cursor.getColumnIndex(_id));
		} while (cursor.moveToNext());
		cursor.close();
		db.close();
		return id;
	}

	/**
	 * @return rowID for insert. -1: error
	 * */
	public long insertTo(int id, String... values) throws SQLException {
		if (!checkInit())
			return -1;
		ContentValues content = new ContentValues();
		content.put("_id", id);
		for (int i = 0; i < values.length; ++i) {
			content.put(COLUMNS[i + 1], values[i]);
		}
		if (help.getWritableDatabase().isOpen())
			help.getWritableDatabase().close();
		SQLiteDatabase db = help.getWritableDatabase();
		long value = -1;
		try {
			value = db.insertOrThrow(TABLE, null, content);
		} catch (SQLiteConstraintException e) {
			return -1;
		} finally {
			db.close();
		}
		return value;
	}

	/**
	 * @return rowID for insert new record. -1: error
	 * */
	public long insertNewTo(String... values) throws SQLException {
		if (!checkInit())
			return -1;
		int id = getLastIndex() + 1;
		return insertTo(id, values);
	}

	/**
	 * @return number of record updated
	 * */
	public int edit(int id, String... values) throws SQLiteException {
		if (!checkInit())
			return -1;
		ContentValues content = new ContentValues();
		content.put("_id", id);
		for (int i = 0; i < values.length; ++i) {
			content.put(COLUMNS[i + 1], values[i]);
		}
		try {
			if (help.getWritableDatabase().isOpen()) {
				help.getWritableDatabase().close();
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			try {
				help.getWritableDatabase().close();
			} catch (SQLiteException e) {
			}
		}
		SQLiteDatabase db = null;
		try {
			db = help.getWritableDatabase();
			int value = db.update(TABLE, content, _id + "='" + id + "'", null);
			return value;
		} catch (SQLiteException e) {
			log.e("SQL Exception int edit", e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}
		return -1;
	}

	/**
	 * @see reindex() must be called after delete()
	 * @return number record deleted
	 * */
	public boolean delete(int id) throws SQLiteException {
		if (!checkInit())
			return false;

		if (help.getWritableDatabase().isOpen()) {
			help.getWritableDatabase().close();
		}
		SQLiteDatabase db = null;
		try {
			db = help.getWritableDatabase();
			int sum = db.delete(TABLE, _id + "='" + id + "'", null);
			boolean value = false;
			if (sum > 0)
				value = true;
			else
				value = false;
			return value;
		}
		// catch (SQLiteDatabaseLockedException e) {
		// return false;
		// }
		catch (SQLException e) {
			return false;
		} finally {
			if (db != null && db.isOpen())
				db.close();
		}

	}

	/**
	 * @return number record re-index
	 * */
	public int reindex() throws SQLiteException {
		if (!checkInit())
			return -1;
		int id = 0;
		if (help.getReadableDatabase().isOpen()) {
			help.getReadableDatabase().close();
		}
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cursor = db.query(TABLE, COLUMNS, null, null, null, null, _id);
		cursor.moveToFirst();
		do {
			int _index = cursor.getInt(cursor.getColumnIndex(_id));
			int size = COLUMNS.length - 1;
			String[] values = new String[size];
			for (int i = 0; i < size; ++i) {
				values[i] = cursor.getString(i + 1);
			}
			if (id == _index) {
				edit(_index, values);
			} else {
				delete(_index);
				insertTo(id, values);
			}
			id++;
		} while (cursor.moveToNext());
		cursor.close();
		db.close();
		return id;
	}

	public abstract <T> ArrayList<T> getArrayObjectFromCursor(Cursor cursor);

	public abstract adbObject getObjectFromCusor(Cursor cursor);

}
