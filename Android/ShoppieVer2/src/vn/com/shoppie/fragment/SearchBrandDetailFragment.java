package vn.com.shoppie.fragment;

import com.antonyt.infiniteviewpager.StoreImageFragment;
import com.antonyt.infiniteviewpager.InfinitePagerAdapter;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchBrandDetailFragment extends FragmentBasic{
	// =============================Constant Define=====================
	// ============================Control Define =====================
	private TextView name;
	private TextView desc;
	private TextView count;
	private PagerAdapter wrappedAdapter;
	private ViewPager viewPager;
	// ============================Class Define =======================
	private IOnClickShowStoreDetail mListener;
	// ============================Variable Define =====================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_search_brand_detail_fragment, null);
		name = (TextView) root.findViewById(R.id.name);
		desc = (TextView) root.findViewById(R.id.desc);
		count = (TextView) root.findViewById(R.id.count);
		viewPager = (ViewPager) root.findViewById(R.id.pager);
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		name.setText(store.getStoreName());
		desc.setText(store.getMerchDesc());
		count.setText("+" + store.getPieQty());
		
		PagerAdapter adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
			int[] colours = new int[] { Color.CYAN, Color.GRAY, Color.MAGENTA};

			@Override
			public int getCount() {
				return colours.length * 2;
			}

			@Override
			public Fragment getItem(int position) {
				if(Math.abs(position) >= colours.length) {
					return getItem(position % colours.length);
				}
				Fragment fragment = new StoreImageFragment();
				Bundle args = new Bundle();
				args.putInt("colour", colours[position]);
				args.putInt("identifier", position);
				args.putString("link0", CatelogyAdapter.URL_HEADER + store.getMerchLogo());;
				args.putString("link1", CatelogyAdapter.URL_HEADER + store.getMerchLogo());;
				args.putString("link2", CatelogyAdapter.URL_HEADER + store.getMerchLogo());;
				fragment.setArguments(args);
				return fragment;
			}
		};
		// wrap pager to provide infinite paging with wrap-around
		wrappedAdapter = new InfinitePagerAdapter(adapter);

		// actually an InfiniteViewPager
		viewPager.setAdapter(wrappedAdapter);

	}
}
