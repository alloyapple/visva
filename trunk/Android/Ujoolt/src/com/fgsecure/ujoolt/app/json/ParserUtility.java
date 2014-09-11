package com.fgsecure.ujoolt.app.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.fgsecure.ujoolt.app.adapter.Rejolter;
import com.fgsecure.ujoolt.app.define.LoginType;
import com.fgsecure.ujoolt.app.facebook.UtilityFacebook;
import com.fgsecure.ujoolt.app.info.WebServiceConfig;
import com.fgsecure.ujoolt.app.network.AsyncHttpBase;
import com.fgsecure.ujoolt.app.network.AsyncHttpGet;
import com.fgsecure.ujoolt.app.network.AsyncHttpResponseListener;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.utillity.ConfigUtility;
import com.fgsecure.ujoolt.app.utillity.JoltHolder;

/**
 * ParserUtility supports to parser http response
 * 
 * @author Lemon
 */
public final class ParserUtility {
	private final static String TAG = "ParserUtility";
	public static final byte GET_JOLT = 0;
	public static final byte GET_SEARCH = 1;
	public static final byte GET_JOLT_FOLLOW = 2;

	// ========================= CORE FUNCTIONS ===========================

	/**
	 * Extract user information
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static String getStringValue(JSONObject obj, String key) {
		try {
			if (obj == null) {
				return "";
			} else {
				String result = obj.isNull(key) ? "" : obj.getString(key);
				return result;
			}
		} catch (JSONException e) {
			return "";
		}
	}

	/**
	 * Get long value
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	private static long getLongValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? 0L : obj.getLong(key);
		} catch (JSONException e) {
			return 0L;
		}
	}

	/**
	 * Get int value
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	private static int getIntValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? 0 : obj.getInt(key);
		} catch (JSONException e) {
			return 0;
		}
	}

	/**
	 * Get Double
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	private static double getDoubleValue(JSONObject obj, String key) {
		double d = 0.0;
		try {
			return obj.isNull(key) ? d : obj.getDouble(key);
		} catch (JSONException e) {
			return d;
		}
	}

	/**
	 * Get boolean
	 * 
	 * @param obj
	 * @param key
	 * @return
	 */
	public static boolean getBooleanValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? false : obj.getBoolean(key);
		} catch (JSONException e) {
			return false;
		}
	}

	public static UserInfo getUserInfo(String json) {
		if (!json.equalsIgnoreCase("false")) {
			UserInfo userUjoolt = new UserInfo();
			try {
				JSONObject entry = new JSONObject(json);
				userUjoolt.setUserName(getStringValue(entry, "nick"));
				userUjoolt.setUserId(getStringValue(entry, "userid"));
				userUjoolt.setLoginType(getStringValue(entry, "loginType"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return userUjoolt;
		} else {
			return null;
		}
	}

	public static void getJoltObjectFromJson(String json, ArrayList<Jolt> arrJolt,
			MainScreenActivity mainScreenActivity) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Jolt jolt = new Jolt(mainScreenActivity.joltHolder);
				setInfo(jolt, jsonObject, mainScreenActivity);
				arrJolt.add(jolt);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static void getJoltObjectsFromJS(String json, JoltHolder joltHolder, byte type,
			byte getType, MainScreenActivity mainScreenActivity) throws JSONException {

		JSONArray jsonArray = new JSONArray(json);

		int size = jsonArray.length();
		joltHolder.arrJolt.clear();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);

			getStringValue(jsonObject, WebServiceConfig.PARAM_NICK);

			Jolt jolt = new Jolt(joltHolder);
			if (type == GET_JOLT) {
				jolt.groupID = -1;
				jolt.setId(getStringValue(jsonObject, WebServiceConfig.PARAM_JOLT_ID));

				jolt.setNewCreate(true);
				int s = joltHolder.arrJolt.size();
				jolt.setPositionArr(s);
				setInfo(jolt, jsonObject, mainScreenActivity);

				if (jolt.isInstagram()) {
					joltHolder.arrJoltInstagram.add(jolt);
				} else if (jolt.isFacebook()) {
					// if (UtilityFacebook.mFacebook.getAccessToken() != null
					// &&
					// UtilityFacebook.mFacebook.getAccessToken().equalsIgnoreCase(""))
					// {
					joltHolder.arrJoltFacebook.add(jolt);
					// }
				} else {
					joltHolder.arrJolt.add(jolt);
				}

				if (getType == JoltHolder.GET_AFTER_POST) {
					joltHolder.postRequestNotification(jolt);
				}

			} else if (type == GET_SEARCH) {
				int s = joltHolder.arrSearchResult.size();
				jolt.setPositionArr(s);
				setInfo(jolt, jsonObject, mainScreenActivity);
				joltHolder.arrSearchResult.add(jolt);

			} else if (type == GET_JOLT_FOLLOW) {
				int s = joltHolder.arrJoltFollow.size();
				jolt.setPositionArr(s);
				setInfo(jolt, jsonObject, mainScreenActivity);
				joltHolder.arrJoltFollow.add(jolt);
			}
		}
	}

	public static void getFriendObjectsFromID(String json, JoltHolder joltHolder,
			MainScreenActivity mainScreenActivity) throws JSONException {

		JSONArray jsonArray = new JSONArray(json);
		int size = jsonArray.length();

		joltHolder.arrRejolt.clear();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);

			String nick = getStringValue(jsonObject, "nick");
			String rejolt_fbid = getStringValue(jsonObject, "rejolt_fbid");

			ArrayList<String> item = new ArrayList<String>();
			item.add(nick);
			item.add(rejolt_fbid);

			joltHolder.arrRejolt.add(item);
		}

		Log.e(TAG, "luong rejolt" + joltHolder.arrRejolt.toString());

		mainScreenActivity.setAdapterListView(joltHolder.arrRejolt);
	}

	public static void getRejolterFromID(String json, JoltHolder joltHolder,
			MainScreenActivity mainScreenActivity) throws JSONException {

		JSONArray jsonArray = new JSONArray(json);
		int size = jsonArray.length();

		mainScreenActivity.arrRejolter.clear();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);

			String nick = getStringValue(jsonObject, "nick");
			String rejoltFbId = getStringValue(jsonObject, "rejolt_fbid");

			Log.e(TAG, "rejolt nick: " + nick);
			Log.e(TAG, "rejolt fbid: " + rejoltFbId);

			mainScreenActivity.arrRejolter.add(new Rejolter(nick, rejoltFbId.equalsIgnoreCase("")));

		}

		mainScreenActivity.fillRejoltTable();
	}

	/**
	 * get jolt objects from instagram json
	 * 
	 * @param json
	 * @param joltHolder
	 * @param type
	 * @param getType
	 * @return
	 * @throws JSONException
	 */
	public static void getJoltObjectsFromInstagramJS(String json, JoltHolder joltHolder,
			MainScreenActivity mainScreenActivity) throws JSONException {

		if (json.contains(":200}")) {

			JSONObject bigJsonObject = new JSONObject(json);
			JSONArray jsonArray = new JSONArray(getStringValue(bigJsonObject, "data"));

			mainScreenActivity.numberDisConnectInstagram = 0;
			int size = jsonArray.length();

			joltHolder.arrJoltInstagram.clear();

			for (int i = 0; i < size; i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Jolt instagramJolt = new Jolt(joltHolder);
				instagramJolt.setNewCreate(true);
				int s = joltHolder.arrJoltInstagram.size();
				instagramJolt.setPositionArr(s);

				setInfoIntagramJolt(instagramJolt, jsonObject);

				if (isValidTime(instagramJolt.getDate())) {
					joltHolder.arrJoltInstagram.add(instagramJolt);
				}
			}

			Log.i("arrJoltInstagram size", " = " + joltHolder.arrJoltInstagram.size());
			Log.e("arrJoltInstagram size", " = " + joltHolder.arrJoltInstagram.size());
			Log.e("arrJoltInstagram size", " = " + joltHolder.arrJoltInstagram.size());
			Log.e("arrJoltInstagram size", " = " + joltHolder.arrJoltInstagram.size());

		} else {
			joltHolder.arrJoltInstagram.clear();

			mainScreenActivity.numberDisConnectInstagram++;

			if (mainScreenActivity.numberDisConnectInstagram > 2)
				mainScreenActivity.numberDisConnectInstagram = 0;
		}
	}

	public static boolean isValidTime(long time) {

		long lifeTime = (System.currentTimeMillis() - 1000 * time);
		Log.i("Life time", " = " + lifeTime / 3600000 + "h");

		if (lifeTime <= 14400000) {
			Log.i(": isShow:", "True");
			return true;
		} else {
			Log.i(": isShow:", "False");
			return false;
		}
	}

	public static void getJoltObjectsFromFacebookJson(String json, JoltHolder joltHolder)
			throws JSONException {
		JSONObject bigJsonObject = new JSONObject(json);
		JSONArray jsonArray = new JSONArray(getStringValue(bigJsonObject, "data"));

		int size = jsonArray.length();
		Log.e(TAG, "info fb json size: " + size);

		joltHolder.arrJoltFacebook.clear();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Jolt facebookJolt = new Jolt(joltHolder);
			facebookJolt.setNewCreate(true);
			// facebookJolt.setPositionArr(s);

			setInfoFacebookJolt(facebookJolt, jsonObject, joltHolder);
		}
	}

	public static void getJoltObjectsOfMeFromFacebook(String json, JoltHolder joltHolder)
			throws JSONException {
		JSONObject bigJsonObject = new JSONObject(json);
		JSONArray jsonArray = new JSONArray(getStringValue(bigJsonObject, "data"));

		int size = jsonArray.length();
		Log.e(TAG, "info fb json size: " + size);

		joltHolder.arrJoltFacebookMe.clear();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Jolt facebookJoltMe = new Jolt(joltHolder);
			facebookJoltMe.setNewCreate(true);
			// facebookJolt.setPositionArr(s);

			setInfoFacebookJoltOfMe(facebookJoltMe, jsonObject, joltHolder);
		}
	}

	public static void getJoltObjectsOfMyEventFromFacebook(String json, JoltHolder joltHolder)
			throws JSONException {
		JSONObject bigJsonObject = new JSONObject(json);
		JSONArray jsonArray = new JSONArray(getStringValue(bigJsonObject, "data"));

		int size = jsonArray.length();
		Log.e(TAG, "info fb json size: " + size);

		joltHolder.arrJoltFacebookMyEvent.clear();

		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Jolt facebookJoltMyEvent = new Jolt(joltHolder);
			facebookJoltMyEvent.setNewCreate(true);
			// facebookJolt.setPositionArr(s);

			setInfoFacebookJoltOfMyEvent(facebookJoltMyEvent, jsonObject, joltHolder);
		}
	}

	public static void getUserSync(String json) {
		try {
			JSONObject jsonSync = new JSONObject(json);
			UserSync.IdUjoolt = getStringValue(jsonSync, "ujooltId");
			UserSync.IdFacebook = getStringValue(jsonSync, "facebookId");
			UserSync.IdTwitter = getStringValue(jsonSync, "twitterId");
			// Log.e(TAG, "sync user ujoolt: "+UserSync.IdUjoolt);
			// Log.e(TAG, "sync user facebook: "+UserSync.IdFacebook);
			// Log.e(TAG, "sync user twitter: "+UserSync.IdTwitter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param jolt
	 * @param jsonObject
	 */
	private static void setInfoIntagramJolt(Jolt jolt, JSONObject jsonObject) {

		String lati = "", longi = "", text = "", nick = "", imageURL = "", instagramID = "", createdTime = "";
		int realLati = 0, realLongi = 0;
		try {
			JSONObject jsonObjectUser = new JSONObject(getStringValue(jsonObject, "user"));
			JSONObject jsonObjectLocation = new JSONObject(getStringValue(jsonObject, "location"));
			JSONObject jsonObjectImages = new JSONObject(getStringValue(jsonObject, "images"));
			JSONObject jsonObjectLowImage = new JSONObject(getStringValue(jsonObjectImages,
					"low_resolution"));

			JSONObject jsonObjectCaption = null;

			if (!"".endsWith(getStringValue(jsonObject, "caption"))) {
				jsonObjectCaption = new JSONObject(getStringValue(jsonObject, "caption"));
			}
			nick = getStringValue(jsonObjectUser, WebServiceConfig.PARAM_USER_NAME);
			lati = getStringValue(jsonObjectLocation, "latitude");
			instagramID = getStringValue(jsonObject, "id");
			longi = getStringValue(jsonObjectLocation, "longitude");
			realLati = (int) (Double.parseDouble(lati) * 1E6);
			realLongi = (int) (Double.parseDouble(longi) * 1E6);
			imageURL = getStringValue(jsonObjectLowImage, "url");
			createdTime = getStringValue(jsonObject, "created_time");

			if (jsonObjectCaption != null) {
				text = getStringValue(jsonObjectCaption, "text");
			}

		} catch (Exception e) {
		}

		jolt.setId("");
		jolt.setDeviceID("");
		jolt.setNick(nick);
		jolt.isGrouped = false;
		jolt.setDate(Long.parseLong(createdTime));

		jolt.setLatitudeE6(realLati);
		jolt.setLongitudeE6(realLongi);
		jolt.setPhotoURL(imageURL);
		jolt.setNumberRejolt(0);
		jolt.setText(text);
		jolt.setDistance(0);
		jolt.setRadius(500);
		jolt.setLifeTime(4.0);
		jolt.setLoginType(LoginType.NONE);
		jolt.setLoginUserid("instagram_jolt_fruity123");
		jolt.setInstagramId(instagramID);
		jolt.setFacebookId("");
	}

	private static void setInfoFacebookJolt(final Jolt jolt, JSONObject jsonObject,
			final JoltHolder joltHolder) {
		final MainScreenActivity mainScreenActivity = joltHolder.mainScreenActivity;
		JSONObject jsonObjectFrom = null;
		JSONObject jsonObjectPlace = null;
		JSONObject jsonObjectLocation = null;
		try {
			jsonObjectFrom = new JSONObject(getStringValue(jsonObject, "from"));
			jsonObjectPlace = new JSONObject(getStringValue(jsonObject, "place"));
			jsonObjectLocation = new JSONObject(getStringValue(jsonObjectPlace, "location"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		final String facebookId = getStringValue(jsonObject, "id");
		final String type = getStringValue(jsonObject, "type");
		final String nick = getStringValue(jsonObjectFrom, "name");

		// final String address = getStringValue(jsonObjectPlace, "name");
		final String lati = getStringValue(jsonObjectLocation, "latitude");
		final String longi = getStringValue(jsonObjectLocation, "longitude");
		final int realLati = (int) (Double.parseDouble(lati) * 1E6);
		final int realLongi = (int) (Double.parseDouble(longi) * 1E6);

		final long createdTime = getMillisFromFacebookTime(getStringValue(jsonObject,
				"created_time"));

		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void before() {
					}

					@Override
					public void after(int statusCode, HttpResponse response) {

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
									try {
										JSONObject jsonObjectEventFacebook = new JSONObject(json);
										String imageURL = "";
										String text = "";
										if (type.equalsIgnoreCase("photo")) {
											JSONArray jsonArrayImage = new JSONArray(
													getStringValue(jsonObjectEventFacebook,
															"images"));
											JSONObject jsonObjectImage360x480 = jsonArrayImage
													.getJSONObject(3);
											imageURL = getStringValue(jsonObjectImage360x480,
													"source");
											text = getStringValue(jsonObjectEventFacebook, "name");
										} else {
											text = getStringValue(jsonObjectEventFacebook,
													"message");
										}

										jolt.setDeviceID("");
										jolt.setNick(nick);
										jolt.isGrouped = false;

										jolt.setDate(createdTime / 1000);
										// jolt.setDate(formatCreatTimeFacebook(createdTime));

										jolt.setLatitudeE6(realLati);
										jolt.setLongitudeE6(realLongi);
										jolt.setPhotoURL(imageURL);
										jolt.setNumberRejolt(0);
										jolt.setText(text);
										jolt.setDistance(0);
										jolt.setRadius(500);
										jolt.setLifeTime(4.0);
										jolt.setLoginType(LoginType.FACEBOOK);
										jolt.setLoginUserid("facebook_jolt_fruity123");
										jolt.setId("");
										jolt.setInstagramId("");
										jolt.setFacebookId(facebookId);

										joltHolder.arrJoltFacebook.add(jolt);
										joltHolder.isDisPlayFacebook = true;
										joltHolder.handle_disPlayFacebook
												.post(joltHolder.runnable_disPlayFacebook);

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

		asyncHttpGet.execute(WebServiceConfig.URL_FACEBOOK_GRAPHIC + facebookId + "&access_token="
				+ UtilityFacebook.mFacebook.getAccessToken());
	}

	private static void setInfoFacebookJoltOfMe(final Jolt jolt, JSONObject jsonObject,
			final JoltHolder joltHolder) {
		final MainScreenActivity mainScreenActivity = joltHolder.mainScreenActivity;
		JSONObject jsonObjectFrom = null;
		JSONObject jsonObjectPlace = null;
		JSONObject jsonObjectLocation = null;
		try {
			jsonObjectFrom = new JSONObject(getStringValue(jsonObject, "from"));
			jsonObjectPlace = new JSONObject(getStringValue(jsonObject, "place"));
			jsonObjectLocation = new JSONObject(getStringValue(jsonObjectPlace, "location"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		final String facebookId = getStringValue(jsonObject, "id");
		final String type = getStringValue(jsonObject, "type");
		final String nick = getStringValue(jsonObjectFrom, "name");

		// final String address = getStringValue(jsonObjectPlace, "name");
		final String lati = getStringValue(jsonObjectLocation, "latitude");
		final String longi = getStringValue(jsonObjectLocation, "longitude");
		final int realLati = (int) (Double.parseDouble(lati) * 1E6);
		final int realLongi = (int) (Double.parseDouble(longi) * 1E6);

		final long createdTime = getMillisFromFacebookTime(getStringValue(jsonObject,
				"created_time"));

		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void before() {
						Log.e(TAG, "z respone ");
					}

					@Override
					public void after(int statusCode, HttpResponse response) {

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
									try {
										JSONObject jsonObjectEventFacebook = new JSONObject(json);
										String imageURL = "";
										String text = "";
										if (type.equalsIgnoreCase("photo")) {
											JSONArray jsonArrayImage = new JSONArray(
													getStringValue(jsonObjectEventFacebook,
															"images"));
											JSONObject jsonObjectImage360x480 = jsonArrayImage
													.getJSONObject(3);
											imageURL = getStringValue(jsonObjectImage360x480,
													"source");
											text = getStringValue(jsonObjectEventFacebook, "name");
										} else {
											text = getStringValue(jsonObjectEventFacebook,
													"message");
										}

										jolt.setDeviceID("");
										jolt.setNick(nick);
										jolt.isGrouped = false;

										jolt.setDate(createdTime / 1000);
										// jolt.setDate(formatCreatTimeFacebook(createdTime));

										jolt.setLatitudeE6(realLati);
										jolt.setLongitudeE6(realLongi);
										jolt.setPhotoURL(imageURL);
										jolt.setNumberRejolt(0);
										jolt.setText(text);
										jolt.setDistance(0);
										jolt.setRadius(500);
										jolt.setLifeTime(4.0);
										// jolt.setAddress(address);
										jolt.setLoginType(LoginType.FACEBOOK);
										jolt.setLoginUserid("facebook_jolt_fruity123");
										jolt.setId("");
										jolt.setInstagramId("");
										jolt.setFacebookId(facebookId);

										joltHolder.arrJoltFacebookMe.add(jolt);
										joltHolder.isDisPlayFacebook = true;
										joltHolder.handle_disPlayFacebook
												.post(joltHolder.runnable_disPlayFacebook);

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

		asyncHttpGet.execute(WebServiceConfig.URL_FACEBOOK_GRAPHIC + facebookId + "&access_token="
				+ UtilityFacebook.mFacebook.getAccessToken());
	}

	private static void setInfoFacebookJoltOfMyEvent(final Jolt jolt, JSONObject jsonObject,
			final JoltHolder joltHolder) {
		final MainScreenActivity mainScreenActivity = joltHolder.mainScreenActivity;
		// JSONObject jsonObjectFrom = null;
		// JSONObject jsonObjectPlace = null;
		// JSONObject jsonObjectLocation = null;
		// try {
		// jsonObjectFrom = new JSONObject(getStringValue(jsonObject, "from"));
		// jsonObjectPlace = new JSONObject(getStringValue(jsonObject,
		// "place"));
		// jsonObjectLocation = new JSONObject(getStringValue(jsonObjectPlace,
		// "location"));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// final String facebookId = getStringValue(jsonObject, "id");
		// final String type = getStringValue(jsonObject, "type");
		// final String nick = getStringValue(jsonObjectFrom, "name");

		// final String address = getStringValue(jsonObjectPlace, "name");
		// final String lati = getStringValue(jsonObjectLocation, "latitude");
		// final String longi = getStringValue(jsonObjectLocation, "longitude");
		// final int realLati = (int) (Double.parseDouble(lati) * 1E6);
		// final int realLongi = (int) (Double.parseDouble(longi) * 1E6);
		final String eventId = getStringValue(jsonObject, "id");

		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
				new AsyncHttpResponseListener() {

					@Override
					public void before() {
					}

					@Override
					public void after(int statusCode, HttpResponse response) {

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
									try {
										Log.e(TAG, "fb event: " + json);
										JSONObject jsonObjectEventFacebook = new JSONObject(json);
										setInfoFacebookJoltOfMyVenue(jolt, eventId,
												jsonObjectEventFacebook, joltHolder);
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

		asyncHttpGet.execute(WebServiceConfig.URL_FACEBOOK_GRAPHIC + eventId + "&access_token="
				+ UtilityFacebook.mFacebook.getAccessToken());
	}

	private static void setInfoFacebookJoltOfMyVenue(final Jolt jolt, final String facebookId,
			JSONObject jsonObject, final JoltHolder joltHolder) {
		final MainScreenActivity mainScreenActivity = joltHolder.mainScreenActivity;

		JSONObject jsonObjectVenue = null;
		JSONObject jsonObjectOwner = null;

		try {
			Log.d(TAG, "fb venue: " + getStringValue(jsonObject, "venue"));
			jsonObjectVenue = new JSONObject(getStringValue(jsonObject, "venue"));
			jsonObjectOwner = new JSONObject(getStringValue(jsonObject, "owner"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		final String venueId = getStringValue(jsonObjectVenue, "id");
		final String name = getStringValue(jsonObject, "name");
		final String description = getStringValue(jsonObject, "description");
		final String nick = getStringValue(jsonObjectOwner, "name");
		final String loginUserId = getStringValue(jsonObjectOwner, "id");
		// final String address = getStringValue(jsonObject, "location");

		final String startTime = getStringValue(jsonObject, "start_time");
		// final String endTime = getStringValue(jsonObject, "end_time");
		final long longStartTime = getMillisFromFacebookTime(startTime);
		// final long longEndTime = getMillisFromFacebookTime(endTime);
		// Log.e(TAG, "event fb start: " + startTime);
		// Log.e(TAG, "event fb end: " + endTime);
		// Log.e(TAG, "event fb time: " + (longEndTime - longStartTime));

		if (startTime.length() < 12) {
			jolt.setId("jolt_event_all_day_facebook");
			jolt.setLifeTime(24.0);
			jolt.setDate(longStartTime / 1000);
		} else {
			jolt.setId("");
			jolt.setLifeTime(4.0);
			jolt.setDate(longStartTime / 1000 - 7200);
		}

		if (jolt.getDate() < ConfigUtility.getCurTimeStamp()) {

			AsyncHttpGet asyncHttpGet = new AsyncHttpGet(mainScreenActivity,
					new AsyncHttpResponseListener() {
						@Override
						public void before() {
						}

						@Override
						public void after(int statusCode, HttpResponse response) {

							if (statusCode == AsyncHttpBase.NETWORK_STATUS_OK) {
								if (response != null) {
									String json = null;

									try {
										json = EntityUtils.toString(response.getEntity(),
												HTTP.UTF_8);
									} catch (org.apache.http.ParseException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}

									if (json != null) {
										try {
											JSONObject jsonObjectVenueFacebook = new JSONObject(
													json);
											JSONObject jsonObjectLocation = new JSONObject(
													getStringValue(jsonObjectVenueFacebook,
															"location"));

											final String lati = getStringValue(jsonObjectLocation,
													"latitude");
											final String longi = getStringValue(jsonObjectLocation,
													"longitude");
											final int realLati = (int) (Double.parseDouble(lati) * 1E6);
											final int realLongi = (int) (Double.parseDouble(longi) * 1E6);

											String imageURL = "";
											String text = name + "\n" + description;

											jolt.setDeviceID("");
											jolt.setNick(nick);
											jolt.isGrouped = false;

											// jolt.setDate(longStartTime /
											// 1000);
											// jolt.setDateSource(longStartTime
											// /
											// 1000 - 7200);
											jolt.setLatitudeE6(realLati);
											jolt.setLongitudeE6(realLongi);
											jolt.setPhotoURL(imageURL);
											jolt.setNumberRejolt(0);
											jolt.setText(text);
											jolt.setDistance(0);
											jolt.setRadius(500);

											// jolt.setAddress(address);
											jolt.setLoginType(LoginType.FACEBOOK);
											jolt.setLoginUserid(loginUserId);

											jolt.setInstagramId("");
											jolt.setFacebookId(facebookId);

											joltHolder.arrJoltFacebookMyEvent.add(jolt);
											joltHolder.isDisPlayFacebook = true;
											joltHolder.handle_disPlayFacebook
													.post(joltHolder.runnable_disPlayFacebook);

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

			asyncHttpGet.execute(WebServiceConfig.URL_FACEBOOK_GRAPHIC + venueId + "&access_token="
					+ UtilityFacebook.mFacebook.getAccessToken());
		}
	}

	// private static long getMillisFromFacebookTime(String facebookTime) {
	// DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'+'SSSS",
	// Locale.getDefault());
	// Date date = null;
	// try {
	// date = df.parse(facebookTime);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// TimeZone tz = TimeZone.getDefault();
	// String timeZoneCur = tz.getDisplayName(false,
	// TimeZone.SHORT).substring(3);
	//
	// int h = Integer.parseInt(timeZoneCur.substring(1, 3));
	// int m = Integer.parseInt(timeZoneCur.substring(4));
	// Log.e("", "tz h: " + h);
	// Log.e("", "tz m: " + m);
	// int timeBonus = h * 3600000 + m * 60000;
	// Log.e("", "tz: " + timeBonus);
	//
	// if (timeZoneCur.charAt(0) == '+') {
	// return date.getTime() + timeBonus;
	// } else {
	// return date.getTime() - timeBonus;
	// }
	// }

	private static long getMillisFromFacebookTime(String facebookTime) {
		Log.e(TAG, "zz fb time event: " + facebookTime + " leng:" + facebookTime.length());
		DateFormat df;
		Date date = null;
		if (facebookTime.length() < 12) {
			df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			try {
				date = df.parse(facebookTime);
				return date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}

		} else if (facebookTime.length() < 22) {
			df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss", Locale.getDefault());
			try {
				date = df.parse(facebookTime);
				return date.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}

		} else {
			df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'+'SSSS", Locale.getDefault());
			try {
				date = df.parse(facebookTime);
				int timeBonus = getTimeBonus();
				if (timeBonus == getMiliSecondFromTimeZone(facebookTime)) {
					return date.getTime();
				} else {
					return date.getTime() + timeBonus;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	private static int getTimeBonus() {
		Calendar cal = Calendar.getInstance();
		TimeZone tz = cal.getTimeZone();
		String s = tz.toString();

		for (int i = 0; i < s.length() - 11; i++) {
			if (s.substring(i, i + 11).equalsIgnoreCase("mRawOffSet=")) {
				for (int j = i + 11; j < i + 25; j++) {
					if (s.charAt(j) == ',') {
						return Integer.parseInt(s.substring(i + 11, j));
					}
				}
			}
		}
		return 0;
	}

	private static int getMiliSecondFromTimeZone(String time) {
		if (time.length() < 22) {
			return -1;
		} else {
			int h = Integer.parseInt(time.substring(20, 22));
			int m = Integer.parseInt(time.substring(22));
			int timeBonus = h * 3600000 + m * 60000;
			if (time.charAt(19) == '+') {
				return timeBonus;
			} else {
				return -timeBonus;
			}
		}
	}

	public static void setInfo(Jolt jolt, JSONObject jsonObject,
			MainScreenActivity mainScreenActivity) {
		jolt.setId(getStringValue(jsonObject, WebServiceConfig.PARAM_JOLT_ID));
		jolt.setDeviceID(getStringValue(jsonObject, WebServiceConfig.PARAM_DEVICE_ID));
		jolt.setNick(getStringValue(jsonObject, WebServiceConfig.PARAM_NICK));
		// jolt.setDateSource(getLongValue(jsonObject,
		// WebServiceConfig.PARAM_DATE_SRC));
		jolt.setDate(getLongValue(jsonObject, WebServiceConfig.PARAM_DATE));
		jolt.setLatitudeE6(getIntValue(jsonObject, WebServiceConfig.PARAM_LATITUE));
		jolt.setLongitudeE6(getIntValue(jsonObject, WebServiceConfig.PARAM_LONGITUDE));
		jolt.setPhotoURL(getStringValue(jsonObject, WebServiceConfig.PARAM_PHOTO_URL));
		jolt.setVideoURL(getStringValue(jsonObject, WebServiceConfig.PARAM_VIDEO_URL));
		jolt.setNumberRejolt(getIntValue(jsonObject, WebServiceConfig.PARAM_NB_REJOLT));
		jolt.setText(getStringValue(jsonObject, WebServiceConfig.PARAM_TEXT));
		jolt.setDistance(getIntValue(jsonObject, WebServiceConfig.PARAM_DISTANCE));
		jolt.setRadius(getDoubleValue(jsonObject, WebServiceConfig.PARAM_RADIUS));
		jolt.setLifeTime(getDoubleValue(jsonObject, WebServiceConfig.PARAM_LIFETIME));
		// jolt.setAddress(getStringValue(jsonObject,
		// WebServiceConfig.PARAM_ADDRESS));
		jolt.setLoginType(getStringValue(jsonObject, WebServiceConfig.PARAM_LOGIN_TYPE));
		jolt.setLoginUserid(getStringValue(jsonObject, WebServiceConfig.PARAM_LOGIN_USERID));
		jolt.setLike(mainScreenActivity.arrFavouriteId);
		jolt.setTop(getBooleanValue(jsonObject, WebServiceConfig.PARAM_TOP));
		jolt.setPublicFacebook(getBooleanValue(jsonObject, WebServiceConfig.PARAM_PUBLIC));

		jolt.setInstagramId(getStringValue(jsonObject, WebServiceConfig.PARAM_INSTAGRAM_ID));
		jolt.setFacebookId(getStringValue(jsonObject, WebServiceConfig.PARAM_FACEBOOK_ID));

		String jsonRejoltFacebookId = getStringValue(jsonObject, "rejolt_fbid");
		if (jolt.isFacebook()) {
			jolt.setNumberFacebookRejolt(0);
		} else {
			String[] arrayRejoltFacebookId = jsonRejoltFacebookId.split(",");
			for (int i = 0; i < arrayRejoltFacebookId.length; i++) {
				jolt.addArrJoltFollow(arrayRejoltFacebookId[i]);
			}
			jolt.setNumberFacebookRejolt(mainScreenActivity.arrFriendsFacebook);
		}
		jolt.showInfo();
	}

	public static void setInfoJoltOnlyText(Jolt jolt, JSONObject jsonObject,
			MainScreenActivity mainScreenActivity) {
		jolt.setId(getStringValue(jsonObject, WebServiceConfig.PARAM_JOLT_ID));
		jolt.setDeviceID(getStringValue(jsonObject, WebServiceConfig.PARAM_DEVICE_ID));
		jolt.setNick(getStringValue(jsonObject, WebServiceConfig.PARAM_NICK));
		// jolt.setNick_src(getStringValue(jsonObject,
		// WebServiceConfig.PARAM_NICK_SRC));
		// jolt.setDateSource(getLongValue(jsonObject,
		// WebServiceConfig.PARAM_DATE_SRC));
		jolt.setDate(getLongValue(jsonObject, WebServiceConfig.PARAM_DATE));
		jolt.setLatitudeE6(getIntValue(jsonObject, WebServiceConfig.PARAM_LATITUE));
		jolt.setLongitudeE6(getIntValue(jsonObject, WebServiceConfig.PARAM_LONGITUDE));

		jolt.setPhotoURL(null);

		jolt.setPhotoBitmap(null);
		jolt.setVideoURL(getStringValue(jsonObject, WebServiceConfig.PARAM_VIDEO_URL));

		jolt.setInstagramId(getStringValue(jsonObject, WebServiceConfig.PARAM_INSTAGRAM_ID));
		jolt.setFacebookId(getStringValue(jsonObject, WebServiceConfig.PARAM_FACEBOOK_ID));

		jolt.setNumberRejolt(getIntValue(jsonObject, WebServiceConfig.PARAM_NB_REJOLT));
		jolt.setText(getStringValue(jsonObject, WebServiceConfig.PARAM_TEXT));
		jolt.setDistance(getIntValue(jsonObject, WebServiceConfig.PARAM_DISTANCE));
		jolt.setRadius(getDoubleValue(jsonObject, WebServiceConfig.PARAM_RADIUS));
		jolt.setLifeTime(getDoubleValue(jsonObject, WebServiceConfig.PARAM_LIFETIME));
		// jolt.setAddress(getStringValue(jsonObject,
		// WebServiceConfig.PARAM_ADDRESS));
		jolt.setLoginType(getStringValue(jsonObject, WebServiceConfig.PARAM_LOGIN_TYPE));
		jolt.setLoginUserid(getStringValue(jsonObject, WebServiceConfig.PARAM_LOGIN_USERID));

		String jsonRejoltFacebookId = getStringValue(jsonObject, "rejolt_fbid");
		if (jolt.isFacebook()) {
			jolt.setNumberFacebookRejolt(0);
		} else {
			String[] arrayRejoltFacebookId = jsonRejoltFacebookId.split(",");
			for (int i = 0; i < arrayRejoltFacebookId.length; i++) {
				jolt.addArrJoltFollow(arrayRejoltFacebookId[i]);
			}
			jolt.setNumberFacebookRejolt(mainScreenActivity.arrFriendsFacebook);
		}
	}
}
