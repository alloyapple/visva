package com.visva.android.flashlight.gesture;

import android.view.View;

public interface SwipeInterface {
	public void BottomToTop(View view, float distance);
	
	public void LeftToRight(View view, float distance);
	
	public void RightToLeft(View view, float distance);
	
	public void TopToBottom(View view, float distance);
}
