package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.constant.ShopieSharePref;
import vn.com.shoppie.database.sobject.MerchProductList;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.database.sobject.MerchantCategoryList;
import vn.com.shoppie.database.sobject.MerchantStoreList;
import vn.com.shoppie.database.sobject.StatusUpdatePie;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.util.DialogUtility;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.MPager.OnStartExtend;
import vn.com.shoppie.view.OnItemClick;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.gson.Gson;

public class HomeActivity extends VisvaAbstractActivity {
	public static final int REQUEST_CODE_BLUETOOTH = 1;
	private RelativeLayout actionBar;
	private View checkinCircle;
	private MPager pager;
	private CatelogyAdapter adapter;
	private boolean isChecked = false;
	private TextView mTxtTitle;
	private ShopieSharePref mShopieSharePref;

	// Google analysis
	protected Tracker mGaTracker;
	protected GoogleAnalytics mGaInstance;

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return R.layout.home_act;

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate() {
		if (Build.VERSION.SDK_INT >= 11)
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

		// setup actionbar
		actionBar = (RelativeLayout) findViewById(R.id.actionbar);
		mTxtTitle = new TextView(this);
		mTxtTitle.setGravity(Gravity.CENTER);
		mTxtTitle.setTextSize(getResources().getDimension(
				R.dimen.actionbar_title_textsize));
		mTxtTitle.setText("Tìm nơi tích điểm");
		mTxtTitle.setTextColor(0xffffffff);
		actionBar.addView(mTxtTitle, -1, -1);
		mShopieSharePref = new ShopieSharePref(this);

		checkinCircle = findViewById(R.id.checkin_circle);
		pager = (MPager) findViewById(R.id.pager);

		pager.setOnStartExtendListenner(new OnStartExtend() {

			@Override
			public void onExtend(View v) {
				isChecked = false;
				setCheckIn(isChecked);
				adapter.hideBottom();
			}

			@Override
			public void onCollapse(View v) {

			}

			@Override
			public void onFinishCollapse(View v) {
				adapter.showBottom();
			}
		});

		requestToGetCampainCategory();

		//requestupdateToGetMerchProducts("95", "148"/*
		//											 * +mShopieSharePref.getCustId
		//											 * ()
		//											 */);

		//updateLuckyPie("95", "148"/*
		//						 * +mShopieSharePref.getCustId ()
		//						 */);

		//requestGetMerchantStores("148"/** +mShopieSharePref.getCustId () */
		//);

		//requestToUpdateFriends("241"/** +mShopieSharePref.getCustId () */
		//);
	}

	private void requestToUpdateFriends(String string) {
		// TODO Auto-generated method stub

	}

	static int DURATION = 60;
	public static boolean requesting = false;
	Thread scanBluetooth = new Thread(new Runnable() {

		@Override
		public void run() {
			requesting = true;
			boolean isBlueOn = false;
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			isBlueOn = adapter.isEnabled();
			int count = 0;
			while (true) {
				count++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				if (count > DURATION) {
					break;
				}
			}
			if (!isBlueOn) {
				adapter.disable();
			}
			requesting = false;
		}
	});

