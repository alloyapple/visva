package vn.com.shoppie.fragment;

import java.util.List;

import org.apache.http.NameValuePair;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.database.ShoppieDBProvider;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.object.FavouriteDataObject;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.antonyt.infiniteviewpager.StoreImageFragment;

public class SearchBrandDetailFragment extends FragmentBasic {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_search_brand_detail_fragment, null);
		name = (TextView) root.findViewById(R.id.name);
		desc = (TextView) root.findViewById(R.id.desc);
		count = (TextView) root.findViewById(R.id.count);
		viewPager = (ViewPager) root.findViewById(R.id.pager);

		indicator0 = root.findViewById(R.id.indicator0);
		indicator1 = root.findViewById(R.id.indicator1);
		indicator2 = root.findViewById(R.id.indicator2);

		tvLike = (TextView) root.findViewById(R.id.like);
		like = (Button) root.findViewById(R.id.like_click);

		
		like.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean currLike = !mMerchantStoreItem.isLiked();
				mMerchantStoreItem.setLiked(currLike);
				tvLike.setCompoundDrawablesWithIntrinsicBounds(mMerchantStoreItem.isLiked() ? R.drawable.ic_liked : R.drawable.ic_like, 0, 0, 0);
				
				if(currLike)
					likeBrand(String.valueOf(mMerchantStoreItem.getStoreId()), String.valueOf(mMerchantStoreItem.getMerchId()));
				else
					unLikeBrand(String.valueOf(mMerchantStoreItem.getStoreId()), String.valueOf(mMerchantStoreItem.getMerchId()));
					
				Log.e("like success ", "like success");

				MediaPlayer mPlayer = MediaPlayer.create(getActivity(),
						R.raw.sound_like2);
				if (mPlayer != null)
					mPlayer.start();

				/** add to favourite product */
				if (mShoppieDBProvider.countFavouriteDataItem(""
						+ mMerchantStoreItem.getStoreId()) == 0) {
					FavouriteDataObject favouriteDataObject = new FavouriteDataObject(
							mMerchantStoreItem.getMerchLogo(),
							GlobalValue.TYPE_FAVOURITE_BRAND, ""
									+ mMerchantStoreItem.getStoreId());
					mShoppieDBProvider.addNewFavouriteData(favouriteDataObject);
				}
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

		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mShoppieDBProvider = new ShoppieDBProvider(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public interface IOnClickShowStoreDetail {
		public void onClickViewStoreDetail(MerchantStoreItem store);
	}

	public void setListener(IOnClickShowStoreDetail iOnClickShowStoreDetail) {
		this.mListener = iOnClickShowStoreDetail;
	}

	public void updateUI(final MerchantStoreItem store) {
		// TODO Auto-generated method stub
		mMerchantStoreItem = store;
		tvLike.setCompoundDrawablesWithIntrinsicBounds(mMerchantStoreItem.isLiked() ? R.drawable.ic_liked : R.drawable.ic_like, 0, 0, 0);
		name.setText(store.getStoreName());
		desc.setText(store.getMerchDesc());
		count.setText("+" + store.getPieQty());

		viewPager.setAdapter(new MyPagerAdapter(getActivity()
				.getSupportFragmentManager(), store));
		viewPager.setCurrentItem(5001);

		tvLike.setText("0");
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		MerchantStoreItem store;

		public MyPagerAdapter(FragmentManager fm, MerchantStoreItem store) {
			super(fm);
			this.store = store;
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Fragment fragment = new StoreImageFragment();
			Bundle args = new Bundle();
			if (position % 3 == 0)
				args.putString("link0",
						WebServiceConfig.HEAD_IMAGE + store.getMerchBanner1());
			else if (position % 3 == 1)
				args.putString("link0",
						WebServiceConfig.HEAD_IMAGE + store.getMerchBanner2());
			else if (position % 3 == 2)
				args.putString("link0",
						WebServiceConfig.HEAD_IMAGE + store.getMerchBanner3());

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10000;
		}
	}

	private void likeBrand(String custId, final String merchId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.likeBrand(custId,
				merchId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("like success ", "like success");
						MediaPlayer mPlayer = MediaPlayer.create(getActivity(),
								R.raw.sound_like2);
						if (mPlayer != null)
							mPlayer.start();

						// /** add to favourite product */
						// FavouriteDataObject favouriteDataObject = new
						// FavouriteDataObject(
						// mMerchProductItem.getProductImage(),
						// GlobalValue.TYPE_FAVOURITE_PRODUCT, productId);
						// mShoppieDBProvider
						// .addNewFavouriteData(favouriteDataObject);
					}

					@Override
					public void processIfResponseFail() {
						showToast("like failed");
						// finish();
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_LIKE_BRAND);

	}

	private void unLikeBrand(String custId, final String merchId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.unLikeBrand(
				custId, merchId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("like success ", "like success");
						MediaPlayer mPlayer = MediaPlayer.create(getActivity(),
								R.raw.sound_like2);
						if (mPlayer != null)
							mPlayer.start();

						// /** add to favourite product */
						// FavouriteDataObject favouriteDataObject = new
						// FavouriteDataObject(
						// mMerchProductItem.getProductImage(),
						// GlobalValue.TYPE_FAVOURITE_PRODUCT, productId);
						// mShoppieDBProvider
						// .addNewFavouriteData(favouriteDataObject);
					}

					@Override
					public void processIfResponseFail() {
						showToast("like failed");
						// finish();
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_UNLIKE_BRAND);

	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
	}
}
