package vn.com.shoppie.activity;

import vn.com.shoppie.database.sobject.User;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class ActivityShoppie extends VisvaAbstractActivity implements
		LocationListener {

	public static LatLng myLocation = new LatLng(21.004269, 105.845910); // default
																			// :
																			// bkhn
	public static User myUser = new User(0, "", "", "", "", "", "");
	private LocationManager mLocaMng;

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		ActivityShoppie.myLocation = new LatLng(location.getLatitude(), location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		// LOCATION
		mLocaMng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		try {
			mLocaMng.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
					0, 0, this);
			mLocaMng.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					this);
			mLocaMng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					0, 0, this);
		} catch (IllegalArgumentException e) {

		}

		Location tmpLoc = mLocaMng
				.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		if (tmpLoc == null) {
			tmpLoc = mLocaMng
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (tmpLoc == null) {
				tmpLoc = mLocaMng
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}
		if (tmpLoc != null) {
			myLocation = new LatLng(tmpLoc.getLatitude(), tmpLoc.getLongitude());
		}
	}

}
