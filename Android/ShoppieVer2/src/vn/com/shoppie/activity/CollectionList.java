package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.ListCollectionAdapter;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchCampaignItem;
import vn.com.shoppie.database.sobject.MerchCampaignList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.NetworkUtility;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.util.log;
import vn.com.shoppie.view.MyTextView;
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
	private static ListCollectionAdapter adapter;
	private static int listpie[];
	private static String listCampaignId[];
	private static String listCampaignName[];
	public static int curId = 0;
	public static boolean autoFinish = false;
	private ShoppieDBProvider mShoppieDBProvider;
	private ShoppieSharePref mSharePref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listcollection_activity);

		init();
	}

	private void init() {
		mShoppieDBProvider = new ShoppieDBProvider(this);
		mSharePref = new ShoppieSharePref(this);
		Bundle extras = getIntent().getExtras();
		merchantId = extras.getString(KEY_MERCHANT_ID);
		log.d("MechanId", merchantId);
		customerId = extras.getString(KEY_CUSTOMER_ID);
		iconLink = extras.getString(KEY_ICON);
		title = extras.getString(KEY_TITLE);
		titleDesc = extras.getString(KEY_DESC);
		number = extras.getString(KEY_NUMBER);

		listView = (ListView) findViewById(R.id.list);
		MyTextView text = new MyTextView(this);
		text.setBackgroundColor(0xffffffff);
		text.setText("abcd");
		text.setTextSize(getResources().getDimension(R.dimen.actionbar_title_textsize));
		text.setLight();
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater
				.inflate(R.layout.catelogy_title, null, false);

		View icon = headerView.findViewById(R.id.icon);
		MyTextView titleTv = (MyTextView) headerView.findViewById(R.id.catelogy);
		MyTextView subTitleTv = (MyTextView) headerView
				.findViewById(R.id.subcatelogy);
		MyTextView countTv = (MyTextView) headerView.findViewById(R.id.count);
		ImageLoader.getInstance(this).DisplayImage(
				WebServiceConfig.HEAD_IMAGE + iconLink, icon);
		titleTv.setText(title);
		subTitleTv.setText(titleDesc);
		countTv.setText(number);

		titleTv.setNormal();
		subTitleTv.setLight();
		countTv.setLight();
		
		listView.addHeaderView(headerView);
		listView.setDivider(null);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0
						&& NetworkUtility.getInstance(CollectionList.this)
								.isNetworkAvailable()) {
					curId = position - 1;
					Intent intent = new Intent(CollectionList.this,
							CatelogyDetailActivity.class);
					log.d("CPAID", "" + adapter.getItem(curId).getCampaignId());

					intent.putExtra(CatelogyDetailActivity.MERCH_ID_KEY,
							merchantId);
					intent.putExtra(CatelogyDetailActivity.CAMPAIGN_ID_KEY, ""
							+ adapter.getItem(curId).getCampaignId());
					intent.putExtra(CatelogyDetailActivity.CUSTOMER_ID_KEY,
							"" + mSharePref.getCustId());
					intent.putExtra(CatelogyDetailActivity.CAMPAIGN_NAME_KEY,
							adapter.getItem(curId).getCampaignName());
					intent.putExtra(CatelogyDetailActivity.LUCKY_PIE_KEY,
							adapter.getItem(curId).getLuckyPie());
					startActivity(intent);

					clearMemory();
				} else {
					showToast(getString(R.string.network_unvailable));
					finish();
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
		if(autoFinish) {
			autoFinish = false;
			finish();
		}
		if (NetworkUtility.getInstance(this).isNetworkAvailable())
			requestToGetMerchantCampaign(merchantId, customerId);
		else {
			showToast(getString(R.string.network_unvailable));
			finish();
		}
		// getMerchantCampaignFromDb();
	}

	private void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	private void setData(ArrayList<MerchCampaignItem> data) {
		adapter = new ListCollectionAdapter(this, data);
		listView.setAdapter(adapter);

		listpie = new int[adapter.getCount()];
		listCampaignId = new String[adapter.getCount()];
		listCampaignName = new String[adapter.getCount()];
		for (int i = 0; i < adapter.getCount(); i++) {
			listpie[i] = adapter.getItem(i).getLuckyPie();
			listCampaignId[i] = "" + adapter.getItem(i).getCampaignId();
			listCampaignName[i] = "" + adapter.getItem(i).getCampaignName();
		}
	}
	
	public static String getCurCampaignName() {
		return listCampaignName[curId];
	}

	public static int getCurPie() {
		return listpie[curId];
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

							setData(merchCampaignList.getResult());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void processIfResponseFail() {
						log.e("failed ", "failed");
						finish();
					}
				}, nameValuePairs, true);
		postUpdateStt.execute(WebServiceConfig.URL_MERCHANT_CAMPAIGN);

	}
}
