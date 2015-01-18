package com.gurusolution.android.hangman2.utils;

import android.net.Uri;

public class Utils {

	public static final String PREFIX_TAG = "VISVA_";
	public static final String URL_LINk = "https://play.google.com/store/apps/developer?id=Visva";
	public static final String URL_FB_LINK = "https://play.google.com/store/apps/details?id=com.facebook.katana";

	public static Uri getUrlMoreGame() {
		return Uri.parse(URL_LINk);
	}

	public static Uri getUrlFacebook() {
		return Uri.parse(URL_FB_LINK);
	}
}
