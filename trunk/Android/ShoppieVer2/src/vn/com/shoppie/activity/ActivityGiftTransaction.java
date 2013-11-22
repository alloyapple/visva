package vn.com.shoppie.activity;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.GiftList;
import vn.com.shoppie.database.sobject.StatusUpdatePie;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.webconfig.WebServiceConfig;

public class ActivityGiftTransaction extends VisvaAbstractActivity{

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return R.layout.page_change_gift;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		updateListGift();
	}

	private void updateListGift() {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = ParameterFactory.getGiftList(
					);
			AsyncHttpPost postGetGiftList = new AsyncHttpPost(ActivityGiftTransaction.this,
					new AsyncHttpResponseProcess(ActivityGiftTransaction.this) {
						@Override
						public void processIfResponseSuccess(String response) {
							try {
								JSONObject jsonObject = new JSONObject(response);
								Gson gson = new Gson();
								GiftList giftList = gson.fromJson(
										jsonObject.toString(),
										GiftList.class);
								Log.e("adkjfhd", "sizeaa "
										+ giftList.getGifts().size());
								for(int i = 0 ;i < giftList.getGifts().size();i++){
									Log.e("sadfdfjh", "asdfdjhf "+giftList.getGifts().get(i).getGiftName());
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void processIfResponseFail() {
							Log.e("failed ", "failed");
							finish();
						}
					}, nameValuePairs, true);
			postGetGiftList.execute(WebServiceConfig.URL_GET_GIFT_LIST);
	}

}
