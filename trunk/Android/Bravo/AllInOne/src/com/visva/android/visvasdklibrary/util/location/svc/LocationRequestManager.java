package com.visva.android.visvasdklibrary.util.location.svc;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.visva.android.visvasdklibrary.provider.LocationProvider;
import com.visva.android.visvasdklibrary.util.AllInOneConstant;

public class LocationRequestManager {
    private static final String TAG = "LocationRequestMananager->nobita";
    private static final int MIN_TIME = 1000 * 30; //30 seconds
    private static final int TWO_MINUTES = 1000 * 60 * 2; //recent location
    private Context mContext;
    private LocationManager mLocationManager;
    private Location mLocation;

    private int mMinTime;
    private int mMinDistance;

    private LocationListener mLocationGPSListener;
    private LocationListener mLocationNetworkListener;

    private ILocationChangedCallback mILocationChangedCallback;
    private ILocationClientCallback mILocationClientCallback;

    private LocationClientConnectHelper mConnectionHelper;

    public LocationRequestManager(Context context) {
        mContext = context;
        mMinTime = MIN_TIME;
        mMinDistance = TWO_MINUTES;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mConnectionHelper = new LocationClientConnectHelper(context);
    }

    public interface ILocationChangedCallback {
        void onLocationChanged(Location location);
    }

    public interface ILocationClientCallback {
        void onLocationConnectionChanged(Location location);
    }

    public void updateCurrentLocation(final Context context,int type) {
        // already request
        if (mILocationChangedCallback != null) {
            return;
        }
        mILocationChangedCallback = new ILocationChangedCallback() {

            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged, location = " + location);
                mILocationChangedCallback = null;
                unRegisterUpdateLocation();
                setLocation(location);
                LocationProvider.getInstance(context).getHandler().sendEmptyMessage(AllInOneConstant.MSG_UPDATE_LOCATION);
            }
        };

        registerUpdateLocation(type);
    }

    public void registerUpdateLocation(int type) {
        Log.d(TAG, "registerUpdateLocaiton, type = " + type);
        if (type == AllInOneConstant.GET_LOCATION_TYPE_GOOGLE_SERVICE) {
            mILocationClientCallback = new ILocationClientCallback() {

                @Override
                public void onLocationConnectionChanged(Location location) {
                    makeUseOfNewLocation(location);
                }
            };
            if (isServicesAvailable()) {
                mConnectionHelper.startConnection(mILocationClientCallback);
            }
        } else if ((type == AllInOneConstant.GET_LOCATION_TYPE_GPS)) {
            addGPSListener();
        } else if (type == AllInOneConstant.GET_LOCATION_TYPE_NETWORK) {
            addNetworkListener();
        }
    }

    private void addNetworkListener() {
        if (mLocationNetworkListener != null) {
            Log.d(TAG, "network listener was already request");
            return;
        }
        mLocationNetworkListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(Location location) {
                makeUseOfNewLocation(location);
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mMinTime, mMinDistance,
                mLocationNetworkListener);
    }

    private void addGPSListener() {
        if (mLocationGPSListener != null) {
            return;
        }

        mLocationGPSListener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLocationChanged(Location location) {
                makeUseOfNewLocation(location);
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mMinTime, mMinDistance, mLocationGPSListener);
    }

    private void unRegisterUpdateLocation() {

        mILocationClientCallback = null;
        removeAllListener();
        mConnectionHelper.stopLocationClient();
    }

    private void removeAllListener() {
        Log.d(TAG, "remove All Listenter");

        if (mLocationManager != null) {
            if (mLocationGPSListener != null) {
                mLocationManager.removeUpdates(mLocationGPSListener);
                mLocationGPSListener = null;
            }
            if (mLocationNetworkListener != null) {
                mLocationManager.removeUpdates(mLocationNetworkListener);
                mLocationNetworkListener = null;
            }
        }

    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public Location getCurrentLocation() {
        return mLocation;
    }

    private void makeUseOfNewLocation(Location location) {
        mILocationChangedCallback.onLocationChanged(location);
    }

    private boolean isServicesAvailable() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext);

        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(TAG, "Google Play services is available.");
            return true;
        } else {
            Log.d(TAG, "Google Play services was not available for some reason");
            Toast.makeText(mContext, "Google Play services was not available for some reason", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private class LocationClientConnectHelper implements
            GooglePlayServicesClient.ConnectionCallbacks,
            GooglePlayServicesClient.OnConnectionFailedListener {
        private static final String TAG_LOCATION = TAG + "LocationHelper";
        private Context mContext;
        private LocationClient mLocationClient;
        private ILocationClientCallback mCallback;

        public LocationClientConnectHelper(Context context) {
            mContext = context;
        }

        public LocationClient getLocationClient() {
            Log.d(TAG, "getLocationClient");
            if (mLocationClient == null) {
                mLocationClient = new LocationClient(mContext, this, this);
            }
            return mLocationClient;
        }

        public void startConnection(ILocationClientCallback callback) {
            Log.d(TAG, "startConnectionClient");
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
            // TODO Auto-generated method stub

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
            // TODO Auto-generated method stub

        }

    }

}
