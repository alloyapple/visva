package com.sharebravo.bravo.view.fragment.home;

import java.util.List;

import org.apache.http.NameValuePair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
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
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentMyDataTab extends FragmentBasic {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_mydata_tab, null);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getUserInfo() {
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
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
}
