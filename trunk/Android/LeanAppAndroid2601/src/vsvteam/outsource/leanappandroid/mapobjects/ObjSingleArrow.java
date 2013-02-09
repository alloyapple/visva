package vsvteam.outsource.leanappandroid.mapobjects;

import vsvteam.outsource.leanappandroid.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ObjSingleArrow extends ObjArrow {
	private String mTextLabel;

	public ObjSingleArrow(Context pContext, MapObject pFirst, MapObject pEnd,
			String pTextLabel) {
		super(pContext, pFirst, pEnd);
		// TODO Auto-generated constructor stub
		mTextLabel = pTextLabel;
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.setLayoutParams(new RelativeLayout.LayoutParams(120, 40));
		getParam().setMargins(
				(int) ((mEdgeFist.x + mEdgeEnd.x) / 2 - getParam().width / 2),
				(int) ((mEdgeFist.y + mEdgeEnd.y) / 2 - getParam().height / 2),
				getParam().rightMargin, getParam().bottomMargin);
		mLayout.requestLayout();
		mLayout.setBackgroundResource(R.drawable.obj_box_takt_time);
		initLabelView();
	}

	public void initLabelView() {
		TextView mTextViewLabel = new TextView(mContext);
		LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		mTextViewLabel.setLayoutParams(param1);
		mTextViewLabel.setGravity(Gravity.CENTER | Gravity.CENTER);
		mTextViewLabel.setText(mTextLabel);
		mTextViewLabel.setTextColor(Color.BLACK);
		mLayout.addView(mTextViewLabel);
	}

	@Override
	public void drawArrow(Canvas cv) {
		// TODO Auto-generated method stub
		Paint g = new Paint();
		g.setStyle(Paint.Style.STROKE);
		g.setColor(Color.BLACK);
		g.setAntiAlias(true);
		g.setDither(true);
		cv.drawLine(mEdgeFist.x, mEdgeFist.y, mEdgeEnd.x, mEdgeEnd.y, g);
		Path path = new Path();
		g.setStyle(Paint.Style.FILL_AND_STROKE);
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(mEdgeEnd.x, mEdgeEnd.y);
		path.lineTo(mTriangle1.x, mTriangle1.y);
		path.lineTo(mTriangle2.x, mTriangle2.y);
		path.close();
		cv.drawPath(path, g);
	}

	@Override
	public void updateParam() {
		// TODO Auto-generated method stub
		mFisrtPoint = mFisrtObject.getCenter();
		getParam().setMargins(
				(int) ((mEdgeFist.x + mEdgeEnd.x) / 2 - getParam().width / 2),
				(int) ((mEdgeFist.y + mEdgeEnd.y) / 2 - getParam().height / 2),
				getParam().rightMargin, getParam().bottomMargin);
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mLayout.requestLayout();
			}
		});

		mEndPoint = mEndObject.getCenter();
		getEdge(mEdgeFist, mEdgeEnd, mFisrtPoint, mEndPoint);
		getTriangle(0.02f);
	}

	@Override
	public void renderArrow(Canvas cv) {
		// TODO Auto-generated method stub
		updateParam();
		drawArrow(cv);
	}

}
