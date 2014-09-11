package com.sharebravo.bravo.sdk.util.network;

import java.util.List;

import khandroid.ext.apache.http.entity.mime.HttpMultipartMode;
import khandroid.ext.apache.http.entity.mime.MultipartEntity;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 * @author Visva
 */
public class AsynHttpPostImage extends AsyncHttpBase {
    /**
     * Constructor
     * 
     * @param context
     * @param listener
     * @param parameters
     */
    public AsynHttpPostImage(Context context, AsyncHttpResponseListener listener,
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
    public AsynHttpPostImage(Context context, AsyncHttpResponseProcess process,
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
            String boundary = "*****";
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, BravoWebServiceConfig.NETWORK_TIME_OUT);
            HttpConnectionParams.setSoTimeout(params, BravoWebServiceConfig.NETWORK_TIME_OUT);
            HttpClient httpclient = createHttpClient(url, params);
            
            HttpPost httppost = new HttpPost(url);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            
            AIOLog.d("parameters:"+parameters.size());
            for(int i = 0 ;i<parameters.size();i++){
                AIOLog.d("parameters.get(i): "+parameters.get(i));
            }
            httppost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
            response = httpclient.execute(httppost);
            statusCode = NETWORK_STATUS_OK;
            
        } catch (Exception e) {
            statusCode = NETWORK_STATUS_ERROR;
            e.printStackTrace();
        }
        return null;
    }
}
