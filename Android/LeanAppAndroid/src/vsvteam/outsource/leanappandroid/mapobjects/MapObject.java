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
	protected boolean mIsSelect = false;
	protected ArrayList<PointF> mPorts=new ArrayList<PointF>();

	public MapObject(Context pContext, float pX, float pY) {
		mLayout = new LinearLayout(pContext);
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));
		getParam().setMargins((int) pX, (int) pY, getParam().rightMargin,
				getParam().bottomMargin);
		mLayout.requestLayout();
	}

	public RelativeLayout.LayoutParams getParam() {
		return ((RelativeLayout.LayoutParams) mLayout.getLayoutParams());
	}

	public void move(float pX, float pY) {
		for(int i=0;i<mPorts.size();i++){
			mPorts.get(i).x+=(int)pX-getParam().leftMargin;
			mPorts.get(i).y+=(int)pY-getParam().topMargin;
		}
		getParam().setMargins((int) pX, (int) pY, getParam().rightMargin,
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
	public ArrayList<PointF> getPorts(){
		return mPorts;
	}
}
