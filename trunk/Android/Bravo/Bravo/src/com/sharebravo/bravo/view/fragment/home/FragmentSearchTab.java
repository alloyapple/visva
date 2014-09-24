package com.sharebravo.bravo.view.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.foursquare.FactoryFoursquareParams;
import com.sharebravo.bravo.foursquare.models.OFGetVenueSearch;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpGet;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpResponseProcess;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetSpotSearch;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterSearchSpotResult;
import com.sharebravo.bravo.view.adapter.SpotSearchListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;

public class FragmentSearchTab extends FragmentBasic implements LocationListener, SpotSearchListener {
    public static int               SEARCH_FOR_SPOT             = 0;
    public static int               SEARCH_LOCAL_BRAVO          = 2;
    public static int               SEARCH_LOCAL_BRAVO_KEY      = 3;
    public static int               SEARCH_ARROUND_ME           = 3;
    public static int               SEARCH_ARROUND_KEY          = 5;
    public static int               SEARCH_PEOPLE_FOLLOWING     = 6;
    public static int               SEARCH_PEOPLE_FOLLOWING_KEY = 7;
    private SessionLogin            mSessionLogin               = null;
    private int                     mLoginBravoViaType          = BravoConstant.NO_LOGIN_SNS;
    private EditText                textboxSearch               = null;
    private LinearLayout            layoutQuickSearchOptions    = null;
    private AdapterSearchSpotResult mAdapter;
    private XListView               listViewResult;
    private Button                  btnBack;
    private TextView                btnLocalBravos;
    private TextView                btnAroundMe;
    private TextView                btnPeopleFollowing;

