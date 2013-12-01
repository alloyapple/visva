package vn.com.shoppie.activity;

import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.adapter.GiftAdapter;
import vn.com.shoppie.adapter.GiftAdapter.OnClickItem;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShopieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.GiftItem;
import vn.com.shoppie.database.sobject.GiftList;
import vn.com.shoppie.database.sobject.GiftRedeemItem;
import vn.com.shoppie.database.sobject.ShoppieObject;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.NetworkUtility;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.JsonDataObject;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
	private ImageLoader imageLoader;

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

		listView = (ListView) findViewById(R.id.list);

		listView.setDividerHeight((int) getResources().getDimension(
				R.dimen.gift_item_padding));

		content = (LinearLayout) findViewById(R.id.listcontent);

		if (NetworkUtility.getInstance(this).isNetworkAvailable())
			updateListGift();
		else
			updateListGiftFromDB();

//		updateGiftListAvailable("22", "99", "123", "30", "10", "275", "20000");
	}

	private void updateGiftListAvailable(String merchId, String storeId,
			String custId, String giftId, String redeemQty, String pieQty,
			String giftPrice) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getGiftListAvailable(merchId, storeId, custId, giftId,
						redeemQty, pieQty, giftPrice);
		AsyncHttpPost postGetGiftList = new AsyncHttpPost(
				ActivityGiftTransaction.this, new AsyncHttpResponseProcess(
						ActivityGiftTransaction.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							System.out.println(response);
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							GiftRedeemItem redeemItem = gson.fromJson(
									jsonObject.toString(), GiftRedeemItem.class);
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

	private void updateListGiftFromDB() {
		// TODO Auto-generated method stub
		JsonDataObject jsonDataObject = mShoppieDBProvider
				.getJsonData(GlobalValue.TYPE_GIFT);
		String merchantGift = jsonDataObject.getJsonData();
		if (merchantGift != null && !"".equals(merchantGift))
			try {
				JSONObject jsonObject = new JSONObject(merchantGift);
				Gson gson = new Gson();
				GiftList giftList = gson.fromJson(jsonObject.toString(),
						GiftList.class);
				setData(giftList.getGifts());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			showToast(getString(R.string.network_unvailable));
	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}

	private void setData(List<GiftItem> listItem) {
		GiftItem onTopItem = null;
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

			ImageLoader.getInstance(this).DisplayImage(
					CatelogyAdapter.URL_HEADER + onTopItem.getGiftImage(),
					image);
			content.addView(headerView);
			content.addView(new View(this), -1, (int) getResources()
					.getDimension(R.dimen.gift_item_padding));
		}

		Vector<GiftItem> item0 = new Vector<GiftItem>();
		Vector<GiftItem> item1 = new Vector<GiftItem>();
		for(int i = 0 ; i < listItem.size() ; i++) {
			if(true) {
				item0.add(listItem.get(i));
			}
			else {
				item1.add(listItem.get(i));
			}
		}
		
		adapter = new GiftAdapter(this, item0 , GiftAdapter.TYPE_INVAI);
		// listView.setAdapter(adapter);

		for (int i = 0; i < adapter.getCount(); i++) {
			content.addView(adapter.getView(i, null, null));
			content.addView(new View(this), -1, (int) getResources()
					.getDimension(R.dimen.gift_item_padding));
		}
		
		adapter1 = new GiftAdapter(this, item1 , GiftAdapter.TYPE_AVAI);
		// listView.setAdapter(adapter);

		for (int i = 0; i < adapter1.getCount(); i++) {
			content.addView(adapter1.getView(i, null, null));
			content.addView(new View(this), -1, (int) getResources()
					.getDimension(R.dimen.gift_item_padding));
		}

		adapter1.setOnClickItem(new OnClickItem() {
			
			@Override
			public void onClickItem(GiftItem item) {
				// TODO Auto-generated method stub
				ShopieSharePref pref = new ShopieSharePref(getApplicationContext());
				updateGiftListAvailable(item.getMerchId(), "", "" + pref.getCustId()	, item.getGiftId(), item.getRedeemQty(), item.getPieQty(), item.getGiftPrice());
			}
		});
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		content.removeAllViews();
		adapter = null;
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
}
