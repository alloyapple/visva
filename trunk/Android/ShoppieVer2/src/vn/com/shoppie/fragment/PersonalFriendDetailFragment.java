package vn.com.shoppie.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import vn.com.shoppie.R;
import vn.com.shoppie.activity.ActivityCustFavouriteProductShow;
import vn.com.shoppie.activity.ActivityFavouriteBrandShow;
import vn.com.shoppie.adapter.FavouriteAdapter;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.CheckedFBFriend;
import vn.com.shoppie.database.sobject.CustomerLikeBrandList;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.database.sobject.MerchantStoreList;
import vn.com.shoppie.database.sobject.CustomerLikeBrandList.MerchId;
import vn.com.shoppie.database.sobject.CustomerLikeProduct;
import vn.com.shoppie.database.sobject.CustomerLikeProductList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.FBUser;
import vn.com.shoppie.object.FavouriteDataObject;
import vn.com.shoppie.object.HorizontalListView;
import vn.com.shoppie.object.JsonDataObject;
import vn.com.shoppie.object.MyCircleImageView;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.view.MyTextView;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class PersonalFriendDetailFragment extends FragmentBasic {
	private ArrayList<FavouriteDataObject> mFavouriteProductObjects = new ArrayList<FavouriteDataObject>();
	private ArrayList<FavouriteDataObject> mFavouriteBrandObjects = new ArrayList<FavouriteDataObject>();
	private FavouriteAdapter mFavouriteProductAdapter;
	private FavouriteAdapter mFavouriteBrandAdapter;

	private ShoppieDBProvider mShoppieDBProvider;
	private MyTextView mUserName;
	private MyCircleImageView mUserAvatar;
	private HorizontalListView mFavouriteBrandList;
	private HorizontalListView mFavouriteProductList;
	private ImageLoader mImageLoader;
	private ShoppieSharePref mShoppieSharePref;

	private CustomerLikeProductList mCustomerLikeProductList;
	private ArrayList<MerchantStoreItem> mMerchantStoreItems = new ArrayList<MerchantStoreItem>();
	private ArrayList<MerchantStoreItem> mFavouriteStoreItems = new ArrayList<MerchantStoreItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_personal_friend_detail, null);
		mUserAvatar = (MyCircleImageView) root
				.findViewById(R.id.img_friend_avatar);
		mUserName = (MyTextView) root.findViewById(R.id.txt_personal_name);
		mFavouriteProductList = (HorizontalListView) root
				.findViewById(R.id.favourite_product_list);
		mFavouriteBrandList = (HorizontalListView) root
				.findViewById(R.id.favourite_brand_list);

		mFavouriteProductAdapter = new FavouriteAdapter(getActivity(),
				mFavouriteProductObjects);
		mFavouriteBrandAdapter = new FavouriteAdapter(getActivity(),
				mFavouriteBrandObjects);
		mFavouriteBrandList.setAdapter(mFavouriteBrandAdapter);
		mFavouriteProductList.setAdapter(mFavouriteProductAdapter);
		mFavouriteProductList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(getActivity(),
								ActivityCustFavouriteProductShow.class);
						intent.putExtra(GlobalValue.CUSTOMER_PRODUCT_ITEM,
								(CustomerLikeProduct) mCustomerLikeProductList
										.getResult().get(arg2));
						startActivity(intent);
					}
				});
		mFavouriteBrandList
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						ActivityFavouriteBrandShow.class);
				intent.putExtra(GlobalValue.MERCH_BRAND_ITEM,
						(MerchantStoreItem) mFavouriteStoreItems.get(arg2));
				startActivity(intent);
			}
		});
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mShoppieSharePref = new ShoppieSharePref(getActivity());
		mImageLoader = new ImageLoader(getActivity());
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mShoppieDBProvider = new ShoppieDBProvider(getActivity());
		mFavouriteBrandObjects = mShoppieDBProvider
				.getFavouriteData(GlobalValue.TYPE_FAVOURITE_BRAND);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void updateUser(FBUser friend) {
		// TODO Auto-generated method stub
		String userAvatar = friend.getUserAvatarLink().replace("_q.jpg",
				"_n.jpg");
		mImageLoader.DisplayImage(userAvatar, mUserAvatar);
		mUserName.setText(friend.getUserName());

		/** check is shoppie friend */
		checkIsShoppieFriend("" + mShoppieSharePref.getCustId(),
				friend.getUserId());
	}

	private void checkIsShoppieFriend(String custId, final String facebookId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.getCheckIsFriend(
				custId, facebookId);
		AsyncHttpPost postUpdateLuckyPie = new AsyncHttpPost(getActivity(),
				new AsyncHttpResponseProcess(getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							CheckedFBFriend checkedFBFriend = gson.fromJson(
									jsonObject.toString(),
									CheckedFBFriend.class);
							if (checkedFBFriend.getResult().size() > 0) {
								checkFriendInfo(""
										+ checkedFBFriend.getResult().get(0).getFriendId() , facebookId);
								mFavouriteBrandList.setVisibility(View.VISIBLE);
								mFavouriteProductList
										.setVisibility(View.VISIBLE);
							} else {
								mFavouriteBrandList.setVisibility(View.GONE);
								mFavouriteProductList.setVisibility(View.GONE);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, nameValuePairs, true);
		postUpdateLuckyPie.execute(WebServiceConfig.URL_CHECK_IS_FRIEND);

	}

	private void checkFriendInfo(String custId , String facebookId) {
		// TODO Auto-generated method stub
		System.out.println("abcdefgh " + custId + " " + facebookId);
		if(mFavouriteProductAdapter != null)
			mFavouriteProductAdapter.clear();
		if(mFavouriteBrandAdapter != null)
			mFavouriteBrandAdapter.clear();
		getCustomerLikeProductInfo(custId , facebookId);
		requestGetMerchantStores(custId , facebookId);
	}

	private void requestGetMerchantStores(final String custId , final String facebookId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantStores(custId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						/** update to database */
						mShoppieDBProvider
								.deleteJsonData(GlobalValue.TYPE_MERCH_STORE);
						JsonDataObject jsonDataObject = new JsonDataObject(
								response, GlobalValue.TYPE_MERCH_STORE, Integer
										.parseInt(custId));
						mShoppieDBProvider.addNewJsonData(jsonDataObject);
						requestGetMerchantStoresFromDB();
						getCustomerLikeBrandInfo(custId , facebookId);
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_MERCHANT_STORES);
	}

	private void getCustomerLikeProductInfo(String custId , String facebookId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getCustomerLikeProductInfo(custId, facebookId);
		AsyncHttpPost postUpdateLuckyPie = new AsyncHttpPost(getActivity(),
				new AsyncHttpResponseProcess(getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {

						try {
							mFavouriteProductObjects = new ArrayList<FavouriteDataObject>();
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							mCustomerLikeProductList = gson.fromJson(
									jsonObject.toString(),
									CustomerLikeProductList.class);
							for (int i = 0; i < mCustomerLikeProductList
									.getResult().size(); i++) {
								CustomerLikeProduct customerLikeProduct = mCustomerLikeProductList
										.getResult().get(i);
								FavouriteDataObject favouriteDataObject = new FavouriteDataObject(
										customerLikeProduct.getProductImage(),
										GlobalValue.TYPE_FAVOURITE_PRODUCT, ""
												+ customerLikeProduct
														.getProductId());
								mFavouriteProductObjects
										.add(favouriteDataObject);
							}
//							mFavouriteProductAdapter.notifyDataSetChanged();
							mFavouriteProductAdapter
									.updateBrandList(mFavouriteProductObjects);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, nameValuePairs, true);
		postUpdateLuckyPie
				.execute(WebServiceConfig.URL_CUSTOMER_LIKE_PRODUCT_INFO);

	}

	private void getCustomerLikeBrandInfo(String custId , String facebookId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getCustomerLikeProductInfo(custId , facebookId);
		AsyncHttpPost postUpdateLuckyPie = new AsyncHttpPost(getActivity(),
				new AsyncHttpResponseProcess(getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("adfdfh", "adsfkjd "+response);
						try {
							JSONObject jsonObject = new JSONObject(response);
							ArrayList<FavouriteDataObject> favouriteBrandObjects = new ArrayList<FavouriteDataObject>();
							Gson gson = new Gson();
							CustomerLikeBrandList customerLikeBrandList = gson
									.fromJson(jsonObject.toString(),
											CustomerLikeBrandList.class);
							Log.e("adfdfjh "+mMerchantStoreItems.size(), "asddfh "+customerLikeBrandList.getResult().get(0).getMerchId());
							ArrayList<FavouriteDataObject> data = new ArrayList<FavouriteDataObject>();
							if (customerLikeBrandList.getResult().size() > 0) {
								for (int i = 0; i < customerLikeBrandList.getResult().size(); i++) {
									boolean flag = true;
									for (int j = 0; j < mMerchantStoreItems.size() && flag; j++) {
										MerchId merchId = customerLikeBrandList.getResult().get(i);
										if (merchId.getMerchId().equals(String.valueOf(
												mMerchantStoreItems.get(j)
														.getMerchId()))) {
//											favouriteBrandObjects
//													.add(mFavouriteBrandObjects
//															.get(j));
											mFavouriteStoreItems.add(mMerchantStoreItems.get(j));
											data.add(new FavouriteDataObject(mMerchantStoreItems.get(j).getMerchLogo(), "", ""));
											flag = false;
										}
									}
								}
							}
							mFavouriteBrandAdapter
									.updateBrandList(data);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void processIfResponseFail() {
						Log.e("failed ", "failed");
					}
				}, nameValuePairs, true);
		postUpdateLuckyPie.execute(WebServiceConfig.URL_CUSTOMER_LIKE_BRAND_INFO);

	}

	private void requestGetMerchantStoresFromDB() {
		// TODO Auto-generated method stub
		JsonDataObject jsonDataObject = mShoppieDBProvider
				.getJsonDataByType(GlobalValue.TYPE_MERCH_STORE);
		String merchantStores = jsonDataObject.getJsonData();
		if (merchantStores != null && !"".equals(merchantStores))
			try {
				JSONObject jsonObject = new JSONObject(merchantStores);
				Gson gson = new Gson();
				MerchantStoreList merchantStoreList = gson.fromJson(
						jsonObject.toString(), MerchantStoreList.class);
				setStoreData(merchantStoreList.getResult());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void setStoreData(ArrayList<MerchantStoreItem> result) {
		// TODO Auto-generated method stub
		mMerchantStoreItems = result;
	}
}
