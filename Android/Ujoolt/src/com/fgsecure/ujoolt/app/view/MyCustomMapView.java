package com.fgsecure.ujoolt.app.view;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.fgsecure.ujoolt.app.define.ModeScreen;
import com.fgsecure.ujoolt.app.define.Step;
import com.fgsecure.ujoolt.app.json.Jolt;
import com.fgsecure.ujoolt.app.screen.ItemizedOverlays;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.utillity.ConfigUtility;
import com.fgsecure.ujoolt.app.utillity.JoltHolder;
import com.fgsecure.ujoolt.app.utillity.Utility;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class MyCustomMapView extends MapView {
	// private float xFirstTouch, yFirstTouch, xLastTouch, yLastTouch, distance;

	// Define the interface we will interact with from our Map
	public interface OnLongpressListener {
		public void onLongpress(MapView view);
	}

	/**
	 * Time in ms before the OnLongpressListener is triggered.
	 */
	static final int LONGPRESS_THRESHOLD = 500;

	public GeoPoint lastMapCenter;
	public MainScreenActivity mainScreenActivity;

	private Timer longpressTimer;
	public Timer groupTimer;
	// private MyCustomMapView.OnLongpressListener longpressListener;

	public GeoPoint geoPoint_item;
	public boolean isLongTap, isUp, isStartReGroup, isActiveRegroup, isActiveLoadJolt;
	public Handler handler = new Handler();
	public int dem = 0;
	public int demIn;
	public int count = 0;
	// public int countUpdateInstagramJolt = 0;
	public JoltHolder joltHolder;
	public boolean isZoomMode;
	// public List<Overlay> mapOverlays;
	public ArrayList<ItemizedOverlays> arrItem;
	public int distanceGroup;
	// public boolean isMoving = false;
	int counter;

	private MyCustomMapView mThis;
	private long mEventsTimeout = 250L; // Set this variable to your preferred
										// timeout
	private boolean mIsTouched = false;
	private GeoPoint mLastCenterPosition;
	private int mLastZoomLevel;
	private Timer mChangeDelayTimer = new Timer();
	private MyCustomMapView.OnChangeListener mChangeListener = null;

	public MyCustomMapView(Context context, String apiKey) {
		super(context, apiKey);
		init();
	}

	public MyCustomMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// handler.removeCallbacks(runnable);
		// handler.post(runnable);
		init();
	}

	public MyCustomMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.e("vao day", ":D 3");
		init();
	}

	private void init() {
		mThis = this;
		mLastCenterPosition = this.getMapCenter();
		mLastZoomLevel = this.getZoomLevel();
		// if (ConfigUtility.scrWidth >= 480) {
		// R1 = 140 * 11 / 15;
		// R2 = 165 * 11 / 15;
		// R3 = 185 * 11 / 15;
		// R4 = 210 * 11 / 15;
		// } else {
		// R1 = 70 * 11 / 15;
		// R2 = 85 * 11 / 15;
		// R3 = 105 * 11 / 15;
		// R4 = 130 * 11 / 15;
		// }
	}

	public void setMainActivity(MainScreenActivity mainScreenActivity) {
		this.mainScreenActivity = mainScreenActivity;
		this.joltHolder = mainScreenActivity.joltHolder;
		this.arrItem = mainScreenActivity.arrItem;
	}

	/**
	 * This method is called every time user touches the map, drags a finger on
	 * the map, or removes finger from the map.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// Set touch internal
		// mIsTouched = (event.getAction() != MotionEvent.ACTION_UP);
		handleLongpress(event);
		return super.onTouchEvent(event);
	}

	/**
	 * This method takes MotionEvents and decides whether or not a longpress has
	 * been detected. This is the meat of the OnLongpressListener.
	 * 
	 * The Timer class executes a TimerTask after a given time, and we start the
	 * timer when a finger touches the screen.
	 * 
	 * We then listen for map movements or the finger being removed from the
	 * screen. If any of these events occur before the TimerTask is executed, it
	 * gets cancelled. Else the listener is fired.
	 * 
	 * @param event
	 */

	private void handleLongpress(final MotionEvent event) {
		if (event.getPointerCount() > 1) {
			isZoomMode = true;
			if (dem < 5) {
				isActiveRegroup = false;
				dem = 0;
			}

		} else {
			if (count < 5) {
				isActiveLoadJolt = false;
				count = 0;
			}
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// xFirstTouch = event.getX();
			// yFirstTouch = event.getY();
			isUp = false;
			isLongTap = false;
			// Finger has touched screen.

			// longpressTimer = new Timer();
			// longpressTimer.schedule(new TimerTask() {
			// @Override
			// public void run() {
			// /*
			// * Fire the listener. We pass the map location of the
			// * longpress as well, in case it is needed by the caller.
			// */
			//
			// longpressListener.onLongpress(MyCustomMapView.this);
			//
			// }
			// }, LONGPRESS_THRESHOLD);
			//

			lastMapCenter = getMapCenter();
			if (mainScreenActivity.isTutorialMode && mainScreenActivity.step == Step.SIX) {
				mainScreenActivity.showStep(Step.QUIT);
				mainScreenActivity.isTutorialMode = false;
			}
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			isUp = false;

			// if (!mainScreenActivity.isShowJoltSource) {
			// mainScreenActivity.view_information.disappear(InformationView.QUIT);
			// }

			// isMoving = true;
			// GeoPoint newGeoPointCenter = getMapCenter();
			// if (!newGeoPointCenter.equals(lastMapCenter)) {
			// longpressTimer.cancel();
			// }
			// lastMapCenter = newGeoPointCenter;
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {

			// xLastTouch = event.getX();
			// yLastTouch = event.getY();
			if (isZoomMode) {
				isActiveRegroup = true;
				isZoomMode = false;
				dem = 0;
			} else {
				isActiveLoadJolt = true;
				count = 0;
			}

			if (mainScreenActivity.isBubbleDetail) {
				ItemizedOverlays item = mainScreenActivity.getCurItem();
				if (item != null) {
					item.balloonView.resetIcon();
				}
				if (!mainScreenActivity.isShowJoltSource) {
					mainScreenActivity.viewInformation.disappear(InformationView.QUIT);
				}
			}

			mainScreenActivity.isTapOnItem = false;
			if (mainScreenActivity.isSearch) {
				mainScreenActivity.endSearch();
			}

			isUp = true;
			dem = 0;

			if (longpressTimer != null)
				longpressTimer.cancel();

		}
	}

	public Point pointFromGeoPoint(GeoPoint gp) {

		Point rtnPoint = new Point();
		Projection projection = this.getProjection();
		projection.toPixels(gp, rtnPoint);
		// Get the top left GeoPoint
		GeoPoint geoPointTopLeft = (GeoPoint) projection.fromPixels(0, 0);
		Point topLeftPoint = new Point();
		// Get the top left Point (includes osmdroid offsets)
		projection.toPixels(geoPointTopLeft, topLeftPoint);
		rtnPoint.x -= topLeftPoint.x; // remove offsets
		rtnPoint.y -= topLeftPoint.y;
		// if (rtnPoint.x > this.getWidth() || rtnPoint.y > this.getHeight()
		// || rtnPoint.x < 0 || rtnPoint.y < 0) {
		// return null; // gp must be off the screen
		// }
		return rtnPoint;
	}

	// public Point getPointFromItem(ItemizedOverlays item) {
	// return pointFromGeoPoint(item.getItem(0).getPoint());
	// }

	public Point getPointFromJolt(Jolt jolt) {
		return pointFromGeoPoint(new GeoPoint(jolt.getLatitudeE6(), jolt.getLongitudeE6()));
	}

	public int getPixelDistanceBetweenTwoPoint(Point p1, Point p2) {
		int x1 = p1.x;
		int y1 = p1.y;
		int x2 = p2.x;
		int y2 = p2.y;
		int dx = x1 - x2;
		int dy = y1 - y2;
		double d = Math.sqrt(1d * dx * dx + 1d * dy * dy);
		return (int) d;
	}

	public void regroupJolts(ArrayList<Jolt> arrJolt) {
		resetJoltStatus(arrJolt);
		// arrJolt.addAll(joltHolder.arrAvailableJoltInstagram);
		int size = arrJolt.size();
		int groupID = 0;
		for (int i = 0; i < size; i++) {
			Jolt jolt = arrJolt.get(i);
			if (!jolt.isGrouped) {
				jolt.isGrouped = true;
				jolt.groupID = groupID;
				int r1;
				if (jolt.getNumberRejolt() == 0) {
					r1 = ConfigUtility.R1;
				} else if (jolt.getNumberRejolt() >= 1 && jolt.getNumberRejolt() < 10) {
					r1 = ConfigUtility.R2;
				} else if (jolt.getNumberRejolt() >= 10 && jolt.getNumberRejolt() < 20) {
					r1 = ConfigUtility.R3;
				} else {
					r1 = ConfigUtility.R4;
				}

				Point pointS = getPointFromJolt(jolt);
				if (pointS != null) {
					for (int j = i + 1; j < size; j++) {
						Jolt jolt2 = arrJolt.get(j);
						if (!jolt2.isGrouped) {
							// continue;
							// } else {
							// if (jolt2.getLatitude() == jolt.getLatitude()
							// && jolt2.getLongitude() == jolt
							// .getLongitude()) {
							// jolt2.isGrouped = true;
							// jolt2.groupID = groupID;
							// } else {
							Point pointD = getPointFromJolt(jolt2);
							if (pointD != null) {
								int d = getPixelDistanceBetweenTwoPoint(pointS, pointD);
								int r2;
								if (jolt2.getNumberRejolt() == 0) {
									r2 = ConfigUtility.R1;
								} else if (jolt2.getNumberRejolt() >= 1
										&& jolt2.getNumberRejolt() < 10) {
									r2 = ConfigUtility.R2;
								} else if (jolt2.getNumberRejolt() >= 10
										&& jolt2.getNumberRejolt() < 20) {
									r2 = ConfigUtility.R3;
								} else {
									r2 = ConfigUtility.R4;
								}
								distanceGroup = (r1 + r2) / 2;
								// Log.e("DISTANCE_GROUP", "" +
								// DISTANCE_GROUP);
								// Log.e("DISTANCE_jolt 1 2", "" + d);
								if (d <= distanceGroup) {
									jolt2.isGrouped = true;
									jolt2.groupID = groupID;
								}
								// else {
								// continue;
								// }
								// }
							}
						}
					}
					groupID++;
				}
			}
			// else {
			// continue;
			// }
		}
		// for (Jolt jolt2 : arrJolt) {
		// Log.e("Group ID", "" + jolt2.groupID);
		// }
	}

	public void resetJoltStatus(ArrayList<Jolt> arrJolt) {
		for (Jolt jolt : arrJolt) {
			jolt.isGrouped = false;
		}
		for (ItemizedOverlays itemizedOverlays : mainScreenActivity.arrItem) {
			itemizedOverlays.balloonView.disappear();
		}
		mainScreenActivity.arrItem.clear();
	}

	public ItemizedOverlays getItem(int id) {
		return mainScreenActivity.getItem(id);
	}

	public GeoPoint getGeopointFromPixel(int x, int y) {
		return getProjection().fromPixels(x, y);
	}

	public void viewFullPoint(ArrayList<Jolt> arrJolt, int mLati, int mLongi, ModeScreen modeScreen) {
		if (arrJolt != null && arrJolt.size() > 0) {
			if (arrJolt.size() > 1) {
				int minLat = arrJolt.get(0).getLatitudeE6();
				int maxLat = arrJolt.get(0).getLatitudeE6();
				int minLong = arrJolt.get(0).getLongitudeE6();
				int maxLong = arrJolt.get(0).getLongitudeE6();

				for (int i = 1; i < arrJolt.size(); i++) {
					Jolt jolt = arrJolt.get(i);
					int la = jolt.getLatitudeE6();
					int lo = jolt.getLongitudeE6();
					if (minLat > la) {
						minLat = la;
					}
					if (maxLat < la) {
						maxLat = la;
					}
					if (minLong > lo) {
						minLong = lo;
					}
					if (maxLong < lo) {
						maxLong = lo;
					}
				}

				switch (modeScreen) {
				case JOLTS:

					int zoomLat = maxLat - minLat;
					int zoomLong = maxLong - minLong;

					if (minLat < mLati && mLati < maxLat) {
						zoomLat = Math.max(mLati - minLat, maxLat - mLati) * 2;
					} else if (minLat >= mLati) {
						zoomLat = (maxLat - mLati) * 2;
					} else if (maxLat <= mLati) {
						zoomLat = (mLati - minLat) * 2;
					}

					if (minLong < mLongi && mLongi < maxLong) {
						zoomLong = Math.max(mLongi - minLong, maxLong - mLongi) * 2;
					} else if (minLong >= mLongi) {
						zoomLong = (maxLong - mLongi) * 2;
					} else if (maxLong <= mLongi) {
						zoomLong = (mLongi - minLong) * 2;
					}

					mainScreenActivity.mapController.zoomToSpan(zoomLat, zoomLong);
					mainScreenActivity.mapController.animateTo(new GeoPoint(mLati, mLongi));
					break;

				case SEARCH:
					mainScreenActivity.mapController.zoomToSpan(maxLat - minLat, maxLong - minLong);

					int zoom = getMaxZoomLevel();
					if (getZoomLevel() == zoom)
						mainScreenActivity.mapController.setZoom(zoom - 2);

					mainScreenActivity.mapController.animateTo(new GeoPoint((maxLat + minLat) / 2,
							(maxLong + minLong) / 2));
					break;
				}

			} else {
				mainScreenActivity.mapController.animateTo(new GeoPoint(mLati, mLongi));
				mainScreenActivity.mapController.setZoom(16);
			}
		}
	}

	public GeoPoint getCenterPoint(ArrayList<Jolt> arrJolt) {
		int minLat = arrJolt.get(0).getLatitudeE6();
		int maxLat = arrJolt.get(0).getLatitudeE6();
		int minLong = arrJolt.get(0).getLongitudeE6();
		int maxLong = arrJolt.get(0).getLongitudeE6();
		int size = arrJolt.size();
		for (int i = 1; i < size; i++) {
			Jolt jolt = arrJolt.get(i);
			int la = jolt.getLatitudeE6();
			int lo = jolt.getLongitudeE6();
			if (minLat > la) {
				minLat = la;
			}
			if (maxLat < la) {
				maxLat = la;
			}
			if (minLong > lo) {
				minLong = lo;
			}
			if (maxLong < lo) {
				maxLong = lo;
			}
		}

		return new GeoPoint((maxLat + minLat) / 2, (maxLong + minLong) / 2);
	}

	public int getRadius() {
		Projection projection = this.getProjection();
		// Get the top left GeoPoint
		GeoPoint geoPointLeft = (GeoPoint) projection.fromPixels(0, ConfigUtility.scrHeight / 2);
		return (int) Utility.getDistance(mainScreenActivity.curLati, mainScreenActivity.curLongi,
				geoPointLeft.getLatitudeE6(), geoPointLeft.getLongitudeE6());
	}

	public boolean isMaxLevelZoomin() {
		if (getZoomLevel() == getMaxZoomLevel()) {
			return true;
		} else {
			return false;
		}
	}

	// ------------------------------------------------------------------------
	// LISTENER DEFINITIONS
	// ------------------------------------------------------------------------

	// Change listener
	public interface OnChangeListener {
		public void onChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter, int newZoom,
				int oldZoom);
	}

	// ------------------------------------------------------------------------
	// MEMBERS
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// GETTERS / SETTERS
	// ------------------------------------------------------------------------

	public void setOnChangeListener(MyCustomMapView.OnChangeListener l) {
		mChangeListener = l;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();

		// Check for change
		if (isSpanChange() || isZoomChange()) {
			// If computeScroll called before timer counts down we should drop
			// it and
			// start counter over again
			resetMapChangeTimer();
		}
	}

	// ------------------------------------------------------------------------
	// TIMER RESETS
	// ------------------------------------------------------------------------

	private void resetMapChangeTimer() {
		mChangeDelayTimer.cancel();
		mChangeDelayTimer = new Timer();
		mChangeDelayTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (mChangeListener != null)
					mChangeListener.onChange(mThis, getMapCenter(), mLastCenterPosition,
							getZoomLevel(), mLastZoomLevel);
				mLastCenterPosition = getMapCenter();
				mLastZoomLevel = getZoomLevel();
			}
		}, mEventsTimeout);
	}

	// ------------------------------------------------------------------------
	// CHANGE FUNCTIONS
	// ------------------------------------------------------------------------

	private boolean isSpanChange() {
		return !mIsTouched && !getMapCenter().equals(mLastCenterPosition);
	}

	private boolean isZoomChange() {
		return (getZoomLevel() != mLastZoomLevel);
	}

}
