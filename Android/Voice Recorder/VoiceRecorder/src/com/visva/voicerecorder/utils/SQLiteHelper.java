package com.visva.voicerecorder.utils;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.record.RecordingSession;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME    = "mycallrecorder.db";
    private static final int    DATABASE_VERSION = 1;

    // ========================Table Favorite======================
    private static final String TABLE_FAVOURITE  = "favourite";
    // TABLE COLUMS NAME
    private static final String FAVOURITE_ID     = "_id";
    /**
     * this column is also in the record table
     */
    private static final String PHONE_NO         = "phone_no";
    private static final String PHONE_NAME       = "phone_name";

    /**
     * check contact is favorite or not.
     * If favorite, check it is favorite from contact app or my call recorder app
     * 
     * is_favourite  = 0 : no favorite contact
     * is_favourite  = 1 : favorite from my call recorder
     * is_favourite  = 2 : favorite contact from contact app
     */

    private static final String IS_FAVORITED     = "is_favourite";
    private static final String CONTACT_ID       = "contact_id";

    //=========================Table record=======================
    private static final String TABLE_RECORD     = "record";
    // TABLE COLUMS NAME
    private static final String RECORD_ID        = "_id";

    /**
     * the phone_no is the 2rd column of table record
     */
    private static final String CALL_STATE       = "call_state";
    private static final String FILE_NAME        = "file_name";
    /**
     * the phone_name is the 4th column of table record
     */
    /**
     * the is_favorite is the 5th column of table record
     */
    private static final String DATE_CREATED     = "date_created";

    private static final String DURATION         = "duration";

    /****************************Value constant*******************************/
    private static final int    _RECORD_ID       = 0;
    private static final int    _PHONE_NO        = _RECORD_ID + 1;
    private static final int    _CALL_STATE      = _RECORD_ID + 2;
    private static final int    _FILE_NAME       = _RECORD_ID + 3;
    private static final int    _PHONE_NAME      = _RECORD_ID + 4;
    private static final int    _IS_FAVORITED    = _RECORD_ID + 5;
    private static final int    _DATE_CREATED    = _RECORD_ID + 6;
    private static final int    _DURATION        = _DATE_CREATED + 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* Create table favorite */
        String CREATE_FAVORITE_TABLE = "CREATE TABLE " + TABLE_FAVOURITE + "(" + FAVOURITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PHONE_NO
                + " TEXT," + PHONE_NAME + " TEXT," + IS_FAVORITED + " INTEGER," + CONTACT_ID + " TEXT" + ")";
        /* Create table record */
        String CREATE_RECORD_TABLE = "CREATE TABLE " + TABLE_RECORD + "(" + RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PHONE_NO
                + " TEXT," + CALL_STATE + " INTEGER," + FILE_NAME + " TEXT," + PHONE_NAME + " TEXT," + IS_FAVORITED + " INTEGER," + DATE_CREATED
                + " TEXT," + DURATION + " TEXT" + ")";

        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists " + TABLE_FAVOURITE);
        //db.execSQL("drop table if exists " + TABLE_FAVOURITE);
        // onCreate(db);
    }

    /*-------------------------- FAVORITE TABLE FUNCTION--------------------------*/
    // add new favorite item
    public void addNewFavoriteItem(FavouriteItem favouriteItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(PHONE_NO, favouriteItem.phoneNo);
        mValues.put(PHONE_NAME, favouriteItem.phoneName);

        mValues.put(IS_FAVORITED, favouriteItem.isFavourite);

        mValues.put(CONTACT_ID, favouriteItem.contactId);

        db.insert(TABLE_FAVOURITE, null, mValues);
        db.close();
    }

    //update number like count to a favorite item
    public void updateFavouriteContact(FavouriteItem favouriteItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(PHONE_NO, favouriteItem.phoneNo);
        mValues.put(PHONE_NAME, favouriteItem.phoneName);

        mValues.put(IS_FAVORITED, favouriteItem.isFavourite);
        mValues.put(CONTACT_ID, favouriteItem.contactId);

        db.update(TABLE_FAVOURITE, mValues, CONTACT_ID + " = ?",
                new String[] { String.valueOf(favouriteItem.contactId) });
        db.close();
    }

    // get a favorite item
    public FavouriteItem getFavouriteItemByContactId(String contactID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, null, CONTACT_ID + " = ?", new String[] { String.valueOf(contactID) }, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            FavouriteItem favouriteItem = new FavouriteItem(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                    cursor.getString(4));
            return favouriteItem;
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        db.close();
        return null;
    }

    // get a favorite item
    public FavouriteItem getFavouriteItemFromPhoneNo(String phoneNo) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, null, PHONE_NO + " = ?", new String[] { String.valueOf(phoneNo) }, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            FavouriteItem favouriteItem = new FavouriteItem(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                    cursor.getString(4));
            return favouriteItem;
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        db.close();
        return null;
    }

    // get all favorite item
    public ArrayList<FavouriteItem> getAllFavouriteItemFromContactApp() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<FavouriteItem> storyItemList = new ArrayList<FavouriteItem>();
        Cursor cursor = db.query(TABLE_FAVOURITE, null, IS_FAVORITED + " = ?", new String[] { String.valueOf(2) }, null, null, null);
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
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    FavouriteItem favouriteItem = new FavouriteItem(cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)),
                            cursor.getString(4));
                    storyItemList.add(favouriteItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        db.close();
        return storyItemList;
    }

    // delete a favorite item
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

    //count all favorite item 
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

    /*-------------------------- RECORD TABLE FUNCTION--------------------------*/
    // add new record row
    public void addNewRecordingSession(RecordingSession session) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();

        mValues.put(PHONE_NO, session.phoneNo);
        mValues.put(CALL_STATE, session.callState);
        mValues.put(FILE_NAME, session.fileName);
        mValues.put(PHONE_NAME, session.phoneName);
        mValues.put(IS_FAVORITED, session.isFavourite);
        mValues.put(DATE_CREATED, session.dateCreated);
        mValues.put(DURATION, session.duration);

        db.insert(TABLE_RECORD, null, mValues);
        db.close();
    }

    //update to a record row
    public void updateARecordItem(RecordingSession session) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();

        mValues.put(PHONE_NO, session.phoneNo);
        mValues.put(CALL_STATE, session.callState);
        mValues.put(FILE_NAME, session.fileName);
        mValues.put(PHONE_NAME, session.phoneName);
        mValues.put(IS_FAVORITED, session.isFavourite);
        mValues.put(DATE_CREATED, session.dateCreated);
        mValues.put(DURATION, session.duration);

        db.update(TABLE_RECORD, mValues, DATE_CREATED + " = ?", new String[] { String.valueOf(session.dateCreated) });
        db.close();
    }

    // get a record item by date created
    public RecordingSession getARecordItem(String dateCreated) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECORD, null, DATE_CREATED + " = ?", new String[] { String.valueOf(dateCreated) }, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            RecordingSession session = new RecordingSession(cursor.getString(_PHONE_NO), Integer.parseInt(cursor.getString(_CALL_STATE)),
                    cursor.getString(_FILE_NAME), cursor.getString(_PHONE_NAME), Integer.parseInt(cursor.getString(_IS_FAVORITED)),
                    cursor.getString(_DATE_CREATED), cursor.getString(_DURATION));
            return session;
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        db.close();
        return null;
    }

    public ArrayList<RecordingSession> getAllRecordItem() {
        SQLiteDatabase db = getReadableDatabase();
        String project[]={RECORD_ID,PHONE_NO,CALL_STATE,FILE_NAME,PHONE_NAME,IS_FAVORITED,DATE_CREATED,DURATION};
        db.query(TABLE_RECORD, project, null, null, null, null, DURATION+" DESC");
        ArrayList<RecordingSession> recordingSessions = new ArrayList<RecordingSession>();
        String sql = "Select * from " + TABLE_RECORD;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    RecordingSession session = new RecordingSession(cursor.getString(_PHONE_NO), Integer.parseInt(cursor.getString(_CALL_STATE)),
                            cursor.getString(_FILE_NAME), cursor.getString(_PHONE_NAME), Integer.parseInt(cursor.getString(_IS_FAVORITED)),
                            cursor.getString(_DATE_CREATED), cursor.getString(_DURATION));
                    recordingSessions.add(session);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        db.close();
        return recordingSessions;
    }

    // delete a favorite item by date created
    public void deleteARecordItem(RecordingSession session) {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_RECORD, DATE_CREATED + " = ?", new String[] { String.valueOf(session.dateCreated) });
        mdb.close();
    }

    public void deleteAllRecordItem() {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_RECORD, null, null);
        mdb.close();
    }

    //count all favorite item 
    public int countAllRecordItem() {
        int count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECORD, null, null, null, null, null, null);
        // return count
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
