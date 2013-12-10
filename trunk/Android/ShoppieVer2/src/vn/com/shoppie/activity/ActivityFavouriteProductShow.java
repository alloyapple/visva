package vn.com.shoppie.activity;

import java.util.List;

import org.apache.http.NameValuePair;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ShareButton;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchProductItem;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.FavouriteDataObject;
import vn.com.shoppie.util.FacebookUtil;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.util.Utils;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityFavouriteProductShow extends Activity {

	private MerchProductItem mMerchProductItem;
	private ShoppieDBProvider mShoppieDBProvider;
	private ShoppieSharePref mShopieSharePref;
	private ShareButton mLoginButton;

	// FaceBook
	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			Log.e("Session change", session.isOpened() + "-" + state.toString());
			onSessionStateChanged(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Facebook
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collectiondetail_1);

		mShopieSharePref = new ShoppieSharePref(this);
		mShoppieDBProvider = new ShoppieDBProvider(this);

		mMerchProductItem = (MerchProductItem) getIntent().getExtras()
				.getParcelable(GlobalValue.MERCH_PRODUCT_ITEM);
		View text = findViewById(R.id.text);
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// v.setVisibility(View.GONE);
				closeDesc(v);
			}
		});
		text.setVisibility(View.GONE);

		TextView name = (TextView) findViewById(R.id.name);
		TextView name1 = (TextView) findViewById(R.id.name1);
		TextView price = (TextView) findViewById(R.id.price);
		TextView priceGoc = (TextView) findViewById(R.id.price_goc);
		TextView price1 = (TextView) findViewById(R.id.price1);
		TextView priceGoc1 = (TextView) findViewById(R.id.price1_goc);
		TextView color = (TextView) findViewById(R.id.color);
		TextView like = (TextView) findViewById(R.id.like);

		priceGoc.setPaintFlags(priceGoc.getPaintFlags()
				| Paint.STRIKE_THRU_TEXT_FLAG);
		priceGoc1.setPaintFlags(priceGoc1.getPaintFlags()
				| Paint.STRIKE_THRU_TEXT_FLAG);

		name.setText(mMerchProductItem.getProductName());
		name1.setText(mMerchProductItem.getProductName());
		price.setText("" + Utils.formatMoney(mMerchProductItem.getPrice())
				+ " VNĐ");
		price1.setText("" + Utils.formatMoney(mMerchProductItem.getPrice())
				+ " VNĐ");
		color.setText("" + mMerchProductItem.getShortDesc());
		like.setText("" + mMerchProductItem.getLikedNumber());

		priceGoc.setText(mMerchProductItem.getOldPrice() > 0 ? "Gốc: "
				+ Utils.formatMoney(mMerchProductItem.getOldPrice()) + " VNĐ"
				: "");
		priceGoc1.setText(mMerchProductItem.getOldPrice() > 0 ? "Gốc: "
				+ Utils.formatMoney(mMerchProductItem.getOldPrice()) + " VNĐ"
				: "");
		TextView count = (TextView) findViewById(R.id.count);
		count.setText(mMerchProductItem.getPieQty() > 0 ? ""
				+ mMerchProductItem.getPieQty() : "");
		//
		TextView count1 = (TextView) findViewById(R.id.count1);
		count1.setText(mMerchProductItem.getPieQty() > 0 ? ""
				+ mMerchProductItem.getPieQty() : "");

		if (mMerchProductItem.getPieQty() > 999) {
			MarginLayoutParams params = (MarginLayoutParams) count
					.getLayoutParams();
			params.width *= 1.25f;
			params.height *= 1.25f;
			count.setLayoutParams(params);

			params = (MarginLayoutParams) count1.getLayoutParams();
			params.width *= 1.25f;
			params.height *= 1.25f;
			count1.setLayoutParams(params);

		}

		Button likeBt = (Button) findViewById(R.id.like_click);
		likeBt.setTag(mMerchProductItem);
		int resId = 0;
		if (mMerchProductItem.getLiked() == 0) {
			resId = R.drawable.ic_like;
		} else {
			resId = R.drawable.ic_liked;
		}
		like.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);

