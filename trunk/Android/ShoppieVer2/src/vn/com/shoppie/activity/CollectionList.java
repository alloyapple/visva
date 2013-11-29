package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.adapter.ListCollectionAdapter;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchCampaignItem;
import vn.com.shoppie.database.sobject.MerchCampaignList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class CollectionList extends Activity {

	public static final String KEY_MERCHANT_ID = "id";
	public static final String KEY_CUSTOMER_ID = "cid";
	public static final String KEY_ICON = "icon";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DESC = "desc";
	public static final String KEY_NUMBER = "number";

	private String merchantId = "";
	private String customerId = "";
	private String iconLink = "";
	private String title = "";
	private String titleDesc = "";
	private String number = "";

	private ListView listView;
	private ListCollectionAdapter adapter;
	private static String listCampaignId[];
	public static int curId = 0;
	private ShoppieDBProvider mShoppieDBProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listcollection_activity);

		init();
	}

	private void init() {
		mShoppieDBProvider = new ShoppieDBProvider(this);
		Bundle extras = getIntent().getExtras();
		merchantId = extras.getString(KEY_MERCHANT_ID);
		customerId = extras.getString(KEY_CUSTOMER_ID);
		iconLink = extras.getString(KEY_ICON);
		title = extras.getString(KEY_TITLE);
		titleDesc = extras.getString(KEY_DESC);
		number = extras.getString(KEY_NUMBER);

		listView = (ListView) findViewById(R.id.list);
		TextView text = new TextView(this);
		text.setBackgroundColor(0xffffffff);
		text.setText("abcd");

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater
				.inflate(R.layout.catelogy_title, null, false);

		View icon = headerView.findViewById(R.id.icon);
		TextView titleTv = (TextView) headerView.findViewById(R.id.catelogy);
		TextView subTitleTv = (TextView) headerView
				.findViewById(R.id.subcatelogy);
		TextView countTv = (TextView) headerView.findViewById(R.id.count); 
		ImageLoader.getInstance(this).DisplayImage(
				CatelogyAdapter.URL_HEADER + iconLink, icon);
		titleTv.setText(title);
		subTitleTv.setText(titleDesc);
		countTv.setText(number);
		
		listView.addHeaderView(headerView);
		listView.setDivider(null);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					curId = position - 1;
					Intent intent = new Intent(CollectionList.this,
							CatelogyDetailActivity.class);
					Log.d("CPAID", "" + adapter.getItem(curId).getCampaignId());
					intent.putExtra(CatelogyDetailActivity.CAMPAIGN_ID_KEY, ""
							+ adapter.getItem(curId).getCampaignId());
					intent.putExtra(CatelogyDetailActivity.CUSTOMER_ID_KEY,
							"148");
					intent.putExtra(CatelogyDetailActivity.CAMPAIGN_NAME_KEY,
							adapter.getItem(curId).getCampaignName());

					startActivity(intent);

					clearMemory();
				}
			}
		});
	}

	private void clearMemory() {
		adapter.clear();
		listView.setAdapter(null);
		adapter = null;
	}

	public void onCLickBackBtn(View v) {
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (NetworkUtility.getInstance(this).isNetworkAvailable())
			requestToGetMerchantCampaign(merchantId, customerId);
		else
			getMerchantCampaignFromDb();
	}

	private void getMerchantCampaignFromDb() {
		// TODO Auto-generated method stub
		JsonDataObject jsonDataObject = mShoppieDBProvider
				.getJsonData(GlobalValue.TYPE_CAMPAIGNS);
		String merchantCampaign = jsonDataObject.getJsonData();
		Log.e("adfdfh", "adfjh " + merchantCampaign);
		if (merchantCampaign != null && !"".equals(merchantCampaign))
			try {
				JSONObject jsonObject = new JSONObject(merchantCampaign);
				Gson gson = new Gson();
				MerchCampaignList merchCampaignList = gson.fromJson(
						jsonObject.toString(), MerchCampaignList.class);
				setData(merchCampaignList.getResult());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			showToast(getString(R.string.network_unvailable));
	}

	private void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private void setData(ArrayList<MerchCampaignItem> data) {
		adapter = new ListCollectionAdapter(this, data);
		listView.setAdapter(adapter);

		listCampaignId = new String[adapter.getCount()];
		for (int i = 0; i < adapter.getCount(); i++) {
			listCampaignId[i] = "" + adapter.getItem(i).getCampaignId();
		}
	}

	public static String getNextCampaignId() {
		if (curId == listCampaignId.length - 1) {
			curId = 0;
			return listCampaignId[0];
		} else {
			return listCampaignId[++curId];
		}
	}

	private void requestToGetMerchantCampaign(String merchCatId, String custId) {
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantCampaignValues(merchCatId, custId);
		AsyncHttpPost postUpdateStt = new AsyncHttpPost(CollectionList.this,
				new AsyncHttpResponseProcess(CollectionList.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchCampaignList merchCampaignList = gson
									.fromJson(jsonObject.toString(),
											MerchCampaignList.class);
							/** update to database */
							mShoppieDBProvider
									.deleteJsonData(GlobalValue.TYPE_CAMPAIGNS);
							JsonDataObject jsonDataObject = new JsonDataObject(
									response, GlobalValue.TYPE_CAMPAIGNS);
							mShoppieDBProvider.addNewJsonData(jsonDataObject);
							Log.e("adfdfh",
									"afdsf "
											+ mShoppieDBProvider
													.countJsonData(GlobalValue.TYPE_CAMPAIGNS));
							setData(merchCampaignList.getResult());
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
		postUpdateStt.execute(WebServiceConfig.URL_MERCHANT_CAMPAIGN);

	}
}
