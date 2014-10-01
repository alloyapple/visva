package com.sharebravo.bravo.sdk.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;

import android.content.Context;

public class RequestWrapper {
    // ================================================================
    private Context               mContext;

    /* singleton class */
    private static RequestWrapper mInstance;

    /**
     * RequestWrapper constructor
     * 
     * @param context
     */
    public RequestWrapper(Context context) {
        super();
        mContext = context;

    }

    /**
     * get instance of RequestWrapper singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized RequestWrapper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new RequestWrapper(context);
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
    public static void requestToChangeNotificationsType(String notificationType, boolean isChecked, IRequestListener iRequestListener) {
        if (isChecked) {
            // putNotification(notificationType, iRequestListener);
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
    private static void deleteNotification(String notificationType, IRequestListener iRequestListener) {
        // TODO Auto-generated method stub

    }

//    private static void putNotification(String notificationType, IRequestListener iRequestListener) {
//        boolean isCheckExistedSNS = false;
//        mSNSList = BravoUtils.getSNSList(this);
//        if (mSNSList == null)
//            mArrSNSList = new ArrayList<SNS>();
//        else
//            mArrSNSList = mSNSList.snsArrList;
//        for (int i = 0; i < mArrSNSList.size(); i++) {
//            if (sns.foreignSNS.equals(mArrSNSList.get(i).foreignSNS))
//                isCheckExistedSNS = true;
//        }
//        if (!isCheckExistedSNS)
//            return;
//        String userId = mSessionLogin.userID;
//        String accessToken = mSessionLogin.accessToken;
//        HashMap<String, String> subParams = new HashMap<String, String>();
//        subParams.put("Foreign_SNS", sns.foreignSNS);
//        subParams.put("Foreign_ID", sns.foreignID);
//        subParams.put("Foreign_Access_Token", sns.foreignAccessToken);
//        JSONObject jsonObject = new JSONObject(subParams);
//        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
//        String url = BravoWebServiceConfig.URL_PUT_SNS.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
//        AsyncHttpPut putReport = new AsyncHttpPut(this, new AsyncHttpResponseProcess(this, mFragmentBravoReturnSpots) {
//            @Override
//            public void processIfResponseSuccess(String response) {
//                AIOLog.d("response putSNS :===>" + response);
//                JSONObject jsonObject = null;
//
//                try {
//                    jsonObject = new JSONObject(response);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (jsonObject == null)
//                    return;
//
//                String status = null;
//                try {
//                    status = jsonObject.getString("status");
//                } catch (JSONException e1) {
//                    e1.printStackTrace();
//                }
//                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
//                    mFragmentBravoReturnSpots.updatePostSNS(sns, true);
//                } else {
//                    mFragmentBravoReturnSpots.updatePostSNS(sns, false);
//                }
//            }
//
//            @Override
//            public void processIfResponseFail() {
//                mFragmentBravoReturnSpots.updatePostSNS(sns, false);
//            }
//        }, params, true);
//        AIOLog.d(url);
//        putReport.execute(url);
//
//    }
}
