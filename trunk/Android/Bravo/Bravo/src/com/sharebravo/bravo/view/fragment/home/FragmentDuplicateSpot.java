package com.sharebravo.bravo.view.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.foursquare.FactoryFoursquareParams;
import com.sharebravo.bravo.foursquare.models.OFPostVenue;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpPost;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpResponseProcess;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetSpotSearch;
import com.sharebravo.bravo.model.response.ObPostSpot;
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
import com.sharebravo.bravo.view.adapter.AdapterDuplicateSpots;
import com.sharebravo.bravo.view.adapter.AdapterMapDuplicateSpot;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;

public class FragmentDuplicateSpot extends FragmentBasic {
    private OFPostVenue             mOFPostVenue;
    XListView                       mListViewContainMap;
    private ImageView               mSpotIcon;
    private TextView                mSpotName;
    private TextView                mSpotAddress;
    private XListView               mListviewDuplicate;
    private Button                  btnAdd;
    private Button                  btnBack;
    private Spot                    mSpot;
    private String                  ignoreDuplicatesKey;
    private ArrayList<Spot>         duplicates         = new ArrayList<Spot>();
    private AdapterDuplicateSpots   mAdapterSpots;
    private AdapterMapDuplicateSpot mAdapterMap;
    private SessionLogin            mSessionLogin      = null;
    private int                     mLoginBravoViaType = BravoConstant.NO_LOGIN_SNS;
    private OnItemClickListener     iSpotClickListener = new OnItemClickListener() {

                                                           @Override
                                                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                               mHomeActionListener.goToSpotDetail(duplicates.get(position - 1));
                                                           }
                                                       };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_duplicate, container);
        mHomeActionListener = (HomeActivity) getActivity();
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mSpotIcon = (ImageView) root.findViewById(R.id.img_spot_icon);
        mSpotName = (TextView) root.findViewById(R.id.txt_spot_name);
        mSpotAddress = (TextView) root.findViewById(R.id.duplicate_spot_address);
        mListViewContainMap = (XListView) root.findViewById(R.id.listview_map_cover_duplicate);
        mListviewDuplicate = (XListView) root.findViewById(R.id.listview_spot_duplicate);
        btnAdd = (Button) root.findViewById(R.id.btn_add_dup);
        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                onAddDuplicate();
            }
        });
        mAdapterMap = new AdapterMapDuplicateSpot(getActivity(), this);
        mListViewContainMap.setAdapter(mAdapterMap);
        mAdapterSpots = new AdapterDuplicateSpots(getActivity());

        mListviewDuplicate.setAdapter(mAdapterSpots);
        mListviewDuplicate.setOnItemClickListener(iSpotClickListener);
        return root;
    }

    public void onAddDuplicate() {
        requestPost4squareVenueSearch(mSpot.Spot_Latitude, mSpot.Spot_Longitude, mSpot.Spot_Name, mSpot.Spot_Address);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onBindData();
        }
    }

    public void onBindData() {
        ignoreDuplicatesKey = mOFPostVenue.response.ignoreDuplicatesKey;
        mSpotName.setText(mSpot.Spot_Name);
        mSpotIcon.setImageResource(R.drawable.place_icon);
        FragmentMapCover.mLat = mSpot.Spot_Latitude;
        FragmentMapCover.mLong = mSpot.Spot_Longitude;
        mAdapterMap.updateMapView();
        mSpotAddress.setText(mSpot.Spot_Address);
        final ArrayList<String> fids = new ArrayList<String>();
        duplicates.clear();
        for (int i = 0; i < mOFPostVenue.response.venues.size(); i++) {
            fids.add(mOFPostVenue.response.venues.get(i).id);
            Spot newSpot = new Spot();
            newSpot.Spot_FID = mOFPostVenue.response.venues.get(i).id;
            newSpot.Spot_Address = mOFPostVenue.response.venues.get(i).location.address;
            newSpot.Spot_Name = mOFPostVenue.response.venues.get(i).name;
            if (mOFPostVenue.response.venues.get(i).categories != null
                    && mOFPostVenue.response.venues.get(i).categories.size() > 0) {
                newSpot.Spot_Icon = mOFPostVenue.response.venues.get(i).categories.get(0).icon.prefix + "bg_44"
                        + mOFPostVenue.response.venues.get(i).categories.get(0).icon.suffix;
                newSpot.Spot_Type = mOFPostVenue.response.venues.get(i).categories.get(0).name;
            } else {
                newSpot.Spot_Type = "Restaurant";
            }
            newSpot.Spot_Genre = "Genre";
            newSpot.Total_Bravos = 0;
            newSpot.Spot_Latitude = mOFPostVenue.response.venues.get(i).location.lat;
            newSpot.Spot_Longitude = mOFPostVenue.response.venues.get(i).location.lon;
            newSpot.Spot_Source = "foursqure";
            newSpot.Spot_Phone = mOFPostVenue.response.venues.get(i).contact.phone;
            duplicates.add(newSpot);
        }
        requestSpotSearch(fids);
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
        subParams.put("FID", mVenues);
        subParams.put("Source", "foursquare");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_SPOT_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet request = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {

            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("getSpotSearch:" + response);
                Gson gson = new Gson();
                ObGetSpotSearch mObGetSpotSearch = gson.fromJson(response.toString(), ObGetSpotSearch.class);
                if (mObGetSpotSearch == null) {
                    return;
                } else {

                    // mergeData(mSpots, mObGetSpotSearch.data);
                    mAdapterSpots.updateData(duplicates);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        request.execute(url);

    }

    private void requestPost4squareVenueSearch(double lat, double lng, String name, String address) {
        String url = BravoWebServiceConfig.URL_FOURSQUARE_POST_VENUE.replace("{client_id}", FactoryFoursquareParams.client_id).replace(
                "{client_secret}", FactoryFoursquareParams.client_secret).replace("{oauth_token}", FactoryFoursquareParams.authen_token)
                .replace("{v}", FactoryFoursquareParams.v + "");
        List<NameValuePair> params = null;
        params = FactoryFoursquareParams
                .createSubParamsRequestAddVenueDuplicate(lat, lng, name, address, ignoreDuplicatesKey);
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
                        requestPostSpot(mSpot);
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
        FAsyncHttpPost request = new FAsyncHttpPost(getActivity(), new FAsyncHttpResponseProcess(getActivity()) {
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

    public OFPostVenue getOFPostVenue() {
        return mOFPostVenue;
    }

    public void setOFPostVenue(OFPostVenue mOFPostVenue) {
        this.mOFPostVenue = mOFPostVenue;
    }

    public Spot getSpot() {
        return mSpot;
    }

    public void setSpot(Spot mSpot) {
        this.mSpot = mSpot;
    }
}
