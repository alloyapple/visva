package vsvteam.outsource.leanappandroid.mapobjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

public class Line {
	public PointF mFisrtPoint = new PointF();
	public MapObject mFisrtObjetc;
	public MapObject mEndObjetc;
	public PointF mEndPoint = new PointF();

	public Line(Context pContext, float pX, float pY) {
		mEndPoint.x = mFisrtPoint.x = pX;
		mEndPoint.y = mFisrtPoint.y = pY;
	}

	public void drawObjects(Canvas cv) {
		// TODO Auto-generated method stub
		Paint g = new Paint();
		g.setColor(Color.WHITE);
		cv.drawLine(mFisrtPoint.x, mFisrtPoint.y, mEndPoint.x, mEndPoint.y, g);
	}

	public void setEndPoint(MotionEvent e) {
		mEndPoint.x = e.getX();
		mEndPoint.y = e.getY();
	}

	public void setFirstObject(MapObject pObject) {
		mFisrtObjetc = pObject;
		mFisrtObjetc.getPorts().add(mFisrtPoint);
	}
	public void setEndObject(MapObject pObject) {
		mEndObjetc = pObject;
		mEndObjetc.getPorts().add(mEndPoint);
	}

}
