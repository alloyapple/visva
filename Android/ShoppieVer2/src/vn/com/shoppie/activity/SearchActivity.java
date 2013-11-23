package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyIconAdapter;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShopieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.database.sobject.MerchantCategoryList;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.database.sobject.MerchantStoreList;
import vn.com.shoppie.fragment.SearchBrandDetailFragment;
import vn.com.shoppie.fragment.SearchBrandDetailFragment.IOnClickShowStoreDetail;
import vn.com.shoppie.fragment.SearchBrandFragment;
import vn.com.shoppie.fragment.SearchMapFragment;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.NetworkUtility;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.HorizontalListView;
import vn.com.shoppie.object.JsonDataObject;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;

public class SearchActivity extends FragmentActivity implements
		IOnClickShowStoreDetail {
	// ==========================Constant Define=================
	private static final String SEARCH_BRAND_FRAGMENT_STRING = "brand_fragment";
	private static final String SEARCH_BRAND_DETAIL_FRAGMENT_STRING = "brand_detail_fragment";
	private static final String SEARCH_MAP_FRAGMENT_STRING = "map_fragment";
	private static final int SEARCH_BRAND_FRAGMENT_ID = 2001;
	private static final int SEARCH_BRAND_DETAIL_FRAGMENT_ID = 2002;
	private static final int SEARCH_MAP_FRAGMENT_ID = 2003;
	// ===========================Control Define==================
	private SearchBrandFragment mSearchBrandFragment;
	private SearchBrandDetailFragment mSearchBrandDetailFragment;
	private SearchMapFragment mSearchMapFragment;
	private FragmentManager mFmManager;
	private FragmentTransaction mTransaction;
	private HorizontalListView mTitleSearchListView;
	private EditText edtSearch;
	// =========================Class Define ===================
	private ShoppieDBProvider mShoppieDBProvider;
	// =========================Variable Define==================
	private ArrayList<String> backstack = new ArrayList<String>();

	private Map<MerchantCategoryItem, Vector<MerchantStoreItem>> manageData = new HashMap<MerchantCategoryItem, Vector<MerchantStoreItem>>();
	private Map<MerchantStoreItem, MerchantCategoryItem> manageCategoryByStore = new HashMap<MerchantStoreItem, MerchantCategoryItem>();
	private Vector<MerchantCategoryItem> iconDataList = new Vector<MerchantCategoryItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_search_activity);

		intialize();
	}

	private void intialize() {
		// TODO Auto-generated method stub
		mShoppieDBProvider = new ShoppieDBProvider(this);
		mTitleSearchListView = (HorizontalListView) findViewById(R.id.list_title_search);

		mFmManager = getSupportFragmentManager();
		mSearchBrandFragment = (SearchBrandFragment) mFmManager
				.findFragmentById(R.id.search_brand_list_fragment);
		mSearchBrandDetailFragment = (SearchBrandDetailFragment) mFmManager
				.findFragmentById(R.id.search_brand_detail_fragment);

		mSearchMapFragment = (SearchMapFragment) mFmManager
				.findFragmentById(R.id.search_map_fragment);

		mSearchBrandDetailFragment.setListener(this);
		showFragment(SEARCH_BRAND_FRAGMENT_ID);
		mTransaction = hideFragment();
		mTransaction.show(mSearchBrandFragment);
		addToSBackStack(SEARCH_BRAND_FRAGMENT_STRING);
		mTransaction.commit();
		edtSearch = (EditText) findViewById(R.id.edt_search);
		edtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				mSearchBrandFragment.filter(edtSearch.getText().toString());
			}
		});

		/** request to server to get campaign category */
		if (NetworkUtility.getInstance(this).isNetworkAvailable()){
			Log.e("adfjfh", "adfdddddd ");
			requestToGetCampainCategory();
		}
		else {
			getMerchantCategoryFromDB();
		}
	}

	private void getMerchantCategoryFromDB() {
		// TODO Auto-generated method stub
		
		JsonDataObject jsonDataObject = mShoppieDBProvider
				.getJsonData(GlobalValue.TYPE_MERCHANT_CATEGORY);
		String merchantCategory = jsonDataObject.getJsonData();
		if (merchantCategory != null && !"".equals(merchantCategory))
			try {
				JSONObject jsonObject = new JSONObject(merchantCategory);
				Gson gson = new Gson();
				MerchantCategoryList merchantCategoryList = gson.fromJson(
						jsonObject.toString(), MerchantCategoryList.class);
				setIconAdapter(merchantCategoryList.getResult());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			showToast(getString(R.string.network_unvailable));
	}

	private void addToSBackStack(String tag) {
		int index = backstack.lastIndexOf(tag);
		if (index == -1) {
			backstack.add(tag);
			return;
		}
		try {
			if (!backstack.get(index - 1).equals(
					backstack.get(backstack.size() - 1))) {
				backstack.add(tag);
				return;
			}
		} catch (IndexOutOfBoundsException e) {

		}
		try {
			ArrayList<String> subStack = new ArrayList<String>(backstack);
			for (int i = 0; i < subStack.size(); i++) {
				if (i > index) {
					backstack.remove(index);
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	public void onClickBtnSearchMap(View v) {
		showFragment(SEARCH_MAP_FRAGMENT_ID);
	}

	private FragmentTransaction hideFragment() {
		mTransaction = mFmManager.beginTransaction();
		mTransaction.hide(mSearchBrandDetailFragment);
		mTransaction.hide(mSearchBrandFragment);
		mTransaction.hide(mSearchMapFragment);
		return mTransaction;
	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(SearchActivity.this, string, Toast.LENGTH_SHORT).show();
	}

	private void showFragment(int searchBrandFragmentId) {
		// TODO Auto-generated method stub
		switch (searchBrandFragmentId) {

		case SEARCH_BRAND_FRAGMENT_ID:
			mTransaction = hideFragment();
			mTransaction.show(mSearchBrandFragment);
			addToSBackStack(SEARCH_BRAND_FRAGMENT_STRING);
			mTransaction.commit();
			break;

		case SEARCH_BRAND_DETAIL_FRAGMENT_ID:
			mTransaction = hideFragment();
			mTransaction.show(mSearchBrandDetailFragment);
			addToSBackStack(SEARCH_BRAND_DETAIL_FRAGMENT_STRING);
			mTransaction.commit();
			break;

		case SEARCH_MAP_FRAGMENT_ID:
			mTransaction = hideFragment();
			mTransaction.show(mSearchMapFragment);
			addToSBackStack(SEARCH_MAP_FRAGMENT_STRING);
			mTransaction.commit();
			break;
		default:
			break;
		}
	}

	public void onClickBtnBack(View v) {
		finish();
	}

	@Override
	public void onBackPressed() {

		/*
		 * if (backstack.size() == 0) { if(mTopPanel.isOpen()){
		 * mTopPanel.setOpen(false, true); return; } super.onBackPressed();
		 * return; } if (backstack.size() == 1) { if
		 * (!backstack.get(0).equals(VIEW_HOME)) { showToast(backstack.get(0));
		 * mTransaction = hideFragment(); mTransaction.show(mFmHome);
		 * backstack.clear(); addToSBackStack(VIEW_HOME); mFmHome.refreshUI();
		 * mTransaction.commitAllowingStateLoss(); } else {
		 * if(mTopPanel.isOpen()){ mTopPanel.setOpen(false, true); return; }
		 * super.onBackPressed(); backstack.clear(); } return; }
		 */
		try {
			backstack.remove(backstack.size() - 1);
			if (backstack.size() == 0) {
				super.onBackPressed();
				return;
			}
		} catch (IndexOutOfBoundsException e) {
			super.onBackPressed();
			return;
		}
		String currentView = backstack.get(backstack.size() - 1);
		mTransaction = hideFragment();
		if (currentView.equals(SEARCH_BRAND_FRAGMENT_STRING)) {
			mTransaction.show(mSearchBrandFragment);
			// mMainPersonalInfoFragment.refreshUI();
		} else if (currentView.equals(SEARCH_BRAND_DETAIL_FRAGMENT_STRING)) {
			mTransaction.show(mSearchBrandDetailFragment);
			// mPersonalFriendFragment.
		} else if (currentView.equals(SEARCH_MAP_FRAGMENT_STRING)) {
			mTransaction.show(mSearchMapFragment);
			// mPersonalFriendFragment.
			// mPersonalFriendFragment.
		}
		mTransaction.commitAllowingStateLoss();
	}

	private void setIconAdapter(ArrayList<MerchantCategoryItem> catelogyList) {
		Log.d("Category Color", "Color start");
		for (int i = 0; i < catelogyList.size(); i++) {
			iconDataList.add(catelogyList.get(i));
			Log.d("Category Color", "Color "
					+ catelogyList.get(i).getLineColor());
		}

		CatelogyIconAdapter adapter = new CatelogyIconAdapter(this,
				catelogyList);
		mTitleSearchListView.setAdapter(adapter);

		mTitleSearchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setDataByIcon(iconDataList.get(position), false);
			}
		});

		ShopieSharePref mShopieSharePref = new ShopieSharePref(this);
		if (NetworkUtility.getInstance(this).isNetworkAvailable())
			requestGetMerchantStores(String.valueOf(mShopieSharePref
					.getCustId()));
		else
			requestGetMerchantStoresFromDB();
	}

	private void requestGetMerchantStoresFromDB() {
		// TODO Auto-generated method stub
		JsonDataObject jsonDataObject = mShoppieDBProvider
				.getJsonData(GlobalValue.TYPE_MERCH_STORE);
		String merchantStores = jsonDataObject.getJsonData();
		if (merchantStores != null && !"".equals(merchantStores))
			try {
				JSONObject jsonObject = new JSONObject(merchantStores);
				Gson gson = new Gson();
				MerchantStoreList merchantStoreList = gson
						.fromJson(jsonObject.toString(),
								MerchantStoreList.class);
				setStoreData(merchantStoreList.getResult());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			showToast(getString(R.string.network_unvailable));
	}

	private void requestToGetCampainCategory() {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantCategoryValue();
		AsyncHttpPost postCampaignCategory = new AsyncHttpPost(
				SearchActivity.this, new AsyncHttpResponseProcess(
						SearchActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchantCategoryList merchantCategoryList = gson
									.fromJson(jsonObject.toString(),
											MerchantCategoryList.class);
							setIconAdapter(merchantCategoryList.getResult());
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
		postCampaignCategory.execute(WebServiceConfig.URL_MERCHCAMPAIGNS);

	}

	private void setStoreData(ArrayList<MerchantStoreItem> data) {
		manageCategoryByStore.clear();
		setPieMap(data);

		manageData.clear();
		for (int i = 0; i < iconDataList.size(); i++) {
			manageData
					.put(iconDataList.get(i), new Vector<MerchantStoreItem>());
		}

		for (MerchantStoreItem merchantStoreItem : data) {
			for (int i = 0; i < iconDataList.size(); i++) {
				if (iconDataList.get(i).getMerchCatId() == merchantStoreItem
						.getMerchCatId()) {
					manageData.get(iconDataList.get(i)).add(merchantStoreItem);
					manageCategoryByStore.put(merchantStoreItem,
							iconDataList.get(i));
					break;
				}
			}
		}

		if (iconDataList.size() > 0)
			setDataByIcon(iconDataList.get(0), false);
	}

	public void setDataByIcon(MerchantCategoryItem icon, boolean isUpdateMap) {
		mSearchBrandFragment.setAdapter(manageData.get(icon));

		if (isUpdateMap) {
			setPieMap(manageData.get(icon));
		}
	}

	private void requestGetMerchantStores(String custId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantStores(custId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				SearchActivity.this, new AsyncHttpResponseProcess(
						SearchActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchantStoreList merchantStoreList = gson
									.fromJson(jsonObject.toString(),
											MerchantStoreList.class);
							/** update to database */
							mShoppieDBProvider
									.deleteJsonData(GlobalValue.TYPE_MERCH_STORE);
							JsonDataObject jsonDataObject = new JsonDataObject(
									response, GlobalValue.TYPE_MERCH_STORE);
							mShoppieDBProvider.addNewJsonData(jsonDataObject);
							setStoreData(merchantStoreList.getResult());
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
		postGetMerchantProducts.execute(WebServiceConfig.URL_MERCHANT_STORES);
	}

	@Override
	public void onClickViewStoreDetail(MerchantStoreItem store) {
		// TODO Auto-generated method stub
		showFragment(SEARCH_BRAND_DETAIL_FRAGMENT_ID);
		mSearchBrandDetailFragment.updateUI(store);
	}

	public void setPieMap(List<MerchantStoreItem> data) {
		mSearchMapFragment.updatePie(data);
	}

	public MerchantCategoryItem getCategoryByStore(MerchantStoreItem store) {
		return manageCategoryByStore.get(store);
	}
}
