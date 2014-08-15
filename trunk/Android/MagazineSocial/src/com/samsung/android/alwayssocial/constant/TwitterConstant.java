package com.samsung.android.alwayssocial.constant;

public class TwitterConstant {
    public final static String TWITTER_CONSUMER_KEY = "Y1dwG6A8MBiy7nbhCPBi1w";
    public final static String TWITTER_CONSUMER_SECRET = "9eNSdHTfP9eBoCcifoEWU4IrsUrvdqciaA4UgMoUK4";
    public static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
    
    // Twitter oauth urls
    public static final String URL_TWITTER_AUTH = "auth_url";
    public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    public static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    
    /* Twitter Preference Constanst */
    public static String PREFERENCE_NAME = "always_twitter_oauth";
    public static final String PREF_KEY_TWITTER_OAUTH_TOKEN = "always_twitter_oauth_token";
    public static final String PREF_KEY_TWITTER_OAUTH_SECRET = "always_twitter_oauth_token_secret";
    public static final String PREF_KEY_TWITTER_LOGIN = "always_twitter_isTwitterLogedIn";
    
    // Same value as fragment
    public static final int TW_DATA_TYPE_TIMELINE = 0;
    public static final int TW_DATA_TYPE_TIMELINE_LINKS = 1;
    public static final int TW_DATA_TYPE_TWEETS = 2;
    public static final int TW_DATA_TYPE_FAVORITES = 3;
    public static final int TW_DATA_TYPE_MENTIONING_TWEETS = 4;
    public static final int TW_DATA_TYPE_SAVED_SEARCHES = 5;
    public static final int TW_DATA_TYPE_LIST = 6;
    public static final int TW_DATA_TYPE_LIST_FOLLOWED = 7;
    public static final int TW_DATA_TYPE_LIST_FOLLOWING_ME = 8;
    public static final int TW_DATA_TYPE_PEOPLE_FOLLOWED = 9;
    public static final int TW_DATA_TYPE_PEOPLE_FOLLOWING_ME = 10;
    
    //for request create favourite, reply, retweet
    public static final int TW_POST_CREATE_FAVOURITE = 11;
    public static final int TW_POST_DESTROY_FAVOURITE = 12;
    public static final int TW_POST_RETWEET = 13;
    public static final int TW_POST_RETWEET_WITH_COMMENT = 14;
    public static final int TW_POST_FOLLOW = 15;
    public static final int TW_POST_UNFOLLOW = 16;
    public static final int TW_POST_REPLY = 17;
    public static final int TW_DATA_TYPE_COMMENT = 18;
    public static final int TW_DATA_VIEW_SEARCH_ITEM = 19;
    public static final int TW_DATA_VIEW_USER_TIMELINE = 20;
    public static final int TW_POST_DESTROY_FRIENDSHIP = 22;
    
    // for 2nd page of list functions
    public static final int TW_DATA_TYPE_LIST_TIMELINE = 21;
    //display type
    public static final int TW_DISPLAY_TYPE_MEDIA_ENTITIES = 1;
    public static final int TW_DISPLAY_TYPE_NO_MEDIA_ENTITIES = 2;
    
    /*Twitter 2nd page display type*/
    public static final int TW_2ND_DISPLAY_TYPE_LINK = 0;
    public static final int TW_2ND_DISPLAY_TYPE_PICTURE = 1;
    public static final int TW_2ND_DISPLAY_TYPE_STATUS = 2;
    
    /*Limit of tweet*/
    public static final int TW_MAX_LENGTH_OF_TWEET = 140;
    public static final String TW_NOTIFCATION = "Length of tweet is 140 characters, your tweet is ";
    
    public static final String TW_KEY_COMMENT_ID = "commentId";
    public static final String TW_KEY_STATUS_USER_NAME = "userName";
    public static final String TW_KEY_STATUS_USER_ID = "userId";
    public static final String TW_KEY_SINCE_MAX_ID = "TweetId";
    public static final String TW_KEY_VIEW_SEARCH_ITEM = "searchName";
    public static final String TW_KEY_LIST_ID = "listId";
    public static final String TW_KEY_LIST_NAME = "listName";
}
