package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CollectionDetailAdapter;
import vn.com.shoppie.adapter.CollectionDetailAdapter.OnLikeListenner;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchProductItem;
import vn.com.shoppie.database.sobject.MerchProductList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.NetworkUtility;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.JsonDataObject;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.MPager.OnPageChange;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class CatelogyDetailActivity extends VisvaAbstractActivity {

	public static final String CAMPAIGN_NAME_KEY = "campaign_name";
	public static final String CAMPAIGN_ID_KEY = "campaign_id";
	public static final String CUSTOMER_ID_KEY = "cuttomer_id";
	public static final String LUCKY_PIE_KEY = "lucky_pie";

	private String camId = "";
	private String custId = "";
	private String camName = "";

	private MPager mPager;
	private CollectionDetailAdapter adapter;
	private ShoppieDBProvider mShoppieDBProvider;

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_catelogy_detail;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate() {
		if (Build.VERSION.SDK_INT >= 11)
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

		/** database */
		mShoppieDBProvider = new ShoppieDBProvider(this);

		ImageButton icon = (ImageButton) findViewById(R.id.actionbar_icon);
		icon.setBackgroundResource(R.drawable.ic_back);
		icon.setImageBitmap(null);

		mPager = (MPager) findViewById(R.id.pager);
		mPager.setCanbeExtended(false);

		Bundle extras = getIntent().getExtras();
		camId = extras.getString(CAMPAIGN_ID_KEY);
		custId = extras.getString(CUSTOMER_ID_KEY);
		camName = extras.getString(CAMPAIGN_NAME_KEY);

		// setup actionbar
		RelativeLayout actionBar = (RelativeLayout) findViewById(R.id.actionbar);
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.collection_detail_actionbar, null);

		TextView mTxtTitle = (TextView) v.findViewById(R.id.title);
		mTxtTitle.setTextSize(getResources().getDimension(
				R.dimen.actionbar_title_textsize));
		mTxtTitle.setText(camName);

		actionBar.addView(v, -1, -1);
		if (NetworkUtility.getInstance(this).isNetworkAvailable())
			requestupdateToGetMerchProducts(camId, custId);
		else
			requestGetMerchProductFromDB();

		/** like vs unlike(custid,productId) */
		likeProduct("", "");
		unlikeProduct("", "");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		adapter.freeAll();
	}
	
	private void unlikeProduct(String custId, String productId) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.likeProduct(
				custId, productId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				CatelogyDetailActivity.this, new AsyncHttpResponseProcess(
						CatelogyDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("like success ", "like success");
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
						finish();
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_UNLIKE_PRODUCT);

	}

	private void likeProduct(String custId, String productId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.likeProduct(
				custId, productId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				CatelogyDetailActivity.this, new AsyncHttpResponseProcess(
						CatelogyDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("like success ", "like success");
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
						finish();
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_LIKE_PRODUCT);

	}

	private void requestGetMerchProductFromDB() {
		// TODO Auto-generated method stub
		JsonDataObject jsonDataObject = mShoppieDBProvider
				.getJsonData(GlobalValue.TYPE_MERCH_PRODUCTS);
		String merchantProduct = jsonDataObject.getJsonData();
		if (merchantProduct != null && !"".equals(merchantProduct))
			try {
				JSONObject jsonObject = new JSONObject(merchantProduct);
				Gson gson = new Gson();
				MerchProductList merchProductList = gson.fromJson(
						jsonObject.toString(), MerchProductList.class);
				setAdapter(merchProductList.getResult());
			} catch (Exception e) {

			}
		else
			showToast(getString(R.string.network_unvailable));
	}

	private void setAdapter(ArrayList<MerchProductItem> data) {
		// if(adapter != null)
		// adapter.recycle();
		adapter = new CollectionDetailAdapter(this, mPager, data);
		adapter.id = CollectionList.curId;
		mPager.setAdapter(adapter);

		mPager.setOnPageChange(new OnPageChange() {

			@Override
			public void onChange(int pos) {
				if (pos == adapter.getCount() - 1) {
					String id = CollectionList.getNextCampaignId();
					if (id != null) {
						camId = id;
						adapter.freeAll();
						requestupdateToGetMerchProducts(camId, custId);
					}
				}
				else
					adapter.freeImage(pos);
			}
		});
		
		adapter.setOnLikeListenner(new OnLikeListenner() {
			
			@Override
			public void onLike(boolean liked, int productionId) {
				likeProduct(liked, productionId);
			}
		});
	}

	private void likeProduct(boolean liked , int pId) {
		Log.e("adfkjdfh likeProduct", "likeProduct "+liked+" productId "+pId);
	}
	
	private void requestupdateToGetMerchProducts(String campaignId,
			String custId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantProduct(campaignId, custId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				CatelogyDetailActivity.this, new AsyncHttpResponseProcess(
						CatelogyDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchProductList merchProductList = gson.fromJson(
									jsonObject.toString(),
									MerchProductList.class);
							/** update to database */
							mShoppieDBProvider
									.deleteJsonData(GlobalValue.TYPE_MERCH_PRODUCTS);
							JsonDataObject jsonDataObject = new JsonDataObject(
									response, GlobalValue.TYPE_MERCH_PRODUCTS);
							mShoppieDBProvider.addNewJsonData(jsonDataObject);
							setAdapter(merchProductList.getResult());
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
		postGetMerchantProducts.execute(WebServiceConfig.URL_MERCHANT_PRODUCT);
	}

}
