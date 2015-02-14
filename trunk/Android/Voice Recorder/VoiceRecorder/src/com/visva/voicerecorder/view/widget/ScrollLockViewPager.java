package com.visva.voicerecorder.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ScrollLockViewPager extends ViewPager {

	public ScrollLockViewPager(Context context) {
		super(context);
	}

	public ScrollLockViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

	// @Override
	// protected boolean canScroll(View v, boolean checkV, int dx, int x, int y)
	// {
	// if (v instanceof ViewPager) {
	// if (getChildAt(getCurrentItem()) != null) {
	// // get the ListView of current Fragment
	// SwipeMenuListView enhancedListView =
	// findSwipeMenuListView(getChildAt(getCurrentItem()));
	// if (enhancedListView != null)
	// // If the user is in first page and tries to swipe left,
	// // enable
	// // the ListView swipe
	// if (getCurrentItem() == 0 && dx > 0) {
	// enhancedListView.enableSwipeMenu();
	// }
	// // If the user is in second page and tries to swipe right,
	// // enable the ListView swipe
	// else if (getCurrentItem() == 1 && dx < 0) {
	// enhancedListView.enableSwipeMenu();
	// }
	// // Block the ListView swipe there by enabling the parent
	// // ViewPager swiping
	// else {
	// enhancedListView.disableSwipeMenu();
	// }
	// }
	// }
	// return super.canScroll(v, checkV, dx, x, y);
	// }
	//
	// private SwipeMenuListView findSwipeMenuListView(View v) {
	// for (int i = 0; i < ((ViewGroup) v).getChildCount(); ++i) {
	// View nextChild = ((ViewGroup) v).getChildAt(i);
	// if (nextChild instanceof SwipeMenuListView) {
	// return (SwipeMenuListView) nextChild;
	// }
	// }
	// return null;
	// }

}
