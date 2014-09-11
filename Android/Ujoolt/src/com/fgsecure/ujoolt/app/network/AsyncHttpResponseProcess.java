/*
 * Name: $RCSfile: AsyncHttpResponseProcess.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Dec 8, 2011 9:54:28 AM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.fgsecure.ujoolt.app.network;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.fgsecure.ujoolt.app.screen.MainScreenActivity;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 * @author Lemon
 */
public class AsyncHttpResponseProcess implements AsyncHttpResponseListener {
	public static final String TAG = "AsyncHttpResponseProcess";

	public MainScreenActivity context;

	public AsyncHttpResponseProcess(MainScreenActivity context) {
		this.context = context;
	}

	@Override
	public void before() {
		// Show waiting dialog during connection

		// Log.e(TAG, "---------------------");
		// mainScreenActivity.showProgressDialog();
	}

	@Override
	public void after(int statusCode, HttpResponse response) {
		// Process server response
		context.closeProgressDialog();
		switch (statusCode) {
		case AsyncHttpBase.NETWORK_STATUS_OFF:
			// DialogUtility.alert(context, "Network 's unavaiable");
			break;
		case AsyncHttpBase.NETWORK_STATUS_ERROR:
			// DialogUtility.alert(context.getParent(),
			// R.string.message_server_error);
			break;
		case AsyncHttpBase.NETWORK_STATUS_OK:
			processHttpResponse(response);
			break;
		default:
			// DialogUtility.alert(context.getParent(),
			// R.string.message_server_error);
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
				// DialogUtility.alert(context.getParent(),
				// R.string.message_error_cant_extract_server_response);
				Log.e(TAG, "Response null");
				return;
			}

			// Parser information

			// CommonInfo commonInfo = ParserUtility.parserCommonResponse(json);
			// if (commonInfo.isSuccess()) {
			processIfResponseSuccess(json);
			// Log.e(TAG, "Server response : " + json);
			// } else {
			// processIfResponseFail();
			// context.checkInvalidToken(commonInfo.getMessage());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, " Error : " + e.toString());

		}
	}

	/**
	 * Interface function
	 */
	public void processIfResponseSuccess(String response) {

	}

	/**
	 * Interface function
	 */
	public void processIfResponseFail() {
		// Log.e(TAG, "Process if response is fail ===================");
	}
}
