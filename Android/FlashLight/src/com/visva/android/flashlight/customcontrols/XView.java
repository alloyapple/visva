package com.visva.android.flashlight.customcontrols;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.visva.android.flashlight.R;

public class XView extends View {

	public XView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public XView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public XView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas) {
		this.drawBackground(canvas);
	}
	
	protected void drawBackground(Canvas canvas){
		Drawable __background = (Drawable)getResources().getDrawable(R.drawable.mainbackground);
		if(__background != null) {
			__background.setBounds(5, 5, getWidth() - 5, getHeight() - 5);
			__background.draw(canvas);
		}
		
		NinePatchDrawable __drawable = (NinePatchDrawable)getResources().getDrawable(R.drawable.frame_small);
		if(__drawable != null) {
			__drawable.setBounds(0, 0, getWidth(), getHeight());
			__drawable.draw(canvas);
		}
	}
}
