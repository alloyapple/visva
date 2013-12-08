package vn.com.shoppie.activity;

import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.GiftAdapter;
import vn.com.shoppie.adapter.GiftAdapter.OnClickItem;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.GiftItem;
import vn.com.shoppie.database.sobject.GiftList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.NetworkUtility;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.JsonDataObject;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ActivityGiftTransaction extends Activity {

	private ListView listView;
	private LinearLayout content;
	private GiftAdapter adapter;
	private GiftAdapter adapter1;
	private ShoppieDBProvider mShoppieDBProvider;
	private ShoppieSharePref mSharePref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
      
		setContentView(R.layout.gift_activity);

		init();
	}

	private void init() {
		/** database */
		mShoppieDBProvider = new ShoppieDBProvider(this);
		mSharePref = new ShoppieSharePref(this);
		listView = (ListView) findViewById(R.id.list);

		listView.setDividerHeight((int) getResources().getDimension(
				R.dimen.gift_item_padding));

		content = (LinearLayout) findViewById(R.id.listcontent);

//		updateGiftListAvailable("22", "99", "123", "30", "10", "275", "20000");
	}

	
//	private void updateListGiftFromDB() {
//		// TODO Auto-generated method stub
//		JsonDataObject jsonDataObject = mShoppieDBProvider
//				.getJsonData(GlobalValue.TYPE_GIFT);
//		String merchantGift = jsonDataObject.getJsonData();
//		if (merchantGift != null && !"".equals(merchantGift))
//			try {
//				JSONObject jsonObject = new JSONObject(merchantGift);
//				Gson gson = new Gson();
//				GiftList giftList = gson.fromJson(jsonObject.toString(),
//						GiftList.class);
//				setData(giftList.getGifts());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		else
//			showToast(getString(R.string.network_unvailable));
//	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
	
	private GiftItem onTopItem = null;
	
	private void setData(List<GiftItem> listItem) {
		content.removeAllViews();
		
		for (int i = 0; i < listItem.size(); i++) {
			if (listItem.get(i).getViewtop().equals("Y")
					|| listItem.get(i).getViewtop().equals("y")) {
				onTopItem = listItem.get(i);
				listItem.remove(i);
				break;
			}
		}

		if (onTopItem != null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View headerView = inflater.inflate(R.layout.gift_header, null,
					false);
			View image = headerView.findViewById(R.id.image);
			// listView.addHeaderView(headerView);

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + WebServiceConfig.HEAD_IMAGE + onTopItem.getGiftImage());
			ImageLoader.getInstance(this).DisplayImage(
					WebServiceConfig.HEAD_IMAGE + onTopItem.getGiftImage(),
					image , true ,true,true,true,true,false);
			content.addView(headerView);
			content.addView(new View(this), -1, (int) getResources()
					.getDimension(R.dimen.gift_item_padding));
			
			headerView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mSharePref.getCurrentBal() >= onTopItem.getMinPie()) {
						currItem = onTopItem;
						startActivity(new Intent(getApplicationContext(), GiftDetailActivity.class));
					}
				}
			});
		}
		
		Vector<GiftItem> item0 = new Vector<GiftItem>();
		Vector<GiftItem> item1 = new Vector<GiftItem>();
		for(int i = 0 ; i < listItem.size() ; i++) {
			if(mSharePref.getCurrentBal() < listItem.get(i).getMinPie()) {
				item0.add(listItem.get(i));
			}
			else {
				item1.add(listItem.get(i));
			}
		}
		
		for(int i = 0 ; i < item0.size() ; i++) {
			for (int j = i + 1; j < item0.size(); j++) {
				if(item0.get(i).getMinPie() > item0.get(j).getMinPie()) {
					GiftItem itemi = item0.get(i);
					GiftItem itemj = item0.get(j);
					item0.remove(j);
					item0.remove(i);
					item0.add(i, itemj);
					item0.add(j, itemi);
				}
			}
		}
		
		for(int i = 0 ; i < item1.size() ; i++) {
			for (int j = i + 1; j < item1.size(); j++) {
				if(item1.get(i).getMinPie() > item1.get(j).getMinPie()) {
					GiftItem itemi = item1.get(i);
					GiftItem itemj = item1.get(j);
					item1.remove(j);
					item1.remove(i);
					item1.add(i, itemj);
					item1.add(j, itemi);
				}
			}
		}
		
		adapter1 = new GiftAdapter(this, item1 , GiftAdapter.TYPE_AVAI);

		if(item1.size() > 0) {
			for (int i = 0; i < adapter1.getCount(); i++) {
				content.addView(adapter1.getView(i, null, null));
				content.addView(new View(this), -1, (int) getResources()
						.getDimension(R.dimen.gift_item_padding));
			}
		}
		
		adapter1.setOnClickItem(new OnClickItem() {
			
			@Override
			public void onClickItem(GiftItem item) {
				// TODO Auto-generated method stub
				currItem = item;
				startActivity(new Intent(getApplicationContext(), GiftDetailActivity.class));
			}
		});
		
		adapter = new GiftAdapter(this, item0 , GiftAdapter.TYPE_INVAI);
		// listView.setAdapter(adapter);

		if(item0.size() > 0) {
			for (int i = 0; i < adapter.getCount(); i++) {
				content.addView(adapter.getView(i, null, null));
				content.addView(new View(this), -1, (int) getResources()
						.getDimension(R.dimen.gift_item_padding));
			}
		}
		
		adapter.setOnClickItem(new OnClickItem() {
			
			@Override
			public void onClickItem(GiftItem item) {
				// TODO Auto-generated method stub
				currItem = item;
				startActivity(new Intent(getApplicationContext(), GiftDetailActivity.class));
			}
		});
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (NetworkUtility.getInstance(this).isNetworkAvailable())
			updateListGift();
		else{
			showToast(getString(R.string.network_unvailable));
			finish();
		}
	}
	
	private void updateListGift() {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.getGiftList();
		AsyncHttpPost postGetGiftList = new AsyncHttpPost(
				ActivityGiftTransaction.this, new AsyncHttpResponseProcess(
						ActivityGiftTransaction.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							GiftList giftList = gson.fromJson(
									jsonObject.toString(), GiftList.class);
							/** update to database */
							mShoppieDBProvider
									.deleteJsonData(GlobalValue.TYPE_GIFT);
							JsonDataObject jsonDataObject = new JsonDataObject(
									response, GlobalValue.TYPE_GIFT);
							mShoppieDBProvider.addNewJsonData(jsonDataObject);
							setData(giftList.getGifts());
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
	
	public static GiftItem currItem;
}
