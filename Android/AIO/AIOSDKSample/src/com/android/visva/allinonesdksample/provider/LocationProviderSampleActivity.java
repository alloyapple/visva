package com.android.visva.allinonesdksample.provider;

import android.app.Activity;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.visva.allinonesdksample.R;
import com.visva.android.visvasdklibrary.constant.AIOConstant;
import com.visva.android.visvasdklibrary.location.AddressLoader;
import com.visva.android.visvasdklibrary.location.IAddressListener;
import com.visva.android.visvasdklibrary.location.LocationUtils;
import com.visva.android.visvasdklibrary.log.AIOLog;
import com.visva.android.visvasdklibrary.provider.LocationProvider;
import com.visva.android.visvasdklibrary.provider.LocationProvider.ILocationUpdateCallback;

public class LocationProviderSampleActivity extends Activity implements ILocationUpdateCallback {
    public static final String TAG           = "LocationProviderSampleActivity";
    // Handles to UI widgets
    private TextView           mLatLng;
    private TextView           mTxtAddress;
    private ProgressBar        mActivityIndicator;
    private Button             mGetLocationByGoogleSvc;
    private Button             mGetLocationByGPS;
    private Button             mGetLocationByNetwork;
    private Button             mGetAddress;

    private boolean            mIsAddressBtnClick;
    private int                mLocationType = AIOConstant.MSG_REQUEST_LOCATION_GOOGLESERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_provider_sample);

        // Get handles to the UI view objects
        mLatLng = (TextView) findViewById(R.id.lat_lng);
        mTxtAddress = (TextView) findViewById(R.id.address);
        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);

        mGetLocationByGoogleSvc = (Button) findViewById(R.id.btn_get_location_by_googlesvc);
        mGetLocationByGoogleSvc.setOnClickListener(mOnClickListener);

        mGetLocationByGPS = (Button) findViewById(R.id.btn_get_location_by_gps);
        mGetLocationByGPS.setOnClickListener(mOnClickListener);

        mGetLocationByNetwork = (Button) findViewById(R.id.btn_get_location_by_network);
        mGetLocationByNetwork.setOnClickListener(mOnClickListener);

        mGetAddress = (Button) findViewById(R.id.btn_get_add);
        mGetAddress.setOnClickListener(mOnClickListener);

        mIsAddressBtnClick = false;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

                                                      @Override
                                                      public void onClick(View v) {
                                                          mActivityIndicator.setVisibility(View.VISIBLE);
                                                          switch (v.getId()) {
                                                          case R.id.btn_get_location_by_googlesvc:
                                                              mLatLng.setText("");
                                                              updateLocationByType(AIOConstant.MSG_REQUEST_LOCATION_GOOGLESERVICE);
                                                              break;
                                                          case R.id.btn_get_location_by_gps:
                                                              Log.d(TAG, "get Location by GPS, onclick");
                                                              mLatLng.setText("");
                                                              updateLocationByType(AIOConstant.MSG_REQUEST_LOCATION_GPS);
                                                              break;
                                                          case R.id.btn_get_location_by_network:
                                                              Log.d(TAG, "get Location by network, onclick");
                                                              mLatLng.setText("");
                                                              updateLocationByType(AIOConstant.MSG_REQUEST_GET_ADDRESS);
                                                              break;
                                                          case R.id.btn_get_add:
                                                              mIsAddressBtnClick = true;
                                                              updateAddress(AIOConstant.MSG_REQUEST_GET_ADDRESS);

                                                              break;
                                                          default:
                                                              break;
                                                          }
                                                      }
                                                  };

    private void updateLocationByType(int type) {
        LocationProvider.getInstance(LocationProviderSampleActivity.this).setListener(LocationProviderSampleActivity.this);
        Handler googleSvcHandler = LocationProvider.getInstance(LocationProviderSampleActivity.this).getHandler();
        googleSvcHandler.sendEmptyMessage(type);

    }

    private void updateAddress(int type) {
        LocationProvider.getInstance(LocationProviderSampleActivity.this).setListener(LocationProviderSampleActivity.this);
        Handler getAddressHander = LocationProvider.getInstance(LocationProviderSampleActivity.this).getHandler();
        getAddressHander.sendEmptyMessage(type);
    }

    public void setLatText(Location location) {
        Log.d(TAG, "setLatText location Lat = " + location.getLatitude());
        mLatLng.setText(LocationUtils.getLatLng(this, location));
    }

    @Override
    public void onLocationUpdate(Location location) {
        Log.d(TAG, "onLocationUpdate");
        mActivityIndicator.setVisibility(View.INVISIBLE);
        if (location == null) {
            Toast.makeText(getApplicationContext(), "cannot get the location", Toast.LENGTH_SHORT).show();
            return;
        } else {
            setLatText(location);
        }

        if (mIsAddressBtnClick) {
            mIsAddressBtnClick = false;
            AddressLoader addLoader = new AddressLoader(this);
            addLoader.loadAddress(location, new IAddressListener() {

                @Override
                public void onResponse(Address address) {
                    Log.d(TAG, "onResponse address" + address);
                    if (address == null) {
                        return;
                    }
                    onAddressUpdate(address);
                }

                @Override
                public void onFailedResponse(int arg0) {

                }
            });

        }
    }

    private void onAddressUpdate(Address add) {
        AIOLog.d(TAG, "==============Address Information===============");
        AIOLog.d(TAG, "add.getLocality=>" + add.getLocale());
        AIOLog.d(TAG, "add.getAdminArea=>" + add.getAdminArea());
        AIOLog.d(TAG, "add.getCountryCode=>" + add.getCountryCode());
        AIOLog.d(TAG, "add.getCountryName=>" + add.getCountryName());
        AIOLog.d(TAG, "add.getFeatureName=>" + add.getFeatureName());
        AIOLog.d(TAG, "add.getLatitude=>" + add.getLatitude());
        AIOLog.d(TAG, "add.getLocality=>" + add.getLocality());
        AIOLog.d(TAG, "add.getLongitude=>" + add.getLongitude());
        AIOLog.d(TAG, "add.getMaxAddressLineIndex=>" + add.getMaxAddressLineIndex());
        AIOLog.d(TAG, "add.getPhone=>" + add.getPhone());
        AIOLog.d(TAG, "add.getPostalCode=>" + add.getPostalCode());
        AIOLog.d(TAG, "add.getPremises=>" + add.getPremises());
        AIOLog.d(TAG, "add.getSubAdminArea=>" + add.getSubAdminArea());
        AIOLog.d(TAG, "add.getSubLocality=>" + add.getSubLocality());
        AIOLog.d(TAG, "add.getSubThoroughfare=>" + add.getSubThoroughfare());
        AIOLog.d(TAG, "add.getThoroughfare=>" + add.getThoroughfare());
        AIOLog.d(TAG, "add.getUrl=>" + add.getUrl());
        AIOLog.d(TAG, "====================================================");
        String addressText = add.getMaxAddressLineIndex() > 0 ? add.getAddressLine(0) + add.getLocality() + add.getCountryName() : "" + add.getLocality() + add.getCountryName();
        mTxtAddress.setText(addressText);
    }

    public void startUpdates(View v) {
        LocationProvider.getInstance(this).getLocationRequestManager().updateCurrentLocation(this, mLocationType);
    }

    public void stopUpdates(View v) {
        Toast.makeText(this, "On developing", Toast.LENGTH_SHORT).show();
    }
}
