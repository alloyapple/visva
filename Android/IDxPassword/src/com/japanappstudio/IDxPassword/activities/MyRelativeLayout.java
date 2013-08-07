package com.japanappstudio.IDxPassword.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MyRelativeLayout extends RelativeLayout {
	private int largestHeight;
	private Context mContext;
	private IParent parent;

	public MyRelativeLayout(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		mContext=context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
		largestHeight = Math.max(largestHeight, getHeight());

		if (largestHeight > proposedheight) {
			// Keyboard is shown
			Toast.makeText(mContext, "show", Toast.LENGTH_SHORT).show();
			if(parent!=null)
				parent.show_keyboard();
		} else {
			Toast.makeText(mContext, "hide", Toast.LENGTH_SHORT).show();
			if(parent!=null)
				parent.hide_keyboard();
		}
		// Keyboard is hidden

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public IParent getIParent() {
		return parent;
	}

	public void setIParent(IParent parent) {
		this.parent = parent;
	}
}
