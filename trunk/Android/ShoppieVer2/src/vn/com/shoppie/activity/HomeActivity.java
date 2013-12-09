package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.database.sobject.MerchantCategoryList;
import vn.com.shoppie.database.sobject.StatusUpdatePie;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.NetworkUtility;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.JsonDataObject;
import vn.com.shoppie.util.FacebookUtil;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.MPager.OnPageChange;
import vn.com.shoppie.view.MPager.OnStartExtend;
import vn.com.shoppie.view.MyTextView;
import vn.com.shoppie.view.OnItemClick;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.gson.Gson;

public class HomeActivity extends VisvaAbstractActivity {
	public static final int REQUEST_CODE_BLUETOOTH = 1;
	private RelativeLayout actionBar;
	private View checkinCircle;
	private MPager pager;
	private CatelogyAdapter adapter;
	private MyTextView mTxtTitle;
	private MyTextView hint;
	private boolean isShowFirstHint = false;
	// Google analysis
	protected GoogleAnalytics mGaInstance;
	private ShoppieDBProvider mShoppieDBProvider;
	private static ShoppieSharePref mShoppieSharePref;
	private AlertDialog mAlertDialog;

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
		mTxtTitle = new MyTextView(this);
		mTxtTitle.setGravity(Gravity.CENTER);
		mTxtTitle.setText("Tìm nơi tích điểm");
		mTxtTitle.setTextColor(0xffffffff);
		mTxtTitle.setTextSize(getResources().getDimension(
				R.dimen.actionbar_title_textsize));
		actionBar.addView(mTxtTitle, -1, -1);
		hint = (MyTextView) findViewById(R.id.hint);
		checkinCircle = findViewById(R.id.checkin_circle);
		pager = (MPager) findViewById(R.id.pager);

		pager.setOnStartExtendListenner(new OnStartExtend() {

			@Override
			public void onExtend(View v) {
				hideCheckin();
				adapter.hideBottom();
				if (hint.getVisibility() == View.VISIBLE)
					hint.setVisibility(View.GONE);
			}

			@Override
			public void onCollapse(View v) {

			}

			@Override
			public void onFinishCollapse(View v) {
				adapter.showBottom();
				showCheckin();
			}
		});

		mShoppieSharePref = new ShoppieSharePref(this);
		mShoppieSharePref.setCheckinStatus(0);
		mShoppieDBProvider = new ShoppieDBProvider(this);
		if (NetworkUtility.getInstance(this).isNetworkAvailable())
			requestToGetMerchantCategory();
		else {
			getMerchantCategoryFromDB();
		}

