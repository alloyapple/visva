package com.sharebravo.bravo.view.fragment.maps;

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
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.view.fragment.FragmentMapBasic;

public class FragmentLocateMySpot extends FragmentMapBasic implements LocationListener {
    private GoogleMap  map;
    private int        typeMaker;
    private View       mOriginalContentView;
    Location           location            = null;
    LocationManager    locationManager     = null;
    Button             btnBack             = null;
    HomeActionListener mHomeActionListener = null;
    LayoutInflater     mLayoutInflater;
    private Context    mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, null, null);
        setMapTransparent((ViewGroup) mOriginalContentView);
        mHomeActionListener = (HomeActivity) getActivity();
        mContext = getActivity();
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout mView = (LinearLayout) inflater.inflate(R.layout.page_fragment_locate_map, container);
        FrameLayout layoutMap = (FrameLayout) mView.findViewById(R.id.layout_map_locate_spot);

        btnBack = (Button) mView.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToBack();
            }
        });
        layoutMap.addView(mOriginalContentView);
        return mView;
    }
    private void setMapTransparent(ViewGroup group) {
        int childCount = group.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = group.getChildAt(i);

            if (child instanceof ViewGroup) {
                setMapTransparent((ViewGroup) child);
            } else if (child instanceof SurfaceView) {
                child.setBackgroundColor(0x00000000);
            }
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            location = getLocation();
            changeLocation(location.getLatitude(), location.getLongitude());
        }
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
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub

                addMaker(point.latitude, point.longitude, "");
            }
        });

        getMap().clear();
        addMaker(latitude, longitude, "");
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
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.nearby_icon);
        marker.icon(BitmapDescriptorFactory.fromBitmap(icon));
        getMap().clear();
        Marker markerObject = getMap().addMarker(marker);
        return markerObject;
    }

    public int getTypeMaker() {
        return typeMaker;
    }

    public void setTypeMaker(int typeMaker) {
        this.typeMaker = typeMaker;
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
}
