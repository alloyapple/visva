package vn.com.shoppie.fragment;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.FriendDetailAdapter;
import vn.com.shoppie.constant.ShopieSharePref;
import vn.com.shoppie.database.sobject.HistoryTransactionItem;
import vn.com.shoppie.database.sobject.HistoryTransactionList;
import vn.com.shoppie.object.FacebookUser;
import vn.com.shoppie.object.HorizontalListView;
import vn.com.shoppie.object.MyCircleImageView;
import vn.com.shoppie.object.OneItem;
import vn.com.shoppie.util.ImageLoader;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainPersonalInfoFragment extends FragmentBasic {

	// ==========================Constant Define=================
	// ===========================Control Define==================
	private LinearLayout mLayoutPersonalInfo;
	private LinearLayout mLayoutFavouriteProduct;
	private LinearLayout mLayoutFrvouriteCategory;
	private LinearLayout mLayoutFriend;
	private LinearLayout mLayoutFeedback;
	private LinearLayout mLayoutHelp;
	private LinearLayout mLayoutHistoryTrade;
	private MyCircleImageView mImgAvatar;
	private TextView mTxtUserName;
	private TextView mTxtUserId;
	private TextView mTxtUserNumberPie;
	private HorizontalListView mFavouriteBrandList;
	private HorizontalListView mFavouriteProductList;
	// =========================Class Define --------------------
	private MainPersonalInfoListener mListener;
	private ImageLoader mImageLoader;
	private ShopieSharePref mShopieSharePref;
	private FriendDetailAdapter mFriendDetailAdapter;
	// =========================Variable Define==================
	private boolean isShowFavouriteProduct = false;
	private boolean isShowFavouriteBrand = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_main_personal_info, null);

		mImageLoader = new ImageLoader(getActivity());
		mShopieSharePref = new ShopieSharePref(getActivity());
		initialize(root);
		return root;
	}

	private void initialize(View v) {
		// TODO Auto-generated method stub

		mTxtUserId = (TextView) v.findViewById(R.id.txt_user_id);
		mTxtUserName = (TextView) v.findViewById(R.id.txt_personal_name);
		mTxtUserNumberPie = (TextView) v.findViewById(R.id.txt_user_number_pie);
		mImgAvatar = (MyCircleImageView) v.findViewById(R.id.img_avatar);
		mLayoutFavouriteProduct = (LinearLayout) v
				.findViewById(R.id.layout_fravourite_product);
		mLayoutFeedback = (LinearLayout) v.findViewById(R.id.layout_feedback);
		mLayoutFriend = (LinearLayout) v
				.findViewById(R.id.layout_personal_people);
		mLayoutFrvouriteCategory = (LinearLayout) v
				.findViewById(R.id.layout_fravourite_category);
		mLayoutHelp = (LinearLayout) v.findViewById(R.id.layout_help);
		mLayoutHistoryTrade = (LinearLayout) v
				.findViewById(R.id.layout_history_trade);
		mLayoutPersonalInfo = (LinearLayout) v.findViewById(R.id.layout_info);

		mFavouriteBrandList = (HorizontalListView) v
				.findViewById(R.id.favourite_brand_list);
		mFavouriteProductList = (HorizontalListView) v
				.findViewById(R.id.favourite_product_list);
		mFriendDetailAdapter = new FriendDetailAdapter(getActivity(),
				constructList());
		mFavouriteBrandList.setAdapter(mFriendDetailAdapter);
		mFavouriteProductList.setAdapter(mFriendDetailAdapter);
		mLayoutFavouriteProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowFavouriteProduct)
					mFavouriteProductList.setVisibility(View.GONE);
				else
					mFavouriteProductList.setVisibility(View.VISIBLE);
				isShowFavouriteProduct = !isShowFavouriteProduct;
			}
		});

		mLayoutFrvouriteCategory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowFavouriteBrand)
					mFavouriteBrandList.setVisibility(View.GONE);
				else
					mFavouriteBrandList.setVisibility(View.VISIBLE);
				isShowFavouriteBrand = !isShowFavouriteBrand;
			}
		});

		mLayoutFeedback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showToast(getActivity().getString(R.string.feedback_content));
				initShareItent(
						getActivity().getString(R.string.feedback_subject),
						getActivity().getString(R.string.feedback_content2),
						getActivity().getString(R.string.feedback_send_to));
			}
		});
		mLayoutFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickFriend();
			}
		});
		mLayoutHelp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickHelp();
			}
		});
		mLayoutHistoryTrade.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickHistoryTrade();
			}
		});
		mLayoutPersonalInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickPersonalInfo();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void onClickMainPersonalInfo(View v) {
		Log.e("click ", "adfdfd ");
	}

	public interface MainPersonalInfoListener {
		public void onClickPersonalInfo();

		public void onClickHelp();

		public void onClickHistoryTrade();

		public void onClickFriend();

		public void onClickFeedback();
	}

	public void setListener(MainPersonalInfoListener listener) {
		this.mListener = listener;
	}

	public void updateUserInfo(FacebookUser user) {
		// TODO Auto-generated method stub
		mImageLoader.DisplayImage(user.getPicture().getData().getUrl(),
				mImgAvatar);
		mTxtUserName.setText(user.getName());
		Log.e("dkdfh " + mShopieSharePref.getCustId(), "asdfkjdh " + mTxtUserId);
		mTxtUserId.setText("ID: " + mShopieSharePref.getCustId());
	}

	public void updatePie(HistoryTransactionList historyTransactionList) {
		// TODO Auto-generated method stub
		HistoryTransactionItem historyTransactionItem = historyTransactionList
				.getResult().get(0);
		mTxtUserNumberPie.setText("Điểm tích lũy: "
				+ historyTransactionItem.getCurrentBal());
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

	private void showToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
	}

	private void initShareItent(String subject, String content, String email) {
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("text/plain");
		share.putExtra(Intent.EXTRA_SUBJECT, "" + subject);
		share.putExtra(Intent.EXTRA_TEXT, "" + content);
		share.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		// if (pFilePath != null)
		// share.putExtra(Intent.EXTRA_STREAM,
		// Uri.fromFile(new File(pFilePath)));
		share.setType("vnd.android.cursor.dir/email");
		startActivity(Intent.createChooser(share, "Select"));

	}
}
