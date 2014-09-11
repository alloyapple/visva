package com.fgsecure.ujoolt.app.utillity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fgsecure.ujoolt.app.GCMIntentService;
import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.define.Filter;
import com.fgsecure.ujoolt.app.define.LoginType;
import com.fgsecure.ujoolt.app.define.ModeScreen;
import com.fgsecure.ujoolt.app.facebook.UtilityFacebook;
import com.fgsecure.ujoolt.app.info.UjooltSharedPreferences;
import com.fgsecure.ujoolt.app.info.WebServiceConfig;
import com.fgsecure.ujoolt.app.json.Jolt;
import com.fgsecure.ujoolt.app.json.ParserUtility;
import com.fgsecure.ujoolt.app.json.UserInfo;
import com.fgsecure.ujoolt.app.network.AsyncHttpBase;
import com.fgsecure.ujoolt.app.network.AsyncHttpGet;
import com.fgsecure.ujoolt.app.network.AsyncHttpPost;
import com.fgsecure.ujoolt.app.network.AsyncHttpResponseListener;
import com.fgsecure.ujoolt.app.network.AsyncHttpResponseProcess;
import com.fgsecure.ujoolt.app.network.ParameterFactory;
import com.fgsecure.ujoolt.app.screen.ItemizedOverlays;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.screen.MyOverlay;
import com.google.android.maps.GeoPoint;

public class JoltHolder {

	public static final byte GET_DEFAULT = 2;
	public static final byte GET_AFTER_POST = 1;

	public MainScreenActivity mainScreenActivity;
	public UjooltSharedPreferences ujooltSharedPreferences;
	private String TAG = getClass().getSimpleName();

	public int maxDistance;
	public int curDistance;
	FileCache fileCache;
	public MemoryCache memoryCache;

	public ArrayList<Jolt> arrJolt;
	public ArrayList<Jolt> arrAvailableJolt;

	public ArrayList<Jolt> arrAvailableJoltRecent;
	public ArrayList<Jolt> arrAvailableJoltSocial;
	public ArrayList<Jolt> arrAvailableJoltFavoris;
	public ArrayList<Jolt> arrAvailableJoltTop;

	public ArrayList<Jolt> arrJoltInstagram;
	public ArrayList<Jolt> arrAvailableJoltInstagram;

	public ArrayList<Jolt> arrJoltFacebook;
	public ArrayList<Jolt> arrJoltFacebookMe;
	public ArrayList<Jolt> arrJoltFacebookMyEvent;
	public ArrayList<Jolt> arrAvailableJoltFacebook;

	public ArrayList<Jolt> arrNearestJolt;
	public ArrayList<Jolt> arrSearchResult;
	public ArrayList<Jolt> arrJoltFollow;
	public ArrayList<Jolt> arrJoltFollowAvai;

	// private String resultAddress = "";
	public static String pathVideoToUpLoad = "";
	public static boolean isPhoto = true;

	public JoltHolder(MainScreenActivity mainScreenActivity) {
		arrJolt = new ArrayList<Jolt>();
		arrJoltInstagram = new ArrayList<Jolt>();
		arrJoltFacebook = new ArrayList<Jolt>();
		arrJoltFacebookMe = new ArrayList<Jolt>();
		arrJoltFacebookMyEvent = new ArrayList<Jolt>();

		arrAvailableJolt = new ArrayList<Jolt>();
		arrAvailableJoltInstagram = new ArrayList<Jolt>();
		arrAvailableJoltFacebook = new ArrayList<Jolt>();

		arrAvailableJoltRecent = new ArrayList<Jolt>();
		arrAvailableJoltSocial = new ArrayList<Jolt>();
		arrAvailableJoltFavoris = new ArrayList<Jolt>();
		arrAvailableJoltTop = new ArrayList<Jolt>();

		arrNearestJolt = new ArrayList<Jolt>();
		arrSearchResult = new ArrayList<Jolt>();
		arrJoltFollow = new ArrayList<Jolt>();
		arrJoltFollowAvai = new ArrayList<Jolt>();

		this.mainScreenActivity = mainScreenActivity;
		fileCache = new FileCache(mainScreenActivity);
		memoryCache = new MemoryCache();
		ujooltSharedPreferences = new UjooltSharedPreferences(mainScreenActivity);
		maxDistance = 3000;
	}

	public void addJolt(ArrayList<Jolt> arrayList, Jolt jolt) {
		if (arrayList == null) {
			arrayList = new ArrayList<Jolt>();
		}
		int size = arrayList.size();
		jolt.setPositionArr(size);
		arrayList.add(jolt);
	}

	/**
	 * 
	 * @param lati
	 * @param longi
	 * @return
	 */
	public Jolt getJolt(ArrayList<Jolt> arrayList, int lati, int longi) {
		int size = arrayList.size();
		for (int i = 0; i < size; i++) {
			Jolt jolt = arrayList.get(i);
			if (jolt.getLatitudeE6() == lati && jolt.getLongitudeE6() == longi) {
				return jolt;
			}
		}
		return null;
	}

	/**
	 * get all jolt from location
	 * 
	 * @param lati
	 * @param longi
	 * @param date
	 * @param isFirstLoad
	 * @param distance
	 * @param keyword
	 * @param type
	 */

	public void getAllJoltsFromLocation(int lati, int longi, long date, final boolean isFirstLoad,
			int distance, String keyword, final byte type, final boolean showDialog) {

		mainScreenActivity.mode = ModeScreen.JOLTS;

		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void after(int statusCode, HttpResponse response) {

						if (showDialog) {
							// mainScreenActivity.closeProgressDialog();
							// Toast.makeText(mContext, "Upload completed",
							// Toast.LENGTH_SHORT).show();
						}

						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							if (response != null) {
								String json = null;
								try {
									json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
									Log.e(TAG, "zzzz : " + json + "");
								} catch (ParseException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								if (json != null) {
									try {
										try {
											ParserUtility.getJoltObjectsFromJS(json,
													JoltHolder.this, ParserUtility.GET_JOLT, type,
													mainScreenActivity);

											if (arrJolt.size() > 0) {

												getAvailableJolt(arrJolt);
												getAvailableJoltFromInstagram(arrJoltInstagram);
												getAvailableJoltFromFacebook(arrJoltFacebook);

												if (isFirstLoad) {
													// getNearestJolts();
													mainScreenActivity.mapView.viewFullPoint(
															arrNearestJolt,
															mainScreenActivity.mLatitudeE6,
															mainScreenActivity.mLongitudeE6,
															ModeScreen.JOLTS);
												}

												if (arrAvailableJolt.size() > 0) {
													mainScreenActivity.groupArrayJoltFilter();
												}
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else if (!mainScreenActivity.isShowNetworkError) {
								mainScreenActivity.showDialogNetworkError();
							}
						} else if (!mainScreenActivity.isShowNetworkError) {
							mainScreenActivity.showDialogNetworkError();
						}
					}

					@Override
					public void before() {

						if (showDialog)
							mainScreenActivity.showProgressDialog(Language.upLoading);
					}
				});

		String URL = WebServiceConfig.URL_GET_JOLTS_FROM_LOCATION + "?lat=" + encode("" + lati)
				+ "&long=" + encode("" + longi) + "&isfirstload="
				+ encode("" + mainScreenActivity.isFirstSearch) + "&distance="
				+ encode("" + distance) + "&keyword=" + encode("" + keyword);

		// Log.e("chua Login", "luong URL " + URL);
		asyncHttpGet.execute(URL);
	}

	public void syncUser(String ujooltId, String facebookId, String twitterId, LoginType loginType,
			final GeoPoint geoPoint) {

		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {
			@Override
			public void processIfResponseSuccess(String response) {
				mainScreenActivity.closeProgressDialog();
				Log.e(TAG, "sync reponse: " + response);
				syncUserAndJolt(response, geoPoint);
			}

			@Override
			public void before() {
				mainScreenActivity.showProgressDialog(Language.pleaseWait);
			}
		});
		post.execute(WebServiceConfig.urlSyncUser(ujooltId, facebookId, twitterId, loginType));
	}

	public void getSyncUser(String id, final GeoPoint geoPoint) {
		mainScreenActivity.showProgressDialog(Language.pleaseWait);
		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void after(int statusCode, HttpResponse response) {
						mainScreenActivity.closeProgressDialog();
						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							if (response != null) {
								String json = null;

								try {
									json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
								} catch (org.apache.http.ParseException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}

								if (json != null) {
									Log.e(TAG, "sync check result: " + json);
									syncUserAndJolt(json, geoPoint);
									// ParserUtility.getUserSync(json);
									//
									// getAllJoltsFromLocation(geoPoint,
									// ConfigUtility.getCurTimeStamp(), false,
									// 6000, "",
									// GET_DEFAULT, false, false);
								}
							} else if (!mainScreenActivity.isShowNetworkError) {
								mainScreenActivity.showDialogNetworkError();
							}
						} else if (!mainScreenActivity.isShowNetworkError) {
							mainScreenActivity.showDialogNetworkError();
						}
					}

					@Override
					public void before() {
					}
				});

