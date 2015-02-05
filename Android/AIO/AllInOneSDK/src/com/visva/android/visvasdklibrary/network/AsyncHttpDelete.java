package com.visva.android.visvasdklibrary.network;

import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.visva.android.visvasdklibrary.constant.AIOConstant;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 * @author Kane
 */
public class AsyncHttpDelete extends AsyncHttpBase {
    /**
     * Constructor
     * 
     * @param context
     * @param listener
     * @param parameters
     */
    public AsyncHttpDelete(Context context, AsyncHttpResponseListener listener,List<NameValuePair> parameters) {
        super(context, listener, parameters);
    }

    /**
     * Constructor
     * 
     * @param context
     * @param process
     * @param parameters
     */
    public AsyncHttpDelete(Context context, AsyncHttpResponseProcess process, List<NameValuePair> parameters) {
        super(context, process, parameters);
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
            HttpConnectionParams.setConnectionTimeout(params, AIOConstant.NETWORK_TIME_OUT);
            HttpConnectionParams.setSoTimeout(params, AIOConstant.NETWORK_TIME_OUT);
            HttpClient httpclient = createHttpClient(url, params);
            
            HttpDelete httpdelete = new HttpDelete(url);
            //httpdelete.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            response = httpclient.execute(httpdelete);
            statusCode = AIOConstant.NETWORK_STATUS_OK;
        } catch (Exception e) {
            statusCode = AIOConstant.NETWORK_STATUS_ERROR;
            e.printStackTrace();
        }
        return null;
    }
}
