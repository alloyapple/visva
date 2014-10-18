package com.sharebravo.bravo.view.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
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
import com.sharebravo.bravo.view.adapter.AdapterBravoList;
import com.sharebravo.bravo.view.adapter.AdapterBravoList.IClickUserAvatar;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentHomeTab extends FragmentBasic implements IClickUserAvatar {
    private XListView                mListviewRecentPost       = null;
    private AdapterBravoList         mAdapterRecentPost        = null;
    private ObGetAllBravoRecentPosts mObGetAllBravoRecentPosts = null;
    private ObGetUserInfo            mObGetUserInfo;
    private Button                   mBtnHomeNotification      = null;
    private SessionLogin             mSessionLogin             = null;
    private int                      mLoginBravoViaType        = BravoConstant.NO_LOGIN_SNS;
    private boolean                  isNoFirstTime             = false;
    private OnItemClickListener      iRecentPostClickListener  = new OnItemClickListener() {

                                                                   @Override
                                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                       AIOLog.d("position:" + position);
                                                                       mHomeActionListener.goToRecentPostDetail(mObGetAllBravoRecentPosts.data
                                                                               .get(position - 1));
                                                                   }
                                                               };
    private TextView                 mNotificationIcon;
    private boolean                  isOutOfDataLoadMore;
    private String                   mRegisterId;
    private static int               mNumberOfNewNotifications = 0;
    private LinearLayout             mLayoutRecentPostText;
    private LinearLayout             mLayoutPoorConnection;
    private LinearLayout             mLayoutLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_home_tab, container);

        intializeView(root);
        /* request news */
        mHomeActionListener = (HomeActivity) getActivity();
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            requestNewsItemsOnBravoServer();
        } else {
            mObGetAllBravoRecentPosts = BravoUtils.getDataFromDb(getActivity());
            if (mObGetAllBravoRecentPosts != null) {
                ArrayList<ObBravo> obBravos = removeIncorrectBravoItems(mObGetAllBravoRecentPosts.data);
                mObGetAllBravoRecentPosts.data = obBravos;
                mAdapterRecentPost.updateRecentPostList(obBravos);
                if (mListviewRecentPost.getVisibility() == View.GONE)
                    mListviewRecentPost.setVisibility(View.VISIBLE);
            }
        }
        mRegisterId = getRegistrationId(getActivity());
        isNoFirstTime = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_BRAVO_FISRT_TIME);
        if (!isNoFirstTime) {
            showDialogWelcome();
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_BRAVO_FISRT_TIME, true);

            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_BRAVO_NOTIFICATIONS, true);
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_COMMENT_NOTIFICATIONS, true);
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOLLOW_NOTIFICATIONS, true);
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FAVOURITE_NOTIFICATIONS, true);
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_TOTAL_BRAVO_NOTIFICATIONS, true);
        }

        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            isOutOfDataLoadMore = false;
            if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
                requestGetUserInfo();
            }
        } else {
            AIOLog.d("mSessionLogin:" + mSessionLogin);
            if (mSessionLogin == null || mObGetAllBravoRecentPosts == null)
                return;
            requestNewsItemsOnBravoServer();
        }
    }

    public void requestGetUserInfo() {

        final int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        AIOLog.d("mUserId:" + _sessionLogin.userID + ", mAccessToken:" + _sessionLogin.accessToken);
        if (StringUtility.isEmpty(_sessionLogin.userID) || StringUtility.isEmpty(_sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        String url = BravoWebServiceConfig.URL_GET_USER_INFO + "/" + userId;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getUserInfoRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), null) {

            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("get user info at my data:" + response);
                if (StringUtility.isEmpty(response))
                    return;
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetUserInfo = gson.fromJson(response.toString(), ObGetUserInfo.class);
                if (mObGetUserInfo == null) {
                    AIOLog.e("obGetUserInfo is null");
                } else {
                    switch (mObGetUserInfo.status) {
                    case BravoConstant.STATUS_FAILED:
                        showToast(getActivity().getResources().getString(R.string.get_user_info_error));
                        break;
                    case BravoConstant.STATUS_SUCCESS:
                        AIOLog.d("BravoConstant.STATUS_SUCCESS");
                        if (mObGetUserInfo.data.Badge_Num <= 0)
                            mNotificationIcon.setVisibility(View.GONE);
                        else {
                            mNotificationIcon.setVisibility(View.VISIBLE);
                            mNumberOfNewNotifications += mObGetUserInfo.data.Badge_Num;
                            if (mNumberOfNewNotifications < 11) {
                                mNotificationIcon.setText(mNumberOfNewNotifications + "");
                            }
                            else
                                mNotificationIcon.setText("10+");
                        }

                        // for GCM
                        putUpdateUserProfile(mObGetUserInfo);

                        if (mObGetUserInfo.data.SNS_List.size() > 0) {
                            BravoUtils.putSNSList(getActivity(), mObGetUserInfo.data.SNS_List);
                        }
                        break;
                    default:
                        break;
                    }
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getUserInfoRequest.execute(url);
    }

    private void putUpdateUserProfile(ObGetUserInfo obGetUserInfo) {
        AIOLog.d("mRegisterId:" + mRegisterId);
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("GCM_Token", mRegisterId);

        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();

        String putUserUrl = BravoWebServiceConfig.URL_PUT_USER.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AIOLog.d("putUserUrl:" + putUserUrl);
        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPut postRegister = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("reponse after update registerId:" + response);
                /* go to home screen */
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postRegister.execute(putUserUrl);

    }

    private void requestNewsItemsOnBravoServer() {
        BravoRequestManager.getInstance(getActivity()).requestNewsItemsOnBravoServer(new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetAllBravoRecentPosts = gson.fromJson(response.toString(), ObGetAllBravoRecentPosts.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + mObGetAllBravoRecentPosts);
                if (mObGetAllBravoRecentPosts == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + mObGetAllBravoRecentPosts.data.size());
                    ArrayList<ObBravo> obBravos = removeIncorrectBravoItems(mObGetAllBravoRecentPosts.data);
                    mObGetAllBravoRecentPosts.data = obBravos;
                    mAdapterRecentPost.updateRecentPostList(obBravos);
                    if (mListviewRecentPost.getVisibility() == View.GONE)
                        mListviewRecentPost.setVisibility(View.VISIBLE);
                    if (mLayoutLoading.getVisibility() == View.VISIBLE)
                        mLayoutLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("Can not get recent posts");
            }
        });
    }

    private void intializeView(View root) {
        mLayoutPoorConnection = (LinearLayout) root.findViewById(R.id.layout_poor_connection);
        mLayoutRecentPostText = (LinearLayout) root.findViewById(R.id.layout_recent_post);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            mLayoutPoorConnection.setVisibility(View.GONE);
            mLayoutRecentPostText.setVisibility(View.VISIBLE);
        } else {
            mLayoutPoorConnection.setVisibility(View.VISIBLE);
            mLayoutRecentPostText.setVisibility(View.GONE);
        }
        mLayoutLoading = (LinearLayout) root.findViewById(R.id.layout_loading);
        mBtnHomeNotification = (Button) root.findViewById(R.id.btn_home_notification);
        mBtnHomeNotification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // show home notification tab
                mHomeActionListener.showPageHomeNotification();
                mNumberOfNewNotifications = 0;
                mNotificationIcon.setVisibility(View.GONE);
            }
        });
        mListviewRecentPost = (XListView) root.findViewById(R.id.listview_recent_post);
        mAdapterRecentPost = new AdapterBravoList(getActivity(), mObGetAllBravoRecentPosts);
        mAdapterRecentPost.setListener(this);
        mListviewRecentPost.setAdapter(mAdapterRecentPost);
        mListviewRecentPost.setOnItemClickListener(iRecentPostClickListener);
        mListviewRecentPost.setVisibility(View.GONE);

        mNotificationIcon = (TextView) root.findViewById(R.id.notification_icon);

        mListviewRecentPost.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                AIOLog.d("IOnRefreshListener");
                int size = mObGetAllBravoRecentPosts.data.size();
                if (size > 0 && NetworkUtility.getInstance(getActivity()).isNetworkAvailable())
                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(0), true);
                else
                    onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                int size = mObGetAllBravoRecentPosts.data.size();
                if (size > 0 && !isOutOfDataLoadMore && NetworkUtility.getInstance(getActivity()).isNetworkAvailable())
                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(size - 1), false);
                else
                    onStopPullAndLoadListView();
                AIOLog.d("IOnLoadMoreListener");
            }
        });
    }
    
    public void showDialogWelcome() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_welcome, null);
        Button btnStart = (Button) dialog_view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }

    private void onPullDownToRefreshBravoItems(ObBravo obBravo, final boolean isPulDownToRefresh) {
        AIOLog.d("obBravo.bravoId:" + obBravo.Bravo_ID);
        HashMap<String, String> subParams = new HashMap<String, String>();
        if (isPulDownToRefresh)
            subParams.put("Min_Bravo_ID", obBravo.Bravo_ID);
        else
            subParams.put("Max_Bravo_ID", obBravo.Bravo_ID);
        subParams.put("View_Deleted_Users", "0");
        subParams.put("Global", "TRUE");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + mSessionLogin.userID + ", mAccessToken:" + mSessionLogin.accessToken);
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        String url = BravoWebServiceConfig.URL_GET_BRAVO_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetNewsBravoItems(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getPullDown_LoadMoreRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("onLoadMoreBravoItems:" + response);
                onStopPullAndLoadListView();

                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetAllBravoRecentPosts obGetAllBravoRecentPosts = gson.fromJson(response.toString(), ObGetAllBravoRecentPosts.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetAllBravoRecentPosts);
                if (obGetAllBravoRecentPosts == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + obGetAllBravoRecentPosts.data.size());
                    int reponseSize = obGetAllBravoRecentPosts.data.size();
                    if (reponseSize <= 0) {
                        if (!isPulDownToRefresh)
                            isOutOfDataLoadMore = true;
                        return;
                    }
                    mAdapterRecentPost.updatePullDownLoadMorePostList(obGetAllBravoRecentPosts.data, isPulDownToRefresh);
                    if (mListviewRecentPost.getVisibility() == View.GONE)
                        mListviewRecentPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.e("response error");
                onStopPullAndLoadListView();
            }
        }, params, true);
        AIOLog.e("url: " + url);
        getPullDown_LoadMoreRequest.execute(url);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private ArrayList<ObBravo> removeIncorrectBravoItems(ArrayList<ObBravo> bravoItems) {
        ArrayList<ObBravo> obBravos = new ArrayList<ObBravo>();
        for (ObBravo obBravo : bravoItems) {
            if (StringUtility.isEmpty(obBravo.User_ID) || (StringUtility.isEmpty(obBravo.Full_Name) || "0".equals(obBravo.User_ID))) {
                AIOLog.e("The incorrect bravo items:" + obBravo.User_ID + ", obBravo.Full_Name:" + obBravo.Full_Name);
            } else
                obBravos.add(obBravo);
        }
        return obBravos;
    }

    @Override
    public void onClickUserAvatar(String userId) {
        mHomeActionListener.goToUserData(userId);
    }

    private void onStopPullAndLoadListView() {
        mListviewRecentPost.stopRefresh();
        mListviewRecentPost.stopLoadMore();
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     * 
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        String registrationId = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        return registrationId;
    }

    public void updateNotification(String badge, String alert, String source) {
        int numberBadge = Integer.valueOf(badge);
        mNumberOfNewNotifications += numberBadge;
        if (mNumberOfNewNotifications < 11) {
            mNotificationIcon.setText(mNumberOfNewNotifications + "");
        }
        else
            mNotificationIcon.setText("10+");
        if (mNumberOfNewNotifications <= 0)
            mNotificationIcon.setVisibility(View.GONE);
        else {
            mNotificationIcon.setVisibility(View.VISIBLE);
        }
    }

    public ObGetAllBravoRecentPosts getRecentPostData() {
        return mObGetAllBravoRecentPosts;
    }
}
