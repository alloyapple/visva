package com.sharebravo.bravo.view.fragment.home;

import java.util.List;

import org.apache.http.NameValuePair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterUserPostProfile;
import com.sharebravo.bravo.view.adapter.UserPostProfileListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.imageheader.PullAndLoadListView;

public class FragmentUserDataTab extends FragmentBasic implements UserPostProfileListener {

    private Button                 mBtnSettings;
    private IShowPageSettings      iShowPageSettings;
    private PullAndLoadListView    mListViewUserPostProfile = null;
    private AdapterUserPostProfile adapterUserPostProfile   = null;
    private HomeActionListener     mHomeActionListener      = null;
    private Button                 btnBack;
    private OnItemClickListener    onItemClick              = new OnItemClickListener() {

                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    // TODO Auto-generated method stub

                                                                }
                                                            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_user_profile, null);

        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mBtnSettings = (Button) root.findViewById(R.id.btn_settings);
        mHomeActionListener = (HomeActivity) getActivity();
        mListViewUserPostProfile = (PullAndLoadListView) root.findViewById(R.id.listview_user_post_profile);
        adapterUserPostProfile = new AdapterUserPostProfile(getActivity());
        adapterUserPostProfile.setListener(this);
        mListViewUserPostProfile.setFooterDividersEnabled(false);
        mListViewUserPostProfile.setAdapter(adapterUserPostProfile);
        mListViewUserPostProfile.setOnItemClickListener(onItemClick);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mBtnSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iShowPageSettings.showPageSettings();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getUserInfo(String foreignUserId) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        AIOLog.d("mUserId:" + _sessionLogin.userID + ", mAccessToken:" + _sessionLogin.accessToken);
        if (StringUtility.isEmpty(_sessionLogin.userID) || StringUtility.isEmpty(_sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        String checkingUserId = foreignUserId;
        if (StringUtility.isEmpty(foreignUserId))
            checkingUserId = userId;
        String url = BravoWebServiceConfig.URL_GET_USER_INFO + "/" + checkingUserId;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("get user info at my data:" + response);
                if (StringUtility.isEmpty(response))
                    return;
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserInfo obGetUserInfo = gson.fromJson(response.toString(), ObGetUserInfo.class);
                if (obGetUserInfo == null) {
                    AIOLog.e("obGetUserInfo is null");
                } else {
                    switch (obGetUserInfo.status) {
                    case BravoConstant.STATUS_FAILED:
                        showToast(getActivity().getResources().getString(R.string.get_user_info_error));
                        break;
                    case BravoConstant.STATUS_SUCCESS:

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
        getLoginRequest.execute(url);

    }

    public interface IShowPageSettings {
        public void showPageSettings();
    }

    public void setListener(IShowPageSettings ÌShowPageSettings) {
        this.iShowPageSettings = ÌShowPageSettings;
    }

}
