package vsvteam.outsource.leanappandroid.mapobjects;

import vsvteam.outsource.leanappandroid.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ObjInventory extends MapObject {

	public ObjInventory(Context pContext, float pX, float pY) {
		super(pContext, pX, pY);
		// TODO Auto-generated constructor stub
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.setLayoutParams(new RelativeLayout.LayoutParams(120, 90));
		getParam().setMargins((int) pX, (int) pY, getParam().rightMargin,
				getParam().bottomMargin);
		mLayout.requestLayout();
		initialize();
	}

	public void initialize() {
		TextView mTextView = new TextView(mContext);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 0, 0.25f);
		mTextView.setLayoutParams(param);
		mTextView.setGravity(Gravity.CENTER | Gravity.CENTER);
		mTextView.setText("Inventory");
		mTextView.setTextColor(Color.BLACK);
		// mTextView.setTypeface(Typeface.BOLD);
		ImageView iconInventory = new ImageView(mContext);
		iconInventory.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 0, 0.75f));
		iconInventory.setImageResource(R.drawable.obj_inventory);
		mLayout.addView(mTextView);
		mLayout.addView(iconInventory);
	}

	@Override
	public void drawObjects(Canvas cv) {
		// TODO Auto-generated method stub

	}

}
