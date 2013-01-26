package vsvteam.outsource.leanappandroid.mapobjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class ObjSingleArrow extends ObjArrow {

	public ObjSingleArrow(Context pContext, MapObject pFirst, MapObject pEnd) {
		super(pContext, pFirst, pEnd);
		// TODO Auto-generated constructor stub
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
