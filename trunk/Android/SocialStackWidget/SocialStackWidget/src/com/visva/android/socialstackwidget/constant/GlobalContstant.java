package com.visva.android.socialstackwidget.constant;

public class GlobalContstant {
    public static final boolean DEBUG_MODE = true;
    public static final String PRE_TAG = "VISVA_";
    public static final String SUB_STRING_CODE = "akjfhiymgpomn06011989";

    // receiver action
    public static final String ACTION_SETTINGS = "com.visva.android.socialstackwidget.ACTION_LOGIN";
    public static final String ACTION_REQUEST = "com.visva.android.socialstackwidget.ACTION_REQUEST";
    public static final String ACTION_UPDATE_DATA = "com.visva.android.socialstackwidget.ACTION_UPDATE_DATA";
    public static final String ACTION_AUTO_UPDATE = "com.visva.android.socialstackwidget.ACTION_AUTO_UPDATE";
    public static final String ACTION_CLICK_DETAIL_ITEM = "com.visva.android.socialstackwidget.ACTION_CLICK_DETAIL_ITEM";
    public static final String ACTION_REFRESH = "com.visva.android.socialstackwidget.ACTION_REFRESH";

    // extra intent
    public static final String EXTRA_SOCIAL_TYPE_REQUEST = "EXTRA_SOCIAL_REQUEST_TYPE";
    public static final String EXTRA_SOCIAL_DETAIL_REQUEST = "EXTRA_SOCIAL_DETAIL_REQUEST";
    public static final String EXTRA_SOCIAL_LIMIT_ITEM = "EXTRA_SOCIAL_LIMIT_ITEM";
    public static final String EXTRA_ITEM_TYPE = "DETAIL_ITEM_TYPE";
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static final String EXTRA_ITEM_AUTHOR = "EXTRA_ITEM_AUTHOR";

    //facebook constant
    public static final String FACEBOOK_USER_ID = "id";
    public static final String FACEBOOK_USER_NAME = "name";
    public static final String FACEBOOK_USER_PICTURE = "picture";
    public static final String FACEBOOK_GET_MYNEWSFEED = "me/home";
    public static final String FACEBOOK_GET_LIKEDPAGES = "me/likes";
    public static final String FACEBOOK_GET_LINKS_ONLY = "me/links";
    public static final String FACEBOOK_GET_PHOTOS = "me/photos";
    public static final String FACEBOOK_GET_TIMELINE = "me/feed";
    public static final String FACEBOOK_GET_FEED_PHOTOS = "me/home/photos";
    public static final String FACEBOOK_GET_GROUPS = "me/groups";
    public static final String FACEBOOK_GET_FRIEND_GROUPS = "me/friendlists";
    public static final String FACEBOOK_GRAPH_ME = "me";

    public static final String FACEBOOK = "FACEBOOK";
    public static final String TWITTER = "TWITTER";
    public static final String GOOGLE_PLUS = "GOOGLE_PLUS";

    // social detail type
    public static final int SOCIAL_TYPE_FEED = 1;
    public static final int SOCIAL__TIMELINE = 2;

    //preference key
    public static final String SHARE_PRE_FACEBOOK_TYPE_KEY = "SHARE_PRE_FACEBOOK_TYPE_KEY";
    public static final String SHARE_PRE_TWITTER_TYPE_KEY = "SHARE_PRE_TWITTER_TYPE_KEY";
    public static final String SHARE_PRE_GOOGLE_PLUS_TYPE_KEY = "SHARE_PRE_GOOGLE_PLUS_TYPE_KEY";
    public static final String SHARE_PRE_REFRESH_TIME_KEY = "SHARE_PRE_REFRESH_TIME_KEY";

}
