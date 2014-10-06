package com.sharebravo.bravo.view.fragment.bravochecking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.ActivityBravoChecking;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.foursquare.FactoryFoursquareParams;
import com.sharebravo.bravo.foursquare.models.OFGetVenueSearch;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpGet;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpResponseProcess;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObAllowBravo;
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
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentBravoSearch extends FragmentBasic implements LocationListener, SpotSearchListener {
    public static int               SEARCH_FOR_SPOT             = 0;
    public static int               SEARCH_LOCAL_BRAVO          = 1;
    public static int               SEARCH_LOCAL_BRAVO_KEY      = 2;
    public static int               SEARCH_ARROUND_ME           = 3;
    public static int               SEARCH_ARROUND_KEY          = 4;
    public static int               SEARCH_PEOPLE_FOLLOWING     = 5;
    public static int               SEARCH_PEOPLE_FOLLOWING_KEY = 6;
    private SessionLogin            mSessionLogin               = null;
    private int                     mLoginBravoViaType          = BravoConstant.NO_LOGIN_SNS;
    private EditText                textboxSearch               = null;
    private Button                  mBtnClose;

    private LinearLayout            layoutQuickSearchOptions    = null;
    private AdapterSearchSpotResult mAdapter;
    private XListView               listViewResult;
    private static final String     CategoryIDs                 = "4bf58dd8d48988d1e7931735,4bf58dd8d48988d1e8931735,4bf58dd8d48988d1a1941735,4d4b7105d754a06374d81259,4d4b7105d754a06376d81259,4bf58dd8d48988d160941735,4d4b7105d754a06378d81259,4bf58dd8d48988d1fa931735,4bf58dd8d48988d104941735";
    private TextView                btnLocalBravos;
    private TextView                btnAroundMe;
    private TextView                btnPeopleFollowing;

    private ArrayList<Spot>         mSpots                      = new ArrayList<Spot>();
    private Location                location                    = null;
    private LocationManager         locationManager             = null;
    private OnItemClickListener     iSpotClickListener          = new OnItemClickListener() {

                                                                    @Override
                                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                        if (position < mAdapter.getCount())
                                                                            mBravoCheckingListener.goToMapView((mSpots.get(position - 1)),
                                                                                    FragmentBravoMap.MAKER_BY_LOCATION_SPOT);
                                                                    }
                                                                };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_bravo_tab, container);
        mBravoCheckingListener = (ActivityBravoChecking) getActivity();
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
        textboxSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listViewResult.setVisibility(View.GONE);
                layoutQuickSearchOptions.setVisibility(View.VISIBLE);
            }
        });
        layoutQuickSearchOptions = (LinearLayout) root.findViewById(R.id.layout_quicksearch_options);
        listViewResult = (XListView) root.findViewById(R.id.listview_result_search);
        listViewResult.setOnItemClickListener(iSpotClickListener);
        listViewResult.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                AIOLog.d("IOnRefreshListener");
                onStopPullAndLoadListView();
            }
        });
        listViewResult.setFooterDividersEnabled(false);
        mAdapter = new AdapterSearchSpotResult(getActivity());
        mAdapter.setListener(this);
        listViewResult.setAdapter(mAdapter);

        btnLocalBravos = (TextView) root.findViewById(R.id.text_local_bravo);
        btnLocalBravos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                mBtnClose.setVisibility(View.VISIBLE);
                requestBravoSearch("", SEARCH_LOCAL_BRAVO);
            }
        });
        btnAroundMe = (TextView) root.findViewById(R.id.text_around_me);
        btnAroundMe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                mBtnClose.setVisibility(View.VISIBLE);
                requestGet4squareVenueSearch(null, SEARCH_ARROUND_ME);
            }
        });
        btnPeopleFollowing = (TextView) root.findViewById(R.id.text_people_following);
        btnPeopleFollowing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                mBtnClose.setVisibility(View.VISIBLE);
                requestBravoSearch("", SEARCH_PEOPLE_FOLLOWING);
            }
        });
        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mBtnClose = (Button) root.findViewById(R.id.btn_back);
        mBtnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mBravoCheckingListener.goToBack();
            }
        });
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
        super.onHiddenChanged(hidden);
        if (!hidden && !isBackStatus()) {
            listViewResult.setVisibility(View.GONE);
            layoutQuickSearchOptions.setVisibility(View.GONE);
            BravoRequestManager.getInstance(getActivity()).getNumberAllowBravo(getActivity(), new IRequestListener() {

                @Override
                public void onResponse(String response) {
                    if (StringUtility.isEmpty(response))
                        return;
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    ObAllowBravo obAllowBravo = gson.fromJson(response.toString(), ObAllowBravo.class);
                    if (obAllowBravo == null)
                        return;
                    AIOLog.d("obAllowBravo.data.Allow_Bravo:" + obAllowBravo.data.Allow_Bravo);
                    int numberAllowBravo = obAllowBravo.data.Allow_Bravo;
                    if (numberAllowBravo > 0) {
                        requestGet4squareVenueSearch(null, SEARCH_ARROUND_ME);
                    } else {
                        showDialogSpentBravoADay();
                    }
                }

                @Override
                public void onErrorResponse(String errorMessage) {

                }
            }, FragmentBravoSearch.this);
        }
    }

    public void showDialogSpentBravoADay() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_spent_bravo_today, null);
        Button btnYes = (Button) dialog_view.findViewById(R.id.btn_ok);
        btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
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
    public void onResume() {
        super.onResume();
    }

    public void requestSpotSearch(ArrayList<String> mVenues, final int mode) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + mSessionLogin.userID + ", mAccessToken:" + mSessionLogin.accessToken);
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("FID", mVenues);
        subParams.put("Source", "foursquare");

        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_SPOT_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getSpotSearch = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            final int ownMode = mode;

            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("getSpotSearch:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();

                ObGetSpotSearch mObGetSpotSearch = gson.fromJson(response.toString(), ObGetSpotSearch.class);
                AIOLog.d("mObGetSpotSearch:" + mObGetSpotSearch);
                if (mObGetSpotSearch == null) {
                    return;
                } else {
                    layoutQuickSearchOptions.setVisibility(View.GONE);
                    listViewResult.setVisibility(View.VISIBLE);
                    mBtnClose.setVisibility(View.VISIBLE);
                    mergeData(mSpots, mObGetSpotSearch.data);
                    mAdapter.updateData(mSpots, ownMode);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getSpotSearch.execute(url);

    }

    private double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180 / 3.14169);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        float t1 = FloatMath.cos(a1) * FloatMath.cos(a2) * FloatMath.cos(b1) * FloatMath.cos(b2);
        float t2 = FloatMath.cos(a1) * FloatMath.sin(a2) * FloatMath.cos(b1) * FloatMath.sin(b2);
        float t3 = FloatMath.sin(a1) * FloatMath.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt / 1000;
    }

    public void mergeData(ArrayList<Spot> mF, ArrayList<Spot> mA) {
        for (int i = 0; i < mA.size(); i++) {
            for (int j = 0; j < mF.size(); j++)
            {
                if (mA.get(i).Spot_FID.equals(mF.get(j).Spot_FID)) {
                    mF.set(j, mA.get(i));
                    mA.remove(i);
                    i--;
                    break;
                }
            }
        }
        if (mA.size() > 0)
            mF.addAll(mA);
        sortSpotWithDistance(mF);
    }

    public void sortSpotWithDistance(ArrayList<Spot> mF) {
        float lLat = (float) location.getLatitude();
        float lLon = (float) location.getLongitude();
        int n = mF.size();
        int i, j, min;
        // Spot temp = new Spot();
        for (i = 0; i < n - 1; i++) {
            min = i;
            for (j = i + 1; j < n; j++) {
                double dMin = gps2m(lLat, lLon, (float) mF.get(min).Spot_Latitude, (float) mF.get(min).Spot_Longitude);
                double dJ = gps2m(lLat, lLon, (float) mF.get(j).Spot_Latitude, (float) mF.get(j).Spot_Latitude);
                if (dJ < dMin)
                    min = j;
            }
            swap(mF, i, min);
        }
    }

    public void swap(ArrayList<Spot> mF, int i, int j) {
        Spot temp = new Spot();
        temp = mF.get(i);
        mF.set(i, mF.get(j));
        mF.set(j, temp);

    }

    public void requestBravoSearch(String nameSpot, final int mode) {
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
        if (mode == SEARCH_LOCAL_BRAVO || mode == SEARCH_LOCAL_BRAVO_KEY)
            subParams.put("Global", "TRUE");
        if (mode == SEARCH_LOCAL_BRAVO_KEY || mode == SEARCH_PEOPLE_FOLLOWING_KEY)
            subParams.put("Name", nameSpot);
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_BRAVO_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getBravoSearch = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            int ownMode = mode;

            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("getBravoSearch:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpotSearch mObGetSpotSearch = gson.fromJson(response.toString(), ObGetSpotSearch.class);
                AIOLog.d("getBravoSearch:" + mObGetSpotSearch);
                if (mObGetSpotSearch == null) {
                    return;
                } else {
                    layoutQuickSearchOptions.setVisibility(View.GONE);
                    listViewResult.setVisibility(View.VISIBLE);
                    mBtnClose.setVisibility(View.VISIBLE);
                    addSpotIconDefault(mObGetSpotSearch.data);
                    mSpots = mObGetSpotSearch.data;
                    mAdapter.updateData(mSpots, ownMode);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getBravoSearch.execute(url);

    }

    private void requestGet4squareVenueSearch(String query, final int mode) {
        String url = BravoWebServiceConfig.URL_FOURSQUARE_GET_VENUE_SEARCH;
        List<NameValuePair> params = null;
        if (mode == SEARCH_FOR_SPOT)
            params = FactoryFoursquareParams
                    .createSubParamsRequestSearchVenue(location.getLatitude(), location.getLongitude(), query);
        if (mode == SEARCH_ARROUND_ME)
            params = FactoryFoursquareParams
                    .createSubParamsRequestSearchArroundMe(location.getLatitude(), location.getLongitude(), CategoryIDs);
        if (mode == SEARCH_ARROUND_KEY)
            params = FactoryFoursquareParams
                    .createSubParamsRequestSearchArroundMeQuery(location.getLatitude(), location.getLongitude(), query);
        FAsyncHttpGet request = new FAsyncHttpGet(getActivity(), new FAsyncHttpResponseProcess(getActivity()) {
            final int ownMode = mode;

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
                        newSpot.Spot_FID = mOFGetVenueSearch.response.venues.get(i).id;
                        newSpot.Spot_Address = mOFGetVenueSearch.response.venues.get(i).location.address;
                        newSpot.Spot_Name = mOFGetVenueSearch.response.venues.get(i).name;

                        if (mOFGetVenueSearch.response.venues.get(i).categories != null
                                && mOFGetVenueSearch.response.venues.get(i).categories.size() > 0) {
                            newSpot.Spot_Icon = mOFGetVenueSearch.response.venues.get(i).categories.get(0).icon.prefix + "bg_44"
                                    + mOFGetVenueSearch.response.venues.get(i).categories.get(0).icon.suffix;
                            newSpot.Spot_Type = mOFGetVenueSearch.response.venues.get(i).categories.get(0).name;
                        } else {
                            newSpot.Spot_Type = "Restaurant";
                        }
                        newSpot.Spot_Genre = "Genre";

                        newSpot.Total_Bravos = 0;
                        double lat = mOFGetVenueSearch.response.venues.get(i).location.lat;
                        double lon = mOFGetVenueSearch.response.venues.get(i).location.lon;

                        newSpot.Spot_Latitude = lat;
                        newSpot.Spot_Longitude = lon;

                        newSpot.Spot_Source = "foursqure";
                        newSpot.Spot_Phone = mOFGetVenueSearch.response.venues.get(i).contact.phone;

                        mSpots.add(newSpot);
                    }

                    requestSpotSearch(fids, ownMode);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        request.execute(url);
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
        requestGet4squareVenueSearch(key, SEARCH_FOR_SPOT);
    }

    private void onStopPullAndLoadListView() {
        listViewResult.stopRefresh();
        listViewResult.stopLoadMore();
    }

    @Override
    public void goToAddMySpot() {
        mBravoCheckingListener.goToAddSpot();
    }

    @Override
    public void onLocationChanged(Location arg0) {

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

    public void addSpotIconDefault(ArrayList<Spot> mSpots) {
        for (int i = 0; i < mSpots.size(); i++) {
            mSpots.get(i).Spot_Icon = null;
        }
    }
}
