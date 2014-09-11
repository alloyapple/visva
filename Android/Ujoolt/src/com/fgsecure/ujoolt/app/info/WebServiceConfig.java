/*
 * Name: $RCSfile: WebServiceConfig.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 2:37:15 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.fgsecure.ujoolt.app.info;

import com.fgsecure.ujoolt.app.define.LoginType;
import com.fgsecure.ujoolt.app.utillity.ConfigUtility;
import com.fgsecure.ujoolt.app.utillity.StringUtility;

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

	// ===================== UJOOLT =====================
	private static String APP_DOMAIN = "http://api.ujoolt.com/webservice/";
	// private static String APP_DOMAIN = "http://ns222115.ovh.net/webservice/";
	// private static String APP_DOMAIN =
	// "http://fruitysolution.no-ip.org:8085/ujoolt_dev2/webservice/";

	public static String URL_GET_JOLTS_FROM_LOCATION = APP_DOMAIN + "getLocalJolts";

	public static String URL_POST_JOLT = APP_DOMAIN + "send";

	public static String URL_POST_REGISTRATION = APP_DOMAIN + "createAndroid";

	public static String URL_CHECK_JOLT_AROUND = APP_DOMAIN + "checkJoltAround";

	public static String URL_DELETE_JOLT = APP_DOMAIN + "delete";

	public static String URL_REGISTER_UJOOLT = APP_DOMAIN + "registerUser";

	public static String URL_LOGIN_UJOOLT = APP_DOMAIN + "checkUser";

	public static String URL_GET_FAVOURITE_FROM_USER = APP_DOMAIN + "getFavoriteJoltFromUser";

	public static String URL_GET_REJOLT_FROM_ID = APP_DOMAIN + "getRejoltFromJoltId";

	// ===================== TWITTER =====================
	public static String URL_GET_LOCATION_URL_TWITPIC = "http://maptwits.com/mapapi.php";

	// ===================== FACEBOOK =====================
	public static String URL_FACEBOOK_GRAPHIC = "https://graph.facebook.com/";

	public static String URL_FACEBOOK_GET_PLACE = URL_FACEBOOK_GRAPHIC + "search?type=place";

	public static String URL_FACEBOOK_GET_LOCATION = URL_FACEBOOK_GRAPHIC
			+ "search?type=location&center=";

	public static String URL_FACEBOOK_GET_EVENT = URL_FACEBOOK_GRAPHIC + "me/events?access_token=";

	public static String URL_FACEBOOK_GET_OBJECT_WITH_LOCATION = URL_FACEBOOK_GRAPHIC
			+ "me/locations?access_token=";

	// ==============================INSTAGRAM===============================
	public static String URL_GET_JOLTS_FROM_INSTAGRAM = "https://api.instagram.com/v1/media/search";

	// ===================== PARAMETER =====================
	public static String PARAM_TOKEN = "accessToken";

	public static String PARAM_USER_ACCESS_TOKEN = "accessToken";

	public static String PARAM_USER_NAME = "username";

	public static String PARAM_PASSWORD = "password";

	public static String PARAM_EMAIL = "email";

	public static String PARAM_CURRENT_PASSWORD = "oldPassword";

	public static String PARAM_NEW_PASSWORD = "newPassword";

	public static String PARAM_JOLT_ID = "id";

	public static String PARAM_DEVICE_ID = "device_id";

	public static String PARAM_TEXT = "text";

	public static String PARAM_LATITUE = "latitude";

	public static String PARAM_LONGITUDE = "longitude";

	public static String PARAM_NICK = "nick";

	public static String PARAM_DATE = "date";

	public static String PARAM_DATE_SRC = "date_src";

	public static String PARAM_PHOTO_URL = "photo_url";

	public static String PARAM_VIDEO_URL = "video_url";

	public static String PARAM_NB_REJOLT = "nb_rejolt";

	public static String PARAM_DISTANCE = "distance";

	public static String PARAM_RADIUS = "radius";

	public static String PARAM_LIFETIME = "lifetime";

	// public static String PARAM_ADDRESS = "address";

	public static String PARAM_LOGIN_TYPE = "loginType";

	public static String PARAM_LOGIN_USERID = "loginUserid";

	public static String PARAM_TOP = "isTop";

	public static String PARAM_PUBLIC = "isPublic";

	public static String PARAM_INSTAGRAM_ID = "instagram_id";

	public static String PARAM_FACEBOOK_ID = "facebook_id";

	// ====================== RESPONSE ================
	public static String RESPONSE_LOGIN_FAIL = "false";
	public static String RESPONSE_REGISTER_EXIST_EMAIL = "User is avaiable";
	public static String RESPONSE_REGISTER_SUCCESS = "success";
	public static String RESPONSE_REGISTER_FAIL = "";

	// METHOD

	public static String urlRejolt(int latitudeE6, int longitudeE6, String deviceId, String nick,
			String id, String loginUserId, LoginType loginType) {
		return (URL_POST_JOLT + "?lat=" + latitudeE6 + "&long=" + longitudeE6 + "&device_id="
				+ deviceId + "&nick=" + encode(nick) + "&id=" + id + "&loginUserid=" + loginUserId
				+ "&loginType=" + LoginType.getString(loginType));
	}

	public static String urlCheckSyncUser(String id) {
		return (APP_DOMAIN + "checkSyncUser?id=" + id);
	}

	public static String urlSyncUser(String ujooltId, String facebookId, String twitterId,
			LoginType loginType) {
		if (ujooltId.equals("")) {
			ujooltId = "0";
		}
		if (facebookId.equals("")) {
			facebookId = "0";
		}
		if (twitterId.equals("")) {
			twitterId = "0";
		}
		String type = LoginType.getString(loginType);
		String url = APP_DOMAIN + "syncUser?ujooltId=" + ujooltId + "&facebookId=" + facebookId
				+ "&twitterId=" + twitterId + "&type=" + type;
		// Log.e("ws", "url sync user: " + url);
		return url;
	}

	public static String urlGetJoltsFromLocation(int lati, int longi, boolean isFirstLoad,
			String keyword) {
		double lat = (double) lati / 1E6;
		double lon = (double) longi / 1E6;
		long date = ConfigUtility.getCurTimeStamp();
		String url = APP_DOMAIN + "getLocalJolts?lat=" + lat + "&long=" + lon + "&date=" + date
				+ "&isFirstLoad=" + isFirstLoad + "&keyword=" + encode(keyword)
				+ "&searchType=jolt";
		return url;
	}

	public static String urlRequestNotification(String id, String userName) {
		return (APP_DOMAIN + "notify?id=" + id + "&userName=" + encode(userName));
	}

	public static String urlPostFavourite(String joltId, String loginUserId, boolean isLike) {
		return (APP_DOMAIN + "favorite?joltId=" + joltId + "&loginUserid=" + loginUserId
				+ "&isLike=" + isLike);
	}

	private static String encode(String value) {
		if (value != null) {
			return StringUtility.encode(value);
		} else {
			return "";
		}
	}
}
