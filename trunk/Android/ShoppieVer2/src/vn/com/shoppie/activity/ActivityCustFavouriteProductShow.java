package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.database.sobject.CustomerLikeProduct;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.util.Utils;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.ShareButton;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class ActivityCustFavouriteProductShow extends Activity {

	private CustomerLikeProduct mMerchProductItem;
	private ShareButton mLoginButton;

	// FaceBook
	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			Log.e("Session change", session.isOpened() + "-" + state.toString());
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

		mMerchProductItem = (CustomerLikeProduct) getIntent().getExtras()
				.getParcelable(GlobalValue.CUSTOMER_PRODUCT_ITEM);
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
		like.setVisibility(View.GONE);

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
		likeBt.setVisibility(View.GONE);

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
		mLoginButton.setVisibility(View.GONE);
		btMail.setVisibility(View.GONE);
		btSms.setVisibility(View.GONE);
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
}
