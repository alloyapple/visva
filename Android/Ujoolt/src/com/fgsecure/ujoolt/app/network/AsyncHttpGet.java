/*
 * Name: $RCSfile: AsyncHttpGet.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: May 12, 2011 2:38:36 PM $
 *
 * Copyright (C) 2011 COMPANY NAME, Inc. All rights reserved.
 */

package com.fgsecure.ujoolt.app.network;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.fgsecure.ujoolt.app.info.WebServiceConfig;

/**
 * AsyncHttpGet makes http get request based on AsyncTask
 * 
 * @author Lemon
 */
public class AsyncHttpGet extends AsyncHttpBase {
	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param parameters
	 */
	public AsyncHttpGet(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters) {
		super(context, listener, parameters);
	}
	
	public AsyncHttpGet(Context context, AsyncHttpResponseListener listener) {
		super(context, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fruity.meyclub.app.network.AsyncHttpBase#request(java.lang.String)
	 */
	protected String request(String url) {
		try {
			HttpParams params = new BasicHttpParams();
			// Lemon commented 19/04/2012
			// if (parameters != null) {
			// Iterator<NameValuePair> it = parameters.iterator();
			// while (it.hasNext()) {
			// NameValuePair nv = it.next();
			// params.setParameter(nv.getName(), nv.getValue());
			// }
			// }

			// Bind param direct to URL

			String combinedParams = "";
//			if (!parameters.isEmpty()) {
//				combinedParams += "?";
//				for (NameValuePair p : parameters) {
//					String paramString = p.getName() + "="
//							+ URLEncoder.encode(p.getValue(), "UTF-8");
//					if (combinedParams.length() > 1) {
//						combinedParams += "&" + paramString;
//					} else {
//						combinedParams += paramString;
//					}
//				}
//			}
//			ResponseHandler<String> handler = new BasicResponseHandler();
			HttpConnectionParams.setConnectionTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			// Lemon commented 19/04/2012
			// HttpClient httpclient = createHttpClient(url, params);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url + combinedParams);
			response = httpclient.execute(httpget);
			// Lemon added
			// httpclient.getConnectionManager().shutdown();
			statusCode = NETWORK_STATUS_OK;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
