package com.sharebravo.bravo.view.fragment.userprofile;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.sharebravo.bravo.view.adapter.AdapterPostList.IClickUserAvatar;
import com.sharebravo.bravo.view.adapter.AdapterUserBravos;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentHistory extends FragmentBasic implements IClickUserAvatar, LocationListener {
    private XListView           mListviewHistory    = null;

    private AdapterUserBravos   mAdapterPost        = null;

    private HomeActionListener  mHomeActionListener = null;
    private ObGetUserTimeline   mObGetUserTimeline  = null;

    private SessionLogin        mSessionLogin       = null;

    private int                 mLoginBravoViaType  = BravoConstant.NO_LOGIN_SNS;

    private ObGetUserInfo       mUserInfo           = null;

    private Location            mLocation           = null;
    private LocationManager     mLocationManager    = null;
    private String              mProvider;
    private Button              mBtnBack            = null;
    private boolean             isOutOfDataLoadMore;
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

                                                        @Override
                                                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                                            mHomeActionListener.goToRecentPostDetail(mObGetUserTimeline.data.get(position - 1));
                                                        }
                                                    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_history, container);

        intializeView(root);
        mHomeActionListener = (HomeActivity) getActivity();
        mBtnBack = (Button) root.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        /* request news */
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        mLocationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        mProvider = mLocationManager.getBestProvider(criteria, true);

        // Getting Current Location
        mLocation = mLocationManager.getLastKnownLocation(mProvider);

        if (mLocation != null) {
            onLocationChanged(mLocation);
        }

        mLocationManager.requestLocationUpdates(mProvider, 20000, 0, this);
        return root;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!isBackStatus())
                requestGetUserTimeLine(mUserInfo.data.User_ID);
        } else {
            isOutOfDataLoadMore = false;
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
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_TIMELINE.replace("{User_ID}", checkingUserId);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                onStopPullAndLoadListView();
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetUserTimeline = gson.fromJson(response.toString(), ObGetUserTimeline.class);
                AIOLog.d("obGetUserTimeline:" + mObGetUserTimeline);
                if (mObGetUserTimeline == null || mObGetUserTimeline.data.size() == 0) {
                    mAdapterPost.updateRecentPostList(null);
                    return;
                } else {
                    ArrayList<ObBravo> obBravos = modifyIncorrectBravoItems(mObGetUserTimeline.data);
                    // addUserNameBravoItems(obBravos);
                    mAdapterPost.updateRecentPostList(obBravos);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                onStopPullAndLoadListView();
            }
        }, params, true);
        getTimeline.execute(url);

    }

    private ArrayList<ObBravo> modifyIncorrectBravoItems(ArrayList<ObBravo> bravoItems) {
        ArrayList<ObBravo> obBravos = new ArrayList<ObBravo>();
        for (ObBravo obBravo : bravoItems) {
            // if (StringUtility.isEmpty(obBravo.User_ID) || (StringUtility.isEmpty(obBravo.Full_Name) || "0".equals(obBravo.User_ID))) {
            AIOLog.e("The incorrect bravo items:" + obBravo.User_ID + ", obBravo.Full_Name:" + obBravo.Full_Name);
            obBravo.User_ID = mUserInfo.data.User_ID;
            obBravo.Full_Name = mUserInfo.data.Full_Name;
            obBravo.Profile_Img_URL = mUserInfo.data.Profile_Img_URL;
            // }
            obBravos.add(obBravo);
        }
        return obBravos;
    }

    private void intializeView(View root) {
        mAdapterPost = new AdapterUserBravos(getActivity(), null);
        mListviewHistory = (XListView) root.findViewById(R.id.listview_history);
        mListviewHistory.setOnItemClickListener(onItemClickListener);
        mListviewHistory.setAdapter(mAdapterPost);
        mListviewHistory.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                // AIOLog.d("IOnRefreshListener");
                // if (mObGetUserTimeline == null) {
                // onStopPullAndLoadListView();
                // return;
                // }
                // int size = mObGetUserTimeline.data.size();
                // if (size > 0)
                // onPullDownToRefreshBravoItems(true, 0);
                // else
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                if (mObGetUserTimeline == null) {
                    onStopPullAndLoadListView();
                    return;
                }
                AIOLog.d("IOnRefreshListener:" + mObGetUserTimeline.data.size());
                int size = mObGetUserTimeline.data.size();
                if (size > 0 && !isOutOfDataLoadMore)
                    onPullDownToRefreshBravoItems(false, size);
                else
                    onStopPullAndLoadListView();

            }
        });
    }

    private void onPullDownToRefreshBravoItems(final boolean isPulDownToRefresh, int position) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", position + "");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_TIMELINE.replace("{User_ID}", mUserInfo.data.User_ID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserTimeline obGetUserTimeline = gson.fromJson(response.toString(), ObGetUserTimeline.class);
                AIOLog.d("obGetUserTimeline:" + obGetUserTimeline);
                if (obGetUserTimeline == null || obGetUserTimeline.data.size() == 0) {
                    if (!isPulDownToRefresh)
                        isOutOfDataLoadMore = true;
                } else {
                    ArrayList<ObBravo> obBravos = modifyIncorrectBravoItems(obGetUserTimeline.data);
                    if (!isPulDownToRefresh)
                        mObGetUserTimeline.data.addAll(obBravos);
                    else
                        mObGetUserTimeline.data.addAll(0, obBravos);
                    mAdapterPost.updatePullDownLoadMorePostList(obBravos, isPulDownToRefresh);
                }
                onStopPullAndLoadListView();
                return;
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                onStopPullAndLoadListView();
            }
        }, params, true);
        getTimeline.execute(url);

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
        // mLat = location.getLatitude();
        //
        // // Getting longitude
        // mLong = location.getLongitude();
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

    // public void addUserNameBravoItems(ArrayList<ObBravo> bravoItems) {
    // if (bravoItems == null)
    // return;
    // for (int i = 0; i < bravoItems.size(); i++) {
    // bravoItems.get(i).Profile_Img_URL = mUserInfo.data.Profile_Img_URL;
    // bravoItems.get(i).Full_Name = mUserInfo.data.Full_Name;
    // bravoItems.get(i).User_ID = mUserInfo.data.User_ID;
    // }
    // }

    public ObGetUserInfo getmUserInfo() {
        return mUserInfo;
    }

    public void setmUserInfo(ObGetUserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
    }
}
