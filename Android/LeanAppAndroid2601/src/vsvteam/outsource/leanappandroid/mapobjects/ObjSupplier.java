package vsvteam.outsource.leanappandroid.mapobjects;

import vsvteam.outsource.leanappandroid.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ObjSupplier extends MapObject {

	private String mTextContent;

	public ObjSupplier(Context pContext, float pX, float pY,String pText) {
		super(pContext, pX, pY);
		// TODO Auto-generated constructor stub
		setTextContent(pText);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.setLayoutParams(new RelativeLayout.LayoutParams(120, 80));
		getParam().setMargins((int) pX, (int) pY, getParam().rightMargin,
				getParam().bottomMargin);
		mLayout.requestLayout();
		mLayout.setBackgroundResource(R.drawable.obj_customer);
		initialize();

	}

	public void initialize() {
		TextView mTextView = new TextView(mContext);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mTextView.setLayoutParams(param);
		param.bottomMargin = 4;
		mTextView.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		mTextView.setLines(8);
		mTextView.setText(mTextContent);
		mTextView.setTextColor(Color.BLACK);
		// mTextView.setTypeface(Typeface.BOLD);
		mLayout.addView(mTextView);
	}

	public void setTextContent(String mTextContent) {
		this.mTextContent = mTextContent;
	}

	public String getTextContent() {
		return mTextContent;
	}

	@Override
	public void drawObjects(Canvas cv) {
		// TODO Auto-generated method stub
		
	}


}
