package com.sharebravo.bravo.sdk.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.sharebravo.bravo.sdk.util.location.svc.LocationRequestManager;
import com.sharebravo.bravo.utils.BravoConstant;

@SuppressLint("HandlerLeak")
public class LocationProvider {
    private Context                 mContext;

    /* singleton class */
    private static LocationProvider mInstance;

    private Handler                 mHandler;
    private LocationRequestManager  mLocationRequestManager;
    private ILocationUpdateCallback mILocationUpdateCallback;

    /**
     * Volley constructor
     * 
     * @param context
     */
    public LocationProvider(Context context) {
        super();
        mContext = context;
        initMainThreadHandler(mContext);
        mLocationRequestManager = new LocationRequestManager(mContext);
    }

    /**
     * get instance of Volley singleton class
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
                case BravoConstant.MSG_REQUEST_LOCATION_GOOGLESERVICE:
                    mLocationRequestManager.updateCurrentLocation(context, BravoConstant.GET_LOCATION_TYPE_GOOGLE_SERVICE);
                    break;
                case BravoConstant.MSG_REQUEST_LOCATION_GPS:
                    mLocationRequestManager.updateCurrentLocation(context, BravoConstant.GET_LOCATION_TYPE_GPS);
                    break;
                case BravoConstant.MSG_REQUEST_LOCATION_NETWORK:
                    mLocationRequestManager.updateCurrentLocation(context, BravoConstant.GET_LOCATION_TYPE_NETWORK);
                    break;
                case BravoConstant.MSG_UPDATE_LOCATION:
                    Location location = mLocationRequestManager.getCurrentLocation();
                    mILocationUpdateCallback.onLocationUpdate(location);
                    break;
                case BravoConstant.MSG_REQUEST_GET_ADDRESS:
                    mLocationRequestManager.updateCurrentLocation(context, BravoConstant.GET_LOCATION_TYPE_GOOGLE_SERVICE);
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
