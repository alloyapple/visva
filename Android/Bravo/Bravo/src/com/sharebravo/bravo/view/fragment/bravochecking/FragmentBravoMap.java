package com.sharebravo.bravo.view.fragment.bravochecking;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.ActivityBravoChecking;
import com.sharebravo.bravo.control.activity.BravoCheckingListener;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetSpot.Spot;
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
import com.sharebravo.bravo.view.lib.gifanimation.ActivityGIFAnimation;

public class FragmentBravoMap extends FragmentMapBasic implements LocationListener {
    public static final int       MAKER_BY_LOCATION_SPOT            = 0;
    public static final int       MAKER_BY_LOCATION_USER            = 1;

    public static final int       REQUEST_SHOW_BRAVO_JUMP_ANIMATION = 6001;

    private GoogleMap             mGoogleMap;
    private Marker                mCurMarker                        = null;

    private int                   mTypeMaker;
    private double                mLat, mLong;

    private View                  mOriginalContentView;
    private TouchableWrapper      mTouchView;
    private Location              mLocation                         = null;
    private LocationManager       mLocationManager                  = null;
    private Button                mBtnBack                          = null;
    private BravoCheckingListener mBravoCheckingListener            = null;
    private SessionLogin          mSessionLogin                     = null;
    private String                foreignID                         = null;
    private int                   mLoginBravoViaType                = BravoConstant.NO_LOGIN_SNS;
    private Movie                 mMovie                            = null;
    private Spot                  mSpot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, container, savedInstanceState);
        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(mOriginalContentView);
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
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
        LinearLayout mView = (LinearLayout) inflater.inflate(R.layout.header_fragment, container);
        mBtnBack = (Button) mView.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mBravoCheckingListener.goToBack();
            }
        });
        mView.addView(mTouchView);
        return mView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AIOLog.d("hidden:" + hidden);
        if (!hidden) {
            if (mTypeMaker == MAKER_BY_LOCATION_SPOT) {
                changeLocation(mLat, mLong);
            } else if (mTypeMaker == MAKER_BY_LOCATION_USER) {
                requestGetUserTimeLine(foreignID, mLocation.getLatitude(), mLocation.getLongitude());
            }
            showDialogBravoConfirm();
        }
    }

    private void showDialogBravoConfirm() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_bravo_confirm, null);
        Button btnBravoAlertYes = (Button) dialog_view.findViewById(R.id.btn_bravo_action_alert_yes);
        btnBravoAlertYes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // getActivity().finish();
                Intent intent = new Intent(getActivity(), ActivityGIFAnimation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                startActivityForResult(intent, REQUEST_SHOW_BRAVO_JUMP_ANIMATION);
            }
        });
        Button btnBravoAlertNo = (Button) dialog_view.findViewById(R.id.btn_bravo_action_alert_no);
        btnBravoAlertNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.show();
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
                if (mObGetSpotTimeline == null || mObGetSpotTimeline.data.size() == 0) {
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
        mGoogleMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                if (mCurMarker != null) {
                }
            }
        });
        mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mCurMarker != null) {

                }
                else {

                }
                return true;
            }
        });
        getMap().clear();
        addMaker(latitude, longitude, "");
    }

    public void changeLocation(ArrayList<SpotTimeline> data, double latitude, double longitude) {
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
        mGoogleMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                if (mCurMarker != null) {
                }
            }
        });
        mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {

                if (mCurMarker != null) {

                }
                else {

                }
                return true;
            }
        });
        getMap().clear();
        addMaker(latitude, longitude, "");
        if (data == null)
            return;

        for (int i = 0; i < data.size(); i++) {
            addMaker(data.get(i).Spot_Latitude, data.get(i).Spot_Longitude, "");
        }
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
        Marker markerObject = getMap().addMarker(marker);
        return markerObject;
    }

    public int getTypeMaker() {
        return mTypeMaker;
    }

    public void setTypeMaker(int typeMaker) {
        this.mTypeMaker = typeMaker;
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
