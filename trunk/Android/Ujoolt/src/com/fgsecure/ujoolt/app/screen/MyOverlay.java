package com.fgsecure.ujoolt.app.screen;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyOverlay extends Overlay {

	private static GeoPoint lastLatLon = new GeoPoint(0, 0);
	private static GeoPoint currLatLon;

	// Event listener to listen for map finished moving events
	private OnMapMoveListener eventListener = null;

	public static boolean isMapMoving = false;
	MainScreenActivity mainScreenActivity;

	public MyOverlay(OnMapMoveListener eventLis, MainScreenActivity main) {
		// Set event listener
		eventListener = eventLis;
		mainScreenActivity = main;
	}

	// ------------------------------------------------------------------------
	// LISTENER DEFINITIONS
	// ------------------------------------------------------------------------

	// Tap listener
	public interface OnTapListener {
		public void onTap(MapView v, GeoPoint geoPoint);
	}

	// ------------------------------------------------------------------------
	// GETTERS / SETTERS
	// ------------------------------------------------------------------------

	// Setters
	public void setOnTapListener(OnTapListener listener) {
		mTapListener = listener;
	}

	// ------------------------------------------------------------------------
	// MEMBERS
	// ------------------------------------------------------------------------

	private OnTapListener mTapListener;

	// ------------------------------------------------------------------------
	// EVENT HANDLERS
	// ------------------------------------------------------------------------

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		mTapListener.onTap(mapView, geoPoint);
		return super.onTap(geoPoint, mapView);
	}

	private int lastZoom;
	private int currentZoom;

	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		// TODO Auto-generated method stub

		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			lastZoom = mapView.getZoomLevel();
		}

		if (e.getAction() == MotionEvent.ACTION_UP) {
			isMapMoving = true;
			currentZoom = mapView.getZoomLevel();
		}

		return super.onTouchEvent(e, mapView);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (!shadow) {
			if (isMapMoving) {
				currLatLon = mapView.getProjection().fromPixels(0, 0);
				if (currLatLon.equals(lastLatLon)) {
					isMapMoving = false;
					eventListener.mapMovingFinishedEvent();

					/*
					 * Refresh map and reGroup all jolts
					 */
					mainScreenActivity.delayAgain = true;
					mainScreenActivity.newDelayTimeFromNow = 400;

					if (mainScreenActivity.isPressHeaderBubbleDetail == false)
						refreshMap();

					/*
					 * Count number jolt on screen
					 */
					mainScreenActivity.numberJoltOnScreen = mainScreenActivity
							.countJoltNumberInsideScreen();

					// mainScreenActivity.handler_countJolt
					// .post(mainScreenActivity.runnable_countJolt);

					/*
					 * 
					 */
					GeoPoint newCenter = mainScreenActivity.mapView
							.getMapCenter();
					if (mainScreenActivity.isPressHeaderBubbleDetail == false)
						mainScreenActivity.updateItem_with_radius(newCenter);

				} else {
					lastLatLon = currLatLon;
				}
			}
		}
	}

	public static boolean isChangZoom = false;
	private int countChangZoom = 0;

	public void refreshMap() {

		int changeZoom = lastZoom - currentZoom;

		if (changeZoom != 0) {
			countChangZoom++;
			if (countChangZoom % 7 == 0)
				isChangZoom = true;
			/*
			 * Regroup for Jolts
			 */

			Log.i("NUMBER jolt on Screen", " = "
					+ mainScreenActivity.numberJoltOnScreen);

			if (mainScreenActivity.numberJoltOnScreen > 0) {

				mainScreenActivity.handgroupFilter.post(mainScreenActivity.runGroupFilter);
			}
		}
	}

	public interface OnMapMoveListener {
		public void mapMovingFinishedEvent();
	}
}