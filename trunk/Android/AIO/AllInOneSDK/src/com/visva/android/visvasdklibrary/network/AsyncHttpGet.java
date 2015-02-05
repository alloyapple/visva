package com.visva.android.visvasdklibrary.network;

import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.util.Log;

import com.visva.android.visvasdklibrary.constant.AIOConstant;

/**
 * AsyncHttpGet makes http get request based on AsyncTask
 * 
 * @author Kane
 */
public class AsyncHttpGet extends AsyncHttpBase {
	private static final String TAG = "AsyncHttpGet";

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param parameters
	 */
	public AsyncHttpGet(Context context, AsyncHttpResponseListener listener,List<NameValuePair> parameters) {
		super(context, listener, parameters);
	}

	@Override
	protected String request(String url) {
		try {
			HttpParams params = new BasicHttpParams();
			String combinedParams = "";
			if (parameters != null && !parameters.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : parameters) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
				Log.i(TAG, "CombineParams : " + combinedParams);
			}
			HttpConnectionParams.setConnectionTimeout(params, AIOConstant.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, AIOConstant.NETWORK_TIME_OUT);
			HttpClient httpclient = createHttpClient(url, params);
			HttpGet httpGet = new HttpGet(url + combinedParams);
			response = httpclient.execute(httpGet);
			statusCode = AIOConstant.NETWORK_STATUS_OK;
		} catch (Exception e) {
			statusCode = AIOConstant.NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