		/** up facebook login success */
		if (mShoppieSharePref.getLoginType() && !mShoppieSharePref.getPostLoginFBSuccess()){
			FacebookUtil.getInstance(self).publishLoginSuccessInBackground(
					mShoppieSharePref.getCustName());
			mShoppieSharePref.setPostLoginFBSuccess(true);
		}
		/**turn off bluetooth*/
		turnoffBluetooth();
	}

	private void getMerchantCategoryFromDB() {
		// TODO Auto-generated method stub
		ArrayList<JsonDataObject> jsonDataObject = mShoppieDBProvider
				.getJsonData(GlobalValue.TYPE_MERCHANT_CATEGORY);
		for (int i = 0; i < jsonDataObject.size(); i++) {
			String merchantCategory = jsonDataObject.get(i).getJsonData();
			if (merchantCategory != null && !"".equals(merchantCategory))
				try {
					JSONObject jsonObject = new JSONObject(merchantCategory);
					Gson gson = new Gson();
					MerchantCategoryList merchantCategoryList = gson.fromJson(
							jsonObject.toString(), MerchantCategoryList.class);
					setAdapter(merchantCategoryList.getResult());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
				showToast(getString(R.string.network_unvailable));
		}
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
				alert = getString(R.string.checkin_not_transaction);
			} else {
				alert = getString(R.string.checking_transaction);
			}
			mAlertDialog = creatDialog(this, alert, null);
			mAlertDialog.show();
		}
	}

	public static AlertDialog creatDialog(Context mContext, String message,
			String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if (title != null)
			builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(
				mContext.getResources().getString(R.string.btn_close),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {
							dialog.dismiss();
						} catch (Exception e) {
							// TODO: handle exception
						}
						mShoppieSharePref.setCheckinStatus(2);
					}
				});

		return builder.create();
	}

	private void startBluetoothByIntent() {
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
		mGaTracker.send(GA_HIT_TYPE_BUTTON, GA_MAP_PARAMS);

		/** set animation for checkin */
		setCheckIn(true);
	}

	private void setAdapter(ArrayList<MerchantCategoryItem> data) {
		adapter = new CatelogyAdapter(this, data);
		pager.setAdapter(adapter);
		pager.setOnPageChange(new OnPageChange() {

			@Override
			public void onChange(int pos) {
				// TODO Auto-generated method stub
				if (hint.getVisibility() == View.VISIBLE) {
					if (isShowFirstHint) {
						hint.setText("Kéo lên trên/xuống dưới để xem danh sách");
						isShowFirstHint = false;
						pager.setOnPageChange(null);
					}
				}
			}
		});
		adapter.setOnItemClick(new OnItemClick() {

			@Override
			public void onClick(int pos) {
				Log.d("OnClick", "Pos " + pos);
				Intent intent = new Intent(HomeActivity.this,
						CollectionList.class);
				intent.putExtra(CollectionList.KEY_MERCHANT_ID, ""
						+ adapter.getItem(pos).getMerchCatId());
				intent.putExtra(CollectionList.KEY_CUSTOMER_ID,
						String.valueOf(mShoppieSharePref.getCustId()));
				intent.putExtra(CollectionList.KEY_ICON, adapter.getItem(pos)
						.getIcon());
				intent.putExtra(CollectionList.KEY_TITLE, adapter.getItem(pos)
						.getMerchCatName());
				intent.putExtra(CollectionList.KEY_DESC, adapter.getItem(pos)
						.getMerchCatDesc());
				intent.putExtra(CollectionList.KEY_NUMBER, ""
						+ adapter.getItem(pos).getCampaignNumber());
				changeToActivity(intent, false);
			}
		});

		int count = mShoppieSharePref.getStartCount();
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
			hint.setText("Kéo trái/phải để xem nhiều chương trình hơn");
			isShowFirstHint = true;
			mShoppieSharePref.addStartCount();
		}
	}

	private void requestToGetMerchantCategory() {
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
							/** update to database */
							mShoppieDBProvider
									.deleteJsonData(GlobalValue.TYPE_MERCHANT_CATEGORY);
							JsonDataObject jsonDataObject = new JsonDataObject(
									response,
									GlobalValue.TYPE_MERCHANT_CATEGORY, 0);
							mShoppieDBProvider.addNewJsonData(jsonDataObject);
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
			gotoActivity(HomeActivity.this, PersonalInfoActivity.class);
			break;
		case R.id.bt_quatang:
			// goBack();
			if (NetworkUtility.getInstance(HomeActivity.this)
					.isNetworkAvailable()) {
				gotoActivity(HomeActivity.this, ActivityGiftTransaction.class);
				pager.collapseView();
			} else {
				showToast(getString(R.string.network_unvailable));
			}
			break;
		case R.id.checkin:
			onClickCheckin();
			break;
		default:
			break;
		}
	}

	private void setCheckIn(final boolean isChecked) {
		if (isChecked) {
			int angle = 36000000;
			final RotateAnimation anim = new RotateAnimation(0, angle,
					checkinCircle.getWidth() / 2, checkinCircle.getHeight() / 2);
			final RotateAnimation anim1 = new RotateAnimation(0, angle,
					checkinCircle.getWidth() / 2, checkinCircle.getHeight() / 2);

			anim.setDuration(angle * 7);
			anim1.setDuration(angle * 7);

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
			if (isCheckin) {
				checkinCircle.clearAnimation();
				isCheckin = false;
			}

			timer.start();
		} else {
			timer.cancel();
			checkinCircle.clearAnimation();
			turnoffBluetooth();
		}
	}

	public void showPieAnimation(int pieValue) {
		SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

		int iTmp = sp.load(this, R.raw.pied, 1);
		sp.play(iTmp, 1, 1, 0, 0, 1);
		MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.pied); // in 2nd
																	// param u
																	// have to
																	// pass
																	// your
																	// desire
																	// ringtone
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
			}
		});

		piedView.startAnimation(anim);
	}

	private void turnoffBluetooth() {
		// Disable bluetooth
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.disable();
		}
	}

	private void hideCheckin() {
		View v = findViewById(R.id.checkin_layout);
		MarginLayoutParams params = (MarginLayoutParams) v.getLayoutParams();
		params.height = (int) getResources()
				.getDimension(R.dimen.footer_height);
		v.setLayoutParams(params);
	}

	private void showCheckin() {
		View v = findViewById(R.id.checkin_layout);
		MarginLayoutParams params = (MarginLayoutParams) v.getLayoutParams();
		params.height = (int) getResources()
				.getDimension(R.dimen.checkin_cirle);
		v.setLayoutParams(params);
	}

	CountDownTimer timer = new CountDownTimer(20000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			Log.e(TAG,
					"getCheckinStatus " + mShoppieSharePref.getCheckinStatus());
			if (mShoppieSharePref.getCheckinStatus() == 1) {
				onFinish();
				showPieAnimation(1);
				mShoppieSharePref.setCheckinStatus(0);
				if (mShoppieSharePref.getLoginType())
					FacebookUtil.getInstance(self).publishLuckyPieInBackground(
							mShoppieSharePref.getCustName());
			} else if (mShoppieSharePref.getCheckinStatus() == 2) {
				onFinish();
				showToast(getString(R.string.checkin_not_success));
				mShoppieSharePref.setCheckinStatus(0);
			}
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			try {
				if (mAlertDialog != null) {
					mAlertDialog.dismiss();
				}
			} catch (Exception e) {

			}
			mShoppieSharePref.setCheckinStatus(0);
			setCheckIn(false);
			turnoffBluetooth();
		}
	};

	protected void onDestroy() {
		updatePieToSPServer();
		super.onDestroy();
	}

	private void updatePieToSPServer() {

	}
}
