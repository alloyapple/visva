package com.sharebravo.bravo.utils;

public class BravoWebServiceConfig {
    // Network time out: 60s
    public static int          NETWORK_TIME_OUT                     = 60000;
    public static int          STATUS_RESPONSE_DATA_SUCCESS         = 0x1;
    public static int          STATUS_RESPONSE_DATA_ERROR           = 0x2;
    // Server api
    public static final String URL_BRAVO_BASE                       = "https://dev1.sharebravo.com/api";
    public static final String URL_GET_NOTIFICATION                 = URL_BRAVO_BASE + "/notification";
    public static final String URL_GET_NOTIFICATION_SEARCH          = URL_BRAVO_BASE
                                                                            + "/notification?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_BLOCKING_CHECK               = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/blocking/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_BRAVO                        = URL_BRAVO_BASE + "/bravo/{Bravo_ID}";
    public static final String URL_GET_ALL_BRAVO                    = URL_BRAVO_BASE + "/bravo";
    public static final String URL_GET_BRAVO_SEARCH                 = URL_BRAVO_BASE
                                                                            + "/bravo?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_COMMENT                      = URL_BRAVO_BASE + "/comment/{Comment_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_COMMENTS                     = URL_BRAVO_BASE + "/bravo/{Bravo_ID}/comments";
    public static final String URL_GET_FOLLOWING_CHECK              = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/following/{User_ID_Other}";
    public static final String URL_GET_MYLIST_ITEM                  = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/mylist/{Bravo_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_NEWS                         = URL_BRAVO_BASE + "/news/{News_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_NEWS_SEARCH                  = URL_BRAVO_BASE + "/news";                                                                            /* ?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params} */
    public static final String URL_GET_SPOT                         = URL_BRAVO_BASE + "/spot/{Spot_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_SPOT_SEARCH                  = URL_BRAVO_BASE
                                                                            + "/spot?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_SPOT_HISTORY                 = URL_BRAVO_BASE + "/spot/{Spot_ID}/history?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_SPOT_RANK                    = URL_BRAVO_BASE + "/spot/{Spot_ID}/rank?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_TSURETE                      = URL_BRAVO_BASE + "/bravo/{Bravo_ID}/tsurete?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_INFO                    = URL_BRAVO_BASE + "/user";
    public static final String URL_GET_USER_INFO_WITH_BRAVO_ACCOUNT = URL_BRAVO_BASE + "/user";
    public static final String URL_GET_USER_BLOCKING                = URL_BRAVO_BASE + "/user/{User_ID}/blocking?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_FOLLOWING               = URL_BRAVO_BASE + "/user/{User_ID}/following?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_FLOWER                  = URL_BRAVO_BASE + "/user/{User_ID}/followers?User_ID={str}&Access_Token={str}";

    public static final String URL_GET_USER_FOLLOW_HISTORY          = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/follow?User_ID={str}&Access_Token={str}&params=JSON_Encoded(Start:{int})";
    public static final String URL_GET_USER_SEARCH                  = URL_BRAVO_BASE
                                                                            + "/user?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_USER_MYLIST                  = URL_BRAVO_BASE + "/user/{User_ID}/mylist?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_TIMELINE                = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/history?User_ID={str}&Access_Token={str}&params=JSON_Encoded(Start:{int})";

    public static final String URL_GET_TIMELINE                     = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/timeline?User_ID={str}&Access_Token={str}&params=JSON_Encoded(Start:{int})";
    public static final String URL_GET_RANK                         = URL_BRAVO_BASE + "/rank?User_ID={str}&Access_Token={str}";

    public static final String URL_POST_BRAVO                       = URL_BRAVO_BASE + "/bravo/{Bravo_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_POST_BRAVO_SNS                   = URL_BRAVO_BASE + "/bravo/{str}/sns?User_ID={str}&Access_Token={str}";
    public static final String URL_POST_COMMENT                     = URL_BRAVO_BASE + "/comment?User_ID={User_ID}&Access_Token={Access_Token}";
    public static final String URL_POST_SPOTS                       = URL_BRAVO_BASE + "/spot?User_ID={str}&Access_Token={str}";
    public static final String URL_POST_USER                        = URL_BRAVO_BASE + "/user";
    public static final String URL_POST_FORGOT                      = URL_BRAVO_BASE + "/forgot";
    public static final String URL_PUT_BLOCKING                     = URL_BRAVO_BASE + "/user/{User_ID}/blocking?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_FOLLOWING                    = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/following?User_ID={User_ID}&Access_Token={Access_Token}";
    public static final String URL_PUT_MYLIST                       = URL_BRAVO_BASE + "/user/{User_ID}/mylist?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_NOTIFICATION                 = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/notifications?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_REPORT                       = URL_BRAVO_BASE + "/report?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_SNS                          = URL_BRAVO_BASE + "/user/{User_ID}/sns?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_USER                         = URL_BRAVO_BASE + "/user/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_BLOCKING                  = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/blocking/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_BRAVO_PIC                 = URL_BRAVO_BASE
                                                                            + "/bravo/{Bravo_ID}/pic/{Pic_Index}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_COMMENT                   = URL_BRAVO_BASE + "/comment/{Comment_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_FOLLOWING                 = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/following/{User_ID_Other}?User_ID={User_ID}&Access_Token={Access_Token}";
    public static final String URL_DELETE_MYLIST                    = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/mylist/{Bravo_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_NOTIFICATION              = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/notifications/{Type}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_SNS                       = URL_BRAVO_BASE
                                                                            + "/user/{User_ID}/sns/{SNS_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_USER                      = "https://dev.sharebravo.com/user/{User_ID}?User_ID={User_ID}&Access_Token={Access_Token}";
    // public static final String URL_POST_BRAVO = URL_BRAVO_BASE+"/bravo/{Bravo_ID}?User_ID={str}&Access_Token={str}";
}
