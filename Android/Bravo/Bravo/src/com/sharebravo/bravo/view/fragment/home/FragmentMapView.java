package com.sharebravo.bravo.view.fragment.home;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sharebravo.bravo.R;

public class FragmentMapView extends SupportMapFragment {
    public static final int  MAKER_BY_LOCATION_SPOT = 0;
    public static final int  MAKER_BY_LOCATION_USER = 1;
    private GoogleMap        map;
    private Marker           curMarker              = null;
    private int              typeMaker;
    private double           mLat, mLong;

    private View             mOriginalContentView;
    private TouchableWrapper mTouchView;

    // private HomeActionListener mHomeActionListener = null;
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(mOriginalContentView);
        if (typeMaker == MAKER_BY_LOCATION_SPOT) {
            changeLocation(mLat, mLong);
        }
        return mTouchView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (typeMaker == MAKER_BY_LOCATION_SPOT) {
                changeLocation(mLat, mLong);
            } else if (typeMaker == MAKER_BY_LOCATION_USER) {
            }
        }
    }

    public void changeLocation(double latitude, double longitute) {
        if (map == null)
            map = getMap();

        LatLng latLng = new LatLng(latitude, longitute);
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
        map.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO Auto-generated method stub

                if (curMarker != null) {

                }
                else {

                }
                return true;
            }
        });
        addMaker(mLat, mLong, "");
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

        // adding marker
        Marker markerObject = getMap().addMarker(marker);
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
}
