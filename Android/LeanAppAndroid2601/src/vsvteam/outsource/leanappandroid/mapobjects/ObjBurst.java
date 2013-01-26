package vsvteam.outsource.leanappandroid.mapobjects;

import vsvteam.outsource.leanappandroid.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ObjBurst extends MapObject {

	public ObjBurst(Context pContext, float pX, float pY) {
		super(pContext, pX, pY);
		// TODO Auto-generated constructor stub
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.setLayoutParams(new RelativeLayout.LayoutParams(110, 80));
		getParam().setMargins((int) pX, (int) pY, getParam().rightMargin,
				getParam().bottomMargin);
		mLayout.requestLayout();

		mLayout.setBackgroundResource(R.drawable.obj_burst);

		initialize();
	}

	public void initialize() {
		TextView mTextView = new TextView(mContext);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mTextView.setLayoutParams(param);
		mTextView.setGravity(Gravity.CENTER | Gravity.CENTER);
		mTextView.setLines(6);
		mTextView.setText("Burst");
		mTextView.setTextColor(Color.BLACK);
		// mTextView.setTypeface(Typeface.BOLD);
		mLayout.addView(mTextView);
	}

	@Override
	public void drawObjects(Canvas cv) {
		// TODO Auto-generated method stub

	}

}