		asyncHttpGet.execute(WebServiceConfig.urlCheckSyncUser(id));
	}

	private void syncUserAndJolt(String json, GeoPoint geoPoint) {
		ParserUtility.getUserSync(json);

		arrAvailableJolt.clear();
		arrAvailableJoltRecent.clear();
		arrAvailableJoltSocial.clear();
		arrAvailableJoltFavoris.clear();
		arrAvailableJoltTop.clear();
		arrJoltInstagram.clear();
		arrAvailableJoltInstagram.clear();
		arrJoltFacebook.clear();
		arrJoltFacebookMe.clear();
		arrJoltFacebookMyEvent.clear();
		arrAvailableJoltFacebook.clear();
		arrNearestJolt.clear();
		arrSearchResult.clear();
		arrJoltFollow.clear();
		arrJoltFollowAvai.clear();

		getAllJoltsFromLocation(geoPoint, ConfigUtility.getCurTimeStamp(), false, 6000, "",
				GET_DEFAULT, false, false);
	}

	public int getIdMAX(ArrayList<Jolt> arr) {
		int idMax = 0;

		idMax = Integer.parseInt(arr.get(0).getId());

		for (Jolt jolt : arr) {
			int id = Integer.parseInt(jolt.getId());
			if (idMax <= id) {
				idMax = id;
			}
		}

		return idMax;
	}

	public Jolt getJolt(ArrayList<Jolt> arr, String id) {
		Jolt jolt = null;
		for (Jolt j : arr) {
			if (j.getId().equalsIgnoreCase(id)) {
				jolt = j;
			}
		}

		return jolt;
	}

	public void getAllJoltsFromLocation(final GeoPoint geoPointInput, long date,
			final boolean isFirstLoad, int distance, String keyword, final byte type,
			final boolean showDialog, final boolean pressGPS) {

		mainScreenActivity.mode = ModeScreen.JOLTS;

		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void after(int statusCode, HttpResponse response) {
						mainScreenActivity.numberThreadNomal--;

						if (showDialog) {
							mainScreenActivity.closeProgressDialog();
							// Toast.makeText(mainScreenActivity.getBaseContext(),
							// "Upload completed", Toast.LENGTH_SHORT).show();
						}

						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							if (response != null) {
								String json = null;
								try {
									json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
									Log.e(TAG, "AllJolt: " + json);

								} catch (ParseException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								if (json != null) {
									try {
										ParserUtility.getJoltObjectsFromJS(json, JoltHolder.this,
												ParserUtility.GET_JOLT, type, mainScreenActivity);

										if (arrJolt.size() > 0) {
											Log.e("size", "luong =" + arrJolt.size());

											getAvailableJolt(arrJolt);
											getAvailableJoltFromInstagram(arrJoltInstagram);
											getAvailableJoltFromFacebook(arrJoltFacebook);
											getAvailableJoltFromFacebook(arrJoltFacebookMe);
											getAvailableJoltFromFacebook(arrJoltFacebookMyEvent);

											if (mainScreenActivity.mapView.getZoomLevel() < 7
													&& pressGPS == true) {
												// getNearestJolts();
												// mainScreenActivity.mapView
												// .viewFullPoint(arrNearestJolt,
												// mainScreenActivity.mLati,
												// mainScreenActivity.mLongi,
												// ModeScreen.JOLTS);
											}

											mainScreenActivity.delayAgain = true;
											mainScreenActivity.newDelayTimeFromNow = 400;
											mainScreenActivity.handgroup
													.post(mainScreenActivity.runGroup);
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							} else {
								if (!mainScreenActivity.isShowNetworkError)
									mainScreenActivity.showDialogNetworkError();
								mainScreenActivity.numberThreadNomal--;
							}

						} else {
							if (!mainScreenActivity.isShowNetworkError)
								mainScreenActivity.showDialogNetworkError();
							mainScreenActivity.numberThreadNomal--;
						}
					}

					@Override
					public void before() {
						if (showDialog)
							mainScreenActivity.showProgressDialog(Language.waiting);
					}
				});

		int lati = geoPointInput.getLatitudeE6();
		int longi = geoPointInput.getLongitudeE6();

		if (mainScreenActivity.numberThreadNomal < 0)
			mainScreenActivity.numberThreadNomal = 0;

		mainScreenActivity.numberThreadNomal++;

		if (mainScreenActivity.numberThreadNomal >= 2)
			mainScreenActivity.numberThreadNomal = 2;

		Log.v(": NUMBER Thead normal", "= " + mainScreenActivity.numberThreadNomal);

		String URL = "";

		if (mainScreenActivity.checkLogin() == false) {
			URL = WebServiceConfig.URL_GET_JOLTS_FROM_LOCATION + "?lat=" + encode("" + lati)
					+ "&long=" + encode("" + longi) + "&isfirstload="
					+ encode("" + mainScreenActivity.isFirstSearch) + "&distance="
					+ encode("" + distance) + "&keyword=" + encode("" + keyword);

			Log.e(TAG, "URL, login: " + URL);

		} else {
			URL = WebServiceConfig.URL_GET_JOLTS_FROM_LOCATION + "?lat=" + encode("" + lati)
					+ "&long=" + encode("" + longi) + "&isfirstload="
					+ encode("" + mainScreenActivity.isFirstSearch) + "&distance="
					+ encode("" + distance) + "&keyword=" + encode("" + keyword) + "&loginUserid="
					+ encode(mainScreenActivity.myLoginUserId);

			Log.e(TAG, "URL not login: " + URL);
		}

		asyncHttpGet.execute(URL);
	}

	/**
	 * get all jolt from instagram data to display on the map
	 * 
	 * @param lat
	 * @param longi
	 * 
	 */
	public void getAllJoltFromInstagram(final int lati, final int longi, final byte type,
			int distance) {

		final AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void before() {
					}

					@Override
					public void after(int statusCode, HttpResponse response) {
						// TODO Auto-generated method stub
						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							if (response != null) {
								String json = null;
								try {
									json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
								} catch (ParseException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}

								if (json != null) {

									try {
										ParserUtility.getJoltObjectsFromInstagramJS(json,
												JoltHolder.this, mainScreenActivity);

										if (arrJoltInstagram.size() > 0) {
											getAvailableJoltFromInstagram(arrJoltInstagram);

											// getNearestJolts();
											// mainScreenActivity.mapView.viewFullPoint(
											// arrNearestJolt,
											// mainScreenActivity.mLati,
											// mainScreenActivity.mLongi,
											// ModeScreen.JOLTS);

											if (arrAvailableJoltInstagram.size() > 0) {
												mainScreenActivity.groupArrayJoltFilter();
											}
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}
							}
						} else {
							if (!mainScreenActivity.isShowNetworkError) {
								mainScreenActivity.showDialogNetworkError();
							}
						}
					}
				});

		asyncHttpGet.execute("https://api.instagram.com/v1/media/search?lat=" + lati / 1E6
				+ "&lng=" + longi / 1E6 + "&client_id=c58596e2e7034872b6a47d0f262b7e5f"
				+ "&distance=" + distance);
	}

	public static boolean isUpdateInstagram = false;

	public void getAllJoltFromInstagram(final GeoPoint geoPointInput, final byte type,
			int distance, final boolean showDialog, final boolean pressGPS) {

		final AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void before() {
						if (showDialog && isUpdateInstagram)
							mainScreenActivity
									.showProgressDialog("Loading Instagram. Please wait...");
					}

					@Override
					public void after(int statusCode, HttpResponse response) {
						mainScreenActivity.numberThreadInstagram--;

						if (mainScreenActivity.showDialogInstagram) {
							mainScreenActivity.dialogInstagram.dismiss();
							mainScreenActivity.showDialogInstagram = false;
						}

						if (showDialog) {
							mainScreenActivity.closeProgressDialog();
							MyOverlay.isChangZoom = false;
							isUpdateInstagram = false;
						}

						// TODO Auto-generated method stub
						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							if (response != null) {
								String json = null;

								try {
									json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
								} catch (ParseException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								if (json != null) {
									try {
										try {
											ParserUtility.getJoltObjectsFromInstagramJS(json,
													JoltHolder.this, mainScreenActivity);

											if (arrJoltInstagram.size() > 0) {

												getAvailableJoltFromInstagram(arrJoltInstagram);

												if (mainScreenActivity.mapView.getZoomLevel() < 7
														|| pressGPS) {

													// getNearestJolts();
													// mainScreenActivity.mapView.viewFullPoint(
													// arrNearestJolt,
													// mainScreenActivity.mLati,
													// mainScreenActivity.mLongi,
													// ModeScreen.JOLTS);
												}

												isUpdateInstagram = true;

												mainScreenActivity.delayAgain = true;
												mainScreenActivity.newDelayTimeFromNow = 400;
												mainScreenActivity.handgroup
														.post(mainScreenActivity.runGroup);

											}
										} catch (JSONException e) {
											e.printStackTrace();

											Toast.makeText(mainScreenActivity,
													"Can't connect to Instagram",
													Toast.LENGTH_SHORT).show();
										}
									} catch (Exception e) {
										e.printStackTrace();
										Toast.makeText(mainScreenActivity,
												"Can't connect to Instagram", Toast.LENGTH_SHORT)
												.show();
									}
								}
							}
						} else {
							if (!mainScreenActivity.isShowNetworkError) {
								mainScreenActivity.showDialogNetworkError();
							}

							Toast.makeText(mainScreenActivity, "Can't connect to Instagram",
									Toast.LENGTH_SHORT).show();
							mainScreenActivity.numberThreadInstagram--;

						}
					}
				});

		if (mainScreenActivity.numberThreadInstagram < 0)
			mainScreenActivity.numberThreadInstagram = 0;

		mainScreenActivity.numberThreadInstagram++;

		if (mainScreenActivity.numberThreadInstagram >= 2)
			mainScreenActivity.numberThreadInstagram = 2;

		Log.v(": NUMBER Thread Instagram", "= " + mainScreenActivity.numberThreadInstagram);

		Log.v(": Lat-Log Instagram  ", "" + geoPointInput.getLatitudeE6() / 1E6 + " "
				+ geoPointInput.getLongitudeE6() / 1E6);

		asyncHttpGet.execute("https://api.instagram.com/v1/media/search?lat="
				+ geoPointInput.getLatitudeE6() / 1E6 + "&lng=" + geoPointInput.getLongitudeE6()
				/ 1E6 + "&client_id=c58596e2e7034872b6a47d0f262b7e5f" + "&distance=" + distance);

	}

	public static boolean isUpdateFacebook = false;

	/**
	 * get all jolt from facebook data to display on the map
	 * 
	 * @param lat
	 * @param longi
	 * 
	 */
	public void getAllJoltFromFacebook(GeoPoint geoLocation) {
		double lati = (double) geoLocation.getLatitudeE6() / 1E6;
		double longi = (double) geoLocation.getLongitudeE6() / 1E6;

		long time4h = System.currentTimeMillis() / 1000 - 14400;

		String stringHttpGet = WebServiceConfig.URL_FACEBOOK_GET_LOCATION + lati + "," + longi
				+ "&distance=1000&limit=50&access_token="
				+ UtilityFacebook.mFacebook.getAccessToken() + "&since=" + time4h;

		Log.e(TAG, "info fb httpget: " + stringHttpGet);

		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {

			@Override
			public void before() {
				mainScreenActivity.numberThreadFacebook++;
			}

			@Override
			public void processIfResponseSuccess(String response) {
				if (response != null) {
					try {
						if (mainScreenActivity.numberThreadFacebook <= 1) {
							Log.e(TAG, "zz fb response: " + response);
							ParserUtility.getJoltObjectsFromFacebookJson(response, JoltHolder.this);
							Log.e(TAG, "zz info fb arr jolt size: " + arrJoltFacebook.size());
						}

						mainScreenActivity.numberThreadFacebook--;
						if (mainScreenActivity.numberThreadFacebook < 0) {
							mainScreenActivity.numberThreadFacebook = 0;
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					if (!mainScreenActivity.isShowNetworkError) {
						mainScreenActivity.showDialogNetworkError();
					}
				}
			}
		});
		post.execute(stringHttpGet);
	}

	public void getAllJoltOfMeFromFacebook() {

		String stringHttpGet = WebServiceConfig.URL_FACEBOOK_GET_OBJECT_WITH_LOCATION
				+ UtilityFacebook.mFacebook.getAccessToken();

		Log.e(TAG, "info fb me httpget: " + stringHttpGet);

		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {

			@Override
			public void before() {
				mainScreenActivity.numberThreadFacebookMe++;
			}

			@Override
			public void processIfResponseSuccess(String response) {
				if (response != null) {
					try {
						if (mainScreenActivity.numberThreadFacebookMe <= 1) {

							ParserUtility.getJoltObjectsOfMeFromFacebook(response, JoltHolder.this);
							Log.e(TAG, "zz info fb me json: " + response);
							Log.e(TAG, "zz info fb arr jolt size: " + arrJoltFacebookMe.size());
						}

						mainScreenActivity.numberThreadFacebookMe--;
						if (mainScreenActivity.numberThreadFacebookMe < 0) {
							mainScreenActivity.numberThreadFacebookMe = 0;
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (!mainScreenActivity.isShowNetworkError) {
					mainScreenActivity.showDialogNetworkError();
				}
			}
		});
		post.execute(stringHttpGet);
	}

	public void getAllJoltOfMyEventFromFacebook() {

		String stringHttpGet = WebServiceConfig.URL_FACEBOOK_GET_EVENT
				+ UtilityFacebook.mFacebook.getAccessToken();
		// +"AAAAAAITEghMBAJeRyQAcA7PTyUCbJ3SOfyrx8lHBvKkcE7AOEMsfKlIkALJw81sVJpFY6cAscNBG7pN8bu9ZAGD6m9ZAvOXNhPfI2Q7pPbFKjE1VF9";

		Log.e(TAG, "info fb me httpget event: " + stringHttpGet);

		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {

			@Override
			public void before() {
				mainScreenActivity.numberThreadFacebookMyEvent++;
			}

			@Override
			public void processIfResponseSuccess(String response) {
				if (response != null) {
					try {
						ParserUtility
								.getJoltObjectsOfMyEventFromFacebook(response, JoltHolder.this);
						// Log.e(TAG, "zz info fb me number thread: "
						// + mainScreenActivity.numberThreadFacebookMyEvent);
						// Log.e(TAG, "zz info fb me json invent: " + response);
						// Log.e(TAG,
						// "zz info fb arr jolt size invent: " +
						// arrJoltFacebookMyEvent.size());

						mainScreenActivity.numberThreadFacebookMyEvent--;
						if (mainScreenActivity.numberThreadFacebookMyEvent < 0) {
							mainScreenActivity.numberThreadFacebookMyEvent = 0;
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (!mainScreenActivity.isShowNetworkError) {
					mainScreenActivity.showDialogNetworkError();
				}
			}
		});
		post.execute(stringHttpGet);
	}

	public void getFavouriteFromUser(final GeoPoint geoPoint, final long date,
			final boolean isFirstLoad, final int distance, final String keyword, final byte type,
			final boolean showDialog) {

		String stringHttpGet = WebServiceConfig.URL_GET_FAVOURITE_FROM_USER + "?loginUserid="
				+ encode(mainScreenActivity.myLoginUserId);

		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {

			@Override
			public void processIfResponseSuccess(String response) {

				Log.e("hhhe", "zzzz" + response);

				if (response != null) {
					arrAvailableJolt.clear();
					addToArrFavouriteId(response);
					getAllJoltsFromLocation(geoPoint, date, isFirstLoad, distance, keyword, type,
							showDialog, false);

				} else {
					if (!mainScreenActivity.isShowNetworkError) {
						mainScreenActivity.showDialogNetworkError();
					}
				}
			}
		});
		post.execute(stringHttpGet);
	}

	public ArrayList<ArrayList<String>> arrRejolt = new ArrayList<ArrayList<String>>();

	public void getRejoltFromId() {
		String joltId = mainScreenActivity.curItemizedOverlays.arrayJolt.get(
				mainScreenActivity.curItemizedOverlays.currentPositionJolt).getId();
		String stringHttpGet = WebServiceConfig.URL_GET_REJOLT_FROM_ID + "?joltId="
				+ encode(joltId);

		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {

			@Override
			public void processIfResponseSuccess(String response) {

				Log.e(TAG, "zzzz response get rejolt from id: " + response);

				if (response != null) {
					try {
						if (response.contains("No one rejolt")) {
							mainScreenActivity.layoutViewListRejolt.removeAllViewsInLayout();
						} else {
							// ParserUtility.getFriendObjectsFromID(response,
							// JoltHolder.this,
							// mainScreenActivity);

							ParserUtility.getRejolterFromID(response, JoltHolder.this,
									mainScreenActivity);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (!mainScreenActivity.isShowNetworkError) {
					mainScreenActivity.showDialogNetworkError();
				}
			}
		});
		post.execute(stringHttpGet);
	}

	public void addToArrFavouriteId(String listId) {
		String[] list = listId.split(",");

		for (int i = 0; i < list.length; i++) {
			if (!isStringExistInArray(list[i], mainScreenActivity.arrFavouriteId)) {
				mainScreenActivity.arrFavouriteId.add(list[i]);
			}
		}
	}

	public boolean isStringExistInArray(String string, ArrayList<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			if (string.equals(arr.get(i))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Hander update facebook jolt
	 */
	public Handler handle_updateJoltFacebook = new Handler();

	public boolean isUpdateJoltFacebook = false;
	public int timeFacebook = 400;
	public Runnable runnable_updateJoltFacebook = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (isDisPlayFacebook) {
				isDisPlayFacebook = false;

				handle_updateJoltFacebook.removeCallbacksAndMessages(null);
				handle_updateJoltFacebook.postDelayed(this, timeFacebook);

			} else {
				getAllJoltFromFacebook(mainScreenActivity.mapView.getMapCenter());
				getAllJoltOfMeFromFacebook();
				getAllJoltOfMyEventFromFacebook();
			}
		}
	};

	/*
	 * Handler display facebook jolt
	 */
	public Handler handle_disPlayFacebook = new Handler();

	public boolean isDisPlayFacebook = false;
	public Runnable runnable_disPlayFacebook = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (isDisPlayFacebook) {
				isDisPlayFacebook = false;

				handle_disPlayFacebook.removeCallbacksAndMessages(null);
				handle_disPlayFacebook.postDelayed(this, 4000);

			} else {
				displayJoltFacebook();
			}
		}
	};

	public void displayJoltFacebook() {
		boolean avai = false;
		if (arrJoltFacebook.size() > 0) {
			getAvailableJoltFromFacebook(arrJoltFacebook);
			avai = true;
		}

		if (arrJoltFacebookMe.size() > 0) {
			getAvailableJoltFromFacebook(arrJoltFacebookMe);
			avai = true;
		}

		if (arrJoltFacebookMyEvent.size() > 0) {
			getAvailableJoltFromFacebook(arrJoltFacebookMyEvent);
			avai = true;
		}

		if (avai) {
			mainScreenActivity.groupArrayJoltFilter();
		}
	}

	/**
	 * register Ujoolt account
	 * 
	 * @param email
	 * @param password
	 * @param loginType
	 * @param nick
	 */

	/**
	 * 
	 * @param user
	 * @param password
	 * @param loginType
	 */
	public void loginUjoolt(String user, String password, String loginType) {
		mainScreenActivity.showProgressDialog(Language.pleaseWait);
		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void after(int statusCode, HttpResponse response) {
						mainScreenActivity.closeProgressDialog();
						if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
							if (response != null) {
								String json = null;
								try {
									json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
								} catch (ParseException e1) {
									e1.printStackTrace();
								} catch (IOException e1) {
									e1.printStackTrace();
								}

								if (json != null) {
									Log.e(TAG, "login result: " + json);
									if (!json.equalsIgnoreCase("false")) {
										UserInfo userUjoolt = ParserUtility.getUserInfo(json);
										mainScreenActivity.viewLogin.setOnTouchListener(null);
										mainScreenActivity.viewLogin.isLogin = true;
										mainScreenActivity.isQuitRegister = true;
										// mainScreenActivity.myUserName =
										// userInfo.getUserName();
										// mainScreenActivity.lblMyNickname
										// .setText(mainScreenActivity.myUserName);
										// mainScreenActivity.myLoginUserid =
										// userInfo.getUserId();
										// mainScreenActivity.myLoginType =
										// userInfo.getLoginType();
										mainScreenActivity.setAccount(userUjoolt.getUserName(),
												userUjoolt.getUserId(), LoginType.UJOOLT);

										ujooltSharedPreferences.putLoginStatus(true);
										// ujooltSharedPreferences
										// .putUserName(mainScreenActivity.myUserName);
										// ujooltSharedPreferences
										// .putUserId(mainScreenActivity.myLoginUserid);
										// ujooltSharedPreferences
										// .putUserType(mainScreenActivity.myLoginType);

										ujooltSharedPreferences.putUserUjoolt(userUjoolt);
										ujooltSharedPreferences.putMainUser(userUjoolt);

										mainScreenActivity.resetIconItems();
										mainScreenActivity.setIconSocialNetwork();
									} else {
										mainScreenActivity.showDialogAlert(Language.login,
												Language.loginNotSuccess, Language.tryAgain);
									}
								}
							} else if (!mainScreenActivity.isShowNetworkError) {
								mainScreenActivity.showDialogNetworkError();
							}
						} else if (!mainScreenActivity.isShowNetworkError) {
							mainScreenActivity.showDialogNetworkError();
						}
					}

					@Override
					public void before() {
					}
				});

		asyncHttpGet.execute(WebServiceConfig.URL_LOGIN_UJOOLT + "?nick=" + encode(user)
				+ "&password=" + encode(password) + "&logintype=" + encode(loginType));
	}

	public void postJolt(Jolt jolt, boolean isPublic) {
		// checkAddressAvaiable(mainScreenActivity.mLatitudeE6,
		// mainScreenActivity.mLongitudeE6);

		if (MainScreenActivity.bitmapJoltAvatar != null) {
			if (!isPhoto) {
				// Log.e(TAG, "Path video: " + pathVideoToUpLoad);
				new uploadVideoAsyncTask(jolt, MainScreenActivity.bitmapJoltAvatar,
						pathVideoToUpLoad, isPublic).execute();
				if (mainScreenActivity.isShareFB) {
					mainScreenActivity.facebookConnector.uploadVideo(pathVideoToUpLoad,
							jolt.getText());
				}
			} else {
				new uploadImageAsyncTask(jolt, MainScreenActivity.bitmapJoltAvatar, isPublic, "")
						.execute();
			}
			// ujooltSharedPreferences.updateUpImageLanguage(true);
		} else {
			jolt.setPhotoBitmap(null);
			jolt.setPhotoBitmapFromURL(null);

			// ujooltSharedPreferences.updateUpImageLanguage(false);
			new uploadOnlyTextAsyncTask(jolt, isPublic).execute();
		}
		resetWhenFishPost(jolt);
	}

	/**
	 * reset link and icon when finish post jolt
	 */
	private void resetWhenFishPost(Jolt jolt) {

		isPhoto = true;
		pathVideoToUpLoad = "";
		MainScreenActivity.btnUploadPhoto.setBackgroundResource(R.drawable.ic_camera_off);
		// MainScreenActivity.bm_jolt_avatar = null;

		// mainScreenActivity.isShowDetail = false;
		// mainScreenActivity.groupArrayJolt(GET_AFTER_POST);
		mainScreenActivity.setNullInformationPostJolt();

	}

	/**
	 * 
	 * @param lati
	 * @param longi
	 */
	// public void checkAddressAvaiable(final double lati, final double longi) {
	// if (mainScreenActivity.myAddress == null
	// || (mainScreenActivity.myAddress != null && mainScreenActivity.myAddress
	// .equalsIgnoreCase(""))) {
	//
	// AsyncTask<String, String, String> asyn = new AsyncTask<String, String,
	// String>() {
	//
	// @Override
	// protected void onPreExecute() {
	// // mainScreenActivity.showProgressDialog("Please wait...");
	// // SmartLog.elog("Tomato", "checking address");
	// }
	//
	// @Override
	// protected String doInBackground(String... params) {
	// while (mainScreenActivity.myAddress == null
	// || (mainScreenActivity.myAddress != null && mainScreenActivity.myAddress
	// .equalsIgnoreCase(""))) {
	// Geocoder geo = new Geocoder(mainScreenActivity.getBaseContext(),
	// Locale.getDefault());
	// try {
	// List<Address> addresses = geo.getFromLocation(lati, longi, 1);
	// if (addresses != null && addresses.size() > 0) {
	// Address address = addresses.get(0);
	// mainScreenActivity.myAddress = "" + address.getFeatureName() + ", "
	// + address.getSubAdminArea() + ", " + address.getAdminArea()
	// + ", " + address.getCountryName();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// mainScreenActivity.closeProgressDialog();
	// }
	// };
	// asyn.execute();
	// }
	// }

	/**
	 * post a jolt only text
	 * 
	 * @param lati
	 * @param longi
	 * @param text
	 * @param device_id
	 * @param nick
	 * @param address
	 * @param loginType
	 * @param loginUserid
	 * @param instagramId
	 */
	// private void postJoltOnlyText(final int lati, final int longi,
	// final String text, final String device_id, final String nick,
	// final String address, final String loginType,
	// final String loginUserid, final String instagramId) {
	// List<NameValuePair> parameters = ParameterFactory.createPostJoltParams(
	// "" + lati, "" + longi, text, device_id, nick, address,
	// loginType, loginUserid, instagramId);
	//
	// AsyncHttpPost post = new AsyncHttpPost(mainScreenActivity,
	// new AsyncHttpResponseProcess(mainScreenActivity) {
	// @Override
	// public void processIfResponseSuccess(String response) {
	// JSONArray jsonArray = null;
	// JSONObject jsonObject = null;
	// try {
	// jsonArray = new JSONArray(response);
	// jsonObject = jsonArray.getJSONObject(0);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// if (jsonObject != null) {
	// Jolt jolt = new Jolt(JoltHolder.this);
	// ParserUtility.setInfo(jolt, jsonObject,
	// mainScreenActivity);
	// jolt.isNewCreate = true;
	// arrAvailableJolt.add(jolt);
	// }
	// }
	// }, parameters);
	// post.execute(WebServiceConfig.URL_POST_JOLT);
	// }

	/**
	 * 
	 * @param regID
	 * @param lati
	 * @param longi
	 * @param isFrance
	 */
	// public void postLocation(String regID, double lati, double longi,
	// boolean isFrance) {
	// String stringLanguage;
	// if (isFrance) {
	// stringLanguage = "FR";
	// } else {
	// stringLanguage = "EN";
	// }
	//
	// AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
	// new AsyncHttpResponseListener() {
	// @Override
	// public void before() {
	// }
	//
	// @Override
	// public void after(int statusCode, HttpResponse response) {
	// }
	//
	// }, null);
	// try {
	// String url = WebServiceConfig.URL_CHECK_JOLT_AROUND + "&token"
	// + encode(regID) + "deviceType" + encode("AD") + "&lat="
	// + encode("" + lati) + "&long=" + encode("" + longi)
	// + "&language" + encode(stringLanguage);
	// asyncHttpGet.execute(url);
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 
	 * @param isFrance
	 */
	// public void postLanguage(boolean isFrance) {
	// String stringLanguage;
	// if (isFrance) {
	// stringLanguage = "fr";
	// } else {
	// stringLanguage = "en";
	// }
	//
	// AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
	// new AsyncHttpResponseListener() {
	//
	// @Override
	// public void before() {
	// }
	//
	// @Override
	// public void after(int statusCode, HttpResponse response) {
	// }
	//
	// }, null);
	// try {
	// String url = WebServiceConfig.URL_POST_REGISTRATION + "&language"
	// + encode(stringLanguage);
	// asyncHttpGet.execute(url);
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * post registration id to server
	 * 
	 * @param reg
	 * @param lati
	 * @param longi
	 * @param isActive
	 * @param badge
	 * @param isFrance
	 */
	// public void postRegistrationID(String reg, double lati, double longi,
	// boolean isActive, int badge, boolean isFrance) {
	// UjooltSharedPreferences ujooltSharedPreferences = UjooltSharedPreferences
	// .getInstance(mainScreenActivity.getBaseContext());
	// String registration = ujooltSharedPreferences.getRegistrationId();
	// String stringLanguage;
	// if (isFrance) {
	// stringLanguage = "fr";
	// } else {
	// stringLanguage = "en";
	// }
	//
	// AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
	// new AsyncHttpResponseListener() {
	//
	// @Override
	// public void after(int statusCode, HttpResponse response) {
	// if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
	// if (response != null) {
	// // String json = null;
	// try {
	// // json =
	// EntityUtils.toString(response.getEntity(),
	// HTTP.UTF_8);
	// } catch (ParseException e1) {
	// e1.printStackTrace();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// } else if (!mainScreenActivity.isShowNetworkError) {
	// mainScreenActivity.showDialogNetworkError();
	// }
	// } else if (!mainScreenActivity.isShowNetworkError) {
	// mainScreenActivity.showDialogNetworkError();
	// }
	// }
	//
	// @Override
	// public void before() {
	// }
	// }, null);
	// try {
	// String url = WebServiceConfig.URL_POST_REGISTRATION + "?udid="
	// + encode(DeviceConfig.device_id) + "&register_id="
	// + encode(registration) + "&lat=" + encode("" + lati)
	// + "&long=" + encode("" + longi) + "&isactivate="
	// + encode("" + mainScreenActivity.isPush) + "&badge="
	// + encode("" + badge) + "&language" + encode(stringLanguage);
	// asyncHttpGet.execute(url);
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }

	// private void postRegistrationID(String reg, double lati, double longi,
	// boolean isActive,
	// boolean isFrance) {
	//
	// String stringLanguage;
	// if (isFrance) {
	// stringLanguage = "fr";
	// } else {
	// stringLanguage = "en";
	// }
	//
	// AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
	// new AsyncHttpResponseListener() {
	//
	// @Override
	// public void after(int statusCode, HttpResponse response) {
	// if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
	// if (response != null) {
	// String json = null;
	// try {
	// json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
	// // Log.e(TAG,
	// // "response registration id: " + json);
	// } catch (ParseException e1) {
	// e1.printStackTrace();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	// } else if (!mainScreenActivity.isShowNetworkError) {
	// mainScreenActivity.showDialogNetworkError();
	// }
	// } else if (!mainScreenActivity.isShowNetworkError) {
	// mainScreenActivity.showDialogNetworkError();
	// }
	// }
	//
	// @Override
	// public void before() {
	// }
	// }, null);
	// try {
	// String url = WebServiceConfig.URL_POST_REGISTRATION + "?udid="
	// + encode(DeviceConfig.device_id) + "&register_id="
	// + encode(CommonUtilities.REGISTRATION_ID) + "&lat=" + encode("" + lati)
	// + "&long=" + encode("" + longi) + "&isactivate=" + encode("" + isActive)
	// + "&language" + encode(stringLanguage);
	// asyncHttpGet.execute(url);
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 
	 * @param arrJolt
	 */
	public void filterJolts(ArrayList<Jolt> arrJolt) {
		arrJoltFollowAvai.clear();
		for (Jolt jolt : arrJolt) {
			if (!isExistInArray(jolt, arrAvailableJolt)) {
				arrJoltFollowAvai.add(jolt);
			}
		}
	}

	/**
	 * 
	 * @param lati
	 * @param longi
	 */
	// public void setAddress(final double lati, final double longi) {
	// AsyncTask<String, String, String> asyn = new AsyncTask<String, String,
	// String>() {
	//
	// @Override
	// protected void onPreExecute() {
	// }
	//
	// @Override
	// protected String doInBackground(String... params) {
	// while (resultAddress == null
	// || (resultAddress != null && resultAddress.equalsIgnoreCase(""))) {
	// Geocoder geo = new Geocoder(mainScreenActivity.getBaseContext(),
	// Locale.getDefault());
	// try {
	// List<Address> addresses = geo.getFromLocation(lati, longi, 1);
	// if (addresses != null && addresses.size() > 0) {
	// Address address = addresses.get(0);
	// resultAddress = "" + address.getFeatureName() + ", "
	// + address.getSubAdminArea() + ", " + address.getAdminArea()
	// + ", " + address.getCountryName();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// mainScreenActivity.closeProgressDialog();
	// }
	//
	// };
	// asyn.execute();
	//
	// }

	/**
	 * 
	 * @param jolt
	 */

	public void postRejolt(final Jolt jolt) {
		if (jolt.isOnUjooltServer()) {
			
		}
		
		

		if (jolt.isFacebook() && jolt.getId().equalsIgnoreCase("")
				&& !jolt.getVideoURL().equalsIgnoreCase("")) {
			BitmapDrawable drawable = (BitmapDrawable) mainScreenActivity.imgAvatar.getDrawable();
			new uploadImageAsyncTask(jolt, drawable.getBitmap(), true,
					mainScreenActivity.myUserName).execute();

		} else if (jolt.isInstagram() && jolt.getId().equalsIgnoreCase("")) {
			BitmapDrawable drawable = (BitmapDrawable) mainScreenActivity.imgAvatar.getDrawable();
			new uploadImageAsyncTask(jolt, drawable.getBitmap(), true,
					mainScreenActivity.myUserName).execute();

		} else {
			List<NameValuePair> parameters = ParameterFactory.createPostRejoltParams(
					"" + jolt.getLatitudeE6(), "" + jolt.getLongitudeE6(), jolt.getDeviceId(),
					mainScreenActivity.myUserName, jolt.getId(),
					LoginType.getString(mainScreenActivity.myLoginType),
					mainScreenActivity.myLoginUserId);

			AsyncHttpPost post = new AsyncHttpPost(mainScreenActivity,
					new AsyncHttpResponseProcess(mainScreenActivity) {

						@Override
						public void processIfResponseSuccess(String response) {
							context.closeProgressDialog();
							processPostRejoltSuccess(response, jolt);
						}

						@Override
						public void before() {
							context.showProgressDialog(Language.pleaseWait);
						}

					}, parameters);

			post.execute(WebServiceConfig.URL_POST_JOLT);
		}
		mainScreenActivity.layoutShowAvatar.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @param response
	 */
	public void processPostJoltSuccess(String response, Jolt jolt) {

		Log.e(TAG, "rejolt response" + response);
		mainScreenActivity.postRegistrationId();

		JSONArray jsonArray = null;
		JSONObject jsonObject = null;
		String idJolt = null;
		try {
			jsonArray = new JSONArray(response);
			if (jsonArray != null && jsonArray.length() > 0) {
				jsonObject = jsonArray.getJSONObject(0);
				idJolt = ParserUtility.getStringValue(jsonObject, WebServiceConfig.PARAM_JOLT_ID);
			}

			if (mainScreenActivity.curItemizedOverlays != null) {
				isRejolt = true;
				flagCurrentIndexJolt = mainScreenActivity.curItemizedOverlays.index;
			}

			if (jolt.isFacebook()) {

				jolt.setDeviceID(DeviceConfig.device_id);
				// jolt.setAddress(resultAddress);
				jolt.setLoginType(mainScreenActivity.myLoginType);
				// jolt.setLoginUserid(mainScreenActivity.myLoginUserid);
				jolt.setNumberRejolt(1);
				jolt.setPublicFacebook(true);
				jolt.setId(idJolt);
				Log.e(TAG, "rejolt FB" + idJolt);

				if (mainScreenActivity.postFavourite) {
					mainScreenActivity.postFavourite = false;
					mainScreenActivity.postFavourite(jolt);
				}

				mainScreenActivity.groupArrayJoltFilter();

				flagJolt = mainScreenActivity.curItemizedOverlays.currentPositionJolt;
				mainScreenActivity.showJoltDetail(jolt);
				mainScreenActivity.curItemizedOverlays.setInformation();

			} else if (jolt.isInstagram()) {

				jolt.setDeviceID(DeviceConfig.device_id);
				// jolt.setAddress(resultAddress);
				jolt.setLoginType(mainScreenActivity.myLoginType);
				// jolt.setLoginUserid(mainScreenActivity.myLoginUserid);
				jolt.setNumberRejolt(1);
				jolt.setPublicFacebook(true);
				jolt.setId(idJolt);

				if (mainScreenActivity.postFavourite) {
					mainScreenActivity.postFavourite = false;
					mainScreenActivity.postFavourite(jolt);
				}

				mainScreenActivity.groupArrayJoltFilter();

				flagJolt = mainScreenActivity.curItemizedOverlays.currentPositionJolt;
				mainScreenActivity.showJoltDetail(jolt);
				mainScreenActivity.curItemizedOverlays.setInformation();

			} else {
				Jolt newJolt = new Jolt(this);

				// SetInfo for new jolt
				ParserUtility.setInfo(newJolt, jsonObject, mainScreenActivity);
				arrAvailableJolt.add(newJolt);
				mainScreenActivity.isPublicPostJolt = true;

				mainScreenActivity.groupArrayJoltFilter();
				// mainScreenActivity.showJoltDetail(GET_AFTER_POST);
				mainScreenActivity.showJoltDetail(newJolt);

				mainScreenActivity.isFilterMine = true;
				mainScreenActivity.setIconFilter(Filter.MINE, mainScreenActivity.isFilterMine);
				mainScreenActivity.hideKeyBoard(mainScreenActivity.txtSearchBar);
			}

			postRequestNotification(jolt);

		} catch (JSONException e) {
			e.printStackTrace();
			mainScreenActivity.isRejolt = false;
			Toast.makeText(mainScreenActivity, "Not success!", Toast.LENGTH_SHORT).show();
		}

		mainScreenActivity.viewJolt.setVisibility(View.VISIBLE);
		mainScreenActivity.viewJolt.setOnTouchListener(null);
		mainScreenActivity.imgToungeJolt.setOnTouchListener(mainScreenActivity);
	}

	public int flagCurrentIndexJolt = 0;
	public int flagJolt = 0;
	public boolean isRejolt = false;
	public boolean isJoltOnlyText = false;

	public void processPostJoltOnlyTextSuccess(String response, Jolt jolt) {
		JSONArray jsonArray = null;
		JSONObject jsonObject = null;
		String idJolt = null;
		try {
			jsonArray = new JSONArray(response);
			if (jsonArray != null && jsonArray.length() > 0) {
				jsonObject = jsonArray.getJSONObject(0);
				idJolt = ParserUtility.getStringValue(jsonObject, WebServiceConfig.PARAM_JOLT_ID);
			}
			Jolt jolt2 = new Jolt(this);
			ParserUtility.setInfoJoltOnlyText(jolt2, jsonObject, mainScreenActivity);
			arrAvailableJolt.add(jolt2);

			mainScreenActivity.groupArrayJoltFilter();
			mainScreenActivity.showJoltDetail(jolt2);

			mainScreenActivity.hideKeyBoard(mainScreenActivity.txtSearchBar);
			Log.e(TAG, "ID new jolt = " + idJolt);

			// }
		} catch (JSONException e) {
			e.printStackTrace();
			mainScreenActivity.isRejolt = false;
			Toast.makeText(mainScreenActivity, "Not success!", Toast.LENGTH_SHORT).show();
		}

		/**
		 * tomato change method post jolt
		 */

		mainScreenActivity.viewJolt.setVisibility(View.VISIBLE);
		mainScreenActivity.viewJolt.setOnTouchListener(null);
		mainScreenActivity.imgToungeJolt.setOnTouchListener(mainScreenActivity);

		postRequestNotification(jolt);
	}

	/**
	 * 
	 * @param response
	 * @param jolt
	 */
	public void processPostRejoltSuccess(String response, Jolt jolt) {

		if (!response.equals("") || response != null) {
			jolt.isRejolted = true;
		}

		if (response.contains("cannot rejolt") == false) {
			jolt.rejolt();
		} else {
			Toast.makeText(mainScreenActivity.getBaseContext(), "This Jolt has already rejolted !",
					Toast.LENGTH_SHORT).show();
		}

		ItemizedOverlays itemizedOverlays = mainScreenActivity.curItemizedOverlays;
		// itemizedOverlays.balloonView.setIcon();
		itemizedOverlays.setInformation();

		// mainScreenActivity.groupArrayJolt(JoltHolder.GET_DEFAULT);
		mainScreenActivity.groupArrayJoltFilter();

		mainScreenActivity.lblDragAndReleaseToRejolt.setText(R.string.text_view_drag_to_rejolt);

		// mainScreenActivity.button_rejolt.setVisibility(View.INVISIBLE);

	}

	/**
	 * 
	 * @author
	 * 
	 */
	public byte[] data;
	public ByteArrayBody bab;
	public FileBody filebodyVideo;

	private class uploadVideoAsyncTask extends AsyncTask<String, String, String> {
		// private final ProgressDialog dialog = new ProgressDialog(
		// mainScreenActivity);
		public Jolt jolt;
		public Bitmap bitmap;
		public String videoPath;
		public boolean isPublic;
		// public String nickRejolt;
		// public MainScreenActivity mainScreenActivity;
		ProgressDialog dialog;

		public uploadVideoAsyncTask(Jolt jolt, Bitmap bitmap, String videoPath, boolean isPublic) {
			this.isPublic = isPublic;
			this.jolt = jolt;
			this.bitmap = bitmap;
			this.videoPath = videoPath;
			// this.nickRejolt = nickRejolt;
			// this.mainScreenActivity = mainScreenActivity;
		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(mainScreenActivity, "", Language.upLoading, true);
		}

		@Override
		protected String doInBackground(String... args) {
			String response = null;
			try {
				response = executeMultipartPost(jolt, bitmap, videoPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			dialog.dismiss();
			Toast.makeText(mainScreenActivity, "Upload Error", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPostExecute(String response) {
			processPostJoltSuccess(response, jolt);
			dialog.dismiss();
		}

		public String executeMultipartPost(Jolt jolt, Bitmap bitmap, String videoPath)
				throws ParseException, IOException {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(WebServiceConfig.URL_POST_JOLT);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 50, bos);

				data = bos.toByteArray();
				bab = new ByteArrayBody(data, "image/jpeg", "x.jpg");
				filebodyVideo = new FileBody(new File(videoPath));

				StringBody title = new StringBody("Filename: " + videoPath);
				StringBody description = new StringBody("This is a video of the agent");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));

				reqEntity.addPart("video", filebodyVideo);
				reqEntity.addPart("photo", bab);
				reqEntity.addPart("title", title);
				reqEntity.addPart("description", description);

				reqEntity.addPart("userName",
						new StringBody(mainScreenActivity.myUserName, Charset.forName("UTF-8")));

				reqEntity.addPart("lat",
						new StringBody("" + jolt.getLatitudeE6(), Charset.forName("UTF-8")));
				reqEntity.addPart("long",
						new StringBody("" + jolt.getLongitudeE6(), Charset.forName("UTF-8")));
				reqEntity.addPart("text",
						new StringBody("" + jolt.getText(), Charset.forName("UTF-8")));
				reqEntity.addPart("device_id",
						new StringBody(DeviceConfig.device_id, Charset.forName("UTF-8")));
				reqEntity.addPart("nick",
						new StringBody("" + jolt.getNick(), Charset.forName("UTF-8")));
				// reqEntity.addPart("address",
				// new StringBody("" + jolt.getAddress(),
				// Charset.forName("UTF-8")));
				reqEntity.addPart("loginType",
						new StringBody("" + jolt.getLoginType(), Charset.forName("UTF-8")));
				reqEntity.addPart("loginUserid",
						new StringBody("" + jolt.getLoginUserid(), Charset.forName("UTF-8")));

				reqEntity.addPart("isPublic",
						new StringBody("" + isPublic, Charset.forName("UTF-8")));

				httppost.setEntity(reqEntity);
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent(), "UTF-8"));
				StringBuilder s = new StringBuilder();
				String result = "";
				while ((result = reader.readLine()) != null) {
					s = s.append(result);
				}
				result = s.toString();

				// fix bugs wrong image - video upload
				pathVideoToUpLoad = "";

				httpclient.getConnectionManager().shutdown();

				data = null;
				bab = null;
				bos = null;
				filebodyVideo = null;

				return result;
			} catch (Exception e) {
				return "";
			}
		}
	}

	private class uploadImageAsyncTask extends AsyncTask<String, String, String> {
		// private final ProgressDialog dialog = new ProgressDialog(
		// mainScreenActivity);
		public Jolt jolt;
		public boolean isPublic;
		public String nickRejolt;
		public Bitmap bmpRejolt;
		private ProgressDialog progressDialog;

		public uploadImageAsyncTask(Jolt j, Bitmap b, boolean isPublic, String nickRejolt) {
			jolt = j;
			this.isPublic = isPublic;
			this.nickRejolt = nickRejolt;
			bmpRejolt = b;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(mainScreenActivity, "", Language.upLoading, true);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressDialog.dismiss();
		}

		@Override
		protected String doInBackground(String... args) {
			String response = "";
			try {
				response = executeMultipartPost(jolt, bmpRejolt, nickRejolt);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String response) {
			progressDialog.dismiss();
			processPostJoltSuccess(response, jolt);
		}

		public String executeMultipartPost(Jolt jolt, Bitmap bm, String nickRejolt)
				throws Exception {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bm.compress(CompressFormat.JPEG, 50, bos);

				data = bos.toByteArray();

				HttpClient httpClient = new DefaultHttpClient();

				HttpPost postRequest = new HttpPost(WebServiceConfig.URL_POST_JOLT);

				bab = new ByteArrayBody(data, "image/jpeg", "x.jpg");

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
				reqEntity.addPart("photo", bab);
				reqEntity.addPart("lat",
						new StringBody("" + jolt.getLatitudeE6(), Charset.forName("UTF-8")));
				reqEntity.addPart("long",
						new StringBody("" + jolt.getLongitudeE6(), Charset.forName("UTF-8")));
				reqEntity.addPart("text",
						new StringBody(jolt.getText().toString(), Charset.forName("UTF-8")));
				reqEntity.addPart("device_id",
						new StringBody(DeviceConfig.device_id, Charset.forName("UTF-8")));

				reqEntity.addPart("userName",
						new StringBody(mainScreenActivity.myUserName, Charset.forName("UTF-8")));

				if (nickRejolt.equalsIgnoreCase("")) {
					reqEntity.addPart("nick",
							new StringBody(jolt.getNick(), Charset.forName("UTF-8")));
				} else {
					reqEntity.addPart("nick", new StringBody(nickRejolt, Charset.forName("UTF-8")));
					reqEntity.addPart("nick_rejolt",
							new StringBody(jolt.getNick(), Charset.forName("UTF-8")));
					reqEntity.addPart("date",
							new StringBody("" + jolt.getDate(), Charset.forName("UTF-8")));
				}

				// reqEntity.addPart("address",
				// new StringBody("" + jolt.getAddress(),
				// Charset.forName("UTF-8")));
				reqEntity.addPart("loginType",
						new StringBody("" + jolt.getLoginType(), Charset.forName("UTF-8")));
				reqEntity.addPart("loginUserid",
						new StringBody("" + jolt.getLoginUserid(), Charset.forName("UTF-8")));
				if (!jolt.getInstagramId().equals("") && jolt.getInstagramId() != null) {
					reqEntity.addPart("instagram_id",
							new StringBody(jolt.getInstagramId(), Charset.forName("UTF-8")));
				} else if (!jolt.getFacebookId().equals("") && jolt.getFacebookId() != null) {
					reqEntity.addPart("facebook_id",
							new StringBody(jolt.getFacebookId(), Charset.forName("UTF-8")));
				}

				reqEntity.addPart("isPublic",
						new StringBody("" + isPublic, Charset.forName("UTF-8")));

				postRequest.setEntity(reqEntity);

				HttpResponse response = httpClient.execute(postRequest);
				BufferedReader reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent(), "UTF-8"));

				StringBuilder s = new StringBuilder();
				String result = "";
				while ((result = reader.readLine()) != null) {
					s = s.append(result);
				}

				result = s.toString();
				httpClient.getConnectionManager().shutdown();

				data = null;
				bab = null;
				bos = null;

				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	private class uploadOnlyTextAsyncTask extends AsyncTask<String, String, String> {
		public Jolt jolt;
		public boolean isPublic;
		private ProgressDialog progressDialog;

		public uploadOnlyTextAsyncTask(Jolt jolt, boolean isPublic) {
			this.jolt = jolt;
			this.isPublic = isPublic;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(mainScreenActivity, "", Language.upLoading, true);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressDialog.dismiss();
		}

		@Override
		protected String doInBackground(String... args) {
			String response = "";
			try {
				response = executeMultipartPost(jolt);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String response) {

			progressDialog.dismiss();
			processPostJoltOnlyTextSuccess(response, jolt);
		}

		public String executeMultipartPost(Jolt jolt) throws Exception {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				// bm.compress(CompressFormat.JPEG, 50, bos);

				data = bos.toByteArray();

				HttpClient httpClient = new DefaultHttpClient();

				HttpPost postRequest = new HttpPost(WebServiceConfig.URL_POST_JOLT);

				// bab = new ByteArrayBody(data, "image/jpeg",
				// "x.jpg");

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
				// reqEntity.addPart("photo", bab);
				reqEntity.addPart("lat",
						new StringBody("" + jolt.getLatitudeE6(), Charset.forName("UTF-8")));
				reqEntity.addPart("long",
						new StringBody("" + jolt.getLongitudeE6(), Charset.forName("UTF-8")));
				reqEntity.addPart("text",
						new StringBody(jolt.getText().toString(), Charset.forName("UTF-8")));
				reqEntity.addPart("device_id",
						new StringBody(DeviceConfig.device_id, Charset.forName("UTF-8")));
				reqEntity.addPart("nick",
						new StringBody("" + jolt.getNick(), Charset.forName("UTF-8")));
				// reqEntity.addPart("address",
				// new StringBody("" + jolt.getAddress(),
				// Charset.forName("UTF-8")));
				reqEntity.addPart("loginType",
						new StringBody("" + jolt.getLoginType(), Charset.forName("UTF-8")));
				reqEntity.addPart("loginUserid",
						new StringBody("" + jolt.getLoginUserid(), Charset.forName("UTF-8")));
				reqEntity.addPart("userName",
						new StringBody(mainScreenActivity.myUserName, Charset.forName("UTF-8")));

				reqEntity.addPart("instagram_id", new StringBody("", Charset.forName("UTF-8")));

				reqEntity.addPart("isPublic",
						new StringBody("" + isPublic, Charset.forName("UTF-8")));

				postRequest.setEntity(reqEntity);

				HttpResponse response = httpClient.execute(postRequest);
				BufferedReader reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent(), "UTF-8"));

				StringBuilder s = new StringBuilder();
				String result = "";
				while ((result = reader.readLine()) != null) {
					s = s.append(result);
				}

				result = s.toString();

				Log.e("resPonse", result);

				httpClient.getConnectionManager().shutdown();

				data = null;
				bab = null;
				bos = null;

				return result;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmap(String url) {

		File f = fileCache.getFile(url);

		// File f = new File(fileCache.getFileName(url));
		// from SD cache
		// Bitmap b = decodeFile(f);
		Bitmap b = memoryCache.get(url);
		if (b != null) {
			return b;
		}
		// from web
		try {

			URL imageUrl = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();

			// int size =
			is.available();
			// BufferedInputStream bis = new BufferedInputStream(is, size);
			OutputStream os = new FileOutputStream(f);
			Utility.copyStream(is, os);
			os.close();

			// bitmap = decodeFile(is,url);
			memoryCache.put(url, decodeFile(f));

			return decodeFile(f);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 */
	public void clearCache() {
		AsyncTask<String, String, String> asyn = new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... params) {
				fileCache.clear();
				return null;
			}
		};
		asyn.execute();
		// fileCache.clear();
		// memoryCache.clear();
	}

	/**
	 * decodes image and scales it to reduce memory consumption
	 * 
	 * @param f
	 * @return
	 */
	public Bitmap bitmap;

	public Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 260;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			// long avai = Utils.getAvailableStorage();
			// Log.ee("AvaiStorage", "" + avai);
			// if(avai < Utils.MEM_ALLOW){
			// Log.ee("Out", "Memory");
			// clearCache();
			// lazyAdapter.notifyDataSetChanged();
			// }
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			// BitmapFactory.decodeByteArray(data, offset, length, opts)
			bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			return bitmap;
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	/**
	 * 
	 * @param in
	 * @param url
	 * @return
	 * @throws IOException
	 */
	// private Bitmap decodeFile(InputStream in, String url) throws IOException
	// {
	// // try {
	// // decode image size
	// BufferedInputStream bis = new BufferedInputStream(in);
	// BitmapFactory.Options o = new BitmapFactory.Options();
	// o.inJustDecodeBounds = true;
	// // BufferedInputStream temp = new BufferedInputStream(in);
	// BitmapFactory.decodeStream(in, null, o);
	//
	// // Find the correct scale value. It should be the power of 2.
	// final int REQUIRED_SIZE = 260;
	// int width_tmp = o.outWidth, height_tmp = o.outHeight;
	// int scale = 1;
	// while (true) {
	// if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
	// break;
	// width_tmp /= 2;
	// height_tmp /= 2;
	// scale *= 2;
	// }
	// // long avai = Utils.getAvailableStorage();
	// // Log.ee("AvaiStorage", "" + avai);
	// // if(avai < Utils.MEM_ALLOW){
	// // Log.ee("Out", "Memory");
	// // clearCache();
	// // lazyAdapter.notifyDataSetChanged();
	// // }
	// // decode with inSampleSize
	// BitmapFactory.Options o2 = new BitmapFactory.Options();
	// o2.inSampleSize = scale;
	// o2.inJustDecodeBounds = true;
	// // BufferedInputStream bis = new BufferedInputStream(in);
	// ByteArrayBuffer baf = null;
	// try {
	// baf = new ByteArrayBuffer(in.available());
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// int current = 0;
	// try {
	// while ((current = bis.read()) != -1) {
	// baf.append((byte) current);
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// File file = new File(fileCache.getFileName(url));
	// FileOutputStream fos = new FileOutputStream(file);
	// fos.write(baf.toByteArray());
	// fos.close();
	// Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(file), null,
	// o2);
	// return bm;
	// }

	/**
	 * 
	 * @param jolt
	 * @return
	 */
	public boolean isExistInArray(Jolt jolt, ArrayList<Jolt> arrayList) {
		if (arrayList.size() == 0) {
			return false;
		} else {
			for (Jolt jolt1 : arrayList) {

				if (jolt.isInstagram()) {
					if (jolt1.getInstagramId().equals(jolt.getInstagramId())) {
						return true;
					}

				} else if (jolt.isFacebook()) {
					if (jolt1.getFacebookId().equals(jolt.getFacebookId())
					// || jolt1.getLoginUserid().equals(jolt.getFacebookId())
					) {
						return true;
					}

				} else {
					if (jolt1.getId().equals(jolt.getId())) {
						return true;
					}
				}
			}
			return false;
		}
	}

	/**
	 * 
	 * @param arrJolt
	 */
	public void getAvailableJolt(ArrayList<Jolt> arrJolt) {

		if (mainScreenActivity.mode == ModeScreen.SEARCH) {
			arrAvailableJolt.clear();
		}

		ArrayList<Jolt> JoltNormal = new ArrayList<Jolt>();

		for (Jolt jolt : arrJolt) {
			if ((jolt.checkIsAlive() || jolt.isLike())
					&& isExistInArray(jolt, arrAvailableJolt) == false) {

				JoltNormal.add(jolt);
			}
		}

		if (arrAvailableJolt.size() > 0) {
			/*
			 * Stop this when connected to server UJoolt
			 */
			mainScreenActivity.handler.removeCallbacksAndMessages(null);
		}

		if (arrAvailableJolt.size() > 150 && JoltNormal.size() > 0) {
			arrAvailableJolt.clear();
		}

		arrAvailableJolt.addAll(JoltNormal);
	}

	/**
	 * get available jolt from instagram data
	 * 
	 * @param arrJolt
	 */

	private void getAvailableJoltFromInstagram(ArrayList<Jolt> arrJolt) {

		if (mainScreenActivity.mode == ModeScreen.SEARCH) {
			arrAvailableJoltInstagram.clear();
		}

		ArrayList<Jolt> JoltInstagram = new ArrayList<Jolt>();

		for (Jolt jolt : arrJolt) {

			if (isExistInArray(jolt, arrAvailableJoltInstagram) == false
					&& isExistInArray(jolt, arrAvailableJolt) == false) {
				JoltInstagram.add(jolt);
			}
		}

		if (arrAvailableJoltInstagram.size() > 150 && JoltInstagram.size() > 0) {
			arrAvailableJoltInstagram.clear();
		}

		arrAvailableJoltInstagram.addAll(JoltInstagram);
	}

	private void getAvailableJoltFromFacebook(ArrayList<Jolt> arrJolt) {

		if (mainScreenActivity.mode == ModeScreen.SEARCH) {
			arrAvailableJoltFacebook.clear();
		}

		ArrayList<Jolt> joltFacebook = new ArrayList<Jolt>();

		for (Jolt jolt : arrJolt) {
			if ((jolt.checkIsAlive() || (jolt.getId().equalsIgnoreCase(
					"jolt_event_all_day_facebook") && jolt.checkIsAliveEvent()))
					&& isExistInArray(jolt, arrAvailableJoltFacebook) == false
					&& isExistInArray(jolt, arrAvailableJolt) == false) {
				joltFacebook.add(jolt);
				// Log.e(TAG, "event: true");
			}
			// Log.e(TAG, "event: zz "+joltFacebook.size());
			// Log.e(TAG, "event text: "+jolt.getText());
			// Log.e(TAG, "event id: "+jolt.getId());
			// Log.e(TAG, "event is alive: "+jolt.checkIsAliveEvent());
			// Log.e(TAG, "event is exist: "+isExistInArray(jolt,
			// arrAvailableJoltFacebook) );
			// Log.e(TAG, "event is exist: "+isExistInArray(jolt,
			// arrAvailableJolt) );
		}

		for (int i = arrAvailableJoltFacebook.size() - 1; i >= 1; i--) {
			// Log.d(TAG, "id fb:z " + arrAvailableJoltFacebook.size());
			// Log.d(TAG, "id fb:z " + i);

			Jolt joltFacebook1 = arrAvailableJoltFacebook.get(i);

			for (int j = i - 1; j >= 0; j--) {

				Jolt joltFacebook2 = arrAvailableJoltFacebook.get(j);

				if (joltFacebook1.getFacebookId().equals(joltFacebook2.getFacebookId())) {
					arrAvailableJoltFacebook.remove(j);
				}
			}
		}

		if (arrAvailableJoltFacebook.size() > 100 && joltFacebook.size() > 0) {
			arrAvailableJoltFacebook.clear();
		}

		arrAvailableJoltFacebook.addAll(joltFacebook);
	}

	/**
	 * 
	 * @param lati
	 * @param longi
	 * @param arr
	 * @return
	 */
	public ArrayList<Jolt> get5NearestJolt(int lati, int longi, ArrayList<Jolt> arr) {
		ArrayList<Jolt> arrJolt = new ArrayList<Jolt>();
		int maxIndex = 0;
		int size = arr.size();
		for (int i = 0; i < size; i++) {
			if (arrJolt.size() < 5) {
				arrJolt.add(arr.get(i));
				if (getDistanceToLocation(arrJolt.get(maxIndex).getLatitudeE6(),
						arrJolt.get(maxIndex).getLongitudeE6(), lati, longi) < getDistanceToLocation(
						arr.get(i).getLatitudeE6(), arr.get(i).getLongitudeE6(), lati, longi)) {
					maxIndex = arrJolt.size() - 1;
				}
			} else {
				if (getDistanceToLocation(arrJolt.get(maxIndex).getLatitudeE6(),
						arrJolt.get(maxIndex).getLongitudeE6(), lati, longi) > getDistanceToLocation(
						arr.get(i).getLatitudeE6(), arr.get(i).getLongitudeE6(), lati, longi)) {
					arrJolt.remove(maxIndex);
					arrJolt.add(arr.get(i));
					for (int j = 0; j < arrJolt.size(); j++) {
						if (getDistanceToLocation(arrJolt.get(maxIndex).getLatitudeE6(), arrJolt
								.get(maxIndex).getLongitudeE6(), lati, longi) < getDistanceToLocation(
								arr.get(j).getLatitudeE6(), arr.get(j).getLongitudeE6(), lati,
								longi)) {
							maxIndex = j;
						}
					}
				}
			}
		}
		return arrJolt;
	}

	/**
	 * 
	 * @param latiS
	 * @param longS
	 * @param latiD
	 * @param longiD
	 * @return
	 */
	public long getDistanceToLocation(int latiS, int longS, int latiD, int longiD) {

		// int dx = Math.abs(lati - latitude);
		// int dy = Math.abs(longi - longitude);
		//
		// return (long) Math.sqrt(dx * dx + dy * dy);
		Location locationA = new Location("point A");

		locationA.setLatitude(latiS / 1E6);
		locationA.setLongitude(longS / 1E6);

		Location locationB = new Location("point B");

		locationB.setLatitude(latiD / 1E6);
		locationB.setLongitude(longiD / 1E6);

		float distance = locationA.distanceTo(locationB);

		return (long) (distance);
	}

	public void getNearestJolts(final int lati, final int longi) {
		arrNearestJolt.clear();
		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {
			@Override
			public void processIfResponseSuccess(String response) {

				if (response.length() > 4) {
					ParserUtility.getJoltObjectFromJson(response, arrNearestJolt,
							mainScreenActivity);
					if (arrNearestJolt != null && arrNearestJolt.size() > 0) {
						mainScreenActivity.mapView.viewFullPoint(arrNearestJolt, lati, longi,
								ModeScreen.JOLTS);
					}

				} else {
					mainScreenActivity.mapController.setZoom(17);
				}

				if (mainScreenActivity.mapView.getZoomLevel() < 17) {
					mainScreenActivity.mapController.setZoom(17);
				}
				mainScreenActivity.mapController.animateTo(new GeoPoint(lati, longi));
				mainScreenActivity.groupArrayJoltFilter();
				mainScreenActivity.closeProgressDialog();
			}

			@Override
			public void before() {
				mainScreenActivity.showProgressDialog(Language.pleaseWait);
			}
		});
		post.execute(WebServiceConfig.urlGetJoltsFromLocation(lati, longi, false, ""));
	}

	public void getSearchResult(final int lati, final int longi, String keyword) {
		mainScreenActivity.mode = ModeScreen.SEARCH;
		arrSearchResult.clear();

		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {
			@Override
			public void processIfResponseSuccess(String response) {
				mainScreenActivity.isStartSearch = false;
				mainScreenActivity.closeProgressDialog();
				Log.e(TAG, "search result reponse: " + response + " length: " + response.length());

				if (response.length() > 4) {
					ParserUtility.getJoltObjectFromJson(response, arrSearchResult,
							mainScreenActivity);
					if (arrSearchResult != null && arrSearchResult.size() > 0) {
						mainScreenActivity.mapView.viewFullPoint(arrSearchResult, lati, longi,
								ModeScreen.SEARCH);
						mainScreenActivity.displayJolts(arrSearchResult, Utility.CREATE);
						mainScreenActivity.countJoltNumberInsideScreen();
					}

				} else if (!isShowingDialog) {
					showDialogNoResult();
				}
			}

			@Override
			public void before() {
				mainScreenActivity.isStartSearch = true;
				mainScreenActivity.showProgressDialog(Language.pleaseWait);
			}
		});
		post.execute(WebServiceConfig.urlGetJoltsFromLocation(lati, longi, false, keyword));
	}

	private boolean isShowingDialog;

	private void showDialogNoResult() {
		isShowingDialog = true;
		AlertDialog.Builder builder = new AlertDialog.Builder(mainScreenActivity);
		builder.setMessage(Language.notifyNoResult).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mainScreenActivity.txtSearchBar.setText("");
						isShowingDialog = false;
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void postRequestNotification(Jolt jolt) {
		if (jolt.isPublicFacebook()) {
			String id = jolt.getId();
			GCMIntentService.idPostJolt = id;

			AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
					mainScreenActivity) {
				// @Override
				// public void processIfResponseSuccess(String response) {
				// Log.e("Notification POST", "response = " + response);
				// }
			});
			post.execute(WebServiceConfig.urlRequestNotification(id, mainScreenActivity.myUserName));
		}
	}

	public void postFavourite(String joltID, String loginUserid, boolean isLike) {
		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {
			// @Override
			// public void processIfResponseSuccess(String response) {
			// Log.e(TAG, "Favourite Reponse" + response);
			// }
		});
		post.execute(WebServiceConfig.urlPostFavourite(joltID, loginUserid, isLike));
	}

	public String encode(String value) {
		if (value != null) {
			return StringUtility.encode(value);
		} else {
			return "";
		}
	}

	/**
	 * Count number Of Jolt between top and bottm GeoPoint in screen
	 * 
	 * @param latTopLeft
	 * @param longTopLeft
	 * @param latBottomRight
	 * @param longBottomRight
	 * @return
	 */
	public int countNumberJoltInMapView(ArrayList<Jolt> arrAllJolt, int latTopLeft,
			int longTopLeft, int latBottomRight, int longBottomRight) {
		int result = 0;
		if (arrAllJolt != null && arrAllJolt.size() > 0) {
			for (Jolt jolt : arrAllJolt) {
				if (jolt.getLatitudeE6() <= latTopLeft && jolt.getLatitudeE6() >= latBottomRight
						&& jolt.getLongitudeE6() <= longBottomRight
						&& jolt.getLongitudeE6() >= longTopLeft)
					result++;
			}
		}
		return result;
	}

	public void exampleGet(String s) {
		AsyncHttpGet post = new AsyncHttpGet(mainScreenActivity, new AsyncHttpResponseProcess(
				mainScreenActivity) {
			@Override
			public void processIfResponseSuccess(String response) {
				mainScreenActivity.closeProgressDialog();
				Log.e(TAG, "reponse: " + response);
			}

			@Override
			public void before() {
				mainScreenActivity.showProgressDialog(Language.pleaseWait);
			}
		});
		post.execute(s);
	}
}
