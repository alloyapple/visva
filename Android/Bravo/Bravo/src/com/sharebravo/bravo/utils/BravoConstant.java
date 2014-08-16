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

    /* Twitter key and api */
    public static final String  BRAVO_PREFERENCE                   = "BRAVO_PREFERENCE";
    public final static String  TWITTER_CONSUMER_KEY               = "hmouxtO1RpTRj8zXI0REdlesH";
    public final static String  TWITTER_CONSUMER_SECRET            = "cPsBinH5CyWQBUpFcXZRz2QkXoeeec94erGXBdHrpl7TZmlwch";
    public static final String  TWITTER_CALLBACK_URL               = "bravo://connect";
    // public static final String TWITTER_ACCESS_TOKEN = "490386183-ChIDIftcjtoAqfcLJBxoUDuNqRiCHTNuXDwIG8by";
    // public static final String TWITTER_ACCESS_TOKEN_SECRET = " RFKrxNEAlUqO84CZ8ZkhSUwOXI10ahuempi8R31husKN3";

    // Twitter oauth urls
    public static final String  URL_TWITTER_AUTH                   = "auth_url";
    public static final String  URL_TWITTER_OAUTH_VERIFIER         = "oauth_verifier";
    public static final String  URL_TWITTER_OAUTH_TOKEN            = "oauth_token";

    /* share preferences key */
    public static final String  PREF_KEY_TWITTER_OAUTH_TOKEN       = "pref_key_twitter_oauth_token";
    public static final String  PREF_KEY_TWITTER_OAUTH_VERIFIER    = "pref_key_twitter_oauth_verifier";

    /* SNS Login, Register */
    public static final String  LOGIN_SNS_TYPE                     = "login_sns_type";
    public static final String  REGISTER_SNS_TYPE                  = "register_sns_type";
    public static final int     NO_LOGIN_SNS                       = -1;
    public static final int     LOGIN_FACEBOOK                     = NO_LOGIN_SNS + 1;
    public static final int     LOGIN_TWITTER                      = LOGIN_FACEBOOK + 1;
    public static final int     LOGIN_4SQUARE                      = LOGIN_TWITTER + 1;
    public static final int     LOGIN_BRAVO                        = LOGIN_4SQUARE + 1;
    public static final int     NO_REGISTER_SNS                    = LOGIN_BRAVO + 1;
    public static final int     REGISTER_FACEBOOK                  = NO_REGISTER_SNS + 1;
    public static final int     REGISTER_TWITTER                   = REGISTER_FACEBOOK + 1;
    public static final int     REGISTER_4SQUARE                   = REGISTER_TWITTER + 1;
    public static final int     REGISTER_BRAVO                     = REGISTER_4SQUARE + 1;
}
