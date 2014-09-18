package com.sharebravo.bravo.view.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.ObGetUserTimeline;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterPostList;
import com.sharebravo.bravo.view.adapter.AdapterPostList.IClickUserAvatar;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentHistory extends FragmentBasic implements IClickUserAvatar, LocationListener {
    private XListView          mListviewHistory    = null;

    private AdapterPostList    mAdapterPost        = null;

    private HomeActionListener mHomeActionListener = null;
    private ObGetUserTimeline  mObGetUserTimeline  = null;

    private SessionLogin       mSessionLogin       = null;

    private int                mLoginBravoViaType  = BravoConstant.NO_LOGIN_SNS;

    private ObGetUserInfo      mUserInfo           = null;

    Location                   location            = null;
    LocationManager            locationManager     = null;
    double                     mLat, mLong;
    String                     provider;
    Button                     btnBack             = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_history, container);

        intializeView(root);
        mHomeActionListener = (HomeActivity) getActivity();
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        /* request news */
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        return root;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestGetUserTimeLine(mUserInfo.data.User_ID);
        }
    }

    private void requestGetUserTimeLine(String checkingUserId) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        // subParams.put("Location", String.valueOf(mLat) + "," + String.valueOf(mLong));
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_TIMELINE.replace("{User_ID}", checkingUserId);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                // AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                // ObGetUserTimeline obGetUserTimeline;
                mObGetUserTimeline = gson.fromJson(response.toString(), ObGetUserTimeline.class);
                AIOLog.d("obGetUserTimeline:" + mObGetUserTimeline);
                if (mObGetUserTimeline == null || mObGetUserTimeline.data.size() == 0) {
                    mAdapterPost.updateRecentPostList(null);
                    return;
                }
                else {
                    // ArrayList<ObBravo> obBravos = removeIncorrectBravoItems(mObGetUserTimeline.data);
                    addUserNameBravoItems(mObGetUserTimeline.data);
                    mAdapterPost.updateRecentPostList(mObGetUserTimeline.data);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getTimeline.execute(url);

    }

    private void intializeView(View root) {
        mAdapterPost = new AdapterPostList(getActivity(), null);
        mAdapterPost.setListener(this);
        mListviewHistory = (XListView) root.findViewById(R.id.listview_history);
        mListviewHistory.setAdapter(mAdapterPost);
        mListviewHistory.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                onStopPullAndLoadListView();
            }
        });
    }

    private void onStopPullAndLoadListView() {
        mListviewHistory.stopRefresh();
        mListviewHistory.stopLoadMore();
    }

    @Override
    public void onClickUserAvatar(String userId) {
        mHomeActionListener.goToUserData(userId);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();

        // Getting longitude
        mLong = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void addUserNameBravoItems(ArrayList<ObBravo> bravoItems) {
        if (bravoItems == null)
            return;
        for (int i = 0; i < bravoItems.size(); i++) {
            bravoItems.get(i).Profile_Img_URL = mUserInfo.data.Profile_Img_URL;
            bravoItems.get(i).Full_Name = mUserInfo.data.Full_Name;
        }
    }

    public ObGetUserInfo getmUserInfo() {
        return mUserInfo;
    }

    public void setmUserInfo(ObGetUserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }
}
