package vn.com.shoppie.view;

import vn.com.shoppie.R;
import vn.com.shoppie.util.SceneAnimation;
import vn.com.shoppie.util.SceneAnimation.OnSceneAnimationListener;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class PopupPie extends PopupWindow {
	Context context;
	View contentView;

	public OnSceneAnimationListener listener;

	private ImageView mTapScreenTextAnimImgView;
	private final int[] mTapScreenTextAnimRes = { R.drawable.pie_01, R.drawable.pie_02, R.drawable.pie_03, R.drawable.pie_04, R.drawable.pie_05, R.drawable.pie_06, R.drawable.pie_07, R.drawable.pie_08, R.drawable.pie_09, R.drawable.pie_10, R.drawable.pie_11, R.drawable.pie_12, R.drawable.pie_13, R.drawable.pie_14, R.drawable.pie_15, R.drawable.pie_16, R.drawable.pie_17, R.drawable.pie_18, R.drawable.pie_19, R.drawable.pie_20, R.drawable.pie_21, R.drawable.pie_22, R.drawable.pie_23 };
	private final int mTapScreenTextAnimDuration = 10;
	private final int mTapScreenTextAnimBreak = -1;

	SceneAnimation animShoppie;

	public PopupPie(Context context) {
		super(context);
		this.context = context;
		contentView = LayoutInflater.from(context).inflate(R.layout.layout_pie, null);
		setContentView(contentView);
		setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mTapScreenTextAnimImgView = (ImageView) contentView.findViewById(R.id.activity_logo_iv_bubble);
		
		animShoppie = new SceneAnimation(mTapScreenTextAnimImgView, false, mTapScreenTextAnimRes, mTapScreenTextAnimDuration, mTapScreenTextAnimBreak);
		animShoppie.setOnAnimationListener(new OnSceneAnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mTapScreenTextAnimImgView.setVisibility(View.VISIBLE);
				if (listener != null) {
					listener.onAnimationStart(animation);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				if (listener != null) {
					listener.onAnimationRepeat(animation);
				}
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mTapScreenTextAnimImgView.setVisibility(View.GONE);
				try {
					PopupPie.this.dismiss();
				} catch (NullPointerException e) {
				}catch (IllegalArgumentException e) {
				}
				if (listener != null) {
					listener.onAnimationEnd(animation);
				}
			}
		});
		setBackgroundDrawable(null);
		// showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
		// update(50, 50, 300, 80);
	}

	// private static PopupPie _instance = null;

	public static PopupPie getInstance(Context context) {
		return new PopupPie(context);
		// if (_instance == null || _instance.context == null) {
		// _instance = new PopupPie(context);
		// }
		// return _instance;
	}

	public void setOnAnimListener(OnSceneAnimationListener listener) {
		this.listener = listener;
	}

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	public void fire(Context context) {
		try {
			showAtLocation(contentView, Gravity.BOTTOM, 10, 10);
		} catch (BadTokenException e) {
			// _instance = new PopupPie(context);
			return;
		} catch (IllegalStateException e) {
			// _instance = new PopupPie(context);
			return;
		}
		update(50, 50, 300, 80);
		mTapScreenTextAnimImgView.setVisibility(View.VISIBLE);
		animShoppie.playConstant(1);
	}
}
