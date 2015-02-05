package com.visva.android.visvasdklibrary.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.visva.android.visvasdklibrary.constant.AIOConstant;
import com.visva.android.visvasdklibrary.location.LocationRequestManager;

/**
 * LocationProvider supports get and update user's location and address by using google service, gps and network.
 * It uses a {@link Handler} and {@link LocationRequestManager} to handler location.Therefore, {@link HandlerLeak} can be occured
 * 
 * @author kieu.thang
 * 
 */
@SuppressLint("HandlerLeak")
public class LocationProvider {
    private Context                 mContext;

    /* singleton class */
    private static LocationProvider mInstance;

    private Handler                 mHandler;
    private LocationRequestManager  mLocationRequestManager;
    private ILocationUpdateCallback mILocationUpdateCallback;

    /**
     * Location constructor
     * it defines a handler and {@link LocationRequestManager} to request and update location
     * 
     * @param context
     */
    public LocationProvider(Context context) {
        super();
        mContext = context;
        mLocationRequestManager = new LocationRequestManager(mContext);
        initMainThreadHandler(mContext);
    }

    /**
     * get instance of LocationProvider singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized LocationProvider getInstance(Context context) {
        if (mInstance == null)
            mInstance = new LocationProvider(context);
        return mInstance;
    }

    private void initMainThreadHandler(final Context context) {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                case AIOConstant.MSG_REQUEST_LOCATION_GOOGLESERVICE:
                    mLocationRequestManager.updateCurrentLocation(context, AIOConstant.GET_LOCATION_TYPE_GOOGLE_SERVICE);
                    break;
                case AIOConstant.MSG_REQUEST_LOCATION_GPS:
                    mLocationRequestManager.updateCurrentLocation(context, AIOConstant.GET_LOCATION_TYPE_GPS);
                    break;
                case AIOConstant.MSG_REQUEST_LOCATION_NETWORK:
                    mLocationRequestManager.updateCurrentLocation(context, AIOConstant.GET_LOCATION_TYPE_NETWORK);
                    break;
                case AIOConstant.MSG_UPDATE_LOCATION:
                    Location location = mLocationRequestManager.getCurrentLocation();
                    mILocationUpdateCallback.onLocationUpdate(location);
                    break;
                case AIOConstant.MSG_REQUEST_GET_ADDRESS:
                    mLocationRequestManager.updateCurrentLocation(context, AIOConstant.GET_LOCATION_TYPE_GOOGLE_SERVICE);
                    break;
                default:
                    break;
                }
            }
        };
    }

    public interface ILocationUpdateCallback {
        void onLocationUpdate(Location location);
    }

    public void setListener(ILocationUpdateCallback callback) {
        mILocationUpdateCallback = callback;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public LocationRequestManager getLocationRequestManager() {
        return mLocationRequestManager;
    }
}
