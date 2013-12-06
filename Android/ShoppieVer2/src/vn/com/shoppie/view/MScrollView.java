package vn.com.shoppie.view;

import java.util.Currency;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MScrollView extends LinearLayout{
	private OnReachBottom onReachBottom;
	private boolean stopScroll = false;
	private boolean isReachBottom = false;
	
	private int currY = 0;
//	public MScrollView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		init();
//	}

	public MScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MScrollView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setGravity(Gravity.CENTER);
	}
	
	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
//		super.scrollTo(x, y);
	}
	
	public void scroll(int y) {
		if (stopScroll)
			return;
		currY += y;
		if(getChildCount() > 0) {
			if(getHeight() - realChildHeight < 0) {
				if(currY > realChildHeight - getHeight()) {
					currY = realChildHeight - getHeight();
					isReachBottom = true;
				}
				else if(currY < 0) {
					currY = 0;
				}
			}
			else {
				isReachBottom = false;
			}
		}
		getChildAt(0).scrollTo(0, currY);
	}
	
	public void setRealChildHeight(int height) {
		this.realChildHeight = height;
	}
	
	private int realChildHeight;
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		if(ev.getPointerCount() > 1)
//			return false;
		
		if(!stopScroll)
			return super.onTouchEvent(ev);
		else
			return false;
	}
	
//	@Override
//	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//		// TODO Auto-generated method stub
//		View view = (View) getChildAt(getChildCount()-1);
//
//		if(view != null){
//			// Calculate the scrolldiff
//			int diff = (view.getBottom()-(getHeight()+getScrollY()));
//	
//			// if diff is zero, then the bottom has been reached
//			if( diff == 0 )
//			{
//				if(onReachBottom != null)
//					onReachBottom.onReachBottom();
//			}
//			if(diff < 3)
//				isReachBottom = true;
//			else
//				isReachBottom = false;
//		}
//		Log.d("Scroll", "Change");
//		super.onScrollChanged(l, t, oldl, oldt);
//	}

	public void reset() {
		isReachBottom = false;
		currY = 0;
		if(getChildCount() > 0) {
			getChildAt(0).scrollTo(0, 0);
		}
	}
	
	public void setOnReachBottom(OnReachBottom onReachBottom) {
		this.onReachBottom = onReachBottom;
	}

	public boolean isStopScroll() {
		return stopScroll;
	}

	public void setStopScroll(boolean stopScroll) {
		this.stopScroll = stopScroll;
	}

	public boolean isReachBottom() {
		if(getChildCount() == 0)
			return true;
		return getHeight() >= realChildHeight || isReachBottom;
	}

	interface OnReachBottom{
		public void onReachBottom();
	}
}
