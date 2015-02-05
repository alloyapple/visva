package com.visva.android.visvasdklibrary.network;

import org.json.JSONException;
import org.json.JSONObject;

public class ParserUtility {
	/**
	 * Extract user information
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static String getStringValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? "" : obj.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}

	/**
	 * Get long value
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static long getLongValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? 0L : obj.getLong(key);
		} catch (JSONException e) {
			return 0L;
		}
	}

	/**
	 * Get int value
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static int getIntValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? 0 : obj.getInt(key);
		} catch (JSONException e) {
			return 0;
		}
	}

	/**
	 * Get Double
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static Double getDoubleValue(JSONObject obj, String key) {
		double d = 0.0;
		try {
			return obj.isNull(key) ? d : obj.getDouble(key);
		} catch (JSONException e) {
			return d;
		}
	}

	/**
	 * Get boolean
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static boolean getBooleanValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? false : obj.getBoolean(key);
		} catch (JSONException e) {
			return false;
		}
	}

}
