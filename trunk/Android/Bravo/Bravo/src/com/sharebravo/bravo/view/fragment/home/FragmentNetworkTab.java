package com.sharebravo.bravo.view.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.sharebravo.bravo.model.response.UserSearch;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterBravoList;
import com.sharebravo.bravo.view.adapter.AdapterBravoList.IClickUserAvatar;
import com.sharebravo.bravo.view.adapter.AdapterUserSearchList;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentNetworkTab extends FragmentBasic implements IClickUserAvatar {
    private XListView                mListviewPost            = null;
    private XListView                mListviewUser            = null;
    private AdapterBravoList          mAdapterPost             = null;
    private AdapterUserSearchList    mAdapterUser             = null;
    private HomeActionListener       mHomeActionListener      = null;
    private ArrayList<UserSearch>    mUsers                   = new ArrayList<UserSearch>();
    private ObGetAllBravoRecentPosts mObGetTimelineBravo      = null;

    private SessionLogin             mSessionLogin            = null;
    private LinearLayout             layoutSearch             = null;
    private int                      mLoginBravoViaType       = BravoConstant.NO_LOGIN_SNS;
    private EditText                 textboxSearch            = null;
    private ImageButton               cancelSearch;
    private OnItemClickListener      iRecentPostClickListener = new OnItemClickListener() {

                                                                  @Override
                                                                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                      AIOLog.d("position:" + position);
                                                                      mHomeActionListener.goToRecentPostDetail(mObGetTimelineBravo.data
                                                                              .get(position - 1));
                                                                  }
                                                              };
    private boolean                  isOutOfDataLoadMore;
    private OnItemClickListener      itemClickListener        = new OnItemClickListener() {

                                                                  @Override
                                                                  public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                                                                      mHomeActionListener.goToUserData(mUsers.get(pos - 1).userID);
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
        cancelSearch = (ImageButton) root.findViewById(R.id.icon_cancel_search);
        textboxSearch = (EditText) root.findViewById(R.id.txtbox_search_network);
        textboxSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keySearch = textboxSearch.getEditableText().toString();
                    if (!keySearch.equals(""))
                        requestUserSearch(mSessionLogin, keySearch);
                    return true;
                }
                return false;
            }
        });
        textboxSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (!textboxSearch.getEditableText().toString().equals("")) {
                    if (cancelSearch.getVisibility() == View.GONE) {
                        cancelSearch.setVisibility(View.VISIBLE);
                    }
                } else
                    cancelSearch.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
        cancelSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                cancelSearch.setVisibility(View.GONE);
                textboxSearch.setText("");

            }
        });
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            layoutSearch.setVisibility(View.GONE);
//            mListviewPost.setVisibility(View.GONE);
//            mListviewUser.setVisibility(View.GONE);
            if (!isBackStatus()) {
                requestGetTimeLine(mSessionLogin);
            }
        } else {
            isOutOfDataLoadMore = false;
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
                    mUsers = obGetUserSearch.data;
                    mAdapterUser.updateUserList(mUsers);
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
                AIOLog.d("obGetTimeline:" + response);
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
                    mObGetTimelineBravo = new ObGetAllBravoRecentPosts();
                    mObGetTimelineBravo.data = obBravos;
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
        mListviewPost = (XListView) root.findViewById(R.id.listview_post);
        mListviewUser = (XListView) root.findViewById(R.id.listview_user);
        mAdapterPost = new AdapterBravoList(getActivity(), mObGetTimelineBravo);
        mAdapterUser = new AdapterUserSearchList(getActivity());
        mAdapterUser.setListener(this);
        mAdapterPost.setListener(this);
        mListviewPost.setAdapter(mAdapterPost);
        mListviewUser.setAdapter(mAdapterUser);
        mListviewUser.setOnItemClickListener(itemClickListener);
        mListviewPost.setOnItemClickListener(iRecentPostClickListener);
        mListviewPost.setVisibility(View.GONE);
        mListviewPost.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                AIOLog.d("IOnRefreshListener");
                int size = mObGetTimelineBravo.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetTimelineBravo.data.get(0), true);
                else
                    onStopPullAndLoadRecentPostListView();
            }

            @Override
            public void onLoadMore() {
                int size = mObGetTimelineBravo.data.size();
                if (size > 0 && !isOutOfDataLoadMore)
                    onPullDownToRefreshBravoItems(mObGetTimelineBravo.data.get(size - 1), false);
                else
                    onStopPullAndLoadRecentPostListView();
                AIOLog.d("IOnLoadMoreListener");

            }
        });
        mListviewUser.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                onStopPullAndLoadUserListView();
            }

            @Override
            public void onLoadMore() {
                onStopPullAndLoadUserListView();
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
        AsyncHttpGet getPullDown_LoadMoreRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("onLoadMoreTimeline:" + response);
                onStopPullAndLoadRecentPostListView();

                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetTimeline obGetTimeline;
                obGetTimeline = gson.fromJson(response.toString(), ObGetTimeline.class);
                AIOLog.d("obGetTimeline:" + obGetTimeline);
                if (obGetTimeline == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + obGetTimeline.data.size());
                    int reponseSize = obGetTimeline.data.size();
                    if (reponseSize <= 0) {
                        if (!isPulDownToRefresh)
                            isOutOfDataLoadMore = true;
                        return;
                    }
                    AIOLog.d("mObGetTimelineBravo.data.size():" + mObGetTimelineBravo);
                    int size = mObGetTimelineBravo.data.size();
                    int sizeOfPullLoad = obGetTimeline.data.size();
                    if (isPulDownToRefresh && size == sizeOfPullLoad) {
                        return;
                    }
                    ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(obGetTimeline.data);
                    mAdapterPost.updatePullDownLoadMorePostList(newObBravos, isPulDownToRefresh);
                    if (mListviewPost.getVisibility() == View.GONE)
                        mListviewPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.e("response error");
                onStopPullAndLoadRecentPostListView();
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

    private void onStopPullAndLoadRecentPostListView() {
        mListviewPost.stopRefresh();
        mListviewPost.stopLoadMore();
    }

    private void onStopPullAndLoadUserListView() {
        mListviewUser.stopRefresh();
        mListviewUser.stopLoadMore();
    }
}
