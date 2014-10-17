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
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.NetworkUtility;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterFavourite;
import com.sharebravo.bravo.view.adapter.AdapterFavourite.IClickUserAvatar;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentFavourite extends FragmentBasic implements IClickUserAvatar, LocationListener {
    // =======================Constant Define==============
    // =======================Class Define=================
    private SessionLogin             mSessionLogin             = null;
    private ObGetAllBravoRecentPosts mObGetAllBravoRecentPosts = null;
    private AdapterFavourite         mAdapterFavourite;
    // =======================Variable Define==============
    private Button                   mBtnSortByLocation;
    private Button                   mBtnSortByDate;
    private Double                   mLat, mLong;
    private Button                   mBtnBack;
    private XListView                mFavouriteListView;
    private LinearLayout             mLayoutNoFavorite;
    private boolean                  isSortByDate;
    private LinearLayout             mLayoutPoorConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_favourite, container);
        mHomeActionListener = (HomeActivity) getActivity();
        initializeView(root);
        initLocation();
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initLocation();
            if (!isBackStatus() && NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
                requestUserFavouriteSortByLocation();
            }
            isSortByDate = false;
            mBtnSortByDate.setBackgroundResource(R.drawable.btn_share_1);
            mBtnSortByLocation.setBackgroundResource(R.drawable.btn_save_2);
            if (mObGetAllBravoRecentPosts == null || mObGetAllBravoRecentPosts.data.size() == 0) {
                mLayoutNoFavorite.setVisibility(View.VISIBLE);
                mFavouriteListView.setVisibility(View.GONE);
            } else {
                mLayoutNoFavorite.setVisibility(View.GONE);
                mFavouriteListView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void requestUserFavouriteSortByDate() {
        int loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), loginBravoViaType);
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
        String url = BravoWebServiceConfig.URL_GET_USER_MYLIST.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetFavorites(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getGetFavourites = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetAllBravoRecentPosts = gson.fromJson(response.toString(), ObGetAllBravoRecentPosts.class);
                if (mObGetAllBravoRecentPosts == null)
                    return;
                else {
                    AIOLog.d("size of recent post list by date: " + mObGetAllBravoRecentPosts.data.size());
                    if (mObGetAllBravoRecentPosts.data.size() > 0) {
                        mAdapterFavourite.updateRecentPostList(mObGetAllBravoRecentPosts, true, mLat, mLong);
                        if (mFavouriteListView.getVisibility() == View.GONE) {
                            mFavouriteListView.setVisibility(View.VISIBLE);
                            mLayoutNoFavorite.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                mFavouriteListView.stopLoadMore();
                mFavouriteListView.stopRefresh();
            }
        }, params, true);

        getGetFavourites.execute(url);

    }

    private void requestUserFavouriteSortByLocation() {
        int loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), loginBravoViaType);
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + mSessionLogin.userID + ", mAccessToken:" + mSessionLogin.accessToken);
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        subParams.put("Location", mLat + "," + mLong);
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_MYLIST.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetFavorites(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getGetFavourites = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetAllBravoRecentPosts = gson.fromJson(response.toString(), ObGetAllBravoRecentPosts.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + mObGetAllBravoRecentPosts);
                if (mObGetAllBravoRecentPosts == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + mObGetAllBravoRecentPosts.data.size());
                    if (mObGetAllBravoRecentPosts.data.size() > 0) {
                        ArrayList<ObBravo> obBravos = BravoUtils.removeIncorrectBravoItems(mObGetAllBravoRecentPosts.data);
                        mObGetAllBravoRecentPosts.data = obBravos;
                        mAdapterFavourite.updateRecentPostList(mObGetAllBravoRecentPosts, false, mLat, mLong);
                        if (mFavouriteListView.getVisibility() == View.GONE) {
                            mFavouriteListView.setVisibility(View.VISIBLE);
                            mLayoutNoFavorite.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);

        getGetFavourites.execute(url);
    }

    private void initializeView(View root) {
        mLayoutPoorConnection = (LinearLayout) root.findViewById(R.id.layout_poor_connection);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            mLayoutPoorConnection.setVisibility(View.GONE);
        } else {
            mLayoutPoorConnection.setVisibility(View.VISIBLE);
        }
        mBtnBack = (Button) root.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mBtnSortByDate = (Button) root.findViewById(R.id.btn_sort_by_date);
        mBtnSortByLocation = (Button) root.findViewById(R.id.btn_sort_by_location);
        mFavouriteListView = (XListView) root.findViewById(R.id.listview_fragment_favourite);
        mAdapterFavourite = new AdapterFavourite(getActivity(), mObGetAllBravoRecentPosts);
        mAdapterFavourite.setListener(this);
        mFavouriteListView.setAdapter(mAdapterFavourite);
        mLayoutNoFavorite = (LinearLayout) root.findViewById(R.id.layout_no_favourite);
        //
        mFavouriteListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AIOLog.d("position:" + position);
                mHomeActionListener.goToRecentPostDetail(mObGetAllBravoRecentPosts.data
                        .get(position - 1));

            }
        });
        mFavouriteListView.setVisibility(View.GONE);
        /* load more old items */
        mFavouriteListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                int size = mObGetAllBravoRecentPosts.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(size - 1), true);
                else
                    mFavouriteListView.stopRefresh();
                AIOLog.d("IOnLoadMoreListener");
            }

            @Override
            public void onLoadMore() {
                AIOLog.d("IOnRefreshListener");
                int size = mObGetAllBravoRecentPosts.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(0), false);
                else
                    mFavouriteListView.stopLoadMore();
            }
        });
        mBtnSortByDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!isSortByDate) {
                    isSortByDate = true;
                    mBtnSortByDate.setBackgroundResource(R.drawable.btn_share_2);
                    mBtnSortByLocation.setBackgroundResource(R.drawable.btn_save_1);
                    requestUserFavouriteSortByDate();
                }
            }
        });
        mBtnSortByLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isSortByDate) {
                    isSortByDate = false;
                    mBtnSortByDate.setBackgroundResource(R.drawable.btn_share_1);
                    mBtnSortByLocation.setBackgroundResource(R.drawable.btn_save_2);
                    requestUserFavouriteSortByLocation();
                }
            }
        });
    }

    private void onPullDownToRefreshBravoItems(ObBravo obBravo, boolean b) {
        onStopPullAndLoadListView();
    }

    private void onStopPullAndLoadListView() {
        mFavouriteListView.stopRefresh();
        mFavouriteListView.stopLoadMore();
    }

    @Override
    protected void onClickGoToFragment() {
        super.onClickGoToFragment();
    }

    @Override
    public void onClickUserAvatar(String userId) {

    }

    private void requestDeleteMyListItem(String bravoID) {
        BravoRequestManager.getInstance(getActivity()).requestDeleteMyListItem(bravoID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response putFollow :===>" + response);
                showToast("deleted");
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage: " + errorMessage);
            }
        }, FragmentFavourite.this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();
        mLong = location.getLongitude();
    }

    public void initLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);
        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();
        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);
        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        if (location == null)
            return;
        mLat = location.getLatitude();
        mLong = location.getLongitude();

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

    @Override
    public void deleteItem(String bravo_ID) {
        if (mObGetAllBravoRecentPosts == null)
            return;
        for (int i = 0; i < mObGetAllBravoRecentPosts.data.size(); i++)
            if (mObGetAllBravoRecentPosts.data.get(i).Bravo_ID.equals(bravo_ID))
                mAdapterFavourite.remove(i);
        requestDeleteMyListItem(bravo_ID);
    }
}
