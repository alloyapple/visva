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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetNotification.Notification;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.model.response.ObGetNotificationSearch;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.NetworkUtility;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterHomeNotification;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentHomeNotification extends FragmentBasic implements com.sharebravo.bravo.view.adapter.AdapterBravoList.IClickUserAvatar {
    private XListView               mListViewNotifications;
    private TextView                mTextNoNotifications;
    private Button                  mBtnCloseNotifications;
    private AdapterHomeNotification mAdapterHomeNotification;
    private LinearLayout            mLayoutPoorConnection;
    private ArrayList<Notification> mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_home_notification, container);
        initializeView(root);
        mHomeActionListener = (HomeActivity) getActivity();
        mListViewNotifications.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                onClickItem(arg2);
            }
        });
        return root;
    }

    public void onClickItem(int pos) {
        if (mData.get(pos).Notification_Type.equals("follow") || mData.get(pos).Notification_Type.equals("mylist")) {
            mHomeActionListener.goToUserData(mData.get(pos).User_ID);
        } else if (mData.get(pos).Notification_Type.equals("comment") || mData.get(pos).Notification_Type.equals("bravo")) {
            requestGetBravo(mData.get(pos).Bravo_ID);
        } else {

        }
    }

    private void requestGetBravo(String Bravo_ID) {

        BravoRequestManager.getInstance(getActivity()).requestToGetBravoForSpotDetail(getActivity(), Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetBravo obGetBravo = gson.fromJson(response.toString(), ObGetBravo.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetBravo);
                if (obGetBravo == null)
                    return;
                else {
                    mHomeActionListener.goToRecentPostDetail(obGetBravo.data);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, this);
    }

    private void initializeView(View root) {
        mLayoutPoorConnection = (LinearLayout) root.findViewById(R.id.layout_poor_connection);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            mLayoutPoorConnection.setVisibility(View.GONE);
        } else {
            mLayoutPoorConnection.setVisibility(View.VISIBLE);
        }
        mListViewNotifications = (XListView) root.findViewById(R.id.listview_home_notification);
        mTextNoNotifications = (TextView) root.findViewById(R.id.text_no_notification);
        mBtnCloseNotifications = (Button) root.findViewById(R.id.btn_home_close_notification);
        mBtnCloseNotifications.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.closePageHomeNotification();
            }
        });
        mAdapterHomeNotification = new AdapterHomeNotification(getActivity());
        mAdapterHomeNotification.setListener(this);
        mListViewNotifications.setAdapter(mAdapterHomeNotification);
        mListViewNotifications.setXListViewListener(new IXListViewListener() {

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
        mListViewNotifications.stopRefresh();
        mListViewNotifications.stopLoadMore();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isBackStatus() && NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            onRequestListHomeNotification();
            putUpdateBadgeNum();
        }
    }

    public void onRequestListHomeNotification() {
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        AIOLog.d("mUserId:" + _sessionLogin.userID + ", mAccessToken:" + _sessionLogin.accessToken);
        if (StringUtility.isEmpty(_sessionLogin.userID) || StringUtility.isEmpty(_sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        // subParams.put("Full_Name", keyName);
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_NOTIFICATION_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetNotificationSearch(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getNotificationSearch = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("get notification:" + response);
                if (StringUtility.isEmpty(response))
                    return;
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetNotificationSearch mObGetNotificationSearch = gson.fromJson(response.toString(), ObGetNotificationSearch.class);
                if (mObGetNotificationSearch == null) {
                    AIOLog.e("obGetNotification is null");
                    return;
                }
                AIOLog.d("obGetNotification status:" + mObGetNotificationSearch.status);
                switch (mObGetNotificationSearch.status) {
                case BravoConstant.STATUS_SUCCESS:
                    if (mObGetNotificationSearch.data == null || mObGetNotificationSearch.data.size() <= 0) {
                        mTextNoNotifications.setVisibility(View.VISIBLE);
                        mListViewNotifications.setVisibility(View.GONE);

                    } else {
                        mTextNoNotifications.setVisibility(View.GONE);
                        mListViewNotifications.setVisibility(View.VISIBLE);
                        mData = mObGetNotificationSearch.data;
                        mAdapterHomeNotification.updateNotificationList(mObGetNotificationSearch.data);
                    }
                    break;
                case BravoConstant.STATUS_FAILED:
                    showToast(getActivity().getResources().getString(R.string.get_notification_error));
                    break;
                default:
                    break;
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getNotificationSearch.execute(url);
    }

    private void putUpdateBadgeNum() {
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Badge_Num", "0");
        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();

        String putUserUrl = BravoWebServiceConfig.URL_PUT_USER.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AIOLog.d("putUserUrl:" + putUserUrl);
        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPut postRegister = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("reponse after uploading image:" + response);
                /* go to home screen */
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postRegister.execute(putUserUrl);

    }

    @Override
    public void onClickUserAvatar(String userId) {
        mHomeActionListener.goToUserData(userId);
    }
}
