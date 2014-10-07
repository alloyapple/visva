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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetUserBlocking.User;
import com.sharebravo.bravo.model.response.ObGetUsersList;
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

public class FragmentFollower extends FragmentBasic implements IClickUserAvatar {
    private XListView           mListviewFollower   = null;

    private AdapterUserList     mAdapterUserList    = null;

    private HomeActionListener  mHomeActionListener = null;
    private ObGetUsersList      mObGetUserFollower  = null;
    private ArrayList<User>     mUserCorrect        = new ArrayList<User>();

    private SessionLogin        mSessionLogin       = null;
    private String              mForeignUserID      = "";
    private int                 mLoginBravoViaType  = BravoConstant.NO_LOGIN_SNS;
    private boolean             isOutOfDataLoadMore;
    private OnItemClickListener itemClickListener   = new OnItemClickListener() {

                                                        @Override
                                                        public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                                                            mHomeActionListener.goToUserData(mUserCorrect.get(pos - 1).User_ID);
                                                        }
                                                    };
    Button                      btnBack             = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_user_list, container);

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

        return root;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!isBackStatus())
                requestGetUserFollower(mSessionLogin);
        } else {
            isOutOfDataLoadMore = false;
            mAdapterUserList.removeAllList();
        }
    }

    private void requestGetUserFollower(SessionLogin sessionLogin) {
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
        String url = BravoWebServiceConfig.URL_GET_USER_FLOWER.replace("{User_ID}", mForeignUserID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getUserFollowing = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("mObGetUserFollower:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetUserFollower = gson.fromJson(response.toString(), ObGetUsersList.class);
                AIOLog.d("mObGetUserFollowing:" + mObGetUserFollower);
                if (mObGetUserFollower == null || mObGetUserFollower.data.size() == 0) {
                    return;
                }
                else {
                    mUserCorrect = removeIncorrectUserItem(mObGetUserFollower.data);
                    mAdapterUserList.updateUserList(removeIncorrectUserItem(mObGetUserFollower.data));
                }
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
        mListviewFollower = (XListView) root.findViewById(R.id.listview_user);
        mListviewFollower.setAdapter(mAdapterUserList);
        mListviewFollower.setOnItemClickListener(itemClickListener);
        onStopPullAndLoadListView();
        mListviewFollower.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                /*
                 * if (mObGetUserFollower == null) {
                 * onStopPullAndLoadListView();
                 * return;
                 * }
                 * int size = mObGetUserFollower.data.size();
                 * if (size > 0)
                 * onPullDownToRefreshBravoItems(true, 0);
                 * else
                 */
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                AIOLog.d("IOnRefreshListener");
                if (mObGetUserFollower == null) {
                    onStopPullAndLoadListView();
                    return;
                }
                int size = mObGetUserFollower.data.size();
                if (size > 0 && !isOutOfDataLoadMore)
                    onPullDownToRefreshBravoItems(false, size);
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
        String url = BravoWebServiceConfig.URL_GET_USER_FLOWER.replace("{User_ID}", mForeignUserID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getUserFollowing = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUsersList obGetUserFollower = gson.fromJson(response.toString(), ObGetUsersList.class);
                AIOLog.d("obGetUserFollower:" + obGetUserFollower);
                if (obGetUserFollower == null || obGetUserFollower.data.size() == 0) {
                    if (!isPullDownToRefresh)
                        isOutOfDataLoadMore = true;
                } else {
                    ArrayList<User> mCorrects = removeIncorrectUserItem(obGetUserFollower.data);
                    if (isPullDownToRefresh) {
                        mUserCorrect.addAll(0, mCorrects);
                        mObGetUserFollower.data.addAll(0, obGetUserFollower.data);
                    }
                    else {
                        mUserCorrect.addAll(mCorrects);
                        mObGetUserFollower.data.addAll(obGetUserFollower.data);
                    }
                    mAdapterUserList.updatePullDownLoadMorePostList(mCorrects, isPullDownToRefresh);
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
        getUserFollowing.execute(url);
    }

    private ArrayList<User> removeIncorrectUserItem(ArrayList<User> mUsers) {
        ArrayList<User> users = new ArrayList<User>();
        for (User user : mUsers) {
            if (StringUtility.isEmpty(user.Full_Name) || (StringUtility.isEmpty(user.User_ID))) {
                AIOLog.e("The incorrect bravo items:" + user.User_ID + ", obBravo.Full_Name:" + user.Full_Name);
            } else
                users.add(user);
        }
        return users;
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

    private void onStopPullAndLoadListView() {
        mListviewFollower.stopRefresh();
        mListviewFollower.stopLoadMore();
    }
}
