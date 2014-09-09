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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
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
import com.sharebravo.bravo.view.adapter.AdapterRecentPost;
import com.sharebravo.bravo.view.adapter.AdapterRecentPost.IClickUserAvatar;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.PullAndLoadListView;
import com.sharebravo.bravo.view.lib.PullAndLoadListView.IOnLoadMoreListener;
import com.sharebravo.bravo.view.lib.PullToRefreshListView.IOnRefreshListener;

public class FragmentNetworkTab extends FragmentBasic implements IClickUserAvatar {
    private PullAndLoadListView      mListviewPost            = null;
    private AdapterRecentPost        mAdapterPost             = null;
    private HomeActionListener       mHomeActionListener      = null;
    private ObGetAllBravoRecentPosts mObGetAllBravoPostSearch = null;

    private SessionLogin             mSessionLogin            = null;
    private int                      mLoginBravoViaType       = BravoConstant.NO_LOGIN_SNS;
    private OnItemClickListener      iRecentPostClickListener = new OnItemClickListener() {

                                                                  @Override
                                                                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                      AIOLog.d("position:" + position);
                                                                      mHomeActionListener.goToRecentPostDetail(mObGetAllBravoPostSearch.data
                                                                              .get(position - 1));
                                                                  }
                                                              };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_network_tab, container);

        intializeView(root);

        /* request news */
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);

        return root;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestNewsItemsOnBravoServer(mSessionLogin);
        }
    }

    private void requestNewsItemsOnBravoServer(SessionLogin sessionLogin) {
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        AIOLog.d("mUserId:" + sessionLogin.userID + ", mAccessToken:" + sessionLogin.accessToken);
        if (StringUtility.isEmpty(sessionLogin.userID) || StringUtility.isEmpty(sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        String url = BravoWebServiceConfig.URL_GET_ALL_BRAVO;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetAllBravoPostSearch = gson.fromJson(response.toString(), ObGetAllBravoRecentPosts.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + mObGetAllBravoPostSearch);
                if (mObGetAllBravoPostSearch == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + mObGetAllBravoPostSearch.data.size());
                    ArrayList<ObBravo> obBravos = removeIncorrectBravoItems(mObGetAllBravoPostSearch.data);
                    mObGetAllBravoPostSearch.data = obBravos;
                    mAdapterPost.updateRecentPostList(mObGetAllBravoPostSearch);
                    if (mListviewPost.getVisibility() == View.GONE)
                        mListviewPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);

        getLoginRequest.execute(url);

    }

    private void intializeView(View root) {
        mListviewPost = (PullAndLoadListView) root.findViewById(R.id.listview_post);
        mAdapterPost = new AdapterRecentPost(getActivity(), mObGetAllBravoPostSearch);
        mAdapterPost.setListener(this);
        mListviewPost.setAdapter(mAdapterPost);
        mListviewPost.setOnItemClickListener(iRecentPostClickListener);
        mListviewPost.setVisibility(View.GONE);
        /* load more old items */
        mListviewPost.setOnLoadMoreListener(new IOnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                int size = mObGetAllBravoPostSearch.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetAllBravoPostSearch.data.get(size - 1), false);
                else
                    mListviewPost.onLoadMoreComplete();
                AIOLog.d("IOnLoadMoreListener");
            }
        });

        /* on refresh new items */
        /* load more old items */
        mListviewPost.setOnRefreshListener(new IOnRefreshListener() {

            @Override
            public void onRefresh() {
                AIOLog.d("IOnRefreshListener");
                int size = mObGetAllBravoPostSearch.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetAllBravoPostSearch.data.get(0), true);
                else
                    mListviewPost.onRefreshComplete();
            }
        });
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
        AsyncHttpGet getPullDown_LoadMoreRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("onLoadMoreBravoItems:" + response);
                if (isPulDownToRefresh) {
                    mListviewPost.onRefreshComplete();
                } else {
                    mListviewPost.onLoadMoreComplete();
                }

                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetAllBravoRecentPosts obGetAllBravoRecentPosts = gson.fromJson(response.toString(), ObGetAllBravoRecentPosts.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetAllBravoRecentPosts);
                if (obGetAllBravoRecentPosts == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + obGetAllBravoRecentPosts.data.size());
                    int reponseSize = obGetAllBravoRecentPosts.data.size();
                    if (reponseSize <= 0)
                        return;
                    updatePullDownLoadMorePostList(obGetAllBravoRecentPosts, isPulDownToRefresh);
                    mAdapterPost.updatePullDownLoadMorePostList(obGetAllBravoRecentPosts, isPulDownToRefresh);
                    if (mListviewPost.getVisibility() == View.GONE)
                        mListviewPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.e("response error");
                if (isPulDownToRefresh) {
                    mListviewPost.onRefreshComplete();
                } else {
                    mListviewPost.onLoadMoreComplete();
                }
            }
        }, params, true);
        AIOLog.e("url: " + url);
        getPullDown_LoadMoreRequest.execute(url);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mHomeActionListener = (HomeActionListener) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface IShowPageHomeNotification {
        public void showPageHomeNotification();
    }

    private void updatePullDownLoadMorePostList(ObGetAllBravoRecentPosts obGetAllBravoRecentPosts, boolean isPulDownToRefresh) {
        ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(obGetAllBravoRecentPosts.data);
        if (mObGetAllBravoPostSearch == null) {
            mObGetAllBravoPostSearch = new ObGetAllBravoRecentPosts();
            mObGetAllBravoPostSearch.data = new ArrayList<ObBravo>();
        }
        if (isPulDownToRefresh)
            mObGetAllBravoPostSearch.data.addAll(0, newObBravos);
        else
            mObGetAllBravoPostSearch.data.addAll(newObBravos);
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
}
