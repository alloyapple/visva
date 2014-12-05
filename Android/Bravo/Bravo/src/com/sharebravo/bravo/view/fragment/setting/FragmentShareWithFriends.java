package com.sharebravo.bravo.view.fragment.setting;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetUserSearch;
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
import com.sharebravo.bravo.view.adapter.AdapterBravoList.IClickUserAvatar;
import com.sharebravo.bravo.view.adapter.AdapterUserSearchList;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentShareWithFriends extends FragmentBasic implements IClickUserAvatar {
    private XListView             mListviewUser       = null;
    private AdapterUserSearchList mAdapterUser        = null;
    private HomeActionListener    mHomeActionListener = null;

    private SessionLogin          mSessionLogin       = null;
    private int                   mLoginBravoViaType  = BravoConstant.NO_LOGIN_SNS;
    private EditText              textboxSearch       = null;
    private Button                btnBack;
    private ObGetUserSearch       mObGetUserSearch;
    private LinearLayout          mLayoutPoorConnection;
    private ImageButton             cancelSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_share_with_friends, container);

        intializeView(root);

        /* request news */
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        cancelSearch = (ImageButton) root.findViewById(R.id.icon_cancel_search);
        textboxSearch = (EditText) root.findViewById(R.id.txtbox_search_spot);
        textboxSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keySearch = textboxSearch.getEditableText().toString();
                    if (!keySearch.equals("") && NetworkUtility.getInstance(getActivity()).isNetworkAvailable())
                        requestUserSearch(mSessionLogin, keySearch);
                    return true;
                }
                return false;
            }
        });
        textboxSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (!textboxSearch.getEditableText().toString().equals("")) {
                    if (cancelSearch.getVisibility() == View.GONE) {
                        cancelSearch.setVisibility(View.VISIBLE);
                    }
                } else
                    cancelSearch.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        cancelSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cancelSearch.setVisibility(View.GONE);
                textboxSearch.setText("");

            }
        });
        btnBack = (Button) root.findViewById(R.id.btn_back);
        mHomeActionListener = (HomeActivity) getActivity();
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
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

                mObGetUserSearch = gson.fromJson(response.toString(), ObGetUserSearch.class);
                AIOLog.d("obGetTimeline:" + mObGetUserSearch);
                if (mObGetUserSearch == null) {
                    return;
                }
                else {
                    mAdapterUser.updateUserList(mObGetUserSearch.data);
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
        mLayoutPoorConnection = (LinearLayout) root.findViewById(R.id.layout_poor_connection);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            mLayoutPoorConnection.setVisibility(View.GONE);
        } else {
            mLayoutPoorConnection.setVisibility(View.VISIBLE);
        }
        mListviewUser = (XListView) root.findViewById(R.id.listview_user);
        mAdapterUser = new AdapterUserSearchList(getActivity());
        mAdapterUser.setListener(this);
        mListviewUser.setAdapter(mAdapterUser);

        mListviewUser.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                AIOLog.d("IOnRefreshListener");
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                AIOLog.d("IOnLoadMoreListener");
                onStopPullAndLoadListView();
            }
        });
        mListviewUser.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mObGetUserSearch == null)
                    return;
                if (mObGetUserSearch.data.size() < position)
                    return;
                String userId = mObGetUserSearch.data.get(position - 1).userID;
                mHomeActionListener.goToUserData(userId);
            }
        });
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

    @Override
    public void onClickUserAvatar(String userId) {
        mHomeActionListener.goToUserData(userId);
    }

    private void onStopPullAndLoadListView() {
        mListviewUser.stopRefresh();
        mListviewUser.stopLoadMore();
    }
}
