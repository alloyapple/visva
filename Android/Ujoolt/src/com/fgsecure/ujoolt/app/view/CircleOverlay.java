package com.fgsecure.ujoolt.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class CircleOverlay extends Overlay {

	Context context;
	double mLat;
	double mLon;
	double distance;
	float circleRadius;

	public CircleOverlay(Context context, double lat, double lon, double distance) {
		this.context = context;
		this.mLat = lat;
		this.mLon = lon;
		this.distance = distance;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		super.draw(canvas, mapView, shadow);

		Projection projection = mapView.getProjection();
		Point pt = new Point();
		GeoPoint geo = new GeoPoint((int) mLat, (int) mLon);
		projection.toPixels(geo, pt);
		circleRadius = projection.metersToEquatorPixels((int) distance);

		Paint innerCirclePaint;
		innerCirclePaint = new Paint();
		innerCirclePaint.setARGB(255, 0, 0, 0);
		innerCirclePaint.setAntiAlias(true);
		innerCirclePaint.setStyle(Paint.Style.STROKE);

		canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius, innerCirclePaint);
	}
}