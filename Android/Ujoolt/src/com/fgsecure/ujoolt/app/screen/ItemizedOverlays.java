package com.fgsecure.ujoolt.app.screen;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.info.WebServiceConfig;
import com.fgsecure.ujoolt.app.json.Jolt;
import com.fgsecure.ujoolt.app.network.AsyncHttpGet;
import com.fgsecure.ujoolt.app.network.AsyncHttpResponseListener;
import com.fgsecure.ujoolt.app.utillity.ConfigUtility;
import com.fgsecure.ujoolt.app.utillity.JoltHolder;
import com.fgsecure.ujoolt.app.utillity.Language;
import com.fgsecure.ujoolt.app.utillity.LoadImageTask;
import com.fgsecure.ujoolt.app.view.BallooOverlayView;
import com.fgsecure.ujoolt.app.view.InformationView;
import com.fgsecure.ujoolt.app.view.MyCustomMapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class ItemizedOverlays {
	public MainScreenActivity mainScreenActivity;
	public ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public int index = 1;
	private MyCustomMapView mapView;
	public ArrayList<Jolt> arrayJolt = new ArrayList<Jolt>();

	public int currentPositionJolt;
	public int groupID;
	public int positionOfItemizedOverlay;

	public static Handler handlerCountLifejolt = new Handler();

	public BallooOverlayView<OverlayItem> balloonView;
	public BallooOverlayView<OverlayItem> ballooOverlayCircle;
	private GeoPoint geoPoint;

	private static boolean flag = true;
	private static boolean flagPreNear = true;
	private static boolean flagNextNear = true;

	public ItemizedOverlays(MainScreenActivity mainScreenActivity, MyCustomMapView mapView,
			Jolt jolt, int positionOfItemizedOverlay, byte type) {
		this.mainScreenActivity = mainScreenActivity;
		this.mapView = mapView;
		this.positionOfItemizedOverlay = positionOfItemizedOverlay;
		geoPoint = new GeoPoint(jolt.getLatitudeE6(), jolt.getLongitudeE6());
		addJolt(jolt);
		createAndDisplayBalloonOverlay(jolt, type);
	}

	public Jolt CurrentJolt;

	public void setInformation() {

		mainScreenActivity.linearLayoutJoltDetail.setVisibility(View.VISIBLE);
		mainScreenActivity.joltHolder.flagJolt = mainScreenActivity.curItemizedOverlays.currentPositionJolt;

		if (arrayJolt.size() > 0) {
			Jolt currentJolt = arrayJolt.get(currentPositionJolt);
			CurrentJolt = currentJolt;
			currentJolt.showInfo();

			mainScreenActivity.FlagJoltID = currentJolt.getId() + currentJolt.getFacebookId()
					+ currentJolt.getInstagramId();

			long curTime = ConfigUtility.getCurTimeStamp();
			long startTime = currentJolt.getDate();
			long lifetime = (long) (currentJolt.getLifeTime() * 3600);

			if (!currentJolt.isVideoJolt()) {
				mainScreenActivity.imgPlay.setVisibility(View.GONE);
			} else {
				mainScreenActivity.imgPlay.setVisibility(View.VISIBLE);
			}

			if (!mainScreenActivity.isShowJoltSource) {
				if (!mainScreenActivity.viewLogin.isLogin && !currentJolt.isRejolted) {
					mainScreenActivity.btnRejolt.setVisibility(View.VISIBLE);
					if (currentJolt.isMyJolt()) {
						mainScreenActivity.viewInformation.isEnableDrag = false;
						mainScreenActivity.viewInformation.isEnableDelte = true;
						mainScreenActivity.btnRejolt.setBackgroundResource(R.drawable.delete);

					} else {
						mainScreenActivity.viewInformation.isEnableDrag = true;
						mainScreenActivity.viewInformation.isEnableDelte = false;
						mainScreenActivity.btnRejolt.setBackgroundResource(R.drawable.icon_rejolt);

					}
				} else {
					mainScreenActivity.viewInformation.isEnableDrag = false;
					mainScreenActivity.viewInformation.isEnableDelte = false;
				}

				if (currentJolt.getNumberRejolt() <= 10) {
					mainScreenActivity.lblNumberRejolt.setText("000"
							+ currentJolt.getNumberRejolt());
				} else if (currentJolt.getNumberRejolt() < 100) {
					mainScreenActivity.lblNumberRejolt
							.setText("00" + currentJolt.getNumberRejolt());
				} else if (currentJolt.getNumberRejolt() < 100) {
					mainScreenActivity.lblNumberRejolt.setText("0" + currentJolt.getNumberRejolt());
				} else {
					mainScreenActivity.lblNumberRejolt.setText(currentJolt.getNumberRejolt());
				}

				if (flag) {
					flag = false;
				} else if (flagPreNear) {
					flagPreNear = false;
				} else if (flagNextNear) {
					flagNextNear = false;
				}

				mainScreenActivity.lblText.setText(currentJolt.getText());

				/*
				 * count life time of jolt
				 */
				handlerCountLifejolt.removeCallbacksAndMessages(null);
				handlerCountLifejolt.post(countTime);

				if (index == 0) {
					index = 1;
				}

				String text = currentJolt.getNick();

				String indexText = index + "/" + arrayJolt.size();
				if (arrayJolt.size() > 1)
					mainScreenActivity.lblIndexJolt.setText(indexText);
				else
					mainScreenActivity.lblIndexJolt.setText("");
				mainScreenActivity.lblNick.setText(text);

				if (currentJolt.getPhotoBitmap() != null) {
					mainScreenActivity.imgAvatar.setImageBitmap(currentJolt.getPhotoBitmap());
					mainScreenActivity.progressBar.setVisibility(View.INVISIBLE);

				} else if (mainScreenActivity.joltHolder.isJoltOnlyText) {
					mainScreenActivity.progressBar.setVisibility(View.INVISIBLE);
					mainScreenActivity.imgAvatar.setImageResource(R.drawable.default_avatar);
					mainScreenActivity.joltHolder.isJoltOnlyText = false;
				} else {
					new LoadImageTask(mainScreenActivity, currentJolt).execute();
				}

			}
			/*
			 * Show icon Favorite for Ujoolt jolts
			 */

			mainScreenActivity.setIconFavourite(currentJolt.isLike());

			if ((currentJolt.isFacebook() || currentJolt.isInstagram())
					&& (currentJolt.getId().equalsIgnoreCase(""))) {
				mainScreenActivity.imgClockAndSkull.setImageResource(R.drawable.ic_skull);
				mainScreenActivity.lblJoltAge.setVisibility(View.VISIBLE);

				if (mainScreenActivity.viewInformation.countDownControl != null)
					mainScreenActivity.viewInformation.countDownControl.stop();

				mainScreenActivity.viewInformation.setCountDownTimer(mainScreenActivity.lblJoltAge,
						lifetime, startTime, curTime, true);

			} else {
				mainScreenActivity.lblJoltAge.setVisibility(View.INVISIBLE);
				mainScreenActivity.imgClockAndSkull.setImageResource(R.drawable.ic_amis);
			}

			if (currentJolt.isMyJolt()) {
				mainScreenActivity.viewInformation.isEnableDelte = true;
				mainScreenActivity.btnRejolt.setBackgroundResource(R.drawable.delete);

			} else {
				mainScreenActivity.btnRejolt.setBackgroundResource(R.drawable.icon_rejolt);
				mainScreenActivity.viewInformation.isEnableDrag = true;
			}

			double d = currentJolt.getRadius();
			mainScreenActivity.setImageSourceDistance((int) d);

			mainScreenActivity.viewInformation.isEnableDrag = true;
			mainScreenActivity.onClickImgCloseFilter();
		}
	}

	public Runnable countTime = new Runnable() {

		@Override
		public void run() {
			long currentTime = System.currentTimeMillis() / 1000;
			Jolt jolt = getCurrentJolt();

			// long startTime = currentJolt.getDate();
			long startTime = jolt.getDate();

			long lifeJolt = currentTime - startTime;

			int hour = (int) lifeJolt / 3600;
			int minute = (int) (lifeJolt - hour * 3600) / 60;
			int second = (int) (lifeJolt - hour * 3600 - minute * 60);

			String time = Language.getTimeString(mainScreenActivity.language, hour, minute, second);

			mainScreenActivity.lblLifeJolt.setText(time);
			handlerCountLifejolt.postDelayed(countTime, 1000);
		}
	};

	public void addJolt(Jolt jolt) {
		if (arrayJolt == null) {
			arrayJolt = new ArrayList<Jolt>();
		}
		int size = arrayJolt.size();
		if (size == 0) {
			arrayJolt.add(jolt);
		} else if (size > 0) {
			int po = 0;
			for (int i = 0; i < size; i++) {
				Jolt j1 = arrayJolt.get(i);
				if (j1.getDate() <= jolt.getDate()) {
					po = i;
					break;
				}
			}
			arrayJolt.add(po, jolt);
			currentPositionJolt = po;
		}
	}

	public void nextJolt() {
		int size = arrayJolt.size();
		// if (mapView.isMaxLevelZoomin()) {
		if (index < size && size > 1) {
			index++;
			if (currentPositionJolt == size - 1) {
				currentPositionJolt = 0;
			} else {
				currentPositionJolt++;
			}

			balloonView.setIcon();
			balloonView.setVisibility(View.VISIBLE);
			// balloonView.zoomIcon();

			setInformation();
		} else {
			index = 1;
			nextNearestJolt();
		}
		// } else {
		// nextNearestJolt();
		// }
		// Log.e(TAG, "Instagram id: " +
		// arrayJolt.get(currentPositionJolt).getInstagramId());
	}

	public void preJolt() {
		int size = arrayJolt.size();
		// if (mapView.isMaxLevelZoomin()) {
		if (index > 1 && arrayJolt.size() > 1) {
			index--;
			if (currentPositionJolt == 0) {
				currentPositionJolt = size - 1;
			} else {
				currentPositionJolt--;
			}

			balloonView.setIcon();

			balloonView.setVisibility(View.VISIBLE);
			// balloonView.zoomIcon();

			setInformation();
		} else {
			index = 1;
			preNearestJolt();
		}
		// } else {
		// preNearestJolt();
		// }
		// Log.e(TAG, "Instagram id: " +
		// arrayJolt.get(currentPositionJolt).getInstagramId());
	}

	public void nextNearestJolt() {
		flagNextNear = true;
		int s = mainScreenActivity.arrItem.size();
		int mLati = mainScreenActivity.mLatitudeE6;
		int mLong = mainScreenActivity.mLongitudeE6;
		if (currentPositionJolt >= 0 && currentPositionJolt < arrayJolt.size()) {
			long curDistance = arrayJolt.get(currentPositionJolt).getDistanceToLocation(mLati,
					mLong);

			ItemizedOverlays minItem = null;
			Jolt jolt = null;
			long min = 0;
			int start = 0;
			int dem = 0;
			for (int i = 0; i < s; i++) {
				minItem = mainScreenActivity.getItem(i);

				if (minItem != null) {
					jolt = minItem.arrayJolt.get(0);

					long distance = jolt.getDistanceToLocation(mLati, mLong);
					if (distance > curDistance
							&& positionOfItemizedOverlay != minItem.positionOfItemizedOverlay) {
						dem++;
						min = distance;
						start = i;
						break;
					}
				}
			}

			if (dem > 0) {
				for (int i = start + 1; i < s; i++) {
					ItemizedOverlays itemizedOverlays = mainScreenActivity.getItem(i);
					if (itemizedOverlays != null) {
						jolt = itemizedOverlays.arrayJolt.get(0);
						long distance = jolt.getDistanceToLocation(mLati, mLong);
						if (distance > curDistance
								&& positionOfItemizedOverlay != itemizedOverlays.positionOfItemizedOverlay) {
							if (distance < min) {
								min = distance;
								minItem = itemizedOverlays;
							}
						}
					}
				}
				balloonView.resetIcon();
				mainScreenActivity.curItemizedOverlays = minItem;
				mainScreenActivity.curTapItem = minItem.positionOfItemizedOverlay;
				mainScreenActivity.curItemizedOverlays.index = 1;
				balloonView.setVisibility(View.VISIBLE);
				mainScreenActivity.curItemizedOverlays.balloonView.zoomIcon();
				mainScreenActivity.curItemizedOverlays.centerMap();
				mainScreenActivity.curItemizedOverlays.setInformation();
			}
		}
	}

	public void preNearestJolt() {
		flagPreNear = true;
		int s = mainScreenActivity.arrItem.size();
		int mLati = mainScreenActivity.mLatitudeE6;
		int mLong = mainScreenActivity.mLongitudeE6;
		if (currentPositionJolt >= 0 && currentPositionJolt < arrayJolt.size()) {
			long curDistance = arrayJolt.get(currentPositionJolt).getDistanceToLocation(mLati,
					mLong);
			long max = curDistance;

			ItemizedOverlays maxItem = null;
			Jolt jolt = null;

			int start = 0;
			int dem = 0;
			for (int i = 0; i < s; i++) {
				maxItem = mainScreenActivity.getItem(i);

				if (maxItem != null) {
					jolt = maxItem.arrayJolt.get(0);

					long distance = jolt.getDistanceToLocation(mLati, mLong);
					if (distance < curDistance
							&& positionOfItemizedOverlay != maxItem.positionOfItemizedOverlay) {
						start = i;
						dem++;
						max = distance;
						break;
					}
				}
			}
			if (dem > 0) {
				for (int i = start + 1; i < s; i++) {
					ItemizedOverlays itemizedOverlays = mainScreenActivity.getItem(i);
					if (itemizedOverlays != null) {
						jolt = itemizedOverlays.arrayJolt.get(0);
						long distance = jolt.getDistanceToLocation(mLati, mLong);
						if (distance < curDistance
								&& positionOfItemizedOverlay != itemizedOverlays.positionOfItemizedOverlay) {
							if (distance > max) {
								max = distance;
								maxItem = itemizedOverlays;
							}
						}
					}
				}
				balloonView.resetIcon();
				mainScreenActivity.curItemizedOverlays = maxItem;
				mainScreenActivity.curTapItem = maxItem.positionOfItemizedOverlay;
				mainScreenActivity.curItemizedOverlays.index = 1;
				balloonView.setVisibility(View.VISIBLE);
				mainScreenActivity.curItemizedOverlays.balloonView.zoomIcon();
				mainScreenActivity.curItemizedOverlays.centerMap();
				mainScreenActivity.curItemizedOverlays.setInformation();
			}
		}
	}

	public int getTotalJolt() {
		int total = 0;
		int length = mainScreenActivity.arrItem.size();
		ItemizedOverlays item = null;
		for (int i = 0; i < length; i++) {
			item = mainScreenActivity.getItem(i);
			total = total + item.arrayJolt.size();
		}

		return total;
	}

	public Jolt getCurrentJolt() {
		if (arrayJolt != null && currentPositionJolt >= 0
				&& currentPositionJolt <= arrayJolt.size()) {
			if (currentPositionJolt < arrayJolt.size() || currentPositionJolt == 0) {
				return arrayJolt.get(currentPositionJolt);
			} else {
				return arrayJolt.get(--currentPositionJolt);
			}
		} else {
			return null;
		}
	}

	public void centerMap() {
		if (arrayJolt != null && arrayJolt.size() > 0) {
			// Jolt jolt = arrJolt.get(0);
			// GeoPoint geoPoint = new GeoPoint(jolt.getLatitude() + 150,
			// jolt.getLongitude());
			mainScreenActivity.mapController.animateTo(geoPoint);
		}
	}

	public BallooOverlayView<OverlayItem> createBalloonOverlayView(int type, double radius, byte t) {
		return new BallooOverlayView<OverlayItem>(this, 0, type, radius, t);
	}

	private boolean createAndDisplayBalloonOverlay(Jolt jolt, byte type) {
		boolean isRecycled = false;
		if (balloonView == null) {
			balloonView = createBalloonOverlayView(0, jolt.getRadius(), type);
			isRecycled = false;
		} else {
			isRecycled = true;
		}
		GeoPoint point = new GeoPoint(jolt.getLatitudeE6(), jolt.getLongitudeE6());
		MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, point, MapView.LayoutParams.CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;

		balloonView.setVisibility(View.VISIBLE);

		if (isRecycled) {
			balloonView.setLayoutParams(params);
		} else {
			mapView.addView(balloonView, params);
		}

		return isRecycled;
	}

	public boolean createAndDisplayBalloonOverlayCircle(Jolt jolt) {
		boolean isRecycled = false;
		if (ballooOverlayCircle == null) {
			ballooOverlayCircle = createBalloonOverlayView(1, jolt.getRadius(), (byte) 1);
			isRecycled = false;
		} else {
			isRecycled = true;
		}
		GeoPoint point = new GeoPoint(jolt.getLatitudeE6(), jolt.getLongitudeE6());
		MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, point, MapView.LayoutParams.CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;

		ballooOverlayCircle.setVisibility(View.VISIBLE);

		if (isRecycled) {
			ballooOverlayCircle.setLayoutParams(params);
		} else {

			mapView.addView(ballooOverlayCircle, params);
		}

		return isRecycled;
	}

	public void setPositionBalloonView(GeoPoint geopoint) {
		MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, geopoint, MapView.LayoutParams.CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		if (balloonView != null) {
			balloonView.setLayoutParams(params);
		}
	}

	/*
	 * set Icon for Item
	 */
	public void setItemOverlay() {
		if (arrayJolt != null && arrayJolt.size() > 0) {
			Jolt jolt = arrayJolt.get(0);

			int po = 0;

			Jolt joltAvailable = jolt;
			int size = arrayJolt.size();

			boolean isExistFb = false;
			boolean isExistRejolt = false;

			/*
			 * check jolt fb in arr
			 */
			for (Jolt jolt2 : arrayJolt) {
				if (jolt2.isFacebook()) {
					isExistFb = true;
					break;
				}
			}

			/*
			 * check rejolt in array
			 */
			for (Jolt jolt2 : arrayJolt) {
				if (jolt2.getNumberRejolt() > 0) {
					isExistRejolt = true;
					break;
				}
			}

			/*
			 * Check number rejolt max If rejolt of the jolt in array = true
			 * then choose item have number rejolt max
			 */
			if (isExistFb) {
				for (int i = 0; i < size; i++) {
					Jolt jolt2 = arrayJolt.get(i);
					if (jolt2.isFacebook()) {
						po = i;
						joltAvailable = jolt2;
						break;
					}
				}

			} else if (isExistRejolt) {
				int max = jolt.getNumberRejolt();
				for (int i = 1; i < size; i++) {
					Jolt jolt2 = arrayJolt.get(i);
					int nb = jolt2.getNumberRejolt();
					if (max < nb) {
						max = nb;
						po = i;
						joltAvailable = jolt2;
					}
				}
			} else {
				double max = jolt.getDate();
				for (int i = 1; i < size; i++) {
					Jolt jolt2 = arrayJolt.get(i);
					double nb = jolt2.getDate();
					if (max < nb) {
						max = nb;
						po = i;
						joltAvailable = jolt2;
					}
				}
			}

			// GeoPoint geoPoint = new GeoPoint(joltAvailable.getLatitude(),
			// joltAvailable.getLongitude());

			geoPoint = new GeoPoint(joltAvailable.getLatitudeE6(), joltAvailable.getLongitudeE6());

			g = geoPoint;

			setPositionBalloonView(geoPoint);
			currentPositionJolt = po;

			balloonView.setIcon();
		}
	}

	public GeoPoint g;

	public void delete() {
		this.balloonView.disappear();

		deleteJolt(arrayJolt, getCurrentJolt().getId(), getCurrentJolt().getInstagramId(),
				getCurrentJolt().getFacebookId(), mainScreenActivity.myLoginUserId,
				mainScreenActivity.joltHolder);
	}

	/**
	 * 
	 * @param id
	 */
	public void deleteJolt(final ArrayList<Jolt> arrayList, final String id,
			final String instagramId, final String facebookId, String loginUserId,
			final JoltHolder joHolder) {

		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void after(int statusCode, HttpResponse response) {
						try {
							String json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

							if (json.contains("rejolt deleted")) {
								Toast.makeText(mainScreenActivity, Language.notifyDeleteSuccessful,
										Toast.LENGTH_LONG).show();

								for (int i = arrayList.size() - 1; i >= 0; i--) {
									Jolt jolt = arrayList.get(i);
									if (jolt != null) {
										if (jolt.getId().equalsIgnoreCase(id)
												|| (jolt.isInstagram()
														&& jolt.getInstagramId().equalsIgnoreCase(
																instagramId) && jolt
														.getFacebookId().equalsIgnoreCase(
																facebookId))) {
											arrayList.remove(i);
											break;
										}
									}
								}

								for (int i = joHolder.arrAvailableJolt.size() - 1; i >= 0; i--) {
									Jolt jolt = joHolder.arrAvailableJolt.get(i);
									if (jolt != null) {
										if (jolt.getId().equalsIgnoreCase(id)) {
											joHolder.arrAvailableJolt.remove(i);
											break;
										}
									}
								}

								for (int i = joHolder.arrAvailableJoltInstagram.size() - 1; i >= 0; i--) {
									Jolt jolt = joHolder.arrAvailableJoltInstagram.get(i);
									if (jolt != null) {
										if (jolt.getInstagramId().equalsIgnoreCase(instagramId)) {
											joHolder.arrAvailableJoltInstagram.remove(i);
											break;
										}
									}
								}

								for (int i = joHolder.arrAvailableJoltFacebook.size() - 1; i >= 0; i--) {
									Jolt jolt = joHolder.arrAvailableJoltFacebook.get(i);
									if (jolt != null) {
										if (jolt.getFacebookId().equalsIgnoreCase(facebookId)) {
											joHolder.arrAvailableJoltFacebook.remove(i);
											break;
										}
									}
								}
								showNextJoltWhenDelete();
							}

						} catch (ParseException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void before() {
					}
				});
		asyncHttpGet.execute(WebServiceConfig.URL_DELETE_JOLT + "?id="
				+ mainScreenActivity.joltHolder.encode(id) + "&loginUserid=" + loginUserId);
	}

	public void showNextJoltWhenDelete() {
		if (arrayJolt.size() <= 0) {
			mainScreenActivity.viewInformation.disappear(InformationView.QUIT);
			arrayJolt.clear();

		} else {
			if (currentPositionJolt == arrayJolt.size()) {
				currentPositionJolt--;
			}
			setInformation();
		}
		mainScreenActivity.groupArrayJoltFilter();
	}

	public boolean checkExistJolt(Jolt jolt) {
		if (jolt.isInstagram()) {
			for (Jolt jolt2 : arrayJolt) {
				if (jolt.getInstagramId().equalsIgnoreCase(jolt2.getInstagramId())) {
					return true;
				}
			}
			return false;

		} else if (jolt.isFacebook()) {
			for (Jolt jolt2 : arrayJolt) {
				if (jolt.getFacebookId().equalsIgnoreCase(jolt2.getFacebookId())) {
					return true;
				}
			}
			return false;

		} else {
			for (Jolt jolt2 : arrayJolt) {
				if (jolt.getId().equalsIgnoreCase(jolt2.getId())) {
					return true;
				}
			}
			return false;
		}
	}

	public void setCurrentId(int currentId) {
		this.currentPositionJolt = currentId;
	}

	public int getCurrentId() {
		return currentPositionJolt;
	}
}
