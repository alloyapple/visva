package vn.com.shoppie.util;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class SceneAnimation {
	private ImageView mImageView;
	private int[] mFrameRess;
	private int[] mDurations;
	private int mDuration;

	private int mLastFrameNo;
	private long mBreakDelay;

	private boolean mLoop = false;

	/**
	 * @param play
	 *            (1)
	 * */
	public SceneAnimation(ImageView pImageView, boolean loop, int[] pFrameRess, int[] pDurations) {
		mImageView = pImageView;
		mFrameRess = pFrameRess;
		mDurations = pDurations;
		mLastFrameNo = pFrameRess.length - 1;

		mImageView.setImageResource(mFrameRess[0]);
		// play(1);
		this.mLoop = loop;
	}

	/**
	 * @param playConstant
	 *            (1)
	 * */
	public SceneAnimation(ImageView pImageView, boolean loop, int[] pFrameRess, int pDuration) {
		mImageView = pImageView;
		mFrameRess = pFrameRess;
		mDuration = pDuration;
		mLastFrameNo = pFrameRess.length - 1;

		mImageView.setImageResource(mFrameRess[0]);
		// playConstant(1);
		this.mLoop = loop;
	}

	/**
	 * @param playConstant
	 *            (1)
	 * */
	public SceneAnimation(ImageView pImageView, boolean loop, int[] pFrameRess, int pDuration, long pBreakDelay) {
		mImageView = pImageView;
		mFrameRess = pFrameRess;
		mDuration = pDuration;
		mLastFrameNo = pFrameRess.length - 1;
		mBreakDelay = pBreakDelay;

		mImageView.setImageResource(mFrameRess[0]);
		// playConstant(1);
		this.mLoop = loop;
	}

	/**
	 * @param pFramNo
	 *            : 0/1
	 * */
	public void play(final int pFrameNo) {
		mImageView.setVisibility(View.VISIBLE);
		mImageView.postDelayed(new Runnable() {
			public void run() {
				mImageView.setImageResource(mFrameRess[pFrameNo]);
				if (pFrameNo == mLastFrameNo) {
					if (mLoop) {
						play(0);
					}
				} else
					play(pFrameNo + 1);
			}
		}, mDurations[pFrameNo]);
	}

	/**
	 * @param pFramNo
	 *            : 0/1
	 * */
	public void playConstant(final int pFrameNo) {
		Log.e("PFrameNo", pFrameNo + "");
		mImageView.setVisibility(View.VISIBLE);
		mImageView.postDelayed(new Runnable() {
			public void run() {
				if (mListener != null)
					mListener.onAnimationStart(null);
				mImageView.setImageResource(mFrameRess[pFrameNo]);

				if (pFrameNo == mLastFrameNo) {
					if (mLoop) {
						if (mListener != null) {
							mListener.onAnimationRepeat(null);
						}
						playConstant(0);
					} else {
						if (mListener != null)
							mListener.onAnimationEnd(null);
					}
				} else
					playConstant(pFrameNo + 1);
			}
		}, pFrameNo == mLastFrameNo && mBreakDelay > 0 ? mBreakDelay : mDuration);
	}

	OnSceneAnimationListener mListener = null;

	public void setOnAnimationListener(OnSceneAnimationListener listener) {
		this.mListener = listener;
	}

	public interface OnSceneAnimationListener extends AnimationListener {
	}
};