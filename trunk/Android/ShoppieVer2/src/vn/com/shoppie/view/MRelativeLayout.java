package vn.com.shoppie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

public class MRelativeLayout extends RelativeLayout {

	public MRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void addViewNonLayout(View child, int index, LayoutParams params){
		addViewInLayout(child, index, params, false);
	}

}
