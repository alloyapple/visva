package com.visva.android.socialstackwidget.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SocialStacksDBProvider extends SQLiteOpenHelper {

    // ========================Constant Define======================
    private static final String DATABASE_NAME = "socialstacks.db";
    private static final int DATABASE_VERSION = 1;

    // STORY TABLE NAME
    private static final String TABLE_STORY_ITEM = "social_item";
    // STORY TABLE COLUMS NAME
    private static final String STORY_ITEM_ID = "_id";
    private static final String SOCIAL_FEED_ID = "social_feed_id";
    private static final String SOCIAL_CATEGORY = "social_category";

    private static final String IMAGE_URL = "image_url";
    private static final String IMAGE_HEIGHT = "image_height";
    private static final String IMAGE_WIDTH = "image_width";
    private static final String AUTHOR_NAME = "author_name";
    private static final String AUTHOR_IMAGE = "author_image";
    private static final String AUTHOR_IMAGE_WIDTH = "author_image_width";
    private static final String AUTHOR_IMAGE_HEIGHT = "author_image_height";
    private static final String TIME_STAMP = "time_stamp";
    private static final String LINK_URL = "link_url";

    private static final String FEEDS_TYPE = "feeds_type";
    private static final String MESSAGE = "message";
    private static final String BODY = "body";
    private static final String WEB_LINK = "web_link";

    public SocialStacksDBProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STORY_ITEM_TABLE = "CREATE TABLE " + TABLE_STORY_ITEM + "(" + STORY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SOCIAL_FEED_ID + " TEXT," + SOCIAL_CATEGORY + " TEXT," + IMAGE_URL + " TEXT," + IMAGE_HEIGHT + " INTEGER,"
                + IMAGE_WIDTH + " INTEGER," + AUTHOR_NAME + " TEXT," + AUTHOR_IMAGE + " TEXT," + AUTHOR_IMAGE_WIDTH + " INTEGER," + AUTHOR_IMAGE_HEIGHT + " INTEGER," + TIME_STAMP + " TEXT," + FEEDS_TYPE + " TEXT," + MESSAGE + " TEXT,"
                + LINK_URL + " TEXT," + BODY + " TEXT," + WEB_LINK + " TEXT" + ")";
        db.execSQL(CREATE_STORY_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_STORY_ITEM);
        onCreate(db);
    }

    /*-------------------------- ADD FUNCTION--------------------------*/
    // add new story item
    public void addNewStoryItem(SocialWidgetItem storyItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(SOCIAL_FEED_ID, storyItem.getSocialFeedId());
        mValues.put(SOCIAL_CATEGORY, storyItem.getSocial_category());
        mValues.put(IMAGE_URL, storyItem.getImage_Url());
        mValues.put(IMAGE_HEIGHT, storyItem.getImage_height());
        mValues.put(IMAGE_WIDTH, storyItem.getImage_width());
        mValues.put(AUTHOR_NAME, storyItem.getAuthor_name());
        mValues.put(AUTHOR_IMAGE, storyItem.getAuthor_image());
        mValues.put(AUTHOR_IMAGE_WIDTH, storyItem.getAuthor_image_width());
        mValues.put(AUTHOR_IMAGE_HEIGHT, storyItem.getAuthor_image_height());
        mValues.put(TIME_STAMP, storyItem.getTime_stamp());
        mValues.put(FEEDS_TYPE, storyItem.getFeeds_type());
        mValues.put(MESSAGE, storyItem.getMessage());
        mValues.put(LINK_URL, storyItem.getLink_url());
        mValues.put(BODY, storyItem.body);
        mValues.put(WEB_LINK, storyItem.webLink);

        db.insert(TABLE_STORY_ITEM, null, mValues);
        db.close();
    }

    // update story item
    public void updateAStoryItem(SocialWidgetItem storyItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(SOCIAL_FEED_ID, storyItem.getSocialFeedId());
        mValues.put(SOCIAL_CATEGORY, storyItem.getSocial_category());
        mValues.put(IMAGE_URL, storyItem.getImage_Url());
        mValues.put(IMAGE_HEIGHT, storyItem.getImage_height());
        mValues.put(IMAGE_WIDTH, storyItem.getImage_width());
        mValues.put(AUTHOR_NAME, storyItem.getAuthor_name());
        mValues.put(AUTHOR_IMAGE, storyItem.getAuthor_image());
        mValues.put(AUTHOR_IMAGE_WIDTH, storyItem.getAuthor_image_width());
        mValues.put(AUTHOR_IMAGE_HEIGHT, storyItem.getAuthor_image_height());
        mValues.put(TIME_STAMP, storyItem.getTime_stamp());
        mValues.put(FEEDS_TYPE, storyItem.getFeeds_type());
        mValues.put(MESSAGE, storyItem.getMessage());
        mValues.put(LINK_URL, storyItem.getLink_url());
        mValues.put(BODY, storyItem.body);
        mValues.put(WEB_LINK, storyItem.webLink);

        db.update(TABLE_STORY_ITEM, mValues, STORY_ITEM_ID + " = ?",
                new String[] { String.valueOf(storyItem.getId()) });
        db.close();
    }

    // get a story item
    public SocialWidgetItem getAStoryItemByFeedId(String feedId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.query(TABLE_STORY_ITEM, null, SOCIAL_FEED_ID
                + " = ?", new String[] { String.valueOf(feedId) }, null,
                null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            SocialWidgetItem storyItem = new SocialWidgetItem(mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), Integer.parseInt(mCursor.getString(4)), Integer.parseInt(mCursor.getString(5)), mCursor.getString(6), mCursor.getString(7), Integer.parseInt(mCursor.getString(8)),
                    Integer.parseInt(mCursor.getString(9)), mCursor.getString(10), mCursor.getString(10), mCursor.getString(11), mCursor.getString(12), mCursor.getString(13), mCursor.getString(14));
            mCursor.close();
            return storyItem;
        }
        db.close();
        return null;
    }

    // get all story item from feedType in a social category
    public ArrayList<SocialWidgetItem> getAllStoryItem(String socialCategory, int feedType) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<SocialWidgetItem> storyItemList = new ArrayList<SocialWidgetItem>();
        String querry = "SELECT * FROM " + TABLE_STORY_ITEM + " WHERE " + SOCIAL_CATEGORY + " = '" + socialCategory + "' and " + FEEDS_TYPE + " = '" + feedType + "'";
        Cursor mCursor = db.rawQuery(querry, null);
        if (mCursor.moveToFirst()) {
            do {
                SocialWidgetItem storyItem = new SocialWidgetItem(Integer.parseInt(mCursor.getString(0)), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), Integer.parseInt(mCursor.getString(4)), Integer.parseInt(mCursor.getString(5)), mCursor.getString(6), mCursor.getString(7),
                        Integer.parseInt(mCursor.getString(8)), Integer.parseInt(mCursor.getString(9)), mCursor.getString(10), mCursor.getString(11), mCursor.getString(12), mCursor.getString(13), mCursor.getString(14), mCursor.getString(15));
                storyItemList.add(storyItem);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return storyItemList;
    }

    // get all story item in a social category
    public ArrayList<SocialWidgetItem> getAllStoryItem(String socialCategory) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<SocialWidgetItem> storyItemList = new ArrayList<SocialWidgetItem>();
        String querry = "SELECT * FROM " + TABLE_STORY_ITEM + " WHERE " + SOCIAL_CATEGORY + " = '" + socialCategory + "'";
        Cursor mCursor = db.rawQuery(querry, null);
        if (mCursor != null && mCursor.getColumnCount() > 0 && mCursor.moveToFirst()) {
            do {
                SocialWidgetItem storyItem = new SocialWidgetItem(Integer.parseInt(mCursor.getString(0)), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), Integer.parseInt(mCursor.getString(4)), Integer.parseInt(mCursor.getString(5)), mCursor.getString(6), mCursor.getString(7),
                        Integer.parseInt(mCursor.getString(8)), Integer.parseInt(mCursor.getString(9)), mCursor.getString(10), mCursor.getString(11), mCursor.getString(12), mCursor.getString(13), mCursor.getString(14), mCursor.getString(15));
                storyItemList.add(storyItem);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return storyItemList;
    }

    // get all story item
    public ArrayList<SocialWidgetItem> getAllStoryItem() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<SocialWidgetItem> storyItemList = new ArrayList<SocialWidgetItem>();
        String sql = "Select * from " + TABLE_STORY_ITEM;
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.moveToFirst()) {
            do {
                SocialWidgetItem storyItem = new SocialWidgetItem(Integer.parseInt(mCursor.getString(0)), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), Integer.parseInt(mCursor.getString(4)), Integer.parseInt(mCursor.getString(5)), mCursor.getString(6), mCursor.getString(7),
                        Integer.parseInt(mCursor.getString(8)), Integer.parseInt(mCursor.getString(9)), mCursor.getString(10), mCursor.getString(11), mCursor.getString(12), mCursor.getString(13), mCursor.getString(14), mCursor.getString(15));
                storyItemList.add(storyItem);
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        db.close();
        return storyItemList;
    }

    // delete a story item
    public void deleteAStoryItem(SocialWidgetItem storyItem) {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_STORY_ITEM, STORY_ITEM_ID + " = ?", new String[] { String.valueOf(storyItem.getId()) });
        mdb.close();
    }

    // delete all story item in a feed id
    public void deleteAllStoryItemInAFeedType(String socialFeedId) {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_STORY_ITEM, SOCIAL_CATEGORY + " = ?", new String[] { String.valueOf(socialFeedId) });
        mdb.close();
    }

    // delete all story item in a feed id
    public void deleteAllStoryItemInAFeedDetailType(int feedType) {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_STORY_ITEM, FEEDS_TYPE + " = ?", new String[] { String.valueOf(feedType) });
        mdb.close();
    }

    //delete all story item when user log out
    public void deleteAllStoryItem() {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_STORY_ITEM, null, null);
        mdb.close();
    }

    // get number count story in a feed
    public int countStoryInAFeed(String socialFeedId) {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_STORY_ITEM + " WHERE " + SOCIAL_FEED_ID + "='" + String.valueOf(socialFeedId) + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // count all story item
    public int countAllStory() {
        int count;
        String countQuery = "SELECT  * FROM " + TABLE_STORY_ITEM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    //count all story item of a social type
    public int countAllStoryItemInASocialType(String socialCategory) {
        int count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STORY_ITEM, null, SOCIAL_CATEGORY + " = ?", new String[] { String.valueOf(socialCategory) }, null, null, null);
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    //count all story item of a social type
    public int countAllStoryItemInAType(int feedType) {
        int count;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STORY_ITEM, null, SOCIAL_CATEGORY + " = ?", new String[] { String.valueOf(feedType) }, null, null, null);
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
