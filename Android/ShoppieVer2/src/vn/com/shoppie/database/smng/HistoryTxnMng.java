package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.HistoryTxn;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class HistoryTxnMng extends adbManager{
public static final String TAG="Class: HistoryTxnMng";
	
	Context context;

	public static final String TABLE = "HistoryTxn";

	public static final String txnId = "txnId";
	public static final String merchId = "merchId";
	public static final String txnType = "txnType";
	public static final String txnAmt = "txnAmt";
	public static final String pieQty = "pieQty";
	public static final String billCode = "billCode";
	public static final String txnDate = "txnDate";
	public static final String merchName = "merchName";
	public static final String storeName = "storeName";

	public static final String[] COLUMNS = { adbManager._id, txnId,
		merchId, txnType, txnAmt, pieQty,billCode,txnDate,merchName,storeName};
	
	public HistoryTxnMng(Context context) {
		super(TABLE, COLUMNS);
		help=new SDataBaseHelper(context);//IMPORTANT!! must be called before init()
		super.init(help);	//IMPORTANT!!
	}

	@Override
	public ArrayList<HistoryTxn> select() throws SQLiteException {
		ArrayList<HistoryTxn> array = new ArrayList<HistoryTxn>();
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
	public HistoryTxn select(int id) throws SQLiteException {
		HistoryTxn object = null;
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
	public ArrayList<HistoryTxn> select(String field, String value)
			throws SQLiteException {
		ArrayList<HistoryTxn> array = new ArrayList<HistoryTxn>();
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
	public ArrayList<HistoryTxn> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<HistoryTxn> array = new ArrayList<HistoryTxn>();
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
			HistoryTxn t = new HistoryTxn(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public HistoryTxn getObjectFromCusor(Cursor cursor) {
		HistoryTxn t=null;
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
			t = new HistoryTxn(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}

}
