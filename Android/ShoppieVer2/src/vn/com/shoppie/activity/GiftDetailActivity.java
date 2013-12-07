package vn.com.shoppie.activity;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.sobject.GiftItem;
import vn.com.shoppie.database.sobject.GiftRedeemItem;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class GiftDetailActivity extends Activity{
	private GiftItem item;
	private ShoppieSharePref mSharePref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_detail_activity);
		
		init();
	}

	private void init() {
		mSharePref = new ShoppieSharePref(this);
		item = ActivityGiftTransaction.currItem;
		
		((TextView) findViewById(R.id.desc)).setText(item.getDescription());
		ImageLoader.getInstance(this).DisplayImage(WebServiceConfig.HEAD_IMAGE + item.getGiftImage(), findViewById(R.id.image), true, true, false, false, true, false);
		
		String price[] = item.getPricesNotArr();
		int pie[] = item.getPiesNotArr();
		String text[] = new String[price.length];
		
		for(int i = 0 ; i < pie.length ; i++) {
			text[i] = item.getGiftName() + " " + price[i] + "VNĐ = " + pie[i] + " Pie"; 
		}
		
//		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, text);
		((Spinner) findViewById(R.id.spinner)).setAdapter(new MySpinnerAdapter(text));
	}
	
	public void onClickBack(View v) {
		finish();
	}
	
	private void updateGiftListAvailable(String merchId, String storeId,
			String custId, String giftId, String redeemQty, String pieQty,
			String giftPrice) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getGiftListAvailable(merchId, storeId, custId, giftId,
						redeemQty, pieQty, giftPrice);
		AsyncHttpPost postGetGiftList = new AsyncHttpPost(
				GiftDetailActivity.this, new AsyncHttpResponseProcess(
						GiftDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							System.out.println(response);
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							GiftRedeemItem redeemItem = gson.fromJson(
									jsonObject.toString(), GiftRedeemItem.class);
							finish();
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
		postGetGiftList.execute(WebServiceConfig.URL_GET_GIFT_TRANSACTION_AVAILABLE);
	}

	public void onClickDoiqua(View v) {
		updateGiftListAvailable(ActivityGiftTransaction.currItem.getMerchId(), "", mSharePref.getCustId() + "", ActivityGiftTransaction.currItem.getGiftId(), ActivityGiftTransaction.currItem.getRedeemQty(), ActivityGiftTransaction.currItem.getPieQty(), ActivityGiftTransaction.currItem.getGiftPrice());
	}
	
	class MySpinnerAdapter extends BaseAdapter {

		String data[];
		
		public MySpinnerAdapter(String data[]) {
			this.data = data;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.spinner_item, null);
			((TextView) convertView.findViewById(R.id.text)).setText(data[position]);
			return convertView;
		}
	}
}