    private int                     mMode;
    private ArrayList<Spot>         mSpots                      = new ArrayList<Spot>();
    private ObGetSpotSearch         mObGetSpotSearch            = null;
    Location                        location                    = null;
    LocationManager                 locationManager             = null;
    private OnItemClickListener     iSpotClickListener          = new OnItemClickListener() {

                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                        if (position < mAdapter.getCount())
                                                                            mHomeActionListener.goToSpotDetail(mSpots.get(position - 1));
                                                                    }
                                                                };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_search_tab, null);
        mHomeActionListener = (HomeActivity) getActivity();
        textboxSearch = (EditText) root.findViewById(R.id.txtbox_search_spot);
        textboxSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keySearch = textboxSearch.getEditableText().toString();
                    if (!keySearch.equals(""))
                        onSearch(keySearch);
                    return true;
                }
                return false;
            }
        });
        layoutQuickSearchOptions = (LinearLayout) root.findViewById(R.id.layout_quicksearch_options);
        listViewResult = (XListView) root.findViewById(R.id.listview_result_search);
        listViewResult.setOnItemClickListener(iSpotClickListener);
        mAdapter = new AdapterSearchSpotResult(getActivity());
        mAdapter.setListener(this);
        listViewResult.setAdapter(mAdapter);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBack(mMode);
            }
        });
        btnLocalBravos = (TextView) root.findViewById(R.id.text_local_bravo);
        btnLocalBravos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                mMode = SEARCH_LOCAL_BRAVO;
                requestBravoSearch("", SEARCH_LOCAL_BRAVO);
            }
        });
        btnAroundMe = (TextView) root.findViewById(R.id.text_around_me);
        btnAroundMe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                mMode = SEARCH_ARROUND_ME;
            }
        });
        btnPeopleFollowing = (TextView) root.findViewById(R.id.text_people_following);
        btnPeopleFollowing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                mMode = SEARCH_PEOPLE_FOLLOWING;
                requestBravoSearch("", SEARCH_PEOPLE_FOLLOWING);
            }
        });
        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        location = getLocation();
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            location = getLocation();
            Toast.makeText(getActivity(), "location =" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_LONG).show();
            mMode = SEARCH_FOR_SPOT;
            listViewResult.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            layoutQuickSearchOptions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void requestSpotSearch(ArrayList<String> mVenues) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + mSessionLogin.userID + ", mAccessToken:" + mSessionLogin.accessToken);
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, Object> subParams = new HashMap<String, Object>();
        // subParams.put("Start", "0");
        subParams.put("FID", mVenues);
        subParams.put("Source", "foursquare");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_SPOT_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getSpotSearch = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("getSpotSearch:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();

                mObGetSpotSearch = gson.fromJson(response.toString(), ObGetSpotSearch.class);
                AIOLog.d("mObGetSpotSearch:" + mObGetSpotSearch);
                if (mObGetSpotSearch == null) {
                    return;
                } else {
                    layoutQuickSearchOptions.setVisibility(View.GONE);
                    listViewResult.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.VISIBLE);
                    // addSpotIconDefault(mObGetSpotSearch.data);
                    mAdapter.updateData(mSpots);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getSpotSearch.execute(url);

    }

    public void requestBravoSearch(String nameSpot, int mode) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + mSessionLogin.userID + ", mAccessToken:" + mSessionLogin.accessToken);
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        subParams.put("Location", (float) location.getLongitude() + "," + (float) location.getLatitude());
        if (mode == SEARCH_LOCAL_BRAVO)
            subParams.put("Global", "TRUE");
        if (mode == SEARCH_LOCAL_BRAVO_KEY || mode == SEARCH_PEOPLE_FOLLOWING_KEY)
            subParams.put("Name", nameSpot);
        JSONObject subParamsJson = new JSONObject(subParams);

        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_BRAVO_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getBravoSearch = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("getBravoSearch:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetSpotSearch = gson.fromJson(response.toString(), ObGetSpotSearch.class);
                AIOLog.d("getBravoSearch:" + mObGetSpotSearch);
                if (mObGetSpotSearch == null) {
                    return;
                } else {
                    layoutQuickSearchOptions.setVisibility(View.GONE);
                    listViewResult.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.VISIBLE);
                    addSpotIconDefault(mObGetSpotSearch.data);
                    mSpots = mObGetSpotSearch.data;
                    mAdapter.updateData(mSpots);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getBravoSearch.execute(url);

    }

    private void requestGet4squareVenueSearch(String query) {
        String url = BravoWebServiceConfig.URL_FOURSQUARE_GET_VENUE_SEARCH;
        List<NameValuePair> params = FactoryFoursquareParams
                .createSubParamsRequestSearchVenue(location.getLatitude(), location.getLongitude(), query);
        FAsyncHttpGet request = new FAsyncHttpGet(getActivity(), new FAsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("mOFGetVenueSearch:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                OFGetVenueSearch mOFGetVenueSearch;
                mOFGetVenueSearch = gson.fromJson(response.toString(), OFGetVenueSearch.class);
                AIOLog.d("mOFGetVenueSearch:" + mOFGetVenueSearch);
                if (mOFGetVenueSearch == null)
                    return;
                else {
                    ArrayList<String> fids = new ArrayList<String>();
                    mSpots.clear();
                    for (int i = 0; i < mOFGetVenueSearch.response.venues.size(); i++) {
                        fids.add(mOFGetVenueSearch.response.venues.get(i).id);
                        Spot newSpot = new Spot();
                        newSpot.Spot_Address = mOFGetVenueSearch.response.venues.get(i).location.address;
                        newSpot.Spot_Name = mOFGetVenueSearch.response.venues.get(i).name;
                        newSpot.Spot_Icon = mOFGetVenueSearch.response.venues.get(i).categories.get(0).icon.prefix
                                + mOFGetVenueSearch.response.venues.get(i).categories.get(0).icon.suffix;
                        newSpot.Total_Bravos = 0;
                        newSpot.Spot_Latitude = mOFGetVenueSearch.response.venues.get(i).location.lat;
                        newSpot.Spot_Longitude = mOFGetVenueSearch.response.venues.get(i).location.lon;
                        newSpot.Spot_Source = "foursqure";
                        newSpot.Spot_Phone = mOFGetVenueSearch.response.venues.get(i).contact.phone;
                        newSpot.Spot_Type = mOFGetVenueSearch.response.venues.get(i).categories.get(0).name;
                        mSpots.add(newSpot);
                    }
                    requestSpotSearch(fids);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        request.execute(url);
    }

    public void onBack(int mode) {
        mMode = SEARCH_FOR_SPOT;
        listViewResult.setVisibility(View.GONE);
        btnBack.setVisibility(View.GONE);
        layoutQuickSearchOptions.setVisibility(View.VISIBLE);
    }

    public void addSpotIconDefault(ArrayList<Spot> mSpots) {
        for (int i = 0; i < mSpots.size(); i++) {
            mSpots.get(i).Spot_Icon = null;
        }
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

    public void onSearch(String key) {
        if (mMode == SEARCH_FOR_SPOT) {
            requestGet4squareVenueSearch(key);

        } else if (mMode == SEARCH_LOCAL_BRAVO) {
            requestBravoSearch(key, SEARCH_LOCAL_BRAVO_KEY);
        }
        else if (mMode == SEARCH_ARROUND_ME) {
        }
        else if (mMode == SEARCH_PEOPLE_FOLLOWING) {
            requestBravoSearch(key, SEARCH_PEOPLE_FOLLOWING_KEY);
        }
    }

    @Override
    public void goToAddMySpot() {
        // TODO Auto-generated method stub
        mHomeActionListener.goToAddSpot();
    }
}
