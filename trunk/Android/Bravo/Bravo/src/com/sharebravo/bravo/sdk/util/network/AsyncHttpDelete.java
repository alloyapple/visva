package com.sharebravo.bravo.sdk.util.network;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.sharebravo.bravo.utils.BravoWebServiceConfig;

import android.content.Context;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 * @author Visva
 */
public class AsyncHttpDelete extends AsyncHttpBase {
    /**
     * Constructor
     * 
     * @param context
     * @param listener
     * @param parameters
     */
    public AsyncHttpDelete(Context context, AsyncHttpResponseListener listener,
            List<NameValuePair> parameters, boolean isShowDilog) {
        super(context, listener, parameters, isShowDilog);
    }

    /**
     * Constructor
     * 
     * @param context
     * @param process
     * @param parameters
     */
    public AsyncHttpDelete(Context context, AsyncHttpResponseProcess process,
            List<NameValuePair> parameters, boolean isShowDilag) {
        super(context, process, parameters, isShowDilag);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fgsecure.meyclub.app.network.AsyncHttpBase#request(java.lang.String)
     */
    @Override
    protected String request(String url) {
        try {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, BravoWebServiceConfig.NETWORK_TIME_OUT);
            HttpConnectionParams.setSoTimeout(params, BravoWebServiceConfig.NETWORK_TIME_OUT);
            HttpClient httpclient = createHttpClient(url, params);
            
            HttpDelete httpdelete = new HttpDelete(url);
            //httpdelete.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            response = httpclient.execute(httpdelete);
            statusCode = NETWORK_STATUS_OK;
        } catch (Exception e) {
            statusCode = NETWORK_STATUS_ERROR;
            e.printStackTrace();
        }
        return null;
    }
}
