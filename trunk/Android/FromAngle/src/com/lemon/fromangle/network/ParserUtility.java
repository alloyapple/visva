package com.lemon.fromangle.network;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ParserUtility {
	// ==============FUNCTIONS TO EXTRACT OBJECT FROM JSON STRING=========//

	// ========================= CORE FUNCTIONS ===========================

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

	public static String JsonDateToDate(String jsonDate) {
		// "/Date(1321867151710)/"
		int idx1 = jsonDate.indexOf("(");
		int idx2 = jsonDate.indexOf(")");
		String s = jsonDate.substring(idx1 + 1, idx2);
		long l = Long.valueOf(s);
		Date result = new Date(l);
		int Year = result.getYear() + 1900;
		return result.getDate() + "/" + result.getMonth() + "/" + Year;
	}
}
