/*
 * Name: $RCSfile: ParameterFactory.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 2:45:36 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.fgsecure.ujoolt.app.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.fgsecure.ujoolt.app.info.WebServiceConfig;
import com.fgsecure.ujoolt.app.utillity.DeviceConfig;

/**
 * ParameterFactory class builds parameters for web service apis
 * 
 * @author Lemon
 */
public final class ParameterFactory {

	/**
	 * Create sign in parameters
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public static List<NameValuePair> createSignInParams(String userName, String password) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_USER_NAME, userName));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_PASSWORD, password));
		return parameters;
	}

	public static List<NameValuePair> createPostJoltParams(String lati, String longi, String text,
			String device_id, String nick, String loginType, String loginUserid, String instagramID) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("lat", lati));
		parameters.add(new BasicNameValuePair("long", longi));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_TEXT, text));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_DEVICE_ID, device_id));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_NICK, nick));
		// parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_ADDRESS,
		// address));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_LOGIN_TYPE, loginType));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_LOGIN_USERID, loginUserid));
		if (!instagramID.equalsIgnoreCase("")) {
			parameters
					.add(new BasicNameValuePair(WebServiceConfig.PARAM_INSTAGRAM_ID, instagramID));
		}
		String temp = "";
		for (int i = 0; i < parameters.size(); i++) {
			temp += parameters.get(i).toString();
		}
		Log.e("tomato", "temp: " + temp);
		Log.e("tomato", "temp Post Jolt param : " + parameters.toString());
		return parameters;
	}

	public static List<NameValuePair> createPostRegistrationParams(Context context,
			String registrationId, int lati, int longi, boolean isActive, int badge) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("udid", DeviceConfig.getDeviceId(context)));
		parameters.add(new BasicNameValuePair("register_id", registrationId));
		parameters.add(new BasicNameValuePair("lat", "" + lati));
		parameters.add(new BasicNameValuePair("long", "" + longi));
		parameters.add(new BasicNameValuePair("isactivate", "" + isActive));
		parameters.add(new BasicNameValuePair("badge", "" + badge));
		return parameters;
	}

	public static List<NameValuePair> createPostRejoltParams(String lati, String longi,
			String device_id, String nick, String id, String type, String loginId) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("lat", lati));
		parameters.add(new BasicNameValuePair("long", longi));

		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_DEVICE_ID, device_id));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_NICK, nick));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_JOLT_ID, id));
		// parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_ADDRESS,
		// address));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_LOGIN_TYPE, type));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_LOGIN_USERID, loginId));
		return parameters;
	}

	public static List<NameValuePair> createDeleteJoltParams(String id) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_JOLT_ID, id));
		return parameters;
	}

	/**
	 * Create forgot password parameters
	 * 
	 * @param email
	 * @return
	 */
	public static List<NameValuePair> createForgotAccountParams(String email) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_EMAIL, email));
		return parameters;
	}

	/**
	 * Create Change Password parameters
	 * 
	 * @param userId
	 * @param currentPassword
	 * @param newPassword
	 * 
	 * @return
	 */
	public static List<NameValuePair> createChangePasswordParams(String accessToken,
			String currentPassword, String newPassword) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters
				.add(new BasicNameValuePair(WebServiceConfig.PARAM_USER_ACCESS_TOKEN, accessToken));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_CURRENT_PASSWORD,
				currentPassword));
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_NEW_PASSWORD, newPassword));

		return parameters;
	}

	/**
	 * Create AccessToken parameters
	 * 
	 * @param accessToken
	 * @return
	 */
	public static List<NameValuePair> createAccessTokenParams(String accessToken) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(WebServiceConfig.PARAM_TOKEN, accessToken));
		return parameters;
	}

}
