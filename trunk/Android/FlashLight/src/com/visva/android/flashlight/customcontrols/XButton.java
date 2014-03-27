package com.visva.android.flashlight.customcontrols;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.visva.android.flashlight.R;

public class XButton extends Button {

	public XButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public XButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public XButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas) {
		this.drawBackground(canvas);
	}
	
	protected void drawBackground(Canvas canvas){
		Drawable __background = (Drawable)getResources().getDrawable(R.drawable.w_bulb_icon);
		if(__background != null) {
			__background.setBounds(0, 0, getWidth(), getHeight());
			__background.draw(canvas);
		}
		
		NinePatchDrawable __drawable = (NinePatchDrawable)getResources().getDrawable(R.drawable.frame_small);
		if(__drawable != null) {
			__drawable.setBounds(0, 0, getWidth(), getHeight());
			__drawable.draw(canvas);
		}
	}
}
