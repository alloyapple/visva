package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CollectionDetailAdapter;
import vn.com.shoppie.adapter.CollectionDetailAdapter.OnLikeListenner;
import vn.com.shoppie.adapter.CollectionDetailAdapter.OnPieListenner;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchProductItem;
import vn.com.shoppie.database.sobject.MerchProductList;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.database.sobject.MerchantCategoryList;
import vn.com.shoppie.database.sobject.StatusUpdatePie;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.NetworkUtility;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.FavouriteDataObject;
import vn.com.shoppie.object.JsonDataObject;
import vn.com.shoppie.util.FacebookUtil;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.MPager.OnPageChange;
import vn.com.shoppie.view.MyTextView;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class CatelogyDetailActivity extends VisvaAbstractActivity {

	public static final String MERCH_ID_KEY = "merch_id";
	public static final String CAMPAIGN_NAME_KEY = "campaign_name";
	public static final String CAMPAIGN_ID_KEY = "campaign_id";
	public static final String CUSTOMER_ID_KEY = "cuttomer_id";
	public static final String LUCKY_PIE_KEY = "lucky_pie";
	private static final String TAG = "CatelogyDetailActivity";

	private String merchId = "";
	private String camId = "";
	private String custId = "";
	private String camName = "";
	private int pie = 0;

	private MPager mPager;
	private CollectionDetailAdapter adapter;
	private ShoppieDBProvider mShoppieDBProvider;
	private ArrayList<MerchProductItem> mMerchProductItems = new ArrayList<MerchProductItem>();

	private TextView hint;
	private ShoppieSharePref mSharePref;

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_catelogy_detail;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (adapter != null)
			adapter.refresh(mPager.getCurrentItem());
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
		mSharePref = new ShoppieSharePref(this);

		hint = (TextView) findViewById(R.id.hint);
		int count = mSharePref.getLikeCount();
		if (count < 3) {
			hint.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hint.setVisibility(View.GONE);
					return false;
				}
			});
			hint.setVisibility(View.VISIBLE);
			mSharePref.addLikeCount();
		}

		ImageButton icon = (ImageButton) findViewById(R.id.actionbar_icon);
		icon.setBackgroundResource(R.drawable.ic_back);
		icon.setImageBitmap(null);

		mPager = (MPager) findViewById(R.id.pager);
		mPager.setCanbeExtended(false);

		Bundle extras = getIntent().getExtras();
		merchId = extras.getString(MERCH_ID_KEY);
		camId = extras.getString(CAMPAIGN_ID_KEY);
		custId = extras.getString(CUSTOMER_ID_KEY);
		camName = extras.getString(CAMPAIGN_NAME_KEY);
		pie = extras.getInt(LUCKY_PIE_KEY);
		System.out.println(">>>>>>>>>>>>>>>>>>>pie : " + pie);

		// setup actionbar
		RelativeLayout actionBar = (RelativeLayout) findViewById(R.id.actionbar);
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(R.layout.collection_detail_actionbar, null);

		MyTextView mTxtTitle = (MyTextView) v.findViewById(R.id.title);
		mTxtTitle.setTextSize(getResources().getDimension(
				R.dimen.actionbar_title_textsize));
		mTxtTitle.setText(camName);
		mTxtTitle.setLight();
		
		actionBar.addView(v, -1, -1);
		if (NetworkUtility.getInstance(this).isNetworkAvailable())
			requestupdateToGetMerchProducts(camId, custId);
		else {
			showToast(getString(R.string.network_unvailable));
			finish();
		}
		// requestGetMerchProductFromDB();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (adapter != null)
			adapter.freeAll();
	}

	public void onClickShowFavouritePersonal(View v) {
		Intent intent = new Intent(CatelogyDetailActivity.this, PersonalInfoActivity.class);
		intent.putExtra(GlobalValue.IS_SHOW_FAVOURITE, 1);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	private void unlikeProduct(String custId, final String productId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.likeProduct(
				custId, productId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				CatelogyDetailActivity.this, new AsyncHttpResponseProcess(
						CatelogyDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						// showToast("unlike success");
						mShoppieDBProvider.deleteFavouriteDataById(productId);
					}

					@Override
					public void processIfResponseFail() {
						showToast("unlike failed");
						// finish();
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_UNLIKE_PRODUCT);

	}

	private void likeProduct(String custId, final String productId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.likeProduct(
				custId, productId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				CatelogyDetailActivity.this, new AsyncHttpResponseProcess(
						CatelogyDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("like success ", "like success");
						MediaPlayer mPlayer = MediaPlayer.create(
								CatelogyDetailActivity.this, R.raw.sound_like2);
						if (mPlayer != null)
							mPlayer.start();

						/** add to favourite product */
						MerchProductItem merchProductItem = null;
						for (int i = 0; i < mMerchProductItems.size(); i++) {
							if (mMerchProductItems.get(i).getProductId() == Integer
									.parseInt(productId))
								merchProductItem = mMerchProductItems.get(i);
						}

						FavouriteDataObject favouriteDataObject = new FavouriteDataObject(
								merchProductItem.getProductImage(),
								GlobalValue.TYPE_FAVOURITE_PRODUCT, productId);
						if (merchProductItem.getLiked() == 1
								&& mShoppieDBProvider.countFavouriteDataItem(""
										+ merchProductItem.getProductId()) == 0) {
							mShoppieDBProvider
									.addNewFavouriteData(favouriteDataObject);
						}

						/** post to facebook like product */
						FacebookUtil.getInstance(self)
								.publishLikeProductInBackground(
										mSharePref.getCustName(),
										merchProductItem);
					}

					@Override
					public void processIfResponseFail() {
						showToast("like failed");
						// finish();
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_LIKE_PRODUCT);

	}

	private void setAdapter(ArrayList<MerchProductItem> data) {
		// if(adapter != null)
		// adapter.recycle();

		TextView mTxtTitle = (TextView) findViewById(R.id.title);
		mTxtTitle.setTextSize(getResources().getDimension(
				R.dimen.actionbar_title_textsize));
		mTxtTitle.setText(camName);

		adapter = new CollectionDetailAdapter(CatelogyDetailActivity.this, mPager, data, pie > 0 , pie , camId);
		adapter.id = CollectionList.curId;
		
		mPager.setAdapter(adapter);

		mPager.setOnPageChange(new OnPageChange() {

			@Override
			public void onChange(int pos) {
				if (pos == adapter.getCount() - 1) {
					mShoppieDBProvider.addNewCollection(
							Integer.parseInt(camId), Integer.parseInt(merchId),
							true);
					String id = CollectionList.getNextCampaignId();
					if(CollectionList.curId != 0) {
						camName = CollectionList.getCurCampaignName();
						pie = CollectionList.getCurPie();
						if (id != null) {
							camId = id;
							adapter.freeAll();
							mPager.setLockSlide(true);
							adapter.startLoading();
							mPager.postDelayed(new Runnable() {
		
								@Override
								public void run() {
									mPager.setLockSlide(false);
									requestupdateToGetMerchProducts(camId, custId);
								}
							}, 2000);
						}
					}
					else {
						CollectionList.autoFinish = true;
						finish();
					}
				} else
					adapter.freeImage(pos);
			}
		});

		adapter.setOnLikeListenner(new OnLikeListenner() {

			@Override
			public void onLike(boolean liked, int productionId) {
				likeProduct(liked, productionId);
			}
		});

		adapter.setOnPieListenner(new OnPieListenner() {
			
			@Override
			public void onPie(int pieValue, String campaignId) {
				showPieAnimation(pieValue , campaignId);
			}
		});
		
		if (adapter.getCount() == 1) {
			String id = CollectionList.getNextCampaignId();
			if (id != null) {
				camName = CollectionList.getCurCampaignName();
				pie = CollectionList.getCurPie();
				camId = id;
				adapter.freeAll();
				requestupdateToGetMerchProducts(camId, custId);
			}
		}
	}

	private void likeProduct(boolean liked, int pId) {
		hint.setVisibility(View.GONE);
		if (liked) {
			likeProduct(custId, "" + pId);
		} else {
			unlikeProduct(custId, "" + pId);
		}
	}

	private void requestupdateToGetMerchProducts(final String campaignId,
			String custId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantProduct(campaignId, custId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				CatelogyDetailActivity.this, new AsyncHttpResponseProcess(
						CatelogyDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.i(TAG, "response " + response);
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchProductList merchProductList = gson.fromJson(
									jsonObject.toString(),
									MerchProductList.class);

							mMerchProductItems = merchProductList.getResult();
							setAdapter(mMerchProductItems);

							/** update to database */
							if (mShoppieDBProvider.countJsonData(response) == 0) {
								JsonDataObject jsonDataObject = new JsonDataObject(
										response,
										GlobalValue.TYPE_MERCH_PRODUCTS,
										Integer.parseInt(campaignId));
								mShoppieDBProvider
										.addNewJsonData(jsonDataObject);
							}

							/** update like to database */
							for (int i = 0; i < mMerchProductItems.size(); i++) {
								MerchProductItem merchProductItem = mMerchProductItems
										.get(i);
								if (merchProductItem.getLiked() == 1
										&& mShoppieDBProvider
												.countFavouriteDataItem(""
														+ merchProductItem
																.getProductId()) == 0) {
									FavouriteDataObject favouriteDataObject = new FavouriteDataObject(
											merchProductItem.getProductImage(),
											GlobalValue.TYPE_FAVOURITE_PRODUCT,
											""
													+ merchProductItem
															.getProductId());
									mShoppieDBProvider
											.addNewFavouriteData(favouriteDataObject);
								}
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
		postGetMerchantProducts.execute(WebServiceConfig.URL_MERCHANT_PRODUCT);
	}

	public void showPieAnimation(int pieValue , final String campId) {
		SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

		int iTmp = sp.load(this, R.raw.pied, 1);
		sp.play(iTmp, 1, 1, 0, 0, 1);
		MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.pied); 
		if (mPlayer != null)
			mPlayer.start();

		final View piedView = findViewById(R.id.pied_view);
		((TextView) findViewById(R.id.pie_text)).setText("+" + pieValue);

		piedView.setVisibility(View.VISIBLE);
		ScaleAnimation anim = new ScaleAnimation(0, 1f, 0, 1f,
				piedView.getWidth() / 2, piedView.getHeight() / 2);
		anim.setDuration(1000);
		anim.setInterpolator(new DecelerateInterpolator());

		final ScaleAnimation anim2 = new ScaleAnimation(1f, 1f, 1f, 1f,
				piedView.getWidth() / 2, piedView.getHeight() / 2);
		anim2.setDuration(1000);

		final ScaleAnimation anim1 = new ScaleAnimation(1f, 0f, 1f, 0f,
				piedView.getWidth() / 2, piedView.getHeight() / 2);
		anim1.setInterpolator(new AccelerateInterpolator());
		anim1.setDuration(500);

		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				piedView.startAnimation(anim2);
			}
		});
		anim2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				piedView.startAnimation(anim1);
			}
		});
		anim1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				View v = findViewById(R.id.pied_view);
				v.setVisibility(View.GONE);
				updateLuckyPie(campId, String.valueOf(mSharePref.getCustId()));
			}
		});

		piedView.startAnimation(anim);
	}
	
	private void updateLuckyPie(String campaignId, String custId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.updateLuckyPie(
				campaignId, custId);
		AsyncHttpPost postUpdateLuckyPie = new AsyncHttpPost(CatelogyDetailActivity.this,
				new AsyncHttpResponseProcess(CatelogyDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							StatusUpdatePie statusUpdatePie = gson.fromJson(
									jsonObject.toString(),
									StatusUpdatePie.class);
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
		postUpdateLuckyPie.execute(WebServiceConfig.URL_UPDATE_PIE);
	}
}
