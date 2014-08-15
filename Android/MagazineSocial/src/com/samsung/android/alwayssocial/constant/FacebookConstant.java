package com.samsung.android.alwayssocial.constant;

public class FacebookConstant {
    // =============================Constant Define=====================

    public static final String FB_USER_ID = "id";
    public static final String FB_USER_NAME = "name";
    public static final String FB_USER_PICTURE = "picture";
    public static final String FB_GET_MYNEWSFEED = "me/home";
    public static final String FB_GET_LIKEDPAGES = "me/likes";
    public static final String FB_GET_LINKS_ONLY = "me/links";
    public static final String FB_GET_PHOTOS = "me/photos";
    public static final String FB_GET_TIMELINE = "me/feed";
    public static final String FB_GET_FEED_PHOTOS = "me/home/photos";
    public static final String FB_GET_GROUPS = "me/groups";
    public static final String FB_GET_FRIEND_GROUPS = "me/friendlists";
    public static final String FB_GRAPH_ME = "me";

    public static final String FB_FIELD_OPTION_LIMIT = "limit";
    public static final String FB_FRAGMENT_TYPE = "fbFragmentType";

    /* -- Fragment type -- */
    public static final int FB_DATA_TYPE_FEED = 0;
    public static final int FB_DATA_TYPE_FEED_PHOTOS = 1;
    public static final int FB_DATA_TYPE_FEED_LINKS = 2;
    public static final int FB_DATA_TYPE_TIMELINE = 3;
    public static final int FB_DATA_TYPE_TAGGEDME = 4;
    public static final int FB_DATA_TYPE_GROUPS = 5;
    public static final int FB_DATA_TYPE_PAGES = 6;
    public static final int FB_DATA_TYPE_FRIENDS = 7;
    public static final int FB_DATA_TYPE_FRIEND_GROUPS = 8;
    public static final int FB_DATA_TYPE_FRIEND_ALBUMS = 9;
    public static final int FB_DATA_TYPE_FRIEND_TIMELINE = 10;
    public static final int FB_DATA_TYPE_PAGE_TIMELINE = 11;
    
    /* -- Other request -- */
    public static final int FB_DATA_TYPE_LIKEINFO = 20;
    public static final int FB_DATA_TYPE_COMMENTINFO = 21;
    public static final int FB_DATA_TYPE_USER = 22;
    public static final int FB_DATA_TYPE_FEED_ITEM = 23;
    public static final int FB_DATA_TYPE_SPECIAL_IMAGE_QUALITY = 24;
    public static final int FB_DATA_TYPE_PHOTO_ITEM = 25;
    public static final int FB_POST_TYPE_LIKE = 26;
    public static final int FB_POST_TYPE_COMMENT = 27;
    public static final int FB_DATA_MORE_COMMENT = 28;
    public static final int FB_DATA_MEMBERS_OF_GROUP = 29;

    //facebook table type name
    public static final String FB_TABLE_TYPE_PHOTO = "PHOTO";
    public static final String FB_TABLE_TYPE_LINK = "LINK";
    public static final String FB_TABLE_TYPE_VIDEO = "VIDEO";
    public static final String FB_TABLE_TYPE_STATUS = "STATUS";

    //like-comment, click
    public static final String FB_LIKE_BTN_CLICK = "like_click";
    public static final String FB_COMMENT_BTN_CLICK = "comment_click";

    //request code
    public static final int FB_REQUEST_CODE = 1;
    public static final String FB_REQUEST_PARAM_FIELDS_CONSTANT = "fields";
    public static final String FB_REQUEST_FEED_FIELDS = "id,type,message,description,caption,picture,name,from,created_time,object_id,link,story,comments";
    public static final String FB_REQUEST_FRIEND_LIST_FIELDS = "id, name, picture";
    public static final String FB_REQUEST_LIKED_PAGES_FIELDS = "id,name,description,category,likes";
    public static final String FB_REQUEST_TAGGED_PHOTO_FIELDS = "id,name,picture,link,from,created_time,comments,message";

    //pref define
    public static final String FB_PREF_KEY_LIKECOMMEND = "facebook_prefs_likecomment_info";
    public static final String FB_PREF_KEY_IS_LIKED = "facebook_prefs_is_liked_info";
    
    public static final String FB_KEY_NEXT_PAGE = "until";

}
