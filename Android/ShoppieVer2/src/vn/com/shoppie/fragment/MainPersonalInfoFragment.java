package vn.com.shoppie.fragment;

import vn.com.shoppie.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

	// =========================Class Define --------------------
	private MainPersonalInfoListener mListener;

	// =========================Variable Define==================
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_main_personal_info, null);

		initialize(root);
		return root;
	}

	private void initialize(View v) {
		// TODO Auto-generated method stub
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

		mLayoutFavouriteProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickFavouriteProduct();
			}
		});

		mLayoutFrvouriteCategory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickFavouriteCategory();
			}
		});

		mLayoutFeedback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onClickFeedback();
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

		public void onClickFavouriteProduct();

		public void onClickFavouriteCategory();

		public void onClickHelp();

		public void onClickHistoryTrade();

		public void onClickFriend();

		public void onClickFeedback();
	}

	public void setListener(MainPersonalInfoListener listener) {
		this.mListener = listener;
	}
}
