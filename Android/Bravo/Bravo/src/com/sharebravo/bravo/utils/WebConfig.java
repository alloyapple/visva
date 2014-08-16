package com.sharebravo.bravo.utils;

public class WebConfig {
    // Network time out: 60s
    public static int          NETWORK_TIME_OUT                     = 60000;
    // Server api
    public static final String URL_GET_NOTIFICATION                 = "https://dev1.sharebravo.com/api/notification/{Action_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_NOTIFICATION_SEARCH          = "https://dev1.sharebravo.com/api/notification?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_BLOCKING_CHECK               = "https://dev1.sharebravo.com/api/user/{User_ID}/blocking/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_BRAVO                        = "https://dev1.sharebravo.com/api/bravo/{Bravo_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_BRAVO_SEARCH                 = "https://dev1.sharebravo.com/api/bravo?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_COMMENT                      = "https://dev1.sharebravo.com/api/comment/{Comment_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_COMMENTS                     = "https://dev1.sharebravo.com/api/bravo/{Bravo_ID}/comments?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_FOLLOWING_CHECK              = "https://dev1.sharebravo.com/api/user/{User_ID}/following/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_MYLIST_ITEM                  = "https://dev1.sharebravo.com/api/user/{User_ID}/mylist/{Bravo_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_NEWS                         = "https://dev1.sharebravo.com/api/news/{News_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_NEWS_SEARCH                  = "https://dev1.sharebravo.com/api/news?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_SPOT                         = "https://dev1.sharebravo.com/api/spot/{Spot_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_SPOT_SEARCH                  = "https://dev1.sharebravo.com/api/spot?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_SPOT_HISTORY                 = "https://dev1.sharebravo.com/api/spot/{Spot_ID}/history?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_SPOT_RANK                    = "https://dev1.sharebravo.com/api/spot/{Spot_ID}/rank?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_TSURETE                      = "https://dev1.sharebravo.com/api/bravo/{Bravo_ID}/tsurete?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_INFO                    = "https://dev1.sharebravo.com/api/user/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_INFO_WITH_BRAVO_ACCOUNT = "https://dev1.sharebravo.com/api/user/{User_ID}?Email={str}&Password={str}";
    public static final String URL_GET_USER_BLOCKING                = "https://dev1.sharebravo.com/api/user/{User_ID}/blocking?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_FOLLOWING               = "https://dev1.sharebravo.com/api/user/{User_ID}/following?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_FLOWER                  = "https://dev1.sharebravo.com/api/user/{User_ID}/followers?User_ID={str}&Access_Token={str}";

    public static final String URL_GET_USER_FOLLOW_HISTORY          = "https://dev1.sharebravo.com/api/user/{User_ID}/follow?User_ID={str}&Access_Token={str}&params=JSON_Encoded(Start:{int})";
    public static final String URL_GET_USER_SEARCH                  = "https://dev1.sharebravo.com/api/user?User_ID={str}&Access_Token={str}&params={JSON_Encoded_Params}";
    public static final String URL_GET_USER_MYLIST                  = "https://dev1.sharebravo.com/api/user/{User_ID}/mylist?User_ID={str}&Access_Token={str}";
    public static final String URL_GET_USER_TIMELINE                = "https://dev1.sharebravo.com/api/user/{User_ID}/history?User_ID={str}&Access_Token={str}&params=JSON_Encoded(Start:{int})";

    public static final String URL_GET_TIMELINE                     = "https://dev1.sharebravo.com/api/user/{User_ID}/timeline?User_ID={str}&Access_Token={str}&params=JSON_Encoded(Start:{int})";
    public static final String URL_GET_RANK                         = "https://dev1.sharebravo.com/api/rank?User_ID={str}&Access_Token={str}";

    public static final String URL_POST_BRAVO                       = "https://dev1.sharebravo.com/api/bravo/{Bravo_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_POST_BRAVO_SNS                   = "https://dev1.sharebravo.com/api/bravo/{str}/sns?User_ID={str}&Access_Token={str}";
    public static final String URL_POST_COMMENT                     = "https://dev1.sharebravo.com/api/comment?User_ID={str}&Access_Token={str}";
    public static final String URL_POST_SPOTS                       = "https://dev1.sharebravo.com/api/spot?User_ID={str}&Access_Token={str}";
    public static final String URL_POST_USER                        = "https://dev1.sharebravo.com/api/user?params={JSON_Encoded(Full_Name:str, Email:str, Password:str, Time_Zone:str,...)}";
    public static final String URL_POST_FORGOT                      = "https://dev1.sharebravo.com/api/forgot?params={JSON_Encoded(Email:str)}";
    public static final String URL_PUT_BLOCKING                     = "https://dev1.sharebravo.com/api/user/{User_ID}/blocking?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_FOLLOWING                    = "https://dev1.sharebravo.com/api/user/{User_ID}/following?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_MYLIST                       = "https://dev1.sharebravo.com/api/user/{User_ID}/mylist?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_NOTIFICATION                 = "https://dev1.sharebravo.com/api/user/{User_ID}/notifications?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_REPORT                       = "https://dev1.sharebravo.com/api/report?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_SNS                          = "https://dev1.sharebravo.com/api/user/{User_ID}/sns?User_ID={str}&Access_Token={str}";
    public static final String URL_PUT_USER                         = "https://dev1.sharebravo.com/api/user/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_BLOCKING                  = "https://dev1.sharebravo.com/api/user/{User_ID}/blocking/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_BRAVO_PIC                 = "https://dev1.sharebravo.com/api/bravo/{Bravo_ID}/pic/{Pic_Index}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_COMMENT                   = "https://dev1.sharebravo.com/api/comment/{Comment_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_FOLLOWING                 = "https://dev1.sharebravo.com/api/user/{User_ID}/following/{User_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_MYLIST                    = "https://dev1.sharebravo.com/api/user/{User_ID}/mylist/{Bravo_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_NOTIFICATION              = "https://dev1.sharebravo.com/api/user/{User_ID}/notifications/{Type}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_SNS                       = "https://dev1.sharebravo.com/api/user/{User_ID}/sns/{SNS_ID}?User_ID={str}&Access_Token={str}";
    public static final String URL_DELETE_USER                      = "https://dev.sharebravo.com/user/{User_ID}?User_ID={str}&Access_Token={str}";
    // public static final String URL_POST_BRAVO = "https://dev1.sharebravo.com/api/bravo/{Bravo_ID}?User_ID={str}&Access_Token={str}";
}
