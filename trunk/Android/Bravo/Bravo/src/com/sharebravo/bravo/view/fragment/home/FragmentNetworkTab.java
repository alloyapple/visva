package com.sharebravo.bravo.view.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.model.response.ObGetTimeline;
import com.sharebravo.bravo.model.response.ObGetUserSearch;
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
import com.sharebravo.bravo.view.adapter.AdapterUserSearchList;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.PullAndLoadListView;
import com.sharebravo.bravo.view.lib.PullAndLoadListView.IOnLoadMoreListener;
import com.sharebravo.bravo.view.lib.PullToRefreshListView.IOnRefreshListener;

public class FragmentNetworkTab extends FragmentBasic implements IClickUserAvatar {
    private PullAndLoadListView      mListviewPost            = null;
    private PullAndLoadListView      mListviewUser            = null;
    private AdapterPostList          mAdapterPost             = null;
    private AdapterUserSearchList    mAdapterUser             = null;
    private HomeActionListener       mHomeActionListener      = null;
    private ObGetAllBravoRecentPosts mObGetTimelineBravo      = null;

    private SessionLogin             mSessionLogin            = null;
    private LinearLayout             layoutSearch             = null;
    private int                      mLoginBravoViaType       = BravoConstant.NO_LOGIN_SNS;
    private EditText                 textboxSearch            = null;
    private OnItemClickListener      iRecentPostClickListener = new OnItemClickListener() {

                                                                  @Override
                                                                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                      AIOLog.d("position:" + position);
                                                                      mHomeActionListener.goToRecentPostDetail(mObGetTimelineBravo.data
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
        layoutSearch = (LinearLayout) root.findViewById(R.id.layout_search);
        textboxSearch = (EditText) root.findViewById(R.id.txtbox_search_network);
        textboxSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keySearch = textboxSearch.getEditableText().toString();
                    if (!keySearch.equals(""))
                        requestUserSearch(mSessionLogin, keySearch);
                    return true;
                }
                return false;
            }
        });

        return root;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            layoutSearch.setVisibility(View.GONE);
            mListviewPost.setVisibility(View.GONE);
            mListviewUser.setVisibility(View.GONE);
            requestGetTimeLine(mSessionLogin);

        }
    }

    private void requestUserSearch(SessionLogin sessionLogin, String keyName) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + sessionLogin.userID + ", mAccessToken:" + sessionLogin.accessToken);
        if (StringUtility.isEmpty(sessionLogin.userID) || StringUtility.isEmpty(sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        subParams.put("Full_Name", keyName);
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_SEARCH.replace("{User_ID}", userId);
        List<NameValuePair> params = ParameterFactory.createSubParamsUserSearch(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                // AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserSearch obGetUserSearch;
                obGetUserSearch = gson.fromJson(response.toString(), ObGetUserSearch.class);
                AIOLog.d("obGetTimeline:" + obGetUserSearch);
                if (obGetUserSearch == null) {
                    return;
                }
                else {
                    mAdapterUser.updateUserList(obGetUserSearch.data);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getTimeline.execute(url);
    }

    private void requestGetTimeLine(SessionLogin sessionLogin) {
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
        String url = BravoWebServiceConfig.URL_GET_TIMELINE.replace("{User_ID}", userId);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                // AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetTimeline obGetTimeline;
                obGetTimeline = gson.fromJson(response.toString(), ObGetTimeline.class);
                AIOLog.d("obGetTimeline:" + obGetTimeline);
                if (obGetTimeline == null || obGetTimeline.data.size() == 0) {
                    layoutSearch.setVisibility(View.VISIBLE);
                    mListviewPost.setVisibility(View.GONE);
                    mListviewUser.setVisibility(View.VISIBLE);
                    mAdapterUser.updateUserList(null);
                    return;
                }
                else {
                    ArrayList<ObBravo> obBravos = removeIncorrectBravoItems(obGetTimeline.data);
                    // mObGetTimelineBravo = new ObGetAllBravoRecentPosts();
                    // mObGetTimelineBravo.data = obBravos;
                    mAdapterPost.updateRecentPostList(obBravos);
                    mListviewUser.setVisibility(View.GONE);
                    layoutSearch.setVisibility(View.GONE);
                    mListviewPost.setVisibility(View.VISIBLE);
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
        mListviewPost = (PullAndLoadListView) root.findViewById(R.id.listview_post);
        mListviewUser = (PullAndLoadListView) root.findViewById(R.id.listview_user);
        mAdapterPost = new AdapterPostList(getActivity(), mObGetTimelineBravo);
        mAdapterUser = new AdapterUserSearchList(getActivity());
        mAdapterUser.setListener(this);
        mAdapterPost.setListener(this);
        mListviewPost.setAdapter(mAdapterPost);
        mListviewUser.setAdapter(mAdapterUser);
        mListviewPost.setOnItemClickListener(iRecentPostClickListener);
        mListviewPost.setVisibility(View.GONE);
        /* load more old items */
        mListviewPost.setOnLoadMoreListener(new IOnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                int size = mObGetTimelineBravo.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetTimelineBravo.data.get(size - 1), false);
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
                int size = mObGetTimelineBravo.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetTimelineBravo.data.get(0), true);
                else
                    mListviewPost.onRefreshComplete();
            }
        });
        mListviewUser.setOnLoadMoreListener(new IOnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                AIOLog.d("IOnLoadMoreListener");
            }
        });

        /* on refresh new items */
        /* load more old items */
        mListviewUser.setOnRefreshListener(new IOnRefreshListener() {

            @Override
            public void onRefresh() {
                AIOLog.d("IOnRefreshListener");
            }
        });
    }

    private void onPullDownToRefreshBravoItems(ObBravo obBravo, final boolean isPulDownToRefresh) {
        AIOLog.d("obBravo.bravoId:" + obBravo.Bravo_ID);
        HashMap<String, String> subParams = new HashMap<String, String>();
        if (isPulDownToRefresh)
            subParams.put("Start", "0");
        else
            subParams.put("Max_Bravo_ID", obBravo.Bravo_ID);
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + mSessionLogin.userID + ", mAccessToken:" + mSessionLogin.accessToken);
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        String url = BravoWebServiceConfig.URL_GET_TIMELINE.replace("{User_ID}", userId);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getPullDown_LoadMoreRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("onLoadMoreTimeline:" + response);
                if (isPulDownToRefresh) {
                    mListviewPost.onRefreshComplete();
                } else {
                    mListviewPost.onLoadMoreComplete();
                }

                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetTimeline obGetTimeline;
                obGetTimeline = gson.fromJson(response.toString(), ObGetTimeline.class);
                AIOLog.d("obGetTimeline:" + obGetTimeline);
                if (obGetTimeline == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + obGetTimeline.data.size());
                    int reponseSize = obGetTimeline.data.size();
                    if (reponseSize <= 0)
                        return;

                    ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(obGetTimeline.data);
                    mAdapterPost.updatePullDownLoadMorePostList(newObBravos, isPulDownToRefresh);
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

    // private void updatePullDownLoadMorePostList(ObGetTimeline obGetTimeline, boolean isPulDownToRefresh) {
    // ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(obGetTimeline.data);
    // if (mObGetTimelineBravo == null) {
    // mObGetTimelineBravo = new ObGetAllBravoRecentPosts();
    // mObGetTimelineBravo.data = new ArrayList<ObBravo>();
    // }
    // if (isPulDownToRefresh)
    // mObGetTimelineBravo.data.addAll(0, newObBravos);
    // else
    // mObGetTimelineBravo.data.addAll(newObBravos);
    // }

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
