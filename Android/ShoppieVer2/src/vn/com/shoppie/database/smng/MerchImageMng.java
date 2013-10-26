package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.MerchImage;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class MerchImageMng extends adbManager{
public static final String TAG="Class: MerchImageMng";
	
	Context context;

	public static final String TABLE = "MerchImage";

	public static final String merchId = "merchId";
	public static final String imageId = "imageId";
	public static final String imageDesc = "imageDesc";
	public static final String image = "image";

	public static final String[] COLUMNS = { adbManager._id, merchId,
		imageId, imageDesc, image};
	
	public MerchImageMng(Context context) {
		super(TABLE, COLUMNS);
		help=new SDataBaseHelper(context);//IMPORTANT!! must be called before init()
		super.init(help);	//IMPORTANT!!
	}

	@Override
	public ArrayList<MerchImage> select() throws SQLiteException {
		ArrayList<MerchImage> array = new ArrayList<MerchImage>();
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
	public MerchImage select(int id) throws SQLiteException {
		MerchImage object = null;
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
	public ArrayList<MerchImage> select(String field, String value)
			throws SQLiteException {
		ArrayList<MerchImage> array = new ArrayList<MerchImage>();
		if(help.getReadableDatabase().isOpen())
			help.getReadableDatabase().close();
		SQLiteDatabase db = help.getReadableDatabase();
		for(String tmp:COLUMNS)
			if(tmp.equals(field)){
				break;
			}else{
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
	public ArrayList<MerchImage> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<MerchImage> array = new ArrayList<MerchImage>();
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
			MerchImage t = new MerchImage(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public MerchImage getObjectFromCusor(Cursor cursor) {
		MerchImage t=null;
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
			t = new MerchImage(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}

}
