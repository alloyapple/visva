package vn.com.shoppie.constant;

import android.content.Context;
import android.content.SharedPreferences;

public class ShoppieSharePref {

	private static final String CURRENT_BAL = "current_bal";
	private static final String LOGIN_TYPE = "login_type";
	private static final String IMAGE_AVATAR = "image_avatar";
	private static final String IMAGE_COVER = "image_cover";
	private static final String FRIEND_ID = "friendId";
	private static final String CUST_NAME = "cust_name";
	private static final String CUST_PHONE = "cust_phone";
	private static final String CUST_ADDRESS = "cust_address";
	private static final String CUST_EMAIL = "cust_email";
	private static final String GENDER = "gender";
	private static final String BIRTHDAY = "birth";
	private static final String COUNT_CHANGE_INFO_TIME = "count_change_info_time";
	private static final String COUNT_GET_PIE_TIME = "count_get_pie_time";
	private static final String CHECKIN_STATUS = "checkin_status";
	private static final String VALUE_PARAM_MOBILE = "value_param_mobile";
	private static final String VALUE_PARAM_SUPPORT_LINK = "value_param_mobile_link";
	private static final String VALUE_PARAM_TERM_LINK = "value_param_term_link";
	private static final String VALUE_PARAM_EXPIRED_SLEEP_TIME = "sleep_time";
	private static final String ACTION_SHARE_FB = "action_share_fb";
	private static final String POST_LOGIN_FACEBOOK_SUCCESS = "post_login_fb_success";
	private static final String LOGIN_TO_SHOW_FRIEND_SUCCESS = "login_to_show_friend_success";
	private static final String COUNT_LIKE = "count_like";
	private static final String COUNT_SHARE = "count_share";
	private static final String IS_CHOOSE_AVATAR = "choose_image_from_gallery";
	private static final String IS_EDIT_COVER_SCROLLX = "edit_cover_scrollx";
	private static final String IS_EDIT_COVER_SCROLLY = "edit_cover_scrolly";
	private static final String IS_EDIT_COVER_SCALE = "edit_cover_scale";
	private static final String IS_EDIT_COVER_SCALE_CENTERX = "edit_cover_scale_x";
	private static final String IS_EDIT_COVER_SCALE_CENTERY = "edit_cover_scale_y";
	private static final String IS_EDIT_COVER_URI = "edit_cover_uri";
	// ================================================================
	private static final String START_NUM = "num1";
	private static final String LIKE_NUM = "like";

	private Context context;

	public ShoppieSharePref(Context context) {
		this.context = context;
	}

	public void setChooseImageAvatar(boolean b) {
		putBooleanValue(IS_CHOOSE_AVATAR, b);
	}

	public boolean getChooseImageAvatar() {
		return getBooleanValue(IS_CHOOSE_AVATAR);
	}

	public void setEditCoverScale(float scale) {
		putFloatValue(IS_EDIT_COVER_SCALE, scale);
	}
	
	public float getEditCoverScale() {
		return getFloatValue(IS_EDIT_COVER_SCALE);
	}
	
	public void setEditCoverScaleCenterX(float x) {
		putFloatValue(IS_EDIT_COVER_SCALE_CENTERX, x);
	}
	
	public float getEditCoverScaleCenterX() {
		return getFloatValue(IS_EDIT_COVER_SCALE_CENTERX);
	}
	
	public void setEditCoverScaleCenterY(float y) {
		putFloatValue(IS_EDIT_COVER_SCALE_CENTERY, y);
	}
	
	public float getEditCoverScaleCenterY() {
		return getFloatValue(IS_EDIT_COVER_SCALE_CENTERY);
	}
	
	public void setEditCoverUri(String uri) {
		putStringValue(IS_EDIT_COVER_URI, uri);
	}
	
	public String getEditCoverUri() {
		return getStringValue(IS_EDIT_COVER_URI);
	}
	
	public void setEditCoverScrollx(float scrollX) {
		putFloatValue(IS_EDIT_COVER_SCROLLX, scrollX);
	}
	
	public float getEditCoverScrollx() {
		return getFloatValue(IS_EDIT_COVER_SCROLLX);
	}
	
	public void setEditCoverScrolly(float scrollY) {
		putFloatValue(IS_EDIT_COVER_SCROLLY, scrollY);
	}
	
	public float getEditCoverScrolly() {
		return getFloatValue(IS_EDIT_COVER_SCROLLY);
	}
	
	public void setCountLike(int likeCount) {
		putIntValue(COUNT_LIKE, likeCount);
	}

	public int getCountLike() {
		return getIntValue(COUNT_LIKE);
	}

	public void setShareCount(int shareCount) {
		putIntValue(COUNT_SHARE, shareCount);
	}

	public int getShareCount() {
		return getIntValue(COUNT_SHARE);
	}

	public void setLoginToShowFriendSuccess(boolean b) {
		putBooleanValue(LOGIN_TO_SHOW_FRIEND_SUCCESS, b);
	}

	public boolean getLoginToShowFriendSuccess() {
		return getBooleanValue(LOGIN_TO_SHOW_FRIEND_SUCCESS);
	}

	public void setPostLoginFBSuccess(boolean b) {
		putBooleanValue(POST_LOGIN_FACEBOOK_SUCCESS, b);
	}

	public boolean getPostLoginFBSuccess() {
		return getBooleanValue(POST_LOGIN_FACEBOOK_SUCCESS);
	}

