package com.visva.android.flashlight.gesture;

import android.view.MotionEvent;
import android.view.View;

public class ActivitySwipeDetector implements View.OnTouchListener {
	private SwipeInterface _swipeInterface;
	private static final int MIN_DISTANCE = 50;
	private float _downX, _downY, _upX, _upY;
	
	public ActivitySwipeDetector(SwipeInterface swipeInterface) {
		this._swipeInterface = swipeInterface;
	}
	
	private void onBottomToTop(View view, float distance){
		this._swipeInterface.BottomToTop(view, distance);
	}
	
	private void onLeftToRight(View view, float distance){
		this._swipeInterface.LeftToRight(view, distance);
	}
	
	private void onRightToLeft(View view, float distance){
		this._swipeInterface.RightToLeft(view, distance);
	}
	
	private void onTopToBottom(View view, float distance){
		this._swipeInterface.TopToBottom(view, distance);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN: {
				_downX = event.getX();
				_downY = event.getY();
				return true;
			}
			
			case MotionEvent.ACTION_UP: {
				_upX = event.getX();
				_upY = event.getY();
				
				float __deltaX = _downX - _upX;
				float __deltaY = _downY - _upY;
				
				if(Math.abs(__deltaX) > MIN_DISTANCE){
					if(__deltaX < 0) {
						this.onLeftToRight(v, __deltaX);
						return true;
					}
					if(__deltaX > 0) {
						this.onRightToLeft(v, __deltaX);
						return true;
					}
				}
				
				if(Math.abs(__deltaY) > MIN_DISTANCE){
					if(__deltaY < 0) {
						this.onTopToBottom(v, __deltaY);
						return true;
					}
					if(__deltaY > 0) {
						this.onBottomToTop(v, __deltaY);
						return true;
					}
				}
			}
		}
		
		return false;
	}

}
