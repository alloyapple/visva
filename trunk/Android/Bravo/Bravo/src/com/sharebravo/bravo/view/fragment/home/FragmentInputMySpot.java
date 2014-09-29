package com.sharebravo.bravo.view.fragment.home;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.View.OnClickListener;
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
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover2;

public class FragmentInputMySpot extends FragmentBasic implements LocationListener {

    private Button       btnBack;

    private SessionLogin mSessionLogin      = null;
    private int          mLoginBravoViaType = BravoConstant.NO_LOGIN_SNS;
    private TextView     btnLocateSpot;
    private Button       btnAdd             = null;
    FragmentTransaction  fragmentTransaction;
    FragmentMapCover2    mapFragment;
    EditText             txtboxName;
    EditText             txtboxGenre;
    EditText             txtboxAddress;
    Spinner              mSpinnerCategory;
    Location             location           = null;
    LocationManager      locationManager    = null;

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
                mHomeActionListener.goToLocateMySpot();
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
        mapFragment = (FragmentMapCover2) getChildFragmentManager().findFragmentById(R.id.img_map);
        if (mapFragment == null) {
            mapFragment = new FragmentMapCover2();
            fragmentTransaction.add(R.id.spot_map_add, mapFragment).commit();
        }
        ImageView btnMap = (ImageView) root.findViewById(R.id.layout_spot_map_add);
        btnMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToLocateMySpot();
            }
        });
        txtboxAddress = (EditText) root.findViewById(R.id.txtbox_address);
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
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // fragmentTransaction = getChildFragmentManager().beginTransaction();
        // if (!hidden)
        // fragmentTransaction.show(mapFragment);
        // else
        // fragmentTransaction.hide(mapFragment);
        // fragmentTransaction.commit();
        if (!hidden && !isBackStatus()) {
            location = getLocation();
            mapFragment.changeLocation(location.getLatitude(), location.getLongitude());
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