	public void setParamMobileLink(String link) {
		putStringValue(VALUE_PARAM_SUPPORT_LINK, link);
	}

	public String getParamMobileLink() {
		return getStringValue(VALUE_PARAM_SUPPORT_LINK);
	}

	public void setParamTermLink(String link) {
		putStringValue(VALUE_PARAM_TERM_LINK, link);
	}
	
	public String getParamTermLink() {
		return getStringValue(VALUE_PARAM_TERM_LINK);
	}
	
	public void setParamSleepTime(int day) {
		putIntValue(VALUE_PARAM_EXPIRED_SLEEP_TIME, day);
	}
	
	public int getParamSleepTime() {
		return getIntValue(VALUE_PARAM_EXPIRED_SLEEP_TIME);
	}
	
	public void setActionShareFB(boolean b) {
		putBooleanValue(ACTION_SHARE_FB, b);
	}

	public boolean getActionShareFB() {
		return getBooleanValue(ACTION_SHARE_FB);
	}

	public void setValueParammMobile(String value) {
		putStringValue(VALUE_PARAM_MOBILE, value);
	}

	public String getValueParamMobile() {
		return getStringValue(VALUE_PARAM_MOBILE);
	}

	public void setCheckinStatus(int b) {
		putIntValue(CHECKIN_STATUS, b);
	}

	public int getCheckinStatus() {
		return getIntValue(CHECKIN_STATUS);
	}

	public void setCountChangeInfoTime(int time) {
		putIntValue(COUNT_CHANGE_INFO_TIME, time);
	}

	public int getCoutnChangeInfoTime() {
		return getIntValue(COUNT_CHANGE_INFO_TIME);
	}
	
	public void setCountGetPieTime(int time) {
		putIntValue(COUNT_GET_PIE_TIME, time);
	}

	public int getCountGetPieTime() {
		return getIntValue(COUNT_GET_PIE_TIME);
	}

	public void setCustName(String name) {
		putStringValue(CUST_NAME, name);
	}

	public String getCustName() {
		return getStringValue(CUST_NAME);
	}

	public void setCustAddress(String adddress) {
		putStringValue(CUST_ADDRESS, adddress);
	}

	public String getCustAddress() {
		return getStringValue(CUST_ADDRESS);
	}

	public void setPhone(String phone) {
		putStringValue(CUST_PHONE, phone);
	}

	public String getPhone() {
		return getStringValue(CUST_PHONE);
	}

	public void setBirthDay(String birth) {
		putStringValue(BIRTHDAY, birth);
	}

	public String getBirthDay() {
		return getStringValue(BIRTHDAY);
	}

	public void setGender(int gender) {
		putIntValue(GENDER, gender);
	}

	public int getGender() {
		return getIntValue(GENDER);
	}

	public void setEmail(String email) {
		putStringValue(CUST_EMAIL, email);
	}

	public String getEmail() {
		return getStringValue(CUST_EMAIL);
	}

	public void setFriendId(String friendId) {
		putStringValue(FRIEND_ID, friendId);
	}

	public String getFriendId() {
		return getStringValue(FRIEND_ID);
	}

	public void setLoginType(boolean typeLogin) {
		putBooleanValue(LOGIN_TYPE, typeLogin);
	}

	public boolean getLoginType() {
		return getBooleanValue(LOGIN_TYPE);
	}

	public void setImageAvatar(String imageAvatarPath) {
		putStringValue(IMAGE_AVATAR, imageAvatarPath);
	}

	public String getImageAvatar() {
		return getStringValue(IMAGE_AVATAR);
	}

	public void setImageCover(String imageCover) {
		putStringValue(IMAGE_COVER, imageCover);
	}

	public String getImageCover() {
		return getStringValue(IMAGE_COVER);
	}

	public void setCurrentBtl(int currentBal) {
		putIntValue(CURRENT_BAL, currentBal);
	}

	public int getCurrentBal() {
		return getIntValue(CURRENT_BAL);
	}

	public void setCustId(int userId) {
		putIntValue(GlobalValue.USER_ID, userId);
	}

	public int getCustId() {
		return getIntValue(GlobalValue.USER_ID);
	}

	public void addStartCount() {
		int count = getIntValue(START_NUM);
		count++;
		putIntValue(START_NUM, count);
	}

	public int getStartCount() {
		return getIntValue(START_NUM);
	}

	public void addLikeCount() {
		int count = getIntValue(LIKE_NUM);
		count++;
		putIntValue(LIKE_NUM, count);
	}

	public int getLikeCount() {
		return getIntValue(LIKE_NUM);
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
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
		return pref.getInt(key, 0);
	}

	/**
	 * Save an string to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putStringValue(String key, String s) {
		// SmartLog.log(TAG, "Set string value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
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
	public String getStringValue(String key) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
		return pref.getString(key, "");
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getStringValue(String key, String defaultValue) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
		return pref.getString(key, defaultValue);
	}

	/**
	 * Save an boolean to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putBooleanValue(String key, Boolean b) {
		// SmartLog.log(TAG, "Set boolean value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
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
	public boolean getBooleanValue(String key) {
		// SmartLog.log(TAG, "Get boolean value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	/**
	 * Save an float to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putFloatValue(String key, float f) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
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
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.SHOPIE_PREFERENCES, 0);
		return pref.getFloat(key, 0.0f);
	}
}
