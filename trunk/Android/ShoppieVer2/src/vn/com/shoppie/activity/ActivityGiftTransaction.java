package vn.com.shoppie.activity;

import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.adapter.GiftAdapter;
import vn.com.shoppie.database.sobject.GiftItem;
import vn.com.shoppie.database.sobject.GiftList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.util.CoverLoader;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class ActivityGiftTransaction extends Activity{

	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_activity);
		
		init();
	}
	
	private void init() {
		listView = (ListView) findViewById(R.id.list);
		
		listView.setDividerHeight((int) getResources().getDimension(R.dimen.gift_item_padding));
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater.inflate(R.layout.gift_header, null, false);
		
		listView.addHeaderView(headerView);
		
		Vector<GiftItem> listItem = new Vector<GiftItem>();
		for(int i = 0 ; i < 31 ; i++) {
			listItem.add(new GiftItem());
		}
		GiftAdapter adapter = new GiftAdapter(this, listItem);
		listView.setAdapter(adapter);
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
