package com.visva.android.visvasdklibrary.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.visva.android.visvasdklibrary.location.LocationRequestManager.ILocationClientCallback;

public class LocationClientConnectHelper implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
    private static final String     TAG_LOCATION = "LocationHelper";
    private Context                 mContext;
    private LocationClient          mLocationClient;
    private ILocationClientCallback mCallback;

    public LocationClientConnectHelper(Context context) {
        mContext = context;
    }

    public LocationClient getLocationClient() {
        Log.d(TAG_LOCATION, "getLocationClient");
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(mContext, this, this);
        }
        return mLocationClient;
    }

    public void startConnection(ILocationClientCallback callback) {
        Log.d(TAG_LOCATION, "startConnectionClient");
        getLocationClient().connect();
        mCallback = callback;
    }

    public void stopLocationClient() {
        if (mLocationClient != null) {
            mLocationClient = null;
        }
        mCallback = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.d(TAG_LOCATION, "onConnected");
        Location loc = null;
        loc = mLocationClient.getLastLocation();
        if (loc != null) {
            Log.d(TAG_LOCATION, "loc != null, return for callback");
            mCallback.onLocationConnectionChanged(loc);
        }
    }

    @Override
    public void onDisconnected() {

    }

}
