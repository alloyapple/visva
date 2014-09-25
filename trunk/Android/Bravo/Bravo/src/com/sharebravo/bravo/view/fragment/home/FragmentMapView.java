package com.sharebravo.bravo.view.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetSpotTimeline;
import com.sharebravo.bravo.model.response.ObGetSpotTimeline.SpotTimeline;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentMapBasic;

public class FragmentMapView extends FragmentMapBasic implements LocationListener {
    public static final int               MAKER_BY_LOCATION_SPOT = 0;
    public static final int               MAKER_BY_LOCATION_USER = 1;
    private HashMap<Marker, SpotTimeline> mMakers                = new HashMap<Marker, SpotTimeline>();

    private GoogleMap                     map;
    private Marker                        curMarker              = null;

    private int                           typeMaker;
    private double                        mLat, mLong;

    private View                          mOriginalContentView;
    private TouchableWrapper              mTouchView;
    Location                              location               = null;
    LocationManager                       locationManager        = null;
    Button                                btnBack                = null;
    HomeActionListener                    mHomeActionListener    = null;
    private SessionLogin                  mSessionLogin          = null;
    private String                        foreignID              = null;
    private String                        fullName               = null;
    private int                           mLoginBravoViaType     = BravoConstant.NO_LOGIN_SNS;
    private Context                       mContext;
    LayoutInflater                        mLayoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, container, savedInstanceState);
        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(mOriginalContentView);
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        mContext = getActivity();
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mHomeActionListener = (HomeActivity) getActivity();
        LinearLayout mView = (LinearLayout) inflater.inflate(R.layout.header_fragment, container);
        btnBack = (Button) mView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToBack();
            }
        });
        mView.addView(mTouchView);
        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden && !isBackStatus()) {
            if (typeMaker == MAKER_BY_LOCATION_SPOT) {
                changeLocation(mLat, mLong);
            } else if (typeMaker == MAKER_BY_LOCATION_USER) {
                location = getLocation();
                requestGetUserTimeLine(foreignID, location.getLatitude(), location.getLongitude());
            }
        }
    }

    private void requestGetUserTimeLine(String checkingUserId, final double latitude, final double longitude) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Location", String.valueOf(mLat) + "," + String.valueOf(mLong));
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_TIMELINE.replace("{User_ID}", checkingUserId);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("obGetUserTimeline:" + response);

                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpotTimeline mObGetSpotTimeline;
                mObGetSpotTimeline = gson.fromJson(response.toString(), ObGetSpotTimeline.class);
                AIOLog.d("mObGetSpotTimeline:" + mObGetSpotTimeline);
                if (mObGetSpotTimeline == null) {
                    return;
                } else {
                    changeLocation(mObGetSpotTimeline.data, latitude, longitude);
                }

            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getTimeline.execute(url);

    }

    public void changeLocation(double latitude, double longitude) {

        if (map == null)
            map = getMap();

        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        getMap().clear();
        addMaker(latitude, longitude, "").showInfoWindow();
    }

    public void changeLocation(ArrayList<SpotTimeline> data, double latitude, double longitude) {
        if (map == null)
            map = getMap();

        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                if (curMarker != null) {
                }
            }
        });

        getMap().clear();
        // addMaker(latitude, longitude, "");
        if (data == null)
            return;
        mMakers.clear();
        for (int i = 0; i < data.size(); i++) {
            Marker marker = addMaker(data.get(i).Spot_Latitude, data.get(i).Spot_Longitude, "" + data.get(i).Spot_Name);
            mMakers.put(marker, data.get(i));
        }
        getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                SpotTimeline mSpot = mMakers.get(marker);
                ObBravo mBravo = new ObBravo();
                mBravo.Bravo_ID = mSpot.Bravo_ID;
                mBravo.Spot_Longitude = mSpot.Spot_Longitude;
                mBravo.Spot_Latitude = mSpot.Spot_Latitude;
                mBravo.Spot_Name = mSpot.Spot_Name;
                mBravo.User_ID = foreignID;
                mBravo.Full_Name = fullName;
                mHomeActionListener.goToRecentPostDetail(mBravo);
            }
        });

    }

    public int getPixelByDp(int dp) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    private Marker addMaker(double latitude, double longitute, String name) {
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitute)).title(name);
        // Changing marker icon
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        marker.icon(BitmapDescriptorFactory.fromBitmap(icon));
        marker.title(name);
        Marker markerObject = getMap().addMarker(marker);
        // markerObject.showInfoWindow();
        return markerObject;
    }

    public int getTypeMaker() {
        return typeMaker;
    }

    public void setTypeMaker(int typeMaker) {
        this.typeMaker = typeMaker;
    }

    public void setCordinate(String _lat, String _long) {
        mLat = Double.parseDouble(_lat);
        mLong = Double.parseDouble(_long);
    }

    public class TouchableWrapper extends FrameLayout {
        private int lastX;

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                lastX = (int) event.getX();
                break;
            }
            return super.dispatchTouchEvent(event);
        }

        public int getXLastTouchOnScreen() {
            int location[] = new int[2];
            getLocationOnScreen(location);
            return lastX + location[0];
        }
    }

    @Override
    public void onLocationChanged(Location arg0) {
        location = arg0;
    }

    @Override
    public void onProviderDisabled(String arg0) {

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    public String getForeignID() {
        return foreignID;
    }

    public void setForeignID(String foreignID) {
        this.foreignID = foreignID;
    }

    boolean canGetLocation;

    public Location getLocation() {

        locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return null;
        } else {
            canGetLocation = true;
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0, this);
                // Log.d("activity", "LOC Network Enabled");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        return location;
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0, this);
                    // Log.d("activity", "RLOC: GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            // Log.d("activity", "RLOC: loc by GPS");

                            return location;
                        }
                    }
                }
            }
        }
        return location;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
