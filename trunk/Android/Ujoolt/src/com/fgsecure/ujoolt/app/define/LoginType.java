package com.fgsecure.ujoolt.app.define;

public enum LoginType {
	UJOOLT, FACEBOOK, TWITTER, NONE;

	public static String getString(LoginType loginType) {
		switch (loginType) {
		case UJOOLT:
			return "UJ";
		case FACEBOOK:
			return "FB";
		case TWITTER:
			return "TW";
		default:
			return "NONE";
		}
	}

	public static LoginType getLoginType(String loginType) {
		if (loginType.equalsIgnoreCase("UJ")) {
			return UJOOLT;
		} else if (loginType.equalsIgnoreCase("FB")) {
			return FACEBOOK;
		} else if (loginType.equalsIgnoreCase("TW")) {
			return TWITTER;
		} else {
			return NONE;
		}
	}
}
