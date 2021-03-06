package com.sharebravo.bravo.view.fragment.userprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetUserFollowing;
import com.sharebravo.bravo.model.response.ObGetUserBlocking.User;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterBravoList.IClickUserAvatar;
import com.sharebravo.bravo.view.adapter.AdapterUserList;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentFollowing extends FragmentBasic implements IClickUserAvatar {
    private XListView           mListviewFollowing  = null;

    private AdapterUserList     mAdapterUserList    = null;

    private HomeActionListener  mHomeActionListener = null;
    private ObGetUserFollowing  mObGetUserFollowing = null;

    private SessionLogin        mSessionLogin       = null;
    private String              mForeignUserID      = "";
    private int                 mLoginBravoViaType  = BravoConstant.NO_LOGIN_SNS;
    private Button              mBtnBack            = null;
    private boolean             isOutOfDataLoadMore;
    private OnItemClickListener itemClickListener   = new OnItemClickListener() {

                                                        @Override
                                                        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                                                            mHomeActionListener.goToUserData(mObGetUserFollowing.data.get(pos - 1).User_ID);
                                                        }
                                                    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_following, container);

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

        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!isBackStatus())
                requestGetUserFollowing(mSessionLogin);
        } else {
            isOutOfDataLoadMore = false;
        }
        mListviewFollowing.setVisibility(View.GONE);
    }

    private void requestGetUserFollowing(SessionLogin sessionLogin) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + sessionLogin.userID + ", mAccessToken:" + sessionLogin.accessToken);
        if (StringUtility.isEmpty(sessionLogin.userID) || StringUtility.isEmpty(sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_FOLLOWING.replace("{User_ID}", mForeignUserID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getUserFollowing = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetUserFollowing = gson.fromJson(response.toString(), ObGetUserFollowing.class);
                AIOLog.d("mObGetUserFollowing:" + mObGetUserFollowing);
                if (mObGetUserFollowing == null || mObGetUserFollowing.data.size() == 0) {
                    return;
                } else {
                    mAdapterUserList.updateUserList(removeIncorrectUserItem(mObGetUserFollowing.data));
                }
                mListviewFollowing.setVisibility(View.VISIBLE);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getUserFollowing.execute(url);
    }

    private void intializeView(View root) {
        mAdapterUserList = new AdapterUserList(getActivity());
        mAdapterUserList.setListener(this);
        mListviewFollowing = (XListView) root.findViewById(R.id.listview_following);
        mListviewFollowing.setAdapter(mAdapterUserList);
        mListviewFollowing.setOnItemClickListener(itemClickListener);
        onStopPullAndLoadListView();
        mListviewFollowing.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                /*
                 * AIOLog.d("IOnRefreshListener");
                 * if (mObGetUserFollowing == null) {
                 * onStopPullAndLoadListView();
                 * return;
                 * }
                 * int size = mObGetUserFollowing.data.size();
                 * if (size > 0)
                 * onPullDownToRefreshBravoItems(true, 0);
                 * else
                 */
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                AIOLog.d("onLoadMore");
                if (mObGetUserFollowing == null) {
                    onStopPullAndLoadListView();
                    return;
                }
                int size = mObGetUserFollowing.data.size();
                if (size > 0 && !isOutOfDataLoadMore)
                    onPullDownToRefreshBravoItems(true, size);
                else
                    onStopPullAndLoadListView();
            }
        });
    }

    private void onPullDownToRefreshBravoItems(final boolean isPullDownToRefresh, int position) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", position + "");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_FOLLOWING.replace("{User_ID}", mForeignUserID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getUserFollowing = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                // AIOLog.d("requestBravoNews:" + response);
                onStopPullAndLoadListView();
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserFollowing obGetUserFollowing = gson.fromJson(response.toString(), ObGetUserFollowing.class);
                AIOLog.d("mObGetUserFollowing:" + mObGetUserFollowing);
                if (obGetUserFollowing == null || obGetUserFollowing.data.size() == 0) {
                    if (!isPullDownToRefresh)
                        isOutOfDataLoadMore = true;
                    return;
                }
                else {
                    mAdapterUserList.updatePullDownLoadMorePostList(obGetUserFollowing.data, isPullDownToRefresh);
                }
                mListviewFollowing.setVisibility(View.VISIBLE);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                onStopPullAndLoadListView();
            }
        }, params, true);
        getUserFollowing.execute(url);

    }

    @Override
    public void onClickUserAvatar(String userId) {
        mHomeActionListener.goToUserData(userId);
    }

    public String getForeignID() {
        return mForeignUserID;
    }

    public void setForeignID(String foreignID) {
        this.mForeignUserID = foreignID;
    }

    private ArrayList<User> removeIncorrectUserItem(ArrayList<User> mUsers) {
        ArrayList<User> users = new ArrayList<User>();
        for (User user : mUsers) {
            if (StringUtility.isEmpty(user.Full_Name) || (StringUtility.isEmpty(user.Full_Name))) {
                AIOLog.e("The incorrect bravo items:" + user.User_ID + ", obBravo.Full_Name:" + user.Full_Name);
            } else
                users.add(user);
        }
        return users;
    }

    private void onStopPullAndLoadListView() {
        mListviewFollowing.stopRefresh();
        mListviewFollowing.stopLoadMore();
    }
}
