package vn.com.shoppie.fragment;

import java.util.ArrayList;

import vn.com.shoppie.R;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FriendDetailFragment extends FragmentBasic {
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
	private FragmentManager mFmManager;
	private FragmentTransaction mTransaction;
	private TextView mTxtTitle;
	// =========================Class Define --------------------
	// =========================Variable Define==================
	private ArrayList<String> backstack = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_personal_friend_detail, null);
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
}
