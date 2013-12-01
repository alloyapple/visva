package vn.com.shoppie.fragment;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.SearchActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;

public class SearchResultFragment extends FragmentBasic{
	private View viewLoading;
	private View viewConfirm;
	private View viewResult;
	private Button btnAccept;
	private Button btnCancel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_search_result_fragment, null);
		viewLoading = root.findViewById(R.id.loading_layout);
		viewConfirm = root.findViewById(R.id.confirm_layout);
		viewResult = root.findViewById(R.id.result_layout);

		viewLoading.setVisibility(View.VISIBLE);
		viewConfirm.setVisibility(View.GONE);
		viewResult.setVisibility(View.GONE);

		btnAccept = (Button) root.findViewById(R.id.btn_accept);
		btnCancel = (Button) root.findViewById(R.id.btn_cancel);

		btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewLoading.setVisibility(View.GONE);
				viewConfirm.setVisibility(View.GONE);
				viewResult.setVisibility(View.VISIBLE);
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SearchActivity) getActivity()).onBackPressed();
			}
		});

		View checkinCircle = root.findViewById(R.id.loading);

		int angle = 900;
		int width = (int) getResources().getDimension(R.dimen.loading_pie_width);
		int height = (int) getResources().getDimension(R.dimen.loading_pie_height);
		final RotateAnimation anim = new RotateAnimation(0, angle,
				width / 2, height / 2);

		anim.setDuration(angle * 7);

		anim.setInterpolator(new Interpolator() {

			@Override
			public float getInterpolation(float input) {
				// TODO Auto-generated method stub
				return input;
			}
		});

		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				viewLoading.setVisibility(View.GONE);
				viewConfirm.setVisibility(View.VISIBLE);
				viewResult.setVisibility(View.GONE);
			}
		});

		checkinCircle.startAnimation(anim);


		return root;
	}
}
