package com.visva.android.socialstackwidget.util;

import com.visva.android.socialstackwidget.constant.GlobalContstant;

import android.util.Log;

public final class VisvaLog {
	public static void e(String TAG, String msg) {
		if (GlobalContstant.DEBUG_MODE) {
			Log.e(TAG, msg);
		}
	}

	public static void i(String TAG, String msg) {
		if (GlobalContstant.DEBUG_MODE) {
			Log.i(TAG, msg);
		}
	}

	public static void d(String TAG, String msg) {
		if (GlobalContstant.DEBUG_MODE) {
			Log.d(TAG, msg);
		}
	}

	public static void v(String TAG, String msg) {
		if (GlobalContstant.DEBUG_MODE) {
			Log.v(TAG, msg);
		}
	}
}
