package com.sharebravo.bravo.view.lib.undo_listview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * A {@link android.view.View.OnTouchListener} that makes any {@link View}
 * dismissable when the user swipes (drags her finger) horizontally across the
 * view.
 * 
 * <p>
 * <em>For {@link android.widget.ListView} list items that don't manage their own touch events
 * (i.e. you're using
 * {@link android.widget.ListView#setOnItemClickListener(android.widget.AdapterView.OnItemClickListener)}
 * or an equivalent listener on {@link android.app.ListActivity} or
 * {@link android.app.ListFragment}, use {@link SwipeDismissListViewTouchListener} instead.</em>
 * </p>
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * view.setOnTouchListener(new SwipeDismissTouchListener(view, null, // Optional
 * 																	// token/cookie
 * 																	// object
 * 		new SwipeDismissTouchListener.OnDismissCallback() {
 * 			public void onDismiss(View view, Object token) {
 * 				parent.removeView(view);
 * 			}
 * 		}));
 * </pre>
 * 
 * <p>
 * This class Requires API level 12 or later due to use of
 * {@link android.view.ViewPropertyAnimator}.
 * </p>
 * 
 * @see SwipeDismissListViewTouchListener
 */
public class SwipeDismissTouchListener implements View.OnTouchListener {
	// Cached ViewConfiguration and system-wide constant values
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;
	private long mAnimationTime;

	// Fixed properties
	private View mView;
	private OnDismissCallback mCallback;
	private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero

	// Transient properties
	private Object mToken;
	private float mTranslationX;
	private GestureDetectorCompat mDetector;

	/**
	 * The callback interface used by {@link SwipeDismissTouchListener} to
	 * inform its client about a successful dismissal of the view for which it
	 * was created.
	 */
	public interface OnDismissCallback {
		/**
		 * Called when the user has indicated they she would like to dismiss the
		 * view.
		 * 
		 * @param view
		 *            The originating {@link View} to be dismissed.
		 * @param token
		 *            The optional token passed to this object's constructor.
		 */
		void onEndAnimation(View view, Object token);

		void onStartAnimation(boolean dismissRight);
	}

	/**
	 * Constructs a new swipe-to-dismiss touch listener for the given view.
	 * 
	 * @param view
	 *            The view to make dismissable.
	 * @param token
	 *            An optional token/cookie object to be passed through to the
	 *            callback.
	 * @param callback
	 *            The callback to trigger when the user has indicated that she
	 *            would like to dismiss this view.
	 */
	public SwipeDismissTouchListener(Context context, View view, Object token,
			OnDismissCallback callback) {
		ViewConfiguration vc = ViewConfiguration.get(view.getContext());
		mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
		mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
		mAnimationTime = 500;
		mView = view;
		mToken = token;
		mCallback = callback;
		mDetector = new GestureDetectorCompat(context, new MyGestureListener());
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		// offset because the view is translated during swipe
		motionEvent.offsetLocation(mTranslationX, 0);
		mDetector.onTouchEvent(motionEvent);
		return true;
	}

	private void performDismiss() {
		final ViewGroup.LayoutParams lp = mView.getLayoutParams();
		final int originalHeight = mView.getHeight();

		ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1)
				.setDuration(mAnimationTime);

		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				// Reset view presentation
				mView.setAlpha(1f);
				mView.setTranslationX(0);
				lp.height = originalHeight;
				mView.setLayoutParams(lp);
			}
		});

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				lp.height = (Integer) valueAnimator.getAnimatedValue();
				mView.setLayoutParams(lp);
			}
		});
		if(!dismissRight)
			mView.animate().translationX(0).alpha(1).setDuration(mAnimationTime).setListener(null);
		mCallback.onEndAnimation(mView, mToken);
	}

	boolean dismissRight;

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures";

		@Override
		public boolean onDown(MotionEvent event) {
			Log.d(DEBUG_TAG, "onDown: " + event.toString());
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
				float velocityX, float velocityY) {
			Log.d(DEBUG_TAG,
					"onFling: " + event1.toString() + event2.toString());
			Log.d("KieuThang", "adfdfdf ");
			mViewWidth = mView.getWidth();
			float deltaX = event1.getRawX() - event2.getRawX();
			boolean dismiss = false;
			// boolean dismissRight = false;
			if (Math.abs(deltaX) > mViewWidth / 2) {
				dismiss = true;
				dismissRight = deltaX > 0;
			} else if (mMinFlingVelocity <= velocityX
					&& velocityX <= mMaxFlingVelocity && velocityY < velocityX) {
				dismiss = true;
				dismissRight = velocityY > 0;
			}
			Log.d("KieuThang", "dismiss :" + dismiss + ",dismissRight:"
					+ dismissRight + ",mViewWidth: " + mViewWidth);
			if (dismiss) {
				// dismiss
				mView.animate().translationX(dismissRight ? -100 : 100).alpha(1)
						.setDuration(mAnimationTime)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								performDismiss();
							}

							@Override
							public void onAnimationStart(Animator animation) {
								super.onAnimationStart(animation);
								mCallback.onStartAnimation(dismissRight);
							}
						});
			} else {
				// cancel
				mView.animate().translationX(0).alpha(1)
						.setDuration(mAnimationTime).setListener(null);
			}
			mTranslationX = 0;
			return true;
		}
	}
}