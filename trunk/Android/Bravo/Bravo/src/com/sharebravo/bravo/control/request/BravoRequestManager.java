package com.sharebravo.bravo.control.request;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpDelete;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class BravoRequestManager {
    // ================================================================
    private Context                    mContext;

    /* singleton class */
    private static BravoRequestManager mInstance;

    /**
     * RequestWrapper constructor
     * 
     * @param context
     */
    public BravoRequestManager(Context context) {
        super();
        mContext = context;

    }

    /**
     * get instance of RequestWrapper singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized BravoRequestManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new BravoRequestManager(context);
        return mInstance;
    }

    // ======================== CORE FUNCTIONS ========================

    /**
     * control put,delete notifications
     * 
     * @param notificationType
     * @param isChecked
     * @param iRequestListener
     */
    public void requestToChangeNotificationsType(String notificationType, boolean isChecked, IRequestListener iRequestListener) {
        if (isChecked) {
            putNotification(notificationType, iRequestListener);
        } else {
            deleteNotification(notificationType, iRequestListener);
        }
    }

    /**
     * put notification
     * 
     * @param notificationType
     * @param iRequestListener
     */
    private void deleteNotification(final String notificationType, final IRequestListener iRequestListener) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_NOTIFICATION.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Type}", notificationType);
        AsyncHttpDelete deleteSNS = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response deleteSNS :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    iRequestListener.onErrorResponse("failed to delete notification:" + notificationType + " by failed status");
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("failed to delete notification:" + notificationType);
            }
        }, null, true);
        AIOLog.d(url);
        deleteSNS.execute(url);

    }

    private void putNotification(final String notificationType, final IRequestListener iRequestListener) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Type", notificationType);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_NOTIFICATION.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putReport = new AsyncHttpPut(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putNotification :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    iRequestListener.onErrorResponse("failed to put notification:" + notificationType + " by failed status");
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("failed to put notification:" + notificationType);
            }
        }, params, true);
        AIOLog.d(url);
        putReport.execute(url);
    }

    /**
     * request to delete my account
     */
    public void requestToDeleteMyAccount(FragmentBasic fragmentBasic, final IRequestListener iRequestListener) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_USER.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);

        AsyncHttpDelete deleteAccount = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                iRequestListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("response error");
            }
        }, null, true);
        AIOLog.d("requestToDeleteMyAccount:" + url);
        deleteAccount.execute(url);
    }
}
