package vn.com.shoppie.fragment;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.FriendDetailAdapter;
import vn.com.shoppie.object.FBUser;
import vn.com.shoppie.object.HorizontalListView;
import vn.com.shoppie.object.OneItem;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FriendDetailFragment extends FragmentBasic {
	// ==========================Constant Define=================

	// ===========================Control Define==================
	private TextView mTxtFriendName;
	private TextView mTxtFriendNumberPie;
	private TextView mTxtFriendId;
	private HorizontalListView mListFavouriteProduct;
	private HorizontalListView mListFavouriteBrand;
	// =========================Class Define --------------------
	private FriendDetailAdapter mFriendDetailAdapter;
	// =========================Variable Define==================
	private ArrayList<String> backstack = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_personal_friend_detail, null);

		mTxtFriendId = (TextView) root
				.findViewById(R.id.txt_personal_friend_id);
		mTxtFriendName = (TextView) root
				.findViewById(R.id.txt_personal_friend_name);
		mTxtFriendNumberPie = (TextView) root
				.findViewById(R.id.txt_personal_friend_id);
		mListFavouriteBrand = (HorizontalListView) root
				.findViewById(R.id.list_friend_favorite_category);
		mListFavouriteProduct = (HorizontalListView) root
				.findViewById(R.id.list_friend_favorite_product);

		mFriendDetailAdapter = new FriendDetailAdapter(getActivity(),
				constructList());
		mListFavouriteBrand.setAdapter(mFriendDetailAdapter);
		mListFavouriteProduct.setAdapter(mFriendDetailAdapter);
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

	private ArrayList<OneItem> constructList() {
		ArrayList<OneItem> al = new ArrayList<OneItem>();

		OneItem op = new OneItem(R.drawable.maison, "maison");
		al.add(op);

		OneItem op2 = new OneItem(R.drawable.dans, "dans");
		al.add(op2);

		OneItem op3 = new OneItem(R.drawable.dort, "dort");
		al.add(op3);

		OneItem op4 = new OneItem(R.drawable.garcon, "gar�on");
		al.add(op4);

		OneItem op5 = new OneItem(R.drawable.le, "le");
		al.add(op5);

		return al;
	}

	public void updateUser(FBUser friend) {
		// TODO Auto-generated method stub

	}

}
