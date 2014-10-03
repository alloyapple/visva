package com.sharebravo.bravo.sdk.util.network;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.FragmentMapBasic;
import com.sharebravo.bravo.view.fragment.IUISync;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 * @author Visva
 */
public class AsyncHttpResponseProcess implements AsyncHttpResponseListener {
    private static final String TAG = "AsyncHttpResponseProcess";

    private Context             mContext;
    IUISync                     asyncUI;

    public AsyncHttpResponseProcess(Context mContext, FragmentBasic asyncUI/* , boolean isShowUI, boolean isDismissUI */) {
        this.mContext = mContext;
        this.asyncUI = asyncUI;
    }

    public AsyncHttpResponseProcess(Context context, FragmentMapBasic asyncUI, boolean isShowUI) {
        this.mContext = context;
        this.asyncUI = asyncUI;
    }

    @Override
    public void before() {
        // Show waiting dialog during connection
        if (asyncUI != null)
            asyncUI.before();
    }

    @Override
    public void after(int statusCode, HttpResponse response) {
        // Process server response
        if (asyncUI != null)
            asyncUI.after();

        switch (statusCode) {
        case AsyncHttpBase.NETWORK_STATUS_OFF:
            Toast.makeText(mContext, mContext.getString(R.string.network_unvailable), Toast.LENGTH_SHORT).show();
            break;
        case AsyncHttpBase.NETWORK_STATUS_OK:
            processHttpResponse(response);
            break;
        default:
            //Toast.makeText(mContext, mContext.getString(R.string.failed_to_conect_server), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, mContext.getString(R.string.network_unvailable), Toast.LENGTH_SHORT).show();
                return;
            }

            // Parser information
            Log.i(TAG, "Webservice response : " + json);
            processIfResponseSuccess(json);
        } catch (Exception e) {
            e.printStackTrace();
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
}
