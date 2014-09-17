package com.sharebravo.bravo.sdk.util.network;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.app.Activity;
import android.util.Log;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.sdk.util.DialogUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 * @author Visva
 */
public class AsyncHttpResponseProcess implements AsyncHttpResponseListener {
    private static final String TAG = "AsyncHttpResponseProcess";

    private Activity            context;
    FragmentBasic               asyncUI;

    // boolean isShowUI;
    // boolean isDismissUI;

    public AsyncHttpResponseProcess(Activity context, FragmentBasic asyncUI/* , boolean isShowUI, boolean isDismissUI */) {
        this.context = context;
        this.asyncUI = asyncUI;
        // this.isShowUI = isShowUI;
        // this.isDismissUI = isDismissUI;
    }

    @Override
    public void before() {
        // Show waiting dialog during connection
        asyncUI.before();
    }

    @Override
    public void after(int statusCode, HttpResponse response) {
        // Process server response

        asyncUI.after();

        switch (statusCode) {
        case AsyncHttpBase.NETWORK_STATUS_OFF:
            try {
                DialogUtility.alert(context, context.getString(R.string.network_unvailable));
            } catch (Exception e) {
                DialogUtility.alert(context.getParent(),
                        context.getString(R.string.network_unvailable));
                e.printStackTrace();
            }
            break;
        case AsyncHttpBase.NETWORK_STATUS_OK:
            processHttpResponse(response);
            break;
        default:
            try {
                DialogUtility.alert(context, context.getString(R.string.failed_to_conect_server));
            } catch (Exception e) {
            }
            break;
        }
    }

    /**
     * Process HttpResponse
     * 
     * @param response
     */
    public void processHttpResponse(HttpResponse response) {
        String json = "";
        try {
            // Get json response
            json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            if (json == null) {
                DialogUtility.alert(context, "Can't extract server data");
                return;
            }

            // Parser information
            Log.i(TAG, "Webservice response : " + json);
            // CommonInfo commonInfo = ParserUtility.parserCommonResponse(json);
            // if (commonInfo.isSuccess()) {
            processIfResponseSuccess(json);
            // } else {
            // processIfResponseFail();
            // context.checkInvalidToken(commonInfo.getMessage());
            // }
        } catch (Exception e) {
            e.printStackTrace();
            // try {
            // DialogUtility.alert(context, "Server error");
            // } catch (Exception e1) {
            // // TODO Auto-generated catch block
            // DialogUtility.alert(context.getParent(), "Server error");
            // e1.printStackTrace();
            // }

        }
    }

    /**
     * Interface function
     * 
     * @throws JSONException
     */
    public void processIfResponseSuccess(String response) {
        Log.i(TAG, "Process if response is success ===================");
    }

    /**
     * Interface function
     */
    public void processIfResponseFail() {
        // SmartLog.log(TAG, "Process if response is fail ===================");
    }

    // public boolean isLoading() {
    // return isLoading;
    // }
    //
    // public void setLoading(boolean isLoading) {
    // this.isLoading = isLoading;
    // }
}