	protected void onActivityResult(int req, int res, Intent data) {
		super.onActivityResult(req, res, data);
		if (req == REQUEST_CODE_BLUETOOTH) {
			String alert = "";
			if (res == RESULT_CANCELED) {
				alert = "Chưa thực hiện giao dịch checkin.";
			} else {
				alert = "Đang thực hiện giao dịch checkin.";
			}
			DialogUtility.alert(self, alert, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					// continue with delete
					dialog.dismiss();
				}
			});
		}
	}

	public void startBluetoothByIntent() {
		// start Bluetooth by Intent
		if (BluetoothAdapter.getDefaultAdapter() != null) {
			if (BluetoothAdapter.getDefaultAdapter().getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
				Intent intent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
						DURATION);
				try {
					startActivityForResult(intent, REQUEST_CODE_BLUETOOTH);
					if (!requesting)
						try {
							scanBluetooth.start();
						} catch (IllegalStateException e) {
						}
				} catch (IllegalThreadStateException e) {
					showToast("Chưa thể thực hiện checkin. Bạn hãy thử lại!");
				}
			}

		}

	}

	private void onClickCheckin() {
		super.mGaTracker.sendEvent(getString(R.string.ca_button),
				getString(R.string.ac_press), "btn_pie",
				System.currentTimeMillis());
		startBluetoothByIntent();
		GA_MAP_PARAMS.clear();
		GA_MAP_PARAMS.put("method", "btnClicked");
		GA_MAP_PARAMS.put("button", "activity_home_btn_pie");
		//mGaTracker.send(GA_HIT_TYPE_BUTTON, GA_MAP_PARAMS);
	}

	private void requestGetMerchantStores(String custId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantStores(custId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				HomeActivity.this, new AsyncHttpResponseProcess(
						HomeActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchantStoreList merchantStoreList = gson
									.fromJson(jsonObject.toString(),
											MerchantStoreList.class);
							Log.e("merchantproductlist", "merchantproductlist "
									+ merchantStoreList.getResult().size());
							for (int i = 0; i < merchantStoreList.getResult()
									.size(); i++) {
								Log.e("merchantproductlist",
										"merchantproductlist "
												+ merchantStoreList.getResult()
														.get(i)
														.getMerchBanner());
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
		postGetMerchantProducts.execute(WebServiceConfig.URL_MERCHANT_STORES);
	}

	private void updateLuckyPie(String campaignId, String custId) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.updateLuckyPie(
				campaignId, custId);
		AsyncHttpPost postUpdateLuckyPie = new AsyncHttpPost(HomeActivity.this,
				new AsyncHttpResponseProcess(HomeActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							StatusUpdatePie statusUpdatePie = gson.fromJson(
									jsonObject.toString(),
									StatusUpdatePie.class);
							Log.e("adkjfhd", "sizeaa "
									+ statusUpdatePie.getResult().getValue());
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

	private void setAdapter(ArrayList<MerchantCategoryItem> data) {
		adapter = new CatelogyAdapter(this, data);
		for (int i = 0; i < 10; i++) {
			pager.setAdapter(adapter);
		}

		adapter.setOnItemClick(new OnItemClick() {

			@Override
			public void onClick(int pos) {
				Log.d("OnClick", "Pos " + pos);
				Intent intent = new Intent(HomeActivity.this,
						CollectionList.class);
				intent.putExtra(CollectionList.KEY_MERCHANT_ID, ""
						+ adapter.getItem(pos).getMerchCatId());
				intent.putExtra(CollectionList.KEY_CUSTOMER_ID, "149");
				intent.putExtra(CollectionList.KEY_ICON, adapter.getItem(pos)
						.getIcon());
				intent.putExtra(CollectionList.KEY_TITLE, adapter.getItem(pos)
						.getMerchCatName());
				intent.putExtra(CollectionList.KEY_DESC, adapter.getItem(pos)
						.getMerchCatDesc());
				changeToActivity(intent, false);
			}
		});

	}

	private void requestupdateToGetMerchProducts(String campaignId,
			String custId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantProduct(campaignId, custId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				HomeActivity.this, new AsyncHttpResponseProcess(
						HomeActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchProductList merchProductList = gson.fromJson(
									jsonObject.toString(),
									MerchProductList.class);
							Log.e("merchantproductlist",
									"merchantproductlisdfdsfdt "
											+ merchProductList.getResult()
													.size());
							for (int i = 0; i < merchProductList.getResult()
									.size(); i++) {
								Log.e("merchantproductlist",
										"merchantproductlistdfdf "
												+ merchProductList.getResult()
														.get(i).getLongDesc());
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

	private void requestToGetCampainCategory() {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantCategoryValue();
		AsyncHttpPost postCampaignCategory = new AsyncHttpPost(
				HomeActivity.this, new AsyncHttpResponseProcess(
						HomeActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchantCategoryList merchantCategoryList = gson
									.fromJson(jsonObject.toString(),
											MerchantCategoryList.class);
							setAdapter(merchantCategoryList.getResult());
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

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_canhan:
			Log.e("click here", "click here");
			gotoActivity(HomeActivity.this, PersonalInfoActivity.class);
			break;
		case R.id.bt_quatang:
			// goBack();
			gotoActivity(HomeActivity.this, ActivityGiftTransaction.class);
			pager.collapseView();
			break;
		case R.id.checkin:
			onClickCheckin();
			isChecked = !isChecked;
			setCheckIn(isChecked);
			break;

		default:
			break;
		}
	}

	private void setCheckIn(boolean isChecked) {
		if (isChecked) {
			int angle = 36000000;
			final RotateAnimation anim = new RotateAnimation(0, angle,
					checkinCircle.getWidth() / 2, checkinCircle.getHeight() / 2);
			final RotateAnimation anim1 = new RotateAnimation(0, angle,
					checkinCircle.getWidth() / 2, checkinCircle.getHeight() / 2);

			anim.setDuration(angle * 15);
			anim1.setDuration(angle * 15);

			anim.setInterpolator(new Interpolator() {

				@Override
				public float getInterpolation(float input) {
					// TODO Auto-generated method stub
					return input;
				}
			});
			anim1.setInterpolator(anim.getInterpolator());

			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					checkinCircle.startAnimation(anim1);
				}
			});

			checkinCircle.startAnimation(anim);
		} else {
			checkinCircle.clearAnimation();
		}
	}
}
