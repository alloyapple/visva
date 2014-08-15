package com.samsung.android.alwayssocial.constant;

import android.os.Environment;

public class GlobalConstant {

    public static final boolean DEBUG_MODE = false;
    public static final String VIEW_MODE = "view_mode";
    public static final int VIEW_TYPE_LIST = 0;
    public static final int VIEW_TYPE_FLIP = 1;
    public static final String ALWAYS_PREFERENCE = "always_pref";
    public static final String ALWAYS_FILE_PATH = Environment.getExternalStorageDirectory() + "/Always/";
    // Pref about logged in user information
    public static final String PREF_KEY_USER_ID = "always_user_id_";
    public static final String PREF_KEY_USER_NAME = "always_user_name_";
    public static final String PREF_KEY_USER_IMAGE_URL = "always_user_image_url_";
    public static final String PREF_KEY_VIEW_TYPE = "view_type";

    /* facebook data */
    public static final String FB_ACCESS_TOKEN = "access_token";
    public static final String FB_ACCESS_EXPIRED = "access_expires";
    public static final String FB_APP_ID = "1401516506752314";

    // Social
    public static final String GENERAL_SOCIAL = "SOCIAL";
    public static final String SOCIAL_TYPE_FACEBOOK = "facebook";
    public static final String SOCIAL_TYPE_TWITTER = "twitter";
    public static final String SOCIAL_TYPE_GOOGLEPLUS = "googleplus";
    public static final String SOCIAL_TYPE_LINKEDIN = "linkedin";
    public static final String SOCIAL_TYPE_INSTAGRAM = "instagram";
    public static final String SOCIAL_TYPE_FLICKR = "flickr";

    // Request to authorize   
    public static int REQUEST_CODE_AUTHORIZE_ACTIVITY = 1000;

    //service
    public static final String ALWAYS_SERVICE_START = "com.samsung.android.alwayssocial.service.AlwaysService.SERVICE_START";
    public static final String ALWAYS_SERVICE_STOP = "com.samsung.android.alwayssocial.service.AlwaysService.SERVICE_STOP";
    public static final String ALWAYS_SERVICE_BIND = "com.samsung.android.alwayssocial.service.AlwaysService.BIND";

    //share preference
    public static final String LIKE_COUNT = "like_count";
    public static final String COMMENT_COUNT = "comment_count";

    //Parcel
    public static final String STORY_ITEM_UNIT = "StoryItemUnit";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String SOCIAL_TYPE = "socialtype";
    public static final String SOCIAL_2ND_VIEW_TYPE = "movedetails";
    public static final String SUB_STRING_CODE = "akjfhiymgpomn24792";

    public static final String TYPE_DATA_UPDATE = "type_data_update";

    //Error handler
    public static final String ERROR_SNS_REPONSE = "SNS_ERROR";
}
