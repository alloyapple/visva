/*
 * Name: $RCSfile: MeyClubSharedPreferences.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 2:07:45 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.fgsecure.ujoolt.app.info;

import android.content.Context;
import android.content.SharedPreferences;

import com.fgsecure.ujoolt.app.define.LoginType;
import com.fgsecure.ujoolt.app.json.UserInfo;

/**
 * MeyClubSharedPreferences class which saves setting values
 * 
 * @author Lemon
 */
public class UjooltSharedPreferences {

	private final String UJOOLT_DROID_PREFERENCES = "UJOOLT_DROID_PREFERENCES";

	private final String APPLICATION_INSTALL_FIRST_TIME = "APPLICATION_INSTALL_FIRST_TIME";

	private final String LANGUAGE = "LANGUAGE";
	private final String IS_PUSH = "IS_PUSH";
	private final String IS_LOGIN = "IS_LOGIN";
	private final String REGISTRATION_ID = "REGISTRATION";

	private final String MAIN_USER_NAME = "MAIN_USER_NAME";
	private final String MAIN_USER_ID = "MAIN_USER_ID";
	private final String MAIN_USER_TYPE = "MAIN_USER_TYPE";

	private final String UJOOLT_USER_NAME = "UJOOLT_USER_NAME";
	private final String UJOOLT_USER_ID = "UJOOLT_USER_ID";

	private final String FACEBOOK_USER_NAME = "FACEBOOK_USER_NAME";
	private final String FACEBOOK_USER_ID = "FACEBOOK_USER_ID";
	private final String FACEBOOK_ACCESS_TOKEN = "FACEBOOK_ACCESS_TOKEN";

	private final String TWITTER_USER_NAME = "TWITTER_USER_NAME";
	private final String TWITTER_USER_ID = "TWITTER_USER_ID";

	private Context context;

	public UjooltSharedPreferences(Context context) {
		this.context = context;
	}

	public void putUserUjoolt(UserInfo userUjoolt) {
		if (userUjoolt != null) {
			putStringValue(UJOOLT_USER_NAME, userUjoolt.getUserName());
			putStringValue(UJOOLT_USER_ID, userUjoolt.getUserId());
		}
	}

	public void putMainUser(UserInfo mainUser) {
		if (mainUser != null) {
			putMainUserName(mainUser.getUserName());
			putMainUserId(mainUser.getUserId());
			putMainUserType(mainUser.getStringLoginType());
		}
	}

	public UserInfo getMainUser() {
		return new UserInfo(getMainUserName(), getMainUserId(), getMainUserType());
	}

	public UserInfo getUserUjoolt() {
		String name = getStringValue(UJOOLT_USER_NAME);
		String id = getStringValue(UJOOLT_USER_ID);
		return new UserInfo(name, id, LoginType.UJOOLT);
	}

	public void putFirstIstallStatus() {
		putBooleanValue(APPLICATION_INSTALL_FIRST_TIME, true);
	}

	public boolean getFirstInstallStatus() {
		return getBooleanValue(APPLICATION_INSTALL_FIRST_TIME);
	}

	public void putLoginStatus(boolean b) {
		putBooleanValue(IS_LOGIN, b);
	}

	public boolean getLoginStatus() {
		return getBooleanValue(IS_LOGIN);
	}

	public void putPushStatus(boolean b) {
		putBooleanValue(IS_PUSH, b);
	}

	public boolean getPushStatus() {
		return getBooleanValue(IS_PUSH);
	}

	public void putMainUserName(String name) {
		putStringValue(MAIN_USER_NAME, name);
	}

	public void putMainUserId(String userId) {
		putStringValue(MAIN_USER_ID, userId);
	}

	public void putMainUserType(String userType) {
		putStringValue(MAIN_USER_TYPE, userType);
	}

	public void putMainUserType(LoginType userType) {
		putStringValue(MAIN_USER_TYPE, LoginType.getString(userType));
	}

