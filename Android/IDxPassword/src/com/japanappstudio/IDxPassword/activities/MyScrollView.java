package com.japanappstudio.IDxPassword.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{
	private float x0, y0;
	private float xTouch, yTouch;
	private SettingActivity activityCall;
	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xTouch = x0 = ev.getX();
			yTouch = y0 = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			xTouch = ev.getX();
			yTouch = ev.getY();
			if (Math.abs(xTouch - x0) > Math.abs(yTouch - y0)
					&& x0 - xTouch > activityCall.getWidthScreen() / 6) {
				activityCall.onReturn(null);
				return true;

			}

		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public void setActivityCall(SettingActivity activityCall) {
		this.activityCall = activityCall;
	}

	public SettingActivity getActivityCall() {
		return activityCall;
	}
	

}
