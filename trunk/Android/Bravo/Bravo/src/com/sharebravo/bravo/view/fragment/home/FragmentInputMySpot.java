package com.sharebravo.bravo.view.fragment.home;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.foursquare.FactoryFoursquareParams;
import com.sharebravo.bravo.foursquare.models.OFPostVenue;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpPost;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpResponseProcess;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObPostSpot;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.adapter.AdapterMapInputSpot;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;

@SuppressLint("ClickableViewAccessibility")
public class FragmentInputMySpot extends FragmentBasic implements LocationListener {

    private Button       btnBack;

    private SessionLogin mSessionLogin      = null;
    private int          mLoginBravoViaType = BravoConstant.NO_LOGIN_SNS;
    private TextView     btnLocateSpot;
    private Button       btnAdd             = null;
    FragmentTransaction  fragmentTransaction;
    // FragmentMapCover mapFragment;
    XListView            mListView;
    AdapterMapInputSpot  mAdapter;
    EditText             txtboxName;
    EditText             txtboxGenre;
    EditText             txtboxAddress;
    Spinner              mSpinnerCategory;
    Location             location           = null;
    LocationManager      locationManager    = null;
    public static double checkLat, checkLong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_input_myspot, container);
        mHomeActionListener = (HomeActivity) getActivity();

        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });

        btnLocateSpot = (TextView) root.findViewById(R.id.txt_locate_your_spot);
        btnLocateSpot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToLocateMySpot(checkLat, checkLong, FragmentMapView.MAKER_BY_USER);
            }
        });
        btnAdd = (Button) root.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onAddMySpot();
            }
        });
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        mListView = (XListView) root.findViewById(R.id.listview_map_cover_input_spot);
        mAdapter = new AdapterMapInputSpot(getActivity(), this);
        mListView.setAdapter(mAdapter);
        ImageView btnMap = (ImageView) root.findViewById(R.id.layout_spot_map_add);
        btnMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToLocateMySpot(checkLat, checkLong, FragmentMapView.MAKER_BY_USER);
            }
        });
        txtboxName = (EditText) root.findViewById(R.id.txtbox_name);
        txtboxName.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    txtboxName.setHint("");
                }
                return false;
            }
        });
        txtboxGenre = (EditText) root.findViewById(R.id.txtbox_genre);
        txtboxGenre.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    txtboxGenre.setHint("");
                }
                return false;
            }
        });
        txtboxAddress = (EditText) root.findViewById(R.id.txtbox_address);
        txtboxAddress.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    txtboxAddress.setHint("");
                }
                return false;
            }
        });
        mSpinnerCategory = (Spinner) root.findViewById(R.id.choose_category);
        ArrayAdapter<String> adapterSpiner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new String[] {
                "Restaurant", "Art",
                "Coffee", "NightLife", "Shopping", "Tourist", "Others" });

        adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(adapterSpiner);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    String name    = "";
    String genre   = "";
    String address = "";
    String type    = "";

    public void onAddMySpot() {
        boolean isAdd = true;
        type = mSpinnerCategory.getSelectedItem().toString();
        name = txtboxName.getEditableText().toString();
        genre = txtboxGenre.getEditableText().toString();
        address = txtboxAddress.getEditableText().toString();
        if (name.equals("")) {
            txtboxName.setHintTextColor(Color.RED);
            txtboxName.setHint("Please enter your");
            isAdd = false;
        }
        if (genre.equals("")) {
            txtboxGenre.setHintTextColor(Color.RED);
            txtboxGenre.setHint("Please enter your");
            isAdd = false;
        }
        if (address.equals("")) {
            txtboxAddress.setHintTextColor(Color.RED);
            txtboxAddress.setHint("Please enter your");
            isAdd = false;
        }
        if (isAdd) {
            requestPost4squareVenueSearch(checkLat, checkLong, name, address);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!isBackStatus()) {
                location = getLocation();
                checkLat = location.getLatitude();
                checkLong = location.getLongitude();
            } else {

            }
            FragmentMapCover.mLat = checkLat;
            FragmentMapCover.mLong = checkLong;
            mAdapter.updateMapView();
            txtboxAddress.setText(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
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

    private Spot mSpot;

    private void requestPost4squareVenueSearch(double lat, double lng, String name, String address) {
        String url = BravoWebServiceConfig.URL_FOURSQUARE_POST_VENUE.replace("{client_id}", FactoryFoursquareParams.client_id).replace(
                "{client_secret}", FactoryFoursquareParams.client_secret).replace("{oauth_token}", FactoryFoursquareParams.authen_token)
                .replace("{v}", FactoryFoursquareParams.v + "");
        List<NameValuePair> params = null;
        params = FactoryFoursquareParams
                .createSubParamsRequestAddVenue(lat, lng, name, address);
        FAsyncHttpPost request = new FAsyncHttpPost(getActivity(), new FAsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response OFPostVenue:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                OFPostVenue mOFPostVenue;
                mOFPostVenue = gson.fromJson(response.toString(), OFPostVenue.class);
                AIOLog.d("mOFGetVenue:" + mOFPostVenue);
                if (mOFPostVenue == null)
                    return;
                else {
                    if (mOFPostVenue.meta.code == 200) {
                        mSpot = new Spot();
                        mSpot.Spot_FID = mOFPostVenue.response.venue.id;
                        mSpot.Spot_Address = mOFPostVenue.response.venue.location.address;
                        mSpot.Spot_Name = mOFPostVenue.response.venue.name;
                        if (mOFPostVenue.response.venue.categories.size() > 0)
                            mSpot.Spot_Icon = mOFPostVenue.response.venue.categories.get(0).icon.prefix + "bg_44"
                                    + mOFPostVenue.response.venue.categories.get(0).icon.suffix;

                        mSpot.Total_Bravos = 0;
                        mSpot.Spot_Latitude = mOFPostVenue.response.venue.location.lat;
                        mSpot.Spot_Longitude = mOFPostVenue.response.venue.location.lon;
                        mSpot.Spot_Source = "foursqure";
                        mSpot.Spot_Phone = mOFPostVenue.response.venue.contact.phone;
                        mSpot.Spot_Type = type;
                        mSpot.Spot_Genre = genre;
                        requestPostSpot(mSpot);
                    } else if (mOFPostVenue.meta.code == 409) {
                        mSpot = new Spot();
                        mSpot.Spot_Address = FragmentInputMySpot.this.address;
                        mSpot.Spot_Name = FragmentInputMySpot.this.name;
                        mSpot.Total_Bravos = 0;
                        mSpot.Spot_Latitude = checkLat;
                        mSpot.Spot_Longitude = checkLong;
                        mSpot.Spot_Source = "foursqure";
                        mSpot.Spot_Type = type;
                        mSpot.Spot_Genre = genre;
                        mHomeActionListener.goToDuplicateSpot(mSpot, mOFPostVenue);
                    }
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        request.execute(url);
    }

    private void requestPostSpot(Spot spot) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_POST_SPOTS.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);

        HashMap<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("Spot_Name", spot.Spot_Name);
        subParams.put("Spot_FID", spot.Spot_FID);
        subParams.put("Spot_Source", spot.Spot_Source);
        subParams.put("Spot_Longitude", spot.Spot_Longitude);
        subParams.put("Spot_Latitude", spot.Spot_Latitude);
        subParams.put("Spot_Type", spot.Spot_Type);
        subParams.put("Spot_Genre", spot.Spot_Genre);
        subParams.put("Spot_Address", spot.Spot_Address);
        // subParams.put("Spot_Phone", spot.Spot_Phone);
        // subParams.put("Spot_Price", spot.Spot_Price);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        AsyncHttpPost request = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity(),this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response mObPostSpot:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPostSpot mObPostSpot;
                mObPostSpot = gson.fromJson(response.toString(), ObPostSpot.class);
                AIOLog.d("mObPostSpot:" + mObPostSpot);
                if (mObPostSpot == null)
                    return;
                else {
                    mSpot.Spot_ID = mObPostSpot.data.Spot_ID;
                    mHomeActionListener.goToSpotDetail(mSpot);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        request.execute(url);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
