package com.sharebravo.bravo.utils;

public class BravoConstant {
    /* Login-Register */
    public static final String ACCESS_TYPE                    = "access_type";

    /* fragment id */
    public static final int    FRAGMENT_BRAVO_REGISTER_ID     = 1001;
    public static final int    FRAGMENT_LOGIN_ID              = FRAGMENT_BRAVO_REGISTER_ID + 1;
    public static final int    FRAGMENT_REGISTER_ID           = FRAGMENT_LOGIN_ID + 1;
    public static final int    FRAGMENT_REGISTER_USER_INFO_ID = FRAGMENT_REGISTER_ID + 1;
    public static final int    FRAGMENT_BRAVO_LOGIN_ID        = FRAGMENT_REGISTER_USER_INFO_ID + 1;
    public static final int    FRAGMENT_FORGOT_PASSWORD       = FRAGMENT_BRAVO_LOGIN_ID + 1;
}
