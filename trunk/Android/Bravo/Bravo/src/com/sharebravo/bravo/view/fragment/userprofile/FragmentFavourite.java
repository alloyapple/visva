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
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterFavourite;
import com.sharebravo.bravo.view.adapter.AdapterFavourite.IClickUserAvatar;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.ContextualUndoAdapter;
import com.sharebravo.bravo.view.lib.PullAndLoadListView;
import com.sharebravo.bravo.view.lib.PullAndLoadListView.IOnLoadMoreListener;
import com.sharebravo.bravo.view.lib.PullToRefreshListView.IOnRefreshListener;

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
    private PullAndLoadListView      mFavouriteListView;
    private OnItemClickListener      iRecentPostClickListener  = new OnItemClickListener() {

                                                                   @Override
                                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                       AIOLog.d("position:" + position);
                                                                       mHomeActionListener.goToRecentPostDetail(mObGetAllBravoRecentPosts.data
                                                                               .get(position - 1));
                                                                   }
                                                               };
    private boolean                  isSortByDate;

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
            requestUserFavouriteSortByDate();
            isSortByDate = true;
            mBtnSortByDate.setBackgroundResource(R.drawable.btn_share_2);
            mBtnSortByLocation.setBackgroundResource(R.drawable.btn_save_1);
        }
    }

    private void requestUserFavouriteSortByDate() {
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
        // subParams.put("Location", String.valueOf(mLat)+","+String.valueOf(mLong));
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
                    ArrayList<ObBravo> obBravos = BravoUtils.removeIncorrectBravoItems(mObGetAllBravoRecentPosts.data);
                    mObGetAllBravoRecentPosts.data = obBravos;
                    mAdapterFavourite.updateRecentPostList(mObGetAllBravoRecentPosts, true, mLat, mLong);
                    if (mFavouriteListView.getVisibility() == View.GONE)
                        mFavouriteListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
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
                    ArrayList<ObBravo> obBravos = BravoUtils.removeIncorrectBravoItems(mObGetAllBravoRecentPosts.data);
                    mObGetAllBravoRecentPosts.data = obBravos;
                    mAdapterFavourite.updateRecentPostList(mObGetAllBravoRecentPosts, false, mLat, mLong);
                    if (mFavouriteListView.getVisibility() == View.GONE)
                        mFavouriteListView.setVisibility(View.VISIBLE);
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
        mBtnBack = (Button) root.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mBtnSortByDate = (Button) root.findViewById(R.id.btn_sort_by_date);
        mBtnSortByLocation = (Button) root.findViewById(R.id.btn_sort_by_location);
        mFavouriteListView = (PullAndLoadListView) root.findViewById(R.id.listview_fragment_favourite);
        mAdapterFavourite = new AdapterFavourite(getActivity(), mObGetAllBravoRecentPosts);
        // mContextualUndoAdapter = new ContextualUndoAdapter(mAdapterFavourite, R.layout.row_recent_post, R.id.undo_row_undobutton);
        // mAdapterFavourite.setListener(this);
        // mContextualUndoAdapter.setAbsListView(mFavouriteListView);
        mFavouriteListView.setAdapter(mAdapterFavourite);
        // mContextualUndoAdapter.setDeleteItemCallback(new MyDeleteItemCallback());

        // mFavouriteListView.setAdapter(mAdapterFavourite);
        // mFavouriteListView.setOnItemClickListener(iRecentPostClickListener);
        mFavouriteListView.setVisibility(View.GONE);
        /* load more old items */
        mFavouriteListView.setOnLoadMoreListener(new IOnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                int size = mObGetAllBravoRecentPosts.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(size - 1), false);
                else
                    mFavouriteListView.onLoadMoreComplete();
                AIOLog.d("IOnLoadMoreListener");
            }
        });

        /* on refresh new items */
        /* load more old items */
        mFavouriteListView.setOnRefreshListener(new IOnRefreshListener() {

            @Override
            public void onRefresh() {
                AIOLog.d("IOnRefreshListener");
                int size = mObGetAllBravoRecentPosts.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(0), true);
                else
                    mFavouriteListView.onRefreshComplete();
            }
        });
        mBtnSortByDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub

    }

    @Override
    protected void onClickGoToFragment() {
        super.onClickGoToFragment();
    }

    @Override
    public void onClickUserAvatar(String userId) {

    }

    private class MyDeleteItemCallback implements ContextualUndoAdapter.DeleteItemCallback {

        @Override
        public void deleteItem(int position) {
            mAdapterFavourite.remove(position);
            mAdapterFavourite.notifyDataSetChanged();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
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
        mLat = location.getLatitude();
        mLong = location.getLongitude();

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }
}
