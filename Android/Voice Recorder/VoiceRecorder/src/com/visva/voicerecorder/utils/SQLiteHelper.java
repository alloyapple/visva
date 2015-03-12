package com.visva.voicerecorder.utils;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.visva.voicerecorder.model.FavouriteItem;

public class SQLiteHelper extends SQLiteOpenHelper {

    // ========================Constant Define======================
    private static final String DATABASE_NAME    = "mycallrecorder.db";
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_FAVOURITE  = "favourite";

    // TABLE COLUMS NAME
    private static final String FAVOURITE_ID     = "_id";
    private static final String PHONE_NO         = "phone_no";
    private static final String PHONE_NAME       = "phone_name";

    /**
     * check contact is favourite or not.
     * If favourited, check it is favourited from contact app or my call recorder app
     * 
     * is_favourite  = 0 : no favourite contact
     * is_favourite  = 1 : favourite from my call recorder
     * is_favourite  = 2 : favourite contacct from contact app
     */

    private static final String IS_FAVOURITE     = "is_favourite";

    private static final String CONTACT_ID       = "contact_id";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* Create table story */
        String CREATE_STORY_ITEM_TABLE = "CREATE TABLE "
                + TABLE_FAVOURITE + "(" + FAVOURITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PHONE_NO + " TEXT," + PHONE_NAME
                + " TEXT," + IS_FAVOURITE + " INTEGER," + CONTACT_ID + " TEXT" + ")";

        db.execSQL(CREATE_STORY_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_FAVOURITE);
        onCreate(db);
    }

    /*-------------------------- ADD FUNCTION--------------------------*/
    // add new story item
    public void addNewRecordingSession(FavouriteItem favouriteItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(PHONE_NO, favouriteItem.phoneNo);
        mValues.put(PHONE_NAME, favouriteItem.phoneName);

        mValues.put(IS_FAVOURITE, favouriteItem.isFavourite);

        mValues.put(CONTACT_ID, favouriteItem.contactId);

        db.insert(TABLE_FAVOURITE, null, mValues);
        db.close();
    }

    //update number like count to a storyItem
    public void updateFavouriteContact(FavouriteItem favouriteItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(PHONE_NO, favouriteItem.phoneNo);
        mValues.put(PHONE_NAME, favouriteItem.phoneName);

        mValues.put(IS_FAVOURITE, favouriteItem.isFavourite);
        mValues.put(CONTACT_ID, favouriteItem.contactId);

        db.update(TABLE_FAVOURITE, mValues, CONTACT_ID + " = ?",
                new String[] { String.valueOf(favouriteItem.contactId) });
        db.close();
    }

    // get a recording session
    public FavouriteItem getFavouriteItem(String contactID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, null, CONTACT_ID + " = ?", new String[] { String.valueOf(contactID) }, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            FavouriteItem favouriteItem = new FavouriteItem(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                    cursor.getString(4));
            return favouriteItem;
        }
        if(cursor != null){
            cursor.close();
            cursor = null;
        }
        db.close();
        return null;
    }

    // get all story item
    public ArrayList<FavouriteItem> getAllFavouriteItemFromContactApp() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<FavouriteItem> storyItemList = new ArrayList<FavouriteItem>();
        Cursor cursor = db.query(TABLE_FAVOURITE, null, IS_FAVOURITE + " = ?", new String[] { String.valueOf(2) }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FavouriteItem favouriteItem = new FavouriteItem(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                        cursor.getString(4));
                storyItemList.add(favouriteItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return storyItemList;
    }

    public ArrayList<FavouriteItem> getAllFavouriteItem() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<FavouriteItem> storyItemList = new ArrayList<FavouriteItem>();
        String sql = "Select * from " + TABLE_FAVOURITE;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FavouriteItem favouriteItem = new FavouriteItem(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                        cursor.getString(4));
                storyItemList.add(favouriteItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return storyItemList;
    }

    // delete a story item
    public void deleteFavouriteItem(FavouriteItem favouriteItem) {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_FAVOURITE, CONTACT_ID + " = ?", new String[] { String.valueOf(favouriteItem.contactId) });
        mdb.close();
    }

    public void deleteAllFavouriteItem() {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_FAVOURITE, null, null);
        mdb.close();
    }

    //count all story item 
    public int countAllFavouriteItem() {
        int count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, null, null, null, null, null, null);
        // return count
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

}
