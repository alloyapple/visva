package com.samsung.android.alwayssocial.object;

import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.object.twitter.TwitterFollowersData;
import com.samsung.android.alwayssocial.object.twitter.TwitterListData;
import com.samsung.android.alwayssocial.object.twitter.TwitterListFollowData;
import com.samsung.android.alwayssocial.object.twitter.TwitterSavedSearchesData;
import com.samsung.android.alwayssocial.object.twitter.TwitterStatusData;
import com.samsung.android.alwayssocial.object.twitter.TwitterTimeLineData;
import com.samsung.android.alwayssocial.object.twitter.TwitterUserData;

public class ResponseData {

    public String status;
    public String apiName;
    public String errorCode;
    public String errorContext;
    public String responseDate;
    public ResponseBase responseData;
    public boolean isRequestUpdateRefresh;
    public boolean isMeRequest;
    public boolean isFirstTimeUpdate;

    public void setResponseFacebookType(int type)
    {
        //For Twitter data
        if (type == TwitterConstant.TW_DATA_TYPE_TIMELINE || type == TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS || type == TwitterConstant.TW_DATA_TYPE_TWEETS || type == TwitterConstant.TW_DATA_TYPE_FAVORITES || type == TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS || type == TwitterConstant.TW_DATA_TYPE_COMMENT || type ==  TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM ||  type == TwitterConstant.TW_DATA_VIEW_USER_TIMELINE) {
            responseData = new TwitterTimeLineData();
        } else if (type == TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES) {
            responseData = new TwitterSavedSearchesData();
        } else if (type == TwitterConstant.TW_DATA_TYPE_LIST) {
            responseData = new TwitterListData();
        } else if (type == TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED||type == TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME) {
            responseData = new TwitterListFollowData();
        } else if (type == TwitterConstant.TW_POST_RETWEET) {
            responseData = new TwitterStatusData();
        } else if (type == TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME || type == TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED) {
            responseData = new TwitterFollowersData();
        } else if (type == TwitterConstant.TW_POST_RETWEET) {
            responseData = new TwitterStatusData();
        } else if (type == TwitterConstant.TW_POST_DESTROY_FRIENDSHIP) {
            responseData = new TwitterUserData();
        }
    }

    public ResponseData(int type) {
        setResponseFacebookType(type);
    }

}
