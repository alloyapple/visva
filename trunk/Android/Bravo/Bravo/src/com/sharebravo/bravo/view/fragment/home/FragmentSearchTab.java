package com.sharebravo.bravo.view.fragment.home;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetSpotSearch;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterSearchSpotResult;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;

public class FragmentSearchTab extends FragmentBasic {
    public static int               SEARCH_FOR_SPOT          = 0;
    public static int               SEARCH_LOCAL_BRAVO       = 1;
    public static int               SEARCH_ARROUND_ME        = 2;
    public static int               SEARCH_PEOPLE_FOLLOWING  = 3;
    private SessionLogin            mSessionLogin            = null;
    private int                     mLoginBravoViaType       = BravoConstant.NO_LOGIN_SNS;
    private EditText                textboxSearch            = null;
    private LinearLayout            layoutQuickSearchOptions = null;
    private AdapterSearchSpotResult mAdapter;
    private XListView               listViewResult;
    private Button                  btnBack;
    private TextView                btnLocalBravos;
    private TextView                btnAroundMe;
    private TextView                btnPeopleFollowing;
    private int                     mMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_search_tab, null);
        textboxSearch = (EditText) root.findViewById(R.id.txtbox_search_spot);
        textboxSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keySearch = textboxSearch.getEditableText().toString();
                    if (!keySearch.equals(""))
                        requestSpotSearch(keySearch);
                    return true;
                }
                return false;
            }
        });
        layoutQuickSearchOptions = (LinearLayout) root.findViewById(R.id.layout_quicksearch_options);
        listViewResult = (XListView) root.findViewById(R.id.listview_result_search);
        mAdapter = new AdapterSearchSpotResult(getActivity());
        listViewResult.setAdapter(mAdapter);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBack(mMode);
            }
        });
        btnLocalBravos = (TextView) root.findViewById(R.id.text_local_bravo);
        btnLocalBravos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                mMode = SEARCH_LOCAL_BRAVO;
            }
        });
        btnAroundMe = (TextView) root.findViewById(R.id.text_around_me);
        btnAroundMe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                mMode = SEARCH_ARROUND_ME;
            }
        });
        btnPeopleFollowing = (TextView) root.findViewById(R.id.text_people_following);
        btnPeopleFollowing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layoutQuickSearchOptions.setVisibility(View.GONE);
                listViewResult.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                mMode = SEARCH_PEOPLE_FOLLOWING;
            }
        });
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mMode = SEARCH_FOR_SPOT;
            listViewResult.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            layoutQuickSearchOptions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void requestSpotSearch(String nameSpot) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + mSessionLogin.userID + ", mAccessToken:" + mSessionLogin.accessToken);
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        subParams.put("Name", nameSpot);
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_SPOT_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getSpotSearch = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("getSpotSearch:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpotSearch mObGetSpotSearch;
                mObGetSpotSearch = gson.fromJson(response.toString(), ObGetSpotSearch.class);
                AIOLog.d("mObGetSpotSearch:" + mObGetSpotSearch);
                if (mObGetSpotSearch == null) {
                    return;
                } else {
                    layoutQuickSearchOptions.setVisibility(View.GONE);
                    listViewResult.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.VISIBLE);
                    mAdapter.updateData(mObGetSpotSearch.data);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getSpotSearch.execute(url);

    }

    public void onBack(int mode) {
        if (mode == SEARCH_ARROUND_ME) {
            mMode = SEARCH_FOR_SPOT;
            listViewResult.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            layoutQuickSearchOptions.setVisibility(View.VISIBLE);
        } else if (mode == SEARCH_LOCAL_BRAVO) {
            mMode = SEARCH_FOR_SPOT;
            listViewResult.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            layoutQuickSearchOptions.setVisibility(View.VISIBLE);
        } else if (mode == SEARCH_PEOPLE_FOLLOWING) {
            mMode = SEARCH_FOR_SPOT;
            listViewResult.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            layoutQuickSearchOptions.setVisibility(View.VISIBLE);
        }
    }

    public void requestBaravoSearch() {
    }

}
