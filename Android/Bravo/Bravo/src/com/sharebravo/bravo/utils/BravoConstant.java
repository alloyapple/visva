package com.sharebravo.bravo.utils;

public class BravoConstant {
    /* Login-Register */
    public static final String  ACCESS_TYPE                        = "access_type";
    /**
     * set the value to show the log for sdk
     */
    public static final boolean DEBUG_MODE                         = true;

    /**
     * set the TAG for all log in sdk
     */
    public static final String  TAG                                = "AIOLog";

    /**
     * constant value for location provider
     */
    /* request type */
    public static final int     GET_LOCATION_TYPE_GOOGLE_SERVICE   = 1;
    public static final int     GET_LOCATION_TYPE_GPS              = 2;
    public static final int     GET_LOCATION_TYPE_NETWORK          = 3;
    public static final int     GET_LOCATION_TYPE_PASSIVE          = 4;
    public static final int     GET_LOCATION_TYPE_ADDRESS          = 5;

    /* message key */
    public static final int     MSG_REQUEST_LOCATION_GOOGLESERVICE = 10;
    public static final int     MSG_REQUEST_LOCATION_GPS           = 11;
    public static final int     MSG_REQUEST_LOCATION_NETWORK       = 12;
    public static final int     MSG_REQUEST_LOCATION_PASSIVE       = 13;
    public static final int     MSG_REQUEST_GET_ADDRESS            = 18;
    public static final int     MSG_UPDATE_LOCATION                = 14;
    public static final int     MSG_UPDATE_LOCATION_GPS            = 15;
    public static final int     MSG_UPDATE_LOCATION_NETWORK        = 16;
    public static final int     MSG_UPDATE_LOCATION_PASSIVE        = 17;

    /* fragment id */
    public static final int     FRAGMENT_BRAVO_REGISTER_ID         = 1001;
    public static final int     FRAGMENT_LOGIN_ID                  = FRAGMENT_BRAVO_REGISTER_ID + 1;
    public static final int     FRAGMENT_REGISTER_ID               = FRAGMENT_LOGIN_ID + 1;
    public static final int     FRAGMENT_REGISTER_USER_INFO_ID     = FRAGMENT_REGISTER_ID + 1;
    public static final int     FRAGMENT_BRAVO_LOGIN_ID            = FRAGMENT_REGISTER_USER_INFO_ID + 1;
    public static final int     FRAGMENT_FORGOT_PASSWORD           = FRAGMENT_BRAVO_LOGIN_ID + 1;

    public static final String  BRAVO_PREFERENCE                   = "BRAVO_PREFERENCE";

    public final static String  TWITTER_CONSUMER_KEY               = "Y1dwG6A8MBiy7nbhCPBi1w";

    public final static String  TWITTER_CONSUMER_SECRET            = "9eNSdHTfP9eBoCcifoEWU4IrsUrvdqciaA4UgMoUK4";

    public static final String  TWITTER_CALLBACK_URL               = "oauth://t4jsample";

    // Twitter oauth urls
    public static final String  URL_TWITTER_AUTH                   = "auth_url";
    public static final String  URL_TWITTER_OAUTH_VERIFIER         = "oauth_verifier";
    public static final String  URL_TWITTER_OAUTH_TOKEN            = "oauth_token";
}
