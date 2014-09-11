package com.fgsecure.ujoolt.app;

public final class CommonUtilities {
	public static final String SENDER_ID = "860064243219";
	public static String REGISTRATION_ID;

	public static boolean isRegistrationNull() {
		if (REGISTRATION_ID == null || REGISTRATION_ID.equalsIgnoreCase("")) {
			return true;
		} else {
			return false;
		}
	}
}