	public void putUserNameFaceook(String name) {
		putStringValue(FACEBOOK_USER_NAME, name);
	}

	public void putUserIdFacebook(String userId) {
		putStringValue(FACEBOOK_USER_ID, userId);
	}

	public void putAccessTokenFacebookUserName(String accessTokenFacebookUserName) {
		putStringValue(FACEBOOK_ACCESS_TOKEN, accessTokenFacebookUserName);
	}

	public void putUserNameTwitter(String name) {
		putStringValue(TWITTER_USER_NAME, name);
	}

	public void putUserIdTwitter(String userId) {
		putStringValue(TWITTER_USER_ID, userId);
	}

	public void putRegistrationId(String registrationId) {
		putStringValue(REGISTRATION_ID, registrationId);
	}

	public String getRegistrationId() {
		return getStringValue(REGISTRATION_ID);
	}

	public void putLanguage(boolean language) {
		putBooleanValue(LANGUAGE, language);
	}

	public boolean getLanguage() {
		return getBooleanValue(LANGUAGE);
	}

	public String getMainUserName() {
		return getStringValue(MAIN_USER_NAME);
	}

	public String getMainUserId() {
		return getStringValue(MAIN_USER_ID);
	}

	public LoginType getMainUserType() {
		return LoginType.getLoginType(getMainUserTypeInString());
	}

	public String getMainUserTypeInString() {
		return getStringValue(MAIN_USER_TYPE);
	}

	public String getUserNameFacebook() {
		return getStringValue(FACEBOOK_USER_NAME);
	}

	public String getUserIdFacebook() {
		return getStringValue(FACEBOOK_USER_ID);
	}

	public String getAccessTokenFacebook() {
		return getStringValue(FACEBOOK_ACCESS_TOKEN);
	}

	public String getUserNameTwitter() {
		return getStringValue(TWITTER_USER_NAME);
	}

	public String getUserIdTwitter() {
		return getStringValue(TWITTER_USER_ID);
	}

	// ======================== CORE FUNCTIONS ========================

	/**
	 * Save a long integer to SharedPreferences
	 * 
	 * @param key
	 * @param n
	 */
	public void putLongValue(String key, long n) {
		// SmartLog.log(TAG, "Set long integer value");
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, n);
		editor.commit();
	}

	/**
	 * Read a long integer to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public long getLongValue(String key) {
		// SmartLog.log(TAG, "Get long integer value");
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		return pref.getLong(key, 0);
	}

	/**
	 * Save an integer to SharedPreferences
	 * 
	 * @param key
	 * @param n
	 */
	public void putIntValue(String key, int n) {
		// SmartLog.log(TAG, "Set integer value");
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, n);
		editor.commit();
	}

	/**
	 * Read an integer to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public int getIntValue(String key) {
		// SmartLog.log(TAG, "Get integer value");
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		return pref.getInt(key, 0);
	}

	/**
	 * Save an string to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	private void putStringValue(String key, String s) {
		// SmartLog.log(TAG, "Set string value");
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, s);
		editor.commit();
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	private String getStringValue(String key) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		return pref.getString(key, "");
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	// public String getStringValue(String key, String defaultValue) {
	// // SmartLog.log(TAG, "Get string value");
	// SharedPreferences pref = context.getSharedPreferences(
	// GlobalValue.UJOOLT_DROID_PREFERENCES, 0);
	// return pref.getString(key, defaultValue);
	// }

	/**
	 * Save an boolean to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	private void putBooleanValue(String key, Boolean b) {
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	/**
	 * Read an boolean to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	private boolean getBooleanValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	/**
	 * Save an float to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putFloatValue(String key, float f) {
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putFloat(key, f);
		editor.commit();
	}

	/**
	 * Read an float to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public float getFloatValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(UJOOLT_DROID_PREFERENCES, 0);
		return pref.getFloat(key, 0.0f);
	}
}
