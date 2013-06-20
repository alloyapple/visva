package com.lemon.fromangle.config;

/**
 * GlobalValue class contains global static values
 * 
 * @author Lemon
 */
public final class GlobalValue {

	public static boolean DEBUG_MODE = true;
	public static final String FROM_ANGLE_PREFERENCES = "FROM_ANGLE_PREFERENCES";
	public static final String IS_RUN_FROM_ACTIVITY = "isRunFromActivity";
	public static String userId = "12";
	public static String userEmail = "";

	public static final int DIALOG_FAILED_TO_CONNECT_SERVER = 0;

	public static final int MSG_REPONSE_SUCESS = 1;
	public static final int MSG_CHECK_USER_EXIST = 0;
	public static final int MSG_REPONSE_FAILED = 2;
	public static final int MSG_REPONSE_OTHER = 3;
	public static final String PARAM_USER_ID = "id";
	public static final String PARAM_DATA = "data";
	public static final String PARAM_USER_NAME = "user_name";
	public static final String PARAM_ERROR = "error";

	public static final int MSG_REPONSE_PAID_NOT_EXPIRED = 1;
	public static final int MSG_REPONSE_PAID_EXPIRED = 0;
	public static final int MSG_REPONSE_NOT_PAID = 2;
	public static final int MSG_REPONSE_PAID_SUCCESS = 3;

	public static final int MSG_RESPONSE_UPDATE_INFO_SUCESS = 1;
	public static final int MSG_RESPONSE_UPDATE_INFO_FAILED = 2;

	public static final int MSG_RESPONSE_MSG_SETTING_SUCESS = 1;
	public static final int MSG_RESPONSE_MSG_SETTING_FAILED = 0;
	public static final int MSG_RESPONSE_MSG_SETING_CHANGE_SUCESS = 2;

	public static final int MSG_RESPONSE_CHECK_USER_EXIST_SUCESS = 1;
	public static final int MSG_RESPONSE_CHECK_USER_EXIST_FAILED = 0;

	public static final String APP_STATUS_OK = "1";
	public static final String APP_STATUS_NG = "ng!";
	public static final String APP_STATUS_STOP = "0";

	public static final int KEY_DESTROYED_SERVICE_BY_CANCEL = 1;
	public static final int KEY_DESTROYED_SERVICE_BY_OK = 2;
	public static final int KEY_DESTROYED_SERVICE_BY_BACK = 3;
	public static final int KEY_DESTROYED_SERVICE_BY_ON_FINISH = 4;
	public static final int KEY_DESTROYED_SERVICE_BY_FORCE_CLOSE = 5;
}
