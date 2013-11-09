package vn.com.shoppie.network;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes.Name;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class ParameterFactory {
	private static String TAG = "ParameterFactory";

	public static List<NameValuePair> createRegisterSettingParam(
			String useName, String device_id, String tel, String email,
			String dateStr, String timeStr, String daysAfter) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("tel", tel));
		parameters.add(new BasicNameValuePair("device_id", device_id));
		parameters.add(new BasicNameValuePair("mail", email));
		parameters.add(new BasicNameValuePair("user_name", useName));
		parameters.add(new BasicNameValuePair("day", dateStr));
		parameters.add(new BasicNameValuePair("time", timeStr));
		parameters.add(new BasicNameValuePair("days_after", daysAfter));

		return parameters;
	}

	public static List<NameValuePair> createRegisterSPAccount(
			String deviceToken, String bluetoothId, String deviceId,
			String latitude, String longitude, String custName) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("deviceToken", deviceToken));
		nameValuePairs.add(new BasicNameValuePair("deviceType", "Android"));
		nameValuePairs.add(new BasicNameValuePair("custCode", bluetoothId));
		nameValuePairs.add(new BasicNameValuePair("deviceId", deviceId));
		nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
		nameValuePairs.add(new BasicNameValuePair("longtitude", longitude));
		nameValuePairs.add(new BasicNameValuePair("custName", custName));
		return nameValuePairs;
	}
	
	public static List<NameValuePair> getMerchantCategoryValue(){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return nameValuePairs;
	}
}
