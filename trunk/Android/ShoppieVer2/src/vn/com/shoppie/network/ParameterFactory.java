package vn.com.shoppie.network;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class ParameterFactory {
	private static String TAG = "ParameterFactory";

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

	public static List<NameValuePair> updateCampaignViewd(String campaignId,
			String custId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("campaignId", campaignId));
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		return nameValuePairs;
	}

	// private void updateCampaignView(String campaignId, String custId) {
	// // TODO Auto-generated method stub
	// List<NameValuePair> nameValuePairs = ParameterFactory
	// .updateCampaignViewd(campaignId,custId);
	// AsyncHttpPost postUpdateStt = new AsyncHttpPost(HomeActivity.this,
	// new AsyncHttpResponseProcess(HomeActivity.this) {
	// @Override
	// public void processIfResponseSuccess(String response) {
	// try {
	// JSONObject jsonObject = new JSONObject(response);
	// Gson gson = new Gson();
	// StatusUpdateView statusUpdateView = gson.fromJson(
	// jsonObject.toString(),
	// StatusUpdateView.class);
	// Log.e("adkjfhd", "sizeaa "
	// + statusUpdateView.getResult().getValue());
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
	// postUpdateStt.execute(WebServiceConfig.URL_UPDATE_CAMPAIGN_VIEW);
	//
	// }

	public static List<NameValuePair> getMerchantProduct(String campaignId,
			String custId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("campignId", campaignId));
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		return nameValuePairs;
	}

	public static List<NameValuePair> updateLuckyPie(String campaignId,
			String custId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("campaignId", campaignId));
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		return nameValuePairs;
	}

	public static List<NameValuePair> getMerchantStores(String custId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		return nameValuePairs;
	}

	public static List<NameValuePair> updateFriends(String custId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		return nameValuePairs;
	}

	public static List<NameValuePair> updateHistoryTransaction(String custId) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		return nameValuePairs;
	}

	public static List<NameValuePair> getGiftList() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		return nameValuePairs;
	}
}
