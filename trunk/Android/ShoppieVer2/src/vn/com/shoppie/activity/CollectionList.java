package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.adapter.ListCollectionAdapter;
import vn.com.shoppie.database.sobject.MerchCampaignItem;
import vn.com.shoppie.database.sobject.MerchCampaignList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.util.CoverLoader;
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

import com.google.gson.Gson;

public class CollectionList extends Activity{

	public static final String KEY_MERCHANT_ID = "id";
	public static final String KEY_CUSTOMER_ID = "cid";
	public static final String KEY_ICON 	   = "icon";
	public static final String KEY_TITLE       = "title";
	public static final String KEY_DESC 	   = "desc";
	
	private String merchantId = "";
	private String customerId = "";
	private String iconLink = "";
	private String title = "";
	private String titleDesc = "";
	
	private ListView listView;
	private ListCollectionAdapter adapter;
	
	private static String listCampaignId[];
	public static int curId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listcollection_activity);

		init();
	}

	private void init() {
		
		Bundle extras = getIntent().getExtras();
		merchantId = extras.getString(KEY_MERCHANT_ID);
		customerId = extras.getString(KEY_CUSTOMER_ID);
		iconLink   = extras.getString(KEY_ICON);
		title      = extras.getString(KEY_TITLE);
		titleDesc  = extras.getString(KEY_DESC);
		
		listView = (ListView) findViewById(R.id.list);
		TextView text = new TextView(this);
		text.setBackgroundColor(0xffffffff);
		text.setText("abcd");

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater.inflate(R.layout.catelogy_title, null, false);
		
		View icon = headerView.findViewById(R.id.icon);
		TextView titleTv = (TextView) headerView.findViewById(R.id.catelogy);
		TextView subTitleTv = (TextView) headerView.findViewById(R.id.subcatelogy);
		CoverLoader.getInstance(this).DisplayImage(CatelogyAdapter.URL_HEADER + iconLink, icon);
		titleTv.setText(title);
		subTitleTv.setText(titleDesc);
		
		listView.addHeaderView(headerView);
		listView.setDivider(null);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position > 0) {
					curId = position - 1;
					Intent intent = new Intent(CollectionList.this, CatelogyDetailActivity.class);
					Log.d("CPAID", "" + adapter.getItem(curId).getCampaignId());
					intent.putExtra(CatelogyDetailActivity.CAMPAIGN_ID_KEY, "" + adapter.getItem(curId).getCampaignId());
					intent.putExtra(CatelogyDetailActivity.CUSTOMER_ID_KEY, "148");
					startActivity(intent);
				}
			}
		});
		requestToGetgetMerchantCampaign(merchantId, customerId);
	}

	private void setData(ArrayList<MerchCampaignItem> data) {
		adapter = new ListCollectionAdapter(this, data);
		listView.setAdapter(adapter);
		
		listCampaignId = new String[adapter.getCount()];
		for(int i = 0 ; i < adapter.getCount() ; i++) {
			listCampaignId[i] = "" + adapter.getItem(i).getCampaignId();
		}
	}
	
	public static String getNextCampaignId() {
		if(curId == listCampaignId.length - 1) {
			curId = 0;
			return listCampaignId[0];
		}
		else {
			return listCampaignId[++curId]; 
		}
	}
	
	private void requestToGetgetMerchantCampaign(String merchCatId,
			String custId) {
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantCampaignValues(merchCatId, custId);
		AsyncHttpPost postUpdateStt = new AsyncHttpPost(CollectionList.this,
				new AsyncHttpResponseProcess(CollectionList.this) {
			@Override
			public void processIfResponseSuccess(String response) {
				Log.e("adkfj", "reponse " + response.toString());
				try {
					JSONObject jsonObject = new JSONObject(response);
					Gson gson = new Gson();
					MerchCampaignList merchCampaignList = gson
							.fromJson(jsonObject.toString(),
									MerchCampaignList.class);
					Log.e("adkjfhd", "sizeaa "
							+ merchCampaignList.getResult().size());
					setData(merchCampaignList.getResult());
//					for (int i = 0; i < merchCampaignList.getResult()
//							.size(); i++)
//						Log.e("adkjfhd", "asdfjd "
//								+ merchCampaignList.getResult().get(i)
//								.getCampaignDesc());
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
