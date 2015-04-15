package com.visva.android.visvasdklibrary.remind;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.visva.voicerecorder.log.AIOLog;
/**
 * 
 * @author kane
 *
 */
public class ReminderDBHelper extends SQLiteOpenHelper {

    private final String DB_PRIMARY_KEY_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    private final String DB_INTEGER_TYPE = "INTEGER";
    private final String DB_NAME_TYPE = "VARCHAR(40)";
    private final String DB_BYTE_TYPE = "BLOB";

    public static final String DATABASE_NAME = "reminder.db";
    public static final String TABLE_NAME = "reminder_items";
    public static final int DATABASE_VERSION = 1;

    public static interface JobsColumns {

        final String _ID = "_id";
        final String CLASS_NAME = "class_name";
        final String CUSTOM_ID = "custom_id";
        final String TRIGGERTIME = "triggerAtMillis";
        final String INTERVALTIME = "intervalMillis";
        final String REPEATCOUNT = "repeatCount";
        final String REPEATMODE = "repeatMode";
        final String RAWDATA = "rawdata";

    }

    //for singleton
    private volatile static ReminderDBHelper mInstance = null;

    public static synchronized ReminderDBHelper getInstance(Context context) {

        if (mInstance == null) {
            synchronized (ReminderDBHelper.class) {
                if (mInstance == null) {
                    mInstance = new ReminderDBHelper(context);
                }
            }
        }
        return mInstance;

    }

    private ReminderDBHelper(Context context) {
        super(context, ReminderDBHelper.DATABASE_NAME, null, ReminderDBHelper.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {

        db.beginTransaction();

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ReminderDBHelper.TABLE_NAME

        + " (" + ReminderDBHelper.JobsColumns._ID + " " + DB_PRIMARY_KEY_TYPE + ","

        + ReminderDBHelper.JobsColumns.CLASS_NAME + " " + DB_NAME_TYPE + ","

        + ReminderDBHelper.JobsColumns.CUSTOM_ID + " " + DB_NAME_TYPE + ","

        + ReminderDBHelper.JobsColumns.TRIGGERTIME + " " + DB_NAME_TYPE + ","

        + ReminderDBHelper.JobsColumns.INTERVALTIME + " " + DB_NAME_TYPE + ","

        + ReminderDBHelper.JobsColumns.REPEATCOUNT + " " + DB_INTEGER_TYPE + ","

        + ReminderDBHelper.JobsColumns.REPEATMODE + " " + DB_INTEGER_TYPE + ","

        + ReminderDBHelper.JobsColumns.RAWDATA + " " + DB_BYTE_TYPE + ");");

        AIOLog.d(AIOConstant.TAG, "DB TABLE(" + ReminderDBHelper.TABLE_NAME + ") Has been created.");

        db.setTransactionSuccessful();

        db.endTransaction();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AIOLog.d(AIOConstant.TAG,"Upgrading database from version " + oldVersion + " to " + newVersion);
        upgradeTables(db);
    }

    private void upgradeTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ReminderDBHelper.TABLE_NAME);
        createTables(db);
    }

    public List<ReminderItem> getAllReminders(){
        ArrayList<ReminderItem> alarmJobList = new ArrayList<ReminderItem>();

        ReminderItem alarmJob = null;
        SQLiteDatabase db = null;
        synchronized (this) {
            db = this.getWritableDatabase();

            try {
                Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
                if (cursor != null) {
                    while(cursor.moveToNext()) {
                        alarmJob = createAlarmJobFromCursor(cursor);
                        if (alarmJob != null) {
                            alarmJobList.add(alarmJob);
                        }
                    }
                    cursor.close();
                }
                else {
                    AIOLog.d(AIOConstant.TAG,"[getAlarmJobs]: Cursor is null.");
                }
            }catch(SQLiteException e) {
                e.printStackTrace();
            }
        }

        return alarmJobList;
    }

    public long addJob(ReminderItem alarmJob){
        return addJob(alarmJob.className, alarmJob.id, alarmJob.triggerAtMillis, alarmJob.intervalMillis, alarmJob.repeatCount, alarmJob.isRepeatMode);
    }

    public long addJob(String className, String id, long triggerAtMillis, long intervalMillis, int repeatCount, boolean repeatMode){

        long rowId = -1;

        if(deleteJob(className, id) > 0){
            AIOLog.d(AIOConstant.TAG,"addJob: delete a jobs");
        }

        rowId = insert(className, id, triggerAtMillis, intervalMillis, repeatCount, repeatMode);

        if(rowId < 0){
            AIOLog.e(AIOConstant.TAG,"Fail to insert a job for schdule");
            return rowId;
        }else
            return rowId;

    }

    private long insert(String className, String id, long triggerAtMillis, long intervalMillis, int repeatCount, boolean repeatMode) {

        ContentValues values = new ContentValues();
        values.put(ReminderDBHelper.JobsColumns.CLASS_NAME, className);
        if(id != null)
            values.put(ReminderDBHelper.JobsColumns.CUSTOM_ID, id);
        values.put(ReminderDBHelper.JobsColumns.TRIGGERTIME, triggerAtMillis);
        values.put(ReminderDBHelper.JobsColumns.INTERVALTIME, intervalMillis);
        values.put(ReminderDBHelper.JobsColumns.REPEATCOUNT, repeatCount);
        values.put(ReminderDBHelper.JobsColumns.REPEATMODE, repeatMode);


        long rowId = -1;
        synchronized (this) {
            SQLiteDatabase db = null;

            try {
                db = this.getWritableDatabase();
                rowId = db.insert(TABLE_NAME, null, values);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
        return rowId;
    }

    public int deleteJob(String className, String id){

        int result = -1;

        String where = JobsColumns.CLASS_NAME + "='" + className +"'";
        if(id != null)
            where += " AND " + JobsColumns.CUSTOM_ID + "='" + id +"'";

        synchronized (this) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                result = db.delete(TABLE_NAME, where, null);
            }catch (SQLiteException e) {
                e.printStackTrace();
                assert false;
            }
        }
        return result;

    }

    public int deleteJob(long rowId){

        int result = -1;

        String where = JobsColumns._ID + "=" + rowId;

        synchronized (this) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                result = db.delete(TABLE_NAME, where, null);
            }catch (SQLiteException e) {
                e.printStackTrace();
                assert false;
            }
        }
        return result;

    }

    private ReminderItem createAlarmJobFromCursor(Cursor cursor){
        ReminderItem alarmJob = null;
        if (cursor != null) {
            String className = cursor.getString(cursor.getColumnIndex(ReminderDBHelper.JobsColumns.CLASS_NAME));
            String id = cursor.getString(cursor.getColumnIndex(ReminderDBHelper.JobsColumns.CUSTOM_ID));
            long triggerAtMillis = cursor.getLong(cursor.getColumnIndex(ReminderDBHelper.JobsColumns.TRIGGERTIME));
            long intervalMillis = cursor.getLong(cursor.getColumnIndex(ReminderDBHelper.JobsColumns.INTERVALTIME));
            int repeatCount = cursor.getInt(cursor.getColumnIndex(ReminderDBHelper.JobsColumns.REPEATCOUNT));
            int repeatMode = cursor.getInt(cursor.getColumnIndex(ReminderDBHelper.JobsColumns.REPEATMODE));
            boolean bRepeatMode = (repeatMode > 0)? true:false;

            alarmJob = new ReminderItem(className, id, triggerAtMillis, intervalMillis, repeatCount, bRepeatMode);
        }
        return alarmJob;
    }

    public void deleteAllJobs()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

}
