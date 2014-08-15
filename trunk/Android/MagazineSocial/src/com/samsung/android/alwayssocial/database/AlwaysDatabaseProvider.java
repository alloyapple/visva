package com.samsung.android.alwayssocial.database;

import java.util.ArrayList;

import com.samsung.android.alwayssocial.service.StoryItemUnit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlwaysDatabaseProvider extends SQLiteOpenHelper {

    // ========================Constant Define======================
    private static final String DATABASE_NAME = "always.db";
    private static final int DATABASE_VERSION = 1;

    // STORY TABLE NAME
    private static final String TABLE_STORY_ITEM = "story_item";
    // STORY TABLE COLUMS NAME
    /***
     * MagazineHome data
     */
    private static final String STORY_ITEM_ID = "_id";
    private static final String SOCIAL_FEED_ID = "social_feed_id";
    private static final String SOCIAL_CATEGORY = "social_category";

    private static final String TITLE = "title";
    private static final String BODY = "body";
    private static final String IMAGE_URL = "body_url";
    private static final String IMAGE_HEIGHT = "image_height";
    private static final String IMAGE_WIDTH = "image_width";
    private static final String AUTHOR_NAME = "author_name";
    private static final String AUTHOR_IMAGE = "author_image";
    private static final String AUTHOR_IMAGE_WIDTH = "author_image_width";
    private static final String AUTHOR_IMAGE_HEIGHT = "author_image_height";
    private static final String TIME_STAMP = "time_stamp";
    private static final String MORE = "more";
    private static final String SOURCE = "source";
    private static final String TARGET = "target";
    private static final String LINK_URL = "link_url";
    private static final String IS_LIKED = "is_liked";

    /**
     * Always Social Data
     * @param context
     */
    private static final String FEEDS_TYPE = "feeds_type";
    private static final String FEEDS_TYPE_DETAIL = "feeds_type_detail";
    private static final String NUMBER_OF_LIKE = "number_of_like";
    private static final String NUMBER_OF_COMMENT = "number_of_comment";
    private static final String UPDATE_TIME = "update_time";
    private static final String MESSAGE = "message";
    private static final String NEXT_PAGE = "next_page";
    private static final String OBJECT_ID = "object_id";

    public AlwaysDatabaseProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        /* Create table story */
        String CREATE_STORY_ITEM_TABLE = "CREATE TABLE "
                + TABLE_STORY_ITEM + "(" + STORY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SOCIAL_FEED_ID + " TEXT," + SOCIAL_CATEGORY + " TEXT," + TITLE + " TEXT," + BODY + " TEXT," + IMAGE_URL + " INTEGER,"
                + IMAGE_HEIGHT + " INTEGER," + IMAGE_WIDTH + " INTEGER," + AUTHOR_NAME + " TEXT," + AUTHOR_IMAGE + " TEXT," + AUTHOR_IMAGE_WIDTH + " INTEGER," + AUTHOR_IMAGE_HEIGHT + " INTEGER," + TIME_STAMP + " TEXT,"
                + MORE + " INTEGER," + SOURCE + " TEXT," + TARGET + " TEXT," + FEEDS_TYPE + " TEXT," + FEEDS_TYPE_DETAIL + " TEXT," + NUMBER_OF_LIKE + " INTEGER," + NUMBER_OF_COMMENT + " INTEGER,"
                + UPDATE_TIME + " TEXT," + MESSAGE + " TEXT," + LINK_URL + " TEXT," + IS_LIKED + " INTEGER," + NEXT_PAGE + " TEXT," + OBJECT_ID + " TEXT" + ")";

        db.execSQL(CREATE_STORY_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists " + TABLE_STORY_ITEM);
        onCreate(db);
    }

    /*-------------------------- ADD FUNCTION--------------------------*/
    // add new story item
    public void addNewStoryItem(StoryItemUnit storyItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(SOCIAL_FEED_ID, storyItem.getSocialFeedId());
        mValues.put(SOCIAL_CATEGORY, storyItem.getSocial_category());

        mValues.put(TITLE, storyItem.getTitle());
        mValues.put(BODY, storyItem.getBody());
        mValues.put(IMAGE_URL, storyItem.getBody_url());
        mValues.put(IMAGE_HEIGHT, storyItem.getImage_height());
        mValues.put(IMAGE_WIDTH, storyItem.getImage_width());

        mValues.put(AUTHOR_NAME, storyItem.getAuthor_name());
        mValues.put(AUTHOR_IMAGE, storyItem.getAuthor_image());
        mValues.put(AUTHOR_IMAGE_WIDTH, storyItem.getAuthor_image_width());
        mValues.put(AUTHOR_IMAGE_HEIGHT, storyItem.getAuthor_image_height());

        mValues.put(TIME_STAMP, storyItem.getTime_stamp());
        mValues.put(MORE, storyItem.getMore());
        mValues.put(SOURCE, storyItem.getSource());
        mValues.put(TARGET, storyItem.getTarget());

        mValues.put(FEEDS_TYPE, storyItem.getFeeds_type());
        mValues.put(FEEDS_TYPE_DETAIL, storyItem.getFeeds_type_detail());
        mValues.put(NUMBER_OF_LIKE, storyItem.getNumber_of_like());
        mValues.put(NUMBER_OF_COMMENT, storyItem.getNumber_of_comment());
        mValues.put(UPDATE_TIME, storyItem.getUpdate_time());
        mValues.put(MESSAGE, storyItem.getMessage());
        mValues.put(LINK_URL, storyItem.getLink_url());
        mValues.put(IS_LIKED, storyItem.isLikedFeed());
        mValues.put(NEXT_PAGE, storyItem.getNextPage());
        mValues.put(OBJECT_ID, storyItem.getObjectId());

        db.insert(TABLE_STORY_ITEM, null, mValues);
        db.close();
    }

    //update number like count to a storyItem
    public void updateLikeCommentInfo(StoryItemUnit storyItem, int likeCount, int commentCount, int isLiked) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(NUMBER_OF_LIKE, likeCount);
        mValues.put(NUMBER_OF_COMMENT, commentCount);
        mValues.put(IS_LIKED, isLiked);
        db.update(TABLE_STORY_ITEM, mValues, STORY_ITEM_ID + " = ?",
                new String[] { String.valueOf(storyItem.getStoryItemId()) });
        db.close();
    }

    //update number comment count to a storyItem
    public void updateCommentCount(StoryItemUnit storyItemUnit, int likeCount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(NUMBER_OF_COMMENT, storyItemUnit.getNumber_of_comment());
        db.update(TABLE_STORY_ITEM, mValues, STORY_ITEM_ID + " = ?",
                new String[] { String.valueOf(storyItemUnit.getSocialFeedId()) });
        db.close();
    }

    // update story item
    public void updateAStoryItem(StoryItemUnit storyItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mValues = new ContentValues();
        mValues.put(SOCIAL_FEED_ID, storyItem.getSocialFeedId());
        mValues.put(SOCIAL_CATEGORY, storyItem.getSocial_category());

        mValues.put(TITLE, storyItem.getTitle());
        mValues.put(BODY, storyItem.getBody());
        mValues.put(IMAGE_URL, storyItem.getBody_url());
        mValues.put(IMAGE_HEIGHT, storyItem.getImage_height());
        mValues.put(IMAGE_WIDTH, storyItem.getImage_width());

        mValues.put(AUTHOR_NAME, storyItem.getAuthor_name());
        mValues.put(AUTHOR_IMAGE, storyItem.getAuthor_image());
        mValues.put(AUTHOR_IMAGE_WIDTH, storyItem.getAuthor_image_width());
        mValues.put(AUTHOR_IMAGE_HEIGHT, storyItem.getAuthor_image_height());

        mValues.put(TIME_STAMP, storyItem.getTime_stamp());
        mValues.put(MORE, storyItem.getMore());
        mValues.put(SOURCE, storyItem.getSource());
        mValues.put(TARGET, storyItem.getTarget());

        mValues.put(FEEDS_TYPE, storyItem.getFeeds_type());
        mValues.put(FEEDS_TYPE_DETAIL, storyItem.getFeeds_type_detail());
        mValues.put(NUMBER_OF_LIKE, storyItem.getNumber_of_like());
        mValues.put(NUMBER_OF_COMMENT, storyItem.getNumber_of_comment());
        mValues.put(UPDATE_TIME, storyItem.getUpdate_time());
        mValues.put(MESSAGE, storyItem.getMessage());
        mValues.put(LINK_URL, storyItem.getLink_url());
        mValues.put(IS_LIKED, storyItem.isLikedFeed());
        mValues.put(NEXT_PAGE, storyItem.getNextPage());
        mValues.put(OBJECT_ID, storyItem.getObjectId());

        db.update(TABLE_STORY_ITEM, mValues, STORY_ITEM_ID + " = ?",
                new String[] { String.valueOf(storyItem.getStoryItemId()) });
        db.close();
    }

    // get a story item
    public StoryItemUnit getAStoryItemByFeedId(String feedId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.query(TABLE_STORY_ITEM, null, SOCIAL_FEED_ID
                + " = ?", new String[] { String.valueOf(feedId) }, null,
                null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            StoryItemUnit storyItem = new StoryItemUnit(mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getString(4),
                    mCursor.getString(5), Integer.parseInt(mCursor.getString(6)), Integer.parseInt(mCursor.getString(7)), mCursor.getString(8), mCursor.getString(9),
                    Integer.parseInt(mCursor.getString(10)), Integer.parseInt(mCursor.getString(11)), mCursor.getString(12), Integer.parseInt(mCursor.getString(13)),
                    mCursor.getString(14), mCursor.getString(15), mCursor.getString(16), mCursor.getString(17), Integer.parseInt(mCursor.getString(18)),
                    Integer.parseInt(mCursor.getString(19)), mCursor.getString(20), mCursor.getString(21), mCursor.getString(22), Integer.parseInt(mCursor.getString(23)), mCursor.getString(24), mCursor.getString(25));

            mCursor.close();
            return storyItem;
        }
        db.close();
        return null;
    }

    // get all story item from feedType in a social category
    public ArrayList<StoryItemUnit> getAllStoryItem(String socialCategory, int feedType) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StoryItemUnit> storyItemList = new ArrayList<StoryItemUnit>();
        String querry = "SELECT * FROM " + TABLE_STORY_ITEM + " WHERE " + SOCIAL_CATEGORY + " = '" + socialCategory + "' and " + FEEDS_TYPE + " = '" + feedType + "'";
        Cursor mCursor = db.rawQuery(querry, null);
        // Cursor mCursor = db.query(TABLE_STORY_ITEM, null, FEEDS_TYPE_DETAIL
        //        + " = ?", new String[] { String.valueOf(feedType) }, null,
        //        null, null);
        Log.e(getDatabaseName(), "mCursor.getCount()" + mCursor.getCount());
        if (mCursor.moveToFirst()) {
            do {
                StoryItemUnit storyItem = new StoryItemUnit(Integer.parseInt(mCursor.getString(0)), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getString(4),
                        mCursor.getString(5), Integer.parseInt(mCursor.getString(6)), Integer.parseInt(mCursor.getString(7)), mCursor.getString(8), mCursor.getString(9),
                        Integer.parseInt(mCursor.getString(10)), Integer.parseInt(mCursor.getString(11)), mCursor.getString(12), Integer.parseInt(mCursor.getString(13)),
                        mCursor.getString(14), mCursor.getString(15), mCursor.getString(16), mCursor.getString(17), Integer.parseInt(mCursor.getString(18)), Integer.parseInt(mCursor.getString(19)),
                        mCursor.getString(20), mCursor.getString(21), mCursor.getString(22), Integer.parseInt(mCursor.getString(23)), mCursor.getString(24), mCursor.getString(25));
                storyItemList.add(storyItem);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return storyItemList;
    }

    // get all story item in a social category
    public ArrayList<StoryItemUnit> getAllStoryItem(String socialCategory) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StoryItemUnit> storyItemList = new ArrayList<StoryItemUnit>();
        String querry = "SELECT * FROM " + TABLE_STORY_ITEM + " WHERE " + SOCIAL_CATEGORY + " = " + socialCategory;
        Cursor mCursor = db.rawQuery(querry, null);
        // Cursor mCursor = db.query(TABLE_STORY_ITEM, null, FEEDS_TYPE_DETAIL
        //        + " = ?", new String[] { String.valueOf(feedType) }, null,
        //        null, null);
        if (mCursor.moveToFirst()) {
            do {
                StoryItemUnit storyItem = new StoryItemUnit(Integer.parseInt(mCursor.getString(0)), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getString(4),
                        mCursor.getString(5), Integer.parseInt(mCursor.getString(6)), Integer.parseInt(mCursor.getString(7)), mCursor.getString(8), mCursor.getString(9),
                        Integer.parseInt(mCursor.getString(10)), Integer.parseInt(mCursor.getString(11)), mCursor.getString(12), Integer.parseInt(mCursor.getString(13)),
                        mCursor.getString(14), mCursor.getString(15), mCursor.getString(16), mCursor.getString(17), Integer.parseInt(mCursor.getString(18)), Integer.parseInt(mCursor.getString(19)),
                        mCursor.getString(20), mCursor.getString(21), mCursor.getString(22), Integer.parseInt(mCursor.getString(23)), mCursor.getString(24), mCursor.getString(25));
                storyItemList.add(storyItem);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        db.close();
        return storyItemList;
    }

    // get all story item
    public ArrayList<StoryItemUnit> getAllStoryItem() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<StoryItemUnit> storyItemList = new ArrayList<StoryItemUnit>();
        String sql = "Select * from " + TABLE_STORY_ITEM;
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.moveToFirst()) {
            do {
                StoryItemUnit storyItem = new StoryItemUnit(Integer.parseInt(mCursor.getString(0)), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getString(4),
                        mCursor.getString(5), Integer.parseInt(mCursor.getString(6)), Integer.parseInt(mCursor.getString(7)), mCursor.getString(8), mCursor.getString(9),
                        Integer.parseInt(mCursor.getString(10)), Integer.parseInt(mCursor.getString(11)), mCursor.getString(12), Integer.parseInt(mCursor.getString(13)),
                        mCursor.getString(14), mCursor.getString(15), mCursor.getString(16), mCursor.getString(17), Integer.parseInt(mCursor.getString(18)), Integer.parseInt(mCursor.getString(19)),
                        mCursor.getString(20), mCursor.getString(21), mCursor.getString(22), Integer.parseInt(mCursor.getString(23)), mCursor.getString(24), mCursor.getString(25));
                storyItemList.add(storyItem);
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        db.close();
        return storyItemList;
    }

    // delete a story item
    public void deleteAStoryItem(StoryItemUnit storyItem) {
        SQLiteDatabase mdb = getWritableDatabase();
        mdb.delete(TABLE_STORY_ITEM, STORY_ITEM_ID + " = ?", new String[] { String.valueOf(storyItem.getStoryItemId()) });
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
        String countQuery = "SELECT  * FROM " + TABLE_STORY_ITEM + " WHERE " + SOCIAL_FEED_ID + "='" + String.valueOf(socialFeedId)+"'";
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
        // return count
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
        // return count
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
        // return count
        count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}
