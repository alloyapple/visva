package com.visva.android.hangman.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class SMImageButton extends ImageButton {

	public SMImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		this.setMeasuredDimension(parentWidth / 2, parentHeight);
	}

}
