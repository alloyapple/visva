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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class ActivityGiftTransaction extends Activity{

	private ListView listView;
	private LinearLayout content;
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
		
		content = (LinearLayout) findViewById(R.id.listcontent);
		
		updateListGift();
	}
	
	private void setData(List<GiftItem> listItem) {
		GiftItem onTopItem = null;
		for(int i = 0 ; i < listItem.size() ; i++) {
			if(listItem.get(i).getViewtop().equals("Y")
					|| listItem.get(i).getViewtop().equals("y")) {
				onTopItem = listItem.get(i);
				listItem.remove(i);
				break;
			}
		}
		
		if(onTopItem != null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View headerView = inflater.inflate(R.layout.gift_header, null, false);
			View image = headerView.findViewById(R.id.image);
//			listView.addHeaderView(headerView);
			
			CoverLoader.getInstance(this).DisplayImage(CatelogyAdapter.URL_HEADER + onTopItem.getGiftImage(), image);
			
			content.addView(headerView);
			content.addView(new View(this) , - 1 , (int) getResources().getDimension(R.dimen.gift_item_padding));
		}
		
		GiftAdapter adapter = new GiftAdapter(this, listItem);
//		listView.setAdapter(adapter);
		
		for(int i = 0 ; i < adapter.getCount() ; i++) {
			content.addView(adapter.getView(i, null, null));
			content.addView(new View(this) , - 1 , (int) getResources().getDimension(R.dimen.gift_item_padding));
		}
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
								setData(giftList.getGifts());
//								for(int i = 0 ;i < giftList.getGifts().size();i++){
//									Log.e("sadfdfjh", "asdfdjhf "+giftList.getGifts().get(i).getGiftName());
//								}
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

	public void onClickBack(View v) {
		this.finish();
	}
}
