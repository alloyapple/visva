package com.fgsecure.ujoolt.app.utillity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class GoogleLocation {

	private int lat;
	private int lng;
	private int distanceLat;
	private int distanceLng;

	public GoogleLocation(String address) {

		Vector<GeoPoint> vecGeoPoints = VectorLatLng(address);

		lat = vecGeoPoints.get(0).getLatitudeE6();
		lng = vecGeoPoints.get(0).getLongitudeE6();

		distanceLat = Math.abs(vecGeoPoints.get(1).getLatitudeE6()
				- vecGeoPoints.get(2).getLatitudeE6());

		distanceLng = Math.abs(vecGeoPoints.get(1).getLongitudeE6()
				- vecGeoPoints.get(1).getLongitudeE6());
	}

	public int getLat() {
		return lat;
	}

	public int getLng() {
		return lng;
	}

	public int getDistance_lat() {
		return distanceLat;
	}

	public int getDistance_lng() {
		return distanceLng;
	}

	public Vector<GeoPoint> VectorLatLng(String address) {

		double lng_center  = 0;
		double lat_center  = 0;

		double lat_northeast =0;
		double lng_northeast = 0;

		double lat_southwest = 0;
		double lng_southwest = 0;

		JSONObject jsonObject = this.getLocationInfo(address);

		try {
			lng_center = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

			lat_center = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location").getDouble("lat");

			lat_northeast = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("viewport").getJSONObject("northeast")
					.getDouble("lat");

			lng_northeast = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("viewport").getJSONObject("northeast")
					.getDouble("lat");

			lat_southwest = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("viewport").getJSONObject("southwest")
					.getDouble("lat");

			lng_southwest = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("viewport").getJSONObject("southwest")
					.getDouble("lat");

		} catch (Exception e) {
			e.printStackTrace();

		}

		GeoPoint gPoint_center = new GeoPoint((int) (lat_center * 1E6), (int) (lng_center * 1E6));
		GeoPoint gPoint_northeast = new GeoPoint((int) (lat_northeast * 1E6),
				(int) (lng_northeast * 1E6));
		GeoPoint gPoint_southwest = new GeoPoint((int) (lat_southwest * 1E6),
				(int) (lng_southwest * 1E6));

		Vector<GeoPoint> gPointVector = new Vector<GeoPoint>();

		gPointVector.add(gPoint_center);
		gPointVector.add(gPoint_northeast);
		gPointVector.add(gPoint_southwest);

		return gPointVector;
	}

	public JSONObject getLocationInfo(String address) {

		StringBuilder stringBuilder = new StringBuilder();
		try {

			address = address.replaceAll(" ", "%20");
			String URL_Json_Google_Host = "http://maps.google.com/maps/api/geocode/json?address="
					+ address + "&sensor=false";

			HttpPost httppost = new HttpPost(URL_Json_Google_Host);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			stringBuilder = new StringBuilder();

			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}
}