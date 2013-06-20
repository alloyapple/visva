package com.lemon.fromangle.config;

/**
 * WebServiceConfig class contains web service settings
 * 
 * @author Lemon
 */
public final class WebServiceConfig {
	// Network time out: 60s
	public static int NETWORK_TIME_OUT = 60000;

	// result code for activity result
	public static int RESULT_OK = 1;

	public static String STRING_INVALID_TOKEN = "AccessToken invalid";

	public static String STRING_EXPIRE_TOKEN = "AccessToken expired";

	// ===================== DOMAIN ==========================//

	// TODO

	public static String PROTOCOL_HTTP = "http://";

	public static String PROTOCOL_HTTPS = "https:";

	public static String APP_DOMAIN = "http://49.212.184.41:7576/Angle/public/";
	// ===================== WEB SERVICE LINK ==================//

	public static String URL_LOGIN = APP_DOMAIN + "login";
	public static String URL_REGISTER = APP_DOMAIN + "register";
	public static String URL_RESET_PASSWORD = APP_DOMAIN + "reset_password";
	public static String URL_REGISTER_SETTING = APP_DOMAIN
			+ "setting_registration";
	public static String URL_UPDATE_REGISTER_SETTING = APP_DOMAIN
			+ "edit_setting_registration";
	public static String URL_CHECK_PAYMENT = APP_DOMAIN + "check_payment";
	public static String URL_UPDATE_PAYMENT = APP_DOMAIN + "update_payment";
	public static String URL_MESSAGE_SETTING = APP_DOMAIN
			+ "message_setting_registration";
	public static String URL_CHECK_USER_EXIT=APP_DOMAIN+"check_exist_user";
	// ===================== PARAMETER =======================//

}
