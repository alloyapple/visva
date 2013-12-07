package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.fragment.SearchBrandDetailFragment.IOnClickShowStoreDetail;
import vn.com.shoppie.object.FavouriteDataObject;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ActivityFavouriteBrandShow extends Activity {
	// =============================Constant Define=====================
	// ============================Control Define =====================
	private TextView name;
	private TextView desc;
	private TextView count;
	private ViewPager viewPager;
	private View indicator0;
	private View indicator1;
	private View indicator2;
	private TextView tvLike;
	private Button like;
	// ============================Class Define =======================
	private IOnClickShowStoreDetail mListener;
	private ShoppieDBProvider mShoppieDBProvider;
	// ============================Variable Define =====================
	private MerchantStoreItem mMerchantStoreItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.page_search_brand_detail_fragment);

		name = (TextView) findViewById(R.id.name);
		desc = (TextView) findViewById(R.id.desc);
		count = (TextView) findViewById(R.id.count);
		viewPager = (ViewPager) findViewById(R.id.pager);

		indicator0 = findViewById(R.id.indicator0);
		indicator1 = findViewById(R.id.indicator1);
		indicator2 = findViewById(R.id.indicator2);

		tvLike = (TextView) findViewById(R.id.like);
		like = (Button) findViewById(R.id.like_click);

		like.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("like success ", "like success");

				MediaPlayer mPlayer = MediaPlayer.create(
						ActivityFavouriteBrandShow.this, R.raw.sound_like2);
				if (mPlayer != null)
					mPlayer.start();

				/** add to favourite product */
				FavouriteDataObject favouriteDataObject = new FavouriteDataObject(
						mMerchantStoreItem.getMerchLogo(),
						GlobalValue.TYPE_FAVOURITE_BRAND, ""
								+ mMerchantStoreItem.getMerchId());
				mShoppieDBProvider.addNewFavouriteData(favouriteDataObject);
			}
		});
		viewPager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				indicator0.setBackgroundResource(R.drawable.indicator0);
				indicator1.setBackgroundResource(R.drawable.indicator0);
				indicator2.setBackgroundResource(R.drawable.indicator0);
				switch (arg0 % 3) {
				case 0:
					indicator0.setBackgroundResource(R.drawable.indicator1);
					break;
				case 1:
					indicator1.setBackgroundResource(R.drawable.indicator1);
					break;
				case 2:
					indicator2.setBackgroundResource(R.drawable.indicator1);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				viewPager.getParent().requestDisallowInterceptTouchEvent(true);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
}
