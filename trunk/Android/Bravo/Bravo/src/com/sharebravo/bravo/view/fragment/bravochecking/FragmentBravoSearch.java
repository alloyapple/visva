package com.sharebravo.bravo.view.fragment.bravochecking;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.ActivityBravoChecking;
import com.sharebravo.bravo.control.activity.BravoCheckingListener;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetSpot.Spot;
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
import com.sharebravo.bravo.view.adapter.AdapterBravoSearch;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentBravoSearch extends FragmentBasic {
    public static int             SEARCH_FOR_SPOT         = 0;
    public static int             SEARCH_LOCAL_BRAVO      = 1;
    public static int             SEARCH_ARROUND_ME       = 2;
    public static int             SEARCH_PEOPLE_FOLLOWING = 3;
    private SessionLogin          mSessionLogin           = null;
    private int                   mLoginBravoViaType      = BravoConstant.NO_LOGIN_SNS;
    private EditText              textboxSearch           = null;
    private AdapterBravoSearch    mAdapterBravoSearch;
    private XListView             mListViewResult;
    private Button                mBtnClose;
    private ObGetSpotSearch       mObGetSpotSearch;
    private BravoCheckingListener mBravoCheckingListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBravoCheckingListener = (ActivityBravoChecking) getActivity();
        View root = (ViewGroup) inflater.inflate(R.layout.page_bravo_tab, container);
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
        mListViewResult = (XListView) root.findViewById(R.id.listview_bravo_search);
        mAdapterBravoSearch = new AdapterBravoSearch(getActivity());
        mListViewResult.setAdapter(mAdapterBravoSearch);
        mListViewResult.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                onStopPullAndLoadListView();
            }
        });
        mListViewResult.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parentView, View View, int position, long arg3) {
                AIOLog.d("mObGetSpotSearch:position=>" + position);
                Spot spot = mObGetSpotSearch.data.get(position - 1);
                if (spot == null)
                    return;
                AIOLog.d("mObGetSpotSearch:spot=>" + spot);
                mBravoCheckingListener.goToMapView(spot, FragmentBravoMap.MAKER_BY_LOCATION_SPOT);
            }
        });
        mBtnClose = (Button) root.findViewById(R.id.btn_close);
        mBtnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mBravoCheckingListener.goToBack();
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
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mListViewResult.setVisibility(View.GONE);
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
                mObGetSpotSearch = gson.fromJson(response.toString(), ObGetSpotSearch.class);
                AIOLog.d("mObGetSpotSearch:" + mObGetSpotSearch);
                if (mObGetSpotSearch == null) {
                    return;
                } else {
                    mListViewResult.setVisibility(View.VISIBLE);
                    mAdapterBravoSearch.updateData(mObGetSpotSearch.data);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getSpotSearch.execute(url);
    }

    public void requestBaravoSearch() {
    }

    private void onStopPullAndLoadListView() {
        mListViewResult.stopRefresh();
        mListViewResult.stopLoadMore();
    }
}
