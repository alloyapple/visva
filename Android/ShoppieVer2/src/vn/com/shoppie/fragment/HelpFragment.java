package vn.com.shoppie.fragment;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.AdapterWelcomeImage;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpFragment extends FragmentBasic {
	public static int PAGE_REGISTER = 1001;
	private ViewPager mPager;
	private AdapterWelcomeImage mAdapter;
	private ArrayList<Integer> mData = new ArrayList<Integer>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(R.layout.page_personal_help,
				null);

		
		mPager = (ViewPager) root.findViewById(R.id.pager);

		mAdapter = new AdapterWelcomeImage(getActivity(), mData);
		mPager.setAdapter(mAdapter);
		
		addDataSample();
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

	private void findViewById(View v) {
		mPager = (ViewPager) v.findViewById(R.id.pager);

		mAdapter = new AdapterWelcomeImage(getActivity(), mData);
		mPager.setAdapter(mAdapter);
	}

	private void addDataSample() {
		mData.clear();
		mData.add(R.drawable.slide1);
		mData.add(R.drawable.slide2);
		mData.add(R.drawable.slide3);
		mData.add(R.drawable.slide4);
		mData.add(R.drawable.slide5);
		mData.add(R.drawable.slide6);
		
		mAdapter.updateData(mData);
	}

}
