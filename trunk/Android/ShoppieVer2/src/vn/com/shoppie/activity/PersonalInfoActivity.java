package vn.com.shoppie.activity;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.fragment.FavouriteFragment;
import vn.com.shoppie.fragment.FragmentPersonalInfo;
import vn.com.shoppie.fragment.HistoryTradeFragment;
import vn.com.shoppie.fragment.MainPersonalInfoFragment;
import vn.com.shoppie.fragment.PersonalFriendFragment;
import vn.com.shoppie.fragment.MainPersonalInfoFragment.MainPersonalInfoListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalInfoActivity extends FragmentActivity implements
		MainPersonalInfoListener {
	// ==========================Constant Define=================
	private static final String MAIN_PERSONAL_INFO_FRAGMENT = "main_info";
	private static final String PERSONAL_FRIEND_FRAGMENT = "friend";
	private static final String FAVOURITE_FRAGMENT = "favourite";
	private static final String HISTORY_TRADE_FRAGMET = "history_trade";
	private static final String FRAGMENT_PERSONAL_INFO = "personal_info";
	private static final int MAIN_PERSONAL_INFO = 1001;
	private static final int PERSONAL_FRIEND = 1002;
	private static final int HISTORY_TRADE = 1003;
	private static final int FAVOURITE = 1004;
	private static final int PERSONAL_INFO = 1005;
	// ===========================Control Define==================
	private MainPersonalInfoFragment mMainPersonalInfoFragment;
	private PersonalFriendFragment mPersonalFriendFragment;
	private FragmentPersonalInfo mFragmentPersonalInfo;
	private HistoryTradeFragment mHistoryTradeFragment;
	private FavouriteFragment mFavouriteFragment;
	private ArrayList<Fragment> mLstFragments;
	private FragmentManager mFmManager;
	private FragmentTransaction mTransaction;
	private TextView mTxtTitle;
	// =========================Class Define --------------------
	// =========================Variable Define==================
	private ArrayList<String> backstack = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.page_personal_info);

		initialize();

	}

	private void initialize() {
		// TODO Auto-generated method stub
		mFmManager = getSupportFragmentManager();
		mMainPersonalInfoFragment = (MainPersonalInfoFragment) mFmManager
				.findFragmentById(R.id.layout_personal_main_info);
		mPersonalFriendFragment = (PersonalFriendFragment) mFmManager
				.findFragmentById(R.id.layout_personal_friend);
		mFavouriteFragment = (FavouriteFragment) mFmManager
				.findFragmentById(R.id.layout_personal_favourite);
		mFragmentPersonalInfo = (FragmentPersonalInfo) mFmManager
				.findFragmentById(R.id.layout_personal_info_fragment);
		mHistoryTradeFragment = (HistoryTradeFragment) mFmManager
				.findFragmentById(R.id.layout_personal_history_trade);

		mTxtTitle = (TextView) findViewById(R.id.txt_title_fragment);
		mMainPersonalInfoFragment.setListener(this);

		showFragment(MAIN_PERSONAL_INFO);
		mTransaction = hideFragment();
		mTransaction.show(mMainPersonalInfoFragment);
		addToSBackStack(MAIN_PERSONAL_INFO_FRAGMENT);
		mTransaction.commit();
	}

	private void showFragment(int fragment) {
		// TODO Auto-generated method stub
		switch (fragment) {

		case MAIN_PERSONAL_INFO:
			mTransaction = hideFragment();
			mTransaction.show(mMainPersonalInfoFragment);
			addToSBackStack(MAIN_PERSONAL_INFO_FRAGMENT);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personal_info));
			break;

		case PERSONAL_FRIEND:
			mTransaction = hideFragment();
			mTransaction.show(mPersonalFriendFragment);
			addToSBackStack(PERSONAL_FRIEND_FRAGMENT);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personl_friend));
			break;

		case HISTORY_TRADE:
			mTransaction = hideFragment();
			mTransaction.show(mHistoryTradeFragment);
			addToSBackStack(HISTORY_TRADE_FRAGMET);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personl_friend));
			break;

		case FAVOURITE:
			mTransaction = hideFragment();
			mTransaction.show(mFavouriteFragment);
			addToSBackStack(FAVOURITE_FRAGMENT);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personl_friend));
			break;

		case PERSONAL_INFO:
			mTransaction = hideFragment();
			mTransaction.show(mFragmentPersonalInfo);
			addToSBackStack(FRAGMENT_PERSONAL_INFO);
			mTransaction.commit();
			mTxtTitle.setText(getString(R.string.personl_friend));
			break;
		default:
			break;
		}
	}

	public FragmentTransaction hideFragment() {
		mTransaction = mFmManager.beginTransaction();
		mTransaction.hide(mMainPersonalInfoFragment);
		mTransaction.hide(mPersonalFriendFragment);
		mTransaction.hide(mFavouriteFragment);
		mTransaction.hide(mFragmentPersonalInfo);
		mTransaction.hide(mHistoryTradeFragment);
		if (!backstack.isEmpty())
			showToast(backstack.get(0));
		return mTransaction;
	}

	private void showToast(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(PersonalInfoActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	public void onClickBackPersonal(View v) {
		finish();
	}

	public void addToSBackStack(String tag) {
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

	@Override
	public void onBackPressed() {

		showToast("BackPress");
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
		if (currentView.equals(MAIN_PERSONAL_INFO_FRAGMENT)) {
			mTransaction.show(mMainPersonalInfoFragment);
			// mMainPersonalInfoFragment.refreshUI();
		} else if (currentView.equals(PERSONAL_FRIEND_FRAGMENT)) {
			mTransaction.show(mPersonalFriendFragment);
			// mPersonalFriendFragment.
		} else if (currentView.equals(FAVOURITE_FRAGMENT)) {
			mTransaction.show(mFavouriteFragment);
			// mPersonalFriendFragment.
		} else if (currentView.equals(HISTORY_TRADE_FRAGMET)) {
			mTransaction.show(mHistoryTradeFragment);
			// mPersonalFriendFragment.
		} else if (currentView.equals(FRAGMENT_PERSONAL_INFO)) {
			mTransaction.show(mFragmentPersonalInfo);
			// mPersonalFriendFragment.
		}
		mTransaction.commitAllowingStateLoss();
	}

	@Override
	public void onClickPersonalInfo() {
		// TODO Auto-generated method stub
		Log.e("onclickinfo", "onCLick oinfo");
		showFragment(PERSONAL_INFO);
	}

	@Override
	public void onClickFavouriteProduct() {
		// TODO Auto-generated method stub
		showFragment(FAVOURITE);
	}

	@Override
	public void onClickFavouriteCategory() {
		// TODO Auto-generated method stub
		showFragment(FAVOURITE);
	}

	@Override
	public void onClickHelp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickHistoryTrade() {
		// TODO Auto-generated method stub
		showFragment(HISTORY_TRADE);
	}

	@Override
	public void onClickFriend() {
		// TODO Auto-generated method stub
		showFragment(PERSONAL_FRIEND);
	}

	@Override
	public void onClickFeedback() {
		// TODO Auto-generated method stub

	}

}
