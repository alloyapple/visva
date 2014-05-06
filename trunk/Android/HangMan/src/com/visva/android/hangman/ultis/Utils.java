package com.visva.android.hangman.ultis;

import android.net.Uri;

public class Utils {

	public static final String PREFIX_TAG = "VISVA_";
	public static final String URL_LINk = "https://play.google.com/store/apps/developer?id=Visva";

	public static Uri getUrlMoreGame() {
		return Uri.parse(URL_LINk);
	}
}
