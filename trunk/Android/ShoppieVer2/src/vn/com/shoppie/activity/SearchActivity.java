package vn.com.shoppie.activity;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.fragment.SearchBrandDetailFragment;
import vn.com.shoppie.fragment.SearchBrandFragment;
import vn.com.shoppie.fragment.SearchMapFragment;
import vn.com.shoppie.object.HorizontalListView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

public class SearchActivity extends FragmentActivity {
	// ==========================Constant Define=================
	private static final String SEARCH_BRAND_FRAGMENT_STRING = "brand_fragment";
	private static final String SEARCH_BRAND_DETAIL_FRAGMENT_STRING = "brand_detail_fragment";
	private static final String SEARCH_MAP_FRAGMENT_STRING = "map_fragment";
	private static final int SEARCH_BRAND_FRAGMENT_ID = 2001;
	private static final int SEARCH_BRAND_DETAIL_FRAGMENT_ID = 2002;
	private static final int SEARCH_MAP_FRAGMENT_ID = 2003;
	// ===========================Control Define==================
	private SearchBrandFragment mSearchBrandFragment;
	private SearchBrandDetailFragment mSearchBrandDetailFragment;
	private SearchMapFragment mSearchMapFragment;
	private FragmentManager mFmManager;
	private FragmentTransaction mTransaction;
	private HorizontalListView mTitleSearchListView;
	// =========================Class Define --------------------
	// =========================Variable Define==================
	private ArrayList<String> backstack = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_search_activity);

		intialize();
	}

	private void intialize() {
		// TODO Auto-generated method stub
		mTitleSearchListView = (HorizontalListView) findViewById(R.id.list_title_search);

		mFmManager = getSupportFragmentManager();
		mSearchBrandFragment = (SearchBrandFragment) mFmManager
				.findFragmentById(R.id.search_brand_list_fragment);
		mSearchBrandDetailFragment = (SearchBrandDetailFragment) mFmManager
				.findFragmentById(R.id.search_brand_detail_fragment);
		mSearchMapFragment = (SearchMapFragment) mFmManager
				.findFragmentById(R.id.search_map_fragment);

		showFragment(SEARCH_BRAND_FRAGMENT_ID);
		mTransaction = hideFragment();
		mTransaction.show(mSearchBrandFragment);
		addToSBackStack(SEARCH_BRAND_FRAGMENT_STRING);
		mTransaction.commit();
	}

	private void addToSBackStack(String tag) {
		int index = backstack.lastIndexOf(tag);
		if (index == -1) {
			backstack.add(tag);
			return;
		}
		try {
			if (!backstack.get(index - 1).equals(
					backstack.get(backstack.size() - 1))) {
				backstack.add(tag);
				return;
			}
		} catch (IndexOutOfBoundsException e) {

		}
		try {
			ArrayList<String> subStack = new ArrayList<String>(backstack);
			for (int i = 0; i < subStack.size(); i++) {
				if (i > index) {
					backstack.remove(index);
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private FragmentTransaction hideFragment() {
		mTransaction = mFmManager.beginTransaction();
		mTransaction.hide(mSearchBrandDetailFragment);
		mTransaction.hide(mSearchBrandFragment);
		mTransaction.hide(mSearchMapFragment);
		return mTransaction;
	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(SearchActivity.this, string, Toast.LENGTH_SHORT).show();
	}

	private void showFragment(int searchBrandFragmentId) {
		// TODO Auto-generated method stub
		switch (searchBrandFragmentId) {

		case SEARCH_BRAND_FRAGMENT_ID:
			mTransaction = hideFragment();
			mTransaction.show(mSearchBrandFragment);
			addToSBackStack(SEARCH_BRAND_FRAGMENT_STRING);
			mTransaction.commit();
			break;

		case SEARCH_BRAND_DETAIL_FRAGMENT_ID:
			mTransaction = hideFragment();
			mTransaction.show(mSearchBrandDetailFragment);
			addToSBackStack(SEARCH_BRAND_DETAIL_FRAGMENT_STRING);
			mTransaction.commit();
			break;

		case SEARCH_MAP_FRAGMENT_ID:
			mTransaction = hideFragment();
			mTransaction.show(mSearchMapFragment);
			addToSBackStack(SEARCH_MAP_FRAGMENT_STRING);
			mTransaction.commit();
			break;
		default:
			break;
		}
	}

	public void onClickBtnBack(View v) {
		finish();
	}

	@Override
	public void onBackPressed() {

		/*
		 * if (backstack.size() == 0) { if(mTopPanel.isOpen()){
		 * mTopPanel.setOpen(false, true); return; } super.onBackPressed();
		 * return; } if (backstack.size() == 1) { if
		 * (!backstack.get(0).equals(VIEW_HOME)) { showToast(backstack.get(0));
		 * mTransaction = hideFragment(); mTransaction.show(mFmHome);
		 * backstack.clear(); addToSBackStack(VIEW_HOME); mFmHome.refreshUI();
		 * mTransaction.commitAllowingStateLoss(); } else {
		 * if(mTopPanel.isOpen()){ mTopPanel.setOpen(false, true); return; }
		 * super.onBackPressed(); backstack.clear(); } return; }
		 */
		try {
			backstack.remove(backstack.size() - 1);
			if (backstack.size() == 0) {
				super.onBackPressed();
				return;
			}
		} catch (IndexOutOfBoundsException e) {
			super.onBackPressed();
			return;
		}
		String currentView = backstack.get(backstack.size() - 1);
		mTransaction = hideFragment();
		if (currentView.equals(SEARCH_BRAND_FRAGMENT_STRING)) {
			mTransaction.show(mSearchBrandFragment);
			// mMainPersonalInfoFragment.refreshUI();
		} else if (currentView.equals(SEARCH_BRAND_DETAIL_FRAGMENT_STRING)) {
			mTransaction.show(mSearchBrandDetailFragment);
			// mPersonalFriendFragment.
		} else if (currentView.equals(SEARCH_MAP_FRAGMENT_STRING)) {
			mTransaction.show(mSearchMapFragment);
			// mPersonalFriendFragment.
			// mPersonalFriendFragment.
		}
		mTransaction.commitAllowingStateLoss();
	}

}
