package com.samsung.android.alwayssocial.database;

import java.util.ArrayList;

import android.content.Context;

import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.util.Utils;

public class DBManager {

    private static DBManager mInstance = null;
    protected static final String TAG = "DBManager";
    private static Context mContext;
    private static AlwaysDatabaseProvider mAlwaysDatabaseProvider;

    /*FB data*/
    private ArrayList<StoryItemUnit> mStoryItemList = new ArrayList<StoryItemUnit>();

    public static DBManager getInstance(Context context)
    {
        if (mInstance == null) {
            mInstance = new DBManager();
            mContext = context;
            mAlwaysDatabaseProvider = new AlwaysDatabaseProvider(mContext);
        }
        return mInstance;
    }

    public ArrayList<StoryItemUnit> updateDataToUI(String socialCategory, int feedType, boolean isNeedSort) {
        ArrayList<StoryItemUnit> storyItemList = mAlwaysDatabaseProvider.getAllStoryItem(socialCategory, feedType);
        if (isNeedSort == false) {
            mStoryItemList = storyItemList;
        } else if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialCategory) == 0) {
            mStoryItemList = sortFBStoryItemListFromDB(storyItemList);
        } else if (GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialCategory) == 0)
            mStoryItemList = sortTWStoryItemListFromDB(storyItemList);
        return mStoryItemList;
    }

    public void deleteAllStoryItem(String socialCategory) {
        mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedType(socialCategory);
    }

    public int countFeedOfASocial(String socialCategory) {
        return mAlwaysDatabaseProvider.countAllStoryItemInASocialType(socialCategory);
    }

    public void updateLikeCommentInfo(StoryItemUnit storyItem, int likeCount, int commentCount, int isLiked) {
        mAlwaysDatabaseProvider.updateLikeCommentInfo(storyItem, likeCount, commentCount, isLiked);
    }

    private ArrayList<StoryItemUnit> sortFBStoryItemListFromDB(ArrayList<StoryItemUnit> storyItemList) {
        for (int i = 0; i < storyItemList.size() - 1; i++)
            for (int j = i + 1; j < storyItemList.size(); j++) {
                long _tempTime1 = Utils.convertTimeToLong(storyItemList.get(i).getTime_stamp());
                long _tempTime2 = Utils.convertTimeToLong(storyItemList.get(j).getTime_stamp());
                if (_tempTime1 < _tempTime2) {
                    wrapStoryItemInList(storyItemList, i, j);
                }
            }
        return storyItemList;
    }

    private ArrayList<StoryItemUnit> sortTWStoryItemListFromDB(ArrayList<StoryItemUnit> storyItemList) {
        for (int i = 0; i < storyItemList.size() - 1; i++)
            for (int j = i + 1; j < storyItemList.size(); j++) {
                long _tempTime1 = Utils.convertTWTimeToLong(storyItemList.get(i).getTime_stamp());
                long _tempTime2 = Utils.convertTWTimeToLong(storyItemList.get(j).getTime_stamp());
                if (_tempTime1 < _tempTime2) {
                    wrapStoryItemInList(storyItemList, i, j);
                }
            }
        return storyItemList;
    }

    private void wrapStoryItemInList(ArrayList<StoryItemUnit> storyItemList, int i, int j) {
        // TODO Auto-generated method stub
        StoryItemUnit storyItemUnit = storyItemList.get(i);
        StoryItemUnit storyItemUnit2 = storyItemList.get(j);
        storyItemList.remove(i);
        storyItemList.add(i, storyItemUnit2);
        storyItemList.remove(j);
        storyItemList.add(j, storyItemUnit);
    }

    public void filterDatabase(String socialType) {

        ArrayList<StoryItemUnit> storyItemList = new ArrayList<StoryItemUnit>();
        if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) == 0) {
            ArrayList<StoryItemUnit> storyList = new ArrayList<StoryItemUnit>();
            /**filter feed*/
            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_FEED));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(FacebookConstant.FB_DATA_TYPE_FEED);
            for (int i = 0; i < storyItemList.size(); i++) {
                mAlwaysDatabaseProvider.addNewStoryItem(storyItemList.get(i));
            }

            /**filter feed photo*/
            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS);
            for (int i = 0; i < storyItemList.size(); i++) {
                mAlwaysDatabaseProvider.addNewStoryItem(storyItemList.get(i));
            }

            /**filter feed links*/
            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_FEED_LINKS));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(FacebookConstant.FB_DATA_TYPE_FEED_LINKS);

            /**filter timeline*/
            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_TIMELINE));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(FacebookConstant.FB_DATA_TYPE_TIMELINE);
            for (int i = 0; i < storyItemList.size(); i++) {
                mAlwaysDatabaseProvider.addNewStoryItem(storyItemList.get(i));
            }
        } else if (GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) == 0) {

            ArrayList<StoryItemUnit> storyList = new ArrayList<StoryItemUnit>();
            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_TIMELINE));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(TwitterConstant.TW_DATA_TYPE_TIMELINE);
            for (int i = 0; i < storyItemList.size(); i++) {
                mAlwaysDatabaseProvider.addNewStoryItem(storyItemList.get(i));
            }

            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_TIMELINE));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(TwitterConstant.TW_DATA_TYPE_TIMELINE);
            for (int i = 0; i < storyItemList.size(); i++) {
                mAlwaysDatabaseProvider.addNewStoryItem(storyItemList.get(i));
            }

            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS);
            for (int i = 0; i < storyItemList.size(); i++)
                mAlwaysDatabaseProvider.addNewStoryItem(storyItemList.get(i));

            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_TWEETS));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(TwitterConstant.TW_DATA_TYPE_TWEETS);
            for (int i = 0; i < storyItemList.size(); i++) {
                mAlwaysDatabaseProvider.addNewStoryItem(storyItemList.get(i));
            }

            storyItemList = sortFBStoryItemListFromDB(mAlwaysDatabaseProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_FAVORITES));
            if (storyItemList.size() > 20) {
                for (int i = 20; i < storyItemList.size(); i++)
                    storyList.add(storyItemList.get(i));
            }
            storyItemList.removeAll(storyList);
            storyList.clear();
            mAlwaysDatabaseProvider.deleteAllStoryItemInAFeedDetailType(TwitterConstant.TW_DATA_TYPE_FAVORITES);
            for (int i = 0; i < storyItemList.size(); i++) {
                mAlwaysDatabaseProvider.addNewStoryItem(storyItemList.get(i));
            }

        }
    }

    public StoryItemUnit getStoryItemByFeedId(String feedId) {
        return mAlwaysDatabaseProvider.getAStoryItemByFeedId(feedId);
    }

    public int countStoryItemByFeedId(String feedId) {
        return mAlwaysDatabaseProvider.countStoryInAFeed(feedId);
    }
}
