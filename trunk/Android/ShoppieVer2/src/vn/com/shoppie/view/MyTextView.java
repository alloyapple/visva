package vn.com.shoppie.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

	public MyTextView(Context context) {
		super(context);
        init();
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        init();
	}

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
    	Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "roboto.ttf");
        setTypeface(typeface);
	}
}
