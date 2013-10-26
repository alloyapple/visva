package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;

@SuppressWarnings("serial")
public class Store extends ShoppieObject {
	public static final int CATE_THOI_TRANG=1;
	public static final int CATE_LAM_DEP=2;
	public static final int CATE_AN_UONG=3;
	public static final int CATE_GIAI_TRI=4;
	
	public static final String CLASS_UNIQUE = "Store";
	public static final int NUM_FIELDS = 9;

	public String storeName = "";
	public String merchId = "";
	public String storeId = "";
	public String storeCode = "";
	public String storeAddress = "";
	public String latitude = "";
	public String longtitude = "";
	public String thumbnail = "";
	public String merchCatId = "";
	
	public String distanceInM = "";

	public Store(int id, String[] values) {
		super(id, values);
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.storeName = values[0];
		this.merchId = values[1];
		this.storeId = values[2];
		this.storeCode = values[3];
		this.storeAddress = values[4];
		this.latitude = values[5];
		this.longtitude = values[6];
		this.thumbnail = values[7];
		this.merchCatId = values[8];
	}

	public LatLng getLocation() {
		try {
			LatLng _geo = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));
			return _geo;
		} catch (NumberFormatException e) {
			Log.e("number parse error", latitude + "" + longtitude);
			return new LatLng(0, 0);
		} catch (NullPointerException e){
			Log.e("number null error", latitude + "" + longtitude);
			return new LatLng(0, 0);
		}
	}

	public String getDistance() {
		if (distanceInM.equals("")) {
			return "distance calculating...";
		} else {
			float distance = Float.valueOf(distanceInM);
			if (distance > 1000) {
				distance=distance/1000;
				return String.format("%.2f", distance) + " km";
			} else {
				return String.format("%.2f", distance) + " m";
			}
		}
	}

	public Drawable getDrawable(Context context) {
		Bitmap bmp = getBitmap(context, thumbnail);
		if (bmp != null) {
			return new BitmapDrawable(context.getResources(), bmp);
		}
		return null;
	}

	@Override
	public String toString() {
		return "Store [merchId=" + merchId + ", storeId=" + storeId + ", storeCode=" + storeCode + ", storeName=" + storeName + ", storeAddress=" + storeAddress + ", latitude=" + latitude + ", longtitude=" + longtitude + ", thumbnail=" + thumbnail + ", merchCatId=" + merchCatId + "]";
	}

	public interface StoreColumn extends ShoppieObjectColumn {
		public static final String TABLE_NAME = "Store";
		public static final String MERCH_ID = "merchId";
		public static final String STORE_ID = "storeId";
		public static final String STORE_CODE = "storeCode";
		public static final String STORE_NAME = "storeName";
		public static final String STORE_ADDRESS = "storeAddress";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longtitude";
		public static final String THUMBNAIL = "thumbnail";
		public static final String MERCH_CAT_ID = "merchCatId";
	}

	public float getDistanceInMiles(GeoPoint p1, GeoPoint p2) {
		double lat1 = ((double) p1.getLatitudeE6()) / 1e6;
		double lng1 = ((double) p1.getLongitudeE6()) / 1e6;
		double lat2 = ((double) p2.getLatitudeE6()) / 1e6;
		double lng2 = ((double) p2.getLongitudeE6()) / 1e6;
		float[] dist = new float[1];
		Location.distanceBetween(lat1, lng1, lat2, lng2, dist);
		return dist[0] * 0.000621371192f;
	}

}
