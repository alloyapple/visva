package vsvteam.outsource.leanappandroid.mapobjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

public class ObjBigArrow extends ObjArrow {
	private float with_arrow = 3f;
	private PointF mFist1 = new PointF();
	private PointF mFist2 = new PointF();
	private PointF mEnd1 = new PointF();
	private PointF mEnd2 = new PointF();

	private PointF mEdgeFist1 = new PointF();
	private PointF mEdgeFist2 = new PointF();
	private PointF mEdgeEnd1 = new PointF();
	private PointF mEdgeEnd2 = new PointF();

	private PointF mPathPoint1 = new PointF();
	private PointF mPathPoint2 = new PointF();

	public ObjBigArrow(Context pContext, MapObject pFirst, MapObject pEnd) {
		super(pContext, pFirst, pEnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawArrow(Canvas cv) {
		// TODO Auto-generated method stub
		Paint g = new Paint();
		g.setAntiAlias(true);
		g.setDither(true);
		g.setStyle(Paint.Style.STROKE);
		g.setColor(Color.BLACK);
		cv.drawLine(mEdgeFist1.x, mEdgeFist1.y, mPathPoint1.x, mPathPoint1.y, g);
		cv.drawLine(mEdgeFist2.x, mEdgeFist2.y, mPathPoint2.x, mPathPoint2.y, g);
		Path path = new Path();
		g.setStyle(Paint.Style.STROKE);
		g.setAntiAlias(true);
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(mPathPoint1.x, mPathPoint1.y);
		path.lineTo(mTriangle2.x, mTriangle2.y);
		path.lineTo(mEdgeEnd.x, mEdgeEnd.y);
		path.lineTo(mTriangle1.x, mTriangle1.y);
		path.lineTo(mPathPoint2.x, mPathPoint2.y);
		path.close();
		cv.drawPath(path, g);
		g.setColor(Color.WHITE);
		g.setStrokeWidth(2);
		cv.drawLine(mPathPoint1.x, mPathPoint1.y, mPathPoint2.x, mPathPoint2.y,
				g);
	}

	@Override
	public void updateParam() {
		// TODO Auto-generated method stub
		mFisrtPoint = mFisrtObject.getCenter();
		mEndPoint = mEndObject.getCenter();
		Log.i("Sin", String.valueOf(getSinSlope()));
		Log.i("Cos", String.valueOf(getCosSlope()));
		float k = (mFisrtPoint.x - mEndPoint.x) * (mFisrtPoint.y - mEndPoint.y);
		if (k >= 0) {
			mFist1.x = (float) (mFisrtPoint.x - getSinSlope() * with_arrow);
			mFist1.y = (float) (mFisrtPoint.y + getCosSlope() * with_arrow);

			mFist2.x = (float) (mFisrtPoint.x + getSinSlope() * with_arrow);
			mFist2.y = (float) (mFisrtPoint.y - getCosSlope() * with_arrow);

			mEnd1.x = (float) (mEndPoint.x - getSinSlope() * with_arrow);
			mEnd1.y = (float) (mEndPoint.y + getCosSlope() * with_arrow);

			mEnd2.x = (float) (mEndPoint.x + getSinSlope() * with_arrow);
			mEnd2.y = (float) (mEndPoint.y - getCosSlope() * with_arrow);
		} else {
			mFist1.x = (float) (mFisrtPoint.x + getSinSlope() * with_arrow);
			mFist1.y = (float) (mFisrtPoint.y + getCosSlope() * with_arrow);

			mFist2.x = (float) (mFisrtPoint.x - getSinSlope() * with_arrow);
			mFist2.y = (float) (mFisrtPoint.y - getCosSlope() * with_arrow);

			mEnd1.x = (float) (mEndPoint.x + getSinSlope() * with_arrow);
			mEnd1.y = (float) (mEndPoint.y + getCosSlope() * with_arrow);

			mEnd2.x = (float) (mEndPoint.x - getSinSlope() * with_arrow);
			mEnd2.y = (float) (mEndPoint.y - getCosSlope() * with_arrow);
		}

		getEdge(mEdgeFist1, mEdgeEnd1, mFist1, mEnd1);
		getEdge(mEdgeFist2, mEdgeEnd2, mFist2, mEnd2);
		getEdge(mEdgeFist, mEdgeEnd, mFisrtPoint, mEndPoint);
		getTriangle(0.03f);
		mPathPoint1 = getIntersect(mEdgeFist1, mEdgeEnd1, mTriangle1,
				mTriangle2);
		mPathPoint2 = getIntersect(mEdgeFist2, mEdgeEnd2, mTriangle1,
				mTriangle2);
	}

	@Override
	public void renderArrow(Canvas cv) {
		// TODO Auto-generated method stub
		updateParam();
		drawArrow(cv);
	}

}
