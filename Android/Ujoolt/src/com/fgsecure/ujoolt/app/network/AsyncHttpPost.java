/*
 * Name: $RCSfile: AsyncHttpPost.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Apr 21, 2011 2:43:05 PM $
 *
 * Copyright (C) 2011 COMPANY NAME, Inc. All rights reserved.
 */

package com.fgsecure.ujoolt.app.network;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.fgsecure.ujoolt.app.info.WebServiceConfig;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 * @author Lemon
 */
public class AsyncHttpPost extends AsyncHttpBase {
	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param parameters
	 */
	public AsyncHttpPost(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters) {
		super(context, listener, parameters);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param process
	 * @param parameters
	 */
	public AsyncHttpPost(Context context, AsyncHttpResponseProcess process,
			List<NameValuePair> parameters) {
		super(context, process, parameters);
	}
	public AsyncHttpPost(Context context, AsyncHttpResponseProcess process,
			List<NameValuePair> parameters,boolean isShowDialog) {
		super(context, process, parameters,isShowDialog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fruity.meyclub.app.network.AsyncHttpBase#request(java.lang.String)
	 */
	@Override
	protected String request(String url) {
		
		try {
			HttpParams params = new BasicHttpParams();
//
//			String combinedParams = "";
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

			HttpConnectionParams.setConnectionTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			HttpClient httpclient = createHttpClient(url, params);

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(parameters,"UTF-8"));
			// httppost.setHeader("Content-Type", "multipart/form-data");
			response = httpclient.execute(httppost);
			statusCode = NETWORK_STATUS_OK;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
