package com.lemon.fromangle.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes.Name;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class ParameterFactory {
	private static String TAG = "ParameterFactory";

	public static List<NameValuePair> createLoginParam(String email,
			String password) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("email", email));
		parameters.add(new BasicNameValuePair("password", password));

		return parameters;
	}

	public static List<NameValuePair> getMessageInfoFromServer(String userId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("user_id", userId));
		
		return parameters;
	}

	public static List<NameValuePair> createRegisterSettingParam(
			String useName, String tel, String email, String dateStr,
			String timeStr, String daysAfter) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("tel", tel));
		parameters.add(new BasicNameValuePair("mail", email));
		parameters.add(new BasicNameValuePair("user_name", useName));
		parameters.add(new BasicNameValuePair("day", dateStr));
		parameters.add(new BasicNameValuePair("time", timeStr));
		parameters.add(new BasicNameValuePair("days_after", daysAfter));

		return parameters;

	}

	public static List<NameValuePair> createEditRegisterSettingParam(
			String useName, String tel, String email, String dateStr,
			String timeStr, String daysAfter, String userId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("tel", tel));
		parameters.add(new BasicNameValuePair("mail", email));
		parameters.add(new BasicNameValuePair("user_name", useName));
		parameters.add(new BasicNameValuePair("day", dateStr));
		parameters.add(new BasicNameValuePair("time", timeStr));
		parameters.add(new BasicNameValuePair("days_after", daysAfter));
		parameters.add(new BasicNameValuePair("user_id", userId));

		return parameters;

	}

	public static List<NameValuePair> createUpdateSettingParam(String userId,
			String useName, String tel, String email, String dateStr,
			String timeStr, String daysAfter) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("user_id", userId));
		parameters.add(new BasicNameValuePair("tel", tel));
		parameters.add(new BasicNameValuePair("mail", email));
		parameters.add(new BasicNameValuePair("user_name", useName));
		parameters.add(new BasicNameValuePair("day", dateStr));
		parameters.add(new BasicNameValuePair("time", timeStr));
		parameters.add(new BasicNameValuePair("days_after", daysAfter));

		return parameters;

	}

	public static List<NameValuePair> createMessageSettingParams(String userId,
			String receive1, String email1, String message1, String receive2,
			String email2, String message2, String receive3, String email3,
			String message3,String status) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("user_id", userId));
		parameters.add(new BasicNameValuePair("receiver_1", receive1));
		parameters.add(new BasicNameValuePair("mail_1", email1));
		parameters.add(new BasicNameValuePair("message_1", message1));
		parameters.add(new BasicNameValuePair("receiver_2", receive2));
		parameters.add(new BasicNameValuePair("mail_2", email2));
		parameters.add(new BasicNameValuePair("message_2", message2));
		parameters.add(new BasicNameValuePair("receiver_3", receive3));
		parameters.add(new BasicNameValuePair("mail_3", email3));
		parameters.add(new BasicNameValuePair("message_3", message3));
		parameters.add(new BasicNameValuePair("status", status));

		return parameters;

	}

	public static List<NameValuePair> createCheckPayment(String userId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("user_id", userId));
		return parameters;

	}
	public static List<NameValuePair> createUpdatePayment(String userId,Date date, int status) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("user_id", userId));
		parameters.add(new BasicNameValuePair( "day_payment",date.toString()));
		parameters.add(new BasicNameValuePair( "expiry",date.toString()));
		return parameters;

	}

}
