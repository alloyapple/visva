package com.visva.android.visvasdklibrary.util;

/**
 * This class define all global values for this sdk
 * 
 * @author kieu.thang
 *
 */
public class AllInOneConstant {

    /**
     * set the value to show the log for sdk
     */
    public static final boolean DEBUG_MODE = true;

    /**
     * set the TAG for all log in sdk
     */
    public static final String TAG = "AIOLog";

    /**
     * constant value for location provider
     */
    /*request type*/
    public static final int GET_LOCATION_TYPE_GOOGLE_SERVICE = 1;
    public static final int GET_LOCATION_TYPE_GPS = 2;
    public static final int GET_LOCATION_TYPE_NETWORK = 3;
    public static final int GET_LOCATION_TYPE_PASSIVE = 4;
    public static final int GET_LOCATION_TYPE_ADDRESS = 5;

    /*message key*/
    public static final int MSG_REQUEST_LOCATION_GOOGLESERVICE = 10;
    public static final int MSG_REQUEST_LOCATION_GPS = 11;
    public static final int MSG_REQUEST_LOCATION_NETWORK = 12;
    public static final int MSG_REQUEST_LOCATION_PASSIVE = 13;
    public static final int MSG_REQUEST_GET_ADDRESS = 18;
    public static final int MSG_UPDATE_LOCATION = 14;
    public static final int MSG_UPDATE_LOCATION_GPS = 15;
    public static final int MSG_UPDATE_LOCATION_NETWORK = 16;
    public static final int MSG_UPDATE_LOCATION_PASSIVE = 17;
}