//		OnClickListener onClickListener = new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (mMerchProductItem.getLiked() == 0) {
//					mMerchProductItem.setLiked(1);
//					mMerchProductItem.setLikedNumber(mMerchProductItem
//							.getLikedNumber() + 1);
//				} else {
//					mMerchProductItem.setLiked(0);
//					mMerchProductItem.setLikedNumber(mMerchProductItem
//							.getLikedNumber() - 1);
//				}
//				updateLiked(mMerchProductItem.getProductId());
//
//				onPostLikeToServer(mMerchProductItem.getLiked(),
//						mMerchProductItem.getMerchId());
//			}
//		};
//
//		likeBt.setOnClickListener(onClickListener);

		TextView desc = (TextView) findViewById(R.id.desc1);
		desc.setText(mMerchProductItem.getLongDesc());
		desc.setTag(findViewById(R.id.text));
		desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View text = (View) v.getTag();
				closeDesc(text);
			}
		});

		mLoginButton = (ShareButton) findViewById(R.id.bt_fb);
		mLoginButton.setTag(mMerchProductItem);
		mLoginButton.setApplicationId(getString(R.string.fb_app_id));
		Button btMail = (Button) findViewById(R.id.bt_mail);
		btMail.setTag(mMerchProductItem);
		Button btSms = (Button) findViewById(R.id.bt_sms);
		btSms.setTag(mMerchProductItem);

		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MerchProductItem item = (MerchProductItem) v.getTag();
				Session session = Session.getActiveSession();
				boolean enableButtons = (session != null && session.isOpened());
				Log.e("adfkjdhf", "enableButtons "+enableButtons);
				if (enableButtons) {
					FacebookUtil.getInstance(ActivityFavouriteProductShow.this)
							.publishShareDialog(item);
				} else {
					 mLoginButton.onClickLoginFb();
					 mShopieSharePref.setActionShareFB(true);
				}
			}
		});
		btSms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MerchProductItem item = (MerchProductItem) v.getTag();
				initShareSMS(item);
			}
		});
		btMail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MerchProductItem item = (MerchProductItem) v.getTag();
				initShareItent(item);
			}
		});

		View image = findViewById(R.id.image);
		ImageLoader.getInstance(this).DisplayImage(
				WebServiceConfig.HEAD_IMAGE
						+ mMerchProductItem.getProductImage(), image, true,
				true, false, false, true, false);

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View text = findViewById(R.id.text);
				if (text.getAnimation() == null) {
					text.setVisibility(View.VISIBLE);
					AnimatorSet set = new AnimatorSet();
					set.playTogether(ObjectAnimator.ofFloat(text, "alpha", 0,
							0.8f), ObjectAnimator.ofFloat(text, "translationY",
							getViewHeight(), 0));
					set.setDuration(350);
					set.setInterpolator(new AccelerateInterpolator());
					set.start();
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();

		final Session session = Session.getActiveSession();
		if (session == null || session.isClosed() || !session.isOpened()) {
			uiHelper = new UiLifecycleHelper(this, callback);
		} else {
			Log.e("resume: session", "not null");
			if (mShopieSharePref.getActionShareFB()) {
				FacebookUtil.getInstance(ActivityFavouriteProductShow.this)
						.publishShareDialog(mMerchProductItem);
				mShopieSharePref.setActionShareFB(false);
			}
		}

	}
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	private void onPostLikeToServer(int liked, int merchId) {
		// TODO Auto-generated method stub
		if (liked == 1) {
			likeProduct("" + mShopieSharePref.getCustId(), "" + merchId);
		} else {
			unlikeProduct("" + mShopieSharePref.getCustId(), "" + merchId);
		}
	}

	private void unlikeProduct(String custId, final String productId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.likeProduct(
				custId, productId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				ActivityFavouriteProductShow.this,
				new AsyncHttpResponseProcess(ActivityFavouriteProductShow.this) {
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
				ActivityFavouriteProductShow.this,
				new AsyncHttpResponseProcess(ActivityFavouriteProductShow.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("like success ", "like success");
						MediaPlayer mPlayer = MediaPlayer.create(
								ActivityFavouriteProductShow.this,
								R.raw.sound_like2);
						if (mPlayer != null)
							mPlayer.start();

						/** add to favourite product */
						FavouriteDataObject favouriteDataObject = new FavouriteDataObject(
								mMerchProductItem.getProductImage(),
								GlobalValue.TYPE_FAVOURITE_PRODUCT, productId);
						mShoppieDBProvider
								.addNewFavouriteData(favouriteDataObject);
					}

					@Override
					public void processIfResponseFail() {
						showToast("like failed");
						// finish();
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_LIKE_PRODUCT);

	}

	/**
	 * share via email
	 */
	private void initShareItent(MerchProductItem item) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, "Shoppie Invitation");
		share.putExtra(
				Intent.EXTRA_TEXT,
				""
						+ getString(R.string.introduction_invitation,
								item.getProductName(), item.getShortDesc()));
		// share.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		// if (pFilePath != null)
		// share.putExtra(Intent.EXTRA_STREAM,
		// Uri.fromFile(new File(pFilePath)));
		share.setType("vnd.android.cursor.dir/email");
		startActivity(Intent.createChooser(share, "Select"));
	}

	/**
	 * share vis sms
	 */
	private void initShareSMS(MerchProductItem item) {
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra(
				"sms_body",
				getString(R.string.introduction_invitation,
						item.getProductName(), item.getShortDesc()));
		sendIntent.setType("vnd.android-dir/mms-sms");
		startActivity(sendIntent);
	}

	private boolean ensureOpenSession() {
		android.util.Log.e("adfdsfh", "afiun df ");
		Session.openActiveSession(ActivityFavouriteProductShow.this, true,
				new Session.StatusCallback() {
					@Override
					public void call(final Session session, SessionState state,
							Exception exception) {
						// TODO Auto-generated method stub
						Request request = Request.newMeRequest(session,
								new Request.GraphUserCallback() {
									@Override
									public void onCompleted(GraphUser user,
											Response response) {
										if (session == Session
												.getActiveSession()) {
											if (user != null) {
												String name = user.getName();
												android.util.Log.e(
														"name " + user.getId(),
														"adfname "
																+ user.getName());
											}
										}
									}
								});
						request.executeAsync();
					}
				});
		return true;
	}

	private void onSessionStateChanged(final Session session,
			SessionState state, Exception exception) {
		android.util.Log.e("adfdf",
				"adfd " + (session != null && session.isOpened()));
		if (session != null && session.isOpened()) {
			if (state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				Session.getActiveSession();
			} else {
				Request request = Request.newMeRequest(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								if (session == Session.getActiveSession()) {
									if (user != null) {
										String name = user.getName();
										android.util.Log.e(
												"name " + user.getId(),
												"adfname " + user.getName());
									}
								}
							}
						});
				request.executeAsync();
			}
		} else {
			mLoginButton.onClickLoginFb();
		}
	}

	private void closeDesc(final View v) {
		Log.d("ONClick", ">>>>>>>>>>>>>>>>>>>>> ");
		// if (v.getAnimation() != null)
		// return;
		if (Build.VERSION.SDK_INT >= 11) {
			TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
					getViewHeight());
			anim.setDuration(350);
			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					v.setVisibility(View.GONE);
				}
			});

			v.startAnimation(anim);
		} else {
			TranslateAnimation anim = new TranslateAnimation(0, 0, 0,
					getViewHeight());
			anim.setDuration(0);
			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					v.setVisibility(View.GONE);
				}
			});

			v.startAnimation(anim);
		}
	}

	public int getViewWidth() {
		return (int) getResources().getDimension(
				R.dimen.collectiondetail_item_width);
	}

	public int getViewHeight() {
		return (int) getResources().getDimension(
				R.dimen.collectiondetail_item_height);
	}

	private void updateLiked(int productionId) {
		TextView like = (TextView) findViewById(R.id.like);
		int resId = 0;
		if (mMerchProductItem.getLiked() == 0) {
			resId = R.drawable.ic_like;
		} else {
			resId = R.drawable.ic_liked;
		}
		like.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
		like.setText("" + mMerchProductItem.getLikedNumber());
	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(ActivityFavouriteProductShow.this, string,
				Toast.LENGTH_SHORT).show();
	}
}
