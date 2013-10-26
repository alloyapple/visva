package vn.com.shoppie.database.smng;

import java.util.ArrayList;

import vn.com.shoppie.database.adbManager;
import vn.com.shoppie.database.sobject.CampaignImage;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class CampaignImageMng extends adbManager{
public static final String TAG="Class: CampaignImageMng";
	
	Context context;

	public static final String TABLE = "CampaignImage";

	public static final String campaignId = "campaignId";
	public static final String imageName = "imageName";
	public static final String imagePath = "imagePath";
	public static final String imageDesc = "imageDesc";
	public static final String price = "price";

	public static final String[] COLUMNS = { adbManager._id, campaignId,
		imageName, imagePath, imageDesc, price};
	
	public CampaignImageMng(Context context) {
		super(TABLE, COLUMNS);
		help=new SDataBaseHelper(context);//IMPORTANT!! must be called before init()
		super.init(help);	//IMPORTANT!!
	}

	@Override
	public ArrayList<CampaignImage> select() throws SQLiteException {
		ArrayList<CampaignImage> array = new ArrayList<CampaignImage>();
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
	public CampaignImage select(int id) throws SQLiteException {
		CampaignImage object = null;
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
	public ArrayList<CampaignImage> select(String field, String value)
			throws SQLiteException {
		ArrayList<CampaignImage> array = new ArrayList<CampaignImage>();
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
	public ArrayList<CampaignImage> getArrayObjectFromCursor(Cursor cursor) {
		ArrayList<CampaignImage> array = new ArrayList<CampaignImage>();
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
			CampaignImage t = new CampaignImage(_id, values);
			array.add(t);
		} while (cursor.moveToNext());
		cursor.close();
		return array;
	}

	@Override
	public CampaignImage getObjectFromCusor(Cursor cursor) {
		CampaignImage t=null;
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
			t = new CampaignImage(_id, values);
		} while (cursor.moveToNext());
		cursor.close();
		return t;
	}

}
