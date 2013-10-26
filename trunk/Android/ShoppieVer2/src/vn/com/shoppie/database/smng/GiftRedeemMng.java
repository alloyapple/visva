package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.GiftRedeem;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class GiftRedeemMng extends adbManager{
public static final String TAG="Class: GiftRedeemMng";
	
	Context context;

	public static final String TABLE = "GiftRedeem";

	public static final String txnId = "txnId";
	public static final String merchName = "merchName";
	public static final String storeAddress = "storeAddress";
	public static final String giftName = "giftName";
	public static final String pieQty = "pieQty";
	public static final String time = "time";
	public static final String giftImage = "giftImage";
	public static final String merchId="merchId";
	
	public static final String redeemQty="redeemQty";
	public static final String giftId="giftId";
	public static final String storeName="storeName";
	public static final String storeId="storeId";
	public static final String status="status";
	
	public static final String[] COLUMNS = { adbManager._id, txnId,
		merchName, storeAddress, giftName, pieQty,time,giftImage,merchId,redeemQty,giftId,storeName,storeId,status};
	
	public GiftRedeemMng(Context context) {
		super(TABLE, COLUMNS);
		help=new SDataBaseHelper(context);//IMPORTANT!! must be called before init()
		super.init(help);	//IMPORTANT!!
	}

	@Override
	public ArrayList<GiftRedeem> select() throws SQLiteException {
		ArrayList<GiftRedeem> array = new ArrayList<GiftRedeem>();
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
	public GiftRedeem select(int id) throws SQLiteException {
		GiftRedeem object = null;
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
	public ArrayList<GiftRedeem> select(String field, String value)
			throws SQLiteException {
		ArrayList<GiftRedeem> array = new ArrayList<GiftRedeem>();
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
	public ArrayList<GiftRedeem> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<GiftRedeem> array = new ArrayList<GiftRedeem>();
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
			GiftRedeem t = new GiftRedeem(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public GiftRedeem getObjectFromCusor(Cursor cursor) {
		GiftRedeem t=null;
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
			t = new GiftRedeem(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}

}
