package vsvteam.outsource.leanappandroid.mapobjects;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class MapObject {
	protected LinearLayout mLayout;
	protected EditText mEditText[];
	protected PointF mCenter = new PointF();
	protected boolean mIsSelect = false;
	protected Context mContext;
	protected ArrayList<PointF> mPorts = new ArrayList<PointF>();

	public MapObject(Context pContext, float pX, float pY) {
		mContext= pContext;
		mLayout = new LinearLayout(pContext);
//		mCenter.x=pX+mLayout.getWidth() / 2;
//		mCenter.y=pY+mLayout.getHeight() / 2;
	}

	public RelativeLayout.LayoutParams getParam() {
		return ((RelativeLayout.LayoutParams) mLayout.getLayoutParams());
	}

	public void move(float pDeltaX, float pDeltaY) {
		for (int i = 0; i < mPorts.size(); i++) {
			mPorts.get(i).x += pDeltaX;
			mPorts.get(i).y += pDeltaY;
		}
		
		getParam().setMargins(getParam().leftMargin+(int)pDeltaX, getParam().topMargin+(int)pDeltaY, getParam().rightMargin,
				getParam().bottomMargin);
		mLayout.requestLayout();
	}

	public abstract void drawObjects(Canvas cv);

	public LinearLayout getLayout() {
		return mLayout;
	}

	public void setLayout(LinearLayout mLayout) {
		this.mLayout = mLayout;
	}

	public boolean isScope(MotionEvent e) {
		if (getParam().leftMargin < e.getX()
				&& getParam().leftMargin + getLayout().getWidth() > e.getX()
				&& getParam().topMargin < e.getY()
				&& getParam().topMargin + getLayout().getHeight() > e.getY()) {
			return true;
		}
		return false;
	}

	public boolean isIsSelect() {
		return mIsSelect;
	}

	public void setIsSelect(boolean mIsSelect) {
		this.mIsSelect = mIsSelect;
	}

	public ArrayList<PointF> getPorts() {
		return mPorts;
	}

	public PointF getCenter() {
		return new PointF(getParam().leftMargin + mLayout.getWidth() / 2,
				getParam().topMargin + mLayout.getHeight() / 2);
	}

}
