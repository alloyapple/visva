package com.sharebravo.bravo.foursquare.network;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.app.Activity;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.sdk.util.DialogUtility;
import com.sharebravo.bravo.sdk.util.VisvaDialog;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 * @author Visva
 */
public class FAsyncHttpResponseProcess implements FAsyncHttpResponseListener {

	private Activity context;
	private VisvaDialog progressDialog;

	public FAsyncHttpResponseProcess(Activity context) {
		this.context = context;
	}

	@Override
	public void before() {
		// Show waiting dialog during connection
		progressDialog = new VisvaDialog(context);
		try {
			progressDialog.show();
		} catch (Exception e) {
			progressDialog = null;
		}
	}

	@Override
	public void after(int statusCode, HttpResponse response) {
		// Process server response
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {
			}
			progressDialog = null;
		}

		switch (statusCode) {
		case FAsyncHttpBase.NETWORK_STATUS_OFF:
			try {
				DialogUtility.alert(context,
						context.getString(R.string.network_unvailable));
			} catch (Exception e) {
				DialogUtility.alert(context.getParent(),
						context.getString(R.string.network_unvailable));
				e.printStackTrace();
			}
			break;
		case FAsyncHttpBase.NETWORK_STATUS_OK:
			processHttpResponse(response);
			break;
		default:
			try {
				DialogUtility.alert(context,
						context.getString(R.string.failed_to_conect_server));
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
//		log.i(TAG, "Process if response is success ===================");
	}

	/**
	 * Interface function
	 */
	public void processIfResponseFail() {
		// Smartlog.log(TAG, "Process if response is fail ===================");
	}
}
