package com.sharebravo.bravo.view.fragment.bravochecking;

import java.util.List;
import java.util.Locale;

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

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapView;

public class FragmentBravoInputMySpot extends FragmentBasic implements LocationListener {

    private Button       btnBack;

    private SessionLogin mSessionLogin      = null;
    private int          mLoginBravoViaType = BravoConstant.NO_LOGIN_SNS;
    private TextView     btnLocateSpot;
    private Button       btnAdd             = null;
    FragmentTransaction  fragmentTransaction;
    FragmentMapCover     mapFragment;
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

        fragmentTransaction = getChildFragmentManager().beginTransaction();
        mapFragment = (FragmentMapCover) getChildFragmentManager().findFragmentById(R.id.img_map);
        if (mapFragment == null) {
            mapFragment = new FragmentMapCover();
            fragmentTransaction.add(R.id.spot_map_add, mapFragment).commit();
        }
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

    public void onAddMySpot() {
        boolean isAdd = true;
        String name = txtboxName.getEditableText().toString();
        String genre = txtboxGenre.getEditableText().toString();
        String address = txtboxAddress.getEditableText().toString();
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
            mapFragment.changeLocation(checkLat, checkLong);
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
