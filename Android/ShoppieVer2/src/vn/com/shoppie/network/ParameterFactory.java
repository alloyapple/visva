package vn.com.shoppie.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.activity.HomeActivity;
import vn.com.shoppie.database.sobject.MerchCampaignList;
import vn.com.shoppie.database.sobject.MerchantCategoryList;
import vn.com.shoppie.database.sobject.StatusUpdateView;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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

	public static List<NameValuePair> getMerchantCategoryValue() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return nameValuePairs;
	}

	public static List<NameValuePair> getMerchantCampaignValues(
			String merchCatId, String custId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("merchCatId", merchCatId));
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		return nameValuePairs;
	}

	// private void requestToGetgetMerchantCampaign(String merchCatId,
	// String custId) {
	// List<NameValuePair> nameValuePairs = ParameterFactory
	// .getMerchantCampaignValues(merchCatId, custId);
	// AsyncHttpPost postUpdateStt = new AsyncHttpPost(HomeActivity.this,
	// new AsyncHttpResponseProcess(HomeActivity.this) {
	// @Override
	// public void processIfResponseSuccess(String response) {
	// Log.e("adkfj", "reponse " + response.toString());
	// try {
	// JSONObject jsonObject = new JSONObject(response);
	// Gson gson = new Gson();
	// MerchCampaignList merchCampaignList = gson
	// .fromJson(jsonObject.toString(),
	// MerchCampaignList.class);
	// Log.e("adkjfhd", "sizeaa "
	// + merchCampaignList.getResult().size());
	// for (int i = 0; i < merchCampaignList.getResult()
	// .size(); i++)
	// Log.e("adkjfhd", "asdfjd "
	// + merchCampaignList.getResult().get(i)
	// .getCampaignDesc());
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void processIfResponseFail() {
	// Log.e("failed ", "failed");
	// finish();
	// }
	// }, nameValuePairs, true);
	// postUpdateStt.execute(WebServiceConfig.URL_MERCHANT_CAMPAIGN);
	//
	// }

	public static List<NameValuePair> updateCampaignViewd(String campaignId,
			String custId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("campaignId", campaignId));
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		return nameValuePairs;
	}

//	private void updateCampaignView(String campaignId, String custId) {
//		// TODO Auto-generated method stub
//		List<NameValuePair> nameValuePairs = ParameterFactory
//				.updateCampaignViewd(campaignId,custId);
//		AsyncHttpPost postUpdateStt = new AsyncHttpPost(HomeActivity.this,
//				new AsyncHttpResponseProcess(HomeActivity.this) {
//					@Override
//					public void processIfResponseSuccess(String response) {
//						try {
//							JSONObject jsonObject = new JSONObject(response);
//							Gson gson = new Gson();
//							StatusUpdateView statusUpdateView = gson.fromJson(
//									jsonObject.toString(),
//									StatusUpdateView.class);
//							Log.e("adkjfhd", "sizeaa "
//									+ statusUpdateView.getResult().getValue());
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//					@Override
//					public void processIfResponseFail() {
//						Log.e("failed ", "failed");
//						finish();
//					}
//				}, nameValuePairs, true);
//		postUpdateStt.execute(WebServiceConfig.URL_UPDATE_CAMPAIGN_VIEW);
//
//	}
}
