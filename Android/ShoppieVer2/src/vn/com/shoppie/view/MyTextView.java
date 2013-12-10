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
    	Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "ROBOTO-REGULAR.TTF");
        setTypeface(typeface);
	}
    
    public void setBold() {
    	Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "ROBOTO-BOLD.TTF");
        setTypeface(typeface);
    }
    
    public void setNormal() {
    	Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "ROBOTO-REGULAR.TTF");
        setTypeface(typeface);
    }
    
    public void setItalic() {
    	Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "ROBOTO-ITALIC.TTF");
        setTypeface(typeface);
    }
    
    public void setLight() {
    	Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "ROBOTO-LIGHT.TTF");
        setTypeface(typeface);
    }
}
