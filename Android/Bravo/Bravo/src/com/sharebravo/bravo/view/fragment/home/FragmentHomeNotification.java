package com.sharebravo.bravo.view.fragment.home;

import java.util.List;

import org.apache.http.NameValuePair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetNotification;
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

public class FragmentHomeNotification extends FragmentBasic {
    private ListView                   mListViewNotifications;
    private TextView                   mTextNoNotifications;
    private Button                     mBtnCloseNotifications;
    private IClosePageHomeNotification iClosePageHomeNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_home_notification, container);
        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mListViewNotifications = (ListView) root.findViewById(R.id.listview_home_notification);
        mTextNoNotifications = (TextView) root.findViewById(R.id.text_no_notification);
        mBtnCloseNotifications = (Button) root.findViewById(R.id.btn_home_close_notification);
        mBtnCloseNotifications.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iClosePageHomeNotification.closePageHomeNotification();
            }
        });
    }

    public void onRequestListHomeNotification() {
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        AIOLog.d("mUserId:" + _sessionLogin.userID + ", mAccessToken:" + _sessionLogin.accessToken);
        if (StringUtility.isEmpty(_sessionLogin.userID) || StringUtility.isEmpty(_sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        String url = BravoWebServiceConfig.URL_GET_NOTIFICATION;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("get notification:" + response);
                if (StringUtility.isEmpty(response))
                    return;
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetNotification obGetNotification = gson.fromJson(response.toString(), ObGetNotification.class);
                if (obGetNotification == null) {
                    AIOLog.e("obGetNotification is null");
                    return;
                }
                AIOLog.d("obGetNotification status:" + obGetNotification.status);
                switch (obGetNotification.status) {
                case BravoConstant.STATUS_SUCCESS:
                    if (obGetNotification.data == null || obGetNotification.data.size() <= 0) {
                        mTextNoNotifications.setVisibility(View.VISIBLE);
                        mListViewNotifications.setVisibility(View.GONE);
                    } else {
                        mTextNoNotifications.setVisibility(View.GONE);
                        mListViewNotifications.setVisibility(View.VISIBLE);
                    }
                    break;
                case BravoConstant.STATUS_FAILED:
                    showToast(getActivity().getResources().getString(R.string.get_notification_error));
                    break;
                default:
                    break;
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getLoginRequest.execute(url);
    }

    public interface IClosePageHomeNotification {
        public void closePageHomeNotification();
    }

    public void setListener(IClosePageHomeNotification iClosePageHomeNotification) {
        this.iClosePageHomeNotification = iClosePageHomeNotification;
    }
}
