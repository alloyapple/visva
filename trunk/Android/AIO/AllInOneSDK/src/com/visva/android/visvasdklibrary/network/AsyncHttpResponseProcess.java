package com.visva.android.visvasdklibrary.network;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.visva.android.visvasdklibrary.constant.AIOConstant;
import com.visva.android.visvasdklibrary.util.StringUtils;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 * @author Kane
 */
public class AsyncHttpResponseProcess implements AsyncHttpResponseListener {
	private static final String TAG = "AsyncHttpResponseProcess";

	public AsyncHttpResponseProcess(Context context) {
	}

	@Override
	public void before() {

	}

	@Override
	public void after(int statusCode, HttpResponse response) {
		// Process server response
		switch (statusCode) {
		case AIOConstant.NETWORK_STATUS_OFF:
			processIfResponseFail(AIOConstant.NETWORK_STATUS_OFF,response);
			break;
		case AIOConstant.NETWORK_STATUS_OK:
			processHttpResponse(response);
			break;
		default:
		    processIfResponseFail(AIOConstant.NETWORK_FAILED_TO_CONECT_SERVER,response);
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
			json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

			if (StringUtils.isEmpty(json)) {
			    processIfResponseFail(AIOConstant.NETWORK_PARSE_JSON_ERROR,response);
				return;
			}

			// Parser information
			processIfResponseSuccess(json);
		} catch (Exception e) {
			e.printStackTrace();
			processIfResponseFail(AIOConstant.NETWORK_PARSE_JSON_ERROR,response);
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
	public void processIfResponseFail(int errorType,HttpResponse errorResponse) {
		// SmartLog.log(TAG, "Process if response is fail ===================");
	}
}
