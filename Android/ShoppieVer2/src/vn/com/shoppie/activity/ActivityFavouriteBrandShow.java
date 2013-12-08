package vn.com.shoppie.activity;

import com.antonyt.infiniteviewpager.StoreImageFragment;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.object.FavouriteDataObject;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ActivityFavouriteBrandShow extends FragmentActivity {
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
	private ShoppieDBProvider mShoppieDBProvider;
	// ============================Variable Define =====================
	private MerchantStoreItem mMerchantStoreItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.page_search_brand_detail_fragment);
		mShoppieDBProvider = new ShoppieDBProvider(this);
		
		mMerchantStoreItem = (MerchantStoreItem) getIntent().getExtras()
				.getParcelable(GlobalValue.MERCH_BRAND_ITEM);
		name = (TextView) findViewById(R.id.name);
		desc = (TextView) findViewById(R.id.desc);
		count = (TextView) findViewById(R.id.count);
		viewPager = (ViewPager) findViewById(R.id.pager);

		indicator0 = findViewById(R.id.indicator0);
		indicator1 = findViewById(R.id.indicator1);
		indicator2 = findViewById(R.id.indicator2);

		tvLike = (TextView) findViewById(R.id.like);
		like = (Button) findViewById(R.id.like_click);

//		like.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Log.e("like success ", "like success");
//
//				MediaPlayer mPlayer = MediaPlayer.create(
//						ActivityFavouriteBrandShow.this, R.raw.sound_like2);
//				if (mPlayer != null)
//					mPlayer.start();
//
//				/** add to favourite product */
//				FavouriteDataObject favouriteDataObject = new FavouriteDataObject(
//						mMerchantStoreItem.getMerchLogo(),
//						GlobalValue.TYPE_FAVOURITE_BRAND, ""
//								+ mMerchantStoreItem.getMerchId());
//				mShoppieDBProvider.addNewFavouriteData(favouriteDataObject);
//			}
//		});
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
		
		if(mMerchantStoreItem!=null)
			updateUI(mMerchantStoreItem);
	}
	
	public void updateUI(final MerchantStoreItem store) {
		mMerchantStoreItem = store;
		name.setText(store.getStoreName());
		desc.setText(store.getMerchDesc());
		count.setText("+" + store.getPieQty());

		viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), store));
		viewPager.setCurrentItem(5001);
		
		tvLike.setText("0");
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		MerchantStoreItem store;

		public MyPagerAdapter(FragmentManager fm , MerchantStoreItem store) {
			super(fm);
			this.store = store;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new StoreImageFragment();
			Bundle args = new Bundle();
			args.putString("link0", WebServiceConfig.HEAD_IMAGE + store.getMerchLogo());

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 10000;
		}

	}
}
