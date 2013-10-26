package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.CustBalance;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class CustBalanceMng extends adbManager{
public static final String TAG="Class: CustBalance";
	
	Context context;

	public static final String TABLE = "CustBalance";

	public static final String creditBal = "creditBal";
	public static final String debitBal = "debitBal";
	public static final String currentBal = "currentBal";

	public static final String[] COLUMNS = { adbManager._id, creditBal,
		debitBal, currentBal};
	
	public CustBalanceMng(Context context) {
		super(TABLE, COLUMNS);
		help=new SDataBaseHelper(context);//IMPORTANT!! must be called before init()
		super.init(help);	//IMPORTANT!!
	}

	@Override
	public ArrayList<CustBalance> select() throws SQLiteException {
		ArrayList<CustBalance> array = new ArrayList<CustBalance>();
		if(help.getReadableDatabase().isOpen())
			help.getReadableDatabase().close();
		SQLiteDatabase db = help.getReadableDatabase();
		Cursor cursor = db.query(TABLE, COLUMNS, null, null, null, null,
				adbManager._id);
		array=getArrayObjectFromCursor(cursor);
		db.close();
		help.close();
		return array;
	}

	@Override
	public CustBalance select(int id) throws SQLiteException {
		CustBalance object = null;
		if (id >= 0) {
			if(help.getReadableDatabase().isOpen())
				help.getReadableDatabase().close();
			SQLiteDatabase db = help.getReadableDatabase();
			Cursor cursor = db.query(TABLE, COLUMNS, adbManager._id + "='" + id
					+ "'", null, null, null, adbManager._id);
			cursor.moveToFirst();
			
			object=getObjectFromCusor(cursor);
			db.close();
			help.close();
		}
		return object;
	}

	@Override
	public ArrayList<CustBalance> select(String field, String value)
			throws SQLiteException {
		ArrayList<CustBalance> array = new ArrayList<CustBalance>();
		if(help.getReadableDatabase().isOpen())
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
		Cursor cursor = db.query(TABLE, COLUMNS, field + "='" + value
				+ "'", null, null, null, adbManager._id);
		
		array=getArrayObjectFromCursor(cursor);
		db.close();
		help.close();
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<CustBalance> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<CustBalance> array = new ArrayList<CustBalance>();
		if(cursor.getCount()==0){
			Log.e(TAG, "cursor is null");
			cursor.close();
			return array;
		}
		
		cursor.moveToFirst();
		do {
			int _id = cursor.getInt(cursor.getColumnIndex(adbManager._id));
			int size = COLUMNS.length-1;
			String[] values = new String[size];
			for (int i = 0; i < size; ++i) {
				values[i] = cursor.getString(i + 1);
			}
			CustBalance t = new CustBalance(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public CustBalance getObjectFromCusor(Cursor cursor) {
		CustBalance t=null;
		cursor.moveToFirst();
		
		if(cursor.getCount()==0){
			Log.e(TAG, "cursor is null");
			cursor.close();
			return t;
		}
		do {
			int _id = cursor.getInt(cursor.getColumnIndex(adbManager._id));
			int size = COLUMNS.length-1;
			String[] values = new String[size];
			for (int i = 0; i < size; ++i) {
				values[i] = cursor.getString(i + 1);
			}
			t = new CustBalance(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}

}
