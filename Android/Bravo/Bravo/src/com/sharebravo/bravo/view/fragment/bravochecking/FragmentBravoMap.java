package com.sharebravo.bravo.view.fragment.bravochecking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.sharebravo.bravo.control.activity.ActivityBravoChecking;
import com.sharebravo.bravo.control.activity.BravoCheckingListener;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.view.fragment.FragmentMapBasic;
import com.sharebravo.bravo.view.lib.gifanimation.ActivityGIFAnimation;

public class FragmentBravoMap extends FragmentMapBasic implements LocationListener {
    public static final int       MAKER_BY_LOCATION_SPOT            = 0;
    public static final int       MAKER_BY_LOCATION_USER            = 1;

    public static final int       REQUEST_SHOW_BRAVO_JUMP_ANIMATION = 6001;

    private GoogleMap             mGoogleMap;

    private int                   mTypeMaker;
    private double                mLat;
    private double                mLong;

    private View                  mOriginalContentView;
    private Location              mLocation                         = null;
    private LocationManager       mLocationManager                  = null;

    private String                foreignID                         = null;
    private Spot                  mSpot;
    private BravoCheckingListener mBravoCheckingListener;
    Button                        btnYes, btnNo;
    LinearLayout                  layoutConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, container, savedInstanceState);
        mBravoCheckingListener = (ActivityBravoChecking) getActivity();

        if (mTypeMaker == MAKER_BY_LOCATION_SPOT) {
            // changeLocation(mLat, mLong);
        }
        mLocationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = mLocationManager.getBestProvider(criteria, true);

        // Getting Current Location
        mLocation = mLocationManager.getLastKnownLocation(provider);

        if (mLocation != null) {
            onLocationChanged(mLocation);
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mBravoCheckingListener = (ActivityBravoChecking) getActivity();
        LinearLayout mView = (LinearLayout) inflater.inflate(R.layout.page_fragment_bravo_map, container);
        FrameLayout layoutMap = (FrameLayout) mView.findViewById(R.id.layout_map);
        layoutConfirm = (LinearLayout) mView.findViewById(R.id.layout_confirm);
        btnYes = (Button) mView.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                layoutConfirm.setVisibility(View.GONE);
                mGoogleMap.clear();
                Intent intent = new Intent(getActivity(), ActivityGIFAnimation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(intent, REQUEST_SHOW_BRAVO_JUMP_ANIMATION);
            }
        });
        btnNo = (Button) mView.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mBravoCheckingListener.goToBack();
            }
        });
        layoutMap.addView(mOriginalContentView);
        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AIOLog.d("hidden:" + hidden);
        if (!hidden) {
            changeLocation(mLat, mLong);
            AIOLog.d("lat:" + mLat + "; " + "lon:" + mLong);
            layoutConfirm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_SHOW_BRAVO_JUMP_ANIMATION:
            if (mSpot == null)
                return;
            mBravoCheckingListener.goToReturnSpotFragment(mSpot);
            break;
        default:
            return;
        }

    }

    public void changeLocation(double latitude, double longitude) {
        if (mGoogleMap == null)
            mGoogleMap = getMap();

        LatLng latLng = new LatLng(latitude, longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        getMap().clear();
        addMaker(latitude, longitude, "" + mSpot.Spot_Name);
    }

    public int getPixelByDp(int dp) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    private Marker addMaker(double latitude, double longitute, String name) {
        AIOLog.d("spot name:" + name);
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitute)).title(name);
        // Changing marker icon
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.nearby_icon);
        marker.icon(BitmapDescriptorFactory.fromBitmap(icon));
        marker.title(name);
        Marker markerObject = getMap().addMarker(marker);
        marker.snippet(name);
        markerObject.showInfoWindow();
        return markerObject;
    }

    public int getTypeMaker() {
        return mTypeMaker;
    }

    public void setTypeMaker(int typeMaker) {
        this.mTypeMaker = typeMaker;
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
        mLocation = arg0;
    }

    @Override
    public void onProviderDisabled(String arg0) {

    }

    @Override
    public void onProviderEnabled(String arg0) {

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }

    public String getForeignID() {
        return foreignID;
    }

    public void setForeignID(String foreignID) {
        this.foreignID = foreignID;
    }

    public void setBravoSpot(Spot spot) {
        mSpot = spot;
        mLat = spot.Spot_Latitude;
        mLong = spot.Spot_Longitude;
    }

}
