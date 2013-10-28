package vn.com.shoppie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MScrollView extends ScrollView{
	private OnReachBottom onReachBottom;
	private boolean stopScroll = false;
	
	public MScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
//		if(ev.getPointerCount() > 1)
//			return false;
		
		if(!stopScroll)
			return super.onTouchEvent(ev);
		else
			return false;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		View view = (View) getChildAt(getChildCount()-1);

		if(view != null){
			// Calculate the scrolldiff
			int diff = (view.getBottom()-(getHeight()+getScrollY()));
	
			// if diff is zero, then the bottom has been reached
			if( diff == 0 )
			{
				if(onReachBottom != null)
					onReachBottom.onReachBottom();
			}
		}
		super.onScrollChanged(l, t, oldl, oldt);
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

	interface OnReachBottom{
		public void onReachBottom();
	}
}
